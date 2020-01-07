package com.augurit.aplanmis.common.vo.agency;

import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class AgencyDetailVo implements Serializable {

        @ApiModelProperty(name = "aeaUnitProjLinkmanVo", value = "项目单位信息 ")
        private AeaUnitProjLinkmanVo aeaUnitProjLinkmanVo;

        @ApiModelProperty(name = "aeaProjInfo", value = "项目信息 ")
        private AeaProjInfo aeaProjInfo;

        @ApiModelProperty(name = "aeaProjApplyAgent", value = "代办申请信息 ")
        private AeaProjApplyAgent aeaProjApplyAgent;

}
