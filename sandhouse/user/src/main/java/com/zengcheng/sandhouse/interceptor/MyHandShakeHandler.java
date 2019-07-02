package com.zengcheng.sandhouse.interceptor;


import com.zengcheng.sandhouse.common.enums.RedisKeys;
import com.zengcheng.sandhouse.common.util.RedisService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 自定义{@link org.springframework.web.socket.server.support.DefaultHandshakeHandler}，实现“生成自定义的{@link java.security.Principal}”
 * 参考链接 https://www.zifangsky.cn/1364.html
 * @author zengcheng
 * @date 2019/07/02
 */
@Component
public class MyHandShakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loginUser != null){
            logger.debug(MessageFormat.format("WebSocket连接开始创建Principal，用户：{0}", loginUser.getUsername()));
            //1. 将用户名存到Redis中
            RedisService.Set.cacheSetObject(RedisKeys.WEBSOCKET_LINK_SET,loginUser.getUsername());

            //2. 返回自定义的Principal
            return principal;
        }else{
            logger.error("未登录系统，禁止连接WebSocket");
            return null;
        }
    }

}
