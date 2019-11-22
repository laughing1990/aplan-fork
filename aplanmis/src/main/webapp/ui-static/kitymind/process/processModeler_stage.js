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
    if (currRows && currRows.length > 0) {
        var i = 0;
        $.each(currRows, function (index, e) {
            if (row.id == e.id) {
                i++;
                e.startEl = row.startEl;
            }
        });
        if (i == 0) {
            currRows.push(row);
        }
    } else {
        currRows.push(row);
    }
    var editLimitBtn = '<a href="javascript:editLimit(' + "'" + row.timeLimit + "','" + row.timeruleId + "','" + row.appFlowdefId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="配置时限">' +
        '<i class="la la-calendar-check-o"></i>' + '</a>';

    var activateBtn = '<a href="javascript:showStartEl(' + "'" + row.id + "'," + index + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="启动情形">' +
        '<i class="la la-diamond"></i>' +
        '</a>';
    var editBtn = '<a href="javascript:chooseProcDef(\'' + row.modelId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="编辑流程"><i class="la la-edit"></i>' +
        '</a>';

    var priBtn = '<a target="_blank" href="' + ctx + '/bpm/admin/template/authConfigForm.html?appFlowdefId=' + row.id + '&appId=' + appId + '"' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="权限配置"><i class="la la-cog"></i>' +
        '</a>';

    var subprocessBtn = '<a href="javascript:showSubprocess(\'' + row.appFlowdefId + '\',\'' + row.defKey + '\');" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="子流程配置"><i class="la la-cogs"></i>' +
        '</a>';

    var deleteBtn = '<a href="javascript:delActTplAppProc(\'' + row.appFlowdefId + '\');" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="删除"><i class="la la-trash"></i>' +
        '</a>';

    if (currentBusiType == 'item') {
        return editLimitBtn + activateBtn + editBtn + priBtn + subprocessBtn + deleteBtn;
    } else {
        return editLimitBtn + editBtn + priBtn + subprocessBtn + deleteBtn;
    }
}

function search() {
    commonQueryParams['keyword'] = $("#keyword").val();
    // commonQueryParams['busiType'] = currentBusiType;
    // commonQueryParams['busiId'] = currentBusiId;
    commonQueryParams['appId'] = appId;
    $("#process_design_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#process_design_tb").bootstrapTable('refresh');       //无参数刷新
}

function refresh() {
  // search();
  vm.getProcessDefTree();
}

$(function () {

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

    $("#clearBtn").click(function () {
        $('#search_item_process_design')[0].reset();
      // search();
      vm.getProcessDefTree();
    });

    $("#searchBtn").click(function () {
      vm.getProcessDefTree();
      // search();
    });

    getTimeLimitRule();//获取时限规则列表

  // initProcTreeGrid();

    leftRIghtSelectEvent();

});

function masterFormatter(value, row, index) {
    var isCheack = '';
    if (row.isMasterDef == '1') isCheack = 'checked="checked"';
    var isMaster = '<span><span class="m-switch m-switch--success"><label><input type="checkbox"' + isCheack + 'onclick="setProcMaster(\'' + row.id + '\',\'' + row.isMasterDef + '\',this)"><span></span> </label></span></span>';
    return isMaster;

}

function timeLimitUnitFormatter(value, row, index) {
    //console.log(row);
    var timeUnit = row.timeLimitUnit;
    var unit;
    //console.log(unit);
    if (timeUnit == "d") {
        unit = value + " 天";
    } else if (timeUnit == "m") {
        unit = value + " 分钟";
    }
    return unit;
}

function limitIsWorkDayFormatter(value, row, index) {
    if (!value) {
        return "";
    } else {
        return (value == "0") ? "否" : "是";
    }
}

function privFormater(value, row, index) {
    return row.enableAppPriv == '1' ? "启用" : "不启用";
}

//选择流程定义
function chooseProcDef(appProcDeModelId) {
    window.open(ctx + '/bpm/admin/modeler/index.html#/editor/' + appProcDeModelId, '_blank');
    // window.open(ctx + '/bpm/admin/modeler/index.html#/editor/'+ appProcDeModelId+"/"+ appId, '_blank');
}


var currentDefKey = "";
var currentAppFlowdefId = "";
var subprocessTabe = "";
var listenerList = "";

function showSubprocess(appFlowdefId, defKey,nodeName,nodeId) {
    //20190829全局变量用于控制节点配置子流程
    if (nodeName) {
        selectNodeName = nodeName;
    }
    if (nodeId) {
        selectNodeId = nodeId;
    }
    singleConfig = true;

    $("#subprocess_list_tb").bootstrapTable('destroy');
    var searchKey = $('#subprocessKeyword').val();
    currentDefKey = defKey;
    currentAppFlowdefId = appFlowdefId;
    subprocessTabe = $("#subprocess_list_tb").bootstrapTable({
        url: ctx + "/rest/mind/subProcessDefs.do",
        queryParamsType: "",
        pagination: false,
        queryParams: function () {
            return {
                "appFlowdefId": appFlowdefId,
                "appId": appId,
                "defKey": defKey,
                "nodeId": selectNodeId,
                "keyword": searchKey
            }
        },
        columns: [
            {
                checkbox: true,
                align: "center"
            }, {
                field: "nodeName",
                title: "触发的节点",
                align: "left",
                width: "60"
            }, {
                field: "procName",
                title: "被触发的流程",
                align: "left",
                width: "150",
                formatter: function (value, row, index) {
                    if (value) {
                        return value;
                    } else {//如果为空，则有可能是外部流程，保存的是外部市局流程的监听器包名+类名，需要解析。目前在前端解析，因为后台代码在bpm包内。
                        var rs = "";
                        if (!listenerList || listenerList.length == 0) {
                            $.ajax({
                                url: ctx + '/act/sto/flow/listener/process/list?procKey=' + defKey,
                                type: 'GET',
                                async: false,
                                success: function (result) {
                                    listenerList = result;
                                }
                            });
                        }
                        for (var i = 0; i < listenerList.length; i++) {
                            var entity = listenerList[i];
                            if (row.triggerAppFlowdefId == entity.className) {
                                rs = entity.listenerName;
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
                width: "60",
                formatter: function (value, row, index) {
                    var val = row.triggerEvent;
                    if (value == "create") {
                        val = "任务创建时触发";
                    } else if (value == "complete") {
                        val = "任务完成时触发";
                    } else if (value == "take") {
                        val = "到达时触发";
                    } else if (value == "start") {
                        val = "节点开始时触发";
                    } else if (value == "end") {
                        val = "节点结束时触发";
                    }
                    return val;
                }
            },
            {
                field: "itemName",
                title: "关联事项",
                align: "left",
                width: "200",
                formatter: function (value, row, index) {
                    if(value){
                      return "【实施事项】" + value;
                    }
                }
            },{
                field: '',
                title: '操作',
                align: "center",
                width: "50",
                formatter: function (value, row, index) {
                    var a1 = '<a href="javascript:editSubTrigger(\'' + row.triggerId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="编辑配置"><i class="la la-edit"></i>' +
                        '</a>';
                    var a2 = '<a href="javascript:delSubTrigger(\'' + row.triggerId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                        '</a>';
                    return a1 + a2;
                }
            }
        ]

    });
    $("#subprocess_list_modal").modal("show");
}

function showStartEl(appFlowdefId, index) {
    //console.log(currRows);

    $('#tplAppDefElDialog').modal('show');
    appFlowdefIdVal = appFlowdefId;
    var row = currRows[index];
    var startEl = row.startEl;

    $("#startEl_select").empty();
    $("#startEl_select").append('<option value="">请选择</option>');
  var url;
  var data;
  var stateKey;
  if (currentBusiType == "item") {
    url = ctx + '/aea/item/state/getProcStartCondItemStates.do';
    data = {stateVerId: currentStateVerId};
    stateKey = 'itemStateId';
  } else {
    url = ctx + '/admin/aea/stage/state/getProcStartCondStageStates.do';
    data = {stageId: currentBusiId};
    stateKey = 'parStateId';
  }
    $.ajax({
      url: url,
      data: data,
        type: 'POST',
        success: function (data) {
            if (data != '' && data.length > 0) {
                $.each(data, function (i, n) {
                  if (startEl == ("${form.stateinsts['" + n[stateKey] + "']==true}")) {
                    $("#startEl_select").append(" <option value=\"${form.stateinsts['" + n[stateKey] + "']==true}\"  selected>" + n['stateName'] + "</option>");
                  } else if (n['value'] != '') {
                    $("#startEl_select").append(" <option value=\"${form.stateinsts['" + n[stateKey] + "']==true}\">" + n['stateName'] + "</option>");
                  }
                });
            }
        },
        error: function () {
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}


function editLimit(currLimit, timeruleId, appFlowdefId,nodeId) {
    $('#edit_timeLimit').val();
    $('#flowDefId_hidden').val();

    $('#editLimitDialog').modal('show');
    $('#edit_timeLimit').val(currLimit);
    $('#flowDefId_hidden').val(appFlowdefId);
    if(nodeId){
        $('#nodeId_hidden').val(nodeId);
    }else{
        $('#nodeId_hidden').val('');
    }
    if (timeruleId != null) {
        $("#timeruleId_").find("option[value=" + timeruleId + "]").attr("selected", true);
    }
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
                $("select[name=timeruleId]").append(html);
            }
        }
    })
}

//ajax加载下拉框数据
function loadStageSelect(url, selectTagId, value) {
    $(selectTagId).empty();
    $(selectTagId).append('<option value="">请选择</option>');
    $.ajax({
        type: 'post',
        url: url,
        dataType: "Json",
        success: function (data) {
            if (data != '' && data.length > 0) {
                $.each(data, function (i, n) {
                    if (value == ("${form.starParStageId=='" + n['stageId'] + "'}")) {
                        $(selectTagId).append(" <option value=\"\${form.starParStageId==\'" + n['stageId'] + "\'}\"  selected>" + n['stageName'] + "</option>");
                    } else {
                        $(selectTagId).append(" <option value=\"\${form.starParStageId==\'" + n['stageId'] + "\'}\">" + n['stageName'] + "</option>");
                    }
                });
            }
        }
    })
}


//ajax加载下拉框数据
function loadItemSelect(url, selectTagId, value) {
    $(selectTagId).empty();
    $(selectTagId).append('<option value="">请选择</option>');
    $.ajax({
        type: 'post',
        url: url,
        dataType: "Json",
        success: function (data) {
            if (data != '' && data.length > 0) {
                $.each(data, function (i, n) {
                    if (value == ("${form.starIteminstId=='" + n['itemId'] + "'}")) {
                        $(selectTagId).append(" <option value=\"\${form.starIteminstId==\'" + n['itemId'] + "\'}\"  selected>" + n['itemName'] + "</option>");
                    } else {
                        $(selectTagId).append(" <option value=\"\${form.starIteminstId==\'" + n['itemId'] + "\'}\">" + n['itemName'] + "</option>");
                    }
                });
            }
        }
    })
}

function startEl_save() {
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

function showCreateProcess() {

    /*  $("#procForm").find('input,select').not('input[name="limitIsWorkDay"]').each(function() {
          $(this).val('');
      });*/
    //$('input[name="itemLimits"][value="0"]').prop("checked",true);
    //$('input[name="limitIsPro"][value="0"]').prop("checked",true);
    //$('#showStageOrItems').prop("hidden",true);
    //$('#isLimits').prop("hidden",true);
    //加载事项/阶段列表
    /*if("stage"==currentBusiType){
        onloadStageOrItems();
    }*/
    //$('input[name="limitIsWorkDay"][value="0"]').prop("checked",true);
    $('#createProcessDialog').modal('show');
    $('#procForm')[0].reset();
    $('#procForm').validate().resetForm();
}

/*function showLimits(type){
    if(type==0){
        $('#isLimits').prop("hidden",true);
        $('input[name="itemLimits"][value="0"]').prop("checked",true);
    }else{
        $('#isLimits').prop("hidden",false);
    }
}*/

/*function showStageOrItems(type) {
    if("stage"==currentBusiType){
        if(type==0){
            $('#showStageOrItems').prop("hidden",true);
            //$('input[name="itemLimits"][value="0"]').prop("checked",true);
        }else{
            $('#showStageOrItems').prop("hidden",false);
        }
    }
}*/

//全局变量  stageAndItems:用来存当前阶段的阶段和事项的select控件，便于加载列表时，只查询一次即可
/*var stageAndItems="";
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
}*/

//保存流程模板和流程关联关系
function saveFlowdef(limitIsWorkDay, timeLimitUnit, timeLimit, key, isMaster, timeruleId) {
    var procDefJsonData = [];
    procDefJsonData.push('{procdefKey:"' + key + '",enableAppPriv:"1",isMaster:"' + isMaster + '",priorityOrder:' + 0 + ',procType:"b",limitIsWorkDay:"' + limitIsWorkDay + '",timeLimitUnit:"' + timeLimitUnit + '",timeLimit:"' + timeLimit + '",timeruleId:"' + timeruleId + '"}');
    procDefJsonData = "[" + procDefJsonData + "]";
    $.ajax({
        url: ctx + '/admin/act/tpl/app/saveActTplAppData.do',
        type: 'POST',
        data: {procDefJsonData: procDefJsonData, appId: appId, operate: 'editProcInfo'},
        success: function (result) {
            $('#createProcessDialog').modal('hide');
            refresh();
            agcloud.ui.metronic.showConfirm("保存成功,是否打开流程设计器？", newProcWin);
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
        },
        timeLimit: {
            required: true
        }
    },
    messages: {
        name: {
            required: '<font color="red" style="padding-top:5px">此项必填！</font>'
        },
        key: {
            required: '<font color="red" style="padding-top:5px">此项必填！</font>'
        },
        timeLimit: {
            required: '<font color="red" style="padding-top:5px">此项必填！</font>'
        }
    },
    submitHandler: function (form) {
        saveNewProcDef();
    }
});

$('#addTimeGroupDialogForm').validate({// 定义校验规则
    rules: {
        timegroupName: {
            required: true
        },
        timeruleId: {
            required: true
        },
        timeLimit: {
            required: true
        }
    },
    messages: {
        timegroupName: {
            required: '<font color="red" style="padding-top:5px">此项必填！</font>'
        },
        timeruleId: {
            required: '<font color="red" style="padding-top:5px">此项必选！</font>'
        },
        timeLimit: {
            required: '<font color="red" style="padding-top:5px">此项必填！</font>'
        }
    },
    submitHandler: function (form) {
        addTimeGroupImpl();
    }
});


//保存新建流程定义
function saveNewProcDef() {
    var data = {name: "", key: "", description: "", categoryId: "", modelType: "0"};
    data.name = $('#procName').val();
    data.key = $('#procKey').val();
    data.description = $('#procDescription').val();
  var has = checkHasSpecialChar(data.key);
  if (has) {
    swal('错误信息', '流程编号不能包含特殊字符', 'error');
    return;
  }
    //var limitIsWorkDay=$('input[name="limitIsWorkDay"]:checked').val();
    //var timeLimitUnit=$('#timeLimitUnit').find('option:selected').val();
    //var timeLimit=$('#timeLimit').val();
    var limitIsWorkDay = "1";
    var timeLimitUnit = "d";
    var timeLimit = $('#timeLimit').val();
    var timeruleId = $("#timeruleId").val();
    if (timeLimit == undefined || timeLimit == null)
        timeLimit = "0";
    //var itemLimits = $('input[name="itemLimits"]:checked').val();
    //var limitIsPro = $('input[name="limitIsPro"]:checked').val();
    /*if(itemLimits==1 && limitIsPro==1){
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
    }*/

    $.ajax({
        url: ctx + '/app/rest/models',
        type: 'POST',
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(data),
        async: false,
        success: function (result) {
            if (result) {
                modelId = result.id;

                saveFlowdef(limitIsWorkDay, timeLimitUnit, timeLimit, $('#procKey').val(), "0", timeruleId);
            } else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function (result) {
            var errMsg = JSON.parse(result.responseText).message;
            var reg = "key already exists";
            if (errMsg.indexOf(reg) != -1) {
                errMsg = "该流程编号已存在，请直接导入已有流程或修改流程编号！";
            }
            swal('错误信息', errMsg, 'error');
        }
    });
}

//打开流程设计器窗口
function newProcWin() {
    //在异步回调中没法用代码打开窗口，因为$http没法同步请求，改为用定时器方式获取返回值，再根据请求返回状态清除定时器
    var interval = window.setInterval(function () {
        if (modelId && modelId != "httpError") {
            window.clearInterval(interval);
            window.open(ctx + '/bpm/admin/modeler/index.html#/editor/' + modelId + "/" + appId);
        } else if ("httpError" == result.id) {
            window.clearInterval(interval);
        }
    }, 100);

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

function searchTplAppProcChooseDataV2() {
    //onloadStageOrItems();
    var data = {appId: appId};
    if (tplAppProcChooseDialog_tb != null) tplAppProcChooseDialog_tb.bootstrapTable("destroy");
    // var dataJSONArray = getDataJSONArray(data, ctx + '/admin/act/tpl/app/getActStoProcList.do', '', '没有更多流程!');
    var dataJSONArray = getDataJSONArray(data, ctx + '/rest/act/tpl/app/getImportModelList', '', '没有更多流程!');
    tplAppProcChooseDialog_tb = $('#tplAppProcChooseDialog_tb').bootstrapTable({
        data: dataJSONArray,
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
                width: 500,  // 宽度,单位: px
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
                width: 100,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
                formatter: isUploadProcFormatter
            }, /* {
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
            },*/ {
                field: '', // 数据字段
                title: '时限', // 页面字段显示
                sortable: false, // 禁用排序
                width: 200,  // 宽度,单位: px
                textAlign: 'center', // 字段列标题和数据排列方式
                formatter: itemsOrStageFormatter
            }
        ],
        sortOrder: "asc",
        pageNumber: 1,
        pageSize: 5,
        pageList: [5, 10, 20, 50],
        sidePagination: 'client',
        pagination: true,
        search: true,
        maintainSelected: true
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

/*function itemsOrStageFormatter(value, row, index) {
    var isCheack = '';
    //if (row.isMasterDef == '1') isCheack = 'checked="checked"';
    var isMaster = '<div class="form-group"><div class="col-sm-12">' +
        '<select disabled class="form-control" onchange="selItemOrStage(\''+ row.key +'\' ,this)"> <option> 请选择</option>' +
        stageAndItems+
        '</select>' +
        '</div></div>';
    return isMaster;

}*/
function itemsOrStageFormatter(value, row, index) {
    var isCheack = '';
    var isMaster = '<div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="number" id="' + row.key + '_input" name="timeLimit" min="0" disabled placeholder="单位：工作日" style="width:200px" onchange="selItemOrStage(\'' + row.key + '\' ,this)"/>';
    return isMaster;

}


//全局变量 procIds ，存储导入的流程Id-选择的事项版本VerId 键值对
var procIdsMap = {};

function selProc(key, thisInput) {
    var isCheck = $(thisInput).prop("checked");
    if (isCheck) {
        procIdsMap[key] = "";
        $("#" + key + "_input").attr("disabled", false);
    } else {
        //removeElementsInList(procIds,id);
        if (procIdsMap.hasOwnProperty(key)) delete(procIdsMap[key]);
        $("#" + key + "_input").attr("disabled", true);
    }
    //changeNextDisabled(key,thisInput);
}

function selItemOrStage(id, thisInput) {
    var val = $(thisInput).val();
    procIdsMap[id] = val;
}

/*
function selItemOrStage(id,thisInput) {
    var val=$(thisInput).find("option:selected").val();
    procIdsMap[id]=val;
}
*/

function changeNextDisabled(id, thisInput) {
    var isCheck = $(thisInput).prop("checked");
    var kj = $(thisInput).parent().parent().parent().parent("td").next().find("input,select");
    if (kj.length > 0) {
        if (isCheck) {
            $(kj).attr("disabled", false);
        } else {
            if (procIdsMap.hasOwnProperty(id)) procIdsMap[id] = "";
            $(kj).prop("checked", false);
            $(kj).attr("disabled", true);
            var option = $(kj).find("option:selected");
            if (option.length > 0) {
                $(option).prop("selected", false);
            }
        }
        changeNextDisabled(id, kj[0]);
    } else {
        return;
    }
}

function setProcLimit(id, thisInput) {
    changeNextDisabled(id, thisInput);
}


function setItemsOrStageLimits(id, thisInput) {
    changeNextDisabled(id, thisInput);
}

function reloadTplAppProcData() {
    procIdsMap = {};
    searchTplAppProcChooseDataV2();
}

function addChooseProc() {
  vm.importProcess();
  // procIdsMap = {};
  // $('#tplAppProcChooseDialog').modal('show');
  //
  // // searchTplAppProcChooseData();
  // searchTplAppProcChooseDataV2();
}

function getDataJSONArray(data, url, dataJSONArray, infoMsg) {
    $.ajax({
        url: url,
        type: 'GET',
        data: data,
        cache: false,
        async: false,
        success: function (result) {
            if (result.success) {
                dataJSONArray = result.content;
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

    if (procIdsMap == null || procIdsMap.length == 0) {
        $('#tplAppProcChooseDialog').modal('hide');
        return false;
    }
    var procDefJsonData = [];
    var index = 0;
    for (var procdefKey in procIdsMap) {
        if (procIdsMap.hasOwnProperty(procdefKey)) {
            index++;
            //var oldProc = $('#tplAppProcChooseDialog_tb tr');
            var timeLimit = $('#' + procdefKey + '_input').val();
            if (timeLimit == undefined || timeLimit == "" || timeLimit == null) {
                swal('错误信息', "时限不能为空！", 'error');
                return;
            }
            if (parseFloat(timeLimit) < 0 || Math.floor(parseFloat(timeLimit)) !== parseFloat(timeLimit)) {
                swal('错误信息', "时限必须是正整数！", 'error');
                return;
            }
            var timeLimitUnit = "d";
            var limitIsWorkDay = "1";
            var isMasterProcdef = '0';
            /*$.ajax({
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
            });*/
            procDefJsonData.push('{procdefKey:"' + procdefKey + '",enableAppPriv:"1",isMaster:"' + isMasterProcdef + '",priorityOrder:' + (index) + ',procType:"b",limitIsWorkDay:"' + limitIsWorkDay + '",timeLimitUnit:"' + timeLimitUnit + '",timeLimit:' + timeLimit + '}');
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


function startEl_flowType_change() {
    var flowType = $("input[name='flowType']:checked").val();
    if (flowType == 'stage') {
        $('#startEl_stage').css('display', 'block');
        $('#startEl_not_stage').css('display', 'none');
    } else {
        $('#startEl_stage').css('display', 'none');
        $('#startEl_not_stage').css('display', 'block');
    }
}

function searchtplAppProcChooseKeyword() {
    $("#tplAppProcChooseDialog_tb").bootstrapTable('resetSearch', $("#uploadFlowForm #tplAppProcChooseKeyword").val());
}


function cleartplAppProcChooseKeyword() {
    $("#tplAppProcChooseKeyword").val("");
    $("#tplAppProcChooseDialog_tb").bootstrapTable('resetSearch', "");

}

function convertToKey(_this) {
    var code = pinyin.getCamelChars(_this.value);
  code = replaceSpecialChar(code);
    $('#procKey').val(code);
}

var specialChar = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*%（）&;|{}【】‘；：”“'。，、？]";

function replaceSpecialChar(value) {
  if (value) {
    var pattern = new RegExp(specialChar);
    var rs = "";
    for (var i = 0; i < value.length; i++) {
      rs = rs + value.substr(i, 1).replace(pattern, '');
    }
    return rs;
  }
}

function checkHasSpecialChar(value) {
  if (value) {
    var pattern = new RegExp(specialChar);
    var b = pattern.test(value);
    return b;
  }
  return false;
}

function openTimeGroupDialog(row, $this){
    $('#timeGroupModal').data('row',row);
    searchTimegroupTable();
    $('#timeGroupModal').modal('show');

}
function addTimeGroupImpl(){
    var row =  $('#timeGroupModal').data('row');
    var data = $('#addTimeGroupDialogForm').serialize();
    var options = $('#select2 option');
    var actIds = '';
    options.each(function(ind,ele){
        console.log(ele.value)
        actIds = actIds+ele.value+'`~';
    });
    data = data+'&appFlowdefId='+row.appFlowdefId+'&accId='+actIds;
    $.ajax({
        url:ctx+'/bpm/admin/sto/save',
        data:data,
        method:'POST',
        success:function(res){
            if(res.success){
                swal('提示', '保存成功！', 'info');
            }else if(res.message){
                swal('提示', res.message, 'error');
            }else{
                swal('错误信息', '请联系管理员！', 'error');
            }
            $('#addTimeGroupDialog').modal('hide');
            searchTimegroupTable();
        },error:function(res){
            $('#addTimeGroupDialog').modal('hide');
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}

var timeGroupModalTable = null;
function searchTimegroupTable() {
    var row =  $('#timeGroupModal').data('row');
    var data = {appFlowdefId: row.appFlowdefId};
    if (timeGroupModalTable != null) timeGroupModalTable.bootstrapTable("destroy");
    var dataJSONArray = getDataJSONArray(data, ctx + '/bpm/admin/sto/list', '', '没有更多流程!');
    timeGroupModalTable = $('#timeGroupModal_tb').bootstrapTable({
        data: dataJSONArray,
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
                field: 'timegroupName', // 数据字段
                title: '时限组名称', // 页面字段显示
                sortable: false, // 禁用排序
                width: 500,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
            }, {
                field: 'timeruleName', // 数据字段
                title: '规则名称', // 页面字段显示
                sortable: false, // 禁用排序
                width: 300,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
            }, {
                field: 'timeLimit', // 数据字段
                title: '时限', // 页面字段显示
                sortable: false, // 禁用排序
                width: 100,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
            }, {
                field: 'timegroupId', // 数据字段
                title: '操作', // 页面字段显示
                sortable: false, // 禁用排序
                width: 100,  // 宽度,单位: px
                textAlign: 'left', // 字段列标题和数据排列方式
                formatter: function (value, row, index) {
                    var operate = "<button class='el-button--text'style='cursor: pointer' href='#' onclick=editTimeGroup('"+value+"')><span>编辑</span></button>";
                    operate = operate+"&nbsp;&nbsp;<button class='el-button--text' style='cursor: pointer' href='#' onclick=deleteTimeGroup('"+value+"')><span>删除</span></button>";
                    return operate;
                }
            }
        ],
        sortOrder: "asc",
        pageNumber: 1,
        pageSize: 5,
        pageList: [5, 10, 20, 50],
        sidePagination: 'client',
        pagination: true,
        search: true,
        maintainSelected: true
    })
}

function leftRIghtSelectEvent(){
    //移到右边
    $('#add').click(function() {
        //获取选中的选项，删除并追加给对方
        $('#select1 option:selected').appendTo('#select2');
    });
    //移到左边
    $('#remove').click(function() {
        $('#select2 option:selected').appendTo('#select1');
    });
    //全部移到右边
    $('#add_all').click(function() {
        //获取全部的选项,删除并追加给对方
        $('#select1 option').appendTo('#select2');
    });
    //全部移到左边
    $('#remove_all').click(function() {
        $('#select2 option').appendTo('#select1');
    });
    //双击选项
    $('#select1').dblclick(function(){ //绑定双击事件
        //获取全部的选项,删除并追加给对方
        $("option:selected",this).appendTo('#select2'); //追加给对方
    });
    //双击选项
    $('#select2').dblclick(function(){
        $("option:selected",this).appendTo('#select1');
    });
}

function initLeftRightSelect(){
    var row = $('#timeGroupModal').data('row');
    $('#addTimeGroupDialog form')[0].reset();
    $('#addTimeGroupDialog form').validate().resetForm();
    var $select = $('#select1');
    $select.empty();
    $('#select2').empty();
    row.children.forEach(function(ele){
        $select.append($('<option>',{value:ele.modelId}).text(ele.defName));
    });
}

function editTimeGroup(timegroupId){
    initLeftRightSelect();
    $.ajax({
        url:ctx+'/bpm/admin/sto/get',
        data:{timegroupId:timegroupId},
        method:'POST',
        success:function(res){
            var $form = $('#addTimeGroupDialogForm');
            $form.find('input[name=timegroupId]').val(res.timegroupId);
            $form.find('input[name=timegroupName]').val(res.timegroupName);
            $form.find('input[name=timeLimit]').val(res.timeLimit);
            $('#timeruleGroupId_ option[value='+res.timeruleId+']').prop('selected', true);
            if(res.modifier){
                var actIds = res.modifier.split('`~');
                for(var i in actIds){
                    if(actIds[i] && actIds[i] != ''){
                        $('#select2').append($('#select1 option[value='+actIds[i]+']'));
                    }
                }
            }
            $('#addTimeGroupDialog').modal('show');
        },error:function(res){
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}

function deleteTimeGroup(timegroupId){
    $.ajax({
        url:ctx+'/bpm/admin/sto/delete',
        data:{timegroupId:timegroupId},
        method:'POST',
        success:function(res){
            if(res.success){
                swal('提示', '删除成功！', 'info');
            }else if(res.message){
                swal('提示', res.message, 'error');
            }else{
                swal('错误信息', '请联系管理员！', 'error');
            }
            $('#addTimeGroupDialog').modal('hide');
            searchTimegroupTable();
        },error:function(res){
            $('#addTimeGroupDialog').modal('hide');
            swal('错误信息', '服务器异常！', 'error');
        }
    });
}

function openTimeGroupFormModal(){
    $('#addTimeGroupDialog').modal('show');
    $('#addTimeGroupDialogForm input[name=timegroupId]').val(null);
    $('#addTimeGroupDialog form')[0].reset();
    $('#addTimeGroupDialog form').validate().resetForm();
    $('#select1, #select2').empty();
    initLeftRightSelect();
}

//子流程配置js
var selectItemTree = null;
var itemTreeData = [];
var itemTreeDataCache = [];
var lastSelectNode = null;
var lastClickNode = null;
var selectItemTreeSetting = {
  check: {
    enable: true,
    chkStyle: "checkbox",
    chkboxType: {"Y": "", "N": ""}
  },
  data: {
    simpleData: {
      enable: true,
      idKey: "id",
      pIdKey: "pId",
      rootPId: 0
    }
  },
  view: {
    selectedMulti: false,//设置是否允许同时选中多个节点
    showTitle: true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
    showLine: true, //设置 zTree 是否显示节点之间的连线。
    showHorizontal: false//设置是否水平平铺树（自定义属性）
  },
  //用于捕获节点被点击的事件回调函数
  callback: {
    beforeCheck: beforeSelectItemCheck,
    onCheck: onSelectItemCheck,
    beforeClick: beforeSelectItemClick,
    onClick: onClickItemCheck,
  }
};

function beforeSelectItemCheck(treeId, treeNode) {
  if(treeNode && treeNode.type == 'ss_item') {
    return true;
  }else {
    swal({
      title: '提示信息',
      text: "只能关联实施事项，请重新选择！",
      type: 'warning',
      timer: 1000,
      showConfirmButton: false
    });
    return false;
  }
}

function beforeSelectItemClick(treeId, treeNode, clickFlag) {
  if(treeNode && treeNode.type == 'ss_item') {
    return true;
  }else {
    swal({
      title: '提示信息',
      text: "只能关联实施事项，请重新选择！",
      type: 'warning',
      timer: 1000,
      showConfirmButton: false
    });
    return false;
  }
}

/**
 * onCheck事件响应函数
 * @param event
 * @param treeId
 * @param treeNode
 */
function onSelectItemCheck(event, treeId, treeNode) {
  var treeObj = $.fn.zTree.getZTreeObj(treeId);
  if (treeNode && treeNode.type == 'ss_item') {
    // 选中
    if (treeNode.checked) {
      if (lastSelectNode && lastSelectNode != treeNode) {
        treeObj.checkNode(lastSelectNode, false, false, true);
        treeObj.cancelSelectedNode(lastSelectNode);
      }
      lastSelectNode = treeNode;
      treeObj.selectNode(treeNode);
      $("#busRecordId").val(treeNode.id);
      $("#busRecordName").val(treeNode.name);
      // 取消
    } else {
      lastSelectNode = null;
      treeObj.cancelSelectedNode(treeNode);
      $("#busRecordId").val('');
      $("#busRecordName").val('');
    }
  }
}
/**
 * onClick事件响应函数
 * @param event
 * @param treeId
 * @param treeNode
 */
function onClickItemCheck(event, treeId, treeNode) {
  var treeObj = $.fn.zTree.getZTreeObj(treeId);
  if (treeNode && treeNode.type == 'ss_item') {
    // 选中
    if (treeNode.checked) {
      treeObj.checkNode(treeNode, false, false, true);
      treeObj.cancelSelectedNode(treeNode);
      $("#busRecordId").val('');
      $("#busRecordName").val('');
      // 取消
    } else {
      treeObj.checkNode(treeNode, true, false, true);
      if(lastClickNode && lastClickNode != treeNode){
        treeObj.checkNode(lastClickNode, false, false, true);
        treeObj.cancelSelectedNode(lastClickNode);
      }
      lastClickNode = treeNode;
      treeObj.selectNode(treeNode);
      $("#busRecordId").val(treeNode.id);
      $("#busRecordName").val(treeNode.name);
    }
  }
}
/**
 * 事项树模糊查询
 */
function itemTreeDataSearch(node){
  var keyword = $(node).val();
  var temp = [];
  if(keyword && itemTreeDataCache.length > 0) {
    for (var i = 0, len = itemTreeDataCache.length; i < len; i++) {
      if (itemTreeDataCache[i].name.indexOf(keyword) != -1) {
        temp.push(itemTreeDataCache[i]);
      }
    }
  }else{
    temp = itemTreeDataCache;
  }
  if (selectItemTree) {
    selectItemTree.destroy();
  }
  selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting,temp);
  for (var i = 0; i < temp.length; i++) {
    if (temp[i].id == $('#busRecordId').val()) {
      //回显表单的事项名称
      $('#busRecordName').val(temp[i].name);
      if(selectItemTree){
        //回显到事项树中
        var node = selectItemTree.getNodeByParam("id", temp[i].id, null);
        if (node) {
          selectItemTree.checkNode(node, true, true, false);
        }
      }
    }
  }
}
//绑定模糊查询事项
$("#itemSearchId").keyup(function(){
  itemTreeDataSearch(this);
});

/**
 * 控制面板显隐，临时
 */
$("#selectItemTree").on('click',function (e) {
  var text = $(e.target).text();
  if(text.indexOf("实") == -1){
    e.stopPropagation();
  }
});

//清除查询
function clearSubprocessSearch() {
  $('#subprocessKeyword').val('');
  //$('#subprocess_list_tb').bootstrapTable('refresh');
  searchSubprocess();
}

//
var outerFlowList=[];
var innerFlowList=[];
function addSubProcess(type,id) {
  vm.addSubProcess(type,id);
  // $('#busRecordId').val('');
  // $('#busRecordName').val('');
  // $('#subprocess_modal').modal("show");
  // onloadNode();
  // onloadTriggerAppFlowdefId();
  // // $('input[name="isOuterFlow"]').prop("checked",false);
  // isOuterFlow()
  // if(type==0){
  //     $('#triggerId').text("");
  // }
  // if(currentBusiType=='stage'){
  //     $('#form_bus').attr("hidden",false);
  //     // onloadBus();
  //     onloadItemTree();
  // }else{
  //     $('#form_bus').attr("hidden",true);
  // }
  // $.ajax({
  //   url: ctx + '/rest/mind/getSubTriggerById.do',
  //   type: 'POST',
  //   data: {id:id},
  //   async:false,
  //   success: function (result) {
  //     $('#node').find('option[value=\"'+result.triggerElementId+'\"]').attr("selected","selected");
  //     // $('#busRecordId').find('option[value=\"'+result.busRecordId+'\"]').attr("selected","selected");
  //     $('#busRecordId').val(result.busRecordId);
  //     for (var i = 0; i < itemTreeData.length; i++) {
  //       if (itemTreeData[i].id == $('#busRecordId').val()) {
  //         //回显表单的事项名称
  //         $('#busRecordName').val(itemTreeData[i].name);
  //         if(selectItemTree){
  //           //回显到事项树中
  //           var node = selectItemTree.getNodeByParam("id", itemTreeData[i].id, null);
  //           if (node) {
  //             selectItemTree.checkNode(node, true, true, false);
  //           }
  //         }
  //       }
  //     }
  //     $('#triggerId').html(result.triggerId);
  //     $('input[name="checkName"][value=\"'+result.triggerEvent+'\"]').attr("checked",true);
  //     $('input[name="isOuterFlow"][value=\"'+result.isOuterFlow+'\"]').prop("checked",true);
  //     isOuterFlow();
  //     $('#triggerAppFlowdefId').find('option[value=\"'+result.triggerAppFlowdefId+'\"]').attr("selected","selected");
  //   }
  // });
}

function onloadBus() {
  $('#busRecordId').empty();
  $.ajax({
    url: ctx + '/aea/par/stage/item/listAeaStageItemByStageId.do',
    type: 'POST',
    data: {stageId: currentBusiId},
    async:false,
    success: function (result) {
      var option='<option value="">--请选择--</option>';
      if(result && result.length>0){
        for (var i=0;i<result.length>0;i++){
          var row=result[i];
          option+='<option value="'+row.itemVerId+'">'+row.itemName+'</option> ';
        }
      }
      $('#busRecordId').append(option);
    }
  });
}

/**
 * 加载事项树
 */
function onloadItemTree() {
  itemTreeDataCache = [];
  itemTreeData = [];
  $.ajax({
    url: ctx + '/aea/par/stage/item/getStageItemTreeByStageId.do',
    type: 'POST',
    data: {stageId: currentBusiId},
    async: false,
    success: function (result) {
      if (result && result.length > 0) {
        itemTreeData = result;
        formatTreeData(result);
        if(selectItemTree){
          selectItemTree.destroy();
        }
        selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting,result);
      }
    }
  });
}
/**
 * 格式化实现类型名称
 */
function formatTreeData(data) {
  for(var i=0; i<data.length; i++){
    var item = data[i];
    if(item.type == 'bz_item'){
      item.name = "【标准事项】" + item.name;
    }else if(item.type == 'ss_item'){
      item.name = "【实施事项】" + item.name;
    }
  }
  itemTreeDataCache = data;
}

function selTriggerAppFlowdefId(flowType) {
  // $('#triggerAppFlowdefId').empty();
  // var option='<option value="">--请选择--</option>';
  vm.flowData = [];
  if(flowType=="outer"){
    if(outerFlowList && outerFlowList.length>0){
      for (var i=0;i<outerFlowList.length;i++){
        var entity=outerFlowList[i];
        // option+='<option value="'+entity.className+'">'+entity.listenerName+'</option> ';
        var temp ={label:entity.listenerName,value:entity.className};
        vm.flowData.push(temp);
      }

    }
  }else{
    if(innerFlowList && innerFlowList.length>0){
      for (var i=0;i<innerFlowList.length;i++){
        var entity=innerFlowList[i];
        // option+='<option value="'+entity.id+'">'+entity.defName+'</option> ';
        var temp ={label:entity.defName,value:entity.id};
        vm.flowData.push(temp);
      }
    }
  }
  // $('#triggerAppFlowdefId').append(option);
}

//加载被触发的流程控件
function onloadTriggerAppFlowdefId() {
  if(!listenerList||listenerList.length==0){
    $.ajax({
      // url: ctx + '/act/sto/flow/listener/getProcessFlowListeners.do?procKey='+currentDefKey,
      url:ctx+'/act/sto/flow/listener/process/list?procKey='+currentDefKey,
      type: 'GET',
      async:false,
      success: function (result) {
        outerFlowList=result;
      }
    });
  }else{
    outerFlowList=listenerList;
  }
  $.ajax({
    url: ctx + '/rest/mind/processDefs.do',
    type: 'POST',
    data: {appId: appId},
    async:false,
    success: function (result) {
      innerFlowList=result;
    }
  });
}

//当前选择流程节点id
var selectNodeId = "";
//当前选择流程节点名称
var selectNodeName = "";
//启用单个节点配置，只能配置当前节点下的子流程
var singleConfig = false;
//加载触发的节点控件，20190829增加从节点进入子流程配置，只能配置当前节点下的子流程
function onloadNode() {
  // $('#node').empty();
  var option;
  vm.nodeData = [];
  if(singleConfig){
    // option = '<option value="'+ selectNodeId +'" selected>' + selectNodeName + '</option> ';
    var temp = {label:selectNodeName,value:selectNodeId};
    vm.nodeData.push(temp);
  }else{
    $.ajax({
      url: ctx + '/rest/mind/getTaskNodeList.do',
      type: 'POST',
      async:false,
      data: {defKey: currentDefKey},
      success: function (result) {
        option = '<option value="">--请选择--</option>';
        if(result && result.length>0){
          for (var i=0;i<result.length;i++){
            var vo=result[i];
            var value=vo.name;
            if(!value){
              value=vo.id;
            }
            // if(selectNodeId && selectNodeId == vo.id){
            //   option+='<option value="'+vo.id+'" selected>'+value+'</option> ';
            // }else{
            //   option+='<option value="'+vo.id+'">'+value+'</option> ';
            // }
            var temp = {label:value,value:id};
            vm.nodeData.push(temp);
          }
        }
      }
    });
  }
  vm.triggerNodeId = selectNodeId;
  // $('#node').append(option);
  TypeSel();
}

//控制内部流程或外部流程选择时，表单的切换
$('#isOuterFlow').change(function () {
  isOuterFlow();
});

function isOuterFlow(){

  var isOuterFlow=$('input[name="isOuterFlow"]:checked').val();
  if(0==isOuterFlow){//是外部流程，这时应该隐藏被触发的流程和触发时机表单控件
    $('#isInnerFlow1').attr("hidden",false);
    selTriggerAppFlowdefId("outer");
  }else if(1==isOuterFlow){
    $('#isInnerFlow1').attr("hidden",false);
    selTriggerAppFlowdefId("inner");
  }else{
    $('#isInnerFlow1').attr("hidden",true);
  }
}


$('#node').change(function () {
  TypeSel();
});
//加载触发时机控件
function TypeSel() {
  // var strCode=$('#node').find("option:selected").text();
  var strCode = vm.triggerNodeId;
  $('#chufa').empty();
  var label="";
  if(strCode=="开始"||strCode=="结束"){
    label=' <label class="m-checkbox ng-hide" ng-show="tabType == 2&amp;&amp;!isSequenceFlow">\n' +
      '            <input ng-model="triggerEvent_node_start" value="start" name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;节点开始时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
      '    <span></span>\n' +
      '        </label>\n' +
      '        <label class="m-checkbox ng-hide" ng-show="tabType == 2&amp;&amp;!isSequenceFlow">\n' +
      '            <input ng-model="triggerEvent_node_end" value="end"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;节点结束时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
      '    <span></span>\n' +
      '        </label>';
  }
  else if(strCode.indexOf("sid") == 0){

  }else if(strCode.indexOf("sequeFlow")==0) {
    label='<label class="m-checkbox ng-hide" ng-show="isSequenceFlow">\n' +
      '            <input ng-model="triggerEvent_seq_take" value="take"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;到达时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
      '    <span></span>\n' +
      '        </label>';

  }else if(strCode.indexOf("gateway")==0){

  }
  else {
    label='<label class="m-checkbox" ng-show="tabType == 1" style="">\n' +
      '       <input ng-model="triggerEvent_task_create" value="create"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;任务创建时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
      '       <span></span>\n' +
      '  </label>\n' +
      '  <label class="m-checkbox" ng-show="tabType == 1" style="">\n' +
      '       <input ng-model="triggerEvent_task_complete" value="complete"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;任务完成时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
      '       <span></span>\n' +
      '  </label>';
  }
  $('#chufa').append(label);
}

/**
 * 保存触发条件
 */
function saveSubTrigger() {
  // var triggerAppFlowdefId=$('#triggerAppFlowdefId').find("option:selected").val();
  var triggerAppFlowdefId=vm.triggerAppFlowdefId;
  // var triggerElementId=$('#node').find("option:selected").val();
  var triggerElementId=vm.triggerNodeId;
  var triggerEvent=$('input[name="checkName"]:checked').val();
  var busRecordId='';
  var triggerId=$('#triggerId').text();
  var isOuterFlow=$('input[name="isOuterFlow"]:checked').val();
  if(!triggerAppFlowdefId){
    swal('提示信息', '请选择被触发的流程！', 'error');
    return;
  }
  if (triggerAppFlowdefId == currentAppFlowdefId) {
    swal('提示信息', '被触发的流程不可以是当前节点所属的流程，请选择其他流程！', 'error');
    return;
  }
  if(!triggerElementId){
    swal('提示信息', '请选择触发的节点!', 'error');
    return;
  }
  if(!triggerEvent){
    swal('提示信息', '请选择触发时机！', 'error');
    return;
  }
  if(currentBusiType=='stage'){
    // busRecordId=$('#busRecordId').find("option:selected").val();
    busRecordId=$('#busRecordId').val();
    /*if(!busRecordId){
        swal('提示信息', '请选择关联事项!', 'error');
        return;
    }*/
  }else{
    busRecordId=currentBusiId;
  }
  var data={
    "triggerId":triggerId,
    "appFlowdefId":currentAppFlowdefId,
    "triggerAppId":appId,
    "triggerElementId":triggerElementId,
    "triggerAppFlowdefId":triggerAppFlowdefId,
    "triggerEvent":triggerEvent,
    "busRecordId":busRecordId,
    "isOuterFlow":isOuterFlow
  };

  $.ajax({
    url: ctx + '/rest/mind/saveOrUpdateSubTrigger.do',
    type: 'POST',
    data: data,
    success: function (result) {
      if(result.success){
        $('#subprocess_modal').modal("hide");
        subprocessTabe.bootstrapTable('refresh');
        // customTable_tree.bootstrapTable('refresh');
        refresh();
      }else{
        swal({
          title: '提示信息',
          text: result.message,
          type: 'warning',
          timer: 2000,
          showConfirmButton: false
        });
      }
    },
    error:function(){
      swal('错误信息', '服务器异常！', 'error');
    }
  });
}

//批量删除
function batchDelSubTrigger() {
  var ids=[];
  var checkBoxs= $('#subprocess_list_tb').bootstrapTable('getSelections');
  var url=ctx +"/rest/mind/batchDelSubTrigger.do";
  if(checkBoxs!=null&&checkBoxs.length>0){
    for(var i=0;i<checkBoxs.length;i++){
      ids.push(checkBoxs[i].triggerId);
    }
    swal({
      title: '提示信息',
      text: '此操作将删除子流程触发配置,您确定删除吗?',
      type: 'warning',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    }).then(function(result) {
      if (result.value) {
        $.ajax({
          url: url,
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
              subprocessTabe.bootstrapTable('refresh');
              // customTable_tree.bootstrapTable('refresh');
              refresh();
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

//编辑配置
function editSubTrigger(id) {
  //$('#subProcForm')[0].reset();
  $(':input','#subProcForm')
    .not(':button,:submit,:reset,:hidden')   //将myform表单中input元素type为button、submit、reset、hidden排除
    .val('')  //将input元素的value设为空值
    .removeAttr('checked')
    .removeAttr('selected');
  addSubProcess(1,id);
}

/**
 * 模糊查询
 */
function searchSubprocess() {
  showSubprocess(currentAppFlowdefId,currentDefKey);
}

function delSubTrigger(id){
  var url=ctx +"/rest/mind/delSubTrigger.do";
  swal({
    title: '提示信息',
    text: '此操作将删除子流程触发配置,您确定删除吗?',
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
            subprocessTabe.bootstrapTable('refresh');
            $("#process_design_tb").bootstrapTable('refresh');       //无参数刷新
            refresh();
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

