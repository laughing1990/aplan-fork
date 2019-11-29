package com.augurit.aplanmis.admin.market.register.vo;

import com.augurit.aplanmis.common.domain.AeaImUnitService;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import lombok.Data;

import java.util.List;

@Data
public class RegisterServiceAndQualVo {
    private AeaImUnitService aeaImUnitService;//服务
    private List<AeaHiCertinstBVo> aeaHiCertinstBVo;//服务资格证书
}
