package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.augurit.aplanmis.common.domain.AeaHiItemSpecial;
import lombok.Data;

import java.util.List;

/**
 * @author:chendx
 * @date: 2019-10-30
 * @time: 16:33
 */
@Data
public class AeaHiIteminstDetailVo {
    /**
     * 事项基本信息
     */
    private AeaHiIteminstVo aeaHiIteminst;
    /**
     * 审批意见列表
     */
    List<BpmHistoryCommentFormVo> commentList;
    /**
     * 补正列表
     */
    List<AeaHiItemCorrect> supplyList;
    /**
     * 特殊程序列表
     */
    List<AeaHiItemSpecial> specialList;
    /**
     * 批文批复列表
     */
    List<OfficialDocumentInfoVo> officialDocumentList;


}
