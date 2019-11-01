package com.zengcheng.sandhouse.filter;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    /**
     * 存放Token的HeaderKey 或者 QueryParam
     */
    private static final String TOKEN = "Authorization";
    /**
     * 配置无需校验token的请求
     */
    private static final String PATTERN = "/service-auth/**";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //检验是否需要被拦截
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        PathMatcher pathMatcher = new AntPathMatcher();
        if(pathMatcher.match(PATTERN,path)){
            //验证失败，不进行路由
            log.info("来自{}的请求无需token,过滤器不拦截", path);
        }else{
            //获取请求头里的认证信息，如果没有，或者验证不成功返回无权限
            List<String> headerTokens = request.getHeaders().get(TOKEN);
            String message = null;
            if (CollectionUtils.isEmpty(headerTokens)) {
                //header和pathVariable里未找到accessToken
                message = "accessToken is not found!";
            } else {
                //此处不校验权限 默认取header中token对应的第一个 或者pathVariable中的第一个
                Map<String,Object> queryParams = new HashMap<>(2);
                queryParams.put("token",headerTokens.get(0));
                try{
                    ResponseEntity<JsonNode> tokenRespEntity =
                            restTemplate.getForEntity("http://SERVICE-AUTH/oauth/check_token?token={token}", JsonNode.class,queryParams);
                    if(StringUtils.isEmpty(tokenRespEntity.getBody()) || !StringUtils.isEmpty(tokenRespEntity.getBody().get("error"))){
                        message = "invalid token!";
                    }
                }catch (Exception ex){
                    if(ex instanceof HttpClientErrorException){
                        message = "invalid token!";
                    }else{
                        message = "Check Token Error";
                    }
                }
            }
            if (message != null) {
                //验证失败，不进行路由
                log.info("来自{}的请求被过滤器拦截返回: {}", path,message);
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                byte[] bytes = "{\"status\":\"-1\",\"msg\":\"error\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                response.writeWith(Flux.just(buffer));
                return response.setComplete();
            }
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return -100;
    }
}