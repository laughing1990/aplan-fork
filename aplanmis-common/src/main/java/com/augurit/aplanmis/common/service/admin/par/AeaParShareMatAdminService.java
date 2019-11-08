package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParShareMat;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 主题共享材料表-Service服务调用接口类
 */
public interface AeaParShareMatAdminService {

    void saveAeaParShareMat(AeaParShareMat aeaParShareMat);

    void updateAeaParShareMat(AeaParShareMat aeaParShareMat);

    void deleteAeaParShareMatById(String id);

    void deleteAeaParShareMatByIds(String[] shareMatIds);

    PageInfo<AeaParShareMat> listAeaParShareMat(AeaParShareMat aeaParShareMat, Page page);

    PageInfo<AeaParShareMat> listAeaParShareMatCascade(AeaParShareMat aeaParShareMat, Page page) throws Exception;

    AeaParShareMat getAeaParShareMatById(String id);

    AeaParShareMat getAeaParShareMatCascade(String id);

    List<AeaParShareMat> listAeaParShareMat(AeaParShareMat aeaParShareMat);

    boolean existShare(String inoutId);
}
