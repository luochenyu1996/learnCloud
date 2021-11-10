package com.chenyu.gateway.handler;


import com.chenyu.common.web.domain.AjaxResult;
import com.chenyu.gateway.filter.AuthFilter;
import com.chenyu.gateway.service.ValidateCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;


/**
 * 处理验证码请求
 *
 */
@Component
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {
    private static final Logger log = LoggerFactory.getLogger(ValidateCodeHandler.class);
    @Autowired
    private ValidateCodeService validateCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        log.info("进行验证码处理");
        AjaxResult ajaxResult;
        try {
            ajaxResult = validateCodeService.createCapcha();
        } catch (IOException e) {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(ajaxResult));
    }
}
