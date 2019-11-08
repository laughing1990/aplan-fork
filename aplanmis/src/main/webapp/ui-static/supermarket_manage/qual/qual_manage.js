var commonQueryParams;         //查询参数数组
var currQualId;                 //当前操作的资质id
var ae_qual_form_validator;   //表单校验器
//参数设置
function rootRelQualQueryParam(params) {

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
//查询结果
function rootRelQualResponseData(res) {
    var data = res.rows;
    //初始化
    if(!currQualId && (data.length > 0)){
        currQualId = data[0].qualId;
        initMajorZtree();
    }
    return res;
}

// 设置初始化校验
function initQualFormValida() {
    ae_qual_form_validator = $("#ae_qual_form").validate({
        // 定义校验规则
        rules: {
            qualName: {
                required: true,
                maxlength: 200
            },
            qualCode:{
                required: true,
                maxlength: 200,
                remote: {
                    url: ctx+'/supermarket/qual/checkUniqueQualCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        qualId: function(){
                            return $("#ae_qual_form input[name='qualId']").val();
                        },
                        qualCode: function() {
                            return $("#ae_qual_form input[name='qualCode']").val();
                        }
                    }
                }
            },
            memo: {
                maxlength: 2000
            },
        },
        messages: {
            qualName: {
                required: '此项必填!',
                maxlength: "长度不能超过200个字母!"
            },
            qualCode:{
                required: '此项必填!',
                maxlength: "长度不能超过200个字母!",
                remote: "编号已存在！",
            },
            memo: {
                maxlength: "长度不能超过2000个字母!"
            },
        },
        // 提交表单
        submitHandler: function(form){
            var d = {};
            var t = $('#ae_qual_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/supermarket/qual/saveAeaImQual.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){
                        closeQualModal();
                        clearRootRelQual();
                        initMajorZtree();
                        swal({
                            title: '提示信息',
                            text: '操作成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', "保存信息失败！", 'error');
                }
            });
        }
    });
}
$(function () {
    initQualFormValida();
    //表格单击
    $('#root_rel_qual_list_tb').on('click-row.bs.table',function (e,rows) {
        currQualId = rows.qualId;
        initMajorZtree();
    })
    $('#qualListTbScrollable').niceScroll({
        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });
})

// 查询
function searchRootRelQual() {

    var params = $('#search_root_rel_qual_form').serializeArray();
    commonQueryParams = [];
    if (params != "") {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    $("#root_rel_qual_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#root_rel_qual_list_tb").bootstrapTable('refresh');
}

// 清空查询
function clearRootRelQual(){
    $('#search_root_rel_qual_form')[0].reset();
    searchRootRelQual();
}

function rootRelQualOperatorFormatter(value, row, index){
    var editBtn =   '<a href="javascript:editQualById(\'' + row.qualId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
    var deleteBtn = '<a href="javascript:deleteQualById(\''+row.qualId + '\',\''+ row.qualName +'\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
    return editBtn + deleteBtn;
}

// 批量删除
function batchDeleteQual(){
    var rows = $("#root_rel_qual_list_tb").bootstrapTable('getSelections');
    if (rows&&rows.length>0) {
        var QualIds = [];
        for(var i=0;i<rows.length;i++){
            QualIds.push(rows[i].qualId);
        }
        swal({
            title: '提示信息',
            text: '此操作将批量删除选择的数据,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/supermarket/qual/batchDeleteQual.do',
                    type: 'POST',
                    data:{'ids': QualIds.toString()},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '批量删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            //  刷新
                            searchRootRelQual();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', '操作失败！', 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作数据！', 'info');
    }
}
//新增
function addQual() {
    $('#ae_qual_modal').modal('show');
    $('#ae_qual_modal_title').html('新增资质');
    $('#ae_qual_form')[0].reset();
    $('#qualId').val('');
    ae_qual_form_validator.resetForm();
}

// 单条编辑
function editQualById(id){
    if(id) {
        $('#ae_qual_modal').modal('show');
        $('#ae_qual_modal_title').html('编辑资质');
        $.ajax({
            url: ctx + '/supermarket/qual/getAeaImQual.do',
            type: 'POST',
            data: {'id': id},
            async: false,
            success: function (data) {
                loadFormData(true, '#ae_qual_form', data);
            },
            error: function () {
                swal('错误信息', "获取服务类型信息失败！", 'error');
            }
        });
    }
}

// 单条删除
function deleteQualById(id,name){
    if(id){
        swal({
            title: '提示信息',
            text: '此操作将删除：'+ name +',您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/supermarket/qual/deleteAeaImQualById.do',
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
                            // 刷新列表
                            searchRootRelQual();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', '操作失败！', 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作数据！', 'info');
    }
}
//关闭窗口
function closeQualModal() {
    $('#ae_qual_modal').modal('hide');
}