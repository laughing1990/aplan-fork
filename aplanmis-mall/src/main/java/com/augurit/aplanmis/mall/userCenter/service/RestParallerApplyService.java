package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaParFactor;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.mall.main.vo.ItemListVo;
import com.augurit.aplanmis.mall.userCenter.vo.StageApplyDataPageVo;
import com.augurit.aplanmis.mall.userCenter.vo.StageApplyDataVo;
import io.jsonwebtoken.lang.Assert;

import java.util.List;
import java.util.Map;

public interface RestParallerApplyService {

    /**
     * 根据阶段ID查询事项一单清列表数据
     * @param stageId 阶段ID
     * @return
     */
    ItemListVo listItemAndStateByStageId(String stageId, String projInfoId, String regionalism, String projectAddress) throws Exception;

    /**
     * 根据rootOrgId查询因子列表
     * @param rootOrgId
     * @return
     * @throws Exception
     */
    List<AeaParFactor> listFactorByRootOrgId(String rootOrgId)throws Exception;

    /**
     * 根据父根因子获取子因子列表
     * @param parentFactorId
     * @return
     * @throws Exception
     */
    List<AeaParFactor> listChildFactorByParentFactorId(String parentFactorId)throws Exception;

    /**
     * 根据选择的因子智能推荐主题
     * @param factorIds
     * @return
     */
    Map<String,Object> getThemeByFactorIds(String[] factorIds);



    List<AeaItemMat> listMatByStateIdAndStageIdAndItemVerId(String stageId, String[] stateIds, String[] itemVerIds)throws Exception;

    /**
     * 仅实例化申请实例
     * @param stageApplyDataPageVo
     * @return
     * @throws Exception
     */
    public String onlyInstApply(StageApplyDataPageVo stageApplyDataPageVo) throws Exception;
}
