package com.chenyu.system.api;

import com.chenyu.common.core.constant.SecurityConstants;
import com.chenyu.common.core.constant.ServiceNameConstants;
import com.chenyu.common.core.domian.R;
import com.chenyu.system.api.domain.SysLogininfor;
import com.chenyu.system.api.domain.SysOperLog;
import com.chenyu.system.api.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 远程日志服务
 *
 * @author chen yu
 * @create 2021-11-30 14:43
 */
@FeignClient(contextId = "remoteLogService",value = ServiceNameConstants.SYSTEM_SERVICE,fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {

    /**
     * 保存系统日志
     *
     */
    @PostMapping("/operlog")
    public R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);



    /**
     * 保存访问记录
     *
     */
    @PostMapping("/logininfor")
    public R<Boolean> saveLogininfor(@RequestBody SysLogininfor sysLogininfor, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
