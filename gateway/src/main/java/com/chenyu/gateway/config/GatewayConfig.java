package com.chenyu.gateway.config;

import com.chenyu.gateway.handler.SentinelFallbackHandler;
import org.springframework.context.annotation.Configuration;

/**
 * 网关限流配置
 *
 * @author chen yu
 * @create 2021-10-27 17:37
 */
@Configuration
public class GatewayConfig {
    public SentinelFallbackHandler sentinelGatewayExceptionHandler(){
        return  new SentinelFallbackHandler();
    }
}
