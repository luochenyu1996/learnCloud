package com.chenyu.common.security.aspect;

import com.chenyu.common.core.constant.SecurityConstants;
import com.chenyu.common.core.utils.ServletUtils;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.exception.InnerAuthException;
import com.chenyu.common.security.annotation.InnerAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 注解，内部服务调用验证处理
 * <p>
 * 使用一个环绕通知，方法执行前和执行后都会执行
 *
 * @author chen yu
 * @create 2021-11-11 17:26
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered {


    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        //  请求来源

        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        System.out.println(source);

        //验证是否是内部请求
        if (!StringUtils.equals(SecurityConstants.INNER, source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }
        String userid = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USER_ID);
        String username = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USERNAME);

        if (innerAuth.isUser() && (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username))) {
            throw new InnerAuthException("没有设置用户信息，不允许访问 ");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop 前 执行
     *
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
