package com.augurit.aplanmis.supermarket.evaluation.service;

import com.augurit.aplanmis.common.domain.AeaImServiceEvaluation;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:thinkpad</li>
    <li>创建时间：2019-06-03 13:59:35</li>
</ul>
*/
public interface AeaImServiceEvaluationService {

    void saveAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception;

    public void updateAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception;

    void deleteAeaImServiceEvaluationById(String id) throws Exception;


    public PageInfo<AeaImServiceEvaluation> listAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation, Page page) throws Exception;

    AeaImServiceEvaluation getAeaImServiceEvaluationById(String id) throws Exception;


    public List<AeaImServiceEvaluation> listAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception;

}
