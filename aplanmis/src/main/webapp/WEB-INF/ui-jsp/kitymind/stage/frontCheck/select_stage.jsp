<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="select_stage_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 70%;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title">选择阶段</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px 5px 0px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-9" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7" style="text-align: right;">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input type="text" class="form-control m-input"
                                               placeholder="请输入关键字..."  value="" id = "select_stage_keyword"/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                    </div>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info" id = "select_stage_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id = "select_stage_clear_btn">清空</button>
                                    <button type="button" class="btn btn-info" onclick="batchSaveFrontStage();" >导入</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px;height: 450px;overflow-x: hidden;overflow-y: auto;">
                    <table id="select_stage_tb">
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var select_stage_tb;

    function batchImprotParFrontStage() {
        $("#select_stage_keyword").val("");
        $("#select_stage_modal").modal("show");
        if(select_stage_tb){
            select_stage_tb.destroy();
        }
        initStageTable();
    }


    function initStageTable() {
        select_stage_tb = defaultBootstrap("select_stage_tb",[{
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            checkbox: true
        }, {
            field: "histStageId",
            visible: false
        }, {
            field: "histStageName",
            title: "阶段名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }],ctx + '/aea/par/front/stage/listSelectParFrontStageByPage.do');
        var select_stage_tb_params = {stageId:currentBusiId};
        select_stage_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(select_stage_tb_params).setRowId("histStageId").setClearBtn("select_stage_clear_btn").setSearchBtn("select_stage_search_btn","select_stage_keyword").init();
    }

    function batchSaveFrontStage() {
        var stages = select_stage_tb.getSelections();
        if(stages.length==0){
            swal('错误信息', '没有勾选阶段!', 'error');
        }else{
            var stageIds = new Array();
            for(var i=0;i<stages.length;i++){
                stageIds.push(stages[i].histStageId);
            }

            $("#uploadProgressMsg").html("批量导入阶段，请稍后...");
            $("#uploadProgress").modal("show");

            $.ajax({
                url: ctx+'/aea/par/front/stage/batchSaveAeaParFrontStage.do',
                type:'post',
                data:{stageId:currentBusiId,histStageIds:stageIds.join(",")},
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
                        swal('错误信息', '批量导入阶段异常!', 'error');
                    },500);
                }
            });
        }
    }
</script>