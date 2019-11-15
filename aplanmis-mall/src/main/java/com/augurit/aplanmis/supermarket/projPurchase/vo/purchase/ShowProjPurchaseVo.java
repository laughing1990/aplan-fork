package com.augurit.aplanmis.supermarket.projPurchase.vo.purchase;

import com.augurit.aplanmis.common.domain.AeaImMajorQual;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImUnitRequire;
import com.augurit.aplanmis.common.vo.AeaImServiceVo;
import com.augurit.aplanmis.common.vo.AeaItemServiceVo;
import com.augurit.aplanmis.common.vo.SaveAeaImProjPurchaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class ShowProjPurchaseVo extends AeaImProjPurchase {

    @ApiModelProperty(value = "机构要求")
    private AeaImUnitRequire aeaImUnitRequire;

    @ApiModelProperty(value = "中介服务事项")
    private AeaItemServiceVo aeaItemServiceVo;

    @ApiModelProperty(value = "专业要求")
    private List<AeaImMajorQual> aeaImMajorQuals;

    @ApiModelProperty(value = "服务列表")
    private List<AeaImServiceVo> aeaImServices;

    @ApiModelProperty(value = "采购项目信息")
    private SaveAeaImProjPurchaseVo.SaveAeaProjInfoVo aeaProjInfoVo;

}
