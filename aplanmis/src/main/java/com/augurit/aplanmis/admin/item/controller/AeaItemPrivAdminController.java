package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemPriv;
import com.augurit.aplanmis.common.service.admin.item.AeaItemAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPrivAdminService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/5 005 14:54
 * @desc
 **/
@RestController
@RequestMapping("/aea/item/priv")
public class AeaItemPrivAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemPrivAdminController.class);

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;

    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private AeaItemAdminService aeaItemAdminService;

    @Autowired
    private AeaItemPrivAdminService aeaItemPrivService;

    /**
     * 获取可用事项列表
     *
     * @param aeaItemBasic
     * @param page
     * @return
     */
    @RequestMapping("/listUsedAeaItemBasicTreeByPage.do")
    public EasyuiPageInfo<AeaItemBasic> listUsedAeaItemBasicTreeByPage(AeaItemBasic aeaItemBasic, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemBasic);
        return aeaItemBasicAdminService.listUsedAeaItemBasicTreeByPage(aeaItemBasic, page);
    }

    @RequestMapping("/getBscDicRegionList.do")
    public List<BscDicRegion> getBscDicRegionList() {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(rootOrgId);
        List<BscDicRegion> list = bscDicRegionMapper.listSelfAndChildRegionsByOrgName(opuOmOrg.getOrgName(), "");

        return list;
    }

    /**
     * 获取可用事项树
     *
     * @param aeaItemBasic
     * @return
     */
    @RequestMapping("/getTreeByAeaItemBasicList.do")
    public List<AeaItem> getTreeByAeaItemBasicList(AeaItemBasic aeaItemBasic) {
        aeaItemBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItem> list = aeaItemAdminService.gtreeTestRunOrPublishedItem(aeaItemBasic);
        return list;
    }

    /**
     * 获取事项下放区域部门,keyword:地区名、机构名
     *
     * @param itemVerId
     * @param keyword
     * @param page
     * @return
     */
    @RequestMapping("/getPrivListByItemId.do")
    public EasyuiPageInfo<AeaItemPriv> getPrivListByItemId(String itemVerId, String keyword, Page page) {

        return aeaItemPrivService.getPrivListByItemId(itemVerId, keyword, page);
    }

    /**
     * 保存或编辑事项下放信息
     *
     * @param aeaItemPriv 事项下放表
     * @return 返回结果对象 包含结果信息
     * @
     */
    @RequestMapping("/saveAeaItemPriv.do")
    public ResultForm saveAeaItemBasic(AeaItemPriv aeaItemPriv) throws Exception {

        if (StringUtils.isNotBlank(aeaItemPriv.getPrivId())) {
            return new ContentResultForm<>(false, aeaItemPriv);
        } else {
            if((StringUtils.isNotBlank(aeaItemPriv.getItemVerId())||(aeaItemPriv.getItemVerIds()!=null&&aeaItemPriv.getItemVerIds().size()>0))
                &&StringUtils.isNotBlank(aeaItemPriv.getRegionId())&&StringUtils.isNotBlank(aeaItemPriv.getOrgId())){

                aeaItemPrivService.saveAeaItemPriv(aeaItemPriv);
            }
        }
        return new ContentResultForm<>(true, aeaItemPriv);
    }

    @RequestMapping("/deleteAeaItemPrivById.do")
    public ResultForm deleteAeaItemPrivById(String privId) throws Exception{

        if (StringUtils.isBlank(privId)) {
            return new ContentResultForm<>(false, privId,"请选择删除项");
        } else {
            aeaItemPrivService.deleteAeaItemPrivById(privId);
            return new ContentResultForm<>(true, privId);
        }
    }


    @RequestMapping("/deleteAeaItemPrivByIds.do")
    public ResultForm deleteAeaItemPrivByIds(String privIds) throws Exception{

        if (StringUtils.isBlank(privIds)) {
            return new ContentResultForm<>(false, privIds,"请选择删除项");
        } else {
            String[] privIdList = privIds.split(",");
            for(String privId : privIdList){
                aeaItemPrivService.deleteAeaItemPrivById(privId);
            }
            return new ContentResultForm<>(true, privIds);
        }
    }
}
