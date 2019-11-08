package com.augurit.aplanmis.common.service.receive;

import com.augurit.aplanmis.common.domain.AeaHiReceive;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AeaHiReceiveService {
    /**
     * 1.根据申请实例ID和回执类型获取指定类型的回执
     * @param applyinstId  必须参数  申请实例ID
     * @param receiptType  必须参数  回执类型
     * @return
     * @throws Exception
     */
    public AeaHiReceive getAeaHiReceiveByApplyinstIdAndReceiveType(String applyinstId, String receiptType) throws Exception;

    /**
     *  2.根据申请实例ID获取回执列表
     * @param applyinstId 必须参数  申请实例ID
     * @return
     * @throws Exception
     */
    public List<AeaHiReceive> getReceiveListByApplyinstId(String applyinstId) throws Exception;

    /**
     *  3.新增/修改回执
     * @param aeaHiReceive
     * @throws Exception
     */
    public void saveAeaHiReceive(AeaHiReceive aeaHiReceive) throws Exception;

    /**
     * 4.根据ID删除回执
     * @param id
     * @throws Exception
     */
    public void deleteAeaHiReceive(@Param("id") String id) throws Exception;

    /**
     * 5.查询回执列表
     * @param aeaHiReceive
     * @return
     * @throws Exception
     */
    List<AeaHiReceive> listAeaHiReceive(AeaHiReceive aeaHiReceive) throws Exception;

    /**
     *  6.根据ID查询回执
     * @param id
     * @return
     * @throws Exception
     */
    public AeaHiReceive getAeaHiReceiveById(@Param("id") String id) throws Exception;

    /**
     * 7.分页查询回执
     * @param aeaHiReceive
     * @return
     * @throws Exception
     */
    PageInfo<AeaHiReceive> listAeaHiReceive(AeaHiReceive aeaHiReceive, Page page) throws Exception;
}
