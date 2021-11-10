package com.chenyu.common.security.auth;

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
}
