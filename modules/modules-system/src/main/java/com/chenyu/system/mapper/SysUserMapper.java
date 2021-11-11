package com.chenyu.system.mapper;

import com.chenyu.system.api.domain.SysUser;

import java.util.List;

/**
 *  用户表  数据处理层
 *
 */
public interface SysUserMapper {


    /**
     * 根据分页条件查询用户列表
     *
     */
   public List<SysUser> selectUserList(SysUser user);


}
