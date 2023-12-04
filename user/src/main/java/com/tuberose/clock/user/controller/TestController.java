package com.tuberose.clock.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/test")
public class TestController {

    @Value("${test.nacos}")
    private String nacosTestMsg;

    @GetMapping("/hello")
    public String hello() {
        return "hello, user service.";
    }

    @GetMapping("/nacos")
    public String nacos() {
        return nacosTestMsg;
    }
}
