package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaImUnitService;
import lombok.Data;

@Data
public class ServiceAndQualVo {
    private AeaImUnitService aeaImUnitService;//服务
    private AeaHiCertinstBVo aeaHiCertinstBVo;//服务资格证书
}
