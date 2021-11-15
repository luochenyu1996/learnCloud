package com.chenyu.system.mapper;

import com.chenyu.system.domain.SysConfig;

/**
 * 系统配置  持久层
 *
 * @author chen yu
 * @create 2021-11-11 21:05
 */
public interface SysConfigMapper {

    /**
     * 查询参数配置信息
     *
     * @param config 参数配置信息
     * @return 参数配置信息
     */
    public SysConfig selectConfig(SysConfig config);
}
