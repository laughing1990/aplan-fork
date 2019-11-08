package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParStateForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author jjt
 * @date 2019/4/16
 *
 * 情形与表单关联定义表-Service服务调用接口类
 */
public interface AeaParStateFormAdminService {

    /**
     *  保存
     *
     * @param aeaParStateForm
     */
    void saveAeaParStateForm(AeaParStateForm aeaParStateForm);

    /**
     * 更新
     *
     * @param aeaParStateForm
     */
    void updateAeaParStateForm(AeaParStateForm aeaParStateForm);

    /**
     *  删除
     *
     * @param id
     */
    void deleteAeaParStateFormById(String id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelAeaParStateFormByIds(String[] ids);

    /**
     *  获取
     *
     * @param id
     * @return
     */
    AeaParStateForm getAeaParStateFormById(String id);

    /**
     * 不带分页获取
     *
     * @param aeaParStateForm
     * @return
     */
    List<AeaParStateForm> listAeaParStateForm(AeaParStateForm aeaParStateForm);

    /**
     *  分页获取
     * @param aeaParStateForm
     * @param page
     * @return
     */
    PageInfo<AeaParStateForm> listAeaParStateForm(AeaParStateForm aeaParStateForm, Page page);

    /**
     * 获取最大排序
     * @return
     */
    Long getMaxSortNo(String stageId);

    /**
     * 获取组织表单以及情形关联表单
     * @param aeaParStateForm
     * @param page
     * @return
     */
    PageInfo<AeaParStateForm> listFormAndStateFormByPage(AeaParStateForm aeaParStateForm, Page page);

    /**
     * 保存情形表单
     *
     * @param parStageId
     * @param parStateId
     * @param isStateForm
     * @param formIds
     */
    void saveAeaParStateForms(String parStageId, String parStateId, String isStateForm, String[] formIds);

    void saveAeaParStateFormsAndNotDelOld(String parStageId, String parStateId, String isStateForm, String[] formIds);

    List<AeaParStateForm> listStageNoSelectForm(AeaParStateForm form);

    PageInfo<AeaParStateForm> listStageNoSelectFormByPage(AeaParStateForm form, Page page);
}
