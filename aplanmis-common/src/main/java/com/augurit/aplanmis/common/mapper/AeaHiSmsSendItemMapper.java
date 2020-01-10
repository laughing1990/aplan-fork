package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaHiSmsSendItemMapper {

    void insertAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;

    void updateAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;

    void deleteAeaHiSmsSendItem(@Param("id") String id) throws Exception;

    List<AeaHiSmsSendItem> listAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;

    AeaHiSmsSendItem getAeaHiSmsSendItemById(@Param("id") String id) throws Exception;

    List<AeaHiSmsSendItem> getAeaHiSmsSendItemListByApplyinstId(String applyinstId) throws Exception;

    void batchInsertAeaHiSmsSendItem(@Param("hiSmsSendItemList") List<AeaHiSmsSendItem> hiSmsSendItemList);

    /**
     * 获取已经出证的数量
     *
     * @param applyinstId
     * @return
     */

    int countSendItemByApplyinstId(@Param("applyinstId") String applyinstId);

    /**
     * 获取并联申报需要出证的数量
     *
     * @param applyinstId
     */
    int getNeedSendCount(@Param("applyinstId") String applyinstId);
}
