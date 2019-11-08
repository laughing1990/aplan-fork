var certQueryParams;
var allCertId = new Array();            //已勾选的
var cancelCheckCertIds = new Array();  //取消勾选的已加载的数据库已保存的
var dbsavedCertIds = new Array();      //已加载的数据库已保存的
$(function () {
    $('#cert_list_tb_scroll').niceScroll({
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
    // $('#dialog_config_qual_cert').on('hidden.bs.modal', function () {
    // });
    //注册触发事件处理函数
    $('#cert_list_tb').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        checkTrigger(e.type, datas);                        // 保存到全局 Set() 里
    });
})
//参数设置
function queryCertParam(params) {
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
    if (certQueryParams) {
        for (var i = 0; i < certQueryParams.length; i++) {
            buildParam[certQueryParams[i].name] = certQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}
function configCertDialogClose() {
    $('#dialog_config_qual_cert').modal('hide');
}
//查询结果
function queryCertResponseData(res) {
    if(res){
        var data = res.rows;
        $.each(data,function(i,c){
            if(c.isConfig && dbsavedCertIds.indexOf(c.certId) == -1){
                dbsavedCertIds.push(c.certId);
            }
            if(c.isConfig && allCertId.indexOf(c.certId) == -1 && cancelCheckCertIds.indexOf(c.certId) == -1){
                allCertId.push(c.certId);
            }
        });
    }
    return res;
}
// 查询
function searchCertList(checkCertIds){
    certQueryParams = [];
    if(checkCertIds){
        certQueryParams.push({'name':'checkedCertIds',value:checkCertIds});        //查看已勾选的
    }else {
        $('#viewCheckedCertBtn').html('查看已勾选');
    }
    certQueryParams.push({'name':'keyword',value:$('#certKeyword').val()});
    certQueryParams.push({'name':'configCertRecordId',value:currQualId});
    $("#cert_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}
//清空
function clearSearchCertList() {
    $('#certKeyword').val('');
    searchCertList();
}
//勾选或取消勾选时维护全局比变量
function checkTrigger(type,datas){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            if(allCertId.indexOf(v.certId) == -1){
                allCertId.push(v.certId);
            }
        });
    }else{
        $.each(datas,function(i,v) {
            var j = allCertId.indexOf(v.certId);
            allCertId.splice(j, 1);    //删除取消选中行
            if (cancelCheckCertIds.indexOf(v.certId) == -1 && v.isConfig) {
                cancelCheckCertIds.push(v.certId);    //取消勾选的
            }
        });
    }
}
//回显checkbox
function checkAllCertId(i,row) {
    if($.inArray(row.certId,allCertId)!=-1){
        return {
            checked : true               // 存在则选中
        }
    }
}

//保存配置
function saveConfigCert() {
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
            if(allCertId && allCertId.length > 0){
                for(var i=0,len=allCertId.length;i<len;i++){
                    if(dbsavedCertIds.indexOf(allCertId[i])==-1){   //已勾选但数据库没有保存的，则是要新增的
                        idsArr += ',' + allCertId[i];
                    }
                }
                if(idsArr){
                    idsArr = idsArr.substring(1);
                }
            }
            if(dbsavedCertIds && dbsavedCertIds.length > 0){
                for(var i=0,len=dbsavedCertIds.length;i<len;i++){
                    var id = dbsavedCertIds[i];
                    if(allCertId.indexOf(id) == -1){   //已加载但是没有勾选的，则是要取消的
                        cancelIds +=  ',' + id;
                    }
                }
                if(cancelIds){
                    cancelIds = cancelIds.substring(1);
                }
            }
            $.ajax({
                url: ctx+'/aea/bus/cert/saveAeaBusCert.do',
                type: 'POST',
                data:{
                    'saveCertIds': idsArr,
                    'cancelCertIds': cancelIds,
                    'busRecordId':currQualId,
                    'busTableName':'AEA_IM_QUAL'
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
                        $('#viewCheckedCertBtn').html('查看已勾选');
                        //清空
                        clearGlobalArray();
                        clearSearchCertList();
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
//清空全局变量数组
function clearGlobalArray() {
    allCertId.splice(0,allCertId.length);
    dbsavedCertIds.splice(0,dbsavedCertIds.length);
    cancelCheckCertIds.splice(0,cancelCheckCertIds.length);
}
//查看已勾选的证书
function viewCheckedCert() {
    var btnName = $('#viewCheckedCertBtn').html();
    if(btnName == '查看已勾选'){
        $('#viewCheckedCertBtn').html('返回');
        var idsArr = '';
        if(allCertId && allCertId.length > 0){
            for(var i=0,len=allCertId.length-1;i<len;i++){
                idsArr += allCertId[i] + ',';
            }
            idsArr += allCertId[allCertId.length-1];
        }
        var dbCertIds = getSaveCurrQualCert();//查出已保存的
        if(dbCertIds && dbCertIds.length > 0){
            for(var i=0,len=dbCertIds.length;i<len;i++){
                var certId = dbCertIds[i];
                if(allCertId.indexOf(certId) == -1 && cancelCheckCertIds.indexOf(certId) == -1){
                    idsArr += ',' + certId;
                }
            }
            if(idsArr.indexOf(',') != -1 && idsArr.indexOf(',') == 0){
                idsArr = idsArr.substring(1);
            }
        }
        searchCertList(idsArr)
    }else{
        $('#viewCheckedCertBtn').html('查看已勾选');
        clearSearchCertList();
    }
}
//查询当前已经配置了的证书
function getSaveCurrQualCert() {
    var dbCertIds = '';
    $.ajax({
        url: ctx+'/aea/bus/cert/getSavedCert.do',
        type: 'POST',
        async:false,    //同步查询
        data:{
            'busRecordId': currQualId,
            'busTableName':'AEA_IM_QUAL'
        },
        success: function (result){
            if(result.success){
                dbCertIds = result.content;
            }
        },
        error:function(){
            swal('错误信息', '查询失败！', 'error');
        }
    });
    return dbCertIds;
}

//配置资质等级类型
function configQualLevel() {
    if(typeof currQualId == 'undefined'){
        swal({
            title: '提示信息',
            text: '没有要配置的对象!',
            type: 'info',
            timer: 1000,
            showConfirmButton: false
        });
    }else{
        //回显配置
        $.ajax({
            url: ctx + '/supermarket/qual/getAeaImQual.do',
            type: 'POST',
            data: {'id': currQualId},
            async: false,
            success: function (data) {
                if(data){
                    $('#cfg_qual_level_modal').modal('show');
                    var qualLevelId = data.qualLevelId;
                    $('#cfg_qual_level_tip').css('display',qualLevelId == null?'':'none');
                    loadQualLevelRootNode(qualLevelId);
                }else{
                    swal('提示信息', "查询配置信息失败！", 'info');
                }
            },
            error: function () {
                swal('错误信息', "查询配置信息失败！", 'error');
            }
        });
    }
}
//加载资质等级类型根节点
function loadQualLevelRootNode(qualLevelId) {
    $.ajax({
        url: ctx + '/supermarket/qual/level/listAeaImQualLevelRootNode.do',
        type: 'POST',
        data: {},
        async: false,
        success: function (data) {
            if(data){
                var labels = '';
                var check = '';
                $(data).each(function (index,ele) {
                    if(qualLevelId && qualLevelId != null){
                        //回显已保存的
                        check = ele.qualLevelId == qualLevelId?'checked="checked"':'';
                    }else{
                        //没有保存默认选中第一个
                        check = index == 0?'checked="checked"':'';
                    }
                    var label =
                        '<label class="m-radio m-radio--solid m-radio--brand">\n' +
                        '     <input type="radio" value="'+ ele.qualLevelId +'" name="qualLevel" '+ check +' >'+ ele.qualLevelName +'\n' +
                        '          <span></span>\n' +
                        '</label>&nbsp;&nbsp';
                    labels += label;
                });
                $('#qualLevelRootLabel').html(labels);
            }else{
                swal('提示信息', "查询配置信息失败！", 'info');
            }
        },
        error: function () {
            swal('错误信息', "查询配置信息失败！", 'error');
        }
    });
}
//保存资质等级类型配置
function saveQualLevelCfg() {
    var checkValue = $('#cfg_qual_level_form input[name="qualLevel"]:checked').val();
    $.ajax({
        url: ctx + '/supermarket/qual/saveQualLevelCfg.do',
        type: 'POST',
        data: {
            'qualId': currQualId,
            'qualLevelId':checkValue
        },
        success: function (data) {
            var result = data.success;
            if(result){
                $('#cfg_qual_level_modal').modal('hide');
                swal({
                    title: '提示信息',
                    text: '保存成功!',
                    type: 'success',
                    timer: 1000,
                    showConfirmButton: false
                });
            }else{
                swal('提示信息', "保存失败！", 'info');
            }
        },
        error: function () {
            swal('错误信息', "配置资质类型失败！", 'error');
        }
    });
}
//配置证书
function configCert() {
    if(typeof currQualId == 'undefined'){
        swal({
            title: '提示信息',
            text: '没有要配置的对象!',
            type: 'info',
            timer: 1000,
            showConfirmButton: false
        });
    }else{
        $('#dialog_config_qual_cert').modal('show');
        clearGlobalArray();
        clearSearchCertList();
    }
}
