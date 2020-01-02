package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApproveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//审批情况
@RestController
@RequestMapping("rest/user")
@Api(value = "", tags = "法人空间 --> 审批情况相关接口")
public class RestApproveController {
    Logger logger= LoggerFactory.getLogger(RestApproveController.class);

    @Autowired
    RestApproveService restApproveService;
    @Value("${aplanmis.mall.skin:skin_v4.0}/")
    private String skin;
    @GetMapping("toapprovePage")
    @ApiOperation(value = "跳转审批情况页面")
    public ModelAndView toApprovePage(){
        return new ModelAndView("mall/"+skin+"userCenter/components/approve");
    }

    @GetMapping("approve/list")
    @ApiOperation(value = "审批情况 --> 审批情况列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键字",name = "keyword",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ResultForm getApprovelist(String keyword,int pageNum,int pageSize,HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return new ContentResultForm<>(true,restApproveService.searchIteminstApproveInfoListByUnitIdAndUserId(keyword,"",loginInfo.getUserId(),pageNum,pageSize));
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return new ContentResultForm<>(true,restApproveService.searchIteminstApproveInfoListByUnitIdAndUserId(keyword,loginInfo.getUnitId(),loginInfo.getUserId(),pageNum,pageSize));
            }else{//企业
                return new ContentResultForm<>(true,restApproveService.searchIteminstApproveInfoListByUnitIdAndUserId(keyword,loginInfo.getUnitId(),"",pageNum,pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查询审批情况列表查询接口异常");
        }
    }

    @GetMapping("approvalAndMatCompletion/num")
    @ApiOperation(value = "申报查询->统计数目接口")
    public ResultForm getApprovalAndMatCompletionNum(HttpServletRequest request){
        try {
            return new ContentResultForm<>(true,restApproveService.getApprovalAndMatCompletionNum(request));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"申报查询->统计数目接口异常");
        }
    }

}
