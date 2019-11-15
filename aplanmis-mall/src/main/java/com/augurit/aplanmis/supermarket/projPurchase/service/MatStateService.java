package com.augurit.aplanmis.supermarket.projPurchase.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.constants.CommonConstant;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaHiCertinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.supermarket.projPurchase.vo.mat.ItemMatVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.mat.Mat2MatInstVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.mat.SaveMatinstVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class MatStateService {
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaCertMapper aeaCertMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    public List<ItemMatVo> getApplyMatList(String itemVerId) throws Exception {
        String[] itemVerIds = {itemVerId};
        List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(itemVerIds, "1", null);

        List<ItemMatVo> list = new ArrayList<>();
        for (AeaItemMat mat : matList) {
            ItemMatVo vo = new ItemMatVo();
            BeanUtils.copyProperties(mat, vo);
            list.add(vo);
        }
        return list;
    }

    /**
     * 删除材料实例及附件
     *
     * @param matinstId
     */
    public void deleteMatinst(String matinstId) {
        String[] matinstIds = matinstId.split(CommonConstant.COMMA_SEPARATOR);
        for (String id : matinstIds) {
            try {
                aeaHiItemMatinstService.deleteAeaHiItemMatinstById(id);
                List<BscAttFileAndDir> matAttDetailByMatinstId = fileUtilsService.getMatAttDetailByMatinstId(id);
                if (matAttDetailByMatinstId.size() > 0) {
                    String[] recordIds = matAttDetailByMatinstId.stream().map(BscAttFileAndDir::getFileId).toArray(String[]::new);

                    //判断该附件是否存在共享，如果存在共享，则删除link表关联关系，否则返回不存在共享的附件ID
                    List<String> detailIds_ = this.getOnlyOneRecord(recordIds, matinstId);
                    fileUtilsService.deleteAttachments(detailIds_.toArray(new String[detailIds_.size()]));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Delete aea_item_matinst by matinstId failed, matinstId: {}", id);
            }
        }
    }

    //申报页面根据材料定义生成材料实例id
    public List<Mat2MatInstVo> saveMatinsts(SaveMatinstVo saveMatinstVo) {
        Assert.isTrue(saveMatinstVo.getMatCountVos().size() > 0, "matCountVos is empty");

        // matId与材料份数的对应关系
        Map<String, SaveMatinstVo.MatCountVo> matCountMap = saveMatinstVo.buildMatCountMap();

        String projectInfoId = saveMatinstVo.getProjInfoId();
        String unitInfoId = saveMatinstVo.getUnitInfoId();
        List<AeaHiItemMatinst> matinsts = new ArrayList<>();
        List<AeaHiCertinst> certinsts = new ArrayList<>();
        List<Mat2MatInstVo> mat2MatInstVos = new ArrayList<>();
        final String currentOrgId = SecurityContext.getCurrentOrgId();
        aeaItemMatMapper.listAeaItemMatByIds(matCountMap.keySet().toArray(new String[0]))
                .forEach(mat -> {
                    if ("f".equals(mat.getMatProp())) {
                        // todo
                        /*AeaHiItemMatinst aeaHiItemMatinst = mat2Matinst(mat, unitInfoId, projectInfoId, currentOrgId);
                        matinstIds.add(aeaHiItemMatinst.getMatinstId());*/
                    } else {
                        List<String> matinstIds = new ArrayList<>();
                        SaveMatinstVo.MatCountVo matCountVo = matCountMap.get(mat.getMatId());

                        // 纸质件不为0
                        int paperCnt = matCountVo.getPaperCnt();
                        int copyCnt = matCountVo.getCopyCnt();

                        if (paperCnt > 0) {
                            AeaHiItemMatinst aeaHiItemMatinst = mat2Matinst(mat, unitInfoId, projectInfoId, currentOrgId);
                            if ("c".equals(mat.getMatProp())) {
                                AeaCert aeaCert = aeaCertMapper.getAeaCertById(mat.getCertId(), currentOrgId);
                                AeaHiCertinst aeaHiCertinst = cert2Certinst(aeaCert, matCountVo.getAuthCode(), unitInfoId, projectInfoId, currentOrgId);
                                aeaHiItemMatinst.setCertinstId(aeaHiCertinst.getCertinstId());
                                certinsts.add(aeaHiCertinst);
                            }
                            aeaHiItemMatinst.setRealPaperCount((long) paperCnt);
                            matinsts.add(aeaHiItemMatinst);
                            // 返回关联关系
                            matinstIds.add(aeaHiItemMatinst.getMatinstId());
                        }
                        // 复印件不为0
                        if (copyCnt > 0) {
                            AeaHiItemMatinst aeaHiItemMatinst = mat2Matinst(mat, unitInfoId, projectInfoId, currentOrgId);
                            ;
                            if ("c".equals(mat.getMatProp())) {
                                AeaCert aeaCert = aeaCertMapper.getAeaCertById(mat.getCertId(), currentOrgId);
                                AeaHiCertinst aeaHiCertinst = cert2Certinst(aeaCert, matCountVo.getAuthCode(), unitInfoId, projectInfoId, currentOrgId);
                                aeaHiItemMatinst.setCertinstId(aeaHiCertinst.getCertinstId());
                                certinsts.add(aeaHiCertinst);
                            }
                            aeaHiItemMatinst.setRealCopyCount((long) copyCnt);
                            matinsts.add(aeaHiItemMatinst);
                            matinstIds.add(aeaHiItemMatinst.getMatinstId());
                        }
                        if (matinstIds.size() > 0) {
                            mat2MatInstVos.add(new Mat2MatInstVo(mat.getMatId(), matinstIds));
                        }
                    }
                });

        if (certinsts.size() > 0) {
            log.info("batch insert certinst, size: {}", certinsts.size());
            aeaHiCertinstMapper.batchInsertAeaHiCertinst(certinsts);
        }

        if (matinsts.size() > 0) {
            try {
                aeaHiItemMatinstMapper.batchInsertAeaHiItemMatinst(matinsts);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Batch save matinst error", e);
            }
        }
        return mat2MatInstVos;
    }

    private AeaHiCertinst cert2Certinst(AeaCert aeaCert, String authCode, String unitInfoId, String projInfoId, String rootOrgId) {
        AeaHiCertinst aeaHiCertinst = new AeaHiCertinst();
        aeaHiCertinst.setCertinstId(UuidUtil.generateUuid());
        aeaHiCertinst.setCertId(aeaCert.getCertId());
        aeaHiCertinst.setUnitInfoId(aeaCert.getUnitInfoId());
        aeaHiCertinst.setProjInfoId(projInfoId);
        aeaHiCertinst.setAttLinkId(unitInfoId);
        aeaHiCertinst.setCreater(SecurityContext.getCurrentUserId());
        aeaHiCertinst.setCreateTime(new Date());
        aeaHiCertinst.setCertinstCode(aeaCert.getCertCode());
        aeaHiCertinst.setCertinstName(aeaCert.getCertName());
        aeaHiCertinst.setRootOrgId(rootOrgId);
        aeaHiCertinst.setMemo(aeaCert.getCertMemo());
        aeaHiCertinst.setAuthCode(authCode);
        return aeaHiCertinst;
    }

    private AeaHiItemMatinst mat2Matinst(AeaItemMat aeaItemMat, String unitInfoId, String projInfoId, String rootOrgId) {
        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        aeaHiItemMatinst.setMatinstId(UuidUtil.generateUuid());
        aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
        aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
        aeaHiItemMatinst.setCreateTime(new Date());
        aeaHiItemMatinst.setMatId(aeaItemMat.getMatId());
        aeaHiItemMatinst.setUnitInfoId(unitInfoId);
        aeaHiItemMatinst.setProjInfoId(projInfoId);
        aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
        aeaHiItemMatinst.setMatProp(aeaItemMat.getMatProp());
        aeaHiItemMatinst.setRootOrgId(rootOrgId);
        return aeaHiItemMatinst;
    }

    //判断该附件是否存在共享，如果存在共享，则只删除LINK关联表
    private List<String> getOnlyOneRecord(String[] recordIds, String matinstId) throws Exception {

        List<String> detailIds_ = new ArrayList();

        for (int i = 0; i < recordIds.length; i++) {
            BscAttLink attLink = new BscAttLink();
            attLink.setDetailId(recordIds[i]);
            attLink.setPkName("MATINST_ID");
            List<BscAttLink> attLinks = bscAttMapper.listBscAttLink(attLink);
            if (attLinks.size() > 1) {
                for (BscAttLink bscAttLink : attLinks) {
                    if (bscAttLink.getRecordId().equals(matinstId))
                        bscAttMapper.deleteAttLinkBylinkId(bscAttLink.getLinkId());
                }
            } else {
                detailIds_.add(recordIds[i]);
            }
        }

        return detailIds_;
    }
}
