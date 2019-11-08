package com.augurit.aplanmis.front.subject.linkman.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.subject.linkman.serivce.RestLinkmanService;
import com.augurit.aplanmis.front.subject.linkman.vo.*;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/rest/linkman")
@Api(tags = "申报-联系人")
public class RestLinkmanController {

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private RestLinkmanService restLinkmanService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @ApiOperation(value = "根据联系人名称模糊查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "企业id", dataType = "String"),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "String")
    })
    @GetMapping("/list")
    public ContentResultForm<Set<LinkmanVo>> list(@RequestParam String keyword, @RequestParam(required = false) String unitInfoId, String projInfoId) {
        Set<LinkmanVo> linkmanVos = aeaLinkmanInfoService.findLinkmanInfoByKeyword(keyword)
                .stream()
                .map(LinkmanVo::from)
                .collect(Collectors.toSet());
        // 企业单位联系人
        if (StringUtils.isNotBlank(unitInfoId)) {
            Set<LinkmanVo> unitLinkVos = aeaLinkmanInfoService.findAllUnitLinkman(unitInfoId)
                    .stream()
                    .map(LinkmanVo::from)
                    .collect(Collectors.toSet());
            linkmanVos.retainAll(unitLinkVos);
        }
        if (StringUtils.isNotBlank(projInfoId)) {
            String[] unitInfoIds = aeaUnitInfoService.findAllProjUnit(projInfoId).stream().map(AeaUnitInfo::getUnitInfoId).toArray(String[]::new);

            for (String unitId : unitInfoIds) {
                Set<LinkmanVo> unitLinkVos = aeaLinkmanInfoService.findAllUnitLinkman(unitId)
                        .stream()
                        .map(LinkmanVo::from)
                        .collect(Collectors.toSet());
                linkmanVos.retainAll(unitLinkVos);
            }
        }
        return new ContentResultForm<>(true, linkmanVos, "Query linkman success.");
    }

    @ApiOperation(value = "编辑联系人", httpMethod = "POST")
    @PostMapping("/edit")
    public ContentResultForm<String> edit(LinkmanEditVo linkmanEditVo) {
        Assert.isTrue(StringUtils.isNotBlank(linkmanEditVo.getLinkmanInfoId()), "linkmanInfoId is null");
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(linkmanEditVo.getLinkmanInfoId());
        if (aeaLinkmanInfo == null) {
            throw new RuntimeException("AeaLinkmanInfo not found, linkmanInfoId: " + linkmanEditVo.getLinkmanInfoId());
        }
        aeaLinkmanInfoService.updateAeaLinkmanInfo(linkmanEditVo.mergeAeaLinkmanInfo(aeaLinkmanInfo));
        return new ContentResultForm<>(true, linkmanEditVo.getLinkmanInfoId(), "Edit linkman success");
    }

    @ApiOperation(value = "新增联系人", httpMethod = "POST")
    @PostMapping("/save")
    public ContentResultForm<String> save(LinkmanAddVo linkmanAddVo) {
        String linkmanInfoId = restLinkmanService.save(linkmanAddVo);
        return new ContentResultForm<>(true, linkmanInfoId, "Save linkman success");
    }

    @GetMapping("/one/{linkmanInfoId}")
    @ApiOperation(value = "根据id获取联系人信息", httpMethod = "GET")
    @ApiImplicitParam(name = "linkmanInfoId", value = "联系人id", required = true, dataType = "String")
    public ContentResultForm<LinkmanInfoVo> one(@PathVariable String linkmanInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(linkmanInfoId), "linkmanInfoId is null");
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(linkmanInfoId);
        LinkmanInfoVo one = LinkmanInfoVo.fromAeaLinkmanInfo(aeaLinkmanInfo);
        return new ContentResultForm<>(true, one, "Query success");
    }

    @ApiOperation(value = "个人申报时, 新增或者更新申请人和联系人", httpMethod = "POST")
    @PostMapping("/save/personal")
    public ContentResultForm<PersonalResultVo> savePersonalInfo(@Valid @RequestBody PersonalInfoListVo personalInfoListVo) {
        PersonalResultVo personalResultVo = restLinkmanService.saveOrUpdatePersonalInfo(personalInfoListVo.getPersonalInfos());
        return new ContentResultForm<>(true, personalResultVo, "Save or update success");
    }

    @ApiOperation(value = "删除企业联系人", httpMethod = "POST")
    @PostMapping("/delete/by/unit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人id", required = true, dataType = "String")
            , @ApiImplicitParam(name = "unitInfoId", value = "企业单位id", required = true, dataType = "String")
    })
    public ContentResultForm<String> deleteLinkmanInfoByUnitId(@RequestParam String linkmanInfoId, @RequestParam String unitInfoId) {
        restLinkmanService.deleteLinkmanInfoByUnitId(linkmanInfoId, unitInfoId);
        return new ContentResultForm<>(true, linkmanInfoId, "Delete linkmanInfo by unitInfoId success");
    }
}
