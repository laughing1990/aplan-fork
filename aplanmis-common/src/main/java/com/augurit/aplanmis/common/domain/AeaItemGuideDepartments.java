package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 窗口信息-模型
 */
@Data
public class AeaItemGuideDepartments implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // (主键)
    private String itemVerId; // (事项ID)
    private String address; // (窗口地址)
    private String name; // (窗口名称)
    private String tel; // (窗口电话)
    private String time; // (窗口办公时间)
    private String timeDelayBus; // (交通指引)
    private String topwin; // (是否首选项)
    private String topwinText; // (是否首选项文)
    private String fileid; // (地图定位链接)
    private String rootOrgId; // 根组织id
}
