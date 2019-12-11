package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.search.ApproveDataService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApproveService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaProjInfoResultVo;
import com.augurit.aplanmis.mall.userCenter.vo.LifeCycleDiagramVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//
@RestController
@RequestMapping("rest/user")
@Api(value = "", tags = "法人空间 --> 项目进度相关接口")
public class RestScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(RestScheduleController.class);

    @Autowired
    RestApproveService restApproveService;
    @Autowired
    ApproveDataService approveDataService;

    @GetMapping("toscheduleInquirePage")
    @ApiOperation(value = "跳转项目进度页面")
    public ModelAndView toSchedulePage(){
        return new ModelAndView("mall/userCenter/components/scheduleInquire");
    }

    @GetMapping("tolifeCyclePage")
    @ApiOperation(value = "跳转生命周期图页面")
    public ModelAndView toLifeCyclePage(String applyInstId, ModelMap modelMap){
        modelMap.put("applyInstId",applyInstId);
        return new ModelAndView("mall/userCenter/components/lifeCycle");
    }

    @GetMapping("itemSchedule/list")
    @ApiOperation(value = "项目进度 --> 项目进度列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "关键词",name = "keyword",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ContentResultForm<PageInfo<AeaProjInfoResultVo>> getItemSchedulelist(String keyword, int pageNum, int pageSize, HttpServletRequest request) {
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            List<AeaProjInfo> list = new ArrayList<>();
//            String unitInfoId="";
//            String linkmanInfoId="";
//            if("1".equals(loginInfo.getIsPersonAccount())){//个人
//                linkmanInfoId=loginInfo.getUserId();
//            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
//                linkmanInfoId=loginInfo.getUserId();
//                unitInfoId=loginInfo.getUnitId();
//            }else{//企业
//                unitInfoId=loginInfo.getUnitId();
//            }
            list = approveDataService.getScheduleProjListByUnitInfoIdOrLinkman(loginInfo.getUnitId(),loginInfo.getUserId(), keyword, pageNum, pageSize);
            PageInfo origPageInfo = new PageInfo<>(list);
            List<AeaProjInfoResultVo> projsByBuild = list.size() > 0 ? list.stream().map(AeaProjInfoResultVo::build).peek(vo->{
                List<AeaProjInfo> childs=approveDataService.getScheduleProjListByUnitInfoIdOrLinkmanNoPage(loginInfo.getUnitId(),loginInfo.getUserId(),keyword,vo.getProjInfoId());
                vo.setChildren(childs.size()==0?new ArrayList<>():childs.stream().map(AeaProjInfoResultVo::build).collect(Collectors.toList()));
            }).collect(Collectors.toList()) : new ArrayList<>();
            PageInfo<AeaProjInfoResultVo> pageInfo = new PageInfo<>(projsByBuild);
            BeanUtils.copyProperties(origPageInfo,pageInfo,"list");
            return new ContentResultForm<>(true,pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","查询项目进度列表查询接口异常");
        }
    }

    @GetMapping("item/schedule/diagram/{projInfoId}")
    @ApiOperation(value = "进度查询 --> 生命周期图相关数据接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "项目ID",name = "projInfoId",required = true,dataType = "string")})
    public ContentResultForm<LifeCycleDiagramVo> getLiftCycleDiagramInfo(@PathVariable("projInfoId") String projInfoId, HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            LifeCycleDiagramVo lifeCycleDiagramVo =new LifeCycleDiagramVo();
            long start=System.currentTimeMillis();
            logger.error("进度查询 --> 生命周期图相关数据接口getLiftCycleDiagramInfo---start");
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                lifeCycleDiagramVo=restApproveService.getLiftCycleDiagramInfo(projInfoId,"",loginInfo.getUserId());
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                lifeCycleDiagramVo=restApproveService.getLiftCycleDiagramInfo(projInfoId,loginInfo.getUnitId(),loginInfo.getUserId());
            }else{//企业
                lifeCycleDiagramVo=restApproveService.getLiftCycleDiagramInfo(projInfoId,loginInfo.getUnitId(),"");
            }
            logger.error("进度查询 --> 生命周期图相关数据接口getLiftCycleDiagramInfo,耗时:"+(System.currentTimeMillis()-start));
            return new ContentResultForm<LifeCycleDiagramVo>(true,lifeCycleDiagramVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","根据申请实例Id查询生命周期图相关数据异常");
        }
    }

}
