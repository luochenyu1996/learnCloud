package com.chenyu.system;

import com.chenyu.common.security.annotation.EnableCustomConfig;
import com.chenyu.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chen yu
 * @create 2021-10-29 16:44
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
}
