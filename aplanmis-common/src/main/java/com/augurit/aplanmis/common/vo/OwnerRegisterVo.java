package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import lombok.Data;

@Data
public class OwnerRegisterVo {
    private AeaUnitInfo unitInfo;//单位信息
    private AeaLinkmanInfo contactManInfo;//联系人信息
    private AeaHiCertinst unitAeaHiCertinst;//机构证件实例
    private AeaLinkmanInfo authorManInfo;//委托人信息

    private String loginName;
    private String password;
}
