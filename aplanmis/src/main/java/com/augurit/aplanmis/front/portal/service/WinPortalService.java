package com.augurit.aplanmis.front.portal.service;

import com.augurit.agcloud.bpm.common.domain.ActStoView;
import com.augurit.agcloud.bpm.common.mapper.ActStoViewMapper;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowUserMapper;
import com.augurit.aplanmis.common.mapper.ConditionalQueryMapper;
import com.augurit.aplanmis.common.mapper.WinPortalMapper;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryAeaProjInfo;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.portal.vo.ApplyCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangwn on 2019/9/9.
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class WinPortalService {

    @Autowired
    private WinPortalMapper winPortalMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Autowired
    private ActStoViewMapper actStoViewMapper;
    @Autowired
    private AeaServiceWindowUserMapper aeaServiceWindowUserMapper;
    @Autowired
    private ConditionalQueryMapper conditionalQueryMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    public ApplyCountVo countApply() throws Exception{
        String currentOrgId = SecurityContext.getCurrentOrgId();
        String currentUserId = SecurityContext.getCurrentUserId();
        String loginName = SecurityContext.getCurrentUserName();
        List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(currentUserId);
        if (list.size() == 0) {
            throw new RuntimeException("当前用户没有绑定所在服务窗口");
        }
        //多个窗口取第一个
        String windowId = list.get(0).getWindowId();
        //网上待预审数
        ConditionalQueryRequest query1 = new ConditionalQueryRequest();
        query1.setEntrust(true);
        query1.setWindowId(windowId);
        query1.setCurrentOrgId(currentOrgId);
        query1.setLoginName(loginName);
        int wangShangDaiYuShen = conditionalQueryMapper.listPreliminaryTasks(query1).size();
        //待补全申报数
        int daiBuQuanShenBao = conditionalQueryMapper.listNeedCompletedApply(query1).size();
        //补全待确认申报数
        int buQuanDaiQueRenShenBao = conditionalQueryMapper.listNeedConfirmCompletedApply(query1).size();
        //不予受理数
        int buYuShouLiShenBao = conditionalQueryMapper.listDismissedApply(query1).size();
        //申报时限预警数
        ConditionalQueryRequest query2 = new ConditionalQueryRequest();
        query2.setEntrust(true);
        query2.setWindowId(windowId);
        query2.setIsConcluding("0");
        query2.setInstState("2");
        query2.setCurrentOrgId(currentOrgId);
        int shenBaoShiXianYuJing = conditionalQueryMapper.listApplyinfo(query2).size();
        //申报时限逾期数
        query2.setInstState("3");
        int shenBaoShiXianYuQi = conditionalQueryMapper.listApplyinfo(query2).size();
        //项目数
        ConditionalQueryAeaProjInfo conditionalQueryAeaProjInfo = new ConditionalQueryAeaProjInfo();
        conditionalQueryAeaProjInfo.setRootOrgId(currentOrgId);
        conditionalQueryAeaProjInfo.setUserId(currentUserId);
        int xiangMu = aeaProjInfoMapper.conditionalQueryAeaProjInfo(conditionalQueryAeaProjInfo).size();
        //代办任务数
        int daiBanRenWu = conditionalQueryMapper.listWaitDoTasks(query1).size();
        //已办任务数
        int yiBanRenWu =  conditionalQueryMapper.listDoneTasks(query1).size();

        ApplyCountVo vo = new ApplyCountVo();
        vo.setWangShangDaiYuShen(wangShangDaiYuShen);
        vo.setDaiBuQuanShenBao(daiBuQuanShenBao);
        vo.setBuQuanDaiQueRenShenBao(buQuanDaiQueRenShenBao);
        vo.setBuYuShouLiShenBao(buYuShouLiShenBao);
        vo.setShenBaoShiXianYuJing(shenBaoShiXianYuJing);
        vo.setShenBaoShiXianYuQi(shenBaoShiXianYuQi);
        vo.setXiangMu(xiangMu);
        vo.setDaiBanRenWu(daiBanRenWu);
        vo.setYiBanRenWu(yiBanRenWu);
        return vo;
    }

    public ActStoView getViewByContent(String content) throws Exception {
        ActStoView stoView = new ActStoView();
        stoView.setViewComment(content);
        stoView.setIsAppView("1");
        stoView.setIsActive("1");
        List<ActStoView> actStoViews = actStoViewMapper.listActStoView(stoView);
        if (actStoViews.size() == 0) {
            throw new Exception("查不到view视图");
        }
        return actStoViews.get(0);
    }

    public List<BscDicCodeItem> getDicViewCode(String typeCode){
        List<BscDicCodeItem> items = bscDicCodeMapper.getActiveItemsByTypeCode(typeCode, SecurityContext.getCurrentOrgId());
        return items;
    }
}
