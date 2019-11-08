var qualQueryParams;
var allQualId = new Array();            //已勾选的
var cancelCheckQualIds = new Array();  //取消勾选的已加载的数据库已保存的

$(function () {
    $('#qual_list_tb_scroll').niceScroll({
        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });
    //当窗口关闭的时候
    // $('#dialog_config_qual').on('hidden.bs.modal', function () {
    // });
    //注册触发事件处理函数
    $('#qual_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        checkQualTrigger(e.type, datas);                        // 保存到全局 Set() 里
    });
})
//勾选或取消勾选时维护全局比变量
function checkQualTrigger(type,datas){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            if(allQualId.indexOf(v.qualId) == -1){
                allQualId.push(v.qualId);
            }
        });
    }else{
        $.each(datas,function(i,v) {
            var j = allQualId.indexOf(v.qualId);
            allQualId.splice(j, 1);    //删除取消选中行
            if (cancelCheckQualIds.indexOf(v.qualId) == -1 && v.ischeck) {
                cancelCheckQualIds.push(v.qualId);    //取消勾选的
            }
        });
    }
}
//参数设置
function queryQualParam(params) {
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
    if (qualQueryParams) {
        for (var i = 0; i < qualQueryParams.length; i++) {
            buildParam[qualQueryParams[i].name] = qualQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

//查询结果
function queryQualResponseData(res) {
    if(res){
        var data = res.rows;
        $.each(data,function(i,c){
            if(c.ischeck && allQualId.indexOf(c.qualId) == -1 && cancelCheckQualIds.indexOf(c.qualId) == -1){
                allQualId.push(c.qualId);
            }
        });
    }
    return res;
}
// 查询
function searchQualList(){
    qualQueryParams = [];
    qualQueryParams.push({'name':'keyword',value:$('#qualKeyword').val()});
    qualQueryParams.push({'name':'itemVerId',value:currItemVerId});
    $("#qual_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}
//清空
function clearQualServiceList() {
    $('#qualKeyword').val('');
    searchQualList();
}

//回显checkbox
function checkQualId(i,row) {
    if($.inArray(row.qualId,allQualId)!=-1){
        return {
            checked : true               // 存在则选中
        }
    }
}
//关闭窗口
function configQualDialogClose() {
    $('#dialog_config_qual').modal('hide');
}
//清空全局变量数组
function clearQualGlobalArray() {
    allQualId.splice(0,allQualId.length);
    cancelCheckQualIds.splice(0,cancelCheckQualIds.length);
}

//确定选择资质
function chooseConfigService() {
    $.ajax({
        url: ctx + '/supermarket/qual/getQualIdsByItemVerId.do',
        type: 'POST',
        data: {'itemVerId': currItemVerId},
        async: false,
        success: function (result) {
           if(result.success){
               var data = result.content;
                if(data){
                    $(data).each(function (i,n) {
                        var id = n.qualId;
                        if($.inArray(id,allQualId) ==-1 && cancelCheckQualIds.indexOf(id) == -1){
                            allQualId.push(id);
                        }
                    })
                }
               var ids = '';
               if(allQualId && allQualId.length > 0){
                   ids += '.';
                   for(var i=0,len=allQualId.length;i<len;i++){
                       ids += allQualId[i] +  '.';
                   }
               }
               $('#qualRequire').val(ids);
               showQualRequire(ids)
               configQualDialogClose();
           }else{
               swal('错误信息', "获取信息失败！", 'error');
           }
        },
        error: function () {
            swal('错误信息', "获取中介机构要求信息失败！", 'error');
        }
    });
}
//显示资质要求
function showQualRequire(ids) {
    $.ajax({
        url: ctx + '/supermarket/qual/getQualNamesByIds.do',
        type: 'POST',
        data: {'idSeq': ids},
        async: false,
        success: function (data) {
           if(data){
               var qualName = '';
               $(data).each(function (i,n) {
                   var name = n.qualName;
                   qualName += '、' + name;
               })
               if(qualName){
                   qualName = qualName.substring(1);
               }
               $('#qualRequireName').val(qualName);
           }
        },
        error: function () {
            swal('错误信息', "获取中介机构要求信息失败！", 'error');
        }
    });
}
