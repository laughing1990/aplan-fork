package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.aplanmis.common.domain.AeaProjWindow;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.mapper.AeaProjWindowMapper;
import com.augurit.aplanmis.common.service.window.AeaProjWindowService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
/**
* 项目与代办中心（本质也是窗口）代办关联表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaProjWindowServiceImpl implements AeaProjWindowService {

    private static Logger logger = LoggerFactory.getLogger(AeaProjWindowServiceImpl.class);

    @Autowired
    private AeaProjWindowMapper aeaProjWindowMapper;

    public void saveAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception{
        aeaProjWindowMapper.insertAeaProjWindow(aeaProjWindow);
    }
    public void updateAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception{
        aeaProjWindowMapper.updateAeaProjWindow(aeaProjWindow);
    }
    public void deleteAeaProjWindowById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaProjWindowMapper.deleteAeaProjWindow(id);
    }
    public PageInfo<AeaProjWindow> listAeaProjWindow(AeaProjWindow aeaProjWindow,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaProjWindow> list = aeaProjWindowMapper.listAeaProjWindow(aeaProjWindow);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaProjWindow>(list);
    }
    public AeaProjWindow getAeaProjWindowById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaProjWindowMapper.getAeaProjWindowById(id);
    }
    public List<AeaProjWindow> listAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception{
        List<AeaProjWindow> list = aeaProjWindowMapper.listAeaProjWindow(aeaProjWindow);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List <AeaServiceWindow> listAeaServiceWindowByProjInfoId(String projInfoId){
        return aeaProjWindowMapper.listAeaServiceWindowByProjInfoId(projInfoId);
    }
}

