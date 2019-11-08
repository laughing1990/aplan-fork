package com.augurit.aplanmis.common.utils;


public class Md5Utils {
    public Md5Utils() {
    }

    public static String encrypt32(String encryptStr) {
        encryptStr = MD5.GetMD5Code(encryptStr);
        return encryptStr;
    }

    public static String encrypt16(String encryptStr) {
        return encrypt32(encryptStr).substring(8, 24);
    }

    public static void main(String[] args) {
        System.out.println(encrypt32("123456"));
        System.out.println(encrypt16("123456"));
    }
}