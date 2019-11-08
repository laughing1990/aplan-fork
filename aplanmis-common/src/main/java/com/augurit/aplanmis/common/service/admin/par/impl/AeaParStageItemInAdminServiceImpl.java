package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.common.domain.AeaParStageItemIn;
import com.augurit.aplanmis.common.mapper.AeaParInMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemInMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemInAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 阶段事项与输入关联定义表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParStageItemInAdminServiceImpl implements AeaParStageItemInAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageItemInAdminServiceImpl.class);

    @Autowired
    private AeaParStageItemInMapper aeaParStageItemInMapper;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Override
    public void saveAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn) {
        aeaParStageItemIn.setSortNo(getMaxSortNo());
        aeaParStageItemInMapper.insertAeaParStageItemIn(aeaParStageItemIn);
    }

    @Override
    public void updateAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn) {
        aeaParStageItemInMapper.updateAeaParStageItemIn(aeaParStageItemIn);
    }

    @Override
    public void deleteAeaParStageItemInById(String id) {
        Assert.notNull(id, "id is null.");
        aeaParStageItemInMapper.deleteAeaParStageItemIn(id);
    }

    @Override
    public PageInfo<AeaParStageItemIn> listAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn, Page page) {
        PageHelper.startPage(page);
        List<AeaParStageItemIn> list = aeaParStageItemInMapper.listAeaParStageItemIn(aeaParStageItemIn);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParStageItemIn getAeaParStageItemInById(String id) {
        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStageItemInMapper.getAeaParStageItemInById(id);
    }

    @Override
    public List<AeaParStageItemIn> listAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn) {
        List<AeaParStageItemIn> list = aeaParStageItemInMapper.listAeaParStageItemIn(aeaParStageItemIn);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchSaveStageItemIn(String inId, String[] stageItemIds) {

        // 查询阶段事项列表
        aeaParStageItemInMapper.deleteAeaParStageItemInByInId(inId);
        //保存数据
        if(stageItemIds.length!=0){
            for (String stageItemId : stageItemIds) {
                AeaParStageItemIn aeaParStageItemIn = new AeaParStageItemIn();
                aeaParStageItemIn.setItemInId(UUID.randomUUID().toString());
                aeaParStageItemIn.setInId(inId);
                aeaParStageItemIn.setStageItemId(stageItemId);
                aeaParStageItemIn.setCreateTime(new Date());
                aeaParStageItemIn.setCreater(SecurityContext.getCurrentUserId());
                aeaParStageItemInMapper.insertAeaParStageItemIn(aeaParStageItemIn);
            }
        }
    }

    @Override
    public void batchSaveStageItemInByStageItemId(String stageItemId, String[] inIds) {

        //先删除
        deleteAeaParStageItemInByStageItemId(stageItemId);
        //存入
        if(inIds!=null&&inIds.length>0){
            for (String inId : inIds) {
                AeaParStageItemIn aeaParStageItemIn = new AeaParStageItemIn();
                aeaParStageItemIn.setItemInId(UUID.randomUUID().toString());
                aeaParStageItemIn.setInId(inId);
                aeaParStageItemIn.setStageItemId(stageItemId);
                aeaParStageItemIn.setCreateTime(new Date());
                aeaParStageItemIn.setCreater(SecurityContext.getCurrentUserId());
                saveAeaParStageItemIn(aeaParStageItemIn);
            }
        }
    }

    @Override
    public void deleteAeaParStageItemInByStageItemId(String stageItemId) {
        aeaParStageItemInMapper.deleteAeaParStageItemInByStageItemId(stageItemId);
    }

    @Override
    public List<AeaParStageItemIn> listAeaParStageItemInByMatOrCertId(String matOrCertId, String stageId, String parStateId, String fileType, String isStateIn) {
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setStageId(stageId);

        if (StringUtils.isNotBlank(parStateId)) {
            aeaParIn.setParStateId(parStateId);
        }

        if (StringUtils.isNotBlank(isStateIn)) {
            aeaParIn.setIsStateIn(isStateIn);
        }

        if (StringUtils.isNotBlank(fileType) && MindType.CERTIFICATE.getValue().equals(fileType)) {
            aeaParIn.setFileType(MindType.CERTIFICATE.getValue());
            aeaParIn.setCertId(matOrCertId);
        } else {
            aeaParIn.setFileType(MindType.MATERIAL.getValue());
            aeaParIn.setMatId(matOrCertId);
        }

        List<AeaParIn> parIns = aeaParInMapper.listAeaParIn(aeaParIn);

        if (parIns != null && parIns.size() > 0) {
            return aeaParStageItemInMapper.listAeaParStageItemInByInId(parIns.get(0).getInId());
        }
        return null;
    }

    private Long getMaxSortNo() {
        Long sortNo = aeaParStageItemInMapper.getMaxSortNo();
        return sortNo == null ? 1L : sortNo + 1;
    }


}

