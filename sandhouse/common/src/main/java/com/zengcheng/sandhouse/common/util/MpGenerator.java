package com.zengcheng.sandhouse.common.util;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbType;

/**
 * <p>
 * 代码生成器演示
 * </p>
 */
public class MpGenerator {

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert());
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("yzzy65203");
        dsc.setUrl("jdbc:mysql://localhost:3306/test?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true");
        //去前缀
        String[] prefix = new String[]{""};
        //表名
        String [] tables  =  new String[]{"student"};
        String entityDir = "C:\\Users\\zeng\\Documents\\zengcheng\\sandhouse\\order\\src\\main\\java";
        String mapperDir = "C:\\Users\\zeng\\Documents\\zengcheng\\sandhouse\\order\\src\\main\\resources\\mapper\\";
        String controllerDir = "C:\\Users\\zeng\\Documents\\zengcheng\\sandhouse\\order\\src\\main\\java";

        //mapper.xml entity mapper.java 都放在common-db中
        String pkgName = "com.zengcheng.sandhouse";
        MpGeneratorUtil.createModel(dsc,entityDir,"zengcheng",pkgName,null,prefix,
                tables,null);

        MpGeneratorUtil.createDao(dsc,entityDir,"zengcheng",pkgName,null,prefix,
                tables,null);

        MpGeneratorUtil.createMapperXml(dsc,mapperDir, "zengcheng",pkgName,null,prefix,
                tables,null);

        //service serviceiml web 都放在各自的项目中
        String controllerPkgName = "com.zengcheng.sandhouse";
        MpGeneratorUtil.createService(dsc,controllerDir,"zengcheng",controllerPkgName,null,prefix,
               tables,null);

        MpGeneratorUtil.createController(dsc,controllerDir,"zengcheng",controllerPkgName,null,prefix,
               tables,null);

    }

}