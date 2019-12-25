package com.augurit.aplanmis.front.solicit.vo;

import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.domain.AeaHiSolicitDetail;
import com.augurit.aplanmis.common.domain.AeaHiSolicitDetailUser;

import java.io.Serializable;
import java.util.List;

/**
 * 意见征求信息复合对象
 */
public class AeaHiSolicitInfo implements Serializable {
    private AeaHiSolicit solicit;
    private AeaHiSolicitDetailUser solicitDetailUser;
    private List<AeaHiSolicitDetail> solicitDetails;

    public AeaHiSolicit getSolicit() {
        return solicit;
    }

    public void setSolicit(AeaHiSolicit solicit) {
        this.solicit = solicit;
    }

    public AeaHiSolicitDetailUser getSolicitDetailUser() {
        return solicitDetailUser;
    }

    public void setSolicitDetailUser(AeaHiSolicitDetailUser solicitDetailUser) {
        this.solicitDetailUser = solicitDetailUser;
    }

    public List<AeaHiSolicitDetail> getSolicitDetails() {
        return solicitDetails;
    }

    public void setSolicitDetails(List<AeaHiSolicitDetail> solicitDetails) {
        this.solicitDetails = solicitDetails;
    }
}
