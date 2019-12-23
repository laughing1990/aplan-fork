package com.augurit.aplanmis.common.service.solicit;

import com.augurit.aplanmis.common.domain.AeaHiSolicitDetailUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 征求意见详情用户任务表-Service服务调用接口类
*/
public interface AeaHiSolicitDetailUserService {
    public void saveAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception;
    public void updateAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception;
    public void deleteAeaHiSolicitDetailUserById(String id) throws Exception;
    public PageInfo<AeaHiSolicitDetailUser> listAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser, Page page) throws Exception;
    public AeaHiSolicitDetailUser getAeaHiSolicitDetailUserById(String id) throws Exception;
    public List<AeaHiSolicitDetailUser> listAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception;

}
