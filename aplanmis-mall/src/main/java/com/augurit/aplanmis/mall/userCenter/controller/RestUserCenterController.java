package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.constant.LoginUserRoleEnum;
import com.augurit.aplanmis.mall.userCenter.service.RestUserCenterService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaLinkmanInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest/user")
@Api(value = "", tags = "法人空间 -->企业中心相关接口")
public class RestUserCenterController {
    /*******************************************企业中心模块页面跳转开始*****************************************************/
    Logger logger= LoggerFactory.getLogger(RestUserCenterController.class);

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private RestUserCenterService restUserCenterService;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @GetMapping("/toMyCloundSpacesPage")
    @ApiOperation(value = "法人空间我的首页")
    public ModelAndView toMyCloundSpacesPage(){
        return new ModelAndView("mall/userCenter/components/my-cloundSpaces");
    }

    @GetMapping("/toMyHomeIndexPage")
    @ApiOperation(value = "法人空间我的首页")
    public ModelAndView toMyHomeIndexPage(){
        return new ModelAndView("mall/userCenter/components/myHomeIndex");
    }

    @GetMapping("/toMyMaterialsPage")
    @ApiOperation(value = "我的材料库页面")
    public ModelAndView toMyMaterialsPage(){
        return new ModelAndView("mall/userCenter/components/my-materials");
    }

    @GetMapping("/toUserCenterindexPage")
    @ApiOperation(value = "法人空间页面")
    @ApiImplicitParam(value = "事项版本ID",name = "itemVerId",required = false,dataType = "string")
    public ModelAndView toUserCenterindexPage(String itemVerId, ModelMap modelMap){
        modelMap.put("itemVerId",itemVerId);
        return new ModelAndView("mall/userCenter/userCenterIndex");
    }

    @GetMapping("toUserInfoPage")
    @ApiOperation(value = "跳转企业中心信息页面")
    public ModelAndView userInfo () throws Exception {
        return new ModelAndView("mall/userCenter/components/userCenterPage");
    }

    @GetMapping("toCreditDetailPage")
    @ApiOperation(value = "跳转企业信用页面")
    public ModelAndView toCreditDetailPage() throws Exception {
        return new ModelAndView("mall/userCenter/components/creditDetail");
    }


    @PostMapping("unitInfo/save")
    @ApiOperation("保存单位信息")
    public ResultForm save(AeaUnitInfo aeaUnitInfo, HttpServletRequest request){
        //要求密码字段MD5加密
        if(StringUtils.isNotBlank(aeaUnitInfo.getUnitInfoId())){
            aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
        }else{
            aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
        }
        return new ResultForm(true,"success");
    }

    @PostMapping("linkman/save")
    @ApiOperation("保存联系人信息")
    public ResultForm saveLinkman(AeaLinkmanInfo aeaLinkmanInfo, HttpServletRequest request) throws  Exception{
        //要求密码字段MD5加密
        if(StringUtils.isNotBlank(aeaLinkmanInfo.getLinkmanInfoId())){
            aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
        }else{
            aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
        }
        //mainService.freshenUnitInfo(request);
        return new ResultForm(true,"success");
    }

    @PostMapping("linkmanCascade/save")
    @ApiOperation("保存单位联系人信息及单位与联系人的关系")
    public ResultForm saveLinkmanAndUnitLinkman(AeaLinkmanInfo aeaLinkmanInfo, HttpServletRequest request){
        //要求：登录名，密码字段不用填，unitInfoId不为空
        if(StringUtils.isEmpty(aeaLinkmanInfo.getUnitInfoId()))  return new ResultForm(false,"单位为空");
        try {
            if(StringUtils.isNotBlank(aeaLinkmanInfo.getLinkmanInfoId())){
                aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
            }else{//根据身份证号码查询联系人，若已存在，则更新信息，若不存在，则新增
                AeaLinkmanInfo query=new AeaLinkmanInfo();
                query.setLinkmanCertNo(aeaLinkmanInfo.getLinkmanCertNo());
                List<AeaLinkmanInfo> linkmans = aeaLinkmanInfoService.findLinkmanInfo(query);
                if(linkmans.size()>0){
                    aeaLinkmanInfo.setLinkmanInfoId(linkmans.get(0).getLinkmanInfoId());
                    aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
                }else{
                    aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
                }
            }
            //先查询单位与联系人的关系是否存在，若存在则无需保存，若不存在，则新增
            AeaUnitLinkman param=new AeaUnitLinkman();
            param.setUnitInfoId(aeaLinkmanInfo.getUnitInfoId());
            param.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            List<AeaUnitLinkman> unitLinkmans = aeaUnitLinkmanMapper.listAeaUnitLinkman(param);
            if(unitLinkmans.size()==0) {
                param.setIsBindAccount(aeaLinkmanInfo.getIsBindAccount());
                param.setUnitLinkmanId(UUID.randomUUID().toString());
                param.setCreater(SessionUtil.getSessionUnitId(request));
                param.setCreateTime(new Date());
                param.setIsAdministrators("0");//不是管理员
                aeaUnitLinkmanMapper.insertAeaUnitLinkman(param);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"保存联系人信息失败");
        }
        return new ResultForm(true,"success");
    }

    //委托人管理-绑定/解绑单位
    @PostMapping("bindRelation/update")
    @ApiOperation(value = "委托人管理-绑定/解绑单位")
    @ApiImplicitParams({@ApiImplicitParam(value = "单位Id",name = "unitInfoId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "联系人Id",name = "linkmanInfoId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "绑定关系0:解绑，1:绑定",name = "isBindAccount",required = true,dataType = "string")})
    public ResultForm bindOrUnbindRelation(String unitInfoId,String linkmanInfoId,String isBindAccount){
        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        aeaUnitLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaUnitLinkman.setUnitInfoId(unitInfoId);
        //isBindAccount=0解绑  isBindAccount=1绑定
        aeaUnitLinkman.setIsBindAccount(isBindAccount);
        try {
            aeaLinkmanInfoService.updateAeaUnitLinkmanByUnitAndLinkman(aeaUnitLinkman);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,e.getMessage());
        }
        return new ResultForm(true,"success");
    }

    //删除联系人
    @DeleteMapping("linkman/delete/{unitInfoId}/{linkmanInfoId}")
    @ApiOperation(value = "删除联系人")
    @ApiImplicitParams({@ApiImplicitParam(value = "单位id",name = "unitInfoId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "联系人id",name = "linkmanInfoId",required = true,dataType = "string")})
    public ResultForm deleteRelation(@PathVariable("unitInfoId") String unitInfoId,@PathVariable("linkmanInfoId") String linkmanInfoId){
        aeaLinkmanInfoService.deleteUnitLinkman(unitInfoId,linkmanInfoId);
        return new ResultForm(true,"success");
    }

    //查询所有人或者委托人列表
    @GetMapping("linkman/list")
    @ApiOperation(value = "查询所有人或者委托人列表")
    @ApiImplicitParam(value = "空值:查询所有人，1:查询委托人",name = "isAll",required = true,dataType = "string")
    public ResultForm getAllUnitLinkInfo(HttpServletRequest request, String isAll) throws Exception{
        LoginInfoVo user = SessionUtil.getLoginInfo(request);
        List<AeaLinkmanInfo> aeaLinkmanInfos = new ArrayList<>();
        if(user != null){
            aeaLinkmanInfos =  aeaLinkmanInfoMapper.getAeaLinkmanInfoByUnitInfoIdAndIsBindAccount(user.getUnitId(),isAll);
        }
        return new ContentResultForm<>(true,aeaLinkmanInfos.stream().map(AeaLinkmanInfoVo::build).collect(Collectors.toList())) ;
    }

    //查询某单位的所有联系人列表
    @GetMapping("linkman/getByUnitInfoId/{unitInfoId}")
    @ApiOperation(value = "查询某单位的所有联系人列表")
    @ApiImplicitParam(value = "单位ID", name = "unitInfoId", required = true, dataType = "string")
    public ResultForm getAllUnitLinkInfo(@PathVariable("unitInfoId") String unitInfoId) throws Exception {
        List<AeaLinkmanInfo> linkmanInfoList = aeaLinkmanInfoService.findAllUnitLinkman(unitInfoId);
        return new ContentResultForm<>(true, linkmanInfoList);
    }

    @GetMapping("userinfo")
    @ApiOperation(value = "获取当前登录用户详情")
    public ContentResultForm<UserInfoVo> getUserInfo(HttpServletRequest request){
        UserInfoVo userInfoVo = new UserInfoVo();
        LoginInfoVo user = SessionUtil.getLoginInfo(request);

        try {
            if(user!=null){
                if("1".equals(user.getIsPersonAccount())){//个人
                    AeaLinkmanInfoVo aeaLinkmanInfo=restUserCenterService.getAeaLinkmanInfoByLinkmanInfoId(user.getUserId());
                    userInfoVo.setAeaLinkmanInfo(aeaLinkmanInfo);
                    userInfoVo.setRole(LoginUserRoleEnum.PERSONAL.getValue());
                }else if(StringUtils.isNotBlank(user.getUserId())){//委托人
                    AeaLinkmanInfoVo aeaLinkmanInfo=restUserCenterService.getAeaLinkmanInfoByLinkmanInfoId(user.getUserId());
                    List<AeaUnitInfoVo> aeaUnitList=restUserCenterService.getUnitInfoListByLinkmanInfoId(user.getUserId());
                    userInfoVo.setAeaUnitList(aeaUnitList);
                    userInfoVo.setAeaLinkmanInfo(aeaLinkmanInfo);
                    userInfoVo.setRole(LoginUserRoleEnum.HANDLE.getValue());
                }else{//企业
                    AeaUnitInfoVo aeaUnitInfo = restUserCenterService.getAeaUnitInfoByUnitInfoId(user.getUnitId());
                    List<AeaLinkmanInfoVo> linkmanInfoList=restUserCenterService.findAllUnitLinkman(user.getUnitId());
                    userInfoVo.setLinkmanInfoList(linkmanInfoList);
                    userInfoVo.setAeaUnitInfo(aeaUnitInfo);
                    userInfoVo.setRole(LoginUserRoleEnum.UNIT.getValue());
                }
            }
            return new ContentResultForm<>(true,userInfoVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false,null,"获取当前登录用户详情发生异常");
        }
    }

    @GetMapping("unitinfo/list/{linkmanInfoId}")
    @ApiOperation(value = "查询联系人单位列表")
    @ApiImplicitParam(value = "联系人id",name = "linkmanInfoId",required = true,dataType = "string")
    public ResultForm getUnitInfoByLinkmanInfoId (@PathVariable("linkmanInfoId") String linkmanInfoId) throws Exception {
        return new ContentResultForm<>(true,restUserCenterService.getUnitInfoListByLinkmanInfoId(linkmanInfoId)) ;
    }

    @GetMapping("linkman/{linkmanInfoId}/{unitInfoId}")
    @ApiOperation(value = "查询联系人信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "单位id",name = "unitInfoId",required = true,dataType = "string"),
                @ApiImplicitParam(value = "联系人id",name = "linkmanInfoId",required = true,dataType = "string")})
    public ResultForm getByLinkmanInfoId (@PathVariable("linkmanInfoId") String linkmanInfoId,@PathVariable("unitInfoId") String unitInfoId) {
        if(StringUtils.isBlank(linkmanInfoId)) return new ResultForm(false,"联系人参数缺失");
        if(StringUtils.isBlank(unitInfoId)) return new ResultForm(false,"单位参数缺失");
        List<AeaUnitLinkman> unitLinkmans = new ArrayList<>();
        AeaLinkmanInfoVo aeaLinkmanInfo;
        try {
        aeaLinkmanInfo=restUserCenterService.getAeaLinkmanInfoByLinkmanInfoId(linkmanInfoId);
        AeaUnitLinkman query=new AeaUnitLinkman();
        query.setUnitInfoId(unitInfoId);
        query.setLinkmanInfoId(linkmanInfoId);
        unitLinkmans = aeaUnitLinkmanMapper.listAeaUnitLinkman(query);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查找单位下的联系人信息失败");
        }
        if(unitLinkmans.size()>0) aeaLinkmanInfo.setIsBindAccount(StringUtils.isBlank(unitLinkmans.get(0).getIsBindAccount())?"0":unitLinkmans.get(0).getIsBindAccount());
        return new ContentResultForm<>(true,aeaLinkmanInfo) ;
    }

    @GetMapping("linkman/list/key")
    @ApiOperation(value = "根据姓名，电话，身份证号等关键字查询联系人列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "姓名，电话，身份证号等关键字",name = "keyword",required = false,dataType = "string"),
            @ApiImplicitParam(value = "单位信息ID",name = "unitInfoId",required = false,dataType = "string")})
    public ResultForm getByIdCard (String keyword, String unitInfoId) throws Exception {
        List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoService.getByKeyword(keyword,unitInfoId);
        return new ContentResultForm<>(true,aeaLinkmanInfos.stream().map(AeaLinkmanInfoVo::build).collect(Collectors.toList())) ;
    }

    /*******************************************企业中心模块数据请求结束*****************************************************/



}
