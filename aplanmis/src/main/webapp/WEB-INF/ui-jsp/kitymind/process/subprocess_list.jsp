<%@ page contentType="text/html;charset=UTF-8" %>


<div id="subprocess_list_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="subprocess_list_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 70%;width: 70%;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="subprocess_list_modal_title">子流程配置</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;max-height: 500px;height: 500px;overflow-y:auto;overflow-x: hidden">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6" style="padding: 0px;text-align: left;">
                            <button type="button"  class="btn btn-info" onclick="addSubProcess(0)">新增配置</button>
                            <button type="button" class="btn btn-danger" onclick="batchDelSubTrigger()">删除配置</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="subprocessKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchSubprocess();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSubprocessSearch();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="subprocess_list_tb">
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <%--<button type="button" class="btn btn-info" onclick="saveStateMatItem();">保存</button>--%>
                <%--<button type="button" class="btn btn-secondary"--%>
                <%--onclick="$('#view_state_mat_item_modal').modal('hide');">关闭</button>--%>
            </div>
        </div>
    </div>
</div>

<div id="subprocess_modal" class="modal fade" tabindex="4" role="dialog"
     aria-labelledby="createProcessDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="width:1000px;margin:0 auto;">
            <div class="modal-header" style="padding: 10px;height: 40px;">
                <h5 id="createProcessDialog_title" class="modal-title">子流程触发条件配置</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="text-align: center;">
                <form id="subProcForm">
                    <br/>
                    <div class="form-group m-form__group row" hidden>
                        <label class="col-3 col-form-label">
                            ID：
                        </label>
                        <div class="col-9">
                            <%--<input name="triggerId" class="form-control" type="text" id="triggerId" value=""/>--%>
                            <span id="triggerId"></span>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>触发的节点：
                        </label>
                        <div class="col-9">
                            <el-select v-model="triggerNodeId" filterable placeholder="请选择流程" style="width: 100%;">
                                <el-option
                                        v-for="item in nodeData"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                            <%--<select id="node" type="text" class="form-control" name="node">--%>

                            <%--</select>--%>

                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>是否外部流程：
                        </label>
                        <div class="col-9">
                            <div class="col-sm-10 d-flex" id="isOuterFlow">
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    <input ng-model="triggerEvent_task_create" value="0"  name="isOuterFlow" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;是&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span></span>
                                </label>
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    <input ng-model="triggerEvent_task_complete" value="1"  name="isOuterFlow" checked="checked" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;否&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span></span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>被触发的流程：
                        </label>
                        <div class="col-9">
                            <el-select v-model="triggerAppFlowdefId" filterable placeholder="请选择流程" style="width: 100%;">
                                <el-option
                                        v-for="item in flowData"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                            <%--<select id="triggerAppFlowdefId" type="text" class="form-control" name="triggerAppFlowdefId">--%>

                            <%--</select>--%>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>触发时机：
                        </label>
                        <div class="col-9">
                            <div class="col-sm-10 d-flex" id="chufa">

                            </div>
                        </div>
                    </div>

                    <%--<div class="form-group m-form__group row" hidden id="form_bus">--%>
                        <%--<label class="col-3 col-form-label">--%>
                            <%--关联事项：--%>
                        <%--</label>--%>
                        <%--<div class="col-9">--%>
                            <%--<select id="busRecordId" type="text" class="form-control" name="busRecordId">--%>

                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>

                    <div class="form-group m-form__group row dropdown" id="form_bus">
                        <label class="col-3 col-form-label">
                            关联事项：
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control m-input" readonly data-toggle="dropdown" id="busRecordName" name="busRecordName" value="" placeholder="请选择关联的事项"/>
                            <ul id="selectItemTreePanel" class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
                                <div style="padding: 0px 12px;" >
                                    <input type="text" id="itemSearchId" class="form-control m-input" placeholder="请输入事项名称查询"/>
                                </div>
                                <div id="selectItemTree" class="ztree" style="height: 300px;overflow: auto;"></div>
                            </ul>
                            <input type="hidden" name="busRecordId" id="busRecordId"/>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="padding: 10px">
                <button type="button" class="btn btn-default" onclick="saveSubTrigger()">保存</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>