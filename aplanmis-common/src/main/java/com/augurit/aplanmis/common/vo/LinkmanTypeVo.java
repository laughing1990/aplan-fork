package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel(value = "项目单位联系人类型")
public class LinkmanTypeVo {
    @ApiModelProperty(value = "联系人类型，数据字典获取")
    private String linkmanType;
    @ApiModelProperty(value = "联系人ID")
    private String linkmanInfoId;
    @ApiModelProperty(value = "联系人名")
    private String linkmanName;
    @ApiModelProperty(value = "单位ID")
    private String unitInfoId;
    @ApiModelProperty(value = "项目ID")
    private String projInfoId;

    public static LinkmanTypeVo build(AeaLinkmanInfo aeaLinkmanInfo){
        LinkmanTypeVo vo=new LinkmanTypeVo();
        if (aeaLinkmanInfo != null)
            BeanUtils.copyProperties(aeaLinkmanInfo, vo);
        return vo;
    }
}