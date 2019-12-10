package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
/**
* 材料补全应收实例表-模型
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>日期：2019-08-28 17:34:02</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:tiantian</li>
    <li>创建时间：2019-08-28 17:34:02</li>
    <li>修改人1：</li>
    <li>修改时间1：</li>
    <li>修改内容1：</li>
</ul>
*/
@Data
public class AeaHiApplyinstCorrectDueIninst implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String applyinstDueIninstId; // (ID)
        private String applyinstCorrectId; // (材料补全实例ID)
        private String isNewMatinst; // ()
        private String matinstId; // (单个事项输入输出定义ID)
        private String correctOpinion; // ()
        private String creater; // (创建人)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        @Size(max=10)
        private Long paperCount; // (纸质材料数量)
        @Size(max=10)
        private Long copyCount; // (复印件材料数量)
        private String isNeedAtt; // (是否需要电子件：1 是，0 否)
        private String rootOrgId; // ()

        //扩展字段
        private String matinstName;
        private String matId;
        private String reviewKeyPoints;
        private String matProp;//材料性质，m表示普通材料，c表示证照材料，f表示在线表单
        private String certId;//证照ID
}
