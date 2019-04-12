package com.zengcheng.sandhouse.common.base;

import com.zengcheng.sandhouse.common.enums.ResCodeEnum;
import com.zengcheng.sandhouse.common.entity.ResponseEntity;
import com.zengcheng.sandhouse.common.entity.ResponseEntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CommonExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);
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
        logger.error("请求地址{}\n\t,请求方式{}\n\t,请求来源{}\n\t,发生异常:",
                request.getRequestURI(),request.getMethod()
                ,request.getRemoteAddr(),ex);
        if(ex instanceof HttpRequestMethodNotSupportedException){
            //不支持该请求方式
            return ResponseEntityFactory.error(ResCodeEnum.R02);
        } else if(ex instanceof IllegalArgumentException){
            //参数错误
            return ResponseEntityFactory.error(ResCodeEnum.R04);
        } else {
            //其他异常
            return ResponseEntityFactory.error(ResCodeEnum.R01);
        }
    }

}
