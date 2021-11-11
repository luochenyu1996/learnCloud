package com.chenyu.system.mapper;


import com.chenyu.system.api.domain.SysRole;

import java.util.List;

public interface SysRoleMapper {

    /**
     * 根据用户ID查询角色 权限
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);


}
