package com.augurit.aplanmis.common.check;

import base.BaseTest;
import com.augurit.aplanmis.common.check.exception.ItemItemCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("事项的前置事项检查")
class ItemItemCheckerTest extends BaseTest {

    @Autowired
    private ItemItemChecker itemItemChecker;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Test
    void doCheck() {
        String itemVerId = "0f29df5b-970b-4667-8c29-906a1a117bc0";
        String rootOrgId = "0368948a-1cdf-4bf8-a828-71d796ba89f6";
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, rootOrgId);

        CheckerInfo checkerInfo = new CheckerInfo();
        checkerInfo.setProjInfoId("2f7d2454-6b9d-45fe-8afe-5f84ecf40ae0");
        try {
            itemItemChecker.doCheck(aeaItemBasic, new CheckerContext(checkerInfo));
        } catch (ItemItemCheckException e) {
            e.printStackTrace();
        }
    }
}