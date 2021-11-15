package com.chenyu.common.exception.auth;

import org.apache.commons.lang3.StringUtils;

/**
 * 未能通过角色认证异常
 *
 * @author chen yu
 * @create 2021-11-15 17:29
 */
public class NotRoleException  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotRoleException(String role)
    {
        super(role);
    }

    public NotRoleException(String[] roles)
    {
        super(StringUtils.join(roles, ","));
    }
}
