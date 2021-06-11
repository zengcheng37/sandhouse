package com.zengcheng.sandhouse.common.config.antMatcherRule;

import com.zengcheng.sandhouse.common.config.ApolloPropsRefreshConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件
 * @version 0_1
 * @author zengcheng
 * @date 2020/09/28
 */
@Component
@ConfigurationProperties("sandhouse.auth")
@RefreshScope
@Data
@Validated
public class CustomAuthConfig extends ApolloPropsRefreshConfig implements Serializable {

    private static final long serialVersionUID = 1784597590880136885L;

    /**
     * 校验规则集合
     */
    @NotNull
    @Valid
    private List<CustomAntMatchRuleVO> antMatchRules = new ArrayList<>();

}
