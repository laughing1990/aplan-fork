package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontPartformService;
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
 * 阶段的扩展表单前置检测表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/front/partform")
public class AeaParFrontPartformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontPartformController.class);

    @Autowired
    private AeaParFrontPartformService aeaParFrontPartformService;

    @RequestMapping("/listAeaParFrontPartformByPage.do")
    public EasyuiPageInfo<AeaParFrontPartform> listAeaParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {

        PageInfo<AeaParFrontPartform> pageInfo =  aeaParFrontPartformService.listAeaParFrontPartformVoByPage(aeaParFrontPartform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaParFrontPartform.do")
    public ResultForm getAeaParFrontPartform(String frontPartformId){
        try {
            if (StringUtils.isNotBlank(frontPartformId)) {
                return new ContentResultForm<>(true, aeaParFrontPartformService.getAeaParFrontPartformVoById(frontPartformId));
            } else {
                return new ResultForm(false, "frontPartformId不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/saveOrUpdateAeaParFrontPartform.do")
    public ResultForm saveOrUpdateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform){

        try {
            if (aeaParFrontPartform.getFrontPartformId() != null && !"".equals(aeaParFrontPartform.getFrontPartformId())) {
                aeaParFrontPartformService.updateAeaParFrontPartform(aeaParFrontPartform);
            } else {
                if (aeaParFrontPartform.getFrontPartformId() == null || "".equals(aeaParFrontPartform.getFrontPartformId()))
                    aeaParFrontPartform.setFrontPartformId(UUID.randomUUID().toString());
                aeaParFrontPartformService.saveAeaParFrontPartform(aeaParFrontPartform);
            }

            return new ContentResultForm<>(true, aeaParFrontPartform);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/deleteAeaParFrontPartformById.do")
    public ResultForm deleteAeaParFrontPartformById(String id)  {

        try {
            logger.debug("删除阶段的扩展表单前置检测表Form对象，对象id为：{}", id);
            if (StringUtils.isNotBlank(id))
                aeaParFrontPartformService.deleteAeaParFrontPartformById(id);
            return new ResultForm(true);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(String stageId) {

        try {
            return new ContentResultForm<>(true, aeaParFrontPartformService.getMaxSortNo(stageId, SecurityContext.getCurrentOrgId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listSelectParFrontPartformByPage.do")
    public EasyuiPageInfo<AeaParFrontPartform> listSelectParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {

        PageInfo<AeaParFrontPartform> pageInfo = aeaParFrontPartformService.listSelectParFrontPartformByPage(aeaParFrontPartform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/batchSaveAeaParFrontPartform.do")
    public ResultForm batchSaveAeaParFrontPartform(String stageId, String[] stagePartformIds){

        try {
            aeaParFrontPartformService.batchSaveAeaParFrontPartform(stageId, stagePartformIds);
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/updateAeaParFrontPartformSortNos.do")
    public ResultForm updateAeaParFrontPartformSortNos(String[] ids, Long[] sortNos) throws Exception {

        if (ids != null && ids.length > 0 && sortNos != null && sortNos.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                AeaParFrontPartform aeaParFrontPartform = new AeaParFrontPartform();
                aeaParFrontPartform.setFrontPartformId(ids[i]);
                aeaParFrontPartform.setSortNo(sortNos[i]);
                aeaParFrontPartformService.updateAeaParFrontPartform(aeaParFrontPartform);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递排序数据有问题,请检查!");
    }

    @RequestMapping("/listAeaParFrontPartformByNoPage.do")
    public List<AeaParFrontPartform> listAeaParFrontItemformByNoPage(AeaParFrontPartform aeaParFrontPartform) throws Exception {

        List<AeaParFrontPartform> list = aeaParFrontPartformService.listAeaParFrontPartformVoByNoPage(aeaParFrontPartform);
        return list;
    }

    @RequestMapping("/changIsActive.do")
    public ResultForm changIsActive(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParFrontPartformService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
