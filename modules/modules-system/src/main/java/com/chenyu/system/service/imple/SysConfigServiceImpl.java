package com.chenyu.system.service.imple;

import com.chenyu.common.core.constant.Constants;
import com.chenyu.common.core.text.Convert;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.redis.service.RedisService;
import com.chenyu.system.domain.SysConfig;
import com.chenyu.system.mapper.SysConfigMapper;
import com.chenyu.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配置 业务处理
 *
 * @author chen yu
 * @create 2021-11-10 18:19
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private SysConfigMapper configMapper;

    @Override
    public String selectConfigByKey(String configKey) {

        String configValue = Convert.toStr(redisService.getCacheObject(getCacheKey(configKey)));
        if(StringUtils.isNotEmpty(configKey)){
            return configValue;
        }
        //如果缓存中查出来为空 则去数据库中查询  查出来放到缓存中去
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if(StringUtils.isNotNull(retConfig)){
            redisService.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }


    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        
        return Constants.SYS_CONFIG_KEY + configKey;
    }
}
