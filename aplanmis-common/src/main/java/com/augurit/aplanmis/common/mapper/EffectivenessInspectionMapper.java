package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.EffectInspectForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EffectivenessInspectionMapper {
    //预警办件
    public List<EffectInspectForm> yuJingBanJianList(@Param("keyword") String keyword);
    public Integer yuJingBanJianCount();

    //逾期办件
    public List<EffectInspectForm> yuQiBanJianList(@Param("keyword") String keyword);
    public Integer yuQiBanJianCount();

    //网上待预审
    public List<EffectInspectForm> wangShangDaiYuShenList(@Param("keyword") String keyword);
    public Integer wangShangDaiYuShenCount();

    //材料待补全
    public List<EffectInspectForm> caiLiaoBuQuanBanJianList(@Param("keyword") String keyword);
    public Integer caiLiaoBuQuanBanJianCount();

    //待过程补正
    public List<EffectInspectForm> daiGuoChengBuZhengBanJianList(@Param("keyword") String keyword);
    public Integer daiGuoChengBuZhengBanJianCount();
}
