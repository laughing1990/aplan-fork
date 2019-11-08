<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 配置资质证书列表弹窗 -->
<div class="modal fade" id="dialog_config_qual_cert" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_config_qual_cert_header">配置资质证书</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-12" style="padding: 0px;">
                                <form id="search_org_rel_item_form" method="post">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-6">
                                            <button type="button" class="btn btn-info" onclick="saveConfigCert();">保存</button>
                                            <button type="button" id="viewCheckedCertBtn" class="btn btn-secondary" onclick="viewCheckedCert();">查看已勾选</button>
                                        </div>
                                        <div class="col-4" style="text-align: right;">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input id="certKeyword" type="text" class="form-control m-input"
                                                       placeholder="请输入证照名称或者证照编号" name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
													<span><i class="la la-search"></i></span>
												</span>
                                            </div>
                                        </div>
                                        <div class="col-2"  style="text-align: center;">
                                            <button type="button" class="btn btn-info"onclick="searchCertList();">查询</button>
                                            <button type="button" class="btn btn-secondary"onclick="clearSearchCertList();">清空</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                        <div id="cert_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                            <div class="base" style="padding: 0px 5px;">
                                <table  id="cert_list_tb"
                                        data-toggle="table",
                                        data-click-to-select=true,
                                        queryParamsType="",
                                        data-pagination=true,
                                        data-page-size="10",
                                        data-page-list="[10]",
                                        data-side-pagination="server",
                                        data-query-params="queryCertParam",
                                        data-response-handler="queryCertResponseData"
                                        data-pagination-show-page-go="true",
                                        data-url="${pageContext.request.contextPath}/aea/bus/cert/listQualCert.do">
                                    <thead>
                                    <tr>
                                        <th data-field="" data-checkbox="true"
                                            data-align="center" data-colspan="1"
                                            data-width="20" data-formatter="checkAllCertId"></th>
                                        <th data-field="certName" data-align="left" data-width=180>证照证件名称</th>
                                        <th data-field="certCode" data-align="left" data-width=180>证照证件编号</th>
                                        <th data-field="typeName" data-align="left" data-width="200">证照类型</th>
                                        <th data-field="attDirName" data-align="left" data-width=180>所属文件库</th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    <!-- 列表区域end -->
                    <%--<div class="modal-footer" style="padding: 10px;height: 60px;">--%>
                        <%--<button type="button" class="btn btn-secondary" onclick="configCertDialogClose();">关闭</button>--%>
                    <%--</div>--%>
                </div>
            </div>
        </div>
    </div>
</div>