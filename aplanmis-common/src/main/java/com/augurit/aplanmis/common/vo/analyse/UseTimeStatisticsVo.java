package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/11/7 10:51
 */
@Data
public class UseTimeStatisticsVo {

    private String themeId;
    private String themeName;

    private String windowId;
    private String windowName;

    private String applyRecordId;
    private String applyRecordName;

    private String regionId;
    private String regionName;

    private String orgId;
    private String orgName;

    private String itemId;
    private String itemName;

    private Double maxUseTime;
    private Double minUseTime;
    private Double avgUseTime;

}
