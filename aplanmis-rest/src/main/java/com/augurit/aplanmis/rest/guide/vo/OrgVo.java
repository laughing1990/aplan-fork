package com.augurit.aplanmis.rest.guide.vo;

import lombok.Data;

@Data
public class OrgVo {
    private String orgId;
    private String orgName;

    public OrgVo(String orgId, String orgName) {
        this.orgId = orgId;
        this.orgName = orgName;
    }
}
