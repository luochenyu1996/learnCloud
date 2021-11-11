package com.chenyu.system.service.imple;

import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.system.api.domain.SysRole;
import com.chenyu.system.domain.SysUserRole;
import com.chenyu.system.mapper.SysRoleMapper;
import com.chenyu.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chen yu
 * @create 2021-11-10 18:20
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;



    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return null;
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return null;
    }


    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        //查出对应角色名称
        List<SysRole> roles = roleMapper.selectRolePermissionByUserId(userId);
        HashSet<String> permsSet = new HashSet<>();
        for (SysRole role : roles) {
            if(StringUtils.isNotNull(role)){
                permsSet.addAll(Arrays.asList(role.getRoleKey().trim().split(",")) );
            }
        }
        return permsSet;
    }

    @Override
    public List<SysRole> selectRoleAll() {
        return null;
    }

    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return null;
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        return null;
    }

    @Override
    public String checkRoleNameUnique(SysRole role) {
        return null;
    }

    @Override
    public String checkRoleKeyUnique(SysRole role) {
        return null;
    }

    @Override
    public void checkRoleAllowed(SysRole role) {

    }

    @Override
    public void checkRoleDataScope(Long roleId) {

    }

    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return 0;
    }

    @Override
    public int insertRole(SysRole role) {
        return 0;
    }

    @Override
    public int updateRole(SysRole role) {
        return 0;
    }

    @Override
    public int updateRoleStatus(SysRole role) {
        return 0;
    }

    @Override
    public int authDataScope(SysRole role) {
        return 0;
    }

    @Override
    public int deleteRoleById(Long roleId) {
        return 0;
    }

    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        return 0;
    }

    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return 0;
    }

    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return 0;
    }

    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        return 0;
    }
}
