package com.zengcheng.sandhouse.common.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * apollo配置实时刷新基类
 * apollo实时刷新配置类,需要继承该类
 * @author zengcheng
 * @date 2020/08/07
 */
@Data
@Slf4j
public class ApolloPropsRefreshConfig implements Serializable {

    private static final long serialVersionUID = -6410851787776966837L;

    @Resource
    private RefreshScope refreshScope;

    /**
     * apollo配置监听器,默认监听 application.properties
     */
    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        String beanName = this.getClass().getSimpleName();
        log.info("ApolloPropsRefreshConfig 刷新配置类 : [{}] ,刷新配置属性 : [{}]", beanName,changeEvent.changedKeys());
        beanName = getFirstLowerField(beanName);
        log.info("ApolloPropsRefreshConfig 刷新spring IOC容器中的配置类 : [{}] 成功", beanName);
        refreshScope.refresh(beanName);
    }

    /**
     * 将字符串首字母转为小写
     */
    public String getFirstLowerField(String fieldName) {
        return StringUtils.isEmpty(fieldName) ? "" : fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
    }

}
