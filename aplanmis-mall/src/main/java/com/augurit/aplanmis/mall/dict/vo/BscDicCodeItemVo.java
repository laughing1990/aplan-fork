package com.augurit.aplanmis.mall.dict.vo;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("数据字典")
public class BscDicCodeItemVo {
    @ApiModelProperty(value = "主键", required = true, dataType = "string")
    private String itemId;
    @ApiModelProperty(value = "类型ID", required = true, dataType = "string")
    private String typeId;
    @ApiModelProperty(value = "子项编号", required = true, dataType = "string")
    private String itemCode;
    @ApiModelProperty(value = "子项名称", required = true, dataType = "string")
    private String itemName;
    @ApiModelProperty(value = "是否启用。0表示禁用，1表示启用", required = true, dataType = "string", allowableValues = "0,1")
    private String itemIsActive = "1";
    @ApiModelProperty(value = "排列顺序号，为0-999", required = true, dataType = "long")
    private Long itemSortNo;
    @ApiModelProperty(value = "父子项ID，该字段仅适用于树结构", required = true, dataType = "string")
    private String itemParentId;
    @ApiModelProperty(value = "子项序列，该字段仅适用于树结构", required = true, dataType = "string")
    private String itemSeq;
    @ApiModelProperty(value = "子项层次，该字段仅适用于树结构", required = true, dataType = "long")
    private Long itemLevel;
    @ApiModelProperty(value = "子节点总数", required = true, dataType = "long")
    private Long itemSubCount;
    @ApiModelProperty(value = "是否锁定。0表示可删除，1表示不可删除。", required = true, dataType = "String", allowableValues = "0,1")
    private String itemIsLock = "0";
    @ApiModelProperty(value = "备注说明", required = true, dataType = "string")
    private String itemMemo;
    @ApiModelProperty(value = "子项的创建者", required = true, dataType = "string")
    private String itemCreater;
    @ApiModelProperty(value = "子项的创建时间，精确到秒", required = true, dataType = "date")
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date itemCreateTime;
    @ApiModelProperty(value = "子项的修改者", required = true, dataType = "string")
    private String itemModifier;
    @ApiModelProperty(value = "子项的修改时间，精确到秒", required = true, dataType = "date")
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date itemModifyTime;
    @ApiModelProperty(value = "所属顶级组织ID或ROOT", required = true, dataType = "string")
    private String orgId;

    public static List<BscDicCodeItemVo> toBscDicCodeItemVos(List<BscDicCodeItem> bscDicCodeItems) {
        List<BscDicCodeItemVo> bscDicCodeItemVos = new ArrayList<>();
        for (BscDicCodeItem bscDicCodeItem : bscDicCodeItems) {
            BscDicCodeItemVo bscDicCodeItemVo = new BscDicCodeItemVo();
            bscDicCodeItemVo.setItemId(bscDicCodeItem.getItemId());
            bscDicCodeItemVo.setTypeId(bscDicCodeItem.getTypeId());
            bscDicCodeItemVo.setItemCode(bscDicCodeItem.getItemCode());
            bscDicCodeItemVo.setItemName(bscDicCodeItem.getItemName());
            bscDicCodeItemVo.setItemIsActive(bscDicCodeItem.getItemIsActive());
            bscDicCodeItemVo.setItemSortNo(bscDicCodeItem.getItemSortNo());
            bscDicCodeItemVo.setItemParentId(bscDicCodeItem.getItemParentId());
            bscDicCodeItemVo.setItemSeq(bscDicCodeItem.getItemSeq());
            bscDicCodeItemVo.setItemLevel(bscDicCodeItem.getItemLevel());
            bscDicCodeItemVo.setItemSubCount(bscDicCodeItem.getItemSubCount());
            bscDicCodeItemVo.setItemIsLock(bscDicCodeItem.getItemIsLock());
            bscDicCodeItemVo.setItemMemo(bscDicCodeItem.getItemMemo());
            bscDicCodeItemVo.setItemCreater(bscDicCodeItem.getItemCreater());
            bscDicCodeItemVo.setItemCreateTime(bscDicCodeItem.getItemCreateTime());
            bscDicCodeItemVo.setItemModifier(bscDicCodeItem.getItemModifier());
            bscDicCodeItemVo.setItemModifyTime(bscDicCodeItem.getItemModifyTime());
            bscDicCodeItemVo.setOrgId(bscDicCodeItem.getOrgId());
            bscDicCodeItemVos.add(bscDicCodeItemVo);
        }
        return bscDicCodeItemVos;
    }

}
