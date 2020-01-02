package com.augurit.aplanmis.front.apply.vo.guide;

import com.augurit.aplanmis.common.domain.AeaHiGuide;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("部门辅导列表查询vo")
public class GuideQueryVo {

    // todo

    public AeaHiGuide toAeaHiGuide() {
        return new AeaHiGuide();
    }
}
