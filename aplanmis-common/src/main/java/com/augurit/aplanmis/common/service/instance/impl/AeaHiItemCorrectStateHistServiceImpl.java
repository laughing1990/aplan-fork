package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectStateHist;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectStateHistMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectStateHistService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
* 事项输入输出实例表-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-08-03 10:32:34</li>
</ul>
*/
@Service
@Transactional
public class AeaHiItemCorrectStateHistServiceImpl implements AeaHiItemCorrectStateHistService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemCorrectStateHistServiceImpl.class);

    @Autowired
    private AeaHiItemCorrectStateHistMapper aeaHiItemCorrectStateHistMapper;

    public void saveAeaHiItemCorrectStateHist(AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist) throws Exception{
        aeaHiItemCorrectStateHist.setCreater(SecurityContext.getCurrentUserName());
        aeaHiItemCorrectStateHist.setCreateTime(new Date());
        aeaHiItemCorrectStateHistMapper.insertAeaHiItemCorrectStateHist(aeaHiItemCorrectStateHist);
    }
    public void updateAeaHiItemCorrectStateHist(AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist) throws Exception{
        aeaHiItemCorrectStateHistMapper.updateAeaHiItemCorrectStateHist(aeaHiItemCorrectStateHist);
    }
    public void deleteAeaHiItemCorrectStateHistById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiItemCorrectStateHistMapper.deleteAeaHiItemCorrectStateHist(id);
    }
    public PageInfo<AeaHiItemCorrectStateHist> listAeaHiItemCorrectStateHist(AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiItemCorrectStateHist> list = aeaHiItemCorrectStateHistMapper.listAeaHiItemCorrectStateHist(aeaHiItemCorrectStateHist);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemCorrectStateHist>(list);
    }
    public AeaHiItemCorrectStateHist getAeaHiItemCorrectStateHistById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemCorrectStateHistMapper.getAeaHiItemCorrectStateHistById(id);
    }
    public List<AeaHiItemCorrectStateHist> listAeaHiItemCorrectStateHist(AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist) throws Exception{
        List<AeaHiItemCorrectStateHist> list = aeaHiItemCorrectStateHistMapper.listAeaHiItemCorrectStateHist(aeaHiItemCorrectStateHist);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

