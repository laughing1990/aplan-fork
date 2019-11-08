package com.augurit.aplanmis.supermarket.main.vo;

import lombok.Data;

@Data
public class BiddingNoticeVo {

    private String projServiceId;
    //项目名称
    private String projName;

    //业主姓名
    private String ownerUserName;
    //中介事项名称
    private String biddingItemName;
    //竞价金额
    private String biddingAmount;
    //竞价状态
    private String biddingState;
    //竞价状态描述
    private String biddingStateStr;
}
