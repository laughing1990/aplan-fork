package com.augurit.aplanmis.common.check;

/**
 * 存放前置检查需要的信息
 */
public class CheckerContext {

    private static final ThreadLocal<CheckerInfo> checkerInfoContext = new ThreadLocal<>();

    public CheckerContext(CheckerInfo checkerInfo) {
        checkerInfoContext.set(checkerInfo);
    }

    public static CheckerInfo getCheckInfo() {
        return checkerInfoContext.get();
    }

    public void clear() {
        checkerInfoContext.set(null);
    }

    public String getProjInfoId() {
        return getCheckInfo().getProjInfoId();
    }
}
