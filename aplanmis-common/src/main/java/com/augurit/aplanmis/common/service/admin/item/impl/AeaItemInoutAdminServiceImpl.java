package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.mapper.AeaItemStateFormMapper;
import com.augurit.aplanmis.common.mapper.AeaParShareMatMapper;
import com.augurit.aplanmis.common.qo.item.ItemMatInoutQo;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemStateFormAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author ZhangXinhui
 * @date 2019/7/31 031 10:08
 * @desc
 **/
@Service
@Transactional
public class AeaItemInoutAdminServiceImpl implements AeaItemInoutAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemInoutAdminServiceImpl.class);

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaItemStateFormAdminService aeaItemStateFormAdminService;

    @Autowired
    private AeaItemStateFormMapper aeaItemStateFormMapper;

    @Autowired
    private AeaParShareMatMapper aeaParShareMatMapper;

    @Override
    public void saveAeaItemInout(AeaItemInout aeaItemInout) {

        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemInout.setCreater(SecurityContext.getCurrentUserId());
        aeaItemInout.setCreateTime(new Date());
        aeaItemInoutMapper.insertAeaItemInout(aeaItemInout);
    }

    @Override
    public void updateAeaItemInout(AeaItemInout aeaItemInout) {

        aeaItemInout.setModifier(SecurityContext.getCurrentUserId());
        aeaItemInout.setModifyTime(new Date());
        aeaItemInoutMapper.updateAeaItemInout(aeaItemInout);
    }

    @Override
    public void deleteAeaItemInoutById(String id){

        Assert.notNull(id, "id is null.");
        aeaItemInoutMapper.deleteAeaItemInoutById(id);
    }

    @Override
    public List<AeaItemInout> getAeaItemInoutByMatId(String matId, String rootOrgId){

        if (StringUtils.isBlank(matId)) {
            throw new InvalidParameterException(matId);
        }
        logger.debug("根据MAT_ID获取Form对象，matId为：{}", matId);
        return aeaItemInoutMapper.selectOneByMatId(matId, rootOrgId);
    }

    @Override
    public void saveItemInout(List<String> inoutIdList, String stateId, String itemVerId) {

        AeaItemInout param = new AeaItemInout();
        param.setItemVerId(itemVerId);
        param.setItemStateId(stateId);
        List<AeaItemInout> aeaItemInouts = aeaItemInoutMapper.listInoutByItemId(param);
        if (CollectionUtils.isNotEmpty(aeaItemInouts)) {
            for (AeaItemInout inout : aeaItemInouts) {
                boolean isHandled = false;
                for (String inoutId : inoutIdList) {
                    if (inoutId.equals(inout.getInoutId())) {
                        inout.setItemStateId(stateId);
                        inout.setIsDeleted(Status.OFF);
                        isHandled = true;
                        aeaItemInoutMapper.updateAeaItemInout(inout);
                        break;
                    }
                }
                if (!isHandled && StringUtils.isNotBlank(inout.getItemStateId())) {
                    inout.setItemStateId("");
                    aeaItemInoutMapper.updateAeaItemInout(inout);
                }
            }
        }
    }

    @Override
    public PageInfo<AeaItemInout> listAeaItemInout(AeaItemInout aeaItemInout, Page page) {

        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemInout> list = aeaItemInoutMapper.listAeaItemInout(aeaItemInout);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemInout> listAeaItemInout(AeaItemInout aeaItemInout) {

        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemInout> list = aeaItemInoutMapper.listAeaItemInout(aeaItemInout);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaItemInout> listAeaItemInoutMatCert(AeaItemInout aeaItemInout, Page page) {

        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemInout> list = aeaItemInoutMapper.listAeaItemInoutMatCert(aeaItemInout);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaItemInout> listAeaItemMatCertFormByPage(AeaItemInout aeaItemInout, Page page){

        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemInout> list = aeaItemInoutMapper.listAeaItemMatCertForm(aeaItemInout);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemInout> listAeaItemInoutMatCert(AeaItemInout aeaItemInout) {

        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaItemInoutMapper.listAeaItemInoutMatCert(aeaItemInout);
    }

    @Override
    public List<AeaItemInout> listAeaItemInoutMatCertForm(AeaItemInout aeaItemInout) {

        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaItemInoutMapper.listAeaItemMatCertForm(aeaItemInout);
    }

    @Override
    public Long getMaxSortNo(String itemVerId, String itemStateVerId, String isInput, String rootOrgId){

        Long sortNo = aeaItemInoutMapper.getItemInoutMaxSortNo(itemVerId, itemStateVerId, isInput, rootOrgId);
        return sortNo==null?1L : (sortNo+1L);
    }

    @Override
    public void batchSaveItemInoutMatCert(AeaItemInout inout, String[] matCertIds) {

        if (inout != null) {
//            inout.setRootOrgId(SecurityContext.getCurrentOrgId());
//            inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//            if (MindType.CERTIFICATE.getValue().equals(inout.getFileType())) {
//                List<AeaItemInout> list = aeaItemInoutMapper.listAeaItemInout(inout);
//                if (list != null && list.size() > 0) {
//                    for (AeaItemInout inout1 : list) {
//                        aeaItemInoutMapper.deleteAeaItemInoutById(inout1.getInoutId());
//                    }
//                }
//            }
            batchSaveItemInoutMatCertAndNotDelOld(inout, matCertIds);
        }
    }

    @Override
    public void batchSaveItemInoutMatCertAndNotDelOld(AeaItemInout inout,String[] matCertIds){

        if (inout != null) {
            if (matCertIds != null && matCertIds.length > 0) {
                String userId = SecurityContext.getCurrentUserId();
                String rootOrgId = SecurityContext.getCurrentOrgId();
                for (String matCertId : matCertIds) {
                    AeaItemInout vo = new AeaItemInout();
                    vo.setInoutId(UUID.randomUUID().toString());
                    vo.setItemVerId(inout.getItemVerId());
                    vo.setIsInput(inout.getIsInput());
                    vo.setIsStateIn(inout.getIsStateIn());
                    vo.setItemStateId(inout.getItemStateId());
                    vo.setStateVerId(inout.getStateVerId());
                    vo.setFileType(inout.getFileType());
                    vo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                    Long inSortNo = getMaxSortNo(inout.getItemVerId(), inout.getStateVerId(), inout.getIsInput(), rootOrgId);
                    vo.setSortNo(inSortNo);
//                    if (MindType.CERTIFICATE.getValue().equals(inout.getFileType())) {
//                        vo.setCertId(matCertId);
//                    } else {
                        vo.setMatId(matCertId);
//                    }
                    vo.setIsOwner(Status.ON);
                    vo.setCreater(userId);
                    vo.setRootOrgId(rootOrgId);
                    vo.setCreateTime(new Date());
                    aeaItemInoutMapper.insertAeaItemInout(vo);
                }
            }
        }
    }

    @Override
    public void deleteAeaItemInoutCascade(String id) {

        deleteAeaItemInoutById(id);
    }

    @Override
    public void batchDeleteAeaItemInoutCascade(String[] ids) {

        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                this.deleteAeaItemInoutCascade(id);
            }
        }
    }

    @Override
    public void batchDelItemMatCertFormCascade(String[] ids, String[] fileTypes){

        if(ids!=null&&ids.length>0){
            for(int i=0; i<ids.length; i++){
                // 表单
                if(fileTypes[i].equals("form")){
                    aeaItemStateFormMapper.deleteAeaItemStateFormById(ids[i]);
                    // 材料与证照
                }else{
                    deleteAeaItemInoutCascade(ids[i]);
                }
            }
        }
    }

    @Override
    public List<AeaItemInout> listMatAndInoutList(ItemMatInoutQo itemMatInoutQo) {

        List<AeaItemInout> list = aeaItemInoutMapper.listMatAndInoutList(itemMatInoutQo);
        return list;
    }
}
