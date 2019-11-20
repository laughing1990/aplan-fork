package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontStageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 阶段的前置阶段检测表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/front/stage")
public class AeaParFrontStageController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontStageController.class);

    @Autowired
    private AeaParFrontStageService aeaParFrontStageService;

    @RequestMapping("/listAeaParFrontStageByPage.do")
    public EasyuiPageInfo<AeaParFrontStage> listAeaParFrontStageByPage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception {

        PageInfo<AeaParFrontStage> pageInfo = aeaParFrontStageService.listAeaParFrontStageVoByPage(aeaParFrontStage, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaParFrontStage.do")
    public ResultForm getAeaParFrontStage(String frontStageId){

        try {
            if (StringUtils.isNotBlank(frontStageId)) {
                return new ContentResultForm<>(true, aeaParFrontStageService.getAeaParFrontStageVoById(frontStageId));
            } else {
                return new ResultForm(false, "frontStageId不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/saveOrUpdateAeaParFrontStage.do")
    public ResultForm saveOrUpdateAeaParFrontStage(AeaParFrontStage aeaParFrontStage){

        try {
            if (StringUtils.isNotBlank(aeaParFrontStage.getFrontStageId())) {
                aeaParFrontStageService.updateAeaParFrontStage(aeaParFrontStage);
            } else {
                aeaParFrontStage.setFrontStageId(UUID.randomUUID().toString());
                aeaParFrontStageService.saveAeaParFrontStage(aeaParFrontStage);
            }
            return new ContentResultForm<>(true, aeaParFrontStage);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/deleteAeaParFrontStageById.do")
    public ResultForm deleteAeaParFrontStageById(String id){

        try {
            logger.debug("删除阶段的前置阶段检测表Form对象，对象id为：{}", id);
            if (StringUtils.isNotBlank(id)) {
                aeaParFrontStageService.deleteAeaParFrontStageById(id);
            }
            return new ResultForm(true);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(String stageId) {

        try {
            return new ContentResultForm<>(true, aeaParFrontStageService.getMaxSortNo(stageId, SecurityContext.getCurrentOrgId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listSelectParFrontStage.do")
    public ResultForm listSelectParFrontStage(AeaParFrontStage aeaParFrontStage) {

        try {
            return new ContentResultForm<>(true, aeaParFrontStageService.listSelectParFrontStage(aeaParFrontStage));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listSelectParFrontStageByPage.do")
    public EasyuiPageInfo<AeaParFrontStage> listSelectParFrontStageByPage(AeaParFrontStage aeaParFrontStage,Page page) throws Exception{

        PageInfo<AeaParFrontStage> pageInfo = aeaParFrontStageService.listSelectParFrontStageByPage(aeaParFrontStage,page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }


    @RequestMapping("/batchSaveAeaParFrontStage.do")
    public ResultForm batchSaveAeaParFrontStage(String stageId, String[] histStageIds){

        try {
            aeaParFrontStageService.batchSaveAeaParFrontStage(stageId, histStageIds);
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/updateAeaParFrontStageSortNos.do")
    public ResultForm updateAeaParFrontStageSortNos(String[] ids, Long[] sortNos) throws Exception {

        if (ids != null && ids.length > 0 && sortNos != null && sortNos.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                AeaParFrontStage aeaParFrontStage = new AeaParFrontStage();
                aeaParFrontStage.setFrontStageId(ids[i]);
                aeaParFrontStage.setSortNo(sortNos[i]);
                aeaParFrontStageService.updateAeaParFrontStage(aeaParFrontStage);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递排序数据有问题,请检查!");
    }

    @RequestMapping("/listAeaParFrontStageByNoPage.do")
    public List<AeaParFrontStage> listAeaParFrontStageByNoPage(AeaParFrontStage aeaParFrontStage) throws Exception {

        List<AeaParFrontStage> list = aeaParFrontStageService.listAeaParFrontStageVoByNoPage(aeaParFrontStage);
        return list;
    }

    @RequestMapping("/changIsActive.do")
    public ResultForm changIsActive(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParFrontStageService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
