function childCertTypeQueryParam(params) {

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

function childCertTypeResponseData(res) {

    return res;
}

// 查询
function searchChildCertType(){

    if(certInfoTreeSelectNode) {

        var params = $('#search_child_cert_type_form').serializeArray();
        commonQueryParams = [];
        commonQueryParams.push({name: "parentTypeId", value: certInfoTreeSelectNode.id});
        if (params != "") {
            $.each(params, function() {
                commonQueryParams.push({name: this.name, value: this.value});
            });
        }
        $("#child_cert_type_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#child_cert_type_list_tb").bootstrapTable('refresh');       //无参数刷新
    }else{
        swal('提示信息', '请选择操作证照库节点！', 'info');
    }
}

function childCertTypeIsActiveFormatter(value, row, index) {

    return isChildCertTypeActiveStr(row);
}

function childCertTypeOperatorFormatter(value, row, index) {

    var editBtn =   '<a href="javascript:editChildCertTypeById(\'' + row.certTypeId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';

    var deleteBtn = '<a href="javascript:deleteChildCertTypeById(\''+row.certTypeId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';

    return editBtn + deleteBtn;
}

function isChildCertTypeActiveStr(row){

    if(row.isActive=='1'){
        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" checked="checked" name="isActive" onclick="changeChildCertTypeStateIsActive(this,\''+ row.certTypeId +'\',\''+ row.isActive +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }else{
        return  '<span class="m-switch m-switch--success">' +
                '  <label>' +
                '     <input type="checkbox" name="isActive" onclick="changeChildCertTypeStateIsActive(this,\''+ row.certTypeId +'\',\''+ row.isActive +'\');">' +
                '     <span></span>' +
                '   </label>' +
                '</span>'
    }
}

function changeChildCertTypeStateIsActive(obj,id,isActive){

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
                url: ctx+'/aea/cert/type/changIsActiveState.do',
                type: 'POST',
                data: {'id': id},
                success: function (result) {
                    if(result.success){
                        searchChildCertType();
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

// 刷新
function refreshChildCertType(){

    searchChildCertType();
}

// 清空查询
function clearChildCertType(){

    $('#search_child_cert_type_form')[0].reset();
    searchChildCertType();
}

// 批量删除
function batchDeleteChildCertType(){

    var rows = $('#child_cert_type_list_tb').bootstrapTable('getSelections');
    if (rows&&rows.length>0) {
        var certTypeIds = [];
        for(var i=0;i<rows.length;i++){
            certTypeIds.push(rows[i].certTypeId);
        }
        swal({
            title: '提示信息',
            text: '此操作将批量删除分类、下级子分类以及相关电子证照信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/cert/type/batchDeleteCertType.do',
                    type: 'POST',
                    data:{'ids': certTypeIds.toString()},
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
                                var node = certInfoTree.getNodeByParam("id", rows[i].certTypeId, null);
                                certInfoTree.removeNode(node);
                            }
                            //  刷新
                            searchChildCertType();
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
function editChildCertTypeById(id){

    if(id) {

        //将节点设置为选中状态
        var node = certInfoTree.getNodeByParam("id", id, null);
        certInfoTree.selectNode(node);
        certInfoTreeSelectNode = node;
        initRightShowPage(certInfoTreeSelectNode,false,false,'certType','1');
    }
}

// 单条删除
function deleteChildCertTypeById(id){

    if(id){
        swal({
            title: '提示信息',
            text: '此操作将删除分类、下级子分类以及相关电子证照信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/cert/type/deleteAeaCertTypeById.do',
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
                            var node = certInfoTree.getNodeByParam("id", id, null);
                            certInfoTree.removeNode(node);
                            // 刷新列表
                            searchChildCertType();
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