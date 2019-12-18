package com.augurit.aplanmis.common.service.applyinstCancel;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCancelLog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 申报撤件实例和申请实例历史状态关联表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * </ul>
 */
public interface AeaHiApplyinstCancelLogService {
    public void saveAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception;

    public void updateAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception;

    public void deleteAeaHiApplyinstCancelLogById(String id) throws Exception;

    public PageInfo<AeaHiApplyinstCancelLog> listAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog, Page page) throws Exception;

    public AeaHiApplyinstCancelLog getAeaHiApplyinstCancelLogById(String id) throws Exception;

    public List<AeaHiApplyinstCancelLog> listAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception;

}
