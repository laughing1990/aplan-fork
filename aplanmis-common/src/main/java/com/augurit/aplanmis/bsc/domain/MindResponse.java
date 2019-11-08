package com.augurit.aplanmis.bsc.domain;

import com.augurit.aplanmis.common.domain.AeaMindUi;

import java.io.Serializable;

/**
 *
 * 思维导图返回封装类
 *
 * @author hzl
 * @date 2019/4/17
 */
public class MindResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private MindBaseNode[] data;
    private AeaMindUi aeaMindUi;

    public MindResponse() {
    }

    public MindResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MindResponse(int code, String msg, MindBaseNode[] data) {
        this(code, msg);
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MindBaseNode[] getData() {
        return data;
    }

    public void setData(MindBaseNode[] data) {
        this.data = data;
    }

    public AeaMindUi getAeaMindUi() {
        return aeaMindUi;
    }

    public void setAeaMindUi(AeaMindUi aeaMindUi) {
        this.aeaMindUi = aeaMindUi;
    }
}
