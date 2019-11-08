package com.augurit.aplanmis.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/5/005 14:55
 */
@Data
public class AeaImServiceTypeVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serviceTypeId;
    private String serviceTypeName; // (服务类型名称)
}
