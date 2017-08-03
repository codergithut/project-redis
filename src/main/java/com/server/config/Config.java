package com.server.config;

import com.client.config.MessageListenerContainer;

import com.common.util.AESUtil;
import com.server.received.MyMessageContainer;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/1
 * @description
 */
@Configurable
@Component
public class Config {

    @Bean
    public Jedis getJedis() {
        return new Jedis("192.168.50.210");
    }

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    MyMessageContainer myMessageContainer;

    public static final String EXCHANGE_DIRECT   = "exchange";

    public static final String RECEIVE_MESSAGE = "100010001000";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }

    @Bean
    public MyMessageContainer getMyMessageContainer(){
        return myMessageContainer;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_DIRECT);
    }

    @Bean
    public AESUtil getAESUtil() {
        return new AESUtil();
    }


    @Bean
    public Queue ReceiveMessageQueue() {
        return new Queue(RECEIVE_MESSAGE, true); //队列持久
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        MessageListenerContainer container = new MessageListenerContainer(connectionFactory);
        container.setQueues(ReceiveMessageQueue());
        container.setListener(getMyMessageContainer());
        return container;
    }


}
