package com.zengcheng.sandhouse.interceptor;

import com.zengcheng.sandhouse.common.enums.RedisKeys;
import com.zengcheng.sandhouse.common.util.JwtTokenUtil;
import com.zengcheng.sandhouse.common.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


/**
 * 自定义{@link org.springframework.messaging.support.ChannelInterceptor}，实现断开连接的处理
 * 参考链接 https://www.zifangsky.cn/1364.html
 * @author zengcheng
 * @date 2019/07/02
 */
@Component
@Slf4j
public class MyChannelInterceptor implements ChannelInterceptor {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取stomp 头中的token并解析以供后续使用用户信息
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                //这里就是token
                LinkedList token = (LinkedList) ((Map) raw).get("Authorization");
                // 设置当前访问器的认证用户
                UsernamePasswordAuthenticationToken user = getAuthentication(token.get(0).toString());
                if(StringUtils.isEmpty(user)){
                    throw new AccessDeniedException("websocket认证失败");
                }
                accessor.setUser(user);
            }
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            //发送数据

        }

        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        //用户已经断开连接
        if(StompCommand.DISCONNECT.equals(command)){
            UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) accessor.getUser();
            String userName = (String) principal.getPrincipal();
            //从Redis中移除用户
            RedisService.Set.deleteSetObject(RedisKeys.WEBSOCKET_LINK_SET, userName);
            log.info("用户{}的WebSocket连接已经断开", userName);
        }
    }

    /**
     * 这里从token中获取用户信息并新建一个UsernamePasswordAuthenticationToken
     * @param tokenHeader
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String username = jwtTokenUtil.getUserNameFromToken(tokenHeader);
        if (username != null){
            Set<SimpleGrantedAuthority> grantedAuthorities;
            try{
                grantedAuthorities= jwtTokenUtil.getAuthoritiesFromToken(tokenHeader);
            }catch (Exception ex){
                return null;
            }
            //新建一个UsernamePasswordAuthenticationToken
            return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        }
        return null;
    }

}
