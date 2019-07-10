package com.zengcheng.sandhouse.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger api接口扫描配置
 * @author zengcheng
 * @date 2019/4/13
 */
@Component
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public Docket createRestApi() {
        //设置header参数 accessToken
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("accessToken").description("大部分接口需要token,部分接口(如登录接口)不需要token")
                .modelRef(new ModelRef("string")).parameterType("header")
                //header中的ticket参数非必填，传空也可以
                .required(false).build();
        //根据每个方法名也知道当前方法在设置什么参数
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                //构建api选择器
                .apiInfo(apiInfo())
                .select()
                //api选择器选择api的包
                .apis(RequestHandlerSelectors.basePackage("com.zengcheng.sandhouse.web"))
                //api选择器选择包路径下任何api显示在文档中
                .paths(PathSelectors.any())
                .build()
                //设置全局header
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("sandhouse项目接口文档集成中心")
                .description(appName+"模块")
                .contact(new Contact("zengcheng", "", "374649103@qq.com"))
                .version("1.0.0")
                .build();
    }

}