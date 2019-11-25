package com.augurit.aplanmis.common.form.controller;


import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaExProjDrawing;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.common.service.form.AeaExProjDrawingService;
import com.augurit.aplanmis.common.service.form.AeaProjDrawingSerivce;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return aeaExProjDrawing;
    }

}
