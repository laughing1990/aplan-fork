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

    private String code;
    private String message;
    private String inner_code;
}
