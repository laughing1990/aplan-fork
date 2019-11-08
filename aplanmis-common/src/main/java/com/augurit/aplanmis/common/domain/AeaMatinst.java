package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author xiaohutu
 */
@Data
public class AeaMatinst implements Serializable {
    private static final long serialVersionUID = 1L;

    private String inoutinstId;
    /**
     * (事项实例ID)
     */
    private String iteminstId;
    private String iteminstName;
    private String itemVerId;
    /**
     * 输入输出定义ID
     */
    private String itemInoutId;
    /**
     * 材料ID
     */
    private String matId;
    private String matCode;
    private String matFrom;
    /**
     * 是否通用材料  1 是 0 不是
     */
    private String isCommon;
    /**
     * 材料实例ID
     */
    private String matinstId;
    /**
     * 材料实例名称
     */
    private String matinstName;
    /**
     * 纸质件要求数量
     */
    private Long duePaperCount;
    /**
     * 实收纸质件数量
     */
    private Long realPaperCount;
    /**
     * 复印件要求数量
     */
    private Long dueCopyCount;
    /**
     * 实收复印件数量
     */
    private Long realCopyCount;
    /**
     * 电子材料是否必需，0表示容缺，1表示必须
     */
    private String attIsRequire;
    /**
     * 电子件数量
     */
    private Long attCount;
    /**
     * 实收纸质件材料实例ID
     */
    private String paperMatinstId;
    /**
     * 实收复印件材料实例ID
     */
    private String copyMatinstId;
    /**
     * 电子件材料实例ID
     */
    private String attMatinstId;
    /**
     * 是否批复文件，0表示否，1表示是
     */
    private String isOfficeDoc;
    private String officialDocNo;
    private String officialDocDueDate;
    private String officialDocTitle;

    private String isCollected;
    private String isParIn;
    private String parInId;
    /**
     * 上传的文件附件列表
     */
    private List<BscAttFileAndDir> attFileList;


}
