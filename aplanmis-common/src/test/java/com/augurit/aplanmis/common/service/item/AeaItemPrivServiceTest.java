package com.augurit.aplanmis.common.service.item;

import base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.AeaItemPrivConstants;
import com.augurit.aplanmis.common.constants.AeaLinkmanConstants;
import com.augurit.aplanmis.common.domain.AeaItemPriv;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/8/2
 */
@DisplayName("事项承办接口测试")
public class AeaItemPrivServiceTest extends BaseTest {

    @Autowired
    AeaItemPrivService aeaItemPrivService;

    @Test
    @DisplayName("新建联系人")
    public void insertAeaItemPrivTest(){
        AeaItemPriv aeaItemPriv = new AeaItemPriv();
        aeaItemPriv.setRegionId("64");
        aeaItemPriv.setItemVerId("663e2083-9464-4be7-b58e-895d62b68ba4");
        aeaItemPriv.setOrgId("07afbb52-23ac-4968-b322-6e18553d60b6");
        aeaItemPriv.setRootOrgId("0368948a-1cdf-4bf8-a828-71d796ba89f6");
        aeaItemPriv.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaItemPriv.setUseEl(AeaItemPrivConstants.USE_EL_FALSE);
        aeaItemPriv.setAllowManual(AeaItemPrivConstants.ALLOW_MANUAL_FALSE);
        aeaItemPrivService.insertAeaItemPriv(aeaItemPriv);
    }

    @Test
    @DisplayName("新建联系人2")
    public void insertAeaItemPrivTest2(){
        aeaItemPrivService.insertAeaItemPriv("663e2083-9464-4be7-b58e-895d62b68ba4","64","07afbb52-23ac-4968-b322-6e18553d60b6",AeaItemPrivConstants.ALLOW_MANUAL_FALSE,null);
    }

    @Test
    @DisplayName("删除联系人")
    public void deleteAeaItemPrivTest(){
        aeaItemPrivService.deleteAeaItemPriv("64","07fa45af-b91c-47b1-ad67-6f000b206793","29873d61-b481-4769-ad5d-37b7a1a24530");
    }

    @Test
    @DisplayName("获取当前用户事项的承办情况")
    public void findCurrentUserItemPrivTest(){
        List<AeaItemPriv> currentUserItemPriv = aeaItemPrivService.findCurrentUserItemPriv("29873d61-b481-4769-ad5d-37b7a1a24530");
        System.out.println(JSON.toJSONString(currentUserItemPriv));
    }
}
