package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaItemStateVer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class AeaItemStateVersionVo {

    private String itemId;

    /**
     * 事项版本id
     */
    private String itemVerId;

    /**
     * 情形版本id
     */
    private String itemStateVerId;

    /**
     * 版本号
     */
    private String verNum;

    private String verStatus;

    /**
     * 是否删除
     */
    private String isDeleted;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String modifier;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    public static AeaItemStateVersionVo from(AeaItemStateVer version) {

        AeaItemStateVersionVo vo = new AeaItemStateVersionVo();
        vo.setItemStateVerId(version.getItemStateVerId());
        vo.setItemId(version.getItemId());
        vo.setItemVerId(version.getItemVerId());
        vo.setVerNum("V" + version.getVerNum());
        vo.setVerStatus(version.getVerStatus());
        vo.setIsDeleted(version.getIsDeleted());
        vo.setCreater(version.getCreater());
        vo.setCreateTime(version.getCreateTime());
        vo.setModifier(version.getModifier());
        vo.setModifyTime(version.getModifyTime());
        return vo;
    }
}