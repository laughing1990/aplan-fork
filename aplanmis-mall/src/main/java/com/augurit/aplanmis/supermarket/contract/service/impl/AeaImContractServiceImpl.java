package com.augurit.aplanmis.supermarket.contract.service.impl;

import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.ChosenProjInfoVo;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.supermarket.apply.service.RestImApplyService;
import com.augurit.aplanmis.supermarket.contract.service.AeaImContractService;
import com.augurit.aplanmis.supermarket.contract.vo.ContractVo;
import com.augurit.aplanmis.supermarket.utils.LoginUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * -Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-03 17:58:23</li>
 * </ul>
 */
@Service
@Transactional
public class AeaImContractServiceImpl implements AeaImContractService {

    private static Logger logger = LoggerFactory.getLogger(AeaImContractServiceImpl.class);

    @Autowired
    private AeaImContractMapper aeaImContractMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private IBscAttService bscAtService;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;
    @Autowired
    private RestImApplyService restImApplyService;
    @Autowired
    private FileUtilsService fileUtilsService;


    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    /*public void saveAeaImContract(AeaImContract aeaImContract) throws Exception {
        aeaImContract.setRootOrgId(topOrgId);
        aeaImContractMapper.insertAeaImContract(aeaImContract);
    }*/

    @Override
    public AeaImContract saveAeaImContract(AeaImContract aeaImContract, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        AeaImContract query = new AeaImContract();
        query.setUnitBiddingId(aeaImContract.getUnitBiddingId());
        query.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        query.setRootOrgId(topOrgId);
        List list = aeaImContractMapper.listAeaImContract(query);
        if (list != null && list.size() > 0) {
            throw new RuntimeException("合同已存在");
        }

        aeaImContract.setContractId(UuidUtil.generateUuid());
        aeaImContract.setCreateTime(new Date());
        aeaImContract.setCreater(SecurityContext.getCurrentUserName());
        aeaImContract.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        aeaImContract.setAuditFlag("0");
        aeaImContract.setIsConfirm("0");
        aeaImContract.setRootOrgId(topOrgId);
        aeaImContractMapper.insertAeaImContract(aeaImContract);

        saveContractFiles(files, aeaImContract.getContractId());

        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setUnitBiddingId(aeaImContract.getUnitBiddingId());
        aeaImUnitBidding.setIsUploadContract("1");
        aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);

        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImContract.getProjPurchaseId());
        if (aeaImProjPurchase == null) {
            throw new RuntimeException("项目不存在");
        }
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
        aeaImPurchaseinst.setProjPurchaseId(aeaImContract.getProjPurchaseId());
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
        aeaImPurchaseinst.setOperateAction("3");
        aeaImPurchaseinst.setOperateDescribe("新增合同");
        aeaImPurchaseinst.setIsOwnFile((files != null && files.size() > 0) ? "1" : "0");
        aeaImPurchaseinst.setLinkmanInfoId(loginInfoVo.getUserId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);

        return aeaImContract;
    }

    private void saveContractFiles(List<MultipartFile> files, String recordId) {
        if (files != null && !files.isEmpty() && StringUtils.isNotBlank(recordId)) {
            String tableName = "AEA_IM_CONTRACT";
            String pkName = "CONTRACT_ID";
            FileUtils.uploadFile(tableName, pkName, recordId, files);
        }
    }

    @Override
    public AeaImContract updateAeaImContract(AeaImContract aeaImContract, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        aeaImContract.setModifyTime(new Date());
        aeaImContract.setModifier(SecurityContext.getCurrentUserName());
        aeaImContractMapper.updateAeaImContract(aeaImContract);

        saveContractFiles(files, aeaImContract.getContractId());

        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImContract.getProjPurchaseId());
        if (aeaImProjPurchase == null) {
            throw new RuntimeException("项目不存在");
        }
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
        aeaImPurchaseinst.setProjPurchaseId(aeaImContract.getProjPurchaseId());
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
        aeaImPurchaseinst.setOperateAction("4");
        aeaImPurchaseinst.setOperateDescribe("修改合同");
        aeaImPurchaseinst.setIsOwnFile((files != null && files.size() > 0) ? "1" : "0");
        aeaImPurchaseinst.setLinkmanInfoId(loginInfoVo.getUserId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);

        return aeaImContract;
    }

    /*public void updateAeaImContract(AeaImContract aeaImContract) throws Exception {
        aeaImContractMapper.updateAeaImContract(aeaImContract);
    }*/

    @Override
    public boolean deleteContractFile(String detailId) throws Exception {

        if (StringUtils.isNotBlank(detailId)) {
            String[] detailIds = detailId.split(",");

            if (detailIds != null && detailIds.length > 0) {
                return fileUtilsService.deleteAttachments(detailIds);
//                return FileUtils.deleteFiles(detailIds);
            }
        }
        return false;
    }

    @Override
    public void deleteAeaImContractById(String contractId) throws Exception {
        //采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
        //已确认合同不能删除
        AeaImContract contract = aeaImContractMapper.getAeaImContractById(contractId);
        if (null == contract) throw new Exception("can not find contract");
        String auditFlag = contract.getAuditFlag();
        if ("1".equals(auditFlag)) {
            throw new Exception("has confirm contract ,can not delete");
        }
        String projPurchaseId = contract.getProjPurchaseId();
        AeaImProjPurchase purchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);
        String[] status = new String[]{"1", "2"};
        if (null != purchase && Arrays.binarySearch(status, purchase.getAuditFlag()) > 0) {
            throw new Exception("service status over ,can not delete");
        }
        AeaImContract aeaImContract = new AeaImContract();
        aeaImContract.setIsDelete(DeletedStatus.DELETED.getValue());
        aeaImContract.setContractId(contractId);
        aeaImContract.setModifier(SecurityContext.getCurrentUserName());
        aeaImContract.setModifyTime(new Date());
        aeaImContractMapper.updateAeaImContract(aeaImContract);
    }

    public PageInfo<AeaImContract> listAeaImContract(AeaImContract aeaImContract, Page page) throws Exception {
        aeaImContract.setRootOrgId(topOrgId);
        PageHelper.startPage(page);
        List<AeaImContract> list = aeaImContractMapper.listAeaImContract(aeaImContract);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImContract>(list);
    }

    @Override
    public AeaImContract getAeaImContractById(String contractId) throws Exception {
        if (StringUtils.isNotBlank(contractId)) {
            AeaImContract aeaImContract = aeaImContractMapper.getAeaImContractById(contractId);

            if (aeaImContract != null && DeletedStatus.NOT_DELETED.getValue().equals(aeaImContract.getIsDelete())) {

                String unitInfoId = aeaImContract.getServiceUnitInfoId();
                if ("1".equals(aeaImContract.getIsOwnerUpload())) {
                    unitInfoId = aeaImContract.getOwnerUnitInfoId();
                }
                AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
                if (aeaUnitInfo != null) {
                    aeaImContract.setCreaterUnitName(aeaUnitInfo.getApplicant());
                }
                aeaImContract.setBscAttForms(bscAtService.listAttLinkAndDetailByTablePKRecordId("AEA_IM_CONTRACT", "CONTRACT_ID", contractId, topOrgId));
                return aeaImContract;
            }
        }
        return null;
    }

    public List<AeaImContract> listAeaImContract(AeaImContract aeaImContract) throws Exception {
        aeaImContract.setRootOrgId(topOrgId);
        List<AeaImContract> list = aeaImContractMapper.listAeaImContract(aeaImContract);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public AeaImContract getNewContract(String projPurchaseId, HttpServletRequest request) throws Exception {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);

        if (loginInfoVo != null && StringUtils.isNotBlank(projPurchaseId)) {
            AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);

            if (aeaImProjPurchase != null) {

                AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
                aeaImUnitBidding.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImUnitBidding.setIsWonBid("1");
                aeaImUnitBidding.setProjPurchaseId(projPurchaseId);
                aeaImUnitBidding.setRootOrgId(topOrgId);
                List<AeaImUnitBidding> biddings = aeaImUnitBiddingMapper.listAeaImUnitBidding(aeaImUnitBidding);

                if (!biddings.isEmpty()) {
                    aeaImUnitBidding = biddings.get(0);
                    AeaImContract aeaImContract = new AeaImContract();
                    aeaImContract.setProjPurchaseId(projPurchaseId);
                    aeaImContract.setCreaterUnitName(loginInfoVo.getUnitName());
                    aeaImContract.setContractCode(BusinessUtils.getAutoContractCode());
                    aeaImContract.setServiceContent(aeaImProjPurchase.getServiceContent());
                    aeaImContract.setOwnerUnitInfoId(aeaImProjPurchase.getPublishUnitInfoId());
                    aeaImContract.setServiceUnitInfoId(aeaImUnitBidding.getUnitInfoId());
                    aeaImContract.setPrice(aeaImUnitBidding.getRealPrice());
                    aeaImContract.setUnitBiddingId(aeaImUnitBidding.getUnitBiddingId());
                    aeaImContract.setCreateTime(new Date());
                    aeaImContract.setCreater(SecurityContext.getCurrentUserName());
                    return aeaImContract;
                }
            }
        }

        return null;
    }

    @Override
    public List<ChosenProjInfoVo> getChosenProjInfoList(String keyword, String auditFlag, Page page, HttpServletRequest request) throws Exception {

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);

        if (loginInfoVo != null && StringUtils.isNotBlank(loginInfoVo.getUnitId())) {
            if (page != null) {
                PageHelper.startPage(page);
            }

            return aeaImContractMapper.getChosenProjInfoList(loginInfoVo.getUnitId(), auditFlag, keyword);
        }

        return new ArrayList<>();

    }

    /*@Override
    public AeaImContract confirmContractByContractId(AeaImContract aeaImContract, HttpServletRequest request) throws Exception {
        try {
            aeaImContract.setAuditFlag("1");
            aeaImContract.setIsConfirm("1");
            aeaImContractMapper.updateAeaImContract(aeaImContract);

            AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImContract.getProjPurchaseId());
            if (aeaImProjPurchase == null) {
                throw new RuntimeException("项目不存在");
            }
            LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
            AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
            aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
            aeaImPurchaseinst.setProjPurchaseId(aeaImContract.getProjPurchaseId());
            aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
            aeaImPurchaseinst.setCreateTime(new Date());
            aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
            aeaImPurchaseinst.setOperateDescribe("确认合同");
            aeaImPurchaseinst.setIsOwnFile("0");
            aeaImPurchaseinst.setLinkmanInfoId(loginInfoVo.getUserId());
            aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
            return aeaImContract;
        } catch (Exception e) {

        }

        return null;
    }*/

    /**
     * 更新或新增合同---20191114 add
     *
     * @param contractVo
     * @param request
     * @return
     */
    @Override
    public AeaImContract saveOrUpdateContract(ContractVo contractVo, HttpServletRequest request) throws Exception {
        String projPurchaseId = contractVo.getProjPurchaseId();
        AeaImProjPurchase purchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);
        if (null == purchase) throw new Exception("can not find proj purchase");

        String ownerUnitInfoId = purchase.getPublishUnitInfoId();
        //String rootOrgId = purchase.getRootOrgId();
        String serviceUnitInfoId = "";
        String unitBiddingId = "";

        AeaImContract aeaImContract = new AeaImContract();
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        //String creater = LoginUtil.getCreater(loginInfoVo);

        if (StringUtils.isBlank(contractVo.getContractId())) {
            //查询需要的信息
            List<AeaImUnitBidding> bidding = aeaImUnitBiddingMapper.getUnitBiddingByProjPurchase(projPurchaseId);
            if (!bidding.isEmpty()) {
                AeaImUnitBidding unitBidding = bidding.get(0);
                unitBiddingId = unitBidding.getUnitBiddingId();
                serviceUnitInfoId = unitBidding.getUnitInfoId();
            }

            aeaImContract = contractVo.createAeaImContract(serviceUnitInfoId, ownerUnitInfoId, unitBiddingId);
            boolean ownerUnit = LoginUtil.isOwnerUnit(loginInfoVo);
            if (ownerUnit) {
                aeaImContract.setIsOwnerUpload("1");//业主上传
            } else {
                aeaImContract.setIsOwnerUpload("0");
            }

        } else {
            BeanUtils.copyProperties(contractVo, aeaImContract);
            aeaImContract.setModifier(SecurityContext.getCurrentUserName());
            aeaImContract.setModifyTime(new Date());
        }
        restImApplyService.uploadContract(aeaImContract, purchase.getLinkmanInfoId(), request);
        return aeaImContract;
    }

    /**
     * 确认合同
     *
     * @param contractId 合同ID
     * @param request    REQUEST
     */
    @Override
    public boolean confirmContract(String contractId, String auditFlag, String auditOpinion, Date postponeServiceEndTime, HttpServletRequest request) throws Exception {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        LoginUtil.validLoginStatus(loginInfoVo);
        AeaImContract contract = aeaImContractMapper.getAeaImContractById(contractId);
        if (null == contract) throw new Exception("can not find contract");

        if (loginInfoVo.getIsOwner().equals(contract.getIsOwnerUpload())) {
            throw new Exception("只能由" + ("1".equals(loginInfoVo.getIsOwner()) ? "中介" : "业主") + "确认");
        }
        //String creater = LoginUtil.getCreater(loginInfoVo);
        contract.setModifier(SecurityContext.getCurrentUserName());
        restImApplyService.confirmContract(contract, auditFlag, auditOpinion, postponeServiceEndTime);
        return true;
    }

}

