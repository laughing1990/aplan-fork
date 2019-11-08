package com.augurit.aplanmis.common.utils;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ImageFileReader {

	public static final String IMG_PATH = "/ui-static/agcloud/framework/ui-scheme/common/images/menu-icon/";

	public static final String SMALL_IMG = "16";
	public static final String MIDDLE_IMG = "24";
	public static final String BIG_IMG = "32";
	public static final String HUGE_IMG = "64";
	
	/**
	 * 读取目录下的png文件和gif文件
	 * @return
	 */
	public List<Map<String,String>> getImageFiles(String realPath,String imgSize){

		return getImageFileList(realPath, IMG_PATH + imgSize);
	}

	/**
	 * 读取目录下的png文件和gif文件
	 * @return
	 */
	public List<Map<String,String>> getImageFileList(String realPath, String filePath){

		//判断路径是否在war内,如果是，就是加压到某文件夹
		realPath = getUnzipRealpath(realPath);
		String relativePath = realPath + filePath;
		System.out.println("relativePath:"+relativePath);

		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		File file = new File(relativePath);
		if(file.exists()){
			File[] fileList = file.listFiles();
			for(File f : fileList){
				if(f.isFile()){
					String fileName = f.getName();
					//符合格式的图片才要
					if(fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".gif")) {
						Image img = null;
						try {
							img = javax.imageio.ImageIO.read(f);  //构造Image对象
						} catch (IOException e) {
							e.printStackTrace();
						}
						Map<String, String> map = new HashMap<String, String>();
						map.put("fileName", fileName.substring(0, fileName.indexOf(".")) + "_" + fileName.substring(fileName.indexOf(".") + 1));
						map.put("fullName", filePath + "/" + fileName);
						returnList.add(map);
					}
				}
			}
		}else{
			System.out.println("路径"+relativePath+"不存在");
		}
		return returnList;
	}

	/**
	 * 用war包运行项目，文件打包到war里面，不解压是获取不到里面的文件目录的，
	 * 在idea运行不会有该情况发生
	 * @param realPath
	 * @return
	 */
	private String getUnzipRealpath(String realPath) {

		try {
			if(realPath.indexOf(".war!") > 0){
				String os = System.getProperty("os.name");
				if(os.toLowerCase().startsWith("win")){
					realPath = realPath.replaceAll("\\*","/").substring(realPath.indexOf("/"));
				}
				String path = realPath.substring(0,realPath.indexOf("!/"));
				String tempPath = path.substring(0, path.lastIndexOf("/"))+"/tmp";
				String warName = path.substring(path.lastIndexOf("/"));
				File tmpFile = new File(tempPath);
				if(!tmpFile.exists()){
					tmpFile.mkdirs();
					//解压zip
					ZipFile zf = new ZipFile(path);
					for (Enumeration entries = zf.entries(); entries.hasMoreElements();){
						ZipEntry entry = (ZipEntry) entries.nextElement();
//                entry.setUnixMode(644);//解决Linux乱码
						String zipEntryName = entry.getName();
						InputStream in = zf.getInputStream(entry);
						BufferedInputStream bis = new BufferedInputStream(in);
						String outPath = (tempPath+"/"+zipEntryName).replaceAll("\\*","/");
						//判断路径是否存在，不存在则创建文件路径
						File file = new File(outPath.substring(0,outPath.lastIndexOf("/")));
						if (!file.exists()){
							file.mkdirs();
						}
						//判断文件全路径是否为文件夹，如果是上面已经上传，不需要解压
						if (new File(outPath).isDirectory()){
							continue;
						}

						OutputStream out = new FileOutputStream(outPath);
						byte[] buf1 = new byte[1024];
						int len;
						while ((len = in.read(buf1)) > 0){
							out.write(buf1,0,len);
						}
						in.close();
						out.close();
					}
				}
				return  tempPath;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return realPath;
	}
}
