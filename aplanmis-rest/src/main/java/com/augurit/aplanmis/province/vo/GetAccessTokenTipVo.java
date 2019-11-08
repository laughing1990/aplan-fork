package com.augurit.aplanmis.province.vo;

/**
 * @author:chendx
 * @date: 2019-06-12
 * @time: 16:38
 */
public class GetAccessTokenTipVo {
    private boolean result;
    private String message;
    private String getAccessTokenUrl;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGetAccessTokenUrl() {
        return getAccessTokenUrl;
    }

    public void setGetAccessTokenUrl(String getAccessTokenUrl) {
        this.getAccessTokenUrl = getAccessTokenUrl;
    }
}
