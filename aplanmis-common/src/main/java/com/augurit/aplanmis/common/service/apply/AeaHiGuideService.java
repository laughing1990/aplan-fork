package com.augurit.aplanmis.common.service.apply;

import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.vo.guide.GuideDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaHiGuideService {

    void deleteAeaHiGuideByGuideId(String guideId);

    void insertAeaHiGuide(AeaHiGuide record);

    AeaHiGuide getAeaHiGuideByGuideId(String guideId);

    void updateAeaHiGuide(AeaHiGuide record);

    void batchInsertAeaHiGuide(List<AeaHiGuide> list);

    PageInfo<AeaHiGuide> list(AeaHiGuide aeaHiGuide, Page page);

    /**
     * 获取部门辅导详情信息
     * @param guideId 部门辅导id
     */
    GuideDetailVo detail(String guideId) throws Exception;

    /**
     * 查询部门辅导的项目类型是否有被牵头部门修改过
     *
     * @param guideId 部门辅导id
     */
    AeaHiGuideDetail themeChangedBefore(String guideId);

    /**
     * 部门辅导操作结果保存
     *
     * @param aeaHiGuideDetails 辅导的事项
     */
    void guide(List<AeaHiGuideDetail> aeaHiGuideDetails);

    void solicitDept(List<AeaHiGuideDetail> aeaHiGuideDetails);

    List<AeaHiGuide> listAeaHiGuideListUnitIdOrLinkmanInfoId(AeaHiGuide query);
}
