package com.augurit.aplanmis.province.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaMatinst;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.province.vo.AeaMatinstAndIteminstVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApproveMatService {
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;


    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;

    /**
     * 查询申报材料
     *
     * @param project_code
     * @param item_instance_code iteminstId
     * @return
     * @throws Exception
     */
    public AeaMatinstAndIteminstVo getApplyMatinstList(String project_code, String item_instance_code) throws Exception {
        AeaMatinstAndIteminstVo vo = new AeaMatinstAndIteminstVo();
        Map<String, Object> map = new HashMap<>();
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(item_instance_code);
        if (aeaHiIteminst != null) {
            //单项申报标识
            String isSeriesApprove = "1";
            //单项申报材料
            if (isSeriesApprove.equals(aeaHiIteminst.getIsSeriesApprove())) {
                vo.setStageName(aeaHiIteminst.getIteminstName());
                //查询单项申报下所有材料实例
                List<AeaMatinst> seriesMatingList = aeaHiItemInoutinstMapper.getSeriesMatingList(aeaHiIteminst.getIteminstId(), null);
                //合并相同matId对应的matinstId
                seriesMatingList = combineListByMatId(seriesMatingList);

                if (seriesMatingList.size() > 0) {
                    //通用材料
                    List<AeaMatinst> commonMatList = seriesMatingList.stream().filter(aea -> !StringUtils.isEmpty(aea.getIsCommon()) && "1".equals(aea.getIsCommon())).collect(Collectors.toList());
                    setAttFileList(commonMatList);
                    vo.setCommonMatList(commonMatList);
                    //事项材料
                    //事项材料-去除通用材料和批文批复
                    List<AeaMatinst> itemMat = seriesMatingList.stream().filter(aea -> (StringUtils.isEmpty(aea.getIsCommon()) || "0".equals(aea.getIsCommon())) && (StringUtils.isEmpty(aea.getIsOfficeDoc()) || !"0".equals(aea.getIsOfficeDoc()))).collect(Collectors.toList());
                    setAttFileList(itemMat);
                    map.put(aeaHiIteminst.getIteminstName(), null == itemMat ? new ArrayList<>() : itemMat);
                    vo.setItemMatinst(map);
                    //批文批复
                    List<AeaMatinst> officeMatList = seriesMatingList.stream().filter(aea -> (!StringUtils.isEmpty(aea.getIsOfficeDoc()) && "1".equals(aea.getIsOfficeDoc()))).collect(Collectors.toList());
                    setAttFileList(commonMatList);
                    vo.setOfficeMatList(officeMatList);

                } else {
                    //初始化值，避免页面报错
                    vo.setCommonMatList(new ArrayList<>());
                    vo.setOfficeMatList(new ArrayList<>());
                    vo.setItemMatinst(map);
                }

            } else {
                //查询到所有的事项实例
                List<AeaHiIteminst> iteminstList = aeaHiIteminstMapper.getUnitAeaHiIteminstByiteminstId(aeaHiIteminst.getIteminstId());
                //并联申报材料
                AeaHiParStageinst aeaHiParStageinstById = aeaHiParStageinstMapper.getAeaHiParStageinstById(aeaHiIteminst.getStageinstId());
                vo.setStageName(aeaHiParStageinstById.getStageName());
                //查询到所有的材料定义及关联的事项材料实例
                List<AeaMatinst> allUnionMatinstList = aeaHiItemInoutinstMapper.getAllUnionMatinstList(aeaHiIteminst.getIteminstId(), null);
                //合并相同材料
                allUnionMatinstList = combineListByMatId(allUnionMatinstList);
                //通用材料
                List<AeaMatinst> commonMatList = allUnionMatinstList.stream().filter(aea -> !StringUtils.isEmpty(aea.getIsCommon()) && "1".equals(aea.getIsCommon())).collect(Collectors.toList());
                setAttFileList(commonMatList);
                vo.setCommonMatList(null == commonMatList ? new ArrayList<>() : commonMatList);
                //事项材料-去除通用材料和批文批复
                List<AeaMatinst> itemMat = allUnionMatinstList.stream().filter(aea -> (StringUtils.isEmpty(aea.getIsCommon()) || "0".equals(aea.getIsCommon())) && (StringUtils.isEmpty(aea.getIsOfficeDoc()) || !"0".equals(aea.getIsOfficeDoc()))).collect(Collectors.toList());
                if (itemMat != null && itemMat.size() > 0) {
                    //合并相同事项的材料
                    String itemIdAndName = "";
                    for (AeaHiIteminst temp2 : iteminstList) {
                        itemIdAndName += temp2.getIteminstId() + "," + temp2.getIteminstName() + ";";
                    }

                    String[] itemIdAndNames = itemIdAndName.substring(0, itemIdAndName.length() - 1).split(";");
                    for (String s : itemIdAndNames) {
                        String[] split = s.split(",");
                        String iteminstId = split[0];
                        String iteminstName = split[1];
                        List<AeaMatinst> collect = itemMat.stream().filter(aea -> iteminstId.equals(aea.getIteminstId())).collect(Collectors.toList());
                        if (collect != null && collect.size() > 0) {
                            setAttFileList(collect);
                        }
                        map.put(iteminstName, collect);
                    }
                    vo.setItemMatinst(map);

                } else {
                    //事项材料为空，需要初始化list,不能出现null,避免页面取值报错
                    Map<String, Object> itemMap = new HashMap<>();
                    if (iteminstList.size() > 0) {
                        String[] itemNames = iteminstList.stream().map(AeaHiIteminst::getIteminstName).toArray(String[]::new);
                        for (String s : itemNames) {
                            itemMap.put(s, new ArrayList<>());
                        }
                    }
                    vo.setItemMatinst(itemMap);
                }
                //批文批复
                List<AeaMatinst> officeMatList = allUnionMatinstList.stream().filter(aea -> (!StringUtils.isEmpty(aea.getIsOfficeDoc()) && "1".equals(aea.getIsOfficeDoc()))).collect(Collectors.toList());
                setAttFileList(commonMatList);
                vo.setOfficeMatList(officeMatList == null ? new ArrayList<>() : officeMatList);
            }
            return vo;
        } else {
            throw new Exception("查询不到事项实例信息");
        }
    }

    /**
     * 根据事项实例ID查询单项并联事项实例下的材料实例
     *
     * @param iteminstId
     * @return
     */
    public List<AeaMatinst> getMatinstListByIteminstId(String iteminstId) throws Exception {
        AeaHiIteminst aeaHiIteminstById = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
        if (aeaHiIteminstById != null) {
            String seriesApprove = aeaHiIteminstById.getIsSeriesApprove();
            //并联
            if (!StringUtils.isEmpty(seriesApprove) && "0".equals(seriesApprove)) {
                List<AeaHiIteminst> iteminstList = aeaHiIteminstMapper.getUnitAeaHiIteminstByiteminstId(iteminstId);
                if (iteminstList.size() > 0) {
                    iteminstId = iteminstList.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.joining(","));
                }
            }
            List<AeaMatinst> matinstListBy = aeaHiItemInoutinstMapper.getMatinstListBy(iteminstId.split(","), null);
            return matinstListBy;

        }
        return new ArrayList();
    }

    /**
     * 查询材料附件列表
     *
     * @param matinstId
     * @return
     * @throws Exception
     */
    public List<BscAttFileAndDir> getMatAttDetail(String matinstId) throws Exception {
        String[] recordIds = matinstId.split(",");
        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList<BscAttFileAndDir>();
        if (recordIds.length > 0) {
            List<BscAttFileAndDir> attFileList = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_MATINST", "MATINST_ID", recordIds);
            bscAttFileAndDirs.addAll(attFileList == null ? new ArrayList<>() : attFileList);
        }
        return bscAttFileAndDirs;
    }

    private void setAttFileList(List<AeaMatinst> list) throws Exception {
        if (list != null && list.size() > 0) {
            for (AeaMatinst temp : list) {
                if (temp.getAttCount() != null && temp.getAttCount() > 0) {
                    temp.setAttFileList(getMatAttDetail(temp.getMatinstId()));
                } else {
                    temp.setAttFileList(new ArrayList<>());
                }
            }
        }
    }

    private List<AeaMatinst> combineListByMatId(List<AeaMatinst> oldList) throws Exception {
        //合并相同matId对应的matinstId
        List<AeaMatinst> newList = new ArrayList<>();
        Map<String, AeaMatinst> temp = new HashMap<>();
        if (oldList.size() > 0) {
            for (AeaMatinst aeaMatinst : oldList) {
                if (temp.containsKey(aeaMatinst.getMatId())) {
                    AeaMatinst mat = temp.get(aeaMatinst.getMatId());
                    if (!StringUtils.isEmpty(aeaMatinst.getAttCount()) && aeaMatinst.getAttCount() > 0) {
                        mat.setMatinstId(aeaMatinst.getMatinstId());
                        mat.setAttFileList(getMatAttDetail(aeaMatinst.getMatinstId()));
                    }
                    mat.setRealPaperCount(aeaMatinst.getRealPaperCount() + mat.getRealPaperCount());
                    mat.setRealCopyCount(aeaMatinst.getRealCopyCount() + mat.getRealCopyCount());
                    mat.setAttCount(aeaMatinst.getAttCount() + mat.getAttCount());
                    temp.replace(mat.getMatId(), mat);
                } else {
                    if (!StringUtils.isEmpty(aeaMatinst.getAttCount()) && aeaMatinst.getAttCount() > 0) {
                        aeaMatinst.setAttFileList(getMatAttDetail(aeaMatinst.getMatinstId()));
                    }
                    temp.put(aeaMatinst.getMatId(), aeaMatinst);
                }
            }

        }

        for (String key : temp.keySet()) {
            newList.add(temp.get(key));
        }
        return newList;
    }
}

