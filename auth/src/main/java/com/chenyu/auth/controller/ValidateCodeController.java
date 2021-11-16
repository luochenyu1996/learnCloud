package com.chenyu.auth.controller;


import com.chenyu.common.core.domian.R;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码服务
 *
 * @author chen yu
 * @create 2021-11-16 22:43
 */
@RestController
public class ValidateCodeController {

    /**
     * 用户注册
     *
     */
    @GetMapping("/code")
    public R<?> register() {


        return null;

    }


}
