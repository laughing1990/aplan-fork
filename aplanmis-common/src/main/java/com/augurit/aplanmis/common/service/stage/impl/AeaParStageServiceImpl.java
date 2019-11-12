package com.augurit.aplanmis.common.service.stage.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小糊涂
 */
@Service
@Slf4j
public class AeaParStageServiceImpl implements AeaParStageService {
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    /**
     * 根据主题ID或主题版本ID查询主题下的阶段列表
     * 1 themeId!=null && themeVerId==null 查询主题下最新已发布或试运行下的阶段列表
     * 2 themeVerId!=null 查询指定主题版本的阶段列表
     *
     * @param themeId    主题ID 至少一个不为空
     * @param themeVerId 主题版本ID 至少一个不为空
     * @return List<AeaParStage>
     */
    @Override
    public List<AeaParStage> listAeaParStageByThemeIdOrThemeVerId(String themeId, String themeVerId, String topOrgId) throws Exception {
        if (StringUtils.isEmpty(topOrgId)) {
            topOrgId = SecurityContext.getCurrentOrgId();
        }
        if (StringUtils.isEmpty(themeVerId) && !StringUtils.isEmpty(themeId)) {
            AeaParThemeVer aeaParThemeVer = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum(themeId, null, topOrgId);
            if (null != aeaParThemeVer) {
                themeVerId = aeaParThemeVer.getThemeVerId();
            }
        }
        if (!StringUtils.isEmpty(themeVerId)) {
            return aeaParStageMapper.listAeaParStageByThemeVerId(themeVerId);
        }
        return new ArrayList<>();
    }

    /**
     * 根据 项目ID 或 编码（localCode||gcbm)
     *
     * @param projInfoId 项目ID
     * @param projCode   编码（localCode||gcbm)
     * @return List<AeaParStage>
     */
    @Override
    public List<AeaParStage> listAeaParStageByProjIdOrProjCode(String projInfoId, String projCode) throws Exception {
        String themeId = "";
        if (!StringUtils.isEmpty(projInfoId)) {
            AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
            if (null != aeaProjInfo && !StringUtils.isEmpty(aeaProjInfo.getThemeId())) {
                themeId = aeaProjInfo.getThemeId();
            }
        } else if (!StringUtils.isEmpty(projCode)) {
            //1 模糊搜索 查询；可能查询多个或者一个，默认取第一个
            List<AeaProjInfo> projList = aeaProjInfoService.findAeaProjInfoByKeyword(projCode);
            if (projList.size() > 0 && !StringUtils.isEmpty(projList.get(0).getThemeId())) {
                themeId = projList.get(0).getThemeId();
            }
        }
        AeaParThemeVer aeaParThemeVer = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum(themeId, null, SecurityContext.getCurrentOrgId());
        if (null != aeaParThemeVer) {
            return aeaParStageMapper.listAeaParStageByThemeVerId(aeaParThemeVer.getThemeVerId());
        }
        return null;
    }

    @Override
    public AeaParStage getAeaParStageById(String id) throws Exception {
        if (StringUtils.isEmpty(id)) throw new InvalidParameterException("参数id为空！");
        return aeaParStageMapper.getAeaParStageById(id);
    }

    @Override
    public AeaParStage getAeaParStageByIteminstId(String iteminstId) throws Exception {
        if (StringUtils.isEmpty(iteminstId)) throw new InvalidParameterException("参数iteminstId为空！");
        List<AeaParStage> list = aeaParStageMapper.getAeaParStageListByIteminstId(iteminstId);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public AeaParStage getAeaParStageByApplyinstId(String applyinstId) throws Exception {
        if (StringUtils.isEmpty(applyinstId)) throw new InvalidParameterException("参数applyinstId为空！");
        List<AeaParStage> list = aeaParStageMapper.getAeaParStageByApplyinstId(applyinstId);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<String> getApplyInstStatusByProjInfoIdAndStageId(String stageId, String projInfoId, String unitInfoId, String linkmanInfoId) throws Exception {
        if (StringUtils.isEmpty(stageId)) throw new InvalidParameterException("参数stageId为空！");
        if (StringUtils.isEmpty(projInfoId)) throw new InvalidParameterException("参数projInfoId为空！");
        if (StringUtils.isEmpty(unitInfoId))
            return aeaParStageMapper.getApplyInstStatusByProjInfoIdAndStageIdAndLinkmanInfoId(stageId, projInfoId, linkmanInfoId);
        return aeaParStageMapper.getApplyInstStatusByProjInfoIdAndStageIdAndUnitInfoId(stageId, projInfoId, unitInfoId);
    }

    @Override
    public List<AeaParStage> getCompletedStageByProjInfoId(String projInfoId) {
        List<AeaParStage> stages = new ArrayList();
        if (StringUtils.isEmpty(projInfoId)) return stages;
        return aeaParStageMapper.getCompletedStageByProjInfoId(projInfoId, SecurityContext.getCurrentOrgId());
    }

}
