package com.augurit.aplanmis.front.solicit.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.solicit.AeaHiSolicitVo;
import com.augurit.aplanmis.common.vo.solicit.QueryCondVo;
import com.augurit.aplanmis.front.solicit.service.RestAeaHiSolicitService;
import com.augurit.aplanmis.front.solicit.service.SolicitCodeService;
import com.augurit.aplanmis.common.constants.SolicitBusTypeEnum;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author:chendx
 * @date: 2019-12-23
 * @time: 13:55
 */
@Service
@Transactional
public class RestAeaHiSolicitServiceImpl implements RestAeaHiSolicitService{

    @Autowired
    private AeaHiSolicitDetailMapper aeaHiSolicitDetailMapper;

    @Autowired
    private AeaHiSolicitMapper aeaHiSolicitMapper;

    @Autowired
    private AeaHiSolicitDetailUserMapper aeaHiSolicitDetailUserMapper;

    @Autowired
    private SolicitCodeService solicitCodeService;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private OpuOmOrgService opuOmOrgService;
    @Autowired
    private AeaSolicitItemUserMapper aeaSolicitItemUserMapper;
    @Autowired
    private AeaSolicitOrgUserMapper aeaSolicitOrgUserMapper;

    @Override
    public List<OpuOmOrg> listOrg(String isRoot, String parentOrgId) throws Exception {
        String topOrgId = SecurityContext.getCurrentOrgId();

        if(StringUtils.isBlank(isRoot))
            throw new RuntimeException("参数isRoot不能为空！");
        if(StringUtils.isNotBlank(isRoot)&&"0".equals(isRoot)
                &&StringUtils.isBlank(parentOrgId))
            throw new RuntimeException("非根组织，参数parentOrgId不能为空！");


        List<OpuOmOrg> list = new ArrayList<>();

        if("0".equals(isRoot)) {
            OpuOmOrg opuOmOrg = new OpuOmOrg();
            opuOmOrg.setParentOrgId(parentOrgId);
            opuOmOrg.setOrgProperty("d");
            opuOmOrg.setOrgDeleted("0");
            list = opuOmOrgService.listOpuOmOrg(opuOmOrg);
        }

        if ("1".equals(isRoot)) {
            OpuOmOrg opuOmOrg = opuOmOrgService.getOrg(topOrgId);
            list.add(opuOmOrg);
        }

        return list;
    }

    /**
     * 意见征求列表
     *
     * @param page 分页参数
     * @return List<SolicitListVo>
     */
    @Override
    public List<AeaHiSolicitVo> listSolicit(QueryCondVo condVo, Page page) throws Exception {
        this.changeOrderBySql(page);
        this.buildQueryCondvo(condVo);
        PageHelper.startPage(page);
        List<AeaHiSolicitVo> voList = aeaHiSolicitMapper.listSolicit(condVo);
        return voList;
    }

    /**
     * 创建意见征求实例
     * @param aeaHiSolicit 征求基本信息
     * @param type 征求类型，i事项征求，d部门征询
     * @param busType 征求业务类型，一次征询、意见征求、部门辅导
     * @param detailInfo 征求详细信息 格式[{\"itemId\":\"123\",\"itemVerId\":\"123\",\"orgId\":\"123\",\"orgName\":\"123\"}]"
     * @throws Exception
     */
    @Override
    public void createSolicit(AeaHiSolicit aeaHiSolicit, String type, String detailInfo,String busType) throws Exception {
        //1、先解析事项信息
        JSONArray jsonArray = JSONArray.parseArray(detailInfo);
        if(jsonArray == null || jsonArray.size() == 0){
            //事项信息为空则直接返回
            throw new RuntimeException("征求的事项或部门信息不能为空！");
        }
        String state = "1";//征求中
        String currentUserName = SecurityContext.getCurrentUserName();
        String currentUserId = SecurityContext.getCurrentUserId();
        String topOrgId = SecurityContext.getCurrentOrgId();
        String currentOrgId = null;
        String currentOrgName = null;
        List<OpuOmOrg> orgs = opuOmOrgMapper.listBelongOrgByUserId(currentUserId);
        if(orgs.size() > 0){
            currentOrgId = orgs.get(0).getOrgId();
            currentOrgName = orgs.get(0).getOrgName();
        }
        Date date = new Date();
        String solicitId = UUID.randomUUID().toString();
        //2、保存意见征求主表信息
        aeaHiSolicit.setSolicitId(solicitId);
        aeaHiSolicit.setSolicitCode(solicitCodeService.createSolicitCode());
        aeaHiSolicit.setSolicitType(type);
        aeaHiSolicit.setSolicitStartTime(date);
        aeaHiSolicit.setSolicitState(state);
        aeaHiSolicit.setInitiatorOrgId(currentOrgId);
        aeaHiSolicit.setInitiatorOrgName(currentOrgName);
        aeaHiSolicit.setInitiatorUserId(currentUserId);
        aeaHiSolicit.setInitiatorUserName(currentUserName);
        aeaHiSolicit.setCreater(currentUserName);
        aeaHiSolicit.setCreateTime(date);
        aeaHiSolicit.setRootOrgId(topOrgId);
        aeaHiSolicitMapper.insertAeaHiSolicit(aeaHiSolicit);

        //保存意见征求的事项详细信息
        for(Object temp : jsonArray){
            JSONObject jsonObject = (JSONObject) temp;
            AeaHiSolicitDetail detail = new AeaHiSolicitDetail();
            String detailId = UUID.randomUUID().toString();
            detail.setSolicitId(solicitId);
            detail.setSolicitDetailId(detailId);
            if("i".equals(type)) {
                detail.setItemId(jsonObject.getString("itemId"));
                detail.setItemVerId(jsonObject.getString("itemVerId"));
            }
            detail.setDetailOrgId(jsonObject.getString("orgId"));
            detail.setDetailOrgName(jsonObject.getString("orgName"));
            detail.setDetailDueDays(aeaHiSolicit.getSolicitDueDays());
            detail.setDetailStartTime(date);
            detail.setDetailOrgId(topOrgId);
            detail.setCreater(currentUserName);
            detail.setCreateTime(date);
            detail.setDetailStartTime(date);
            detail.setIsDeleted("0");
            detail.setDetailState(state);
            aeaHiSolicitDetailMapper.insertAeaHiSolicitDetail(detail);
            //创建征求的用户详情信息实例
            List<AeaHiSolicitDetailUser> detailUsers = Lists.newArrayList();
            if("i".equals(type)){
                //查询事项配置的用户信息
                List<AeaSolicitItemUser> aeaSolicitItemUsers = aeaSolicitItemUserMapper.listSolicitItemUserByItemVerId(detail.getItemVerId(), topOrgId);
                for(int i=0,len=aeaSolicitItemUsers.size(); i<len; i++){
                    AeaHiSolicitDetailUser detailUser = createDetailUser(currentUserName, date, detailId, aeaSolicitItemUsers.get(i).getUserId());
                    detailUsers.add(detailUser);
                }
            }else {
                List<AeaSolicitOrgUser> aeaSolicitOrgUsers = aeaSolicitOrgUserMapper.listAeaSolicitOrgUserByOrgId(detail.getDetailOrgId(), topOrgId);
                for(int j=0,len=aeaSolicitOrgUsers.size(); j<len; j++){
                    AeaHiSolicitDetailUser detailUser = createDetailUser(currentUserName, date, detailId, aeaSolicitOrgUsers.get(j).getUserId());
                    detailUsers.add(detailUser);
                }
            }
            if(detailUsers.size() > 0){
                aeaHiSolicitDetailUserMapper.batchInsertAeaHiSolicitDetailUser(detailUsers);
            }
        }
    }

    @Override
    public List<AeaHiSolicit> listAeaHiSolicitByApplyinstId(String applyinstId, String busType) throws Exception {
        if(StringUtils.isBlank(applyinstId))
            throw new RuntimeException("参数applyinstId不能为空！");
        if(StringUtils.isBlank(busType))
            throw new RuntimeException("参数busType业务类型不能为空！");

        AeaHiSolicit solicit = new AeaHiSolicit();
        solicit.setApplyinstId(applyinstId);
        List<AeaHiSolicit> list = aeaHiSolicitMapper.listAeaHiSolicit(solicit);

        if(list!=null&&list.size()>0){
            for(AeaHiSolicit hiSolicit:list){
                hiSolicit.setSolicitTypeName(SolicitBusTypeEnum.valueOf(hiSolicit.getBusType()).getName());
            }
        }

        return list;
    }

    private AeaHiSolicitDetailUser createDetailUser(String currentUserName, Date date, String detailId, String userId) {
        AeaHiSolicitDetailUser detailUser = new AeaHiSolicitDetailUser();
        detailUser.setDetailTaskId(UUID.randomUUID().toString());
        detailUser.setSolicitDetailId(detailId);
        detailUser.setUserId(userId);
        detailUser.setIsDeleted("0");
        detailUser.setCreater(currentUserName);
        detailUser.setCreateTime(date);
        return detailUser;
    }

    /**
     * 处理多字段排序
     *
     * @param page
     * @return
     */
    private Page changeOrderBySql(Page page) {
        if (page != null && StringUtils.isNotBlank(page.getOrderBy())) {
            String[] sqls = page.getOrderBy().split(" ");
            int pageNum = page.getPageNum();
            int pageSize = page.getPageSize();
            if (sqls != null && sqls.length == 2) {
                String fields = sqls[0];
                String orderBy = sqls[1];

                if (fields.contains(",")) {
                    String replaceSql = " " + orderBy + ",";
                    page.setOrderBy(page.getOrderBy().replaceAll(",", replaceSql));
                }
            }
        }

        return page;
    }

    private void buildQueryCondvo(QueryCondVo condVo) {
        condVo.setRootOrgId(SecurityContext.getCurrentOrgId());
        condVo.setUserId(SecurityContext.getCurrentUserId());
    }


    /**
     * 查询催办信息
     * todo
     *
     * @param solicitVoList
     */
    private void loadRemindInfo(List<AeaHiSolicitVo> solicitVoList) {
        /*if (!taskList.isEmpty()) {
            List<String> taskIds = new ArrayList<>();
            for (TaskInfo taskInfo : taskList) {
                if (StringUtils.isNotBlank(taskInfo.getTaskId())) {
                    taskIds.add(taskInfo.getTaskId());
                }
            }
            List<ActStoRemindAndReceiver> actStoRemindAndReceivers = conditionalQueryMapper.listTaskRemindInfo(SecurityContext.getCurrentUserId(), taskIds);

            for (ActStoRemindAndReceiver actStoRemindAndReceiver : actStoRemindAndReceivers) {
                for (TaskInfo taskInfo : taskList) {
                    if (StringUtils.isNotBlank(taskInfo.getTaskId())) {
                        if (taskInfo.getTaskId().equals(actStoRemindAndReceiver.getTaskInstId())) {
                            if (taskInfo.getRemindList() == null) {
                                List<ActStoRemindAndReceiver> remindList = new ArrayList<>();
                                taskInfo.setRemindList(remindList);
                            }
                            taskInfo.getRemindList().add(actStoRemindAndReceiver);
                        }
                    }
                }
            }

        }*/
    }


}
