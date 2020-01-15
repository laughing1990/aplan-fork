package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.dto.AeaItemInoutMatDto;
import com.augurit.aplanmis.common.qo.item.ItemMatInoutQo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项输入输出定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemInoutMapper {

     void insertAeaItemInout(AeaItemInout aeaItemInout);

     void updateAeaItemInout(AeaItemInout aeaItemInout) ;

     void deleteAeaItemInoutById(@Param("id") String id) ;

     AeaItemInout getAeaItemInoutById(@Param("id") String id);

     List<AeaItemInout> listAeaItemInout(AeaItemInout aeaItemInout);

     /**
      * 融合三种材料：材料、证照、表单
      *
      * @param aeaItemInout
      * @return
      *
      */
     List<AeaItemInout> listAeaItemInoutRelMat(AeaItemInout aeaItemInout);

     List<AeaItemInout> listAeaItemInoutByItemVerId(AeaItemInout aeaItemInout);

     List<AeaItemInout> listAeaItemInoutAndMat(AeaItemInout aeaItemInout);

     List<AeaItemInout> getAeaItemInoutByMatId(@Param("matId") String matId);

     List<String> getCertIdListByItemVerId(@Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

     List<String> getMatIdListByItemId(@Param("itemId") String itemId);

     List<AeaItemInout> listInStateMatByItemIdAndStateId(@Param("itemId") String itemId,
                                                         @Param("itemStateId") String itemStateId,
                                                         @Param("isInput") String isInput);

     List<String> getGlobalMatListByStateId(@Param("itemVerId") String itemVerId, @Param("itemStateId") String itemStateId);

     List<String> getCertListByStateId(@Param("itemId") String itemId, @Param("itemStateId") String itemStateId);

     /**
      * 获取事项输入输出材料、证照
      *
      * @param aeaItemInout
      * @return
      */
     List<AeaItemInout> listAeaItemInoutMatCert(AeaItemInout aeaItemInout);

     /**
      * 获取事项输入输出材料、证照、表单
      *
      * @param aeaItemInout
      * @return
      */
     List<AeaItemInout> listAeaItemMatCertForm(AeaItemInout aeaItemInout);

     List<AeaItemInout> listMatAndInoutList(ItemMatInoutQo itemMatInoutQo);

     List<AeaItemInout> listInoutByItemId(AeaItemInout aeaItemInout);

    List<AeaItemInout> getAeaItemMatByItemVerIdAndMatIdAndStateId(@Param("itemVerId") String itemVerId, @Param("matId") String matId, @Param("stateId") String stateId);

     void deleteAeaItemInoutByStateId(@Param("itemStateId") String itemStateId,
                                      @Param("itemVerId") String itemVerId,
                                      @Param("stateVerId") String stateVerId,
                                      @Param("rootOrgId") String rootOrgId);

     void batchDeleteAeaItemInoutByIds(@Param("inoutIds") List<String> inoutIds);

     void thoroughDeleteAeaItemInoutByItemId(@Param("itemId") String itemId);

     void thoroughDeleteAeaItemInoutByStateId(@Param("itemId") String itemId, @Param("stateId") String stateId);

     void batchDeleteAeaItemInout(AeaItemInout aeaItemInout);

     List<AeaItemInout> selectOneByMatId(@Param("matId") String matId,
                                         @Param("rootOrgId")String rootOrgId);

     // 查询库材料--事项思维导图所需
     List<AeaItemInout> listStoMatByCondition(AeaItemInout aeaItemInout);

     List<AeaItemInoutMatDto> listInoutAndMatByCriteria(ItemMatInoutQo commonCriteria);

     List<AeaItemInout> listAeaItemInoutByMatIds(@Param("matIds") List<String> matIds, @Param("itemVerId") String itemVerId, @Param("isInput") String isInput);

     /**
      * 获取输入或者输出排序
      *
      * @param itemVerId
      * @param stateVerId
      * @param isInput
      * @return
      */
     Long getItemInoutMaxSortNo(@Param("itemVerId") String itemVerId,
                                @Param("stateVerId") String stateVerId,
                                @Param("isInput") String isInput,
                                @Param("rootOrgId")String rootOrgId);

     /**
      * 获取前置事项输出作为后置事项输入
      * @return
      */
     List<AeaItemInout> getPreItemInItemInout(AeaHiIteminst iteminst);

     /**
      * 获取输入输出批文批复不携带事项信息
      *
      * @param aeaItemInout
      * @return
      */
     List<AeaItemInout> listItemInoutOfficeMat(AeaItemInout aeaItemInout);

     /**
      * 获取输入输出批文批复并携带事项信息,增加了事项去重
      *
      * @param aeaItemInout
      * @return
      */
     List<AeaItemInout> listInoutOfficeMatRelItemInfo(AeaItemInout aeaItemInout);

     /**
      *  获取事项最大可用情形版本存在批文批复输入并携带事项信息,增加了事项去重
      *
      * @param aeaItemInout
      * @return
      */
     List<AeaItemInout> listInOfficeMatRelItemInfo(AeaItemInout aeaItemInout);

    /**
     * 查询单项不分情形下材料定义列表-中介超市用
     *
     * @param itemVerId 事项版本ID
     * @return List<AeaItemMat>
     */
    List<AeaItemMat> getSeriesNoStateMatList(@Param("itemVerId") String itemVerId);
}
