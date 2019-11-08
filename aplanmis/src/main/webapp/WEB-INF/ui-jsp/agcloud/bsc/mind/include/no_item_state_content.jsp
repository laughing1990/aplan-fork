<%@ page contentType="text/html;charset=UTF-8" %>

<div id="mainContentPanel" class="row" style="width: 100%;">
    <div class="col-xl-12" style="padding: 0px;">
        <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <div class="m-portlet__body" style="padding: 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5 m--margin-top-10">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6" style="text-align: left;">
                            <button type="button" class="btn btn-info"
                                    onclick="addItemInMat();">新增材料</button>
                            <button type="button" class="btn btn-info"
                                    onclick="addItemInGlobalMat();">导入库材料</button>
                            <button type="button" class="btn btn-info"
                                    onclick="addItemInCert();">导入电子证照</button>
                            <button type="button" class="btn btn-info"
                                    onclick="addItemInForm();">导入表单</button>
                            <button type="button" class="btn btn-info"
                                    onclick="sortItemIn();">排序</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="batchDeleteItemInMatCert();">删除</button>
                            <button type="button" class="btn btn-secondary"
                                    onclick="refreshItemInMatCet();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-4"></div>
                                <div class="col-5" style="text-align: right;">
                                    <form id="search_item_in_mat_cert" method="post">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input type="text" class="form-control m-input" id="itemMatSearch"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                        <span><i class="la la-search"></i></span>
                                    </span>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info"
                                            onclick="searchItemInMatCert();">查询</button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="clearSearchItemInMatCert();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="item_in_mat_cert_tb"
                            data-toggle="table"
                            data-method="post"
                            data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                            data-click-to-select=true
                            data-pagination-detail-h-align="left"
                            data-pagination-show-page-go="true"
                            data-page-size="10"
                            data-page-list="[10,20,30,50,100]",
                            data-pagination=true
                            data-side-pagination="server"
                            data-pagination-detail-h-align="left"
                            data-query-params="itemInMatCertParam"
                            data-response-handler="itemInMatCertResponseData",
                            data-url="${pageContext.request.contextPath}/aea/item/inout/listAeaItemMatCertFormByPage.do">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="center" data-width="10">ID</th>
                                <th data-field="inoutId" data-visible="false"></th>
                                <th data-field="fileType" data-formatter="fileTypeFormatter"
                                    data-align="left" data-width=60>类型</th>
                                <th data-field="aeaMatCertName" data-align="left" data-width="250">名称</th>
                                <th data-field="aeaMatCertCode" data-align="left" data-width="250">编号</th>
                                <th data-field="sortNo" data-sortable="true" data-align="left"
                                    data-width="10" <%--data-formatter="sortNoFormatter"--%>>排序</th>
                                <th data-field="_operator" data-formatter="itemInMatCertFormatter"
                                    data-align="center" data-width="120">操作</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>

