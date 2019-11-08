package com.augurit.aplanmis.front.subject.unit.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.subject.unit.service.RestUnitService;
import com.augurit.aplanmis.front.subject.unit.vo.UnitAddVo;
import com.augurit.aplanmis.front.subject.unit.vo.UnitEditVo;
import com.augurit.aplanmis.front.subject.unit.vo.UnitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
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

    @ApiOperation(value = "根据企业单位名称模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", dataType = "String"),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "String")
    })
    @GetMapping("/list")
    public ContentResultForm<Set<UnitVo>> list(@RequestParam(required = false) String keyword, @RequestParam(required = false) String projInfoId) {
        Set<UnitVo> unitsByKeyword = aeaUnitInfoService.findAeaUnitInfoByKeyword(keyword)
                .stream()
                .map(this::getUnitVo).collect(Collectors.toSet());
        /// 先放开，可以根据关键字查询所有单位
        /*if (StringUtils.isNotBlank(projInfoId)) {
            Set<UnitVo> unitVos = aeaUnitInfoService.findAllProjUnit(projInfoId)
                    .stream()
                    .map(this::getUnitVo).collect(Collectors.toSet());
            // 取交集，合并结果
            unitsByKeyword.retainAll(unitVos);
        }*/
        return new ContentResultForm<>(true, unitsByKeyword, "Query success");
    }

    private UnitVo getUnitVo(AeaUnitInfo u) {
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        List<AeaLinkmanInfo> allUnitLinkman = aeaLinkmanInfoService.findAllUnitLinkman(u.getUnitInfoId());
        if (allUnitLinkman.size() > 0) {
            aeaLinkmanInfo = allUnitLinkman.get(0);
        }
        return UnitVo.from(u, aeaLinkmanInfo);
    }

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
            restUnitService.updateUnitProj(unitEditVo.getProjInfoId(), unitEditVo.getUnitInfoId(), unitEditVo.getLinkmanInfoId(), unitEditVo.getUnitType());
        }
        if (unitEditVo.getLinkmanType() != null) {
            restUnitService.updateUnitProjLinkman(unitEditVo);
        }
        return new ContentResultForm<>(true, unitEditVo.getUnitInfoId(), "Edit unit success");
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
