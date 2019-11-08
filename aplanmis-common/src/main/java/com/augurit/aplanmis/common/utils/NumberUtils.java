package com.augurit.aplanmis.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 判断是否为整数
 * @param str 传入的字符串
 * @return 是整数返回true,否则返回false
 */

public class NumberUtils {
    private static Pattern pattern = Pattern.compile("[0-9]*");
    public static boolean isNumeric(String str){

        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
