package com.client.received;

import com.alibaba.fastjson.JSON;
import com.client.config.SendMessage;
import com.common.model.MessageServer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/2/13
 * @description
 */
@Service
@Component
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
        MessageServer rec_message= JSON.parseObject(new String(body),MessageServer.class);
        System.out.println("接受到消息" + rec_message.getResponseMessage());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true); //确认消息成功消费
    }


}
