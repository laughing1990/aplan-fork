package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
import com.augurit.aplanmis.common.mapper.AeaHiSmsSendItemMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsSendItemService;
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
    <li>创建人:Administrator</li>
    <li>创建时间：2019-08-03 10:37:41</li>
</ul>
*/
@Service
@Transactional
public class AeaHiSmsSendItemServiceImpl implements AeaHiSmsSendItemService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiSmsSendItemServiceImpl.class);

    @Autowired
    private AeaHiSmsSendItemMapper aeaHiSmsSendItemMapper;

    public void saveAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception{
        aeaHiSmsSendItemMapper.insertAeaHiSmsSendItem(aeaHiSmsSendItem);
    }
    public void updateAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception{
        aeaHiSmsSendItemMapper.updateAeaHiSmsSendItem(aeaHiSmsSendItem);
    }
    public void deleteAeaHiSmsSendItemById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiSmsSendItemMapper.deleteAeaHiSmsSendItem(id);
    }
    public PageInfo<AeaHiSmsSendItem> listAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiSmsSendItem> list = aeaHiSmsSendItemMapper.listAeaHiSmsSendItem(aeaHiSmsSendItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiSmsSendItem>(list);
    }
    public AeaHiSmsSendItem getAeaHiSmsSendItemById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiSmsSendItemMapper.getAeaHiSmsSendItemById(id);
    }
    public List<AeaHiSmsSendItem> listAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception{
        List<AeaHiSmsSendItem> list = aeaHiSmsSendItemMapper.listAeaHiSmsSendItem(aeaHiSmsSendItem);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

