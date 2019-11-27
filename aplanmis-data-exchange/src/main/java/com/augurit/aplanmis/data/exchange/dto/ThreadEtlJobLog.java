package com.augurit.aplanmis.data.exchange.dto;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobLog;

/**
 * @author yinlf
 * @Date 2019/11/26
 */
public class ThreadEtlJobLog {

    private ThreadEtlJobLog() {
    }

    private static ThreadLocal<EtlJobLog> local = new ThreadLocal<>();

    public static EtlJobLog get() {
        EtlJobLog etlJobLog = local.get();
        if (etlJobLog == null) {
            etlJobLog = new EtlJobLog();
            etlJobLog.setReadNum(0L);
            etlJobLog.setWrittenNum(0L);
            etlJobLog.setErrorNum(0L);
            local.set(etlJobLog);
        }
        return etlJobLog;
    }

    public static Long getJobLogId() {
        return ThreadEtlJobLog.get().getJobLogId();
    }

    public static void setJobLogId(Long jobLogId) {
        ThreadEtlJobLog.get().setJobLogId(jobLogId);
    }

    public static void increaseReadNum() {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long readNum = etlJobLog.getReadNum();
        readNum++;
        etlJobLog.setReadNum(readNum);
    }

    public static void readNumAddNum(int add) {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long readNum = etlJobLog.getReadNum();
        readNum = readNum + add;
        etlJobLog.setReadNum(readNum);
    }

    public static void reduceReadNum() {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long readNum = etlJobLog.getReadNum();
        readNum--;
        etlJobLog.setReadNum(readNum);
    }

    public static Long getReadNum() {
        return ThreadEtlJobLog.get().getReadNum();
    }

    public static void increaseWrittenNum() {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long writtenNum = etlJobLog.getWrittenNum();
        writtenNum++;
        etlJobLog.setWrittenNum(writtenNum);
    }

    public static void writtenNumAddNum(int add) {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long writtenNum = etlJobLog.getWrittenNum();
        writtenNum = writtenNum + add;
        etlJobLog.setWrittenNum(writtenNum);
    }

    public static void reduceWrittenNum() {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long writtenNum = etlJobLog.getWrittenNum();
        writtenNum--;
        etlJobLog.setWrittenNum(writtenNum);
    }

    public static Long getWrittenNum() {
        return ThreadEtlJobLog.get().getWrittenNum();
    }

    public static void increaseErrorNum() {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long errorNum = etlJobLog.getErrorNum();
        errorNum++;
        etlJobLog.setErrorNum(errorNum);
    }

    public static void errorNumAddNum(int add) {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long errorNum = etlJobLog.getErrorNum();
        errorNum = errorNum + add;
        etlJobLog.setErrorNum(errorNum);
    }

    public static void reduceErrorNum() {
        EtlJobLog etlJobLog = ThreadEtlJobLog.get();
        Long errorNum = etlJobLog.getErrorNum();
        errorNum--;
        etlJobLog.setErrorNum(errorNum);
    }

    public static Long getErrorNum() {
        return ThreadEtlJobLog.get().getErrorNum();
    }

}
