<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="add_ItemForm_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_ItemForm_modal_title">导入智能表单</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5" style="margin-top: 0%;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;"></div>
                        <div class="col-md-6" style="padding: 0px;">
                            <form id="search_all_sto_form" method="post">

                                <input type="hidden" id="stageItemId" value=""/>
                                <input type="hidden" id="itemVerId" value=""/>

                                <div class="row" style="margin: 0px;">
                                    <div class="col-7">
                                        <div class="m-input-icon m-input-icon--left">
                                            <input id="stoFormKeyword" type="text" class="form-control m-input"
                                                   placeholder="请输入关键字..." name="keyword" value=""/>
                                            <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="col-5">
                                        <button type="button" class="btn btn-info" onclick="searchStoFormList();">查询</button>
                                        <button type="button" class="btn btn-secondary" onclick="clearSearchStoFormList();">清空</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="add_stoform_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table id="selectAllStoFormTable"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>