package com.augurit.aplanmis.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * md5加密出来的长度是32位
 * sha加密出来的长度是40位
 */
public class Encrypt
{
	/**
	 * 加密
	 * @param inputText
	 * @return
	 */
	public static String e(String inputText)
	{
		return md5(inputText);
	}

	/**
	 * 二次加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5AndSha(String inputText)
	{
		return sha(md5(inputText));
	}

	/**
	 * md5加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText)
	{
		return encrypt(inputText, "md5");
	}

	/**
	 * sha加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String sha(String inputText)
	{
		return encrypt(inputText, "sha-1");
	}

	/**
	 * md5或者sha-1加密
	 * @param inputText 要加密的内容
	 * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName)
	{
		if (inputText == null || "".equals(inputText.trim()))
		{
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim()))
		{
			algorithmName = "md5";
		}
		String encryptText = null;
		try
		{
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return encryptText;
	}

	/**
	 * 返回十六进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i)
		{
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex)
	{
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++)
		{
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c)
	{
		byte b = (byte) "0123456789abcdef".indexOf(c);
		return b;
	}
	
	public static String encodeSHA256(byte[] inputText){
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(inputText);
			byte[] hash = messageDigest.digest();
			return hex(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}