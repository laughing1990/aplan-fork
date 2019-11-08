package com.augurit.aplanmis.common.service.service;

import base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/26
 */
@DisplayName("窗口事项接口测试")
public class AeaServiceWindowItemServiceTest extends BaseTest {

    @Autowired
    AeaServiceWindowItemService aeaServiceWindowItemService;

    @Test
    @DisplayName("查询当前窗口人员可办理事项")
    public void findCurrentUserWindowItemTest() {
        List<AeaItemBasic> list = aeaServiceWindowItemService.findCurrentUserWindowItem();
        Assert.notEmpty(list,"未查询到窗口人员可办理事项");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询当前窗口人员是否可办理事项")
    public void hasAuthOnCurrentUserTest() {
        boolean hasAuth = aeaServiceWindowItemService.hasAuthOnCurrentUser("78a3017f-57a8-4f4e-a2fa-6ae2a77ba59d");
        System.out.println(JSON.toJSONString(hasAuth));
    }

    @Test
    @DisplayName("查询窗口事项")
    public void findWindowItemByWindowIdTest() {
        List<AeaItemBasic> list = aeaServiceWindowItemService.findWindowItemByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb");
        Assert.notEmpty(list,"未查询窗口事项");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页查询窗口事项")
    public void listWindowItemByWindowIdTest() {
        Page page = new Page(1,10);
        PageInfo<AeaItemBasic> pageInfo = aeaServiceWindowItemService.listWindowItemByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口事项");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("搜索窗口事项")
    public void findWindowItemByKeywordTest() {
        List<AeaItemBasic> list = aeaServiceWindowItemService.findWindowItemByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","施工");
        Assert.notEmpty(list,"未查询窗口事项");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页搜索窗口事项")
    public void listWindowItemByKeywordTest() {
        Page page = new Page(1,10);
        PageInfo<AeaItemBasic> pageInfo = aeaServiceWindowItemService.listWindowItemByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","施工",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口事项");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("查询窗口可办理事项")
    public void findActiveWindowItemByWindowIdTest() {
        List<AeaItemBasic> list = aeaServiceWindowItemService.findActiveWindowItemByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb");
        Assert.notEmpty(list,"未查询窗口可办理事项");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页查询窗口可办理事项")
    public void listActiveWindowItemByWindowIdTest() {
        Page page = new Page(1,10);
        PageInfo<AeaItemBasic> pageInfo = aeaServiceWindowItemService.listActiveWindowItemByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口可办理事项");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("搜索窗口可办理事项")
    public void findActiveWindowItemByKeywordTest() {
        List<AeaItemBasic> list = aeaServiceWindowItemService.findActiveWindowItemByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","施工");
        Assert.notEmpty(list,"未查询窗口可办理事项");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页搜索窗口可办理事项")
    public void listActiveWindowItemByKeywordTest() {
        Page page = new Page(1,10);
        PageInfo<AeaItemBasic> pageInfo = aeaServiceWindowItemService.listActiveWindowItemByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","施工",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口可办理事项");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

}
