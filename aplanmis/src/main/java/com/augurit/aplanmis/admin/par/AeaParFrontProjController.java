package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontProj;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontProjService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 阶段的项目信息前置检测-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/front/proj")
@Slf4j
public class AeaParFrontProjController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontProjController.class);

    @Autowired
    private AeaParFrontProjService aeaParFrontProjService;

    @RequestMapping("/listAeaParFrontProjByPage.do")
    public EasyuiPageInfo<AeaParFrontProj> listAeaParFrontProjByPage(AeaParFrontProj aeaParFrontProj, Page page) throws Exception {
        PageInfo<AeaParFrontProj> pageInfo = aeaParFrontProjService.listAeaParFrontProj(aeaParFrontProj, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaParFrontProj.do")
    public ResultForm getAeaParFrontProj(String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                logger.debug("根据ID获取AeaParFrontProj对象，ID为：{}", id);
                return new ContentResultForm<>(true, aeaParFrontProjService.getAeaParFrontProjById(id));
            } else {
                return new ResultForm(false, "id不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }

    }

    @RequestMapping("/saveOrUpdateAeaParFrontProj.do")
    public ResultForm saveOrUpdateAeaParFrontProj(AeaParFrontProj aeaParFrontProj) {
        try {
            if (aeaParFrontProj.getFrontProjId() != null && !"".equals(aeaParFrontProj.getFrontProjId())) {
                aeaParFrontProjService.updateAeaParFrontProj(aeaParFrontProj);
            } else {
                if (aeaParFrontProj.getFrontProjId() == null || "".equals(aeaParFrontProj.getFrontProjId()))
                    aeaParFrontProj.setFrontProjId(UUID.randomUUID().toString());
                aeaParFrontProjService.saveAeaParFrontProj(aeaParFrontProj);
            }

            return new ContentResultForm<>(true, aeaParFrontProj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/deleteAeaParFrontProjById.do")
    public ResultForm deleteAeaParFrontProjById(String id) {

        try {
            logger.debug("删除阶段的项目信息前置检测Form对象，对象id为：{}", id);
            if (StringUtils.isNotBlank(id)) {
                aeaParFrontProjService.deleteAeaParFrontProjById(id);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaParFrontProj aeaParFrontProj) {
        try {
            return new ContentResultForm<>(true,aeaParFrontProjService.getMaxSortNo(aeaParFrontProj));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/updateAeaParFrontProjSortNos.do")
    public ResultForm updateAeaParFrontProjSortNos(String[] ids, Long[] sortNos) throws Exception {

        if (ids != null && ids.length > 0 && sortNos != null && sortNos.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                AeaParFrontProj aeaParFrontProj = new AeaParFrontProj();
                aeaParFrontProj.setFrontProjId(ids[i]);
                aeaParFrontProj.setSortNo(sortNos[i]);
                aeaParFrontProjService.updateAeaParFrontProj(aeaParFrontProj);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递排序数据有问题,请检查!");
    }


    @RequestMapping("/listAeaParFrontProjByNoPage.do")
    public List<AeaParFrontProj> listAeaParFrontProjByNoPage(AeaParFrontProj aeaParFrontProj) throws Exception {

        List<AeaParFrontProj> list = aeaParFrontProjService.listAeaParFrontProj(aeaParFrontProj);
        return list;
    }

    @RequestMapping("/changIsActive.do")
    public ResultForm changIsActive(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParFrontProjService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
