package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectService;
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
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:26:15</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiItemCorrectServiceImpl implements AeaHiItemCorrectService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemCorrectServiceImpl.class);

    @Autowired
    private AeaHiItemCorrectMapper aeaHiItemCorrectMapper;

    public void saveAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception {
        aeaHiItemCorrect.setCreater(SecurityContext.getCurrentUserId());
        aeaHiItemCorrect.setCreateTime(new Date());
        aeaHiItemCorrect.setIsDeleted("0");
        aeaHiItemCorrect.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiItemCorrectMapper.insertAeaHiItemCorrect(aeaHiItemCorrect);
    }

    public void updateAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception {
        aeaHiItemCorrect.setModifier(SecurityContext.getCurrentUserName());
        aeaHiItemCorrect.setModifyTime(new Date());
        aeaHiItemCorrectMapper.updateAeaHiItemCorrect(aeaHiItemCorrect);
    }

    public void deleteAeaHiItemCorrectById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemCorrectMapper.deleteAeaHiItemCorrect(id);
    }

    public PageInfo<AeaHiItemCorrect> listAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiItemCorrect> list = aeaHiItemCorrectMapper.listAeaHiItemCorrect(aeaHiItemCorrect);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemCorrect>(list);
    }

    public AeaHiItemCorrect getAeaHiItemCorrectById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemCorrectMapper.getAeaHiItemCorrectById(id);
    }

    public List<AeaHiItemCorrect> listAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception {
        List<AeaHiItemCorrect> list = aeaHiItemCorrectMapper.listAeaHiItemCorrect(aeaHiItemCorrect);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public AeaHiItemCorrect getCurrentCorrectinst(String iteminstId) throws Exception {
        if (StringUtils.isBlank(iteminstId)) throw new InvalidParameterException(iteminstId);
        AeaHiItemCorrect aeaHiItemCorrect = new AeaHiItemCorrect();
        aeaHiItemCorrect.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiItemCorrect.setIteminstId(iteminstId);
        List<AeaHiItemCorrect> itemCorrects = aeaHiItemCorrectMapper.listAeaHiItemCorrect(aeaHiItemCorrect);
        return itemCorrects.size() > 0 ? itemCorrects.get(0) : null;
    }
}

