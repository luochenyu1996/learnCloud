package com.chenyu.auth.controller;

import com.chenyu.auth.form.LoginBody;
import com.chenyu.auth.form.RegisterBody;
import com.chenyu.auth.service.SysLoginService;
import com.chenyu.common.core.domian.R;
import com.chenyu.common.core.utils.JwtUtils;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.security.auth.AuthUtil;
import com.chenyu.common.security.service.TokenService;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * token模块
 *
 * @author chen yu
 * @create 2021-10-21 15:10
 */
@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 用户注册
     *
     */
    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }


    /**
     * 用户登录
     * done
     *
     */
    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBody form) {
        //内部服务调用 只是涉及到用户信息的查询
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 登录、返回jwtToken
        return R.ok(tokenService.createToken(userInfo));
    }


    /**
     * 用户登出
     * done
     *
     */
    @DeleteMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        //获取当前请求从前端携带的jwtToken
        String jwtToken = SecurityUtils.getToken(request);
        if (StringUtils.isNotNull(jwtToken)) {
            //对JWT串进行解析同时获得当前用户信息
            String userName = JwtUtils.getUserName(jwtToken);
            //删除用户缓存记录
            AuthUtil.logoutByToken(jwtToken);
            //todo 记录日志  包含用户信息  username
        }
        return R.ok();
    }


    /**
     * 刷新令牌有效期
     *
     */
    @PostMapping("/refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }


}
