package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParInAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateFormAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 阶段输入定义表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParInAdminServiceImpl implements AeaParInAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParInAdminServiceImpl.class);

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private AeaItemMatAdminService aeaItemMatAdminService;

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;

    @Autowired
    private AeaParStageItemInMapper aeaParStageItemInMapper;

    @Autowired
    private AeaParStateFormMapper aeaParStateFormMapper;

    @Autowired
    private AeaParStateFormAdminService aeaParStateFormAdminService;

    @Override
    public void saveAeaParIn(AeaParIn aeaParIn) {

        aeaParIn.setCreater(SecurityContext.getCurrentUserId());
        aeaParIn.setCreateTime(new Date());
        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParInMapper.insertAeaParIn(aeaParIn);
    }

    @Override
    public void updateAeaParIn(AeaParIn aeaParIn) {

        aeaParIn.setModifier(SecurityContext.getCurrentUserId());
        aeaParIn.setModifyTime(new Date());
        aeaParInMapper.updateAeaParIn(aeaParIn);
    }

    @Override
    public void updateSortNo(String inId, Long sortNo) {

        aeaParInMapper.updateSortNo(inId, sortNo);
    }


    @Override
    public void deleteAeaParInById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaParInMapper.deleteAeaParIn(id);
        }
    }

    @Override
    public void batchDeleteAeaParInByIds(String[] ids){

        if(ids!=null&&ids.length>0){
            for(String id:ids){
                aeaParInMapper.deleteAeaParIn(id);
            }
        }
    }

    @Override
    public void batchDelMatCertFormByInIdsAndTypes(String[] ids, String[] fileTypes){

        if(ids!=null&&ids.length>0){
            for(int i=0; i<ids.length; i++){
                // 表单
                if(fileTypes[i].equals("form")){
                    aeaParStateFormMapper.deleteAeaParStateForm(ids[i]);
                // 材料与证照
                }else{
                    aeaParInMapper.deleteAeaParIn(ids[i]);
                }
            }
        }
    }

    @Override
    public PageInfo<AeaParIn> listAeaParIn(AeaParIn aeaParIn, Page page) {

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParIn> list = aeaParInMapper.listAeaParIn(aeaParIn);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParIn getAeaParInById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            return aeaParInMapper.getAeaParInById(id);
        } else {
            return null;
        }
    }

    @Override
    public List<AeaParIn> listAeaParIn(AeaParIn aeaParIn) {

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParIn> list = aeaParInMapper.listAeaParIn(aeaParIn);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaParIn> listInStateMatByStageId(String stageId, String keyword) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParInMapper.listInStateMatByStageId(stageId, keyword, rootOrgId);
    }

    @Override
    public List<AeaParIn> listInStateMatByStageIdAndStateId(String stageId, String stateId, String keyword) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParInMapper.listInStateMatByStageIdAndStateId(stageId, stateId, keyword, rootOrgId);
    }

    @Override
    public List<AeaParIn> listInStateCertByStageId(String stageId, String keyword) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParInMapper.listInStateCertByStageId(stageId, keyword, rootOrgId);
    }

    @Override
    public List<AeaParIn> listInStateCertByStageIdAndStateId(String stageId, String stateId, String keyword) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParInMapper.listInStateCertByStageIdAndStateId(stageId, stateId, keyword, rootOrgId);
    }

    @Override
    public void batchSaveStageNoStateMatIn(String stageId, String[] matIds) {

        if (StringUtils.isNotBlank(stageId) && matIds != null && matIds.length > 0) {
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for (String matId : matIds) {
                AeaParIn in2 = new AeaParIn();
                in2.setInId(UUID.randomUUID().toString());
                in2.setStageId(stageId);
                in2.setIsOwner(Status.ON);
                in2.setIsStateIn(Status.OFF);
                in2.setIsDeleted(Status.OFF);
                in2.setFileType(MindType.MATERIAL.getValue());
                in2.setMatId(matId);
                in2.setCreater(userId);
                in2.setCreateTime(new Date());
                Long inSortNo = getMaxSortNoByStageId(stageId);
                Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                in2.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                in2.setRootOrgId(rootOrgId);
                aeaParInMapper.insertAeaParIn(in2);
            }
        }
    }

    @Override
    public void batchSaveStageStateMatIn(String stageId, String stageStateId, String[] matIds) {

        if (StringUtils.isNotBlank(stageId) && StringUtils.isNotBlank(stageStateId) && matIds != null && matIds.length > 0) {
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for (String matId : matIds) {
                AeaParIn in2 = new AeaParIn();
                in2.setInId(UUID.randomUUID().toString());
                in2.setStageId(stageId);
                in2.setIsOwner(Status.ON);
                in2.setIsStateIn(Status.ON);
                in2.setIsDeleted(Status.OFF);
                in2.setParStateId(stageStateId);
                in2.setFileType(MindType.MATERIAL.getValue());
                in2.setMatId(matId);
                in2.setCreater(userId);
                in2.setCreateTime(new Date());
                Long inSortNo = getMaxSortNoByStageId(stageId);
                Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                in2.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                in2.setRootOrgId(rootOrgId);
                aeaParInMapper.insertAeaParIn(in2);
            }
        }
    }

    @Override
    public void batchSaveStageNoStateCertIn(String stageId, String[] certIds) {

        if (StringUtils.isNotBlank(stageId)) {
            // 先删除阶段非情形电子证照
            AeaParIn in = new AeaParIn();
            in.setStageId(stageId);
            in.setIsStateIn(Status.OFF);
            in.setFileType(MindType.CERTIFICATE.getValue());
            List<AeaParIn> list = aeaParInMapper.listAeaParIn(in);
            if (list != null && list.size() > 0) {
                for (AeaParIn vo : list) {
                    aeaParInMapper.deleteAeaParIn(vo.getInId());
                }
            }
            if(certIds != null && certIds.length > 0){
                String userId = SecurityContext.getCurrentUserId();
                // 再增加
                for (String certId : certIds) {
                    AeaParIn in2 = new AeaParIn();
                    in2.setInId(UUID.randomUUID().toString());
                    in2.setStageId(stageId);
                    in2.setIsOwner(Status.ON);
                    in2.setIsStateIn(Status.OFF);
                    in2.setIsDeleted(Status.OFF);
                    in2.setFileType(MindType.CERTIFICATE.getValue());
                    in2.setCertId(certId);
                    in2.setCreater(userId);
                    in2.setCreateTime(new Date());
                    Long inSortNo = getMaxSortNoByStageId(stageId);
                    Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                    in2.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                    in2.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaParInMapper.insertAeaParIn(in2);
                }
            }
        }
    }

    @Override
    public void batchSaveStageNoStateCertInNotDelOld(String stageId, String[] certIds){

        if (StringUtils.isBlank(stageId)) {
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(certIds != null && certIds.length > 0){
            String userId = SecurityContext.getCurrentUserId();
            // 再增加
            for (String certId : certIds) {
                AeaParIn in2 = new AeaParIn();
                in2.setInId(UUID.randomUUID().toString());
                in2.setStageId(stageId);
                in2.setIsOwner(Status.ON);
                in2.setIsStateIn(Status.OFF);
                in2.setIsDeleted(Status.OFF);
                in2.setFileType(MindType.CERTIFICATE.getValue());
                in2.setCertId(certId);
                in2.setCreater(userId);
                in2.setCreateTime(new Date());
                Long inSortNo = getMaxSortNoByStageId(stageId);
                Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                in2.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                in2.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaParInMapper.insertAeaParIn(in2);
            }
        }
    }

    @Override
    public void batchSaveStageStateCertIn(String stageId, String stageStateId, String[] certIds) {

        if (StringUtils.isNotBlank(stageId) && StringUtils.isNotBlank(stageStateId)) {
            AeaParIn in = new AeaParIn();
            in.setStageId(stageId);
            in.setIsStateIn(Status.ON);
            in.setParStateId(stageStateId);
            in.setFileType(MindType.CERTIFICATE.getValue());
            List<AeaParIn> list = aeaParInMapper.listAeaParIn(in);
            if (list != null && list.size() > 0) {
                for (AeaParIn vo : list) {
                    aeaParInMapper.deleteAeaParIn(vo.getInId());
                }
            }
            // 再增加
            if(certIds != null && certIds.length > 0){
                String userId = SecurityContext.getCurrentUserId();
                for (String certId : certIds) {
                    AeaParIn in2 = new AeaParIn();
                    in2.setInId(UUID.randomUUID().toString());
                    in2.setStageId(stageId);
                    in2.setIsOwner(Status.ON);
                    in2.setIsDeleted(Status.OFF);
                    in2.setIsStateIn(Status.ON);
                    in2.setParStateId(stageStateId);
                    in2.setFileType(MindType.CERTIFICATE.getValue());
                    in2.setCertId(certId);
                    in2.setCreater(userId);
                    in2.setCreateTime(new Date());
                    Long inSortNo = getMaxSortNoByStageId(stageId);
                    Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                    in2.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                    in2.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaParInMapper.insertAeaParIn(in2);
                }
            }
        }
    }

    @Override
    public AeaParIn saveStageNoStateMatIn(HttpServletRequest request, String stageId,
                                          String inId, AeaItemMat mat) throws Exception {
        AeaParIn in = null;
        if (StringUtils.isNotBlank(stageId)) {
            // 首先保存材料
            String matId;
            if (StringUtils.isNotBlank(mat.getMatId())) {
                aeaItemMatAdminService.updateAeaItemMat(request, mat);

                in = aeaParInMapper.getAeaParInById(inId);
                if (in != null) {
                    in.setAeaMatCertName(mat.getMatName());
                    updateAeaParIn(in);
                }
            } else {
                matId = UUID.randomUUID().toString();
                mat.setMatId(matId);
                aeaItemMatAdminService.saveAeaItemMat(request, mat);

                // 创建in关联
                in = new AeaParIn();
                in.setInId(UUID.randomUUID().toString());
                in.setStageId(stageId);
                in.setIsOwner(Status.ON);
                in.setIsDeleted(Status.OFF);
                in.setIsStateIn(Status.OFF);
                in.setFileType(MindType.MATERIAL.getValue());
                in.setMatId(matId);
                in.setAeaMatCertName(mat.getMatName());
                Long inSortNo = getMaxSortNoByStageId(stageId);
                Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                in.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                saveAeaParIn(in);
            }
        }
        return in;
    }

    @Override
    public AeaParIn saveStageStateMatIn(HttpServletRequest request, String stageId,
                                        String stageStateId, String inId, AeaItemMat mat) throws Exception {
        AeaParIn in = null;
        if (StringUtils.isNotBlank(stageId) && StringUtils.isNotBlank(stageStateId)) {
            // 首先保存材料
            String matId;
            if (StringUtils.isNotBlank(mat.getMatId())) {
                aeaItemMatAdminService.updateAeaItemMat(request, mat);

                in = aeaParInMapper.getAeaParInById(inId);
                if (in != null) {
                    in.setAeaMatCertName(mat.getMatName());
                    updateAeaParIn(in);
                }
            } else {
                matId = UUID.randomUUID().toString();
                mat.setMatId(matId);
                aeaItemMatAdminService.saveAeaItemMat(request, mat);

                // 创建in关联
                in = new AeaParIn();
                in.setInId(UUID.randomUUID().toString());
                in.setStageId(stageId);
                in.setIsOwner(Status.ON);
                in.setIsDeleted(Status.OFF);
                in.setIsStateIn(Status.ON);
                in.setParStateId(stageStateId);
                in.setFileType(MindType.MATERIAL.getValue());
                in.setMatId(matId);
                in.setAeaMatCertName(mat.getMatName());
                Long inSortNo = getMaxSortNoByStageId(stageId);
                Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                in.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                saveAeaParIn(in);
            }
        }
        return in;
    }

    @Override
    public PageInfo<AeaParIn> listInStateMatCertByStageIdAndStateId(String stageId, String parStateId, String keyword, Page page) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParIn> list = new ArrayList<>();
        PageHelper.startPage(page);
        List<AeaParIn> matList = aeaParInMapper.listInStateMatByStageIdAndStateId(stageId, parStateId, keyword, rootOrgId);
        List<AeaParIn> certList = aeaParInMapper.listInStateCertByStageIdAndStateId(stageId, parStateId, keyword, rootOrgId);
        if (matList != null && matList.size() > 0) {
            list.addAll(matList);
        }
        if (certList != null && certList.size() > 0) {
            list.addAll(certList);
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaParIn> listNoStateInMatCertByStageId(String stageId, String keyword, Page page) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParIn> list = new ArrayList<>();
        PageHelper.startPage(page);
        List<AeaParIn> matList = aeaParInMapper.listNoStateInMatByStageId(stageId, keyword, rootOrgId);
        List<AeaParIn> certList = aeaParInMapper.listNoStateInCertByStageId(stageId, keyword, rootOrgId);
        if (matList != null && matList.size() > 0) {
            list.addAll(matList);
        }
        if (certList != null && certList.size() > 0) {
            list.addAll(certList);
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaParIn> listNoStateInAndItemByStageId(String stageId, String keyword, Page page) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        PageHelper.startPage(page);
        List<AeaParIn> parInList = aeaParInMapper.listNoStateInCertAndMatByStageId(stageId, keyword, rootOrgId);
        collectItemNames(parInList, rootOrgId);
        return new PageInfo<>(parInList);
    }

    private void collectItemNames(List<AeaParIn> parInList, String rootOrgId) {

        if (CollectionUtils.isNotEmpty(parInList)) {
            List<String> inIds = new ArrayList<String>();
            for (AeaParIn in : parInList) {
                inIds.add(in.getInId());
            }
            List<AeaParStageItem> stageItemList = aeaParStageItemMapper.listAeaStageItemByParIns(inIds, rootOrgId);
            if (CollectionUtils.isNotEmpty(stageItemList)) {
                for (AeaParIn in : parInList) {
                    StringBuffer itemNames = new StringBuffer();
                    List<AeaParStageItem> needRemove = new ArrayList<>();
                    for (AeaParStageItem item : stageItemList) {
                        if(StringUtils.isNotBlank(item.getInId())&&in.getInId().equals(item.getInId())){
                            needRemove.add(item);
                            String itemName = item.getItemName();
                            if (StringUtils.isNotBlank(item.getIsCatalog())) {
                                // 标准事项
                                if(item.getIsCatalog().equals(Status.ON)){
                                    itemName = ("【标准事项】" + itemName);
                                    if(StringUtils.isNotBlank(item.getGuideOrgName())){
                                        itemName = (itemName + "【" + item.getGuideOrgName() + "】");
                                    }
                                // 实施事项
                                }else{
                                    itemName = ("【实施事项】" + itemName);
                                    if(StringUtils.isNotBlank(item.getOrgName())){
                                        itemName = (itemName + "【" + item.getOrgName() + "】");
                                    }
                                }
                            }
                            itemNames.append(itemName).append("、");
                        }
                    }
                    if(StringUtils.isNotBlank(itemNames.toString())){
                        in.setItemName(itemNames.toString().substring(0, itemNames.toString().lastIndexOf("、")));
                    }
                    stageItemList.removeAll(needRemove);
                }
            }
        }
    }

    @Override
    public PageInfo<AeaParIn> listParInAndItemByStageId(String stageId, String keyword, Page page) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        PageHelper.startPage(page);
        List<AeaParIn> parInList = aeaParInMapper.lisCertAndMatByStageId(stageId, keyword, rootOrgId);
        collectItemNames(parInList, rootOrgId);
        return new PageInfo<>(parInList);
    }

    @Override
    public List<AeaParIn> listParInByStageItemId(String stageId, String stageItemId, String keyword) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setStageId(stageId);
        aeaParIn.setKeyword(keyword);
        aeaParIn.setRootOrgId(rootOrgId);
        List<AeaParIn> aeaparInList = aeaParInMapper.listStageInMatCert(aeaParIn);
        if (aeaparInList != null && aeaparInList.size() > 0) {
            AeaParStageItemIn aeaParStageItemIn = new AeaParStageItemIn();
            aeaParStageItemIn.setStageItemId(stageItemId);
            List<AeaParStageItemIn> itemInList = aeaParStageItemInMapper.listAeaParStageItemIn(aeaParStageItemIn);
            if (itemInList != null && itemInList.size() > 0) {
                for (AeaParIn in : aeaparInList) {
                    for (AeaParStageItemIn itemIn : itemInList) {
                        if (in.getInId().equals(itemIn.getInId())) {
                            in.setCheck(true);
                            break;
                        } else {
                            in.setCheck(false);
                        }
                    }
                }

            }
        }
        return aeaparInList;
    }

    @Override
    public void addGlobalMatParIn(String[] matIds, String stageId, String isStateIn, String parStateId) {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParIn parIn = new AeaParIn();
        parIn.setStageId(stageId);
        parIn.setIsStateIn(isStateIn);
        if (StringUtils.isNotBlank(parStateId)) {
            parIn.setParStateId(parStateId);
        }
        parIn.setIsDeleted(Status.OFF);
        parIn.setRootOrgId(rootOrgId);
        List<AeaParIn> matIdList = aeaParInMapper.listAeaParIn(parIn);
        for (String id : matIds) {
            if (StringUtils.isNotBlank(id)) {
                boolean sign = true;
                for (AeaParIn aMatIdList : matIdList) {
                    if (id.equals(aMatIdList.getMatId())) {
                        sign = false;
                        break;
                    }
                }
                if (sign) {
                    AeaParIn aeaParIn = new AeaParIn();
                    aeaParIn.setMatId(id);
                    aeaParIn.setStageId(stageId);
                    aeaParIn.setInId(UUID.randomUUID().toString());
                    aeaParIn.setFileType(MindType.MATERIAL.getValue());
                    aeaParIn.setIsOwner(Status.ON);
                    aeaParIn.setCreater(userId);
                    aeaParIn.setCreateTime(new Date());
                    aeaParIn.setIsStateIn(isStateIn);
                    aeaParIn.setParStateId(parStateId);
                    aeaParIn.setIsDeleted(Status.OFF);
                    Long inSortNo = getMaxSortNoByStageId(stageId);
                    Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                    aeaParIn.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                    aeaParIn.setRootOrgId(rootOrgId);
                    aeaParInMapper.insertAeaParIn(aeaParIn);
                }
            }
        }
        logger.debug("成功执行为阶段添加输入材料！！");
    }

    @Override
    public void addGlobalCertParIn(String[] certIds, String stageId, String isStateIn, String parStateId) {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParIn parIn = new AeaParIn();
        if (StringUtils.isNotBlank(parStateId)) {
            parIn.setParStateId(parStateId);
        }
        parIn.setStageId(stageId);
        parIn.setIsStateIn(isStateIn);
        parIn.setIsDeleted("0");
        parIn.setRootOrgId(rootOrgId);
        List<AeaParIn> certIdList = aeaParInMapper.listAeaParIn(parIn);
        for (String id : certIds) {
            if (StringUtils.isNotBlank(id)) {
                boolean sign = true;
                for (AeaParIn aCertIdList : certIdList) {
                    if (id.equals(aCertIdList.getCertId())) {
                        sign = false;
                        break;
                    }
                }
                if (sign) {
                    AeaParIn aeaParIn = new AeaParIn();
                    aeaParIn.setCertId(id);
                    aeaParIn.setStageId(stageId);
                    aeaParIn.setInId(UUID.randomUUID().toString());
                    aeaParIn.setFileType(MindType.CERTIFICATE.getValue());
                    aeaParIn.setIsOwner(Status.ON);
                    aeaParIn.setCreater(userId);
                    aeaParIn.setCreateTime(new Date());
                    aeaParIn.setIsStateIn(isStateIn);
                    aeaParIn.setParStateId(parStateId);
                    aeaParIn.setIsDeleted(Status.OFF);
                    Long inSortNo = getMaxSortNoByStageId(stageId);
                    Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                    aeaParIn.setSortNo(inSortNo>=formSortNo?inSortNo:formSortNo);
                    aeaParIn.setRootOrgId(rootOrgId);
                    aeaParInMapper.insertAeaParIn(aeaParIn);
                }
            }
        }
        logger.debug("成功执行为阶段添加证照！！");
    }


    @Override
    public List<String> getCertListByStateId(String parStateId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setParStateId(parStateId);
        aeaParIn.setIsOwner(Status.ON);
        aeaParIn.setFileType(MindType.CERTIFICATE.getValue());
        aeaParIn.setIsStateIn(Status.ON);
        aeaParIn.setIsDeleted(Status.OFF);
        aeaParIn.setRootOrgId(rootOrgId);
        List<AeaParIn> aeaParInList = aeaParInMapper.listAeaParIn(aeaParIn);
        List<String> list = new ArrayList<>();
        if (aeaParInList != null&& aeaParInList.size()>0) {
            for (AeaParIn parIn : aeaParInList) {
                list.add(parIn.getCertId());
            }
        }
        return list;
    }

    @Override
    public List<String> listCertListByStageId(String stageId){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParInMapper.listCertListByStageId(stageId, rootOrgId);
    }

    @Override
    public List<String> getGlobalMatListByStateId(String parStateId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParInMapper.getGlobalMatListByStateId(parStateId, rootOrgId);
    }

    @Override
    public void deleteAeaParIn(String inId) {

        if (StringUtils.isNotBlank(inId)) {
            aeaParStageItemInMapper.deleteAeaParStageItemInByInId(inId);
            aeaParInMapper.deleteAeaParIn(inId);
        }
    }


    @Override
    public PageInfo<AeaParIn> listStageMatByStageId(String stageId, String stateId, String keyword, Boolean isCommon, String isStateIn, Page page) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        PageHelper.startPage(page);
        List<AeaParIn> matList = aeaParInMapper.listStageMatNew(stageId, stateId, (isCommon != null && isCommon) ? Status.ON : Status.OFF, isStateIn, keyword, rootOrgId, null);
        return new PageInfo<>(matList);
    }

    @Override
    public void updateStageStateParIn(String stageId, String stateId, String inIds) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParIn> matList = aeaParInMapper.listInStateStageMat(stageId, stateId, Status.OFF, null, rootOrgId);
        if (matList != null && !matList.isEmpty()) {
            for (AeaParIn parIn : matList) {
                aeaParInMapper.updateParStateIdNull(parIn.getInId());
            }
        }
        if (inIds != null && StringUtils.isNotBlank(inIds)) {
            String[] ids = inIds.split(CommonConstant.COMMA_SEPARATOR);
            if (ids.length > 0) {
                for (String id : ids) {
                    aeaParInMapper.updateParStateId(id, stateId);
                }
            }
        }
    }

    @Override
    public void deleteMatAndParIn(String inId) {

        AeaParIn aeaParIn = aeaParInMapper.getAeaParInById(inId);
        if (aeaParIn != null) {
            AeaItemMat aeaItemMat = aeaItemMatMapper.selectOneById(aeaParIn.getMatId());
            aeaParInMapper.deleteAeaParIn(inId);
            if (aeaItemMat != null) {
                if (Status.OFF.equals(aeaItemMat.getIsGlobalShare())) {
                    aeaItemMatMapper.deleteAeaItemMatById(aeaItemMat.getMatId());
                }
            }
        }
    }

    @Override
    public void batchDeleteMatAndParIns(String[] inIds){

        if(inIds!=null&&inIds.length>0){
           for(String inId: inIds){
               if(StringUtils.isNotBlank(inId)){
                   deleteMatAndParIn(inId);
               }
           }
        }
    }

    @Override
    public List<String> getGlobalMatListByStageIdAndParStateId(String stageId, String parStateId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParInMapper.getGlobalMatListByStageIdAndParStateId(stageId, parStateId, rootOrgId);
    }

    @Override
    public PageInfo<AeaParIn> listStageInMatCert(AeaParIn aeaParIn, Page page){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaParIn.setRootOrgId(rootOrgId);
        PageHelper.startPage(page);
        List<AeaParIn> list = aeaParInMapper.listStageInMatCert(aeaParIn);
        collectItemNames(list, rootOrgId);
        return new PageInfo<AeaParIn>(list);
    }

    @Override
    public PageInfo<AeaParIn> listStageInMatCertForm(AeaParIn aeaParIn, Page page){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaParIn.setRootOrgId(rootOrgId);
        PageHelper.startPage(page);
        List<AeaParIn> list = aeaParInMapper.listStageInMatCertForm(aeaParIn);
        collectItemNames(list, rootOrgId);
        return new PageInfo<AeaParIn>(list);
    }

    @Override
    public List<AeaParIn> listStageInMatCertForm(AeaParIn aeaParIn){

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParIn> list = aeaParInMapper.listStageInMatCertForm(aeaParIn);
        return list;
    }

    @Override
    public List<AeaParIn> listStoMatByCondition(AeaParIn aeaParIn){

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaParInMapper.listStoMatByCondition(aeaParIn);
    }

    @Override
    public Long getMaxSortNoByStageId(String stageId){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        Long sortNo = aeaParInMapper.getMaxSortNoByStageId(stageId, rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }
}

