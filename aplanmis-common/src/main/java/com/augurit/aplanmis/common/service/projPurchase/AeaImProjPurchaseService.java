package com.augurit.aplanmis.common.service.projPurchase;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.AeaImServiceVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author: lucas Chan
 * Date: 2019-11-8
 */
public interface AeaImProjPurchaseService {

    /**
     * 更新采购需求实例状态和采购需求历史实例状态
     *
     * @param purchaseId
     * @param newState
     * @param operateAction
     * @param opsLinkmanInfoId
     * @param option
     * @param taskId
     * @throws Exception
     */
    void updateProjPurchaseStateAndInsertPurchaseinstState(String purchaseId, String newState, String operateAction, String opsLinkmanInfoId, String option, String taskId) throws Exception;

    /**
     * 更新采购需求实例状态和采购需求历史实例状态，有附加数据JSON
     *
     * @param purchaseId
     * @param newState
     * @param operateAction
     * @param opsLinkmanInfoId
     * @param option
     * @param isOwnFile
     * @param applyDataJson
     * @param taskId
     * @throws Exception
     */
    void updateProjPurchaseStateAndInsertPurchaseinstStateAndApplyData(String purchaseId, String newState, String operateAction, String opsLinkmanInfoId, String option, String isOwnFile, String applyDataJson, String taskId) throws Exception;

    /**
     * 新增采购需求实例和采购需求历史实例
     *
     * @param aeaImProjPurchase
     * @throws Exception
     */
    void insertAeaImProjPurchaseAndInsertPurchaseinst(AeaImProjPurchase aeaImProjPurchase) throws Exception;

    /**
     * 根据服务事项获取服务
     *
     * @param itemVerId
     * @return
     */
    List<AeaImServiceVo> getItemServiceListByItemVerId(String itemVerId) throws Exception;


    /**
     * 文件上传
     * 批文文件上传officialRemarkFile，要求说明文件上传requireExplainFile
     *
     * @param request
     * @return
     */
    String uploadFiles(HttpServletRequest request) throws Exception;
}
