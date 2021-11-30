package com.chenyu.common.security.service;

import com.chenyu.common.core.constant.CacheConstants;
import com.chenyu.common.core.constant.SecurityConstants;
import com.chenyu.common.core.utils.*;
import com.chenyu.common.redis.service.RedisService;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token处理相关
 *
 * @author chen yu
 * @create 2021-10-21 17:23
 */
@Component
public class TokenService {
    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        //跟token相关的UUID  区别与验证码相关的UUID  这个token要转换成jwt格式的
        String token = IdUtils.fastUUID();//从新生成一个uuid而不是验证码的那个ID
        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        //刷新令牌，同时会把loginUser存入到redis中
        refreshToken(loginUser);

        //根据用户信息和UUID生成 JWT 串
        HashMap<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);
        //生成JWT 形似的token
        String jwtToken = JwtUtils.creatToken(claimsMap);

        //将信息返回
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("access_token", jwtToken);
        resMap.put("expires_in", expireTime);
        return resMap;
    }


    /**
     * 刷新令牌 同时会把验证通过的用户信息存放到 redis
     *
     * 从当前时间进行延长
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        String userKey = getTokenKey(loginUser.getToken());
        //对loginUser进行缓存
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取用户缓存信息
     *
     * @param request 当前请求
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户缓存信息
     *
     * @param token JWT令牌
     */
    public LoginUser getLoginUser(String token) {
        LoginUser loginUser = null;
        try {
            if (StringUtils.isNotNull(token)) {
                String userkey = JwtUtils.getUserKey(token);
                loginUser = redisService.getCacheObject(getTokenKey(userkey));
                return loginUser;
            }
        } catch (Exception e) {
        }
        return loginUser;
    }


    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }


    /**
     * 生成在redis中存储的key
     */
    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }


}
