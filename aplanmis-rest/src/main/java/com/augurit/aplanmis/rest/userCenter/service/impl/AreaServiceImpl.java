package com.augurit.aplanmis.rest.userCenter.service.impl;

import com.augurit.aplanmis.common.service.area.SyncAreaCodeJob;
import com.augurit.aplanmis.common.vo.AreaVo;
import com.augurit.aplanmis.common.vo.CityVo;
import com.augurit.aplanmis.common.vo.ProvinceVo;
import com.augurit.aplanmis.rest.userCenter.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    SyncAreaCodeJob syncAreaCodeJob;

    @Override
    public List<ProvinceVo> queryProvinceData() {
        List<ProvinceVo> provinceList = syncAreaCodeJob.getSyncAreaCodeData();

        List<ProvinceVo> list = new ArrayList<>();
        if (provinceList != null) {
            for (ProvinceVo pro : provinceList) {
                ProvinceVo province = new ProvinceVo();
                province.setCode(pro.getCode());
                province.setName(pro.getName());
                list.add(province);
            }
        }

        return list;
    }

    @Override
    public List<CityVo> queryCityData(String provinceCode) {
        List<CityVo> cityList = syncAreaCodeJob.getCityData(provinceCode);

        List<CityVo> list = new ArrayList<>();
        if (cityList != null) {
            for (CityVo cy : cityList) {
                CityVo city = new CityVo();
                city.setCode(cy.getCode());
                city.setName(cy.getName());
                list.add(city);
            }
        }
        return list;
    }

    @Override
    public List<AreaVo> queryAreaData(String cityCode) {

        List<AreaVo> areaList = syncAreaCodeJob.getAreaData(cityCode);
        List<AreaVo> list = new ArrayList<>();
        if (areaList != null) {
            for (AreaVo ar : areaList) {
                AreaVo area = new AreaVo();
                area.setCode(ar.getCode());
                area.setName(ar.getName());
                list.add(area);
            }
        }
        return list;
    }
}
