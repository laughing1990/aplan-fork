package com.augurit.aplanmis.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 *  用于业务上的工具类
 */
public class BusinessUtil {
    /**
     *  从分局承办中获取当前事项的组织ID
     * @param branchOrgMap
     * @param itemVerId
     */
    public static String getOrgIdFromBranchOrgMap(String branchOrgMap,String itemVerId){
        if(StringUtils.isNotBlank(branchOrgMap)){
            List<Map> mapList = JSONArray.parseArray(branchOrgMap, Map.class);
            for (Map map:mapList){
                if(itemVerId.equals(map.get("itemVerId").toString())){
                    return (String)map.get("branchOrg");
                }
            }
        }
        return "";
    }

}
