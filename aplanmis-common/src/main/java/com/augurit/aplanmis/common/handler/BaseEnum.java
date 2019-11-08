package com.augurit.aplanmis.common.handler;

/**
 * @author ZhangXinhui
 * @date 2019/7/26 026 15:13
 * @desc
 **/
public interface BaseEnum<E extends Enum<?>, T> {
    T getValue();

    String getName();
}
