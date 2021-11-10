package com.chenyu.gateway.service;


import com.chenyu.common.exception.CaptchaException;
import com.chenyu.common.web.domain.AjaxResult;

import java.io.IOException;

/**
 *  验证码
 *
 */
public interface ValidateCodeService {

    /**
     * 生成验证码
     */
    public AjaxResult createCapcha() throws IOException, CaptchaException;


    /**
     * 校验验证码
     */
    public void checkCapcha(String key, String value) throws CaptchaException;


}
