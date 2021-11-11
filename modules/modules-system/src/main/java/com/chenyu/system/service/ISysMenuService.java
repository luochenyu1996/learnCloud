package com.chenyu.system.service;

import java.util.Set;

/**
 * @author chen yu
 * @create 2021-11-10 22:53
 */
public interface ISysMenuService {


    /**
     * 根据用户ID查询拥有的菜单权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);


}
