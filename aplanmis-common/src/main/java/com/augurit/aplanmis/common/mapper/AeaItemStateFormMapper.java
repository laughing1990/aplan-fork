package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemStateForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AeaItemStateFormMapper class
 *
 * @author jjt
 * @date 2019/4/21
 */
@Mapper
@Repository
public interface AeaItemStateFormMapper {

    /**
     *插入
     *
     * @param aeaItemStateForm
     */
    void insertAeaItemStateForm(AeaItemStateForm aeaItemStateForm);

    /**
     * 更新
     *
     * @param aeaItemStateForm
     */
    void updateAeaItemStateForm(AeaItemStateForm aeaItemStateForm);

    /**
     * 用过id删除
     *
     * @param id
     */
    void deleteAeaItemStateFormById(@Param("id") String id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelAeaItemStateFormByIds(@Param("ids") String[] ids);

    /**
     * 通过id获取
     *
     * @param id
     * @return
     */
    AeaItemStateForm getAeaItemStateFormById(@Param("id") String id);

    /**
     * 获取列表
     *
     * @param aeaItemStateForm
     * @return
     */
    List<AeaItemStateForm> listAeaItemStateForm(AeaItemStateForm aeaItemStateForm);

    /**
     * 获取当前结构表单数据
     *
     * @param form
     * @return
     */
    List<AeaItemStateForm> listCurOrgForm(AeaItemStateForm form);

    /**
     * 条件删除
     *
     * @param form
     */
    void deleteAeaItemStateFormByCond(AeaItemStateForm form);

    /**
     * 获取最大排序
     * @return
     */
    Long getMaxSortNo(@Param("itemVerId") String itemVerId, @Param("itemStateVerId")String itemStateVerId);

    /**
     * 事项情形表单
     *
     * @param form
     * @return
     */
    List<AeaItemStateForm> listItemStateFormRelInfo(AeaItemStateForm form);

    /**
     *
     * @param form
     * @return
     */
    List<AeaItemStateForm> listItemNoSelectForm(AeaItemStateForm form);
}
