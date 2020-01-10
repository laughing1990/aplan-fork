package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("申报出件domain")
public class AeaHiSmsSendApply extends AeaHiSmsSendBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sendApplyId;

    private String sendApplyCode;
}
