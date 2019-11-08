var administrativeItemQueryParams;
var allAdministrativeItemId = new Array();            //已勾选的
var cancelCheckAdministrativeItemId = new Array();  //取消勾选的已加载的数据库已保存的
var dbsavedAdministrativeItemId = new Array();      //已加载的数据库已保存的
$(function () {
    $('#administrative_item_list_tb_scroll').niceScroll({
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
    // $('#dialog_config_item').on('hidden.bs.modal', function () {
    // });
    //注册触发事件处理函数
    $('#administrative_item_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        checkItemTrigger(e.type, datas);                        // 保存到全局 Set() 里
    });
})
//勾选或取消勾选时维护全局比变量
function checkItemTrigger(type,datas){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            if(allAdministrativeItemId.indexOf(v.itemId) == -1){
                allAdministrativeItemId.push(v.itemId);
            }
        });
    }else{
        $.each(datas,function(i,v) {
            var j = allAdministrativeItemId.indexOf(v.itemId);
            allAdministrativeItemId.splice(j, 1);    //删除取消选中行
            if (cancelCheckAdministrativeItemId.indexOf(v.itemId) == -1 && v.isCheck) {
                cancelCheckAdministrativeItemId.push(v.itemId);    //取消勾选的
            }
        });
    }
}
//参数设置
function queryAdministrativeItemParam(params) {
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
    if (administrativeItemQueryParams) {
        for (var i = 0; i < administrativeItemQueryParams.length; i++) {
            buildParam[administrativeItemQueryParams[i].name] = administrativeItemQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

//查询结果
function queryAdministrativeItemResponseData(res) {
    if(res){
        var data = res.rows;
        $.each(data,function(i,c){
            if(c.isCheck && dbsavedAdministrativeItemId.indexOf(c.itemId) == -1){
                dbsavedAdministrativeItemId.push(c.itemId);
            }
            if(c.isCheck && allAdministrativeItemId.indexOf(c.itemId) == -1 && cancelCheckAdministrativeItemId.indexOf(c.itemId) == -1){
                allAdministrativeItemId.push(c.itemId);
            }
        });
    }
    return res;
}
// 查询
function searchItemList(){
    administrativeItemQueryParams = [];
    administrativeItemQueryParams.push({'name':'keyword',value:$('#itemKeyword').val()});
    administrativeItemQueryParams.push({'name':'currItemId',value:currItemId});
    administrativeItemQueryParams.push({'name':'itemNature',value:'0'});
    administrativeItemQueryParams.push({'name':'isCatalog',value:'1'});
    $("#administrative_item_list_tb").bootstrapTable('refreshOptions',{selectPage:1});  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}
//清空
function clearSearchItemList() {
    $('#itemKeyword').val('');
    searchItemList();
}

//回显checkbox
function checkItemId(i,row) {
    if($.inArray(row.itemId,allAdministrativeItemId)!=-1){
        return {
            checked : true               // 存在则选中
        }
    }
}

//保存配置
function saveConfigItem() {
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
            if(allAdministrativeItemId && allAdministrativeItemId.length > 0){
                for(var i=0,len=allAdministrativeItemId.length;i<len;i++){
                    if(dbsavedAdministrativeItemId.indexOf(allAdministrativeItemId[i])==-1){   //已勾选但数据库没有保存的，则是要新增的
                        idsArr += ',' + allAdministrativeItemId[i];
                    }
                }
                if(idsArr){
                    idsArr = idsArr.substring(1);
                }
            }
            if(dbsavedAdministrativeItemId && dbsavedAdministrativeItemId.length > 0){
                for(var i=0,len=dbsavedAdministrativeItemId.length;i<len;i++){
                    var id = dbsavedAdministrativeItemId[i];
                    if(allAdministrativeItemId.indexOf(id) == -1){   //已加载但是没有勾选的，则是要取消的
                        cancelIds +=  ',' + id;
                    }
                }
                if(cancelIds){
                    cancelIds = cancelIds.substring(1);
                }
            }
            $.ajax({
                url: ctx+'/aea/item/basic/saveConfigItem.do',
                type: 'POST',
                data:{
                    'itemId': currItemId,
                    'saveItemIds': idsArr,
                    'cancelItemIds': cancelIds
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
                        clearItemGlobalArray();
                        configItemDialogClose();
                        searchItemtb();
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
function configItemDialogClose() {
    $('#dialog_config_item').modal('hide');
}
//清空全局变量数组
function clearItemGlobalArray() {
    allAdministrativeItemId.splice(0,allAdministrativeItemId.length);
    dbsavedAdministrativeItemId.splice(0,dbsavedAdministrativeItemId.length);
    cancelCheckAdministrativeItemId.splice(0,cancelCheckAdministrativeItemId.length);
}
