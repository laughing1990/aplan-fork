package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 权利与义务
 * Author: Michael
 * Date: 2018-10-27 19:55:56
 */
@Data
public class AeaItemRightsObligations implements Serializable {
    private static final long serialVersionUID = 1L;

    private String rightObliId;                 //权利义务ID
    private String itemBasicId;                      //本级事项目录ID
    private String rightObliDetails;            //申请人权利和义务
    private String rightObliType;               //申请人权利和义务类型
    private String dataId;                      //省的数据ID

    private String creater;                     //创建人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;                    //创建时间

    private String modifier;                    //修改人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime;                    //修改时间

    private String itemName;                    //事项名

    // 扩展字段
    private String keyword;
}
