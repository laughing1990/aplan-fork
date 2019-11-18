<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="select_partform_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 70%;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title">选择事项扩展表单</h5>
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
                                               placeholder="请输入关键字..."  value="" id = "select_partform_keyword"/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                                <span><i class="la la-search"></i></span>
                                            </span>
                                    </div>
                                </div>
                                <div class="col-3"  style="text-align: left;">
                                    <button type="button" class="btn btn-info" id = "select_partform_search_btn">查询</button>
                                    <button type="button" class="btn btn-secondary" id = "select_partform_clear_btn">清空</button>
                                    <button type="button" class="btn btn-info" onclick="batchSaveFrontPartform();" id = "batch_save_front_partform_btn">导入</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 10px;height: 450px;overflow-x: hidden;overflow-y: auto;">
                    <table id="select_partform_tb">
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var select_partform_tb;
    var itemPartformId_id;
    var partformName_id;
    var frontPartformId_id;
    var isSmartForm_name;
    function openSelectItemFrontPartform(itemPartformId,partformName,isSmartForm,frontPartformId){
        itemPartformId_id = itemPartformId;
        partformName_id = partformName;
        frontPartformId_id = frontPartformId;
        isSmartForm_name = isSmartForm;

        $("#batch_save_front_partform_btn").hide();

        $("#select_partform_modal").modal("show");
        if(select_partform_tb){
            select_partform_tb.destroy();
        }
        initSelectPartformTable();
    }


    function initSelectPartformTable() {
        select_partform_tb = defaultBootstrap("select_partform_tb",[{
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
            field: "itemPartformId",
            visible: false
        }, {
            field: "partformName",
            title: "事项扩展表单名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        },{
            field: "isSmartForm",
            title: "是否智能表单",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isSmartForm=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        },{
            field: '_operate',
            title: '操作',
            sortable: false,
            width: 100,
            textAlign: 'center',
            formatter: function (value, row, index) {
                var btn =  '<a href="javascript:selectItemFrontPartform(\'' + row.itemPartformId + '\',\''+row.partformName+'\',\''+row.isSmartForm+'\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="选择"><i class="la la-plus"></i></a>';
                return btn;
            }
        }],ctx + '/aea/item/front/partform/listSelectItemFrontPartformByPage.do');
        var select_partform_tb_params = {itemVerId:currentBusiId};
        if(frontPartformId_id){
            select_partform_tb_params['frontPartformId']= $("#"+frontPartformId_id).val();
        }
        select_partform_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(select_partform_tb_params).setRowId("itemPartformId").setClearBtn("select_partform_clear_btn").setSearchBtn("select_partform_search_btn","select_partform_keyword").init();
    }

    function selectItemFrontPartform(itemPartformId,partformName,isSmartForm) {
        if(itemPartformId_id){
            $("#"+itemPartformId_id).val(itemPartformId);
        }
        if(partformName_id){
            $("#"+partformName_id).val(partformName);
        }
        if(isSmartForm_name && isSmartForm){
            $("input[name='"+isSmartForm_name+"'][value='"+isSmartForm+"']").attr("checked",true);
        }

        $("#select_partform_modal").modal("hide");
    }

    function batchImprotFrontPartform(){
        $("#batch_save_front_partform_btn").show();
        $("#select_partform_modal").modal("show");
        if(select_partform_tb){
            select_partform_tb.destroy();
        }
        initCheckPartformTable();
    }


    function initCheckPartformTable() {
        select_partform_tb = defaultBootstrap("select_partform_tb",[{
            width: "10%",
            align: "center",
            sortable: false,
            textAlign: 'center',
            checkbox: true
        }, {
            field: "itemPartformId",
            visible: false
        },{
            field: "partformName",
            title: "事项扩展表单名称",
            textAlign: 'center',
            width: 500,
            textAlign: 'left',
            sortable: true
        },{
            field: "isSmartForm",
            title: "是否智能表单",
            textAlign: 'center',
            width: 50,
            textAlign: 'left',
            sortable: true,
            formatter: function (value, row, index) {
                if(row.isSmartForm=='1'){
                    return "是";
                }else{
                    return "否";
                }
            }
        }],ctx + '/aea/item/front/partform/listSelectItemFrontPartformByPage.do');
        var select_partform_tb_params = {itemVerId:currentBusiId};
        select_partform_tb.setPageSize(5).setPageList([5,10,20,50,100]).setQueryParams(select_partform_tb_params).setRowId("itemPartformId").setClearBtn("select_partform_clear_btn").setSearchBtn("select_partform_search_btn","select_partform_keyword").init();
    }

    function batchSaveFrontPartform() {
        var partforms = select_partform_tb.getSelections();
        if(partforms.length==0){
            swal('错误信息', '没有勾选事项表单!', 'error');
        }else{
            var itemPartformIds = new Array();
            for(var i=0;i<partforms.length;i++){
                itemPartformIds.push(partforms[i].itemPartformId);
            }

            $("#uploadProgressMsg").html("批量导入事项表单，请稍后...");
            $("#uploadProgress").modal("show");

            $.ajax({
                url: ctx+'/aea/item/front/partform/batchSaveAeaItemFrontPartform.do',
                type:'post',
                data:{itemVerId:currentBusiId,itemPartformIds:itemPartformIds.join(",")},
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
                            $("#select_partform_modal").modal("hide");
                            item_front_partform_tb.clear();

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