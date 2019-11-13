package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.util.StringUtils;
import com.google.common.base.Strings;

/**
 * 脱敏处理工具类
 */
public  class DesensitizedUtil {

    /**
     * 手机号脱敏，保留前三后四中间*
     * @param phoneNumber
     * @return
     */
    public static String desensitizedPhoneNumber(String phoneNumber){
        if(StringUtils.isNotBlank(phoneNumber)){
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }
    /**
     * 身份证号脱敏，保留前六后e二中间*
     * @param idNumber
     * @return
     */
    public static String desensitizedIdNumber(String idNumber){
        if (!Strings.isNullOrEmpty(idNumber)) {
            if (idNumber.length() == 15){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{2})", "$1******$2");
            }
            if (idNumber.length() == 18){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{2})", "$1*********$2");
            }
        }
        return idNumber;
    }
}
