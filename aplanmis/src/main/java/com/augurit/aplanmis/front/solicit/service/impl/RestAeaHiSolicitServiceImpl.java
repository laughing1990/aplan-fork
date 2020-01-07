package com.augurit.aplanmis.front.solicit.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.mapper.*;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleService;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.constants.SolicitBusTypeEnum;
import com.augurit.aplanmis.common.constants.TimeruleInstState;
import com.augurit.aplanmis.common.constants.TimeruleUnit;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.vo.solicit.AeaHiSolicitVo;
import com.augurit.aplanmis.common.vo.solicit.QueryCondVo;
import com.augurit.aplanmis.front.constant.SolicitConstant;
import com.augurit.aplanmis.front.queryView.service.ConditionalQueryService;
import com.augurit.aplanmis.front.solicit.service.RestAeaHiSolicitService;
import com.augurit.aplanmis.front.solicit.service.SolicitCodeService;
import com.augurit.aplanmis.front.solicit.vo.*;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.flowable.engine.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:chendx
 * @date: 2019-12-23
 * @time: 13:55
 */
@Service
@Transactional
public class RestAeaHiSolicitServiceImpl implements RestAeaHiSolicitService {

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
    private AeaSolicitOrgMapper aeaSolicitOrgMapper;
    @Autowired
    private AeaSolicitOrgUserMapper aeaSolicitOrgUserMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Autowired
    private ActStoTimeruleMapper actStoTimeruleMapper;

    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;

    @Autowired
    private ActStoAppinstMapper actStoAppinstMapper;

    @Autowired
    private ActStoAppinstSubflowMapper actStoAppinstSubflowMapper;

    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;
    @Autowired
    private ActStoTimegroupActMapper actStoTimegroupActMapper;
    @Autowired
    private ActStoTimeruleService actStoTimeruleService;
    @Autowired
    private ActStoTimegroupMapper actStoTimegroupMapper;
    @Autowired
    private ConditionalQueryService conditionalQueryService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private BpmProcessService bpmProcessService;

    @Autowired
    private BpmTaskService bpmTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private OpuOmUserInfoMapper opuOmUserInfoMapper;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Override
    public List<OpuOmOrg> listOrg(String isRoot, String parentOrgId) throws Exception {
        String topOrgId = SecurityContext.getCurrentOrgId();

        if (StringUtils.isBlank(isRoot))
            throw new RuntimeException("参数isRoot不能为空！");
        if (StringUtils.isNotBlank(isRoot) && "0".equals(isRoot)
                && StringUtils.isBlank(parentOrgId))
            throw new RuntimeException("非根组织，参数parentOrgId不能为空！");


        List<OpuOmOrg> list = new ArrayList<>();

        if ("0".equals(isRoot)) {
            OpuOmOrg opuOmOrg = new OpuOmOrg();
            opuOmOrg.setParentOrgId(parentOrgId);
            opuOmOrg.setOrgDeleted("0");
            list = opuOmOrgService.listOpuOmOrg(opuOmOrg);
        }

        if ("1".equals(isRoot)) {
            OpuOmOrg opuOmOrg = opuOmOrgService.getOrg(topOrgId);
            list.add(opuOmOrg);
        }

        return list;
    }

    @Override
    public List<OpuOmOrg> listOrgByKeyword(String busType, String keyword) throws Exception {
        AeaSolicitOrg query = new AeaSolicitOrg();
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        query.setIsBusSolicit("1");
        query.setBusType(busType);
        query.setKeyword(keyword);
        List<AeaSolicitOrg> aeaSolicitOrgs = aeaSolicitOrgMapper.listAeaSolicitOrgRelOrgInfo(query);
        List<OpuOmOrg> result = new ArrayList<>(aeaSolicitOrgs.size());
        for (int i = 0, len = aeaSolicitOrgs.size(); i < len; i++) {
            AeaSolicitOrg aeaSolicitOrg = aeaSolicitOrgs.get(i);
            OpuOmOrg temp = new OpuOmOrg();
            BeanUtils.copyProperties(aeaSolicitOrg, temp);
            result.add(temp);
        }
        return result;
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

        this.loadRemainingOrOverTimeText(voList);
//        this.loadDueNumText(voList);
        return voList;
    }


    /**
     * 创建意见征求实例
     *
     * @param aeaHiSolicit 征求基本信息
     * @param type         征求类型，i事项征求，d部门征询
     * @param busType      征求业务类型，一次征询、意见征求、部门辅导
     * @param detailInfo   征求详细信息 格式[{\"itemId\":\"123\",\"itemVerId\":\"123\",\"orgId\":\"123\",\"orgName\":\"123\"}]"
     * @throws Exception
     */
    @Override
    public void createSolicit(AeaHiSolicit aeaHiSolicit, String type, String detailInfo, String busType) throws Exception {
        //1、先解析事项信息
        JSONArray jsonArray = JSONArray.parseArray(detailInfo);
        if (jsonArray == null || jsonArray.size() == 0) {
            //事项信息为空则直接返回
            throw new RuntimeException("征求的事项或部门信息不能为空！");
        }
        String state = SolicitConstant.SOLICIT_STATE_DO;//征求中
        String currentLoginName = SecurityContext.getCurrentUserName();
        String currentUserName = SecurityContext.getCurrentUser().getUserName();
        String currentUserId = SecurityContext.getCurrentUserId();
        String topOrgId = SecurityContext.getCurrentOrgId();
        String currentOrgId = null;
        String currentOrgName = null;
        List<OpuOmOrg> orgs = opuOmOrgMapper.listBelongOrgByUserId(currentUserId);
        if (orgs.size() > 0) {
            currentOrgId = orgs.get(0).getOrgId();
            currentOrgName = orgs.get(0).getOrgName();
        }
        String timeUnit = null;
        if (StringUtils.isNotBlank(aeaHiSolicit.getSolicitTimeruleId())) {
            ActStoTimerule actStoTimerule = actStoTimeruleMapper.getActStoTimeruleById(aeaHiSolicit.getSolicitTimeruleId());
            if (actStoTimerule != null) {
                timeUnit = actStoTimerule.getTimeruleUnit();
            }
        }
        Date date = new Date();
        if (StringUtils.isBlank(aeaHiSolicit.getSolicitId())) {
            aeaHiSolicit.setSolicitId(UUID.randomUUID().toString());
        }
        //2、保存意见征求主表信息
        aeaHiSolicit.setSolicitCode(solicitCodeService.createSolicitCode());
        aeaHiSolicit.setSolicitType(type);
        aeaHiSolicit.setSolicitStartTime(date);
        aeaHiSolicit.setSolicitState(state);
        aeaHiSolicit.setInitiatorOrgId(currentOrgId);
        aeaHiSolicit.setInitiatorOrgName(currentOrgName);
        aeaHiSolicit.setInitiatorUserId(currentUserId);
        aeaHiSolicit.setInitiatorUserName(currentUserName);
        aeaHiSolicit.setCreater(currentLoginName);
        aeaHiSolicit.setCreateTime(date);
        aeaHiSolicit.setRootOrgId(topOrgId);
        aeaHiSolicit.setSolicitDaysUnit(timeUnit);
        aeaHiSolicitMapper.insertAeaHiSolicit(aeaHiSolicit);

        //保存意见征求的事项详细信息
        for (Object temp : jsonArray) {
            JSONObject jsonObject = (JSONObject) temp;
            AeaHiSolicitDetail detail = new AeaHiSolicitDetail();
            String detailId = UUID.randomUUID().toString();
            detail.setSolicitId(aeaHiSolicit.getSolicitId());
            detail.setSolicitDetailId(detailId);
            if ("i".equals(type)) {
                detail.setItemId(jsonObject.getString("itemId"));
                detail.setItemVerId(jsonObject.getString("itemVerId"));
            }
            detail.setDetailOrgId(jsonObject.getString("orgId"));
            detail.setDetailOrgName(jsonObject.getString("orgName"));
            detail.setDetailDueDays(aeaHiSolicit.getSolicitDueDays());
            detail.setDetailStartTime(date);
            detail.setCreater(currentLoginName);
            detail.setCreateTime(date);
            detail.setDetailStartTime(date);
            detail.setIsDeleted("0");
            detail.setDetailState(state);
            detail.setDetailDaysUnit(timeUnit);
            aeaHiSolicitDetailMapper.insertAeaHiSolicitDetail(detail);
            //创建征求的用户详情信息实例
            List<AeaHiSolicitDetailUser> detailUsers = Lists.newArrayList();
            if ("i".equals(type)) {
                //查询事项配置的用户信息
                List<AeaSolicitItemUser> aeaSolicitItemUsers = aeaSolicitItemUserMapper.listSolicitItemUserByItemVerId(detail.getItemVerId(), topOrgId, aeaHiSolicit.getBusType());
                for (int i = 0, len = aeaSolicitItemUsers.size(); i < len; i++) {
                    AeaHiSolicitDetailUser detailUser = createDetailUser(currentLoginName, date, detailId, aeaSolicitItemUsers.get(i).getUserId());
                    detailUsers.add(detailUser);
                }
            } else {
                List<AeaSolicitOrgUser> aeaSolicitOrgUsers = aeaSolicitOrgUserMapper.listAeaSolicitOrgUserByOrgId(detail.getDetailOrgId(), topOrgId, aeaHiSolicit.getBusType());
                for (int j = 0, len = aeaSolicitOrgUsers.size(); j < len; j++) {
                    AeaHiSolicitDetailUser detailUser = createDetailUser(currentLoginName, date, detailId, aeaSolicitOrgUsers.get(j).getUserId());
                    detailUsers.add(detailUser);
                }
            }
            if (detailUsers.size() > 0) {
                aeaHiSolicitDetailUserMapper.batchInsertAeaHiSolicitDetailUser(detailUsers);
            }
        }
        //处理流程挂起操作
        if (StringUtils.isNotBlank(aeaHiSolicit.getProcinstId()) && !bpmProcessService.isProcessSuspended(aeaHiSolicit.getProcinstId())) {
            bpmProcessService.suspendProcessInstanceById(aeaHiSolicit.getProcinstId());
        }
    }

    @Override
    public List<AeaHiSolicitInfo> listAeaHiSolicitByApplyinstId(String applyinstId, String busType) throws Exception {
        if (StringUtils.isBlank(applyinstId))
            throw new RuntimeException("参数applyinstId不能为空！");
        if (StringUtils.isBlank(busType))
            throw new RuntimeException("参数busType业务类型不能为空！");
        //结果集
        List<AeaHiSolicitInfo> solicitInfoList = new ArrayList<>();

        //通过申请实例id和征求业务类型，查询对应的征求内容，可能有多次
        AeaHiSolicit solicit = new AeaHiSolicit();
        solicit.setApplyinstId(applyinstId);
        solicit.setBusType(busType);
        List<AeaHiSolicit> solicits = aeaHiSolicitMapper.listAeaHiSolicit(solicit);
        //转为值对象后再组装数据
        List<SolicitVo> solicitVos = converToSolicitVo(solicits);
        if (solicitVos != null && solicitVos.size() > 0) {
            //当前登录人信息
            String currentOrgId = SecurityContext.getCurrentOrgId();
            String currUserName = SecurityContext.getCurrentUser().getUserName();
            String currUserId = SecurityContext.getCurrentUserId();
            SolicitDetailUserVo currDetailUser = null;

            List<String> solicitIds = new ArrayList<>();
            for (SolicitVo hiSolicit : solicitVos) {
                hiSolicit.setSolicitTypeName(SolicitBusTypeEnum.valueOf(hiSolicit.getBusType()).getName());
                solicitIds.add(hiSolicit.getSolicitId());
            }
            //先一次查出所有征求的详情信息和涉及的审批人员信息
            List<AeaHiSolicitDetail> solicitDetails = aeaHiSolicitDetailMapper.listAeaHiSolicitDetailBySolicitIds(solicitIds);
            List<AeaHiSolicitDetailUser> solicitDetailUsers = aeaHiSolicitDetailUserMapper.listAeaHiSolicitDetailUserBySolicitIds(solicitIds);
            //转为值对象后再组装数据
            List<SolicitDetailVo> solicitDetailVos = converToSolicitDetailVo(solicitDetails);
            List<SolicitDetailUserVo> solicitDetailUserVos = converToSolicitDetailUserVo(solicitDetailUsers);

            Map<String, List<SolicitDetailVo>> detailMap = new HashMap<>();
            Map<String, List<SolicitDetailUserVo>> detailUserMap = new HashMap<>();
            Map<String, SolicitDetailUserVo> currentAnswerUserMap = new HashMap<>();

            String[] detailUserIds = new String[solicitDetailUserVos.size()];
            if (solicitDetailUserVos != null && solicitDetailUserVos.size() > 0) {
                int i = 0;
                for (SolicitDetailUserVo solicitDetailUser : solicitDetailUserVos) {
                    detailUserIds[i] = solicitDetailUser.getDetailUserId();
                    i++;

                    //如果是当前审批人需要填写意见的，则单独拿出来，等待填写
                    if (currUserId != null && currUserId.equals(solicitDetailUser.getUserId()) && solicitDetailUser.getUserConclusion() == null) {
                        currDetailUser = solicitDetailUser;
                        //查询用户详细信息，姓名，联系方式等
                        OpuOmUserInfo opuOmUserInfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(currUserId);
                        if (opuOmUserInfo != null) {
                            currDetailUser.setLinkmanName(currUserName);
                            currDetailUser.setLinkmanPhone(opuOmUserInfo.getUserMobile());
                        }
                        for (int j = 0, len = solicitDetailVos.size(); j < len; j++) {
                            SolicitDetailVo aeaHiSolicitDetail = solicitDetailVos.get(j);
                            if (aeaHiSolicitDetail.getSolicitDetailId().equals(currDetailUser.getSolicitDetailId())) {
                                currentAnswerUserMap.put(aeaHiSolicitDetail.getSolicitId(), currDetailUser);
                                break;
                            }
                        }
                    }
                    //将一条详情对应的用户放到一个map中
                    List<SolicitDetailUserVo> users = detailUserMap.get(solicitDetailUser.getSolicitDetailId());
                    if (users != null) {
                        users.add(solicitDetailUser);
                    } else {
                        users = new ArrayList<>();
                        users.add(solicitDetailUser);
                        detailUserMap.put(solicitDetailUser.getSolicitDetailId(), users);
                    }
                }
            }
            //查询当前这个详情下所有用户上传的附件
            List<BscAttFileAndDir> attFileList = new ArrayList<>();
            if (detailUserIds.length > 0)
                attFileList = bscAttDetailMapper.searchFileAndDirsSimple(null, currentOrgId, "AEA_HI_SOLICIT_DETAIL_USER", "DETAIL_USER_ID", detailUserIds);
            //遍历再挂到各个用户下
            Map<String, List<BscAttFileAndDir>> fileAndDirMap = new HashMap<>();
            if (attFileList != null && attFileList.size() > 0) {
                for (BscAttFileAndDir fileAndDir : attFileList) {
                    List<BscAttFileAndDir> files = fileAndDirMap.get(fileAndDir.getBscAttLink().getRecordId());

                    if (files != null) {
                        files.add(fileAndDir);
                    } else {
                        files = new ArrayList<>();
                        files.add(fileAndDir);
                        fileAndDirMap.put(fileAndDir.getBscAttLink().getRecordId(), files);
                    }
                }

                if (solicitDetailUserVos != null && solicitDetailUserVos.size() > 0) {
                    for (SolicitDetailUserVo solicitDetailUser : solicitDetailUserVos) {
                        List<BscAttFileAndDir> files = fileAndDirMap.get(solicitDetailUser.getDetailUserId());
                        if (files != null)
                            solicitDetailUser.setFileAndDirs(files);
                    }
                }
            }
            //遍历所有详情详细，并根据征求的主表id挂到各个征求下
            if (solicitDetailVos != null && solicitDetailVos.size() > 0) {
                for (SolicitDetailVo solicitDetail : solicitDetailVos) {
                    List<SolicitDetailUserVo> users = detailUserMap.get(solicitDetail.getSolicitDetailId());
                    solicitDetail.setDetailUsers(users);
                    List<SolicitDetailVo> details = detailMap.get(solicitDetail.getSolicitId());
                    if (details != null) {
                        details.add(solicitDetail);
                    } else {
                        details = new ArrayList<>();
                        details.add(solicitDetail);
                        detailMap.put(solicitDetail.getSolicitId(), details);
                    }
                }
            }

            //查询征求主表信息关联的附件
            Map<String, List<BscAttFileAndDir>> solicitFileAndDirMap = Maps.newHashMap();
            String[] solicitIdArr = new String[solicitIds.size()];
            if (solicitIds.size() > 0) {
                solicitIds.toArray(solicitIdArr);
                List<BscAttFileAndDir> solicitAttFileList = bscAttDetailMapper.searchFileAndDirsSimple(null, currentOrgId,
                        "AEA_HI_SOLICIT", "SOLICIT_ID", solicitIdArr);
                for (BscAttFileAndDir fileAndDir : solicitAttFileList) {
                    List<BscAttFileAndDir> files = solicitFileAndDirMap.get(fileAndDir.getBscAttLink().getRecordId());
                    if (files != null) {
                        files.add(fileAndDir);
                    } else {
                        files = new ArrayList<>();
                        files.add(fileAndDir);
                        solicitFileAndDirMap.put(fileAndDir.getBscAttLink().getRecordId(), files);
                    }
                }
            }


            //遍历征求主表信息，组装数据
            for (SolicitVo hiSolicit : solicitVos) {
                //组装附件信息
                hiSolicit.setFileAndDirs(solicitFileAndDirMap.get(hiSolicit.getSolicitId()));

                List<SolicitDetailVo> details = detailMap.get(hiSolicit.getSolicitId());
                //时限单位转换
                if (hiSolicit.getSolicitDaysUnit() != null) {
                    hiSolicit.setSolicitDaysUnitCn(TimeruleUnit.valueOf(hiSolicit.getSolicitDaysUnit()).getName());
                }
                //如果是事项征求则查询事项名称
                if (SolicitConstant.SOLICIT_TYPE_ITEM.equals(hiSolicit.getSolicitType())) {
                    List<String> itemVerIds = Lists.newArrayList();
                    for (int i = 0, len = details.size(); i < len; i++) {
                        itemVerIds.add(details.get(i).getItemVerId());
                    }
                    List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.getAeaItemBasicListByItemVerIds(itemVerIds);
                    for (int i = 0, len = details.size(); i < len; i++) {
                        for (int j = 0, lenj = aeaItemBasics.size(); j < lenj; j++) {
                            if (details.get(i).getItemVerId().equals(aeaItemBasics.get(j).getItemVerId())) {
                                details.get(i).setItemName(aeaItemBasics.get(j).getItemName());
                                break;
                            }
                        }
                    }
                }

                //判断当前发起的征求信息是否可以被结束，填写汇总意见
                if (currUserId.equals(hiSolicit.getInitiatorUserId()) && (hiSolicit.getSolicitEndTime() == null || SolicitConstant.SOLICIT_STATE_DO.equals(hiSolicit.getSolicitState()))) {
                    //判断当前意见征求是否各个部门已经给出了审批意见结果
                    boolean hasDone = true;
                    for (int i = 0, len = details.size(); i < len; i++) {
                        if (details.get(i).getDetailEndTime() == null || !SolicitConstant.SOLICIT_STATE_DONE.equals(details.get(i).getDetailState())) {
                            hasDone = false;
                            break;
                        }
                    }
                    if (hasDone) {
                        hiSolicit.setSolicitCanBeFinish("1");
                        if (StringUtils.isBlank(hiSolicit.getSolicitLinkmanPhone())) {
                            //查询用户详细信息，姓名，联系方式等
                            OpuOmUserInfo opuOmUserInfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(currUserId);
                            if (opuOmUserInfo != null) {
                                hiSolicit.setSolicitLinkmanName(currUserName);
                                hiSolicit.setSolicitLinkmanPhone(opuOmUserInfo.getUserMobile());
                            }
                        }
                    }
                }

                //针对联合评审类型，做附件的查询和附件类型关联，注意联合评审一次申报只有一次
                if (SolicitBusTypeEnum.LHPS.getValue().equals(busType)) {
                    List<BscDicCodeItem> list = bscDicCodeService.getActiveItemsByTypeCode("SOLICIT_LHPS_FILE_TYPE", currentOrgId);
                    //联合评审主表的附件
                    List<BscAttFileAndDir> fileList1 = bscAttDetailMapper.searchFileAndDirsSimple(null, currentOrgId,
                            "AEA_HI_SOLICIT", null, solicitIdArr);
                    List<SolicitLhpsFile> lhpsFileList1 = Lists.newArrayList();
                    for (int i = 0, len = list.size(); i < len; i++) {
                        BscDicCodeItem bscDicCodeItem = list.get(i);
                        List<BscAttFileAndDir> temp = Lists.newArrayList();
                        for (int j = 0, lenj = fileList1.size(); j < lenj; j++) {
                            if (bscDicCodeItem.getItemCode().equals(fileList1.get(j).getBscAttLink().getPkName())) {
                                temp.add(fileList1.get(j));
                            }
                        }
                        SolicitLhpsFile file = new SolicitLhpsFile();
                        file.setFileTypeCode(bscDicCodeItem.getItemCode());
                        file.setFileTypeName(bscDicCodeItem.getItemName());
                        file.setFileAndDirs(temp);
                        lhpsFileList1.add(file);
                    }
                    hiSolicit.setLhpsFiles(lhpsFileList1);
                    //联合评审被征求人的附件
                    List<BscAttFileAndDir> fileList2 = bscAttDetailMapper.searchFileAndDirsSimple(null, currentOrgId,
                            "AEA_HI_SOLICIT_DETAIL_USER", null, detailUserIds);
                    List<SolicitLhpsFile> lhpsFileList2 = Lists.newArrayList();
                    List<SolicitLhpsFile> lhpsFileList3 = Lists.newArrayList();

                    SolicitDetailUserVo solicitDetailUserVo = currentAnswerUserMap.get(hiSolicit.getSolicitId());
                    if (solicitDetailUserVo != null) {
                        solicitDetailUserVo.setLhpsFiles(lhpsFileList2);
                    }
                    for (int i = 0, len = list.size(); i < len; i++) {
                        BscDicCodeItem bscDicCodeItem = list.get(i);
                        List<BscAttFileAndDir> temp1 = Lists.newArrayList();
                        List<BscAttFileAndDir> temp2 = Lists.newArrayList();
                        for (int j = 0, lenj = fileList2.size(); j < lenj; j++) {
                            if (bscDicCodeItem.getItemCode().equals(fileList2.get(j).getBscAttLink().getPkName())) {
                                temp1.add(fileList2.get(j));
                                if (solicitDetailUserVo != null && solicitDetailUserVo.getDetailUserId().equals(fileList2.get(j).getBscAttLink().getRecordId())) {
                                    temp2.add(fileList2.get(j));
                                }
                            }
                        }
                        SolicitLhpsFile file1 = new SolicitLhpsFile();
                        SolicitLhpsFile file2 = new SolicitLhpsFile();
                        file1.setFileTypeCode(bscDicCodeItem.getItemCode());
                        file1.setFileTypeName(bscDicCodeItem.getItemName());
                        BeanUtils.copyProperties(file1, file2);
                        file1.setFileAndDirs(temp1);
                        file2.setFileAndDirs(temp2);
                        lhpsFileList2.add(file1);
                        lhpsFileList3.add(file2);
                    }
                    //再将回复的和汇总的文件合并导一起
                    for (int i = 0, len = lhpsFileList1.size(); i < len; i++) {
                        SolicitLhpsFile solicitLhpsFile1 = lhpsFileList1.get(i);
                        for (int j = 0, lenj = lhpsFileList2.size(); j < lenj; j++) {
                            SolicitLhpsFile solicitLhpsFile2 = lhpsFileList2.get(j);
                            if (solicitLhpsFile1.getFileTypeCode().equals(solicitLhpsFile2.getFileTypeCode())) {
                                solicitLhpsFile1.getFileAndDirs().addAll(solicitLhpsFile2.getFileAndDirs());
                            }
                        }
                    }
                    hiSolicit.setAllLhpsFiles(lhpsFileList1);
                }

                //组装返回内容
                AeaHiSolicitInfo solicitInfo = new AeaHiSolicitInfo();
                //征求主表实体信息
                solicitInfo.setSolicit(hiSolicit);
                //当前需要被征求部门给意见的实体信息
                solicitInfo.setSolicitDetailUser(currentAnswerUserMap.get(hiSolicit.getSolicitId()));
                //所有的被征求的详细信息
                solicitInfo.setSolicitDetails(details);
                solicitInfoList.add(solicitInfo);
            }
        }

        return solicitInfoList;
    }

    private List<SolicitVo> converToSolicitVo(List<AeaHiSolicit> solicits) {
        List<SolicitVo> result = Lists.newArrayList();
        for (int i = 0, len = solicits.size(); i < len; i++) {
            SolicitVo temp = new SolicitVo();
            BeanUtils.copyProperties(solicits.get(i), temp);
            result.add(temp);
        }
        return result;
    }

    private List<SolicitDetailVo> converToSolicitDetailVo(List<AeaHiSolicitDetail> solicitDetail) {
        List<SolicitDetailVo> result = Lists.newArrayList();
        for (int i = 0, len = solicitDetail.size(); i < len; i++) {
            SolicitDetailVo temp = new SolicitDetailVo();
            BeanUtils.copyProperties(solicitDetail.get(i), temp);
            result.add(temp);
        }
        return result;
    }

    private List<SolicitDetailUserVo> converToSolicitDetailUserVo(List<AeaHiSolicitDetailUser> solicitDetailUsers) {
        List<SolicitDetailUserVo> result = Lists.newArrayList();
        for (int i = 0, len = solicitDetailUsers.size(); i < len; i++) {
            SolicitDetailUserVo temp = new SolicitDetailUserVo();
            BeanUtils.copyProperties(solicitDetailUsers.get(i), temp);
            result.add(temp);
        }
        return result;
    }

    /**
     * 签收
     *
     * @param detailUserId 签收人主键
     * @throws Exception e
     */
    @Override
    public void signSolicitDetail(String detailUserId) throws Exception {
        if (StringUtils.isEmpty(detailUserId)) return;
        AeaHiSolicitDetailUser aeaHiSolicitDetailUser = new AeaHiSolicitDetailUser();
        aeaHiSolicitDetailUser.setDetailUserId(detailUserId);
        aeaHiSolicitDetailUser.setSignTime(new Date());
        aeaHiSolicitDetailUserMapper.updateAeaHiSolicitDetailUser(aeaHiSolicitDetailUser);
    }

    private AeaHiSolicitDetailUser createDetailUser(String currentUserName, Date date, String detailId, String userId) {
        AeaHiSolicitDetailUser detailUser = new AeaHiSolicitDetailUser();
        detailUser.setDetailUserId(UUID.randomUUID().toString());
        detailUser.setSolicitDetailId(detailId);
        detailUser.setUserId(userId);
        detailUser.setIsDeleted("0");
        detailUser.setCreater(currentUserName);
        detailUser.setCreateTime(date);
        return detailUser;
    }

    /**
     * 意见征求被征求人回复接口
     *
     * @param aeaHiSolicitDetailUser
     * @throws Exception
     */
    @Override
    public void createSolicitOpinion(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception {
        String solicitDetailId = aeaHiSolicitDetailUser.getSolicitDetailId();
        String currentUserName = SecurityContext.getCurrentUserName();
        Date date = new Date();
        //更新征求的用户填写的意见
        if (StringUtils.isBlank(aeaHiSolicitDetailUser.getTaskAction())) {
            aeaHiSolicitDetailUser.setTaskAction(SolicitConstant.SOLICIT_TASK_ACTION_NARMAL);
        }
        aeaHiSolicitDetailUser.setFillTime(date);
        aeaHiSolicitDetailUser.setModifier(currentUserName);
        aeaHiSolicitDetailUser.setModifyTime(date);
        aeaHiSolicitDetailUserMapper.updateAeaHiSolicitDetailUser(aeaHiSolicitDetailUser);

        //判断是否需要更新detail信息，如果detail下的所有用户都征求完成，则结束。
        boolean flag = true;
        List<AeaHiSolicitDetailUser> aeaHiSolicitDetailUsers = aeaHiSolicitDetailUserMapper.getAeaHiSolicitDetailUserBySolicitDetailId(solicitDetailId);
        for (int i = 0, len = aeaHiSolicitDetailUsers.size(); i < len; i++) {
            if (!aeaHiSolicitDetailUsers.get(i).getDetailUserId().equals(aeaHiSolicitDetailUser.getDetailUserId()) &&
                    aeaHiSolicitDetailUsers.get(i).getFillTime() == null) {
                flag = false;
            }
        }
        if (flag) {
            AeaHiSolicitDetail aeaHiSolicitDetail = new AeaHiSolicitDetail();
            aeaHiSolicitDetail.setSolicitDetailId(solicitDetailId);
            aeaHiSolicitDetail.setDetailEndTime(date);
            aeaHiSolicitDetail.setDetailState(SolicitConstant.SOLICIT_STATE_DONE);
            aeaHiSolicitDetail.setModifier(currentUserName);
            aeaHiSolicitDetail.setModifyTime(date);
            aeaHiSolicitDetailMapper.updateAeaHiSolicitDetail(aeaHiSolicitDetail);
        }
    }

    /**
     * 意见征求发起人意见汇总接口，可能会涉及流程流转
     *
     * @param aeaHiSolicit
     * @return
     * @throws Exception
     */
    @Override
    public AeaHiSolicit createSolicitCollectOpinion(AeaHiSolicit aeaHiSolicit) throws Exception {
        Date date = new Date();
        OpuOmUser currentUser = SecurityContext.getCurrentUser();
        aeaHiSolicit.setModifyTime(date);
        aeaHiSolicit.setModifier(currentUser.getLoginName());
        aeaHiSolicit.setConclusionTime(date);
        aeaHiSolicit.setSolicitEndTime(date);
        aeaHiSolicit.setConclusionUserName(currentUser.getUserName());
        aeaHiSolicit.setConclusionUserId(currentUser.getUserId());
        aeaHiSolicit.setSolicitState(SolicitConstant.SOLICIT_STATE_DONE);

        aeaHiSolicitMapper.updateAeaHiSolicit(aeaHiSolicit);
        //处理流程激活操作，先默认是通过则激活流程
        AeaHiSolicit temp = aeaHiSolicitMapper.getAeaHiSolicitById(aeaHiSolicit.getSolicitId());
        if (temp != null) {
            String procinstId = temp.getProcinstId();
            boolean isComplete = true;
            if (bpmProcessService.isProcessSuspended(procinstId)) {
                isComplete = false;
                //一次征询，需要汇总结论是通过的才激活流程
                if (SolicitBusTypeEnum.YCZX.getValue().equals(temp.getBusType()) &&
                        SolicitConstant.SOLICIT_CONCLUSION_FLAG_TG.equals(temp.getConclusionFlag())) {
                    bpmProcessService.activateProcessInstanceById(temp.getProcinstId());
                    isComplete = true;
                }
                //联合评审最后结论 通过和不通过都会激活流程
                if (SolicitBusTypeEnum.LHPS.getValue().equals(temp.getBusType())) {
                    bpmProcessService.activateProcessInstanceById(temp.getProcinstId());
                    isComplete = true;
                }
                //意见征求暂时默认给激活流程
                if (SolicitBusTypeEnum.YJZQ.getValue().equals(temp.getBusType())) {
                    bpmProcessService.activateProcessInstanceById(temp.getProcinstId());
                    isComplete = true;
                }
            }
            if (isComplete) {
                completeTaskAfterfinishSolicit(temp);
            }
        }
        return temp;
    }

    /**
     * 意见征求意见汇总后推动流程流转接口
     * 依据目前第一版的，一二阶段（一次征询和联合评审）流程图做的，如果有调整需要跟进优化
     *
     * @param aeaHiSolicit
     * @throws Exception
     */
    @Override
    public void completeTaskAfterfinishSolicit(AeaHiSolicit aeaHiSolicit) throws Exception {
        String hiTaskinstId = aeaHiSolicit.getHiTaskinstId();
        //直接根据汇总结论，决定流程流向
        if (SolicitBusTypeEnum.YCZX.getValue().equals(aeaHiSolicit.getBusType()) &&
                SolicitConstant.SOLICIT_CONCLUSION_FLAG_TG.equals(aeaHiSolicit.getConclusionFlag())) {
            //一次征询需要汇总结论是通过的，才会结束当前节点，并往下流转
            // 注意：基于目前只有一个流向的情况，如果多于一个流向则需要按照实际需求优化此处代码
            taskService.complete(hiTaskinstId);
        } else if (SolicitBusTypeEnum.YJZQ.getValue().equals(aeaHiSolicit.getBusType())) {
            //意见征求默认往下推送，此处后续根据实际需求进行优化
            taskService.complete(hiTaskinstId);
        } else if (SolicitBusTypeEnum.LHPS.getValue().equals(aeaHiSolicit.getBusType())) {
            List<BpmDestTaskConfig> bpmDestTaskConfig = bpmTaskService.getBpmDestTaskConfigByCurrTaskId(hiTaskinstId);
            for (int i = 0, len = bpmDestTaskConfig.size(); i < len; i++) {
                String destActId = bpmDestTaskConfig.get(i).getDestActId();
                //如果联合评审结果时通过，则流向形式审查节点
                if (SolicitConstant.SOLICIT_CONCLUSION_FLAG_TG.equals(aeaHiSolicit.getConclusionFlag()) &&
                        StringUtils.isNotBlank(destActId) && (!destActId.startsWith("endEvent") || !destActId.equals("jieshu"))) {
                    taskService.complete(hiTaskinstId, new String[]{destActId}, (Map) null);
                    return;
                }
                //如果不通过，则直接流向结束节点
                if (SolicitConstant.SOLICIT_CONCLUSION_FLAG_BTG.equals(aeaHiSolicit.getConclusionFlag()) &&
                        StringUtils.isNotBlank(destActId) && (destActId.startsWith("endEvent") || destActId.equals("jieshu"))) {
                    taskService.complete(hiTaskinstId, new String[]{destActId}, (Map) null);
                    return;
                }
            }
        }
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
        if (null == condVo) return;

        condVo.setRootOrgId(SecurityContext.getCurrentOrgId());
        condVo.setUserId(SecurityContext.getCurrentUserId());

        if (SolicitBusTypeEnum.LHPS.getValue().equals(condVo.getBusType())) {
            //联合评审只查询未完成状态
            condVo.setSolicitState(new String[]{"1"});
        } else {
            condVo.setSolicitState(new String[]{"0", "1"});
        }
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

    private void loadDueNumText(List<AeaHiSolicitVo> voList) {
        for (AeaHiSolicitVo info : voList) {
            if (info == null) {
                return;
            }

            String timeText = getTimeText(info.getTimeruleUnit(), info.getSolicitDueDays());
            if (StringUtils.isBlank(timeText)) {
                timeText = "-天";
            }

            info.setDueNumText(timeText);
        }

    }

    private void loadRemainingOrOverTimeText(List<AeaHiSolicitVo> voList) throws Exception {
        Map<String, List<String>> map = new HashMap<>();//保存根据applyinstId已经查询到的solicitId
        Set<String> applyinstIds = new HashSet<>();//保存已经查询的applyinstId
        for (AeaHiSolicitVo info : voList) {
            if (info == null) return;
            //设置viewId
            String viewId = conditionalQueryService.queryViewId(info.getBusType());
            if (StringUtils.isBlank(viewId)) {
                viewId = conditionalQueryService.queryViewId("所有办件");
            }
            info.setViewId(viewId);

            //设置发起的次数
            String solicitId = info.getSolicitId();
            String applyinstId = info.getApplyinstId();
            List<String> solicitIds;
            if (applyinstIds.contains(applyinstId)) {//已经查询过了
                solicitIds = map.get(applyinstId);
//                info.setSolicitIndex(solicitIds.indexOf(solicitId) + 1);
            } else {
                //不存在
                applyinstIds.add(applyinstId);
                // 查询当前申报实例下的发起的总的意见征询数量
                List<AeaHiSolicit> solicits = aeaHiSolicitMapper.getAeaHiSolicitByApplyinstId(applyinstId);
                solicitIds = solicits.stream().map(AeaHiSolicit::getSolicitId).collect(Collectors.toList());
                map.put(applyinstId, solicitIds);

            }
            info.setSolicitIndex(solicitIds.indexOf(solicitId) + 1);
            info.setPromoter(info.getInitiatorUserId().equals(SecurityContext.getCurrentUserId()));
            String busType = info.getBusType();
            if (!"YJZQ".equals(busType)) {
                List<AeaHiSolicitDetail> solicitDetail = aeaHiSolicitDetailMapper.getAeaHiSolicitDetailBySolicitId(solicitId);
                int allNum = solicitDetail.size();
                info.setAllProgressNum(allNum);
                //已完成数量
                int finishNum = 0;
                //征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止
                String[] states = {"2", "3"};
                for (AeaHiSolicitDetail detailUser : solicitDetail) {
                    String detailState = detailUser.getDetailState();
                    if (Arrays.binarySearch(states, detailState) != -1) {
                        finishNum++;
                    }
                }
                info.setFinishProgressNum(finishNum);
                info.setRateProgress(finishNum + "/" + allNum);
            }
            if ("1".equals(info.getIsCalcTimerule())) {//加入计时
                //设置承诺时限文本
                String dueNumText = getTimeText(info.getTimeruleUnit(), info.getSolicitDueDays());
                if (StringUtils.isBlank(dueNumText)) {
                    dueNumText = "-天";
                }
                info.setDueNumText(dueNumText);


                //设置剩余时限文本
                if (StringUtils.isBlank(info.getTimeruleUnit())) {
                    continue;
                }
                Double time = info.getRemainingTime();
                if (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState())) {
                    time = info.getOverdueTime();
                }
                String timeText = getTimeText(info.getTimeruleUnit(), time);
                if (StringUtils.isNotBlank(timeText)) {
                    timeText = (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState()) ? "逾期" : "剩余") + timeText;
                    info.setRemainingOrOverTimeText(timeText);
                }
            } else {
                info.setDueNumText("不计时");
                info.setRemainingOrOverTimeText("不计时");
            }

            loadTaskRemainingOrOverTimeText(info);
        }

    }

    /**
     * 获取时限的显示文本
     *
     * @param timeruleUnit
     * @param time
     * @return
     */
    private String getTimeText(String timeruleUnit, Double time) {
        if (StringUtils.isBlank(timeruleUnit)) {
            timeruleUnit = TimeruleUnit.WD.getValue();
        }

        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            return sb.toString();
        }

        return null;
    }

    /**
     * 加载节点的时限列表
     *
     * @param info
     */
    private void loadTaskRemainingOrOverTimeText(AeaHiSolicitVo info) {
        try {
            List nodeTimelimitList = new ArrayList();
            List hasInst = new ArrayList();
            //获取节点时限实例信息
            ActStoTimeruleInst query1 = new ActStoTimeruleInst();
            query1.setProcInstId(info.getProcinstId());
            query1.setTaskinstId(info.getTaskId());
            List<ActStoTimeruleInst> actStoTimeruleInsts = actStoTimeruleInstMapper.listActStoTimeruleInst(query1);
            if (actStoTimeruleInsts.size() > 0) {
                for (int i = 0; i < actStoTimeruleInsts.size(); i++) {
                    ActStoTimeruleInst actStoTimeruleInst = actStoTimeruleInsts.get(i);
                    String timeruleInstTimeText = getTimeruleInstTimeText(actStoTimeruleInst);
                    if ("2".equals(actStoTimeruleInst.getTimeruleInstType())) {
                        if (StringUtils.isNotBlank(timeruleInstTimeText)) {
                            nodeTimelimitList.add(createVo("当前环节时限", timeruleInstTimeText, actStoTimeruleInst.getInstState()));
                        }
                        hasInst.add(actStoTimeruleInst.getTimegroupActId());

                    } else if ("3".equals(actStoTimeruleInst.getTimeruleInstType())) {
                        if (StringUtils.isNotBlank(timeruleInstTimeText)) {
                            String name = "时限组时限";
                            ActStoTimegroup actStoTimegroup = actStoTimegroupMapper.getActStoTimegroupById(actStoTimeruleInst.getTimegroupId());
                            if (actStoTimegroup != null) {
                                name = actStoTimegroup.getTimegroupName() + "时限";
                            }
                            nodeTimelimitList.add(createVo(name, timeruleInstTimeText, actStoTimeruleInst.getInstState()));
                        }
                        hasInst.add(actStoTimeruleInst.getTimegroupId());
                    }
                }
            }
            //获取节点时限定义信息
            String appFlowdefId = null;
            //查询appFlowdefId 用于关联出节点时限配置
            ActStoAppinst actStoAppinst = actStoAppinstMapper.getActStoAppinstByProcInstId(info.getProcinstId());
            //查的到则是一级流程节点
            if (actStoAppinst != null) {
                appFlowdefId = actStoAppinst.getAppFlowdefId();
            } else {
                //查询子流程
                ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(info.getProcinstId());
                if (actStoAppinstSubflow != null) {
                    ActTplAppTrigger trigger = actTplAppTriggerMapper.getAllActTplAppTriggerById(actStoAppinstSubflow.getTriggerId());
                    if (trigger != null) {
                        appFlowdefId = trigger.getTriggerAppFlowdefId();
                    }
                }
            }
            if (StringUtils.isNotBlank(appFlowdefId)) {
                ActStoTimegroupAct query = new ActStoTimegroupAct();
                query.setAppFlowdefId(appFlowdefId);
                query.setActId(info.getTaskDefKey());
                List<ActStoTimegroupAct> actStoTimegroupActs = actStoTimegroupActMapper.listActStoTimegroupAct(query);
                if (actStoTimegroupActs.size() > 0) {
                    List<ActStoTimerule> actStoTimerules = actStoTimeruleService.listActStoTimerule(new ActStoTimerule());
                    if (actStoTimerules.size() == 0) return;
                    for (int i = 0, len = actStoTimegroupActs.size(); i < len; i++) {
                        ActStoTimegroupAct actStoTimegroupAct = actStoTimegroupActs.get(i);
                        String unit = "WD";
                        for (int j = 0, lenj = actStoTimerules.size(); j < lenj; j++) {
                            if (actStoTimegroupAct.getTimeruleId().equals(actStoTimerules.get(j).getTimeruleId())) {
                                unit = actStoTimerules.get(j).getTimeruleUnit();
                                break;
                            }
                        }
                        String state = "1";
                        if (unit.contains("H")) {
                            if (actStoTimegroupAct.getTimeLimit() <= 48) {
                                state = "2";
                            }
                        } else {
                            if (actStoTimegroupAct.getTimeLimit() <= 2) {
                                state = "2";
                            }
                        }
                        if (StringUtils.isNotBlank(actStoTimegroupAct.getTimegroupId())) {
                            if (hasInst.contains(actStoTimegroupAct.getTimegroupId())) continue;
                            hasInst.add(actStoTimegroupAct.getTimegroupId());
                            String name = "时限组时限";
                            ActStoTimegroup actStoTimegroup = actStoTimegroupMapper.getActStoTimegroupById(actStoTimegroupAct.getTimegroupId());
                            if (actStoTimegroup != null) {
                                name = actStoTimegroup.getTimegroupName() + "时限";
                            }
                            String timeText = getTimeText(unit, actStoTimegroupAct.getTimeLimit().doubleValue());
                            if (StringUtils.isNotBlank(timeText)) {
                                timeText = "剩余" + timeText;
                            }
                            nodeTimelimitList.add(createVo(name, timeText, state));

                        } else if (StringUtils.isBlank(actStoTimegroupAct.getTimegroupId())) {
                            if (hasInst.contains(actStoTimegroupAct.getTimegroupActId())) continue;

                            String timeText = getTimeText(unit, actStoTimegroupAct.getTimeLimit().doubleValue());
                            if (StringUtils.isNotBlank(timeText)) {
                                timeText = "剩余" + timeText;
                            }
                            nodeTimelimitList.add(createVo("当前环节时限", timeText, state));
                        }
                    }
                }
            }
            if (nodeTimelimitList.size() > 0) {
                nodeTimelimitList.add(createVo("当前办件时限", info.getRemainingOrOverTimeText(), info.getInstState()));
                info.setNodeTimelimitList(nodeTimelimitList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map createVo(String name, String text, String state) {
        Map nodeTimelimit = new HashMap();
        nodeTimelimit.put("name", name);
        nodeTimelimit.put("text", text);
        nodeTimelimit.put("instState", state);
        return nodeTimelimit;
    }

    /**
     * 获取时限实例的时间值
     *
     * @param info
     * @return
     */
    private String getTimeruleInstTimeText(ActStoTimeruleInst info) {
        if (StringUtils.isBlank(info.getTimeruleUnit())) {
            return null;
        }
        Double time = info.getRemainingTime();
        if (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState())) {
            time = info.getOverdueTime();
        }
        String timeText = getTimeText(info.getTimeruleUnit(), time);
        if (StringUtils.isNotBlank(timeText)) {
            timeText = (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState()) ? "逾期" : "剩余") + timeText;
        }
        return timeText;
    }

    @Override
    public List<AeaHiIteminst> getApplyItems(String applyinstId) throws Exception {
        return aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
    }

    /**
     * 上传电子附件
     *
     * @param solicitId
     * @param tableName
     * @param request
     * @return
     * @throws Exception
     */
    public String uploadAttFile(String solicitId, String tableName, String pkName, HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(tableName)) throw new Exception("缺少表名参数！");
        if (StringUtils.isBlank(pkName)) throw new Exception("缺少表主键字段名参数！");

        if (StringUtils.isBlank(solicitId)) {
            solicitId = UUID.randomUUID().toString();
        }

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            fileUtilsService.uploadAttachments(tableName, pkName, solicitId, files);
        }
        return solicitId;
    }

    @Override
    public com.augurit.agcloud.opus.common.domain.OpuOmUser getCurrentUser() throws Exception {
        com.augurit.agcloud.opus.common.domain.OpuOmUser user = new com.augurit.agcloud.opus.common.domain.OpuOmUser();
        OpuOmUser currentUser = SecurityContext.getCurrentUser();
        OpuOmUserInfo opuOmUserInfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(currentUser.getUserId());
        user.setUserName(currentUser.getUserName());
        if (opuOmUserInfo != null) {
            user.setUserMobile(opuOmUserInfo.getUserMobile());
        }
        return user;
    }
}
