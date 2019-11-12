package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.CheckException;

/**
 * 前置检查
 */
public interface Checker<O> {

    /**
     * 根据前置检查配置，校验是否满足前提条件， 不满足时抛出对应异常
     *
     * @param o              要前置检查的对象
     * @param checkerContext 上下文信息，如: 当前项目id
     * @throws CheckException 不通过时抛出的异常
     */
    String check(O o, CheckerContext checkerContext) throws CheckException;
}
