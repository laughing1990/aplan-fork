package com.augurit.aplanmis.integration.license.service;

import com.augurit.aplanmis.integration.license.dto.*;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/11/4 004 15:20
 * @desc
 **/
public interface LicenseApiService {
    /**
     * 登录接口
     * @return
     * @throws Exception
     */
    LoginResDTO login() throws Exception;

    /**
     * 登出接口
     * @param accessToken
     * @return
     * @throws Exception
     */
    PubResponseDTO logout(String accessToken) throws Exception;

    /**
     * 获取登录token，如果session已保存token则不调用登录接口
     * @return
     * @throws Exception
     */
    String getLoginToken() throws Exception;

    /**
     * 依智能用证调用接口
     * @param accessToken
     * @param data
     * @return
     * @throws Exception
     */
    LicenseAuthResDTO licenseAuth(String accessToken, LicenseAuthReqDTO data) throws Exception;

    /**
     * 以用证码换取token凭证
     * @param accessToken
     * @param authCode 用证码
     * @return
     * @throws Exception
     */
    LicenseTokenResDTO LicenseToken(String accessToken, String authCode) throws Exception;

    /**
     * 以用证码获取证照新信息
     * @param accessToken
     * @param authCode
     * @return
     * @throws Exception
     */
    GetLicenseResDTO getLicense(String accessToken, String authCode) throws Exception;

    /**
     * 归档电子证照
     * @param accessToken
     * @param authCode
     * @param fileType
     * @return
     * @throws Exception
     */
    LicenseArchiveResDTO licenseArchive(String accessToken, String authCode, String fileType) throws Exception;

    /**
     * 依智能用证调用接口（批量）
     * @param accessToken
     * @param list
     * @return
     * @throws Exception
     */
    List<LicenseAuthResDTO> licenseAuthMulti(String accessToken, List<LicenseAuthReqDTO> list) throws Exception;
}
