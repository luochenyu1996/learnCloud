package com.chenyu.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller01 {
    @GetMapping("/test")
    public String test(){
        return "text接口响应";
    }
}
