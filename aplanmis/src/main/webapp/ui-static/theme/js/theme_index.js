// 初始化加载函数
var add_theme_validator = null;
var submitIsNewOrNot = 0;
$(function(){

    //初始化分类下拉框的值
    if(!isNotBank(themeTypeName))themeTypeName = "全部分类";
    $('#searchAllBtn').html(themeTypeName+'<i class="fa fa-angle-down m--margin-left-10"></i>');

    //初始化查询框的值
    if(isNotBank(keyword)){
        $("#keyword").focus();
        $("#keyword").val(keyword);
    }

    //初始化查询框 enter搜索
    $("#keyword").on("keydown",function (e) {
        e = e || event;
        var currKey = e.keyCode||e.which||e.charCode;
        if(currKey == 13){
            searchThemeData();
            return false;
        }
    });

    $('#common_set_img_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#opusOmSortDiv1').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#m_tabs_1_1').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    //初始化查询框的值
    if(isNotBank(keyword)){
        $("#keyword").focus();
        $("#keyword").val(keyword);
    }

    //初始化查询框 enter搜索
    $("#keyword").on("keydown",function (e) {
        e = e || event;
        var currKey = e.keyCode||e.which||e.charCode;
        if(currKey == 13){
            searchThemeData();
            return false;
        }
    });

    $('#submitAndNew').click(function(){
        submitIsNewOrNot = 1;
    });

    $('#submitAndNoNew').click(function(){
        submitIsNewOrNot = 0;
    });

    // 设置初始化校验
    add_theme_validator = $("#add_theme_form").validate({

        // 定义校验规则
        rules: {
            themeName: {
                required: true,
                maxlength: 500
            },
            themeCode: {
                required: true
            },
            themeType: {
                required: true,
            },
            gjbzsplclx: {
                required: true,
            },
            dueNum:{
                maxlength: 5
            },
            dueUnit:{
                maxlength: 1
            },
            complainPhone:{
                maxlength: 500
            },
            hotlinePhone:{
                maxlength: 500
            },
            handleAddress:{
                maxlength: 500
            },
            themeMemo:{
                required: true,
                maxlength: 2000
            },
            isOnlineSb:{
                required: true
            },
        },
        messages: {
            themeName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过500个字母!"
            },
            themeCode: {
                required: '<font color="red">此项必填！</font>',
            },
            themeType: {
                required: '<font color="red">此项必填！</font>',
            },
            gjbzsplclx: {
                required: '<font color="red">请填写国家标准审批流程类型！</font>',
            },
            dueNum:{
                maxlength: "长度不能超过5个字母!"
            },
            dueUnit:{
                maxlength: "长度不能超过1个字母!"
            },
            complainPhone:{
                maxlength: "长度不能超过500个字母!"
            },
            hotlinePhone:{
                maxlength: "长度不能超过500个字母!"
            },
            handleAddress:{
                maxlength: "长度不能超过500个字母!"
            },
            themeMemo:{
                required: '<font color="red">请填写申报说明！</font>',
                maxlength: "长度不能超过2000个字母!"
            },
            isOnlineSb:{
                required: '<font color="red">此项必填！</font>',
            },
        },
        // 提交表单
        submitHandler: function(form){

            var d = {};
            var t = $('#add_theme_form').serializeArray();
            $.each(t, function() {
                if(this.name=='handleTime'){ // 处理时间格式问题
                    if(this.value){
                        d[this.name] = this.value.replace('T',' ');
                    }
                }else{
                    d[this.name] = this.value;
                }
            });
            $('#submitAndNew').hide();
            $('#submitAndNoNew').hide();
            $.ajax({
                url: ctx+'/aea/par/theme/saveThemeData.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    $('#submitAndNew').show();
                    $('#submitAndNoNew').show();
                    if (result.success){
                        if(submitIsNewOrNot==1){
                            swal({
                                type: 'success',
                                title: '保存主题信息成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            addTheme();
                        }else{
                            $('#add_theme_modal').modal('hide');
                            location.reload();
                        }
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    $('#submitAndNew').show();
                    $('#submitAndNoNew').show();
                    // swal('错误信息', '保存主题信息失败！', 'error');
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });

    $('#closeAddThemeBtn').click(function(){
        $('#add_theme_modal').modal('hide');
        if(add_theme_validator!=null) {
            $("#add_theme_form").validate().resetForm();
        }
        searchThemeData();
    });
});

// 获取分类
function fecthCategory(aid){

    themeType = aid;
    themeTypeName = '全部分类';
    if(isNotBank(themeType)){
        themeTypeName = $('#'+aid).text();
    }
    $('#searchAllBtn').html(themeTypeName+'<i class="fa fa-angle-down m--margin-left-10"></i>');
    searchThemeData();
}

// 查询
function searchThemeData() {

    keyword = $('#keyword').val();
    location.href = ctx+'/aea/par/theme/index.do?themeType='+ themeType +'&themeTypeName='+ encodeURI(themeTypeName) +'&keyword='+ encodeURI(keyword);
}

// 清空查询
function clearSearchThemeData() {

    $('#keyword').val('');
    location.href = ctx+'/aea/par/theme/index.do';
}

// 判断是否为空
function isNotBank(str){

    if(str!=null&&str!=undefined&&str!=''){
        return true;
    }else{
        return false;
    }
}

//新增主题
function addTheme(){

    $('#add_theme_modal').modal('show');
    $('#m_tabs_1_1').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#add_theme_modal_title').html('新增主题');
    $('#add_theme_form')[0].reset();
    if(add_theme_validator!=null) {
        $("#add_theme_form").validate().resetForm();
    }
    $('#add_theme_form input[name="themeId"]').val('');
    $('#submitAndNew').show();

    // 默认选中第一个
    $('#themeBasicInfo').trigger('click');

    // 初始化值
    $("#add_theme_form input[name='handleTime']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));

    $("#add_theme_form select[name='isMainline'] option:eq(1)").prop("selected", 'selected');
    $("#add_theme_form select[name='isAuxiline'] option:eq(1)").prop("selected", 'selected');
    // $("#add_theme_form select[name='isTechspectline'] option:eq(1)").prop("selected", 'selected');

    $("#add_theme_form input[name='mainlineAlias']").val('主线阶段');
    $("#add_theme_form input[name='auxilineAlias']").val('辅线服务');
    // $("#add_theme_form input[name='techspectlineAlias']").val('技术审查');

    $("#add_theme_form select[name='themeType'] option:eq(1)").prop("selected", 'selected');
    $("#add_theme_form select[name='isOnlineSb'] option:eq(0)").prop("selected", 'selected');
    $("#add_theme_form select[name='dueUnit'] option:eq(1)").prop("selected", 'selected');
    $("#add_theme_form select[name='isImgIcon'] option:eq(2)").prop("selected", 'selected');
    $("#add_theme_form input[name='bigImgPath']").val('/admin/theme/index/imgs/32/pro_type_4.png');
}

// 编辑主题
function editThemeById(themeId){

    if(themeId){

        $('#add_theme_modal').modal('show');
        $('#m_tabs_1_1').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#add_theme_modal_title').html('编辑主题');
        $('#add_theme_form')[0].reset();
        if(add_theme_validator!=null) {
            $("#add_theme_form").validate().resetForm();
        }
        $('#submitAndNew').hide();

        // 默认选中第一个
        $('#themeBasicInfo').trigger('click');

        var themeData = null;
        $.ajax({
            url: ctx + '/aea/par/theme/getAeaParTheme.do',
            type: 'POST',
            data: {'id': themeId},
            async: false,
            success: function (data) {
                themeData = data;
                loadFormData(true,'#add_theme_form',data);
            },
            error:function(){
                swal('错误信息', "获取主题信息失败！", 'error');
            }
        });
        if(themeData!=null&&themeData.handleTime){
            $("#add_theme_form input[name='handleTime']").val(new Date(themeData.handleTime).format("yyyy-MM-ddThh:mm:ss"));
        }else{
            $("#add_theme_form input[name='handleTime']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
        }
    }else{
        swal('提示信息', "主题id为空！", 'info');
    }
}

// 进入主题设置
function goThemeSetings(themeId){

    location.href = ctx + '/aea/par/theme/themeVerIndex.do?themeId='+themeId;
}

// 删除主题
function deleteThemeById(themeId){

    swal({
        title: '此操作影响：',
        text: '将删除此主题,主题相关关联数据将失效，您确定删除吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value){
            $.ajax({
                url: ctx + '/aea/par/theme/deleteAeaParThemeById.do',
                type: 'POST',
                data: {'id': themeId},
                success: function (result) {
                    if(result.success){
                        location.reload();
                    }else{
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

// 启用
function enableThemeById(obj,themeId){

    swal({
        title: '提示信息',
        text: "确定要启用选中的记录吗?",
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value){
            $.ajax({
                url: ctx + '/aea/par/theme/enableOrDisableThemeById.do',
                type: 'POST',
                data: {'themeId': themeId,'isActive': '1'},
                success: function (result) {
                    if(result.success){
                        swal({
                            title: '提示信息',
                            text: '启用成功！',
                            type: 'success',
                            timer: 1000
                        });
                    }else{
                        swal('错误信息', result.message, 'error');
                        $(obj).prop("checked",false);
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                    $(obj).prop("checked",false);
                }
            });
        }else{
            $(obj).prop("checked",false);
        }
    });
}

// 禁用
function disableThemeById(obj,themeId){

    swal({
        title: '提示信息',
        text: "确定要禁用选中的记录吗?",
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value){
            $.ajax({
                url: ctx + '/aea/par/theme/enableOrDisableThemeById.do',
                type: 'POST',
                data: {'themeId': themeId,'isActive': '0'},
                success: function (result) {
                    if(result.success){
                        swal({
                            title: '提示信息',
                            text: '禁用成功！',
                            type: 'success',
                            timer: 1000
                        });
                    }else{
                        swal('错误信息', result.message, 'error');
                        $(obj).prop("checked",true);
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                    $(obj).prop("checked",true);
                }
            });
        }else{
            $(obj).prop("checked",true);
        }
    });
}

function isSelectImgType(obj, type){

    var value = $(obj).val();
    if(!value){
        if(type=='small'){
            selectImgType('theme','smallImgPath','small');
        }else if(type=='middle') {
            selectImgType('theme','middleImgPath','middle');
        }else if(type=='big'){
            selectImgType('theme','bigImgPath','big');
        }else{
            selectImgType('theme','hugeImgPath','huge');
        }
    }
}
