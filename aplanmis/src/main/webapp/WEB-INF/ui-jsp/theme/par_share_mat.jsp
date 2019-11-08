<%@ page contentType="text/html;charset=UTF-8" %>

<div id="show_share_mat_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true" style="overflow-x: hidden;overflow-y: auto;">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 1300px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="show_share_mat_modal_title">材料共享</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 5px;">
                <div id="mainContentPanel" class="row" style="width: 100%;height: 99%; padding: 5px 0px; margin: 0px;">
                    <div class="col-xl-6" style="padding: 0px 2px 0px 5px;">
                        <div id="westPanel" class="m-portlet" style="margin-bottom: 5px;">
                            <div class="m-portlet__head">
                                <div class="m-portlet__head-caption">
                                    <div class="m-portlet__head-title">
                                        <span class="m-portlet__head-icon m--hide">
                                            <i class="la la-gear"></i>
                                        </span>
                                        <h3 class="m-portlet__head-text">
                                            事项输出&nbsp;&nbsp;
                                        </h3>
                                    </div>
                                </div>
                            </div>
                            <div class="m-portlet__body" style="padding: 10px 0px;">
                                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-md-12" style="padding: 0px;">
                                            <form id="search_all_out_item_list_form" method="post">
                                                <div class="row" style="margin: 0px;">
                                                    <div class="col-12">
                                                        <div class="form-group m-form__group row" >
                                                            <label class="col-lg-2 col-form-label" style="text-align: left;">事项选择:</label>
                                                            <div class="col-lg-8 input-group" style="margin-left:-20px">
                                                                <input type="hidden" name="outItemVerId" id="outItemVerId" value=""/>
                                                                <input type="text" class="form-control m-input" name="outItemName"
                                                                       placeholder="请点击选择" aria-describedby="select_item_Id" readonly
                                                                       onclick="isSelectOutItem(this, true);">
                                                                <div class="input-group-append">
                                                                    <span id="select_item_Id" class="input-group-text" onclick="isSelectOutItem(null, true);">
                                                                        <i class="la la-group"></i>
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                <div id="all_out_mat_list_tb_div" style="height:400px;overflow-y:auto;overflow-x:auto;" <%--class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;"--%>>
                                    <table id="all_out_mat_list_tb"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xl-6" style="padding: 0px 2px 0px 5px;">
                        <div class="m-portlet" style="margin-bottom: 5px;">
                            <div class="m-portlet__head">
                                <div class="m-portlet__head-caption">
                                    <div class="m-portlet__head-title">
                                        <span class="m-portlet__head-icon m--hide">
                                            <i class="la la-gear"></i>
                                        </span>
                                        <h3 class="m-portlet__head-text">
                                            事项输入
                                        </h3>
                                    </div>
                                </div>
                            </div>
                            <div class="m-portlet__body" style="padding: 10px 0px;">
                                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-md-12" style="padding: 0px;">
                                            <form id="search_in_item_list_form" method="post">
                                                <div class="row" style="margin: 0px;">
                                                    <div class="col-12">
                                                        <div class="form-group m-form__group row" >
                                                            <label class="col-lg-2 col-form-label" style="text-align: left;">事项选择:</label>
                                                            <div class="col-lg-8 input-group" style="margin-left:-20px">
                                                                <input type="hidden" name="inItemVerId" id="inItemVerId" value=""/>
                                                                <input type="hidden" name="inItemStateVerId" id="inItemStateVerId" value=""/>
                                                                <input type="text" class="form-control m-input" name="inItemName"
                                                                       placeholder="请点击选择" aria-describedby="select_in_item_Id" readonly
                                                                       onclick="isSelectInItem(this, true);">
                                                                <div class="input-group-append">
                                                                    <span id="select_in_item_Id" class="input-group-text" onclick="isSelectInItem(null, true);">
                                                                        <i class="la la-group"></i>
                                                                    </span>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-2"  style="text-align: right;">
                                                                <button id="saveShareBtn" type="button" class="btn btn-info" onclick="saveSharemat();">保存</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                <!-- 列表区域 -->
                                <div id="all_in_mat_list_tb_div" style="height: 400px;overflow-y:auto;overflow-x:auto;"<%--class="m-portlet__body iframe-content miniScrollbar" style="padding: 0px 5px;"--%>>
                                    <table id="all_in_mat_list_tb"></table>
                                </div>
                                <!-- 列表区域end -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>