package com.augurit.aplanmis.common.form.controller;


import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaExProjDrawing;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.common.form.vo.LinkmanAddVo;
import com.augurit.aplanmis.common.form.vo.LinkmanEditVo;
import com.augurit.aplanmis.common.form.vo.LinkmanInfoVo;
import com.augurit.aplanmis.common.form.vo.UnitVo;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjDrawingService;
import com.augurit.aplanmis.common.service.form.AeaProjDrawingSerivce;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 施工图审查信息-Controller 页面控制转发类
 、*/
@RestController
@RequestMapping("/rest/form/drawing")
// /rest/form/drawing
public class AeaExProjDrawingController {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjDrawingController.class);

    @Autowired
    private AeaExProjDrawingService aeaExProjDrawingService;

    @Autowired
    private AeaProjDrawingSerivce aeaProjDrawingSerivce;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @Autowired
    private AeaExProjCertBuildService aeaExProjCertBuildService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;



    @RequestMapping("/index.html")
    public ModelAndView indexAeaExProjDrawing(String projInfoId,String formId,String refEntityId){
        ModelAndView modelAndView = new ModelAndView("form/constructReview");
        modelAndView.addObject("projInfoId",projInfoId);
        modelAndView.addObject("formId",formId);
        modelAndView.addObject("refEntityId",refEntityId);
        return modelAndView;
    }

 /*   @RequestMapping("/listAeaExProjDrawing.do")
    public PageInfo<AeaExProjDrawing> listAeaExProjDrawing(  AeaExProjDrawing aeaExProjDrawing, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaExProjDrawing);
        return aeaExProjDrawingService.listAeaExProjDrawing(aeaExProjDrawing,page);
    }*/

    @RequestMapping("/index.do")
    public  ResultForm getAeaExProjDrawing(String projInfoId) throws Exception {
        if (projInfoId==null||"".equals(projInfoId)){
            return new ResultForm(false, "获取项目信息失败，项目id "+projInfoId);
        }

        AeaProjInfo aeaProjInfoByProjInfoId = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if ( aeaProjInfoByProjInfoId==null  ){
            return new ResultForm(false, "获取项目信息失败，项目id "+projInfoId);
        }

        try {
        JSONObject object = new JSONObject();
        List<AeaProjDrawing> aeaProjDrawing = aeaProjDrawingSerivce.getAeaProjDrawing(projInfoId);
        if (aeaProjDrawing != null&&aeaProjDrawing.size()>0){
            logger.debug("根据ID获取AeaExProjDrawing对象，ID为：{}", projInfoId);
            object.put(  "drawings",aeaProjDrawing);
        }
        else {
            logger.debug("构建新的AeaExProjDrawing对象");
            ArrayList<AeaProjDrawing> drawings = new ArrayList<>();
            object.put( "drawings",drawings);
        }

        AeaExProjDrawing query = new AeaExProjDrawing();
        query.setProjInfoId(projInfoId);
        List<AeaExProjDrawing> aeaExProjDrawings = aeaExProjDrawingService.listAeaExProjDrawing(query);
        if (aeaExProjDrawings .size()>0){
            logger.debug("根据ID获取AeaExProjDrawing对象，ID为：{}", projInfoId);
            AeaExProjDrawing aeaExProjDrawing = aeaExProjDrawings.get(0);
            object.put("aeaExProjDrawing",aeaExProjDrawing);
        }
        else {

            object.put("aeaExProjDrawing",new AeaExProjDrawing());
        }

        return  new ContentResultForm<JSONObject>(true,object);
        }catch(Exception e){
            return new ContentResultForm<JSONObject>(false, new JSONObject(),"施工图审查信息！原因："+e.getMessage());
        }
    }


    @RequestMapping("/updateAeaExProjDrawing.do")
    public ResultForm updateAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaExProjDrawing);
        aeaExProjDrawingService.updateAeaExProjDrawing(aeaExProjDrawing);

        return new ResultForm(true);
    }


    /**
     * 保存或编辑施工图审查信息

     */
    @RequestMapping("/saveAeaExProjDrawing.do")
    public ResultForm saveAeaExProjDrawing(@RequestBody AeaProjDrawingVo aeaProjDrawingVo) throws Exception {
        AeaProjInfo aeaProjInfoByProjInfoId = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaProjDrawingVo.getProjInfoId());
        if ( aeaProjInfoByProjInfoId==null  ){
            return new ResultForm(false, "获取项目信息失败，项目id "+aeaProjDrawingVo.getProjInfoId());
        }
        try {
        AeaExProjDrawing aeaExProjDrawing = new AeaExProjDrawing();
        ContentResultForm<String> stringContentResultForm = aeaExProjCertBuildService.SynchronizeDataByAeaProjDrawingForm(aeaProjDrawingVo);//同步数据
            if(stringContentResultForm.isSuccess()){
                voToPojo(aeaProjDrawingVo,aeaExProjDrawing);

                if(aeaExProjDrawing.getDrawingId()!=null&&!"".equals(aeaExProjDrawing.getDrawingId())){
                    aeaExProjDrawingService.updateAeaExProjDrawing(aeaExProjDrawing);
                }else{
                    if(aeaExProjDrawing.getDrawingId()==null||"".equals(aeaExProjDrawing.getDrawingId()))
                        aeaExProjDrawing.setDrawingId(UUID.randomUUID().toString());
                    aeaExProjDrawingService.saveAeaExProjDrawing(aeaExProjDrawing);
                }
                aeaProjDrawingSerivce.saveAeaProjDrawing(aeaProjDrawingVo);
                return  new ContentResultForm<AeaProjDrawingVo>(true,aeaProjDrawingVo);
            }else {
                return stringContentResultForm;
            }

        }catch(Exception e){
            return new ContentResultForm<JSONObject>(false, new JSONObject(),"施工图审查信息！原因："+e.getMessage());
        }
    }




    //vo 转对象
    public AeaExProjDrawing voToPojo(AeaProjDrawingVo aeaProjDrawingVo,AeaExProjDrawing aeaExProjDrawing){
        aeaExProjDrawing.setDrawingId(aeaProjDrawingVo.getDrawingId());
        aeaExProjDrawing.setProjInfoId(aeaProjDrawingVo.getProjInfoId());
        aeaExProjDrawing.setDrawingQuabookCode(aeaProjDrawingVo.getDrawingQuabookCode());
        aeaExProjDrawing.setInverstmentMoeny(aeaProjDrawingVo.getInverstmentMoeny());
        aeaExProjDrawing.setApproveDrawingArea(aeaProjDrawingVo.getApproveDrawingArea());
        aeaExProjDrawing.setApproveStartTime(aeaProjDrawingVo.getApproveStartTime());
        aeaExProjDrawing.setApproveEndTime(aeaProjDrawingVo.getApproveEndTime());
        aeaExProjDrawing.setIsOncePass(aeaProjDrawingVo.getIsOncePass());
        aeaExProjDrawing.setOncePassAgainstCount(aeaProjDrawingVo.getOncePassAgainstCount());
        aeaExProjDrawing.setOncePassAgainstItem(aeaProjDrawingVo.getOncePassAgainstItem());
        aeaExProjDrawing.setApproveOpinion(aeaProjDrawingVo.getApproveOpinion());
        aeaExProjDrawing.setApproveConfirmTime(aeaProjDrawingVo.getApproveConfirmTime());
        aeaExProjDrawing.setGovOrgAreaCode(aeaProjDrawingVo.getGovOrgAreaCode());
        aeaExProjDrawing.setGovOrgCode(aeaProjDrawingVo.getGovOrgCode());
        aeaExProjDrawing.setGovOrgName(aeaProjDrawingVo.getGovOrgName());
        aeaExProjDrawing.setFormId(aeaProjDrawingVo.getFormId());
        aeaExProjDrawing.setRefEntityId(aeaProjDrawingVo.getRefEntityId());
        return aeaExProjDrawing;
    }

    @ApiOperation(value = "根据企业单位名称模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", dataType = "String"),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "String")
    })
    @GetMapping("/listUnit")
    public ContentResultForm<Set<UnitVo>> list(@RequestParam(required = false) String keyword, @RequestParam(required = false) String projInfoId) {
        Set<UnitVo> unitsByKeyword = aeaUnitInfoService.findAeaUnitInfoByKeyword(keyword)
                .stream()
                .map(this::getUnitVo).collect(Collectors.toSet());
        /// 先放开，可以根据关键字查询所有单位
        /*if (StringUtils.isNotBlank(projInfoId)) {
            Set<UnitVo> unitVos = aeaUnitInfoService.findAllProjUnit(projInfoId)
                    .stream()
                    .map(this::getUnitVo).collect(Collectors.toSet());
            // 取交集，合并结果
            unitsByKeyword.retainAll(unitVos);
        }*/
        return new ContentResultForm<>(true, unitsByKeyword, "Query success");
    }

    private UnitVo getUnitVo(AeaUnitInfo u) {
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        List<AeaLinkmanInfo> allUnitLinkman = aeaLinkmanInfoService.findAllUnitLinkman(u.getUnitInfoId());
        if (allUnitLinkman.size() > 0) {
            aeaLinkmanInfo = allUnitLinkman.get(0);
        }
        return UnitVo.from(u, aeaLinkmanInfo);
    }

    @ApiOperation(value = "编辑联系人", httpMethod = "POST")
    @PostMapping("/editlinkman")
    public ContentResultForm<String> edit(LinkmanEditVo linkmanEditVo) {
        Assert.isTrue(StringUtils.isNotBlank(linkmanEditVo.getLinkmanInfoId()), "linkmanInfoId is null");
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(linkmanEditVo.getLinkmanInfoId());
        if (aeaLinkmanInfo == null) {
            throw new RuntimeException("AeaLinkmanInfo not found, linkmanInfoId: " + linkmanEditVo.getLinkmanInfoId());
        }
        aeaLinkmanInfoService.updateAeaLinkmanInfo(linkmanEditVo.mergeAeaLinkmanInfo(aeaLinkmanInfo));
        return new ContentResultForm<>(true, linkmanEditVo.getLinkmanInfoId(), "Edit linkman success");
    }

    @ApiOperation(value = "新增联系人", httpMethod = "POST")
    @PostMapping("/savelinkman")
    public ContentResultForm<String> save(LinkmanAddVo linkmanAddVo) {
        String linkmanInfoId = aeaExProjDrawingService.save(linkmanAddVo);

        return new ContentResultForm<>(true, linkmanInfoId, "Save linkman success");

    }

    @GetMapping("/onelinkman/{linkmanInfoId}")
    @ApiOperation(value = "根据id获取联系人信息", httpMethod = "GET")
    @ApiImplicitParam(name = "linkmanInfoId", value = "联系人id", required = true, dataType = "String")
    public ContentResultForm<LinkmanInfoVo> one(@PathVariable String linkmanInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(linkmanInfoId), "linkmanInfoId is null");
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(linkmanInfoId);
        LinkmanInfoVo one = LinkmanInfoVo.fromAeaLinkmanInfo(aeaLinkmanInfo);
        return new ContentResultForm<>(true, one, "Query success");
    }



}
