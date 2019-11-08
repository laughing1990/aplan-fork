package com.augurit.aplanmis.common.service.admin.helper;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.aplanmis.common.utils.AuthHelper;
import com.augurit.aplanmis.common.utils.DESUtil;
import com.augurit.aplanmis.common.utils.HttpUtil;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class SyncItemInfoFromKPHelper {

    public static final String BASE_URL = AuthHelper.BASE_URL + "/access";

    /*事项*/
    public static final String GET_ALL_ITEM_LIST_API = "getAllItemList";

    /*事项情形*/
    public static final String GET_QX_BY_ITEM_CODE_API = "getQxByItemCode";

    /*材料清单*/
    public static final String GET_CLQD_BY_ITEM_CODE_API = "getClqdByItemCode";

    /*办事指南*/
    public static final String GET_BSZN_BY_ITEM_CODE_API = "getBsznByItemCode";

    /*开普文件下载*/
    public static final String DOWNLOAD_FILE_API = "downloadFile";

    /*事项前置条件*/
    public static final String GET_QZTJ_BY_ITEM_CODE_API = "getQztjByItemCode";

    //全路径拼接
    public static final String GET_ALL_ITEMS = BASE_URL + CommonConstant.FORWARD_SLASH_SEPARATOR_STR + GET_ALL_ITEM_LIST_API;
    public static final String GET_STATES_BY_ITEM = BASE_URL + CommonConstant.FORWARD_SLASH_SEPARATOR_STR + GET_QX_BY_ITEM_CODE_API;
    public static final String GET_MAT_BY_ITEM = BASE_URL + CommonConstant.FORWARD_SLASH_SEPARATOR_STR + GET_CLQD_BY_ITEM_CODE_API;
    public static final String GET_GUIDE_BY_ITEM = BASE_URL + CommonConstant.FORWARD_SLASH_SEPARATOR_STR + GET_BSZN_BY_ITEM_CODE_API;
    public static final String GET_QZTJ_BY_ITEM = BASE_URL + CommonConstant.FORWARD_SLASH_SEPARATOR_STR + GET_QZTJ_BY_ITEM_CODE_API;

    private static final String TOKEN_PARAM = "token";
    private static final String API_NAME_PARAM = "apiName";
    private static final String DATA_PARAM = "data";

    public static JSONObject getDataFromKP(String apiName, String dataParam) {
        Map<String, String> params = buildApiParams(apiName, dataParam);
        return HttpUtil.sendPost(BASE_URL, params, "");
    }

    private static Map<String, String> buildApiParams(String apiName, String customParam) {
        Assert.notNull(apiName, "apiName is null.");
        Assert.notNull(customParam, "customParam is null.");

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put(TOKEN_PARAM, getAccessToken());
        apiParams.put(API_NAME_PARAM, apiName);
        apiParams.put(DATA_PARAM, getEncryptDataParam(customParam));

        return apiParams;
    }

    private static String getEncryptDataParam(String customParam) {
        try {
            return DESUtil.encrypt(customParam, AuthHelper.LOGIN_PWD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("对参数加密失败, customParam: " + customParam, e);
        }
    }

    private static String getAccessToken() {
        try {
            return AuthHelper.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("准备调用开普接口拼接参数时获取token失败: " + e.getMessage(), e);
        }
    }
}
