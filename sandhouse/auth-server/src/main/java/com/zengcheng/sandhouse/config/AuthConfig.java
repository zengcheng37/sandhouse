package com.zengcheng.sandhouse.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 授权服务器相关配置
 * @author zengcheng
 * @date 2019/09/29
 */
@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Resource
    @Qualifier("CustomAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Resource
    private DataSource dataSource;

    /**
     * AuthorizationClient配置
     * Grant Type代表当前授权的类型：
     * <p>
     *     authorization_code：传统的授权码模式<br>
     *     implicit：隐式授权模式<br>
     *     password：资源所有者（即用户）密码模式<br>
     *     client_credentials：客户端凭据（客户端ID以及Key）模式<br>
     *     refresh_token：获取access token时附带的用于刷新新的token模式
     * </p>
     * 将ClientDetailsServiceConfigurer（从您的回调AuthorizationServerConfigurer）可以用来在内存或JDBC实现客户的细节服务来定义的。客户端的重要属性是
     *      clientId：（必填）客户端ID。
     *      secret:(可信客户端需要）客户机密码（如果有）。没有可不填
     *      scope：客户受限的范围。如果范围未定义或为空（默认值），客户端不受范围限制。read write all
     *      authorizedGrantTypes：授予客户端使用授权的类型。默认值为空。
     *      authorities授予客户的授权机构（普通的Spring Security权威机构）。
     * 客户端的详细信息可以通过直接访问底层商店（例如，在数据库表中JdbcClientDetailsService）或通过ClientDetailsManager接口（这两种实现ClientDetailsService也实现）来更新运行的应用程序。
     *
     * 这个如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
     * 如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的，走basic认证保护
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(accessTokenConverter());
        endpoints.tokenStore(tokenStore());
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        // 允许表单认证
        oauthServer.allowFormAuthenticationForClients();
        //开启/oauth/check_token验证端口认证权限访问。
        oauthServer.checkTokenAccess("permitAll()");
    }

    /**
     * JWT实现token转换器
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        // 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
        accessTokenConverter.setSigningKey("sandhouse");
        return accessTokenConverter;
    }

    /**
     * token存储方式配置,暂使用redis存储
     * 这里使用redis存储是为了实现登出时立即失效token的场景,但此处实际已经违背JWT无状态的定义,属无奈之举.
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 暂时用默认SpringSecurity数据库客户端设置配置
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }



}