package com.augurit.aplanmis.integration.license.constont;

import lombok.Getter;

/**
 * @author ZhangXinhui
 * @date 2019/11/15 015 15:45
 * @desc
 **/
@Getter
public enum LicenseType {

    CERTIFICATE("已签发","CERTIFICATE"),
    PROOF("草案","PROOF"),
    APPROVAL("已制证（未签发）","APPROVAL"),
    REPORT("已废止","REPORT"),
    RESULT("已制证（未签发）","RESULT");

    private String name;
    private String value;

    LicenseType( String name,String value) {
        this.name = name;
        this.value = value;
    }
}
