package com.augurit.aplanmis.front.approve.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.cert.AeaCertAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParShareMatAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.receive.vo.ConstructPermitVo;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.front.approve.vo.certinst.CertVo;
import com.augurit.aplanmis.front.approve.vo.certinst.CertinstVo;
import com.augurit.aplanmis.front.approve.vo.official.OfficialDocumentEditVo;
import com.augurit.aplanmis.front.approve.vo.official.OfficialDocumentInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OfficialDocumentService {

    private static final String TABLE_NAME = "AEA_HI_ITEM_MATINST";
    private static final String PK_NAME = "MATINST_ID";

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private AeaLinkmanInfoMapper linkmanInfoMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaParShareMatAdminService aeaParShareMatAdminService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;

    @Autowired
    private AeaCertMapper aeaCertMapper;
    @Autowired
    private AeaCertAdminService aeaCertAdminService;
    @Autowired
    private OpuOmUserInfoMapper opuOmUserInfoMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaExProjCertBuildMapper aeaExProjCertBuildMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    /**
     * @param applyinstId 申报实例id
     * @param iteminstId  事项实例ID
     */
    public List<Map<String, Object>> getUnitOfficialDocs(String applyinstId, String iteminstId) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<AeaHiIteminst> aeaHiItems = new ArrayList<>();
        // 部门人员用 iteminstId，窗口人员用 applyinstId
        if (StringUtils.isBlank(iteminstId)) {
            aeaHiItems.addAll(aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId));
        } else {
            aeaHiItems.add(aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId));
        }

        if (null != aeaHiItems && aeaHiItems.size() > 0) {
            for (AeaHiIteminst aeaHiIteminst : aeaHiItems) {
                Map<String, Object> map = new HashMap<>();
                List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listOfficialDocsByIteminstId(aeaHiIteminst.getIteminstId()); //查询批文批复
                List<OfficialDocumentInfoVo> collect = new ArrayList();
                for (AeaHiItemMatinst matinst : aeaHiItemMatinsts) {
                    OfficialDocumentInfoVo officialDocumentInfo = OfficialDocumentInfoVo.from(matinst);
//                    officialDocumentInfo.setAttFiles(fileUtilsService.getMatAttDetailByMatinstId(matinst.getMatinstId()));
                    //好像上传附件那里，没保存到上传者信息createrName，这里附件上传应该和批文上传的创建者是一个人
                    List<BscAttFileAndDir> bscAtts = fileUtilsService.getMatAttDetailByMatinstId(matinst.getMatinstId());
                    for (int i = 0, len = bscAtts.size(); i < len; i++) {
                        bscAtts.get(i).setCreaterName(officialDocumentInfo.getCreator());
                    }
                    officialDocumentInfo.setAttFiles(bscAtts);
                    collect.add(officialDocumentInfo);
                }

                if (collect.size() < 1) continue;
                map.put("itemMatinst", collect);
                OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(aeaHiIteminst.getApproveOrgId()); //查询审批人员
                map.put("orgName", opuOmOrg.getOrgName());//部门名称
                map.put("orgId", opuOmOrg.getOrgId());
                resultList.add(map);
            }
        }

        return combineSameOrg(resultList);
    }

    private List<Map<String, Object>> combineSameOrg(List<Map<String, Object>> list) {
        List<Map<String, Object>> copyList = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : list) {
            String orgId = (String) stringObjectMap.get("orgId");
            List<OfficialDocumentInfoVo> itemMatinst = (List<OfficialDocumentInfoVo>) stringObjectMap.get("itemMatinst");
            boolean flag = true;
            for (Map<String, Object> objectMap : copyList) {
                String copyOrgId = (String) objectMap.get("orgId");
                if (copyOrgId.equalsIgnoreCase(orgId)) {//已经存在
                    List<OfficialDocumentInfoVo> collect = (List<OfficialDocumentInfoVo>) objectMap.get("itemMatinst");
                    collect.addAll(itemMatinst);
                    flag = false;
                }
            }
            if (flag) {
                copyList.add(stringObjectMap);
            }

        }
        return copyList;
    }

    public void delete(String matinstId, String currentOrgId) {
        org.springframework.util.Assert.notNull(matinstId, "matinstId is null");

        deleteAeaHiItemInoutinst(matinstId);
        deleteUploadedFiles(matinstId, currentOrgId);
        deleteAeaHiItemMatinst(matinstId);
    }

    private void deleteAeaHiItemInoutinst(String matinstId) {
        try {
            aeaHiItemInoutinstMapper.deleteAeaHiItemInoutinstByMatinstId(matinstId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Delete aeaHiItemInoutinst and aeaHiItemMatinst failed, matinstId: " + matinstId, e);
        }
    }

    private void deleteUploadedFiles(String matinstId, String currentOrgId) {
        List<BscAttForm> bscAttForms = bscAttMapper.listAttLinkAndDetailByTablePKRecordId(TABLE_NAME, PK_NAME, matinstId, currentOrgId);
        Set<String> detailIds = bscAttForms.stream().map(BscAttForm::getDetailId).collect(Collectors.toSet());
        if (null == detailIds && detailIds.size() == 0) return;
        try {
            fileUtilsService.deleteAttachments(detailIds.toArray(new String[]{}));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除批文附件时报错了，但此错误逻辑上时可以忽略的，详情请查看日志, matinstId: {}", matinstId);
            //ignore
        }
    }

    private void deleteAeaHiItemMatinst(String matinstId) {
        try {
            aeaHiItemMatinstMapper.deleteAeaHiItemMatinst(matinstId);
        } catch (Exception e) {
            log.error("删除批文批复操作中删除AeaHiItemMatinst失败, matinstId: {}", matinstId);
            throw new RuntimeException("删除批文批复操作中删除AeaHiItemMatinst失败, matinstId: " + matinstId + e.getMessage(), e);
        }
    }

    public void create(OfficialDocumentEditVo officialDocumentEditVo, HttpServletRequest request) throws Exception {

        String applyinstId = officialDocumentEditVo.getApplyinstId();
        Assert.notNull(applyinstId, "applyinstId is null.");
        AeaHiApplyinst aeaHiApplyinst = getHiApplyinstById(applyinstId);
        if (aeaHiApplyinst == null) {
            return;
        }
        String iteminstId = officialDocumentEditVo.getIteminstId();
        // 并联的时候没有iteminstId 用taskId查询所有的材料
        if (StringUtils.isBlank(iteminstId) || "undefined".equalsIgnoreCase(iteminstId)) {
            iteminstId = approveService.getIteminstIdByTaskId(officialDocumentEditVo.getTaskId());
        }

        AeaHiIteminst aeaHiIteminst = getHiIteminstById(iteminstId);
        if (null != aeaHiIteminst) {
            AeaItemMat aeaItemOfficeMat = aeaItemMatMapper.getAeaItemMatById(officialDocumentEditVo.getMatId());
            AeaItemInout bindInout = getBindingAeaItemInoutOrCreateNewOne(aeaHiIteminst.getItemVerId(), aeaItemOfficeMat.getMatId());
            AeaHiItemMatinst aeaHiItemMatinst = createAeaItemMatinst(aeaHiApplyinst, aeaHiIteminst, aeaItemOfficeMat, officialDocumentEditVo);
            Long attCount = officialDocumentEditVo.getAttCount();
            String isCollected = Status.OFF;
            if (null != attCount && attCount > 0) {//电子材料
                uploadElectronicAttacments(aeaHiItemMatinst.getMatinstId(), request);
                isCollected = Status.ON;
            }
            createAeaItemInoutinst(aeaHiIteminst.getIteminstId(), bindInout.getInoutId(), aeaHiItemMatinst.getMatinstId(), isCollected);

            //将批文批复自动关联为后置事项的输入材料
            this.createPostIteminstMatinst(aeaItemOfficeMat.getMatId(), aeaHiItemMatinst.getMatinstId(), aeaHiItemMatinst.getProjInfoId(), bindInout.getInoutId(), iteminstId, aeaHiIteminst.getItemVerId());
        }
    }

    private void createPostIteminstMatinst(String matId, String matinstId, String projInfoId, String inoutId, String iteminstId, String itemVerId) throws Exception {

        AeaParShareMat aeaParShareMat = new AeaParShareMat();
        aeaParShareMat.setIsActive("1");
        aeaParShareMat.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParShareMat.setOutInoutId(inoutId);
        aeaParShareMat.setOutItemVerId(itemVerId);
        List<AeaParShareMat> aeaParShareMats = aeaParShareMatAdminService.listAeaParShareMat(aeaParShareMat);
        List<AeaHiApplyinst> aeaHiApplyinsts = aeaHiApplyinstService.getAllApplyinstesByProjInfoId(projInfoId, SecurityContext.getCurrentOrgId());
        for (AeaHiApplyinst aeaHiApplyinst : aeaHiApplyinsts) {

            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(aeaHiApplyinst.getApplyinstId());
            String handWay = null;
            if (aeaHiParStageinst != null) {
                handWay = aeaParStageMapper.getAeaParStageById(aeaHiParStageinst.getStageId()).getHandWay();
            }

            if (ApplyState.COMPLETED.getValue().equals(aeaHiApplyinst.getApplyinstState())) continue;
            List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(aeaHiApplyinst.getApplyinstId());
            for (AeaHiIteminst iteminst : aeaHiIteminsts) {

                if (iteminstId.equals(iteminst.getIteminstId())) continue;

                if ("1".equals(aeaHiApplyinst.getIsSeriesApprove()) || (StringUtils.isNotBlank(handWay) && "0".equals(handWay))) {
                    AeaItemInout aeaItemInout = new AeaItemInout();
                    aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaItemInout.setItemVerId(iteminst.getItemVerId());
                    aeaItemInout.setMatId(matId);
                    aeaItemInout.setIsInput("1");
                    aeaItemInout.setIsDeleted("0");
                    List<AeaItemInout> inouts = aeaItemInoutAdminService.listAeaItemInout(aeaItemInout);
                    if (inouts.size() < 1) continue;
                    AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(iteminst.getItemVerId());
                    for (AeaItemInout inout : inouts) {

                        if (this.iteminstOwnThisMatinst(iteminst.getIteminstId(), inout.getInoutId())) break;

                        //不是情形输入或该事项不分情形
                        if ("0".equals(inout.getIsStateIn()) || "0".equals(aeaItemBasic.getIsNeedState())) {
                            this.createAeaItemInoutinst(iteminst.getIteminstId(), inout.getInoutId(), matinstId, "1");
                        } else {
                            List<AeaItemState> itemStates = aeaHiItemStateinstService.listAeaItemStateByApplyinstIdOrSeriesinstId(aeaHiApplyinst.getApplyinstId(), null);
                            if (itemStates.contains(inout.getItemStateId())) {
                                this.createAeaItemInoutinst(iteminst.getIteminstId(), inout.getInoutId(), matinstId, "1");
                            }
                        }
                    }
                } else {
                    if (aeaParShareMats.size() < 1) break;  //  当前主题下没有前后置材料关联配置
                    List<AeaHiItemMatinst> matinsts = aeaHiItemMatinstService.getMatinstListByStageIteminstId(iteminst.getIteminstId());

                    boolean check = false;
                    for (AeaHiItemMatinst matinst : matinsts) {
                        if (matinst.getMatId().equals(matId)) {
                            check = true;
                            break;
                        }
                    }
                    if (check) continue;

                    for (AeaParShareMat parShareMat : aeaParShareMats) {
                        if (parShareMat.getInItemVerId().equals(iteminst.getItemVerId()) && aeaHiParStageinst.getThemeVerId().equals(parShareMat.getThemeVerId())) {
                            this.createAeaItemInoutinst(iteminst.getIteminstId(), parShareMat.getInInoutId(), matinstId, "1");
                        }
                    }
                }
            }
        }
    }

    private boolean iteminstOwnThisMatinst(String iteminstId, String inoutId) throws Exception {
        AeaHiItemInoutinst itemInoutinst = new AeaHiItemInoutinst();
        itemInoutinst.setIsCollected("1");
        itemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        itemInoutinst.setIteminstId(iteminstId);
        itemInoutinst.setItemInoutId(inoutId);
        List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(itemInoutinst);
        return itemInoutinsts.size() > 0 ? true : false;
    }

    private AeaHiIteminst getHiIteminstById(String iteminst) {
        try {
            return aeaHiIteminstMapper.getAeaHiIteminstById(iteminst);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询AeaHiIteminst失败, iteminst: " + iteminst, e);
        }
    }

    private AeaHiApplyinst getHiApplyinstById(String applyinst) {
        try {
            return aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinst);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询AeaHiApplyinst失败, applyinst: " + applyinst, e);
        }
    }


    /*
    根据itemVerId和matId获取aea_item_inout的记录， 如果为空，则添加一条
     */
    private AeaItemInout getBindingAeaItemInoutOrCreateNewOne(String itemVerId, String matId) throws Exception {
        AeaItemInout queryParam = new AeaItemInout();
        queryParam.setItemVerId(itemVerId);
        queryParam.setMatId(matId);
        queryParam.setIsStateIn(Status.OFF);
        queryParam.setIsInput(Status.OFF);
        queryParam.setIsDeleted("0");
        queryParam.setIsOwner(Status.ON);
        queryParam.setFileType("mat");
        queryParam.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemInout> bindingInouts = aeaItemInoutMapper.listAeaItemInout(queryParam);
        if (CollectionUtils.isNotEmpty(bindingInouts)) {
            if (bindingInouts.size() > 1) {
                log.warn("同一事项关联了多条相同的批文材料, itemVerId: {}, matId: {}", itemVerId, matId);
            }
            return bindingInouts.get(0);
        }
        return null;//bindItemAndOfficialDocMat(itemVerId, matId);
    }

    /*
     * 为事项建立到批文批复定义的关系
     * 新增一条aea_item与aea_item_mat的关联记录到aea_item_inout，
     */
    private AeaItemInout e(String itemVerId, String matId) throws Exception {
        //    AeaItemVer aeaItemVer = aeaItemVerMapper.selectOneById(itemVerId);
        AeaItemInout bindingInout = new AeaItemInout();
        bindingInout.setInoutId(UUID.randomUUID().toString());
        bindingInout.setItemVerId(itemVerId);
        //   bindingInout.setItemId(aeaItemVer.getItemId());
        bindingInout.setIsOwner(Status.ON);
        bindingInout.setIsInput(Status.OFF);
        bindingInout.setFileType("mat");
        bindingInout.setMatId(matId);
        bindingInout.setCreater(SecurityContext.getCurrentUserName());
        bindingInout.setCreateTime(new Date());
        bindingInout.setModifier(SecurityContext.getCurrentUserName());
        bindingInout.setModifyTime(new Date());
        bindingInout.setIsStateIn(Status.OFF);
        bindingInout.setIsDeleted("0");
        bindingInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemInoutMapper.insertAeaItemInout(bindingInout);
        return bindingInout;
    }

    private AeaHiItemMatinst createAeaItemMatinst(AeaHiApplyinst aeaHiApplyinst, AeaHiIteminst aeaHiIteminst, AeaItemMat defaultOfficialMat, OfficialDocumentEditVo editVo) {

        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        try {
            // 文件文号
            aeaHiItemMatinst.setOfficialDocNo(editVo.getOfficialDocNo());
            // 文件标题
            aeaHiItemMatinst.setOfficialDocTitle(editVo.getOfficialDocTitle());
            if (editVo.getOfficialDocDueDate() != null && !"".equals(editVo.getOfficialDocDueDate().trim())) {
                // 有限期限
                aeaHiItemMatinst.setOfficialDocDueDate(editVo.strToDate(editVo.getOfficialDocDueDate()));
            }
            if (editVo.getOfficialDocPublishDate() != null && !"".equals(editVo.getOfficialDocPublishDate().trim())) {
                // 批复日期
                aeaHiItemMatinst.setOfficialDocPublishDate(editVo.strToDate(editVo.getOfficialDocPublishDate()));
            }
            // 纸质份数
            aeaHiItemMatinst.setRealPaperCount(0L);
            aeaHiItemMatinst.setRealCopyCount(0L);
            aeaHiItemMatinst.setAttCount(editVo.getAttCount() == null ? 0L : editVo.getAttCount());

            aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());

            OpuOmUserInfo userinfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
            aeaHiItemMatinst.setCreater(userinfo.getUserId());
            aeaHiItemMatinst.setCreateTime(new Date());

            aeaHiItemMatinst.setMatId(defaultOfficialMat.getMatId());
            aeaHiItemMatinst.setMatinstCode(defaultOfficialMat.getMatCode());
            aeaHiItemMatinst.setMatinstName(defaultOfficialMat.getMatName());

            List<AeaProjInfo> applyProj = aeaProjInfoService.findApplyProj(aeaHiApplyinst.getApplyinstId());
            if (applyProj.size() < 1) throw new Exception("找不到项目信息");
            aeaHiItemMatinst.setProjInfoId(applyProj.get(0).getProjInfoId());
            aeaHiItemMatinst.setUnitInfoId(getUnitInfoIds(aeaHiApplyinst.getLinkmanInfoId()));
            aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiItemMatinst.setMemo(editVo.getMemo());
            aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增批复时保存AeaHiItemMatinst失败, iteminstId: " + aeaHiIteminst.getIteminstId() + ", applyinstId: " + aeaHiApplyinst.getApplyinstId());
        }
        return aeaHiItemMatinst;
    }

    /**
     * 两种取值方案
     * 1.根据projInfoId获取所有is_owner=0的unit_info_id 可能多个
     * 2.根据aeaHiApplyinst中的linkmanInfoId所关联的unit_info_id  只有一个
     */
    private String getUnitInfoIds(String linkmanInfoId) {
        try {
            if (StringUtils.isNotBlank(linkmanInfoId)) {
                AeaLinkmanInfo aeaLinkmanInfoById = linkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);
                return aeaLinkmanInfoById.getUnitInfoId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void createAeaItemInoutinst(String iteminstId, String inoutId, String matinstId, String isCollected) {
        AeaHiItemInoutinst inoutinst = new AeaHiItemInoutinst();
        inoutinst.setInoutinstId(UUID.randomUUID().toString());
        inoutinst.setIteminstId(iteminstId);
        inoutinst.setItemInoutId(inoutId);
        inoutinst.setMatinstId(matinstId);
        inoutinst.setCreater(SecurityContext.getCurrentUserName());
        inoutinst.setCreateTime(new Date());
        inoutinst.setModifier(SecurityContext.getCurrentUserName());
        inoutinst.setModifyTime(new Date());
        inoutinst.setIsCollected("1".equals(isCollected) ? Status.ON : Status.OFF);
        inoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        try {
            aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(inoutinst);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增批复时保存aeaHiItemMatinstMapper失败, iteminstId: " + iteminstId + ", inoutId: " + inoutId + ", matinstId: " + matinstId);
        }
    }

    private void uploadElectronicAttacments(String matinstId, HttpServletRequest request) {
        try {
            fileUtilsService.uploadAttachments(TABLE_NAME, PK_NAME, matinstId, null, request);
            List<BscAttFileAndDir> bscAttFileAndDirs = fileUtilsService.getMatAttDetailByMatinstId(matinstId);
            if (bscAttFileAndDirs != null) {
                AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
                aeaHiItemMatinst.setAttCount((long) bscAttFileAndDirs.size());
                aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserId());
                aeaHiItemMatinst.setModifyTime(new Date());
                aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
            }
        } catch (Exception e) {
            log.error("新增批文批复时上传文件失败: matinstId: {}", matinstId);
            throw new RuntimeException("新增批文批复时上传文件失败: matinstId: " + matinstId, e);

        }
    }

    public void edit(HttpServletRequest request, OfficialDocumentEditVo officialDocumentEditVo) throws Exception {
        AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(officialDocumentEditVo.getMatinstId());

        org.springframework.util.Assert.notNull(aeaHiItemMatinst, "根据matInstId: [" + officialDocumentEditVo.getMatinstId() + "]无法找到对应的的AeaHiItemMatinst，请检查该条记录是否已被删除");

        merge(aeaHiItemMatinst, officialDocumentEditVo);
        aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);

        if (officialDocumentEditVo.getAttCount() > 0) {
            //先删除之前的
//            deleteUploadedFiles(matinstId);
            //再保存新上传的
            uploadElectronicAttacments(officialDocumentEditVo.getMatinstId(), request);
        }
    }

    private AeaHiItemMatinst merge(AeaHiItemMatinst originalMatinst, OfficialDocumentEditVo editVo) {
        originalMatinst.setOfficialDocNo(editVo.getOfficialDocNo());
        originalMatinst.setOfficialDocTitle(editVo.getOfficialDocTitle());
        originalMatinst.setOfficialDocDueDate(editVo.strToDate(editVo.getOfficialDocDueDate()));
        originalMatinst.setOfficialDocPublishDate(editVo.strToDate(editVo.getOfficialDocPublishDate()));
        originalMatinst.setAttCount(editVo.getAttCount() == null ? 0L : editVo.getAttCount());
        originalMatinst.setModifier(SecurityContext.getCurrentUserId());
        originalMatinst.setModifyTime(new Date());
        originalMatinst.setMemo(editVo.getMemo());
        return originalMatinst;
    }

    @Transactional(readOnly = true)
    public OfficialDocumentInfoVo getOne(String matinstId) {
        try {
            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
            return OfficialDocumentInfoVo.from(aeaHiItemMatinst);
        } catch (Exception e) {
            log.error("根据matInstId: [{}]查询对应的的AeaHiItemMatinst失败，请联系管理员", matinstId);
            throw new RuntimeException("根据matInstId: [" + matinstId + "]查询对应的的AeaHiItemMatinst失败，请联系管理员", e);
        }
    }

    /**
     * （新）获取建筑工程施工许可证打印信息 2019/11/16 11：13
     *
     * @param certinstId
     * @return
     * @throws Exception
     */
    public ConstructPermitVo getConstructPermitInfo(String certinstId) throws Exception {
        ConstructPermitVo constructPermit = new ConstructPermitVo();
        if (StringUtils.isBlank(certinstId) || "null".equals(certinstId) || "undefined".equals(certinstId))
            return constructPermit;

        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        aeaHiItemMatinst.setCertinstId(certinstId);
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listAeaHiItemMatinst(aeaHiItemMatinst);
        List<AeaHiItemInoutinst> aeaHiItemInoutinsts = null;
        if(aeaHiItemMatinsts!=null && aeaHiItemMatinsts.size() >0){
            aeaHiItemInoutinsts = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstByMatinstId(aeaHiItemMatinsts.get(0).getMatinstId());
            if (aeaHiItemInoutinsts.isEmpty()) {
                return constructPermit;
            }
        }else {
            return constructPermit;
        }

        //查询电子证照实例表
        AeaHiCertinst aeaHiCertinstById = aeaHiCertinstMapper.getAeaHiCertinstById(certinstId);

        String iteminstId = aeaHiItemInoutinsts.get(0).getIteminstId();
        String applyinstId = getApplyinstIdByIteminstId(iteminstId);

        List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findApplyProj(applyinstId);
        if (aeaProjInfos.isEmpty()) return null;
        AeaProjInfo projInfo = aeaProjInfos.get(0);

        //获取机构信息
        AeaHiCertinst certinst = aeaHiCertinstMapper.getAeaHiCertinstById(certinstId);

        //获取施工核发许可证表的信息
        AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(projInfo.getProjInfoId());
        if (aeaExProjCertBuildByProjId != null) {
            String certBuildCode = aeaExProjCertBuildByProjId.getCertBuildCode();//建设工程规划许可证编号
            byte[] certBuildQrcode = aeaExProjCertBuildByProjId.getCertBuildQrcode();//建设工程规划许可证二维码
            if(certBuildQrcode != null){
                constructPermit.setCertBuildQrcode(certBuildQrcode);
            }
            //广东分支将此段放开
            if (StringUtils.isBlank(certBuildCode) && certBuildQrcode.length == 0 && certBuildQrcode == null) {
                //获取施工许可证编码和二维码
//                ResponseConsPermitInfo consPermitInfo = webservice.getConsPermitInfo(projInfo.getProjInfoId(), iteminstId);
//                if (consPermitInfo.getSuccess()){
//                    constructPermit.setCertBuildQrcode(consPermitInfo.getqRCodeArray());//二维码
//                    constructPermit.setConstructPermitCode(consPermitInfo.getPermitNum());//省级施工许可证编号
//                    aeaExProjCertBuildByProjId.setCertBuildCode(consPermitInfo.getPermitNum());
//                    aeaExProjCertBuildByProjId.setCertBuildQrcode(consPermitInfo.getqRCodeArray());
//                    aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuildByProjId);
//                }else {
//                    String errorMsg = consPermitInfo.getErrorMsg();
//                }
            }else {
                constructPermit.setCertBuildQrcode(certBuildQrcode);
                constructPermit.setConstructPermitCode(certBuildCode);
            }
            constructPermit.setCertBuildQrcode(certBuildQrcode);
            if (StringUtils.isBlank(aeaExProjCertBuildByProjId.getConstructionsSize())) {
                constructPermit.setContructScale(projInfo.getProjScale());
            } else {
                constructPermit.setContructScale(aeaExProjCertBuildByProjId.getConstructionsSize());//建设规模
            }
            //获取核发机关相关信息
            String govOrgName = aeaExProjCertBuildByProjId.getGovOrgName();
//            String govOrgCode = aeaExProjCertBuildByProjId.getGovOrgCode();
            Date publishTime = aeaExProjCertBuildByProjId.getPublishTime();
            if (StringUtils.isBlank(govOrgName)) {
                constructPermit.setIssueOrgName(certinst.getIssueOrgName());//核发机关
                Date issueDate = certinst.getIssueDate();
                if (null != issueDate) {
                    String s = DateUtils.convertDateToString(issueDate, "yyyy-MM-dd");
                    String[] split = s.split("-");
                    constructPermit.setIssueYear(split[0]);
                    constructPermit.setIssueMonth(split[1]);
                    constructPermit.setIssueDay(split[2]);
                }
                aeaExProjCertBuildByProjId.setGovOrgName(certinst.getIssueOrgName());
                aeaExProjCertBuildByProjId.setPublishTime(issueDate);
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuildByProjId);
            } else {
                constructPermit.setIssueOrgName(govOrgName);
                if (publishTime != null) {
                    String s = DateUtils.convertDateToString(publishTime, "yyyy-MM-dd");
                    String[] split = s.split("-");
                    constructPermit.setIssueYear(split[0]);
                    constructPermit.setIssueMonth(split[1]);
                    constructPermit.setIssueDay(split[2]);
                }
            }

            constructPermit.setBuildUnitName(aeaExProjCertBuildByProjId.getConstructionUnit());//建设单位
            constructPermit.setProjectName(aeaExProjCertBuildByProjId.getProjName());//工程名称
            constructPermit.setContructAddress(aeaExProjCertBuildByProjId.getConstructionAddr());//建设地址
            constructPermit.setContractPrice(Double.parseDouble(aeaExProjCertBuildByProjId.getContractPrice()));//合同价格，单位：万元
            constructPermit.setGczcbUnitName(aeaExProjCertBuildByProjId.getGczcbUnit());//工程总承包单位
            constructPermit.setGczcbUnitLeader(aeaExProjCertBuildByProjId.getGczcbUnitLeader());//工程总承包单位负责人
            constructPermit.setExplorationUnitName(aeaExProjCertBuildByProjId.getKcUnit());//勘察单位
            constructPermit.setDesignUnitName(aeaExProjCertBuildByProjId.getSjUnit());//设计单位
            constructPermit.setConstructUnitName(aeaExProjCertBuildByProjId.getSgUnit());//施工单位
            constructPermit.setSupervisionUnitName(aeaExProjCertBuildByProjId.getJlUnit());//监理单位
            constructPermit.setExplorationUnitLeader(aeaExProjCertBuildByProjId.getKcUnitLeader());//勘察单位项目负责人
            constructPermit.setDesignUnitLeader(aeaExProjCertBuildByProjId.getSjUnitLeader());//设计单位项目负责人
            constructPermit.setConstructUnitLeader(aeaExProjCertBuildByProjId.getSgUnitLeader());//施工单位项目负责人
            constructPermit.setChiefEngineer(aeaExProjCertBuildByProjId.getJlUnitLeader());//总监理工程师
            constructPermit.setContractDuration(aeaExProjCertBuildByProjId.getContractPeriod());//合同工期
            if(StringUtils.isBlank(aeaHiCertinstById.getMemo())){
                constructPermit.setRemarks(aeaHiCertinstById.getMemo());//备注
            }else {
                constructPermit.setRemarks(aeaExProjCertBuildByProjId.getCertBuildMemo());//备注
            }
        }
        return constructPermit;
    }

    /**
     * 获取建筑工程施工许可证打印信息
     *
     * @param certinstId
     * @return
     */
    public ConstructPermitVo getConstructPermitInfoVo(String certinstId) throws Exception {
        ConstructPermitVo constructPermit = new ConstructPermitVo();
        if (StringUtils.isBlank(certinstId) || "null".equals(certinstId) || "undefined".equals(certinstId))
            return constructPermit;

        List<AeaHiItemInoutinst> aeaHiItemInoutinsts = aeaHiItemInoutinstMapper.getAeaHiIteminstCertByCertinstId(certinstId);
        if (aeaHiItemInoutinsts.isEmpty()) {
            return constructPermit;
        }
        String iteminstId = aeaHiItemInoutinsts.get(0).getIteminstId();
        String applyinstId = getApplyinstIdByIteminstId(iteminstId);

        List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findApplyProj(applyinstId);
        if (aeaProjInfos.isEmpty()) return null;
        AeaProjInfo projInfo = aeaProjInfos.get(0);


        constructPermit.setProjectName(projInfo.getProjName());
        constructPermit.setContructAddress(projInfo.getProjAddr());
        constructPermit.setContructScale(projInfo.getProjScale());
        constructPermit.setContractPrice(projInfo.getInvestSum());
        //设置备注信息和机构
        AeaHiCertinst certinst = aeaHiCertinstMapper.getAeaHiCertinstById(certinstId);
        if (null != certinst) {
            constructPermit.setRemarks(certinst.getMemo());
            constructPermit.setIssueOrgName(certinst.getIssueOrgName());
            constructPermit.setConstructPermitCode(certinst.getCertinstCode());
            Date issueDate = certinst.getIssueDate();
            if (null != issueDate) {

                String s = DateUtils.convertDateToString(issueDate, "yyyy-MM-dd");
                String[] split = s.split("-");
                constructPermit.setIssueYear(split[0]);
                constructPermit.setIssueMonth(split[1]);
                constructPermit.setIssueDay(split[2]);
            }

        }

        //设置单位信息--- //20191018 修改为从 aea_unit_proj_linkman 查询联系人类型
        List<AeaUnitInfo> unitInfos = aeaUnitInfoService.findAllProjUnit(projInfo.getProjInfoId());
        for (AeaUnitInfo unitInfo : unitInfos) {
            String unitProjId = unitInfo.getUnitProjId();
            String unitType = unitInfo.getUnitProjUnitType();//unitProjUnitType
            String linkmanName = "";
            List<AeaUnitProjLinkman> aeaUnitProjLinkmen;
            if (UnitType.SUPERVISION_UNIT.getValue().equals(unitType)) {//监理单位
                aeaUnitProjLinkmen = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(unitProjId, null, "2");//获取总监理工程师
            } else {
                //获取项目负责人
                aeaUnitProjLinkmen = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(unitProjId, null, "1");//获取项目负责人
            }
            if (!aeaUnitProjLinkmen.isEmpty()) {
                linkmanName = aeaUnitProjLinkmen.get(0).getLinkmanName();
            }
            constructPermit.buidlUnitAndLink(unitInfo.getApplicant(), linkmanName, unitType);
        }
        return constructPermit;
    }

    //根据事项实例ID获取申请实例
    public String getApplyinstIdByIteminstId(String iteminstId) throws Exception {
        AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
        String applyinstId = "";
        if (iteminst != null) {
            String isSeriesApprove = iteminst.getIsSeriesApprove();
            if ("0".equals(isSeriesApprove)) {
                //并联
                String stageinstId = iteminst.getStageinstId();
                AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstById(stageinstId);
                if (stageinst != null) {
                    applyinstId = stageinst.getApplyinstId();
                }
            } else if ("1".equals(isSeriesApprove)) {
                //单项
                String seriesinstId = iteminst.getSeriesinstId();
                AeaHiSeriesinst seriesinst = aeaHiSeriesinstMapper.getAeaHiSeriesinstById(seriesinstId);
                if (null != seriesinst) {
                    applyinstId = seriesinst.getApplyinstId();
                }
            }

        }
        return applyinstId;
    }

    //获取证照实例列表---指定事项实例下或申报实例下的
    public List<CertinstVo> getCertinstListByApplyinstIdAndIteminstId(String applyinstId, String iteminstId) throws Exception {
        //不要返回 null，
        List<AeaHiCertinst> list = new ArrayList<>();
        if (StringUtils.isNotBlank(iteminstId) && !"undefined".equalsIgnoreCase(iteminstId)) {
            //只查询当前事项的证照实例
            list = aeaHiCertinstMapper.getAeaHiCertinstByIteminstId(iteminstId);
        } else {
            //获取所有的申请事项实例下的证照实例列表
            List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            if (iteminstList.size() > 0) {
                String[] iteminstIds = iteminstList.stream().map(AeaHiIteminst::getIteminstId).toArray(String[]::new);
                list = aeaHiCertinstMapper.listAeaHiCertinstByIteminstIds(iteminstIds);
            }

        }
        List<CertinstVo> voList = new ArrayList<>();
        if (list.size() > 0) {
            for (AeaHiCertinst certinst : list) {
                CertinstVo vo = new CertinstVo();
                BeanUtils.copyProperties(certinst, vo);
                Date issueDate = certinst.getIssueDate();
                vo.setIssueDate(formDate(issueDate));
                String certinstId = vo.getCertinstId();

                if (StringUtils.isNotBlank(iteminstId) && "undefined".equalsIgnoreCase(iteminstId)) {
                    vo.setIteminstId(iteminstId);
                } else {
                    List<AeaHiItemInoutinst> inoutinsts = aeaHiItemInoutinstMapper.getAeaHiIteminstCertByCertinstId(certinstId);
                    if (inoutinsts.size() > 0) {
                        String temp = inoutinsts.get(0).getIteminstId();
                        vo.setIteminstId(temp);
                    }
                }

                List<AeaProjInfo> applyProj = aeaProjInfoService.findApplyProj(applyinstId);
                if (applyProj.size() > 0) {
                    AeaProjInfo aeaProjInfo = applyProj.get(0);
                    vo.setProjScale(aeaProjInfo.getBuildAreaSum());
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    public String formDate(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }

    /**
     * 保存或更新证照实例
     *
     * @param certinstVo
     */
    public void saveOrUpdateAeaCertinst(CertinstVo certinstVo) throws Exception {
        if (null == certinstVo) return;
        AeaHiCertinst certinst = new AeaHiCertinst();
        String certinstId = certinstVo.getCertinstId();
        String iteminstId = certinstVo.getIteminstId();
        String applyinstId = certinstVo.getApplyinstId();
        String certId = certinstVo.getCertId();
        String issueDate = certinstVo.getIssueDate();
        String matId = certinstVo.getMatId();

        BeanUtils.copyProperties(certinstVo, certinst);
        certinst.setIssueDate(certinstVo.strToDate(issueDate));
        certinst.setCreateTime(new Date());
//        certinst.setCreater(SecurityContext.getCurrentUserName());这里好像是登录名
        OpuOmUserInfo currUser = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
        certinst.setCreater(currUser.getUserName());
        certinst.setRootOrgId(SecurityContext.getCurrentOrgId());

        if (StringUtils.isBlank(certinstVo.getCertinstName())) {
            AeaCert cert = aeaCertMapper.selectOneById(certId);
            if (null != cert) {
                certinst.setCertinstName(cert.getCertName());
            }
        }

        if (StringUtils.isNotBlank(certinstId)) {
            certinst.setModifier(SecurityContext.getCurrentUserName());
            certinst.setModifyTime(new Date());
            aeaHiCertinstMapper.updateAeaHiCertinst(certinst);
        } else {
            certinst.setCertinstId(UUID.randomUUID().toString());
            aeaHiCertinstMapper.insertAeaHiCertinst(certinst);
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            String inoutId = "";
            if (null == iteminst) throw new Exception("找不到事项实例！");
            String itemVerId = iteminst.getItemVerId();
            AeaItemInout inout = new AeaItemInout();
            inout.setItemVerId(itemVerId);
            inout.setIsDeleted("0");
            inout.setIsInput("0");
            inout.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaItemInout> itemInouts = aeaItemInoutMapper.listAeaItemInoutByItemVerId(inout);
            if (itemInouts.size() > 0) {
                for (AeaItemInout inout1 : itemInouts) {
                    String matId1 = inout1.getMatId();
                    if (matId.equalsIgnoreCase(matId1)) {
                        inoutId = inout1.getInoutId();
                        break;
                    }

                }
            }

            AeaItemMat mat = aeaItemMatMapper.getAeaItemMatById(matId);
            if (mat == null) throw new Exception("找不到材料定义！");

            AeaHiItemMatinst matinst = new AeaHiItemMatinst();
            matinst.setMatId(matId);
            matinst.setMatinstName(certinst.getCertinstName());
            matinst.setMatinstCode(mat.getMatCode());
            matinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            matinst.setMemo(certinst.getMemo());
            matinst.setMatProp("c");
            if ("c".equals(mat.getMatHolder())) {
                List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByApplyinstIdAndUnitType(applyinstId, UnitType.DEVELOPMENT_UNIT.getValue());
                if (aeaUnitInfos.size() > 0) {
                    matinst.setUnitInfoId(aeaUnitInfos.get(0).getUnitInfoId());
                }
                matinst.setMatinstSource("u");
            } else {
                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                if (aeaHiApplyinst != null) {
                    matinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                }
                matinst.setMatinstSource("l");
            }
            matinst.setMatinstId(UUID.randomUUID().toString());
            matinst.setCertinstId(certinst.getCertinstId());
            matinst.setCreater(SecurityContext.getCurrentUserId());
            matinst.setCreateTime(new Date());
            aeaHiItemMatinstMapper.insertAeaHiItemMatinst(matinst);

            //保存到 aea_hi_iteminout_inst表
            AeaHiItemInoutinst inoutinst = new AeaHiItemInoutinst();
            inoutinst.setInoutinstId(UUID.randomUUID().toString());
            inoutinst.setIteminstId(iteminstId);
            inoutinst.setItemInoutId(inoutId);
            inoutinst.setMatinstId(matinst.getMatinstId());
            inoutinst.setCreater(SecurityContext.getCurrentUserName());
            inoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            inoutinst.setCreateTime(new Date());
            inoutinst.setIsParIn("0");
            aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(inoutinst);
        }

        //保存修改的项目信息
        List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findApplyProj(applyinstId);
        if (aeaProjInfos.size() > 0) {
            AeaProjInfo aeaProjInfo = aeaProjInfos.get(0);
            if (StringUtils.isBlank(aeaProjInfo.getProjScale())) {
                AeaProjInfo temp = new AeaProjInfo();
                temp.setProjInfoId(aeaProjInfo.getProjInfoId());
                temp.setBuildAreaSum(certinstVo.getProjScale());
                aeaProjInfoService.updateAeaProjInfo(temp);
            }
        }

    }

    /**
     * 批量删除证照实例---建筑工程施工许可证-先不管附件
     *
     * @param matinstIds 证照实例IDS
     */
    public void batchDeleteCertinst(String[] matinstIds) throws Exception {

        //存在外键-注意删除顺序
        aeaHiItemInoutinstMapper.deleteAeaHiItemInoutinstByMatinstIds(matinstIds);

        aeaHiItemMatinstService.deleteAeaHiItemMatinstByIds(matinstIds);

        aeaHiCertinstMapper.batchDeleteAeaHiCertinstByMatinstIds(matinstIds);
    }

    /**
     * 获取当前事项实例下证照定义列表，如果事项实例id为空，申请实例id不为空，则查询申请实例下所有事项的证照定义
     *
     * @param iteminstId
     * @param applyinstId
     * @return
     * @throws Exception
     */
    public CertVo getItemOutputCertByIteminstId(String applyinstId, String iteminstId) throws Exception {
        CertVo vo = new CertVo();
        List<AeaHiIteminst> aeaHiIteminstList = new ArrayList<>();
        if (StringUtils.isBlank(iteminstId)) {
            aeaHiIteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId);
        } else {
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            if (null == iteminst) {
                return vo;
            }
            aeaHiIteminstList.add(iteminst);
        }
        List<AeaProjInfo> applyProj = aeaProjInfoService.findApplyProj(applyinstId);
        for (AeaHiIteminst iteminst : aeaHiIteminstList) {


            List<AeaCert> certs = aeaCertAdminService.getOutputCertsByIteminstId(iteminstId);
            if (certs == null) {
                continue;
            }
            vo.changeToCertVo(certs);

            String orgId = iteminst.getApproveOrgId();
            OpuOmOrg org = opuOmOrgMapper.getOrg(orgId);
            if (null == org) {
                continue;
            }
            String orgSeq = org.getOrgSeq();
            if (StringUtils.isNotBlank(orgSeq)) {
                String[] orgIds = orgSeq.split("\\.");
                List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.getOrgNameByOrgIds(orgIds);
                List<OpuOmOrg> omOrgs = opuOmOrgs.stream().filter(orgs -> StringUtils.isBlank(orgs.getOrgProperty()) || "d".equals(orgs.getOrgProperty())).collect(Collectors.toList());
                vo.changeToOrgVo(omOrgs);
            }
            if (applyProj.size() > 0) {
                vo.setProjScale(applyProj.get(0).getBuildAreaSum());
            }
        }
        return vo;
    }

    /**
     * 获取当前事项实例下批文批复定义列表，如果事项实例id为空，申请实例id不为空，则查询申请实例下所有事项的批文批复定义
     *
     * @param iteminstId
     * @param applyinstId
     * @return
     * @throws Exception
     */
    public List<AeaItemMat> getItemInOfficeMat(String applyinstId, String iteminstId) throws Exception {
        List<AeaItemMat> result = new ArrayList<>();
        List<AeaHiIteminst> aeaHiIteminstList = new ArrayList<>();
        if (StringUtils.isBlank(iteminstId)) {
            aeaHiIteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId);
        } else {
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            aeaHiIteminstList.add(iteminst);
        }
        if (aeaHiIteminstList.size() > 0) {
            String[] itemVerIds = new String[aeaHiIteminstList.size()];
            for (int i = 0; i < aeaHiIteminstList.size(); i++) {
                itemVerIds[i] = aeaHiIteminstList.get(i).getItemVerId();
            }
            List<AeaItemMat> matListByItemVerIds = aeaItemMatService.getMatListByItemVerIds(itemVerIds, "0", null);
            if (matListByItemVerIds.size() > 0) {
                result = matListByItemVerIds.stream().filter(mat -> StringUtils.isNotBlank(mat.getIsOfficialDoc()) && "1".equalsIgnoreCase(mat.getIsOfficialDoc()) && "m".equals(mat.getMatProp())).collect(Collectors.toList());
            }
        }
        return result;
    }
}
