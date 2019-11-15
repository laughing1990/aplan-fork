package com.augurit.aplanmis.supermarket.contract.service;

import com.augurit.aplanmis.common.domain.AeaImContract;
import com.augurit.aplanmis.common.vo.ChosenProjInfoVo;
import com.augurit.aplanmis.supermarket.contract.vo.ContractVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * -Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-03 17:58:23</li>
 * </ul>
 */
public interface AeaImContractService {
    /*//弃用，测试无问题后课删除 20191114
    @Deprecated
    void saveAeaImContract(AeaImContract aeaImContract) throws Exception;

    //弃用，测试无问题后课删除 20191114
    @Deprecated
    void updateAeaImContract(AeaImContract aeaImContract) throws Exception;*/

    void deleteAeaImContractById(String contractId) throws Exception;

    PageInfo<AeaImContract> listAeaImContract(AeaImContract aeaImContract, Page page) throws Exception;

    AeaImContract getAeaImContractById(String contractId) throws Exception;

    List<AeaImContract> listAeaImContract(AeaImContract aeaImContract) throws Exception;

    AeaImContract saveAeaImContract(AeaImContract aeaImContract, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    AeaImContract updateAeaImContract(AeaImContract aeaImContract, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    boolean deleteContractFile(String detailId) throws Exception;

    AeaImContract getNewContract(String projPurchaseId, HttpServletRequest request) throws Exception;

    List<ChosenProjInfoVo> getChosenProjInfoList(String keyword, String auditFlag, Page page, HttpServletRequest request) throws Exception;

//    @Deprecated
//    AeaImContract confirmContractByContractId(AeaImContract aeaImContract, HttpServletRequest request) throws Exception;

    /**
     * 更新或新增合同
     *
     * @param contractVo
     * @param request
     * @return
     */
    AeaImContract saveOrUpdateContract(ContractVo contractVo, HttpServletRequest request) throws Exception;

    /**
     * 确认合同
     *
     * @param contractId 合同ID
     * @param request    REQUEST
     */
    boolean confirmContract(String contractId, String auditFlag, String auditOpinion, Date postponeServiceEndTime, HttpServletRequest request) throws Exception;

}
