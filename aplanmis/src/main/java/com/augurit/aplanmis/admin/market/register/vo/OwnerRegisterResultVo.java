package com.augurit.aplanmis.admin.market.register.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import lombok.Data;

import java.util.List;

@Data
public class OwnerRegisterResultVo {
    private AeaUnitInfo unitInfo;//单位信息
    private List<AeaLinkmanInfo> contactManList;//联系人列表信息（包括授权人、执业人员）
    private List<BscAttFileAndDir> unitFileList;
    private List<BscAttFileAndDir> authorManFileList;
    private AeaLinkmanInfo authorManInfo;//执业/职业人员信息

}
