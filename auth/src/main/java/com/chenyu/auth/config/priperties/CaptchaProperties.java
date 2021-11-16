package com.chenyu.auth.config.priperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码相关的自定义扩展
 *
 * @author chen yu
 * @create 2021-11-16 22:59
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.captcha")
public class CaptchaProperties {
}
