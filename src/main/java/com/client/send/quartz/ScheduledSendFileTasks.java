package com.client.send.quartz;

import com.alibaba.fastjson.JSON;
import com.client.config.SendMessage;
import com.common.model.DocumentInfo;
import com.common.model.FileMessage;
import com.common.model.MessageClient;
import com.common.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/2/13
 * @description 定时器启动到指定文件夹下获取数据并上传到消费接受中心
 */
@Component
@Configurable
public class ScheduledSendFileTasks {


    @Autowired
    SendMessage sendMessage;

    @Autowired
    @Qualifier("read")
    Jedis jedis;

    @Autowired
    AESUtil aesUtil;

    /**
     * 需要发送的文件消息
     */
    private final static String WAITE_SEND_KEY = "files-wait-send-hash";

    /**
     * 需要确认的文件消息
     */
    private final static String WAITE_RESPONSE_KEY = "files-response-hash";



    public void sendFileSchedule() throws Exception {

        /**
         * 根据报文信息进行封装
         */
        jedis.watch(WAITE_SEND_KEY);

        List<String> sendMessages = jedis.hvals(WAITE_SEND_KEY);

        if(sendMessages == null) {
            return ;
        }

        MessageClient messageClient = new MessageClient();

        List<FileMessage> fileContents = new ArrayList<FileMessage>();

        messageClient.setQueueName("10000001");

        for(String sendMessage : sendMessages) {

            System.out.println("sendMessage : " + sendMessage);

            DocumentInfo documentInfo = JSON.parseObject(sendMessage, DocumentInfo.class);

            fileContents.add(getFileMessage(documentInfo));
        }

        /**
         * 将消息发送送到中心端
         */
        if(fileContents.size() > 0) {

            messageClient.setMessageContents(fileContents);

            String ss = JSON.toJSONString(messageClient);


            sendMessage.sendDirectMsg("exchange", aesUtil.encrypt(JSON.toJSONString(messageClient)), "100010001000");

        }

    }


    private FileMessage getFileMessage(DocumentInfo documentInfo) throws UnsupportedEncodingException {
        FileMessage fileMessage = new FileMessage();

        fileMessage.setBizMsgId(documentInfo.getBizMsgId());
        fileMessage.setSign(documentInfo.getSign());
        fileMessage.setContent(documentInfo.getRequestContent().getBytes());
        fileMessage.setFileName(documentInfo.getFileName());
        fileMessage.setRectype(documentInfo.getRecType());

        System.out.println("response : " + documentInfo.getReponseContent());


        if(documentInfo.getReponseContent() != null) {

            /**
             * 对消息进行设置告诉服务端该消息有问题无需进行多余操作只要备份下就好
             */
            fileMessage.setResult(documentInfo.getReponseContent());

        } else {

            /**
             * 对成功验证的报文进行返回结果验证
             */
            Map<String,String> doc = new HashMap<String,String>();
            doc.put(documentInfo.getFileName(), documentInfo.toString());
            jedis.hmset(WAITE_RESPONSE_KEY,doc);

        }

        /**
         * 在等待发送报文的消息中删除已经处理过的消息
         */
        jedis.hdel(WAITE_SEND_KEY, documentInfo.getFileName());

        return fileMessage;
    }





}

