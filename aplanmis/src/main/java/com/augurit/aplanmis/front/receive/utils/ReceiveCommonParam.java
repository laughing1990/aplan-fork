package com.augurit.aplanmis.front.receive.utils;

import lombok.Data;

/**
 * 回执参数（各项目组自行修改）
 */
@Data
public class ReceiveCommonParam {
    public static final String CITY_NAME="九江";
    private static String suffix="0792";
    protected static final String Telephone=suffix+"-2250205";//联系电话
    protected static final String Complaint_Telephone_Number=suffix+"-2250205";//监督电话

}
