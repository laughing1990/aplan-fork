package com.augurit.aplanmis.front.basis.item.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.front.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestItemService {

    @Autowired
    private OpuOmOrgService opuOmOrgService;
    @Autowired
    private BscDicCodeService bscDicCodeService;

    /**
     * 查询事项的服务对象
     * @param dicName 数据字典名称
     * @param code 数据字典编码
     * @return 服务对象
     */
    public String getServiceObject(String dicName,String code, String currentOrgId) {
        if(StringUtils.isBlank(code)) {
            return "";
        }
        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(currentOrgId);
        if (topOrg != null) {
            List<BscDicCodeItem> activeItemsByTypeCode = bscDicCodeService.getActiveItemsByTypeCode(dicName, topOrg.getOrgId());
            if(code.contains(CommonConstant.COMMA_SEPARATOR)){
                String[] split = code.split(CommonConstant.COMMA_SEPARATOR);
                StringBuilder str = new StringBuilder();
                for(int j=0; j<split.length; j++){
                    for (BscDicCodeItem bscDicCodeItem : activeItemsByTypeCode) {
                        if (bscDicCodeItem.getItemCode().equals(split[j])) {
                            if (j != split.length - 1) {
                                str.append(bscDicCodeItem.getItemName()).append(CommonConstant.COMMA_SEPARATOR);
                            } else {
                                str.append(bscDicCodeItem.getItemName());
                            }
                        }
                    }
                }
                return str.toString();
            }else{
                for (BscDicCodeItem bscDicCodeItem : activeItemsByTypeCode) {
                    if (bscDicCodeItem.getItemCode().equals(code)) {
                        return bscDicCodeItem.getItemName();
                    }
                }
            }
        }
        return null;
    }
}
