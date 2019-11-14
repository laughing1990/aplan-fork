package com.augurit.aplanmis.common.vo.conditional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author tiantian
 * @date 2019/10/23
 */
@Data
@ApiModel("项目信息查询条件")
public class ConditionalQueryAeaProjInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("查询关键字")
    private String keyword;
    @ApiModelProperty("立项类型")
    private String projType;
    @ApiModelProperty("行政分区")
    private String regionalism;
    @ApiModelProperty("建设性质")
    private String projNature;
    @ApiModelProperty("项目级别")
    private String projLevel;
    @ApiModelProperty("最小建筑面积")
    private Double minBuildAreaSum;
    @ApiModelProperty("最大建筑面积")
    private Double maxBuildAreaSum;
    @ApiModelProperty("修改时间-开始")
    private String modifyStartTime;
    @ApiModelProperty("修改时间-结束")
    private String modifyEndTime;
    @ApiModelProperty(value="根组织id",hidden = true)
    private String rootOrgId;
    @ApiModelProperty("true为只查询当前用户行政区划下的项目")
    private boolean onlyRegion;
    @ApiModelProperty("true为只查询当前用户所属部门经办过的项目")
    private boolean onlyOrg;
    @ApiModelProperty("true为只查询当前用户经办过的项目")
    private boolean handler;
    @ApiModelProperty(value ="当前行政区划id及其子类id",hidden = true)
    private List<String> selfAndChildRegionIds;
    @ApiModelProperty(value ="当前用户登录名",hidden = true)
    private String loginName;
    @ApiModelProperty(value ="当前用户id",hidden = true)
    private String userId;
    @ApiModelProperty(value = "当前用户所属组织ID列表",hidden = true)
    private Set<String> currentUserOrgIdList;
    @ApiModelProperty(value = "当前用户所属组织ID和上级ID",hidden = true)
    private Set<String> selfAndParentOrgIdList;
}
