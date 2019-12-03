package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaUnitConstants;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaApplyinstUnitProjMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyCommonService;
import com.augurit.aplanmis.mall.userCenter.vo.SmsInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class RestApplyCommonServiceImpl implements RestApplyCommonService {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaApplyinstUnitProjMapper aeaApplyinstUnitProjMapper;
    @Autowired
    private AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private RestApplyCommonService restApplyCommonService;


    @Override
    public Map<String,Object> saveOrUpdateUnitInfo(String projInfoId, List<AeaUnitInfoVo> aeaUnitInfos) {
        Map<String,Object> map=new HashMap<>(2);
        List<String> projUnitIds = new ArrayList<>();
        if (aeaUnitInfos==null||aeaUnitInfos.size()==0) return map;
        AeaUnitProj aeaUnitProj = new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projInfoId);
        aeaUnitProj.setCreater(SecurityContext.getCurrentUserId());
        aeaUnitProj.setCreateTime(new Date());
        List<Map> unitReturnJson=new ArrayList<>();
        aeaUnitInfos.stream().forEach(aeaUnitInfo -> {
            Map<String,Object> jsonMap=new HashMap<>(2);
            String unitInfoId = "";
            //unitInfoId = aeaUnitInfo.getUnitInfoId();
            if (StringUtils.isEmpty(aeaUnitInfo.getUnitInfoId())){
                aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                aeaUnitInfoService.insertAeaUnitInfo(AeaUnitInfoVo.returnForm(aeaUnitInfo));
            }else  {
                aeaUnitInfoService.updateAeaUnitInfo(AeaUnitInfoVo.returnForm(aeaUnitInfo));
            }
            unitInfoId = aeaUnitInfo.getUnitInfoId();
            jsonMap.put("unitInfoId",unitInfoId);
            jsonMap.put("unifiedSocialCreditCode",aeaUnitInfo.getUnifiedSocialCreditCode());
            unitReturnJson.add(jsonMap);
            //AeaLinkmanInfo selectedLinkman = aeaUnitInfo.getSelectedLinkman();
            aeaUnitProj.setUnitType(aeaUnitInfo.getUnitType());
            aeaUnitProj.setIsOwner(AeaUnitConstants.IS_OWNER_TRUE);
            aeaUnitProj.setUnitInfoId(unitInfoId);
            aeaUnitProj.setIsDeleted("0");
            try {
                testUnitLinkman(aeaUnitInfo);//检测单位和当前联系人是否存在关联关系，若不存在，则需保存
                List<AeaUnitProj> unitProjList =  aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);//先判断有无关联关系
                if (unitProjList==null||unitProjList.size()==0){
                    String unitProjId = UUID.randomUUID().toString();
                    aeaUnitProj.setUnitProjId(unitProjId);
                    aeaUnitProj.setLinkmanInfoId(aeaUnitInfo.getLinkmanInfoId());
                    aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
                    projUnitIds.add(unitProjId);
                } else {
                    projUnitIds.add(unitProjList.get(0).getUnitProjId());
                }
                if (aeaUnitInfo.getLinkmanTypes() != null && aeaUnitInfo.getLinkmanTypes().size() > 0 && StringUtils.isNotBlank(aeaUnitProj.getUnitProjId())) {
                    AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
                    aeaUnitProjLinkman.setCreater(SecurityContext.getCurrentUserName());
                    aeaUnitProjLinkman.setCreateTime(new Date());
                    aeaUnitProjLinkman.setIsDeleted("0");
                    aeaUnitProjLinkman.setUnitProjId(aeaUnitProj.getUnitProjId());
                    AeaUnitProjLinkman query = new AeaUnitProjLinkman();
                    query.setUnitProjId(aeaUnitProj.getUnitProjId());
                    for (LinkmanTypeVo vo : aeaUnitInfo.getLinkmanTypes()) {
                        aeaUnitProjLinkman.setLinkmanInfoId(vo.getLinkmanInfoId());
                        aeaUnitProjLinkman.setLinkmanType(vo.getLinkmanType());
                        query.setLinkmanType(vo.getLinkmanType());
                        List<AeaUnitProjLinkman> list = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(query);
                        if (list.size() == 0) {
                            aeaUnitProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
                            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(aeaUnitProjLinkman);
                        } else {
                            AeaUnitProjLinkman updateParam = list.get(0);
                            updateParam.setModifier(SecurityContext.getCurrentUserName());
                            updateParam.setModifyTime(new Date());
                            updateParam.setLinkmanInfoId(vo.getLinkmanInfoId());
                            aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(updateParam);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        map.put("unitProjIds",projUnitIds);
        map.put("unitReturnJson",unitReturnJson);
        return map;
    }

    private void testUnitLinkman(AeaUnitInfoVo aeaUnitInfo) throws Exception {
        String currentUnitId=aeaUnitInfo.getUnitInfoId();
        String currentLinkmanInfoId=aeaUnitInfo.getLinkmanInfoId();
        if(StringUtils.isNotBlank(currentUnitId) && StringUtils.isNotBlank(currentLinkmanInfoId)){
            AeaUnitLinkman param=new AeaUnitLinkman();
            param.setLinkmanInfoId(currentLinkmanInfoId);
            param.setUnitInfoId(currentUnitId);
            List<AeaUnitLinkman> list = aeaUnitLinkmanMapper.listAeaUnitLinkman(param);
            if(list.size()==0){
                param.setCreater(SecurityContext.getCurrentUserName());
                param.setCreateTime(new Date());
                param.setUnitLinkmanId(UUID.randomUUID().toString());
                aeaUnitLinkmanMapper.insertAeaUnitLinkman(param);
            }
        }
    }

    @Override
    public void saveOrUpdateLinkmanTypes(List<LinkmanTypeVo> linkmanTypeVos) throws Exception {
        if (linkmanTypeVos.size() > 0) {
            AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
            aeaUnitProjLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaUnitProjLinkman.setCreateTime(new Date());
            aeaUnitProjLinkman.setIsDeleted("0");
            AeaUnitProjLinkman query = new AeaUnitProjLinkman();
            for (LinkmanTypeVo vo : linkmanTypeVos) {
                AeaUnitProj aeaUnitProj = new AeaUnitProj();
                aeaUnitProj.setUnitInfoId(vo.getUnitInfoId());
                aeaUnitProj.setProjInfoId(vo.getProjInfoId());
                aeaUnitProj.setUnitType("1");//建设单位
                List<AeaUnitProj> unitProjList = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);//先判断有无关联关系
                if (unitProjList.size() > 0) {
                    aeaUnitProjLinkman.setUnitProjId(unitProjList.get(0).getUnitProjId());
                    query.setUnitProjId(unitProjList.get(0).getUnitProjId());
                    aeaUnitProjLinkman.setLinkmanInfoId(vo.getLinkmanInfoId());
                    aeaUnitProjLinkman.setLinkmanType(vo.getLinkmanType());
                    query.setLinkmanType(vo.getLinkmanType());
                    List<AeaUnitProjLinkman> list = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(query);
                    if (list.size() == 0) {
                        aeaUnitProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
                        aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(aeaUnitProjLinkman);
                    } else {
                        AeaUnitProjLinkman updateParam = list.get(0);
                        updateParam.setModifier(SecurityContext.getCurrentUserName());
                        updateParam.setModifyTime(new Date());
                        updateParam.setLinkmanInfoId(vo.getLinkmanInfoId());
                        aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(updateParam);
                    }
                }
            }
        }
    }

    @Override
    public Boolean isProjBelong(String projInfoId, HttpServletRequest request) {

        return null;
    }

    @Override
    public void insertAeaApplyinstUnitProj(String applyinstId,List<String>unitProjIds){
        if(unitProjIds!=null && unitProjIds.size()>0 && StringUtils.isNotBlank(applyinstId)){
            List<AeaApplyinstUnitProj> paramList=new ArrayList<>();
            for (String unitProjId:unitProjIds){
                AeaApplyinstUnitProj entity=new AeaApplyinstUnitProj();
                entity.setUnitProjId(unitProjId);
                entity.setApplyinstId(applyinstId);
                entity.setIsDeleted("0");
                List<AeaApplyinstUnitProj> list = aeaApplyinstUnitProjMapper.listAeaApplyinstUnitProj(entity);
                if(list.size()>0) continue;
                entity.setApplyinstUnitProjId(UUID.randomUUID().toString());
                entity.setCreater(SecurityContext.getCurrentUserName());
                entity.setCreateTime(new Date());
                paramList.add(entity);
            }
            if(paramList.size()==0) return;
            aeaApplyinstUnitProjMapper.batchInsertAeaApplyinstUnitProjMapper(paramList);
        }
    }

    @Override
    public Map<String, Object> getStringObjectMap(@RequestBody SmsInfoVo smsInfoVo, Map<String, Object> resultMap, AeaProjInfo aeaProjInfo) throws Exception {
        String smsId;//保存或修改领件人信息
        if (StringUtils.isBlank(smsInfoVo.getId())) {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.createAeaHiSmsInfo(smsInfoVo.toSmsInfo());
            smsId = aeaHiSmsInfo.getId();
        } else {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.getAeaHiSmsInfoById(smsInfoVo.getId());
            aeaHiSmsInfoService.updateAeaHiSmsInfo(smsInfoVo.merge(aeaHiSmsInfo));
            smsId = smsInfoVo.getId();
        }
        //修改项目信息
        if (StringUtils.isBlank(aeaProjInfo.getGcbm()))
            aeaProjInfo.setGcbm(aeaProjInfo.getLocalCode());
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        //当前企业用户的人员设置
        if (smsInfoVo.getLinkmanTypeVos() != null && smsInfoVo.getLinkmanTypeVos().size() > 0) {
            restApplyCommonService.saveOrUpdateLinkmanTypes(smsInfoVo.getLinkmanTypeVos());
        }

        //项目单位关联
        //List<String> projUnitIds = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(2);
        if (smsInfoVo.getAeaUnitInfos() != null && smsInfoVo.getAeaUnitInfos().size() > 0) {
            map = restApplyCommonService.saveOrUpdateUnitInfo(aeaProjInfo.getProjInfoId(), smsInfoVo.getAeaUnitInfos());
        }
        resultMap.put("smsId", smsId);
        resultMap.put("unitProjIds", map.get("unitProjIds"));
        resultMap.put("unitReturnJson", map.get("unitReturnJson"));
        resultMap.put("regionalism", aeaProjInfo.getRegionalism());
        return map;
    }

    @Override
    public void deleteReInsertAeaApplyinstUnitProj(String applyinstId, List<String> unitProjIds){
        List<AeaApplyinstUnitProj> applyinstUnitProjList = aeaApplyinstUnitProjMapper.getAeaApplyinstUnitProjByApplyinstId(applyinstId);
        if(applyinstUnitProjList.size()>0){
            applyinstUnitProjList.stream().forEach(entity->{
                entity.setIsDeleted("1");
                aeaApplyinstUnitProjMapper.updateAeaApplyinstUnitProj(entity);//逻辑删除
            });
        }
        insertAeaApplyinstUnitProj(applyinstId,unitProjIds);
    }

}
