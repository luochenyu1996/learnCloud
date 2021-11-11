package com.chenyu.system.service;

import java.util.Set;

public interface ISysPermissionService {


    /**
     *  根据用户ID 获取用户角色的集合
     *
     */
    public Set<String> getRolePermission(Long userId);



    /**
     * 获取角色拥有的菜单的集合
     *
     */
   public Set<String> getMenuPermission(Long userId);


}
