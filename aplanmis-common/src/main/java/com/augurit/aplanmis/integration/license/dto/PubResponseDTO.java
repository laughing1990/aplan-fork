package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/11/4 004 15:52
 * @desc
 **/
@Data
public class PubResponseDTO<T> implements Serializable {

    private String ack_code;//接口调用是否成功

    private List<ErrorDTO> errors;//错误信息

    private String sign;//请求参数签名值（预留）

    private String sign_method;//签名方法（预留）

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;//接口响应的服务端时间

    private String correlation_id;//对应的请求 request_id

    private String response_id;//接口返回的唯一标识号

    private Long total_count;//记录数

    private T data;//证照列表
}
