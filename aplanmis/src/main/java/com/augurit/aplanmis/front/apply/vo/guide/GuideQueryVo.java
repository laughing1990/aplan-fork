package com.augurit.aplanmis.front.apply.vo.guide;

import com.augurit.aplanmis.common.domain.AeaHiGuide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ApiModel(value = "部门辅导列表查询vo")
public class GuideQueryVo {

    @ApiModelProperty(value = "项目、主题、工程代码关键字")
    private String keyword;

    @ApiModelProperty(value = "部门辅导状态")
    private String applyState;

    @ApiModelProperty(value = "部门辅导发起时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date guideStartTime;

    @ApiModelProperty(value = "部门辅导结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date guideEndTime;

    public AeaHiGuide toAeaHiGuide() {
        AeaHiGuide aeaHiGuide = new AeaHiGuide();
        aeaHiGuide.setKeyword(this.keyword);
        aeaHiGuide.setApplyState(this.applyState);
        aeaHiGuide.setGuideStartTime(guideStartTime);
        aeaHiGuide.setGuideEndTime(guideEndTime);
        return aeaHiGuide;
    }
}
