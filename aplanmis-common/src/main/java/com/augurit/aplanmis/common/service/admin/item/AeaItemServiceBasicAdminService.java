package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 设立依据-Service服务调用接口类
 */
public interface AeaItemServiceBasicAdminService {

    void saveAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception;

    void updateAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception;

    void deleteAeaItemServiceBasicById(String id) throws Exception;

    PageInfo<AeaItemServiceBasic> listAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic, Page page) throws Exception;

    AeaItemServiceBasic getAeaItemServiceBasicById(String id) throws Exception;

    List<AeaItemServiceBasic> listAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception;

    void batchDeleteServiceBasic(String[] ids) throws Exception;

    void batchSaveServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception;

}
