package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemCondService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.state.AeaParStateService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.guide.controller.RestGuideController;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyService;
import com.augurit.aplanmis.mall.userCenter.service.RestParallerApplyService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaGuideApplyVo;
import com.augurit.aplanmis.mall.userCenter.vo.ApplyIteminstConfirmVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("rest/userCenter/apply")
@Api(value = "部门确认",tags = "部门确认 --> 部门确认接口")
public class RestItemGuideController {
    Logger logger= LoggerFactory.getLogger(RestItemGuideController.class);
    @Autowired
    RestApplyService restApplyService;
    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    AeaParStateService aeaParStateService;
    @Autowired
    FileUtilsService fileUtilsService;
    @Autowired
    AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    AeaItemStateService aeaItemStateService;
    @Autowired
    AeaItemCondService aeaItemCondService;
    @Autowired
    RestParallerApplyService restParallerApplyService;
    @Autowired
    AeaItemMatService aeaItemMatService;
    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;

    @PostMapping("net/guide/apply/start")
    @ApiOperation("阶段申报--> 部门辅导申请")
    public ContentResultForm<String> startGuideApply(@Valid @RequestBody AeaGuideApplyVo aeaGuideApplyVo){
        try {
            String applyinstId=restParallerApplyService.initGuideApply(aeaGuideApplyVo);
            return new ContentResultForm<>(true, applyinstId, "部门辅导申请成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"",e.getMessage());
        }
    }

    @GetMapping("guide/list")
    @ApiOperation("部门辅导--> 部门辅导列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键字",name = "keyword",required = false,dataType = "string"),
            @ApiImplicitParam(value = "辅导状态",name = "applyState",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ResultForm guideApplyList(String keyword, String applyState, int pageNum, int pageSize, HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return new ContentResultForm<>(true,new PageInfo<AeaHiGuide>(restParallerApplyService.searchGuideApplyListByUnitIdAndUserId(keyword,applyState,"",loginInfo.getUserId(),pageNum,pageSize)));
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return new ContentResultForm<>(true,new PageInfo<AeaHiGuide>(restParallerApplyService.searchGuideApplyListByUnitIdAndUserId(keyword,applyState,loginInfo.getUnitId(),loginInfo.getUserId(),pageNum,pageSize)));
            }else{//企业
                return new ContentResultForm<>(true,new PageInfo<AeaHiGuide>(restParallerApplyService.searchGuideApplyListByUnitIdAndUserId(keyword,applyState,loginInfo.getUnitId(),"",pageNum,pageSize)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"",e.getMessage());
        }
    }

    @GetMapping("guideItems/list")
    @ApiOperation(value = "阶段申报 --> 事项-部门确认/申请人确认接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "部门辅导ID",name = "guideId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "申请实例ID",name = "applyinstId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "项目ID",name = "projInfoId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "是否展示事项情形 1 是，0 否", name = "isSelectItemState", required = true, dataType = "string")})
    public ContentResultForm<ApplyIteminstConfirmVo> listGuideItemsByApplyinstId(String guideId, String applyinstId, String projInfoId, String isSelectItemState, HttpServletRequest request) {
        try {
            LoginInfoVo login = SessionUtil.getLoginInfo(request);
            if(login==null) return new ContentResultForm(false,"","登录状态异常，请重新登录");
            logger.error("-----listGuideItemsByApplyinstId----start--------");
            long l=System.currentTimeMillis();
            ApplyIteminstConfirmVo vo = restParallerApplyService.listGuideItemsByApplyinstId(guideId,applyinstId,projInfoId,isSelectItemState);
            logger.error("-----listGuideItemsByApplyinstId----end--------耗时："+(System.currentTimeMillis()-l));
            vo.setLoginType(StringUtils.isNotBlank(login.getUnitId())?"1":"0");
            vo.setIdCardCode(StringUtils.isNotBlank(login.getUnitId())?login.getUnifiedSocialCreditCode():login.getIdCard());
            return new ContentResultForm<ApplyIteminstConfirmVo>(true,vo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","事项-部门确认/申请人确认接口异常");
        }
    }
}
