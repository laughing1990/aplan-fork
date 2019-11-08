package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.vo.AeaImMajorQualVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
* -资质表
*/
@Data
public class AeaImQual implements Serializable{

        private static final long serialVersionUID = 1L;

        private String qualId; // ()
        private String qualCode; // (资质编码)
        private String qualName; // (资质名称)
        private String isDelete; // (是否删除，0表示未删除，1表示已删除)
        private String creater; // (创建人)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private String memo; // (备注)
        private String qualLevelId; // (资质等级ID)
        private String rootOrgId;//根组织ID

        //扩展字段
        private String keyword;
        private int ischeck;
        private String itemVerId;
        private String qualLevelName;
        private List<AeaImMajorQualVo> aeaImMajorQualVoList;


}
