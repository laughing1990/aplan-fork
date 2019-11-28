package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.qo.item.ItemMatInoutQo;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemStateFormAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 事项输入输出定义表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/inout")
public class AeaItemInoutAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemInoutAdminController.class);

    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;

    @Autowired
    private AeaItemStateFormAdminService aeaItemStateFormAdminService;

//    @RequestMapping("/indexAeaItemInout.do")
//    public ModelAndView indexAeaItemInout() {
//        return new ModelAndView("aea/item/inout_index");
//    }
//
//    @RequestMapping("/addGlobalMatAsInout.do")
//    public ResultForm addGlobalMatAsInout(String[] matIds, String itemId, String isInput, String isStateIn, String itemStateId) {
//        try {
//            if (isStateIn == null || "".equals(isStateIn) || "0".equals(isStateIn)) {
//                itemStateId = "";
//            }
//            aeaItemInoutAdminService.addGlobalMatAsInout(matIds, itemId, isInput, isStateIn, itemStateId);
//        } catch (Exception e) {
//            return new ResultForm(false);
//        }
//        return new ResultForm(true);
//    }
//
//    @RequestMapping("/addGlobalCertAsInout.do")
//    public ResultForm addGlobalCertAsInout(String[] certIds, String itemId, String isInput, String isStateIn, String itemStateId) {
//        try {
//            if (isStateIn == null || "".equals(isStateIn) || "0".equals(isStateIn)) {
//                itemStateId = "";
//            }
//            aeaItemInoutAdminService.addGlobalCertAsInout(certIds, itemId, isInput, isStateIn, itemStateId);
//        } catch (Exception e) {
//            return new ResultForm(false);
//        }
//        return new ResultForm(true);
//    }
//
//    @RequestMapping("/listAeaItemInout.do")
//    public PageInfo<AeaItemInout> listAeaItemInout(AeaItemInout aeaItemInout, Page page) {
//
//        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemInout);
//        return aeaItemInoutAdminService.listAeaItemInout(aeaItemInout, page);
//    }

    @RequestMapping("/listAeaItemInoutNoPage.do")
    public List<AeaItemInout> listAeaItemInoutNoPage(AeaItemInout aeaItemInout){

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemInout);
        aeaItemInout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        return aeaItemInoutAdminService.listAeaItemInout(aeaItemInout);
    }

//    @RequestMapping("/listAeaItemInoutAndMat.do")
//    public EasyuiPageInfo<AeaItemInout> listAeaItemInoutAndMat(AeaItemInout aeaItemInout, Page page, @Valid BsTablePage query) {
//        Map queryMap = query.getQuery();
//        AeaItemMat mat = new AeaItemMat();
//        if (queryMap != null) {
//            if (queryMap.containsKey("keyword") && queryMap.get("keyword") != null) {
//                if (StringUtils.isNotEmpty(queryMap.get("keyword").toString())) {
//                    mat.setKeyword(queryMap.get("keyword").toString());
//                }
//            }
//        }
//        aeaItemInout.setAeaItemMat(mat);
//        PageInfo<AeaItemInout> pageList = aeaItemInoutAdminService.listAeaItemInoutAndMat(aeaItemInout, page);
//        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemInout);
//        return PageHelper.toEasyuiPageInfo(pageList);
//    }
//
//    @RequestMapping("/listAeaItemInoutAndMatNoPagination.do")
//    public List<AeaItemInout> listAeaItemInoutAndMat(AeaItemInout aeaItemInout) {
//        AeaItemMat mat = new AeaItemMat();
//        mat.setKeyword(aeaItemInout.getKeyword());
//        aeaItemInout.setAeaItemMat(mat);
//        return aeaItemInoutAdminService.listAeaItemInoutAndMatNoPagination(aeaItemInout);
//    }
//
//    /*
//     * 查询事项输入输出并且关联出材料或证件
//     */
//    @RequestMapping("/listAeaItemInoutCascade.do")
//    public List<AeaItemInout> listAeaItemInoutCascade(AeaItemInout aeaItemInout) {
//        logger.debug("过滤条件为{}，查询关键字为{}", aeaItemInout);
//        return aeaItemInoutAdminService.listAeaItemInoutCascade(aeaItemInout);
//    }
//
//    @RequestMapping("/listAeaItemInoutByStageIdCascade.do")
//    public List<AeaItemInout> listAeaItemInoutByStageIdCascade(String stageId, AeaItemInout aeaItemInout) {
//        logger.debug("过滤条件为{}，查询关键字为{}", aeaItemInout, stageId);
//        return aeaItemInoutAdminService.listAeaItemInoutByStageIdCascade(stageId, aeaItemInout);
//    }
//
//    @RequestMapping("/getGlobalMatListByStateId.do")
//    public List<String> getGlobalMatListByStateId(String itemVerId,String itemStateId) {
//        return aeaItemInoutAdminService.getGlobalMatListByStateId(itemVerId,itemStateId);
//    }

    @RequestMapping("/listItemOrStateCerts.do")
    public List<String> listItemOrStateCerts(String itemVerId, String stateVerId,
                                             String isStateIn, String itemStateId) {

        AeaItemInout inout = new AeaItemInout();
        inout.setItemVerId(itemVerId);
        inout.setStateVerId(stateVerId);
        inout.setIsStateIn(isStateIn);
        inout.setItemStateId(itemStateId);
        inout.setIsInput(Status.ON);
        inout.setFileType(MindType.CERTIFICATE.getValue());
        inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<String> certIds = new ArrayList<>();
        List<AeaItemInout> inouts = aeaItemInoutAdminService.listAeaItemInout(inout);
        if(inouts!=null&& inouts.size()>0){
           for(AeaItemInout in: inouts){
               if(StringUtils.isNotBlank(in.getCertId())){
                   certIds.add(in.getCertId());
               }
           }
        }
        return certIds;
    }

//    // 条件查询库材料
//    @RequestMapping("/listStoMatByCondition.do")
//    public List<AeaItemInout> listStoMatByCondition(AeaItemInout aeaItemInout) {
//
//        logger.debug("过滤条件为{}，查询关键字为{}", aeaItemInout);
//        return aeaItemInoutAdminService.listStoMatByCondition(aeaItemInout);
//    }
//
//    @RequestMapping("/gtreeAeaItemInoutCascade.do")
//    public List<ZtreeNode> gtreeAeaItemInoutCascade(AeaItemInout aeaItemInout) {
//        if (aeaItemInout != null && StringUtils.isNotBlank(aeaItemInout.getItemId())) {
//            return aeaItemInoutAdminService.gtreeAeaItemInoutCascade(aeaItemInout);
//        }
//        return null;
//    }
//
//
//    @RequestMapping("/getAeaItemInout.do")
//    public AeaItemInout getAeaItemInout(String id) {
//        if (StringUtils.isNotBlank(id)) {
//            logger.debug("根据ID获取AeaItemInout对象，ID为：{}", id);
//            return aeaItemInoutAdminService.getAeaItemInoutById(id);
//        } else {
//            logger.debug("构建新的AeaItemInout对象");
//            return new AeaItemInout();
//        }
//    }
//
//    @RequestMapping("/updateAeaItemInout.do")
//    public ResultForm updateAeaItemInout(AeaItemInout aeaItemInout) {
//        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemInout);
//        aeaItemInoutAdminService.updateAeaItemInout(aeaItemInout);
//        return new ResultForm(true);
//    }
//
//
//    /**
//     * 保存或编辑事项输入输出定义表
//     *
//     * @param aeaItemInout 事项输入输出定义表
//     * @return 返回结果对象 包含结果信息
//     */
//    @RequestMapping("/saveAeaItemInout.do")
//    public ResultForm saveAeaItemInout(AeaItemInout aeaItemInout) {
//        if (aeaItemInout.getInoutId() != null && !"".equals(aeaItemInout.getInoutId())) {
//            aeaItemInoutAdminService.updateAeaItemInout(aeaItemInout);
//        } else {
//            if (aeaItemInout.getInoutId() == null || "".equals(aeaItemInout.getInoutId())) {
//                aeaItemInout.setInoutId(UUID.randomUUID().toString());
//            }
//            aeaItemInoutAdminService.saveAeaItemInout(aeaItemInout);
//        }
//        return new ContentResultForm<>(true, aeaItemInout);
//    }
//
    @RequestMapping("/deleteAeaItemInoutById.do")
    public ResultForm deleteAeaItemInoutById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除事项输入输出定义表Form对象，对象id为：{}", id);
            aeaItemInoutAdminService.deleteAeaItemInoutById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false, "参数id为空!");
    }

    @RequestMapping("/deleteAeaItemInoutCascade.do")
    public ResultForm deleteAeaItemInoutCascade(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除事项输入输出定义表Form对象，对象id为：{}", id);
            aeaItemInoutAdminService.deleteAeaItemInoutCascade(id);
            return new ResultForm(true);
        }
        return new ResultForm(false);
    }

    @RequestMapping("/batchDeleteAeaItemInoutCascade.do")
    public ResultForm batchDeleteAeaItemInoutCascade(String[] ids) throws Exception {

        if (ids != null && ids.length > 0) {
            logger.debug("删除事项输入输出定义，对象id为：{}", (Object) ids);
            aeaItemInoutAdminService.batchDeleteAeaItemInoutCascade(ids);
            return new ResultForm(true);
        }
        return new ResultForm(false);
    }

    @RequestMapping("/batchDelItemMatCertFormCascade.do")
    public ResultForm batchDelItemMatCertFormCascade(String[] ids, String[] fileTypes) throws Exception {

        if (ids != null && ids.length > 0 && fileTypes!=null && fileTypes.length>0) {
            logger.debug("删除事项输入输出定义，对象id为：{}", ids);
            aeaItemInoutAdminService.batchDelItemMatCertFormCascade(ids, fileTypes);
            return new ResultForm(true);
        }
        return new ResultForm(false);
    }

    @RequestMapping("/listAeaItemInoutCascadeByPage.do")
    public EasyuiPageInfo<AeaItemInout> listAeaItemInoutCascadeByPage(AeaItemInout aeaItemInout, Page page) {

        logger.debug("过滤条件为{}，查询关键字为{}", aeaItemInout);
        PageInfo<AeaItemInout> pageInfo = aeaItemInoutAdminService.listAeaItemInoutMatCert(aeaItemInout, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaItemInoutCascadeByNoPage.do")
    public List<AeaItemInout> listAeaItemInoutCascadeByNoPage(AeaItemInout aeaItemInout) {

        logger.debug("过滤条件为{}，查询关键字为{}", aeaItemInout);
        List<AeaItemInout> list = aeaItemInoutAdminService.listAeaItemInoutMatCert(aeaItemInout);
        return list;
    }

//    @RequestMapping("/updateItemOutSortNo.do")
//    public ResultForm updateItemOutSortNo(AeaItemInout inout) {
//
//        aeaItemInoutAdminService.updateAeaItemInout(inout);
//        return new ResultForm(true);
//    }

    @RequestMapping("/updateItemOutSortNos.do")
    public ResultForm updateItemOutSortNo(String[] inoutIds, Long[] sortNos) {

        if(inoutIds!=null&&inoutIds.length>0&&sortNos!=null&&sortNos.length>0){
            for(int i=0;i<inoutIds.length;i++){
                AeaItemInout inout = new AeaItemInout();
                inout.setInoutId(inoutIds[i]);
                inout.setSortNo(sortNos[i]);
                aeaItemInoutAdminService.updateAeaItemInout(inout);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递材料证照排序数据有问题,请检查!");
    }

    @RequestMapping("/updateItemInSortNos.do")
    public ResultForm updateItemInSortNos(String[] inoutIds, String[] fileTypes, Long[] sortNos) {

        if(inoutIds!=null&&inoutIds.length>0
//            &&fileTypes!=null&&fileTypes.length>0
            &&sortNos!=null&&sortNos.length>0){
            for(int i=0; i<inoutIds.length; i++){
//                if(fileTypes[i].equals("form")){
//                    AeaItemStateForm stateForm = new AeaItemStateForm();
//                    stateForm.setItemStateFormId(inoutIds[i]);
//                    stateForm.setSortNo(sortNos[i]);
//                    aeaItemStateFormAdminService.updateAeaItemStateForm(stateForm);
//                }else{
                    AeaItemInout inout = new AeaItemInout();
                    inout.setInoutId(inoutIds[i]);
                    inout.setSortNo(sortNos[i]);
                    aeaItemInoutAdminService.updateAeaItemInout(inout);
//                }
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递材料证照表单排序数据有问题,请检查!");
    }

    @RequestMapping("/listAeaItemMatCertFormByPage.do")
    public EasyuiPageInfo<AeaItemInout> listAeaItemMatCertFormByPage(AeaItemInout aeaItemInout, Page page) {

        logger.debug("过滤条件为{}，查询关键字为{}", aeaItemInout);
        PageInfo<AeaItemInout> pageInfo = aeaItemInoutAdminService.listAeaItemMatCertFormByPage(aeaItemInout, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaItemMatCertFormByNoPage.do")
    public List<AeaItemInout> listAeaItemMatCertFormByNoPage(AeaItemInout aeaItemInout) {

        List<AeaItemInout> list = aeaItemInoutAdminService.listAeaItemInoutMatCertForm(aeaItemInout);
        return list;
    }

//    @RequestMapping("/listAeaItemInoutMatCert.do")
//    public List<AeaItemInout> listAeaItemInoutMatCert(AeaItemInout aeaItemInout) {
//        logger.debug("过滤条件为{}，查询关键字为{}", aeaItemInout);
//        return aeaItemInoutAdminService.listAeaItemInoutMatCert(aeaItemInout);
//    }

    /**
     * 保存事项输入输出材料证照
     *
     * @param inout
     * @param matCertIds
     * @return
     */
    @RequestMapping("/batchSaveItemInoutMatCert.do")
    public ResultForm batchSaveItemInoutMatCert(AeaItemInout inout, String[] matCertIds) {

        aeaItemInoutAdminService.batchSaveItemInoutMatCert(inout, matCertIds);
        return new ResultForm(true);
    }

    @RequestMapping("/batchSaveItemInoutMatCertAndNotDelOld.do")
    public ResultForm batchSaveItemInoutMatCertAndNotDelOld(AeaItemInout inout, String[] matCertIds) {

        aeaItemInoutAdminService.batchSaveItemInoutMatCertAndNotDelOld(inout, matCertIds);
        return new ResultForm(true);
    }

//    @Deprecated
//    @RequestMapping("/saveAeaItemMat.do")
//    public ResultForm saveAeaItemMat(HttpServletRequest request, AeaItemInout inout, AeaItemMat mat) throws Exception {
//
//        if (inout != null && StringUtils.isNotBlank(inout.getItemId())) {
//            AeaItemInout inoutResult = aeaItemInoutAdminService.saveAeaItemMat(request, inout, mat);
//            return new ContentResultForm<>(true, inoutResult);
//        }
//        return new ResultForm(false, "传递参数异常！");
//    }

    @RequestMapping("/getMatAndInoutList.do")
    public List<AeaItemInout> getMatAndInoutList(String keyword, String stateId, String itemVerId,
                                                 String isCommon, String isStateIn, String stateVerId) {

        ItemMatInoutQo matInoutQuery = ItemMatInoutQo.createMatInoutQuery()
                .rootOrgIdEq(SecurityContext.getCurrentOrgId())
                .itemVerIdEq(itemVerId)
                .stateVerIdEq(stateVerId)
                .isActiveEq(Status.ON)
                .isCommonEq(isCommon)
                .isStateInEq(isStateIn)
                .itemStateIdEq(stateId)
                .isInputEq(Status.ON);
        if (StringUtils.isNotBlank(keyword)) {
            matInoutQuery.matNameOrMatCodeFullLike(keyword);
        }
        return aeaItemInoutAdminService.listMatAndInoutList(matInoutQuery);
    }
}
