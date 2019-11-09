package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontStageService;
import com.augurit.aplanmis.common.vo.AeaParFrontStageVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 阶段的前置阶段检测表-Controller 页面控制转发类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:41</li>
 * </ul>
 */
@RestController
@RequestMapping("/aea/par/front/stage")
public class AeaParFrontStageController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontStageController.class);

    @Autowired
    private AeaParFrontStageService aeaParFrontStageService;

    @RequestMapping("/listAeaParFrontStageByPage.do")
    public EasyuiPageInfo<AeaParFrontStageVo> listAeaParFrontStageByPage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception {
        PageInfo<AeaParFrontStageVo> pageInfo = aeaParFrontStageService.listAeaParFrontStageVoByPage(aeaParFrontStage, page);
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
            if (aeaParFrontStage.getFrontStageId() != null && !"".equals(aeaParFrontStage.getFrontStageId())) {
                aeaParFrontStageService.updateAeaParFrontStage(aeaParFrontStage);
            } else {
                if (aeaParFrontStage.getFrontStageId() == null || "".equals(aeaParFrontStage.getFrontStageId()))
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
            if (StringUtils.isNotBlank(id))
                aeaParFrontStageService.deleteAeaParFrontStageById(id);
            return new ResultForm(true);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaParFrontStage aeaParFrontStage) {
        try {
            return new ContentResultForm<>(true, aeaParFrontStageService.getMaxSortNo(aeaParFrontStage));
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

}
