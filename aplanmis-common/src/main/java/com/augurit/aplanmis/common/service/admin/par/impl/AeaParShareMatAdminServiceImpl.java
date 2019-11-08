package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaParShareMat;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.mapper.AeaParShareMatMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParShareMatAdminService;
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
 * 主题共享材料表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParShareMatAdminServiceImpl implements AeaParShareMatAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParShareMatAdminServiceImpl.class);

    @Autowired
    private AeaParShareMatMapper aeaParShareMatMapper;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Override
    public void saveAeaParShareMat(AeaParShareMat aeaParShareMat) {
        aeaParShareMat.setIsActive("1");
        aeaParShareMat.setCreater(SecurityContext.getCurrentUserName());
        aeaParShareMat.setCreateTime(new Date());
        aeaParShareMatMapper.insertAeaParShareMat(aeaParShareMat);
    }

    @Override
    public void updateAeaParShareMat(AeaParShareMat aeaParShareMat) {
        aeaParShareMat.setModifier(SecurityContext.getCurrentUserName());
        aeaParShareMat.setModifyTime(new Date());
        aeaParShareMatMapper.updateAeaParShareMat(aeaParShareMat);
    }

    @Override
    public void deleteAeaParShareMatById(String id) {
        aeaParShareMatMapper.deleteAeaParShareMat(id);
    }

    @Override
    public void deleteAeaParShareMatByIds(String[] shareMatIds) {
        aeaParShareMatMapper.deleteAeaParShareMatByIds(shareMatIds);
    }

    @Override
    public PageInfo<AeaParShareMat> listAeaParShareMat(AeaParShareMat aeaParShareMat, Page page) {
        PageHelper.startPage(page);
        List<AeaParShareMat> list = aeaParShareMatMapper.listAeaParShareMat(aeaParShareMat);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaParShareMat> listAeaParShareMatCascade(AeaParShareMat aeaParShareMat, Page page) {
        PageHelper.startPage(page);
        List<AeaParShareMat> list = aeaParShareMatMapper.listAeaParShareMatCascade(aeaParShareMat);

        for (AeaParShareMat shareMat : list) {
            setShareMatInfo(shareMat);
        }

        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParShareMat getAeaParShareMatById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParShareMatMapper.getAeaParShareMatById(id);
    }

    @Override
    public AeaParShareMat getAeaParShareMatCascade(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        AeaParShareMat aeaParShareMat = aeaParShareMatMapper.getAeaParShareMatCascade(id);
        setShareMatInfo(aeaParShareMat);
        return aeaParShareMat;
    }

    /**
     * 设置共享的资料或者电子证照信息
     */
    private void setShareMatInfo(AeaParShareMat aeaParShareMat) {
        //设置输出
        if (MindType.MATERIAL.getValue().equals(aeaParShareMat.getOutFileType())) {
            AeaItemMat aeaItemMat = aeaItemMatMapper.selectOneById(aeaParShareMat.getOutMatId());
            if (aeaItemMat != null) {
                aeaParShareMat.setOutMatCertName(aeaItemMat.getMatName());
            }
        } else if (MindType.CERTIFICATE.getValue().equals(aeaParShareMat.getOutFileType())) {
            AeaCert aeaCert = aeaCertMapper.selectOneById(aeaParShareMat.getOutCertId());
            if (aeaCert != null) {
                aeaParShareMat.setOutMatCertName(aeaCert.getCertName());
            }
        }

        //设置输入
        if (MindType.MATERIAL.getValue().equals(aeaParShareMat.getInFileType())) {
            AeaItemMat aeaItemMat = aeaItemMatMapper.selectOneById(aeaParShareMat.getInMatId());
            if (aeaItemMat != null) {
                aeaParShareMat.setInMatCertName(aeaItemMat.getMatName());
            }
        } else if (MindType.CERTIFICATE.getValue().equals(aeaParShareMat.getInFileType())) {
            AeaCert aeaCert = aeaCertMapper.selectOneById(aeaParShareMat.getInCertId());
            if (aeaCert != null) {
                aeaParShareMat.setInMatCertName(aeaCert.getCertName());
            }
        }

    }

    @Override
    public List<AeaParShareMat> listAeaParShareMat(AeaParShareMat aeaParShareMat) {
        List<AeaParShareMat> list = aeaParShareMatMapper.listAeaParShareMat(aeaParShareMat);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public boolean existShare(String inoutId) {
        Long shareNum = aeaParShareMatMapper.existShare(inoutId);
        return shareNum > 0;
    }
}

