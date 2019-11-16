package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ZhangXinhui
 * @date 2019/11/4 004 16:36
 * @desc
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResDTO extends PubResponseDTO{

    private String access_token;//登录成功则返回安全会话访问令牌
}
