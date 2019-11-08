package com.augurit.aplanmis.front.approve.vo.official;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.util.DateUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("申报材料")
@Data
public class OfficialDocumentInfoVo {

    private static final String DOC_TYPE_PAPER = "纸质";
    private static final String DOC_TYPE_ELEC = "电子";

    private static String CREATE_DATE_PATTEN = "yyyy.MM.dd";

    @ApiModelProperty(value = "ID", required = true, dataType = "string")
    private String matinstId;
    @ApiModelProperty(value = "材料实例名称", required = true, dataType = "string")
    private String matinstName;

    @ApiModelProperty(value = "材料ID", required = true, dataType = "string")
    private String matId;

    @ApiModelProperty(value = "文件文号", required = true, dataType = "string")
    private String officialDocNo;

    @ApiModelProperty(value = "文件标题", required = true, dataType = "string")
    private String officialDocTitle;

    @ApiModelProperty(value = "有效期限", required = true, dataType = "string")
    private String officialDocDueDate;

    @ApiModelProperty(value = "批复文件日期【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】", required = true, dataType = "string")
    private String officialDocPublishDate;

    @ApiModelProperty(value = "材料文件类型名称", required = true, dataType = "string")
    private String docTypeName;

    @ApiModelProperty(value = "材料文件个数", required = true, dataType = "string")
    private int docCount = 0;

    @ApiModelProperty(value = "创建人", required = true, dataType = "string")
    private String creator;

    @ApiModelProperty(value = "创建时间", required = true, dataType = "string")
    private String createDate;

    @ApiModelProperty(value = "备注", required = true, dataType = "string")
    private String memo;

    // 扩展字段
    private List<BscAttFileAndDir> attFiles;//  附件列表

    public static OfficialDocumentInfoVo from(AeaHiItemMatinst aeaHiItemMatinst) {
        OfficialDocumentInfoVo vo = new OfficialDocumentInfoVo();
        if (null != aeaHiItemMatinst) {
            vo.setMatId(aeaHiItemMatinst.getMatId());
            vo.setMatinstId(aeaHiItemMatinst.getMatinstId());
            vo.setMatinstName(aeaHiItemMatinst.getMatinstName());
            vo.setOfficialDocNo(aeaHiItemMatinst.getOfficialDocNo());
            vo.setOfficialDocTitle(aeaHiItemMatinst.getOfficialDocTitle());
            vo.setOfficialDocDueDate(DateUtils.parseToDefaultDateString(aeaHiItemMatinst.getOfficialDocDueDate()));
            vo.setOfficialDocPublishDate(DateUtils.parseToDefaultDateString(aeaHiItemMatinst.getOfficialDocPublishDate()));
            vo.setCreateDate(DateUtils.parseToFormatDateString(aeaHiItemMatinst.getCreateTime(), CREATE_DATE_PATTEN));
            vo.setCreator(aeaHiItemMatinst.getCreater());
            vo.setDocTypeName(DOC_TYPE_ELEC);
            vo.setDocCount(aeaHiItemMatinst.getAttCount() == null ? 0 : aeaHiItemMatinst.getAttCount().intValue());
            vo.setMemo(aeaHiItemMatinst.getMemo());
        }
        return vo;
    }
}
