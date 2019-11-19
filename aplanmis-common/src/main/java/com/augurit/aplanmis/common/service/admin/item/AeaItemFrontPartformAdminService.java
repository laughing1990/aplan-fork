package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 事项的前置检查事项-Service服务调用接口类
 */
public interface AeaItemFrontPartformAdminService {

    void saveAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform);

    void updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform);

    void deleteAeaItemFrontPartformById(String id);

    void deleteAeaItemFrontPartformByIds(String[] ids);

    PageInfo<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform, Page page);

    AeaItemFrontPartform getAeaItemFrontPartformById(String id);

    List<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform);

    PageInfo<AeaItemFrontPartformVo> listAeaItemFrontPartformVoByPage(AeaItemFrontPartform aeaItemFrontPartform, Page page);

    List<AeaItemFrontPartformVo> listAeaItemFrontPartformVo(AeaItemFrontPartform aeaItemFrontPartform);

    Long getMaxSortNo(String itemVerId, String rootOrgId);

    PageInfo<AeaItemFrontPartformVo> listItemNoSelectPartform(AeaItemFrontPartform aeaItemFrontPartform, Page page);

    AeaItemFrontPartformVo getAeaItemFrontPartformVoById(String frontPartformId);

    List<AeaItemFrontPartformVo> getAeaItemFrontPartformVoByItemVerId(String itemVerId);

    void batchSaveAeaItemFrontPartform(String itemVerId, String[] itemPartformIds)throws Exception;

    void changIsActive(String id, String rootOrgId);
}
