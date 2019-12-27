package com.augurit.aplanmis.mall.guide.vo;


import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@ApiModel("办事指南事项列表VO")
public class RestItemListVo {

    @ApiModelProperty("事项列表")
    private List<RestitemVo> items;
    @ApiModelProperty("列表title，如:第一阶段并联事项")
    private String title;
    @ApiModel("事项VO")

    @Data
    public static class RestitemVo{

        @ApiModelProperty("事项ID")
        private String itemId;
        @ApiModelProperty("事项名称")
        private String itemName;
        @ApiModelProperty("事项版本ID")
        private String itemVerId;

        public static RestitemVo build(AeaItemBasic aeaItemBasic){
            RestitemVo restitemVo = new RestitemVo();
            BeanUtils.copyProperties(aeaItemBasic,restitemVo);
            return  restitemVo;
        }
    }
}
