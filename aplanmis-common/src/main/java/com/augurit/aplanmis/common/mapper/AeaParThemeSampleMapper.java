package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParThemeSample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 主题样本表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-09-11 14:19:07</li>
 * </ul>
 */
@Mapper
public interface AeaParThemeSampleMapper {

    void insertAeaParThemeSample(AeaParThemeSample aeaParThemeSample);

    void updateAeaParThemeSample(AeaParThemeSample aeaParThemeSample);

    void deleteAeaParThemeSample(@Param("id") String id);

    List<AeaParThemeSample> listAeaParThemeSample(AeaParThemeSample aeaParThemeSample);

    AeaParThemeSample getAeaParThemeSampleById(@Param("id") String id);

    void batchDelThemeSampleByIds(@Param("ids") String[] ids);
}
