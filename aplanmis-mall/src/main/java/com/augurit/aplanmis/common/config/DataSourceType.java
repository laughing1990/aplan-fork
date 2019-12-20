package com.augurit.aplanmis.common.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceType {
    /**
     * 默认数据源
     */
    public static final String DEFAULT_DB = "primary";
    //前置库数据源
    public static final String PROVINCE_DB = "province";

    private static final ThreadLocal<String> dataType = new ThreadLocal<>();

    //设置数据源名
    public static void setDB(String dbType) {
//        log.debug("切换到数据源: ", dbType);
        dataType.set(dbType);
    }

    //获取数据源名
    public static String getDB() {
//        log.debug("get data source :" + dataType.get());
        return dataType.get() == null ? DEFAULT_DB : dataType.get();
    }

    //清除数据源
    public static void clearDB() {
//        log.debug("clear database:" + dataType.get());
        dataType.remove();
    }

}
