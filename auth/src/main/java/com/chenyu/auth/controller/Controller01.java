package com.chenyu.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller01 {
    @GetMapping("/test01")
    public String test01(){
        System.out.println("微服务被调用");
        return "微服务模块";
    }

}
