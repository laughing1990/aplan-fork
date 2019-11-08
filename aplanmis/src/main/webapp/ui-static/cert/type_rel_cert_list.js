function typeRelCertQueryParam(params) {

    //组装分页参数
    var pageNum = (params.offset / params.limit) + 1;
    var pagination = {
        page: pageNum,
        pages: pageNum,
        perpage: params.limit
    };
    var sort = {
        field: params.sort,
        sort: params.order
    };
    var queryParam = {
        pagination: pagination,
        sort: sort
    };
    //组装查询参数
    var buildParam = {};
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

function typeRelCertResponseData(res) {

    return res;
}

function certHolderFormatter(value, row, index) {

    if(row.certHolder=='c'){
        return '企业';
    }else if(row.certHolder=='u'){
        return '个人';
    }else if(row.certHolder=='p'){
        return '工程项目';
    }
}

function typeRelCertOperatorFormatter(value, row, index) {

    var editBtn =   '<a href="javascript:editCertById(\'' + row.certId + '\')" ' +
                    'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑">' +
                    '<i class="la la-edit"></i></a>';

    var deleteBtn = '<a href="javascript:deleteCertById(\''+row.certId + '\')"' +
                    'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';

    return editBtn + deleteBtn;
}


// // 获取证照列表
function searchTypeRelCert(){

    if(certInfoTreeSelectNode) {

        var params = $('#search_type_rel_cert_list_form').serializeArray();
        commonQueryParams = [];
        commonQueryParams.push({name: "certTypeId", value: certInfoTreeSelectNode.id});
        if (params != "") {
            $.each(params, function() {
                commonQueryParams.push({name: this.name, value: this.value});
            });
        }
        $("#type_rel_cert_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#type_rel_cert_list_tb").bootstrapTable('refresh');       //无参数刷新
    }else{
        swal('提示信息', '请选择操作证照库节点！', 'info');
    }
}

function refreshTypeRelCert(){

    searchTypeRelCert();
}

function isCertActiveStr(row){

    if(row.isActive=='1'){
        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" checked="checked" name="isActive" onclick="changeCertStateIsActive(this,\''+ row.certId +'\',\''+ row.isActive +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }else{
        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" name="isActive" onclick="changeCertStateIsActive(this,\''+ row.certId +'\',\''+ row.isActive +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }
}

function changeCertStateIsActive(obj,id,isActive){

    var action = isActive=='1'? "禁用" : "启用" ;
    swal({
        title: '提示信息',
        text: "确定要" + action + "选中的记录吗?",
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value){
            $.ajax({
                url: ctx+'/aea/cert/changIsActiveState.do',
                type: 'POST',
                data: {'id': id},
                success: function (result) {
                    if(result.success){
                        searchTypeRelCert();
                    }else{
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                }
            });
        }else{
            isActive=='1'?$(obj).prop("checked",true):$(obj).prop("checked",false);
        }
    });
}

// 清空查询
function clearSearchTypeRelCert(){

    $('#search_type_rel_cert_list_form')[0].reset();
    searchTypeRelCert();
}

// 批量删除电子证照
function batchDeleteTypeRelCert(){

    var rows = $('#type_rel_cert_list_tb').bootstrapTable('getSelections');
    if (rows&&rows.length>0) {
        var certIds = [];
        for(var i=0;i<rows.length;i++){
            certIds.push(rows[i].certId);
        }
        swal({
            title: '提示信息',
            text: '此操作将批量删除电子证照信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/cert/batchDeleteCertByIds.do',
                    type: 'POST',
                    data:{'ids': certIds.toString()},
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
                            for(var i=0;i<rows.length;i++){
                                var node = certInfoTree.getNodeByParam("id", rows[i].certId, null);
                                certInfoTree.removeNode(node);
                            }
                            //  刷新
                            searchTypeRelCert();
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

// 单条编辑
function editCertById(id){

    if(id) {
        //将节点设置为选中状态
        var node = certInfoTree.getNodeByParam("id", id, null);
        certInfoTree.selectNode(node);
        certInfoTreeSelectNode = node;
        initRightShowPage(node,false,false,'cert','1');
    }
}

// 单条删除
function deleteCertById(id) {

    if (id) {
        swal({
            title: '提示信息',
            text: '此操作将删除电子证照信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/cert/deleteAeaCertById.do',
                    type: 'POST',
                    data: {'id': id},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 移除节点
                            var node = certInfoTree.getNodeByParam("id", id, null);
                            certInfoTree.removeNode(node);
                            // 刷新列表
                            searchTypeRelCert();
                        } else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        swal('错误信息', '服务器异常！', 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', '请选择操作数据！', 'info');
    }
}