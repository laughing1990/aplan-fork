package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangXinhui
 * @date 2019/11/4 004 15:55
 * @desc
 **/
@Data
public class PubParamDTO implements Serializable {

    private String access_token;
    private String request_id;

    public Map toMap() {
        Map data = new HashMap();
        data.put("access_token", access_token);
        data.put("request_id", request_id);
        return data;
    }

}
