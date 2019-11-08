package com.augurit.aplanmis.admin.market.service.service;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
*/
public interface AeaImServiceMajorService {

    public void saveAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception;
    public void updateAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception;
    public void deleteAeaImServiceMajorById(String id) throws Exception;
    public PageInfo<AeaImServiceMajor> listAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor, Page page) throws Exception;
    public AeaImServiceMajor getAeaImServiceMajorById(String id) throws Exception;
    public List<AeaImServiceMajor> listAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception;

    List<ZtreeNode> getMajorTreeByQualId(String serviceTypeId) throws Exception;

    boolean checkUniqueMajorTypeCode(String majorId, String majorCode);

    void batchDeleteMajor(String[] ids) throws Exception;

    public ResultForm deleteAeaImServiceMajorById(String id, Boolean delChildren) throws Exception;
}
