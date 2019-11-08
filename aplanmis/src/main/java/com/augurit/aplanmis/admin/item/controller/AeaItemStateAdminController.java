package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaMindUi;
import com.augurit.aplanmis.common.service.admin.item.impl.AeaItemStateAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 事项情形表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/state")
public class AeaItemStateAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemStateAdminController.class);

    @Autowired
    private AeaItemStateAdminService aeaItemStateAdminService;


    @RequestMapping("/indexAeaItemState.do")
    public ModelAndView indexAeaItemState() {
        return new ModelAndView("aea/item/state_index");
    }

    @RequestMapping("/listAeaItemState.do")
    public EasyuiPageInfo<AeaItemState> listAeaItemState(AeaItemState aeaItemState, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemState);
        PageInfo<AeaItemState> pageInfo = aeaItemStateAdminService.listAeaItemState(aeaItemState, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaItemState.do")
    public AeaItemState getAeaItemState(String id) {
        if (id != null) {
            logger.debug("根据ID获取AeaItemState对象，ID为：{}", id);
            return aeaItemStateAdminService.getAeaItemStateById(id);
        } else {
            logger.debug("构建新的AeaItemState对象");
            return new AeaItemState();
        }
    }

    @RequestMapping("/updateAeaItemState.do")
    public ResultForm updateAeaItemState(AeaItemState aeaItemState) {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemState);
        aeaItemStateAdminService.updateAeaItemState(aeaItemState);
        return new ResultForm(true);
    }

    @RequestMapping("/deleteAeaItemStateById.do")
    public ResultForm deleteAeaItemStateById(String id) {
        logger.debug("删除事项情形表Form对象，对象id为：{}", id);
        if (StringUtils.isNotBlank(id)) {
            aeaItemStateAdminService.deleteAeaItemStateById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchDeleteAeaItemState.do")
    public ResultForm batchDeleteAeaItemState(String[] ids) {
        if (ids != null && ids.length > 0) {
            aeaItemStateAdminService.batchDeleteAeaItemState(ids);
            return new ResultForm(true);
        }
        return new ResultForm(false, "参数ids为空!");
    }

    @RequestMapping("/getMaxSortNo.do")
    public Long getMaxSortNo() {
        return aeaItemStateAdminService.getMaxSortNo();
    }

    @RequestMapping("/changIsActiveState.do")
    public ResultForm changIsActiveState(String id) {
        if (StringUtils.isBlank(id)) {
            return new ResultForm(false, "参数id为空!");
        }
        aeaItemStateAdminService.changIsActiveState(id);
        return new ResultForm(true);
    }

    @RequestMapping("/getAeaItemStateByItemId.do")
    public MindBaseNode getAeaItemStatetreeByItemId(String itemId, AeaMindUi aeaMindUi) {

        return aeaItemStateAdminService.listAeaItemStateTreeByItemVerId(itemId, "", SecurityContext.getCurrentOrgId(), aeaMindUi);
    }

    /**
     * 获取作为流程启动条件的情形集合
     * @param stateVerId 事项ID
     * @return
     * @throws Exception
     */
    @RequestMapping("/getProcStartCondItemStates.do")
    public List<AeaItemState> getProcStartCondItemStates(String stateVerId) {
        AeaItemState itemState = new AeaItemState();
        itemState.setStateVerId(stateVerId);
        itemState.setIsProcStartCond("1");
        return aeaItemStateAdminService.listAeaItemState(itemState);
    }
}
