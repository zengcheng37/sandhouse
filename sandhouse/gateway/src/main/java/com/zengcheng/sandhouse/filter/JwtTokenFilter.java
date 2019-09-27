package com.zengcheng.sandhouse.filter;

import com.zengcheng.sandhouse.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 请求过滤器
 * OncePerRequestFilter抽象类代表一次请求只过滤一次
 * @author zengcheng
 * @date 2019/4/11
 */
@Component
@Slf4j
public class JwtTokenFilter implements GlobalFilter, Ordered {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 存放Token的HeaderKey 或者 QueryParam
     */
    private static final String TOKEN = "accessToken";
    /**
     * 配置无需校验token的请求
     */
    private static final Pattern PATTERN = Pattern.compile("/service-user/admin/login");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //检验是否需要被拦截
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        if(PATTERN.matcher(path).matches()){
            //验证失败，不进行路由
            log.info("来自{}的请求无需token,过滤器不拦截", path);
        }else{
            //获取请求头里的认证信息，如果没有，或者验证不成功返回无权限
            List<String> headerTokens = request.getHeaders().get(TOKEN);
            String pathVarToken = request.getQueryParams().getFirst(TOKEN);
            String message;
            if (CollectionUtils.isEmpty(headerTokens) && StringUtils.isEmpty(pathVarToken)) {
                //header和pathVariable里未找到accessToken
                message = "accessToken is not found!";
            } else {
                //此处不校验权限 默认取header中token对应的第一个 或者pathVariable中的第一个
                message = jwtTokenUtil.validateToken(
                        CollectionUtils.isEmpty(headerTokens)? pathVarToken : headerTokens.get(0));
            }
            if (message != null) {
                //验证失败，不进行路由
                log.info("来自{}的请求被过滤器拦截返回: {}", path,message);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return -100;
    }
}