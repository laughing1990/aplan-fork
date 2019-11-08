//参数设置
function selectStateGlobalMatParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    commonQueryParams['pageNum'] = pageNum,
    commonQueryParams['pageSize'] = params.limit;
    return commonQueryParams;
}

// 查询
function searchStateGlobalMat(){

    commonQueryParams = {};
    var t = $('#search_state_global_mat_form').serializeArray();
    $.each(t, function() {
        commonQueryParams[this.name] = this.value;
    });
    commonQueryParams['stageId'] = stageId;
    commonQueryParams['parStateId'] = stageStateTreeSelectNode.id;
    $("#select_state_global_mat_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#select_state_global_mat_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchStateGlobalMat(){

    $('#search_state_global_mat_form')[0].reset();
    searchStateGlobalMat();
}

// 保存情形选择的全局材料数据
function saveStateGlobalMat(){

    if(stageStateTreeSelectNode){
        var rows = $("#select_state_global_mat_tb").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0){
            var matIds = [];
            var maNames = [];
            for(var i=0;i<rows.length;i++){
                matIds.push(rows[i].matId);
                maNames.push(rows[i].matName);
            }
            var stageStateId = stageStateTreeSelectNode.id;
            $.ajax({
                url: ctx + '/aea/par/in/batchSaveStageStateMatIn.do',
                type: 'POST',
                data: {'stageId': stageId,'stageStateId': stageStateId,'matIds': matIds.toString()},
                async: false,
                success: function (result) {
                    if (result.success) {
                        swal({
                            text: '保存成功！',
                            type: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });
                        $('#select_state_global_mat_modal').modal('hide');
                        // 创建节点
                        if(matIds!=null&&matIds.length>0){
                            for(var i=0;i<matIds.length;i++){
                                var node = stageStateTree.getNodeByParam("id",matIds[i], null);
                                if(node==null){
                                    var newNode = createNodeData(matIds[i],maNames[i],'mat',stageStateId,null);
                                    stageStateTree.addNodes(stageStateTreeSelectNode,-1,newNode,true);
                                }
                            }
                        }
                        // 刷新材料证照列表
                        searchStateRelMatCert();
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
    } else{
        swal('提示信息', "请选择节点！", 'info');
    }
}