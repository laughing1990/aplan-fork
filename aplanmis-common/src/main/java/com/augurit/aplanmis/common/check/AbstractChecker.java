package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.CheckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * 前置检查基类
 */
@Slf4j
public abstract class AbstractChecker<O> implements Checker<O> {

    public static final int HIGHEST_PRECEDENCE = 0;

    protected void beforeCheck(O o) {
        log.info("前置检查: " + this.getClass());
    }

    public String check(O o, CheckerContext checkerContext) throws CheckException {
        Assert.notNull(o, "待检查的对象不能为空");
        Assert.notNull(checkerContext, "待检查上下文对象不能为空");

        beforeCheck(o);

        return doCheck(o, checkerContext);
    }

    protected abstract String doCheck(O o, CheckerContext checkerContext) throws CheckException;

}
