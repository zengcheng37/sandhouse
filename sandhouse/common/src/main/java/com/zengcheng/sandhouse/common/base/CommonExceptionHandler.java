package com.zengcheng.sandhouse.common.base;

import com.zengcheng.sandhouse.common.enums.ResCodeEnum;
import com.zengcheng.sandhouse.common.entity.ResponseEntity;
import com.zengcheng.sandhouse.common.entity.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理类
 * @author zengcheng
 * @date 2019/4/12
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    /**
     * 主要异常处理方法
     * @param ex 异常对象
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity mainExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex){
        if(ex instanceof NoHandlerFoundException){
            //404 not found 本处不处理交由服务器处理
            response.setStatus(404);
            return null;
        }
        log.error("请求地址{},\n\t请求方式{},\n\t请求来源{},\n\t发生异常:",
                request.getRequestURI(),request.getMethod()
                ,request.getRemoteAddr(),ex);
        if(ex instanceof HttpRequestMethodNotSupportedException){
            //不支持该请求方式
            return ResponseEntityFactory.error(ResCodeEnum.R02);
        } else if(ex instanceof IllegalArgumentException){
            //参数错误
            return ResponseEntityFactory.error(ResCodeEnum.R04);
        } else if(ex instanceof AccessDeniedException){
            //SpringSecurity 权限不足异常
            return ResponseEntityFactory.error(ResCodeEnum.R05);
        } else {
            //其他异常
            return ResponseEntityFactory.error(ResCodeEnum.R01);
        }
    }

}
