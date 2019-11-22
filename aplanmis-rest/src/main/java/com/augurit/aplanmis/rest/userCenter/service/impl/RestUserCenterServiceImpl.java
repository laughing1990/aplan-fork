package com.augurit.aplanmis.rest.userCenter.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaParentProj;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.service.RestUserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RestUserCenterServiceImpl implements RestUserCenterService {

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Override
    public String saveChildProject(AeaProjInfo aeaProjInfo) throws Exception {
        //判定登录类型
        AuthUser loginInfoVo = AuthContext.getCurrentUser();
        String modify = StringUtils.isNotBlank(loginInfoVo.getLinkmanInfoId()) ? loginInfoVo.getLinkmanInfoId() : loginInfoVo.getUnitInfoId();
        AeaProjInfo oldAeaProjInfo;
        //判定是编辑主项目还是保存分离子项目
        if (StringUtils.isNotBlank(aeaProjInfo.getProjInfoId())) {//如果aeaProjInfo的id不为空，则为编辑主项目,需创建新的aeaProjInfoLog
            oldAeaProjInfo = aeaProjInfo;
            aeaProjInfo.setModifier(modify);//更新修改人
            aeaProjInfo.setModifyTime(new Date());//更新修改时间
            aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfo);//更新aeaProjInfo
            //创建新的aeaProjInfoLog
//            insertAeaProjInfoLog(modify,aeaProjInfo,oldAeaProjInfo);
        } else {//如果aeaProjInfo的id为空，则为分离出来的子项目，需创建新的aeaProjInfo，新的aeaParentProj，新的aeaProjInfoLog
            aeaProjInfo.setProjInfoId(UUID.randomUUID().toString()); //更新主键
            aeaProjInfo.setCreater(modify);//更新创建人
            aeaProjInfo.setCreateTime(new Date());//更新创建时间
            aeaProjInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaProjInfo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);//insert新的aeaProjInfo
            //创建新的aeaParentProj
            AeaParentProj aeaParentProj = new AeaParentProj();
            aeaParentProj.setNodeProjId(UUID.randomUUID().toString());//更新主键
            aeaParentProj.setCreater(modify); //更新创建人
            aeaParentProj.setCreateTime(new Date()); //更新创建时间
            aeaParentProj.setParentProjId(aeaProjInfo.getParentProjId()); //更新parentProjId
            aeaParentProj.setChildProjId(aeaProjInfo.getProjInfoId());//更新childProjId
            aeaParentProj.setProjSeq(aeaProjInfo.getParentProjId() + "." + aeaParentProj.getChildProjId());//更新projSeq
            aeaProjInfoMapper.insertAeaParentProj(aeaParentProj); //insert新的aeaParentProj
            //查询子项目的父项
            oldAeaProjInfo = aeaProjInfoMapper.getOnlyAeaProjInfoById(aeaParentProj.getParentProjId());
//            insertAeaProjInfoLog(modify,aeaProjInfo,oldAeaProjInfo);
            oldAeaProjInfo = aeaProjInfo;
        }
        return oldAeaProjInfo.getProjInfoId();
    }

}
