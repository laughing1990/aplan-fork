package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.qo.item.ItemMatInoutQo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/31 031 10:06
 * @desc
 **/
public interface AeaItemInoutAdminService {

    void saveAeaItemInout(AeaItemInout aeaItemInout) ;

    void updateAeaItemInout(AeaItemInout aeaItemInout) ;

    void deleteAeaItemInoutById(String id) ;

    /**
     * 根据材料id获取事项输入输出
     * @param id 材料id
     * @return List<AeaItemInout>
     */
    List<AeaItemInout> getAeaItemInoutByMatId(String id, String rootOrgId);

    void saveItemInout(List<String> matIdList, String stateId, String itemId);

    PageInfo<AeaItemInout> listAeaItemInout(AeaItemInout aeaItemInout, Page page);

    List<AeaItemInout> listAeaItemInout(AeaItemInout aeaItemInout) ;

    /**
     * 分页获取材料、证照
     *
     * @param aeaItemInout
     * @param page
     * @return
     */
    PageInfo<AeaItemInout> listAeaItemInoutMatCert(AeaItemInout aeaItemInout, Page page) ;

    /**
     *
     * 分页获取材料、证照、表单
     *
     * @param aeaItemInout
     * @param page
     * @return
     */
    PageInfo<AeaItemInout> listAeaItemMatCertFormByPage(AeaItemInout aeaItemInout, Page page);

    /**
     * 不分页获取材料、证照
     *
     * @param aeaItemInout
     * @return
     */
    List<AeaItemInout> listAeaItemInoutMatCert(AeaItemInout aeaItemInout) ;

    /**
     *
     * 分页获取材料、证照、表单
     *
     * @param aeaItemInout
     * @return
     */
    List<AeaItemInout> listAeaItemInoutMatCertForm(AeaItemInout aeaItemInout);

    /**
     * 获取排序号
     *
     * @param itemVerId
     * @param itemStateVerId
     * @param isInput
     * @param rootOrgId
     * @return
     */
    Long getMaxSortNo(String itemVerId, String itemStateVerId, String isInput, String rootOrgId);

    /**
     * 保存材料与证照
     *
     * @param inout
     * @param matCertIds
     */
    void batchSaveItemInoutMatCert(AeaItemInout inout,String[] matCertIds);

    /**
     * 保存材料与证照且不删除旧的数据
     *
     * @param inout
     * @param matCertIds
     */
    void batchSaveItemInoutMatCertAndNotDelOld(AeaItemInout inout,String[] matCertIds);

    void deleteAeaItemInoutCascade(String id) ;

    void batchDeleteAeaItemInoutCascade(String[] ids) ;

    void batchDelItemMatCertFormCascade(String[] ids, String[] fileTypes);

    List<AeaItemInout> listMatAndInoutList(ItemMatInoutQo itemMatInoutQo);
}
