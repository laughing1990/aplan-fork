package com.augurit.aplanmis.front.approve.vo.official;

import com.augurit.agcloud.framework.util.DateUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

@Data
public class OfficialDocumentEditVo {

    private static final Logger loggger = LoggerFactory.getLogger(OfficialDocumentEditVo.class);

    /*文件文号*/
    private String officialDocNo;
    /*文件标题*/
    private String officialDocTitle;
    /*有效期限*/
    private String officialDocDueDate;
    /*批复日期*/
    private String officialDocPublishDate;
    /*电子件份数*/
    private Long attCount = 0L;
    /*事项实例ID*/
    private String iteminstId;
    /*申请实例ID*/
    private String applyinstId;
    /*备注*/
    private String memo;

    /*并联审批taskId*/
    private String taskId;
    private String matId;//批文批复对应的材料matId

    /*材料实例ID*/
    private String matinstId;

    public Date strToDate(String dateStr) {
        try {
            return DateUtils.toDefaultDate(dateStr);
        } catch (ParseException e) {
            loggger.error("新建批文批复操作中解析日期失败, dateStr: " + dateStr + e.getMessage(), e);
            throw new RuntimeException("新建批文批复操作中解析日期失败, dateStr: " + dateStr, e);
        }
    }
}
