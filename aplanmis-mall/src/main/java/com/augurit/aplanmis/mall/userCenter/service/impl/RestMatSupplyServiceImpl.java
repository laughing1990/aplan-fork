package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.MatCorrectDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectDueIninstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectRealIninstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.augurit.aplanmis.mall.userCenter.service.RestMatSupplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class RestMatSupplyServiceImpl implements RestMatSupplyService {

    @Autowired
    private AeaHiItemCorrectService aeaHiItemCorrectService;

    @Autowired
    private AeaHiItemCorrectRealIninstService aeaHiItemCorrectRealIninstService;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiItemCorrectDueIninstService aeaHiItemCorrectDueIninstService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private RestFileService restFileService;

    /**
     * 保存补正的材料
     *
     * @param matCorrectinstDto
     * @throws Exception
     */
    @Override
    public void saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) throws Exception {

        if (StringUtils.isBlank(matCorrectinstDto.getCorrectId())) throw new Exception("材料补正实例ID为空！");
        if (StringUtils.isBlank(matCorrectinstDto.getCorrectState())) throw new Exception("材料补正实例状态为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(matCorrectinstDto.getCorrectId());
        if (aeaHiItemCorrect == null) throw new Exception("找不到材料补正实例信息！");

        aeaHiItemCorrect.setCorrectDesc(matCorrectinstDto.getCorrectDesc());
        String state = matCorrectinstDto.getCorrectState();

        if ("6".equals(state)) {    //待补正

            JSONArray jsonArray = JSONArray.parseArray(matCorrectinstDto.getMatCorrectDtosJson());
            List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if (matCorrectDto.getRealPaperCount() != null && matCorrectDto.getRealPaperCount() > 0) {

                    if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {
                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {
                        saveAeaHiItemCorrectRealIninst(matCorrectinstDto, matCorrectDto);
                    }
                }

                if (matCorrectDto.getRealCopyCount() != null && matCorrectDto.getRealCopyCount() > 0) {

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {
                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {
                        saveAeaHiItemCorrectRealIninst(matCorrectinstDto, matCorrectDto);
                    }
                }
            }

        } else if ("7".equals(state)) {     //已补正
            aeaHiItemCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiItemCorrect.setCorrectEndTime(new Date());

            JSONArray jsonArray = JSONArray.parseArray(matCorrectinstDto.getMatCorrectDtosJson());
            List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if (matCorrectDto.getPaperCount() != null && matCorrectDto.getPaperCount() > 0) {
                    if (matCorrectDto.getRealPaperCount() == null || matCorrectDto.getRealPaperCount() < matCorrectDto.getPaperCount())
                        throw new Exception("缺少纸质材料（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {
                        saveAeaHiItemCorrectRealIninst(matCorrectinstDto, matCorrectDto);
                    }
                }

                if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {
                    if (matCorrectDto.getRealCopyCount() == null || matCorrectDto.getRealCopyCount() < matCorrectDto.getCopyCount())
                        throw new Exception("缺少复印件（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {
                        saveAeaHiItemCorrectRealIninst(matCorrectinstDto, matCorrectDto);
                    }
                }

                if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {
                    if (matCorrectDto.getAttCount() == null || matCorrectDto.getAttCount() < 1)
                        throw new Exception("缺少电子件（" + matCorrectDto.getMatinstName() + "）");
                }
            }

            //检查材料是否已全部补正
            if (!this.isCheckCorrect(matCorrectinstDto.getCorrectId())) throw new Exception("还有材料未补正！");

        } else {    //不予受理
            aeaHiItemCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiItemCorrect.setCorrectEndTime(new Date());
        }

        if (StringUtils.isBlank(aeaHiItemCorrect.getOpsUserId()) || !SecurityContext.getCurrentUserId().equals(StringUtils.isBlank(aeaHiItemCorrect.getOpsUserId()))) {
            aeaHiItemCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiItemCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        }
        aeaHiItemCorrect.setOpsTime(new Date());
        aeaHiItemCorrect.setCorrectState(state);
        aeaHiItemCorrectService.updateAeaHiItemCorrect(aeaHiItemCorrect);

        if ("7".equals(state) || "5".equals(state)) {
            AeaLogItemStateHist aeaLogItemStateHist = aeaLogItemStateHistService.getAeaLogItemStateHistByCorrectId(aeaHiItemCorrect.getCorrectId());
            //更新事项状态和新增历史记录
            if ("1".equals(aeaLogItemStateHist.getIsFlowTrigger())) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), ItemStatus.CORRECT_MATERIAL_END.getValue(), null);
            } else {
                String option = "7".equals(state) ? "材料已补正" : "不予受理";
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaHiItemCorrect.getCorrectDesc(), option, null, ItemStatus.CORRECT_MATERIAL_END.getValue(), null);
            }
        }
    }


    private void saveAeaHiItemCorrectRealIninst( MatCorrectinstDto matCorrectinstDto,  MatCorrectDto matCorrectDto) throws Exception {
        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
        aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getPaperDueIninstId());
        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
        aeaHiItemCorrectRealIninst.setIsPass("0");
        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getPaperInoutinstId());
        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
    }

    //判断材料补正是否已收齐
    private boolean isCheckCorrect(String correctId) throws Exception {

        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空！");
        //需要补正的材料清单
        List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts = aeaHiItemCorrectDueIninstService.getCorrectDueIninstByCorrectId(correctId);
        if (aeaHiItemCorrectDueIninsts.size() < 1) return true;

        //已补正的材料清单
        List<AeaHiItemCorrectRealIninst> aeaHiItemCorrectRealIninsts = aeaHiItemCorrectRealIninstService.getCorrectRealIninstByCorrectId(correctId);
        if (aeaHiItemCorrectRealIninsts.size() < 1) return false;

        List correctedList = new ArrayList();
        for (AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst : aeaHiItemCorrectRealIninsts) {

            for (AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst : aeaHiItemCorrectDueIninsts) {

                if (aeaHiItemCorrectDueIninst.getDueIninstId().equals(aeaHiItemCorrectRealIninst.getDueIninstId())) {

                    if (aeaHiItemCorrectDueIninst.getPaperCount() != null && aeaHiItemCorrectDueIninst.getPaperCount() > 0 && aeaHiItemCorrectRealIninst.getRealPaperCount() != null && aeaHiItemCorrectRealIninst.getRealPaperCount() >= aeaHiItemCorrectDueIninst.getPaperCount()) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getRealIninstId());
                        break;
                    }

                    if (aeaHiItemCorrectDueIninst.getCopyCount() != null && aeaHiItemCorrectDueIninst.getCopyCount() > 0 && aeaHiItemCorrectRealIninst.getRealCopyCount() != null && aeaHiItemCorrectRealIninst.getRealCopyCount() >= aeaHiItemCorrectDueIninst.getCopyCount()) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getRealIninstId());
                        break;
                    }

                    if (StringUtils.isNotBlank(aeaHiItemCorrectDueIninst.getIsNeedAtt()) && "1".equals(aeaHiItemCorrectDueIninst.getIsNeedAtt()) && aeaHiItemCorrectRealIninst.getAttCount() != null && aeaHiItemCorrectRealIninst.getAttCount() > 0) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getRealIninstId());
                        break;
                    }
                }
            }
        }

        if (correctedList.size() == aeaHiItemCorrectDueIninsts.size())
            return true;
        else
            return false;
    }


    /**
     * 材料删除
     *
     * @param detailIds
     * @throws Exception
     */
    @Override
    public void delelteAttFile(String detailIds, String attRealIninstId) throws Exception {
        AeaHiItemCorrectRealIninst realIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(attRealIninstId);
        if (realIninst != null) {
            restFileService.delelteAttachmentByCloud(detailIds.split(","),attRealIninstId);
            updateAeaHiItemCorrectRealIninst(attRealIninstId, realIninst);
        }
    }


    @Override
    public void uploadFile(String attRealIninstId, HttpServletRequest request) throws Exception {
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            AeaHiItemCorrectRealIninst realIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(attRealIninstId);
            if (realIninst == null) throw new Exception("材料实例不存在！");
            fileUtilsService.uploadAttachments("AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", attRealIninstId, files);
            updateAeaHiItemCorrectRealIninst(attRealIninstId, realIninst);

        }
    }

    public void uploadFileByCloud(String attRealIninstId, String  detailIds) throws Exception {
        AeaHiItemCorrectRealIninst realIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(attRealIninstId);
        if (realIninst == null) throw new Exception("材料实例不存在！");
        String[] detailIdArr = detailIds.split(",");
        for (String s : detailIdArr) {
            insertCorrectRealInst(attRealIninstId,s);
        }
        updateAeaHiItemCorrectRealIninst(attRealIninstId, realIninst);
    }


    private void insertCorrectRealInst(String  attRealIninstId,String detailId) throws Exception {
        BscAttLink bscAttLink = new BscAttLink();
        bscAttLink.setLinkId(UUID.randomUUID().toString());
        bscAttLink.setTableName("AEA_HI_ITEM_CORRECT_REAL_ININST");
        bscAttLink.setPkName("REAL_ININST_ID");
        bscAttLink.setRecordId(attRealIninstId);
        bscAttLink.setDetailId(detailId);
        bscAttLink.setLinkType("a");
        bscAttMapper.insertLink(bscAttLink);
    }
    private void updateAeaHiItemCorrectRealIninst(String attRealIninstId, AeaHiItemCorrectRealIninst realIninst) throws Exception {
        List<BscAttFileAndDir> matAttDetail = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
        realIninst.setAttCount(Long.valueOf(matAttDetail.size()));
        realIninst.setModifyTime(new Date());
        realIninst.setModifier(SecurityContext.getCurrentUserName());
        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(realIninst);
    }
}
