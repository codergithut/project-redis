package com.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2017/2/28 9:40.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RedisApplication {
    public static void main(String[] args) {
//        System.getProperties().put("projectName", "springApp");

        SpringApplication.run(RedisApplication.class, args);
    }
}