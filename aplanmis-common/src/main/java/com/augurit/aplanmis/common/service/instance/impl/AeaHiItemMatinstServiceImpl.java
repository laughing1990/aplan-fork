package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.mapper.AeaHiCertinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@Slf4j
public class AeaHiItemMatinstServiceImpl implements AeaHiItemMatinstService {

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    BscAttMapper bscAttMapper;

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;

    //根据事项实例ID获取（输入或输出）材料实例列表
    public List<AeaHiItemMatinst> getMatinstListByIteminstIds(String[] iteminstIds, String isInput) throws Exception {
        if (iteminstIds.length < 1) throw new Exception("事项实例ID数组长度为0");
        if (StringUtils.isBlank(isInput)) throw new Exception("isInput不能为空");
        return aeaHiItemMatinstMapper.getMatinstListByIteminstIds(iteminstIds, isInput);
    }

    public List<AeaHiItemMatinst> getMatinstListByIteminstIdsAndKeyword(String[] iteminstIds, String isInput,String keyword) throws Exception {
        if (iteminstIds.length < 1) throw new Exception("事项实例ID数组长度为0");
        if (StringUtils.isBlank(isInput)) throw new Exception("isInput不能为空");
        return aeaHiItemMatinstMapper.getMatinstListByIteminstIdsAndKeyword(iteminstIds, isInput,keyword);
    }


    //根据阶段实例ID获取（输入或输出）材料实例列表
    public List<AeaHiItemMatinst> getMatinstListByStageinstId(String stageinstId, String isInput) throws Exception {
        if (StringUtils.isBlank(stageinstId)) throw new Exception("阶段实例ID不能为空");
        if (StringUtils.isBlank(isInput)) throw new Exception("isInput不能为空");
        if ("1".equals(isInput)) {
            return aeaHiItemMatinstMapper.getMatinstListByStageinstId(stageinstId, SecurityContext.getCurrentOrgId());
        } else {
            return aeaHiItemMatinstMapper.getOutputMatinstListByStageinstId(stageinstId);
        }
    }

    @Override
    public List<AeaHiItemMatinst> getMatinstListByStageIteminstId(String iteminstId) throws Exception {
        if (StringUtils.isBlank(iteminstId)) throw new Exception("事项实例ID不能为空！");
        return aeaHiItemMatinstMapper.getMatinstListByStageIteminstId(iteminstId);
    }

    //根据材料实例ID获取材料实例信息
    public AeaHiItemMatinst getAeaHiItemMatinstById(String id) throws Exception {
        if (StringUtils.isBlank(id)) throw new Exception("Id不能为null");
        return aeaHiItemMatinstMapper.getAeaHiItemMatinstById(id);
    }

    //根据项目ID和材料ID获取材料实例列表
    public List<AeaHiItemMatinst> getMatinstListByProjInfoIdAndMatIds(String projInfoid, String[] matIds) throws Exception {
        if (StringUtils.isBlank(projInfoid)) throw new Exception("projInfoid不能为空");
        if (matIds.length < 1) throw new Exception("matIds数组长度为0");
        return aeaHiItemMatinstMapper.getMatinstListByProjInfoIdAndMatIds(projInfoid, matIds);
    }

    //根据材料实例ID删除材料实例
    @Override
    public void deleteAeaHiItemMatinstById(String id) throws Exception {
        if (StringUtils.isBlank(id)) throw new Exception("Id不能为null");
        aeaHiItemMatinstMapper.deleteAeaHiItemMatinst(id);
    }


    //根据多个材料实例ID批量删除材料实例列表
    @Override
    public void deleteAeaHiItemMatinstByIds(String[] ids) throws Exception {
        if (ids.length < 1) throw new Exception("Id数组长度为0");
        aeaHiItemMatinstMapper.deleteAeaHiItemMatinsts(ids);
    }

    //新增或修改材料实例信息
    @Override
    public String saveAeaHiItemMatinst(AeaHiItemMatinst aeaHiItemMatinst, HttpServletRequest request) throws Exception {
        if (aeaHiItemMatinst == null) throw new Exception("材料实例对象为空");
        String matinstId = aeaHiItemMatinst.getMatinstId();
        if (StringUtils.isBlank(matinstId)) {

            Assert.isTrue(StringUtils.isNotBlank(aeaHiItemMatinst.getMatId()), "matId is null");
            Assert.isTrue(StringUtils.isNotBlank(aeaHiItemMatinst.getMatinstCode()), "matinstCode is null");
            Assert.isTrue(StringUtils.isNotBlank(aeaHiItemMatinst.getMatinstName()), "matinstName is null");

            // todo 根据 aeaItemMat 的matHolder 字段设置对应值
            /*if (StringUtils.isNotBlank(aeaHiItemMatinst.getUnitInfoId())) {
                aeaHiItemMatinst.setMat
            }*/

            matinstId = UUID.randomUUID().toString();
            aeaHiItemMatinst.setMatinstId(matinstId);
            aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserName());
            aeaHiItemMatinst.setCreateTime(new Date());
            aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            this.setAttCount(aeaHiItemMatinst, request);
            aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
        } else {
            //材料补全时如果先删除材料在上传文件，此时matinstId是存在的，但是aea_hi_item_matinst表中的数据已删除，会导致空指针异常
            // 判断是否存在 aea_hi_item_inoutinst ||aea_hi_item_matinst
            List<AeaHiItemInoutinst> aeaHiItemInoutinst = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstByMatinstId(matinstId);
            AeaHiItemMatinst itemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
            if (null == itemMatinst && aeaHiItemInoutinst.size() == 0) {
                itemMatinst = new AeaHiItemMatinst();
                itemMatinst.setMatinstId(matinstId);
                itemMatinst.setMatId(aeaHiItemMatinst.getMatId());
                itemMatinst.setMatinstCode(aeaHiItemMatinst.getMatinstCode());
                itemMatinst.setMatinstName(aeaHiItemMatinst.getMatinstName());
                itemMatinst.setCreater(SecurityContext.getCurrentUserName());
                itemMatinst.setCreateTime(new Date());
                itemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiItemMatinstMapper.insertAeaHiItemMatinst(itemMatinst);
            }
            this.setAttCount(aeaHiItemMatinst, request);
            aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiItemMatinst.setModifyTime(new Date());
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
        }
        return matinstId;
    }

    @Override
    public String saveAeaHiItemMatinstByCloud(AeaHiItemMatinst aeaHiItemMatinst) throws Exception {
        {
            if (aeaHiItemMatinst == null) throw new Exception("材料实例对象为空");
            String matinstId = aeaHiItemMatinst.getMatinstId();
            String[] detailIdArr = aeaHiItemMatinst.getDetailIds().split(",");
            for (int i = 0; i < detailIdArr.length; i++) {
                aeaHiItemMatinst.setDetailId(detailIdArr[i]);
                if (StringUtils.isBlank(matinstId)) {

                    Assert.isTrue(StringUtils.isNotBlank(aeaHiItemMatinst.getMatId()), "matId is null");
                    Assert.isTrue(StringUtils.isNotBlank(aeaHiItemMatinst.getMatinstCode()), "matinstCode is null");
                    Assert.isTrue(StringUtils.isNotBlank(aeaHiItemMatinst.getMatinstName()), "matinstName is null");
                    Assert.isTrue(StringUtils.isNotBlank(aeaHiItemMatinst.getDetailId()), "detailId is null");

                    matinstId = UUID.randomUUID().toString();
                    aeaHiItemMatinst.setMatinstId(matinstId);
                    aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserName());
                    aeaHiItemMatinst.setCreateTime(new Date());
                    aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                    this.setAttCountByCloud(aeaHiItemMatinst);
                    aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
                } else {
                    //材料补全时如果先删除材料在上传文件，此时matinstId是存在的，但是aea_hi_item_matinst表中的数据已删除，会导致空指针异常
                    // 判断是否存在 aea_hi_item_inoutinst ||aea_hi_item_matinst
                    List<AeaHiItemInoutinst> aeaHiItemInoutinst = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstByMatinstId(matinstId);
                    AeaHiItemMatinst itemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
                    if (null == itemMatinst && aeaHiItemInoutinst.size() == 0) {
                        itemMatinst = new AeaHiItemMatinst();
                        itemMatinst.setMatinstId(matinstId);
                        itemMatinst.setMatId(aeaHiItemMatinst.getMatId());
                        itemMatinst.setMatinstCode(aeaHiItemMatinst.getMatinstCode());
                        itemMatinst.setMatinstName(aeaHiItemMatinst.getMatinstName());
                        itemMatinst.setCreater(SecurityContext.getCurrentUserName());
                        itemMatinst.setCreateTime(new Date());
                        itemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiItemMatinstMapper.insertAeaHiItemMatinst(itemMatinst);
                    }
                    this.setAttCountByCloud(aeaHiItemMatinst);
                    aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserName());
                    aeaHiItemMatinst.setModifyTime(new Date());
                    aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
                }
            }

            return matinstId;
        }
    }

    private void setAttCount(AeaHiItemMatinst aeaHiItemMatinst, HttpServletRequest request) throws Exception {
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            fileUtilsService.uploadAttachments("AEA_HI_ITEM_MATINST", "MATINST_ID", aeaHiItemMatinst.getMatinstId(), files);
            aeaHiItemMatinst.setAttCount(Long.valueOf(fileUtilsService.getAttachmentsByRecordId(new String[]{aeaHiItemMatinst.getMatinstId()}, "AEA_HI_ITEM_MATINST", "MATINST_ID").size()));
        }
    }

    private void setAttCountByCloud(AeaHiItemMatinst aeaHiItemMatinst) throws Exception {
        BscAttLink  bscAttLink = new BscAttLink();
        bscAttLink.setLinkId(UUID.randomUUID().toString());
        bscAttLink.setTableName("AEA_HI_ITEM_MATINST");
        bscAttLink.setPkName("MATINST_ID");
        bscAttLink.setRecordId(aeaHiItemMatinst.getMatinstId());
        bscAttLink.setDetailId(aeaHiItemMatinst.getDetailId());
        bscAttLink.setDirId(aeaHiItemMatinst.getDirId());
        bscAttLink.setLinkType("a");
        bscAttMapper.insertLink(bscAttLink);
        aeaHiItemMatinst.setAttCount(Long.valueOf(fileUtilsService.getAttachmentsByRecordId(new String[]{aeaHiItemMatinst.getMatinstId()}, "AEA_HI_ITEM_MATINST", "MATINST_ID").size()));
    }


    //批量新增或修改材料实例信息
    @Override
    public void batchSaveAeaHiItemMatinst(List<AeaHiItemMatinst> aeaHiItemMatinst) throws Exception {
        if (aeaHiItemMatinst.size() < 1) throw new Exception("对象数组长度为0");
        List<AeaHiItemMatinst> updateList = new ArrayList();
        List<AeaHiItemMatinst> insertList = new ArrayList();

        for (AeaHiItemMatinst matinst : aeaHiItemMatinst) {
            if (StringUtils.isBlank(matinst.getMatinstId())) {
                matinst.setMatinstId(UUID.randomUUID().toString());
                matinst.setCreater(SecurityContext.getCurrentUserName());
                matinst.setCreateTime(new Date());
                matinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                insertList.add(matinst);
            } else {
                matinst.setModifier(SecurityContext.getCurrentUserName());
                matinst.setModifyTime(new Date());
                updateList.add(matinst);
            }
        }

        if (updateList.size() > 0)
            aeaHiItemMatinstMapper.batchUpdateAeaHiItemMatinst(updateList);
        if (insertList.size() > 0)
            aeaHiItemMatinstMapper.batchInsertAeaHiItemMatinst(insertList);
    }

    @Override
    public boolean matinstbeLong2MatId(String matinstId, String matId) throws Exception {
        Integer count = aeaHiItemMatinstMapper.matinstbeLong2MatId(matinstId, matId, SecurityContext.getCurrentOrgId());
        return (count != null && count > 0) ? true : false;
    }

    @Override
    public AeaHiItemMatinst bindCertinst(AeaHiCertinst aeaHiCertinst, String currentUserName) throws Exception {
        Assert.hasText(aeaHiCertinst.getMatId(), "matId is null");
        Assert.hasText(aeaHiCertinst.getAuthCode(), "authCode is null");

        String currentOrgId = SecurityContext.getCurrentOrgId();

        AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(aeaHiCertinst.getMatId());
        Assert.state("c".equals(aeaItemMat.getMatProp()), "matProp should 'c'");

        aeaHiCertinst.setCertinstId(UuidUtil.generateUuid());
        aeaHiCertinst.setRootOrgId(currentOrgId);
        aeaHiCertinst.setCreater(currentUserName);

        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        BeanUtils.copyProperties(aeaItemMat, aeaHiItemMatinst);
        aeaHiItemMatinst.setRootOrgId(currentOrgId);
        aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
        aeaHiItemMatinst.setCreateTime(new Date());
        aeaHiItemMatinst.setCreater(currentUserName);
        aeaHiItemMatinst.setCertinstId(aeaHiCertinst.getCertinstId());

        aeaHiCertinstMapper.insertAeaHiCertinst(aeaHiCertinst);
        aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);

        return aeaHiItemMatinst;
    }

    @Override
    public void unbindCertinst(String matinstId) throws Exception {
        Assert.hasText(matinstId, "证照材料实例id不能为空");
        AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);

        // 删除电子证照
        if (StringUtils.isNotBlank(aeaHiItemMatinst.getCertinstId())) {
            aeaHiCertinstMapper.deleteAeaHiCertinst(aeaHiItemMatinst.getCertinstId());
        }
        aeaHiItemMatinstMapper.deleteAeaHiItemMatinst(matinstId);
    }
}
