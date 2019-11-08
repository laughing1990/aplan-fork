package com.augurit.aplanmis.common.service.applyinst.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectStateHist;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstCorrectStateHistMapper;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectStateHistService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* 事项输入输出实例表-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:tiantian</li>
    <li>创建时间：2019-08-28 17:34:26</li>
</ul>
*/
@Service
@Transactional
public class AeaHiApplyinstCorrectStateHistServiceImpl implements AeaHiApplyinstCorrectStateHistService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstCorrectStateHistServiceImpl.class);

    @Autowired
    private AeaHiApplyinstCorrectStateHistMapper aeaHiApplyinstCorrectStateHistMapper;

    public void saveAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist) throws Exception{
        aeaHiApplyinstCorrectStateHist.setApplyinstCorrectStateHistId(UUID.randomUUID().toString());
        aeaHiApplyinstCorrectStateHist.setCreater(SecurityContext.getCurrentUserName());
        aeaHiApplyinstCorrectStateHist.setCreateTime(new Date());
        aeaHiApplyinstCorrectStateHistMapper.insertAeaHiApplyinstCorrectStateHist(aeaHiApplyinstCorrectStateHist);
    }
    public void updateAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist) throws Exception{
        aeaHiApplyinstCorrectStateHistMapper.updateAeaHiApplyinstCorrectStateHist(aeaHiApplyinstCorrectStateHist);
    }
    public void deleteAeaHiApplyinstCorrectStateHistById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiApplyinstCorrectStateHistMapper.deleteAeaHiApplyinstCorrectStateHist(id);
    }
    public PageInfo<AeaHiApplyinstCorrectStateHist> listAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiApplyinstCorrectStateHist> list = aeaHiApplyinstCorrectStateHistMapper.listAeaHiApplyinstCorrectStateHist(aeaHiApplyinstCorrectStateHist);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiApplyinstCorrectStateHist>(list);
    }
    public AeaHiApplyinstCorrectStateHist getAeaHiApplyinstCorrectStateHistById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiApplyinstCorrectStateHistMapper.getAeaHiApplyinstCorrectStateHistById(id);
    }
    public List<AeaHiApplyinstCorrectStateHist> listAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist) throws Exception{
        List<AeaHiApplyinstCorrectStateHist> list = aeaHiApplyinstCorrectStateHistMapper.listAeaHiApplyinstCorrectStateHist(aeaHiApplyinstCorrectStateHist);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

