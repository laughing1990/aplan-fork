
$(function () {
   $('#itemTitle').html(currItemName);
    $('#itemExplainItemVerId').val(currItemVerId);
    initExplainFormValida();                // 服务信息表单校验
    loadExplainForm();                      //回显服务信息
    searchServicetb();                      //回显已配置的中介服务
    loadQualRequireForm();                  //回显中介机构资质要求信息
    initUnitRequireFormValida();            // 中介机构资质要求表单校验
    searchAccordingtb();                    //回显已配置的设立依据
    searchItemtb();                         //回显已配置的行政事项
});

//复选框点击事件
function checkBoxClickEvent(eleId,callback) {
    var result = $('#'+eleId).prop('checked');
    $('#'+eleId).val(result?'1':'0');
    callback(result);
}
//回显复选框
function loadCheckbox(value,eleId) {
    if(value == '1'){
        $('#'+eleId).prop("checked","checked");
        $('#'+eleId).val('1');
    }else{
        $("#"+eleId).removeAttr("checked");
        $('#'+eleId).val('0');
    }
}
//序号
function numberFormatter(value, row, index) {
    return index+1;
}
//=================================================================服务信息=======================================================================
var ae_item_explain_form_validator;
// 服务信息表单校验
function initExplainFormValida() {
    ae_item_explain_form_validator = $("#ae_item_explain_form").validate({
        // 定义校验规则
        rules: {
            serviceContent: {
                required: true,
                maxlength: 500
            },
            serviceResult:{
                required: true,
                maxlength: 200,
            },
            memo: {
                maxlength: 2000
            }
        },
        messages: {
            serviceContent: {
                required: '此项必填!',
                maxlength: "长度不能超过500个字母!"
            },
            serviceResult:{
                required: '此项必填!',
                maxlength: "长度不能超过200个字母!"
            },
            memo: {
                maxlength: "长度不能超过2000个字母!"
            }
        },
        // 提交表单
        submitHandler: function(form){
            var d = {};
            var t = $('#ae_item_explain_form').serializeArray();
            var isSocialFund = $('#isSocialFund').prop('checked');
            var isFinancialFund = $('#isFinancialFund').prop('checked');
            if(!isSocialFund){
                d['isSocialFund'] = '0';
            }
            if(!isFinancialFund){
                d['isFinancialFund'] = '0';
            }
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/supermarket/explain/saveAeaImItemExplain.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){
                        swal({
                            title: '提示信息',
                            text: '操作成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                        loadExplainForm();
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
//回显服务信息
function loadExplainForm() {
    $.ajax({
        url: ctx + '/supermarket/explain/getAeaImItemExplainByItemVerId.do',
        type: 'POST',
        data: {'itemVerId': currItemVerId},
        async: false,
        success: function (data) {
            loadFormData(true, '#ae_item_explain_form', data);
            if(data){
                var isFinancialFund = data.isFinancialFund;
                var isSocialFund = data.isSocialFund;
                loadCheckbox(isFinancialFund,'isFinancialFund');
                loadCheckbox(isSocialFund,'isSocialFund');
            }
        },
        error: function () {
            swal('错误信息', "获取服务信息失败！", 'error');
        }
    });
}
//=================================================================中介服务=======================================================================
var serviceQueryParams;         //查询参数数组
//参数设置
function serviceQueryParam(params) {

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
    if (serviceQueryParams) {
        for (var i = 0; i < serviceQueryParams.length; i++) {
            buildParam[serviceQueryParams[i].name] = serviceQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}
//查询结果
function serviceResponseData(res) {
    // var data = res.rows;
    return res;
}
// 查询
function searchServicetb(){
    serviceQueryParams = [];
    serviceQueryParams.push({'name':'itemVerId',value:currItemVerId});
    // $("#service_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#service_list_tb").bootstrapTable('refresh');
}
//打开中介服务配置窗口
function openCfgServiceWin() {
    $('#dialog_config_service').modal('show');
    clearServiceGlobalArray();
    clearSearchServiceList();
}
//=================================================================中介机构要求=======================================================================
var ae_unit_require_form_validator;
// 设置初始化校验
function initUnitRequireFormValida() {
    // ae_unit_require_form_validator.resetForm();
    //添加自定义校验
    jQuery.validator.addMethod("isQualRequire", function(value, element) {
        var isQualRequire = $('#isQualRequire').val();
        if(isQualRequire == '1'){
            if(value==null || typeof value == 'undefined' || value.trim() == '') {
                return false;
            }
        }
        return true;
    }, "此项必填！");
    jQuery.validator.addMethod("isRegisterRequire", function(value, element) {
        var isRegisterRequire = $('#isRegisterRequire').val();
        if(isRegisterRequire == '1'){
            if(value==null || typeof value == 'undefined' || value.trim() == '') {
                return false;
            }
        }
        return true;
    }, "此项必填！");
    jQuery.validator.addMethod("isRecordRequire", function(value, element) {
        var isRecordRequire = $('#isRecordRequire').val();
        if(isRecordRequire == '1'){
            if(value==null || typeof value == 'undefined' || value.trim() == '') {
                return false;
            }
        }
        return true;
    }, "此项必填！");
    ae_unit_require_form_validator = $("#ae_unit_require_form").validate({
        // 定义校验规则
        rules: {
            qualRequireName: {
                isQualRequire: true,
                maxlength: 2000
            },
            qualRequireExplain:{
                isQualRequire: true,
                maxlength: 2000
            },
            qualRecordRequire:{
                isQualRequire: true,
                maxlength: 2000
            },
            registerRequire:{
                isRegisterRequire: true,
                maxlength: 2000
            },
            recordRequireExplain:{
                isRecordRequire: true,
                maxlength: 2000
            }
        },
        messages: {
            qualRequireName: {
                isQualRequire: '此项必填!',
                maxlength: "长度不能超过2000个字母!"
            },
            qualRequireExplain:{
                isQualRequire: '此项必填!',
                maxlength: "长度不能超过2000个字母!",
                remote: "编号已存在！",
            },
            qualRecordRequire:{
                isQualRequire: '此项必填!',
                maxlength: "长度不能超过2000个字母!",
                remote: "编号已存在！",
            },
            registerRequire:{
                isRegisterRequire: '此项必填!',
                maxlength: "长度不能超过2000个字母!",
                remote: "编号已存在！",
            },
            recordRequireExplain:{
                isRecordRequire: '此项必填!',
                maxlength: "长度不能超过2000个字母!",
                remote: "编号已存在！",
            }
        },
        // 提交表单
        submitHandler: function(form){
            var isSaveExplain = getAeaImItemExplainByItemVerId();
            if(!isSaveExplain){
                swal('提示', "请先保存服务信息！", 'info');
                return;
            }
            var d = {};
            var t = $('#ae_unit_require_form').serializeArray();
            var isQualRequire = $('#isQualRequire').prop('checked');
            var isRegisterRequire = $('#isRegisterRequire').prop('checked');
            var isRecordRequire = $('#isRecordRequire').prop('checked');
            if(!isQualRequire){
                d['isQualRequire'] = '0';
            }
            if(!isRegisterRequire){
                d['isRegisterRequire'] = '0';
            }
            if(!isRecordRequire){
                d['isRecordRequire'] = '0';
            }
            d['itemVerId'] = currItemVerId;
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/supermarket/unit/require/saveAeaImUnitRequire.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){
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
//打开资质要求配置窗口
function cfgQualRequire() {
    $('#dialog_config_qual').modal('show');
    var ids = $('#qualRequire').val();
    allQualId.splice(0,allQualId.length);
    if(ids){
        var idArr = ids.split('.');
        $(idArr).each(function (i,n) {
            if(n){
                allQualId.push(n);
            }
        })
    }
    clearQualServiceList();
}
//获取服务信息
function getAeaImItemExplainByItemVerId() {
    var result = false;
    $.ajax({
        url: ctx + '/supermarket/explain/getAeaImItemExplainByItemVerId.do',
        type: 'POST',
        data: {'itemVerId': currItemVerId},
        async: false,
        success: function (data) {
            result = data?true:false;
        },
        error: function () {
            swal('错误信息', "获取服务信息失败！", 'error');
        }
    });
    return result;
}
//加载中介机构资质要求信息
function loadQualRequireForm() {
    if(ae_unit_require_form_validator){
        ae_unit_require_form_validator.resetForm();
    }
    clearQualGlobalArray();
    $.ajax({
        url: ctx + '/supermarket/unit/require/getAeaImUnitRequireByItemVerId.do',
        type: 'POST',
        data: {'itemVerId': currItemVerId},
        async: false,
        success: function (data) {
            loadFormData(true, '#ae_unit_require_form', data);
            var isQualRequire = '0';
            var isRegisterRequire = '0';
            var isRecordRequire = '0';
            if(data){
                isQualRequire = data.isQualRequire;
                isRegisterRequire = data.isRegisterRequire;
                isRecordRequire = data.isRecordRequire;
            }
            loadCheckbox(isQualRequire,'isQualRequire');
            loadCheckbox(isRegisterRequire,'isRegisterRequire');
            loadCheckbox(isRecordRequire,'isRecordRequire');
            showQualRequireInfo(isQualRequire,'showQual');
            showQualRequireInfo(isRegisterRequire,'showRegister');
            showQualRequireInfo(isRecordRequire,'showRecord');
        },
        error: function () {
            swal('错误信息', "获取中介机构要求信息失败！", 'error');
        }
    });
}
//保存配置服务时，如果勾选了资质要求，自动保存对应的资质要求。
function autoSaveQualRequire() {
    var qualSeq = $('#qualRequire').val();
    $.ajax({
        url: ctx + '/supermarket/unit/require/autoSaveQualRequire.do',
        type: 'POST',
        data: {
            'itemVerId': currItemVerId,
            'qualSeq':qualSeq
        },
        async: false,
        success: function (result) {
            if(result.success){
              var data = result.content;
              if(data){
                  var unitRequireId = data.unitRequireId;
                  $('#unitRequireId').val(unitRequireId);
              }
            }else{
                // swal('提示信息', "保存信息出错！", 'info');
            }
        },
        error: function () {
            swal('错误信息', "保存信息出错！", 'error');
        }
    });
}
//显示资质
function showChooseQual(result) {
    if(result){
        $.ajax({
            url: ctx + '/supermarket/service/listItemServiceNoPage.do',
            type: 'POST',
            data: {'itemVerId': currItemVerId},
            async: false,
            success: function (data) {
                if(data && data.length > 0){
                    var serviceIds = '';
                    $(data).each(function (i,n) {
                        serviceIds += ','+ n.serviceId;
                    });
                    serviceIds = serviceIds.substring(1);
                    getQualIdsByItemService(serviceIds);
                }else{
                    swal('提示信息', "请先配置所属服务！", 'info');
                }
            },
            error: function () {
                swal('错误信息', "获取服务信息失败！", 'error');
            }
        });
    }
}
//查询关联的中介服务对应的资质
function getQualIdsByItemService(serviceIds) {
    $.ajax({
        url: ctx + '/supermarket/serviceQual/listAeaImServiceQualNoPage.do',
        type: 'POST',
        data: {'serviceIds': serviceIds},
        async: false,
        success: function (data) {
            if(data && data.length > 0){
                var ids = '.';
                var names = '';
                $(data).each(function (i,n) {
                    ids += n.qualId + '.';
                    names += '、' +n.qualName
                });
                names = names.substring(1);
                $('#qualRequire').val(ids);
                $('#qualRequireName').val(names);
                showQualRequireInfo('1','showQual');
            }else{
                swal('提示信息', "所属服务没有配置资质！", 'info');
            }
        },
        error: function () {
            swal('错误信息', "获取资质信息失败！", 'error');
        }
    });
}
//显示要求信息
function showQualRequireInfo(value,eleId) {
    $('#'+eleId).css('display',value == '1'?'':'none');
}
function showQual(result) {
    if(!result){
        //取消选中，清空对应的值
        $('#qualRequire').val('');
        $('#qualRequireName').val('');
        $('#qualRequireExplain').val('');
        $('#qualRecordRequire').val('');
        showQualRequireInfo('0','showQual');
    } else{
        //不弹出窗口选择，直接读取关联的服务，找到对应的资质ID回显
        showChooseQual(result);
    }
}
function showRegister(result) {
    showQualRequireInfo(result?'1':'0','showRegister');
    if(!result){
        //取消选中，清空对应的值
        $('#registerRequire').val('');
    }
}
function showRecord(result) {
    showQualRequireInfo(result?'1':'0','showRecord');
    if(!result){
        //取消选中，清空对应的值
        $('#recordRequireExplain').val('');
    }
}
//=================================================================设立依据=======================================================================
var accordingQueryParams;
//参数设置
function accordingQueryParam(params) {
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
    if (accordingQueryParams) {
        for (var i = 0; i < accordingQueryParams.length; i++) {
            buildParam[accordingQueryParams[i].name] = accordingQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}
//查询结果
function accordingResponseData(res) {
    return res;
}
// 查询
function searchAccordingtb(){
    accordingQueryParams = [];
    accordingQueryParams.push({'name':'itemVerId',value:currItemVerId});
    $("#according_list_tb").bootstrapTable('refresh');
}
//打开配置设立依据窗口
function openCfgAccordingWin() {
    $('#dialog_config_according').modal('show');
    clearAccordingGlobalArray();
    clearSearchAccordingList();
}
//
function clauseContentFormatter(value, row, index) {
    var result = '<div class="viewClauseContent">'+ value +'</div>';
    return result;
}
//=================================================================涉及行政事项=======================================================================
var itemQueryParams;
//参数设置
function itemQueryParam(params) {
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
    if (itemQueryParams) {
        for (var i = 0; i < itemQueryParams.length; i++) {
            buildParam[itemQueryParams[i].name] = itemQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}
//查询结果
function itemResponseData(res) {
    return res;
}
// 查询
function searchItemtb(){
    itemQueryParams = [];
    itemQueryParams.push({'name':'itemId',value:currItemId});
    // itemQueryParams.push({'name':'itemNature',value:'0'});
    $("#item_list_tb").bootstrapTable('refresh');
}
//打开行政事项配置窗口
function openCfgItemWin() {
    $('#dialog_config_item').modal('show');
    clearItemGlobalArray();
    clearSearchItemList();
}