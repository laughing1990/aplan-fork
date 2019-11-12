package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaImUnitService;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/5/005 14:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceMatterVo extends AeaImUnitService implements Serializable {
    private String serviceName; //中介服务名称
//    private String linkmanInfoName;//从业人员名称
    private List<AeaLinkmanInfo> linkmanInfo;//
    private List<AeaHiCertinstBVo> certinstBVos; //证照列表
    private List<AeaItemBasic> agentItemServices;//中介服务事项
    private String certinstStr;//服务审核，用到拼接多个证照名在一起
}
