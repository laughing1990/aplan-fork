package com.augurit.aplanmis.common.service.applyinstCancel.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCancelLog;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstCancelLogMapper;
import com.augurit.aplanmis.common.service.applyinstCancel.AeaHiApplyinstCancelLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 申报撤件实例和申请实例历史状态关联表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiApplyinstCancelLogServiceImpl implements AeaHiApplyinstCancelLogService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstCancelLogServiceImpl.class);

    @Autowired
    private AeaHiApplyinstCancelLogMapper aeaHiApplyinstCancelLogMapper;

    public void saveAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception {
        aeaHiApplyinstCancelLogMapper.insertAeaHiApplyinstCancelLog(aeaHiApplyinstCancelLog);
    }

    public void updateAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception {
        aeaHiApplyinstCancelLogMapper.updateAeaHiApplyinstCancelLog(aeaHiApplyinstCancelLog);
    }

    public void deleteAeaHiApplyinstCancelLogById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiApplyinstCancelLogMapper.deleteAeaHiApplyinstCancelLog(id);
    }

    public PageInfo<AeaHiApplyinstCancelLog> listAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiApplyinstCancelLog> list = aeaHiApplyinstCancelLogMapper.listAeaHiApplyinstCancelLog(aeaHiApplyinstCancelLog);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiApplyinstCancelLog>(list);
    }

    public AeaHiApplyinstCancelLog getAeaHiApplyinstCancelLogById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiApplyinstCancelLogMapper.getAeaHiApplyinstCancelLogById(id);
    }

    public List<AeaHiApplyinstCancelLog> listAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception {
        List<AeaHiApplyinstCancelLog> list = aeaHiApplyinstCancelLogMapper.listAeaHiApplyinstCancelLog(aeaHiApplyinstCancelLog);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

