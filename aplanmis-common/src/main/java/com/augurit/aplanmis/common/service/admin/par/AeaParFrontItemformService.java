package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.vo.AeaParFrontItemformVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段事项表单前置检测表-Service服务调用接口类
 */
public interface AeaParFrontItemformService {

    void saveAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    void updateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    void deleteAeaParFrontItemformById(String id) throws Exception;

    PageInfo<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception;

    AeaParFrontItemform getAeaParFrontItemformById(String id) throws Exception;

    List<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    PageInfo<AeaParFrontItemformVo> listAeaParFrontItemformVoByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception;

    Long getMaxSortNo(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    PageInfo<AeaParFrontItemformVo> listSelectParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception;

    AeaParFrontItemformVo getAeaParFrontItemformVoById(String frontItemformId) throws Exception;

    List<AeaParFrontItemformVo> getAeaParFrontItemformVoByStageId(String stageId);

    void batchSaveAeaParFrontItemform(String stageId,String stageItemIds)throws Exception;

    List<AeaParFrontItemformVo> listAeaParFrontItemformVoByNoPage(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    void changIsActive(String id, String rootOrgId);
}
