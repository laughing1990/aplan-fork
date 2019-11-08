package com.augurit.aplanmis.rest.userCenter.service;

import com.augurit.aplanmis.common.vo.AreaVo;
import com.augurit.aplanmis.common.vo.CityVo;
import com.augurit.aplanmis.common.vo.ProvinceVo;

import java.util.List;

public interface AreaService {

    List<ProvinceVo> queryProvinceData() throws Exception;

    List<CityVo> queryCityData(String provinceCode) throws Exception;

    List<AreaVo> queryAreaData(String cityCode) throws Exception;
}
