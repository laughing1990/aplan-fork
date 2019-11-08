package com.augurit.aplanmis.common.service.service;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/8/2
 */
@DisplayName("窗口事项接口测试")
public class AeaServiceWindowServiceTest  extends BaseTest {

    @Autowired
    AeaServiceWindowService windowService;

    @Test
    @DisplayName("查询所有窗口列表")
    public void findAllAeaServiceWindowTest() {
        List<AeaServiceWindow> list = windowService.findAllAeaServiceWindow();
        Assert.notEmpty(list,"未查询到窗口");
    }

    @Test
    @DisplayName("分页查询所有窗口列表")
    public void listAllAeaServiceWindowTest() {
        Page page = new Page(1,10);
        PageInfo<AeaServiceWindow> info = windowService.listAllAeaServiceWindow(page);
        Assert.notEmpty(info.getList(),"未查询到窗口");
    }

    @Test
    @DisplayName("查询区域内所有窗口列表")
    public void findAeaServiceWindowByRegionIdTest() {
        List<AeaServiceWindow> list = windowService.findAeaServiceWindowByRegionId("64");
        Assert.notEmpty(list,"未查询到窗口");
    }

    @Test
    @DisplayName("分页查询区域内所有窗口列表")
    public void listAeaServiceWindowByRegionIdTest() {
        Page page = new Page(1,10);
        PageInfo<AeaServiceWindow> info = windowService.listAeaServiceWindowByRegionId("64",page);
        Assert.notEmpty(info.getList(),"未查询到窗口");
    }

    @Test
    @DisplayName("查询事项可办理窗口")
    public void findWindowByItemVerIdTest() {
        List<AeaServiceWindow> list = windowService.findWindowByItemVerId("78a3017f-57a8-4f4e-a2fa-6ae2a77ba59d");
        Assert.notEmpty(list,"未查询到窗口");
    }

    @Test
    @DisplayName("获取当前用户所在窗口")
    public void getCurrentUserWindowTest() {
        AeaServiceWindow aeaServiceWindow = windowService.getCurrentUserWindow();
        Assert.notNull(aeaServiceWindow,"未查询到窗口");
    }

    @Test
    @DisplayName("窗口搜索")
    public void getCurrentUserWindow() {
        AeaServiceWindow serviceWindow = new AeaServiceWindow();
        serviceWindow.setKeyword("唐山");
        List<AeaServiceWindow> list = windowService.findAeaServiceWindow(serviceWindow);
        Assert.notEmpty(list,"未查询到窗口");;
    }

    @Test
    @DisplayName("分页搜索")
    public void listAeaServiceWindowTest() {
        Page page = new Page(1,10);
        AeaServiceWindow serviceWindow = new AeaServiceWindow();
        serviceWindow.setKeyword("唐山");
        PageInfo<AeaServiceWindow> info = windowService.listAeaServiceWindow(serviceWindow,page);
        Assert.notEmpty(info.getList(),"未查询到窗口");
    }


}
