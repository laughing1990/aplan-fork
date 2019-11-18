package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.mapper.AeaParFrontItemMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemService;
import com.augurit.aplanmis.common.vo.AeaParFrontItemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 阶段事项前置检测表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:47:49</li>
 * </ul>
 */
@Service
@Transactional
public class AeaParFrontItemServiceImpl implements AeaParFrontItemService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemServiceImpl.class);

    @Autowired
    private AeaParFrontItemMapper aeaParFrontItemMapper;

    @Override
    public void saveAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception {
        checkSame(aeaParFrontItem);

        aeaParFrontItem.setCreateTime(new Date());
        aeaParFrontItem.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontItemMapper.insertAeaParFrontItem(aeaParFrontItem);
    }

    @Override
    public void updateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception {
//        checkSame(aeaParFrontItem);

        aeaParFrontItem.setModifyTime(new Date());
        aeaParFrontItem.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontItemMapper.updateAeaParFrontItem(aeaParFrontItem);
    }

    @Override
    public void deleteAeaParFrontItemById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontItemId : ids) {
            aeaParFrontItemMapper.deleteAeaParFrontItem(frontItemId);
        }
    }

    @Override
    public PageInfo<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontItem> list = aeaParFrontItemMapper.listAeaParFrontItem(aeaParFrontItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontItem getAeaParFrontItemById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontItemMapper.getAeaParFrontItemById(id);
    }

    @Override
    public List<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception {
        List<AeaParFrontItem> list = aeaParFrontItemMapper.listAeaParFrontItem(aeaParFrontItem);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaParFrontItemVo> listAeaParFrontItemVoByPage(AeaParFrontItem aeaParFrontItem, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontItemVo> list = aeaParFrontItemMapper.listAeaParFrontItemVo(aeaParFrontItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(AeaParFrontItem aeaParFrontItem) throws Exception {
        Long sortNo = aeaParFrontItemMapper.getMaxSortNo(aeaParFrontItem);
        if (sortNo == null) {
            sortNo = 1l;
        } else {
            sortNo = sortNo + 1;
        }

        return sortNo;
    }

    @Override
    public AeaParFrontItemVo getAeaParFrontItemVoByFrontItemId(String frontItemId) throws Exception {
        if (StringUtils.isBlank(frontItemId)) {
            throw new InvalidParameterException(frontItemId);
        }
        return aeaParFrontItemMapper.getAeaParFrontItemVoByFrontItemId(frontItemId);
    }

    @Override
    public List<AeaParFrontItemVo> listAeaParFrontItemByStageId(String stageId) {
        List<AeaParFrontItemVo> aeaParFrontItemVos = new ArrayList();
        if (StringUtils.isBlank(stageId)) {
            return aeaParFrontItemVos;
        }
        aeaParFrontItemVos.addAll(aeaParFrontItemMapper.listAeaParFrontItemByStageId(stageId, SecurityContext.getCurrentOrgId()));
        return aeaParFrontItemVos;
    }

    private void checkSame(AeaParFrontItem aeaParFrontItem) throws Exception {
        AeaParFrontItem queryParFrontItem = new AeaParFrontItem();
        queryParFrontItem.setStageId(aeaParFrontItem.getStageId());
        queryParFrontItem.setItemVerId(aeaParFrontItem.getItemVerId());
        List<AeaParFrontItem> list = aeaParFrontItemMapper.listAeaParFrontItem(queryParFrontItem);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置事项检测!");
        }

    }

    @Override
    public void batchSaveAeaParFrontItem(String stageId,String itemVerIds)throws Exception{
        if(StringUtils.isBlank(stageId)  || StringUtils.isBlank(itemVerIds)){
            throw new InvalidParameterException(stageId,itemVerIds);
        }

        String[] itemVerIdArr = itemVerIds.split(",");
        if(itemVerIdArr!=null && itemVerIdArr.length>0){
            AeaParFrontItem query = new AeaParFrontItem();
            query.setStageId(stageId);
            Long maxSortNo = getMaxSortNo(query);
            for(String itemVerId:itemVerIdArr){
                AeaParFrontItem aeaParFrontItem = new AeaParFrontItem();
                aeaParFrontItem.setStageId(stageId);
                aeaParFrontItem.setItemVerId(itemVerId);
                List<AeaParFrontItem> list = aeaParFrontItemMapper.listAeaParFrontItem(aeaParFrontItem);
                if (list.size() > 0) {
                    continue;
                }
                aeaParFrontItem.setFrontItemId(UUID.randomUUID().toString());
                aeaParFrontItem.setCreateTime(new Date());
                aeaParFrontItem.setCreater(SecurityContext.getCurrentUserId());
                aeaParFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaParFrontItem.setSortNo(maxSortNo);
                aeaParFrontItem.setIsActive("1");
                aeaParFrontItemMapper.insertAeaParFrontItem(aeaParFrontItem);
                maxSortNo++;
            }
        }
    }

    @Override
    public List<AeaParFrontItemVo> listAeaParFrontItemVoByNoPage(AeaParFrontItem aeaParFrontItem) throws Exception{
        List<AeaParFrontItemVo> list = aeaParFrontItemMapper.listAeaParFrontItemVo(aeaParFrontItem);
        return list;
    }


}

