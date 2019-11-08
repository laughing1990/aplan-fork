package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.domain.AeaParStateForm;
import com.augurit.aplanmis.common.service.admin.par.AeaParInAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateFormAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 阶段输入定义表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/in")
public class AeaParInAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParInAdminController.class);

    @Autowired
    private AeaParInAdminService aeaParInAdminService;

    @Autowired
    private AeaParStateAdminService aeaParStateAdminService;

    @Autowired
    private AeaParStateFormAdminService aeaParStateFormAdminService;

    @RequestMapping("/indexAeaParIn.do")
    public ModelAndView indexAeaParIn() {
        return new ModelAndView("aea/par/in_index");
    }

    @RequestMapping("/listAeaParIn.do")
    public PageInfo<AeaParIn> listAeaParIn(AeaParIn aeaParIn, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParIn);
        return aeaParInAdminService.listAeaParIn(aeaParIn, page);
    }

    @RequestMapping("/listAeaParInNoPage.do")
    public List<AeaParIn> listAeaParInNoPage(AeaParIn aeaParIn) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParIn);
        return aeaParInAdminService.listAeaParIn(aeaParIn);
    }

    @RequestMapping("/getAeaParIn.do")
    public AeaParIn getAeaParIn(String id) {
        if (id != null) {
            logger.debug("根据ID获取AeaParIn对象，ID为：{}", id);
            return aeaParInAdminService.getAeaParInById(id);
        } else {
            logger.debug("构建新的AeaParIn对象");
            return new AeaParIn();
        }
    }

    @RequestMapping("/updateAeaParIn.do")
    public ResultForm updateAeaParIn(AeaParIn aeaParIn) {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParIn);
        aeaParInAdminService.updateAeaParIn(aeaParIn);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑阶段输入定义表
     *
     * @param aeaParIn 阶段输入定义表
     * @return 返回结果对象 包含结果信息
     */
    @RequestMapping("/saveAeaParIn.do")
    public ResultForm saveAeaParIn(AeaParIn aeaParIn) {

        if (aeaParIn.getInId() != null && !"".equals(aeaParIn.getInId())) {
            aeaParInAdminService.updateAeaParIn(aeaParIn);
        } else {
            if (aeaParIn.getInId() == null || "".equals(aeaParIn.getInId())) {
                aeaParIn.setInId(UUID.randomUUID().toString());
            }
            aeaParInAdminService.saveAeaParIn(aeaParIn);
        }
        return new ContentResultForm<>(true, aeaParIn);
    }

    @RequestMapping("/deleteAeaParInById.do")
    public ResultForm deleteAeaParInById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除阶段输入定义表Form对象，对象id为：{}", id);
            aeaParInAdminService.deleteAeaParInById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false,"传递参数id为空!");
    }

    /**
     * 批量删除材料、证照关联关系
     *
     * @param ids
     * @return
     */
    @RequestMapping("/batchDeleteAeaParInByIds.do")
    public ResultForm batchDeleteAeaParInByIds(String[] ids) {

        if (ids!=null&&ids.length>0) {
            logger.debug("删除阶段输入定义表Form对象，对象id为：{}", ids);
            aeaParInAdminService.batchDeleteAeaParInByIds(ids);
            return new ResultForm(true);
        }
        return new ResultForm(false,"传递参数ids为空!");
    }

    /**
     * 批量删除材料、证照、表单关联关系
     *
     * @param ids
     * @param fileTypes
     * @return
     */
    @RequestMapping("/batchDelMatCertFormByInIdsAndTypes.do")
    public ResultForm batchDelMatCertFormByInIdsAndTypes(String[] ids, String[] fileTypes) {

        if (ids!=null&&ids.length>0&&fileTypes!=null&&fileTypes.length>0) {
            logger.debug("删除阶段输入定义表Form对象，对象id为：{}", ids);
            aeaParInAdminService.batchDelMatCertFormByInIdsAndTypes(ids, fileTypes);
            return new ResultForm(true);
        }
        return new ResultForm(false,"传递参数ids为空!");
    }

    // 单个保存阶段无情形材料
    @RequestMapping("/saveStageNoStateMatIn.do")
    public ResultForm saveStageNoStateMatIn(HttpServletRequest request, String stageId,
                                            String inId, AeaItemMat mat) throws Exception {
        if (StringUtils.isNotBlank(stageId)) {
            AeaParIn aeaParIn = aeaParInAdminService.saveStageNoStateMatIn(request, stageId, inId, mat);
            return new ContentResultForm<>(true, aeaParIn);
        }
        return new ResultForm(false, "传递参数异常！");
    }

    // 单个保存阶段情形材料
    @RequestMapping("/saveStageStateMatIn.do")
    public ResultForm saveStageStateMatIn(HttpServletRequest request, String stageId,
                                          String stageStateId, String inId, AeaItemMat mat) throws Exception {
        if (StringUtils.isNotBlank(stageId) && StringUtils.isNotBlank(stageStateId)) {
            AeaParIn aeaParIn = aeaParInAdminService.saveStageStateMatIn(request, stageId, stageStateId, inId, mat);
            return new ContentResultForm<>(true, aeaParIn);
        }
        return new ResultForm(false, "传递参数异常！");
    }

    // 批量保存阶段材料
    @RequestMapping("/batchSaveStageNoStateMatIn.do")
    public ResultForm batchSaveStageNoStateMatIn(String stageId, String[] matIds) {

        if (StringUtils.isNotBlank(stageId) && matIds != null && matIds.length > 0) {
            aeaParInAdminService.batchSaveStageNoStateMatIn(stageId, matIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递参数异常！");
    }

    // 批量保存情形材料
    @RequestMapping("/batchSaveStageStateMatIn.do")
    public ResultForm batchSaveStageStateMatIn(String stageId, String stageStateId, String[] matIds) {
        if (StringUtils.isNotBlank(stageId) && StringUtils.isNotBlank(stageStateId) && matIds != null && matIds.length > 0) {
            aeaParInAdminService.batchSaveStageStateMatIn(stageId, stageStateId, matIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递参数异常！");
    }

    // 批量保存阶段电子证照
    @RequestMapping("/batchSaveStageNoStateCertIn.do")
    public ResultForm batchSaveStageCertIn(String stageId, String[] certIds) {

        if (StringUtils.isNotBlank(stageId)) {
            aeaParInAdminService.batchSaveStageNoStateCertIn(stageId, certIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递参数stageId为空！");
    }

    @RequestMapping("/batchSaveStageNoStateCertInNotDelOld.do")
    public ResultForm batchSaveStageNoStateCertInNotDelOld(String stageId, String[] certIds) {

        if (StringUtils.isNotBlank(stageId)) {
            aeaParInAdminService.batchSaveStageNoStateCertInNotDelOld(stageId, certIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递参数stageId为空！");
    }

    // 批量保存情形电子证照
    @RequestMapping("/batchSaveStageStateCertIn.do")
    public ResultForm batchSaveStageStateCertIn(String stageId, String stageStateId, String[] certIds) {

        if (StringUtils.isNotBlank(stageId) && StringUtils.isNotBlank(stageStateId)) {
            aeaParInAdminService.batchSaveStageStateCertIn(stageId, stageStateId, certIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递参数异常！");
    }

    /**
     * 查询列表
     *
     * @param stageId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/listItemParIn.do")
    public EasyuiPageInfo<AeaParIn> listItemParIn(String stageId, String keyword, int pageNum, int pageSize) {

        Page page = new Page();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        PageInfo<AeaParIn> pageInfo = aeaParInAdminService.listParInAndItemByStageId(stageId, keyword, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 获取阶段所有材料与证照
     *
     * @param aeaParIn
     * @param page
     * @return
     */
    @RequestMapping("/listStageInMatCert.do")
    public EasyuiPageInfo<AeaParIn> listStageInMatCert(AeaParIn aeaParIn, Page page) {

        PageInfo<AeaParIn> pageInfo = aeaParInAdminService.listStageInMatCert(aeaParIn, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 获取阶段下所有材料证照表单
     *
     * @param aeaParIn
     * @param page
     * @return
     */
    @RequestMapping("/listStageInMatCertForm.do")
    public EasyuiPageInfo<AeaParIn> listStageInMatCertForm(AeaParIn aeaParIn, Page page) {

        PageInfo<AeaParIn> pageInfo = aeaParInAdminService.listStageInMatCertForm(aeaParIn, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listStageInMatCertFormNoPage.do")
    public List<AeaParIn> listStageInMatCertFormNoPage(AeaParIn aeaParIn) {

        List<AeaParIn> list = aeaParInAdminService.listStageInMatCertForm(aeaParIn);
        return list;
    }

    @RequestMapping("/listParInByStageItemId.do")
    public List<AeaParIn> listParInByStageItemId(String stageId, String stageItemId, String keyword) {

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParInAdminService.listParInByStageItemId(stageId, stageItemId, keyword);
        }
        return null;
    }

    @RequestMapping("/addGlobalMatParIn.do")
    public ResultForm addGlobalMatParIn(String[] matIds, String stageId, String isStateIn, String parStateId) {
        try {
            if (isStateIn == null || "".equals(isStateIn) || "0".equals(isStateIn)) {
                parStateId = "";
            }
            aeaParInAdminService.addGlobalMatParIn(matIds, stageId, isStateIn, parStateId);
        } catch (Exception e) {
            return new ResultForm(false);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/addGlobalCertParIn.do")
    public ResultForm addGlobalCertParIn(String[] certIds, String stageId, String isStateIn, String parStateId) {
        try {
            if (isStateIn == null || "".equals(isStateIn) || "0".equals(isStateIn)) {
                parStateId = "";
            }
            aeaParInAdminService.addGlobalCertParIn(certIds, stageId, isStateIn, parStateId);
        } catch (Exception e) {
            return new ResultForm(false);
        }
        return new ResultForm(true);
    }


    @RequestMapping("/getCertListByStateId.do")
    public List<String> getCertListByStateId(String parStateId) {
        return aeaParInAdminService.getCertListByStateId(parStateId);
    }

    @RequestMapping("/listNoStateCertByStageId.do")
    public List<String> listNoStateCertByStageId(AeaParIn in) {

        List<String> list = new ArrayList<>();
        List<AeaParIn> ins =  aeaParInAdminService.listAeaParIn(in);
        if(ins!=null&& ins.size()>0){
            for(AeaParIn vo:ins){
                if(StringUtils.isNotBlank(vo.getCertId())){
                    list.add(vo.getCertId());
                }
            }
        }
        return list;
    }

    // 获取阶段下所有电子证照id
    @RequestMapping("/listCertListByStageId.do")
    public List<String> listCertListByStageId(String stageId) {
        return aeaParInAdminService.listCertListByStageId(stageId);
    }

    @RequestMapping("/getGlobalMatListByStateId.do")
    public List<String> getGlobalMatListByStateId(String parStateId) {
        return aeaParInAdminService.getGlobalMatListByStateId(parStateId);
    }

    //刪除材料和关联事项的关系
    @RequestMapping("/deleteAeaParIn.do")
    public ResultForm deleteAeaParIn(String inId) {
        if (inId != null) {
            aeaParInAdminService.deleteAeaParIn(inId);
        }
        return new ResultForm(true);
    }


    //查询列表
    @RequestMapping("/listStageMatByStageId.do")
    public EasyuiPageInfo<AeaParIn> listStageMatByStageId(String stageId, String stateId, Boolean isCommon, String isStateIn, String keyword, int pageNum, int pageSize) {

        Page page = new Page();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        PageInfo<AeaParIn> pageInfo = aeaParInAdminService.listStageMatByStageId(stageId, stateId, keyword, isCommon, isStateIn, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/updateStageStateParIn.do")
    public ResultForm updateStageStateParIn(String stageId, String stateId, String inIds) throws Exception {
        AeaParState aeaParState = aeaParStateAdminService.getAeaParStateById(stateId);
        if (aeaParState == null) {
            return new ResultForm(false, "请先保存情形");
        } else {
            aeaParInAdminService.updateStageStateParIn(stageId, stateId, inIds);
            return new ResultForm(true);
        }
    }

    @RequestMapping("/deleteMatAndParIn.do")
    public ResultForm deleteMatAndParIn(String inId) {
        aeaParInAdminService.deleteMatAndParIn(inId);
        return new ResultForm(true);
    }

    @RequestMapping("/batchDeleteMatAndParIns.do")
    public ResultForm batchDeleteMatAndParIns(String[] inIds) {

        aeaParInAdminService.batchDeleteMatAndParIns(inIds);
        return new ResultForm(true);
    }

    @RequestMapping("/getGlobalMatListByStageIdAndParStateId.do")
    public List<String> getGlobalMatListByStageIdAndParStateId(String stageId, String parStateId) {
        return aeaParInAdminService.getGlobalMatListByStageIdAndParStateId(stageId, parStateId);
    }

    @RequestMapping("/listStoMatByCondition.do")
    public List<AeaParIn> listStoMatByCondition(AeaParIn aeaParIn) {

        return aeaParInAdminService.listStoMatByCondition(aeaParIn);
    }

    @RequestMapping("/updateStageInSortNos.do")
    public ResultForm updateStageInSortNos(String[] inIds, String[] fileTypes, Long[] sortNos) {

        if(inIds!=null&&inIds.length>0
            &&fileTypes!=null&&fileTypes.length>0
            &&sortNos!=null&&sortNos.length>0){
            for(int i=0; i<inIds.length; i++){
                if(fileTypes[i].equals("form")){
                    AeaParStateForm stateForm = new AeaParStateForm();
                    stateForm.setStateFormId(inIds[i]);
                    stateForm.setSortNo(sortNos[i]);
                    aeaParStateFormAdminService.updateAeaParStateForm(stateForm);
                }else{
                    AeaParIn in = new AeaParIn();
                    in.setInId(inIds[i]);
                    in.setSortNo(sortNos[i]);
                    aeaParInAdminService.updateAeaParIn(in);
                }
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递材料证照表单排序数据有问题,请检查!");
    }
}
