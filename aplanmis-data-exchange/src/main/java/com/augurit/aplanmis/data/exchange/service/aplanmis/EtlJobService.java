package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJob;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface EtlJobService {
    void saveEtlJob(EtlJob etlJob);

    void updateEtlJob(EtlJob etlJob);

    void deleteEtlJobById(String id);

    PageInfo<EtlJob> listEtlJob(EtlJob etlJob, Page page);

    EtlJob getEtlJobById(String id);

    List<EtlJob> listEtlJob(EtlJob etlJob);
}