package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgentLinkmanCert implements Serializable {
    private static final long serialVersionUID = 1L;
    /**联系人姓名*/
    private String linkmanName;
    private String serviceName;
    private String serviceId;
    private String certinstName;
    private String linkmanMobilePhone;
}
