package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.vo.AuditVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

/**
 * 事项情形版本
 */
@Data
public class AeaItemStateVer {

    public static final double INIT_VER = 0.01D;

    /**
     * 主健id
     */
    private String itemStateVerId;

    /**
     * 事项id
     */
    private String itemId;

    /**
     * 事项版本id
     */
    private String itemVerId;

    /**
     * 版本号
     */
    private double verNum;

    /**
     * 未发布， 试运行， 已发布, 已过时
     */
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

    /**
     * 根组织id
     */
    private String rootOrgId;

    public static AeaItemStateVer initVer(String itemId, String itemVerId, AuditVo auditVo) {

        AeaItemStateVer init= new AeaItemStateVer();
        init.setItemStateVerId(UUID.randomUUID().toString());
        init.setItemId(itemId);
        init.setItemVerId(itemVerId);
        init.setVerNum(INIT_VER);
        init.setVerStatus(PublishStatus.UNPUBLISHED.getValue());
        init.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        init.setRootOrgId(auditVo.getRootOrgId());
        init.setCreater(auditVo.getCreater());
        init.setCreateTime(auditVo.getCreateTime());
        init.setModifier(auditVo.getModifier());
        init.setModifyTime(auditVo.getModifyTime());
        return init;
    }

    /**
     * 复制情形版本，并增加版本号
     *
     * @param verNum
     * @return
     */
    public AeaItemStateVer copyAndIncVersion(double verNum) {

        AeaItemStateVer copy = new AeaItemStateVer();
        copy.setItemStateVerId(UUID.randomUUID().toString());
        copy.setItemId(this.itemId);
        copy.setItemVerId(this.itemVerId);
        copy.setVerNum(verNum < 0 ? INIT_VER : verNum);
        // 复制情形版本数据等于新建版本就是未发布
        copy.setVerStatus(PublishStatus.UNPUBLISHED.getValue());
        copy.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        copy.setRootOrgId(this.rootOrgId);
        copy.setCreater(this.creater);
        copy.setCreateTime(new Date());
        copy.setModifier(this.modifier);
        copy.setModifyTime(new Date());
        return copy;
    }

    public boolean isInitVer() {

        return this.verStatus.equals(PublishStatus.UNPUBLISHED.getValue()) && verNum == INIT_VER;
    }

    public boolean isUnPublishedVer() {

        return this.verStatus.equals(PublishStatus.UNPUBLISHED.getValue());
    }

    public boolean isPublishedVer() {

        return this.verStatus.equals(PublishStatus.PUBLISHED.getValue());
    }

    public boolean isTestRunVer() {

        return this.verStatus.equals(PublishStatus.TEST_RUN.getValue());
    }

    public boolean isDeprecatedVer() {

        return this.verStatus.equals(PublishStatus.DEPRECATED.getValue());
    }

    public boolean isEditable() {

        return this.verStatus.equals(PublishStatus.UNPUBLISHED.getValue()) || this.verStatus.equals(PublishStatus.TEST_RUN.getValue());
    }

    public void testRun() {

        this.verStatus.equals(PublishStatus.TEST_RUN.getValue());
    }

    public void publish() {

        this.verStatus.equals(PublishStatus.PUBLISHED.getValue());
    }

    public void deprecate() {

        this.verStatus.equals(PublishStatus.DEPRECATED.getValue());
    }
}
