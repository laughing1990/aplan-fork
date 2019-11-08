package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AeaItemAgency implements Serializable {
    private static final long serialVersionUID = 1L;

    private String agencyId;//中介服务ID
    private String itemId;//本级事项目录ID
    private String sfxyzjfw;//是否需要中介服务辅助办理
    private String zjfwssyj;//中介服务实施依据
    private String dataId;//省的数据ID
    private String zjfwssyjIds;//中介服务实施依据IDS
    private String creater;//创建人
    private Date createrTime;//创建时间
    private String modifier;//修改人
    private Date modifyTime;//修改时间

    private String keyword;
}
