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
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

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
                .setAllowedOrigins("*")
                //默认使用SockJS,如使用原始用法则需要另外实现
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端需要把消息发送到/message/xxx地址
        registry.setApplicationDestinationPrefixes("/message");
        //服务端广播消息的路径前缀，客户端需要相应订阅/topic/yyy这个地址的消息
        registry.enableStompBrokerRelay("/topic","/user");
        //给指定用户发送消息的路径前缀，默认值是/user/
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * 输入通道配置
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(myChannelInterceptor);
        // 线程信息
        registration.taskExecutor()
                // 核心线程池
                .corePoolSize(400)
                // 最多线程池数
                .maxPoolSize(800)
                // 超过核心线程数后，空闲线程超时60秒则杀死
                .keepAliveSeconds(60);
    }

    /**
     * 消息传输参数配置
     *
     * @param registration
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        // 超时时间
        registration.setSendTimeLimit(15 * 1000)
                // 缓存空间
                .setSendBufferSizeLimit(512 * 1024)
                // 消息大小
                .setMessageSizeLimit(128 * 1024);
    }

}