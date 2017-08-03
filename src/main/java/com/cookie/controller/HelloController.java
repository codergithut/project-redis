package com.cookie.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/3
 * @description
 */
@RestController
public class HelloController {

    @RequestMapping(value = "/hello")
    public String hello(
            @CookieValue(value = "hitCounter", defaultValue = "0") Long hitCounter,
            HttpServletResponse response) {

        // increment hit counter
        hitCounter++;

        // create cookie and set it in response
        Cookie cookie = new Cookie("hitCounter", hitCounter.toString());
        response.addCookie(cookie);

        // render hello.jsp page
         return "hello";
    }

}
