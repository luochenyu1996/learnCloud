package com.chenyu.system.mapper;

import java.util.List;

/**
 * 菜单  数据层
 *
 *
 * @author chen yu
 * @create 2021-11-11 17:09
 */
public interface SysMenuMapper {



    /**
     * 根据用户 ID 查询该ID 拥有的菜单权限
     *
     */
    List<String> selectMenuPermsByUserId(Long userId);
}
