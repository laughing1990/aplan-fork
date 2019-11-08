package com.augurit.aplanmis.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/12/012 14:48
 */
@Data
public class ElTreeVo {

    private String pid;
    private String id;
    private String name;
    private List<ElTreeVo> child;
}
