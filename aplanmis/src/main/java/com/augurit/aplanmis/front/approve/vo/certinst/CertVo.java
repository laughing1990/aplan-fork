package com.augurit.aplanmis.front.approve.vo.certinst;

import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaCert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "证照定义vo", description = "证照定义vo")
public class CertVo {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "证照定义列表")
    List<Cert> certList;

    @ApiModelProperty(value = "单位列表")
    List<OpuOrg> opuOmOrgList;

    @ApiModelProperty(value = "项目规模")
    private String projScale;

    public CertVo() {
        this.certList = new ArrayList<>();
        this.opuOmOrgList = new ArrayList<>();
        this.projScale = "";
    }

    public void changeToCertVo(List<AeaCert> certs) {
        if (null == certList || certs.isEmpty()) return;
        for (AeaCert cert : certs) {
            Cert vo = new Cert();
            BeanUtils.copyProperties(cert, vo);
            this.certList.add(vo);
        }
    }

    public void changeToOrgVo(List<OpuOmOrg> omOrgs) {
        if (null == omOrgs || omOrgs.isEmpty()) return;
        for (OpuOmOrg org : omOrgs) {
            OpuOrg temp = new OpuOrg();
            BeanUtils.copyProperties(org, temp);
            this.opuOmOrgList.add(temp);
        }
    }

    @Data
    @ApiModel
    public class OpuOrg {
        @ApiModelProperty("主键")
        private String orgId;
        @ApiModelProperty("组织编号")
        private String orgCode;
        @ApiModelProperty("组织名称")
        private String orgName;
    }

    @Data
    @ApiModel
    public class Cert {
        @ApiModelProperty(value = "主键ID")
        private String certId;

        @ApiModelProperty(value = "证照证件编号，颁证单位编制的证照文号")
        private String certCode;

        @ApiModelProperty(value = "证照证件名称")
        private String certName;
    }
}
