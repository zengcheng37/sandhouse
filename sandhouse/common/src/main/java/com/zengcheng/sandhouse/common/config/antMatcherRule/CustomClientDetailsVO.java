package com.zengcheng.sandhouse.common.config.antMatcherRule;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengcheng
 */
@Data
@Validated
public class CustomClientDetailsVO implements Serializable {

    private static final long serialVersionUID = 615463809288727903L;

    /**
     * 客户端id
     */
    @NotNull
    private String clientId;
    /**
     * 客户端密码
     */
    @NotNull
    private String clientSecret;
    /**
     * 授权域
     */
    private List<String> scope = new ArrayList<>();
    /**
     * 资源id
     */
    private List<String> resourceIds = new ArrayList<>();
    /**
     * 授权类型
     */
    private List<String> authorizedGrantTypes = new ArrayList<>();
    /**
     * 注册跳转地址
     */
    private List<String> registeredRedirectUris = new ArrayList<>();
    /**
     * 自动通过的授权域
     */
    private List<String> autoApproveScopes = scope;
    /**
     * 授予权限
     */
    private List<GrantedAuthority> authorities = new ArrayList<>();
    /**
     * accessToken失效时间(秒)
     */
    private Integer accessTokenValiditySeconds;
    /**
     * refreshToken失效时间(秒)
     */
    private Integer refreshTokenValiditySeconds;
    /**
     * 额外信息
     */
    private transient Map<String, Object> additionalInformation = new LinkedHashMap<>();

}
