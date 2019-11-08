package com.augurit.aplanmis.data.exchange.service;

import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.data.exchange.constant.EtlError;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlErrorLog;
import com.augurit.aplanmis.data.exchange.service.aplanmis.*;
import com.augurit.aplanmis.data.exchange.service.spgl.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据上传错误诊断
 *
 * @author yinlf
 * @Date 2019/10/21
 */
@Component
public class DiagnoseErrorService {
    private static final int UPLOAD_LIMIT = 3000;

    @Autowired
    EtlErrorLogService etlErrorLogService;

    @Autowired
    ThemeVerService themeVerService;
    @Autowired
    StageService stageService;
    @Autowired
    StageItemService stageItemService;
    @Autowired
    StageSubordinateItemService stageSubordinateItemService;
    @Autowired
    ApplyProjService applyProjService;
    @Autowired
    ApplyProjUnitService applyProjUnitService;
    @Autowired
    IteminstService iteminstService;

    @Autowired
    SpglDfxmsplcxxbService spglDfxmsplcxxbService;
    @Autowired
    SpglDfxmsplcjdxxbService spglDfxmsplcjdxxbService;
    @Autowired
    SpglDfxmsplcjdsxxxbService spglDfxmsplcjdsxxxbService;
    @Autowired
    SpglXmjbxxbService spglXmjbxxbService;
    @Autowired
    SpglXmdwxxbService spglXmdwxxbService;
    @Autowired
    SpglXmspsxblxxbService spglXmspsxblxxbService;

    @Autowired
    AeaHiItemInoutinstMapper inoutinstMapper;
    @Autowired
    AeaLogItemStateHistService logItemStateHistService;
    @Autowired
    AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    AeaApplyinstProjMapper aeaApplyinstProjMapper;

    public void zheduandashi() {
        int i = 1;
        PageInfo<EtlErrorLog> pageInfo;
        Page page = new Page(i, UPLOAD_LIMIT);
        do {
            pageInfo = etlErrorLogService.listUnDiagnoseEtlErrorLog(page);
            List<EtlErrorLog> list = pageInfo.getList();
            StringBuilder  diagnoseResult = null;
            for (EtlErrorLog errorLog : list) {
                diagnoseResult = null;
                try {
                    if (EtlError.ITEMINST_NOT_FOUND.getValue().equals(errorLog.getErrCode())) {
                        //事项实例没有上传
                        String iteminstId = "";
                        if (TableNameConstant.SPGL_XMQTFJXXB.equals(errorLog.getTableName()) || TableNameConstant.SPGL_XMSPSXPFWJXXB.equals(errorLog.getTableName())) {
                            String iteminoutInst = errorLog.getRecordId();
                            AeaHiItemInoutinst inoutinst = inoutinstMapper.getAeaHiItemInoutinstById(iteminoutInst);
                            iteminstId = inoutinst.getIteminstId();
                        } else if (TableNameConstant.SPGL_XMSPSXBLXXXXB.equals(errorLog.getTableName())) {
                            String stateHistId = errorLog.getRecordId();
                            AeaLogItemStateHist logItemStateHist = logItemStateHistService.getAeaLogItemStateHistById(stateHistId);
                            iteminstId = logItemStateHist.getIteminstId();
                        }
                        AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
                        if (aeaHiIteminst == null) {

                        }
                    }else if(EtlError.PROJ_NOT_FOUND.getValue().equals(errorLog.getErrCode())){
                        //项目未上传
                        //1.查询日志表是否有记录
                         if(StringUtils.isNotBlank(errorLog.getErrValue())){
                             AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByGcbm(errorLog.getErrValue());
                             if(aeaProjInfo==null){
                                 diagnoseResult.append("项目表没有GCDM为：").append(errorLog.getErrValue()).append("的项目");
                                 continue;
                             }
                             String projInfoId = aeaProjInfo.getProjInfoId();
                             AeaApplyinstProj condi = new AeaApplyinstProj();
                             condi.setProjInfoId(projInfoId);
                             //2.查询申报记录是否存在
                             List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.listAeaApplyinstProj(condi);
                             if(aeaApplyinstProjs==null||aeaApplyinstProjs.size()<=0){
                                 diagnoseResult.append("项目表没有GCDM为：").append(errorLog.getErrValue()).append("的项目");
                                 continue;
                             }
                         }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
        } while (pageInfo.isHasNextPage());
    }
}
