var agencyServiceQueryParams;
var allServiceId = new Array();            //已勾选的
var cancelCheckServiceIds = new Array();  //取消勾选的已加载的数据库已保存的
var dbsavedServiceIds = new Array();      //已加载的数据库已保存的
$(function () {
    $('#service_list_tb_scroll').niceScroll({
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
    // $('#dialog_config_service').on('hidden.bs.modal', function () {
    // });
    //注册触发事件处理函数
    $('#agency_service_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        checkServiceTrigger(e.type, datas);                        // 保存到全局 Set() 里
    });
})
//勾选或取消勾选时维护全局比变量
function checkServiceTrigger(type,datas){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            if(allServiceId.indexOf(v.serviceId) == -1){
                allServiceId.push(v.serviceId);
            }
        });
    }else{
        $.each(datas,function(i,v) {
            var j = allServiceId.indexOf(v.serviceId);
            allServiceId.splice(j, 1);    //删除取消选中行
            if (cancelCheckServiceIds.indexOf(v.serviceId) == -1 && v.isCheck) {
                cancelCheckServiceIds.push(v.serviceId);    //取消勾选的
            }
        });
    }
}
//参数设置
function queryAgencyServiceParam(params) {
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
    if (agencyServiceQueryParams) {
        for (var i = 0; i < agencyServiceQueryParams.length; i++) {
            buildParam[agencyServiceQueryParams[i].name] = agencyServiceQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

//查询结果
function queryAgencyServiceResponseData(res) {
    if(res){
        var data = res.rows;
        $.each(data,function(i,c){
            if(c.isCheck && dbsavedServiceIds.indexOf(c.serviceId) == -1){
                dbsavedServiceIds.push(c.serviceId);
            }
            if(c.isCheck && allServiceId.indexOf(c.serviceId) == -1 && cancelCheckServiceIds.indexOf(c.serviceId) == -1){
                allServiceId.push(c.serviceId);
            }
        });
    }
    return res;
}
// 查询
function searchServiceList(){
    agencyServiceQueryParams = [];
    agencyServiceQueryParams.push({'name':'keyword',value:$('#serviceKeyword').val()});
    agencyServiceQueryParams.push({'name':'itemVerId',value:currItemVerId});
    $("#agency_service_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}
//清空
function clearSearchServiceList() {
    $('#serviceKeyword').val('');
    searchServiceList();
}

//回显checkbox
function checkServiceId(i,row) {
    if($.inArray(row.serviceId,allServiceId)!=-1){
        return {
            checked : true               // 存在则选中
        }
    }
}

//保存配置
function saveConfigService() {
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
            if(allServiceId && allServiceId.length > 0){
                for(var i=0,len=allServiceId.length;i<len;i++){
                    if(dbsavedServiceIds.indexOf(allServiceId[i])==-1){   //已勾选但数据库没有保存的，则是要新增的
                        idsArr += ',' + allServiceId[i];
                    }
                }
                if(idsArr){
                    idsArr = idsArr.substring(1);
                }
            }
            if(dbsavedServiceIds && dbsavedServiceIds.length > 0){
                for(var i=0,len=dbsavedServiceIds.length;i<len;i++){
                    var id = dbsavedServiceIds[i];
                    if(allServiceId.indexOf(id) == -1){   //已加载但是没有勾选的，则是要取消的
                        cancelIds +=  ',' + id;
                    }
                }
                if(cancelIds){
                    cancelIds = cancelIds.substring(1);
                }
            }
            $.ajax({
                url: ctx+'/supermarket/service/saveConfigService.do',
                type: 'POST',
                data:{
                    'itemVerId': currItemVerId,
                    'saveServiceIds': idsArr,
                    'cancelServiceIds': cancelIds
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
                        clearServiceGlobalArray();
                        configServiceDialogClose();
                        searchServicetb();
                        //根据所属服务显示资质要求
                        var isQualRequireCheck = $('#isQualRequire').prop('checked');
                        showChooseQual(isQualRequireCheck);
                        //如果勾选了，保存资质要求
                        if(isQualRequireCheck){
                            autoSaveQualRequire();
                        }
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
function configServiceDialogClose() {
    $('#dialog_config_service').modal('hide');
}
//清空全局变量数组
function clearServiceGlobalArray() {
    allServiceId.splice(0,allServiceId.length);
    dbsavedServiceIds.splice(0,dbsavedServiceIds.length);
    cancelCheckServiceIds.splice(0,cancelCheckServiceIds.length);
}
