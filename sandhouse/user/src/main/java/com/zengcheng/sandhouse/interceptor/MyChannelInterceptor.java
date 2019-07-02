package com.zengcheng.sandhouse.interceptor;

import com.zengcheng.sandhouse.common.enums.RedisKeys;
import com.zengcheng.sandhouse.common.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * 自定义{@link org.springframework.messaging.support.ChannelInterceptor}，实现断开连接的处理
 * 参考链接 https://www.zifangsky.cn/1364.html
 * @author zengcheng
 * @date 2019/07/02
 */
@Component
@Slf4j
public class MyChannelInterceptor implements ChannelInterceptor {

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        //用户已经断开连接
        if(StompCommand.DISCONNECT.equals(command)){
            UserDetails user = (UserDetails) accessor.getUser();
            if(!StringUtils.isEmpty(user) && !StringUtils.isEmpty(user.getUsername())){
                //从Redis中移除用户
                RedisService.Set.deleteSetObject(RedisKeys.WEBSOCKET_LINK_SET, user.getUsername());
            }
            log.debug(MessageFormat.format("用户{0}的WebSocket连接已经断开", user));
        }
    }

}
