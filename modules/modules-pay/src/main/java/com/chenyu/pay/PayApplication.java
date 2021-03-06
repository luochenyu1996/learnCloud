package com.chenyu.pay;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chenyu.pay.mapper")
@EnableEncryptableProperties //开启加密注解
public class KsdAlipayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsdAlipayApplication.class, args);
    }

}
