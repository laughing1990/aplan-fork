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

    private String license_code;//电子证照唯一标识码

    private String name;//证照中文名称

    private String license_type;//证照类型

    private String id_code;//证照号码

    private String doc_name;//文书标题

    private String doc_summary;//文书内容概要

    private String doc_keyword;//文书主题词

    private String holder_name;//持证者名称（如有多个，中间以逗号分割）

    private String holder_identity_type;//持证者身份证件类型代码（如有多个，中间以逗号分割）

    private String holder_identity_num;//持证者身份证件号码（如有多个，中间以逗号分割）

    private String issue_org_name;//签发机构名称

    private String issue_org_code;//签发机构的组织机构代码

    private String division;//签发机构所属行政区划

    private String division_code;//签发机构所属行政区划代码

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issue_date;//签发日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin_date;//证照有效期开始日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiry_date;//证照有效期截止日期

    private String data_fields;//证照业务数据

    private String attachment_fields;//证照附件数据

    private String trust_level;//证照信息的可信等级

    private String extend_props;//证照扩展信息

    private String remark;//备注

    private String biz_num;//签发证照对应办件的业务流水号

    private String license_item_code;//目录编码

    private String license_status;//证照状态

    private String creator;//制证操作人

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creation_time;//制证时间

    private String issuer;//签发操作人

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issue_time;//签发时间

    private String abolisher;//废止操作人

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date abolish_time;//废止时间

    private String abolish_reason;//废止原因

    private String correlative_license;//相关联的证照标识

    private String public_key;//公钥值

    private String s_sign_cert;//签发机构密钥标识符

    private String algorithm;//签名算法

    private String s_sign_data;//证照数据电文经过签名得出的签名值

    private String last_modificator;//最后修改人

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date last_modification_time;//最后修改时间

    private String auth_code;//电子证照用证码

    private String license_status_name;//证照状态名称

    private String license_type_name;//证照类型名称（证照执照，证明文件，批文批复，鉴定报告，办事结果）
}
