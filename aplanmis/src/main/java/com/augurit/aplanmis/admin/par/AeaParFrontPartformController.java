package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontPartformService;
import com.augurit.aplanmis.common.vo.AeaParFrontPartformVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 阶段的扩展表单前置检测表-Controller 页面控制转发类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:23</li>
 * </ul>
 */
@RestController
@RequestMapping("/aea/par/front/partform")
public class AeaParFrontPartformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontPartformController.class);

    @Autowired
    private AeaParFrontPartformService aeaParFrontPartformService;

    @RequestMapping("/listAeaParFrontPartformByPage.do")
    public EasyuiPageInfo<AeaParFrontPartformVo> listAeaParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {
        PageInfo<AeaParFrontPartformVo> pageInfo =  aeaParFrontPartformService.listAeaParFrontPartformVoByPage(aeaParFrontPartform, page);
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
    public ResultForm getMaxSortNo(AeaParFrontPartform aeaParFrontPartform) {
        try {
            return new ContentResultForm<>(true, aeaParFrontPartformService.getMaxSortNo(aeaParFrontPartform));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listSelectParFrontPartformByPage.do")
    public EasyuiPageInfo<AeaParFrontPartformVo> listSelectParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {
        PageInfo<AeaParFrontPartformVo> pageInfo = aeaParFrontPartformService.listSelectParFrontPartformByPage(aeaParFrontPartform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

}
