package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.mapper.AeaParFrontItemMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemService;
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
 */
@Service
@Transactional
public class AeaParFrontItemServiceImpl implements AeaParFrontItemService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemServiceImpl.class);

    @Autowired
    private AeaParFrontItemMapper aeaParFrontItemMapper;

    @Override
    public void saveAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception {

        aeaParFrontItem.setCreateTime(new Date());
        aeaParFrontItem.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontItemMapper.insertAeaParFrontItem(aeaParFrontItem);
    }

    @Override
    public void updateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception {

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

        aeaParFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
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

        aeaParFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParFrontItem> list = aeaParFrontItemMapper.listAeaParFrontItem(aeaParFrontItem);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaParFrontItem> listAeaParFrontItemVoByPage(AeaParFrontItem aeaParFrontItem, Page page) throws Exception {

        aeaParFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontItem> list = aeaParFrontItemMapper.listAeaParFrontItemVo(aeaParFrontItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParFrontItem>(list);
    }

    @Override
    public Long getMaxSortNo(String stageId, String rootOrgId) throws Exception {

        Long sortNo = aeaParFrontItemMapper.getMaxSortNo(stageId, rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public AeaParFrontItem getAeaParFrontItemVoByFrontItemId(String frontItemId) throws Exception {

        if (StringUtils.isBlank(frontItemId)) {
            throw new InvalidParameterException(frontItemId);
        }
        return aeaParFrontItemMapper.getAeaParFrontItemVoByFrontItemId(frontItemId);
    }

    @Override
    public List<AeaParFrontItem> listAeaParFrontItemByStageId(String stageId) {

        List<AeaParFrontItem> aeaParFrontItemVos = new ArrayList();
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
    public List<AeaParFrontItem> listAeaParFrontItemVoByNoPage(AeaParFrontItem aeaParFrontItem) throws Exception{

        aeaParFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParFrontItem> list = aeaParFrontItemMapper.listAeaParFrontItemVo(aeaParFrontItem);
        return list;
    }

    @Override
    public void batchSaveFrontItem(String stageId, String[] itemVerIds, Long[] sortNos){

        if (StringUtils.isNotBlank(stageId)) {
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            if(itemVerIds!=null&&itemVerIds.length>0) {
                // 查找需要删除的
                List<String> needDelIdList = new ArrayList<String>();
                List<AeaParFrontItem> needDelList = new ArrayList<AeaParFrontItem>();
                AeaParFrontItem sfrontItem = new AeaParFrontItem();
                sfrontItem.setStageId(stageId);
                sfrontItem.setRootOrgId(rootOrgId);
                List<AeaParFrontItem> frontItemList = aeaParFrontItemMapper.listAeaParFrontItem(sfrontItem);
                if(frontItemList!=null&&frontItemList.size()>0){
                    for(AeaParFrontItem item : frontItemList){
                        int count=0;
                        for (String itemVerId : itemVerIds) {
                            if(item.getItemVerId().equals(itemVerId)){
                                break;
                            }else{
                                count++;
                            }
                        }
                        if(count==itemVerIds.length){
                            needDelList.add(item);
                            needDelIdList.add(item.getFrontItemId());
                        }
                    }
                }
                // 先删除
                if(needDelList!=null&&needDelList.size()>0){

                    frontItemList.removeAll(needDelList);
                    aeaParFrontItemMapper.batchDelAeaItemFrontByIds(needDelIdList);
                }

                // 保存
                for (int i=0; i<itemVerIds.length;i++) {
                    AeaParFrontItem updateVo = null;
                    if (frontItemList != null && frontItemList.size() > 0) {
                        for (AeaParFrontItem item : frontItemList) {
                            if(item.getItemVerId().equals(itemVerIds[i])){
                                updateVo = item;
                                break;
                            }
                        }
                    }
                    if(updateVo==null){
                        AeaParFrontItem itemFront = new AeaParFrontItem();
                        itemFront.setFrontItemId(UUID.randomUUID().toString());
                        itemFront.setStageId(stageId);
                        itemFront.setItemVerId(itemVerIds[i]);
                        itemFront.setSortNo(new Long(sortNos[i]));
                        itemFront.setIsActive(Status.ON);
                        itemFront.setCreater(userId);
                        itemFront.setCreateTime(new Date());
                        itemFront.setRootOrgId(rootOrgId);
                        aeaParFrontItemMapper.insertAeaParFrontItem(itemFront);
                    }else{
                        updateVo.setModifier(userId);
                        updateVo.setModifyTime(new Date());
                        updateVo.setSortNo(new Long(sortNos[i]));
                        aeaParFrontItemMapper.updateAeaParFrontItem(updateVo);
                    }
                }
            }else{
                aeaParFrontItemMapper.batchDelItemByStageId(stageId, rootOrgId);
            }
        }
    }

    @Override
    public void batchDelItemByStageId(String stageId, String rootOrgId){

        if(StringUtils.isNotBlank(stageId)){
            aeaParFrontItemMapper.batchDelItemByStageId(stageId, rootOrgId);
        }
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaParFrontItemMapper.changIsActive(id, rootOrgId);
    }
}

