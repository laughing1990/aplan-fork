package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemService;
import com.augurit.aplanmis.common.vo.AeaParFrontItemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 阶段事项前置检测表-Controller 页面控制转发类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:47:49</li>
 * </ul>
 */
@RestController
@RequestMapping("/aea/par/front/item")
public class AeaParFrontItemController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemController.class);

    @Autowired
    private AeaParFrontItemService aeaParFrontItemService;

    @RequestMapping("/listAeaParFrontItemByPage.do")
    public EasyuiPageInfo<AeaParFrontItemVo> listAeaParFrontItemByPage(AeaParFrontItem aeaParFrontItem, Page page) throws Exception {
        PageInfo<AeaParFrontItemVo> pageInfo = aeaParFrontItemService.listAeaParFrontItemVoByPage(aeaParFrontItem, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaParFrontItem.do")
    public ResultForm getAeaParFrontItem(String frontItemId) {
        try {
            if (StringUtils.isNotBlank(frontItemId)) {
                return new ContentResultForm<>(true, aeaParFrontItemService.getAeaParFrontItemVoByFrontItemId(frontItemId));
            } else {
                return new ResultForm(false, "frontItemId不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/saveOrUpdateAeaParFrontItem.do")
    public ResultForm saveOrUpdateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) {
        try {
            if (aeaParFrontItem.getFrontItemId() != null && !"".equals(aeaParFrontItem.getFrontItemId())) {
                aeaParFrontItemService.updateAeaParFrontItem(aeaParFrontItem);
            } else {
                if (aeaParFrontItem.getFrontItemId() == null || "".equals(aeaParFrontItem.getFrontItemId()))
                    aeaParFrontItem.setFrontItemId(UUID.randomUUID().toString());
                aeaParFrontItemService.saveAeaParFrontItem(aeaParFrontItem);
            }

            return new ContentResultForm<>(true, aeaParFrontItem);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/deleteAeaParFrontItemById.do")
    public ResultForm deleteAeaParFrontItemById(String id) {
        try {
            logger.debug("删除阶段事项前置检测表Form对象，对象id为：{}", id);
            if (StringUtils.isNotBlank(id)) {
                aeaParFrontItemService.deleteAeaParFrontItemById(id);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaParFrontItem aeaParFrontItem) {
        try {
            return new ContentResultForm<>(true, aeaParFrontItemService.getMaxSortNo(aeaParFrontItem));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

}
