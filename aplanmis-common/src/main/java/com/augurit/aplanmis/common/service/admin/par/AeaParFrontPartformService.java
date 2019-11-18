package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.vo.AeaParFrontPartformVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段的扩展表单前置检测表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:23</li>
 * </ul>
 */
public interface AeaParFrontPartformService {
    void saveAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    void updateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    void deleteAeaParFrontPartformById(String id) throws Exception;

    PageInfo<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception;

    AeaParFrontPartform getAeaParFrontPartformById(String id) throws Exception;

    List<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    PageInfo<AeaParFrontPartformVo> listAeaParFrontPartformVoByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception;

    Long getMaxSortNo(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    PageInfo<AeaParFrontPartformVo> listSelectParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception;

    AeaParFrontPartformVo getAeaParFrontPartformVoById(String frontPartformId) throws Exception;

    List<AeaParFrontPartformVo> getAeaParFrontPartformVoByStageId(String stageId);

    void batchSaveAeaParFrontPartform(String stageId,String stagePartformIds)throws Exception;

    List<AeaParFrontPartformVo> listAeaParFrontPartformVoByNoPage(AeaParFrontPartform aeaParFrontPartform) throws Exception;

}
