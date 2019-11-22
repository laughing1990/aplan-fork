<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="select_stage_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 950px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_stage_modal_title">选择主/辅线</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-4" style="text-align: left;">
                            <button id="batch_save_front_stage_btn" type="button" class="btn btn-info">导入</button>&nbsp;
                            <button type="button" class="btn btn-secondary" onclick="closeSelectStageDialog();">关闭</button>
                        </div>
                        <div class="col-md-8" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-2"></div>
                                <div class="col-6">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="select_stage_keyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <button type="button" class="btn btn-info" id = "select_stage_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id = "select_stage_clear_btn">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 开始 -->
                <div id="select_stage_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table id="select_stage_tb"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

       $('#select_stage_tb_scroll').niceScroll({

           cursorcolor: "#e2e5ec",//#CC0071 光标颜色
           cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
           cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
           touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
           cursorwidth: "4px", //像素光标的宽度
           cursorborder: "0", //   游标边框css定义
           cursorborderradius: "2px",//以像素为光标边界半径
           autohidemode: true  //是否隐藏滚动条
       });

        $('#batch_save_front_stage_btn').click(function () {

            batchSaveFrontStage();
        });

    });

    function closeSelectStageDialog(){

        $('#select_stage_modal').modal('hide');
    }

    var select_stage_tb;
    function batchImprotParFrontStage() {

        if(curIsEditable){

            $("#select_stage_keyword").val("");
            $("#select_stage_modal").modal("show");
            if(select_stage_tb){
                select_stage_tb.destroy();
            }
            initStageTable();
        }else{
            swal('提示信息', '当前版本下数据不可编辑!', 'info');
        }
    }


    function initStageTable() {

        select_stage_tb = defaultBootstrap("select_stage_tb",[
            {
                checkbox: true,
            },
            {
                field: "histStageName",
                title: "前置阶段名称",
                width: 350,
                align: 'left',
            },
            {
                field: "histStageCode",
                title: "前置阶段编号",
                width: 300,
                align: 'left',
            },
            {
                field: "histIsNode",
                title: "是否主线",
                width: 80,
                align: 'center',
                formatter: function (value, row, index) {

                    if(value){
                        if(value=='1'){

                            return '主线';
                        }else if(value=='2'){

                            return '辅线';
                        }else if(value=='3'){

                            return '技术审查线';
                        }
                    }
                }
            },
        ],ctx + '/aea/par/front/stage/listSelectParFrontStageByPage.do');
        var select_stage_tb_params = {
            'stageId': currentBusiId
        };
        select_stage_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(select_stage_tb_params).setRowId("histStageId").setClearBtn("select_stage_clear_btn").setSearchBtn("select_stage_search_btn","select_stage_keyword").init();
    }

    function batchSaveFrontStage() {

        var rows = select_stage_tb.getSelections();
        if(rows!=null&&rows.length>0){
            var ids = [];
            for(var i=0;i<rows.length;i++){
                ids.push(rows[i].histStageId);
            }
            $("#uploadProgressMsg").html("批量导入前置检测主|辅线中，请稍后...");
            $("#uploadProgress").modal("show");

            $.ajax({
                url: ctx +'/aea/par/front/stage/batchSaveAeaParFrontStage.do',
                type:'post',
                data:{
                    'stageId': currentBusiId,
                    'histStageIds': ids.toString()
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
                            $("#select_stage_modal").modal("hide");
                            par_front_stage_tb.clear();

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
                        swal('错误信息', '批量导入主|辅线异常!', 'error');
                    },500);
                }
            });
        }else{
            swal('错误信息', '请选择前置检测主|辅线!', 'error');
        }
    }
</script>