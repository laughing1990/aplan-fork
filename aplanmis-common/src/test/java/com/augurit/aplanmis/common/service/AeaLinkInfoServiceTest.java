package com.augurit.aplanmis.common.service;

import base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.common.constants.AeaLinkmanConstants;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/10
 */
@DisplayName("联系人接口测试")
public class AeaLinkInfoServiceTest extends BaseTest{
    @Autowired
    AeaLinkmanInfoService aeaLinkmanInfoService;

    @Test
    @DisplayName("新建联系人")
    public void testInsertAeaLinkmanInfo(){
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        aeaLinkmanInfo.setLinkmanName("陈泽浩");
        aeaLinkmanInfo.setLinkmanType(AeaLinkmanConstants.LINKMAN_TYPE_PERSON);
        aeaLinkmanInfo.setLinkmanMobilePhone("1564789652");
        aeaLinkmanInfo.setCreater("大浩浩");
        aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
    }

    @Test
    @DisplayName("为项目添加联系人")
    public void testInsertProjLinkman(){
        aeaLinkmanInfoService.insertProjLinkman("0259eca7-d01e-432b-b2b0-473857201ecb","061dd74e-2007-445d-9819-c9f00276ca62","00dba4b9-e986-47c1-9cf1-3e2b3d459980");
    }

    @Test
    @DisplayName("新增项目申请人")
    public void testInsertApplyProjLinkman(){
        aeaLinkmanInfoService.insertApplyProjLinkman("0259eca7-d01e-432b-b2b0-473857201ecb","061dd74e-2007-445d-9819-c9f00276ca62","00dba4b9-e986-47c1-9cf1-3e2b3d459980");
    }

    @Test
    @DisplayName("为多个项目新增联系人和申请人")
    public void testInsertApplyAndLinkProjLinkman(){
        aeaLinkmanInfoService.insertApplyAndLinkProjLinkman("0259eca7-d01e-432b-b2b0-473857201ecb",new String[]{"061dd74e-2007-445d-9819-c9f00276ca62"},"00dba4b9-e986-47c1-9cf1-3e2b3d459980","00dba4b9-e986-47c1-9cf1-3e2b3d459980");
    }

    @Test
    @DisplayName("为单位添加联系人")
    public void testInsertUnitLinkman(){
        aeaLinkmanInfoService.insertUnitLinkman("0324b597-4f51-427e-b2d2-a00a9f3c8a69","092de494-d810-439b-81f6-f46cb9e22bec");
    }

    @Test
    @DisplayName("为单位添加联系人")
    public void testUpdateAeaLinkmanInfo(){
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        aeaLinkmanInfo.setLinkmanInfoId("092de494-d810-439b-81f6-f46cb9e22bec");
        aeaLinkmanInfo.setLinkmanName("陈泽浩");
        aeaLinkmanInfo.setLinkmanType(AeaLinkmanConstants.LINKMAN_TYPE_PERSON);
        aeaLinkmanInfo.setLinkmanMobilePhone("1564789652");
        aeaLinkmanInfo.setCreater("大浩浩");
        aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
    }

    @Test
    @DisplayName("为单位添加联系人")
    public void testDeleteAeaLinkmanInfo(){
        aeaLinkmanInfoService.deleteAeaLinkmanInfo("092de494-d810-439b-81f6-f46cb9e22bec");
    }

    @Test
    @DisplayName("删除项目下联系人")
    public void testDeleteProjLinkman(){
        aeaLinkmanInfoService.deleteProjLinkman("87e25032-5f52-409d-b3dd-8a6ce49f7994","da505768-f814-49d6-9df1-2f578c5c4967");
    }

    @Test
    @DisplayName("查询联系人信息")
    public void testGetAeaLinkmanInfoByLinkmanId(){
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId("092de494-d810-439b-81f6-f46cb9e22bec");
        Assert.notNull(aeaLinkmanInfo,"未查询到联系人信息");
        System.out.println(JSON.toJSONString(aeaLinkmanInfo));
    }

    @Test
    @DisplayName("删除单位下联系人")
    public void testDeleteUnitLinkman(){
        aeaLinkmanInfoService.deleteUnitLinkman("c1a21358-0fc4-4614-89b6-65d13ae298e1","f0e3fc32-b0ad-4529-87f6-24b60a2a8863");
    }

    @Test
    @DisplayName("查询项目申请人")
    public void testGetApplyProjLinkman(){
        AeaLinkmanInfo  aeaLinkmanInfo = aeaLinkmanInfoService.getApplyProjLinkman("a316e0dd-fad5-484b-ae03-f3b292fde81f","87e25032-5f52-409d-b3dd-8a6ce49f7994");
        Assert.notNull(aeaLinkmanInfo,"未查询到联系人信息");
        System.out.println(JSON.toJSONString(aeaLinkmanInfo));
    }

    @Test
    @DisplayName("查询单位所有联系人")
    public void testFindAllUnitLinkman(){
        List<AeaLinkmanInfo> allUnitLinkman = aeaLinkmanInfoService.findAllUnitLinkman("4028869e682cbe89016836996741120e");
        Assert.notEmpty(allUnitLinkman,"未查询到联系人信息");
        System.out.println(JSON.toJSONString(allUnitLinkman));
    }

    @Test
    @DisplayName("登录名查询联系人")
    public void testGetAeaLinkmanInfoByLoginName(){
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLoginName("唐山中康物流有限公司");
        Assert.notNull(aeaLinkmanInfo,"未查询到联系人信息");
        System.out.println(JSON.toJSONString(aeaLinkmanInfo));
    }

    @Test
    @DisplayName("搜索联系人")
    public void testFindLinkmanInfoByKeyword(){
        List<AeaLinkmanInfo> list = aeaLinkmanInfoService.findLinkmanInfoByKeyword("泽浩");
        Assert.notEmpty(list,"未查询到联系人信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("多条件分页查询联系人")
    public void testFindLinkmanInfo(){
        AeaLinkmanInfo aeaLinkmanInfoCondition = new AeaLinkmanInfo();
        aeaLinkmanInfoCondition.setKeyword("泽浩");
        List<AeaLinkmanInfo> list = aeaLinkmanInfoService.findLinkmanInfo(aeaLinkmanInfoCondition);
        Assert.notEmpty(list,"未查询到联系人信息");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("多条件分页查询联系人")
    public void testListLinkmanInfo(){
        AeaLinkmanInfo aeaLinkmanInfoCondition = new AeaLinkmanInfo();
        aeaLinkmanInfoCondition.setKeyword("泽浩");
        aeaLinkmanInfoCondition.setLinkmanMobilePhone("13632662142");
        Page page = new Page(1,10);
        PageInfo<AeaLinkmanInfo> pageInfo = aeaLinkmanInfoService.listLinkmanInfo(aeaLinkmanInfoCondition,page);
        Assert.notEmpty(pageInfo.getList(),"未查询到联系人信息");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }
}
