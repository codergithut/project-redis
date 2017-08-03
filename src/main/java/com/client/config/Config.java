package com.client.config;

import com.client.received.MyMessageContainer;
import com.common.util.AESUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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

    @Bean("write")
    public Jedis getWriteJedis() {
        return new Jedis("192.168.50.210");
    }

    @Bean("read")
    public Jedis getReadJedis() {
        return new Jedis("192.168.50.210");
    }

    @Bean
    public AESUtil getAESUtil() {
        return new AESUtil();
    }

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    MyMessageContainer myMessageContainer;

    public static final String EXCHANGE_DIRECT   = "exchange";

    public static final String RECEIVE_MESSAGE = "10000001";


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
    public Queue ReceiveMessageQueue() {
        return new Queue(RECEIVE_MESSAGE, true); //队列持久
    }

    /**
     * 可以用代码绑定exchange，有时间处理下。
     * @return
     */
//    @Bean
//    public Binding directBinding() {
//        return BindingBuilder.bind(queue()).to(directExchange()).with(SUCCESS);
//    }


    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        MessageListenerContainer container = new MessageListenerContainer(connectionFactory);
        container.setQueues(ReceiveMessageQueue());
        container.setListener(getMyMessageContainer());
        return container;
    }


}
