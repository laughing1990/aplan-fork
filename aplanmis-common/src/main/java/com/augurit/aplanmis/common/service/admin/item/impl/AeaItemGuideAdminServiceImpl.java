package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemGuide;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemGuideMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AeaItemGuideAdminServiceImpl implements AeaItemGuideAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideAdminServiceImpl.class);

    @Autowired
    private AeaItemMapper aeaItemMapper;

    @Autowired
    private AeaItemGuideMapper aeaItemGuideMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private FileUtilsService fileUtilsService;


    @Override
    public void saveAeaItemGuide(AeaItemGuide aeaItemGuide) {

        aeaItemGuide.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideMapper.insertAeaItemGuide(aeaItemGuide);
    }

    @Override
    public void updateAeaItemGuide(AeaItemGuide aeaItemGuide) {

        aeaItemGuideMapper.updateAeaItemGuide(aeaItemGuide);
    }

    @Override
    public void deleteAeaItemGuideById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideMapper.deleteAeaItemGuide(id);
    }

    @Override
    public void deleteAeaItemGuideByItemVerId(String itemVerId, String rootOrgId) {

        if (StringUtils.isNotBlank(itemVerId)) {
            aeaItemGuideMapper.deleteAeaItemGuideByItemVerId(itemVerId, rootOrgId);
        }
    }

    @Override
    public PageInfo<AeaItemGuide> listAeaItemGuide(AeaItemGuide aeaItemGuide, Page page) {

        aeaItemGuide.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuide> list = aeaItemGuideMapper.listAeaItemGuide(aeaItemGuide);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuide getAeaItemGuideById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideMapper.getAeaItemGuideById(id);
    }

    @Override
    public List<AeaItemGuide> listAeaItemGuide(AeaItemGuide aeaItemGuide) {

        aeaItemGuide.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuide> list = aeaItemGuideMapper.listAeaItemGuide(aeaItemGuide);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void syncLocalAeaItemGuide() {

        // 获取最新版本事项
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItem sitem = new AeaItem();
        sitem.setRootOrgId(rootOrgId);
        List<AeaItem> itemList = aeaItemMapper.listAeaItem(sitem);
        if (itemList != null && itemList.size() > 0) {
            Set<String> itemIdSet = new HashSet<String>();
            for (AeaItem item : itemList) {
                itemIdSet.add(item.getItemId());
            }
            String[] itemIds = itemIdSet.toArray(new String[]{});
            List<AeaItemBasic> itemBasicList = aeaItemBasicMapper.listAeaItemBasicByItemIds(itemIds, rootOrgId);
            if (itemBasicList != null && itemBasicList.size() > 0) {
                copyItemBasicToGuide(itemBasicList);
            }
        }
    }

    /**
     * 处理事项基本信息复制到操作指南表
     *
     * @param itemBasicList
     */
    private void copyItemBasicToGuide(List<AeaItemBasic> itemBasicList) {

        String topOrgId = SecurityContext.getCurrentOrgId();

        //事项类型
        List<BscDicCodeItem> itemTypes = bscDicCodeService.getActiveItemsByTypeCode("DEPT_ITEM_TYPE", topOrgId);

        //时限单位
        List<BscDicCodeItem> dueUnits = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", topOrgId);

        //办件类型
        List<BscDicCodeItem> itemPropertys = bscDicCodeService.getActiveItemsByTypeCode("ITEM_PROPERTY", topOrgId);

        // 受理方式
        List<BscDicCodeItem> slfss = bscDicCodeService.getActiveItemsByTypeCode("ITEM_SLFS", topOrgId);

        for (AeaItemBasic aeaItemBasic : itemBasicList) {

            Boolean isNew = false;
            AeaItemGuide aeaItemGuide = aeaItemGuideMapper.getOneByItemVerId(aeaItemBasic.getItemVerId(), topOrgId);

            // 不存在创建
            if (aeaItemGuide == null) {
                isNew = true;
                aeaItemGuide = new AeaItemGuide();
                aeaItemGuide.setId(UuidUtil.generateUuid());
                aeaItemGuide.setItemVerId(aeaItemBasic.getItemVerId());
                aeaItemGuide.setRootOrgId(topOrgId);
            }

            //事项名称
            aeaItemGuide.setTaskName(aeaItemBasic.getItemName());

            //实施编码
            aeaItemGuide.setTaskCode(aeaItemBasic.getItemCode());

            //基本编码
            aeaItemGuide.setCatlogCode(aeaItemBasic.getBasecode());

            //事项类型
            aeaItemGuide.setTaskType(aeaItemBasic.getItemType());
            aeaItemGuide.setTaskTypeText(getBscDicCodeItemItemName(aeaItemBasic.getItemType(), itemTypes));

            //办件类型
            aeaItemGuide.setProjectType(aeaItemBasic.getItemProperty());
            aeaItemGuide.setProjectTypeTexy(getBscDicCodeItemItemName(aeaItemBasic.getItemProperty(), itemPropertys));

            aeaItemGuide.setHandleType(aeaItemBasic.getSlfs());
            aeaItemGuide.setHandleTypeText(getBscDicCodeItemItemName(aeaItemBasic.getSlfs(), slfss));

            //时限
            aeaItemGuide.setPromiseDay(aeaItemBasic.getDueNum());
            aeaItemGuide.setPromiseType(aeaItemBasic.getBjType());
            aeaItemGuide.setPromiseTypeText(getBscDicCodeItemItemName(aeaItemBasic.getBjType(), dueUnits));

            //实施主体
            aeaItemGuide.setDeptName(aeaItemBasic.getOrgName());

            //网厅部门编码  aeaItemBasic.getWtbm()
            aeaItemGuide.setDeptCode(aeaItemBasic.getOrgCode());

            //是否收费
            String isFee = aeaItemBasic.getIsFee();
            aeaItemGuide.setIsFee(isFee);
            aeaItemGuide.setIsFeeText(StringUtils.isBlank(isFee) ? isFee : ("0".equals(isFee) ? "不收费" : "收费"));

            if (isNew) {
                aeaItemGuideMapper.insertAeaItemGuide(aeaItemGuide);
            } else {
                aeaItemGuideMapper.updateAeaItemGuide(aeaItemGuide);
            }
        }
    }

    private String getBscDicCodeItemItemName(String itemCode, List<BscDicCodeItem> bscDicCodeItemList) {

        if (bscDicCodeItemList != null && StringUtils.isNotBlank(itemCode)) {
            for (BscDicCodeItem bscDicCodeItem : bscDicCodeItemList) {
                if (itemCode.equals(bscDicCodeItem.getItemCode())) {
                    return bscDicCodeItem.getItemName();
                }
            }
        }
        return null;
    }


    @Override
    public void saveSampleFile(AeaItemGuide aeaItemGuide, HttpServletRequest request) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        aeaItemGuide.setWsbllct(getSampleFile(req, "wsbllctSample", "AEA_ITEM_GUIDE", "WSBLLCT", aeaItemGuide.getId()));
        aeaItemGuide.setCkbllct(getSampleFile(req, "ckbllctSample", "AEA_ITEM_GUIDE", "CKBLLCT", aeaItemGuide.getId()));
        updateAeaItemGuide(aeaItemGuide);
    }

    @Override
    public void delItemGuideAtt(String itemGuideId, String type, String detailId) throws Exception {

        AeaItemGuide itemGuide = aeaItemGuideMapper.getAeaItemGuideById(itemGuideId);
        if (itemGuide != null) {
            // 网上办理
            if (type.equals("WSBLLCT")) {
                if (StringUtils.isNotBlank(itemGuide.getWsbllct())) {
                    String replaceStr = itemGuide.getWsbllct().replaceAll(detailId + ",", "");
                    itemGuide.setWsbllct(replaceStr);
                }
                // 窗口办理
            } else if (type.equals("CKBLLCT")) {
                if (StringUtils.isNotBlank(itemGuide.getCkbllct())) {
                    String replaceStr = itemGuide.getCkbllct().replaceAll(detailId + ",", "");
                    itemGuide.setCkbllct(replaceStr);
                }
            }
            updateAeaItemGuide(itemGuide);
        }
        fileUtilsService.deleteAttachment(detailId);
    }

    private String getSampleFile(StandardMultipartHttpServletRequest request, String fileName, String tableName, String pkName, String recordId) throws Exception {

        List<MultipartFile> files = request.getFiles(fileName);
        if (files.size() > 0) {
            StringBuffer ids = new StringBuffer();
            for (MultipartFile file : files) {
                if (file != null && file.getSize() > 0 && StringUtils.isNotBlank(file.getOriginalFilename())) {
                    BscAttForm form = fileUtilsService.upload(tableName, pkName, recordId, null, file);
                    if (null != form) {
                        ids.append(form.getDetailId()).append(",");
                    }
                }
            }
            return ids.toString();
        }
        return null;
    }
}

