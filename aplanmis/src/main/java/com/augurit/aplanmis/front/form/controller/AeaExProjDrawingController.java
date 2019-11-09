package com.augurit.aplanmis.front.form.controller;


import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicCodeType;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaExProjDrawing;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.form.AeaExProjDrawingService;
import com.augurit.aplanmis.common.service.form.AeaProjDrawingSerivce;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;
import com.augurit.aplanmis.front.subject.linkman.serivce.RestLinkmanService;
import com.augurit.aplanmis.front.subject.linkman.vo.LinkmanAddVo;
import com.augurit.aplanmis.front.subject.unit.vo.UnitVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.spring.web.json.Json;

import java.sql.Timestamp;
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
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaProjDrawingSerivce aeaProjDrawingSerivce;
    @Autowired
    private RestLinkmanService restLinkmanService;

    @RequestMapping("/index.html")
    public ModelAndView indexAeaExProjDrawing(String projInfoId){
        ModelAndView modelAndView = new ModelAndView("form/constructReview");
        modelAndView.addObject("projInfoId",projInfoId);
        return modelAndView;
    }

 /*   @RequestMapping("/listAeaExProjDrawing.do")
    public PageInfo<AeaExProjDrawing> listAeaExProjDrawing(  AeaExProjDrawing aeaExProjDrawing, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaExProjDrawing);
        return aeaExProjDrawingService.listAeaExProjDrawing(aeaExProjDrawing,page);
    }*/

    @RequestMapping("/index.do")
    public  ResultForm getAeaExProjDrawing(String projInfoId) throws Exception {

        JSONObject object = new JSONObject();
        List<AeaProjDrawing> aeaProjDrawing = aeaProjDrawingSerivce.getAeaProjDrawing(projInfoId);
        if (aeaProjDrawing != null){
            logger.debug("根据ID获取AeaExProjDrawing对象，ID为：{}", projInfoId);
            object.put(  "drawings",aeaProjDrawingSerivce.getAeaProjDrawing(projInfoId));
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
     /*   if(result.hasErrors()) {
            logger.error("保存施工图审查信息Form对象出错");
            throw new InvalidParameterException(aeaExProjDrawing);
        }*/

        AeaExProjDrawing aeaExProjDrawing = new AeaExProjDrawing();

        voToPojo(aeaProjDrawingVo,aeaExProjDrawing);

        if(aeaExProjDrawing.getDrawingId()!=null&&!"".equals(aeaExProjDrawing.getDrawingId())){
            aeaExProjDrawingService.updateAeaExProjDrawing(aeaExProjDrawing);
        }else{
            if(aeaExProjDrawing.getDrawingId()==null||"".equals(aeaExProjDrawing.getDrawingId()))
                aeaExProjDrawing.setDrawingId(UUID.randomUUID().toString());
            aeaExProjDrawingService.saveAeaExProjDrawing(aeaExProjDrawing);
        }

        List<AeaProjDrawing> aeaProjDrawing = aeaProjDrawingVo.getAeaProjDrawing();

        aeaProjDrawingSerivce.saveAeaProjDrawing(aeaProjDrawing);


        return  new ContentResultForm<AeaProjDrawingVo>(true,aeaProjDrawingVo);
    }


    @ApiOperation(value = "根据企业单位名称模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", dataType = "String"),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "String")
    })
    @GetMapping("/list")
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

    @GetMapping("/list/by/{projectInfoId}")
    @ApiOperation(value = "根据项目ID查找关联的单位列表")
    @ApiImplicitParam(name = "projectInfoId", value = "项目id", required = true, dataType = "String")
    public ContentResultForm<List<UnitVo>> listByProjectInfoId(@PathVariable String projectInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(projectInfoId) , "projectInfoId is null");

        List<UnitVo> unitVos = aeaUnitInfoService.findAllProjUnit(projectInfoId).stream()
                .map(u -> UnitVo.from(u, null)).collect(Collectors.toList());
        return new ContentResultForm<>(true, unitVos, "Query success");
    }


    @ApiOperation(value = "新增联系人", httpMethod = "POST")
    @PostMapping("/save")
    public ContentResultForm<String> save(LinkmanAddVo linkmanAddVo) {
        String linkmanInfoId = restLinkmanService.save(linkmanAddVo);
        return new ContentResultForm<>(true, linkmanInfoId, "Save linkman success");
    }


    //vo 转对象
    public AeaExProjDrawing voToPojo(AeaProjDrawingVo aeaProjDrawingVo,AeaExProjDrawing aeaExProjDrawing){
        aeaExProjDrawing.setDrawingId(aeaProjDrawingVo.getDrawingId());
        aeaExProjDrawing.setProjInfoId(aeaProjDrawingVo.getProjInfoId());
        aeaExProjDrawing.setProvinceProjCode(aeaProjDrawingVo.getProvinceProjCode());
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
        return aeaExProjDrawing;
    }

}
