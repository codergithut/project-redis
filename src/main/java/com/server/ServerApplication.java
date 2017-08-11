package com.server;

import com.google.common.io.FileBackedOutputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by Administrator on 2017/2/28 9:40.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableAsync
public class ServerApplication implements EmbeddedServletContainerCustomizer{
    public static void main(String[] args) {

        SpringApplication.run(ServerApplication.class, args);

    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(8080);
    }
}