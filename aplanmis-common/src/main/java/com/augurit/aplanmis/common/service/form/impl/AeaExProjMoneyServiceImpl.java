package com.augurit.aplanmis.common.service.form.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaExProjMoney;
import com.augurit.aplanmis.common.mapper.AeaExProjMoneyMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjMoneyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
* 项目资金来源构成比例-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-31 09:28:01</li>
</ul>
*/
@Service
@Transactional
public class AeaExProjMoneyServiceImpl implements AeaExProjMoneyService {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjMoneyServiceImpl.class);

    @Autowired
    private AeaExProjMoneyMapper aeaExProjMoneyMapper;

    public void saveAeaExProjMoney(AeaExProjMoney aeaExProjMoney) throws Exception{
        aeaExProjMoneyMapper.insertAeaExProjMoney(aeaExProjMoney);
    }
    public void updateAeaExProjMoney(AeaExProjMoney aeaExProjMoney) throws Exception{
        aeaExProjMoneyMapper.updateAeaExProjMoney(aeaExProjMoney);
    }
    public void deleteAeaExProjMoneyById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaExProjMoneyMapper.deleteAeaExProjMoney(id);
    }
    public PageInfo<AeaExProjMoney> listAeaExProjMoney(AeaExProjMoney aeaExProjMoney,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaExProjMoney> list = aeaExProjMoneyMapper.listAeaExProjMoney(aeaExProjMoney);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaExProjMoney>(list);
    }
    public AeaExProjMoney getAeaExProjMoneyById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaExProjMoneyMapper.getAeaExProjMoneyById(id);
    }
    public List<AeaExProjMoney> listAeaExProjMoney(AeaExProjMoney aeaExProjMoney) throws Exception{
        List<AeaExProjMoney> list = aeaExProjMoneyMapper.listAeaExProjMoney(aeaExProjMoney);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

