package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/4/004 10:26
 */
@Data
public class AeaImServiceTypeItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String serviceTypeItemId; // ()
    private String itemVerId; // (中介服务ID)
    private String serviceTypeId; // (服务类型ID)
    private String isDelete; // (是否删除：1 已经删，0 未删除)
    private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime; // ()
}
