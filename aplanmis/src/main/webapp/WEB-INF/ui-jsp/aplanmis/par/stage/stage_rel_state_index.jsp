<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
                <h3 id="stage_rel_state_title" class="m-portlet__head-text">
                    情形列表
                </h3>
            </div>
        </div>
    </div>
    <div class="m-portlet__body" style="padding: 10px 0px;height:96%">
        <div class="m-form m-form--label-align-right m--margin-bottom-5">
            <div class="row" style="margin: 0px;">
                <div class="col-md-6"style="text-align: left;">
                    <button type="button" class="btn btn-info" onclick="addStageState();">新增</button>
                    <button type="button" class="btn btn-secondary" onclick="batchDeleteStageState();">删除</button>
                    <button type="button" class="btn btn-secondary" onclick="refreshStageState();">刷新</button>
                </div>
                <div class="col-md-6" style="padding: 0px;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-2"></div>
                        <div class="col-6">
                            <form id="search_stage_rel_state_form" method="post">
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
                            <button type="button" class="btn btn-info" onclick="searchStageState();">查询</button>
                            <button type="button" class="btn btn-secondary" onclick="clearSearchStageState();">清空</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
        <!-- 列表区域 -->
        <div class="base" style="padding: 0px 5px;">
            <table  id="stage_rel_state_tb"
                    data-toggle="table",
                    data-click-to-select=true,
                    queryParamsType="",
                    data-pagination=true,
                    data-page-size="10",
                    data-side-pagination="server",
                    data-page-list="[10, 20, 50, 100]",
                    data-side-pagination="server",
                    data-query-params="stageRelStateParam",
                    data-pagination-show-page-go="true",
                    data-url="${pageContext.request.contextPath}/aea/par/state/listAeaParStateEasyUiPage.do">
                <thead>
                    <tr>
                        <th data-field="#" data-checkbox="true" data-align="left" data-width="10">ID</th>
                        <th data-field="stateName" data-align="left" data-width=230>情形名称</th>
                        <th data-field="useEl" data-align="center" data-width="60"
                            data-formatter="useElFormatter">是否使用EL</th>
                        <th data-field="isQuestion" data-align="center" data-width="60"
                            data-formatter="isQuestionFormatter">是否问题</th>
                        <th data-field="stateMemo" data-align="left" data-width="150">备注</th>
                        <th data-field="_operator" data-formatter="stageRelStateFormatter"
                            data-align="center" data-width="100">操作</th>
                    </tr>
                </thead>
            </table>
        </div>
        <!-- 列表区域end -->
    </div>
</div>