$(function () {
    init();
})
var PAGE_BIZ_TYPE_MSG_RECEIVE='MSG_RECEIVE';
var PAGE_BIZ_TYPE_MSG_SEND='MSG_SEND';
var CURRENT_PAGE_BIZ_TYPE=PAGE_BIZ_TYPE_MSG_RECEIVE;


var URL_MY_RECEIVE='/aoa/msg/range/listMetronicAoaMsgRangeForMyReceive.do';
var URL_MY_SEND='/aoa/msg/range/listMetronicAoaMsgRangeForMySend.do';



function init() {
    initCheckBox();
    searchMsg();
}

//勾选框
function initCheckBox() {
    $('#check0').on('click',function () {
        if(this.checked){
            $('#isImportant').val('0');
            document.getElementById("check1").checked=false;
        }else {
            $('#isImportant').val('');
        }
    });
    $('#check1').on('click',function () {
        if(this.checked){
            $('#isImportant').val('1');
            document.getElementById("check0").checked=false;
        }else {
            $('#isImportant').val('');
        }
    });
}

function boxShow(index) {
    $('#containerMsgReceive').css('display','none');
    if(index == '1'){
        $('#containerMsgReceive').css('display','block');
        $('#inbox').addClass('btn-primary');
        $('#outbox').removeClass('btn-primary');
        CURRENT_PAGE_BIZ_TYPE=PAGE_BIZ_TYPE_MSG_RECEIVE;
        searchMsg();
    }else if(index == '2'){
        $('#inbox').removeClass('btn-primary');
        $('#outbox').addClass('btn-primary');
        CURRENT_PAGE_BIZ_TYPE=PAGE_BIZ_TYPE_MSG_SEND;
        searchMsg();
    }
}

//清空查询
function clearSearch() {
    $('#search_msg_list_form')[0].reset();
    searchMsg();
}

// 查询
var index_msg_list_tb = null;
function searchMsg(){
    var keyword = $('#searchMsgKeyword').val();
    var url='';
    if(CURRENT_PAGE_BIZ_TYPE==PAGE_BIZ_TYPE_MSG_RECEIVE){
        url=URL_MY_RECEIVE;
    }
    else if(CURRENT_PAGE_BIZ_TYPE==PAGE_BIZ_TYPE_MSG_SEND){
        url=URL_MY_SEND;
    }

    if (index_msg_list_tb != null) index_msg_list_tb.destroy();
    index_msg_list_tb = $('#index_msg_list_tb').mDatatable({
        data: {
            type: 'remote',
            source: {
                read: {
                    method: 'post',
                    url: ctx+url,
                    params: {keyword:keyword},
                    map: function (raw) {
                        var dataSet = raw;
                        if (typeof raw.data !== 'undefined') {
                            dataSet = raw.data;
                        }
                        return dataSet;
                    }
                }
            },
            saveState: {            //取消缓存
                cookie: false,
                webstorage: false,
            },
            pageSize: 10,
            serverPaging: true,
            serverFiltering: true,
            serverSorting: true,
        },
        layout: {
            theme: 'default', // datatable theme
            class: '', // custom wrapper class
            scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
            footer: false // display/hide footer
        },
        sortable: true,
        pagination: true,
        toolbar: {
            items: {
                pagination: {
                    pageSizeSelect: [10, 20, 30]
                },
            },
        },
        columns: [
            {
                field: "rangeId",
                title: "#",
                width: 10,
                sortable: false,
                textAlign: 'center',
                selector: {class: 'm-checkbox--solid m-checkbox--brand'}
            },
            {
                field: "contentTitle",
                title: "主题",
                width: '250',
                template: function (row, index, datatable) {
                    var contentTitleValue = row.contentTitle;
                    if(row.isRead == '0'){
                        contentTitleValue = '<span style="color: red">'+ row.contentTitle +'</span>';
                    }
                    return contentTitleValue;
                }
            },
            {
                field: "contentText",
                title: "内容",
                width: 320
            },
            {
                field: "sendUserName",
                title: "发件人",
                textAlign: 'center',
                width: 50
            },
            {
                field: "createTime",
                title: "发件时间",
                type: "date",
                textAlign: 'center',
                width: 150,
                template: function (row, index, datatable) {
                    var value = row.createTime;
                    return formatDatetime(value);
                }
            },
            {
                field: "receiveUserName",
                title: "收件人",
                textAlign: 'center',
                width: 50
            },
            {
                field: "receiveTime",
                title: "收件时间",
                textAlign: 'center',
                width: 150,
                template: function (row, index, datatable) {
                    var value = row.receiveTime;
                   return formatDatetime(value);
                }
            },
            {
                field: '_operate', // 数据字段
                title: '操作', // 页面字段显示
                sortable: false, // 禁用排序
                width: 80,  // 宽度,单位: px
                textAlign: 'center', // 字段列标题和数据排列方式
                template: function (row, index, datatable) {
                    var viewBtn = '<a href="javascript:viewMsg(\'' + row.rangeId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-search"></i></a>';
                    return viewBtn;
                }
            }
        ]
    });
    index_msg_list_tb.options.translate.toolbar.pagination.items.info = '显示 {{start}} - {{end}} 共 {{total}} 条记录';
}
//查看消息
function viewMsg(rangeId) {
    $.ajax({
        url: ctx+'/aoa/msg/range/getAoaMsgRangeCascade.do',
        type: 'POST',
        data:{id:rangeId},
        dataType:'json',
        success: function (data){
            if(data){
                updateAoaMsgRangeReaded(data.rangeId);
                $('#view_person_msg_modal').modal('show');
                $('#view_person_msg_form')[0].reset();
                loadFormData(true,'#view_person_msg_form',data);
                if(data.isImportant == '1'){
                    document.getElementById("view_check0").checked=false;
                    document.getElementById("view_check1").checked=true;
                }else if(data.isImportant == '0'){
                    document.getElementById("view_check0").checked=true;
                    document.getElementById("view_check1").checked=false;
                }
                searchMsg();
            }
        },
        error:function(){
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}

//设置消息读取状态为已读
function updateAoaMsgRangeReaded(rangeId) {
    $.ajax({
        url: ctx + '/aoa/msg/range/updateAoaMsgRangeReaded.do'
        ,type: 'post'
        ,async: false
        ,cache:false
        ,data: {rangeId:rangeId}
        ,success: function (result) {
            if (result.success){

            }else {
                swal('错误信息', result.message, 'error');
            }
        }
        ,error:function(a,b,c){
            swal('错误信息', "发送失败，服务器异常！", 'error');
        }
    });

}


//初始化表单
var person_msg_form = null;
function initNewMsgForm() {
    //if(person_msg_form != null)person_msg_form.resetForm();//清除错误提示

    // 设置页面元素表单初始化校验
    person_msg_form = $("#person_msg_form").validate({
        // 定义校验规则
        rules: {
            sendUserName: {
                required: true
            },
            contentTitle: {
                required: true
            },
            contentText: {
                required: true
            }
        },
        messages: {
            sendUserName: {
                required: "收件人必须填写！"
            },
            contentTitle: {
                required: "主题必须填写！"
            },
            contentText: {
                required: "内容必须填写！"
            }
        },
        invalidHandler: function(event, validator) {
            mApp.scrollTo("#person_msg_form");
        },
        // 提交表单
        submitHandler: function(form){
            var d = {};
            var t = $('#person_msg_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/aoa/msg/range/saveMsgContentAndMsgRange.do',
                type: 'POST',
                data: d,
                success: function (result) {
                    if (result.success){
                        swal({
                            title: '提示信息',
                            text: "发送成功！",
                            type: 'info',
                            showCancelButton: false,
                            confirmButtonText: '是',
                            cancelButtonText: '否'
                        }).then(function(result) {
                            if(result.value==true){
                            }
                            $('#person_msg_modal').modal('hide');
                            location.reload();
                        });
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', "发送失败，服务器异常！", 'error');
                }
            });
        }
    });
}
//打开新建消息窗口
function newPersonMsg() {
    $('#person_msg_modal').modal('show');
    // $('#person_msg_form')[0].reset();
    $('#rangeId').val('');
    $('#sendUserId').val('');
    $('#isImportant').val('');
    initNewMsgForm();
}


function checkRecord(checkBoxs) {
    var result=false;
    if(checkBoxs!=null&&checkBoxs.length>0){
        result=true;
    }
    else{
        result=false;
        swal('提示信息', '请选择操作数据！', 'info');
    }
    return result;
}

// 批量删除
function deleteMsgList(){
    var ids = [];
    var checkBoxs = index_msg_list_tb.getSelectedRecords().find("input[type='checkbox']");
    if(checkRecord(checkBoxs)){
        for(var i=0;i<checkBoxs.length;i++){
            ids.push(checkBoxs[i].value);
        }
        swal({
            title: '提示信息',
            text: '被删除的消息将无法恢复。您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aoa/msg/range/deleteMsgByIds.do',
                    type: 'POST',
                    data:{'ids': ids.toString()},
                    success: function (result){
                        if(result.success){
                            searchMsg();
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });

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
    }
}

// 单条删除
function deleteItemById(itemId){

    if(itemId){
        swal({
            title: '提示信息',
            text: '此操作将删除事项,相关材料关联数据将失效,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/item/deleteAeaItemById.do',
                    type: 'POST',
                    data:{'id': itemId},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });

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

//时间格式转换
function formatDatetime(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
    }
    return dt.format("yyyy-MM-dd hh:mm:ss");
}