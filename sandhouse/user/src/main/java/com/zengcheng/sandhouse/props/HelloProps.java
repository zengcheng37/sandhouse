package com.zengcheng.sandhouse.props;

import com.zengcheng.sandhouse.common.config.ApolloPropsRefreshConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * apollo配置实时刷新 测试类
 * @author zengcheng
 * @date 2020/08/07
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "hello", ignoreUnknownFields = true, ignoreInvalidFields= true)
@PropertySource("classpath:hello.properties")
@Data
public class HelloProps extends ApolloPropsRefreshConfig implements Serializable {

    private static final long serialVersionUID = 3964412769936836904L;

    private String name;
    
}
