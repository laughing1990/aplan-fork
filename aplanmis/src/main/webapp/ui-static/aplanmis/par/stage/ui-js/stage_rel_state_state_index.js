// 格式
function childStateFormatter(value, row, index) {

    var editBtn = '<a href="javascript:editChildStateById(\'' + row.parStateId + '\')" ' +
                    'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                    'title="编辑"><i class="la la-edit"></i>' +
                 '</a>';

    var deleteBtn = '<a href="javascript:deleteChildStateById(\''+row.parStateId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';

    return editBtn + deleteBtn;
}

//参数设置
function childStateParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    commonQueryParams['pageNum'] = pageNum,
    commonQueryParams['pageSize'] = params.limit;
    return commonQueryParams;
}

// 查询
function searchChildState(){

    commonQueryParams = {};
    var t = $('#search_child_state_form').serializeArray();
    $.each(t, function() {
        commonQueryParams[this.name] = this.value;
    });
    commonQueryParams['parStateId'] = stageStateTreeSelectNode.id;
    $("#child_state_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#child_state_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchChildState(){

    $('#search_child_state_form')[0].reset();
    searchChildState();
}


// 刷新
function refreshChildState(){

    searchChildState();
}

// 批量阶段删除情形
function batchDeleteChildState(){

    var checkBoxs = $("#child_state_tb").bootstrapTable('getSelections');
    var ids = [];
    if(checkBoxs!=null&&checkBoxs.length>0){
        for(var i=0;i<checkBoxs.length;i++){
            ids.push(checkBoxs[i].parStateId);
        }
        swal({
            title: '提示信息',
            text: '此操作将删除情形,相关材料关联数据将失效,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/par/state/batchDeleteAeaParStateByIds.do',
                    type: 'POST',
                    data:{'ids': ids.toString()},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '批量删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 移除节点
                            for(var i=0;i<checkBoxs.length;i++){
                                var node = stageStateTree.getNodeByParam("id", checkBoxs[i].parStateId, null);
                                stageStateTree.removeNode(node);
                            }
                            // 刷新表格
                            searchChildState();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', '服务器异常！', 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

function editChildStateById(parStateId){

    if(parStateId){

        //将节点设置为选中状态
        var node = stageStateTree.getNodeByParam("id", parStateId, null);
        stageStateTree.selectNode(node);
        stageStateTreeSelectNode = node;

        // 加载数据
        editStageState();

        // 设置表格编辑节点
        $("#add_stage_state_form input[name='isTbEditStateNode']").val('1');
    }
}

function deleteChildStateById(parStateId){

    if(parStateId){
        swal({
            title: '提示信息',
            text: '此操作将删除情形,相关材料关联数据将失效,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/par/state/deleteAeaParStateById.do',
                    type: 'POST',
                    data:{'id': parStateId},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 移除节点
                            var node = stageStateTree.getNodeByParam("id", parStateId, null);
                            stageStateTree.removeNode(node);
                            // 刷新表格
                            searchChildState();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', '服务器异常！', 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作数据！', 'info');
    }
}
