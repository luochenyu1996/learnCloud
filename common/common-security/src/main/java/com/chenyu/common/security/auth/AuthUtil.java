package com.chenyu.common.security.auth;

import com.chenyu.common.security.annotation.RequiresPermissions;
import com.chenyu.common.security.annotation.RequiresRoles;

/**
 * Token权限验证工具
 *
 * @author chen yu
 * @create 2021-10-21 19:37
 */
public class AuthUtil {
    //负责删除的具体逻辑
    public static AuthLogic authLogic = new AuthLogic();

    /**
     * 会话注销，根据指定Token
     *
     */
    public static void logoutByToken(String token) {
        authLogic.logoutByToken(token);
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     */
    public static void checkLogin() {
        authLogic.checkLogin();
    }


    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotRoleException
     *
     * @param requiresRoles 角色权限注解
     */
    public static void checkRole(RequiresRoles requiresRoles) {
        authLogic.checkRole(requiresRoles);
    }


    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param requiresPermissions 权限注解
     */
    public static void checkPermi(RequiresPermissions requiresPermissions) {
        authLogic.checkPermi(requiresPermissions);
    }
}
