package com.chenyu.common.security.aspect;

import com.chenyu.common.security.annotation.RequiresLogin;
import com.chenyu.common.security.annotation.RequiresPermissions;
import com.chenyu.common.security.annotation.RequiresRoles;
import com.chenyu.common.security.auth.AuthUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于 Spring Aop 的注解鉴权
 *
 * @author chen yu
 * @create 2021-11-11 21:23
 */
@Aspect
@Component
public class PreAuthorizeAspect {

    public PreAuthorizeAspect() {
    }

    /**
     * 对注解
     *
     * @RequiresLogin
     * @RequiresPermissions
     * @RequiresRoles 起作用
     */
    public static final String POINTCUT_SIGN =
            " @annotation(com.chenyu.common.security.annotation.RequiresLogin) || "
                    + "@annotation(com.chenyu.common.security.annotation.RequiresPermissions) || "
                    + "@annotation(com.chenyu.common.security.annotation.RequiresRoles)";

    /**
     * 切入点
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {

    }

    /**
     * 环绕通知
     * JoinPoint类，用来获取代理类和被代理类的信息。
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 注解鉴权   joinPoint.getSignature() 获取目标方法 的方法信息和类信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取目标方法的方法信息 并对其中的注解进行鉴别
        checkMethodAnnotation(signature.getMethod());
        try {
            // 执行原有逻辑
            Object obj = joinPoint.proceed();
            return obj;
        } catch (Throwable e) {
            throw e;
        }


    }

    /**
     * 对一个Method对象进行注解检查
     */
    public void checkMethodAnnotation(Method method) {
        // 校验 @RequiresLogin 注解   ---   如果方法上的是 @RequiresLogin
        RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);
        if (requiresLogin != null) {
            // 检查当前用户是否登录
            AuthUtil.checkLogin();
        }

        // 校验 @RequiresRoles 注解
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);//获取注解中的信息
        if (requiresRoles != null) {
            AuthUtil.checkRole(requiresRoles);
        }

        // 校验 @RequiresPermissions 注解
        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions != null) {
            AuthUtil.checkPermi(requiresPermissions);
        }
    }


}
