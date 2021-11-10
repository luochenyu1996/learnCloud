package com.chenyu.common.core.utils;

import com.chenyu.common.core.constant.SecurityConstants;
import com.chenyu.common.core.constant.TokenConstants;
import com.chenyu.common.core.text.Convert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

/**
 * JWT用的工具类
 *
 * @author chen yu
 * @create 2021-10-21 19:11
 */
public class JwtUtils {
    //令牌的密钥
    public static String secret = TokenConstants.SECRET;


    /**
     * 从数据声明中生成JWT令牌
     *
     */
    public  static  String creatToken(Map<String,Object> claims){
        String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.ES512, secret).compact();
        return jwtToken;
    }

    /**
     * 从JWT令牌中解析除数据声明
     *
     */
    public static Claims parseJwtToken(String jwtToken){
        return  Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
    }

    /**
     * 解析JWT串同时从JWT串中返回用户名
     *
     */
    public static String  getUserName(String jwtToken){
        //进行解析
        Claims claims = parseJwtToken(jwtToken);
        //获取用户信息进行返回
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据声明信息信息获取用户名
     *
     */
    public static String getUserName(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }





    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息

     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }


    /**
     * 根据令牌获取用户标识
     *
     */
    public static String getUserKey(String token) {
        Claims claims = parseJwtToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }




    /**
     * 根据身份信息获取键值
     *
     */
    public static String getValue(Claims claims, String key) {
        return Convert.toStr(claims.get(key), "");
    }





}
