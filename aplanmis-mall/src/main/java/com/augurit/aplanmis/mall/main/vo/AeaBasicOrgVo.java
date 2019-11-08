package com.augurit.aplanmis.mall.main.vo;

import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("单位实体")
public class AeaBasicOrgVo {
    @ApiModelProperty("单位ID")
    private String orgId;
    @ApiModelProperty("单位名称")
    private String orgName;
    @ApiModelProperty("初始化部门列表")
    private List<OpuOmOrg> orgDeptList;

    public AeaBasicOrgVo(String orgId,String orgName){
        this.orgId = orgId;
        this.orgName = orgName;
    }
    public AeaBasicOrgVo(){
    }


}
