package com.augurit.aplanmis.supermarket.agentService.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDetailVo {
    /**
     * (事项内容ID，主键ID)
     */
    private String itemBasicId;
    /**
     * (关联aea_item表id)
     */
    private String itemId;
    /**
     * (关联aea_item_ver表id)
     */
    private String itemVerId;
    /**
     * (事项编号)
     */
    private String itemCode;
    /**
     * (事项类型)
     */
    private String itemType;
    /**
     * (事项名称)
     */
    private String itemName;
    /**
     * (组织ID)
     */
    private String orgId;
    /**
     * 中介事项部门
     */
    private String orgName;

    private String guideOrgName;

    public ItemDetailVo() {
    }

    public ItemDetailVo(AeaItemBasic itemBasic) {
        BeanUtils.copyProperties(itemBasic, this);
        if (StringUtils.isBlank(this.orgName)) {
            this.orgName = this.guideOrgName;
        }
    }

    public static List<ItemDetailVo> conver2List(List<AeaItemBasic> list) {
        List<ItemDetailVo> copyList = new ArrayList<>();
        //ItemDetailVo vo = null;
        for (AeaItemBasic tempBasic : list) {
            /*vo = new ItemDetailVo();
            BeanUtils.copyProperties(tempBasic, vo);*/
            copyList.add(new ItemDetailVo(tempBasic));
        }
        return copyList;
    }
}
