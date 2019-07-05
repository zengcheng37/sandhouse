package com.zengcheng.sandhouse.common.web;


import com.zengcheng.sandhouse.common.entity.ResponseEntity;
import com.zengcheng.sandhouse.common.entity.ResponseEntityFactory;
import com.zengcheng.sandhouse.common.enums.ResCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 错误请求控制器
 * @author zengcheng
 * @since 2019-07-05
 */
@RestController
@RequestMapping("/error")
@Slf4j
public class ErrorController {

    @RequestMapping("/{code}")
    public ResponseEntity to404(HttpServletRequest request, @PathVariable String code){
        log.error("请求地址{},\n\t请求方式{},\n\t请求来源{},\n\t出现{}错误:",
                request.getRequestURI(),request.getMethod()
                ,request.getRemoteAddr(),code);
        return ResponseEntityFactory.error(ResCodeEnum.R06.getCode(),"出现"+code+"错误!");
    }

}

