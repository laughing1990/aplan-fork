package com.augurit.aplanmis.common.service.project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 提供印章编码，印章图形在pdf 上生成，已加盖电子印章文件印章在线验证api
 *
 * StampService
 * @Author: Gongxs
 * @CreateDate:2019/8/19 16:38
 */

public interface StampService {

    /**
     * 获取随机数，增强安全性
     * @return
     */
    String getRandom();

    /**
     * 用信用代码获取印章编号
     * @param unifiedSocialCreditCode
     * @return
     */
    String getStampCode(String unifiedSocialCreditCode);

    /**
     * 对源文件进行处理， 生成带有印章的新文件
     * @param sourceFilePath 没有盖章源文件地址
     * @param targetPath 盖好章的目标文件地址
     */
    void stampSeal(String sourceFilePath, String targetPath, Map<String, String> parameterMap);

    /**
     * 对源文件进行处理， 生成带有印章的新文件
     * 无需指定文件访问地址，只需要获取文件输入流，并返回输出流
     * @param tempFilePath  缓存文件地址
     * @param out 获得的加盖签章的输出流
     * @param parameterMap 盖章需要的入参 hash集合
     *                     该hashmap 必须传入的参数： "page": 盖章所在页（0或其他整数值，最大值为文件最大页码 为0，所有页都盖章）
     *                                    "unifiedSocialCreditCode": 申请企业或单位的信用代码
     *                                    "text": 企业名称
     *                                    "title": 待盖章文档的标题
     *                                    "orderId": 当前业务的业务号，
     */
    void stampSeal(String tempFilePath, FileOutputStream out, Map<String, String> parameterMap);

    /**
     * 获取输入流， 创建缓存文件，并对文件进行在线验证
     * @param in 待验证的盖章文件io 输入流，
     * @param projCode 项目代码
     */
    void verifyStamp(InputStream in, String projCode) throws IOException;

}
