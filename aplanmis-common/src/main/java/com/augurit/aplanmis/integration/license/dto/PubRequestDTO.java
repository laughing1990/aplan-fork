package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhangXinhui
 * @date 2019/9/9 009 11:27
 * @desc
 **/
@Data
public class PubRequestDTO<T> implements Serializable {
    private T data;
}
