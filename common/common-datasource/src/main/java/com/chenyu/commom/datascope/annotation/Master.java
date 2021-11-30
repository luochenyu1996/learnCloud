package com.chenyu.commom.datascope.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

/**
 * 主库数据源
 *
 * 这两个注解是使用的方式
 * @Master
 * public void insertA()
 * {
 * 	return xxxxMapper.insertXxxx();
 * }
 *
 * @Slave
 * public void insertB()
 * {
 * 	return xxxxMapper.insertXxxx();
 *
 *
 * @author chenyu
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("master")
public @interface Master {

}