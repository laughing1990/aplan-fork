package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParStageGuide;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.guide.AeaParStageGuideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ZhangXinhui
 * @date 2019/7/11 011 15:49
 * @desc
 **/
@RestController
@RequestMapping("/aea/par/stage/guide")
public class AeaParStageGuideController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageGuideController.class);

    @Autowired
    private AeaParStageAdminService aeaParStageAdminService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    @Autowired
    private AeaParStageGuideService aeaParStageGuideService;

    @Autowired
    private IBscAttService bscAttService;

    @RequestMapping("/guideBasicInfo.do")
    public ModelAndView basicInfoGuide(@RequestParam String stageId, ModelMap modelMap) {

        boolean curIsEditable = false;
        AeaParStage stage = aeaParStageAdminService.getAeaParStageById(stageId);
        AeaParThemeVer ver = aeaParThemeVerAdminService.getAeaParThemeVerById(stage.getThemeVerId());
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        String topOrgId = SecurityContext.getCurrentOrgId();
        AeaParStageGuide aeaParStageGuide = aeaParStageGuideService.getByStageId(stageId, topOrgId);
        if(aeaParStageGuide == null){
            aeaParStageGuide = new AeaParStageGuide();
            aeaParStageGuide.setStageId(stageId);
        }
        List<BscDicCodeItem> applyObjList = bscDicCodeService.getActiveItemsByTypeCode("ITEM_FWJGXZ", topOrgId);
        List<BscDicCodeItem> legalTypes = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", topOrgId);
        if (StringUtils.isNotBlank(aeaParStageGuide.getGuideId())) {
            parseAttNum(aeaParStageGuide, topOrgId);
        }
        modelMap.put("stageGuideForm", aeaParStageGuide);
        modelMap.put("curIsEditable", curIsEditable);
        modelMap.put("applyObjList", applyObjList);
        modelMap.put("legalTypes", legalTypes);
        return new ModelAndView("ui-jsp/aplanmis/par/stage/stage_guide_basic_inf");


    }

    @RequestMapping("/saveStageGuideBasicInfo.do")
    public Object saveStageGuide(AeaParStageGuide stageGuide, HttpServletRequest request) throws Exception {

        String topOrgId = SecurityContext.getCurrentOrgId();
        stageGuide.setRootOrgId(topOrgId);
        if (StringUtils.isBlank(stageGuide.getGuideId())) {
            stageGuide.setGuideId(UuidUtil.generateUuid());
            aeaParStageGuideService.insertAeaParStageGuide(stageGuide, request);
        } else {
            aeaParStageGuideService.saveAeaParStageGuide(stageGuide, request);
        }
        parseAttNum(stageGuide, topOrgId);
        return new ContentResultForm(true, stageGuide);
    }

    @RequestMapping("/delStageGuideAttFile.do")
    public Object delStageGuideAttFile(String detailId, String guideId, String type) {

        try {
            if (StringUtils.isBlank(guideId) || StringUtils.isBlank(type) || StringUtils.isBlank(detailId)) {
                return new ResultForm(false);
            }
            aeaParStageGuideService.delStageGuideAttFile(detailId, guideId, type);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ContentResultForm(false);
        }
        return new ContentResultForm(true);
    }

    private void parseAttNum(AeaParStageGuide stageGuide, String topOrgId) {

        List<BscAttForm> ckbllctList = bscAttService
                .listAttLinkAndDetailNoPage("AEA_PAR_STAGE_GUIDE", "CKBLLCT", stageGuide.getGuideId(), null, topOrgId, null);
        List<BscAttForm> wsbllctList = bscAttService
                .listAttLinkAndDetailNoPage("AEA_PAR_STAGE_GUIDE", "WSBLLCT", stageGuide.getGuideId(), null, topOrgId, null);
        List<BscAttForm> guideAttList = bscAttService
                .listAttLinkAndDetailNoPage("AEA_PAR_STAGE_GUIDE", "GUIDE_ATT", stageGuide.getGuideId(), null, topOrgId, null);
        stageGuide.setCkbllctNum(Optional.ofNullable(ckbllctList).orElse(new ArrayList<>()).size());
        stageGuide.setWsbllctNum(Optional.ofNullable(wsbllctList).orElse(new ArrayList<>()).size());
        stageGuide.setGuideAttNum(Optional.ofNullable(guideAttList).orElse(new ArrayList<>()).size());
    }
}
