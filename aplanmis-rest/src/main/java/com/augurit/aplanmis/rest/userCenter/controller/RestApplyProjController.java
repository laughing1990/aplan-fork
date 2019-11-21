package com.augurit.aplanmis.rest.userCenter.controller;

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
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.admin.opus.AplanmisOpuOmOrgAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeAdminService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.common.utils.SessionUtil;
import com.augurit.aplanmis.rest.common.vo.LoginInfoVo;
import com.augurit.aplanmis.rest.userCenter.service.RestApproveService;
import com.augurit.aplanmis.rest.userCenter.service.RestUserCenterService;
import com.augurit.aplanmis.rest.userCenter.vo.ApplyDetailVo;
import com.augurit.aplanmis.thirdPlatform.service.ProjectCodeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我要申报/已申报项目
 */
@RestController
@RequestMapping("/rest/user")
@Api(value = "", tags = "法人空间 --> 我要申报/已申报项目相关接口")
public class RestApplyProjController {
    Logger logger = LoggerFactory.getLogger(RestApplyProjController.class);

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

    @GetMapping("todeclarePage")
    @ApiOperation(value = "跳转我要申报页面")
    public ModelAndView toProjListPage() {
        return new ModelAndView("mall/userCenter/components/declare");
    }

    @GetMapping("todeclareHavePage")
    @ApiOperation(value = "跳转已申报项目页面")
    public ModelAndView toHadApplyItemPage() {
        return new ModelAndView("mall/userCenter/components/declareHave");
    }

    @GetMapping("toApplyDetailPage")
    @ApiOperation(value = "跳转已申报项目详情页")
    public ModelAndView toApplyDetailPage() {
        return new ModelAndView("mall/userCenter/components/lifeCycle");
    }

    @GetMapping("toUploadMatListPage")
    @ApiOperation(value = "跳转已申报项目详情页")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例ID", name = "applyinstId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "任务ID", name = "taskId", required = true, dataType = "string")})
    public ModelAndView toUploadMatListPage(ModelMap modelMap, String applyinstId, String taskId) {
        return new ModelAndView("mall/userCenter/components/uploadMatList");
    }


    @GetMapping("/proj/list")
    @ApiOperation(value = "我要申报 --> 查询用户项目列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量", name = "pageNum", required = true, dataType = "string"),
            @ApiImplicitParam(value = "页面页数", name = "pageSize", required = true, dataType = "string")})
    public ResultForm getMyProjList(int pageNum, int pageSize) {
        try {
            List<AeaProjInfo> list;
            PageHelper.startPage(pageNum, pageSize);
            if (AuthContext.isPersionAccount()) {//个人
                list = aeaProjInfoService.findAeaProjInfoByLinkmanInfoId(AuthContext.getCurrentLinkmanInfoId());
            } else {//企业
                list = aeaProjInfoService.findAeaProjInfoByUnitInfoId(AuthContext.getCurrentUnitInfoId());
            }
            String[] localCodes = list.size() > 0 ? list.stream().map(AeaProjInfo::getLocalCode).toArray(String[]::new) : new String[0];
            return new ContentResultForm<>(true, new PageInfo<>(localCodes.length > 0 ? aeaProjInfoService.getProjListAndChildProjsByParent(localCodes) : list));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "查询用户项目列表异常");
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
    @ApiOperation("我要申报  --> 获取主题信息")
    public ResultForm getThemes(String themeType) throws Exception {
        AeaParTheme aeaParTheme = new AeaParTheme();
        aeaParTheme.setThemeType(themeType);
        List<AeaParTheme> aeaParThemes = aeaParThemeAdminService.listAeaParTheme(aeaParTheme);
        return new ContentResultForm<>(true, aeaParThemes);
    }


    /**
     * 获取页面所需的数据字典信息
     *
     * @return
     */
    @GetMapping("/getOrgs")
    @ApiOperation("我要申报  --> 获取部门组织")
    public ResultForm getOrgs(String rootOrgId) throws Exception {
        List<OpusOmZtreeNode> opusOmZtreeNodes = aplanmisOpuOmOrgAdminService.getAllOpuOmOrgZTreeByOrgId(rootOrgId);
        List<OpusOmZtreeNode> oons = new ArrayList<OpusOmZtreeNode>();
        for (OpusOmZtreeNode opusOmZtreeNode : opusOmZtreeNodes) {
            if (!rootOrgId.equals(opusOmZtreeNode.getId())) {
                oons.add(opusOmZtreeNode);
            }
        }
        return new ContentResultForm<>(true, oons);
    }


    @GetMapping("searchProj/list")
    @ApiOperation(value = "我要申报 --> 查询项目列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字", name = "keyWord", required = false, dataType = "string"),
            @ApiImplicitParam(value = "页面数量", name = "pageNum", required = true, dataType = "string"),
            @ApiImplicitParam(value = "页面页数", name = "pageSize", required = true, dataType = "string")})
    public ResultForm searchProjList(String keyWord, int pageNum, int pageSize, HttpServletRequest request) {
        try {
            String currentUnitInfoName = AuthContext.getCurrentUnitInfoName();
            PageInfo<AeaProjInfo> pageInfo = restApproveService.findAeaProjInfoByKeyword(keyWord, pageNum, pageSize);
            if (pageInfo.getList().size() == 0 && !keyWord.contains("#") && !keyWord.contains("ZBM") && CommonTools.isComplianceWithRules(keyWord)) {
                List<AeaProjInfo> list = projectCodeService.getProjInfoFromThirdPlatform(keyWord, currentUnitInfoName, null);
                pageInfo.setList(list);
            }
            return new ContentResultForm<>(true, pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "查询项目列表异常");
        }
    }

    @GetMapping("searchProj/projlist/{keyWord}")
    @ApiOperation(value = "我要申报 --> 查询项目列表下拉框")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字", name = "keyWord", required = false, dataType = "string")})
    public ContentResultForm<List<AeaProjInfo>> searchProjList(HttpServletRequest request, @PathVariable("keyWord") String keyWord) {
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            List<AeaProjInfo> list = aeaProjInfoService.findAeaProjInfoByKeyword(keyWord);
            if (list.size() == 0 && !keyWord.contains("#") && !keyWord.contains("ZBM") && CommonTools.isComplianceWithRules(keyWord)) {
                //list.addAll(projectCodeService.getProjInfoFromThirdPlatform(keyWord,loginInfo.getUnitName())); //正式上线时用
                list.addAll(projectCodeService.getProjInfoFromThirdPlatform(keyWord, "", null));
            }
            return new ContentResultForm<List<AeaProjInfo>>(true, list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "查询项目列表异常");
        }
    }


    @GetMapping("hadApplyItem/list")
    @ApiOperation(value = "已申报项目 --> 已申报项目列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "项目状态(0:办结1:办理中2:草稿)", name = "state", required = false, dataType = "string"),
            @ApiImplicitParam(value = "关键词", name = "keyword", required = false, dataType = "string"),
            @ApiImplicitParam(value = "页面数量", name = "pageNum", required = true, dataType = "string"),
            @ApiImplicitParam(value = "页面页数", name = "pageSize", required = true, dataType = "string")})
    public ResultForm getHadApplyItemlist(String state, String keyword, int pageNum, int pageSize, HttpServletRequest request) {
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if ("1".equals(loginInfo.getIsPersonAccount())) {//个人
                return new ContentResultForm<PageInfo<ApproveProjInfoDto>>(true, restApproveService.searchApproveProjInfoListByUnitOrLinkman("", loginInfo.getUserId(), state, keyword, pageNum, pageSize));
            } else if (StringUtils.isNotBlank(loginInfo.getUserId())) {//委托人
                return new ContentResultForm<PageInfo<ApproveProjInfoDto>>(true, restApproveService.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitId(), loginInfo.getUserId(), state, keyword, pageNum, pageSize));
            } else {//企业
                return new ContentResultForm<PageInfo<ApproveProjInfoDto>>(true, restApproveService.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitId(), "", state, keyword, pageNum, pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "查询已申报项目列表查询接口异常");
        }
    }

    @GetMapping("applydetail/{applyinstId}/{projInfoId}/{isSeriesApprove}")
    @ApiOperation(value = "已申报项目详情信息接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例Id", name = "applyinstId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "1:单项 0:并联", name = "isSeriesApprove", required = true, dataType = "string")})
    public ContentResultForm<ApplyDetailVo> getApplyDetailByApplyinstIdAndProjInfoId(@PathVariable("projInfoId") String projInfoId, @PathVariable("applyinstId") String applyinstId, @PathVariable("isSeriesApprove") String isSeriesApprove, HttpServletRequest request) {
        try {
            return new ContentResultForm<>(true, restApproveService.getApplyDetailByApplyinstIdAndProjInfoId(applyinstId, projInfoId, isSeriesApprove, null, request));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "", "查询已申报项目详情信息接口发生异常");
        }
    }

    @GetMapping("applydetail/mat/list/{matinstId}")
    @ApiOperation(value = "已申报项目材料列表接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "材料实例Id", name = "matinstId", required = true, dataType = "string")})
    public ContentResultForm<List<BscAttFileAndDir>> getMatAttDetailByMatinstId(@PathVariable("matinstId") String matinstId) {
        try {
            return new ContentResultForm<>(true, restApproveService.getMatAttDetailByMatinstId(matinstId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "", "查询已申报项目材料列表接口发生异常");
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
        modelMap.put("projInfo", aeaProjInfoMapper.getOnlyAeaProjInfoById(projInfoId));
        modelMap.put("currentProjInfoId", projInfoId);
        return new ModelAndView("mall/userCenter/components/splitProject");
    }

    //查找子项目是否存在，不存在时添加新的子项目，存在时根据子项目添加新的子项目
    @GetMapping("/getChildProject")
    @ApiOperation(value = "获取子项目工程编码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目名称", name = "projName", required = false, dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = false, dataType = "string"),
            @ApiImplicitParam(value = "地方编码", name = "localCode", required = false, dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = false, dataType = "string")})
    public ResultForm getChildProject(String projName, String projInfoId, String localCode, String gcbm) throws Exception {
        try {
            AeaProjInfo aeaProjInfo = aeaProjInfoService.getChildProject(projName, projInfoId, localCode, gcbm);
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
            @ApiImplicitParam(value = "子项目信息", name = "aeaProjInfo", required = false)})
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
}
