package com.augurit.aplanmis.province.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@Transactional
public class ApproveCommonService {
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaHiReceiveMapper aeaHiReceiveMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;

    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;

    /**
     * 根据 项目编码，事项实例ID 查询申请实例
     *
     * @param proj_code
     * @param item_instance_code
     * @return
     * @throws Exception
     */
    public AeaHiApplyinst getApplyinstByProjCodeAndIteminstId(String proj_code, String item_instance_code) throws Exception {
        if (StringUtils.isNotBlank(item_instance_code)) {
            String applyinstId = getApplyinstIdByIteminstId(item_instance_code);
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
            return aeaHiApplyinst;
//            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(item_instance_code);
//            if (null == iteminst) return null;
//            String isSeriesApprove = iteminst.getIsSeriesApprove();
//            if ("0".equals(isSeriesApprove)) {
//                //并联
//                String stageinstId = iteminst.getStageinstId();
//                AeaHiParStageinst stageinst = aeaHiParStageinstMapper.getAeaHiParStageinstById(stageinstId);
//                if (null == stageinst) return null;
//                AeaHiApplyinst aeaHiApplyinstById = aeaHiApplyinstMapper.getAeaHiApplyinstById(stageinst.getApplyinstId());
//                return aeaHiApplyinstById;
//            } else {
//                String seriesinstId = iteminst.getSeriesinstId();
//            }
        }
        /*List<AeaHiApplyinst> list = aeaHiApplyinstMapper.getApplyinstByProjCodeAndIteminstId(proj_code, item_instance_code);
        if (list.size() > 0) {
            return list.get(0);
        }*/
        return null;
    }

    public String getApplyinstIdByIteminstId(String iteminstId) throws Exception {
        if (StringUtils.isNotBlank(iteminstId)) {
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            if (null == iteminst) return null;
            String isSeriesApprove = iteminst.getIsSeriesApprove();
            if ("0".equals(isSeriesApprove)) {
                //并联
                String stageinstId = iteminst.getStageinstId();
                AeaHiParStageinst stageinst = aeaHiParStageinstMapper.getAeaHiParStageinstById(stageinstId);
                if (null == stageinst) return null;
                return stageinst.getApplyinstId();
            } else {
                String seriesinstId = iteminst.getSeriesinstId();
                AeaHiSeriesinst seriesinst = aeaHiSeriesinstMapper.getAeaHiSeriesinstById(seriesinstId);
                if (null == seriesinst) return null;
                return seriesinst.getApplyinstId();

            }
        }
        return null;
    }

    /**
     * 根据申请流水号或者申请实例ID查找流程实例ID
     *
     * @param applyinstId
     * @param applyinstCode
     * @return
     * @throws Exception
     */
    public List<String> getProcInstIdByApplyinstIdOrApplyinstCode(String applyinstId, String applyinstCode) throws Exception {
        return aeaHiApplyinstMapper.getProcInstIdByApplyinstIdOrApplyinstCode(applyinstId, applyinstCode);
    }

    /**
     * 根据 项目编码，事项实例ID 查询该事项对应阶段下的所有事项的部分信息
     *
     * @param proj_code
     * @param item_instance_code
     * @return
     * @throws Exception
     */
    public List<AeaHiApplyinst> getParalleApproveDataByIteminstIdAndProjCode(String proj_code, String item_instance_code) throws Exception {
        return aeaHiApplyinstMapper.getParalleApproveDataByIteminstIdAndProjCode(proj_code, item_instance_code);
    }

    /**
     * 获取指定事项实例对应的材料（或批文批复）
     *
     * @param iteminstIds 事项实例ID数组
     * @param isOfficeDoc 是否批复文件，0表示否，1表示是
     * @return
     * @throws Exception
     */
    public List<AeaMatinst> getMatlist(String[] iteminstIds, String isOfficeDoc) throws Exception {
        List<AeaMatinst> matList = aeaHiItemInoutinstMapper.getMatinstListBy(iteminstIds, isOfficeDoc);
        //查询电子件
        if (matList.size() > 0) {
            for (AeaMatinst aeaMatinst : matList) {
                //存在电子材料
                if (aeaMatinst.getAttCount() > 0) {
                    String[] recordIds = {aeaMatinst.getMatinstId()};
                    List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList<BscAttFileAndDir>();
                    if (recordIds.length > 0) {
                        bscAttFileAndDirs.addAll(bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_MATINST", "MATINST_ID", recordIds));
                    }
                    aeaMatinst.setAttFileList(bscAttFileAndDirs);
                }
            }
        }
        return matList;
    }

    /**
     * 根据项目code查询项目信息
     */
    public String getProjNameByProjCode(String projCode) {
        List<AeaProjInfo> list = aeaProjInfoMapper.getProjInfoByCode(projCode);
        return list.size() > 0 ? list.get(0).getProjName() : "";
    }

    @Autowired
    protected IBscAttService bscAtService;

    /**
     * jdk8stream 去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<AeaHiReceive> getAeaHiReceiveByApplyinstId(String applyinstId) throws Exception {
        List<AeaHiReceive> receiveList = new ArrayList<>();
        AeaHiReceive query = new AeaHiReceive();
        query.setApplyinstId(applyinstId);
        List<AeaHiReceive> list = aeaHiReceiveMapper.listAeaHiReceive(query);
        for (AeaHiReceive receive : list) {//RECEIPT_TYPE
            BscDicCodeItem dic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("RECEIPT_TYPE", receive.getReceiptType(), topOrgId);
            receive.setReceiveName(dic == null ? "" : dic.getItemName());
            receiveList.add(receive);
        }
        return receiveList;
    }
}
