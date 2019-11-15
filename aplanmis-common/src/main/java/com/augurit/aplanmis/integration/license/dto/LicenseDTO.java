package com.augurit.aplanmis.integration.license.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangXinhui
 * @date 2019/9/6 006 16:18
 * @desc
 **/
@Data
public class LicenseDTO implements Serializable {

    private String license_code;

    private String name;

    private String license_type;

    private String id_code;

    private String doc_name;

    private String doc_summary;

    private String doc_keyword;

    private String holder_name;

    private String holder_identity_type;

    private String holder_identity_num;

    private String issue_org_name;

    private String issue_org_code;

    private String division;

    private String division_code;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issue_date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin_date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiry_date;

    private String data_fields;

    private String attachment_fields;

    private String trust_level;

    private String extend_props;

    private String remark;

    private String biz_num;

    private String license_item_code;

    private String license_status;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creation_time;

    private String issuer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issue_time;

    private String abolisher;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date abolish_time;

    private String abolish_reason;

    private String correlative_license;

    private String public_key;

    private String s_sign_cert;

    private String algorithm;

    private String s_sign_data;

    private String last_modificator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date last_modification_time;

    private String auth_code;

    private String license_status_name;

    private String license_type_name;
}
