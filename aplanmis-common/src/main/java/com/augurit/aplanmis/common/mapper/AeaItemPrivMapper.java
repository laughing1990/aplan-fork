package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemPriv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人：yinlf</li>
 * <li>创建时间：2019-07-25 14:14:42</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaItemPrivMapper {

    void insertAeaItemPriv(AeaItemPriv aeaItemPriv);

    void updateAeaItemPriv(AeaItemPriv aeaItemPriv);

    void deleteAeaItemPriv(@Param("id") String id);

    List<AeaItemPriv> listAeaItemPriv(AeaItemPriv aeaItemPriv);

    AeaItemPriv getAeaItemPrivById(@Param("id") String id);

    List<AeaItemPriv> findItemPrivByRegionIdAndItemVerId(@Param("regionId") String regionId,
                                                         @Param("itemVerIds") String[] itemVerIds);

    void deleteAeaItemPrivByItemVerIdAndRegionIdAndOrgId(@Param("regionId") String regionId,
                                                         @Param("orgId") String orgId,
                                                         @Param("itemVerId") String itemVerIds);

    /**
     *
     * @param itemVerId
     * @param keyword
     * @return
     */
    List<AeaItemPriv> listPrivListByItemVerId(@Param("itemVerId") String itemVerId,
                                             @Param("keyword") String keyword);

}