package com.zengcheng.sandhouse.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign调用拦截器 传递请求header中的内容(目前只传accessToken)
 * 参考地址 https://blog.csdn.net/u014519194/article/details/77160958
 * @author zengcheng
 * @date 2019/07/04
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String values = request.getHeader("Authorization");
        requestTemplate.header("Authorization", values);
    }
}
