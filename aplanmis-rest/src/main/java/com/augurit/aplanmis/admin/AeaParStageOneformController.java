package com.augurit.aplanmis.admin;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStageOneformService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import com.augurit.aplanmis.common.vo.ActStoFormVo;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 事项子表与智能表单关联控制类
 */
@RestController
@RequestMapping("/aea/par/stage/stageOneform")
public class AeaParStageOneformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageOneformController.class);

    @Autowired
    private AeaParStageOneformService aeaParStageOneformService;
    @Autowired
    private AeaParStageItemAdminService AeaParStageItemAdminService;

    /**
     * 获取一张表单事项子表列表
     *
     * @param parStageId
     * @param page
     * @return
     */
    @RequestMapping("/listItemForm.do")
    public EasyuiPageInfo<AeaParStageItem> listItemForm(String parStageId, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", parStageId);
        return aeaParStageOneformService.getItemFormlist(parStageId, page);
    }



    /**
     * 获取智能表单列表
     *
     * @param actStoForm
     * @param page
     * @return
     */
    @RequestMapping("/listStoForm.do")
    public EasyuiPageInfo<ActStoFormVo> listStoForm(ActStoFormVo actStoForm, Page page) {

        logger.debug("分页查询，过滤条件为{}，对象为{}", actStoForm);
        return aeaParStageOneformService.getActStoFormList(actStoForm, page);
    }


    /**
     * 导入智能表单
     *
     * @param aeaParStageItem
     * @return
     */
    @RequestMapping("/saveParStageItem.do")
    public ResultForm saveParStageItem(AeaParStageItem aeaParStageItem, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存智能表单关联出错");
            throw new InvalidParameterException(aeaParStageItem);
        }

        try{
            if (aeaParStageItem.getStageItemId() != null && !"".equals(aeaParStageItem.getStageItemId())) {

                    AeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
                    return new ContentResultForm<>(true, aeaParStageItem);

            }
        }catch(Exception e){
            return new ContentResultForm<>(false, "", "导入智能表单失败！");
        }

        return new ContentResultForm<>(false, "", "导入智能表单失败！");

    }

    @RequestMapping("/getAeaParStageItemListNoPage.do")
    public List<AeaParStageItem> getAeaParStageItemListNoPage(AeaParStageItem aeaParStageItem) {

        List<AeaParStageItem> list = aeaParStageOneformService.getAeaParStageItemListNoPage(aeaParStageItem);
        return list;
    }


    /**
     * 事项子表排序
     *
     * @param stageItemIds,sortNos
     * @return
     */
    @RequestMapping("/updateStageItemSortNos.do")
    public ResultForm updateStageItemSortNos(String[] stageItemIds, Long[] sortNos) {

        if(stageItemIds!=null&&stageItemIds.length>0
                &&sortNos!=null&&sortNos.length>0){
            for(int i=0; i<stageItemIds.length; i++){

                AeaParStageItem aeaParStageItem = new AeaParStageItem();
                aeaParStageItem.setStageItemId(stageItemIds[i]);
                aeaParStageItem.setSortNo(sortNos[i]);
                AeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);

            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "事项子表表单排序数据有问题,请检查!");
    }


    /**
     * 删除事项子表与智能表单关联关系
     *
     * @param aeaParStageItem
     * @return
     */
    @RequestMapping("/deleteStoformByStageItemId.do")
    public ResultForm deleteStoformByStageItemId(AeaParStageItem aeaParStageItem) throws Exception {

        if (StringUtils.isBlank(aeaParStageItem.getStageItemId())) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除事项子表与智能表单关联关系，对象id为：{}", aeaParStageItem.getStageItemId());
        aeaParStageItem.setSubFormId("");
        AeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
        return new ResultForm(true);
    }

}
