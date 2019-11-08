package com.augurit.aplanmis.admin.market.service.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceQualService;
import com.augurit.aplanmis.common.domain.AeaImServiceQual;
import com.augurit.aplanmis.common.mapper.AeaImServiceQualMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* -Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:thinkpad</li>
    <li>创建时间：2019-06-10 13:49:01</li>
</ul>
*/
@Service
@Transactional
public class AeaImServiceQualServiceImpl implements AeaImServiceQualService {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceQualServiceImpl.class);

    @Autowired
    private AeaImServiceQualMapper aeaImServiceQualMapper;

    @Override
    public void saveAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception{
        aeaImServiceQualMapper.insertAeaImServiceQual(aeaImServiceQual);
    }
    @Override
    public void updateAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception{
        aeaImServiceQualMapper.updateAeaImServiceQual(aeaImServiceQual);
    }
    @Override
    public void deleteAeaImServiceQualById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaImServiceQualMapper.deleteAeaImServiceQual(id);
    }
    @Override
    public void deleteServiceQualByServiceId(String serviceId,String qualId) throws Exception{
        if(serviceId!=null&&qualId!=null){

            aeaImServiceQualMapper.deleteServiceQualByServiceId(serviceId,qualId);
        }else{
            throw new InvalidParameterException(serviceId);
        }
    }
    @Override
    public PageInfo<AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImServiceQual> list = aeaImServiceQualMapper.listAeaImServiceQual(aeaImServiceQual);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImServiceQual>(list);
    }
    @Override
    public AeaImServiceQual getAeaImServiceQualById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImServiceQualMapper.getAeaImServiceQualById(id);
    }
    @Override
    public List<AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception{
        List<AeaImServiceQual> list = aeaImServiceQualMapper.listAeaImServiceQual(aeaImServiceQual);
        logger.debug("成功执行查询list！！");
        return list;
    }
    @Override
    public List<AeaImServiceQual> getAeaImServiceQualListByServiceId(String serviceId) throws Exception{
        List<AeaImServiceQual> list = aeaImServiceQualMapper.getAeaImServiceQualListByServiceId(serviceId);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

