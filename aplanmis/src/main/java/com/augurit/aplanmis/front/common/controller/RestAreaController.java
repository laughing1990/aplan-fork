package com.augurit.aplanmis.front.common.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.service.area.SyncAreaCodeJob;
import com.augurit.aplanmis.common.vo.AreaVo;
import com.augurit.aplanmis.common.vo.CityVo;
import com.augurit.aplanmis.common.vo.ProvinceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/apply")
@Api(tags = "申报-省市区")
public class RestAreaController {

    @Autowired
    private SyncAreaCodeJob syncAreaCodeJob;

    @GetMapping("/province")
    @ApiOperation(("查询所有省"))
    public ContentResultForm<List<ProvinceVo>> getProvince() {
        List<ProvinceVo> provinces = syncAreaCodeJob.getSyncAreaCodeData();
        return new ContentResultForm<>(true, provinces);
    }

    @GetMapping("/cities")
    @ApiOperation(("查询所有城市以及城市下的区"))
    @ApiImplicitParam(name = "provinceCode", value = "省代码", required = true, dataType = "string", paramType = "query", readOnly = true)
    public ContentResultForm<List<CityVo>> getCities(@RequestParam String provinceCode) {
        List<CityVo> cityData = syncAreaCodeJob.getCityData(provinceCode);
        return new ContentResultForm<>(true, cityData);
    }

    @GetMapping("/areas")
    @ApiOperation(("查询某个城市下的区"))
    @ApiImplicitParam(name = "cityCode", value = "城市代码", required = true, dataType = "string", paramType = "query", readOnly = true)
    public ContentResultForm<List<AreaVo>> getAreas(String cityCode) {
        List<AreaVo> areaData = syncAreaCodeJob.getAreaData(cityCode);
        return new ContentResultForm<>(true, areaData);
    }
}
