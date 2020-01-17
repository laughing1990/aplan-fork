package com.augurit.aplanmis.common.service.itemFill.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.AeaHiItemFillStateEnum;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 事项容缺补齐实例表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaHiItemFillServiceImpl implements AeaHiItemFillService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemFillServiceImpl.class);

    @Autowired
    private AeaHiItemFillMapper aeaHiItemFillMapper;

    @Autowired
    private AeaHiItemFillDueIninstMapper aeaHiItemFillDueIninstMapper;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaHiApplyinstCorrectService aeaHiApplyinstCorrectService;

    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    public void saveAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        aeaHiItemFillMapper.insertAeaHiItemFill(aeaHiItemFill);
    }

    public void updateAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        aeaHiItemFillMapper.updateAeaHiItemFill(aeaHiItemFill);
    }

    public void deleteAeaHiItemFillById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemFillMapper.deleteAeaHiItemFill(id);
    }

    public PageInfo<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiItemFill> list = aeaHiItemFillMapper.listAeaHiItemFill(aeaHiItemFill);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemFill>(list);
    }

    public AeaHiItemFill getAeaHiItemFillById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemFillMapper.getAeaHiItemFillById(id);
    }

    public List<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        List<AeaHiItemFill> list = aeaHiItemFillMapper.listAeaHiItemFill(aeaHiItemFill);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 创建容缺补齐实例，包括补齐的详细信息
     * 佛山使用
     * @param applyinstId
     * @throws Exception
     */
    @Override
    public void createAeaHiItemFill(String applyinstId) throws Exception {
        //查询申报容缺未上传的材料信息，这里调用的是原 材料补全的接口
        MatCorrectInfoDto lackMats = aeaHiApplyinstCorrectService.getLackMatsByApplyinstId(applyinstId);
        if(lackMats != null && lackMats.getAllMats() != null && lackMats.getAllMats().size() > 0){
            List<AeaItemMat> allMats = lackMats.getAllMats();
            List matIds = Lists.newArrayList();
            Map<String,AeaItemMat> matMap = new HashMap<>();
            for(int i=0,len=allMats.size(); i<len; i++){
                AeaItemMat aeaItemMat = allMats.get(i);
                if("0".equals(aeaItemMat.getAttIsRequire()) && (aeaItemMat.getAttMatinstList() == null || aeaItemMat.getAttMatinstList().size() == 0)){
                    //如果材料是 电子件容缺 且 暂未上传电子件的，才进行容缺补齐
                    String matId = aeaItemMat.getMatId();
                    matIds.add(matId);
                    matMap.put(matId,aeaItemMat);
                }
            }
            List<AeaHiIteminst> iteminstList = lackMats.getIteminstList();

            //登录信息
            OpuOmUser currentUser = SecurityContext.getCurrentUser();
            String currentOrgId = SecurityContext.getCurrentOrgId();
            Date currentTime = new Date();
            for(AeaHiIteminst iteminst : iteminstList){
                //找到需要容缺补齐的 事项输入材料定义
                List<AeaItemInout> inouts = aeaItemInoutMapper.listAeaItemInoutByMatIds(matIds, iteminst.getItemVerId(), "1");
                List<AeaHiItemFillDueIninst> fillDueIninsts = Lists.newArrayList();
                List<AeaHiItemInoutinst> inoutinstList = Lists.newArrayList();
                String fillId = UUID.randomUUID().toString();
                //先判断当前事项是否存在容缺材料
                for(AeaItemInout itemInout : inouts){
                    if(iteminst.getItemVerId().equals(itemInout.getItemVerId())){
                        //创建事项输入材料实例
                        AeaHiItemInoutinst inoutinst = new AeaHiItemInoutinst();
                        inoutinst.setInoutinstId(UUID.randomUUID().toString());
                        inoutinst.setIteminstId(iteminst.getIteminstId());
                        inoutinst.setItemInoutId(itemInout.getInoutId());
                        inoutinst.setIsCollected("0");
                        inoutinst.setIsParIn("0");
                        inoutinst.setCreater(currentUser.getUserName());
                        inoutinst.setCreateTime(currentTime);
                        inoutinst.setRootOrgId(currentOrgId);
                        inoutinstList.add(inoutinst);

                        //创建一个容缺材料实例
                        AeaHiItemFillDueIninst temp = new AeaHiItemFillDueIninst();
                        temp.setDueIninstId(UUID.randomUUID().toString());
                        temp.setFillId(fillId);
                        temp.setInoutinstId(inoutinst.getInoutinstId());
                        temp.setIsNeedAtt("1");
                        //TODO 补齐截止期限类型的值？
                        temp.setTimeLimitType("0");
                        temp.setIsPass("0");
                        temp.setAttCount(0l);
                        AeaItemMat itemMat = matMap.get(itemInout.getMatId());
                        if(itemMat != null){
                            temp.setPaperCount(itemMat.getDuePaperCount());
                            temp.setCopyCount(itemMat.getDueCopyCount());
                        }
                        temp.setRootOrgId(currentOrgId);
                        temp.setCreater(currentUser.getUserId());
                        temp.setCreateTime(currentTime);
                        fillDueIninsts.add(temp);
                    }
                }
                if(fillDueIninsts.size() > 0){
                    //存在则创建容缺实例
                    AeaHiItemFill temp = new AeaHiItemFill();
                    temp.setFillId(fillId);
                    temp.setIteminstId(iteminst.getIteminstId());
                    temp.setApplyinstId(applyinstId);
                    temp.setProjInfoId(lackMats.getProjInfoId());
                    temp.setChargeOrgId(iteminst.getApproveOrgId());
                    temp.setChargeOrgName(iteminst.getApproveOrgName());
                    temp.setLastTipsCount(0L);
                    temp.setFillState(AeaHiItemFillStateEnum.UN_START.getValue());
                    temp.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                    temp.setRootOrgId(currentOrgId);
                    temp.setCreater(currentUser.getUserId());
                    temp.setCreateTime(currentTime);
                    temp.setModifyTime(currentTime);
                    aeaHiItemFillMapper.insertAeaHiItemFill(temp);
                    //插入容缺事项输入材料实例
                    aeaHiItemInoutinstMapper.batchInsertAeaHiItemInoutinst(inoutinstList);
                    //插入事项容缺 要求补齐材料实例
                    aeaHiItemFillDueIninstMapper.batchInsertAeaHiItemFillDueIninst(fillDueIninsts);
                }
            }
        }
    }

    public PageInfo<AeaHiItemFill> listItemFills(AeaHiItemFill aeaHiItemFill, Page page)throws Exception{
        String currentUserId = SecurityContext.getCurrentUserId();
        //查询所属部门的容缺材料审核
        List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(currentUserId);
        if(opuOmOrgs.size() == 1){
            aeaHiItemFill.setChargeOrgId(opuOmOrgs.get(0).getOrgId());
        }else{
            List<String> orgIds = opuOmOrgs.stream().map(OpuOmOrg::getOrgId).collect(Collectors.toList());
            aeaHiItemFill.setOrgIds(orgIds.toArray(new String[orgIds.size()]));
        }
        PageHelper.startPage(page);
        List<AeaHiItemFill> aeaHiItemFills = aeaHiItemFillMapper.listAeaHiItemFillsByCondition(aeaHiItemFill);
        if(aeaHiItemFills != null && aeaHiItemFills.size() > 0){
            aeaHiItemFills.forEach(fill ->{
                fill.setApproveUser(true);
                String opsUserId = fill.getOpsUserId();
                if(StringUtils.isNotBlank(opsUserId) && !opsUserId.equals(currentUserId)){
                    fill.setApproveUser(false);
                }
            });
        }
        logger.debug("成功执行查询list！！");
        return new PageInfo<>(aeaHiItemFills);
    }

    public AeaHiItemFill getAeaHiItemFillDetail(String fillId) throws Exception{
        AeaHiItemFill fillDetail = aeaHiItemFillMapper.getAeaHiItemFillDetail(fillId);
        List<AeaHiItemFillDueIninst> dueIninstList = aeaHiItemFillDueIninstMapper.listAeaHiItemFillDueIninstByFillId(fillId);
        if(dueIninstList != null && dueIninstList.size() > 0){
            List<String> dueIninstIds = dueIninstList.stream().map(AeaHiItemFillDueIninst::getDueIninstId).collect(Collectors.toList());
            List<BscAttForm> forms = fileUtilsService.getAttachmentsByRecordId(dueIninstIds.toArray(new String[dueIninstIds.size()]),
                    "AEA_HI_ITEM_FILL_DUE_ININST", "DUE_ININST_ID");
            if(forms != null && forms.size() > 0){
                dueIninstList.forEach(dueIninst ->{
                    List<BscAttDetail> detailList = new ArrayList<>();
                    forms.forEach(form ->{
                        if(form.getRecordId().equals(dueIninst.getDueIninstId())){
                            detailList.add(form);
                        }
                    });
                    dueIninst.setDetailList(detailList);
                });
            }
        }
        fillDetail.setItemFillDueIninstList(dueIninstList);
        return fillDetail;
    }

    public void saveLeranceAproveOpinion(List<AeaHiItemFillDueIninst> fillDueList) throws Exception{
        if(fillDueList != null && fillDueList.size() > 0){
            String fillId = fillDueList.get(0).getFillId();
            AeaHiItemFill aeaHiItemFill = aeaHiItemFillMapper.getAeaHiItemFillDetail(fillId);
            String opsUserId = aeaHiItemFill.getOpsUserId();
            OpuOmUser currentUser = SecurityContext.getCurrentUser();
            if(StringUtils.isNotBlank(opsUserId)){
                //查看审核人员是否同一个
                if(!currentUser.getUserId().equals(opsUserId)){
                    throw new RuntimeException("审批人已处理过该办件。已绑定审批人为：" + aeaHiItemFill.getOpsUserName());
                }
            }
            Date currDate = new Date();
            aeaHiItemFill.setOpsUserId(currentUser.getUserId());
            aeaHiItemFill.setOpsUserName(currentUser.getUserName());
            aeaHiItemFill.setLastOpsTime(currDate);
            aeaHiItemFill.setFillState(AeaHiItemFillStateEnum.UN_START.getValue());
            //清空提示数量
            aeaHiItemFill.setLastTipsCount(0l);
            aeaHiItemFill.setModifier(currentUser.getUserId());
            aeaHiItemFill.setModifyTime(currDate);

            List<String> inoutinstIdList = fillDueList.stream().map(AeaHiItemFillDueIninst::getInoutinstId).collect(Collectors.toList());
            List<AeaHiItemInoutinst> inoutinstList = aeaHiItemInoutinstMapper.listAeaHiItemInoutinstByInoutinstIds(inoutinstIdList.toArray(new String[inoutinstIdList.size()]));
            List<String> recordIds = fillDueList.stream().map(AeaHiItemFillDueIninst::getDueIninstId).collect(Collectors.toList());
            //查询材料实际上传的附件
            List<BscAttForm> forms = fileUtilsService.getAttachmentsByRecordId(recordIds.toArray(new String[recordIds.size()]),
                    "AEA_HI_ITEM_FILL_DUE_ININST", "DUE_ININST_ID");
            Map<String,String> map = new HashMap<>();
            Map<String,List<String>> matAttMap = new HashMap<>();
            List<AeaHiItemMatinst> matinstList = new ArrayList<>();
            boolean isOver = true;
            for(int i=0,len=fillDueList.size(); i<len; i++){
                AeaHiItemFillDueIninst aeaHiItemFillDueIninst = fillDueList.get(i);
                String isPass = aeaHiItemFillDueIninst.getIsPass();
                if("0".equals(isPass)){
                    //审核不通过。不删除附件，让企业自己手动删除。
                    isOver = false;
                    aeaHiItemFillDueIninst.setAttCount(0l);
                }else if("1".equals(isPass)){
                    //审核通过。
                    String dueIninstId = aeaHiItemFillDueIninst.getDueIninstId();
                    String inoutinstId = aeaHiItemFillDueIninst.getInoutinstId();
                    String matinstId = UUID.randomUUID().toString();
                    map.put(inoutinstId,matinstId);

                    AeaHiItemMatinst matinst = new AeaHiItemMatinst();
                    matinst.setMatinstId(matinstId);
                    matinst.setMatId(aeaHiItemFillDueIninst.getMatId());
                    matinst.setAttCount(aeaHiItemFillDueIninst.getAttCount());
                    matinst.setCreater(currentUser.getUserName());
                    matinst.setCreateTime(currDate);
                    matinst.setMatinstCode(aeaHiItemFillDueIninst.getMatCode());
                    matinst.setMatinstName(aeaHiItemFillDueIninst.getMatName());
                    matinst.setUnitInfoId(aeaHiItemFill.getUnitInfoId());
                    matinst.setProjInfoId(aeaHiItemFill.getProjInfoId());
                    matinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                    //默认都是单位
                    matinst.setMatinstSource("u");
                    //材料性质，m表示普通材料，c表示证照材料，f表示在线表单，of外部表单
                    matinst.setMatProp("m");

                    //一个材料有多个附件
                    List<String> detailIds = forms.stream().filter(form -> form.getRecordId().equals(dueIninstId))
                            .map(BscAttForm::getDetailId).collect(Collectors.toList());
                    matAttMap.put(matinstId,detailIds);
                    matinstList.add(matinst);
                }
            }

            if(inoutinstList != null && inoutinstList.size() > 0){
                inoutinstList.forEach(aeaHiItemInoutinst -> {
                    aeaHiItemInoutinst.setMatinstId(map.get(aeaHiItemInoutinst.getInoutinstId()));
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setModifier(currentUser.getUserName());
                    aeaHiItemInoutinst.setModifyTime(currDate);
                });
                //批量更新事项输入输出实例
                aeaHiItemInoutinstMapper.batchUpdateAeaHiItemInoutinst(inoutinstList);
            }

            //实例化aea_hi_item_matinst，并关联附件。
            aeaHiItemMatinstMapper.batchInsertAeaHiItemMatinst(matinstList);

            if(matAttMap.size() > 0){
                Set<Map.Entry<String, List<String>>> entrySet = matAttMap.entrySet();
                for(Map.Entry<String, List<String>> entry:entrySet){
                    String matinstId = entry.getKey();
                    List<String> detailIds = entry.getValue();
                    //底层包没有批量插入的方法，只能一个个的插入。
                    detailIds.forEach(detailId ->{
                        BscAttLink link = new BscAttLink();
                        link.setLinkId(UUID.randomUUID().toString());
                        link.setTableName("AEA_HI_ITEM_MATINST");
                        link.setPkName("MATINST_ID");
                        link.setRecordId(matinstId);
                        link.setDetailId(detailId);
                        link.setLinkType("a");
                        bscAttMapper.insertLink(link);
                    });
                }
            }
            if(isOver){
                //全部容缺材料都已补齐且审核通过。
                aeaHiItemFill.setFillState(AeaHiItemFillStateEnum.COMPLETED.getValue());
                aeaHiItemFill.setFillEndTime(currDate);
            }
            aeaHiItemFillMapper.updateAeaHiItemFill(aeaHiItemFill);
            aeaHiItemFillDueIninstMapper.batchUpdateAeaHiItemFillDueIninst(fillDueList);
        }
    }

}

