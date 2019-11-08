package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.util.UuidUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 主题定义序列表-模型
 * @author jjt
 * @date 2019/4/30
 *
 */
@Data
public class AeaParThemeSeq implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final double initVer = 0.01D;

    /**
     * 主键ID
     */
    private String themeSeqId;

    /**
     * 主题定义ID
     */
    private String themeId;

    /**
     * 最新值（用于加0.01产生新版本号
     */
    private Double maxNum;

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
     * 根组织id
     */
    private String rootOrgId;

    public static AeaParThemeSeq initOne(String themeId, String creator, String rootOrgId) {

        AeaParThemeSeq themeSeq = new AeaParThemeSeq();
        themeSeq.setThemeSeqId(UuidUtil.generateUuid());
        themeSeq.setThemeId(themeId);
        themeSeq.setMaxNum(initVer);
        themeSeq.setRootOrgId(rootOrgId);
        themeSeq.setCreater(creator);
        themeSeq.setCreateTime(new Date());
        return themeSeq;
    }
}

