package com.augurit.aplanmis.data.exchange.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @Date 2019/11/27
 */
@Data
public class EtlReuploadVo {
    private List<String> tableNames = new ArrayList<>();
    private Date startTime;
    private Date endTime;
}
