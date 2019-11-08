package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class AeaItemAcceptRange implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // (受理范围ID)
    private String itemBasicId; // (事项ID)
    private String ssml; // (申请人分类)
    private String bmfl; // (部门分类信息)
    private String sqr; // (申请人)
    private String sqnr; // (申请内容)
    private String sqtj; // (申请条件)
    private String dataId; // (省的数据ID)

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (修改时间)

    // 扩展字段
    private String ssmlName; // (申请人分类名称集合)
    private String bmflName; // (部门分类信息名称集合)
}
