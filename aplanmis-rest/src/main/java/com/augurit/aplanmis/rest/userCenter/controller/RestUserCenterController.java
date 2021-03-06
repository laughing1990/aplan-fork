package com.augurit.aplanmis.rest.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.constant.LoginUserRoleEnum;
import com.augurit.aplanmis.rest.userCenter.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/user")
@Api(tags = "法人空间 -->个人中心相关接口")
public class RestUserCenterController {
    Logger logger = LoggerFactory.getLogger(RestUserCenterController.class);

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;


    /*@GetMapping("/toUserCenterindexPage")
    @ApiOperation(value = "法人空间页面")
    @ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = false, dataType = "string")
    public ModelAndView toUserCenterindexPage(String itemVerId, ModelMap modelMap) {
        modelMap.put("itemVerId", itemVerId);
        return new ModelAndView("mall/userCenter/userCenterIndex");
    }

    @GetMapping("toUserInfoPage")
    @ApiOperation(value = "跳转个人中心信息页面")
    public ModelAndView userInfo() throws Exception {
        return new ModelAndView("mall/userCenter/components/userCenterPage");
    }*/

    @PostMapping("/unitInfo/save")
    @ApiOperation("保存单位信息")
    public ResultForm save(AeaUnitInfo aeaUnitInfo) {
        //要求密码字段MD5加密
        if (StringUtils.isNotBlank(aeaUnitInfo.getUnitInfoId())) {
            aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
        } else {
            aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
        }
        return new ResultForm(true, "success");
    }

    @PostMapping("/linkman/save")
    @ApiOperation("保存联系人信息")
    public ResultForm saveLinkman(AeaLinkmanInfo aeaLinkmanInfo) {
        //要求密码字段MD5加密
        if (StringUtils.isNotBlank(aeaLinkmanInfo.getLinkmanInfoId())) {
            aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
        } else {
            aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
        }
        return new ResultForm(true, "success");
    }

    @PostMapping("/linkmanCascade/save")
    @ApiOperation("保存单位联系人信息及单位与联系人的关系")
    public ResultForm saveLinkmanAndUnitLinkman(AeaLinkmanInfo aeaLinkmanInfo) {
        //要求：登录名，密码字段不用填，unitInfoId不为空
        if (StringUtils.isEmpty(aeaLinkmanInfo.getUnitInfoId())) return new ResultForm(false, "单位为空");
        try {
            if (StringUtils.isNotBlank(aeaLinkmanInfo.getLinkmanInfoId())) {
                aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
            } else {//根据身份证号码查询联系人，若已存在，则更新信息，若不存在，则新增
                AeaLinkmanInfo query = new AeaLinkmanInfo();
                query.setLinkmanCertNo(aeaLinkmanInfo.getLinkmanCertNo());
                List<AeaLinkmanInfo> linkmans = aeaLinkmanInfoService.findLinkmanInfo(query);
                if (linkmans.size() > 0) {
                    aeaLinkmanInfo.setLinkmanInfoId(linkmans.get(0).getLinkmanInfoId());
                    aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
                } else {
                    aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
                }
            }
            //先查询单位与联系人的关系是否存在，若存在则无需保存，若不存在，则新增
            AeaUnitLinkman param = new AeaUnitLinkman();
            param.setUnitInfoId(aeaLinkmanInfo.getUnitInfoId());
            param.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            List<AeaUnitLinkman> unitLinkmans = aeaUnitLinkmanMapper.listAeaUnitLinkman(param);
            if (unitLinkmans.size() == 0) {
                param.setIsBindAccount(aeaLinkmanInfo.getIsBindAccount());
                param.setUnitLinkmanId(UUID.randomUUID().toString());
                param.setCreater(AuthContext.getCurrentUnitInfoId() == null ? AuthContext.getCurrentLinkmanInfoId() : AuthContext.getCurrentUnitInfoId());
                param.setCreateTime(new Date());
                param.setIsAdministrators("0");//不是管理员
                aeaUnitLinkmanMapper.insertAeaUnitLinkman(param);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "保存联系人信息失败");
        }
        return new ResultForm(true, "success");
    }

    //委托人管理-绑定/解绑单位
    @PostMapping("/bindRelation/update")
    @ApiOperation(value = "委托人管理-绑定/解绑单位")
    @ApiImplicitParams({@ApiImplicitParam(value = "单位Id", name = "unitInfoId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "联系人Id", name = "linkmanInfoId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "绑定关系0:解绑，1:绑定", name = "isBindAccount", required = true, dataType = "string")})
    public ResultForm bindOrUnbindRelation(String unitInfoId, String linkmanInfoId, String isBindAccount) {
        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        aeaUnitLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaUnitLinkman.setUnitInfoId(unitInfoId);
        //isBindAccount=0解绑  isBindAccount=1绑定
        aeaUnitLinkman.setIsBindAccount(isBindAccount);
        try {
            aeaLinkmanInfoService.updateAeaUnitLinkmanByUnitAndLinkman(aeaUnitLinkman);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
        return new ResultForm(true, "success");
    }

    //删除委托人信息
    @DeleteMapping("/linkman/delete/{unitInfoId}/{linkmanInfoId}")
    @ApiOperation(value = "删除委托人信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "单位id", name = "unitInfoId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "联系人id", name = "linkmanInfoId", required = true, dataType = "string")})
    public ResultForm deleteRelation(@PathVariable("unitInfoId") String unitInfoId, @PathVariable("linkmanInfoId") String linkmanInfoId) {
        aeaLinkmanInfoService.deleteUnitLinkman(unitInfoId, linkmanInfoId);
        return new ResultForm(true, "success");
    }

    //查询所有人或者委托人列表
    @GetMapping("/linkman/list")
    @ApiOperation(value = "查询所有人或者委托人列表")
    @ApiImplicitParam(value = "空值:查询所有人，1:查询委托人", name = "isAll", required = true, dataType = "string")
    public ResultForm getAllUnitLinkInfo(String isAll) throws Exception {
        AuthUser user = AuthContext.getCurrentUser();
        List<AeaLinkmanInfo> linkmanInfoList = new ArrayList<>();
        if (user != null) {
            linkmanInfoList = aeaLinkmanInfoService.getAeaLinkmanInfoByUnitInfoIdAndIsBindAccount(user.getUnitInfoId(), isAll);
        }
        return new ContentResultForm<>(true, linkmanInfoList);
    }

    @GetMapping("/userinfo")
    @ApiOperation(value = "获取当前登录用户详情")
    public ResultForm getUserInfo() {
        UserInfoVo userInfoVo = new UserInfoVo();
        AuthUser user = AuthContext.getCurrentUser();
        if (user != null) {
            if (user.isPersonalAccount()) {//个人
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(user.getUnitInfoId());
                userInfoVo.setAeaLinkmanInfo(aeaLinkmanInfo);
                userInfoVo.setRole(LoginUserRoleEnum.PERSONAL.getValue());
            } else if (StringUtils.isNotBlank(user.getLinkmanInfoId())) {//委托人
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(user.getLinkmanInfoId());
                List<AeaUnitInfo> aeaUnitList = aeaUnitInfoService.getUnitInfoByLinkmanInfoId(user.getLinkmanInfoId());
                userInfoVo.setAeaUnitList(aeaUnitList);
                userInfoVo.setAeaLinkmanInfo(aeaLinkmanInfo);
                userInfoVo.setRole(LoginUserRoleEnum.HANDLE.getValue());
            } else {//企业
                AeaUnitInfo aeaUnitInfo = aeaUnitInfoService.getAeaUnitInfoByUnitInfoId(user.getUnitInfoId());
                List<AeaLinkmanInfo> linkmanInfoList = aeaLinkmanInfoService.findAllUnitLinkman(user.getUnitInfoId());
                userInfoVo.setLinkmanInfoList(linkmanInfoList);
                userInfoVo.setAeaUnitInfo(aeaUnitInfo);
                userInfoVo.setRole(LoginUserRoleEnum.UNIT.getValue());
            }
        }
        return new ContentResultForm<>(true, userInfoVo);
    }

    @GetMapping("/unitinfo/list/{linkmanInfoId}")
    @ApiOperation(value = "查询联系人单位列表")
    @ApiImplicitParam(value = "联系人id", name = "linkmanInfoId", required = true, dataType = "string")
    public ResultForm getUnitInfoByLinkmanInfoId(@PathVariable("linkmanInfoId") String linkmanInfoId) {
        return new ContentResultForm<>(true, aeaUnitInfoService.getUnitInfoByLinkmanInfoId(linkmanInfoId));
    }

    @GetMapping("linkman/{linkmanInfoId}/{unitInfoId}")
    @ApiOperation(value = "查询联系人信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "单位id", name = "unitInfoId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "联系人id", name = "linkmanInfoId", required = true, dataType = "string")})
    public ResultForm getByLinkmanInfoId(@PathVariable("linkmanInfoId") String linkmanInfoId, @PathVariable("unitInfoId") String unitInfoId) {
        if (StringUtils.isBlank(linkmanInfoId)) return new ResultForm(false, "联系人参数缺失");
        if (StringUtils.isBlank(unitInfoId)) return new ResultForm(false, "单位参数缺失");
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(linkmanInfoId);
        AeaUnitLinkman query = new AeaUnitLinkman();
        query.setUnitInfoId(unitInfoId);
        query.setLinkmanInfoId(linkmanInfoId);
        List<AeaUnitLinkman> unitLinkmans;
        try {
            unitLinkmans = aeaUnitLinkmanMapper.listAeaUnitLinkman(query);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "查找单位下的联系人信息失败");
        }
        if (unitLinkmans.size() > 0)
            aeaLinkmanInfo.setIsBindAccount(StringUtils.isBlank(unitLinkmans.get(0).getIsBindAccount()) ? "0" : unitLinkmans.get(0).getIsBindAccount());
        return new ContentResultForm<>(true, aeaLinkmanInfo);
    }

    @GetMapping("/linkman/list/key")
    @ApiOperation(value = "根据姓名，电话，身份证号等关键字查询联系人列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "姓名，电话，身份证号等关键字", name = "keyword", dataType = "string"),
            @ApiImplicitParam(value = "单位信息ID", name = "unitInfoId", dataType = "string")})
    public ResultForm getByIdCard(@RequestParam(required = false) String keyword, String unitInfoId) throws Exception {
        return new ContentResultForm<>(true, aeaLinkmanInfoService.getByKeyword(keyword, unitInfoId));
    }
}
