package com.chenyu.common.log.annotation;

import com.chenyu.common.log.enums.BusinessType;
import com.chenyu.common.log.enums.OperatorType;

/**
 * 日志注解
 *
 * @author chen yu
 * @create 2021-11-30 14:30
 */
public @interface Log {

    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}
