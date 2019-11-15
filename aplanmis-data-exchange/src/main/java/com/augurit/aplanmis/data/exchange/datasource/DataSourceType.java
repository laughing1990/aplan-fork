package com.augurit.aplanmis.data.exchange.datasource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceType {

    public enum DataBaseType {
        APLANMIS,DUOGUI, PROVINCE,ANALYSE
    }

    /**
     * 使用ThreadLocal保证线程安全
     */
    private static final ThreadLocal<DataBaseType> TYPE = new ThreadLocal<DataBaseType>();

    /**
     * 使用ThreadLocal保证线程安全
     */
    private static final ThreadLocal<DataBaseType> SMALL_TYPE = new ThreadLocal<DataBaseType>();

    /**
     * 往当前线程里设置数据源类型
     */
    public static void setDataBaseType(DataBaseType dataBaseType) {
        if (dataBaseType == null) {
            throw new NullPointerException();
        }
        log.info("[将当前数据源改为]：{}" + dataBaseType);
        TYPE.set(dataBaseType);
    }

    /**
     * 往当前线程里设置前置库类型
     */
    public static void setSmallType(DataBaseType dataBaseType) {
        if (dataBaseType == null) {
            throw new NullPointerException();
        }
        log.info("[将当前上传前置库改为]：{}" + dataBaseType);
        SMALL_TYPE.set(dataBaseType);
    }


    /**
     * 获取数据源类型
     */
    public static DataBaseType getDataBaseType() {
        DataBaseType dataBaseType = TYPE.get() == null ? DataBaseType.APLANMIS : TYPE.get();
        return dataBaseType;
    }

    /**
     * 获取前置库类型
     */
    public static DataBaseType getSmallType() {
        DataBaseType dataBaseType = SMALL_TYPE.get() == null ? DataBaseType.PROVINCE : SMALL_TYPE.get();
        return dataBaseType;
    }

    /**
     * 清空数据类型
     */
    public static void clearDataBaseType() {
        TYPE.remove();
    }

}