package com.augurit.aplanmis.front.approve.vo;

import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AttDirsVo {
    @ApiModelProperty(value = "dirId", name = "文件夹ID，修改时需要")
    private String dirId;

    @ApiModelProperty(value = "isRoot", name = "是否根文件夹，0表示普通文件夹，1表示根文件夹", required = true, allowableValues = "0,1")
    private String isRoot;

    @ApiModelProperty(value = "dirCode", name = "文件夹编号")
    private String dirCode;

    @ApiModelProperty(value = "dirName", name = "文件夹名字", required = true)
    private String dirName;

    @ApiModelProperty(value = "parentId", name = "文件夹父ID")
    private String parentId;
    @ApiModelProperty(value = "dirSeq", name = "文件夹序列",required = true)
    private String dirSeq;

    @ApiModelProperty(value = "tableName", name = "业主表名",required = true)
    private String tableName;

    @ApiModelProperty(value = "recordId", name = "业务表主键ID",required = true)
    private String recordId;

    @ApiModelProperty(value = "pkName", name = "业务表主键",required = true)
    private String pkName;

    @ApiModelProperty(value = "linkType", name = "d 文件夹，a 文件")
    private String linkType;
    private List<BscAttDir> dirs;
    private List<BscAttForm> files;
}
