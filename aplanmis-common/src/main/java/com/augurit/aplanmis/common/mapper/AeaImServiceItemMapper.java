package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImServiceItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImServiceItemMapper {
    /**查询中介服务事项详情*/
    AeaImServiceItem getAgentServiceItemDetail(@Param("agentItemBasicId") String agentItemBasicId);

    List<AeaImServiceItem> getItemServiceByServiceId(String serviceId);

    /**
     * 根据事项版本ID查询关联的中介服务
     * @param itemVerId
     * @return
     */
    List<AeaImServiceItem> listAeaImServiceItemByItemVerId(@Param("itemVerId") String itemVerId);

    public void insertAeaImServiceItem(AeaImServiceItem aeaImServiceItem) throws Exception;

    /**
     * 取消业务表的关联关系（逻辑删除）
     * @param itemVerId
     * @param serviceIds
     * @throws Exception
     */
    public void cancelServiceRelation(@Param("itemVerId") String itemVerId, @Param("serviceIds") String[] serviceIds) throws Exception;

    public List <AeaImServiceItem> listAeaImServiceItem(AeaImServiceItem aeaImServiceItem) throws Exception;

    AeaImServiceItem getAeaImServiceItemByServiceItemId(@Param("serviceItemId") String serviceItemId);
}
