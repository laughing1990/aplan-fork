package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemformService;
import com.augurit.aplanmis.common.vo.AeaParFrontItemformVo;
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
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:12</li>
 * </ul>
 */
@RestController
@RequestMapping("/aea/par/front/itemform")
public class AeaParFrontItemformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemformController.class);

    @Autowired
    private AeaParFrontItemformService aeaParFrontItemformService;

    @RequestMapping("/listAeaParFrontItemformByPage.do")
    public EasyuiPageInfo<AeaParFrontItemformVo> listAeaParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {
        PageInfo<AeaParFrontItemformVo> pageInfo = aeaParFrontItemformService.listAeaParFrontItemformVoByPage(aeaParFrontItemform, page);
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
            if (aeaParFrontItemform.getFrontItemformId() != null && !"".equals(aeaParFrontItemform.getFrontItemformId())) {
                aeaParFrontItemformService.updateAeaParFrontItemform(aeaParFrontItemform);
            } else {
                if (aeaParFrontItemform.getFrontItemformId() == null || "".equals(aeaParFrontItemform.getFrontItemformId()))
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
            if (StringUtils.isNotBlank(id))
                aeaParFrontItemformService.deleteAeaParFrontItemformById(id);
            return new ResultForm(true);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaParFrontItemform aeaParFrontItemform) {
        try {
            return new ContentResultForm<>(true, aeaParFrontItemformService.getMaxSortNo(aeaParFrontItemform));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listSelectParFrontItemformByPage.do")
    public EasyuiPageInfo<AeaParFrontItemformVo> listSelectParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {
        PageInfo<AeaParFrontItemformVo> pageInfo = aeaParFrontItemformService.listSelectParFrontItemformByPage(aeaParFrontItemform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }


    @RequestMapping("/batchSaveAeaParFrontItemform.do")
    public ResultForm batchSaveAeaParFrontItemform(String stageId,String stageItemIds){
        try {
            aeaParFrontItemformService.batchSaveAeaParFrontItemform(stageId,stageItemIds);
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
    public List<AeaParFrontItemformVo> listAeaParFrontItemformByNoPage(AeaParFrontItemform aeaParFrontItemform) throws Exception {
        List<AeaParFrontItemformVo> list = aeaParFrontItemformService.listAeaParFrontItemformVoByNoPage(aeaParFrontItemform);
        return list;
    }


}
