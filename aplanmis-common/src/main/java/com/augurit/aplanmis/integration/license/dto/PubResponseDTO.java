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

    private String ack_code;

    private List<ErrorDTO> errors;

    private String sign;

    private String sign_method;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    private String correlation_id;

    private String response_id;

    private Long total_count;

    private T data;
}
