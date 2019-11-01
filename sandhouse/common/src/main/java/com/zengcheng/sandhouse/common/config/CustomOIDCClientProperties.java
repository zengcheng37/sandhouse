package com.zengcheng.sandhouse.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义OIDCClient相关配置
 * @author zengcheng
 * @date 2019/10/08
 */
@ConfigurationProperties(prefix = "custom")
public class CustomOIDCClientProperties {

  /**
   * clientId 客户端id
   */
  private String clientId;
  /**
   * clientSecret 客户端秘钥
   */
  private String clientSecret;
  /**
   * 重定向地址模板
   */
  private String redirectUriTemplate;
  /**
   * 授权地址
   */
  private String authorizationUri;
  /**
   * 获取token地址
   */
  private String tokenUri;
  /**
   * 获取用户信息地址
   */
  private String userInfoUri;
  /**
   * 获取jwk地址
   */
  private String jwkSetUri;
  /**
   * 用户名标识
   */
  private String userNameAttributeName;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getRedirectUriTemplate() {
    return redirectUriTemplate;
  }

  public void setRedirectUriTemplate(String redirectUriTemplate) {
    this.redirectUriTemplate = redirectUriTemplate;
  }

  public String getAuthorizationUri() {
    return authorizationUri;
  }

  public void setAuthorizationUri(String authorizationUri) {
    this.authorizationUri = authorizationUri;
  }

  public String getTokenUri() {
    return tokenUri;
  }

  public void setTokenUri(String tokenUri) {
    this.tokenUri = tokenUri;
  }

  public String getUserInfoUri() {
    return userInfoUri;
  }

  public void setUserInfoUri(String userInfoUri) {
    this.userInfoUri = userInfoUri;
  }

  public String getJwkSetUri() {
    return jwkSetUri;
  }

  public void setJwkSetUri(String jwkSetUri) {
    this.jwkSetUri = jwkSetUri;
  }

  public String getUserNameAttributeName() {
    return userNameAttributeName;
  }

  public void setUserNameAttributeName(String userNameAttributeName) {
    this.userNameAttributeName = userNameAttributeName;
  }
}
