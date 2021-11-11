package com.chenyu.common.security.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证注解：必须有指定权限才能进入该方法
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface RequiresPermissions {

    /**
     * 需要验证的权限码
     */
    String[] value()  default {};


    /**
     * 验证逻辑模式
     *
     */
    Logical logical()  default Logical.AND;

}
