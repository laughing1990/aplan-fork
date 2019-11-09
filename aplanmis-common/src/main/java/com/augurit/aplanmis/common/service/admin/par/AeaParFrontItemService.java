package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.vo.AeaParFrontItemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段事项前置检测表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:47:49</li>
 * </ul>
 */
public interface AeaParFrontItemService {
    void saveAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void updateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void deleteAeaParFrontItemById(String id) throws Exception;

    PageInfo<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem, Page page) throws Exception;

    AeaParFrontItem getAeaParFrontItemById(String id) throws Exception;

    List<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    PageInfo<AeaParFrontItemVo> listAeaParFrontItemVoByPage(AeaParFrontItem aeaParFrontItem, Page page) throws Exception;

    Long getMaxSortNo(AeaParFrontItem aeaParFrontItem) throws Exception;

    AeaParFrontItemVo getAeaParFrontItemVoByFrontItemId(String frontItemId) throws Exception;


}
