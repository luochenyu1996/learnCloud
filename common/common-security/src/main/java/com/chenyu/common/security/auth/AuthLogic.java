package com.chenyu.common.security.auth;

import com.chenyu.common.core.utils.JwtUtils;
import com.chenyu.common.core.utils.SpringUtils;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.redis.service.RedisService;
import com.chenyu.common.security.service.TokenService;
import com.chenyu.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Token权限验证，逻辑实现
 *
 * @author chen yu
 * @create 2021-10-21 19:38
 */
@Component
public class AuthLogic {

    @Autowired
    private RedisService redisService;


    //所有权限标识
    private static final String ALL_PERMISSION = "*:*:*";


    //管理员角色权限标识
    private static final String SUPER_ADMIN = "admin";


    //从容器中拿到该对象
    public TokenService tokenService = SpringUtils.getBean(TokenService.class);


    /**
     * 会话注销
     *
     */
    public void logout() {
        String jwtToken = SecurityUtils.getToken();
        if (jwtToken == null) {
            return;
        }
        logoutByToken(jwtToken);
    }

    /**
     * 会话注销，根据指定Token
     */
    public void logoutByToken(String token) {
        tokenService.delLoginUser(token);
    }








}
