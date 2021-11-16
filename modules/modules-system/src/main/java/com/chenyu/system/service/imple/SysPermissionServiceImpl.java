package com.chenyu.system.service.imple;

import com.chenyu.system.api.domain.SysUser;
import com.chenyu.system.service.ISysMenuService;
import com.chenyu.system.service.ISysPermissionService;
import com.chenyu.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chen yu
 * @create 2021-11-10 18:21
 */
@Service
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;


    @Override
    public Set<String> getRolePermission(Long userId) {
        HashSet<String> roleSet = new HashSet<>();
        if(SysUser.isAdmin(userId)){
            //如果当前用户是管理员
            roleSet.add("admin");
        }else {
            //如果不是管理员
            roleSet.addAll(roleService.selectRolePermissionByUserId(userId));
        }
        return roleSet;
    }



    @Override
    public Set<String> getMenuPermission(Long userId) {
        HashSet<String> menuSet = new HashSet<>();

        if(SysUser.isAdmin(userId)){
            menuSet.add("*:*:*");
        }else{

            menuSet.addAll(menuService.selectMenuPermsByUserId(userId));
        }

        return null;
    }


}
