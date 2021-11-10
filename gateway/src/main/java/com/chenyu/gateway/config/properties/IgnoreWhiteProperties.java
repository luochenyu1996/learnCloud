package com.chenyu.gateway.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单
 *
 * @author chen yu
 * @create 2021-10-20 22:38
 */
@Configuration
@RefreshScope  //自动刷新配置文件
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreWhiteProperties {

    //网关放行的白名单
    private List<String> whites = new ArrayList<String>();

    public List<String> getWhites() {
        return whites;
    }

    public void setWhites(List<String> whites) {
        this.whites = whites;
    }
}
