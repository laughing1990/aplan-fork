package com.augurit.aplanmis.integration.license.constont;

import lombok.Getter;

/**
 * @author ZhangXinhui
 * @date 2019/11/15 015 15:45
 * @desc
 **/
@Getter
public enum LicenseType {

    CERTIFICATE("证件执照","CERTIFICATE"),
    PROOF("证明文件","PROOF"),
    APPROVAL("批文批复","APPROVAL"),
    REPORT("鉴定报告","REPORT"),
    RESULT("办事结果","RESULT");

    private String name;
    private String value;

    LicenseType( String name,String value) {
        this.name = name;
        this.value = value;
    }
}
