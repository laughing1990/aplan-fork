commonQueryParams = {};
var timelimitRule = [];
function processParam(params) {
    commonQueryParams['appId'] = appId;
    commonQueryParams['stateVerId'] = currentStateVerId;
    commonQueryParams['keyword'] = $("#keyword").val();
    return commonQueryParams;
}

var currRows = [];//用于存储行对象数组；主要用于解决在按钮无法传递带引号的stratEl数据值的问题
function operate(value, row, index) {
    if(currRows&&currRows.length>0){
        var i = 0;
        $.each(currRows,function(index,e){
            if(row.id==e.id){
                i++;
                e.startEl = row.startEl;
            }
        });
        if(i==0){
            currRows.push(row);
        }
    }else{
        currRows.push(row);
    }

    var editLimitBtn = '<a href="javascript:editLimit('+"'"+row.timeLimit+"','"+row.appFlowdefId+'\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="配置时限">' +
        '<i class="la la-calendar-check-o"></i>' + '</a>';

    var activateBtn = '<a href="javascript:showStartEl('+"'"+row.id+"',"+index+')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="启动情形">' +
                            '<i class="la la-diamond"></i>' + '</a>';

    var editBtn = '<a href="javascript:chooseProcDef(\'' + row.modelId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="编辑流程"><i class="la la-edit"></i>' +
        '</a>';

    var priBtn = '<a target="_blank" href="'+ctx+'/bpm/admin/template/authConfigForm.html?appFlowdefId='+row.id+'&appId='+ appId +'"'+
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="权限配置"><i class="la la-cog"></i>' +
        '</a>';

    var subprocessBtn = '<a href="javascript:showSubprocess(\'' + row.appFlowdefId + '\',\''+row.defKey+'\');" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="子流程配置"><i class="la la-cogs"></i>' +
        '</a>';

    var deleteBtn = '<a href="javascript:delActTplAppProc(\''+row.appFlowdefId+'\');" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="删除"><i class="la la-trash"></i>' +
        '</a>';

    if(currentBusiType=='item'){
        return editLimitBtn + activateBtn + editBtn + priBtn + subprocessBtn + deleteBtn;
    }else{
        return editLimitBtn + editBtn + priBtn + subprocessBtn + deleteBtn;
    }
}

function search() {
    commonQueryParams['keyword'] = $("#keyword").val();
    // commonQueryParams['busiType'] = currentBusiType;
    // commonQueryParams['busiId'] = currentBusiId;
    commonQueryParams['appId'] = appId;
    $("#process_design_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#process_design_tb").bootstrapTable('refresh');       //无参数刷新
}

function refresh() {
    search();
}

$(function () {
    $("#clearBtn").click(function() {
        $('#search_item_process_design')[0].reset();
        search();
    });

    $("#searchBtn").click(function () {
        search();
    });

    getTimeLimitRule();

    initProcTreeGrid();
});

function masterFormatter(value, row, index) {
    var isCheack = '';
    if (row.isMasterDef == '1') isCheack = 'checked="checked"';
    var isMaster = '<span><span class="m-switch m-switch--success"><label><input type="checkbox"' + isCheack + 'onclick="setProcMaster(\'' + row.id + '\',\'' + row.isMasterDef + '\',this)"><span></span> </label></span></span>';
    return isMaster;

}

function timeLimitUnitFormatter(value, row, index) {
    console.log(row);
    var timeUnit = row.timeLimitUnit;
    var unit;
    console.log(unit);
    if(timeUnit=="d"){
        unit = value+" 天";
    }else if(timeUnit=="m"){
        unit = value+" 分钟";
    }
    return unit;
}

function limitIsWorkDayFormatter(value, row, index) {
    if(!value){
        return "";
    }else{
        return (value=="0")?"否":"是";
    }
}

function privFormater(value, row, index) {
    return row.enableAppPriv == '1' ? "启用" : "不启用";
}

//选择流程定义
function chooseProcDef(appProcDeModelId) {
    window.open(ctx + '/bpm/admin/modeler/index.html#/editor/'+ appProcDeModelId, '_blank');
    // window.open(ctx + '/bpm/admin/modeler/index.html#/editor/'+ appProcDeModelId+"/"+ appId, '_blank');
}


var currentDefKey="";
var currentAppFlowdefId="";
var subprocessTabe="";
var listenerList="";
function showSubprocess(appFlowdefId,defKey) {
    $("#subprocess_list_tb").bootstrapTable('destroy');
    var searchKey=$('#subprocessKeyword').val();
    currentDefKey=defKey;
    currentAppFlowdefId=appFlowdefId;
    subprocessTabe = $("#subprocess_list_tb").bootstrapTable({
        url: ctx + "/rest/mind/subProcessDefs.do",
        queryParamsType: "",
        pagination: false,
        queryParams: function() {
            return {
                "appFlowdefId": appFlowdefId,
                "appId": appId,
                "defKey":defKey,
                "keyword":searchKey
            }
        },
        columns: [
            {
                checkbox: true,
                align: "center"
            },{
            field: "nodeName",
            title: "触发的节点",
            align: "left",
            width: "70"
        }, {
            field: "procName",
            title: "被触发的流程",
            align: "left",
            width: "120",
                formatter:function(value,row,index){
                    if(value){
                        return value;
                    }else{//如果为空，则有可能是外部流程，保存的是外部市局流程的监听器包名+类名，需要解析。目前在前端解析，因为后台代码在bpm包内。
                        var rs="";
                        if(!listenerList||listenerList.length==0){
                            $.ajax({
                                url:ctx+'/act/sto/flow/listener/process/list?procKey='+defKey,
                                type: 'GET',
                                async:false,
                                success: function (result) {
                                    listenerList=result;
                                }
                            });
                        }
                        for (var i=0;i<listenerList.length;i++){
                            var entity=listenerList[i];
                            if(row.triggerAppFlowdefId==entity.className){
                                rs= entity.listenerName;
                                break;
                            }
                        }
                        return rs;
                    }
                }
        }, {
            field: "triggerEvent",
            title: "触发时机",
            align: "left",
            width: "50",
            formatter:function (value, row, index) {
                var val=row.triggerEvent;
                if(value=="create"){
                    val= "任务创建时触发";
                }else if(value=="complete"){
                    val= "任务完成时触发";
                }else if(value=="take"){
                    val= "到达时触发";
                }else if(value=="start"){
                    val= "节点开始时触发";
                }else if(value=="end"){
                    val= "节点结束时触发";
                }
                return val;
            }
        },{
            field:'',
                title:'操作',
                align:"center",
                width:"50",
                formatter:function (value, row, index) {
                    var a1= '<a href="javascript:editSubTrigger(\'' + row.triggerId + '\')" ' +
                    'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                    'title="编辑配置"><i class="la la-edit"></i>' +
                    '</a>';
                    var a2='<a href="javascript:delSubTrigger(\'' + row.triggerId + '\')" ' +
                    'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                    'title="删除"><i class="la la-trash"></i>' +
                    '</a>';
                    return a1+a2;
                }
            }
        ]

    });
    subprocessTabe.bootstrapTable('refresh');
    $("#subprocess_list_modal").modal("show");
}

function showStartEl(appFlowdefId,index){
    console.log(currRows);

    $('#tplAppDefElDialog').modal('show');
    appFlowdefIdVal = appFlowdefId;
    var row = currRows[index];
    var startEl = row.startEl;

    $("#startEl_select").empty();
    $("#startEl_select").append('<option value="">请选择</option>');

    $.ajax({
        url: ctx + '/aea/item/state/getProcStartCondItemStates.do',
        type: 'POST',
        data: {stateVerId: currentStateVerId},
        success: function (data) {
            if (data != '' && data.length > 0) {
                $.each(data, function (i, n) {
                    if(startEl==("${form.startProcItemStateId=='"+n['itemStateId']+"'}")){
                        $("#startEl_select").append(" <option value=\"\${form.startProcItemStateId==\'" + n['itemStateId'] + "\'}\"  selected>" + n['stateName'] + "</option>");
                    }else if(n['value']!=''){
                        $("#startEl_select").append(" <option value=\"${form.startProcItemStateId=='" + n['itemStateId'] + "'}\">" + n['stateName'] + "</option>");
                    }
                });
            }
        },
        error: function () {
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}

function editLimit(currLimit,id){
    $('#edit_timeLimit').val();
    $('#flowDefId_hidden').val();

    $('#editLimitDialog').modal('show');
    $('#edit_timeLimit').val(currLimit);
    $('#flowDefId_hidden').val(id);
}


function saveTimeLimit() {
    var timeLimit = $('#edit_timeLimit').val();
    var flowDefId = $('#flowDefId_hidden').val();
    var nodeId = $('#nodeId_hidden').val();
    var timeruleId = $("#timeruleId_").val();
    $.ajax({
        url: ctx + '/rest/act/tpl/app/setTimeLimit',
        type: 'POST',
        data: {appFlowdefId: flowDefId, nodeId:nodeId, timeLimit: timeLimit, timeruleId: timeruleId},
        success: function (result) {
            if (result.success) {
                swal('提示信息', "保存成功！", 'success');
                $('#editLimitDialog').modal('hide');
                refresh();
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function () {
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}

function getTimeLimitRule() {
    $.get(ctx + "/rest/act/sto/timerule/getActStoTimeruleByOrgId", function (data) {
        if (data.success) {
            timelimitRule = data.content;
            for (var i in timelimitRule) {
                var html = "<option value='" + timelimitRule[i].timeruleId + "'>" + timelimitRule[i].timeruleName + "</option>";
                $("#timeruleId").append(html);
                $("#timeruleId_").append(html);
            }
        }
    })
}

//ajax加载下拉框数据
function loadStageSelect(url,selectTagId,value) {
    $(selectTagId).empty();
    $(selectTagId).append('<option value="">请选择</option>');
    $.ajax({
        type: 'post',
        url: url,
        dataType: "Json",
        success: function (data) {
            if (data != '' && data.length > 0) {
                $.each(data, function (i, n) {
                    if(value == ("${form.starParStageId=='"+n['stageId']+"'}")){
                        $(selectTagId).append(" <option value=\"\${form.starParStageId==\'" + n['stageId'] + "\'}\"  selected>" + n['stageName'] + "</option>");
                    }else{
                        $(selectTagId).append(" <option value=\"\${form.starParStageId==\'" + n['stageId'] + "\'}\">" + n['stageName'] + "</option>");
                    }
                });
            }
        }
    })
}


//ajax加载下拉框数据
function loadItemSelect(url,selectTagId,value) {
    $(selectTagId).empty();
    $(selectTagId).append('<option value="">请选择</option>');
    $.ajax({
        type: 'post',
        url: url,
        dataType: "Json",
        success: function (data) {
            if (data != '' && data.length > 0) {
                $.each(data, function (i, n) {
                    if(value == ("${form.starIteminstId=='"+n['itemId']+"'}")){
                        $(selectTagId).append(" <option value=\"\${form.starIteminstId==\'" + n['itemId'] + "\'}\"  selected>" + n['itemName'] + "</option>");
                    }else{
                        $(selectTagId).append(" <option value=\"\${form.starIteminstId==\'" + n['itemId'] + "\'}\">" + n['itemName'] + "</option>");
                    }
                });
            }
        }
    })
}

function startEl_save(){
    var startEl = $('#startEl_select').val();
    $.ajax({
        url: ctx + '/rest/act/tpl/app/setTplAppFlowdefStartEl',
        type: 'POST',
        data: {appFlowdefId: appFlowdefIdVal, startEl: startEl},
        success: function (result) {
            if (result.success) {
                swal('提示信息', "保存成功！", 'success');
                $('#tplAppDefElDialog').modal('hide');
                refresh();
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function () {
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}

var modelId = null;

function showCreateProcess(){

  /*  $("#procForm").find('input,select').not('input[name="limitIsWorkDay"]').each(function() {
        $(this).val('');
    });*/
    $('input[name="itemLimits"][value="0"]').prop("checked",true);
    $('input[name="limitIsPro"][value="0"]').prop("checked",true);
    $('#showStageOrItems').prop("hidden",true);
    $('#isLimits').prop("hidden",true);
    //加载事项/阶段列表
    if("stage"==currentBusiType){
        onloadStageOrItems();
    }
    //$('input[name="limitIsWorkDay"][value="0"]').prop("checked",true);
    $('#createProcessDialog').modal('show');
    $('#procForm')[0].reset();
    $('#procForm').validate().resetForm();
}

function showLimits(type){
    if(type==0){
        $('#isLimits').prop("hidden",true);
        $('input[name="itemLimits"][value="0"]').prop("checked",true);
    }else{
        $('#isLimits').prop("hidden",false);
    }
}

function showStageOrItems(type) {
    if("stage"==currentBusiType){
        if(type==0){
            $('#showStageOrItems').prop("hidden",true);
            //$('input[name="itemLimits"][value="0"]').prop("checked",true);
        }else{
            $('#showStageOrItems').prop("hidden",false);
        }
    }
}

//全局变量  stageAndItems:用来存当前阶段的阶段和事项的select控件，便于加载列表时，只查询一次即可
var stageAndItems="";
function onloadStageOrItems() {
    $.ajax({
        url: ctx + '/aea/approve/getAeaStateAndItemsByStageId.do',
        type: 'POST',
        data: {itemVerId: currentBusiId,currentBusiType:currentBusiType},
        async: false,
        success: function (result) {
            $('#stageOrItems').empty();
            //$('#stageOrItems').append('<option value="">请选择</option>');
            var options="";
            if (result && result.success && result.content) {
                var stage=result.content.stage;
                var items=result.content.items;
                if(stage){
                    options+='<option title="阶段" value="'+stage.stageId+'">'+stage.stageName+'</option>';
                }
                if(items && items.length>0){
                    for (var i=0;i<items.length;i++){
                        var item=items[i];
                        options+='<option title="事项" value="'+item.itemVerId+'">'+item.itemName+'</option>';
                    }
                }
            }
            $('#stageOrItems').append(options);
            stageAndItems=options;
        }
    });
}

//保存流程模板和流程关联关系
function saveFlowdef(limitIsWorkDay,timeLimitUnit,timeLimit,key,isMaster) {debugger
    var procDefJsonData = [];
    procDefJsonData.push('{procdefKey:"' + key + '",enableAppPriv:"1",isMaster:"' + isMaster + '",priorityOrder:' + 0 + ',procType:"b",limitIsWorkDay:"'+limitIsWorkDay+'",timeLimitUnit:"'+timeLimitUnit+'",timeLimit:'+timeLimit+'}');
    procDefJsonData = "[" + procDefJsonData + "]";
    $.ajax({
        url: ctx + '/admin/act/tpl/app/saveActTplAppData.do',
        type: 'POST',
        data: {procDefJsonData: procDefJsonData, appId: appId, operate: 'editProcInfo'},
        success: function (result) {
            $('#createProcessDialog').modal('hide');
            refresh();
            agcloud.ui.metronic.showConfirm("保存成功,是否打开流程设计器？",newProcWin);
        },
        error: function () {
            swal('错误信息', '服务器异常, 保存流程关联信息失败！', 'error');
        }
    });
}


$('#procForm').validate({// 定义校验规则
    rules: {
        name: {
            required: true
        },
        key: {
            required: true
        }
    },
    messages: {
        name: {
            required: '<font color="red" style="padding-top:5px">此项必填！</font>'
        },
        key: {
            required: '<font color="red" style="padding-top:5px">此项必填！</font>'
        }
    },
    submitHandler:function(form){
        saveNewProcDef();
    }
});


//保存新建流程定义
function saveNewProcDef(){
    var data = {name:"",key:"",description:"",categoryId:"",modelType:"0"};
    data.name = $('#procName').val();
    data.key = $('#procKey').val();
    data.description = $('#procDescription').val();
    //var limitIsWorkDay=$('input[name="limitIsWorkDay"]:checked').val();
    //var timeLimitUnit=$('#timeLimitUnit').find('option:selected').val();
    //var timeLimit=$('#timeLimit').val();
    var limitIsWorkDay="";
    var timeLimitUnit="";
    var timeLimit=0;
    var itemLimits=$('input[name="itemLimits"]:checked').val();
    var limitIsPro=$('input[name="limitIsPro"]:checked').val();
    if(itemLimits==1 && limitIsPro==1){
        //根据事项Id查询时限，并赋值，采用同步请求
        var busiId=currentBusiId;
        var busiType=currentBusiType;
        if(busiType=="stage"){
            var title=$('#stageOrItems').find("option:selected").attr("title");
            if("阶段"==title){
                busiId=$('#stageOrItems').find("option:selected").val();
            }else if("事项"==title){
                busiType="item";
                busiId=$('#stageOrItems').find("option:selected").val();
            }
        }
        $.ajax({
            url: ctx + '/aea/approve/getAeaStateOrItemById.do',
            type: 'POST',
            data: {currentBusiId:busiId,currentBusiType:busiType},
            async: false,
            success: function (result) {
                if (result && result.success && result.content) {
                    timeLimit=result.content.dueNum;
                    var unit="";    //1 工作日 2 自然日  3 小时  4 分钟
                    if("item"==busiType){
                        unit=result.content.bjType;
                    }else if("stage"==busiType){
                        unit=result.content.dueUnit;
                    }
                    if(unit==1){
                        limitIsWorkDay="1";
                        timeLimitUnit="d";
                    }else if(unit==2){
                        limitIsWorkDay="0";
                        timeLimitUnit="d";
                    }else if(unit==3){
                        limitIsWorkDay="1";
                        timeLimitUnit="m";
                        timeLimit=timeLimit*60;
                    }else if(unit==4){
                        limitIsWorkDay="1";
                        timeLimitUnit="m";
                    }
                } else {
                    swal('错误信息', result.message, 'error');
                }
            }
        });
    }
    debugger
    $.ajax({
        url: ctx + '/app/rest/models',
        type: 'POST',
        contentType:"application/json",
        dataType:"json",
        data: JSON.stringify(data),
        async:false,
        success: function (result) {
            if (result) {
                modelId = result.id;

                saveFlowdef(limitIsWorkDay,timeLimitUnit,timeLimit,$('#procKey').val(),"0");
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function (result) {
            var errMsg=JSON.parse(result.responseText).message;
            var reg = "key already exists";
            if(errMsg.indexOf(reg)!=-1){
                errMsg="该流程编号已存在，请直接导入已有流程或修改流程编号！";
            }
            swal('错误信息', errMsg, 'error');
        }
    });
}

//打开流程设计器窗口
function newProcWin(){
    //在异步回调中没法用代码打开窗口，因为$http没法同步请求，改为用定时器方式获取返回值，再根据请求返回状态清除定时器
    var interval = window.setInterval(function () {
        if(modelId && modelId != "httpError"){
            window.clearInterval(interval);
            window.open(ctx + '/bpm/admin/modeler/index.html#/editor/' + modelId+"/"+appId);
        }else if("httpError" == result.id){
            window.clearInterval(interval);
        }
    },100);

}

function setProcMaster(appFlowdefId, isMaster, thisInput) {
    var msg = '确定要设为主流程吗？';
    swal({
        title: '此操作影响：',
        text: msg,
        type: 'info',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: ctx + '/admin/act/tpl/app/setPorcMaster.do',
                type: 'POST',
                data: {appFlowdefId: appFlowdefId, appId: appId},
                success: function (result) {
                    if (result.success) {
                        // loadProcdefDatas(thisAppId_procdef)
                        refresh();
                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                }
            });
        } else {
            isMaster == '1' ? $(thisInput).prop("checked", true) : $(thisInput).prop("checked", false);
        }
    });
}


function delActTplAppProc(appProcdefId) {
    var msg = '确定要移除该流程吗？';

    swal({
        title: '此操作影响：',
        text: msg,
        type: 'info',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: ctx + '/admin/act/tpl/app/deleteActTplAppProc.do',
                type: 'POST',
                data: {id: appProcdefId},
                success: function (result) {
                    if (result.success) {
                        refresh();
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
}



var tplAppProcChooseDialog_tb = null;
function searchTplAppProcChooseDataV2(){
    onloadStageOrItems();
    var data = {appId: appId};
    if (tplAppProcChooseDialog_tb != null) tplAppProcChooseDialog_tb.bootstrapTable("destroy");
    var dataJSONArray = getDataJSONArray(data, ctx + '/admin/act/tpl/app/getActStoProcList.do', '', '没有更多流程!');
    tplAppProcChooseDialog_tb = $('#tplAppProcChooseDialog_tb').bootstrapTable({
        url:ctx + '/admin/act/tpl/app/getActStoProcList.do',
        data:dataJSONArray,
        columns: [
            /*{
                checkbox: true,
                field: '#', // 数据字段
                align: "center",
                title: '#', // 页面字段显示
                sortable: false, // 禁用排序
                width: 50,  // 宽度,单位: px
                textAlign: 'center', // 字段列标题和数据排列方式
                selector: {class: 'm-checkbox--solid m-checkbox--brand'},
            },*/
            {
                field: 'id', // 数据字段
                visible: false
            },
            {
                field: 'name', // 数据字段
                title: '流程名称', // 页面字段显示
                sortable: false, // 禁用排序
                width: 400,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
            }, {
                field: 'key', // 数据字段
                title: '流程KEY', // 页面字段显示
                sortable: false, // 禁用排序
                width: 300,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
            }, {
                field: '', // 数据字段
                title: '是否导入流程', // 页面字段显示
                sortable: false, // 禁用排序
                width: 60,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
                formatter:isUploadProcFormatter
            }, {
                field: '', // 数据字段
                title: '是否启动流程时限', // 页面字段显示
                sortable: false, // 禁用排序
                width: 60,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
                formatter:isProcLimitFormatter
            }, {
                field: '', // 数据字段
                title: '是否沿用事项/阶段的时限', // 页面字段显示
                sortable: false, // 禁用排序
                width: 60,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
                formatter:isItemsOrStageLimitsFormatter
            }, {
                field: '', // 数据字段
                title: '沿用时限对应的事项/阶段', // 页面字段显示
                sortable: false, // 禁用排序
                width: 400,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
                formatter:itemsOrStageFormatter
            }
        ],
        sortOrder: "asc",
        pageNumber:1,
        pageSize: 8,
        pageList: [8,10, 20],
        sidePagination: 'client',
        pagination:true,
        search:true,
        maintainSelected:true
    })
}


function isUploadProcFormatter(value, row, index) {
    var isCheack = '';
    //if (row.isMasterDef == '1') isCheack = 'checked="checked"';
    var isMaster = '<span><span class="m-switch m-switch--success"><label><input type="checkbox"' + isCheack + 'onclick="selProc(\'' + row.key + '\',this)"><span></span> </label></span></span>';
    return isMaster;

}

function isProcLimitFormatter(value, row, index) {
    var isCheack = '';
    //if (row.isMasterDef == '1') isCheack = 'checked="checked"';
    var isMaster = '<span><span class="m-switch m-switch--success"><label><input disabled type="checkbox"' + isCheack + 'onclick="setProcLimit(\'' + row.key + '\',this)"><span></span> </label></span></span>';
    return isMaster;

}

function isItemsOrStageLimitsFormatter(value, row, index) {
    var isCheack = '';
    //if (row.isMasterDef == '1') isCheack = 'checked="checked"';
    var isMaster = '<span><span class="m-switch m-switch--success"><label><input disabled type="checkbox"' + isCheack + 'onclick="setItemsOrStageLimits(\'' + row.key + '\',this)"><span></span> </label></span></span>';
    return isMaster;

}

function itemsOrStageFormatter(value, row, index) {
    var isCheack = '';
    //if (row.isMasterDef == '1') isCheack = 'checked="checked"';
    var isMaster = '<div class="form-group"><div class="col-sm-12">' +
        '<select disabled class="form-control" onchange="selItemOrStage(\''+ row.key +'\' ,this)"> <option> 请选择</option>' +
        stageAndItems+
        '</select>' +
        '</div></div>';
    return isMaster;

}

//全局变量 procIds ，存储导入的流程Id-选择的事项版本VerId 键值对
var procIdsMap={};
function selProc(key,thisInput) {
    var isCheck=$(thisInput).prop("checked");
    if(isCheck){
        procIdsMap[key]="";
    }else{
        //removeElementsInList(procIds,id);
        if(procIdsMap.hasOwnProperty(key)) delete(procIdsMap[key]);
    }
    changeNextDisabled(key,thisInput);
}

function selItemOrStage(id,thisInput) {
    var val=$(thisInput).find("option:selected").val();
    procIdsMap[id]=val;
}

function changeNextDisabled(id,thisInput) {
    var isCheck=$(thisInput).prop("checked");
    var kj=$(thisInput).parent().parent().parent().parent("td").next().find("input,select");
    if(kj.length>0){
        if(isCheck){
            $(kj).attr("disabled",false);
        }else{
            if(procIdsMap.hasOwnProperty(id)) procIdsMap[id]="";
            $(kj).prop("checked",false);
            $(kj).attr("disabled",true);
            var option=$(kj).find("option:selected");
            if(option.length>0){
                $(option).prop("selected",false);
            }
        }
        changeNextDisabled(id,kj[0]);
    }else{
        return;
    }
}

function setProcLimit(id,thisInput) {
    changeNextDisabled(id,thisInput);
}


function setItemsOrStageLimits(id,thisInput) {
    changeNextDisabled(id,thisInput);
}

function reloadTplAppProcData() {
    procIdsMap={};
    searchTplAppProcChooseDataV2();
}

function addChooseProc() {
    procIdsMap={};
    // searchTplAppProcChooseData();
    searchTplAppProcChooseDataV2();

    $('#tplAppProcChooseDialog').modal('show');
}

function getDataJSONArray(data, url, dataJSONArray, infoMsg) {
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        cache: false,
        async: false,
        success: function (jsonData) {
            if (jsonData != null && jsonData.length > 0) {
                dataJSONArray = jsonData;
            } else {
                dataJSONArray = '';
                // swal('提示信息', infoMsg, 'info');
            }
        },
        error: function () {
            swal('错误信息', '服务器异常!', 'error');
        }
    });
    return dataJSONArray;
}


function getTableGird(tableId, dataJSONArray, columns) {
    return $('#' + tableId).mDatatable({
        data: {
            type: 'local',
            source: dataJSONArray,
            pageSize: 10
        },
        layout: {
            theme: 'default', // datatable theme
            scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
            footer: false // display/hide footer
        },
        sortable: true,
        pagination: true,
        toolbar: {  // 工具栏
            items: {
                pagination: {
                    pageSizeSelect: [10]
                }
            }
        },
        columns: columns
    });
}


function confirmSelectProcSave() {
    /*var checkboxs = $("#tplAppProcChooseDialog_tb").bootstrapTable('getSelections');
    if(checkboxs==null || checkboxs.length==0) {
        $('#tplAppProcChooseDialog').modal('hide');
        return false;
    }
    var procDefJsonData = [];
    var index = 0;
    for (var i = 0; i < checkboxs.length; i++) {
        var e = checkboxs[i];
        var procdefKey;
            index++;
            procdefKey = e.key;
            var oldProc = $('#tplAppProcChooseDialog_tb tr');
            var isMasterProcdef = '0';
            procDefJsonData.push('{procdefKey:"' + procdefKey + '",enableAppPriv:"1",isMaster:"' + isMasterProcdef + '",priorityOrder:' + (index + oldProc.length) + ',procType:"b"}');
    }
*/
    debugger
    if(procIdsMap==null || procIdsMap.length==0) {
        $('#tplAppProcChooseDialog').modal('hide');
        return false;
    }
    var procDefJsonData = [];
    var index = 0;
    for (var procdefKey in procIdsMap) {
        if(procIdsMap.hasOwnProperty(procdefKey)){
            index++;
            //var oldProc = $('#tplAppProcChooseDialog_tb tr');
            var timeLimit=0;
            var timeLimitUnit="";
            var limitIsWorkDay="";
            var isMasterProcdef = '0';
            $.ajax({
                url: ctx + '/aea/approve/getAeaStateOrItemByIdNoType.do',
                type: 'POST',
                data: {itemOrStateId:procIdsMap[procdefKey]},
                async: false,
                success: function (result) {
                    if (result && result.success && result.content) {
                        var json=result.content.stage;
                        var unit="";    //1 工作日 2 自然日  3 小时  4 分钟
                        if(json){
                            unit=json.dueUnit;
                        }else{
                            json=result.content.item;
                            unit=json.bjType;
                        }
                        timeLimit=json.dueNum;

                        if(unit==1){
                            limitIsWorkDay="1";
                            timeLimitUnit="d";
                        }else if(unit==2){
                            limitIsWorkDay="0";
                            timeLimitUnit="d";
                        }else if(unit==3){
                            limitIsWorkDay="1";
                            timeLimitUnit="m";
                            timeLimit=timeLimit*60;
                        }else if(unit==4){
                            limitIsWorkDay="1";
                            timeLimitUnit="m";
                        }
                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                }
            });
            procDefJsonData.push('{procdefKey:"' + procdefKey + '",enableAppPriv:"1",isMaster:"' + isMasterProcdef + '",priorityOrder:' + (index) + ',procType:"b",limitIsWorkDay:"'+limitIsWorkDay+'",timeLimitUnit:"'+timeLimitUnit+'",timeLimit:'+timeLimit+'}');
        }
    }

    procDefJsonData = "[" + procDefJsonData + "]";
    $.ajax({
        url: ctx + '/admin/act/tpl/app/saveActTplAppData.do',
        type: 'POST',
        data: {procDefJsonData: procDefJsonData, appId: appId, operate: 'editProcInfo'},
        success: function (result) {
            if (result.success) {
                $('#tplAppProcChooseDialog').modal('hide');
                refresh();
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function () {
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}


function startEl_flowType_change(){
    var flowType = $("input[name='flowType']:checked").val();
    if(flowType=='stage'){
        $('#startEl_stage').css('display','block');
        $('#startEl_not_stage').css('display','none');
    }else{
        $('#startEl_stage').css('display','none');
        $('#startEl_not_stage').css('display','block');
    }
}

function searchtplAppProcChooseKeyword() {
    $("#tplAppProcChooseDialog_tb").bootstrapTable('resetSearch',$("#uploadFlowForm #tplAppProcChooseKeyword").val());
}


function cleartplAppProcChooseKeyword() {
    $("#tplAppProcChooseKeyword").val("");
    $("#tplAppProcChooseDialog_tb").bootstrapTable('resetSearch',"");

}

function convertToKey(_this){
    var code = pinyin.getCamelChars(_this.value);
    $('#procKey').val(code);
}