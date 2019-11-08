package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AeaHiSmsInfoServiceImpl implements AeaHiSmsInfoService {
    @Autowired
    private AeaHiSmsInfoMapper aeaHiSmsInfoMapper;

    @Override
    public AeaHiSmsInfo createAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception {
        if(aeaHiSmsInfo==null)
            throw new NullPointerException("需要保存的领件对象为空！");

        aeaHiSmsInfo.setCreater(SecurityContext.getCurrentUserName());
        aeaHiSmsInfo.setCreateTime(new Date());
        aeaHiSmsInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiSmsInfoMapper.insertAeaHiSmsInfo(aeaHiSmsInfo);
        return aeaHiSmsInfo;
    }

    @Override
    public void updateAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception {
        if(aeaHiSmsInfo==null)
            throw new NullPointerException("需要保存的领件对象为空！");
        if(aeaHiSmsInfo.getId()==null)
            throw new InvalidParameterException("更新对象的主键ID为空!");

        aeaHiSmsInfoMapper.updateAeaHiSmsInfo(aeaHiSmsInfo);
    }

    @Override
    public AeaHiSmsInfo getAeaHiSmsInfoById(String id) throws Exception {
        if(id==null)
            throw new InvalidParameterException("参数id为空！");

        return aeaHiSmsInfoMapper.getAeaHiSmsInfoById(id);
    }

    @Override
    public void deleteAeaHiSmsInfoById(String id) throws Exception {
        if(id==null)
            throw new InvalidParameterException("参数id为空！");
        aeaHiSmsInfoMapper.deleteAeaHiSmsInfo(id);
    }

    @Override
    public List<AeaHiSmsInfo> listAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception {
        aeaHiSmsInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiSmsInfoMapper.listAeaHiSmsInfo(aeaHiSmsInfo);
    }

    @Override
    public PageInfo<AeaHiSmsInfo> listAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo, Page page) throws Exception {
        aeaHiSmsInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiSmsInfo> list = aeaHiSmsInfoMapper.listAeaHiSmsInfo(aeaHiSmsInfo);
        return new PageInfo<AeaHiSmsInfo>(list);
    }

    @Override
    public AeaHiSmsInfo getAeaHiSmsInfoByApplyinstId(String applyinstId) throws Exception {
        if(applyinstId==null)
            throw new InvalidParameterException("参数id为空！");

        return aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applyinstId);
    }

    @Override
    public List<AeaHiSmsInfo> listAeaHiSmsInfoByAddresseeIdcard(String addresseeIdcard) throws Exception {
        if(addresseeIdcard==null)
            throw new InvalidParameterException("参数addresseeIdcard为空！");

        AeaHiSmsInfo conditionObj = new AeaHiSmsInfo();
        conditionObj.setAddresseeIdcard(addresseeIdcard);
        conditionObj.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiSmsInfoMapper.listAeaHiSmsInfo(conditionObj);
    }

    @Override
    public List<AeaHiSmsInfo> listAeaHiSmsInfoBySenderPhone(String senderPhone) throws Exception {
        if(senderPhone==null)
            throw new InvalidParameterException("参数senderPhone为空！");

        AeaHiSmsInfo conditionObj = new AeaHiSmsInfo();
        conditionObj.setSenderPhone(senderPhone);
        conditionObj.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiSmsInfoMapper.listAeaHiSmsInfo(conditionObj);
    }

    @Override
    public List<AeaHiSmsInfo> listAeaHiSmsInfoLikeSenderName(String senderName) throws Exception {
        if(senderName==null)
            throw new InvalidParameterException("参数senderName为空！");

        return aeaHiSmsInfoMapper.listAeaHiSmsInfoLikeSenderName(senderName,SecurityContext.getCurrentOrgId());
    }

    @Override
    public AeaHiSmsInfo getAeaHiSmsInfoByOrderIdOrExpressNum(String orderId, String expressNum) throws Exception {
        if(orderId==null&&expressNum==null)
            throw new InvalidParameterException("参数orderId和expressNum不能同时为空！");

        return aeaHiSmsInfoMapper.getAeaHiSmsInfoByOrderIdOrExpressNum(orderId,expressNum,SecurityContext.getCurrentOrgId());
    }
}
