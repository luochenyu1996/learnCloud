package com.chenyu.common.datascope.annotation;

import java.lang.annotation.*;

/**
 * 数据范围权限注解
 *
 * @author chen yu
 * @create 2021-11-15 20:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表别名
     *
     */

    public  String deptAlias() default "";


    /**
     * 用户表别名
     *
     */

    public  String userAlias() default "";
}
