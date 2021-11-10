package com.chenyu.auth.service;

import com.chenyu.common.core.constant.Constants;
import com.chenyu.common.core.constant.SecurityConstants;
import com.chenyu.common.core.constant.UserConstants;
import com.chenyu.common.core.domian.R;
import com.chenyu.common.core.enums.UserStatus;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.exception.ServiceException;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.system.api.RemoteUserService;
import com.chenyu.system.api.domain.SysUser;
import com.chenyu.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册服务 登录服务  登出服务
 *
 * @author chen yu
 * @create 2021-10-21 15:25
 */
@Component
public class SysLoginService {

    @Autowired
    private RemoteUserService remoteUserService;


    /**
     * 用户注册
     *
     * @param username 用户姓名
     * @param password 用户密码
     */
    public void register(String username, String password) {

        //先检查一下用户名和密码

        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        //注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);
        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
        //todo 记录日志
    }

    /**
     * 用户登录
     */
    public LoginUser login(String username, String password) {

        // 先对用户名密码进行一些判断

        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            //todo 记录日志
            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            //todo 记录日志
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            //todo 记录日志
            throw new ServiceException("用户名不在指定范围");
        }

        //查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        //如果内部请求失败
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            //todo 记录用户信息
            throw new ServiceException("登录用户：" + username + " 不存在");
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();

        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            //todo 记录日志
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            //todo 记录日志
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            // 进行密码匹配
            //todo  记录日志
            throw new ServiceException("用户不存在/密码错误");
        }
        //todo 记录日志
        return userInfo;

    }


}
