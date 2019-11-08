package com.augurit.aplanmis.common.service.service;

import base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/30
 */
@DisplayName("窗口阶段接口测试")
public class AeaServiceWindowStageServiceTest extends BaseTest {
    
    @Autowired
    AeaServiceWindowStageService aeaServiceWindowStageService;

    @Test
    @DisplayName("查询当前窗口人员可办理阶段")
    public void findCurrentUserWindowStageTest() {
        List<AeaParStage> list = aeaServiceWindowStageService.findCurrentUserWindowStage();
        Assert.notEmpty(list,"未查询到窗口人员可办理阶段");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询当前窗口人员可办理阶段")
    public void findCurrentUserWindowStageByThemeIdTest() {
        List<AeaParStage> list = null;
        try {
            list = aeaServiceWindowStageService.findCurrentUserWindowStageByThemeId("22927ca3-1f4f-4b11-a016-879e0c073957");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.notEmpty(list,"未查询到窗口人员可办理阶段");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询当前窗口人员是否可办理阶段")
    public void hasAuthOnCurrentUserTest() {
        boolean hasAuth = aeaServiceWindowStageService.hasAuthOnCurrentUser("00ed07a3-31c5-4515-b313-e141b8d7b03d");
        System.out.println(JSON.toJSONString(hasAuth));
    }

    @Test
    @DisplayName("查询窗口阶段")
    public void findWindowStageByWindowIdTest() {
        List<AeaParStage> list = aeaServiceWindowStageService.findWindowStageByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb");
        Assert.notEmpty(list,"未查询窗口阶段");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页查询窗口阶段")
    public void listWindowStageByWindowIdTest() {
        Page page = new Page(1,10);
        PageInfo<AeaParStage> pageInfo = aeaServiceWindowStageService.listWindowStageByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口阶段");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("搜索窗口阶段")
    public void findWindowStageByKeywordTest() {
        List<AeaParStage> list = aeaServiceWindowStageService.findWindowStageByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","竣工验收");
        Assert.notEmpty(list,"未查询窗口阶段");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页搜索窗口阶段")
    public void listWindowStageByKeywordTest() {
        Page page = new Page(1,10);
        PageInfo<AeaParStage> pageInfo = aeaServiceWindowStageService.listWindowStageByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","竣工验收",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口阶段");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("查询窗口可办理阶段")
    public void findActiveWindowStageByWindowIdTest() {
        List<AeaParStage> list = aeaServiceWindowStageService.findActiveWindowStageByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb");
        Assert.notEmpty(list,"未查询窗口可办理阶段");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页查询窗口可办理阶段")
    public void listActiveWindowStageByWindowIdTest() {
        Page page = new Page(1,10);
        PageInfo<AeaParStage> pageInfo = aeaServiceWindowStageService.listActiveWindowStageByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口可办理阶段");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("搜索窗口可办理阶段")
    public void findActiveWindowStageByKeywordTest() {
        List<AeaParStage> list = aeaServiceWindowStageService.findActiveWindowStageByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","竣工验收");
        Assert.notEmpty(list,"未查询窗口可办理阶段");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页搜索窗口可办理阶段")
    public void listActiveWindowStageByKeywordTest() {
        Page page = new Page(1,10);
        PageInfo<AeaParStage> pageInfo = aeaServiceWindowStageService.listActiveWindowStageByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","竣工验收",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口可办理阶段");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }
}
