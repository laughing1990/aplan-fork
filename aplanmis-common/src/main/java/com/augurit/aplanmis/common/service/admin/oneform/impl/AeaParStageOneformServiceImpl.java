package com.augurit.aplanmis.common.service.admin.oneform.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParStageOneform;
import com.augurit.aplanmis.common.mapper.AeaOneformMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageOneformMapper;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStageOneformService;
import com.augurit.aplanmis.common.vo.ActStoFormVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 */
@Service
@Transactional
public class AeaParStageOneformServiceImpl implements AeaParStageOneformService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageOneformServiceImpl.class);

    @Autowired
    private AeaParStageOneformMapper aeaParStageOneformMapper;

    @Autowired
    private AeaOneformMapper aeaParOneformMapper;

    @Override
    public void saveAeaParStageOneform(AeaParStageOneform aeaParStageOneform) {
        aeaParStageOneformMapper.insertAeaParStageOneform(aeaParStageOneform);

    }

    @Override
    public void updateAeaParStageOneform(AeaParStageOneform aeaParStageOneform) {
        aeaParStageOneformMapper.updateAeaParStageOneform(aeaParStageOneform);
    }

    @Override
    public void deleteAeaParStageOneformById(String id) {
        if (id == null) {
            throw new InvalidParameterException(id);
        }
        aeaParStageOneformMapper.deleteAeaParStageOneform(id);
    }

    @Override
    public EasyuiPageInfo<AeaParStageOneform> getAeaParStageOneformList(String parStageId, Page page) {
        if (StringUtils.isBlank(parStageId)) {
            throw new InvalidParameterException("parStageId为空！");
        }
        PageHelper.startPage(page);
        //获取总表导入列表
        List<AeaParStageOneform> list = aeaParStageOneformMapper.listAeaParStageOneform(parStageId);
        logger.debug("成功执行分页查询！！");


        EasyuiPageInfo<AeaParStageOneform> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    @Override
    public EasyuiPageInfo<AeaParStageItem> getItemFormlist(String parStageId, Page page) {

        if (StringUtils.isBlank(parStageId)) {
            throw new InvalidParameterException("parStageId为空！");
        }
        PageHelper.startPage(page);
        List<AeaParStageItem> list = aeaParStageOneformMapper.listAeaStageItem(parStageId, SecurityContext.getCurrentOrgId());
        for (int i = 0; i < list.size(); i++) {
            AeaParStageItem aeaParStageItem = list.get(i);
            String itemVerId = aeaParStageItem.getItemVerId();
            String formId = aeaParStageItem.getSubFormId();
            String stageId = aeaParStageItem.getStageId();
            ActStoForm actStoForm = aeaParStageOneformMapper.getActStoForm(stageId, itemVerId, formId);
            if (actStoForm != null) {
                aeaParStageItem.setFormName(actStoForm.getFormName());
            }

        }
        logger.debug("成功执行分页查询！！");


        EasyuiPageInfo<AeaParStageItem> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    @Override
    public EasyuiPageInfo<AeaOneform> getAeaParOneformList(AeaOneform aeaParOneform, Page page) {

        PageHelper.startPage(page);
        //获取总表列表
        List<AeaOneform> list = aeaParOneformMapper.listAeaOneformNotInStage(aeaParOneform);
        logger.debug("成功执行分页查询！！");
        EasyuiPageInfo<AeaOneform> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    @Override
    public void addParStageOneform(String parStageId, String oneformId) {
        AeaParStageOneform aeaParStageOneform = new AeaParStageOneform();
        try {
            AeaOneform aeaParOneform = aeaParOneformMapper.getParOneformById(oneformId);
            Double sortNo = aeaParOneform.getSortNo().doubleValue();
            aeaParStageOneform.setStageOneformId(UUID.randomUUID().toString());
            aeaParStageOneform.setParStageId(parStageId);
            aeaParStageOneform.setOneformId(oneformId);
            aeaParStageOneform.setSortNo(sortNo);
            aeaParStageOneform.setIsActive(Status.ON);
            aeaParStageOneform.setCreater(SecurityContext.getCurrentUserName());
            aeaParStageOneform.setCreateTime(new Date());
            aeaParStageOneformMapper.insertAeaParStageOneform(aeaParStageOneform);

        } catch (Exception e) {

            throw new InvalidParameterException("导入失败！");
        }
    }

    @Override
    public AeaParStageOneform getAeaParStageOneformById(String id) {
        if (id == null) {
            throw new InvalidParameterException(id);
        }
        AeaParStageOneform aeaParStageOneform = aeaParStageOneformMapper.getAeaParStageOneformById(id);
        return aeaParStageOneform;
    }


    @Override
    public EasyuiPageInfo<ActStoFormVo> getActStoFormList(ActStoForm actStoForm, Page page) {

        actStoForm.setFormOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        //获取总表列表
        List<ActStoFormVo> list = aeaParStageOneformMapper.listActStoFromNotInStageItem(actStoForm);
        logger.debug("成功执行分页查询！！");
        EasyuiPageInfo<ActStoFormVo> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    @Override
    public List<AeaParStageItem> getAeaParStageItemListNoPage(AeaParStageItem aeaParStageItem){

        if (StringUtils.isBlank(aeaParStageItem.getStageId())) {
            throw new InvalidParameterException("stageId为空！");
        }
        List<AeaParStageItem> list = aeaParStageOneformMapper.listAeaStageItem(aeaParStageItem.getStageId(), SecurityContext.getCurrentOrgId());
        if(list!=null && list.size()>0){
            for(AeaParStageItem stageItem : list){
                String stageId = stageItem.getStageId();
                String itemVerId = stageItem.getItemVerId();
                String formId = stageItem.getSubFormId();
                ActStoForm actStoForm = aeaParStageOneformMapper.getActStoForm(stageId, itemVerId, formId);
                if (actStoForm != null) {
                    stageItem.setFormName(actStoForm.getFormName());
                }
            }
        }
        return list;
    }
}
