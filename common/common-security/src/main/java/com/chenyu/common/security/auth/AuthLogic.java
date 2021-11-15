package com.chenyu.common.security.auth;

import com.chenyu.common.core.utils.JwtUtils;
import com.chenyu.common.core.utils.SpringUtils;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.exception.auth.NotLoginException;
import com.chenyu.common.exception.auth.NotPermissionException;
import com.chenyu.common.exception.auth.NotRoleException;
import com.chenyu.common.redis.service.RedisService;
import com.chenyu.common.security.annotation.Logical;
import com.chenyu.common.security.annotation.RequiresPermissions;
import com.chenyu.common.security.annotation.RequiresRoles;
import com.chenyu.common.security.service.TokenService;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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


    /**
     * 检验用户是否已经登录，如未登录，则抛出异常
     */
    public void checkLogin() {
        getLoginUser();
    }



    /**
     * 获取当前用户缓存信息, 如果未登录，则抛出异常
     *
     * @return 用户缓存信息
     */
    public LoginUser getLoginUser() {
        String token = SecurityUtils.getToken();
        if (token == null) {
            throw new NotLoginException("未提供token");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new NotLoginException("无效的token");
        }
        return loginUser;
    }



    /**
     * 根据注解(@RequiresRoles)鉴权
     *
     * @param requiresRoles 注解对象
     */
    public void checkRole(RequiresRoles requiresRoles) {
        if (requiresRoles.logical() == Logical.AND) {
            checkRoleAnd(requiresRoles.value());
        } else {
            checkRoleOr(requiresRoles.value());
        }
    }



    /**
     * 验证用户是否含有指定角色，必须全部拥有
     *
     * @param roles 角色标识数组
     */
    public void checkRoleAnd(String... roles) {
        Set<String> roleList = getRoleList();
        for (String role : roles) {
            if (!hasRole(roleList, role)) {
                throw new NotRoleException(role);
            }
        }
    }

    /**
     * 验证用户是否含有指定角色，只需包含其中一个
     *
     * @param roles 角色标识数组
     */
    public void checkRoleOr(String... roles) {
        Set<String> roleList = getRoleList();
        for (String role : roles) {
            if (hasRole(roleList, role)) {
                return;
            }
        }
        if (roles.length > 0) {
            throw new NotRoleException(roles);
        }
    }

    /**
     * 获取当前账号的角色列表
     *
     * @return 角色列表
     */
    public Set<String> getRoleList() {
        try {
            LoginUser loginUser = getLoginUser();
            return loginUser.getRoles();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色
     * @return 用户是否具备某角色权限
     */
    public boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> SUPER_ADMIN.contains(x) || PatternMatchUtils.simpleMatch(x, role));
    }




    /**
     * 根据注解(@RequiresPermissions)鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param requiresPermissions 注解对象
     */
    public void checkPermi(RequiresPermissions requiresPermissions) {
        if (requiresPermissions.logical() == Logical.AND) {
            checkPermiAnd(requiresPermissions.value());
        } else {
            checkPermiOr(requiresPermissions.value());
        }
    }


    /**
     * 验证用户是否含有指定权限，必须全部拥有
     *
     * @param permissions 权限列表
     */
    public void checkPermiAnd(String... permissions) {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions) {
            if (!hasPermi(permissionList, permission)) {
                throw new NotPermissionException(permission);
            }
        }
    }


    /**
     * 验证用户是否含有指定权限，只需包含其中一个
     *
     * @param permissions 权限码数组
     */
    public void checkPermiOr(String... permissions) {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions) {
            if (hasPermi(permissionList, permission)) {
                return;
            }
        }
        if (permissions.length > 0) {
            throw new NotPermissionException(permissions);
        }
    }


    /**
     * 获取当前账号的权限列表
     *
     * @return 权限列表
     */
    public Set<String> getPermiList() {
        try {
            LoginUser loginUser = getLoginUser();
            return loginUser.getPermissions();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }



}
