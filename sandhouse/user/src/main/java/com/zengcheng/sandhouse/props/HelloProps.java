package com.zengcheng.sandhouse.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 */
@Component
@PropertySource("classpath:hello.properties")
@Data
public class HelloProps {

    @Value("${name}")
    private String name;
    
}
