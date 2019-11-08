package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class AeaItemAuxService implements Serializable {
    private static final long serialVersionUID = 1L;

    private String auxServiceId;//主键
    private String fwxmmc;//服务项目名称
    private String xzfwjgfs;//选择服务机构方式
    private String fwjgmc;//服务机构名称
    private String fwjgxz;//服务机构性质
    private String fwsx;//服务时限,中介服务的文字说明
    private String syqx;//适用情形
    private String ywsf;//服务收费情况(1:有收费;0:无收费)
    private String sflx;//收费类型
    private String sfbz;//收费标准
    private String sfjmqx;//填写本服务项目收费减免的具体情形（有无收费为“有”时，此处为非空）

    private String keyword;
}
