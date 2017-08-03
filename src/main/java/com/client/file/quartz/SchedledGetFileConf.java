package com.client.file.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.expression.ParseException;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/2/13
 * @description 定时器配置 用于定时扫描对应的文件并上传
 */
@Configuration
@Import({ScheduledGetFileTasks.class})
public class SchedledGetFileConf {

    @Autowired
    ScheduledGetFileTasks scheduledTasks;

    private String cronTime = "0/20 * * * * ?";
    // 配置中设定了
    // ① targetMethod: 指定需要定时执行scheduleInfoAction中的simpleJobTest()方法
    // ② concurrent：对于相同的JobDetail，当指定多个Trigger时, 很可能第一个job完成之前，
    // 第二个job就开始了。指定concurrent设为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始。
    // ③ cronExpression：0/10 * * * * ?表示每10秒执行一次，具体可参考附表。
    // ④ triggers：通过再添加其他的ref元素可在list中放置多个触发器。 scheduleInfoAction中的simpleJobTest()方法

    @Bean
    public MethodInvokingJobDetailFactoryBean getFileFactoryBean(){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject (scheduledTasks);
        bean.setTargetMethod ("getFileSchedule");
        bean.setConcurrent (true);
        return bean;
    }


    @Bean
    public CronTriggerFactoryBean getFileCronTriggerBean() throws Exception {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail (getFileFactoryBean().getObject ());
        try {
            tigger.setCronExpression (cronTime);//每5秒执行一次
        } catch (ParseException e) {
            e.printStackTrace ();
            throw new Exception("定时任务失效!");
        }
        return tigger;

    }


}
