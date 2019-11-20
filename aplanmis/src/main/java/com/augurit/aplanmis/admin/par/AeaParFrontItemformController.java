package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemformService;
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
 * 阶段事项表单前置检测表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/front/itemform")
public class AeaParFrontItemformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemformController.class);

    @Autowired
    private AeaParFrontItemformService aeaParFrontItemformService;

    @RequestMapping("/listAeaParFrontItemformByPage.do")
    public EasyuiPageInfo<AeaParFrontItemform> listAeaParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {

        PageInfo<AeaParFrontItemform> pageInfo = aeaParFrontItemformService.listAeaParFrontItemformVoByPage(aeaParFrontItemform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaParFrontItemform.do")
    public ResultForm getAeaParFrontItemform(String frontItemformId){

        try {
            if (StringUtils.isNotBlank(frontItemformId)) {
                return new ContentResultForm<>(true, aeaParFrontItemformService.getAeaParFrontItemformVoById(frontItemformId));
            } else {
                return new ResultForm(false, "frontItemformId不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/saveOrUpdateAeaParFrontItemform.do")
    public ResultForm saveOrUpdateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform){

        try {
            if (StringUtils.isNotBlank(aeaParFrontItemform.getFrontItemformId())) {
                aeaParFrontItemformService.updateAeaParFrontItemform(aeaParFrontItemform);
            } else {
                aeaParFrontItemform.setFrontItemformId(UUID.randomUUID().toString());
                aeaParFrontItemformService.saveAeaParFrontItemform(aeaParFrontItemform);
            }
            return new ContentResultForm<>(true, aeaParFrontItemform);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/deleteAeaParFrontItemformById.do")
    public ResultForm deleteAeaParFrontItemformById(String id) {

        try {
            logger.debug("删除阶段事项表单前置检测表Form对象，对象id为：{}", id);
            if (StringUtils.isNotBlank(id)) {
                aeaParFrontItemformService.deleteAeaParFrontItemformById(id);
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
            return new ContentResultForm<>(true, aeaParFrontItemformService.getMaxSortNo(stageId, SecurityContext.getCurrentOrgId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listSelectParFrontItemformByPage.do")
    public EasyuiPageInfo<AeaParFrontItemform> listSelectParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {

        PageInfo<AeaParFrontItemform> pageInfo = aeaParFrontItemformService.listSelectParFrontItemformByPage(aeaParFrontItemform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }


    @RequestMapping("/batchSaveAeaParFrontItemform.do")
    public ResultForm batchSaveAeaParFrontItemform(String stageId, String[] stageItemIds){

        try {
            aeaParFrontItemformService.batchSaveAeaParFrontItemform(stageId, stageItemIds);
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/updateAeaParFrontItemformSortNos.do")
    public ResultForm updateAeaParFrontItemformSortNos(String[] ids, Long[] sortNos) throws Exception {

        if (ids != null && ids.length > 0 && sortNos != null && sortNos.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                AeaParFrontItemform aeaParFrontItemform = new AeaParFrontItemform();
                aeaParFrontItemform.setFrontItemformId(ids[i]);
                aeaParFrontItemform.setSortNo(sortNos[i]);
                aeaParFrontItemformService.updateAeaParFrontItemform(aeaParFrontItemform);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递排序数据有问题,请检查!");
    }

    @RequestMapping("/listAeaParFrontItemformByNoPage.do")
    public List<AeaParFrontItemform> listAeaParFrontItemformByNoPage(AeaParFrontItemform aeaParFrontItemform) throws Exception {

        List<AeaParFrontItemform> list = aeaParFrontItemformService.listAeaParFrontItemformVoByNoPage(aeaParFrontItemform);
        return list;
    }

    @RequestMapping("/changIsActive.do")
    public ResultForm changIsActive(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParFrontItemformService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
