package com.zengcheng.sandhouse.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger api接口扫描配置
 * @author zengcheng
 * @date 2019/4/13
 */
@Component
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //构建api选择器
                .apiInfo(apiInfo())
                .select()
                //api选择器选择api的包
                .apis(RequestHandlerSelectors.basePackage("com.zengcheng.sandhouse.web"))
                //api选择器选择包路径下任何api显示在文档中
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("sandhouse项目接口文档集成中心")
                .description("当前服务没有对外的文档")
                .contact(new Contact("zengcheng", "", "374649103@qq.com"))
                .version("1.0.0")
                .build();
    }

}