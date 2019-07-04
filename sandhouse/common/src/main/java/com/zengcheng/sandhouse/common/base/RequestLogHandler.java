package com.zengcheng.sandhouse.common.base;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一请求日志打印处理类(切面)
 * @author zengcheng
 * @date 2019/4/12
 */
@Aspect
@Slf4j
@Component
public class RequestLogHandler {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 切入点
     */
    @Pointcut("execution(* com.zengcheng.sandhouse.*.*Controller.*(..))")
    public void execute(){}

    /**
     * 环绕增强
     * @param pjp
     * @return
     */
    @Around("execute()")
    public Object requestLogAroundHandler(ProceedingJoinPoint pjp) throws Throwable {
        startTime.set(System.currentTimeMillis());
        //通过RequestContextHolder获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        //请求参数(请求地址后接的)
        String queryString = request.getQueryString();
        //请求地址
        String uri = request.getRequestURI() + (StringUtils.isEmpty(queryString) ? "" : ("?" + queryString));
        //请求类型
        String method = request.getMethod();
        //请求方法信息
        String classMethod = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        Object[] args = pjp.getArgs();
        //请求参数
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (!(args[i] instanceof BindingResult)) {
                params.append(args[i]);
                if (i != args.length - 1) {
                    params.append(",");
                }
            }
        }
        log.info("\n\t  - {}请求:{}\n\t  - IP:{}\n\t  - 方法:{}\n\t  - 参数:({})", method, uri, request.getRemoteAddr(), classMethod, params.toString());
        return pjp.proceed();
    }

    /**
     * 返回增强
     * @return
     */
    @AfterReturning(pointcut = "execute()",returning = "object")
    public void requestLogAfterReturningHandler(Object object){
        //通过RequestContextHolder获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        //请求参数(请求地址后接的)
        String queryString = request.getQueryString();
        //请求地址
        String uri = request.getRequestURI() + (StringUtils.isEmpty(queryString) ? "" : ("?" + queryString));
        //请求类型
        String method = request.getMethod();
        //请求开始时间 请求耗时 = 当前时间 - 开始时间
        long beginTime = startTime.get();
        if (object instanceof String) {
            log.info("\n\t  - {}请求结束:{}\n\t - 耗时:{}ms\n\t  - 返回 String:{}", method, uri, System.currentTimeMillis() - beginTime, object);
        } else {
            log.info("\n\t  - {}请求结束:{}\n\t  - 耗时:{}ms\n\t  - 返回 Object:{}", method, uri, System.currentTimeMillis() - beginTime, JSON.toJSONString(object));
        }
        startTime.remove();
    }

}
