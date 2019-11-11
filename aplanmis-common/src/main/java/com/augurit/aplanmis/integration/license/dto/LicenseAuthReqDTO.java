package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @author ZhangXinhui
 * @date 2019/9/6 006 11:05
 * @desc 依职能用证请求数据
 **/
@Data
public class LicenseAuthReqDTO implements Serializable {

    private LicenseUserInfoDTO operator;

    private Long page_index;

    private Long page_size;

    private String service_item_code;

    private String service_item_name;

    private String biz_num;

    private String identity_number;


}
