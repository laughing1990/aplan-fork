package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemExeorg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 实施机关-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2018-10-12 09:28:12</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaItemExeorgMapper {

    void insertAeaItemExeorg(AeaItemExeorg aeaItemExeorg) throws Exception;

    void updateAeaItemExeorg(AeaItemExeorg aeaItemExeorg) throws Exception;

    void deleteAeaItemExeorg(@Param("id") String id) throws Exception;

    List<AeaItemExeorg> listAeaItemExeorg(AeaItemExeorg aeaItemExeorg) throws Exception;

    AeaItemExeorg getAeaItemExeorgById(@Param("id") String id) throws Exception;

    AeaItemExeorg getAeaItemExeorgByItemBasicId(@Param("itemBasicId") String itemBasicId);
}
