package com.augurit.aplanmis.integration.license.constont;

import lombok.Getter;

/**
 * @author ZhangXinhui
 * @date 2019/11/15 015 12:42
 * @desc
 **/
@Getter
public enum LicenseStatus {

    ISSUED("已签发", "ISSUED"),
    DRAFT("草案", "DRAFT"),
    REGISTERED("已制证（未签发）", "REGISTERED"),
    ABOLISHED("已废止", "ABOLISHED");
    private String name;
    private String value;

    LicenseStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }




}
