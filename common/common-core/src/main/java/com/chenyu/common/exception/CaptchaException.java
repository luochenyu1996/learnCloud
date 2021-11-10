package com.chenyu.common.exception;

/**
 * 验证码异常
 *
 */

public class CaptchaException extends  RuntimeException{
    private static final long serialVersionUID = 1L;

    public CaptchaException(String msg) {
        super(msg);
    }
}
