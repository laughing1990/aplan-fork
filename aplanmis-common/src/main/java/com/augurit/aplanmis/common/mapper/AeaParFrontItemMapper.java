package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.vo.AeaParFrontItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 阶段事项前置检测表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:47:49</li>
 * </ul>
 */
@Mapper
public interface AeaParFrontItemMapper {

    void insertAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void updateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void deleteAeaParFrontItem(@Param("id") String id) throws Exception;

    List<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    AeaParFrontItem getAeaParFrontItemById(@Param("id") String id) throws Exception;

    List<AeaParFrontItemVo> listAeaParFrontItemVo(AeaParFrontItem aeaParFrontItem) throws Exception;

    Long getMaxSortNo(AeaParFrontItem aeaParFrontItem)throws Exception;

    AeaParFrontItemVo getAeaParFrontItemVoByFrontItemId(@Param("frontItemId") String frontItemId) throws Exception;


}
