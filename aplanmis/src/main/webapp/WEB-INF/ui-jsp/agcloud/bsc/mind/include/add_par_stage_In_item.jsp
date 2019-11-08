<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="add_stage_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_stage_item_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 800px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_stage_item_modal_title">关联材料对应事项</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <input type="hidden" id = "stageItemId" name = "stageItemId"/>
                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">
                        <font style="color:red">*</font>关联事项:
                    </label>
                    <div class="col-lg-9">
                        <select id="item_select" class="form-control" onchange="changeItem();"></select>
                    </div>
                    <div class="col-lg-1"></div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="form-group m-form__group row">
                    <div class="col-lg-5"></div>
                    <div class="col-lg-4">
                        <input id="InName_keyword" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-lg-3">
                        <button type="button" class="btn btn-info"
                                onclick="parInsearch()">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearParInsearch()">清空</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="base table-responsive" style="height: 500px;margin-top: -20px;">
                    <div id="par_stage_item_table" class="m_datatable"></div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="button" class="btn btn-info" onclick="saveStateItemIn();">保存</button>
                    <button type="button" class="btn btn-secondary"
                            onclick="$('#add_stage_item_modal').modal('hide');">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">


    var allCheckedId = new Array();

    function addStageItem(stageId){
        allCheckedId = new Array();
        stageIdVal = stageId;
        globalStageId = stageId;
        $("#add_stage_item_modal").modal("show");
        $("#item_select").html("");

        $.ajax({
            url: ctx + '/aea/par/stage/item/listAeaParStageItemWithoutBindState.do?stageId='+stageIdVal,
            type: 'POST',
            data: {},
            async: false,
            success: function (result) {
                if (result) {
                    if(result.length>0){
                        var options = "";
                        for(var i=0;i<result.length;i++){
                            options +="<option value ='"+result[i].stageItemId+"'>"+result[i].itemName+"</option>";
                        }
                    }
                    $("#item_select").html(options);
                    aeaParStageItemDatatable.init();
                    aeaParStageItemDatatable.getDatatable();
                } else {
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function () {
                swal('错误信息', '获取阶段事项失败！', 'error');
            }
        });
    }

    //材料列表展示
    var aeaParStageItemDatatable = function () {
        var stageItemDatatable;
        var getStageItemDatatable = function () {
            return stageItemDatatable;
        };
        var stageItemDatatableInit = function () {
            var stageItemId = $("#item_select option:selected").val();
            var keyword = $("#InName_keyword").val();
            console.log(keyword);
            if (stageItemDatatable != null) stageItemDatatable.bootstrapTable("destroy");
            stageItemDatatable = $('#par_stage_item_table').bootstrapTable({
                url:ctx + '/aea/par/in/listParInByStageItemId.do',
                columns: [
                    {
                        checkbox: true,
                        align: "center",
                        formatter: function (value,row,index) {
                            if (row.check == true) {

                                allCheckedId.push(row.inId);
                                return {
                                    checked: true//设置选中
                                };
                            }
                            return value;
                        }
                    }, {
                        field: "isCheck",
                        visible: false
                    }, {
                        field: "#",
                        title: "#",
                        width: "30%",
                        align: "center",
                        sortable: false,
                        textAlign: 'center',
                        formatter: function (value, row, index) {
                            return index + 1;
                        }
                    }, {
                        field: "aeaMatCertName",
                        title: "材料名称",
                        align: "center",
                        width: "50%",
                    }, {
                        field: "fileType",
                        title: "材料类别",
                        align: "center",
                        width: "50%",
                    }
                    // , {
                    //     field: "isCommon",
                    //     title: "是否通用",
                    //     align: "center",
                    //     width: "50%",
                    //     formatter: function (value, row, index) {
                    //         if("1"==row.isCommon){
                    //             return "是";
                    //         }else{
                    //             return "否";
                    //         }
                    //     }
                    // }
                ],
                sortOrder: "asc",
                pagination: false,
                queryParams: {stageId: currentBusiId, stageItemId: stageItemId,keyword:keyword}
            });

            stageItemDatatable.on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
                var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
                checkIn(e.type, datas);                                 // 保存到全局 Set() 里
            });
        };

        return {
            init: stageItemDatatableInit,
            getDatatable: getStageItemDatatable,
        };
    }();

    //切换事项的时候材料列表的变化
    function changeItem() {
        aeaParStageItemDatatable.init();
        aeaParStageItemDatatable.getDatatable();
    }

    //保存材料和事项的关系
    function saveStateItemIn() {

        var stageItemId = $("#item_select").val();
        console.log(stageItemId);
        if(stageItemId==null || stageItemId==''){
            swal('错误信息', '请选择事项！', 'error');
            return;
        }

        // var checkBoxs = $("#par_stage_item_table").bootstrapTable('getSelections');
        // var ids = [];
        // if(checkBoxs!=null&&checkBoxs.length>0) {
        //     for (var i = 0; i < checkBoxs.length; i++) {
        //         ids.push(checkBoxs[i].inId);
        //     }
        //     $.ajax({
        //         url: ctx + '/aea/par/stage/item/in/batchSaveStageItemInByStageItemId.do',
        //         type: 'POST',
        //         data: {"stageItemId": stageItemId, "inIds": ids.toString()},
        //         async: false,
        //         success: function (result) {
        //             if (result.success) {
        //                 $("#add_stage_item_modal").modal("hide");
        //                 // location.reload();
        //                 agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
        //                 refresh();
        //             } else {
        //                 swal('错误信息', result.message, 'error');
        //             }
        //         },
        //         error: function () {
        //             swal('错误信息', '保存失败！', 'error');
        //         }
        //     });
        // }else{
        //     swal('错误信息', '请选择材料！', 'error');
        // }

        var ids = [];
        if(allCheckedId!=null&&allCheckedId.length>0) {
            for (var i = 0; i < allCheckedId.length; i++) {
                ids.push(allCheckedId[i]);
            }
            $.ajax({
                url: ctx + '/aea/par/stage/item/in/batchSaveStageItemInByStageItemId.do',
                type: 'POST',
                data: {"stageItemId": stageItemId, "inIds": ids.toString()},
                async: false,
                success: function (result) {
                    if (result.success) {
                        $("#add_stage_item_modal").modal("hide");
                        // location.reload();
                        agcloud.ui.metronic.showSuccessTip('保存成功！', 1500);
                        refresh();
                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '保存失败！', 'error');
                }
            });
        }else{
            swal('错误信息', '请选择材料！', 'error');
        }

    }

    function parInsearch() {
        aeaParStageItemDatatable.init();
        aeaParStageItemDatatable.getDatatable();
    }

    function clearParInsearch() {
        $('#InName_keyword').val("");
        aeaParStageItemDatatable.init();
        aeaParStageItemDatatable.getDatatable();
    }


    function checkIn(type,datas) {

        if(type.indexOf('uncheck')==-1){
            $.each(datas,function(i,v){
                if(allCheckedId.indexOf(v.inId) == -1){
                    allCheckedId.push(v.inId);
                }
            });
        }else{
            $.each(datas,function(i,v){
                var j = allCheckedId.indexOf(v.inId);
                allCheckedId.splice(j,1);    //删除取消选中行
            });
        }
    }

</script>