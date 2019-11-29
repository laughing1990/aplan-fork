package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaUnitConstants;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.domain.AeaUnitProjLinkman;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        map.put("projUnitIds",projUnitIds);
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
}
