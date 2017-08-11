package com.server.asyn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/3/28
 * @description 通用bean注入
 */
@Configuration
public class CommonBean {

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor getExecutorPool() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(6);
        threadPoolTaskExecutor.setKeepAliveSeconds(200);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setRejectedExecutionHandler(getPolicy());
        return threadPoolTaskExecutor;

    }

    @Bean
    public ThreadPoolExecutor.CallerRunsPolicy getPolicy() {
        return new ThreadPoolExecutor.CallerRunsPolicy();
    }

}
