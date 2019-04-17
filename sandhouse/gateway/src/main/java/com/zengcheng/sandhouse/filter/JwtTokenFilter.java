package com.zengcheng.sandhouse.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zengcheng.sandhouse.entity.ResponseEntityFactory;
import com.zengcheng.sandhouse.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求过滤器
 * OncePerRequestFilter抽象类代表一次请求只过滤一次
 * @author zengcheng
 * @date 2019/4/11
 */
@Component
public class JwtTokenFilter extends ZuulFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 存放Token的Header Key
     */
    private static final String HEADER_STRING = "accessToken";
    /**
     * 配置无需校验token的请求
     */
    private static final Pattern PATTERN = Pattern.compile("/api-user/notneedlogin");

    private Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //检验是否需要被拦截(此处可改为从redis中读取)
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        Matcher matcher = PATTERN.matcher(path);
        return !matcher.matches();
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //检验是否需要被拦截
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        //获取请求头里的认证信息，如果没有，或者验证不成功返回无权限
        String accessToken = request.getHeader(HEADER_STRING);
        String message;
        if (accessToken == null) {
            //header里未找到accessToken
            message = "accessToken is not found!";
        } else {
            //此处不校验权限
            message = jwtTokenUtil.validateToken(accessToken);
        }
        if (message != null) {
            //验证失败，不进行路由
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            String body = JSON.toJSONString(ResponseEntityFactory.error("402", message));
            ctx.setResponseBody(body);
            logger.info("来自{}的请求被过滤器拦截返回: {}", path,body);
        }
        return null;
    }
}