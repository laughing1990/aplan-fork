<%@ page contentType="text/html;charset=UTF-8" %>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
                <h3 class="m-portlet__head-text">
                    情形材料列表
                </h3>
            </div>
        </div>
    </div>
    <div class="m-portlet__body" style="padding: 10px 5px;height:96%">
        <ul class="nav nav-tabs" role="tablist" style="margin-bottom: 10px;">
            <li class="nav-item">
                <a id="childStateInfo" class="nav-link active" data-toggle="tab"
                   href="#m_tabs_child_state" onclick="searchChildState();">
                    <i class="la la-gear"></i>
                    情形列表
                </a>
            </li>
            <li class="nav-item">
                <a id="matCertInfo" class="nav-link" data-toggle="tab"
                   href="#m_tabs_mat_cert" onclick="searchStateRelMatCert();">
                    <i class="la la-gear"></i>
                    材料列表
                </a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="m_tabs_child_state" class="tab-pane active" role="tabpanel">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="addStageState();">新增</button>
                            <button type="button" class="btn btn-secondary" onclick="batchDeleteChildState();">删除</button>
                            <button type="button" class="btn btn-secondary" onclick="refreshChildState();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <form id="search_child_state_form" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
										        <span><i class="la la-search"></i></span>
										    </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn btn-info" onclick="searchChildState();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchChildState();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="child_state_tb"
                            data-toggle="table",
                            data-click-to-select=true,
                            queryParamsType="",
                            data-pagination=true,
                            data-page-size="10",
                            data-side-pagination="server",
                            data-page-list="[10, 20, 50, 100]",
                            data-side-pagination="server",
                            data-query-params="childStateParam",
                            data-pagination-show-page-go="true",
                            data-url="${pageContext.request.contextPath}/aea/par/state/listAllChildStateEasyUiPage.do">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="left" data-width="10">ID</th>
                                <th data-field="stateName" data-align="left" data-width=230>情形名称</th>
                                <th data-field="useEl" data-align="center" data-width="60"
                                    data-formatter="useElFormatter">是否使用EL</th>
                                <th data-field="isQuestion" data-align="center" data-width="60"
                                    data-formatter="isQuestionFormatter">是否问题</th>
                                <th data-field="stateMemo" data-align="left" data-width="150">备注</th>
                                <th data-field="_operator2" data-formatter="childStateFormatter"
                                    data-align="center" data-width="100">操作</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>

            <div id="m_tabs_mat_cert" class="tab-pane" role="tabpanel">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="addStateMat();">新增材料</button>
                            <button type="button" class="btn btn-info" onclick="addGlobalMat();">导入全局材料</button>
                            <button type="button" class="btn btn-info" onclick="addStateCert();">导入证照</button>
                            <button type="button" class="btn btn-secondary" onclick="batchDeleteStateRelMatCert();">删除</button>
                            <button type="button" class="btn btn-secondary" onclick="refreshStateRelMatCert();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <form id="search_state_rel_mat_cert_form" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
										        <span><i class="la la-search"></i></span>
										    </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn btn-info"
                                            onclick="searchStateRelMatCert();">查询</button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="clearSearchStateRelMatCert();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="state_rel_mat_cert_tb"
                            data-toggle="table",
                            data-click-to-select=true,
                            queryParamsType="",
                            data-pagination=true,
                            data-page-size="10",
                            data-side-pagination="server",
                            data-page-list="[10, 20, 50, 100]",
                            data-side-pagination="server",
                            data-query-params="stateRelMatCertParam",
                            data-pagination-show-page-go="true",
                            data-url="${pageContext.request.contextPath}/aea/par/state/listInStateMatCertByStageIdAndStateId.do">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="left" data-width="10">ID</th>
                                <th data-field="aeaMatCertName" data-align="left" data-width="250">名称</th>
                                <th data-field="aeaMatCertCode" data-align="left" data-width="150">编号</th>
                                <th data-field="fileType" data-formatter="fileTypeFormatter"
                                    data-align="left" data-width=60>文件类型</th>
                                <th data-field="_operator" data-formatter="stateRelMatCertFormatter"
                                    data-align="center" data-width="100">操作</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>