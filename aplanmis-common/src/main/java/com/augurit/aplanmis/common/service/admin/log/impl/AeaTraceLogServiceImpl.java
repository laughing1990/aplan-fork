package com.augurit.aplanmis.common.service.admin.log.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaTraceLog;
import com.augurit.aplanmis.common.mapper.AeaTraceLogMapper;
import com.augurit.aplanmis.common.service.admin.log.AeaTraceLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统操作日志-Service服务调用接口类
 *
 * @author jjt
 * @date 2019/10/15
 */
@Service
@Transactional
public class AeaTraceLogServiceImpl implements AeaTraceLogService {

    private static Logger logger = LoggerFactory.getLogger(AeaTraceLogServiceImpl.class);

    @Autowired
    private AeaTraceLogMapper aeaTraceLogMapper;

    @Override
    public void saveAeaTraceLog(AeaTraceLog aeaTraceLog) {

        aeaTraceLogMapper.insertAeaTraceLog(aeaTraceLog);
    }

    @Override
    public void updateAeaTraceLog(AeaTraceLog aeaTraceLog) {

        aeaTraceLogMapper.updateAeaTraceLog(aeaTraceLog);
    }

    @Override
    public void deleteAeaTraceLogById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        aeaTraceLogMapper.deleteAeaTraceLog(id);
    }

    @Override
    public void deleteLogMore(String[] ids){

        if(ids!=null&&ids.length>0){
            aeaTraceLogMapper.deleteLogMore(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaTraceLog> listAeaTraceLog(AeaTraceLog aeaTraceLog,Page page) {

        aeaTraceLog.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaTraceLog> list = aeaTraceLogMapper.listAeaTraceLog(aeaTraceLog);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaTraceLog>(list);
    }

    @Override
    public AeaTraceLog getAeaTraceLogById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaTraceLogMapper.getAeaTraceLogById(id);
    }

    @Override
    public List<AeaTraceLog> listAeaTraceLog(AeaTraceLog aeaTraceLog) {

        aeaTraceLog.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaTraceLog> list = aeaTraceLogMapper.listAeaTraceLog(aeaTraceLog);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

