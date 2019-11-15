package com.augurit.aplanmis.supermarket.projPurchase.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.aplanmis.common.constants.CommonConstant;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.supermarket.projPurchase.vo.ItemMatVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
