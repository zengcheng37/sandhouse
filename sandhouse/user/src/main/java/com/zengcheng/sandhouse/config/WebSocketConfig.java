package com.zengcheng.sandhouse.config;

import com.zengcheng.sandhouse.interceptor.AuthHandShakeInterceptor;
import com.zengcheng.sandhouse.interceptor.MyChannelInterceptor;
import com.zengcheng.sandhouse.interceptor.MyHandShakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.annotation.Resource;

/**
 * WebSocket相关配置
 * 参考链接 https://www.zifangsky.cn/1364.html
 * @author zengcheng
 * @date 2019/07/01
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Resource
    private AuthHandShakeInterceptor authHandshakeInterceptor;

    @Resource
    private MyHandShakeHandler myHandShakeHandler;

    @Resource
    private MyChannelInterceptor myChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
                .addInterceptors(authHandshakeInterceptor)
                .setHandshakeHandler(myHandShakeHandler)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端需要把消息发送到/message/xxx地址
        registry.setApplicationDestinationPrefixes("/message");
        //服务端广播消息的路径前缀，客户端需要相应订阅/topic/yyy这个地址的消息
        registry.enableSimpleBroker("/topic");
        //给指定用户发送消息的路径前缀，默认值是/user/
        registry.setUserDestinationPrefix("/user/");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(myChannelInterceptor);
    }

}