package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.mapper.ActStoFormMapper;
import com.augurit.agcloud.bsc.sc.rule.code.service.AutoCodeNumberService;
import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportData;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.common.constants.*;
import com.augurit.aplanmis.common.convert.AeaItemMatConvert;
import com.augurit.aplanmis.common.convert.AeaItemStateConvert;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatAdminService;
import com.augurit.aplanmis.common.vo.AeaItemMatKpVo;
import com.augurit.aplanmis.common.vo.AeaStateDateVo;
import com.augurit.aplanmis.common.vo.AuditVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事项情形表-Service服务接口实现类
 */
@Service
@Transactional
@Slf4j
public class AeaItemStateAdminService {

    private AeaItemStateVersionAdminService aeaItemStateVersionAdminService;

    private AutoCodeNumberService autoCodeNumberService;

    private AeaItemMatAdminService aeaItemMatAdminService;

    private AeaItemMapper aeaItemMapper;

    private AeaItemBasicMapper aeaItemBasicMapper;

    private AeaItemVerMapper aeaItemVerMapper;

    private AeaItemStateVerMapper aeaItemStateVersionMapper;

    private AeaItemStateMapper aeaItemStateMapper;

    private AeaItemInoutMapper aeaItemInoutMapper;

    private AeaItemMatMapper aeaItemMatMapper;

    private AeaCertMapper aeaCertMapper;

    private ActStoFormMapper actStoFormMapper;

    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

    private AeaItemStateFormMapper aeaItemStateFormMapper;

    private AeaItemSeqMapper aeaItemSeqMapper;

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void saveAeaItemState(AeaItemState aeaItemState) {

        aeaItemState.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaItemState.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaItemState.setSortNo(getMaxSortNo());
        aeaItemState.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemState.setCreater(SecurityContext.getCurrentUserId());
        aeaItemState.setCreateTime(new Date());
        aeaItemState.checkBeforeSave();
        aeaItemStateMapper.insertAeaItemState(aeaItemState);
    }

    public void saveAeaItemState(List<AeaStateDateVo> stateDate, String itemId) throws Exception {

        AeaItemState searchState = new AeaItemState();
        searchState.setItemId(itemId);
        List<AeaItemState> itemStateList = aeaItemStateMapper.listAeaItemState(searchState);
        if (stateDate != null && stateDate.size() > 0) {
            this.saveAeaItemStateChilds(stateDate.get(0).getChilds(), "", itemId, itemStateList);
        }
        // 剩下的就是多余的情形,开普已经删除的情形
        if (CollectionUtils.isNotEmpty(itemStateList)) {
            for (AeaItemState entity : itemStateList) {
                // 删除情形
                aeaItemStateMapper.deleteAeaItemState(entity.getItemStateId());
            }
        }
    }

    /**
     * 情形数据集的子节点集合
     */
    private void saveAeaItemStateChilds(List<AeaStateDateVo> stateDate, String parentId,
                                        String itemId, List<AeaItemState> itemStateList) throws Exception {

        String userId = SecurityContext.getCurrentUserId();
        for (AeaStateDateVo stateDateVo : stateDate) {
            // 材料节点
            if (stateDateVo.getData() != null) {
                this.savaAeaItemMatOfState(stateDateVo.getData(), itemId, parentId);
            // 情形节点
            } else {
                AeaItemState aeaItemState = new AeaItemState();
                aeaItemState = AeaItemStateConvert.useByGetItemSteat(true, aeaItemState, stateDateVo);

                //获取已有的情形列表
                AeaItemState old = new AeaItemState();
                old.setItemStateId(aeaItemState.getItemStateId());
                List<AeaItemState> oldList = aeaItemStateMapper.listAeaItemStateBySyncKpState(old);

                //获取已有的需要更新那条记录
                AeaItemState needEntity = null;
                if (oldList != null && oldList.size() > 0) {
                    needEntity = oldList.get(0);
                }
                AeaItemState oldEntity = needEntity;
                // 更新
                if (oldEntity != null) {
                    // 剔除已经存在的
                    if (CollectionUtils.isNotEmpty(itemStateList)) {
                        itemStateList.removeIf(state -> state.getItemStateId().equals(oldEntity.getItemStateId()));
                    }
                    if (StringUtils.isNotBlank(parentId)) {
                        aeaItemState.setParentStateId(parentId);
                    }
                    aeaItemState.setItemId(itemId);
                    String itemStateSeq = getItemStateSeq(aeaItemState.getItemStateId(), parentId);
                    aeaItemState.setStateSeq(itemStateSeq);
                    aeaItemState.setIsActive("1");
                    aeaItemState.setIsDeleted(Status.OFF);
                    aeaItemState.setModifier(userId);
                    aeaItemState.setModifyTime(new Date());

                    aeaItemState.checkBeforeSave();
                    aeaItemStateMapper.updateAeaItemState(aeaItemState);
                } else { // 新增
                    //设置父节点Id
                    if (StringUtils.isNotBlank(parentId)) {
                        aeaItemState.setParentStateId(parentId);
                    }
                    //设置事项id
                    aeaItemState.setItemId(itemId);
                    aeaItemState.setUseEl("0");
                    //获取最大的排列顺序号
                    Long maxSortNo = aeaItemStateMapper.getMaxSortNo();
                    if (maxSortNo == null) {
                        maxSortNo = 1L;
                    } else {
                        maxSortNo++;
                    }
                    aeaItemState.setSortNo(maxSortNo);
                    String itemStateSeq = getItemStateSeq(aeaItemState.getItemStateId(), parentId);
                    aeaItemState.setStateSeq(itemStateSeq);
                    aeaItemState.setCreater(userId);
                    aeaItemState.setCreateTime(new Date());
                    aeaItemState.setIsActive("1");
                    aeaItemState.setIsDeleted(Status.OFF);
                    aeaItemState.checkBeforeSave();
                    aeaItemState.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaItemStateMapper.insertAeaItemState(aeaItemState);
                }
            }

            //获取子节点的数据
            if (CollectionUtils.isNotEmpty(stateDateVo.getChilds())) {
                this.saveAeaItemStateChilds(stateDateVo.getChilds(), stateDateVo.getId(), itemId, itemStateList);
            }
        }
    }

    private String getItemStateSeq(String itemStateId, String parentItemStateId) {

        if (StringUtils.isNotBlank(parentItemStateId)) {
            //获取上级菜单的序列
            AeaItemState parentItem = aeaItemStateMapper.getAeaItemStateById(parentItemStateId);
            String parentItemSeq = CommonConstant.SEQ_SEPARATOR;
            if (parentItem != null && StringUtils.isNotBlank(parentItem.getStateSeq())) {
                parentItemSeq = parentItem.getStateSeq();
            }
            return parentItemSeq + itemStateId + CommonConstant.SEQ_SEPARATOR;
        } else {
            return CommonConstant.SEQ_SEPARATOR + itemStateId + CommonConstant.SEQ_SEPARATOR;
        }
    }

    /**
     * 保存情形下的材料
     */
    private void savaAeaItemMatOfState(AeaItemMatKpVo aeaItemMatKpVo, String itemId, String itemStateId) throws Exception {

        // 保存在mat表中
        AeaItemMat aeaItemMat = new AeaItemMat();
        aeaItemMatAdminService.handleKpItemMat(aeaItemMatKpVo, aeaItemMat);

        //对inout表的处理
        List<AeaItemInout> inouts = getAeaItemInoutList(itemId, aeaItemMat.getMatId(), itemStateId, NeedStateStatus.NEED_STATE.getValue());
        String userId = SecurityContext.getCurrentUserId();
        if (inouts != null && !inouts.isEmpty()) {
            String isDeleted = StringUtils.isNotBlank(aeaItemMatKpVo.getMaterialFzsxsj()) ? DeletedStatus.DELETED.getValue() : DeletedStatus.NOT_DELETED.getValue();
            //前面已将所有的inout表中输入数据isDeleted置为"1"，这里只在同步过来的数据为isDeleted为"0"的情况才处理更新操作
            if (DeletedStatus.NOT_DELETED.getValue().equals(isDeleted)) {
                AeaItemInout aeaItemInout = getNeedAeaItemInout(inouts);
                if (!isDeleted.equals(aeaItemInout.getIsDeleted())) {
                    aeaItemInout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                    aeaItemInout.setModifier(userId);
                    aeaItemInout.setIsStateIn(NeedStateStatus.NEED_STATE.getValue());
                    aeaItemInout.setModifyTime(new Date());
                    aeaItemInoutMapper.updateAeaItemInout(aeaItemInout);
                }
            }
        } else {
            AeaItemInout aeaItemInout = new AeaItemInout();
            AeaItemMatConvert.initAeaItemInout(aeaItemMatKpVo, aeaItemInout, aeaItemMat.getMatId());
            aeaItemInout.setItemId(itemId);
            //设置是否为情形输入
            aeaItemInout.setIsStateIn(NeedStateStatus.NEED_STATE.getValue());
            aeaItemInout.setItemStateId(itemStateId);
            aeaItemInout.setCreater(userId);
            aeaItemInout.setCreateTime(new Date());
            aeaItemInoutMapper.insertAeaItemInout(aeaItemInout);
        }
    }

    private List<AeaItemInout> getAeaItemInoutList(String itemId, String matId, String itemStateId, String isStateIn) {

        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setIsOwner(Status.OFF);
        aeaItemInout.setIsInput(InOutStatus.IN.getValue());
        aeaItemInout.setItemId(itemId);
        aeaItemInout.setFileType(InOutType.MAT.getValue());
        aeaItemInout.setMatId(matId);
        aeaItemInout.setIsStateIn(isStateIn);
        aeaItemInout.setItemStateId(itemStateId);
        return aeaItemInoutMapper.listAeaItemInout(aeaItemInout);
    }

    /**
     * 选择的优先级:
     * 1、有关联aeaHiItemInoutinst
     * 2、修改时间最晚
     */
    private AeaItemInout getNeedAeaItemInout(List<AeaItemInout> list) throws Exception {

        if (list != null && !list.isEmpty()) {
            AeaItemInout need = null;
            if (list.size() == 1) {
                need = list.get(0);
            } else {
                AeaItemInout lastModify = null;
                for (AeaItemInout inout : list) {
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setItemInoutId(inout.getInoutId());
                    List<AeaHiItemInoutinst> aeaHiItemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);

                    if (aeaHiItemInoutinsts != null && !aeaHiItemInoutinsts.isEmpty()) {

                        if (need == null) {
                            need = inout;
                        } else {
                            need = getLastModifyAeaItemInout(need, inout);
                        }
                    } else {
                        if (lastModify == null) {
                            lastModify = inout;
                        } else {
                            lastModify = getLastModifyAeaItemInout(lastModify, inout);
                        }
                    }
                }
                if (need == null) {
                    need = lastModify;
                }
            }
            return need;
        }
        return null;
    }

    /**
     * 获取最后修改的那个对象
     */
    private AeaItemInout getLastModifyAeaItemInout(AeaItemInout inout1, AeaItemInout inout2) {

        if (inout1.getModifyTime() != null && inout2.getModifyTime() != null) {
            //TODO
            /*if(inout2.getModifyTime().after(inout1.getModifyTime())){
                return inout2;
            }*/
        } else if (inout1.getModifyTime() == null && inout2.getModifyTime() != null) {
            return inout2;
        }

        return inout1;
    }

    public void updateAeaItemState(AeaItemState aeaItemState) {
        aeaItemState.setModifier(SecurityContext.getCurrentUserId());
        aeaItemState.setModifyTime(new Date());
        aeaItemStateMapper.updateAeaItemState(aeaItemState);
    }

    public void deleteAeaItemStateById(String id) {
        if (StringUtils.isNotBlank(id)) {
            aeaItemStateMapper.deleteAeaItemState(id);
        }
    }

    public void thoroughDeleteAeaItemState(String id) {
        if (StringUtils.isNotBlank(id)) {
            aeaItemStateMapper.thoroughDeleteAeaItemState(id);
        }
    }

    public void thoroughDeleteAeaItemStateByItemId(String id) {
        if (StringUtils.isNotBlank(id)) {
            aeaItemStateMapper.thoroughDeleteAeaItemStateByItemId(id);
        }
    }

    public PageInfo<AeaItemState> listAeaItemState(AeaItemState aeaItemState, Page page) {
        PageHelper.startPage(page);
        List<AeaItemState> list = aeaItemStateMapper.listAeaItemStateWithStateVer(aeaItemState);
        log.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    public AeaItemState getAeaItemStateById(String id) {
        if (StringUtils.isNotBlank(id)) {
            log.debug("根据ID获取Form对象，ID为：{}", id);
            return aeaItemStateMapper.getAeaItemStateById(id);
        }
        return null;
    }

    public List<AeaItemState> listAeaItemState(AeaItemState aeaItemState) {

        if(aeaItemState!=null)
            aeaItemState.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemState> list = aeaItemStateMapper.listAeaItemStateWithStateVer(aeaItemState);
        log.debug("成功执行查询list！！");
        return list;
    }

    public Long getMaxSortNo() {

        Long sortNo = aeaItemStateMapper.getMaxSortNo();
        return sortNo == null ? 1L : (sortNo + 1);
    }

    public void batchDeleteAeaItemState(String[] ids) {
        if (ids != null && ids.length > 0) {
            aeaItemStateMapper.batchDeleteAeaItemState(ids);
        }
    }

    public void changIsActiveState(String id) {
        if (StringUtils.isNotBlank(id)) {
            aeaItemStateMapper.changIsActiveState(id);
        }
    }

    @Transactional(readOnly = true)
    public MindBaseNode listAeaItemStateTreeByItemVerId(String itemVerId, String stateVerId, String rootOrgId, AeaMindUi aeaMindUi) {

        Assert.notNull(itemVerId, "事项版本itemVerId不能为空");

        // 构造根节点
        MindBaseNode orgNode = buildItemTreeRootNode(itemVerId, rootOrgId);

        // 根据itemVerId和stateVerId获取所有的情形
        List<AeaItemState> allStates = preFetchAllStates(itemVerId, stateVerId, rootOrgId);

        // 构造子树节点，包括情形和材料
        List<MindBaseNode> childNodes = new ArrayList<>();
        node(itemVerId, itemVerId, allStates, childNodes, aeaMindUi, rootOrgId);

        List<AeaItemInout> matIns;
        List<AeaItemInout> certIns;
        List<AeaItemInout> commonForms;

        // 显示事项下的通用材料
        if(aeaMindUi!=null&&aeaMindUi.isShowMat()){
            matIns = listAeaItemInoutNotUnderState(itemVerId, stateVerId, MindType.M.getValue(), rootOrgId);
            // 封装通用材料节点
            getBaseNode(matIns, childNodes, itemVerId);
        }
        // 显示事项下的通用证照
        if(aeaMindUi!=null&&aeaMindUi.isShowCert()){
            certIns = listAeaItemInoutNotUnderState(itemVerId, stateVerId, MindType.C.getValue(), rootOrgId);
            // 封装通用证照节点
            getBaseNode(certIns, childNodes, itemVerId);
        }
        // 显示事项下的通用表单
        if(aeaMindUi!=null&&aeaMindUi.isShowForm()){
            commonForms = listAeaItemInoutNotUnderState(itemVerId, stateVerId, MindType.F.getValue(), rootOrgId);
            // 封装通用证照节点
            getBaseNode(commonForms, childNodes, itemVerId);
        }
        // 设置根节点的孩子们
        orgNode.setChilds(childNodes.toArray(new MindBaseNode[]{}));
        return orgNode;
    }

    /**
     * 预先查询出事项下的所有情形
     *
     * @param itemVerId
     * @param stateVerId
     * @return
     */
    private List<AeaItemState> preFetchAllStates(String itemVerId, String stateVerId, String rootOrgId) {

        if (StringUtils.isBlank(stateVerId)) {
            stateVerId = getMaxStateVerId(itemVerId, rootOrgId);
        }
        if(StringUtils.isBlank(stateVerId)){
            return new ArrayList<>();
        }
        AeaItemState aeaItemState = new AeaItemState();
        aeaItemState.setItemVerId(itemVerId);
        aeaItemState.setStateVerId(stateVerId);
        aeaItemState.setRootOrgId(rootOrgId);
        aeaItemState.setIsActive(Status.ON);
        aeaItemState.setIsDeleted(Status.OFF);
        return aeaItemStateMapper.listAeaItemStateWithStateVer(aeaItemState);
    }

    private String getMaxStateVerId(String itemVerId, String rootOrgId){

        AeaItemStateVer latestVersion = aeaItemStateVersionAdminService.getSpecificVersion(AeaItemStateVersionAdminService.StateVersionStrategy.MAX_VERSION, itemVerId, rootOrgId);
        if (latestVersion != null) {
            return latestVersion.getItemStateVerId();
        }
        return null;
    }

    /**
     * 根据 itemVerId获取构造思维导图的树结构
     *
     * @param itemVerId
     * @param parentId
     * @param allStates
     * @param childNodes
     * @param aeaMindUi
     */
    private void node(String itemVerId, String parentId, List<AeaItemState> allStates, List<MindBaseNode> childNodes, AeaMindUi aeaMindUi, String rootOrgId) {

        List<AeaItemState> list = extractStatesByParentIdAndItemVerId(parentId, itemVerId, allStates);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (AeaItemState state : list) {
            MindBaseNode stateNode;
            stateNode = buildNodeByState(state, parentId);
            List<MindBaseNode> stateChildren = new ArrayList<>();

            //递归处理子情形节点
            node(itemVerId, state.getItemStateId(), allStates, stateChildren, aeaMindUi, rootOrgId);

            List<AeaItemInout> matIns;
            List<AeaItemInout> certIns;
            List<AeaItemInout> stateForms;

            // 显示事项下的情形材料
            if(aeaMindUi!=null&&aeaMindUi.isShowMat()){

                matIns = listAeaItemInoutUnderState(itemVerId, state.getItemStateId(), MindType.M.getValue(), rootOrgId);
                // 封装材料节点
                getBaseNode(matIns, stateChildren, null);
            }

            // 显示事项下的情形证照
            if(aeaMindUi!=null&&aeaMindUi.isShowCert()){

                certIns = listAeaItemInoutUnderState(itemVerId, state.getItemStateId(), MindType.C.getValue(), rootOrgId);
                // 封装证照节点
                getBaseNode(certIns, stateChildren, null);
            }

            // 显示事项下的情形表单
            if(aeaMindUi!=null&&aeaMindUi.isShowForm()){

                stateForms = listAeaItemInoutUnderState(itemVerId, state.getItemStateId(), MindType.F.getValue(), rootOrgId);
                // 封装证照节点
                getBaseNode(stateForms, stateChildren, null);
            }
            stateNode.setChilds(stateChildren.toArray(new MindBaseNode[]{}));
            childNodes.add(stateNode);
        }
    }

    /**
     * 获取指定情形下所有输入材料或者证照
     *
     * @param itemVerId
     * @param itemStateId
     * @return
     */
    private List<AeaItemInout> listAeaItemInoutUnderState(String itemVerId, String itemStateId, String fileType, String rootOrgId){

        AeaItemInout inout = new AeaItemInout();
        inout.setRootOrgId(rootOrgId);
        inout.setItemVerId(itemVerId);
        inout.setIsStateIn(Status.ON);
        inout.setItemStateId(itemStateId);
        inout.setMatProp(fileType);
        inout.setIsInput(Status.ON);
        inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        return aeaItemInoutMapper.listAeaItemInoutRelMat(inout);
    }

    /**
     * 获取指定情形下所有表单
     *
     * @return
     */
    private List<AeaItemStateForm> listAeaItemStateFormUnderState(String itemVerId, String itemStateVerId, String itemStateId){

        AeaItemStateForm form = new AeaItemStateForm();
        form.setItemVerId(itemVerId);
        form.setItemStateVerId(itemStateVerId);
        form.setIsStateForm(Status.ON);
        form.setItemStateId(itemStateId);
        return aeaItemStateFormMapper.listItemStateFormRelInfo(form);
    }

    private void getBaseNode(List<AeaItemInout> inouts, List<MindBaseNode> mindBaseNodes, String parentId) {

        if(inouts!=null&&inouts.size()>0){
            for (AeaItemInout in : inouts) {
                if (StringUtils.isBlank(parentId)) {
                    parentId = in.getItemStateId();
                }
//                if (MindType.M.getValue().equals(in.getMatProp())) {
//                    AeaItemMat mat = aeaItemMatMapper.selectOneById(in.getMatId());
//                    if (Objects.isNull(mat)) {
//                        return;
//                    }
//                    mindBaseNodes.add(buildNodeByMat(mat, parentId));
//                } else if (MindType.C.getValue().equals(in.getMatProp())) {
//                    AeaCert cert = aeaCertMapper.selectOneById(in.getCertId());
//                    if (Objects.isNull(cert)) {
//                        return;
//                    }
//                    mindBaseNodes.add(buildNodeByCert(cert, parentId));
//                }else if(MindType.F.getValue().equals(in.getMatProp())){
//                    ActStoForm form = aeaItemMatMapper.getActStoFormById(in.getFormId());
//                    if (Objects.isNull(form)) {
//                        return;
//                    }
                    mindBaseNodes.add(buildNodeByMatCertForm(in, parentId));
//                }
            }
        }
    }

    /**
     * 获取指定情形下所有输入材料或者证照
     *
     * @param itemVerId
     * @param stateVerId
     * @return
     */
    private List<AeaItemInout> listAeaItemInoutNotUnderState(String itemVerId, String stateVerId, String fileType, String rootOrgId) {

        if (StringUtils.isBlank(stateVerId)) {
            stateVerId = getMaxStateVerId(itemVerId, rootOrgId);
        }
        if(StringUtils.isBlank(stateVerId)){
            return new ArrayList<>();
        }
        AeaItemInout inout = new AeaItemInout();
        inout.setRootOrgId(rootOrgId);
        inout.setItemVerId(itemVerId);
        inout.setStateVerId(stateVerId);
        inout.setIsInput(Status.ON);
        inout.setIsStateIn(Status.OFF);
        inout.setMatProp(fileType);
        inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        return aeaItemInoutMapper.listAeaItemInoutRelMat(inout);
    }

    private List<AeaItemStateForm> listAeaItemStateFormNotUnderState(String itemVerId, String stateVerId, String rootOrgId) {

        if (StringUtils.isBlank(stateVerId)) {
            stateVerId = getMaxStateVerId(itemVerId, rootOrgId);
        }
        if(StringUtils.isBlank(stateVerId)){
            return new ArrayList<>();
        }
        AeaItemStateForm commonForm = new AeaItemStateForm();
        commonForm.setItemVerId(itemVerId);
        commonForm.setItemStateVerId(stateVerId);
        commonForm.setIsStateForm(Status.OFF);
        return aeaItemStateFormMapper.listItemStateFormRelInfo(commonForm);
    }

    private MindBaseNode buildItemTreeRootNode(String itemVerId, String rootOrgId) {

        MindBaseNode orgNode = new MindBaseNode();
        //将itemVerId作为树的根节点
        orgNode.setId(itemVerId);
        orgNode.setName(aeaItemBasicMapper.getOneByItemVerId(itemVerId, rootOrgId).getItemName());
        orgNode.setPid(MindConst.MIND_NODE_ROOT);
        orgNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        orgNode.setPriority("");
        orgNode.setProgress("");
        orgNode.setNodeTypeCode(MindType.ITEM.getValue());
        orgNode.setNodeTypeName(MindType.ITEM.getName());
        return orgNode;
    }

    private List<AeaItemState> extractStatesByParentIdAndItemVerId(String parentId, String itemVerId, List<AeaItemState> allStates) {

        //获取一级情形
        if (itemVerId.equals(parentId)) {
            return allStates.stream().filter(AeaItemState::firstLevelState).collect(Collectors.toList());
        } else {//根据情形父id获取
            return allStates.stream().filter(s -> parentId.equals(s.getParentStateId())).collect(Collectors.toList());
        }
    }

    /**
     * 根据AeaItemState构造一个树子节点
     *
     * @param aeaItemState
     * @param parentId
     * @return
     */
    private MindBaseNode buildNodeByState(AeaItemState aeaItemState, String parentId) {

        MindBaseNode mindBaseNode = new MindBaseNode();
        mindBaseNode.setId(aeaItemState.getItemStateId());
        mindBaseNode.setName(aeaItemState.getStateName());
        //父级id
        mindBaseNode.setPid(parentId);
        mindBaseNode.setOpen(String.valueOf(aeaItemState.isOpen()));
        //是否必选
        mindBaseNode.setPriority("m".equals(aeaItemState.getAnswerType()) ? AeaMindConst.MIND_NODE_PRIORITY_MAPPING_REQUIRED : "");
        //是否多选
        mindBaseNode.setProgress(Status.ON.equals(aeaItemState.getMustAnswer()) ? AeaMindConst.MIND_NODE_PROGRESS_MAPPING_REQUIRED : "");
        mindBaseNode.setNodeTypeCode(MindType.SITUATION.getValue());
        mindBaseNode.setNodeTypeName(MindType.SITUATION.getName());
        mindBaseNode.setNote(aeaItemState.getStateMemo());
        mindBaseNode.setLinkProcessStart(aeaItemState.getIsProcStartCond());
        return mindBaseNode;
    }

    private MindBaseNode buildNodeByMatCertForm(AeaItemInout inout, String parentId) {

        MindBaseNode mindBaseNode = new MindBaseNode();
        mindBaseNode.setProgress("");
        mindBaseNode.setPid(parentId);
        mindBaseNode.setId(inout.getMatId());
        mindBaseNode.setName(inout.getAeaMatCertName());
        mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        Map<String, String> map = new HashMap<>();
        map.put("inoutId", inout.getInoutId());
        if(MindType.M.getValue().equals(inout.getMatProp())){

            mindBaseNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_MAT);
            mindBaseNode.setNodeTypeCode(MindType.MATERIAL.getValue());
            mindBaseNode.setNodeTypeName(MindType.MATERIAL.getName());
            map.put("matId", inout.getMatId());

        }else if(MindType.C.getValue().equals(inout.getMatProp())){

            mindBaseNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_CERT);
            mindBaseNode.setNodeTypeCode(MindType.CERTIFICATE.getValue());
            mindBaseNode.setNodeTypeName(MindType.CERTIFICATE.getName());
            map.put("certId", inout.getCertId());

        }else if(MindType.F.getValue().equals(inout.getMatProp())){

            mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
            mindBaseNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_FORM);
            mindBaseNode.setNodeTypeCode(MindType.FORM.getValue());
            mindBaseNode.setNodeTypeName(MindType.FORM.getName());
            ActStoForm form = aeaItemMatMapper.getActStoFormById(inout.getFormId());
            map.put("formId", inout.getFormId());
            map.put(AeaMindConst.MIND_NODE_EXTRA_KEY_FORM_PROPERTY, form==null?null:form.getFormProperty());
        }
        mindBaseNode.setExtra(map);
        return mindBaseNode;
    }

    /**
     * 根据AeaItemMat构造一个树子节点
     *
     * @param aeaItemMat
     * @param parentId
     * @return
     */
    private MindBaseNode buildNodeByMat(AeaItemMat aeaItemMat, String parentId) {

        MindBaseNode mindBaseNode = new MindBaseNode();
        mindBaseNode.setId(aeaItemMat.getMatId());
        mindBaseNode.setName(aeaItemMat.getMatName());
        mindBaseNode.setPid(parentId);
        mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        mindBaseNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_MAT);
        mindBaseNode.setProgress("");
        mindBaseNode.setNodeTypeCode(MindType.MATERIAL.getValue());
        mindBaseNode.setNodeTypeName(MindType.MATERIAL.getName());
        mindBaseNode.setNote(aeaItemMat.getNote());
        return mindBaseNode;
    }

    /**
     * 根据AeaCert构造一个树子节点
     *
     * @param cert
     * @param parentId
     * @return
     */
    private MindBaseNode buildNodeByCert(AeaCert cert, String parentId) {

        MindBaseNode mindBaseNode = new MindBaseNode();
        mindBaseNode.setId(cert.getCertId());
        mindBaseNode.setName(cert.getCertName());
        mindBaseNode.setPid(parentId);
        mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        //设置代表证照的显示图标
        mindBaseNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_CERT);
        mindBaseNode.setProgress("");
        mindBaseNode.setNodeTypeCode(MindType.CERTIFICATE.getValue());
        mindBaseNode.setNodeTypeName(MindType.CERTIFICATE.getName());
        mindBaseNode.setNote(cert.getCertMemo());
        return mindBaseNode;
    }

    /**
     * 构建表单节点
     *
     * @param form
     * @param parentId
     * @return
     */
    private MindBaseNode buildNodeByForm(AeaItemStateForm form, String parentId) {

        MindBaseNode mindFormNode = new MindBaseNode();
        mindFormNode.setId(form.getItemStateFormId());
        mindFormNode.setName(form.getFormName());
        mindFormNode.setPid(parentId);
        mindFormNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
        mindFormNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_FORM);
        mindFormNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_FORM);
        Map<String, String> map = new HashMap<>();
        map.put("formId", form.getFormId());
        map.put(AeaMindConst.MIND_NODE_EXTRA_KEY_FORM_PROPERTY, form.getFormProperty());
        mindFormNode.setExtra(map);
        return mindFormNode;
    }

    /**
     * 构建表单节点
     *
     * @param form
     * @param parentId
     * @return
     */
    private MindBaseNode buildNodeByForm(ActStoForm form, String parentId) {

        MindBaseNode mindFormNode = new MindBaseNode();
        mindFormNode.setId(form.getFormId());
        mindFormNode.setName(form.getFormName());
        mindFormNode.setPid(parentId);
        mindFormNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
        mindFormNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_FORM);
        mindFormNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_FORM);
        Map<String, String> map = new HashMap<>();
        map.put("formId", form.getFormId());
        map.put(AeaMindConst.MIND_NODE_EXTRA_KEY_FORM_PROPERTY, form.getFormProperty());
        mindFormNode.setExtra(map);
        return mindFormNode;
    }

    /**
     * 更新情形材料(包括编辑， 新增， 删除)
     *
     * @param mindExportObj 事项情形的树型结构数据
     * @param itemVerId     事项版本id
     */
    public void refreshItemTree(MindExportObj mindExportObj, String itemVerId, String stateVerId, String rootOrgId, String userId) {

        Assert.notNull(itemVerId, "itemVerid is null");

        if (StringUtils.isBlank(stateVerId)) {
            AeaItemStateVer latestVersion = aeaItemStateVersionAdminService.getSpecificVersion(AeaItemStateVersionAdminService.StateVersionStrategy.MAX_VERSION, itemVerId, rootOrgId);
            stateVerId = latestVersion == null ? null : latestVersion.getItemStateVerId();
        }
        List<String> stateSeq = new ArrayList<>();
        String needRecursionOccurrence = "";
        stateSeq.add(CommonConstant.SEQ_SEPARATOR);
        //第一次编辑没有初始版本
        if (StringUtils.isBlank(stateVerId)) {
            AeaItemStateVer initVersion = createUnpublishedVersion(itemVerId, rootOrgId, userId);
            stateVerId = initVersion.getItemStateVerId();
        }
        AeaItemStateVer aeaItemStateVersion = aeaItemStateVersionMapper.getAeaItemStateVerById(stateVerId);
        if (!aeaItemStateVersion.isEditable()) {
            throw new RuntimeException("当前版本不可修改，版本状态：" + aeaItemStateVersion.getVerStatus());
        }
        try {
            updateItemTree(mindExportObj, itemVerId, stateVerId, stateSeq, false, needRecursionOccurrence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deleteNodeRecursion(mindExportObj, "", itemVerId, stateVerId, rootOrgId);
    }

    public AeaItemStateVer createUnpublishedVersion(String itemVerId, String rootOrgId, String userId) {

        AeaItemVer aeaItemVer = aeaItemVerMapper.getAeaItemVerById(itemVerId);
        Assert.notNull(aeaItemVer, "无法找到对应的事项版本, itemVerId: " + itemVerId);
        AuditVo auditVo = AuditVo.newOne(userId, rootOrgId);
        AeaItemStateVer initVersion = AeaItemStateVer.initVer(aeaItemVer.getItemId(), itemVerId, auditVo);
        initVersion.setVerNum(1.0D);
        initVersion.setVerStatus(PublishStatus.TEST_RUN.getValue());
        aeaItemStateVersionMapper.insertAeaItemStateVer(initVersion);
        return initVersion;
    }

    /**
     * 1.根据itemVerId从数据库中查询所有情形， 与树的情形节点进行比对
     * 2.对于在数据库中有而树中没有的记录进行删除
     * 3.删除材料时要考虑是否全局材料，因为全局材料是不用删的， 删除关联关系即可
     * 4.证照也需要考虑在内
     */
    private void deleteNodeRecursion(MindExportObj mindExportObj, String parentId, String itemVerId, String stateVerId, String rootOrgId) {

        if (MindType.ITEM.getValue().equals(mindExportObj.getData().getNodeTypeCode()) || MindType.SITUATION.getValue().equals(mindExportObj.getData().getNodeTypeCode())) {

            Set<String> stateIdsFromDB = new HashSet<>();
            Set<String> stateIdsFromTree = new HashSet<>();
            if (mindExportObj.getChildren() != null && mindExportObj.getChildren().length > 0) {
                Arrays.asList(mindExportObj.getChildren()).forEach(c -> stateIdsFromTree.add(c.getData().getId()));
            }
            List<AeaItemState> aeaItemStates = aeaItemStateMapper.listChildAeaItemStateByParentStateId(itemVerId, stateVerId, parentId, rootOrgId);
            aeaItemStates.forEach(s -> stateIdsFromDB.add(s.getItemStateId()));

            stateIdsFromTree.retainAll(stateIdsFromDB);
            stateIdsFromDB.removeAll(stateIdsFromTree);

            for (String stateId : stateIdsFromDB) {
                deleteNodeByItemStateId(stateId, itemVerId, stateVerId, rootOrgId);
            }

            Set<String> matIdsFromTree = new HashSet<>();
            if (mindExportObj.getChildren() != null && mindExportObj.getChildren().length > 0) {
                for (MindExportObj remain : mindExportObj.getChildren()) {
                    MindExportData remainData = remain.getData();
                    if (MindType.SITUATION.getValue().equals(remainData.getNodeTypeCode())) {
                        for (String id : stateIdsFromTree) {
                            if (id.equals(remainData.getId())) {
                                deleteNodeRecursion(remain, remainData.getId(), itemVerId, stateVerId, rootOrgId);
                            }
                        }
                    } else if (MindType.MATERIAL.getValue().equals(remainData.getNodeTypeCode())
                             ||MindType.CERTIFICATE.getValue().equals(remainData.getNodeTypeCode())
                             ||MindType.FORM.getValue().equals(remainData.getNodeTypeCode())) {

                        matIdsFromTree.add(remainData.getId());
                    }
                }
            }
            handMatAndCertDeletion(matIdsFromTree, parentId, itemVerId, stateVerId, rootOrgId);
        }
    }

    private void handMatAndCertDeletion(Set<String> matIdsFromTree, String parentId, String itemVerId, String stateVerId, String rootOrgId) {

        AeaItemInout param = new AeaItemInout();
        if (StringUtils.isBlank(parentId)) {
            param.setItemStateId(null);
            param.setIsStateIn(Status.OFF);
        } else {
            param.setItemStateId(parentId);
            param.setIsStateIn(Status.ON);
        }
        param.setRootOrgId(rootOrgId);
        param.setItemVerId(itemVerId);
        param.setStateVerId(stateVerId);
        param.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        param.setIsInput(Status.ON);
        List<AeaItemInout> inoutFromDb = aeaItemInoutMapper.listAeaItemInoutRelMat(param);
        Set<String> matIdsNeedDel = new HashSet<>();
        if (Objects.nonNull(inoutFromDb)) {
            inoutFromDb.forEach(inout -> {
                if (!matIdsFromTree.contains(inout.getMatId())) {
                    matIdsNeedDel.add(inout.getInoutId());
                }
            });
            if (CollectionUtils.isNotEmpty(matIdsNeedDel)) {
                List<String> inoutIds = new ArrayList<>();
                inoutIds.addAll(matIdsNeedDel);
                aeaItemInoutMapper.batchDeleteAeaItemInoutByIds(inoutIds);
            }
        }
    }

    private void deleteNodeByItemStateId(String stateId, String itemVerId, String stateVerId, String rootOrgId) {

        aeaItemInoutMapper.deleteAeaItemInoutByStateId(stateId, itemVerId, stateVerId, rootOrgId);
        aeaItemMatMapper.deleteAeaItemMatByStateId(stateId, itemVerId, stateVerId, rootOrgId);
        List<AeaItemState> childStates = aeaItemStateMapper.listChildAeaItemStateByParentStateId(itemVerId, stateVerId, stateId, rootOrgId);
        if (CollectionUtils.isNotEmpty(childStates)) {
            childStates.forEach(c -> deleteNodeByItemStateId(c.getItemStateId(), itemVerId, stateVerId, rootOrgId));
        }
        aeaItemStateMapper.deleteAeaItemState(stateId);
    }

    /*
     担任的职责：
     1.新增节点， 递归新增
     2.设置情形的seq (较低层级， 偏细节)
     3.更新节点
     */
    private void updateItemTree(MindExportObj mindExportObj, String itemVerId, String stateVerId, List<String> stateSeq, Boolean needRecursionCreate, String needRecursionOccurrence) throws Exception{

        if (itemVerId.equals(mindExportObj.getData().getId())) {
            stateSeq.clear();
            stateSeq.add(CommonConstant.SEQ_SEPARATOR);
            needRecursionCreate = false;
        } else {
            stateSeq.add(mindExportObj.getData().getId() + CommonConstant.SEQ_SEPARATOR);
        }
        //有子节点才处理
        if (mindExportObj.getChildren() != null && mindExportObj.getChildren().length > 0) {
            for (MindExportObj c : mindExportObj.getChildren()) {
                //判断是否新增节点
                if (MindConst.MIND_NODE_OPERATOR_TAG_NEW.equals(c.getData().getOperatorTag()) || needRecursionCreate) {
                    if (!needRecursionCreate) {
                        needRecursionOccurrence = c.getData().getId();
                        needRecursionCreate = true;
                    }
                    String currentNodeParentId = mindExportObj.getData().getId();
                    if (AeaMindConst.MIND_NODE_TYPE_CODE_SITUATION.equals(c.getData().getNodeTypeCode())) {

                        AeaItemState aeaItemState = insertAeaState(c, itemVerId, stateVerId, currentNodeParentId, stateSeq);
                        //将【新增的情形id】赋予【当前新增的节点】，用作【后续新增子节点】的父id
                        c.getData().setId(aeaItemState.getItemStateId());
                        //材料新增
                    } else if (MindType.MATERIAL.getValue().equals(c.getData().getNodeTypeCode())
                             ||MindType.CERTIFICATE.getValue().equals(c.getData().getNodeTypeCode())
                             ||MindType.FORM.getValue().equals(c.getData().getNodeTypeCode())) {

                        //根据已有材料复制一份
                        AeaItemMat aeaItemMat = copyNewOne(c.getData().getId());
                        insertAeaItemInout(aeaItemMat.getMatId(), itemVerId, stateVerId, currentNodeParentId);
                        c.getData().setId(aeaItemMat.getMatId());

//                    } else if (MindType.CERTIFICATE.getValue().equals(c.getData().getNodeTypeCode())) {
//
////                        insertAeaCertInout(c, itemVerId, stateVerId, currentNodeParentId);
//
//                    }else if (MindType.FORM.getValue().equals(c.getData().getNodeTypeCode())) {

                    }
                } else {//todo 全量更新 暂时这样做
                    updateNodeByCheckEveryone(itemVerId, c.getData());
                }
                //进行递归新增
                updateItemTree(c, itemVerId, stateVerId, stateSeq, needRecursionCreate, needRecursionOccurrence);
                if (needRecursionOccurrence.equals(c.getData().getId())) {
                    needRecursionCreate = false;
                    needRecursionOccurrence = "";
                }
            }
        }
        stateSeq.remove(stateSeq.size() - 1);
    }

    private AeaItemMat copyNewOne(String id) throws Exception{

        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemMat aeaItemMat = aeaItemMatMapper.selectOneById(id);
        String matCode = autoCodeNumberService.generate("AEA-ITEM-MAT-CODE", rootOrgId);
        AeaItemMat newOne = aeaItemMat.copyOne(matCode);
        newOne.setRootOrgId(rootOrgId);
        aeaItemMatMapper.insertAeaItemMat(newOne);
        return newOne;
    }

    private void updateNodeByCheckEveryone(String itemVerId, MindExportData data) {

        //情形更新
        if (AeaMindConst.MIND_NODE_TYPE_CODE_SITUATION.equals(data.getNodeTypeCode())) {

            AeaItemState updateItemState = buildAeaItemState4Update(data);
            this.updateAeaItemState(updateItemState);

        //材料更新
        } else if (MindType.MATERIAL.getValue().equals(data.getNodeTypeCode())
                 ||MindType.CERTIFICATE.getValue().equals(data.getNodeTypeCode())
                 ||MindType.FORM.getValue().equals(data.getNodeTypeCode())) {

            AeaItemMat mat = new AeaItemMat();
            mat.setMatId(data.getId());
            mat.setMatName(StringUtils.isNotBlank(data.getText()) ? data.getText() : data.getName());
            mat.setMatMemo(data.getNote());
            aeaItemMatMapper.updateAeaItemMat(mat);

//        //证照新增
//        } else if (MindType.CERTIFICATE.getValue().equals(data.getNodeTypeCode())) {
//
//            AeaCert cert = new AeaCert();
//            cert.setCertId(data.getId());
//            cert.setCertName(StringUtils.isNotBlank(data.getText()) ? data.getText() : data.getName());
//            cert.setCertMemo(data.getNote());
//            aeaCertMapper.updateOne(cert);
        }
    }

    private AeaItemState buildAeaItemState4Update(MindExportData data) {

        AeaItemState updateItemState = new AeaItemState();
        updateItemState.setItemStateId(data.getId());
        updateItemState.setStateName(StringUtils.isNotBlank(data.getText()) ? data.getText() : data.getName());

        AeaItemState aeaItemStateById = this.getAeaItemStateById(data.getId());
        if (StringUtils.isNotBlank(data.getProgress()) || StringUtils.isNotBlank(data.getPriority()) ||
                (aeaItemStateById != null && StringUtils.isBlank(aeaItemStateById.getParentStateId()))) {
            updateItemState.setIsQuestion(Status.ON);
        } else {
            AeaItemState parentState = aeaItemStateMapper.getAeaItemStateById(aeaItemStateById.getParentStateId());
            if (parentState != null && Status.ON.equals(parentState.getIsQuestion())) {
                updateItemState.setIsQuestion(Status.OFF);
            } else {
                updateItemState.setIsQuestion(Status.ON);
            }
            updateItemState.setIsQuestion(Status.OFF);
        }
        updateItemState.setAnswerType(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_REQUIRED.equals(data.getPriority()) ? AnswerType.MULTIPLE.getValue() : "");//2：多选
        updateItemState.setMustAnswer(AeaMindConst.MIND_NODE_PROGRESS_MAPPING_REQUIRED.equals(data.getProgress()) ? Status.ON : "");
        if (updateItemState.getIsQuestion().equals(Status.ON) && "".equals(updateItemState.getAnswerType())) {
            updateItemState.setAnswerType(AnswerType.SINGLE.getValue());
        }
        if (updateItemState.getIsQuestion().equals(Status.ON) && "".equals(updateItemState.getMustAnswer())) {
            updateItemState.setMustAnswer(Status.OFF);
        }

        updateItemState.setStateMemo(data.getNote());//备注

        if (Status.ON.equals(data.getLinkProcessStart())) {
            updateItemState.setIsProcStartCond(Status.ON);
        } else {
            updateItemState.setIsProcStartCond(Status.OFF);
        }

        return updateItemState;
    }

    //新增情形
    private AeaItemState insertAeaState(MindExportObj c, String itemVerId, String stateVerId, String parentId, List<String> stateSeq) {

        String itemId = aeaItemVerMapper.getAeaItemVerById(itemVerId).getItemId();
        AeaItemState aeaItemState = new AeaItemState();
        aeaItemState.setItemStateId(UUID.randomUUID().toString());
        aeaItemState.setItemId(itemId);
        aeaItemState.setItemVerId(itemVerId);
        aeaItemState.setStateVerId(stateVerId);
        aeaItemState.setStateName(StringUtils.isNotBlank(c.getData().getText()) ? c.getData().getText() : c.getData().getName());
        aeaItemState.setStateEl("");
        aeaItemState.setUseEl(Status.OFF);
        aeaItemState.setParentStateId(itemVerId.equals(parentId) ? null : parentId);
        aeaItemState.setStateSeq(getStateSeq(stateSeq, aeaItemState.getItemStateId()));
        if (StringUtils.isNotBlank(c.getData().getProgress()) || StringUtils.isNotBlank(c.getData().getPriority())) {
            aeaItemState.setIsQuestion(Status.ON);
        } else {
            //是事项下的一级情形，则肯定是问题
            if (itemVerId.equals(parentId)) {
                aeaItemState.setIsQuestion(Status.ON);
            } else {
                AeaItemState parentState = aeaItemStateMapper.getAeaItemStateById(parentId);
                if (parentState != null && Status.ON.equals(parentState.getIsQuestion())) {
                    aeaItemState.setIsQuestion(Status.OFF);
                } else {
                    aeaItemState.setIsQuestion(Status.ON);
                }
            }
        }
        //2：多选
        aeaItemState.setAnswerType(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_REQUIRED.equals(c.getData().getPriority()) ? AnswerType.MULTIPLE.getValue() : "");
        aeaItemState.setMustAnswer(AeaMindConst.MIND_NODE_PROGRESS_MAPPING_REQUIRED.equals(c.getData().getProgress()) ? Status.ON : "");
        if (aeaItemState.getIsQuestion().equals(Status.ON) && "".equals(aeaItemState.getAnswerType())) {
            aeaItemState.setAnswerType(AnswerType.SINGLE.getValue());
        }
        if (aeaItemState.getIsQuestion().equals(Status.ON) && "".equals(aeaItemState.getMustAnswer())) {
            aeaItemState.setMustAnswer(Status.OFF);
        }
        aeaItemState.setStateMemo(c.getData().getNote());

        if (Status.ON.equals(c.getData().getLinkProcessStart())) {
            aeaItemState.setIsProcStartCond(Status.ON);
        } else {
            aeaItemState.setIsProcStartCond(Status.OFF);
        }
        this.saveAeaItemState(aeaItemState);
        return aeaItemState;
    }

    private String getStateSeq(List<String> stateSeq, String itemStateId) {
        StringBuilder seq = new StringBuilder();
        stateSeq.forEach(seq::append);
        return seq.toString() + itemStateId + CommonConstant.SEQ_SEPARATOR;
    }

    /**
     * 新增证照关联
     *
     * @param c
     * @param itemVerId
     * @param stateVerId
     * @param parentId
     */
    private void insertAeaCertInout(MindExportObj c, String itemVerId, String stateVerId, String parentId) {

        String itemId = aeaItemVerMapper.getAeaItemVerById(itemVerId).getItemId();
        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setInoutId(UUID.randomUUID().toString());
        aeaItemInout.setItemId(itemId);
        aeaItemInout.setItemVerId(itemVerId);
        aeaItemInout.setStateVerId(stateVerId);
        aeaItemInout.setIsOwner(Status.ON);
        aeaItemInout.setIsInput(InOutStatus.IN.getValue());
        aeaItemInout.setFileType(MindType.CERTIFICATE.getValue());
        aeaItemInout.setCertId(c.getData().getId());
        aeaItemInout.setCreater(SecurityContext.getCurrentUserId());
        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemInout.setCreateTime(new Date());
        //挂靠在事项下的材料
        if (itemVerId.equals(parentId)) {
            aeaItemInout.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());
        } else {//情形下的证照
            aeaItemInout.setIsStateIn(NeedStateStatus.NEED_STATE.getValue());
            aeaItemInout.setItemStateId(parentId);
        }
        aeaItemInoutMapper.insertAeaItemInout(aeaItemInout);
    }

    //新增材料关联
    private void insertAeaItemInout(String matId, String itemVerId, String stateVerId, String parentId) {

        String itemId = aeaItemVerMapper.getAeaItemVerById(itemVerId).getItemId();
        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setInoutId(UUID.randomUUID().toString());
        aeaItemInout.setItemId(itemId);
        aeaItemInout.setItemVerId(itemVerId);
        aeaItemInout.setStateVerId(stateVerId);
        aeaItemInout.setIsOwner(Status.ON);
        aeaItemInout.setIsInput(InOutStatus.IN.getValue());
        aeaItemInout.setFileType(MindType.MATERIAL.getValue());
        aeaItemInout.setMatId(matId);
        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemInout.setCreater(SecurityContext.getCurrentUserId());
        aeaItemInout.setCreateTime(new Date());
        //挂靠在事项下的材料
        if (itemVerId.equals(parentId)) {
            aeaItemInout.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());
        } else {//情形下的材料
            aeaItemInout.setIsStateIn(NeedStateStatus.NEED_STATE.getValue());
            aeaItemInout.setItemStateId(parentId);
        }
        aeaItemInoutMapper.insertAeaItemInout(aeaItemInout);
    }

    public String copyMaxPublishedOrTestRunStateVer(String itemVerId){

        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        List<AeaItemStateVer> vers = aeaItemStateVersionMapper.listTestRunOrPublishedItemStateVer(itemVerId, SecurityContext.getCurrentOrgId());
        AeaItemStateVer lastVer = null;
        if(vers!=null&&vers.size()>0){
            lastVer = vers.get(0);
        }else{
            throw new IllegalArgumentException("无法获取最新试运行或已发布版本!");
        }
        return copyItemStateVerRelData(itemVerId, null, lastVer.getItemStateVerId(), null);
    }


    /**
     * 复制事项情形版本下的数据并产生新版本
     *
     * @param itemVerId
     * @param newItemVerId 当此项不为空，针对事项版本复制，为空就是事项情形版本复制了
     * @param stateVerId
     * @param newItemId 新的事项itemId
     * @return
     */
    public String copyItemStateVerRelData(String itemVerId, String newItemVerId, String stateVerId, String newItemId){

        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        if(StringUtils.isBlank(stateVerId)){
            throw new IllegalArgumentException("情形版本stateVerId为空!");
        }

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        // 最新的已经发布、试运行情形版本
        AeaItemStateVer stateVer = aeaItemStateVersionMapper.getAeaItemStateVerById(stateVerId);
        Assert.notNull(stateVer,"无法获取当前'stateVerId="+ stateVerId +"'情形版本");

        // 处理序列,某版本下情形复制需要更新序列
        AeaItemSeq seq = aeaItemSeqMapper.getSeqByItemIdAndVerId(stateVer.getItemId(), itemVerId, rootOrgId);
        Assert.notNull(seq,"无法获取当前'itemId="+ stateVer.getItemId() +",newItemVerId='"+ itemVerId +"最新情形版本序列");

        // 处理最大版本号
        Double stateVerNum = seq.getStateVerMax() + AeaItemStateVer.INIT_VER;
        if(StringUtils.isNotBlank(newItemVerId)){
            stateVerNum = stateVer.getVerNum();
        }else{
            seq.setStateVerMax(stateVerNum);
            seq.setModifier(userId);
            seq.setModifyTime(new Date());
            aeaItemSeqMapper.updateAeaItemSeq(seq);
        }

        // 构建新的情形版本
        AeaItemStateVer copiedVersion = stateVer.copyAndIncVersion(stateVerNum);
        if(StringUtils.isNotBlank(newItemVerId)){
            copiedVersion.setItemVerId(newItemVerId);
            copiedVersion.setVerStatus(stateVer.getVerStatus());
        }
        if(StringUtils.isNotBlank(newItemId)){
            copiedVersion.setItemId(newItemId);
        }
        aeaItemStateVersionMapper.insertAeaItemStateVer(copiedVersion);

        // 情形版本下全部情形
        List<AeaItemState> stateList = preFetchAllStates(itemVerId, stateVerId, rootOrgId);
        // 用来保存新旧的stateId的对应关系
        Map<String, String> map = new HashMap<String, String>();
        if (stateList != null && stateList.size() > 0) {
            for (AeaItemState state : stateList) {
                String oldStateId = state.getItemStateId();
                String newStateId = UUID.randomUUID().toString();
                state.setItemStateId(newStateId);
                if(StringUtils.isNotBlank(newItemVerId)){
                    state.setItemVerId(newItemVerId);
                }
                if(StringUtils.isNotBlank(newItemId)){
                    state.setItemId(newItemId);
                }
                state.setStateVerId(copiedVersion.getItemStateVerId());
                state.setStateSeq(state.getStateSeq().replace(oldStateId, newStateId));
                map.put(oldStateId, newStateId);
            }
            // 替换新的父级以及序列
            for (String key : map.keySet()) {
                for (AeaItemState s : stateList) {
                    if (StringUtils.isNotBlank(s.getParentStateId())&& s.getParentStateId().equals(key)) {
                        s.setParentStateId(map.get(key));
                    }
                    if (StringUtils.isNotBlank(s.getStateSeq())&& s.getStateSeq().indexOf(key) != -1) {
                        s.getStateSeq().replace(key, map.get(key));
                    }
                }
            }
            for (AeaItemState ss : stateList) {
                aeaItemStateMapper.insertAeaItemState(ss);
            }
            // 情形版本下全部情形输入
            AeaItemInout inout = new AeaItemInout();
            inout.setItemVerId(itemVerId);
            inout.setStateVerId(stateVerId);
            inout.setRootOrgId(rootOrgId);
            inout.setIsInput(InOutStatus.IN.getValue());
            inout.setIsStateIn(NeedStateStatus.NEED_STATE.getValue());
            inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            List<AeaItemInout> inList = aeaItemInoutMapper.listAeaItemInout(inout);
            if (inList != null && inList.size() > 0) {
                for (AeaItemInout in : inList) {
                    AeaItemInout newIn = in.copyOne();
                    // 替换情形版本字段
                    if(StringUtils.isNotBlank(newItemVerId)){
                        newIn.setItemVerId(newItemVerId);
                    }
                    newIn.setStateVerId(copiedVersion.getItemStateVerId());
                    for (String key : map.keySet()) {
                        if(StringUtils.isNotBlank(newIn.getItemStateId())&&newIn.getItemStateId().equals(key)) {
                            // 替换情形id字段
                            newIn.setItemStateId(map.get(key));
                            break;
                        }
                    }
                    aeaItemInoutMapper.insertAeaItemInout(newIn);
                }
            }

            // 情形版本下全部情形表单
//            AeaItemStateForm stateForm = new AeaItemStateForm();
//            stateForm.setItemVerId(itemVerId);
//            stateForm.setItemStateVerId(stateVerId);
//            stateForm.setIsStateForm(NeedStateStatus.NEED_STATE.getValue());
//            List<AeaItemStateForm> formList = aeaItemStateFormMapper.listAeaItemStateForm(stateForm);
//            if (formList != null && formList.size() > 0) {
//                for (AeaItemStateForm form : formList) {
//                    AeaItemStateForm newForm = form.copyOne();
//                    // 替换情形版本字段
//                    if(StringUtils.isNotBlank(newItemVerId)){
//                        newForm.setItemVerId(newItemVerId);
//                    }
//                    newForm.setItemStateVerId(copiedVersion.getItemStateVerId());
//                    for (String key : map.keySet()) {
//                        if(StringUtils.isNotBlank(newForm.getItemStateId())&&newForm.getItemStateId().equals(key)) {
//                            // 替换情形id字段
//                            newForm.setItemStateId(map.get(key));
//                            break;
//                        }
//                    }
//                    aeaItemStateFormMapper.insertAeaItemStateForm(newForm);
//                }
//            }
        }

        // 情形版本下通用输入
        AeaItemInout inout = new AeaItemInout();
        inout.setItemVerId(itemVerId);
        inout.setStateVerId(stateVerId);
        inout.setRootOrgId(rootOrgId);
        inout.setIsInput(InOutStatus.IN.getValue());
        inout.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());
        inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaItemInout> inList = aeaItemInoutMapper.listAeaItemInout(inout);
        if (inList != null && inList.size() > 0) {
            for (AeaItemInout in : inList) {
                AeaItemInout newIn = in.copyOne();
                // 替换情形版本字段
                if (StringUtils.isNotBlank(newItemVerId)) {
                    newIn.setItemVerId(newItemVerId);
                }
                newIn.setStateVerId(copiedVersion.getItemStateVerId());
                aeaItemInoutMapper.insertAeaItemInout(newIn);
            }
        }

        // 情形版本下通用表单
//        AeaItemStateForm stateForm = new AeaItemStateForm();
//        stateForm.setItemVerId(itemVerId);
//        stateForm.setItemStateVerId(stateVerId);
//        stateForm.setIsStateForm(NeedStateStatus.NOT_NEED_STATE.getValue());
//        List<AeaItemStateForm> formList = aeaItemStateFormMapper.listAeaItemStateForm(stateForm);
//        if (formList != null && formList.size() > 0) {
//            for (AeaItemStateForm form : formList) {
//                AeaItemStateForm newForm = form.copyOne();
//                // 替换情形版本字段
//                if(StringUtils.isNotBlank(newItemVerId)){
//                    newForm.setItemVerId(newItemVerId);
//                }
//                newForm.setItemStateVerId(copiedVersion.getItemStateVerId());
//                aeaItemStateFormMapper.insertAeaItemStateForm(newForm);
//            }
//        }
        return copiedVersion.getItemStateVerId();
    }

    /**
     * 复制情形信息
     *
     * @param itemVerId    旧事项版本id
     * @param newItemVerId 新事项版本id（在事项复制版本时使用该字段，其他地方可设置为空）
     * @param stateVerId   情形版本id
     * @return
     */
    public String copyItemTreeData4SpecificStateVer(String itemVerId, String newItemVerId, String stateVerId) {

        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        if(StringUtils.isBlank(stateVerId)){
            throw new IllegalArgumentException("情形版本stateVerId为空!");
        }
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();

        // 最新的已经发布、试运行情形版本
        AeaItemStateVer stateVersion = aeaItemStateVersionMapper.getAeaItemStateVerById(stateVerId);
        // 事项复制情形，情形默认序号是0.01; 情形版本复制，情形版本和序号都要增加
        Double stateVerNum = AeaItemStateVer.INIT_VER;
        if(StringUtils.isBlank(newItemVerId)){
            stateVerNum = stateVersion.getVerNum() + AeaItemStateVer.INIT_VER ;
        }
        // 构建新的情形版本
        AeaItemStateVer copiedVersion = stateVersion.copyAndIncVersion(stateVerNum);
        //如果新的事项版本id不为空，则进行重新赋值
        if (StringUtils.isNotBlank(newItemVerId)) {
            copiedVersion.setItemVerId(newItemVerId);
        }
        aeaItemStateVersionMapper.insertAeaItemStateVer(copiedVersion);

        // 处理序列,某版本下情形复制需要更新序列
        if (StringUtils.isBlank(newItemVerId)) {
            AeaItemSeq seq = aeaItemSeqMapper.getSeqByItemIdAndVerId(copiedVersion.getItemId(), copiedVersion.getItemVerId(), rootOrgId);
            seq.setStateVerMax(stateVerNum);
            seq.setModifier(userId);
            seq.setModifyTime(new Date());
            aeaItemSeqMapper.updateAeaItemSeq(seq);
        }

        // 复制情形数据
        Map<String, String> oldAndNewStateIdMap = copyStates(itemVerId, newItemVerId, copiedVersion.getItemStateVerId(), stateVersion);
        // 复制情形输入数据
        copyInoutAndMatCerts(itemVerId, newItemVerId, stateVerId, copiedVersion.getItemStateVerId(), oldAndNewStateIdMap);
        // 复制情形表单关联数据
        copyForms(itemVerId, newItemVerId, stateVerId, copiedVersion.getItemStateVerId(), oldAndNewStateIdMap);
        // 事项其他外围表没有复制 ====
        
        return copiedVersion.getItemStateVerId();
    }

    private Map<String, String> copyStates(String itemVerId, String newItemVerId, String newStateVerId, AeaItemStateVer originStateVersion) {

        Map<String, String> oldAndNewStateIdMap = new HashMap<>();
        // 事项情形版本下的所有情形
        List<AeaItemState> allStates = preFetchAllStates(itemVerId, originStateVersion.getItemStateVerId(), SecurityContext.getCurrentOrgId());
        if (CollectionUtils.isEmpty(allStates)) {
            return oldAndNewStateIdMap;
        }
        int beforeSortSize = allStates.size();
        List<AeaItemState> treeStates = new ArrayList<>(allStates.size());
        treeStates = sortToTree(allStates, treeStates, null);
        Assert.isTrue(beforeSortSize == treeStates.size(), "情形树结构排列有误, 联系管理员!");
        List<AeaItemState> copiedStates = new ArrayList<>();
        treeStates.forEach(s -> {
            log.info("id: " + s.getItemStateId() + ", parentStateId: " + s.getParentStateId());
            AeaItemState copy = s.copyOne();
            oldAndNewStateIdMap.put(s.getItemStateId(), copy.getItemStateId());
            //复制后的情形版本id
            copy.setStateVerId(newStateVerId);
            copy.setParentStateId(oldAndNewStateIdMap.get(s.getParentStateId()));
            //如果新的事项版本id不为空，则进行重新赋值
            if (StringUtils.isNotBlank(newItemVerId)) {
                copy.setItemVerId(newItemVerId);
            }
            copiedStates.add(copy);
        });
        Assert.isTrue(copiedStates.size() == beforeSortSize, "复制情形的情形数量不等");
        copiedStates.forEach(s -> aeaItemStateMapper.insertAeaItemState(s));
        return oldAndNewStateIdMap;
    }

    private List<AeaItemState> sortToTree(List<AeaItemState> allStates, List<AeaItemState> treeStates, String parentStateId) {

        if (noChildren(parentStateId, allStates)) {
            return treeStates;
        }
        List<String> stateIds = new ArrayList<>();
        Iterator<AeaItemState> it = allStates.iterator();
        while (it.hasNext()) {
            AeaItemState s = it.next();
            if (StringUtils.isBlank(s.getParentStateId()) || (StringUtils.isNotBlank(parentStateId)&& parentStateId.equals(s.getParentStateId()))) {
                treeStates.add(s);
                stateIds.add(s.getItemStateId());
                it.remove();
            }
        }
        stateIds.forEach(id -> sortToTree(Lists.newArrayList(allStates), treeStates, id));
        return treeStates;
    }

    private boolean noChildren(String parentStateId, List<AeaItemState> allStates) {

        List<AeaItemState> children = allStates.stream().filter(s -> {
            if (StringUtils.isBlank(parentStateId)) {
                return StringUtils.isBlank(s.getParentStateId());
            } else {
                return parentStateId.equals(s.getParentStateId());
            }
        }).collect(Collectors.toList());
        return children.isEmpty();
    }

    private void copyInoutAndMatCerts(String itemVerId, String newItemVerId, String originStateVerId, String newStateVerId, Map<String, String> oldAndNewStateIdMap) {

        AeaItemInout inout = new AeaItemInout();
        inout.setItemVerId(itemVerId);
        inout.setStateVerId(originStateVerId);
        inout.setIsInput(InOutStatus.IN.getValue());
        inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaItemInout> inouts = aeaItemInoutMapper.listAeaItemInout(inout);
        copyInouts(newItemVerId, newStateVerId, oldAndNewStateIdMap, inouts);
    }

    private void copyForms(String itemVerId, String newItemVerId, String originStateVerId, String newStateVerId, Map<String, String> oldAndNewStateIdMap) {

        AeaItemStateForm form = new AeaItemStateForm();
        form.setItemVerId(itemVerId);
        form.setItemStateVerId(originStateVerId);
        List<AeaItemStateForm> forms = aeaItemStateFormMapper.listAeaItemStateForm(form);
        copyForms(newItemVerId, newStateVerId, oldAndNewStateIdMap, forms);
    }

    private Map<String, String> copyMats(List<String> matIds) {

        Map<String, String> oldAndNewMatIdMap = new HashMap<>();
        if (CollectionUtils.isEmpty(matIds)) {
            return oldAndNewMatIdMap;
        }
        ArrayList<String> list = new ArrayList<String>();
        String[] matIdArr = new String[matIds.size()];
        list.toArray(matIdArr);
        List<AeaItemMat> copiedMats = new ArrayList<>();
        List<AeaItemMat> mats = aeaItemMatMapper.listAeaItemMatByIds(matIdArr);
        mats.forEach(m -> {
            try {
                AeaItemMat copiedOne = m.copyOne(autoCodeNumberService.generate("AEA-ITEM-MAT-CODE", SecurityContext.getCurrentOrgId()));
                oldAndNewMatIdMap.put(m.getMatId(), copiedOne.getMatId());
                copiedMats.add(copiedOne);
            }catch (Exception e){
                new RuntimeException(e);
            }
        });
        copiedMats.forEach(m -> aeaItemMatMapper.insertAeaItemMat(m));
        return oldAndNewMatIdMap;
    }

    private void copyInouts(String newItemVerId, String newStateVerId, Map<String, String> oldAndNewStateIdMap, List<AeaItemInout> inouts) {

        if (CollectionUtils.isEmpty(inouts)) {
            return;
        }
        List<AeaItemInout> copiedInouts = new ArrayList<>();
        inouts.forEach(inout -> {
            AeaItemInout copiedOne = inout.copyOne();
            copiedOne.setStateVerId(newStateVerId);
            copiedOne.setItemStateId(oldAndNewStateIdMap.get(inout.getItemStateId()));
            //如果新的事项版本id不为空，则进行重新赋值
            if (StringUtils.isNotBlank(newItemVerId)) {
                copiedOne.setItemVerId(newItemVerId);
            }
            copiedInouts.add(copiedOne);
        });
        copiedInouts.forEach(in -> aeaItemInoutMapper.insertAeaItemInout(in));
    }

    private void copyForms(String newItemVerId, String newStateVerId, Map<String, String> oldAndNewStateIdMap, List<AeaItemStateForm> forms) {

        if (CollectionUtils.isEmpty(forms)) {
            return;
        }
        List<AeaItemStateForm> copiedForms = new ArrayList<>();
        forms.forEach(form -> {
            AeaItemStateForm copiedOne = form.copyOne();
            copiedOne.setItemStateVerId(newStateVerId);
            copiedOne.setItemStateId(oldAndNewStateIdMap.get(form.getItemStateId()));
            //如果新的事项版本id不为空，则进行重新赋值
            if (StringUtils.isNotBlank(newItemVerId)) {
                copiedOne.setItemVerId(newItemVerId);
            }
            copiedForms.add(copiedOne);
        });
        copiedForms.forEach(in -> aeaItemStateFormMapper.insertAeaItemStateForm(in));
    }

    @Autowired
    public void setAeaItemStateMapper(AeaItemStateMapper aeaItemStateMapper) {
        this.aeaItemStateMapper = aeaItemStateMapper;
    }

    @Autowired
    public void setAeaItemInoutMapper(AeaItemInoutMapper aeaItemInoutMapper) {
        this.aeaItemInoutMapper = aeaItemInoutMapper;
    }

    @Autowired
    public void setAeaItemMatMapper(AeaItemMatMapper aeaItemMatMapper) {
        this.aeaItemMatMapper = aeaItemMatMapper;
    }

    @Autowired
    public void setAeaItemMapper(AeaItemMapper aeaItemMapper) {
        this.aeaItemMapper = aeaItemMapper;
    }

    @Autowired
    public void setAeaCertMapper(AeaCertMapper aeaCertMapper) {
        this.aeaCertMapper = aeaCertMapper;
    }

    @Autowired
    public void setAutoCodeNumberService(AutoCodeNumberService autoCodeNumberService) {
        this.autoCodeNumberService = autoCodeNumberService;
    }

    @Autowired
    public void setAeaItemMatAdminService(AeaItemMatAdminService aeaItemMatAdminService) {
        this.aeaItemMatAdminService = aeaItemMatAdminService;
    }

    @Autowired
    public void setAeaHiItemInoutinstMapper(AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper) {
        this.aeaHiItemInoutinstMapper = aeaHiItemInoutinstMapper;
    }

    @Autowired
    public void setAeaItemStateVersionAdminService(AeaItemStateVersionAdminService aeaItemStateVersionAdminService) {
        this.aeaItemStateVersionAdminService = aeaItemStateVersionAdminService;
    }

    @Autowired
    public void setAeaItemBasicMapper(AeaItemBasicMapper aeaItemBasicMapper) {
        this.aeaItemBasicMapper = aeaItemBasicMapper;
    }

    @Autowired
    public void setAeaItemStateVersionMapper(AeaItemStateVerMapper aeaItemStateVersionMapper) {
        this.aeaItemStateVersionMapper = aeaItemStateVersionMapper;
    }

    @Autowired
    public void setAeaItemVerMapper(AeaItemVerMapper aeaItemVerMapper) {
        this.aeaItemVerMapper = aeaItemVerMapper;
    }

    @Autowired
    public void setAeaItemStateFormMapper(AeaItemStateFormMapper aeaItemStateFormMapper) {
        this.aeaItemStateFormMapper = aeaItemStateFormMapper;
    }

    @Autowired
    private void aeaItemSeqMapper(AeaItemSeqMapper aeaItemSeqMapper){
        this.aeaItemSeqMapper = aeaItemSeqMapper;
    }
}

