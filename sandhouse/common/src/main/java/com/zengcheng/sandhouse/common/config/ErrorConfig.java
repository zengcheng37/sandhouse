package com.zengcheng.sandhouse.common.config;

import org.springframework.boot.web.server.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 错误页面设置
 * @author zengcheng
 * @date 2019/07/05
 */
@Component
public class ErrorConfig {

    @Bean
    public ErrorPageRegistrar errorPageRegistrar(){
        return new MyErrorPageRegistrar();
    }

    private static class MyErrorPageRegistrar implements ErrorPageRegistrar {

        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
            registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST,"/error/400"));
            registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN,"/error/403"));
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/error/404"));
            registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST,"/error/500"));

        }

    }

}
