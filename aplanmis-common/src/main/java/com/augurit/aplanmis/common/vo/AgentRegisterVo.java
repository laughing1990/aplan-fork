package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import lombok.Data;

import java.util.List;

@Data
public class AgentRegisterVo {
    private AeaUnitInfo unitInfo;//单位信息
    private AeaLinkmanInfo contactManInfo;//联系人信息
    private AeaHiCertinst unitAeaHiCertinst;//机构证件实例
    private ServiceAndQualVo serviceAndQualVo;
    private List<AeaLinkmanInfo> authorManList;//授权人信息
    private AeaLinkmanInfo practiceManInfo;//执业/职业人员信息

    private String loginName;//账号
    private String password;//密码
}
