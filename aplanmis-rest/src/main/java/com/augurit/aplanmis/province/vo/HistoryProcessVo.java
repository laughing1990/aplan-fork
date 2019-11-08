package com.augurit.aplanmis.province.vo;

import com.augurit.agcloud.bpm.common.domain.BpmHistoryCommentForm;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.common.domain.AeaHiReceive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class HistoryProcessVo extends BpmHistoryCommentForm {

    //private String firstOrgShortName;
    //private String secondOrgShortName;
    //private Integer attDetailNum;
    //private String isApprover;
    private boolean dealingTask;
    private List<BscAttForm> fileList; //相关文件
    private List<AeaHiReceive> receiveList; //回执单
    private List<Long> smsDateList; //短信发送情况

    private List<HistoryProcessVo> childNode;
}