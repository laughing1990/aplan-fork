package com.augurit.aplanmis.common.service;

import base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.common.constants.AeaUnitConstants;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yinlf
 * @Date 2019/7/10
 */
@DisplayName("单位接口测试")
public class AeaUnitInfoServiceTest extends BaseTest {
    @Autowired
    AeaUnitInfoService aeaUnitInfoService;

    @Test
    @DisplayName("查询项目信息")
    public void testInsertAeaUnitInfo(){
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setApplicant("无畏剑圣");
        aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
    }

    @Test
    @DisplayName("更新单位信息")
    public void testUpdateAeaUnitInfo(){
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setUnitInfoId("0b1c323b-7247-4762-a72d-774fc4e07e8f");
        aeaUnitInfo.setApplicant("无畏剑圣");
        aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
    }

    @Test
    @DisplayName("单位ID查询单位信息")
    public void testGetAeaUnitInfo(){
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoService.getAeaUnitInfoByUnitInfoId("0b1c323b-7247-4762-a72d-774fc4e07e8f");
        Assert.notNull(aeaUnitInfo,"未查询到单位信息");
        System.out.println(JSON.toJSONString(aeaUnitInfo));
    }

    @Test
    @DisplayName("删除单位信息")
    public void testDeleteAeaUnitInfo(){
        aeaUnitInfoService.deleteAeaUnitInfo("0b1c323b-7247-4762-a72d-774fc4e07e8f");
    }


    @Test
    @DisplayName("项目添加建设单位")
    public void testInsertOwnerUnitProj(){
        aeaUnitInfoService.insertOwnerUnitProj("0fba1433-1bae-4849-96ae-fc8b8489d1c6","019a7d90-cfc7-4e19-8da1-23c5ee587ccd","0d4eb038-74f4-4366-9e97-362462a53c1f");
    }

    @Test
    @DisplayName("多项目添加建设单位")
    public void testInsertOwnerUnitProj2(){
        Map<String, List<String>> unitProj = new HashMap<>();
        unitProj.put("0fba1433-1bae-4849-96ae-fc8b8489d1c6", Arrays.asList(new String[]{"019a7d90-cfc7-4e19-8da1-23c5ee587ccd","0d4eb038-74f4-4366-9e97-362462a53c1f"}));
        unitProj.put("1b0e26d5-5485-407f-a6ba-0b1be3fff4ed", Arrays.asList(new String[]{"019a7d90-cfc7-4e19-8da1-23c5ee587ccd"}));
        aeaUnitInfoService.insertOwnerUnitProj(unitProj);
    }

    @Test
    @DisplayName("删除项目下单位")
    public void testDeleteUnitProj(){
        aeaUnitInfoService.deleteUnitProj("87e25032-5f52-409d-b3dd-8a6ce49f7994", AeaUnitConstants.IS_OWNER_TRUE,"cbf8497c-689f-49eb-bef1-838007c69de0");
    }

    @Test
    @DisplayName("新增项目申报建设单位")
    public void testInsertApplyOwnerUnitProj(){
        aeaUnitInfoService.insertApplyOwnerUnitProj("0dbde0bd-326c-4ac6-89b3-2a02c6a40c3b","0550fd07-b133-41cd-8139-96662d46aa12","1aa544cd-7623-40c2-8f6e-7b0aa82d6f21");
    }

    @Test
    @DisplayName("新增项目申报建设单位")
    public void testInsertApplyOwnerUnitProj2(){
        Map<String, List<String>> unitProj = new HashMap<>();
        unitProj.put("0fba1433-1bae-4849-96ae-fc8b8489d1c6", Arrays.asList(new String[]{"019a7d90-cfc7-4e19-8da1-23c5ee587ccd","0d4eb038-74f4-4366-9e97-362462a53c1f"}));
        unitProj.put("1b0e26d5-5485-407f-a6ba-0b1be3fff4ed", Arrays.asList(new String[]{"019a7d90-cfc7-4e19-8da1-23c5ee587ccd"}));
        aeaUnitInfoService.insertApplyOwnerUnitProj("0dbde0bd-326c-4ac6-89b3-2a02c6a40c3b",unitProj);
    }

    @Test
    @DisplayName("新增项目申报经办单位")
    public void testInsertApplyNonOwnerUnitProj(){
        aeaUnitInfoService.insertApplyNonOwnerUnitProj("0dbde0bd-326c-4ac6-89b3-2a02c6a40c3b","0550fd07-b133-41cd-8139-96662d46aa12","1aa544cd-7623-40c2-8f6e-7b0aa82d6f21");
    }

    @Test
    @DisplayName("新增项目申报经办单位")
    public void testInsertApplyNonOwnerUnitProj2(){
        aeaUnitInfoService.insertApplyNonOwnerUnitProj("0dbde0bd-326c-4ac6-89b3-2a02c6a40c3b",new String[]{"0fba1433-1bae-4849-96ae-fc8b8489d1c6","1b0e26d5-5485-407f-a6ba-0b1be3fff4ed"},"1aa544cd-7623-40c2-8f6e-7b0aa82d6f21");
    }

    @Test
    @DisplayName("查询项目申报单位")
    public void testFindApplyUnitProj(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findApplyUnitProj("6dc304d6-0938-47cc-a7bc-b465c90da5bb", "87e25032-5f52-409d-b3dd-8a6ce49f7994");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询项目申报建设单位")
    public void testFindApplyOwnerUnitProj(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findApplyOwnerUnitProj("6dc304d6-0938-47cc-a7bc-b465c90da5bb", "87e25032-5f52-409d-b3dd-8a6ce49f7994");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询项目申报经办单位")
    public void testFindApplyNonOwnerUnitProj(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findApplyNonOwnerUnitProj("6dc304d6-0938-47cc-a7bc-b465c90da5bb", "87e25032-5f52-409d-b3dd-8a6ce49f7994");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询项目申报经办单位")
    public void testFindUnitProjByProjInfoIdAndType(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findUnitProjByProjInfoIdAndType("87e25032-5f52-409d-b3dd-8a6ce49f7994", UnitType.CONSTRUCTION_UNIT.getValue());
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询项目的所有单位")
    public void testFindAllProjUnit(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findAllProjUnit("87e25032-5f52-409d-b3dd-8a6ce49f7994");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询项目建设单位")
    public void testFindOwerUnitProj(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findOwerUnitProj("87e25032-5f52-409d-b3dd-8a6ce49f7994");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询项目非建设单位")
    public void testFindNonOwerUnitProj(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findNonOwerUnitProj("87e25032-5f52-409d-b3dd-8a6ce49f7994");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询子单位列表")
    public void testFindChildUnit(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findChildUnit("bdbc8cd4-842a-4bd7-8dc1-174148762336");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("根据登录用户名查询单位信息")
    public void testGetAeaUnitInfoByLoginName(){
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoService.getAeaUnitInfoByLoginName("yaodoudou");
        Assert.notNull(aeaUnitInfo,"未查询到单位信息");
        System.out.println(JSON.toJSONString(aeaUnitInfo));
    }

    @Test
    @DisplayName("搜索单位信息")
    public void testFindAeaUnitInfoByKeyword(){
        List<AeaUnitInfo> list = aeaUnitInfoService.findAeaUnitInfoByKeyword("恒荣房地产");
        Assert.notEmpty(list,"未查询到单位信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("搜索单位信息")
    public void testListAeaUnitInfoByKeyword(){
        Page page = new Page(1,10);
        PageInfo<AeaUnitInfo> pageInfo = aeaUnitInfoService.listAeaUnitInfoByKeyword("恒荣房地产",page);
        Assert.notEmpty(pageInfo.getList(),"未查询到单位信息");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("搜索单位信息")
    public void testListAeaUnitInfo(){
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setKeyword("恒荣房地产");
        Page page = new Page(1,10);
        PageInfo<AeaUnitInfo> pageInfo = aeaUnitInfoService.listAeaUnitInfo(aeaUnitInfo,page);
        Assert.notEmpty(pageInfo.getList(),"未查询到单位信息");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }
}
