package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaImQual;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/5/005 15:39
 */
@Data
public class AeaHiCertinstBVo extends AeaHiCertinst implements Serializable {

    private String qualLevelName; //等级名称
    private List<String> majorId;//专业id
    private String qualId;//资质id
    private String qualName;//资质名称
    private String linkmanInfoId;
    private String serviceId; // 中介服务id
    private List<BscAttFileAndDir> certinstDetail;
    private List<AeaImServiceMajor> ServiceMajor;
    private List<ElTreeVo> majorElTree;
    private List<AeaImQual> qualMajorList;

}
