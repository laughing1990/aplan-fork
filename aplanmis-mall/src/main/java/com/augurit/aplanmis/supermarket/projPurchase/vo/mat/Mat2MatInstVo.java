package com.augurit.aplanmis.supermarket.projPurchase.vo.mat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("材料与材料实例对应关系")
public class Mat2MatInstVo {
    @ApiModelProperty(value = "材料id", dataType = "string")
    private String matId;

    @ApiModelProperty(value = "材料实例id")
    private List<String> matinstIds;

    public Mat2MatInstVo(String matId, List<String> matinstIds) {
        this.matId = matId;
        this.matinstIds = matinstIds;
    }
}
