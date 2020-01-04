package com.augurit.aplanmis.front.apply.vo.guide;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter
@ApiModel(value = "部门辅导列表查询vo")
public class GuideQueryVo {

    @ApiModelProperty(value = "项目、主题、工程代码关键字")
    private String keyword;

    @ApiModelProperty(value = "部门辅导状态")
    private String applyState;

    @ApiModelProperty(value = "部门辅导发起时间")
    private String guideStartTime;

    @ApiModelProperty(value = "部门辅导结束时间")
    private String guideEndTime;

    public AeaHiGuide toAeaHiGuide() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AeaHiGuide aeaHiGuide = new AeaHiGuide();
        aeaHiGuide.setKeyword(this.keyword);
        aeaHiGuide.setApplyState(this.applyState);
        if (StringUtils.isNotBlank(guideEndTime)) {
            aeaHiGuide.setGuideEndTime(sdf.parse(guideEndTime));
        }
        if (StringUtils.isNotBlank(guideStartTime)) {
            aeaHiGuide.setGuideStartTime(sdf.parse(guideStartTime));
        }
        return aeaHiGuide;
    }
}
