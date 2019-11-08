package com.augurit.aplanmis.front.subject.project.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class GlobalProjUnitService {

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    public AeaUnitInfo saveAeaUnitInfo(AeaUnitInfo aeaUnitInfo) throws Exception {
        aeaUnitInfo = addAeaUniqueUnitInfo(aeaUnitInfo);
        String unitProjId = addAeaUnitProj(aeaUnitInfo.getProjInfoId(),aeaUnitInfo.getUnitInfoId(),aeaUnitInfo.getIsOwner());
        aeaUnitInfo.setUnitProjId(unitProjId);
        return aeaUnitInfo;
    }

    public AeaUnitInfo saveAeaUnitInfoNew(AeaUnitInfo aeaUnitInfo) throws Exception {
        AeaUnitInfo query=new AeaUnitInfo();
        query.setApplicant(aeaUnitInfo.getApplicant());
        query.setApplicant(aeaUnitInfo.getUnifiedSocialCreditCode());
        query.setUnitType(aeaUnitInfo.getUnitType());
        List<AeaUnitInfo> list = aeaUnitInfoMapper.listAeaUnitInfo(query);
        String unitProjId = null;
        if(list.size()>0){
            // 单位已经存在
            aeaUnitInfo.setUnitInfoId(list.get(0).getUnitInfoId());
            AeaUnitProj unitProj = new AeaUnitProj();
            unitProj.setProjInfoId(aeaUnitInfo.getProjInfoId());
            unitProj.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
            List<AeaUnitProj> aeaUnitProjs = aeaUnitProjMapper.listAeaUnitProj(unitProj);
            if (list.size()>0) {
                // 项目和单位已经有关联了
                unitProjId = aeaUnitProjs.get(0).getUnitProjId();
            } else {
                unitProjId = addAeaUnitProj(aeaUnitInfo.getProjInfoId(),aeaUnitInfo.getUnitInfoId(),aeaUnitInfo.getIsOwner());
            }
        } else {
            String unitinfoId= UUID.randomUUID().toString();
            aeaUnitInfo.setUnitInfoId(unitinfoId);
            aeaUnitInfo.setCreater(SecurityContext.getCurrentUserName());
            aeaUnitInfo.setCreateTime(new Date());
            aeaUnitInfo.setIsDeleted("0");
            aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
            unitProjId = addAeaUnitProj(aeaUnitInfo.getProjInfoId(),aeaUnitInfo.getUnitInfoId(),aeaUnitInfo.getIsOwner());
        }
        aeaUnitInfo.setUnitProjId(unitProjId);
        return aeaUnitInfo;
    }

    public String addAeaUnitProj(String projInfoId,String unitInfoId,String isOwner) throws Exception {
        AeaUnitProj aeaUnitProj = new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projInfoId);
        if (StringUtils.isBlank(isOwner)) {
            aeaUnitProj.setIsOwner("0");
        } else {
            aeaUnitProj.setIsOwner(isOwner);
        }
        aeaUnitProj.setUnitInfoId(unitInfoId);
        aeaUnitProj.setCreater(SecurityContext.getCurrentUserName());
        aeaUnitProj.setCreateTime(new Date());
        aeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
        aeaUnitProj.setIsDeleted("0");
        aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
        return aeaUnitProj.getUnitProjId();
    }

    private AeaUnitInfo addAeaUniqueUnitInfo(AeaUnitInfo aeaUnitInfo) throws Exception {
        AeaUnitInfo query=new AeaUnitInfo();
        query.setApplicant(aeaUnitInfo.getApplicant());
        query.setUnifiedSocialCreditCode(aeaUnitInfo.getUnifiedSocialCreditCode());
        List<AeaUnitInfo> list = aeaUnitInfoMapper.listAeaUnitInfo(query);
        if(list.size()>0){
            throw new Exception("该企业已存在,请直接搜索！");
        }

        String unitinfoId= UUID.randomUUID().toString();
        aeaUnitInfo.setUnitInfoId(unitinfoId);
        aeaUnitInfo.setCreater(SecurityContext.getCurrentUserName());
        aeaUnitInfo.setCreateTime(new Date());
        aeaUnitInfo.setIsDeleted("0");
        if(StringUtils.isNotBlank(aeaUnitInfo.getIsOwner())&&"1".equals(aeaUnitInfo.getIsOwner())){
            aeaUnitInfo.setIsPrimaryUnit("1");//主单位
            aeaUnitInfo.setUnitType("1");//建设
        }else{
            aeaUnitInfo.setIsPrimaryUnit("0");//副单位
            aeaUnitInfo.setUnitType("7");//代办单位
        }
        aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
        return aeaUnitInfo;
    }

    public List<AeaUnitInfo> listAeaUnitInfo(AeaUnitInfo aeaUnitInfo) {
        List<AeaUnitInfo> result=new ArrayList<>();
        List<AeaUnitInfo> list=aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);
        if(list!=null && list.size()>0){
            for (AeaUnitInfo unitInfo:list){
                if("1".equals(unitInfo.getIsDeleted())){
                    continue;
                }
                result.add(unitInfo);
            }
        }
        return result;
    }

    public AeaUnitInfo getAeaUnitInfoByUnitInfoId(String id) {
        return aeaUnitInfoMapper.getAeaUnitInfoById(id);
    }

    public void deleteAeaUnitProj(String unitProjId) throws Exception {
        aeaUnitProjMapper.deleteAeaUnitProj(unitProjId);
    }

    public AeaUnitInfo updateAeaUnitInfo(AeaUnitInfo aeaUnitInfo) throws Exception {
        if(StringUtils.isBlank(aeaUnitInfo.getIsDeleted())){
            aeaUnitInfo.setIsDeleted("0");
        }
        if(StringUtils.isBlank(aeaUnitInfo.getModifier())){
            aeaUnitInfo.setModifier(SecurityContext.getCurrentUserName());
        }
        if(aeaUnitInfo.getModifyTime()==null){
            aeaUnitInfo.setModifyTime(new Date());
        }
        aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
        if(StringUtils.isBlank(aeaUnitInfo.getUnitProjId())) {
            String unitProjId = addAeaUnitProj(aeaUnitInfo.getProjInfoId(),aeaUnitInfo.getUnitInfoId(),aeaUnitInfo.getIsOwner());
            aeaUnitInfo.setUnitProjId(unitProjId);
        }
        return aeaUnitInfo;
    }

    public AeaUnitInfo saveAeaUnitInfoAndUnitProjs(AeaUnitInfo aeaUnitInfo, String[] projInfoIds) throws Exception {
        if (aeaUnitInfo!=null&& StringUtils.isBlank(aeaUnitInfo.getUnitInfoId())){
            aeaUnitInfo = addAeaUniqueUnitInfo(aeaUnitInfo);
        }else{
            if(StringUtils.isBlank(aeaUnitInfo.getIsDeleted())){
                aeaUnitInfo.setIsDeleted("0");
            }
            if(StringUtils.isBlank(aeaUnitInfo.getModifier())){
                aeaUnitInfo.setModifier(SecurityContext.getCurrentUserName());
            }
            if(aeaUnitInfo.getModifyTime()==null){
                aeaUnitInfo.setModifyTime(new Date());
            }
            aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
            if(StringUtils.isNotBlank(aeaUnitInfo.getUnitProjId())) {
                if("1".equals(aeaUnitInfo.getIsOwner())){//建设单位不用新增
                    return aeaUnitInfo;
                }
                //经办单位保证跟每个项目都有关系
                String[] existUnitProjId = aeaUnitInfo.getUnitProjId().split(",");
                ArrayList<String> existProjInfoIdList = new ArrayList<String>();
                ArrayList<String> allProjInfoIdList = new ArrayList<String>();//所有关联关系ID用于回填
                for(String one:existUnitProjId){
                    allProjInfoIdList.add(one);
                    AeaUnitProj existAeaUnitProj = aeaUnitProjMapper.getAeaUnitProjById(one);
                    String projInfoId = existAeaUnitProj.getProjInfoId();
                    existProjInfoIdList.add(projInfoId);
                }
                for(int i=0;i<projInfoIds.length;i++){
                    if(!existProjInfoIdList.contains(projInfoIds[i])){//不存在的关系需要新增
                        String unitProjId = addAeaUnitProj(projInfoIds[i],aeaUnitInfo.getUnitInfoId(),aeaUnitInfo.getIsOwner());
                        allProjInfoIdList.add(unitProjId);
                    }
                }

                String unitProjIdStr = org.apache.commons.lang3.StringUtils.join(allProjInfoIdList, ",");
                aeaUnitInfo.setUnitProjId(unitProjIdStr);
                return aeaUnitInfo;
            }
        }
        String unitProjIdStr = null;
        if("1".equals(aeaUnitInfo.getIsOwner())){
            unitProjIdStr = addAeaUnitProj(aeaUnitInfo.getProjInfoId(),aeaUnitInfo.getUnitInfoId(),aeaUnitInfo.getIsOwner());
        }else{//经办单位时保存所有项目的关联
            String[] unitProjIdArr = new String[projInfoIds.length];
            for(int i=0;i<projInfoIds.length;i++){
                String unitProjId = addAeaUnitProj(projInfoIds[i],aeaUnitInfo.getUnitInfoId(),aeaUnitInfo.getIsOwner());
                unitProjIdArr[i] = unitProjId;
            }
            unitProjIdStr = StringUtils.join(unitProjIdArr, ",");
        }
        aeaUnitInfo.setUnitProjId(unitProjIdStr);
        return aeaUnitInfo;
    }

    public void batchDeleteAeaUnitProj(String[] unitProjId) throws Exception {
        for(String one : unitProjId){
            aeaUnitProjMapper.deleteAeaUnitProj(one);
        }
    }
}
