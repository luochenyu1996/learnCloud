package com.chenyu.system.mapper;

import com.chenyu.system.api.domain.SysUser;

import java.util.List;

/**
 *  用户表  数据处理层
 *
 */
public interface SysUserMapper {

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);


    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);


    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(String userName);


    /**
     * 根据分页条件查询用户列表
     *
     */
   public List<SysUser> selectUserList(SysUser user);


}
