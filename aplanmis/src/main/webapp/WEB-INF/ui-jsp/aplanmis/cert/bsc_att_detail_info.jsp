<%@ page contentType="text/html;charset=UTF-8" %>

<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
                <h3 id="type_rel_cert_list_title" class="m-portlet__head-text">
                    文件信息
                </h3>
            </div>
        </div>
    </div>

    <%--<div class="tab-pane" id="m_tabs_type_rel_cert" role="tabpanel">--%>
    <div class="m-portlet__body" style="padding: 10px 5px;">
        <div class="m-form m-form--label-align-right m--margin-bottom-5">
            <form id="bac_att_detail_form" method="post">
                <div class="col-md-6" style="text-align: left;">
                        <%--<button type="button" class="btn btn-accent" onclick="closedPage();">返回</button>--%>
                    <a href="#" class="btn btn-secondary m-btn m-btn--icon" onclick="closedPage();">
                        <span><i class="la la-angle-left"></i><span>返回</span></span>
                    </a>
                            <input type="file" id="file" name="uploadFile" class="form-control m-input"
                                   placeholder="" accept="*/*" multiple="true" style="display: none" />
                        <button type="button" class="btn  btn-success" onclick="fileClick();">新增文件</button>
                        <button type="button" class="btn btn-danger" onclick="batchDeleteBscAttDetailByIds();">删除</button>
                </div>
            </form>
            <div class="row" style="margin: 0px;">
                <%--<div class="col-md-8" style="padding: 0px;">--%>
                <%--<div class="row" style="margin: 0px;">--%>
                <%--<div class="col-8">--%>
                <%--<input type="file" id="uploadFile" name="uploadFile" class="form-control m-input" placeholder="" accept="*/*"   multiple="true">--%>
                <%--</div>--%>
                <%--<div class="col-2">--%>
                <%--<button type="button" class="btn  btn-info" onclick="addBscAttDetail();">新增文件</button>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
            </div>
            <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
            <div class="m-scrollable" data-scrollable="true" data-max-height="700" >
                <div id="bsc_att_detail_info_tb" class="m_datatable"></div>
            </div>
        </div>
    </div>
</div>
</div>
