// 格式
function stateRelMatCertFormatter(value, row, index) {

    var editBtn = null;
    var deleteBtn = null;

    if(row.fileType=='mat'){

        editBtn = '<a href="javascript:editStateRelMatCertById(\'' + row.matId + '\',\'mat\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                      'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

        deleteBtn = '<a href="javascript:deleteStateRelMatCertById(\''+row.matId + '\',\'mat\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';

    }else if(row.fileType=='cert'){

        editBtn = '<a href="javascript:editStateRelMatCertById(\'' + row.certId + '\',\'cert\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                      'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

        deleteBtn = '<a href="javascript:deleteStateRelMatCertById(\''+row.certId + '\',\'cert\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';
    }

    return editBtn + deleteBtn;
}

function fileTypeFormatter(value, row, index){

    if(value=='mat'){
        return '材料';
    }else if(value=='cert'){
        return '证照';
    }
}


//参数设置
function stateRelMatCertParam(params) {

    var pageNum = (params.offset / params.limit) + 1;
    commonQueryParams['pageNum'] = pageNum,
    commonQueryParams['pageSize'] = params.limit;
    return commonQueryParams;
}

// 查询
function searchStateRelMatCert(){

    commonQueryParams = {};
    var t = $('#search_state_rel_mat_cert_form').serializeArray();
    $.each(t, function() {
        commonQueryParams[this.name] = this.value;
    });
    commonQueryParams['stageId'] = stageId;
    commonQueryParams['parStateId'] = stageStateTreeSelectNode.id;
    $("#state_rel_mat_cert_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    // $("#state_rel_mat_cert_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchStateRelMatCert(){

    $('#search_state_rel_mat_cert_form')[0].reset();
    searchStateRelMatCert();
}


// 刷新
function refreshStateRelMatCert(){

    searchStateRelMatCert();
}

// 批量阶段删除材料证照
function batchDeleteStateRelMatCert(){

    var rows = $("#state_rel_mat_cert_tb").bootstrapTable('getSelections');
    var matIds = [];
    var certIds = [];
    if(rows!=null&&rows.length>0){
        for(var i=0;i<rows.length;i++){
            if(rows[i].fileType='mat'){
                matIds.push(rows[i].matId);
            }else if(rows[i].fileType='cert'){
                certIds.push(rows[i].certId);
            }
        }

        swal({
            title: '提示信息',
            text: '此操作将批量删除材料、证照数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {

                if(matIds!=null&&matIds.length>0){
                    $.ajax({
                        url: ctx+'/aea/item/mat/batchDeleteAeaItemMatByIds.do',
                        type: 'POST',
                        data:{'ids': matIds.toString()},
                        success: function (result){
                            if(result.success){
                                // swal({
                                //     title: '提示信息',
                                //     text: '批量删除成功!',
                                //     type: 'success',
                                //     timer: 1000,
                                //     showConfirmButton: false
                                // });
                                // 移除节点
                                for(var i=0;i<matIds.length;i++){
                                    var node = stageStateTree.getNodeByParam("id", matIds[i], null);
                                    stageStateTree.removeNode(node);
                                }
                            }else{
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error:function(){
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }

                if(certIds!=null&&certIds.length>0){
                    $.ajax({
                        url: ctx+'/aea/cert/batchDeleteCertByIds.do',
                        type: 'POST',
                        data:{'ids': certIds.toString()},
                        success: function (result){
                            if(result.success){
                                // swal({
                                //     title: '提示信息',
                                //     text: '批量删除成功!',
                                //     type: 'success',
                                //     timer: 1000,
                                //     showConfirmButton: false
                                // });
                                // 移除节点
                                for(var i=0;i<certIds.length;i++){
                                    var node = stageStateTree.getNodeByParam("id", certIds[i], null);
                                    stageStateTree.removeNode(node);
                                }
                            }else{
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error:function(){
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }
                // 刷新表格
                searchStateRelMatCert();
            }
        });
    }else{
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

function editStateRelMatCertById(id,fileType){

    if(id){

        //将节点设置为选中状态
        var node = stageStateTree.getNodeByParam("id", id, null);
        stageStateTree.selectNode(node);
        stageStateTreeSelectNode = node;

        editMatCert();

        // 加载数据
        if(fileType=='mat'){
            $("#add_stage_mat_form input[name='isTbEditMatNode']").val('1');
        }else if(fileType=='cert'){
            $("#add_stage_cert_form input[name='isTbEditCertNode']").val('1');
        }
    }
}

function deleteStateRelMatCertById(id,fileType){

    if(id&&fileType){
        var  url = null;
        var  title = '';
        if(fileType=='mat'){

            title = '材料';
            url = ctx+'/aea/item/mat/deleteAeaItemMatById.do';

        }else if(fileType=='cert'){

            title = '证照';
            url = ctx+'/aea/cert/deleteAeaCertById.do';
        }
        swal({
            title: '提示信息',
            text: '此操作将批量删除'+ title +'数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data:{'id': id},
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
                            var node = stageStateTree.getNodeByParam("id", id, null);
                            stageStateTree.removeNode(node);
                            // 刷新表格
                            searchStateRelMatCert();
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