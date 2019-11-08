package com.augurit.aplanmis.common.service.service;

import base.BaseTest;
import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/29
 */
@DisplayName("窗口人员接口测试")
public class AeaServiceWindowUserServiceTest extends BaseTest {

    @Autowired
    AeaServiceWindowUserService aeaServiceWindowUserService;

    @Test
    @DisplayName("查询窗口人员")
    public void findWindowUserByWindowIdTest() {
        List<OpuOmUser> list = aeaServiceWindowUserService.findWindowUserByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb");
        Assert.notEmpty(list,"未查询窗口人员");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页查询窗口人员")
    public void listWindowUserByWindowIdTest() {
        Page page = new Page(1,10);
        PageInfo<OpuOmUser> pageInfo = aeaServiceWindowUserService.listWindowUserByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口人员");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("搜索窗口人员")
    public void findWindowUserByKeywordTest() {
        List<OpuOmUser> list = aeaServiceWindowUserService.findWindowUserByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","ckry");
        Assert.notEmpty(list,"未查询窗口人员");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页搜索窗口人员")
    public void listWindowUserByKeywordTest() {
        Page page = new Page(1,10);
        PageInfo<OpuOmUser> pageInfo = aeaServiceWindowUserService.listWindowUserByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","ckry",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口人员");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("查询窗口有效人员")
    public void findActiveWindowUserByWindowIdTest() {
        List<OpuOmUser> list = aeaServiceWindowUserService.findActiveWindowUserByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb");
        Assert.notEmpty(list,"未查询窗口可办理人员");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页查询窗口有效人员")
    public void listActiveWindowUserByWindowIdTest() {
        Page page = new Page(1,10);
        PageInfo<OpuOmUser> pageInfo = aeaServiceWindowUserService.listActiveWindowUserByWindowId("2d370336-1896-4c36-8c3a-d79bbee502cb",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口可办理人员");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }

    @Test
    @DisplayName("搜索窗口有效人员")
    public void findActiveWindowUserByKeywordTest() {
        List<OpuOmUser> list = aeaServiceWindowUserService.findActiveWindowUserByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","ckry");
        Assert.notEmpty(list,"未查询窗口可办理人员");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("分页搜索窗口有效人员")
    public void listActiveWindowUserByKeywordTest() {
        Page page = new Page(1,10);
        PageInfo<OpuOmUser> pageInfo = aeaServiceWindowUserService.listActiveWindowUserByKeyword("2d370336-1896-4c36-8c3a-d79bbee502cb","ckry",page);
        Assert.notEmpty(pageInfo.getList(),"未查询窗口可办理人员");
        System.out.println(JSON.toJSONString(pageInfo.getList()));
    }
}
