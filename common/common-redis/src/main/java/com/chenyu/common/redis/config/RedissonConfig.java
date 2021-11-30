//package com.chenyu.common.redis.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//
///**
// * Redisson 配置
// *
// * @author chen yu
// * @create 2021-11-15 16:45
// */
//@Configuration
//public class RedissonConfig {
//    @Bean(destroyMethod = "shutdown")
//    public RedissonClient redisson() throws IOException {
//        //创建配置
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://121.43.53.181:6379");
//        //根据配置创建实例
//        return Redisson.create(config);
//    }
//}
