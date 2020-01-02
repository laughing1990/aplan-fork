package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyCommonService;
import com.augurit.aplanmis.mall.userCenter.service.RestApproveService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//草稿箱
@RestController
@RequestMapping("rest/user")
@Api(value = "", tags = "法人空间 --> 草稿箱相关接口")
public class RestDraftController {
    Logger logger= LoggerFactory.getLogger(RestDraftController.class);

    @Autowired
    RestApproveService restApproveService;
    @Autowired
    private RestApplyCommonService restApplyCommonService;
    @Value("${aplanmis.mall.skin:skin_v4.0}/")
    private String skin;
    @GetMapping("todraftsPage")
    @ApiOperation(value = "跳转草稿箱页面")
    public ModelAndView toDraftPage(){
        return new ModelAndView("mall/"+skin+"userCenter/components/drafts");
    }

    @GetMapping("draftApply/list")
    @ApiOperation(value = "草稿箱 --> 暂存件列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键词",name = "keyword",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ResultForm getDraftApplyList(String keyword, int pageNum, int pageSize,HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return new ContentResultForm(true,restApproveService.getDraftApplyList("",loginInfo.getUserId(),keyword,pageNum,pageSize));
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return new ContentResultForm(true,restApproveService.getDraftApplyList(loginInfo.getUnitId(),loginInfo.getUserId(),keyword,pageNum,pageSize));
            }else{//企业
                return new ContentResultForm(true,restApproveService.getDraftApplyList(loginInfo.getUnitId(),"",keyword,pageNum,pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查询已申报项目列表查询接口异常");
        }
    }

    @GetMapping("draftApply/delete/{applyinstId}")
    @ApiOperation(value = "草稿箱 --> 删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请实例ID",name = "applyinstId",required = true,dataType = "string")})
    public ResultForm getDraftApplyList(@PathVariable String applyinstId){
        try {
            restApplyCommonService.deleteApplyinstAllInstData(applyinstId);
            return new ResultForm(true,"删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"删除数据接口异常"+e.getMessage());
        }
    }
}
