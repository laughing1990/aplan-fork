package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.PublishStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 主题最大版本号表-模型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AeaParThemeVer extends ZtreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final double initVer = 0.01D;

    /**
     * 主键ID
     */
    private String themeVerId;

    /**
     * 主题定义ID
     */
    private String themeId;

    /**
     * 版本号
     */
    private Double verNum;

    /**
     * 是否启用，0表示禁用，1表示启用
     */
    private String isActive;

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
     * 是否发布 0 未发布 1 发布 2 试运行 3已过时
     */
    private String isPublish;

    /**
     * 根组织ID
     */
    private String rootOrgId;

    /**
     * 扩展字段: 主题名称
     */
    private String themeName;

    /**
     * 扩展字段: 是否发布
     */
    private String publish;

    /**
     * 扩展字段: 是否删除
     */
    private String deleted;

    /**
     * 扩展字段： 主题版本拖拽图
     */
    private String themeVerDiagram;

    public static AeaParThemeVer initOne(String themeId, String creator, String rootOrgId) {

        AeaParThemeVer aeaThemeVer = new AeaParThemeVer();
        aeaThemeVer.setThemeVerId(UuidUtil.generateUuid());
        aeaThemeVer.setRootOrgId(rootOrgId);
        aeaThemeVer.setThemeId(themeId);
        aeaThemeVer.setVerNum(initVer);
        aeaThemeVer.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaThemeVer.setIsPublish(PublishStatus.UNPUBLISHED.getValue());
        aeaThemeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaThemeVer.setCreater(creator);
        aeaThemeVer.setCreateTime(new Date());
        return aeaThemeVer;
    }

    public boolean isEditable() {

        return  PublishStatus.UNPUBLISHED.getValue().equals(this.isPublish) ||  PublishStatus.TEST_RUN.getValue().equals(this.isPublish);
    }
}
