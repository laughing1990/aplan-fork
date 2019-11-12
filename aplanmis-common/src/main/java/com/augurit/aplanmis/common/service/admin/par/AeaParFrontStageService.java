package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.augurit.aplanmis.common.vo.AeaParFrontStageVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段的前置阶段检测表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:41</li>
 * </ul>
 */
public interface AeaParFrontStageService {
    void saveAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    void updateAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    void deleteAeaParFrontStageById(String id) throws Exception;

    PageInfo<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception;

    AeaParFrontStage getAeaParFrontStageById(String id) throws Exception;

    List<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    PageInfo<AeaParFrontStageVo> listAeaParFrontStageVoByPage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception;

    Long getMaxSortNo(AeaParFrontStage aeaParFrontStage) throws Exception;

    AeaParFrontStageVo getAeaParFrontStageVoById(String frontStageId) throws Exception;

    List<AeaParFrontStageVo> listSelectParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    List<AeaParFrontStageVo> getHistStageByStageId(String stageId);

}
