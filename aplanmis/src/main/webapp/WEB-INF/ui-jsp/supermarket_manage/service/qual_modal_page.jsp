<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 资质列表弹窗 -->
<div class="modal fade" id="serviceQualRelated" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_add_item_under_dept_header">资质列表</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-12" style="padding: 0px;">
                                <form id="search_root_rel_qual_form" method="post">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-6">
                                            <button type="button" class="btn btn-info" onclick="addServiceQualRelated();">保存</button>

                                        </div>
                                        <div class="col-4" style="text-align: right;">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入资质名称或者资质编号" name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
													<span><i class="la la-search"></i></span>
												</span>
                                            </div>
                                        </div>
                                        <div class="col-2"  style="text-align: center;">
                                            <button type="button" class="btn btn-info"onclick="searchRootRelQual();">查询</button>
                                            <button type="button" class="btn btn-secondary"onclick="clearRootRelQual();">清空</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                    <div id="cert_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">

                                <div class="m-portlet__body" style="padding: 10px 5px;">

                                    <!-- 列表区域 -->
                                    <div class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;">
                                        <table id="root_qual_list_tb">
                                            <thead>
                                            <tr>
                                                <th data-field="#" data-checkbox="true" data-align="center" data-colspan="1" data-width="10"></th>
                                                <th data-field="qualId" data-align="left" data-colspan="1" data-width="200">资质Id</th>
                                                <th data-field="qualName" data-align="left" data-colspan="1" data-width="200">资质名称</th>
                                                <th data-field="qualCode" data-align="left" data-colspan="1" data-width="200">资质编码</th>
                                            </tr>
                                            </thead>
                                        </table>
                                    </div>
                                    <!-- 列表区域end -->
                                </div>

                            </div>
                        </div>
                    </div>
                    <!-- 列表区域end -->
                    <div class="modal-footer" style="padding: 10px;height: 60px;">
                        <button type="button" class="btn btn-secondary" onclick="configCertDialogClose();">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>