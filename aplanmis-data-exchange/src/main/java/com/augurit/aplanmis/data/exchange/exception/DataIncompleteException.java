package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/10/22
 */
public class DataIncompleteException extends EtlTransException {
    public DataIncompleteException() {
        super(EtlError.DATA_INCOMPLETE);
    }

    public DataIncompleteException(String solution) {
        super(EtlError.DATA_INCOMPLETE, solution);
    }

    public void addSolution(String addStr){
        if(this.solution==null){
            this.solution = "";
        }
        StringBuilder builer = new StringBuilder(this.solution);
        builer.append(addStr);
        this.solution = builer.toString();
    }

    public void addUnrelatedTable(String tableName){
        if(this.solution==null){
            this.solution = "";
        }
        StringBuilder builer = new StringBuilder(this.solution);
        builer.append(tableName);
        builer.append("没有关联数据;");
        this.solution = builer.toString();
    }
}
