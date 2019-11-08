package com.augurit.aplanmis.admin.market.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.contract.service.AeaImContractAdminService;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.ApplyPostponeServiceData;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/20
 */
@Service
@Transactional
public class AeaImContractAdminServiceImpl implements AeaImContractAdminService {

    @Autowired
    private AeaImContractMapper aeaImContractMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private IBscAttService bscAttService;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;

    @Override
    public List<AeaImContract> listAuditAeaImContractByPage(String keyword, String auditFlag, Page page)throws Exception{
        AeaImContract aeaImContract = new AeaImContract();

        if(StringUtils.isNotBlank(keyword)) {
            aeaImContract.setKeyword(keyword);
        }
        if(StringUtils.isNotBlank(auditFlag)) {
            aeaImContract.setAuditFlag(auditFlag);
        }
        aeaImContract.setIsDelete(DeletedStatus.NOT_DELETED.getValue());

        PageHelper.startPage(page);

        List<AeaImContract> list = aeaImContractMapper.listAuditAeaImContract(aeaImContract);

        for(AeaImContract contract:list){
            AeaImPurchaseinst aeaImPurchaseinst = aeaImPurchaseinstMapper.getlastestPurchaseinstForContract(aeaImContract.getProjPurchaseId());
            if(aeaImPurchaseinst!=null && StringUtils.isNotBlank(aeaImPurchaseinst.getPurchaseinstId())){
                contract.setAeaImPurchaseinst(aeaImPurchaseinst);
            }
        }

        return list;

//        List<AeaImContract> arrayLists = new ArrayList<>();
//        for(int i=0;i<20;i++){
//            AeaImContract aeaImContract = new AeaImContract();
//            aeaImContract.setAuditFlag("1");
//            aeaImContract.setUnitBiddingId("1");
//            aeaImContract.setKeyword(keyword);
//            aeaImContract.setAuditFlag(auditFlag);
//            aeaImContract.setContractCode("00000"+i);
//            aeaImContract.setContractName("服务合奋斗奋斗奋斗奋斗奋斗分"+i);
//            aeaImContract.setContractId(i+"");
//            aeaImContract.setCreateTime(new Date());
//            arrayLists.add(aeaImContract);
//        }
//
//        return arrayLists;
    }

    @Override
    public AeaImContract getAeaImContractByContractId(String contractId)throws Exception{

        if(StringUtils.isBlank(contractId)){
            throw new InvalidParameterException(contractId);
        }

        AeaImContract aeaImContract = aeaImContractMapper.getAeaImContractById(contractId);

        if(aeaImContract!=null){
            if(StringUtils.isNotBlank(aeaImContract.getOwnerUnitInfoId())){
                AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(aeaImContract.getOwnerUnitInfoId());

                if(aeaUnitInfo!=null){
                    aeaImContract.setOwnerUnitInfoName(aeaUnitInfo.getApplicant());
                }
            }

            if(StringUtils.isNotBlank(aeaImContract.getServiceUnitInfoId())){
                AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(aeaImContract.getServiceUnitInfoId());

                if(aeaUnitInfo!=null){
                    aeaImContract.setServiceUnitInfoName(aeaUnitInfo.getApplicant());
                }
            }

            if(StringUtils.isNotBlank(aeaImContract.getProjPurchaseId())){
                AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImContract.getProjPurchaseId());

                if(aeaImProjPurchase!=null){
                    AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(aeaImProjPurchase.getProjInfoId());
                    aeaImContract.setProjName(aeaProjInfo.getProjName());

                    AeaImPurchaseinst aeaImPurchaseinst = aeaImPurchaseinstMapper.getlastestPurchaseinstForContract(aeaImContract.getProjPurchaseId());

                    if(aeaImPurchaseinst!=null && StringUtils.isNotBlank(aeaImPurchaseinst.getPurchaseinstId())){
                        aeaImContract.setAeaImPurchaseinst(aeaImPurchaseinst);

                        if(AeaImContract.ContractAuditFlag.审核中.getAuditFlag().equals(aeaImContract.getAuditFlag())) {
                            if (AeaImPurchaseinst.OperateAction.延长结束服务时间.getAction().equals(aeaImPurchaseinst.getOperateAction())) {
                                ApplyPostponeServiceData applyPostponeServiceData = JSON.parseObject(aeaImPurchaseinst.getApplyData(), ApplyPostponeServiceData.class);

                                if(applyPostponeServiceData!=null) {
                                    aeaImContract.setPostponeServiceEndTime(applyPostponeServiceData.getServiceEndTime());
                                }
                            }
                        }

                        if(Status.ON.equals(aeaImPurchaseinst.getIsOwnFile())) {
                            aeaImPurchaseinst.setBscAttForms(bscAttService.listAttLinkAndDetailByTablePKRecordId("AEA_IM_PURCHASEINST", "PURCHASEINST_ID", aeaImPurchaseinst.getPurchaseinstId(), SecurityContext.getCurrentOrgId()));
                        }

                    }
                }
            }

            aeaImContract.setBscAttForms(bscAttService.listAttLinkAndDetailByTablePKRecordId("AEA_IM_CONTRACT", "CONTRACT_ID", contractId,SecurityContext.getCurrentOrgId()));
        }

        return aeaImContract;


//        AeaImContract aeaImContract = new AeaImContract();
//        aeaImContract.setContractId(contractId);
//
//        if(contractId.equals("1")) {
//            aeaImContract.setAuditFlag("1");
//        }else{
//            aeaImContract.setAuditFlag("3");
//        }
//        aeaImContract.setUnitBiddingId("1");
//        aeaImContract.setContractCode("000001");
//        aeaImContract.setContractName("服务合奋斗奋斗奋斗奋斗奋斗分");
//        aeaImContract.setCreateTime(new Date());
//        aeaImContract.setServiceUnitInfoName("服务企业");
//        aeaImContract.setOwnerUnitInfoName("业主企业");
//        aeaImContract.setPrice("300000");
//        aeaImContract.setServiceContent("服务内容服务内容服务内容服务内容服务内容服务内容");
//        aeaImContract.setMemo("备注啦");
//        aeaImContract.setAuditTime(new Date());
//        aeaImContract.setAuditOpinion("没啦");
//        aeaImContract.setServiceStartTime(new Date());
//        aeaImContract.setServiceEndTime(new Date());
//
//        List<BscAttForm> list = new ArrayList<>();
//
//        for(int i=0;i<5;i++){
//            BscAttForm bscAttForm = new BscAttForm();
//            bscAttForm.setAttName("奋斗奋斗分奋斗分地方.pdf");
//            bscAttForm.setDetailId("3a9c013c-2c4c-461d-b0be-f142159832c7");
//            list.add(bscAttForm);
//        }
//
//        aeaImContract.setBscAttForms(list);
//
//        return aeaImContract;
    }

    @Override
    public void auditContract(String contractId,String auditFlag,String auditOpinion)throws Exception{
        if(StringUtils.isBlank(contractId) || StringUtils.isBlank(auditFlag)){
            throw new InvalidParameterException(contractId,auditFlag);
        }

        AeaImContract aeaImContract = new AeaImContract();
        aeaImContract.setContractId(contractId);
        aeaImContract.setAuditFlag(auditFlag);
        aeaImContract.setAuditTime(new Date());
        aeaImContract.setAuditOpinion(auditOpinion);
        aeaImContract.setModifyTime(new Date());
        aeaImContract.setModifier(SecurityContext.getCurrentUserName());

        if(AeaImContract.ContractAuditFlag.已确定.getAuditFlag().equals(auditFlag)) {
            AeaImContract oldAeaImContract = aeaImContractMapper.getAeaImContractById(contractId);

            if (oldAeaImContract != null){
                AeaImPurchaseinst aeaImPurchaseinst = aeaImPurchaseinstMapper.getlastestPurchaseinstForContract(oldAeaImContract.getProjPurchaseId());
                if(aeaImPurchaseinst!=null && StringUtils.isNotBlank(aeaImPurchaseinst.getPurchaseinstId())){

                    if (AeaImPurchaseinst.OperateAction.延长结束服务时间.getAction().equals(aeaImPurchaseinst.getOperateAction())) {
                        ApplyPostponeServiceData applyPostponeServiceData = JSON.parseObject(aeaImPurchaseinst.getApplyData(), ApplyPostponeServiceData.class);
                        if(applyPostponeServiceData!=null) {
                            aeaImContract.setPostponeServiceEndTime(applyPostponeServiceData.getServiceEndTime());
                        }
                    }
                }
            }

        }

        aeaImContractMapper.updateAeaImContract(aeaImContract);

    }

}
