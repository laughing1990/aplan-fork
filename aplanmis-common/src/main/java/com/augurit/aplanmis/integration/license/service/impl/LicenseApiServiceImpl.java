package com.augurit.aplanmis.integration.license.service.impl;

import com.augurit.aplanmis.integration.license.config.LicenseConfig;
import com.augurit.aplanmis.integration.license.dto.*;
import com.augurit.aplanmis.integration.license.service.LicenseApiService;
import com.augurit.aplanmis.integration.license.utils.ConnectionHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ZhangXinhui
 * @date 2019/11/4 004 15:21
 * @desc
 **/
@Slf4j
@Service
public class LicenseApiServiceImpl implements LicenseApiService {
    @Autowired
    private LicenseConfig licenseConfig;
    @Autowired
    private ServletContext servletContext;

    @Override
    public LoginResDTO login() throws Exception {
        LoginReqDataDTO data = new LoginReqDataDTO();
        data.setAccount(licenseConfig.getAccount());
        data.setApp_key(licenseConfig.getAppKey());
        data.setApp_secret(licenseConfig.getAppSecret());
        data.setPassword(licenseConfig.getPassword());
        return send("/security/login", "post", null, data, null, LoginResDTO.class);
    }

    @Override
    public String getLoginToken() throws Exception {
        Date now = new Date();
        if (servletContext.getAttribute("third_parner_postal_login_tokenEntity") == null) {
            LoginResDTO loginResDTO = login();
            loginResDTO.setTimestamp(now);
            servletContext.setAttribute("third_parner_postal_login_tokenEntity", loginResDTO);
        }
        LoginResDTO loginResDTO = (LoginResDTO) servletContext.getAttribute("third_parner_postal_login_tokenEntity");
        if ((now.getTime() - loginResDTO.getTimestamp().getTime()) > (24 * 60 - 5) * 60 * 1000) {//超过23小时55分，重新登录
            loginResDTO = login();
            loginResDTO.setTimestamp(now);
            servletContext.setAttribute("third_parner_postal_login_tokenEntity", loginResDTO);
        }
        return loginResDTO.getAccess_token();
    }

    @Override
    public PubResponseDTO logout(String accessToken) throws Exception {

        return send("/security/logout", "post", initPubParam(accessToken).toMap(), null, null, PubResponseDTO.class);
    }

    @Override
    public LicenseAuthResDTO licenseAuth(String accessToken, LicenseAuthReqDTO data) throws Exception {
        return send("/license/auth", "post", initPubParam(accessToken).toMap(), data, null, LicenseAuthResDTO.class);
    }

    @Override
    public LicenseTokenResDTO LicenseToken(String accessToken, String authCode) throws Exception {
        Map param = new HashMap<String, String>(1);
        param.put("auth_code", authCode);
        return send("/license/token", "get", mergeParam(initPubParam(accessToken).toMap(), param), null, null,
                    LicenseTokenResDTO.class);

    }

    @Override
    public GetLicenseResDTO getLicense(String accessToken, String authCode) throws Exception {
        Map param = new HashMap<String, String>(1);
        param.put("auth_code", authCode);
        return send("/license/get_license", "get", mergeParam(initPubParam(accessToken).toMap(), param), null, null,
                    GetLicenseResDTO.class);
    }

    @Override
    public LicenseArchiveResDTO licenseArchive(String accessToken, String authCode, String fileType) throws Exception {
        Map param = new HashMap<String, String>(1);
        param.put("auth_code", authCode);
        param.put("file_type", fileType);
        return send("/license/archive", "get", mergeParam(initPubParam(accessToken).toMap(), param), null, null,
                    LicenseArchiveResDTO.class);
    }



    /**
     * 初始化公共参数
     *
     * @param accessToken
     * @return
     */
    private PubParamDTO initPubParam(String accessToken) {
        PubParamDTO paramDTO = new PubParamDTO();
        paramDTO.setAccess_token(accessToken);
        paramDTO.setRequest_id(String.valueOf(System.currentTimeMillis() / 1000));
        return paramDTO;
    }

    /**
     * @param operate 请求url
     * @param method  请求方式
     * @param param   请求参数
     * @param data    请求数据（application/json请求类型）
     * @param resType 结果转换对象类型
     * @return
     * @throws Exception
     */
    private <T> T send(String operate, String method, Map param, Object data, Map headMap, Class<T> resType) throws Exception {
        if (method.toLowerCase().equals("post")) {
            return new ObjectMapper().readValue(
                    ConnectionHelper.doPost(licenseConfig.getApiRoot() + operate, param, data, headMap),
                    resType);
        } else {
            return new ObjectMapper().readValue(
                    ConnectionHelper.doGet(licenseConfig.getApiRoot() + operate, param, null, headMap),
                    resType);
        }

    }

    /**
     * 合并参数
     *
     * @param pubParam 公共参数
     * @param extParam 拓展参数
     * @return
     */
    private Map mergeParam(Map pubParam, Map extParam) {
        Map param = new HashMap();
        if (pubParam != null) {
            param.putAll(pubParam);
        }
        if (extParam != null) {
            param.putAll(extParam);
        }
        return param;
    }
}
