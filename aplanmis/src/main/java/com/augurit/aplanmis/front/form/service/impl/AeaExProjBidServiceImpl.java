package com.augurit.aplanmis.front.form.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaExProjBid;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.mapper.AeaExProjBidMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.form.service.AeaExProjBidService;
import com.augurit.aplanmis.front.form.vo.AeaExProjBidVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* 招投标信息-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:刘赵雄</li>
    <li>创建时间：2019-10-31 15:56:12</li>
</ul>
*/
@Service
@Transactional
@Slf4j
public class AeaExProjBidServiceImpl implements AeaExProjBidService {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaExProjBidMapper aeaExProjBidMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    public void saveAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception{
        aeaExProjBid.setCreater(SecurityContext.getCurrentUserName());
        aeaExProjBid.setCreateTime(new Date());
        aeaExProjBid.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjBidMapper.insertAeaExProjBid(aeaExProjBid);
    }

    public void updateAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception{
        aeaExProjBid.setModifier(SecurityContext.getCurrentUserName());
        aeaExProjBid.setModifyTime(new Date());
        aeaExProjBidMapper.updateAeaExProjBid(aeaExProjBid);
    }

    public AeaExProjBid getAeaExProjBidByProjId(String projId) throws Exception{
        if(projId == null){
            throw new InvalidParameterException(projId);
        }
        log.debug("根据ID获取Form对象，ID为：{}", projId);
        return aeaExProjBidMapper.getAeaExProjBidByProjId(projId);
    }

    @Override
    public AeaProjInfo getProjInfoByProjId(String projId){
        if(projId == null){
            throw new InvalidParameterException(projId);
        }
        log.debug("根据ID获取Form对象，ID为：{}", projId);
        return aeaProjInfoMapper.getAeaProjInfoById(projId);
    }


    /**
     * 根据项目id和类型查找单位信息
     * @param projInfoId
     * @param unitType
     * @return
     */
    @Override
    public List<AeaUnitInfo> findUnitProjByProjInfoIdAndType(String projInfoId, String unitType){
        log.debug("根据类型查询项目单位，projInfoId：{}，unitType：{}", projInfoId, unitType);
        return aeaExProjBidMapper.findUnitProjByProjInfoIdAndType(projInfoId, unitType,SecurityContext.getCurrentOrgId());
    }


    @Override
    public int updateAeaUnitProj(AeaUnitProj aeaUnitProj) throws Exception{
        return aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
    }

    /**
     * 更新项目单位关联表
     * @param projId
     * @param unitId
     * @param unitProjId
     * @param unitType
     * @throws Exception
     */
    @Override
    public void updateUnitProjInfo(String projId,String unitId,String unitProjId,String unitType) throws Exception{
        AeaUnitProj aeaUnitProj =new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projId);
        aeaUnitProj.setUnitInfoId(unitId);
        aeaUnitProj.setUnitProjId(unitProjId);
        aeaUnitProj.setUnitType(unitType);
        aeaUnitProj.setIsOwner("0");
        this.updateAeaUnitProj(aeaUnitProj);
    }

    /**
     * 删除项目单位关联表
     * @param projId
     * @param unitTypes
     * @throws Exception
     */
    @Override
    public void delUnitProjInfo(String projId,List<String> unitTypes) throws Exception{
        aeaExProjBidMapper.batchDeleteUnitProjByType(projId,unitTypes);
    }

    @Override
    public void saveOrUpdateUnitInfo(AeaExProjBidVo aeaExProjBidVo, List<AeaUnitInfo> aeaUnitInfos, String unitType, List<AeaUnitProj> aeaUnitProjNewList) throws Exception{
        if (aeaUnitInfos != null) {
            for (AeaUnitInfo aeaUnitInfo : aeaUnitInfos) {
                if (aeaUnitInfo.getUnitInfoId() != null && !"".equals(aeaUnitInfo.getUnitInfoId())) {
                    aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
                    //如果本身有关联表记录则更新，否则重新保存项目单位关联表信息
                    if(StringUtils.isNotBlank(aeaUnitInfo.getUnitProjId())) {
                        this.updateUnitProjInfo(aeaExProjBidVo.getProjInfoId(), aeaUnitInfo.getUnitInfoId(), aeaUnitInfo.getUnitProjId(), unitType);
                    }else{
                        aeaUnitProjNewList.add(aeaExProjBidVo.toAeaUnitProj(aeaUnitInfo.getUnitInfoId(), unitType));
                    }
                } else {
                    aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                    aeaUnitInfo.setUnitType(unitType);
                    aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
                    aeaUnitProjNewList.add(aeaExProjBidVo.toAeaUnitProj(aeaUnitInfo.getUnitInfoId(), unitType));
                }
            }
        }
    }

}

