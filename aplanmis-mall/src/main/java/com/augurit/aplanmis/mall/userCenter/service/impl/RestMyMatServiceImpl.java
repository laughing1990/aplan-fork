package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.CommonConstant;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.mall.userCenter.service.RestMyMatService;
import com.augurit.aplanmis.common.vo.MyMatFilesVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestMyMatServiceImpl implements RestMyMatService {

    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private FileUtilsService fileUtilsService;


    public PageInfo<BscAttFileAndDir> getMyMatListByUser(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize) throws Exception {

            //1.根据当前登录用户查询所有申请实例
            List<AeaHiApplyinst> applyinsts = aeaHiApplyinstMapper.listApplyInstListByUser(unitInfoId, userInfoId);
            if (applyinsts == null || applyinsts.size() == 0) return new PageInfo<>(new ArrayList<>());

            //2.根据申请实例查询所有事项实例
            List<AeaHiIteminst> iteminstList = new ArrayList<>();
            if (applyinsts == null || applyinsts.size() == 0) return new PageInfo<>();
            List<String> applyinstIds = applyinsts.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList());
            if (applyinstIds.size() == 0) return new PageInfo<>();
            iteminstList.addAll(aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(applyinstIds, null,"0"));
            if (iteminstList.size() == 0) return new PageInfo<>();
            List<String> iteminstIds = iteminstList.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toList());

            //3.根据事项申请实例ID查询所有材料实例
            List<MyMatFilesVo> aeaHiItemMatinstList = aeaHiItemMatinstService.getMatinstListByIteminstIdsAndKeyword(iteminstIds.toArray(new String[iteminstIds.size()]), "1", null,"1");
            if (aeaHiItemMatinstList.size() == 0) return new PageInfo<>(new ArrayList<>());
            List<String> matInstIds = aeaHiItemMatinstList.stream().map(MyMatFilesVo::getMatinstId).distinct().collect(Collectors.toList());
            String[] matinstArr = matInstIds.stream().toArray(String[]::new);
            //4.根据材料实例ID查询所有文件
            if (StringUtils.isBlank(keyword)) keyword = null;
            PageHelper.startPage(pageNum,pageSize);
            List<BscAttFileAndDir> list = bscAttDetailMapper.searchFileAndDirsSimple(keyword, SecurityContext.getCurrentOrgId(), "AEA_HI_ITEM_MATINST", "MATINST_ID", matinstArr);
                return new PageInfo<>(list);
        }


    public PageInfo<MyMatFilesVo> getMyMatListByUser1(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize) throws Exception {
        List<MyMatFilesVo> myMatFilesVos = null;
        //1.根据当前登录用户查询所有申请实例
        List<AeaHiApplyinst> applyinsts = aeaHiApplyinstMapper.listApplyInstListByUser(unitInfoId, userInfoId);
        if (applyinsts == null || applyinsts.size() == 0) return  new PageInfo<>();

        //2.根据申请实例查询所有事项实例
        List<AeaHiIteminst> iteminstList = new ArrayList<>();
        if (applyinsts == null || applyinsts.size() == 0) return  new PageInfo<>();
        List<String> applyinstIds = applyinsts.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList());
        if (applyinstIds.size() == 0) return  new PageInfo<>();
        iteminstList.addAll(aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(applyinstIds, null,"0"));
        if (iteminstList.size() == 0) return  new PageInfo<>();
        List<String> iteminstIds = iteminstList.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toList());

        //3.根据事项申请实例ID查询所有材料实例
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isBlank(keyword)) keyword = null;
        myMatFilesVos = aeaHiItemMatinstService.getMatinstListByIteminstIdsAndKeyword(iteminstIds.toArray(new String[iteminstIds.size()]), "1", keyword,"1");
        if (myMatFilesVos.size() == 0) return  new PageInfo<>();
        for (MyMatFilesVo vo : myMatFilesVos) {
            String[] matinstArr = new String[]{vo.getMatinstId()};
            //4.根据材料实例ID查询所有文件
            List<BscAttFileAndDir> list = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_ITEM_MATINST", "MATINST_ID", matinstArr);
            vo.setBscAttFileAndDir(list);
        }
        return new PageInfo<>(myMatFilesVos);
    }

    /**
     * 删除材料实例及附件
     *
     * @param matinstId
     */
    @Override
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
