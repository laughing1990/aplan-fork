package com.augurit.aplanmis.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/5/005 14:57
 */
@Data
public class AeaImServiceMajorVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String majorId; // ()
    private String majorName; // (专业名称)
}
