package com.server.received;

import com.alibaba.fastjson.JSON;
import com.common.model.FileMessage;
import com.common.model.MessageClient;
import com.common.model.MessageServer;
import com.common.model.ResponseMessage;
import com.rabbitmq.client.Channel;
import com.server.config.SendMessage;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/2/13
 * @description
 */
@Service
@Component
@DependsOn("sendMessage")
public class MyMessageContainer implements ChannelAwareMessageListener {

    @Value("${back.file.path}")
    private String backFilePath;

    @Autowired
    SendMessage sendMessage;

    public MyMessageContainer(){
    }

    @Override
    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws IOException {
        byte[] body = message.getBody();
        MessageClient rec_message= JSON.parseObject(new String(body),MessageClient.class);
        //todo:服务器信息预先处理
        List<FileMessage> fileMessages=rec_message.getMessageContents();

        int i = 0;

        for(FileMessage fileMessage : fileMessages) {

            System.out.println("接受到消息:" + new String(fileMessage.getContent(), "UTF-8"));
            MessageServer messageServer = new MessageServer();

            if(i%2==0) {
                messageServer.setResult(false);
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setFileName(fileMessage.getFileName());
                responseMessage.setResponseInfo("我就是想让你失败而已");
                messageServer.setResponseMessage(responseMessage);

            } else {

                messageServer.setResult(true);
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setFileName(fileMessage.getFileName());
                responseMessage.setResponseInfo("我就是想让你成功而已");
                messageServer.setResponseMessage(responseMessage);

            }
            i++;
            sendMessage.sendDirectMsg("exchange", JSON.toJSONString(messageServer), rec_message.getQueueName());
        }



        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true); //确认消息成功消费
    }


}
