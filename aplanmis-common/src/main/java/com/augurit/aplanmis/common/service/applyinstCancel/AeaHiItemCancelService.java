package com.augurit.aplanmis.common.service.applyinstCancel;

import com.augurit.aplanmis.common.domain.AeaHiItemCancel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 办件撤件实例表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * </ul>
 */
public interface AeaHiItemCancelService {
    public void saveAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception;

    public void updateAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception;

    public void deleteAeaHiItemCancelById(String id) throws Exception;

    public PageInfo<AeaHiItemCancel> listAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel, Page page) throws Exception;

    public AeaHiItemCancel getAeaHiItemCancelById(String id) throws Exception;

    public List<AeaHiItemCancel> listAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception;

}
