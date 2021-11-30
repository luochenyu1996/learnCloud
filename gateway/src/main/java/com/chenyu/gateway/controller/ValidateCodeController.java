package com.chenyu.gateway.controller;


import com.chenyu.common.exception.CaptchaException;
import com.chenyu.common.web.domain.AjaxResult;
import com.chenyu.gateway.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 验证码服务
 *
 * @author chen yu
 * @create 2021-11-16 22:43
 */
@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeService validateCodeService;

    /**
     * 获取验证码
     *
     */
    @GetMapping("/code")
    public AjaxResult getCode()  {
        AjaxResult ajaxResult=null;
        try {
            AjaxResult capcha = validateCodeService.createCapcha();
            ajaxResult=capcha;
        } catch (CaptchaException | IOException e) {
            e.printStackTrace();
        }
        return ajaxResult;

    }

}
