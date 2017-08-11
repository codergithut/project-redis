package com.server.asyn.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/8
 * @description
 */

@Service
@Component
public class AsynService{

    @Async("taskExecutor")
    public void testAnsyc() {
        boolean flag = true;
        while(flag) {
            System.out.println("this  is test");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
