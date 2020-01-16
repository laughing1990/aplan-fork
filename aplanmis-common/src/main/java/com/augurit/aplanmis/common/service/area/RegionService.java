package com.augurit.aplanmis.common.service.area;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.sc.dic.region.service.BscDicRegionService;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.utils.CommonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * 根据行政区划ID查找当前区划及其子区划，还有父级区划
     * @param regionId
     * @return
     */
    public List<BscDicRegion> getSeqBscDicRegionList(String regionId) {
        List<BscDicRegion> list = bscDicRegionService.listSelfAndChildRegions(regionId, null);
        BscDicRegion region = bscDicRegionService.getBscDicRegionById(regionId);
        String regionSeq=region.getRegionSeq();
        if(StringUtils.isNotBlank(regionSeq)&&StringUtils.isNotBlank(region.getParentRegionId())){
            String[] arr=regionSeq.split("\\.");
            for (String i:arr){
                if(StringUtils.isBlank(i)) continue;
                list.add(bscDicRegionService.getBscDicRegionById(i));
            }
        }
        if(list.size()>0){
            list.stream()
                    .filter(CommonTools.distinctByKey(BscDicRegion::getRegionId))
                    .collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 根据行政区划ID查找当前区划及其子区划，还有父级区划ID列表
     * @param regionId
     * @return
     */
    public List<String> getSeqBscDicRegionIds(String regionId) {
        List<BscDicRegion> list =this.getSeqBscDicRegionList(regionId);
        if(list.size()>0){
            return list.stream()
                    .filter(CommonTools.distinctByKey(BscDicRegion::getRegionId)).map(BscDicRegion::getRegionId)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
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
