package com.chenyu.system.service;

/**
 * @author chen yu
 * @create 2021-11-10 18:16
 */
public interface ISysConfigService {

    /**
     * 根据键名查询参数配置信息
     *
     * 先去缓存中查询
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);
}
