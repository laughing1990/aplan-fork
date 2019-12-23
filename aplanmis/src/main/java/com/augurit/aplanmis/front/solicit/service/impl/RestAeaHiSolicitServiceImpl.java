package com.augurit.aplanmis.front.solicit.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.front.solicit.service.RestAeaHiSolicitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:chendx
 * @date: 2019-12-23
 * @time: 13:55
 */
@Service
@Transactional
public class RestAeaHiSolicitServiceImpl implements RestAeaHiSolicitService{
    @Autowired
    private OpuOmOrgService opuOmOrgService;

    @Override
    public List<OpuOmOrg> listOrg(String isRoot, String parentOrgId) throws Exception {
        String topOrgId = SecurityContext.getCurrentOrgId();

        if(StringUtils.isBlank(isRoot))
            throw new RuntimeException("参数isRoot不能为空！");
        if(StringUtils.isNotBlank(isRoot)&&"0".equals(isRoot)
                &&StringUtils.isBlank(parentOrgId))
            throw new RuntimeException("非根组织，参数parentOrgId不能为空！");


        List<OpuOmOrg> list = new ArrayList<>();

        if("0".equals(isRoot)) {
            OpuOmOrg opuOmOrg = new OpuOmOrg();
            opuOmOrg.setParentOrgId(parentOrgId);
            opuOmOrg.setOrgProperty("d");
            opuOmOrg.setOrgDeleted("0");
            list = opuOmOrgService.listOpuOmOrg(opuOmOrg);
        }

        if("1".equals(isRoot)){
            OpuOmOrg opuOmOrg = opuOmOrgService.getOrg(topOrgId);
            list.add(opuOmOrg);
        }

        return list;
    }
}
