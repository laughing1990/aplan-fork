package com.augurit.aplanmis.common.service.area;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.sc.dic.region.service.BscDicRegionService;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionService {
    @Autowired
    private BscDicRegionService bscDicRegionService;
    @Autowired
    private OpuOmOrgService opuOmOrgService;

    /**
     * 根据顶级组织ID查询区划列表
     * @param rootOrgId
     * @return
     */
    public List<BscDicRegion> getBscDicRegionList(String rootOrgId) {
        OpuOmOrg rootOrg = opuOmOrgService.getOrg(rootOrgId);
        if(rootOrg==null) return new ArrayList<>();
        return bscDicRegionService.listSelfAndChildRegions(rootOrg.getRegionId(),null);
    }

    /**
     * 根据顶级组织ID查询区划树状列表
     *
     * @param rootOrgId
     * @return
     */
    public List<BscDicRegion> getBscDicRegionTreeList(String rootOrgId) {
        List<BscDicRegion> sysRegions = new ArrayList<>();
        OpuOmOrg rootOrg = opuOmOrgService.getOrg(rootOrgId);
        if (rootOrg == null || StringUtils.isBlank(rootOrg.getRegionId())) return new ArrayList<>();
        List<BscDicRegion> regions = bscDicRegionService.listSelfAndChildRegions(rootOrg.getRegionId(), null);
        for (BscDicRegion region : regions) {
            if (StringUtils.isBlank(region.getParentRegionId()) || rootOrg.getRegionId().equals(region.getRegionId())) {
                sysRegions.add(region);
            }
        }
        findChildren(sysRegions, regions);
        return sysRegions;
    }


    private void findChildren(List<BscDicRegion> sysRegions, List<BscDicRegion> regions) {
        for (BscDicRegion sysRegion : sysRegions) {
            List<BscDicRegion> children = new ArrayList<>();
            for (BscDicRegion region : regions) {
                if (StringUtils.isNotBlank(sysRegion.getRegionId()) && sysRegion.getRegionId().equals(region.getParentRegionId())) {
                    children.add(region);
                }
            }
            sysRegion.setChildren(children);
            findChildren(children, regions);
        }
    }
}
