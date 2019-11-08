package com.augurit.aplanmis.common.service.item;


import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AeaItemCondService {

    /**
     *  1.根据事项VerId查询前置条件列表
     * @param itemVerId  必须参数     事项版本ID
     * @param isQuestion 非必须参数   是否是问题  1 问题  0 答案
     * @return
     * @throws Exception
     */
    List<AeaItemCond> getAeaItemCondListByItemVerId(String itemVerId,String isQuestion) throws Exception;

    /**
     * 2.根据父条件ID查询子前置条件列表
     * @param condId  必须参数   前置条件ID
     * @return
     * @throws Exception
     */
    List<AeaItemCond> getChildAeaItemCondListByCondId(String condId) throws Exception;

    /**
     *  3.根据前置条件判断该项目是否能启动
     * @param itemVerId 必须参数     事项版本ID
     * @param projInfoId 必须参数   工程ID
     * @return
     * @throws Exception
     */
    boolean judgeElExpression(String itemVerId, String projInfoId) throws Exception;

    /**
     *  4.新增/编辑前置条件
     * @param aeaItemCond
     * @throws Exception
     */
    public void saveAeaItemCond(AeaItemCond aeaItemCond) throws Exception;

    /**
     *  5.根据ID删除前置条件
     * @param id
     * @throws Exception
     */
    public void deleteAeaItemCond(@Param("id") String id) throws Exception;

    /**
     *  6.查询前置条件列表
     * @param aeaItemCond
     * @return
     * @throws Exception
     */
    public List<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond) throws Exception;

    /**
     * 7.根据ID查询前置条件
     * @param id
     * @return
     * @throws Exception
     */
    public AeaItemCond getAeaItemCondById(@Param("id") String id) throws Exception;

    /**
     * 8.分页查询前置条件列表
     * @param aeaItemCond
     * @param page
     * @return
     * @throws Exception
     */
    public PageInfo<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond, Page page) throws Exception;

    /**
     *  9.根据事项VerId查询前置条件【树状】列表
     * @param itemVerId  必须参数     事项版本ID
     * @return
     * @throws Exception
     */
    List<AeaItemCond> getAeaItemCondTreeListByItemVerId(String itemVerId) throws Exception;

    /**
     *  10.根据事项VerId查询【顶级】前置条件列表
     * @param itemVerId  必须参数     事项版本ID
     * @return
     * @throws Exception
     */
    List<AeaItemCond> getAeaItemCondTopListByItemVerId(String itemVerId) throws Exception;
}
