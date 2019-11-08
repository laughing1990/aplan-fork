package com.augurit.aplanmis.front.portal.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author yinlf
 * @Date 2019/8/13
 */
@Data
public class ItemAndApplyCountVo {
    /**
     * 待办数
     */
    @JsonProperty("dai_ban")
    private Integer daiBan;
    /**
     * 已办数
     */
    @JsonProperty("yi_ban")
    private Integer yiBan;
    /**
     * 网上待预审数
     */
    @JsonProperty("wang_shang_dai_yu_shen")
    private Integer wangShangDaiYuShen;
    /**
     * 逾期数
     */
    @JsonProperty("yu_qi")
    private Integer yuQi;
    /**
     * 材料补全数
     */
    @JsonProperty("cai_liao_bu_zheng")
    private Integer caiLiaoBuZheng;
    /**
     * 督办数
     */
    @JsonProperty("du_ban")
    private Integer duBan;
    /**
     * 作废数
     */
    @JsonProperty("zuo_fei")
    private Integer zuoFei;
    /**
     * 挂起数
     */
    @JsonProperty("gua_qi")
    private Integer guaQi;
}
