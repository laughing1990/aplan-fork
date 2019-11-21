package com.augurit.aplanmis.common.service.instance;


import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.vo.MyMatFilesVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 事项材料实例表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:chenzh</li>
 * <li>创建时间：2019-07-08 16:31:14</li>
 * </ul>
 */
public interface AeaHiItemMatinstService {

    /**
     * 根据事项实例ID获取（输入或输出）材料实例列表
     *
     * @param iteminstId
     * @param isInput（当  isInput=1时，表示输入材料，isInput=0时，表示输出材料）
     * @return
     * @throws Exception
     */
    List<AeaHiItemMatinst> getMatinstListByIteminstIds(String[] iteminstId, String isInput) throws Exception;

    /**
     * 根据事项实例ID以及搜索关键字获取（输入或输出）材料实例列表
     *
     * @param iteminstId
     * @param isInput（当  isInput=1时，表示输入材料，isInput=0时，表示输出材料）
     * @return
     * @throws Exception
     */
    List<MyMatFilesVo> getMatinstListByIteminstIdsAndKeyword(String[] iteminstId, String isInput, String keyword,String isAtt) throws Exception;


    /**
     * 获取并联事项的输入材料实例
     *
     * @param iteminstId
     * @return
     * @throws Exception
     */
    List<AeaHiItemMatinst> getMatinstListByStageIteminstId(String iteminstId) throws Exception;


    /**
     * 根据阶段实例ID获取（输入或输出）材料实例列表
     *
     * @param stageinstId
     * @param isInput     （当 isInput=1时，表示输入材料，isInput=0时，表示输出材料）
     * @return
     * @throws Exception
     */
    List<AeaHiItemMatinst> getMatinstListByStageinstId(String stageinstId, String isInput) throws Exception;

    /**
     * 根据材料实例ID获取材料实例信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    AeaHiItemMatinst getAeaHiItemMatinstById(String id) throws Exception;

    /**
     * 根据项目ID和材料ID获取材料实例列表
     *
     * @param projInfoid
     * @param matId
     * @return
     * @throws Exception
     */
    List<AeaHiItemMatinst> getMatinstListByProjInfoIdAndMatIds(String projInfoid, String[] matId) throws Exception;

    /**
     * 根据材料实例ID删除材料实例
     *
     * @param id
     * @throws Exception
     */
    void deleteAeaHiItemMatinstById(String id) throws Exception;

    /**
     * 根据多个材料实例ID批量删除材料实例列表
     *
     * @param id
     * @throws Exception
     */
    void deleteAeaHiItemMatinstByIds(String[] id) throws Exception;

    /**
     * 新增或修改材料实例信息
     *
     * @param aeaHiItemMatinst
     * @param request          存放 file 文件流
     * @throws Exception
     */
    String saveAeaHiItemMatinst(AeaHiItemMatinst aeaHiItemMatinst, HttpServletRequest request) throws Exception;

    /**
     * 从云盘上传申请材料
     *
     * @param aeaHiItemMatinst
     * @return
     * @throws Exception
     */
    String saveAeaHiItemMatinstByCloud(AeaHiItemMatinst aeaHiItemMatinst) throws Exception;


    /**
     * 批量新增或修改材料实例信息
     *
     * @param aeaHiItemMatinst
     * @throws Exception
     */
    void batchSaveAeaHiItemMatinst(List<AeaHiItemMatinst> aeaHiItemMatinst) throws Exception;

    boolean matinstbeLong2MatId(String matinstId, String matId) throws Exception;

    /**
     * 关联电子证照库中的材料
     *
     * @param aeaHiCertinst 前端回传绑定的信息
     * @param currentUserId
     * @return 证照材料
     */
    AeaHiCertinst bindCertinst(AeaHiCertinst aeaHiCertinst, String currentUserId) throws Exception;

    /**
     * 根据 certinstCode 返回以前绑定过的电子证照材料实例
     *
     * @param authCode
     * @param currentOrgId
     * @return
     */
    List<AeaHiCertinst> getHistoryCertinst(String authCode, String currentOrgId);

    void unbindCertinst(String matinstId) throws Exception;

    void setAttCount(AeaHiItemMatinst aeaHiItemMatinst, HttpServletRequest request) throws Exception;

    AeaHiItemMatinst bindForminst(AeaHiItemMatinst aeaHiItemMatinst, String currentUserId) throws Exception;
}
