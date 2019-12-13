package com.augurit.aplanmis.data.exchange.convert;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.data.exchange.convert.datatree.AeaProjInfoDataTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AeaProjInfoDataTreeConvert extends AbstractDataTreeCovert<AeaProjInfo, AeaProjInfoDataTree> {


    @Override
    public List<AeaProjInfoDataTree> convert(List<AeaProjInfo> aeaProjInfoList) {
        List<AeaProjInfoDataTree> dataTrees = super.convert(aeaProjInfoList);
        List<AeaProjInfoDataTree> others = new ArrayList<>();
        if(aeaProjInfoList.size()>0){
            Set<String> localCodes = aeaProjInfoList.stream().map(k -> k.getLocalCode()).collect(Collectors.toSet());
            for (String localCode:localCodes) {
                AeaProjInfoDataTree dataTree = new AeaProjInfoDataTree();
                dataTree.setLocalCode(localCode);
                dataTree.setGcbm(localCode);
                dataTree.setId(localCode);
                this.addChild(dataTree,aeaProjInfoList);
                others.add(dataTree);
            }
        }
        dataTrees.addAll(others);
        return dataTrees;
    }

    @Override
    String getId(AeaProjInfo aeaProjInfo) {
        return aeaProjInfo.getGcbm();
    }

    @Override
    String getParentId(AeaProjInfo aeaProjInfo) {
        return aeaProjInfo.getLocalCode();
    }
}
