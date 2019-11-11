package com.augurit.aplanmis.integration.license.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ZhangXinhui
 * @date 2019/11/5 005 11:13
 * @desc
 **/
@Data
public class LicenseTokenResDataDTO {

    private String license_access_token;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiry_date;
}
