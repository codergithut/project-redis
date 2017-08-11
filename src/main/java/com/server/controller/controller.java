package com.server.controller;

import com.server.asyn.service.AsynService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/8
 * @description
 */
@RestController
public class controller {

    @Autowired
    AsynService asynService;

    @RequestMapping("/async")
    public Object getTest() {
        System.out.println("hahahahaha");
        asynService.testAnsyc();
        return "test";
    }
}
