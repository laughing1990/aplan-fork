package com.augurit.aplanmis.province.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.agcloud.opus.common.service.om.OpuOmUserService;
import com.augurit.aplanmis.common.domain.AeaMatinst;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.province.auth.AesUtil;
import com.augurit.aplanmis.province.auth.AuthConfig;
import com.augurit.aplanmis.province.service.ApproveCommonService;
import com.augurit.aplanmis.province.service.ApproveItemService;
import com.augurit.aplanmis.province.service.ApproveMatService;
import com.augurit.aplanmis.province.service.ApproveProcessService;
import com.augurit.aplanmis.province.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/province")
@Api(value = "省平台接口", description = "省平台接口")
public class ApproveItemController {
    @Autowired
    private ApproveCommonService approveCommonService;

    @Autowired
    private ApproveItemService approveItemService;

    @Autowired
    private ApproveMatService approveMatService;

    @Autowired
    private ApproveProcessService approveProcessService;


    @Value("${top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;

    @Value("${agcloud.framework.sso.sso-server-url}")
    private String ssoServerUrl;

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpuOmOrgService opuOmOrgService;
    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private OpuOmUserService opuOmUserService;


    /**
     * 城市工程建设项目审批管理系统身份认证系统接口
     * 用户名密码由本系统提供
     *
     * @param user     用户名
     * @param password 密码
     * @return
     */
    @GetMapping("/authentication")
    @ApiOperation(value = "城市工程建设项目审批管理系统身份认证系统接口", notes = "城市工程建设项目审批管理系统身份认证系统接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "user", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")})
    public AccessTokenVo authentication(String user, String password) {
        AccessTokenVo result = new AccessTokenVo();
        //检验空值
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
            //1、使用sso的方式，因为启用sso的话，url中带access_token参数会默认被sso拦截，所以必须有一个正式的获取token的过程
            String token = "";
            if (authConfig.isUseSso()) {
                JSONObject ssoToken = getSsoToken(user, password);
                if (ssoToken != null && ssoToken.getBoolean("success")) {
                    token = ssoToken.getJSONObject("content").getString("access_token");
                    Long expire = ssoToken.getJSONObject("content").getLong("expires_in");
                    //将token缓存到redis，有效期跟token的有效期一致
                    stringRedisTemplate.opsForValue().set(token, token, expire, TimeUnit.SECONDS);
                }
            } else {
                if (user.equals(authConfig.getUser()) && password.equals(authConfig.getPassword())) {
                    //使用账号+密码作为加密的公钥
                    String key = user + "-" + password;
                    //生成随机key
                    String tokenStr = UUID.randomUUID().toString();
                    //加密随机key生成token
                    token = AesUtil.encrypt(tokenStr, key);
                    //将token缓存到redis中，暂定有效期30分钟
                    stringRedisTemplate.opsForValue().set(token, tokenStr, 30, TimeUnit.MINUTES);
                }
            }
            //构建返回结果，规范根据对接标准封装
            result.setResult(true);
            result.setAccess_token(token);
            return result;
        }
        //校验失败返回结果
        result.setResult(false);
        result.setAccess_token("");
        return result;
    }

    /**
     * 处理组织，只返回用户所属的顶级机构
     *
     * @param opuOmOrgListDb
     * @return
     */
    public List<String> handleTopOrgId(List<OpuOmOrg> opuOmOrgListDb) {

        //处理全部的顶级组织（去重），登录只选择顶级组织
        Map<String, String> orgMap = new HashMap<>();
        for (OpuOmOrg opuOmOrg : opuOmOrgListDb) {
            String topOrgId = opuOmOrg.getOrgSeq().split("\\.")[1];
            orgMap.put(topOrgId, "");
        }
        //处理返回的顶级组织
        List<String> opuOmOrgList = new ArrayList<>();
        for (String key : orgMap.keySet()) {
            opuOmOrgList.add(key);
        }
        return opuOmOrgList;
    }

    private JSONObject getSsoToken(String username, String password) {
        String orgId = "0368948a-1cdf-4bf8-a828-71d796ba89f6";
        List<OpuOmOrg> orgs = opuOmOrgService.getOpuOmUserOrg(username);
//        List<OpuOmOrg> orgs = opuOmOrgService.listOpuOmUserOrgForLogin(username, password);
        if (orgs != null && orgs.size() > 0) {
            List<String> orgIds = handleTopOrgId(orgs);
            if (orgIds.size() > 0) {
                orgId = orgIds.get(0);
            }
        }
        String url = ssoServerUrl + "/authentication/form";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", authConfig.getAuthorization());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("orgId", orgId);
        map.add("deviceType", "normal");
        map.add("isApp", "true");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
        if (jsonObjectResponseEntity.getStatusCode().value() == 200) {
            return jsonObjectResponseEntity.getBody();
        } else {
            return null;
        }
    }

    /**
     * 审批详情页面服务接口
     *
     * @param project_code       等价 localCode/CENTRAL_CODE
     * @param item_instance_code 等价 iteminstId
     * @return
     */
    @GetMapping("/getApproveItem")
    @ApiOperation(value = "审批详情页面服务接口", notes = "审批详情页面服务接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "project_code", value = "工程代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "item_instance_code", value = "审批事项实例编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "国家工程建设项目审批管理系统、省级工程建设项目审批管理系统调用城市工程建设项目审批管理系统身份认证服务接口获取", required = true, dataType = "String")})
    public ModelAndView getApproveItem(String project_code, String item_instance_code, String access_token, ModelMap modelMap) {
        modelMap.put("proj_name", approveCommonService.getProjNameByProjCode(project_code));
        modelMap.put("project_code", project_code);
        modelMap.put("item_instance_code", item_instance_code);
        modelMap.put("access_token", access_token);
        return new ModelAndView("approveDetails/index");
    }

    @GetMapping("/getProjectDoc")
    @ApiOperation(value = "电子文档访问服务接口", notes = "电子文档访问服务接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "doc_id", value = "下载的附件在城市工程建设项目审批管理系统中的唯一附件 ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "国家工程建设项目审批管理系统、省级工程建设项目审批管理系统调用城市工程建设项目审批管理系统身份认证服务接口获取", required = true, dataType = "String")})
    public ResultForm mainIndex(String doc_id, String access_token, HttpServletResponse response, HttpServletRequest request) {
        return this.downOneFileByFileId(doc_id, response, request);
    }

    @GetMapping("/getMatintList")
    @ApiOperation(value = "获取材料列表接口", notes = "获取材料列表接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "project_code", value = "工程代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "item_instance_code", value = "审批事项实例编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "国家工程建设项目审批管理系统、省级工程建设项目审批管理系统调用城市工程建设项目审批管理系统身份认证服务接口获取", required = true, dataType = "String")})
    public ResultForm getMatintList(String project_code, String item_instance_code) throws Exception {
        AeaMatinstAndIteminstVo applyMatinstList = approveMatService.getApplyMatinstList(project_code, item_instance_code);
        return new ContentResultForm<>(true, applyMatinstList);
    }

    @GetMapping("/listHistoryApproveProcess")
    @ApiOperation(value = "获取审批办理过程接口", notes = "获取审批办理过程接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "project_code", value = "工程代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "item_instance_code", value = "审批事项实例编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "国家工程建设项目审批管理系统、省级工程建设项目审批管理系统调用城市工程建设项目审批管理系统身份认证服务接口获取", required = true, dataType = "String")})
    public ResultForm listHistoryApproveProcess(String project_code, String item_instance_code, String access_token) {
        try {
            List<HistoryProcessVo> list = approveProcessService.listHistoryApproveProcess(project_code, item_instance_code);
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            e.getStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/listParallelApproveData")
    @ApiOperation(value = "获取并联审批办理情况汇总接口", notes = "获取并联审批办理情况汇总接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "project_code", value = "工程代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "item_instance_code", value = "审批事项实例编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "国家工程建设项目审批管理系统、省级工程建设项目审批管理系统调用城市工程建设项目审批管理系统身份认证服务接口获取", required = true, dataType = "String")})
    public ResultForm listParallelApproveData(String project_code, String item_instance_code, String access_token) {
        try {
            List<ParallelApproveDataVo> list = approveProcessService.listParallelApproveData(project_code, item_instance_code);
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            e.getStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    /**
     * matinstId 与 item_instance_code不能同时存在，如果存在默认下载所有
     *
     * @param matinstId          下载指定材料实例下的所有附件，
     * @param item_instance_code 下载单事项或并联审批下所有的文件
     * @return 下载文件
     */
    @GetMapping("/downFile")
    @ApiOperation(value = "载指定材料实例下的所有附件|下载单事项或并联审批下所有的文件，", notes = "载指定材料实例下的所有附件|下载单事项或并联审批下所有的文件", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "matinstId", value = "材料实例ID", dataType = "String"),
            @ApiImplicitParam(name = "item_instance_code", value = "审批事项实例编码", dataType = "String")})
    public ResultForm downFile(String matinstId, String item_instance_code, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!StringUtils.isEmpty(item_instance_code)) {
                List<AeaMatinst> matinstListByIteminstId = approveMatService.getMatinstListByIteminstId(item_instance_code);
                if (matinstListByIteminstId.size() > 0) {
                    matinstId = matinstListByIteminstId.stream().map(AeaMatinst::getMatinstId).collect(Collectors.joining(","));
                }
            }
            List<BscAttFileAndDir> matAttDetail = approveMatService.getMatAttDetail(matinstId);
            if (matAttDetail.size() > 0) {
                String[] detailIds = matAttDetail.stream().map(BscAttFileAndDir::getFileId).toArray(String[]::new);
                if (fileUtilsService.downloadAttachment(detailIds, response, request, false)) {
                    return new ResultForm(true, "success");
                } else {
                    return new ResultForm(false, "附件不存在");
                }
            } else {
                return new ResultForm(false, "无附件");
            }
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/getBaseInfo")
    @ApiOperation(value = "获取申办事项基本信息接口", notes = "获取申办事项基本信息接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "project_code", value = "工程代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "item_instance_code", value = "审批事项实例编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "国家工程建设项目审批管理系统、省级工程建设项目审批管理系统调用城市工程建设项目审批管理系统身份认证服务接口获取", required = true, dataType = "String")})
    public ResultForm getBaseInfo(String project_code, String item_instance_code) {
        try {
            ProviceBaseInfoVo vo = approveItemService.getProvinceBaseInfo(project_code, item_instance_code);
            return new ContentResultForm<>(true, vo, "success");
        } catch (Exception e) {
            e.getStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/downOneFileByFileId")
    @ApiOperation(value = "根据附件ID下载文件", notes = "根据附件ID下载文件", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileId", value = "文件ID", dataType = "String")})
    public ResultForm downOneFileByFileId(String fileId, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (fileUtilsService.downloadAttachment(fileId, response, request, false)) {
                return new ResultForm(true, "success");
            } else {
                return new ResultForm(false, "fail");
            }
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
    }
}
