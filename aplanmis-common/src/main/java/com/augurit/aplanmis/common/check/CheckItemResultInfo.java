package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CheckItemResultInfo {

    @ApiModelProperty(value = "检查是否通过", notes = "true: 通过; flase: 不通过")
    private boolean passed;

    @ApiModelProperty(value = "前置检查不通过的事项信息")
    private List<ItemResult> itemResults;

    @ApiModelProperty(value = "所有不通过消息")
    private String message;

    public CheckItemResultInfo() {
        this.passed = true;
        itemResults = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class ItemResult {

        private AeaItemBasic checkedItem;

        private List<AeaItemBasic> frontItemBasic;

        private List<AeaItemFrontPartformVo> itemFrontPartforms;

    }
}
