package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhangXinhui
 * @date 2019/9/6 006 15:52
 * @desc
 **/
@Data
public class ErrorDTO implements Serializable {

    private String code;//错误代码
    private String message;//错误信息描述
    private String inner_code;//内部代码
}
