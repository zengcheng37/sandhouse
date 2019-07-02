package com.zengcheng.sandhouse.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 默认SockJS请求会自动添加类似info?t=150xxxxx 的请求，来获取服务端的信息是否是websocket，
 * 然后才会发送websocket真正的请求。如果不处理info请求，会报websocket请求头相关错误。
 * 解决办法在网关添加个全局过滤器，把http://ws://bilibili-grabservice/mq/info
 * 类似的请求中的ws(如果是https://wss),修改为http(wss就修改为https)
 *
 * 参考链接：https://juejin.im/post/5c53e2876fb9a049f23d3159
 * @author zengcheng
 * @date 2019/07/01
 */
@Component
@Slf4j
public class WebSocketFilter implements GlobalFilter, Ordered {

    private final static String DEFAULT_FILTER_PATH = "/mq/info";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String upgrade = exchange.getRequest().getHeaders().getUpgrade();
        log.debug("Upgrade : {}", upgrade);
        URI requestUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        log.debug("path: {}", requestUrl.getPath());
        String scheme = requestUrl.getScheme();
        if (!"ws".equals(scheme) && !"wss".equals(scheme)) {
            return chain.filter(exchange);
        } else if (DEFAULT_FILTER_PATH.equals(requestUrl.getPath())) {
            String wsScheme = convertWsToHttp(scheme);
            URI wsRequestUrl = UriComponentsBuilder.fromUri(requestUrl).scheme(wsScheme).build().toUri();
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, wsRequestUrl);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    private String convertWsToHttp(String scheme) {
        scheme = scheme.toLowerCase();
        return "ws".equals(scheme) ? "http" : "wss".equals(scheme) ? "https" : scheme;
    }
}
