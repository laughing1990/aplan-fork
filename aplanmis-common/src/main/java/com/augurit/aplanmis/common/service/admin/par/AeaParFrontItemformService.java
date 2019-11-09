package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.vo.AeaParFrontItemformVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段事项表单前置检测表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:12</li>
 * </ul>
 */
public interface AeaParFrontItemformService {
    void saveAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    void updateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    void deleteAeaParFrontItemformById(String id) throws Exception;

    PageInfo<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception;

    AeaParFrontItemform getAeaParFrontItemformById(String id) throws Exception;

    List<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    PageInfo<AeaParFrontItemformVo> listAeaParFrontItemformVoByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception;

    Long getMaxSortNo(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    PageInfo<AeaParFrontItemformVo> listSelectParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception;

    AeaParFrontItemformVo getAeaParFrontItemformVoById(String frontItemformId) throws Exception;


}
