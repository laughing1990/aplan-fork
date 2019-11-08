var customTable_tree;
/**
 * 20190828 初始化流程树表
 */
function initProcTreeGrid() {

    var url = ctx + '/rest/mind/processDefTree.do?isPid=true';
    customTable_tree = $('#process_design_tb').bootstrapTable({
        url: url,
        columns: getProcTreeGridColumns(),
        // pagination: true,
        // pageSize: 10,
        // paginationHAlign: 'right',
        // paginationVAlign: 'bottom',
        // paginationDetailHAlign:"left",
        // paginationShowPageGo: true,
        // pageList: [10,20,50,100],
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        queryParams: processParam,
        // sidePagination: 'client',
        treeShowField:'defName',
        parentIdField: 'pid',
        idField: 'treeNodeId',
        onPreBody:function (data) {
        },
        onLoadSuccess: function(result) {
            $('#process_design_tb').treegrid({
                initialState: 'collapsed', // 所有节点都展开
                treeColumn: 0,
                onChange: function() {
                    $('#process_design_tb').bootstrapTable('resetWidth');
                }
            });
            treegridExpand($('#process_design_tb').treegrid('getRootNodes'),2);
        }
    });
}

/**
 * 递归展开树节点
 * @param node 节点对象(jquery)
 * @param level 展示级别
 */
function treegridExpand(node,level) {
    node.treegrid('expand');
    if(level > 1){
        for(var j=0; j<node.length; j++){
            var childNode = $(node[j]).treegrid('getChildNodes');
            if(childNode){
                treegridExpand(childNode,level-1)
            }
        }
    }
}

function treegridExpandAll() {
    $('#process_design_tb').treegrid('expandAll');
}

function treegridCollapseAll() {
    $('#process_design_tb').treegrid('collapseAll');
}


/**
 * 定义树表格的显示列
 */
var getProcTreeGridColumns = function () {

    var columns =  [
        {
            field: 'defName',
            title: '流程/节点名称',
            align: 'left',
            formatter: function (value, row) {
                var result;
                if(row.isProcess == '1'){
                    result = '<span class="circleIcon blueColor">流程</span>' + value ;
                    if(row.isMasterDef == "1"){
                        result += "<span class=\"ellipseIcon\">主流程</span>"
                    }
                    if(row.itemName){
                        if(row.itemType == '1'){
                            //标准事项
                            result += "【<span class=\"squareIcon\">标</span>"+ row.itemName + '】';
                        }else{
                            //实施事项
                            result += "【<span class=\"squareIcon\">实</span>"+ row.itemName + '—' + row.itemApproveOrg + '】';
                        }
                    }
                }else{
                    result = '<span class="circleIcon greenColor">节点</span>' + value ;
                }
                return result;
            }
        },
        {
            field: 'timeLimit',
            title: '时限',
          width: 150,
            align: 'left',
            formatter: function (value, row) {
                //ND：自然日，WD：工作日，NH：小时（自然日），WH：小时（工作日）'
                if(value){
                    var timeLimitUnit = row.timeLimitUnit;
                    var result;
                    if("NH" == timeLimitUnit){
                        result = value + "小时<span style=\"color:#BEBEBE\">（自然日）</span>";
                    }else if("WH" == timeLimitUnit){
                        result = value + "小时<span style=\"color:#BEBEBE\">（工作日）</span>";
                    }else if("ND" == timeLimitUnit){
                        result = value + "天<span style=\"color:#BEBEBE\">（自然日）</span>";
                    }else{
                        result = value + "天<span style=\"color:#BEBEBE\">（工作日）</span>";
                    }
                    return result;
                }
            }
        },
        {
            field: 'version',
            title: '当前版本',
            width: 80,
            align: 'left',
            formatter: function (value, row) {
                if(row.isProcess == '1'){
                    var result;
                    if(value){
                        result = "V" + value;
                    }else{
                        result = row.isPublic;
                    }
                    return result;
                }
            }
        },
        {
            field:'operate_',
            align:'left',
            title:'操作',
          width: 230,
            formatter: procOperatorFormatter
        }
    ];
    return columns;
}
/**
 *操作按钮列
 */
function procOperatorFormatter(value, row, index) {
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
    var setProcMasterBtn = '<a href="javascript:setProcMaster(\''+ row.appFlowdefId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="设为主流程">' +
        '<i class="flaticon-map"></i>' + '</a>';

    var editLimitBtn = '<a href="javascript:editLimit(' + "'" + row.timeLimit + "','" + row.timeruleId + "','" + row.appFlowdefId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="配置时限">' +
        '<i class="flaticon-time-1"></i>' + '</a>';


        '<i class="flaticon-calendar-with-a-clock-time-tools"></i>' + '</a>';

    var activateBtn = '<a href="javascript:showStartEl(' + "'" + row.id + "'," + index + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="启动情形">' +
        '<i class="la la-diamond"></i>' +
        '</a>';

    var editBtn = '<a href="javascript:chooseProcDef(\'' + row.modelId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="设计流程"><i class="la la-edit"></i>' +
        '</a>';

    var priBtn = '<a target="_blank" href="' + ctx + '/bpm/admin/template/authConfigForm.html?appFlowdefId=' + row.id + '&appId=' + appId + '"' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="权限配置"><i class="la la-cog"></i>' +
        '</a>';

    var subprocessBtn = '<a href="javascript:showSubprocess(\'' + row.appFlowdefId + '\',\'' + row.defKey + '\',\'' + row.defName + '\',\'' + row.modelId + '\');" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="子流程配置"><i class="la la-cogs"></i>' +
        '</a>';

    var deleteBtn = '<a href="javascript:delActTplAppProc(\'' + row.appFlowdefId + '\');" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="移除流程"><i class="la la-trash"></i>' +
        '</a>';

  // if (currentBusiType == 'item') {
        if(row.isProcess == '1'){
            return setProcMasterBtn + editLimitBtn + activateBtn + editBtn + priBtn  + deleteBtn;
        }else{
            return subprocessBtn;
        }
  // } else {
  //     if(row.isProcess == '1'){
  //         return setProcMasterBtn + editLimitBtn + activateBtn + editBtn + priBtn  + deleteBtn;
  //     }else{
  //         return subprocessBtn;
  //     }
  // }
}