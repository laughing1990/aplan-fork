package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AeaItemAgencyAux implements Serializable {
    private static final long serialVersionUID = 1L;

    private String agencyAuxId;//
    private String agencyId;//中介服务ID
    private String auxServiceId;//中介辅助服务ID
    private String creater;//创建人
    private Date createrTime;//创建时间
    private String modifier;//修改人
    private Date modifyTime;//修改时间
}
