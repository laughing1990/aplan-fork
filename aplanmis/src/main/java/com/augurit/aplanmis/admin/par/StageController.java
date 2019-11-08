package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.dto.StageMatDto;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatAdminService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/stage")
public class StageController {

    private AeaItemMatAdminService aeaItemMatAdminService;

    @PostMapping("/saveMatAndParIn.do")
    public ResultForm saveStageMatAndParIn(HttpServletRequest request,StageMatDto stageMatDto) throws Exception {

        Assert.notNull(stageMatDto.getStageId(), "stageId不能为空");
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        if (StringUtils.isNotBlank(stageMatDto.getMatId())) {
            AeaItemMat aeaItemMat = stageMatDto.toAeaItemMat(userId, rootOrgId);
            aeaItemMat.setMatId(stageMatDto.getMatId());
            aeaItemMatAdminService.updateAeaItemMat(request, aeaItemMat);
        } else {
            AeaItemMat aeaItemMat = stageMatDto.toAeaItemMat(userId, rootOrgId);
            aeaItemMatAdminService.saveAeaItemMatAndParIn(request, stageMatDto.getStageId(), stageMatDto.getIsStateIn(), stageMatDto.getParStateId(), aeaItemMat);
        }

        return new ResultForm(true);
    }

    @Autowired
    public void setAeaItemMatAdminService(AeaItemMatAdminService aeaItemMatAdminService) {
        this.aeaItemMatAdminService = aeaItemMatAdminService;
    }
}
