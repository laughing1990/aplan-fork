package com.augurit.aplanmis.common.service;

import base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/9
 */
@DisplayName("项目接口测试")
public class AeaProjInfoServiceTest extends BaseTest {
    @Autowired
    AeaProjInfoService projInfoService;

    @Test
    @DisplayName("查询项目信息")
    public void testGetAeaProjInfoByGcbm() {
        AeaProjInfo aeaProjInfoByGcbm = projInfoService.getAeaProjInfoByGcbm("ZBM-R-20190531-65804711");
        Assert.notNull(aeaProjInfoByGcbm, "未查询到项目");
        String proj = JSON.toJSONString(aeaProjInfoByGcbm);
        System.out.println(proj);
    }

    @Test
    @DisplayName("查询项目信息")
    public void testGetAeaProjInfoByProjInfoId() {
        AeaProjInfo aeaProjInfoByProjInfoId = projInfoService.getAeaProjInfoByProjInfoId("050de049-eb7e-459a-9340-9d4fb8120224");
        Assert.notNull(aeaProjInfoByProjInfoId, "未查询到项目");
        String proj = JSON.toJSONString(aeaProjInfoByProjInfoId);
        System.out.println(proj);
    }

    @Test
    @DisplayName("查询申报项目")
    public void testFindApplyProj() {
        List<AeaProjInfo> list = projInfoService.findApplyProj("6dc304d6-0938-47cc-a7bc-b465c90da5bb");
        Assert.notEmpty(list, "未查询到项目");
        String proj = JSON.toJSONString(list);
        System.out.println(proj);
    }

    @Test
    @DisplayName("查询子项目列表")
    public void testFindChildProj() {
        List<AeaProjInfo> list = projInfoService.findChildProj("0f909d3c-2dfe-4ca8-944f-29fe151024e1");
        Assert.notEmpty(list, "未查询到项目");
        String proj = JSON.toJSONString(list);
        System.out.println(proj);
    }

    @Test
    @DisplayName("查询父项目")
    public void testFindParentProj() {
        AeaProjInfo aeaProjInfo = projInfoService.findParentProj("7c0f6b6d-fea7-4fb4-928e-4d169987075e");
        Assert.notNull(aeaProjInfo, "未查询到项目");
        String proj = JSON.toJSONString(aeaProjInfo);
        System.out.println(proj);
    }

    @Test
    @DisplayName("根据申请人查询申报项目")
    public void testFindAeaProjInfoByApplyLinkmanInfoId() {
        List<AeaProjInfo> list = projInfoService.findAeaProjInfoByApplyLinkmanInfoId("72c39dfc-ecf7-467b-b8af-dbdd2814e6df");
        Assert.notEmpty(list, "未查询到项目");
        String proj = JSON.toJSONString(list);
        System.out.println(proj);
    }

    @Test
    @DisplayName("根据联系人查询项目")
    public void testFindAeaProjInfoByLinkmanInfoId() {
        List<AeaProjInfo> list = projInfoService.findRootAeaProjInfoByLinkmanInfoId("72c39dfc-ecf7-467b-b8af-dbdd2814e6df", null);
        Assert.notEmpty(list, "未查询到项目");
        String proj = JSON.toJSONString(list);
        System.out.println(proj);
    }

    @Test
    @DisplayName("新增项目")
    public void testInsertAeaProjInfo() {
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        //重复：ZBM-R-20190531-65804711
        aeaProjInfo.setGcbm("2019-000001-01-02-000001");
        aeaProjInfo.setProjName("新增项目测试");
        projInfoService.insertAeaProjInfo(aeaProjInfo);
        System.out.println(aeaProjInfo.getProjInfoId());
    }

    @Test
    @DisplayName("修改项目")
    public void testUpdateAeaProjInfo() {
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        aeaProjInfo.setProjInfoId("fa733d7a-e74e-4bc5-9663-74c57bdb5b9d");
        aeaProjInfo.setProjName("修改项目测试");
        projInfoService.updateAeaProjInfo(aeaProjInfo);
    }

    @Test
    @DisplayName("删除项目")
    public void testDeleteAeaProjInfo() {
        projInfoService.deleteAeaProjInfo("574fe123-d538-46f1-a613-58c0b7b7a50d", "fa733d7a-e74e-4bc5-9663-74c57bdb5b9d");
    }

    @Test
    @DisplayName("通过单位id获取项目信息")
    public void findAeaProjInfoByUnitInfoId() {
        List<AeaProjInfo> list = projInfoService.findRootAeaProjInfoByUnitInfoId("084fe528-da62-48c4-8ef7-aeba45d636cf", null);
        Assert.notEmpty(list, "未查询到项目");
        String proj = JSON.toJSONString(list);
        System.out.println(proj);
    }
}
