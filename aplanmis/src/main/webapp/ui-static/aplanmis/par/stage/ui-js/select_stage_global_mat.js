//参数设置
function selectStageGlobalMatParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    commonQueryParams['pageNum'] = pageNum,
    commonQueryParams['pageSize'] = params.limit;
    return commonQueryParams;
}

// 查询
function searchStageGlobalMat(){

    commonQueryParams = {};
    var t = $('#search_stage_global_mat_form').serializeArray();
    $.each(t, function() {
        commonQueryParams[this.name] = this.value;
    });
    commonQueryParams['stageId'] = stageId;
    $("#select_stage_global_mat_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#select_stage_global_mat_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchStageGlobalMat(){

    $('#search_stage_global_mat_form')[0].reset();
    searchStageGlobalMat();
}

// 保存情形选择的全局材料数据
function saveStageGlobalMat(){

    // if(stageStateTreeSelectNode){
        var rows = $("#select_stage_global_mat_tb").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0){
            var matIds = [];
            var maNames = [];
            for(var i=0;i<rows.length;i++){
                matIds.push(rows[i].matId);
                maNames.push(rows[i].matName);
            }
            $.ajax({
                url: ctx + '/aea/par/in/batchSaveStageNoStateMatIn.do',
                type: 'POST',
                data: {'stageId': stageId,'matIds': matIds.toString()},
                async: false,
                success: function (result) {
                    if (result.success) {
                        swal({
                            text: '保存成功！',
                            type: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });
                        $('#select_stage_global_mat_modal').modal('hide');
                        // // 创建节点
                        // if(matIds!=null&&matIds.length>0){
                        //     for(var i=0;i<matIds.length;i++){
                        //         var node = stageStateTree.getNodeByParam("id",matIds[i], null);
                        //         if(node==null){
                        //             var newNode = createNodeData(matIds[i],maNames[i],'mat',stageStateId,null);
                        //             stageStateTree.addNodes(stageStateTreeSelectNode,-1,newNode,true);
                        //         }
                        //     }
                        // }
                        // 刷新材料证照列表
                        searchStageRelMatCert();
                    }else{
                        swal('错误信息', result.message ,'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }else{
            swal('提示信息', "请选材料！", 'info');
        }
    // } else{
    //     swal('提示信息', "请选择节点！", 'info');
    // }
}