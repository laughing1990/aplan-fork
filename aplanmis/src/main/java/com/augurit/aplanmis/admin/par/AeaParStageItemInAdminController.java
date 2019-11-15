package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageItemIn;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemInAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * 阶段事项与输入关联定义表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/stage/item/in")
public class AeaParStageItemInAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageItemInAdminController.class);

    @Autowired
    private AeaParStageItemInAdminService aeaParStageItemInAdminService;

    @RequestMapping("/indexAeaParStageItemIn.do")
    public ModelAndView indexAeaParStageItemIn() {
        return new ModelAndView("aea/par/stage/item/stage_item_in_index");
    }

    @RequestMapping("/listAeaParStageItemIn.do")
    public PageInfo<AeaParStageItemIn> listAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn, Page page) {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStageItemIn);
        return aeaParStageItemInAdminService.listAeaParStageItemIn(aeaParStageItemIn, page);
    }

    @RequestMapping("/getAeaParStageItemIn.do")
    public AeaParStageItemIn getAeaParStageItemIn(String id) {
        if (id != null) {
            logger.debug("根据ID获取AeaParStageItemIn对象，ID为：{}", id);
            return aeaParStageItemInAdminService.getAeaParStageItemInById(id);
        } else {
            logger.debug("构建新的AeaParStageItemIn对象");
            return new AeaParStageItemIn();
        }
    }

    @RequestMapping("/updateAeaParStageItemIn.do")
    public ResultForm updateAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn) {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParStageItemIn);
        aeaParStageItemInAdminService.updateAeaParStageItemIn(aeaParStageItemIn);
        return new ResultForm(true);
    }

    /**
     * 保存或编辑阶段事项与输入关联定义表
     *
     * @param aeaParStageItemIn 阶段事项与输入关联定义表
     * @param result            校验对象
     * @return 返回结果对象 包含结果信息
     */
    @RequestMapping("/saveAeaParStageItemIn.do")
    public ResultForm saveAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("保存阶段事项与输入关联定义表Form对象出错");
            throw new InvalidParameterException(aeaParStageItemIn);
        }

        if (aeaParStageItemIn.getItemInId() != null && !"".equals(aeaParStageItemIn.getItemInId())) {
            aeaParStageItemInAdminService.updateAeaParStageItemIn(aeaParStageItemIn);
        } else {
            if (aeaParStageItemIn.getItemInId() == null || "".equals(aeaParStageItemIn.getItemInId())) {
                aeaParStageItemIn.setItemInId(UUID.randomUUID().toString());
            }
            aeaParStageItemInAdminService.saveAeaParStageItemIn(aeaParStageItemIn);
        }

        return new ContentResultForm<>(true, aeaParStageItemIn);
    }

    @RequestMapping("/deleteAeaParStageItemInById.do")
    public ResultForm deleteAeaParStageItemInById(String id) {
        logger.debug("删除阶段事项与输入关联定义表Form对象，对象id为：{}", id);
        if (id != null) {
            aeaParStageItemInAdminService.deleteAeaParStageItemInById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchSaveStageItemIn.do")
    public ResultForm batchSaveStageItemIn(String inId, String[] stageItemIds) {
        if (StringUtils.isNotBlank(inId)) {
            aeaParStageItemInAdminService.batchSaveStageItemIn(inId, stageItemIds);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchSaveStageItemInByStageItemId.do")
    public ResultForm batchSaveStageItemInByStageItemId(String stageItemId, String[] inIds) {

        if (StringUtils.isNotBlank(stageItemId)) {
            aeaParStageItemInAdminService.batchSaveStageItemInByStageItemId(stageItemId, inIds);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/listAeaParStageItemInByMatOrCertId.do")
    public List<AeaParStageItemIn> listAeaParStageItemInByMatOrCertId(String matOrCertId, String stageId, String parStateId, String fileType, String isStateIn) {

        return aeaParStageItemInAdminService.listAeaParStageItemInByMatOrCertId(matOrCertId, stageId, parStateId, fileType, isStateIn);
    }
}
