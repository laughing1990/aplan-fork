package com.augurit.aplanmis.supermarket.vo;

import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.augurit.aplanmis.common.vo.ElTreeVo;
import lombok.Data;

import java.util.List;

@Data
public class ServiceQualVo {

    /**资质名称**/
    private String qualName;

    /**专业树**/
    private List<ElTreeVo> majorTree;

    /**资质等级**/
    private List<AeaImQualLevel> levelList;
}
