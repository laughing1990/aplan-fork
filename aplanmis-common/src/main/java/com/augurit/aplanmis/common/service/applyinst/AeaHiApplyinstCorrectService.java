package com.augurit.aplanmis.common.service.applyinst;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrect;
import com.augurit.aplanmis.common.dto.ApplyinstCorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 材料补全实例表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-08-28 17:33:44</li>
 * </ul>
 */
public interface AeaHiApplyinstCorrectService {
    void saveAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception;

    void updateAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception;

    void deleteAeaHiApplyinstCorrectById(String id) throws Exception;

    PageInfo<AeaHiApplyinstCorrect> listAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect, Page page) throws Exception;

    AeaHiApplyinstCorrect getAeaHiApplyinstCorrectById(String id) throws Exception;

    List<AeaHiApplyinstCorrect> listAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception;

    AeaHiApplyinstCorrect getCurrentCorrectinst(String applyinstIds) throws Exception;

    //创建材料补全实例
    String createMatCorrectinst(ApplyinstCorrectinstDto correctinstDto) throws Exception;

    //获取材料补全实例信息
    ResultForm getMatCorrectInfo(String applyinstCorrectId) throws Exception;

    //查询少交和已交的材料清单
    MatCorrectInfoDto getLackMatsByApplyinstId(String applyinstId) throws Exception;

    //保存材料补全实例
    ResultForm saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) throws Exception;

    //获取待确认的补全材料列表
    AeaHiApplyinstCorrect getApplyinstCorrectRealIninst(String applyinstCorrectId) throws Exception;

    //确认补全材料列表
    void matCorrectConfirm(String applyinstCorrectId, String correctState, String correctMemo, String matCorrectDtosJson) throws Exception;

    //上传电子件
    void uploadFile(String attRealIninstId, HttpServletRequest request) throws Exception;

    //删除电子件
    void delelteAttFile(String detailIds, String attRealIninstId) throws Exception;

    //查询已上传附件列表
    public List<BscAttFileAndDir> getAttFiles(String attRealIninstId) throws Exception;

    //材料补全结束并已确认
    public ResultForm saveMatCorrectInfoAndConfirm(MatCorrectinstDto matCorrectinstDto) throws Exception;

}
