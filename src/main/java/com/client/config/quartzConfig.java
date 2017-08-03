package com.client.config;

import com.client.file.quartz.SchedledGetFileConf;
import com.client.send.quartz.SchedledSendFileConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/2
 * @description
 */
@Configuration
@Import({SchedledGetFileConf.class, SchedledSendFileConf.class})
public class quartzConfig {

    @Autowired
    SchedledGetFileConf schedledGetFileConf;

    @Autowired
    SchedledSendFileConf schedledSendFileConf;

    @Bean
    public SchedulerFactoryBean schedulerFactory() throws Exception {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setTriggers (
                schedledGetFileConf.getFileCronTriggerBean().getObject()
                ,schedledSendFileConf.sendFileCronTriggerBean().getObject()
        );
        return bean;
    }
}
