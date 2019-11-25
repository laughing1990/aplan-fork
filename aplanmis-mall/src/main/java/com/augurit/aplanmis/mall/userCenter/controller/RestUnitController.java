package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.DesensitizedUtil;
import com.augurit.aplanmis.mall.userCenter.service.RestUnitService;
import com.augurit.aplanmis.mall.userCenter.service.RestUserCenterService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaLinkmanInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.UnitAddVo;
import com.augurit.aplanmis.mall.userCenter.vo.UnitEditVo;
import com.augurit.aplanmis.mall.userCenter.vo.UnitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/rest/unit")
@Api(tags = "申报-企业单位")
public class RestUnitController {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private RestUnitService restUnitService;
    @Autowired
    private RestUserCenterService restUserCenterService;

    @ApiOperation(value = "编辑企业单位")
    @PostMapping("/edit")
    public ContentResultForm<String> edit(UnitEditVo unitEditVo) {
        Assert.isTrue(StringUtils.isNotBlank(unitEditVo.getUnitInfoId()) , "unitInfoId is null");
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoService.getAeaUnitInfoByUnitInfoId(unitEditVo.getUnitInfoId());
        if (aeaUnitInfo == null) {
            throw new RuntimeException("AeaUnitInfo not found. unitInfoId: " + unitEditVo.getUnitInfoId());
        }
        aeaUnitInfoService.updateAeaUnitInfo(unitEditVo.mergeAeaUnitInfo(aeaUnitInfo));
        // 更新企业项目关联
        if (StringUtils.isNotBlank(unitEditVo.getProjInfoId())
                && StringUtils.isNotBlank(unitEditVo.getUnitInfoId())
                && StringUtils.isNotBlank(unitEditVo.getLinkmanInfoId())) {
            restUnitService.updateUnitProj(unitEditVo.getProjInfoId(), unitEditVo.getUnitInfoId(), unitEditVo.getLinkmanInfoId());
        }
        return new ContentResultForm<>(true, unitEditVo.getUnitInfoId(), "Edit unit success");
    }


    @ApiOperation(value = "查看联系人")
    @GetMapping("/getLinkmanInfoById/{linkmanInfoId}")
    public ContentResultForm<AeaLinkmanInfoVo> getLinkmanInfoById(@PathVariable("linkmanInfoId") String linkmanInfoId, HttpServletRequest request) throws Exception {
        if (!restUserCenterService.isBelongUnit(linkmanInfoId,request)) return new ContentResultForm(false,null,"查看联系人异常");
        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(linkmanInfoId);
        AeaLinkmanInfoVo aeaLinkmanIanfoVo = new AeaLinkmanInfoVo();
        BeanUtils.copyProperties(linkmanInfo, aeaLinkmanIanfoVo);
        aeaLinkmanIanfoVo.setLinkmanMobilePhone(DesensitizedUtil.desensitizedPhoneNumber(aeaLinkmanIanfoVo.getLinkmanMobilePhone()));
        return new ContentResultForm<>(true,aeaLinkmanIanfoVo, "Save unit success");
    }

    @ApiOperation(value = "新增企业单位")
    @PostMapping("/save")
    public ContentResultForm<String> save(UnitAddVo unitAddVo) throws Exception {
        String unitInfoId = restUnitService.save(unitAddVo);
        return new ContentResultForm<>(true, unitInfoId, "Save unit success");
    }

    @ApiOperation(value = "删除企业单位")
    @PostMapping("/delete")
    public ContentResultForm<String> delete(@RequestParam String unitInfoId, @RequestParam String projInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(unitInfoId), "unitInfoId is null");
        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId is null");
        aeaUnitInfoService.deleteUnitProj(projInfoId, null, unitInfoId);
        return new ContentResultForm<>(true, unitInfoId, "Delete unit success");
    }

    @GetMapping("/list/by/{projectInfoId}")
    @ApiOperation(value = "根据项目ID查找关联的单位列表")
    @ApiImplicitParam(name = "projectInfoId", value = "项目id", required = true, dataType = "String")
    public ContentResultForm<List<UnitVo>> listByProjectInfoId(@PathVariable String projectInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(projectInfoId) , "projectInfoId is null");

        List<UnitVo> unitVos = aeaUnitInfoService.findAllProjUnit(projectInfoId).stream()
                .map(u -> UnitVo.from(u, null)).collect(Collectors.toList());
        return new ContentResultForm<>(true, unitVos, "Query success");
    }

}
