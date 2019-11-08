package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStateForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 情形表单关联表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParStateFormMapper {

     void insertAeaParStateForm(AeaParStateForm aeaParStateForm) ;

     void updateAeaParStateForm(AeaParStateForm aeaParStateForm);

     void deleteAeaParStateForm(@Param("id") String id) ;

     List<AeaParStateForm> listAeaParStateForm(AeaParStateForm aeaParStateForm) ;

     AeaParStateForm getAeaParStateFormById(@Param("id") String id) ;

    /**
     * 获取阶段情形表单
     *
     * @param aeaParStateForm
     * @return
     */
    List<AeaParStateForm> listStageStateFormWithActStoForm(AeaParStateForm aeaParStateForm);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelAeaParStateFormByIds(@Param("ids") String[] ids);

    /**
     * 获取最大排序
     * @return
     */
    Long getMaxSortNo(@Param("stageId") String stageId);

    /**
     * 获取当前组织表单
     *
     * @param aeaParStateForm
     * @return
     */
    List<AeaParStateForm> listCurOrgForm(AeaParStateForm aeaParStateForm);

    /**
     * 通过条件删除表单数据关联
     *
     * @param aeaParStateForm
     */
    void deleteAeaParStateFormByCond(AeaParStateForm aeaParStateForm);

    List<AeaParStateForm> listStageNoSelectForm(AeaParStateForm form);
}
