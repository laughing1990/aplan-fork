package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 领件信息
 * @author:sam
 */
public interface AeaHiSmsInfoService {
    /**
     * 新增领件信息
     * @param aeaHiSmsInfo
     * @return
     * @throws Exception
     */
    public AeaHiSmsInfo createAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception;

    /**
     * 更新领件信息
     * @param aeaHiSmsInfo
     * @throws Exception
     */
    public void updateAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception;

    /**
     * 根据ID获取领件信息
     * @param id
     * @return
     * @throws Exception
     */
    public AeaHiSmsInfo getAeaHiSmsInfoById(String id) throws Exception;

    /**
     * 根据ID删除领件信息
     * @param id
     * @throws Exception
     */
    public void deleteAeaHiSmsInfoById(String id) throws Exception;

    /**
     * 获取领件信息列表
     * @param aeaHiSmsInfo
     * @return
     * @throws Exception
     */
    public List<AeaHiSmsInfo> listAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception;

    /**
     * 获取领件信息分页列表
     * @param aeaHiSmsInfo
     * @param page
     * @return
     * @throws Exception
     */
    public PageInfo<AeaHiSmsInfo> listAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo, Page page) throws Exception;

    /**
     * 根据申请实例ID获取领件信息
     * @param applyinstId 申请实例ID
     * @return
     * @throws Exception
     */
    public AeaHiSmsInfo getAeaHiSmsInfoByApplyinstId(String applyinstId) throws Exception;

    /**
     * 根据领件人身份证获取领件信息列表
     * @param addresseeIdcard 领件人身份证
     * @return
     * @throws Exception
     */
    public List<AeaHiSmsInfo> listAeaHiSmsInfoByAddresseeIdcard(String addresseeIdcard) throws Exception;

    /**
     * 根据领件人手机获取领件信息列表
     * @param senderPhone 领件人手机
     * @return
     * @throws Exception
     */
    public List<AeaHiSmsInfo> listAeaHiSmsInfoBySenderPhone(String senderPhone) throws Exception;

    /**
     * 根据领件人名称模糊查询领件信息列表
     * @param senderName
     * @return
     * @throws Exception
     */
    public List<AeaHiSmsInfo> listAeaHiSmsInfoLikeSenderName(String senderName) throws Exception;

    /**
     * 根据单号或快递号获取领件信息
     * @param orderId 单号
     * @param expressNum 快递号
     * @return
     * @throws Exception
     */
    public AeaHiSmsInfo getAeaHiSmsInfoByOrderIdOrExpressNum(String orderId,String expressNum) throws Exception;
}
