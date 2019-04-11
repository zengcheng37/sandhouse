package com.zengcheng.sandhouse.common.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class MpGeneratorUtil {

    /**
     *生成Mapper文件
     */
    /**
     * @param dataSourceConfig 数据库配置
     * @param mapperDir 文件输出目录
     * @param author 作者
     * @param tablePrefixs 表前缀 如果填写,生成实体的时候将不包含前缀
     * @param includeTables 包含的表
     * @param excludeTables 排除的表
     *tablePrefixs,includeTables,至少填写一项,不然无法生成
     */
    public   static void createMapperXml(DataSourceConfig dataSourceConfig,String mapperDir, String author,String parentPkg,String module,String[] tablePrefixs,String[] includeTables,String[] excludeTables){
        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(dataSourceConfig);

        mpg.setGlobalConfig(getGlobalConfig(author,""));

        InjectionConfig cfg = getInjectionConfig(mapperDir);
        mpg.setCfg(cfg);

        // ======== 策略配置 =======
        StrategyConfig strategy = getStrategyConfig(tablePrefixs,includeTables,excludeTables);
        mpg.setStrategy(strategy);

        //========= 设置包 =======
        mpg.setPackageInfo(getPackageConfig(parentPkg,module));

        //======== 模板配置 ============
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setEntity(null);
        tc.setMapper(null);
        tc.setXml(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // 关闭默认 xml 生成，调整生成 至 根目录
        mpg.setTemplate(tc);

        mpg.execute();
    }

    /**
     * @param dataSourceConfig 数据库配置
     * @param outputDir 文件输出目录
     * @param author 作者
     * @param tablePrefixs 表前缀 如果填写,生成实体的时候将不包含前缀
     * @param includeTables 包含的表
     * @param excludeTables 排除的表
     *tablePrefixs,includeTables,至少填写一项,不然无法生成
     */
    public   static  void createModel(DataSourceConfig dataSourceConfig,String outputDir ,String author,String parentPkg,String module,String[] tablePrefixs,String[] includeTables,String[] excludeTables){

        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(dataSourceConfig);

        //========全局配置=======
        mpg.setGlobalConfig(getGlobalConfig(author,outputDir));

        // ======== 策略配置 =======
        mpg.setStrategy(getStrategyConfig(tablePrefixs,includeTables,excludeTables));

        //========= 设置包 =======
        mpg.setPackageInfo(getPackageConfig(parentPkg,module));

        //======== 模板配置 ============
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setMapper(null);
        tc.setXml(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        //tc.setEntity(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // 关闭默认 xml 生成，调整生成 至 根目录
        mpg.setTemplate(tc);

        mpg.execute();

    }

    /**
     * @param dataSourceConfig 数据库配置
     * @param mapperDir 文件输出目录
     * @param author 作者
     * @param tablePrefixs 表前缀 如果填写,生成实体的时候将不包含前缀
     * @param includeTables 包含的表
     * @param excludeTables 排除的表
     *tablePrefixs,includeTables,至少填写一项,不然无法生成
     */
    public   static  void createDao(DataSourceConfig dataSourceConfig,String outputDir ,String author,String parentPkg,String module,String[] tablePrefixs,String[] includeTables,String[] excludeTables){

        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(dataSourceConfig);

        //========全局配置=======
        mpg.setGlobalConfig(getGlobalConfig(author,outputDir));

        // ======== 策略配置 =======
        mpg.setStrategy(getStrategyConfig(tablePrefixs,includeTables,excludeTables));

        //========= 设置包 =======
        mpg.setPackageInfo(getPackageConfig(parentPkg,module));

        //======== 模板配置 ============
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
//        tc.setMapper(null);
        tc.setXml(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntity(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // 关闭默认 xml 生成，调整生成 至 根目录
        mpg.setTemplate(tc);

        mpg.execute();

    }

    /**
     * @param dataSourceConfig 数据库配置
     * @param mapperDir 文件输出目录
     * @param author 作者
     * @param tablePrefixs 表前缀 如果填写,生成实体的时候将不包含前缀
     * @param includeTables 包含的表
     * @param excludeTables 排除的表
     *tablePrefixs,includeTables,至少填写一项,不然无法生成
     */
    public   static  void createService(DataSourceConfig dataSourceConfig,String outputDir ,String author,String parentPkg,String module,String[] tablePrefixs,String[] includeTables,String[] excludeTables){

        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(dataSourceConfig);

        //========全局配置=======
        mpg.setGlobalConfig(getGlobalConfig(author,outputDir));

        // ======== 策略配置 =======
        mpg.setStrategy(getStrategyConfig(tablePrefixs,includeTables,excludeTables));

        //========= 设置包 =======
        mpg.setPackageInfo(getPackageConfig(parentPkg,module));

        //======== 模板配置 ============
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        //TODO 可以进一步优化
        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setMapper(null);
        tc.setXml(null);
//        tc.setService(null);
//        tc.setServiceImpl(null);
        tc.setEntity(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // 关闭默认 xml 生成，调整生成 至 根目录
        mpg.setTemplate(tc);

        mpg.execute();

    }

    /**
     * @param dataSourceConfig 数据库配置
     * @param mapperDir 文件输出目录
     * @param author 作者
     * @param tablePrefixs 表前缀 如果填写,生成实体的时候将不包含前缀
     * @param includeTables 包含的表
     * @param excludeTables 排除的表
     *tablePrefixs,includeTables,至少填写一项,不然无法生成
     */
    public   static  void createController(DataSourceConfig dataSourceConfig,String outputDir ,String author,String parentPkg,String module,String[] tablePrefixs,String[] includeTables,String[] excludeTables){

        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(dataSourceConfig);

        //========全局配置=======
        mpg.setGlobalConfig(getGlobalConfig(author,outputDir));

        // ======== 策略配置 =======
        mpg.setStrategy(getStrategyConfig(tablePrefixs,includeTables,excludeTables));

        //========= 设置包 =======
        mpg.setPackageInfo(getPackageConfig(parentPkg,module));

        //======== 模板配置 ============
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
//        tc.setController(null);
        tc.setMapper(null);
        tc.setXml(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntity(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // 关闭默认 xml 生成，调整生成 至 根目录
        mpg.setTemplate(tc);

        mpg.execute();

    }


    private static PackageConfig getPackageConfig(String parentPkg, String module) {
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPkg);
        pc.setMapper("mapper");
        pc.setModuleName(module);
        return pc;
    }

    private static InjectionConfig getInjectionConfig(final String mapperDir) {
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        // 自定义 xxList.jsp 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return mapperDir + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }


    private static StrategyConfig getStrategyConfig(String[] tablePrefixs,String[] includeTables,String[] excludeTables ) {
        StrategyConfig strategy = new StrategyConfig();

        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略

        if(tablePrefixs != null && tablePrefixs.length > 0){
            strategy.setTablePrefix(tablePrefixs);// 此处可以修改为您的表前缀
        }

        if(includeTables != null && includeTables.length > 0){
            strategy.setInclude(includeTables); // 需要生成的表
        }

        if(excludeTables != null && excludeTables.length > 0){
            strategy.setExclude(excludeTables); // 排除生成的表
        }

        // 全局大写命名 ORACLE 注意
        // strategy.setCapitalMode(true);
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 web 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuliderModel(true);
        return strategy;
    }

    private static GlobalConfig getGlobalConfig(String author, String outputDir) {
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor(author);
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        // gc.setXmlName("%sDao");
         gc.setServiceName("%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        return gc;
    }

}
