package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.PublishStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 事项定义版本表-模型
 *
 * @author jjt
 * @date 2019/4/23
 *
 */
@Data
public class AeaItemVer implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final double initVer = 0.01D;

    /**
     * 主键
     */
    private String itemVerId;

    /**
     * 事项定义ID
     */
    private String itemId;

    /**
     * 版本号
     */
    private Double verNum;

    /**
     * 事项版本状态 0：待发布 1：试运行 2：已发布  3：已过时(已经发布但已不是最新版本)
     */
    private String itemVerStatus;

    /**
     * 是否删除，0表示未删除，1表示已删除
     */
    private String isDeleted;

    /**
     * 备注说明
     */
    private String verMemo;

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
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 用于存储广东省事项库版本号
     */
    private Double gdsVerNum;

    private String rootOrgId;

    /**
     * 扩展字段: 事项基本信息名称
     */
    private String itemName;

    /**
     * 扩展字段: 事项基本信息id
     */
    private String itemBasicId;

    /**
     * 扩展字段: 是否标准事项  1标准事项 0 实施事项
     */
    private String isCatalog;

    public static AeaItemVer initOne(String itemId, String creator, String rootOrgId) {

        AeaItemVer aeaItemVer = new AeaItemVer();
        aeaItemVer.setItemVerId(UuidUtil.generateUuid());
        aeaItemVer.setItemId(itemId);
        aeaItemVer.setVerNum(initVer);
        aeaItemVer.setGdsVerNum(initVer);
        aeaItemVer.setRootOrgId(rootOrgId);
        aeaItemVer.setCreater(creator);
        aeaItemVer.setCreateTime(new Date());
        aeaItemVer.setItemVerStatus(PublishStatus.UNPUBLISHED.getValue());
        aeaItemVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        return aeaItemVer;
    }

    public boolean isEditable() {

        return (this.itemVerStatus.equals(PublishStatus.UNPUBLISHED.getValue())) || (this.itemVerStatus.equals(PublishStatus.TEST_RUN.getValue()));
    }
}






