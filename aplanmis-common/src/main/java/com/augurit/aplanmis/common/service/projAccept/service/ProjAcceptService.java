package com.augurit.aplanmis.common.service.projAccept.service;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmHistoryCommentForm;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.dic.region.service.BscDicRegionService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParShareMatAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.projAccept.vo.ProjAcceptOpinionSummaryVo;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.receive.utils.ReceivePDFUtils;
import com.augurit.aplanmis.common.service.receive.vo.ReceiveBaseVo;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 竣工验收阶段相关逻辑
 */
@Service
@Transactional
public class ProjAcceptService {
    private static String[] itemCategoryMarks = new String[]{"GHTJHS1","JSGCCJDAYS1","GCJGYSJD1"/*,"FWJZGCHSZJCSSGCJGYSBA1"*/};//规划条件核实、建设工程城建档案验收、工程竣工验收监督、房屋建筑工程和市政基础设施工程竣工验收备案

    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private BscDicRegionService bscDicRegionService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaLinkmanInfoMapper linkmanInfoMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaParShareMatAdminService aeaParShareMatAdminService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;
    @Autowired
    private BscAttMapper bscAttMapper;

    //固定的联合验收终审意见书批文批复材料编号
    private final String MAT_CODE = "MAT-C0000000583";
    //房屋建筑工程和市政基础设施工程竣工验收备案 事项的 唯一分类标记
    private final String ITEM_CATEGORY = "FWJZGCHSZJCSSGCJGYSBA1";

    private final String TABLE_NAME = "AEA_HI_ITEM_MATINST";
    private final String PK_NAME = "MATINST_ID";

    /**
     * 根据申报实例ID，获取竣工验收阶段汇总意见信息（只适合于竣工验收阶段）
     * @param applyinstId 申报实例ID
     * @param yanshouProcInstId 竣工验收阶段验收二级流程的实例ID，可以为空
     * @return
     * @throws Exception
     */
    public ProjAcceptOpinionSummaryVo caculateProjAcceptOpinionSummary(String applyinstId,String yanshouProcInstId) throws Exception {
        if(StringUtils.isBlank(applyinstId))
            throw new RuntimeException("申报实例ID参数不能为空！");

        List<AeaApplyinstProj> applyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);

        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);

        if(applyinstProjs==null||applyinstProjs.size()==0)
            throw new RuntimeException("获取申报项目信息失败！");

        AeaApplyinstProj applyinstProj = applyinstProjs.get(0);
        String projInfoId = applyinstProj.getProjInfoId();

        String buildUnit = null;
        String jianliUnit = null;
        String kanchaUnit = null;
        String designUnit = null;
        String shigongUnit = null;
        String linkman = null;
        String linkmanPhone = null;

        //联系人信息
        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
        if(linkmanInfo!=null){
            linkman = linkmanInfo.getLinkmanName();
            linkmanPhone = linkmanInfo.getLinkmanMobilePhone();
        }


        AeaUnitInfo buildUnitInfo = null;//获取建设单位
        AeaUnitInfo jianliUnitInfo = null;//获取监理单位
        AeaUnitInfo kanchaUnitInfo = null;//获取勘查单位
        AeaUnitInfo designUnitInfo = null;//获取设计单位
        AeaUnitInfo shigongUnitInfo = null;//获取施工单位（总包）

        List<AeaUnitInfo> unitInfoList = aeaUnitInfoService.findApplyUnitProj(applyinstId,projInfoId);

        if(unitInfoList!=null&&unitInfoList.size()>0){
            for(AeaUnitInfo aeaUnitInfo:unitInfoList){
                if("1".equals(aeaUnitInfo.getUnitType()))
                    buildUnitInfo = aeaUnitInfo;
                if("5".equals(aeaUnitInfo.getUnitType()))
                    jianliUnitInfo = aeaUnitInfo;
                if("3".equals(aeaUnitInfo.getUnitType()))
                    kanchaUnitInfo = aeaUnitInfo;
                if("4".equals(aeaUnitInfo.getUnitType()))
                    designUnitInfo = aeaUnitInfo;
                if("22".equals(aeaUnitInfo.getUnitType()))
                    shigongUnitInfo = aeaUnitInfo;
            }
        }

        if(buildUnitInfo!=null) {
            buildUnit = buildUnitInfo.getApplicant();
        }

        if(jianliUnitInfo!=null)
            jianliUnit = jianliUnitInfo.getApplicant();

        if(kanchaUnitInfo!=null)
            kanchaUnit = kanchaUnitInfo.getApplicant();

        if(designUnitInfo!=null)
            designUnit = designUnitInfo.getApplicant();

        if(shigongUnitInfo!=null)
            shigongUnit = shigongUnitInfo.getApplicant();

        ProjAcceptOpinionSummaryVo acceptOpinionSummaryVo = new ProjAcceptOpinionSummaryVo();
        acceptOpinionSummaryVo.setCentralCode(applyinstProj.getCentralCode());
        acceptOpinionSummaryVo.setProjName(applyinstProj.getProjName());
        acceptOpinionSummaryVo.setDocumentNum(null);
        acceptOpinionSummaryVo.setBuildArea(applyinstProj.getCentralCode());
        acceptOpinionSummaryVo.setProjAddr(applyinstProj.getProjAddr());
        acceptOpinionSummaryVo.setBuildArea(applyinstProj.getBuildAreaSum());
        if(applyinstProj.getAboveFloor()!=null)
        acceptOpinionSummaryVo.setAboveFloor(applyinstProj.getAboveFloor().toString());
        acceptOpinionSummaryVo.setBuildUnitName(buildUnit);
        acceptOpinionSummaryVo.setJianliUnitName(jianliUnit);
        acceptOpinionSummaryVo.setDesignUnitName(designUnit);
        acceptOpinionSummaryVo.setKanchaUnitName(kanchaUnit);
        acceptOpinionSummaryVo.setShigongUnitName(shigongUnit);
        acceptOpinionSummaryVo.setLinkman(linkman);
        acceptOpinionSummaryVo.setLinkmanPhone(linkmanPhone);

        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        String regionId = aeaProjInfo.getRegionalism();
        if(StringUtils.isNotBlank(regionId)){
            BscDicRegion region = bscDicRegionService.getBscDicRegionById(regionId);
            if(region!=null)
                acceptOpinionSummaryVo.setRegionName(region.getRegionName());
        }
        String projLevel = aeaProjInfo.getProjLevel();
        if(StringUtils.isNotBlank(projLevel)){
            acceptOpinionSummaryVo.setImportantProj("是");
        }else{
            acceptOpinionSummaryVo.setImportantProj("否");
        }


        AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);

        if(stageinst==null)
            throw new RuntimeException("获取申报阶段实例信息失败，请检查当前申报实例ID是否正确！");

        List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByStageinstId(stageinst.getStageinstId());

        if(iteminstList!=null&&iteminstList.size()>0){
            Map<String,String> deptOpinions = new HashMap<>();
            for(AeaHiIteminst iteminst:iteminstList){
                String itemCategoryMark = iteminst.getItemCategoryMark();
                //获取符合要求的事项节点的审批意见
                if(Arrays.asList(itemCategoryMarks).contains(itemCategoryMark)){
                    String procinstId = iteminst.getProcinstId();

                    if(StringUtils.isNotBlank(procinstId)){
                        List<BpmHistoryCommentForm> commentFormList = bpmTaskService.getHistoryCommentsByProcessInstanceId(procinstId);

                        if(commentFormList!=null&&commentFormList.size()>0) {
                            //倒序排序，获取最后一个节点的意见
                            Collections.sort(commentFormList, new Comparator<BpmHistoryCommentForm>() {
                                @Override
                                public int compare(BpmHistoryCommentForm o1, BpmHistoryCommentForm o2) {
                                    if(o1.getEndDate()==null||o2.getEndDate()==null){
                                        return o1.getSigeInDate().compareTo(o2.getSigeInDate());
                                    }else{
                                        return o1.getEndDate().compareTo(o2.getEndDate());
                                    }
                                }
                            });

                            deptOpinions.put(iteminst.getApproveOrgName()+"（"+iteminst.getIteminstName()+"）", commentFormList.get(0).getCommentMessage());
                        }
                    }
                }
            }

            //获取联合验收二级流程“出具联合验收意见”节点的意见
            if(StringUtils.isNotBlank(yanshouProcInstId)){
                //联合验收二级流程的“出具联合验收意见”节点的编号为：chujulianheyanshouyijian
                String taskDefKey = "chujulianheyanshouyijian";

                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(yanshouProcInstId).singleResult();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
                Process process = bpmnModel.getProcesses().get(0);
                //获取当前任务定义信息
                UserTask currTaskElement = (UserTask) process.getFlowElement(taskDefKey);

                boolean isSubFlow = false;
                if(currTaskElement!=null) {
                    List<FlowableListener> taskListeners = currTaskElement.getTaskListeners();
                    if (taskListeners != null && taskListeners.size() > 0) {
                        for (FlowableListener listener : taskListeners) {
                            if (listener.getSubFlow()) {
                                isSubFlow = true;
                                break;
                            }
                        }
                    }
                }

                //如果是子流程，需要获取子流程的意见
                if(isSubFlow){
                    ActStoAppinstSubflow subflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(yanshouProcInstId);
                    if(subflow!=null){
                        String subflowProcinstId = subflow.getSubflowProcinstId();
                        List<BpmHistoryCommentForm> subflowComments = bpmTaskService.getHistoryCommentsByProcessInstanceId(subflowProcinstId);
                        if(subflowComments!=null&&subflowComments.size()>0) {
                            //倒序排序，获取最后一个节点的意见
                            Collections.sort(subflowComments, new Comparator<BpmHistoryCommentForm>() {
                                @Override
                                public int compare(BpmHistoryCommentForm o1, BpmHistoryCommentForm o2) {
                                    if(o1.getEndDate()==null||o2.getEndDate()==null){
                                        return o1.getSigeInDate().compareTo(o2.getSigeInDate());
                                    }else{
                                        return o1.getEndDate().compareTo(o2.getEndDate());
                                    }
                                }
                            });

                            deptOpinions.put(subflowComments.get(0).getOrgName(),subflowComments.get(0).getCommentMessage());
                        }
                    }
                }else{
                    List<BpmHistoryCommentForm> commentFormList = bpmTaskService.getHistoryCommentsByTaskNode(yanshouProcInstId,taskDefKey);
                    if(commentFormList!=null&&commentFormList.size()>0){
                        BpmHistoryCommentForm commentForm = commentFormList.get(0);
                        if(commentForm!=null&&StringUtils.isNotBlank(commentForm.getOrgName())){
                            deptOpinions.put(commentForm.getOrgName(),commentForm.getCommentMessage());
                        }
                    }
                }
            }

            acceptOpinionSummaryVo.setDeptOpinions(deptOpinions);
        }

        return acceptOpinionSummaryVo;
    }

    /**
     * 根据申报实例ID，生成 竣工验收阶段汇总意见 批文批复（只适合于竣工验收阶段）
     * @param applyinstId 申报实例ID
     * @param yanshouProcInstId 竣工验收阶段验收二级流程的实例ID，可以为空
     * @throws Exception
     */
    public void createOpinionSummaryPwpf(String applyinstId,String yanshouProcInstId) throws Exception {
        String currentOrgId = SecurityContext.getCurrentOrgId();
        //现获取联合验收意见和申报相关信息
        ProjAcceptOpinionSummaryVo projAcceptOpinionSummaryVo = caculateProjAcceptOpinionSummary(applyinstId, yanshouProcInstId);
        ReceiveBaseVo receiveBaseVo = new ReceiveBaseVo();
        receiveBaseVo.setProjAcceptOpinionSummaryVo(projAcceptOpinionSummaryVo);
        receiveBaseVo.setReceiptType("14");
        receiveBaseVo.setApplyinstId(applyinstId);

        //生成意见书，会再本地系统临时目录下生成，后续完成后自动删除
        String fileUrl = ReceivePDFUtils.createPDF(receiveBaseVo);

        //查询默认的联合验收终审意见批文批复材料
        AeaItemMat query = new AeaItemMat();
        query.setMatCode(MAT_CODE);
        query.setRootOrgId(currentOrgId);
        query.setIsDeleted("0");
        List<AeaItemMat> aeaItemMats = aeaItemMatMapper.listAeaItemMat(query);
        if (aeaItemMats.size() > 0) {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            //获取当前需要创建批复的事项实例
            AeaHiIteminst aeaHiIteminst = getPwpfAeaHiIteminst(applyinstId, currentOrgId);
            //先删除原有已经生成的批文批复
            deleteOpinionSummaryPwpf(aeaHiIteminst.getIteminstId());

            AeaItemMat aeaItemOfficeMat = aeaItemMats.get(0);
            AeaItemInout bindInout = getBindingAeaItemInoutOrCreateNewOne(aeaHiIteminst.getItemVerId(), aeaItemOfficeMat.getMatId());
            //创建批文批复材料实例
            AeaHiItemMatinst aeaHiItemMatinst = createAeaItemMatinst(aeaHiApplyinst, aeaItemOfficeMat, receiveBaseVo.getReceiveCertNo());
            //上传附件，将意见书pdf文件和材料实例关联
            File file = new File(fileUrl);
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),null, inputStream);
            fileUtilsService.upload(TABLE_NAME, PK_NAME, aeaHiItemMatinst.getMatinstId(), null, multipartFile);
            //创建事项输入输出实例
            createAeaItemInoutinst(aeaHiIteminst.getIteminstId(), bindInout.getInoutId(), aeaHiItemMatinst.getMatinstId(), "1");

            //自动删除
            file.delete();
        }
    }

    private AeaHiIteminst getPwpfAeaHiIteminst(String applyinstId, String currentOrgId) throws Exception {
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        //暂时默认将批文批复绑定为 房屋建筑工程和市政基础设施工程竣工验收备案 事项的输出材料
        AeaItemBasic query1 = new AeaItemBasic();
        query1.setIsDeleted("0");
        query1.setRootOrgId(currentOrgId);
        query1.setItemCategoryMark(ITEM_CATEGORY);
        List<AeaItemBasic> itemBasics = aeaItemBasicService.listAeaItemBasic(query1);
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstList.get(0);
        boolean flag = false;
        for(int i=0,len=aeaHiIteminstList.size(); i<len; i++){
            aeaHiIteminst = aeaHiIteminstList.get(i);
            for(int j=0,lenj=itemBasics.size(); j<lenj; j++){
                if(aeaHiIteminst.getItemVerId().equals(itemBasics.get(j).getItemVerId())){
                    flag = true;
                    break;
                }
            }
            if(flag) break;
        }
        return aeaHiIteminst;
    }

    /**
     * 根据申报实例ID，查询是否已经生成了竣工验收阶段汇总意见 批文批复（只适合于竣工验收阶段）
     * @param applyinstId 申报实例ID
     * @throws Exception
     */
    public boolean checkOpinionSummaryPwpf(String applyinstId) throws Exception {
        String currentOrgId = SecurityContext.getCurrentOrgId();
        //获取当前需要创建批复的事项实例
        AeaHiIteminst aeaHiIteminst = getPwpfAeaHiIteminst(applyinstId, currentOrgId);
        if(aeaHiIteminst != null){
            List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listOfficialDocsByIteminstId(aeaHiIteminst.getIteminstId());
            if(aeaHiItemMatinsts != null && aeaHiItemMatinsts.size() > 0){
                for(int i=0,len=aeaHiItemMatinsts.size(); i<len; i++){
                    if(aeaHiItemMatinsts.get(i).getMatinstCode().equals(MAT_CODE)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 删除事项批复批复信息
     * @param iteminstId
     * @throws Exception
     */
    private void deleteOpinionSummaryPwpf(String iteminstId) throws Exception{
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listOfficialDocsByIteminstId(iteminstId);
        if(aeaHiItemMatinsts != null && aeaHiItemMatinsts.size() > 0){
            for(int i=0,len=aeaHiItemMatinsts.size(); i<len; i++){
                AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinsts.get(i);
                if(aeaHiItemMatinst.getMatinstCode().equals(MAT_CODE)){
                    String matinstId = aeaHiItemMatinst.getMatinstId();
                    aeaHiItemInoutinstMapper.deleteAeaHiItemInoutinstByMatinstId(matinstId);
                    List<BscAttForm> bscAttForms = bscAttMapper.listAttLinkAndDetailByTablePKRecordId(TABLE_NAME, PK_NAME, matinstId, SecurityContext.getCurrentOrgId());
                    if(bscAttForms.size() > 0) {
                        Set<String> detailIds = bscAttForms.stream().map(BscAttForm::getDetailId).collect(Collectors.toSet());
                        fileUtilsService.deleteAttachments(detailIds.toArray(new String[]{}));
                        bscAttMapper.deleteAttLinkByDetailId(bscAttForms.get(0).getDetailId());
                        aeaHiItemMatinstMapper.deleteAeaHiItemMatinst(matinstId);
                    }
                }
            }
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
            return bindingInouts.get(0);
        }
        return bindItemAndOfficialDocMat(itemVerId, matId);
    }

    /*
     * 为事项建立到批文批复定义的关系
     * 新增一条aea_item与aea_item_mat的关联记录到aea_item_inout，
     */
    private AeaItemInout bindItemAndOfficialDocMat(String itemVerId, String matId) throws Exception {
        AeaItemInout bindingInout = new AeaItemInout();
        bindingInout.setInoutId(UUID.randomUUID().toString());
        bindingInout.setItemVerId(itemVerId);
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

    /**
     * 创建批文批复材料实例
     * @param aeaHiApplyinst
     * @param defaultOfficialMat
     * @param docNo
     * @return
     */
    private AeaHiItemMatinst createAeaItemMatinst(AeaHiApplyinst aeaHiApplyinst, AeaItemMat defaultOfficialMat, String docNo) {

        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        try {
            // 文件文号
            aeaHiItemMatinst.setOfficialDocNo(docNo);
            // 文件标题
            aeaHiItemMatinst.setOfficialDocTitle(defaultOfficialMat.getMatName());
            // 纸质份数
            aeaHiItemMatinst.setRealPaperCount(0L);
            aeaHiItemMatinst.setRealCopyCount(0L);
            aeaHiItemMatinst.setAttCount(1L);

            aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());

            aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
            aeaHiItemMatinst.setCreateTime(new Date());

            aeaHiItemMatinst.setMatId(defaultOfficialMat.getMatId());
            aeaHiItemMatinst.setMatinstCode(defaultOfficialMat.getMatCode());
            aeaHiItemMatinst.setMatinstName(defaultOfficialMat.getMatName());

            List<AeaProjInfo> applyProj = aeaProjInfoService.findApplyProj(aeaHiApplyinst.getApplyinstId());
            if (applyProj.size() < 1) throw new Exception("找不到项目信息");
            aeaHiItemMatinst.setProjInfoId(applyProj.get(0).getProjInfoId());
            aeaHiItemMatinst.setUnitInfoId(getUnitInfoIds(aeaHiApplyinst.getLinkmanInfoId()));
            aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiItemMatinst.setMemo("出具联合验收意见书节点自动生成批文批复");
            aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增批复时保存AeaHiItemMatinst失败");
        }
        return aeaHiItemMatinst;
    }

    /**
     * 创建事项输入输出实例
     * @param iteminstId
     * @param inoutId
     * @param matinstId
     * @param isCollected
     */
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

    /**
     * 将批文批复自动关联为后置事项的输入材料
     * @param matId
     * @param matinstId
     * @param projInfoId
     * @param inoutId
     * @param iteminstId
     * @param itemVerId
     * @throws Exception
     */
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
}
