package com.augurit.aplanmis.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

@Deprecated
public class FileUtils extends FileUtilsBase {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);


    public static boolean uploadFile(String tableName, String pkName, String recordId, HttpServletRequest request) {
        try {

            if (StringUtils.isBlank(tableName) || StringUtils.isBlank(pkName) || StringUtils.isBlank(recordId))
                throw new Exception("缺失参数");

            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            List<MultipartFile> files = req.getFiles("file");

            return uploadFile(tableName, pkName, recordId, files);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public static boolean uploadFile(String tableName, String pkName, String recordId, MultipartFile file) {
        try {
            if (StringUtils.isBlank(tableName) || StringUtils.isBlank(pkName) || StringUtils.isBlank(recordId))
                throw new Exception("缺失参数");

            if (file.getSize() > 0 && StringUtils.isNotBlank(file.getOriginalFilename())) {
                upload(tableName, pkName, recordId, file);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static boolean uploadFile(String tableName, String pkName, String recordId, List<MultipartFile> files) {
        try {

            if (StringUtils.isBlank(tableName) || StringUtils.isBlank(pkName) || StringUtils.isBlank(recordId))
                throw new Exception("缺失参数");

            for (MultipartFile file : files) {
                if (file.getSize() <= 0 && StringUtils.isBlank(file.getOriginalFilename())) {
                    continue;
                }
                upload(tableName, pkName, recordId, file);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static boolean deleteFiles(String[] detailIds) {
        try {
            for (String detailId : detailIds) {
                if (StringUtils.isBlank(detailId)) return false;
                delete(detailId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    // 通过key值获取文件中的value
    public static Object getValue(String filePathStr, String fileName, String key) {

        JSONObject eJSON = null;
        eJSON = read2JSON(filePathStr, fileName);
        if (null != eJSON && eJSON.containsKey(key)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> values = JSON.parseObject(eJSON.toString(), Map.class);
            return values.get(key);
        } else {
            return null;
        }
    }

    // 读文件到json
    public static JSONObject read2JSON(String filePathStr, String fileName) {

        File file = new File(filePathStr + File.separator + fileName + ".xml");
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (JSONObject) JSON.parse(laststr);
    }

    // 读文件到json
    public static JSONObject read2File(File file) {

        BufferedReader reader = null;
        StringBuffer laststr = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return (JSONObject) JSON.parse(laststr.toString());
    }

    // json写入文件
    public synchronized static void write2File(String filePathStr, Object json, String fileName) {

        BufferedWriter writer = null;
        File filePath = new File(filePathStr);
        JSONObject eJSON = null;

        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdirs();
        }

        File file = new File(filePathStr + File.separator + fileName + ".xml");
        System.out.println("path:" + file.getPath() + " abs path:" + file.getAbsolutePath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("createNewFile，出现异常:");
                e.printStackTrace();
            }
        } else {
            eJSON = read2JSON(filePathStr, fileName);
        }

        try {
            writer = new BufferedWriter(new FileWriter(file));

            if (eJSON == null) {
                writer.write(json.toString());
            } else {
                Object[] array = ((JSONObject) json).keySet().toArray();
                for (int i = 0; i < array.length; i++) {
                    eJSON.put(array[i].toString(), ((JSONObject) json).get(array[i].toString()));
                }

                writer.write(eJSON.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
