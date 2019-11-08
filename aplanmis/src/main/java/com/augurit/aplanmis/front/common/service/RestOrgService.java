package com.augurit.aplanmis.front.common.service;

import com.augurit.agcloud.bpm.admin.process.domain.BpmAssigneeOrgEntity;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RestOrgService {

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    public List<BpmAssigneeOrgEntity> getOrgTree(String parentOrgId, String dataType, String orgName) {
        List<BpmAssigneeOrgEntity> list= new ArrayList<>();
        BpmAssigneeOrgEntity rootOrgEntity = null;
        if (StringUtils.isBlank(parentOrgId)) {
            rootOrgEntity = new BpmAssigneeOrgEntity();
            rootOrgEntity.setId("orgRoot");
            rootOrgEntity.setName("组织");
            rootOrgEntity.setDataType(dataType);
            rootOrgEntity.setIsParent(true);
            rootOrgEntity.setChkDisabled(true);
            rootOrgEntity.setIconSkin("");
            rootOrgEntity.setNocheck(true);
            list.add(rootOrgEntity);
            parentOrgId = SecurityContext.getCurrentOrgId();
        }

        OpuOmOrg opuOmOrg = new OpuOmOrg();
        opuOmOrg.setParentOrgId(parentOrgId);
        opuOmOrg.setIsActive("1");
        opuOmOrg.setOrgDeleted("0");

        if (orgName != null && !"".equals(orgName.trim())) {
            opuOmOrg.setOrgName(orgName);
        }

        List allOrgList = opuOmOrgMapper.listOpuOmOrgByAllProp(opuOmOrg);

        if (allOrgList != null && allOrgList.size() > 0) {
            if(rootOrgEntity!=null) {
                rootOrgEntity.setOpen(true);
            }

            for (Object o : allOrgList) {
                OpuOmOrg org = (OpuOmOrg) o;
                BpmAssigneeOrgEntity entity = new BpmAssigneeOrgEntity();
                entity.setId(org.getOrgId());
                entity.setDataType(dataType);
                entity.setName(org.getOrgName());
                entity.setTextValue(org.getOrgId());
                entity.setIconSkin("");

                if (rootOrgEntity != null) {
                    entity.setpId("orgRoot");
                } else {
                    entity.setpId(org.getParentOrgId());
                }
                entity.setIsParent(true);
                list.add(entity);
            }
        }
        return list;
    }

    public BscDicCodeItem getItemByTypeCodeAndItemCode(String typeCode, String itemCode) {
        return aeaItemBasicMapper.getItemByTypeCodeAndItemCode(typeCode,itemCode);

    }
}
