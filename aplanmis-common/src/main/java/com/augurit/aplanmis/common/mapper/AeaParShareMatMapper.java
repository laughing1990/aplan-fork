package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题共享材料表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParShareMatMapper {

     void insertAeaParShareMat(AeaParShareMat aeaParShareMat);

     void updateAeaParShareMat(AeaParShareMat aeaParShareMat);

     void deleteAeaParShareMat(@Param("id") String id);

     List<AeaParShareMat> listAeaParShareMat(AeaParShareMat aeaParShareMat);

     AeaParShareMat getAeaParShareMatById(@Param("id") String id) ;

     void deleteAeaParShareMatByIds(@Param("shareMatIds") String[] shareMatIds);

     List<AeaParShareMat> listAeaParShareMatCascade(AeaParShareMat aeaParShareMat);

     AeaParShareMat getAeaParShareMatCascade(@Param("id") String id);

     Long existShare(@Param("inoutId") String inoutId);

    List<AeaItemInout> listOutItemMat(AeaItemInout aeaItemInout);

    List<AeaItemInout> listInItemMat(AeaItemInout aeaItemInout);

    List<AeaItemBasic> listOutItemTreeByThemeVerId(@Param("themeVerId")String themeVerId,@Param("rootOrgId")String rootOrgId);

    List<AeaItemBasic> listInItemTreeByThemeVerId(@Param("themeVerId")String themeVerId,@Param("rootOrgId")String rootOrgId);

     void saveAeaParShareMat(AeaParShareMat aeaParShareMat);

     List<AeaParShareMat> inOutCheckboxList(@Param("themeVerId")String themeVerId,@Param("outInoutId")String outInoutId);

     AeaItemStateVer getStateByItemVerId(@Param("itemVerId")String itemVerId, @Param("rootOrgId")String rootOrgId);

    List<AeaParShareMat> getAeaParShareMat(AeaParShareMat aeaParShareMat);

    void batchDelAeaParShareMatByCond(AeaParShareMat aeaParShareMat);
}
