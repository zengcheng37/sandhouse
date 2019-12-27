package com.zengcheng.sandhouse.interceptor;

import com.zengcheng.sandhouse.common.enums.RedisKeys;
import com.zengcheng.sandhouse.common.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.text.MessageFormat;
import java.util.Map;

/**
 * 自定义{@link org.springframework.web.socket.server.HandshakeInterceptor}，实现“需要登录才允许连接WebSocket”
 * 参考链接 https://www.zifangsky.cn/1364.html
 * @author zengcheng
 * @date 2019/07/02
 */
@Component
@Slf4j
public class AuthHandShakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest,
                                   ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler,
                                   Map<String, Object> map) {
        String userName = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(RedisService.Set.containsSetObject(RedisKeys.WEBSOCKET_LINK_SET, userName)){
            log.error("同一个用户不准建立多个连接WebSocket");
            return false;
        }else if(StringUtils.isEmpty(userName) || "AnonymousUser".equalsIgnoreCase(userName)){
            log.error("未登录系统，禁止连接WebSocket");
            return false;
        }else{
            log.info("用户{}请求建立WebSocket连接", userName);
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
                               ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
        String userName = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        log.info("用户{}建立WebSocket连接握手结束",userName);
    }

}
