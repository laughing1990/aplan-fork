package com.augurit.aplanmis.common.apply.item;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomAeaDicRegionMapper {

    @Select("<script>" +
            " select region_id regionId, region_seq regionSeq " +
            " from bsc_dic_region where REGION_ID in" +
            " <foreach collection='regionIds' open='(' item='regionId' separator=',' close=')'>#{regionId}</foreach>" +
            " </script>")
    List<RegionVo> listRegionVo(@Param("regionIds") List<String> regionIds);

    @Setter
    @Getter
    class RegionVo {
        private String regionId;

        private String regionSeq;
    }
}
