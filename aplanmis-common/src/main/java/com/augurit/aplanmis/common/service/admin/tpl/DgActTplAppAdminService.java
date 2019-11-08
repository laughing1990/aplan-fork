package com.augurit.aplanmis.common.service.admin.tpl;

import com.augurit.agcloud.bpm.common.domain.ActTplApp;
import com.augurit.agcloud.bpm.common.domain.ActTplAppFlowdef;

import com.augurit.aplanmis.common.vo.AppProcCaseDefPlusAdminVo;
import com.augurit.aplanmis.common.vo.AppProcCaseDefTreeVo;

import java.util.List;

public interface DgActTplAppAdminService {


    /**
     * 一键创建业务流程模板，关联可视范围，视图，表单，公共元素
     * @param appName 模板名称
     * @param isSeries 1 串联  0并联(阶段)
     * @return
     * @throws Exception
     */
    ActTplApp createActTplAppAllInfo(String appName, String isSeries) throws Exception;

    /**
     * 根据业务流程模板获取流程定义
     * @param appId  业务流程模板ID
     * @return
     * @throws Exception
     */
    List<ActTplAppFlowdef> getAppFlowDefList(String appId) throws Exception;


    /**
     * 新接口
     * @param appId  业务流程模板ID
     * @param searchKey  流程定义名称（defName）模糊查询字段
     * @return
     * @throws Exception
     */
    List<AppProcCaseDefPlusAdminVo> getAppProcCaseDefVo(String appId, String searchKey) throws Exception;

    /**
     * 获取流程父子关系树接口
     * @param appId  业务流程模板ID
     * @param stateVerId  情形版本id
     * @param searchKey  流程定义名称（defName）模糊查询字段
     * @param isPid  是否是通过pid构建树
     * @return
     * @throws Exception
     */
    List<AppProcCaseDefTreeVo> getAppProcCaseDefTreeVo(String appId,String stateVerId, String searchKey,boolean isPid) throws Exception;

    boolean syncActTplAppDefLimitTime(String appId, int limitTime, String timeUnit, String isWorkDay) throws Exception;
}
