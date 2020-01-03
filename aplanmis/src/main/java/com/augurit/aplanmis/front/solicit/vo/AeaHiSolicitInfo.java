package com.augurit.aplanmis.front.solicit.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 意见征求信息复合对象
 */
public class AeaHiSolicitInfo implements Serializable {
    //征求主表实体信息
    private SolicitVo solicit;
    //当前需要被征求部门给意见的实体信息
    private SolicitDetailUserVo solicitDetailUser;
    //所有的被征求的详细信息
    private List<SolicitDetailVo> solicitDetails;

    public SolicitVo getSolicit() {
        return solicit;
    }

    public void setSolicit(SolicitVo solicit) {
        this.solicit = solicit;
    }

    public SolicitDetailUserVo getSolicitDetailUser() {
        return solicitDetailUser;
    }

    public void setSolicitDetailUser(SolicitDetailUserVo solicitDetailUser) {
        this.solicitDetailUser = solicitDetailUser;
    }

    public List<SolicitDetailVo> getSolicitDetails() {
        return solicitDetails;
    }

    public void setSolicitDetails(List<SolicitDetailVo> solicitDetails) {
        this.solicitDetails = solicitDetails;
    }
}
