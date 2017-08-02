package com.client.send.quartz;

import com.alibaba.fastjson.JSON;
import com.client.config.SendMessage;
import com.common.model.DocumentInfo;
import com.common.model.FileMessage;
import com.common.model.MessageClient;
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

    static String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDT9TsOfGqhv+3TxeU/jNwskOlXPF35Gffm4oGjUyC2ajplEiRZ+57x5wYXtqh1B3ulnSQY1PLO/Pw8i0jg1uwQoO0izGk31CrIloFpKAcve5JfGx0XBK1cffsGpFvjzL/gvRZHHvqGOH1BOyST8EcOrDnuT3Y3FtYbGdeDlH6AGQIDAQAB";


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
        List<String> sendMessages= jedis.hvals(WAITE_SEND_KEY);

        MessageClient messageClient = new MessageClient();

        List<FileMessage> fileContents = new ArrayList<FileMessage>();

        messageClient.setQueueName("10000001");

        for(String sendMessage : sendMessages) {

            DocumentInfo documentInfo = JSON.parseObject(sendMessage, DocumentInfo.class);

            fileContents.add(getFileMessage(documentInfo));
        }

        /**
         * 将消息发送送到中心端
         */
        if(fileContents.size() > 0) {

            messageClient.setMessageContents(fileContents);

            String ss = JSON.toJSONString(messageClient);

            sendMessage.sendDirectMsg("exchange", JSON.toJSONString(messageClient), "100010001000");

        }

    }


    private FileMessage getFileMessage(DocumentInfo documentInfo) throws UnsupportedEncodingException {
        FileMessage fileMessage = new FileMessage();

        fileMessage.setBizMsgId(documentInfo.getBizMsgId());
        fileMessage.setSign(documentInfo.getSign());
        fileMessage.setContent(documentInfo.getRequestContent().getBytes());
        fileMessage.setFileName(documentInfo.getFileName());
        fileMessage.setRectype(documentInfo.getRecType());

        if(documentInfo.getReponseContent() != null && documentInfo.getReponseContent().length() > 0) {

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

