package com.augurit.aplanmis.admin.market.register.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.common.domain.AeaImUnitService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ServiceAndQualVo;
import lombok.Data;

import java.util.List;

@Data
public class RegisterResultVo {
    private AeaUnitInfo unitInfo;//单位信息
    private RegisterServiceAndQualVo registerServiceAndQualVo;
    private List<AeaLinkmanInfo> authorManList;//授权人列表信息
    private AeaLinkmanInfo practiceManInfo;//执业/职业人员信息

    private List<AeaLinkmanInfo> contactManList;//联系人列表信息（包括授权人、执业人员）
    private List<BscAttFileAndDir> unitFileList;
    private List<BscAttForm> serviceFileList;
    private List<BscAttForm> practiceManCertFileList;
    private List<BscAttForm> practiceManFileList;

}
