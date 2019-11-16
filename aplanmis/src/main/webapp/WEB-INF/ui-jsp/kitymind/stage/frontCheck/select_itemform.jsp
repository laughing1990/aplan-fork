<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="select_itemform_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 70%;">
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
                        <div class="col-md-9" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7" style="text-align: right;">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input type="text" class="form-control m-input"
                                               placeholder="请输入关键字..."  value="" id = "select_itemform_keyword"/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                    </div>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info" id = "select_itemform_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id = "select_itemform_clear_btn">清空</button>
                                    <button type="button" class="btn btn-info" onclick="batchSaveFrontItemform();" id = "batch_save_front_itemform_btn">导入</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px;height: 450px;overflow-x: hidden;overflow-y: auto;">
                    <table id="select_itemform_tb">
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var select_itemform_tb;
    var stageItemId_id;
    var itemFormName_id;
    var formItemName_id;
    var frontItemformId_id;
    function openSelectParFrontItemform(stageItemId,itemFormName,formItemName,frontItemformId){
        stageItemId_id = stageItemId;
        itemFormName_id = itemFormName;
        formItemName_id = formItemName;
        frontItemformId_id = frontItemformId;

        $("#batch_save_front_itemform_btn").hide();
        $("#select_itemform_modal").modal("show");
        if(select_itemform_tb){
            select_itemform_tb.destroy();
        }
        initSelectItemformTable();
    }
    
    
    function initSelectItemformTable() {
        select_itemform_tb = defaultBootstrap("select_itemform_tb",[{
            field: "#",
            title: "#",
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: "stageItemId",
            visible: false
        }, {
            field: "itemName",
            title: "事项名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        },{
            field: "formName",
            title: "表单名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        },{
            field: '_operate',
            title: '操作',
            sortable: false,
            width: 100,
            textAlign: 'center',
            formatter: function (value, row, index) {
                var btn =  '<a href="javascript:selectParFrontItemform(\'' + row.stageItemId + '\',\''+row.itemName+'\',\''+row.formName+'\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="选择"><i class="la la-plus"></i></a>';
                return btn;
            }
        }],ctx + '/aea/par/front/itemform/listSelectParFrontItemformByPage.do');
        var select_itemform_tb_params = {stageId:currentBusiId};
        if(frontItemformId_id){
            select_itemform_tb_params['frontItemformId']= $("#"+frontItemformId_id).val();
        }
        select_itemform_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(select_itemform_tb_params).setRowId("stageItemId").setClearBtn("select_itemform_clear_btn").setSearchBtn("select_itemform_search_btn","select_itemform_keyword").init();
    }
    
    function selectParFrontItemform(stageItemId,itemName,formName) {
        if(stageItemId_id){
            $("#"+stageItemId_id).val(stageItemId);
        }
        if(formItemName_id){
            $("#"+formItemName_id).val(itemName);
        }
        if(itemFormName_id){
            $("#"+itemFormName_id).val(formName);
        }

        $("#select_itemform_modal").modal("hide");
    }

    function batchImprotFrontItemform(){
        $("#batch_save_front_itemform_btn").show();
        $("#select_itemform_modal").modal("show");
        if(select_itemform_tb){
            select_itemform_tb.destroy();
        }
        initCheckItemformTable();
    }


    function initCheckItemformTable() {
        select_itemform_tb = defaultBootstrap("select_itemform_tb",[{
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            checkbox: true
        }, {
            field: "stageItemId",
            visible: false
        }, {
            field: "itemName",
            title: "事项名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        },{
            field: "formName",
            title: "表单名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        }],ctx + '/aea/par/front/itemform/listSelectParFrontItemformByPage.do');
        var select_itemform_tb_params = {stageId:currentBusiId};
        select_itemform_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(select_itemform_tb_params).setRowId("stageItemId").setClearBtn("select_itemform_clear_btn").setSearchBtn("select_itemform_search_btn","select_itemform_keyword").init();
    }
    
    function batchSaveFrontItemform() {
        var itemforms = select_itemform_tb.getSelections();
        if(itemforms.length==0){
            swal('错误信息', '没有勾选事项表单!', 'error');
        }else{
            var stageItemIds = new Array();
            for(var i=0;i<itemforms.length;i++){
                stageItemIds.push(itemforms[i].stageItemId);
            }

            $("#uploadProgressMsg").html("批量导入事项表单，请稍后...");
            $("#uploadProgress").modal("show");

            $.ajax({
                url: ctx+'/aea/par/front/itemform/batchSaveAeaParFrontItemform.do',
                type:'post',
                data:{stageId:currentBusiId,stageItemIds:stageItemIds.join(",")},
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
        }
        
    }
</script>