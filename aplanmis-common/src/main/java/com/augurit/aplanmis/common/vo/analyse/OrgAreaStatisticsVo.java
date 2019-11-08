package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

import java.io.Serializable;
@Data
public class OrgAreaStatisticsVo  implements Serializable {

    private String regionName;
    private String regionId;
    private String areaCount;
}
