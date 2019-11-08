<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_item_form_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_item_form_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_item_form_modal_title">导入表单</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5" style="margin-top: 0%;">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;"></div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="itemFormKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchItemForm();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSearchItemForm();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="item_form_list_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="item_form_list_tb"
                                data-toggle="table",
                                data-method="post",
                                data-pagination=true
                                data-page-size="10",
                                data-page-list="[10]",
                                data-click-to-select=true,
                                data-side-pagination="server",
                                data-query-params="itemFormParam",
                                data-pagination-show-page-go="true",
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8",
                                data-url="${pageContext.request.contextPath}/aea/item/state/form/listItemNoSelectFormByPage.do">
                            <thead>
                                <tr>
                                    <th data-field="#" data-checkbox="true" data-align="center" data-width="10">ID</th>
                                    <th data-field="formCode" data-align="left" data-width="250">表单编号</th>
                                    <th data-field="formName" data-align="left" data-width="250">表单名称</th>
                                    <th data-field="formProperty" data-align="left" data-width="100" data-formatter="formPropertyFormatter">表单类型</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <!-- 列表区域end -->
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="save_item_form_btn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#view_item_form_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        $('#item_form_list_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#save_item_form_btn').click(function(){

            var rows = $("#item_form_list_tb").bootstrapTable('getSelections');
            if(rows!=null&&rows.length>0) {
                var formIds = [];
                for (var i = 0; i < rows.length; i++) {
                    formIds.push(rows[i].formId);
                }
                $.ajax({
                    url: ctx + '/aea/item/state/form/saveAeaItemStateFormsAndNotDelOld.do',
                    type: 'POST',
                    data:  {
                        "itemVerId": currentBusiId,
                        "itemStateVerId": currentStateVerId,
                        "isStateForm": '0',
                        "formIds": formIds.toString(),
                    },
                    async: false,
                    success: function (result) {
                        if (result.success) {
                            $('#view_item_form_modal').modal('hide');
                            swal({
                                text: '保存成功！',
                                type: 'success',
                                timer: 1500,
                                showConfirmButton: false
                            });
                            // 刷新材料证照列表
                            searchItemInMatCert();
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }else{
                swal('提示信息', "请选择数据！", 'info');
            }
        });
    });

    // 表单属性，meta-biz表示元数据普通表单，meta-flow表示元数据流程表单，smart-biz表示智能普通表单，smart-flow表示智能流程表单
    function formPropertyFormatter(value, row, index, field) {

        if(value){
            if(value=='meta-biz'){
                return '元数据普通表单';
            }else if(value=='meta-flow'){
                return '元数据流程表单';
            }else if(value=='smart-biz'){
                return '智能普通表单';
            }else if(value=='smart-flow'){
                return '智能流程表单';
            }
        }
    }

    //参数设置
    function itemFormParam(params) {

        var pageNum = (params.offset / params.limit) + 1;
        var pagination = {
            page: pageNum,
            pages: pageNum,
            perpage: params.limit
        };
        var sort = {
            field: params.sort,
            sort: params.order
        };
        var queryParam = {
            pagination: pagination,
            sort: sort
        };
        //组装查询参数
        var buildParam = {};
        if (commonQueryParams) {
            for (var i = 0; i < commonQueryParams.length; i++) {
                buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;
            }
            queryParam = $.extend(queryParam, buildParam);
        }
        return queryParam;
    }

    // 查询
    function searchItemForm(){

        commonQueryParams = [];
        commonQueryParams.push({'name': 'itemVerId','value': currentBusiId});
        commonQueryParams.push({'name': 'itemStateVerId','value': currentStateVerId});
        commonQueryParams.push({'name': 'keyword','value': $('#itemFormKeyword').val()});
        //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#item_form_list_tb").bootstrapTable('selectPage',1);
    }

    // 清空
    function clearSearchItemForm(){

        $('#itemFormKeyword').val('')
        searchItemForm();
    }

</script>