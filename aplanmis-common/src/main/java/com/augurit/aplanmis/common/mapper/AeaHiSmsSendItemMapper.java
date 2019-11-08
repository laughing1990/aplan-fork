package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

    /**
    * -Mapper数据与持久化接口类
    <ul>
        <li>项目名：奥格erp3.0--第一期建设项目</li>
        <li>版本信息：v1.0</li>
        <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
        <li>创建人:Administrator</li>
        <li>创建时间：2019-08-03 10:37:41</li>
    </ul>
    */
    @Mapper
    @Repository
    public interface AeaHiSmsSendItemMapper {

    public void insertAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;
    public void updateAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;
    public void deleteAeaHiSmsSendItem(@Param("id") String id) throws Exception;
    public List <AeaHiSmsSendItem> listAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;
    public AeaHiSmsSendItem getAeaHiSmsSendItemById(@Param("id") String id) throws Exception;

        List<AeaHiSmsSendItem> getAeaHiSmsSendItemListByApplyinstId(String applyinstId) throws Exception;

        void batchInsertAeaHiSmsSendItem(@Param("hiSmsSendItemList")List<AeaHiSmsSendItem> hiSmsSendItemList);

        /**
         * 获取已经出证的数量
         * @param applyinstId
         * @return
         */

        int countSendItemByApplyinstId(@Param("applyinstId") String applyinstId);

        /**
         * 获取并联申报需要出证的数量
         * @param applyinstId
         * @param rootOrgId
         * @return
         */
        int getNeedSendCount(@Param("applyinstId") String applyinstId,@Param("rootOrgId") String rootOrgId);
    }
