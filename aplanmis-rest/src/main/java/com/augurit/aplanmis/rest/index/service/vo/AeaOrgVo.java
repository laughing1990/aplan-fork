package com.augurit.aplanmis.rest.index.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("单位实体")
public class AeaOrgVo {

    @ApiModelProperty("单位ID")
    private String orgId;
    @ApiModelProperty("单位名称")
    private String orgName;
    @ApiModelProperty("下级单位实体集合")
    private List<AeaBasicOrgVo> childAeaOrgVo = new ArrayList<>();

    public AeaOrgVo(String orgId, String orgName) {
        this.orgId = orgId;
        this.orgName = orgName;
    }

    public AeaOrgVo() {
    }

}
