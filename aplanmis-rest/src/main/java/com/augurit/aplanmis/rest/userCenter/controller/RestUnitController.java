package com.augurit.aplanmis.rest.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.rest.userCenter.service.RestUnitService;
import com.augurit.aplanmis.rest.userCenter.vo.UnitAddVo;
import com.augurit.aplanmis.rest.userCenter.vo.UnitEditVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "编辑企业单位")
    @PostMapping("/edit")
    public ContentResultForm<String> edit(UnitEditVo unitEditVo) {
        Assert.isTrue(StringUtils.isNotBlank(unitEditVo.getUnitInfoId()), "unitInfoId is null");
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

}
