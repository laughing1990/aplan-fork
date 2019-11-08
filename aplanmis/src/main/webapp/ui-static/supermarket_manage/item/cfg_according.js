var accordingAsQueryParams;
var allAccordingId = new Array();            //已勾选的
var cancelCheckAccordingIds = new Array();  //取消勾选的已加载的数据库已保存的
var dbsavedAccordingIds = new Array();      //已加载的数据库已保存的
$(function () {
    $('#according_list_tb_scroll').niceScroll({
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
    // $('#dialog_config_according').on('hidden.bs.modal', function () {
    // });
    //注册触发事件处理函数
    $('#_according_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        checkAccordingTrigger(e.type, datas);                        // 保存到全局 Set() 里
    });
})
//勾选或取消勾选时维护全局比变量
function checkAccordingTrigger(type,datas){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            if(allAccordingId.indexOf(v.legalClauseId) == -1){
                allAccordingId.push(v.legalClauseId);
            }
        });
    }else{
        $.each(datas,function(i,v) {
            var j = allAccordingId.indexOf(v.legalClauseId);
            allAccordingId.splice(j, 1);    //删除取消选中行
            if (cancelCheckAccordingIds.indexOf(v.legalClauseId) == -1 && v.isCheck) {
                cancelCheckAccordingIds.push(v.legalClauseId);    //取消勾选的
            }
        });
    }
}
//参数设置
function queryAccordingParam(params) {
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
    if (accordingAsQueryParams) {
        for (var i = 0; i < accordingAsQueryParams.length; i++) {
            buildParam[accordingAsQueryParams[i].name] = accordingAsQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

//查询结果
function queryAccordingResponseData(res) {
    if(res){
        var data = res.rows;
        $.each(data,function(i,c){
            if(c.isCheck && dbsavedAccordingIds.indexOf(c.legalClauseId) == -1){
                dbsavedAccordingIds.push(c.legalClauseId);
            }
            if(c.isCheck && allAccordingId.indexOf(c.legalClauseId) == -1 && cancelCheckAccordingIds.indexOf(c.legalClauseId) == -1){
                allAccordingId.push(c.legalClauseId);
            }
        });
    }
    return res;
}
// 查询
function searchAccordingList(){
    accordingAsQueryParams = [];
    accordingAsQueryParams.push({'name':'keyword',value:$('#accordingKeyword').val()});
    accordingAsQueryParams.push({'name':'itemVerId',value:currItemVerId});
    // $("#_according_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#_according_list_tb").bootstrapTable('refreshOptions',{selectPage:1});  //跳转到第一页，防止其他页查询第一次不显示数据的问题。

}
//清空
function clearSearchAccordingList() {
    $('#accordingKeyword').val('');
    searchAccordingList();
}

//回显checkbox
function checkAccordingId(i,row) {
    if($.inArray(row.legalClauseId,allAccordingId)!=-1){
        return {
            checked : true               // 存在则选中
        }
    }
}

//保存配置
function saveConfigAccording() {
    var isSaveExplain = getAeaImItemExplainByItemVerId();
    if(!isSaveExplain){
        swal('提示', "请先保存服务信息！", 'info');
        return;
    }
    swal({
        title: '提示信息',
        text: '请确认是否要保存操作结果？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value) {
            var idsArr = '';
            var cancelIds = '';
            if(allAccordingId && allAccordingId.length > 0){
                for(var i=0,len=allAccordingId.length;i<len;i++){
                    if(dbsavedAccordingIds.indexOf(allAccordingId[i])==-1){   //已勾选但数据库没有保存的，则是要新增的
                        idsArr += ',' + allAccordingId[i];
                    }
                }
                if(idsArr){
                    idsArr = idsArr.substring(1);
                }
            }
            if(dbsavedAccordingIds && dbsavedAccordingIds.length > 0){
                for(var i=0,len=dbsavedAccordingIds.length;i<len;i++){
                    var id = dbsavedAccordingIds[i];
                    if(allAccordingId.indexOf(id) == -1){   //已加载但是没有勾选的，则是要取消的
                        cancelIds +=  ',' + id;
                    }
                }
                if(cancelIds){
                    cancelIds = cancelIds.substring(1);
                }
            }
            $.ajax({
                url: ctx+'/aea/item/service/legal/clause/saveConfigAccording.do',
                type: 'POST',
                data:{
                    'itemId': currItemId,
                    'saveLegalClauseIds': idsArr,
                    'cancelLegalClauseIds': cancelIds
                },
                success: function (result){
                    if(result.success){
                        swal({
                            title: '提示信息',
                            text: '保存成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                        clearAccordingGlobalArray();
                        configAccordingDialogClose();
                        searchAccordingtb();
                    }else{
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', '保存失败！', 'error');
                }
            });
        }
    });
}
//关闭窗口
function configAccordingDialogClose() {
    $('#dialog_config_according').modal('hide');
}
//清空全局变量数组
function clearAccordingGlobalArray() {
    allAccordingId.splice(0,allAccordingId.length);
    dbsavedAccordingIds.splice(0,dbsavedAccordingIds.length);
    cancelCheckAccordingIds.splice(0,cancelCheckAccordingIds.length);
}

function openCfgSelectTree(curIsEditable){
    if(!curIsEditable){
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
        return;
    }
    $('#select_clause_ztree_modal').modal('show');
    $('#select_clause_ztree_modal_title').html('选择条款');
    $('#selectClauseKeyWord').val('');
    //初始化条款分类树
    initSelectClauseTree();
    //延迟加载已选择的法律法规
    setTimeout(function(){
        loadSelectedClauseData();
    },500);
}

function batchDelete(curIsEditable){

    if(!curIsEditable){
        swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
        return;
    }

    var checkBoxs = $("#_according_list_tb").bootstrapTable('getSelections');
    var ids = [];
    if(checkBoxs!=null&&checkBoxs.length>0){
        for(var i=0;i<checkBoxs.length;i++){
            ids.push(checkBoxs[i].basicId);
        }
        swal({
            title: '提示信息',
            text: '您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/service/legal/clause/batchDeleteLegalClause.do',
                    type: 'POST',
                    data:{'ids': ids.toString()},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '批量删除成功!',
                                type: 'success',
                                timer: 1500,
                                showConfirmButton: false
                            });
                            // 重新加载数据
                            searchAccordingList();
                            searchAccordingtb();
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
        swal('提示信息', '请选择数据！', 'info');
    }
}