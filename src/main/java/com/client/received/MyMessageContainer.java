package com.client.received;

import com.alibaba.fastjson.JSON;
import com.client.config.SendMessage;
import com.client.send.service.SaveFinalMessage;
import com.common.model.DocumentInfo;
import com.common.model.MessageServer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

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

    /**
     * 需要确认的文件消息
     */
    private final static String WAITE_RESPONSE_KEY = "files-response-hash";

    @Autowired
    SendMessage sendMessage;

    @Autowired
    SaveFinalMessage saveFinalMessage;

    @Autowired
    @Qualifier("write")
    Jedis jedis;

    public MyMessageContainer(){
    }

    @Override
    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws IOException {
        byte[] body = message.getBody();
        MessageServer rec_message= JSON.parseObject(new String(body),MessageServer.class);
        String fileName = rec_message.getResponseMessage().getFileName();
        DocumentInfo documentInfo = JSON.parseObject(jedis.hget(WAITE_RESPONSE_KEY, fileName), DocumentInfo.class);


        saveFinalMessage.saveMessage(documentInfo, rec_message.getResponseMessage());
        jedis.hdel(WAITE_RESPONSE_KEY, fileName);

        System.out.println("接受到消息" + rec_message.getResponseMessage());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true); //确认消息成功消费
    }


}
