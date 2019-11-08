package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 事项输入输出实例表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:26:15</li>
 * </ul>
 */
public interface AeaHiItemCorrectService {
    public void saveAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception;

    public void updateAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception;

    public void deleteAeaHiItemCorrectById(String id) throws Exception;

    public PageInfo<AeaHiItemCorrect> listAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect, Page page) throws Exception;

    public AeaHiItemCorrect getAeaHiItemCorrectById(String id) throws Exception;

    public List<AeaHiItemCorrect> listAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception;

    public AeaHiItemCorrect getCurrentCorrectinst(String iteminstId) throws Exception;
}
