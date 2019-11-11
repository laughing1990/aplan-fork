package com.augurit.aplanmis.integration.license.service;

import com.augurit.aplanmis.integration.license.dto.*;

/**
 * @author ZhangXinhui
 * @date 2019/11/4 004 15:20
 * @desc
 **/
public interface LicenseApiService {
    LoginResDTO login() throws Exception;

    PubResponseDTO logout(String accessToken) throws Exception;

    LicenseAuthResDTO licenseAuth(String accessToken, LicenseAuthReqDTO data) throws Exception;

    LicenseTokenResDTO LicenseToken(String accessToken, String authCode) throws Exception;

    GetLicenseResDTO getLicense(String accessToken, String authCode) throws Exception;

    LicenseArchiveResDTO licenseArchive(String accessToken, String authCode, String fileType) throws Exception;
}
