<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="select_itemform_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 950px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title">选择事项表单</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px 5px 0px;">

                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-4" style="text-align: left;">
                            <button id="batch_save_front_itemform_btn" type="button" class="btn btn-info">导入</button>&nbsp;
                            <button type="button" class="btn btn-secondary" onclick="closeSelectItemformDialog();">关闭</button>
                        </div>
                        <div class="col-md-8" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="select_itemform_keyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn btn-info" id="select_itemform_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id="select_itemform_clear_btn">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 开始 -->
                <div id="select_itemform_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table id="select_itemform_tb"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        $('#select_itemform_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#batch_save_front_itemform_btn').click(function () {

            batchSaveFrontItemform();
        });
    });

    function closeSelectItemformDialog(){

        $("#select_itemform_modal").modal("hide");
    }

    var select_itemform_tb;
    function openSelectParFrontItemform(){

        if(curIsEditable){

            $("#batch_save_front_itemform_btn").show();
            $("#select_itemform_modal").modal("show");
            if(select_itemform_tb){
                select_itemform_tb.destroy();
            }
            initSelectItemformTable();
        }else{
            swal('提示信息', '当前版本下数据不可编辑!', 'info');
        }
    }
    
    function initSelectItemformTable() {

        select_itemform_tb = defaultBootstrap("select_itemform_tb",[
            {
                checkbox: true,
            },
            {
                field: "isCatalog",
                title: "是否实施事项",
                width: 100,
                align: 'center',
                formatter: isCatalogFormatter
            },
            {
                field: "itemName",
                title: "配置事项",
                width: 350,
                align: 'left',
                formatter: itemNameFormatter
            },
            {
                field: "formName",
                title: "关联智能表单",
                width: 350,
                align: 'left',
            },
        ],ctx + '/aea/par/front/itemform/listSelectParFrontItemformByPage.do');
        var select_itemform_tb_params = {'stageId': currentBusiId};
        select_itemform_tb
            .setPageSize(5)
            .setPageList([5,10,20,50,100])
            .setQueryParams(select_itemform_tb_params)
            .setRowId("stageItemId")
            .setClearBtn("select_itemform_clear_btn")
            .setSearchBtn("select_itemform_search_btn","select_itemform_keyword")
            .init();
    }
    
    function batchSaveFrontItemform() {

        var rows = select_itemform_tb.getSelections();
        if (rows != null && rows.length > 0) {
            var ids = [];
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].stageItemId);
            }

            $("#uploadProgressMsg").html("批量导入事项表单，请稍后...");
            $("#uploadProgress").modal("show");

            $.ajax({
                url: ctx +'/aea/par/front/itemform/batchSaveAeaParFrontItemform.do',
                type:'post',
                data:{
                    'stageId': currentBusiId,
                    'stageItemIds': ids.toString()
                },
                async: false,
                dataType: 'json',
                success: function(result){

                    if (result.success) {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '批量导入成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            $("#select_itemform_modal").modal("hide");
                            par_front_itemform_tb.clear();

                        },500);
                    } else {
                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(){
                    setTimeout(function(){
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', '批量导入事项表单异常!', 'error');
                    },500);
                }
            });
        } else {
            swal('错误信息', '没有勾选事项表单!', 'error');
        }
    }
</script>