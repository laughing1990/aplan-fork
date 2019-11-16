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

    private LicenseUserInfoDTO operator;//发起此次用证的业务人员信息

    private Long page_index;//返回第几页数据。默认值 1

    private Long page_size;//返回数据的每页大小。默认值 10，最大值150

    private String service_item_code;//事项编码

    private String service_item_name;//事项名称

    private String biz_num;//对应办件的业务流水号

    private String identity_number;//证件号码。多个时用逗号（,）分隔


}
