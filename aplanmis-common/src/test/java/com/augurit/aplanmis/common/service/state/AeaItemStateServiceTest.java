package com.augurit.aplanmis.common.service.state;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaItemState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("单事项情形测试")
class AeaItemStateServiceTest extends BaseTest {
    @Autowired
    private AeaItemStateService aeaItemStateService;

    @Test
    void listAeaItemStateByItemVerId() throws Exception {
        List<AeaItemState> aeaItemStates = aeaItemStateService.listAeaItemStateByItemVerId("dcfdb26d-1980-43ad-9a5c-ea9e1c0a70d1",null);
        System.out.println(aeaItemStates.size());
    }

    @Test
    void listRootAeaItemStateByItemVerId() throws Exception {
       // List<AeaItemState> aeaItemStates = aeaItemStateService.listRootAeaItemStateByItemVerId("dcfdb26d-1980-43ad-9a5c-ea9e1c0a70d1",null);
       // System.out.println(aeaItemStates.size());
    }

    @Test
    void listAeaItemStateByParentItemStateId() throws Exception {
       // List<AeaItemState> aeaItemStates = aeaItemStateService.listAeaItemStateByParentItemStateId("f95b1c7a-2bfa-4bb0-b1c9-195f21a3c4bc");
       // System.out.println(aeaItemStates.size());
    }

    @Test
    void listTreeAeaItemStateByItemVerId() throws Exception {
        List<AeaItemState> aeaItemStates = aeaItemStateService.listTreeAeaItemStateByItemVerId("042730ee-7e56-4880-94d0-6ea238f91f4b",null);
        System.out.println(aeaItemStates.size());
    }

    @Test
    void listAeaItemStateById() throws Exception {
        List<AeaItemState> aeaItemStates = aeaItemStateService.listAeaItemStateById("0084c35a-e38b-4980-b309-4a652742382f");
        System.out.println(aeaItemStates.size());
    }

    @Test
    void listAeaItemStateByIteminstId() throws Exception {
        List<AeaItemState> aeaItemStates = aeaItemStateService.listAeaItemStateByIteminstId("02b2791c-e464-4008-919f-617923a48be3");
        System.out.println(aeaItemStates.size());
    }

    @Test
    void deleteAeaItemState() throws Exception {
        int i = aeaItemStateService.deleteAeaItemState("001dacec-469f-4294-a6a0-16d6c00518a6");
        Assert.isTrue(i == 1, "删除一条数据");
    }
}