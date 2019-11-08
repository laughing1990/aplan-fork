package com.augurit.aplanmis.data.exchange.dto.view;

import lombok.Data;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/10/22
 */
@Data
public class SpglViewSto {
    protected ViewSubTable masterTable;
    protected List<ViewSubTable> viewSubTableList;
}
