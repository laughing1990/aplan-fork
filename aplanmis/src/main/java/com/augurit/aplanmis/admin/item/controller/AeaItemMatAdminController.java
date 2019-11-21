package com.augurit.aplanmis.admin.item.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.mapper.ActStoFormMapper;
import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.att.utils.AttUtils;
import com.augurit.agcloud.bsc.sc.rule.code.service.AutoCodeNumberService;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemMatType;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatTypeAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.exceptions.InvalidImageException;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * @author ZhangXinhui
 * @date 2019/7/30 030 10:41
 * @desc
 **/
@Api(description = "前缀：/aea/item/mat",tags={"材料清单"})
@RestController
@RequestMapping("/aea/item/mat")
public class AeaItemMatAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemMatAdminController.class);

    @Autowired
    private AeaItemMatAdminService aeaItemMatAdminService;

    @Autowired
    private AeaItemMatTypeAdminService aeaItemMatTypeAdminService;

    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;

    @Autowired
    private IBscAttService bscAttService;

    @Autowired
    private UploaderFactory uploaderFactory;

    @Autowired
    private AutoCodeNumberService autoCodeNumberService;

    @Autowired
    private AeaCertMapper certMapper;

    @Autowired
    private AeaItemMatMapper matMapper;

    private String getRootOrgId(){
        return SecurityContext.getCurrentOrgId();
    }

    @RequestMapping("/globalMaterialIndex.do")
    public ModelAndView globalMaterialIndex() {

        return new ModelAndView("ui-jsp/mat/global_mat_index");
    }

    @ApiOperation(value = "保存/更新材料", notes = "保存/更新材料")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaItemMat", value = "必填" , dataType = "AeaItemMat" ,paramType = "body"),
    })
    @RequestMapping(value = "/handleGlobalMat.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm handleGlobalMat(AeaItemMat aeaItemMat, HttpServletRequest request) throws Exception {

        aeaItemMat.setRootOrgId(getRootOrgId());
        if (StringUtils.isNotBlank(aeaItemMat.getMatId())) {
            aeaItemMatAdminService.updateAeaItemMat(request, aeaItemMat);
        } else {
            aeaItemMat.setMatId(UUID.randomUUID().toString());
            aeaItemMat.setIsGlobalShare(Status.ON);
            aeaItemMat.setIsActive(ActiveStatus.ACTIVE.getValue());
            aeaItemMatAdminService.saveAeaItemMat(request, aeaItemMat);
        }
        return new ContentResultForm<>(true, aeaItemMat);
    }

    @ApiOperation(value = "检查材料编码是否唯一", notes = "检查材料编码是否唯一")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matId", value = "材料Id" , dataType = "String" , paramType = "query"),
        @ApiImplicitParam(name = "matCode", value = "材料编号" , required = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping("/checkMatCode.do")
    public String checkMatCode(String matId, String matCode) {

        if (StringUtils.isBlank(matCode)) {
            return "false";
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaItemMatAdminService.checkMatCode(matId, matCode, rootOrgId) + "";
    }

    @ApiOperation(value = "检查材料名称是否唯一", notes = "检查材料名称是否唯一")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matId", value = "材料Id" , dataType = "String" , paramType = "query"),
        @ApiImplicitParam(name = "matName", value = "材料名称" , required = true, dataType = "String" , paramType = "query"),
        @ApiImplicitParam(name = "isCommon", value = "材料是否通用" , required = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping("/checkMatName.do")
    public String checkMatName(String matId, String matName, String isCommon) {

        if (StringUtils.isBlank(matName)) {
            return "false";
        }
        if (StringUtils.isBlank(isCommon)) {
            return "false";
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaItemMatAdminService.checkMatName(matId, matName, isCommon, rootOrgId) + "";
    }

    @ApiOperation(value = "获取材料清单列表, 带分页", notes = "获取材料清单列表, 带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaItemMat", value = "必填" , dataType = "AeaItemMat" ,paramType = "body"),
    })
    @RequestMapping(value = "/globalMatList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaItemMat> globalMatList(AeaItemMat aeaItemMat, Page page) throws Exception {

        aeaItemMat.setIsActive(Status.ON);
        aeaItemMat.setRootOrgId(getRootOrgId());
        PageInfo<AeaItemMat> pageList = aeaItemMatAdminService.listAeaItemMat(aeaItemMat, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "通过id获取材料数据", notes = "通过id获取材料数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "材料id" , required = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping(value = "/getAeaItemMat.do", method = {RequestMethod.GET, RequestMethod.POST})
    public AeaItemMat getAeaItemMat(String id) {

        if (StringUtils.isNotBlank(id)) {
            String orgId = SecurityContext.getCurrentOrgId();
            AeaItemMat itemMat = aeaItemMatAdminService.getAeaItemMatById(id);
            if (itemMat != null) {
//                if (StringUtils.isNotBlank(itemMat.getMatTypeId())) {
//                    AeaItemMatType type = aeaItemMatTypeAdminService.getAeaItemMatTypeById(itemMat.getMatTypeId());
//                    if (type != null) {
//                        itemMat.setMatTypeName(type.getTypeName());
//                    }
//                }
                if (StringUtils.isNotBlank(itemMat.getMatProp())&& MindType.C.getValue().equals(itemMat.getMatProp())) {
                    AeaCert cert = certMapper.getAeaCertById(itemMat.getCertId(), orgId);
                    if (cert != null) {
                        itemMat.setCertName(cert.getCertName());
                    }
                }
                if (StringUtils.isNotBlank(itemMat.getMatProp())&&MindType.F.getValue().equals(itemMat.getMatProp())) {
                    ActStoForm form = matMapper.getActStoFormById(itemMat.getStoFormId());
                    if (form != null) {
                        itemMat.setFormName(form.getFormName());
                    }
                }
                List<BscAttForm> kbList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_MAT", "TEMPLATE_DOC", id, null, orgId, null);
                List<BscAttForm> ybList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_MAT", "SAMPLE_DOC", id, null, orgId, null);
                List<BscAttForm> sjList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_MAT", "REVIEW_SAMPLE_DOC", id, null, orgId, null);
                itemMat.setTemplateDocCount(kbList == null ? 0L : kbList.size());
                itemMat.setSampleDocCount(ybList == null ? 0L : ybList.size());
                itemMat.setReviewSampleDocCount(sjList == null ? 0L : sjList.size());
            }
            return itemMat;
        } else {
            logger.debug("构建新的AeaItemMat对象");
            return new AeaItemMat();
        }
    }

    @ApiOperation(value = "通过材料Id删除材料数据", notes = "通过材料Id删除材料数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "材料Id" , required = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping(value = "/deleteAeaItemMatById.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm deleteAeaItemMatById(String id) throws Exception {

        List<AeaItemInout> list = new ArrayList<>();
        if (StringUtils.isNotBlank(id)) {
            //如果材料存在关联，则不允许删除
            list = aeaItemInoutAdminService.getAeaItemInoutByMatId(id, SecurityContext.getCurrentOrgId());
            if(list.isEmpty()) {
                logger.debug("删除材料信息表Form对象，对象id为：{}", id);
                aeaItemMatAdminService.deleteAeaItemMatById(id);
                return new ResultForm(true, "删除成功!");
            }else{
                return new ResultForm(false, "材料存在引用!");
            }
        }else{
            return new ResultForm(false, "操作对象id为空!");
        }
    }

    @ApiOperation(value = "批量删除材料", notes = "批量删除材料")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "材料id集，以逗号分隔", dataType = "String", required = true),
        @ApiImplicitParam(name = "names", value = "材料名称集，以逗号分隔", dataType = "String", required = true)
    })
    @RequestMapping(value = "/batchDeleteAeaItemMatByIds.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm batchDeleteAeaItemMatByIds(String[] ids, String[] names) {

        if(ids!=null&&ids.length>0){
            List<String> canDelIds = new ArrayList<String>();
            List<String> canDelNames = new ArrayList<String>();
            List<String> notCanDelIds = new ArrayList<String>();
            List<String> notCanDelNames = new ArrayList<String>();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for (int i = 0; i < ids.length; i++) {
                List<AeaItemInout> list = aeaItemInoutAdminService.getAeaItemInoutByMatId(ids[i], rootOrgId);
                if(list.isEmpty()) {
                    canDelIds.add(ids[i]);
                    canDelNames.add(names[i]);
                }else{
                    notCanDelIds.add(ids[i]);
                    notCanDelNames.add(names[i]);
                }
            }
            int len = canDelIds.size();
            ids = canDelIds.toArray(new String[len]);
            if (ids != null && ids.length > 0) {
                aeaItemMatAdminService.batchDeleteAeaItemMatByIds(ids);
            }
            if(len==0){
                return new ResultForm(false, "您所勾选材料都被引用，不能删除!");
            }else{
                String canDelStr = canDelNames.toString() + canDelNames.size()+"个材料成功删除,";
                String notCanDelStr = "";
                if(!notCanDelNames.isEmpty()) {
                    notCanDelStr = notCanDelNames.toString() + notCanDelNames.size() + "个材料存在引用不能删除!";
                }
                return new ResultForm(true, canDelStr + notCanDelStr);
            }
        }else{
            return new ResultForm(false, "操作对象集合ids为空!");
        }
    }

    @ApiOperation(value = "删除附件并更新对应材料字段", notes = "删除附件并更新对应材料字段")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "type", value = "删除字段类型", dataType = "Integer", required = true),
        @ApiImplicitParam(name = "matId", value = "材料Id数据", dataType = "String", required = true),
        @ApiImplicitParam(name = "detailId", value = "附件Id数据", dataType = "String", required = true)
    })
    @RequestMapping(value = "/deleteGlobalMatDoc.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm deleteGlobalMatDoc(Integer type, String matId, String detailId) throws Exception {

        if (type==null ||  StringUtils.isBlank(detailId) || StringUtils.isBlank(matId)) {
            return new ResultForm(false);
        }
        aeaItemMatAdminService.delelteFile(type, matId, detailId);
        return new ResultForm(true);
    }

    @ApiOperation(value = "下载附件", notes = "下载附件")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "type", value = "删除字段类型", dataType = "Integer", required = true),
        @ApiImplicitParam(name = "detailId", value = "附件Id数据", dataType = "String", required = true)
    })
    @RequestMapping(value = "/downloadGlobalMatDoc.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm downloadGlobalMatDoc(Integer type, String detailId, HttpServletResponse response , HttpServletRequest request)throws Exception{

        if (StringUtils.isBlank(detailId)) {
            return new ResultForm(false, "找不到文件!");
        }else{
            aeaItemMatAdminService.downloadDoc(type, detailId, response, request);
            logger.debug("执行附件下载成功。");
            return new ResultForm(true);
        }
    }

    @ApiOperation(value = "在线预览附件", notes = "在线预览附件")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "detailId", value = "附件Id数据", dataType = "String", required = true)
    })
    @RequestMapping(value = "/showFile.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView showFile(HttpServletRequest request, HttpServletResponse response,
                                 String detailId, ModelMap map) throws Exception {

        BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
        boolean isExistImg = false;
        String msg = "服务器文件不存在！";
        if (form != null) {
            JSONObject img = new JSONObject();
            String base64Content = null;
            img.put("fileName", form.getAttName());
            img.put("fileType", form.getAttFormat());
            UploadFileStrategy uploadFileStrategy = uploaderFactory.create(form.getStoreType());
            InputStream in = uploadFileStrategy.download(detailId);
            if(in!=null){
                Base64.Encoder encoder = Base64.getEncoder();
                base64Content = encoder.encodeToString(AttUtils.inputStreamToBytesAndClose(in));
                isExistImg = true;
            }else{
                isExistImg = false;
            }
            img.put("base64Content", base64Content);
            map.put("att", form);
            map.put("imgData", img);
        }else{
            isExistImg = false;
        }
        map.put("isExistImg", isExistImg);
        map.put("msg", msg);
        return new ModelAndView("ui-jsp/aplanmis/item/show_mat_file");
    }

    /**
     * 获取阶段未选择全局材料
     *
     * @param stageId
     * @param keyword
     * @param page
     * @return
     */
    @RequestMapping("/listStageNoSelectGlobalMat.do")
    public EasyuiPageInfo<AeaItemMat> listStageNoSelectGlobalMat(String stageId, String keyword, Page page) {

        PageInfo<AeaItemMat> pageInfo = aeaItemMatAdminService.listStageNoSelectGlobalMat(stageId, keyword, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 获取事项输入输出未选择全局材料
     *
     * @param itemVerId
     * @param stateVerId
     * @param isInput
     * @param keyword
     * @param page
     * @return
     */
    @RequestMapping("/listItemInOutNoSelectGlobalMat.do")
    public EasyuiPageInfo<AeaItemMat> listItemInOutNoSelectGlobalMat(String itemVerId, String stateVerId,
                                                                     String isInput, String keyword, Page page) {
        String rootOrgId = SecurityContext.getCurrentOrgId();
        PageInfo<AeaItemMat> pageInfo = aeaItemMatAdminService.listItemInOutNoSelectGlobalMat(rootOrgId, itemVerId, stateVerId, isInput, keyword, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @PostMapping("/saveMatAndParIn.do")
    public ResultForm saveStageMatAndParIn(HttpServletRequest request,AeaItemMat aeaItemMat) throws Exception {

        Assert.notNull(aeaItemMat.getStageId(), "stageId不能为空");
        aeaItemMat.setCreater(SecurityContext.getCurrentUserId());
        aeaItemMat.setRootOrgId(SecurityContext.getCurrentOrgId());
        if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(aeaItemMat.getMatId())) {
            aeaItemMatAdminService.updateAeaItemMat(request, aeaItemMat);
        } else {
            aeaItemMat.setMatId(UuidUtil.generateUuid());
            aeaItemMatAdminService.saveAeaItemMatAndParIn(request, aeaItemMat.getStageId(), aeaItemMat.getIsStateIn(), aeaItemMat.getParStateId(), aeaItemMat);
        }

        return new ResultForm(true);
    }

    @RequestMapping("/allListAeaItemMatPage.do")
    public EasyuiPageInfo<AeaItemMat> allListAeaItemMatPage(AeaItemMat aeaItemMat, Page page) throws Exception {

        PageInfo<AeaItemMat> pageList = aeaItemMatAdminService.listStageOrItemNoSelectMatPage(aeaItemMat, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping("/saveChooseStageMatAndParIn.do")
    public ResultForm saveChooseStageMatAndParIn(String ids, String stageId, String isStateIn, String stateId) throws Exception {

        if (StringUtils.isBlank(stageId)) {
            return new ResultForm(false, "请求参数stageId为空!");
        }
        if(StringUtils.isBlank(isStateIn)){
            return new ResultForm(false, "请求参数isStateIn为空!");
        }
        if(isStateIn.equalsIgnoreCase("1")&&StringUtils.isBlank(stateId)){
            return new ResultForm(false, "请求参数stateId为空!");
        }
        aeaItemMatAdminService.saveChooseStageMatAndParIn(ids, stageId, isStateIn, stateId);
        return new ResultForm(true);
    }

    @RequestMapping("/saveChooseItemMatAndInout.do")
    public ResultForm saveChooseItemMatAndInout(String ids, String itemVerId, String stateVerId, String isStateIn, String itemStateId, String isCommon) throws Exception {

        if (StringUtils.isBlank(itemVerId)) {
            throw  new InvalidImageException("参数itemVerId为空!");
        }
        if(StringUtils.isBlank(stateVerId)){
            throw  new InvalidImageException("参数stateVerId为空!");
        }
        if(StringUtils.isBlank(isStateIn)){
            throw  new InvalidImageException("参数isStateIn为空!");
        }
        if(StringUtils.isBlank(isCommon)){
            throw  new InvalidImageException("参数isCommon为空!");
        }
        if(isStateIn.equals("1")&&StringUtils.isBlank(itemStateId)){
            throw  new InvalidImageException("参数itemStateId为空!");
        }
        aeaItemMatAdminService.saveChooseItemMatAndInout(ids, itemVerId, isStateIn, itemStateId, stateVerId, isCommon);
        return new ResultForm(true);
    }

    @RequestMapping("/gtreeForm.do")
    public List<ZtreeNode> gtreeForm() {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaItemMatAdminService.gtreeForm(rootOrgId);
    }

    // ===================== 前后端分离接口新写法 =======================

    @ApiOperation(value = "获取材料清单列表, 带分页", notes = "获取材料清单列表, 带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaItemMat", value = "必填" , dataType = "AeaItemMat" ,paramType = "body"),
    })
    @RequestMapping(value = "/listGlobalMatNew.do", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyuiPageInfo<AeaItemMat> listGlobalMatNew(AeaItemMat aeaItemMat, @ModelAttribute PageParam page) throws Exception {

        aeaItemMat.setIsActive(Status.ON);
        aeaItemMat.setRootOrgId(getRootOrgId());
        PageInfo<AeaItemMat> pageList = aeaItemMatAdminService.listAeaItemMat(aeaItemMat, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "检查材料编号是否唯一", notes = "检查材料编号是否唯一")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matId", value = "材料Id" , dataType = "String" , paramType = "query"),
        @ApiImplicitParam(name = "matCode", value = "材料编号" , required = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping(value = "/checkMatCodeNew.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Boolean checkMatCodeNew(String matId, String matCode) {

        if (StringUtils.isBlank(matCode)) {
            return false;
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaItemMatAdminService.checkMatCode(matId, matCode, rootOrgId);
    }

    @ApiOperation(value = "通过规则编号codeIc生成某顶级组织下的编码", notes = "通过规则编号codeIc生成某顶级组织下的编码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "codeIc", value = "规则编号,必填", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getOneOrgAutoCode.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm getOneOrgAutoCode(String codeIc) throws Exception {

        if (StringUtils.isBlank(codeIc)) {
            return new ResultForm(false, "参数：规则编号codeIc为空!");
        } else {
            try{
                String matCode = autoCodeNumberService.generate(codeIc, SecurityContext.getCurrentOrgId());
                return new ContentResultForm<String>(true, matCode);
            }catch (Exception e){
                return new ResultForm(false, e.getMessage());
            }
        }
    }
}
