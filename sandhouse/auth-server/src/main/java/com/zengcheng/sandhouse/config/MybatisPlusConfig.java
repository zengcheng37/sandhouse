package com.zengcheng.sandhouse.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * mybatis plus 配置，通过代码配置，实现了依赖即配置，不用单独在项目的yml里面配置了
 * 参考：https://gitee.com/baomidou/mybatisplus-spring-boot/blob/config%E6%96%B9%E5%BC%8F/src/main/java/com/baomidou/springboot/config/MybatisPlusConfig.java
 * @author zengcheng
 */
@Configuration
@Slf4j
public class MybatisPlusConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setTypeAliasesPackage("com.zengcheng.sandhouse.**.entity");
        bean.setDataSource(dataSource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        //配置实体字段和表列的自动映射，自动需遵循驼峰发规则
        configuration.setMapUnderscoreToCamelCase(true);
        //设置Null值，防止返回map的时候null字段不显示
        configuration.setCallSettersOnNulls(true);
        configuration.setLogImpl(StdOutImpl.class);
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);

        try {
            // 配置加载mapper.xml映射文件
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));

            //配置mybatis的pagehelper插件
            PageInterceptor pageInterceptor = this.initPageInterceptor();
            bean.setPlugins(new Interceptor[]{pageInterceptor});

            return bean.getObject();
        } catch (Exception e) {
            log.error("加载 mapper文件出错 错误原因为:",e);
            throw e;
        }

    }

    /**
     * 相当于顶部的：
     * {@code @MapperScan("com.baomidou.springboot.mapper*")}
     * 这里可以扩展，比如使用配置文件来配置扫描Mapper的路径
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setBasePackage("com.zengcheng.sandhouse.**.mapper");
        return scannerConfigurer;
    }

    private PageInterceptor initPageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
