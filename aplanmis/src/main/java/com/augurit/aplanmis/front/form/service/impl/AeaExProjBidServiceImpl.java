package com.augurit.aplanmis.front.form.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaExProjBid;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaExProjBidMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.front.form.service.AeaExProjBidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* 招投标信息-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:刘赵雄</li>
    <li>创建时间：2019-10-31 15:56:12</li>
</ul>
*/
@Service
@Transactional
@Slf4j
public class AeaExProjBidServiceImpl implements AeaExProjBidService {

    @Autowired
    private AeaExProjBidMapper aeaExProjBidMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    public void saveAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception{
        aeaExProjBid.setCreater(SecurityContext.getCurrentUserName());
        aeaExProjBid.setCreateTime(new Date());
        aeaExProjBid.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjBidMapper.insertAeaExProjBid(aeaExProjBid);
    }

    public void updateAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception{
        aeaExProjBid.setModifier(SecurityContext.getCurrentUserName());
        aeaExProjBid.setModifyTime(new Date());
        aeaExProjBidMapper.updateAeaExProjBid(aeaExProjBid);
    }

    public AeaExProjBid getAeaExProjBidByProjId(String projId) throws Exception{
        if(projId == null){
            throw new InvalidParameterException(projId);
        }
        log.debug("根据ID获取Form对象，ID为：{}", projId);
        return aeaExProjBidMapper.getAeaExProjBidByProjId(projId);
    }

    @Override
    public AeaProjInfo getProjInfoByProjId(String projId){
        if(projId == null){
            throw new InvalidParameterException(projId);
        }
        log.debug("根据ID获取Form对象，ID为：{}", projId);
        return aeaProjInfoMapper.getAeaProjInfoById(projId);
    }

}

