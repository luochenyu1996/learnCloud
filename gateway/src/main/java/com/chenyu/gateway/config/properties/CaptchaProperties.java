package com.chenyu.gateway.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码参数配置
 *
 * @author: chen yu
 * @create: 2021-10-20 20:55
 */
@Configuration
@ConfigurationProperties(prefix = "security.captcha")
public class CaptchaProperties {
    //验证码开关
    private Boolean enabled;

    //验证码类型
    private String  type;


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
