package com.augurit.aplanmis.data.exchange.constant;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author yinlf
 * @Date 2019/10/17
 */
@Getter
public enum EtlError implements BaseEnum<EtlError, String> {

    DATA_INCOMPLETE("001", "数据不完整"),
    PROJ_UNBIND_THEME("010", "项目未绑定主题"),
    PROJ_NOT_FOUND("011", "未上传项目"),
    ITEM_UNBIND_THEME("020", "单事项未绑定主题"),
    ITEM_NOT_FOUND("021", "未上传事项"),
    ITEMINST_NOT_FOUND("030", "未上传事项实例"),
    PASS_TIME_UPLOAD_ERROR("PASS_TIME_UPLOAD_ERROR", "上传项目完全办结时间错误");

    private String name;
    private String value;

    EtlError(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
