package com.chenyu.common.exception;

/**
 * @author chen yu
 * @create 2021-11-11 20:50
 */
public class InnerAuthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message) {
        super(message);
    }

}
