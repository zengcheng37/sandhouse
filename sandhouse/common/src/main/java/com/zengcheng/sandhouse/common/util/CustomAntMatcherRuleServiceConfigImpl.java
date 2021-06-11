package com.zengcheng.sandhouse.common.util;

import com.zengcheng.sandhouse.common.config.antMatcherRule.CustomAntMatchRulePathVO;
import com.zengcheng.sandhouse.common.config.antMatcherRule.CustomAntMatchRuleVO;
import com.zengcheng.sandhouse.common.config.antMatcherRule.CustomAuthConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 从配置文件读取自定义匹配规则并校验执行结果
 * @author zengcheng
 * @date 2020/09/28
 */
@Slf4j
@Service
public class CustomAntMatcherRuleServiceConfigImpl {

    @Resource
    private CustomAuthConfig customAuthConfig;

    private final PathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 1.遍历所有配置好的规则,找到第一条路径匹配的即停止.
     * 2.如果未找到,直接返回false.
     * 3.判断此次请求是否满足此条规则.
     */
    public boolean hasPermission(HttpServletRequest request,Authentication authentication) {
        //1.遍历所有配置好的规则,找到第一条路径匹配的即停止.
        if(StringUtils.isEmpty(customAuthConfig)
            || CollectionUtils.isEmpty(customAuthConfig.getAntMatchRules())){
            log.error("CustomAntMatcherRuleServiceConfigImpl 未在yml中进行过滤规则相关配置,按照授权信息认证校验!");
            return authentication.isAuthenticated();
        }
        String requestUrlPath = request.getRequestURI();
        String requestMethod = request.getMethod();
        //2.如果未找到,直接返回false.
        CustomAntMatchRuleVO findCustomAntMatchRuleVO = null;
        for(CustomAntMatchRuleVO item : customAuthConfig.getAntMatchRules()){
            for(CustomAntMatchRulePathVO customAntMatchRulePathVO : item.getPaths()){
                if(antPathMatcher.match(customAntMatchRulePathVO.getPath(),requestUrlPath)){
                    if(requestMethod.equals(customAntMatchRulePathVO.getMethod())){
                        findCustomAntMatchRuleVO = item;
                        break;
                    }else if(StringUtils.isEmpty(customAntMatchRulePathVO.getMethod())){
                        findCustomAntMatchRuleVO = item;
                        break;
                    }
                }
            }
        }
        if(StringUtils.isEmpty(findCustomAntMatchRuleVO)){
            log.error("CustomAntMatcherRuleServiceConfigImpl 未在yml中进行过滤规则相关配置,按照授权信息认证校验!");
            return authentication.isAuthenticated();
        }
        //3.判断此次请求是否满足此条规则.
        List<SimpleGrantedAuthority> nowUserAuthorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        if(!CollectionUtils.isEmpty(findCustomAntMatchRuleVO.getRoles())){
            boolean ifFindRole = false;
            for(SimpleGrantedAuthority item : nowUserAuthorities){
                for(String role : findCustomAntMatchRuleVO.getRoles()){
                    if(item.getAuthority().equalsIgnoreCase(role)){
                        ifFindRole = true;
                        break;
                    }
                }
                if(ifFindRole){
                    break;
                }
            }
            log.info("CustomAntMatcherRuleServiceConfigImpl 在yml中找到了匹配的过滤规则{},执行此规则结果{}!", findCustomAntMatchRuleVO.getId(),ifFindRole && findCustomAntMatchRuleVO.getIfPass());
            return ifFindRole && findCustomAntMatchRuleVO.getIfPass();
        }else{
            log.info("CustomAntMatcherRuleServiceConfigImpl 在yml中找到了匹配的过滤规则{},执行此规则结果{}!", findCustomAntMatchRuleVO.getId(), findCustomAntMatchRuleVO.getIfPass());
            return findCustomAntMatchRuleVO.getIfPass();
        }
    }

}
