var commonQueryParams = [];
// 初始化加载函数
var add_stage_validator = null;
$(function () {

    // //在表格 body 渲染完成后触发
    // $('#showParStageTable1').on('post-body.bs.table', function () {
    //     var h = $('.col-xl-9 .m-portlet').height();
    //     $('#showParStageTable1').bootstrapTable('resetView',{
    //         height:0.85*h
    //     });
    //
    // });
    //
    // $('#showParStageTable2').on('post-body.bs.table', function () {
    //     var h = $('.col-xl-9 .m-portlet').height();
    //     $('#showParStageTable2').bootstrapTable('resetView',{
    //         height:0.85*h
    //     });
    //
    // });
    //
    // $('#showParStageTable3').on('post-body.bs.table', function () {
    //     var h = $('.col-xl-9 .m-portlet').height();
    //     $('#showParStageTable3').bootstrapTable('resetView',{
    //         height:0.85*h
    //     });
    //
    // });

    // $('#add_stage_form select[name="useEl"]').change(function(){
    //     var value = $(this).children('option:selected').val();
    //     if(value=='0'){ // 禁用
    //        $('#stageElDiv').hide();
    //     }else{ // 启用
    //         $('#stageElDiv').show();
    //     }
    // });

    //初始化查询框 enter搜索
    $("#stageKeyword1").on("keydown", function (e) {
        e = e || event;
        var currKey = e.keyCode || e.which || e.charCode;
        if (currKey == 13) {
            searchParStageCondition('1');
            return false;
        }
    });

    $("#stageKeyword2").on("keydown", function (e) {
        e = e || event;
        var currKey = e.keyCode || e.which || e.charCode;
        if (currKey == 13) {
            searchParStageCondition('2');
            return false;
        }
    });

    $("#stageKeyword3").on("keydown", function (e) {
        e = e || event;
        var currKey = e.keyCode || e.which || e.charCode;
        if (currKey == 13) {
            searchParStageCondition('3');
            return false;
        }
    });

    // 设置初始化校验
    add_stage_validator = $("#add_stage_form").validate({

        // 定义校验规则
        rules: {
            isNode: {
                required: true
            },
            stageName: {
                required: true,
                maxlength: 500
            },
            stageCode:{
                required: true,
                maxlength: 50
            },
            dueNum: {
                maxlength: 5
            },
            dueUnit: {
                maxlength: 1
            },
            iconCss: {
                maxlength: 500
            },
            bgCss: {
                maxlength: 500
            },
            stageMemo: {
                maxlength: 2000
            },
            isOptionItem:{
                required: true,
            },
            isSelItem:{
                required: true,
            },
            lcbsxlx:{
                required: true,
            },
            dybzspjdxh1:{
                required: true,
            }
        },
        messages: {
            isNode: {
                required: '<font color="red">此项必填！</font>'
            },
            stageName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过500个字母!"
            },
            stageCode:{
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!"
            },
            dueNum: {
                maxlength: "长度不能超过5个字母!"
            },
            dueUnit: {
                maxlength: "长度不能超过1个字母!"
            },
            iconCss: {
                maxlength: "长度不能超过500个字母!"
            },
            bgCss: {
                maxlength: "长度不能超过500个字母!"
            },
            stageMemo: {
                maxlength: "长度不能超过2000个字母!"
            },
            isOptionItem:{
                required: '<font color="red">此项必填！</font>'
            },
            isSelItem:{
                required: '<font color="red">此项必填！</font>'
            },
            lcbsxlx:{
                required: '<font color="red">此项必填！</font>'
            },
            dybzspjdxh1:{
                required: '<font color="red">此项必填！</font>'
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#addStageBtn').hide();
            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

            var isNode = $('#add_stage_form input[name="isNode"]').val();
            var d = {};
            var t = $('#add_stage_form').serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/aea/par/stage/saveAeaParStage.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success) {

                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '保存成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 隐藏模式框
                            $('#addStageBtn').show();
                            $('#add_stage_modal').modal('hide');
                            searchParStageCondition(isNode);

                        },500);
                    } else {
                        setTimeout(function(){

                            $('#addStageBtn').show();
                            $('#add_stage_modal').modal('hide');
                            swal('错误信息', result.message, 'error');

                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $('#addStageBtn').show();
                        $('#add_stage_modal').modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');

                    },500);
                }
            });
        }
    });

    $('#closeAddStageBtn').click(function () {
        $('#add_stage_modal').modal('hide');
        if(add_stage_validator!=null) {
            $("#add_stage_form").validate().resetForm();
        }
    });
});

function isNodeFormatter(value, row, index, field) {

    if(value){
        if(value=='1'){
            return '主线';
        }else if(value=='2'){
            return '辅线';
        }else {
            return '技术审查线';
        }
    }
}

function isNeedStateFormatter(value, row, index, field) {

    if(value){
        if(value=='0'){
            return '否';
        }else if(value=='1'){
            return '是';
        }
    }
}

function isOptionItemFormatter(value, row, index, field) {

    if(value){
        if(value=='0'){
            return '不允许';
        }else if(value=='1'){
            return '允许';
        }
    }
}


//阶段设置相关操作
function operatorFormatter(value, row, index, field) {

    var title = '编辑';
    var icoCss = 'la la-edit';
    var auxSetRelMain = "";
    if(!curIsEditable){
        title = '查看';
        icoCss = 'la la-search';
    }

    var editBtn =   '<a href="javascript:editParStage(\'' + row.stageId + '\', \'' + row.isNode + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="'+ title +'"><i class="'+ icoCss +'"></i>' +
        '</a>';

    var deleteBtn = '<a href="javascript:deleteParStage(\''+row.stageId + '\', \'' + row.isNode + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="删除"><i class="la la-trash"></i>' +
        '</a>';

    var stageStateView = '<a href="javascript:setStageStateMindView(\''+row.stageId + '\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
        'title="思维导图"><i class="la la-gear"></i>' +
        '</a>';

    // 辅线
    if(row.isNode=='2'){
        auxSetRelMain  =  '<a href="javascript:setAuxRelMain(\''+row.stageId + '\',\''+row.parentId + '\')" ' +
                              'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                              'title="关联主线"><i class="flaticon-network"></i>' +
                          '</a>';
    }

    if(curIsEditable){
        return editBtn + auxSetRelMain + stageStateView + deleteBtn;
    }else{
        return editBtn + auxSetRelMain + stageStateView;
    }
}

// 设置辅线关联主线
function setAuxRelMain(stageId, parentId){

    viewAuxRelMain(stageId, parentId);
    if(curIsEditable){
        $('#btn_save_main_line_list_tb').show();
    }else{
        $('#btn_save_main_line_list_tb').hide();
    }
}

// 请空查询
function clearSearchStage(isNode) {

    $("#stageKeyword"+isNode).val('');//搜索框置空
    // 列表数据重新加载
    searchParStageCondition(isNode);
}

//查询
function searchParStageCondition(isNode) {

    var keywordVal = $("#stageKeyword"+isNode).val();
    commonQueryParams = [];
    commonQueryParams.push({name: "themeVerId", value: curThemeVerId});
    commonQueryParams.push({name: "isNode", value: isNode});
    if (keywordVal != "") {
        commonQueryParams.push({name: "keyword", value: keywordVal});
    }
    $("#showParStageTable"+isNode).bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#showParStageTable"+isNode).bootstrapTable('refresh');       //无参数刷新
}

//参数设置
function dealQueryParams(params) {

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
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

//后台返回的数据
function responseData(res) {

    return res;
}

/**
 * 返回主题
 */
function backToTheme() {

    location.href = ctx + '/aea/par/theme/index.do';
}

//新增阶段
function addParStage(isNode) {

    if(curIsEditable){
        if(curThemeVerId){

            $('#add_stage_modal').modal('show');
            var title = "阶段";
            if(isNode=='2'){
                title = "辅线";
            }else if(isNode=='3'){
                title = "技术审查线";
            }
            $('#add_stage_modal_title').html('新增' + title);
            $('#add_stage_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
            $('#add_stage_form')[0].reset();
            if(add_stage_validator!=null) {
                $("#add_stage_form").validate().resetForm();
            }

            // 默认选中第一个
            $('#stageBasicInfo').trigger('click');

            $('#add_stage_form input[name="stageId"]').val('');
            $('#add_stage_form input[name="themeVerId"]').val(curThemeVerId);
            $('#add_stage_form input[name="isNode"]').val(isNode);

            $('#add_stage_form input[name="isOptionItem1"]').prop("checked", true);
            $('#add_stage_form input[name="isOptionItem"]').val('1');

            $('#add_stage_form input[name="isSelItem1"]').prop("checked", false);
            $('#add_stage_form input[name="isSelItem"]').val('0');

            $('#add_stage_form input[name="useEl1"]').prop("checked", false);
            $('#add_stage_form input[name="useEl"]').val('0');

            $('#add_stage_form input[name="useOneForm1"]').prop("checked", false);
            $('#add_stage_form input[name="useOneForm"]').val('0');

            $('#add_stage_form input[name="dybzspjdxh1"]').each(function(){

                if(isNode=='1'){
                    if($(this).val()!="5"){
                        $(this).removeAttr("disabled");
                    }else{
                        $(this).attr("disabled", "disabled");
                    }
                }else{
                    if($(this).val()=="5"){
                        $(this).removeAttr("disabled");
                        $(this).prop("checked", true);
                    }else{
                        $(this).attr("disabled", "disabled");
                    }
                }
            });

            if(isNode=='1'){

                $('#isShowItemDiv').hide();
                $('#dybzfxfwDiv').hide();
                $('#add_stage_form input[name="isShowItem1"]').prop("checked", true);
                $('#add_stage_form input[name="isShowItem"]').val('1');
                $('#add_stage_form input[name="dygjbzfxfw"]').rules("remove");

            }else if(isNode=='2'){

                $('#isShowItemDiv').show();
                $('#dybzfxfwDiv').show();
                $('#add_stage_form input[name="isShowItem1"]').prop("checked", false);
                $('#add_stage_form input[name="isShowItem"]').val('0');
                $('#add_stage_form input[name="dygjbzfxfw"]').rules("add", {
                    required: true,
                    messages: {
                        required: '<font color="red">辅线服务必选！</font>'
                    }
                });
            }

            $('#add_stage_form input[name="isCheckItem1"]').prop("checked", false);
            $('#add_stage_form input[name="isCheckItem"]').val('0');

            $("#add_stage_form input[name='lcbsxlx'][value='1']").prop("checked", true);
            $("#add_stage_form input[name='handWay'][value='1']").prop("checked", true);

            $('#stageElDiv').hide();
            $("#add_stage_form select[name='dueUnit'] option:eq(1)").prop("selected", 'selected');
            $("#add_stage_form select[name='isImgIcon'] option:eq(1)").prop("selected", 'selected');
            $("#add_stage_form input[name='bigImgPath']").val('/admin/theme/index/imgs/32/施工许可02.png');
            $("#add_stage_form select[name='anticipateType'] option:eq(1)").prop("selected", 'selected');

            $.ajax({
                url: ctx + '/aea/par/stage/getMaxSortNo.do',
                type: 'POST',
                data: {'themeVerId': curThemeVerId},
                async: false,
                success: function (data) {
                    if(data){
                        $("#add_stage_form input[name='sortNo']").val(data);
                    }
                },
                error: function () {

                }
            });
        }else{
            swal('提示信息', "请选择版本定义节点！", 'info');
        }
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

// 编辑阶段
function editParStage(stageId, isNode) {

    $('#add_stage_modal').modal('show');

    var title = "阶段";
    if(isNode=='2'){
        title = "辅线";
    }else if(isNode=='3'){
        title = "技术审查线";
    }

    if(isNode=='1'){

        $('#isShowItemDiv').hide();
        $('#dybzfxfwDiv').hide();

    }else if(isNode=='2'){

        $('#isShowItemDiv').show();
        $('#dybzfxfwDiv').show();
    }

    if(curIsEditable){
        $('#addStageBtn').show();
        $('#add_stage_modal_title').html('编辑' + title);
    }else{
        $('#addStageBtn').hide();
        $('#add_stage_modal_title').html('查看' + title);
    }

    $('#add_stage_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    $('#add_stage_form')[0].reset();
    if(add_stage_validator!=null) {
        $("#add_stage_form").validate().resetForm();
    }

    $('#add_stage_form input[name="dybzspjdxh1"]').each(function(){

        if(isNode=='1'){
            if($(this).val()!="5"){
                $(this).removeAttr("disabled");
            }else{
                $(this).attr("disabled", "disabled");
            }
        }else{
            if($(this).val()=="5"){
                $(this).removeAttr("disabled");
            }else{
                $(this).attr("disabled", "disabled");
            }
        }
    });

    // 默认选中第一个
    $('#stageBasicInfo').trigger('click');

    if(stageId){
        $.ajax({
            url: ctx + '/aea/par/stage/getAeaParStage.do',
            type: 'POST',
            data: {'id': stageId},
            async: false,
            success: function (data) {
                if (data){

                    if(data.isOptionItem=='1'){
                        $('#add_stage_form input[name="isOptionItem1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="isOptionItem1"]').prop("checked", false);
                    }
                    if(data.isSelItem=='1'){
                        $('#add_stage_form input[name="isSelItem1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="isSelItem1"]').prop("checked", false);
                    }

                    if(data.useEl=='1'){
                        $('#add_stage_form input[name="useEl1"]').prop("checked", true);
                        $('#stageElDiv').show();
                    }else{
                        $('#add_stage_form input[name="useEl1"]').prop("checked", false);
                        $('#stageElDiv').hide();
                    }

                    if(data.useOneForm=='1'){
                        $('#add_stage_form input[name="useOneForm1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="useOneForm1"]').prop("checked", false);
                    }

                    if(data.isShowItem=='1'){
                        $('#add_stage_form input[name="isShowItem1"]').prop("checked", true);
                    }else {
                        $('#add_stage_form input[name="isShowItem1"]').prop("checked", false);
                    }

                    if(data.isCheckItem=='1'){
                        $('#add_stage_form input[name="isCheckItem1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="isCheckItem1"]').prop("checked", false);
                    }

                    if(data.isCheckItemform=='1'){
                        $('#add_stage_form input[name="isCheckItemform1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="isCheckItemform1"]').prop("checked", false);
                    }

                    if(data.isCheckPartform=='1'){
                        $('#add_stage_form input[name="isCheckPartform1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="isCheckPartform1"]').prop("checked", false);
                    }

                    if(data.isCheckProj=='1'){
                        $('#add_stage_form input[name="isCheckProj1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="isCheckProj1"]').prop("checked", false);
                    }

                    if(data.isCheckStage=='1'){
                        $('#add_stage_form input[name="isCheckStage1"]').prop("checked", true);
                    }else{
                        $('#add_stage_form input[name="isCheckStage1"]').prop("checked", false);
                    }

                    if (data.dybzspjdxh){

                        var arr = data.dybzspjdxh.split(",");
                        for(var i in arr){
                            $("#add_stage_form input[name='dybzspjdxh1'][value='"+ arr[i] +"']").prop("checked", true);
                        }
                    }

                    // $("#add_stage_form input[name='lcbsxlx1']").prop("checked", false);
                    // if (data.lcbsxlx=='1'){
                    //     $("#add_stage_form input[name='lcbsxlx1'][value='1']").prop("checked", true);
                    // }else{
                    //     $("#add_stage_form input[name='lcbsxlx1'][value='2']").prop("checked", true);
                    // }

                    // $("#add_stage_form input[name='handWay1']").prop("checked", false);
                    // if (data.handWay=='1'){
                    //     $("#add_stage_form input[name='handWay1'][value='1']").prop("checked", true);
                    // }else{
                    //     $("#add_stage_form input[name='handWay1'][value='0']").prop("checked", true);
                    // }
                }
                loadFormData(true, '#add_stage_form', data);
            },
            error: function () {
                swal('错误信息', "获取信息失败！", 'error');
            }
        });
    }else{
        swal('提示信息', "请选择操作记录！", 'info');
    }
}

// 删除主题
function deleteParStage(stageId, isNode) {

    var title = "阶段";
    if(isNode=='2'){
        title = "模块";
    }else if(isNode=='3'){
        title = "技术审查线";
    }
    if(stageId){
        swal({
            title: '此操作影响：',
            text: '将删除主题'+ title +',关联'+ title +'数据将失效,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/par/stage/deleteAeaParStageById.do',
                    type: 'POST',
                    data: {'id': stageId},
                    success: function (result) {
                        if (result.success) {
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 重新加载数据
                            searchParStageCondition(isNode);
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
    }else{
        swal('提示信息', '操作对象id为空!', 'info');
    }
}

/**
 * 批量删除阶段
 * @returns {boolean}
 */
function batchDelParStage(isNode) {

    if(curIsEditable){
        var checkBoxs = $("#showParStageTable"+ isNode).bootstrapTable('getSelections');
        var stageIds = [];
        if(checkBoxs!=null&&checkBoxs.length>0){
            for(var i=0;i<checkBoxs.length;i++){
                stageIds.push(checkBoxs[i].stageId);
            }
            var title = "阶段";
            if(isNode=='2'){
                title = "模块";
            }else if(isNode=='3'){
                title = "技术审查线";
            }
            swal({
                title: '此操作影响：',
                text: '将删除主题'+ title +',关联'+ title +'数据将失效,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function(result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/par/stage/deleteAeaParStageByIds.do',
                        type: 'POST',
                        data: {'stageIds': stageIds.toString()},
                        success: function (result){
                            if(result.success){
                                swal({
                                    title: '提示信息',
                                    text: '批量删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                // 重新加载数据
                                searchParStageCondition(isNode);
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
    }else{
        swal('提示信息', '当前版本下数据不可编辑!', 'info');
    }
}

//=========================================================================================================
var stageIdVal;
//查看已发布版本的事项列表
function viewStageItem(stageId) {

    stageIdVal = stageId;
    $('#view_stage_item_modal').modal('show');
    $('#view_stage_item_modal_title').html('事项列表');
    searchStageItemCondition();
}
//=========================================================================================================

// 设置情形
function setStageRelStates(stageId){

    window.open(ctx +'/aea/par/stage/indexSetStageStates2.do?themeId='+themeId+'&stageId='+stageId,'设置情形');
}

// 查看事项情形
function viewStageState(stageId,stageName){

    openviewStageStateModal(stageId,'【'+stageName+'】情形材料数据');
}

function setStageStateMindView(stageId) {

    openFullWindow(ctx + '/rest/mind/mindIndex.do?busiType=stage&busiId='+stageId);
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

/**
 * @param name  获取值得对象
 * @param name1 需要赋值的对象
 * @param isMulit 是否多个
 */
function checkboxOnclick(name, name1, isMulit){

    var obj = document.getElementsByName(name);
    if(obj){
        if(isMulit){
            var check_val = [];
            for(var k in obj){
                if(obj[k].checked){
                    check_val.push(obj[k].value);
                }
            }
            var cb = check_val.join(',');
            $('#add_stage_form input[name="'+ name1 +'"]').val(cb);
        }else{
            if(obj[0].checked){
                $('#add_stage_form input[name="'+ name1 +'"]').val('1');
                if(name=='useEl1'){
                    $('#stageElDiv').show();
                }
            }else{
                $('#add_stage_form input[name="'+ name1 +'"]').val('0');
                if(name=='useEl1'){
                    $('#stageElDiv').hide();
                }
            }
        }
    }
}

function checkboxOnclick2(obj, name, name1, value){

    $('#add_stage_form input[name="'+ name +'"]').prop("checked", false);
    $(obj).prop("checked", true);
    $('#add_stage_form input[name="'+ name1 +'"]').val(value);
}