//参数设置
function setStageItemMatParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    commonQueryParams['pageNum'] = pageNum,
    commonQueryParams['pageSize'] = params.limit;
    return commonQueryParams;
}

// 查询
function searchStageItemMat(){

    commonQueryParams = {};
    var t = $('#search_stage_item_mat_form').serializeArray();
    $.each(t, function() {
        commonQueryParams[this.name] = this.value;
    });
    commonQueryParams['stageId'] = stageId;
    $("#stage_rel_mat_cert_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#stage_rel_mat_cert_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchStageItemMat(){

    $('#search_stage_item_mat_form')[0].reset();
    searchStageRelMatCert();
}

// 设置事项材料
function setItemMatInfo(){

    $('#set_stage_item_mat_modal').modal('show');
    $('#set_stage_item_mat_modal_title').html('设置事项材料');
}

// 保存
function  saveStageItemMat(){

}

$(function(){

    $('#set_stage_item_mat_tb').bootstrapTable('resetView',{
        height: 450
    });

});