package com.augurit.aplanmis.common.check.exception;

import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import lombok.Getter;

import java.util.List;

/**
 * 前置检查异常类
 */
@Getter
public class ItemPartFormCheckException extends CheckException {

    protected String message = "事项的前置扩展表单检查不通过";

    private List<AeaItemFrontPartformVo> aeaItemFrontPartformVoList;

    public ItemPartFormCheckException(String message) {
        super(message);
    }

    public ItemPartFormCheckException(String message, List<AeaItemFrontPartformVo> aeaItemFrontPartformVoList) {
        super(message);
        this.message = message;
        this.aeaItemFrontPartformVoList = aeaItemFrontPartformVoList;
    }
}
