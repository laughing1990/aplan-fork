package com.augurit.aplanmis.province.auth;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

/**
 * @author:chendx
 * @date: 2019-06-12
 * @time: 14:49
 */
public class AesUtil {

    // static String sKey = "HOPERUN.COM";

    /**
     * 密钥如超过16位，截至16位，不足16位，补/000至16位
     *
     * @param key 原密钥
     * @return 新密钥
     */
    public static String secureBytes(String key) {
        if (key.length() > 16) {
            key = key.substring(0, 16);
        } else if (key.length() < 16) {
            for (int i = (key.length() - 1); i < 15; i++) {
                key += "\000";
            }
        }
        return key;
    }

    /**
     * AES解密 用于数据库储存
     *
     * @param sSrc
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptCode(String sSrc, String key) {

        String sKey = secureBytes(key);

        try {
            // 判断Key是否正确
            if (sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey);
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "GBK");
                return originalString;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * AES解密 用于数据库储存
     *
     * @param sSrc
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String key) {

        String sKey = secureBytes(key);

        try {
            // 判断Key是否正确
            if (sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.println("长度不是16");
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey);
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);

                String originalString = new String(original, "utf-8");
                // String originalString = new String(original, "GBK");

                return originalString;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }

    }

    public static String encrypt4Contacts(String sSrc) {
        return sSrc;
    }

    /**
     * AES加密
     *
     * @param sSrc
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String key) {

        String sKey = secureBytes(key);
        try {
            if (sSrc == null || sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey);
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            return byte2hex(encrypted).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param strhex
     * @return
     */
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    /**
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }


    public static void main(String[] args) {
        String key = "123";
        String token = UUID.randomUUID().toString();
        System.out.println("原文：" + token);
        String encrypt = encrypt(token, key);
        System.out.println("密文：" + encrypt);
        String decrypt = decrypt(encrypt, key);
        System.out.println("解密：" + decrypt);
    }
}
