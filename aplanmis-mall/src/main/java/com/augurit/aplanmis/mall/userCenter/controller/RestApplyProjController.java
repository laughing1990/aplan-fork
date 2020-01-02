package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.sc.scc.runtime.kernal.support.om.OpusOmZtreeNode;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.constants.DicConstants;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.CommonCheckService;
import com.augurit.aplanmis.common.service.admin.opus.AplanmisOpuOmOrgAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeAdminService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApproveService;
import com.augurit.aplanmis.mall.userCenter.service.RestUserCenterService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaProjInfoResultVo;
import com.augurit.aplanmis.mall.userCenter.vo.ApplyDetailVo;
import com.augurit.aplanmis.thirdPlatform.service.ProjectCodeService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  我要申报/已申报项目
 */
@RestController
@RequestMapping("rest/user")
@Api(value = "", tags = "法人空间 --> 我要申报/已申报项目相关接口")
public class RestApplyProjController {
    Logger logger= LoggerFactory.getLogger(RestApplyProjController.class);

    @Autowired
    RestApproveService restApproveService;
    @Autowired
    OpuOmOrgService opuOmOrgService;
    @Autowired
    BscDicCodeService bscDicCodeService;
    @Autowired
    RestUserCenterService restUserCenterService;
    @Autowired
    AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private ProjectCodeService projectCodeService;


    @Autowired
    private AeaParThemeAdminService aeaParThemeAdminService;

    @Autowired
    private AplanmisOpuOmOrgAdminService aplanmisOpuOmOrgAdminService;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private CommonCheckService commonCheckService;
    @Value("${aplanmis.mall.skin:skin_v4.1/}")
    private String skin;


    @GetMapping("todeclarePage")
    @ApiOperation(value = "跳转我要申报页面")
    public ModelAndView toProjListPage(){
        return new ModelAndView("mall/"+skin+"userCenter/components/declare");
    }

    @GetMapping("todeclareHavePage")
    @ApiOperation(value = "跳转已申报项目页面")
    public ModelAndView toHadApplyItemPage(){
        return new ModelAndView("mall/"+skin+"userCenter/components/declareHave");
    }

    @GetMapping("towithdrawApplyListPage")
    @ApiOperation(value = "跳转撤件列表页面")
    public ModelAndView toWithdrawApplyListPage(){
        return new ModelAndView("mall/"+skin+"userCenter/components/withdrawApplyList");
    }

    @GetMapping("toAddProjPage")
    @ApiOperation(value = "跳转新增项目页面")
    public ModelAndView toAddProjPage() {
        return new ModelAndView("mall/"+skin+"userCenter/components/addProj");
    }

    @GetMapping("toApplyDetailPage")
    @ApiOperation(value = "跳转已申报项目详情页")
    public ModelAndView toApplyDetailPage(){
        return new ModelAndView("mall/"+skin+"userCenter/components/lifeCycle");
    }

    @GetMapping("toUploadMatListPage")
    @ApiOperation(value = "跳转已申报项目详情页")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例ID",name = "applyinstId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "任务ID",name = "taskId",required = true,dataType = "string")})
    public ModelAndView toUploadMatListPage(ModelMap modelMap, String applyinstId, String taskId){
        return new ModelAndView("mall/"+skin+"userCenter/components/uploadMatList");
    }


    @GetMapping("proj/list")
    @ApiOperation(value = "我的项目 --> 查询用户项目及子工程列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ResultForm getMyProjList(int pageNum, int pageSize, HttpServletRequest request){
        try {
            List<AeaProjInfo> list = new ArrayList<AeaProjInfo>();
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if (loginInfo == null) return new ResultForm(false, "用户身份信息失效，请重新登录！");
            PageHelper.startPage(pageNum, pageSize);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                list=aeaProjInfoService.findRootAeaProjInfoByLinkmanInfoId(loginInfo.getUserId(),"");
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                list=aeaProjInfoService.findRootAeaProjInfoByLinkmanInfoIdAndUnitInfoId(loginInfo.getUserId(),loginInfo.getUnitId(),"");
            }else{//企业
                list=aeaProjInfoService.findRootAeaProjInfoByUnitInfoId(loginInfo.getUnitId(),"");
            }
            //String[] localCodes = list.size() > 0 ? list.stream().map(AeaProjInfo::getLocalCode).toArray(String[]::new) : new String[0];
            //使用lamada，分页数据会丢失，在lambda之前，先搞一个pageinfo, lambda之后，把分页信息copy进去

           // List<AeaProjInfo> projs = aeaProjInfoService.getProjListAndChildProjsByParent(localCodes) ;
            PageInfo origPageInfo = new PageInfo<>(list);
            if(list.size()==0) return new ContentResultForm<>(true,origPageInfo);
            List<AeaProjInfoResultVo> projsByBuild = list.stream().map(AeaProjInfoResultVo::build)
                    .peek(vo->{
                List<AeaProjInfo> childs = aeaProjInfoService.findChildProj(vo.getProjInfoId());
                //if(childs.size()>0) vo.setHasChildren(true);
                vo.setChildren(childs.size()==0?new ArrayList<>():childs.stream().map(AeaProjInfoResultVo::build).collect(Collectors.toList()));
            })
                    .collect(Collectors.toList());
            PageInfo<AeaProjInfoResultVo> pageInfo = new PageInfo<>(projsByBuild);
            BeanUtils.copyProperties(origPageInfo,pageInfo,"list");

            return new ContentResultForm<>(true,pageInfo);
           } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查询用户项目列表异常");
        }
    }

    @GetMapping("root/proj/list")
    @ApiOperation(value = "我的项目 --> 查询用户根项目列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string"),
            @ApiImplicitParam(value = "搜索关键词",name = "keyword",required = false,dataType = "string")})
    public ResultForm getMyRootProjList(int pageNum, int pageSize,String keyword, HttpServletRequest request){
        try {
            List<AeaProjInfo> list = new ArrayList<AeaProjInfo>();
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if (loginInfo == null) return new ResultForm(false, "用户身份信息失效，请重新登录！");
            PageHelper.startPage(pageNum, pageSize);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                list=aeaProjInfoService.findRootAeaProjInfoByLinkmanInfoId(loginInfo.getUserId(),keyword);
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                list=aeaProjInfoService.findRootAeaProjInfoByLinkmanInfoIdAndUnitInfoId(loginInfo.getUserId(),loginInfo.getUnitId(),keyword);
            }else{//企业
                list=aeaProjInfoService.findRootAeaProjInfoByUnitInfoId(loginInfo.getUnitId(),keyword);
            }
            PageInfo origPageInfo = new PageInfo<>(list);
            return new ContentResultForm<>(true,origPageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查询用户根项目列表异常");
        }
    }

    /**
     * 获取页面所需的数据字典信息
     *
     * @return
     */
    @GetMapping("/getDicContents")
    @ApiOperation("我要申报  --> 获取数据字典集合")
    public ResultForm getAllDicContent() throws Exception {

        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());
        if (topOrg != null) {
            Map result = new HashMap();
            result.put(DicConstants.XM_NATURE, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_NATURE, topOrg.getOrgId()));
            result.put(DicConstants.XM_PROJECT_STEP, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_STEP, topOrg.getOrgId()));
            result.put(DicConstants.XM_PROJECT_LEVEL, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_LEVEL, topOrg.getOrgId()));
            result.put(DicConstants.PROJECT_CLASS, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.PROJECT_CLASS, topOrg.getOrgId()));
            result.put(DicConstants.XM_TZLX, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_TZLX, topOrg.getOrgId()));
            result.put(DicConstants.XM_ZJLY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_ZJLY, topOrg.getOrgId()));
            result.put(DicConstants.XM_GBHY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_GBHY, topOrg.getOrgId()));
            result.put(DicConstants.XM_TDLY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_TDLY, topOrg.getOrgId()));
            result.put(DicConstants.XM_JZXZ, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_JZXZ, topOrg.getOrgId()));
            result.put(DicConstants.XM_GCFL, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_GCFL, topOrg.getOrgId()));
            result.put(DicConstants.PROJ_UNIT_LINKMAN_TYPE, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.PROJ_UNIT_LINKMAN_TYPE, topOrg.getOrgId()));
            result.put(DicConstants.XM_DWLX, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_DWLX, topOrg.getOrgId()));

            return new ContentResultForm<>(true, result);
        }
        return null;
    }


    /**
     * 获取页面所需的数据字典信息
     *
     * @return
     */
    @GetMapping("/getThemes")
    @ApiOperation("我要申报  --> 获取主题列表信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "对应国家标准辅线服务:多评合一（51）、方案联审（52）、联合审图（53）、联合测绘（54C）、联合验收（54Y）",name = "dygjbzfxfw",required = false,dataType = "string")})
    public ResultForm getThemes(String themeType,String dygjbzfxfw) throws Exception {
        if(StringUtils.isBlank(dygjbzfxfw)){
            AeaParTheme aeaParTheme = new AeaParTheme();
            if (StringUtils.isNotBlank(themeType)) {
                aeaParTheme.setThemeType(themeType);
            }
            aeaParTheme.setIsOnlineSb("1");
            List<AeaParTheme> aeaParThemes=aeaParThemeAdminService.listAeaParTheme(aeaParTheme);
            if(aeaParThemes.size()>0){
                aeaParThemes.stream().forEach(theme->{
                    AeaParThemeVer themeVer = null;
                    try {
                        themeVer = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum(theme.getThemeId(),null, SecurityContext.getCurrentOrgId());
                    } catch (Exception e) {
                        logger.error("getThemes 获取主题信息接口查询主题版本异常:"+e.getMessage(),e);
                    }
                    theme.setThemeVerId(themeVer==null?"":themeVer.getThemeVerId());
                });
            }
            return new ContentResultForm<>(true, aeaParThemes);
        }else{
            List<AeaParTheme> aeaParThemes=aeaParThemeService.getTestRunOrPublishedVerAeaParThemeByDygjbzfxfw(dygjbzfxfw,"1");
            return new ContentResultForm<>(true, aeaParThemes);
        }
    }


    /**
     * 获取页面所需的数据字典信息
     *
     * @return
     */
    @GetMapping("/getOrgs")
    @ApiOperation("我要申报  --> 获取部门组织")
    public ResultForm getOrgs(String rootOrgId) throws Exception {
        List<OpusOmZtreeNode> opusOmZtreeNodes=aplanmisOpuOmOrgAdminService.getAllOpuOmOrgZTreeByOrgId(rootOrgId);
        List<OpusOmZtreeNode> oons = new ArrayList<OpusOmZtreeNode>();
        for(OpusOmZtreeNode opusOmZtreeNode:opusOmZtreeNodes){
            if(!rootOrgId.equals(opusOmZtreeNode.getId())){
                oons.add(opusOmZtreeNode);
            }
        }
        return new ContentResultForm<>(true, oons);
    }



    @GetMapping("searchProj/list")
    @ApiOperation(value = "我要申报 --> 查询项目列表(分页)")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字",name = "keyWord",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ResultForm searchProjList(String keyWord, int pageNum, int pageSize, HttpServletRequest request) {
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            PageInfo<AeaProjInfo> pageInfo = restApproveService.findAeaProjInfoByKeyword(keyWord, pageNum, pageSize);
            if (pageInfo.getList().size() == 0 && !keyWord.contains("#") && !keyWord.contains("ZBM") && CommonTools.isComplianceWithRules(keyWord)) {
                //List<AeaProjInfo> list = projectCodeService.getProjInfoFromThirdPlatform(keyWord,loginInfo.getUnitName(),loginInfo.getUnifiedSocialCreditCode());  //正式上线时用
                List<AeaProjInfo> list = projectCodeService.getProjInfoFromThirdPlatform(keyWord, "","");
                pageInfo.setList(list);
            }
            return new ContentResultForm<PageInfo<AeaProjInfo>>(true, pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查询项目列表异常");
        }
    }



    @GetMapping("searchProj/projlist/{keyWord}")
    @ApiOperation(value = "单项/并联申报 --> 查询项目列表下拉框")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字",name = "keyWord",required = false,dataType = "string")})
    public ContentResultForm<List<AeaProjInfoResultVo>> searchProjList(HttpServletRequest request, @PathVariable("keyWord") String keyWord) {
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            List<AeaProjInfo> list = aeaProjInfoService.findAeaProjInfoByKeyword(keyWord);
            if (list.size() == 0 && !keyWord.contains("#") && !keyWord.contains("ZBM") && CommonTools.isComplianceWithRules(keyWord)) {
                //list.addAll(projectCodeService.getProjInfoFromThirdPlatform(keyWord,loginInfo.getUnitName(),loginInfo.getUnifiedSocialCreditCode())); //正式上线时用
                list.addAll(projectCodeService.getProjInfoFromThirdPlatform(keyWord, "",""));
            }
            return new ContentResultForm<List<AeaProjInfoResultVo>>(true, list.size() > 0 ? list.stream().map(AeaProjInfoResultVo::build).collect(Collectors.toList()) : new ArrayList<>());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"查询项目列表异常");
        }
    }

    @GetMapping("projInfo/thirdPlatform/{keyWord}")
    @ApiOperation(value = "项目管理 --> 我的项目:查询监管平台下的项目信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字",name = "keyWord",required = false,dataType = "string")})
    public ContentResultForm<AeaProjInfo> getProjInfoFromThirdPlatform(HttpServletRequest request, @PathVariable("keyWord") String keyWord) {
        try {
            List<AeaProjInfo> list = aeaProjInfoService.findAeaProjInfoByKeyword(keyWord);
            if (list.size()==0&&!keyWord.contains("#") && !keyWord.contains("ZBM") && CommonTools.isComplianceWithRules(keyWord)) {
                list = projectCodeService.getProjInfoFromThirdPlatform(keyWord, "","");
            }
            return new ContentResultForm<>(true,list.get(0));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询项目列表异常");
        }
    }


    @GetMapping("hadApplyItem/list")
    @ApiOperation(value = "已申报项目 --> 已申报项目列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "项目状态(0:办结1:办理中2:草稿)",name = "state",required = false,dataType = "string"),
            @ApiImplicitParam(value = "关键词",name = "keyword",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ResultForm getHadApplyItemlist(String state, String keyword, int pageNum, int pageSize,HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return new ContentResultForm(true,restApproveService.searchApproveProjInfoListByUnitOrLinkman("",loginInfo.getUserId(),state,keyword,pageNum,pageSize));
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return new ContentResultForm(true,restApproveService.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitId(),loginInfo.getUserId(),state,keyword,pageNum,pageSize));
            }else{//企业
                return new ContentResultForm(true,restApproveService.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitId(),"",state,keyword,pageNum,pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查询已申报项目列表查询接口异常");
        }
    }

    @GetMapping("withdrawApply/list")
    @ApiOperation(value = "已申报项目 --> 已申报项目列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键词",name = "keyword",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "string")})
    public ResultForm getWithdrawApplylist(String state, String keyword, int pageNum, int pageSize,HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return new ContentResultForm(true,restApproveService.searchWithdrawApplyListByUnitOrLinkman("",loginInfo.getUserId(),keyword,pageNum,pageSize));
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return new ContentResultForm(true,restApproveService.searchWithdrawApplyListByUnitOrLinkman(loginInfo.getUnitId(),loginInfo.getUserId(),keyword,pageNum,pageSize));
            }else{//企业
                return new ContentResultForm(true,restApproveService.searchWithdrawApplyListByUnitOrLinkman(loginInfo.getUnitId(),"",keyword,pageNum,pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查询已申报项目列表查询接口异常");
        }
    }

    @GetMapping("applydetail/{applyinstId}/{projInfoId}/{isSeriesApprove}")
    @ApiOperation(value = "已申报项目详情信息接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例Id",name = "applyinstId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "项目ID",name = "projInfoId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "1:单项 0:并联",name = "isSeriesApprove",required = true,dataType = "string")})
    public ContentResultForm<ApplyDetailVo> getApplyDetailByApplyinstIdAndProjInfoId(@PathVariable("projInfoId") String projInfoId, @PathVariable("applyinstId") String applyinstId, @PathVariable("isSeriesApprove")String isSeriesApprove, HttpServletRequest request){
        try {
            if (!commonCheckService.isApplyBelong(applyinstId,projInfoId,request)) return new ContentResultForm(false,"","查询出错");
            return new ContentResultForm<>(true,restApproveService.getApplyDetailByApplyinstIdAndProjInfoId(applyinstId,projInfoId,isSeriesApprove,null,request));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询已申报项目详情信息接口发生异常");
        }
    }

    @GetMapping("applydetail/mat/list/{matinstId}")
    @ApiOperation(value = "已申报项目材料列表接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "材料实例Id",name = "matinstId",required = true,dataType = "string")})
    public ContentResultForm<List<BscAttFileAndDir>> getMatAttDetailByMatinstId(@PathVariable("matinstId") String matinstId) {
        try {
            return new ContentResultForm<>(true,restApproveService.getMatAttDetailByMatinstId(matinstId)) ;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询已申报项目材料列表接口发生异常");
        }
    }

    /**********************************************拆分详情请求*****************************************************************/
    @GetMapping("/splitProj")
    @ApiOperation(value = "项目编辑详情页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = false, dataType = "string")})
    public ModelAndView splitProj(ModelMap modelMap, String projInfoId) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "splitProjJsp");
        modelMap.put("projInfo", StringUtils.isNotBlank(projInfoId) ? aeaProjInfoMapper.getOnlyAeaProjInfoById(projInfoId) : new AeaProjInfo());
        modelMap.put("currentProjInfoId", projInfoId);
        return new ModelAndView("mall/"+skin+"userCenter/components/splitProject");
    }

    //查找子项目是否存在，不存在时添加新的子项目，存在时根据子项目添加新的子项目
    @GetMapping("/getChildProject")
    @ApiOperation(value = "获取子项目工程编码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目名称", name = "projName", required = false, dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = false, dataType = "string"),
            @ApiImplicitParam(value = "地方编码", name = "localCode", required = false, dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = false, dataType = "string"),
            @ApiImplicitParam(value = "1:工程规划阶段，2：施工许可阶段", name = "stageFlag", required = false, dataType = "string")})
    public ResultForm getChildProject(String projName, String projInfoId, String localCode, String gcbm,String stageFlag) throws Exception {
        try {
            AeaProjInfo aeaProjInfo = aeaProjInfoService.getChildProject(projName, projInfoId, localCode, gcbm,stageFlag);
            return new ContentResultForm<AeaProjInfo>(true, aeaProjInfo);
        } catch (Exception e) {
            return new ResultForm(false, "无法分离子项目");
        }
    }

    //保存子项目
    @PostMapping("/saveEditedProject")
    @ApiOperation(value = "保存（子）项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目ID", name = "parentProjId", required = false, dataType = "string"),
            @ApiImplicitParam(value = "子项目信息", name = "aeaProjInfo", required = false, dataType = "object")})
    public ResultForm saveChildProject(AeaProjInfo aeaProjInfo, HttpServletRequest request) throws Exception {
        try {
            return new ContentResultForm<>(true, restUserCenterService.saveChildProject(request, aeaProjInfo));
        } catch (Exception e) {
            return new ResultForm(false, "无法保存子项目");
        }
    }

    //查询子项目，根据aeaProjInfoId返回它的子项目列表
    @GetMapping("/getChildProjList")
    @ApiOperation(value = "查询子项目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目Id", name = "aeaProjInfoId", required = false, dataType = "string")})
    public ResultForm getChildProjList(String aeaProjInfoId, Integer pageSize, Integer pageNum) {
        try {
            if (StringUtils.isBlank(aeaProjInfoId))
                return new ResultForm(false, "项目id为空");
//          com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
            PageHelper.startPage(pageNum, pageSize);
            //查询子项目列表
            List<AeaProjInfo> aeaProjChildrenList = aeaProjInfoMapper.getChildProjListById(aeaProjInfoId);

            //返回子项目列表
            return new ContentResultForm<>(true, new PageInfo<AeaProjInfo>(aeaProjChildrenList));
        } catch (Exception e) {
            return new ResultForm(false, "无法查询");
        }
    }


    //------------------保存项目信息--------------
    @PostMapping("/saveProjectInfo")
    @ApiOperation(value = "保存项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目信息", name = "aeaProjInfo", required = false, dataType = "object")})
    public ResultForm saveProjectInfo(AeaProjInfo aeaProjInfo, HttpServletRequest request) {
        try {
            return new ContentResultForm<>(true, restUserCenterService.saveProjectInfo(request, aeaProjInfo));
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
    }


    @PostMapping("/generatorProjectCode")
    @ApiOperation(value = "生成项目自编码")
    public ResultForm generatorProjectCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String num = String.valueOf(date.getTime());
        //zbm表示自编码，r表示root，日期加8位数字（当前时间毫秒值截取后8位）例如：ZBM-R-20181203-56899466
        String code = "ZBM-R-" + sdf.format(date) + "-" + num.substring(num.length() - 8);
        return new ResultForm(true,code);
    }

    //------------------保存项目信息--------------
    @PostMapping("/withDraw/{applyInstId}")
    @ApiOperation(value = "撤回办件")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "applyInstId", name = "申请实例ID", required = true, dataType = "object")})
    public ResultForm withDraw(@PathVariable("applyInstId") String applyInstId) {
        try {
            restUserCenterService.withDrawProject(applyInstId);
            return new ResultForm(true, "操作成功");
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
    }
}
