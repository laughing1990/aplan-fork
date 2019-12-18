package com.augurit.aplanmis.common.service.applyinstCancel.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCancel;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstCancelMapper;
import com.augurit.aplanmis.common.service.applyinstCancel.AeaHiApplyinstCancelService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 申报撤件实例表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:58</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiApplyinstCancelServiceImpl implements AeaHiApplyinstCancelService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstCancelServiceImpl.class);

    @Autowired
    private AeaHiApplyinstCancelMapper aeaHiApplyinstCancelMapper;

    public void saveAeaHiApplyinstCancel(AeaHiApplyinstCancel aeaHiApplyinstCancel) throws Exception {
        aeaHiApplyinstCancelMapper.insertAeaHiApplyinstCancel(aeaHiApplyinstCancel);
    }

    public void updateAeaHiApplyinstCancel(AeaHiApplyinstCancel aeaHiApplyinstCancel) throws Exception {
        aeaHiApplyinstCancelMapper.updateAeaHiApplyinstCancel(aeaHiApplyinstCancel);
    }

    public void deleteAeaHiApplyinstCancelById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiApplyinstCancelMapper.deleteAeaHiApplyinstCancel(id);
    }

    public PageInfo<AeaHiApplyinstCancel> listAeaHiApplyinstCancel(AeaHiApplyinstCancel aeaHiApplyinstCancel, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiApplyinstCancel> list = aeaHiApplyinstCancelMapper.listAeaHiApplyinstCancel(aeaHiApplyinstCancel);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiApplyinstCancel>(list);
    }

    public AeaHiApplyinstCancel getAeaHiApplyinstCancelById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiApplyinstCancelMapper.getAeaHiApplyinstCancelById(id);
    }

    public List<AeaHiApplyinstCancel> listAeaHiApplyinstCancel(AeaHiApplyinstCancel aeaHiApplyinstCancel) throws Exception {
        List<AeaHiApplyinstCancel> list = aeaHiApplyinstCancelMapper.listAeaHiApplyinstCancel(aeaHiApplyinstCancel);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public List<AeaHiApplyinstCancel> listAeaHiApplyinstCancelByIteminstId(String iteminstId) throws Exception {
        List<AeaHiApplyinstCancel> aeaHiApplyinstCancels = new ArrayList();
        if (StringUtils.isNotBlank(iteminstId)) {
            aeaHiApplyinstCancels.addAll(aeaHiApplyinstCancelMapper.listAeaHiApplyinstCancelByIteminstId(iteminstId, SecurityContext.getCurrentOrgId()));
        }
        return aeaHiApplyinstCancels;
    }
}

