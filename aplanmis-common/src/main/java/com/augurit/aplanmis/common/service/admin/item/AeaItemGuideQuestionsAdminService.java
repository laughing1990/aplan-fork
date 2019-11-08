package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideQuestions;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * AeaItemGuideQuestionsAdminService class
 *
 * @author jjt
 * @date 2019/05/06
 */
public interface AeaItemGuideQuestionsAdminService {

    /**
     * 保存
     * @param aeaItemGuideQuestions
     */
    void saveAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions);

    /**
     * 更新
     * @param aeaItemGuideQuestions
     */
    void updateAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions);

    /**
     * 通过id单条删除
     *
     * @param id
     */
    void deleteAeaItemGuideQuestionsById(String id);

    /**
     * 分页获取
     *
     * @param aeaItemGuideQuestions
     * @param page
     * @return
     */
    PageInfo<AeaItemGuideQuestions> listAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions, Page page);

    /**
     * 通过id获取
     *
     * @param id
     * @return
     */
    AeaItemGuideQuestions getAeaItemGuideQuestionsById(String id);

    /**
     * 获取list列表，不带分页
     *
     * @param aeaItemGuideQuestions
     * @return
     */
    List<AeaItemGuideQuestions> listAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions);

    /**
     * 通过版本id批量删除
     *
     * @param itemVerId
     */
    void batchDeleteGuideQuestionsByItemVerId(String itemVerId, String rootOrgId);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelQuestAnswerByIds(String[] ids);

    Long getMaxSortNoByItemVerId(String itemVerId, String rootOrgId);
}
