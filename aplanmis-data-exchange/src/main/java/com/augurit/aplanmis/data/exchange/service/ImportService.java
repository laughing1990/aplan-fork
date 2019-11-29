package com.augurit.aplanmis.data.exchange.service;

import com.augurit.agcloud.framework.util.ReflectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.data.exchange.constant.EtlConstant;
import com.augurit.aplanmis.data.exchange.constant.StorageCacheKeyConstants;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.datasource.DataSourceType;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJob;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobDetailLog;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobLog;
import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;
import com.augurit.aplanmis.data.exchange.domain.base.SpglInstEntity;
import com.augurit.aplanmis.data.exchange.domain.base.SpglItemInstEntity;
import com.augurit.aplanmis.data.exchange.domain.spgl.*;
import com.augurit.aplanmis.data.exchange.dto.ThreadEtlJobLog;
import com.augurit.aplanmis.data.exchange.dto.view.SpglDfxmsplcjdfxsxxxbVew;
import com.augurit.aplanmis.data.exchange.dto.view.SpglXmspsxblxxbView;
import com.augurit.aplanmis.data.exchange.dto.view.ViewSubTable;
import com.augurit.aplanmis.data.exchange.exception.*;
import com.augurit.aplanmis.data.exchange.service.aplanmis.*;
import com.augurit.aplanmis.data.exchange.service.duogui.LandRedLineService;
import com.augurit.aplanmis.data.exchange.service.duogui.PlanControlLineService;
import com.augurit.aplanmis.data.exchange.service.duogui.PreIdeaService;
import com.augurit.aplanmis.data.exchange.service.spgl.*;
import com.augurit.aplanmis.data.exchange.util.RedisUtil;
import com.augurit.aplanmis.data.exchange.vo.EtlReuploadVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author yinlf
 * @Date 2019/9/26
 */
@Service
@Slf4j
public class ImportService {

    private static final int UPLOAD_LIMIT = 3000;

    @Value("${aplanmis.data.exchange.analyse.open}")
    private Boolean uploadToAnalyse;

    @Value("${aplanmis.data.exchange.upload-duogui}")
    private Boolean uploadDuoGui;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private EtlErrorLogService etlErrorLogService;


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
    SubordinateIteminstService subordinateIteminstService;
    @Autowired
    ItemOpinionService itemOpinionService;
    @Autowired
    SubordinateItemOpinionService subordinateItemOpinionService;
    @Autowired
    ItemOthersMatService itemOthersMatService;
    @Autowired
    OfficialDocService officialDocService;

    @Autowired
    LandRedLineService landRedLineService;
    @Autowired
    PreIdeaService preIdeaService;
    @Autowired
    PlanControlLineService planControlLineService;

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
    SpglXmspsxblxxxxbService spglXmspsxblxxxxbService;
    @Autowired
    SpglXmspsxpfwjxxbService spglXmspsxpfwjxxbService;
    @Autowired
    SpglXmqtfjxxbService spglXmqtfjxxbService;

    @Autowired
    SpglXmydhxjzxxbService spglXmydhxjzxxbService;
    @Autowired
    SpglXmqqyjxxbService spglXmqqyjxxbService;
    @Autowired
    SpglDfghkzxxxbService spglDfghkzxxxbService;

    @Autowired
    ItemBasicService itemBasicService;

    @Autowired
    EtlJobService etlJobService;
    @Autowired
    EtlJobLogService etlJobLogService;
    @Autowired
    EtlJobDetailLogService etlJobDetailLogService;

    private static String OPERATE_SOURCE = EtlConstant.PROGRAM_OPERATE;

    /**
     * 增量上传
     *
     * @return 上传数据量
     */
    public Long incrementAllTable() {
        EtlJob job = etlJobService.getEtlJobById("1");
        Date startTime = job.getStartTime();
        Date endTime = new Date();
        EtlJob updateJob = new EtlJob();
        updateJob.setJobId(job.getJobId());
        updateJob.setRunStatus("1");
        etlJobService.updateEtlJob(updateJob);
        this.importAllTableAndLog(startTime, endTime, OPERATE_SOURCE);
        Date nextTime = new Date();
        updateJob.setStartTime(endTime);
        updateJob.setEndTime(nextTime);
        updateJob.setRunStatus("0");
        etlJobService.updateEtlJob(updateJob);
        Long writtenNum = ThreadEtlJobLog.getWrittenNum();
        //线程池线程中线程会重用，需要手动清理线程变量
        ThreadEtlJobLog.clear();
        return writtenNum;
    }

    public void importAllTableAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importAllTable(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importThemeMainAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importThemeVer(startTime, endTime);
                importStage(startTime, endTime);
                importItem(startTime, endTime);
                importSubordinateItem(startTime, endTime);
                importProj(startTime, endTime);
                importIteminst(startTime, endTime);
                importSubordinateIteminst(startTime, endTime);
                importItemOpinion(startTime, endTime);
                importSubordinateItemOpinion(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importThemeVerAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importThemeVer(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importStageAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importStage(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importItemAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                //地方项目审批流程阶段事项信息表
                importItem(startTime, endTime);
                // 辅线事项
                importSubordinateItem(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importProjAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importProj(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importUnitAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importUnit(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importIteminstAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                //项目审批事项办理信息表
                importIteminst(startTime, endTime);
                // 辅线事项办理信息
                importSubordinateIteminst(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importItemOpinionAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                //项目审批事项办理详细信息表
                importItemOpinion(startTime, endTime);
                // 辅线事项办理详细信息
                importSubordinateItemOpinion(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importOfficDocAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importOfficDoc(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importItemMatinstAndLog(Date startTime, Date endTime, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                importItemMatinst(startTime, endTime);
            }
        }.wrapper(startTime, endTime);
    }

    public void importByTableName(EtlReuploadVo etlReuploadVo, String operateSource) {
        new ImportJobLogWrapper(operateSource) {

            @Override
            protected void executeImport(Date startTime, Date endTime) {
                List<String> tableNames = etlReuploadVo.getTableNames();
                if (tableNames.contains(TableNameConstant.SPGL_DFXMSPLCXXB)) {
                    importThemeVer(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_DFXMSPLCJDXXB)) {
                    importStage(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_DFXMSPLCJDSXXXB)) {
                    importItem(startTime, endTime);
                    importSubordinateItem(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_XMJBXXB)) {
                    importProj(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_XMDWXXB)) {
                    importUnit(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_XMSPSXBLXXB)) {
                    importIteminst(startTime, endTime);
                    importSubordinateIteminst(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_XMSPSXBLXXXXB)) {
                    importItemOpinion(startTime, endTime);
                    importSubordinateItemOpinion(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_XMSPSXPFWJXXB)) {
                    importOfficDoc(startTime, endTime);
                }
                if (tableNames.contains(TableNameConstant.SPGL_XMQTFJXXB)) {
                    importItemMatinst(startTime, endTime);
                }
                if (uploadDuoGui) {
                    if (tableNames.contains(TableNameConstant.SPGL_XMYDHXJZXXB)) {
                        importLandRedLine(startTime, endTime);
                    }
                    if (tableNames.contains(TableNameConstant.SPGL_XMQQYJXXB)) {
                        importPreIdea(startTime, endTime);
                    }
                    if (tableNames.contains(TableNameConstant.SPGL_DFGHKZXXXB)) {
                        importPlanControlLine(startTime, endTime);
                    }
                }
            }
        }.wrapper(etlReuploadVo.getStartTime(), etlReuploadVo.getEndTime());
    }

    private abstract class ImportJobLogWrapper {
        private String operateSource;

        public ImportJobLogWrapper(String operateSource) {
            this.operateSource = operateSource;
        }

        protected abstract void executeImport(Date startTime, Date endTime);

        public void wrapper(Date startTime, Date endTime) {
            this.initLogNum();
            this.executeImport(startTime, endTime);
            this.endLogNum(startTime, endTime);
        }

        private void initLogNum() {
            EtlJobLog etlJobLog = new EtlJobLog();
            String code = this.generateIncreaseJobLogCode();
            etlJobLog.setJobLogCode(code);
            etlJobLog.setOperateSource(this.operateSource);
            etlJobLogService.saveEtlJobLog(etlJobLog);
            ThreadEtlJobLog.setJobLogId(etlJobLog.getJobLogId());
        }

        private void endLogNum(Date startTime, Date endTime) {
            EtlJobLog etlJobLog = ThreadEtlJobLog.get();
            etlJobLog.setSolveNum(0L);
            etlJobLog.setStartTime(startTime);
            etlJobLog.setEndTime(endTime);
            Date now = new Date();
            etlJobLog.setCreateTime(now);
            etlJobLogService.updateEtlJobLog(etlJobLog);
            log.info("本次上传读取：{}，上传成功：{}，上传出错:{}", etlJobLog.getReadNum(), etlJobLog.getWrittenNum(), etlJobLog.getErrorNum());
        }

        private String generateIncreaseJobLogCode() {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String today = format.format(new Date());
            long code = this.generateIncreaseAtOneDay(EtlConstant.JOB_CODE_INCREASE_KEY);
            String codeStr = String.format("%04d", code);
            StringBuilder builder = new StringBuilder(today);
            builder.append(codeStr);
            return builder.toString();
        }

        private long generateIncreaseAtOneDay(String key) {
            long incr = redisUtil.incr(key, 1);
            if (incr == 1) {
                //当天结束时间
                LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
                Date todayEndTime = Date.from(todayEnd.atZone(ZoneId.systemDefault()).toInstant());
                redisUtil.expireAt(key, todayEndTime);
            }
            return incr;
        }
    }

    private void importAllTable(Date startTime, Date endTime) {
        //1.地方项目审批流程信息表
        this.importThemeVer(startTime, endTime);
        //2.地方项目审批流程阶段信息表
        this.importStage(startTime, endTime);

        //3.地方项目审批流程阶段事项信息表
        this.importItem(startTime, endTime);
        // 辅线事项
        this.importSubordinateItem(startTime, endTime);

        //4.项目基本信息表
        this.importProj(startTime, endTime);
        //5.项目单位信息表
        this.importUnit(startTime, endTime);

        //6.项目审批事项办理信息表
        this.importIteminst(startTime, endTime);
        // 辅线事项办理信息
        this.importSubordinateIteminst(startTime, endTime);

        //7.项目审批事项办理详细信息表
        this.importItemOpinion(startTime, endTime);
        // 辅线事项办理详细信息
        this.importSubordinateItemOpinion(startTime, endTime);

        //8.项目审批事项批复文件信息表
        this.importOfficDoc(startTime, endTime);
        //9.项目其他附件信息表
        this.importItemMatinst(startTime, endTime);

        log.info("工改所有表数据上传成功");
        /*
         * 多规
         * @author qhg
         * @Date 2019/11/04
         *
         */
        if (uploadDuoGui) {
            try {
                //10.项目用地红线界址信息表
                this.importLandRedLine(startTime, endTime);
                //11.项目前期意见信息表
                this.importPreIdea(startTime, endTime);
                //12.地方规划控制线信息表
                this.importPlanControlLine(startTime, endTime);
                log.info("多规数据上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                log.info("多规数据上传失败");
            }
        }
    }

    private void importThemeVer(Date startTime, Date endTime) {
        new AbstractImporter<SpglDfxmsplcxxb>(themeVerService, spglDfxmsplcxxbService) {
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_DFXMSPLCXXB);
    }

    private void importStage(Date startTime, Date endTime) {
        new AbstractImporter<SpglDfxmsplcjdxxb>(stageService, spglDfxmsplcjdxxbService) {
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_DFXMSPLCJDXXB);
    }

    private void importItem(Date startTime, Date endTime) {
        new AbstractImporter<SpglDfxmsplcjdsxxxb>(stageItemService, spglDfxmsplcjdsxxxbService) {
            @Override
            protected void prepareHandle(SpglDfxmsplcjdsxxxb item, Iterator<SpglDfxmsplcjdsxxxb> iterator) {
                //在前置库查询到事项已经上传，不再进行上传
                boolean itemHasImport = spglDfxmsplcjdsxxxbService.findActiveSpglDfxmsplcjdsxxxbByUnique(item.getSplcbm(), item.getSplcbbh(), item.getSpsxbm(), item.getSpsxbbh());
                if (itemHasImport) {
                    iterator.remove();
                    ThreadEtlJobLog.reduceReadNum();
                    detailReadNum--;
                    return;
                }
                if (StringUtils.isBlank(item.getSplcbm())) {
                    throw new ProjUnbindThemeException();
                }
                if (item.getSplcbbh() != null) {
                    //存储事项对象在redis中
                    redisUtil.hset(StorageCacheKeyConstants.ITEM_CACHE_KEY, item.getSpsxbm() + ":" + item.getSplcbm(), item);
                } else {
                    //主题为空的从redis中查询
                    SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb = (SpglDfxmsplcjdsxxxb) redisUtil.hget(StorageCacheKeyConstants.ITEM_CACHE_KEY, item.getSpsxbm() + ":" + item.getSplcbm());
                    if (spglDfxmsplcjdsxxxb != null) {
                        item.setSplcbm(spglDfxmsplcjdsxxxb.getSplcbm());
                        item.setSplcbbh(spglDfxmsplcjdsxxxb.getSplcbbh());
                        item.setSpjdxh(spglDfxmsplcjdsxxxb.getSpjdxh());
                    } else {
                        //redis为空的从工改库查询事项阶段关联
                        SpglDfxmsplcjdxxb spglDfxmsplcjdxxb = itemBasicService.getAeaParThemeVerByItemCodeAndThemeCode(item);
                        if (spglDfxmsplcjdxxb == null) {
                            throw new ItemUnbindThemeException(item.getSpsxbm());
                        }
                        item.setSplcbm(spglDfxmsplcjdxxb.getSplcbm());
                        item.setSplcbbh(spglDfxmsplcjdxxb.getSplcbbh());
                        item.setSpjdxh(spglDfxmsplcjdxxb.getSpjdxh());
                        redisUtil.hset(StorageCacheKeyConstants.ITEM_CACHE_KEY, item.getSpsxbm() + ":" + item.getSplcbm(), item);
                    }
                }
            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_DFXMSPLCJDSXXXB);
    }

    /**
     * 辅线上传到事项信息表
     */
    private void importSubordinateItem(Date startTime, Date endTime) {
        new AbstractImporter<SpglDfxmsplcjdsxxxb>(stageSubordinateItemService, spglDfxmsplcjdsxxxbService) {
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_DFXMSPLCJDSXXXB);
    }

    private void importProj(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmjbxxb>(applyProjService, spglXmjbxxbService) {
            @Override
            protected void prepareHandle(SpglXmjbxxb item, Iterator<SpglXmjbxxb> iterator) {
                if (StringUtils.isBlank(item.getSplcbm())) {
                    throw new ProjUnbindThemeException(item.getGcdm());
                }
                if (item.getSplcbbh() != null) {
                    redisUtil.hset(StorageCacheKeyConstants.PROJ_CACHE_KEY, item.getGcdm(), item);
                } else {
                    SpglXmjbxxb spglXmjbxxb = (SpglXmjbxxb) redisUtil.hget(StorageCacheKeyConstants.PROJ_CACHE_KEY, item.getGcdm());
                    if (spglXmjbxxb != null) {
                        item.setSplcbm(spglXmjbxxb.getSplcbm());
                        item.setSplcbbh(spglXmjbxxb.getSplcbbh());
                    } else {
                        //从单项申报中获取申报的主题版本
                        boolean flag = true;
                        List<SpglDfxmsplcjdsxxxb> itemInstlist = itemBasicService.findItemByGcdm(item.getGcdm());
                        for (SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb : itemInstlist) {
                            spglDfxmsplcjdsxxxb.setSplcbm(item.getSplcbm());
                            SpglDfxmsplcjdxxb spglDfxmsplcjdxxb = itemBasicService.getAeaParThemeVerByItemCodeAndThemeCode(spglDfxmsplcjdsxxxb);
                            if (spglDfxmsplcjdxxb != null) {
                                item.setSplcbbh(spglDfxmsplcjdxxb.getSplcbbh());
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            item.setSplcbbh(1D);
                        }
                        redisUtil.hset(StorageCacheKeyConstants.PROJ_CACHE_KEY, item.getGcdm(), item);
                    }
                }
            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMJBXXB);
    }

    private void importUnit(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmdwxxb>(applyProjUnitService, spglXmdwxxbService) {
            @Override
            protected void prepareHandle(SpglXmdwxxb item, Iterator<SpglXmdwxxb> iterator) {
                validateProj(item);
            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMDWXXB);
    }

    private void importIteminst(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmspsxblxxb>(iteminstService, spglXmspsxblxxbService) {
            @Override
            protected void prepareHandle(SpglXmspsxblxxb item, Iterator<SpglXmspsxblxxb> iterator) {
                validateProj(item);
                if (StringUtils.isBlank(item.getSplcbm())) {
                    throw new ProjUnbindThemeException();
                }
                if (item.getSplcbbh() == null) {
                    SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb = (SpglDfxmsplcjdsxxxb) redisUtil.hget(StorageCacheKeyConstants.ITEM_CACHE_KEY, item.getSpsxbm() + ":" + item.getSplcbm());
                    if (spglDfxmsplcjdsxxxb == null) {
                        throw new ItemNotFindException(item.getSpsxbm());
                    }
                    item.setSplcbm(spglDfxmsplcjdsxxxb.getSplcbm());
                    item.setSplcbbh(spglDfxmsplcjdsxxxb.getSplcbbh());
                }
            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMSPSXBLXXB);
    }

    private void importSubordinateIteminst(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmspsxblxxb>(subordinateIteminstService, spglXmspsxblxxbService) {
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMSPSXBLXXB);
    }

    private void importItemOpinion(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmspsxblxxxxb>(itemOpinionService, spglXmspsxblxxxxbService) {
            @Override
            protected void prepareHandle(SpglXmspsxblxxxxb item, Iterator<SpglXmspsxblxxxxb> iterator) {
                validateProj(item);
                validateItmeinst(item);
            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMSPSXBLXXXXB);
    }

    private void importSubordinateItemOpinion(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmspsxblxxxxb>(subordinateItemOpinionService, spglXmspsxblxxxxbService) {
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMSPSXBLXXXXB);
    }

    private void importOfficDoc(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmspsxpfwjxxb>(officialDocService, spglXmspsxpfwjxxbService) {
            @Override
            protected void prepareHandle(SpglXmspsxpfwjxxb item, Iterator<SpglXmspsxpfwjxxb> iterator) {
                validateProj(item);
                validateItmeinst(item);
            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMSPSXPFWJXXB);
    }

    private void importItemMatinst(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmqtfjxxb>(itemOthersMatService, spglXmqtfjxxbService) {
            @Override
            protected void prepareHandle(SpglXmqtfjxxb item, Iterator<SpglXmqtfjxxb> iterator) {
                validateProj(item);
                validateItmeinst(item);
            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMQTFJXXB);
    }

    //项目用地红线界址信息表
    private void importLandRedLine(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmydhxjzxxb>(landRedLineService, spglXmydhxjzxxbService) {
            @Override
            protected void prepareHandle(SpglXmydhxjzxxb item, Iterator<SpglXmydhxjzxxb> iterator) {

            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMYDHXJZXXB);
    }

    //项目前期意见信息表
    private void importPreIdea(Date startTime, Date endTime) {
        new AbstractImporter<SpglXmqqyjxxb>(preIdeaService, spglXmqqyjxxbService) {
            @Override
            protected void prepareHandle(SpglXmqqyjxxb item, Iterator<SpglXmqqyjxxb> iterator) {

            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_XMQQYJXXB);
    }

    //地方规划控制线信息表
    private void importPlanControlLine(Date startTime, Date endTime) {
        new AbstractImporter<SpglDfghkzxxxb>(planControlLineService, spglDfghkzxxxbService) {
            @Override
            protected void prepareHandle(SpglDfghkzxxxb item, Iterator<SpglDfghkzxxxb> iterator) {

            }
        }.commonImport(startTime, endTime, TableNameConstant.SPGL_DFGHKZXXXB);
    }

    private void validateProj(SpglInstEntity item) {
        Object spglInstEntity = redisUtil.hget(StorageCacheKeyConstants.PROJ_CACHE_KEY, item.getGcdm());
        if (spglInstEntity == null) {
            throw new ProjNotFoundException(item.getGcdm());
        }
    }

    private void validateItmeinst(SpglItemInstEntity item) {
        boolean isSucc = stringRedisTemplate.opsForSet().isMember(TableNameConstant.SPGL_XMSPSXBLXXB, item.getSpsxslbm());
        if (!isSucc) {
            throw new IteminstNotFindException(item.getSpsxslbm());
        }
    }

    private abstract class AbstractImporter<T extends SpglEntity> {

        protected BaseUploadServer<T> baseUploadServer;

        protected BaseSpglServer<T> baseSpglServer;

        public AbstractImporter(BaseUploadServer<T> baseUploadServer, BaseSpglServer<T> baseSpglServer) {
            this.baseUploadServer = baseUploadServer;
            this.baseSpglServer = baseSpglServer;
        }

        protected int detailReadNum = 0;
        protected int detailErrorNum = 0;
        protected int detailWrittenNum = 0;

        protected void commonImport(Date startTime, Date endTime, String key) {
            detailReadNum = 0;
            detailErrorNum = 0;
            detailWrittenNum = 0;
            int i = 1;
            PageInfo<T> pageInfo;
            Set<String> localKeys = new HashSet<>();
            do {
                Page page = new Page(i, UPLOAD_LIMIT);
                pageInfo = baseUploadServer.listByTimeRange(startTime, endTime, page);
                List<T> list = pageInfo.getList();
                if (list != null && list.size() > 0) {
                    ThreadEtlJobLog.readNumAddNum(list.size());
                    detailReadNum = detailReadNum + list.size();
                    Iterator<T> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        T next = iterator.next();
                        try {
                            //this.checkComplete(next, iterator);
                            this.prepareHandle(next, iterator);
                        } catch (EtlTransException e) {
                            try {
                                ThreadEtlJobLog.increaseErrorNum();
                                detailErrorNum++;
                                etlErrorLogService.insertEtlErrorLogWithException(next, e, ThreadEtlJobLog.getJobLogId());
                                iterator.remove();
                            } catch (Exception v) {
                                log.error("向错误日志日志表插入数据发生错误，主键为：{}", next.getDfsjzj());
                                v.printStackTrace();
                            }
                        } catch (Exception o) {
                            o.printStackTrace();
                        }
                    }
                    for (T item : list) {
                        localKeys.add(item.getDfsjzj());
                    }
                    this.batchInsertAndLog(list);
                    if (localKeys != null && localKeys.size() > 0) {
                        String[] idArr = localKeys.toArray(new String[localKeys.size()]);
                        stringRedisTemplate.opsForSet().add(key, idArr);
                    }
                    i++;
                }
            } while (pageInfo.isHasNextPage());
            try {
                EtlJobDetailLog detailLog = new EtlJobDetailLog();
                detailLog.setJobLogId(ThreadEtlJobLog.getJobLogId());
                detailLog.setTableName(key);
                detailLog.setReadNum(Integer.toUnsignedLong(detailReadNum));
                detailLog.setErrorNum(Integer.toUnsignedLong(detailErrorNum));
                detailLog.setWrittenNum(Integer.toUnsignedLong(detailWrittenNum));
                Date now = new Date();
                detailLog.setCreateTime(now);
                etlJobDetailLogService.saveEtlJobDetailLog(detailLog);
            } catch (Exception e2) {
                log.error("保存上传详细错误日志出错，表名为：{}。上传日志主键为：{}", key, ThreadEtlJobLog.getJobLogId());
                e2.printStackTrace();
            }
        }

        protected void checkComplete(T next, Iterator<T> iterator) {
            List<ViewSubTable> subTableList = null;
            if (next instanceof SpglXmspsxblxxb) {
                SpglXmspsxblxxbView view = SpglXmspsxblxxbView.instance();
                subTableList = view.getViewSubTableList();
            } else if (next instanceof SpglDfxmsplcjdsxxxb) {
                SpglDfxmsplcjdfxsxxxbVew view = SpglDfxmsplcjdfxsxxxbVew.instance();
                subTableList = view.getViewSubTableList();
            }
            DataIncompleteException exception = null;
            if (subTableList != null && subTableList.size() > 0) {
                for (ViewSubTable subTable : subTableList) {
                    Set<String> fields = subTable.getFields();
                    for (String field : fields) {
                        Object value = ReflectionUtils.getFieldValue(next, field);
                        if (value == null) {
                            if (exception == null) {
                                exception = new DataIncompleteException();
                            }
                            exception.addUnrelatedTable(subTable.getTableName());
                            break;
                        }
                    }
                }
            }
            if (exception != null) {
                throw exception;
            }
        }

        /**
         * 数据前置处理，用于数据过滤，信息补全等
         *
         * @param item
         */
        protected void prepareHandle(T item, Iterator<T> iterator) {

        }

        protected void batchInsertAndLog(List<T> list) {
            Date now = new Date();
            for (T item : list) {
                item.setScsj(now);
                item.setSjsczt(0L);
            }
            if (uploadToAnalyse) {
                DataSourceType.setSmallType(DataSourceType.DataBaseType.ANALYSE);
                try {
                    baseSpglServer.batchInsert(list);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("batch insert error");
                    for (T item : list) {
                        try {
                            baseSpglServer.insert(item);
                        } catch (Exception v) {
                            v.printStackTrace();
                            log.error("上传出错，表名:{},地方数据主键为：{}", item.getStepName(), item.getDfsjzj());
                        }
                    }
                }
                DataSourceType.setSmallType(DataSourceType.DataBaseType.PROVINCE);
            }
            try {
                baseSpglServer.batchInsert(list);
                ThreadEtlJobLog.writtenNumAddNum(list.size());
                detailWrittenNum = detailWrittenNum + list.size();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("batch insert error");
            }
            for (T item : list) {
                try {
                    baseSpglServer.insert(item);
                    ThreadEtlJobLog.increaseWrittenNum();
                    detailWrittenNum++;
                } catch (Exception e) {
                    log.error("上传出错，表名:{},地方数据主键为：{}", item.getStepName(), item.getDfsjzj());
                    try {
                        ThreadEtlJobLog.increaseErrorNum();
                        detailErrorNum++;
                        etlErrorLogService.insertEtlErrorLog(item.getStepName(), item.getTableName(), item.getDfsjzj(), e, ThreadEtlJobLog.getJobLogId());
                    } catch (Exception e2) {
                        log.error("保存错误日志出错，地方数据主键为：{}", item.getDfsjzj());
                    }
                }
            }
        }
    }
}
