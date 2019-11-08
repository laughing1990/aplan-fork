package com.augurit.aplanmis.supermarket.certinstMajor.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaImCertinstMajor;
import com.augurit.aplanmis.common.mapper.AeaImCertinstMajorMapper;
import com.augurit.aplanmis.supermarket.certinstMajor.service.AeaImCertinstMajorService;
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
    <li>创建时间：2019-06-12 15:59:06</li>
</ul>
*/
@Service
@Transactional
public class AeaImCertinstMajorServiceImpl implements AeaImCertinstMajorService {

    private static Logger logger = LoggerFactory.getLogger(AeaImCertinstMajorServiceImpl.class);

    @Autowired
    private AeaImCertinstMajorMapper aeaImCertinstMajorMapper;

    public void saveAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception{
        aeaImCertinstMajorMapper.insertAeaImCertinstMajor(aeaImCertinstMajor);
    }
    public void updateAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception{
        aeaImCertinstMajorMapper.updateAeaImCertinstMajor(aeaImCertinstMajor);
    }
    public void deleteAeaImCertinstMajorById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaImCertinstMajorMapper.deleteAeaImCertinstMajor(id);
    }
    public PageInfo<AeaImCertinstMajor> listAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImCertinstMajor> list = aeaImCertinstMajorMapper.listAeaImCertinstMajor(aeaImCertinstMajor);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImCertinstMajor>(list);
    }
    public AeaImCertinstMajor getAeaImCertinstMajorById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImCertinstMajorMapper.getAeaImCertinstMajorById(id);
    }
    public List<AeaImCertinstMajor> listAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception{
        List<AeaImCertinstMajor> list = aeaImCertinstMajorMapper.listAeaImCertinstMajor(aeaImCertinstMajor);
        logger.debug("成功执行查询list！！");
        return list;
    }

}

