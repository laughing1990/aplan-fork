var commonQueryParams = [],
    aedit_que_ans_validator;

$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 10);

    // 列表设置高度
    $('#questAnswerTb').bootstrapTable('resetView', {
        height: $('#westPanel').height() - 116
    });

    // 新增编辑页面设置滚动
    $('#aedit_que_ans_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 列表设置可以滚动
    $(".fixed-table-body").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    initValidate();

    // 表格双击事件
    $('#questAnswerTb').on('dbl-click-row.bs.table', function (e, row, element) {

        editQuestAnswer(row.questionId)
    });
});

// 初始化表单检验
function initValidate() {

    aedit_que_ans_validator = $("#aedit_que_ans_form").validate({
        // 定义校验规则
        rules: {
            question:{
                required: true,
                maxlength: 4000
            },
            answer: {
                required: true,
                maxlength: 4000
            },
            sortNo: {
                required: true,
            }
        },
        messages: {
            question:{
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过4000个字母!"
            },
            answer: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过4000个字母!"
            },
            sortNo: {
                required: '<font color="red">此项必填！</font>',
            }
        },
        // 提交表单
        submitHandler: function () {

            var d = $('#aedit_que_ans_form').serializeArray();
            $.ajax({
                url: ctx+'/aea/par/stage/questions/saveOrUpdateQuestAnswer.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success){
                        swal({
                            type: 'success',
                            title: '保存成功！',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        $('#aedit_que_ans_modal').modal('hide');
                        // 列表数据重新加载
                        searchQuestAnswer();
                    } else {
                        swal('错误信息', result.message ,'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });
}

// 查询事件
function searchQuestAnswer() {

    var params = $('#search_que_ans_form').serializeArray();
    commonQueryParams = [];
    if (params != null) {
        $.each(params, function() {
            commonQueryParams.push({name: this.name, value: this.value});
        });
    }
    //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#questAnswerTb").bootstrapTable('selectPage',1);
    $("#questAnswerTb").bootstrapTable('refresh'); //无参数刷新
}

// 刷新事件
function refreshQuestAnswer(){

    $('#search_que_ans_form')[0].reset();
    searchQuestAnswer();
}

// 清空查询
function clearQuestAnswer() {

    $('#search_que_ans_form')[0].reset();
    searchQuestAnswer();
}

// 列表查询参数处理
function questAnswerParam (params) {

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
        sort: sort,
        stageId: recordId
    };
    //组装查询参数
    var buildParam = {};
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

function showLengthFormatter(value, row, index, field) {

    if(value){
        if(value.length>200){
            return value.substr(0,200)+'......';
        }
    }
    return value;
}

// 格式化操作栏
function operatorFormatter(value, row, index, field) {

    var title = '编辑';
    var icoCss = 'la la-edit';
    if(!curIsEditable){
        var title = '查看';
        var icoCss = 'la la-search';
    }
    var editBtn = '<a href="javascript:editQuestAnswer(\'' + row.questionId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="'+ title +'">' +
        '<i class="'+ icoCss +'"></i>' +
        '</a>';

    var delBtn  = '<a href="javascript:delQuestAnswer(\'' + row.questionId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除">' +
        '<i class="la la-trash"></i>' +
        '</a> ';
    return editBtn + delBtn;
}

// 新增
function addQuestAnswer(){

    if(curIsEditable){

        $('#aedit_que_ans_modal_title').html('新增问题答案');
        $('#aedit_que_ans_modal').modal('show');
        $('#aedit_que_ans_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#aedit_que_ans_form')[0].reset();
        $('#aedit_que_ans_form').resetForm();
        $('#aedit_que_ans_form input[name="questionId"]').val('');
        $('#aedit_que_ans_form input[name="stageId"]').val(recordId);

        $.ajax({
            url: ctx+'/aea/par/stage/questions/getMaxSortNo.do',
            type: 'post',
            data: {'stageId': recordId},
            async: false,
            success: function (data) {
                if(data) {
                    $('#aedit_que_ans_form input[name="sortNo"]').val(data);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }else{
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
    }
}

// 编辑
function editQuestAnswer(id){

    if(curIsEditable){

        $('#aedit_que_ans_modal_title').html('编辑问题答案');
        $('#aedit_que_ans_modal').modal('show');
        $('#saveQueAnsBtn').show();
        $('#aedit_que_ans_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#aedit_que_ans_form')[0].reset();
        $('#aedit_que_ans_form').resetForm();
        $('#aedit_que_ans_form input[name="questionId"]').val('');

        $.ajax({
            url: ctx+'/aea/par/stage/questions/getQuestAnswerById.do',
            type: 'post',
            data: {'id': id},
            async: false,
            success: function (data) {
                if(data) {
                    // 加载表单数据
                    loadFormData(true,'#aedit_que_ans_form',data);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }else{
        $('#aedit_que_ans_modal_title').html('查看问题答案');
        $('#saveQueAnsBtn').hide();
    }
}

// 删除
function delQuestAnswer(id) {

    if(curIsEditable){
        if(id){
            swal({
                text: '此操作将删除选择的问题答案数据，您确定执行吗？',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        type: 'post',
                        url: ctx+'/aea/par/stage/questions/delQuestAnswerById.do',
                        dataType: 'json',
                        data: {'id': id},
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    type: 'success',
                                    title: '删除成功!',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                                // 列表数据重新加载
                                searchQuestAnswer();
                            } else {
                                swal('错误信息', result.message ,'error');
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '请选择操作记录！', 'info');
        }
    }else{
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
    }
}

// 批量删除
function batchDelQuestAnswer(){

    if(curIsEditable){
        var rows = $("#questAnswerTb").bootstrapTable('getSelections');
        if(rows!=null&&rows.length>0){
            var ids = [];
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].questionId);
            }
            swal({
                text: '此操作将批量删除选择的问题答案数据，您确定执行吗？',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        type: 'post',
                        url: ctx+'/aea/par/stage/questions/batchDelQuestAnswerByIds.do',
                        dataType: 'json',
                        data: {'ids': ids.toString()},
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    type: 'success',
                                    title: '删除成功!',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                                // 列表数据重新加载
                                searchQuestAnswer();
                            } else {
                                swal('错误信息', result.message ,'error');
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '请选择操作记录！', 'info');
        }
    }else{
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
    }
}
