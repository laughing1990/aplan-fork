package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.util.UuidUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 事项定义序列表-模型
 *
 * @author jjt
* @date 2019/4/23
*
*/
@Data
public class AeaItemSeq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String itemSeqId;

    /**
     * 事项id
     */
    private String itemId;

    /**
     * 事项版本id
     */
    private String itemVerId;

    /**
     * 最新值(未发布版本默认都是以小数结尾.01、1.01、2.01,发布以后就变整数 1,2...)
     */
    private Double itemVerMax;

    /**
     * 最新值(未发布版本默认都是以小数结尾.01、1.01、2.01,发布以后就变整数 1,2...)
     */
    private Double stateVerMax;

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

    public static AeaItemSeq initOne(String itemId, String itemVerId, String creator, String rootOrgId) {

        AeaItemSeq itemSeq = new AeaItemSeq();
        itemSeq.setItemSeqId(UuidUtil.generateUuid());
        itemSeq.setItemId(itemId);
        itemSeq.setItemVerId(itemVerId);
        itemSeq.setItemVerMax(AeaItemVer.initVer);
        itemSeq.setStateVerMax(AeaItemVer.initVer);
        itemSeq.setRootOrgId(rootOrgId);
        itemSeq.setCreater(creator);
        itemSeq.setCreateTime(new Date());
        return itemSeq;
    }

    public void updateVersion(Double maxNum, String modifier) {

        this.setItemVerMax(maxNum);
        this.setModifier(modifier);
        this.setModifyTime(new Date());
    }
}
