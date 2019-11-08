package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.common.vo.AeaItemMatKpVo;
import com.augurit.aplanmis.common.vo.MatQueryVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ZhangXinhui
 * @date 2019/7/30 030 11:10
 * @desc
 **/
public interface AeaItemMatAdminService {

    void saveAeaItemMatAndParIn(HttpServletRequest request,
                                String stageId,
                                String isStateIn,
                                String stateId,
                                AeaItemMat aeaItemMat) throws Exception;

    void saveAeaItemMatAndParIn(HttpServletRequest request, AeaItemMat aeaItemMat, AeaParIn aeaParIn) throws Exception;


    PageInfo<AeaItemMat> listStageNoSelectGlobalMat(String stageId, String keyword, Page page);

    /**
     * 更新事项材料
     * @param request
     * @param aeaItemMat
     * @throws Exception
     */
    void updateAeaItemMat(HttpServletRequest request, AeaItemMat aeaItemMat) throws Exception;

    /**
     * 新增事项材料
     * @param request
     * @param aeaItemMat
     * @throws Exception
     */
    void saveAeaItemMat(HttpServletRequest request, AeaItemMat aeaItemMat) throws Exception;

    /**
     * 新增事项材料
     * @param aeaItemMat
     * @throws Exception
     */
    void saveAeaItemMat(AeaItemMat aeaItemMat) throws Exception;

    /**
     * 获取最大排序号
     * @return
     */
    Long getMatMaxSortNo();

    /**
     * 更新事项材料
     * @param aeaItemMat
     * @throws Exception
     */
    void updateAeaItemMat(AeaItemMat aeaItemMat) throws Exception;

    /**
     * 删除材料
     * @param id 材料id
     * @throws Exception
     */
    void deleteAeaItemMatById(String id) throws Exception;

    /**
     * 验证材料存在性
     * @param matId 材料id
     * @param matCode 材料编号
     * @param rootOrgId
     * @return
     */
    boolean checkMatCode(String matId, String matCode, String rootOrgId);

    /**
     * 材料清单分页查询
     * @param aeaItemMat
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AeaItemMat> listAeaItemMat(AeaItemMat aeaItemMat, Page page) throws Exception;

    /**
     * 获取材料
     * @param id 材料id
     * @return
     */
    AeaItemMat getAeaItemMatById(String id);

    /**
     * 批量删除材料
     * @param ids 材料id集
     */
    void batchDeleteAeaItemMatByIds(String[] ids);

    /**
     * 删除材料附件
     *
     * @param type
     * @param matId
     * @param detailId
     * @throws Exception
     */
    void delelteFile(Integer type,String matId, String detailId)throws Exception;

    /**
     * 下载附件
     *
     * @param type
     * @param detailId
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    boolean downloadDoc(Integer type, String detailId, HttpServletResponse response, HttpServletRequest request)throws Exception;

    void handleKpItemMat(AeaItemMatKpVo aeaItemMatKpVo, AeaItemMat aeaItemMat)throws Exception;

    /**
     * 获取事项输出未选择的材料
     *
     * @param rootOrgId
     * @param itemId
     * @param stateVerId
     * @param isInput
     * @param keyword
     * @param page
     * @return
     */
    PageInfo<AeaItemMat> listItemInOutNoSelectGlobalMat(String rootOrgId, String itemId,String stateVerId, String isInput, String keyword, Page page);

    PageInfo<AeaItemMat> listStageOrItemNoSelectMatPage(AeaItemMat aeaItemMat, Page page);

    void saveChooseStageMatAndParIn(String ids, String stageId, String isStateIn, String stateId);

    void saveChooseItemMatAndInout(String ids, String itemVerId, String isStateIn,String itemStateId, String stateVerId, String isCommon);
}
