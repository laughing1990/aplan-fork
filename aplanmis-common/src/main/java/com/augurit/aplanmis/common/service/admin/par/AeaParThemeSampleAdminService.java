package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParThemeSample;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;


/**
 * @author ZhangXinhui
 * @date 2019/9/11 011 14:34
 * @desc
 **/
public interface AeaParThemeSampleAdminService {

    /**
     * 分页查询主题样本
     * @param aeaParThemeSample
     * @param page
     * @return
     */
    PageInfo<AeaParThemeSample> listAeaParThemeSamplePage(AeaParThemeSample aeaParThemeSample, Page page);

    /**
     * 新增主题样本
     * @param aeaParThemeSample
     */
    void saveThemeSample(AeaParThemeSample aeaParThemeSample);

    /**
     * 更新主题样本
     * @param aeaParThemeSample
     */
    void updateThemeSample(AeaParThemeSample aeaParThemeSample);

    /**
     * 获取单个主题样本
     * @param id
     * @return
     */
    AeaParThemeSample getAeaParThemeSampleById(String id);

    /**
     * 删除主题样本
     * @param id
     */
    void deleteAeaParThemeSampleById(String id);

    /**
     * 批量删除主题样本
     * @param ids
     */
    void batchDelThemeSampleByIds(String[] ids);
}
