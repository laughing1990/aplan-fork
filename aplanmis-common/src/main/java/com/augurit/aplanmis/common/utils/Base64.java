package com.augurit.aplanmis.common.utils;

import java.io.ByteArrayOutputStream;

public class Base64 {
	
	public static String BASE64_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	
	private String base64Table;
	
	public String getBase64Table() {
		return base64Table;
	}

	public void setBase64Table(String base64Table) {
		if(base64Table.length() != 65) 
			throw new IllegalArgumentException("base64必须65位");
		this.base64Table = base64Table;
	}
	
	public static Base64 createBase64(){
		return createBase64(BASE64_TABLE);
	}
	
	public static Base64 createBase64(String base64Table){
		Base64 base64 = new Base64();
		base64.setBase64Table(base64Table);
		return base64;
	}

	/**
	 * Base64 编码
	 * @param bytes		字节数组
	 * @return
	 */
	public String encode(byte[] bytes){
		int length = bytes.length;
		StringBuilder sb = new StringBuilder();
		byte[] bt = new byte[4];
		byte b;
		int flag = -1;
		
		for(int i=0; i<length; i=i+3){
			for(int j=i; j<i+3 && j<length; j++){
				b = bytes[j];
				switch (j % 3) {
					case 0:
						bt[0] = (byte) ((b & 0xfc) >> 2);
						bt[1] = (byte) ((b & 3) << 4);
						flag = 1;
						break;
					case 1:
						bt[1] = (byte)(bt[1] + ((b & 0xf0) >> 4));
						bt[2] = (byte)((b & 0xf) << 2);
						flag = 2;
						break;
					case 2:
						bt[2] = (byte)(bt[2] + ((b & 0xc0) >> 6));
						bt[3] = (byte)(b & 0x3f); 
						flag = 3;
						break;
					default:
						break;
				}
			}
			
			for(int k=0;k<4;k++){
				if(k > flag) bt[k] = 64;
				sb.append(base64Table.charAt(bt[k]));
			}
			flag = -1;
		}
		
		return sb.toString();
	}
	
	/**
	 * Base64 解码
	 * @param data			解码数据
	 * @return
	 */
	public byte[] decode(String data){
		byte[] bytes = data.getBytes();
		int length = bytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bt = new byte[3];
		byte b;
		int flag = -1;
		
		for(int i=0; i<length; i=i+4){
			for(int j=i; j<i + 4 && j<length; j++){
				b = (byte)(base64Table.indexOf(bytes[j]));
				if(64 != b) {
					switch (j % 4) {
						case 0:
							bt[0] = (byte)(b << 2);
							break;
						case 1:
							bt[0] = (byte)(bt[0] + ((b & 0xf0) >> 4));
							bt[1] = (byte)((b & 0xf) << 4);
							flag = 0;
							break;
						case 2:	
							bt[1] = (byte)(bt[1] + ((b & 0xfc) >> 2));
							bt[2] = (byte)((b & 3) << 6);
							flag = 1;
							break;
						case 3:
							bt[2] = (byte)(bt[2] + b);
							flag = 2;
							break;
						default:
							break;
					}
				}
			}
			for(int k=0; k <=flag; k++){
				out.write(bt[k]);
			}
			flag = -1;
		}
		return out.toByteArray();
	}
}
