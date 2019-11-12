package com.augurit.aplanmis.front.specialProcedures.vo;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.common.domain.AeaHiItemSpecial;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author:chendx
 * @date: 2019-10-29
 * @time: 13:50
 */
@Data
@ApiModel(value = "特殊程序返回前端实体，包括附件信息")
@EqualsAndHashCode(callSuper = true)
public class SpecialDataVo extends AeaHiItemSpecial {

    private List<BscAttForm> specialStartMatList;

    private List<BscAttForm> specialEndMatList;
}
