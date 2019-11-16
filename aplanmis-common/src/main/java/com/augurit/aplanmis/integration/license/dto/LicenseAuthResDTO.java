package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/11/5 005 10:24
 * @desc
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LicenseAuthResDTO extends PubResponseDTO<List<LicenseDTO>>{
    private String[] auth_codes;//用证码列表
}
