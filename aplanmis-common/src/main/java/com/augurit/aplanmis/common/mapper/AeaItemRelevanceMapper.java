package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemRelevance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:13:41</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaItemRelevanceMapper {

    public void insertAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception;

    public void updateAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception;

    public void deleteAeaItemRelevance(@Param("id") String id) throws Exception;

    public List<AeaItemRelevance> listAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception;

    public AeaItemRelevance getAeaItemRelevanceById(@Param("id") String id) throws Exception;

    /**
     * 取消业务表的关联关系（删除）
     * @param itemId
     * @param cancelItemIds
     * @throws Exception
     */
    public void cancelItemRelation(@Param("itemId")String itemId,@Param("itemIds")String[] cancelItemIds)throws Exception;
}
