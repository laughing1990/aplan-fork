var curThemeVerId = null;
var curIsEditable = false;
var checkBoxInInoutIds = [];
$(function(){

    // 初始化高度
    $('#view_theme_ver_scroll').css({'max-height': $('#westPanel').height()-100,'height': $('#westPanel').height()-100});

    // $('#view_theme_ver_tb').bootstrapTable('resetView',{
    //     height: $('#westPanel').height()-161
    // });


    $('#showParStageTable1').bootstrapTable('resetView',{
        height: $('#westPanel').height()-110
    });

    $('#showParStageTable2').bootstrapTable('resetView',{
        height: $('#westPanel').height()-110
    });

    // $('#showParStageTable3').bootstrapTable('resetView',{
    //     height: $('#westPanel').height()-161
    // });

    $("#view_theme_ver_scroll").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

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

    // $('#add_stage_scroll').niceScroll({
    //
    //     cursorcolor: "#e2e5ec",//#CC0071 光标颜色
    //     cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
    //     cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
    //     touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
    //     cursorwidth: "4px", //像素光标的宽度
    //     cursorborder: "0", //   游标边框css定义
    //     cursorborderradius: "2px",//以像素为光标边界半径
    //     autohidemode: true  //是否隐藏滚动条
    // });

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

    // 记载主题版本数据
    loadThemeVerTable();
});

// 记载load相关数据
function loadThemeVerRelData(){

    // 记载相关数据
    $('#stageListTab').trigger('click');
    $('#stageListTab').addClass('active');
    $('#auxListTab').removeClass('active');
}

// 操作指引
function openOperaInfo(){

    $('#theme_ver_opera_modal').modal('show');
}

// 加载主题版本数据
var view_theme_ver_tb;
function loadThemeVerTable(){

    $('#view_theme_ver_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
    if (view_theme_ver_tb != null) {
        view_theme_ver_tb.bootstrapTable('destroy');
    }
    view_theme_ver_tb = $('#view_theme_ver_tb').bootstrapTable({
        url: ctx+'/aea/par/theme/ver/listAeaParThemeVerNoPage.do',
        method: 'get',
        queryParams: {
            'themeId': themeId
        },
        height: ($('#westPanel').height()-100),
        undefinedText: '-',
        striped: false,
        cache: false,
        contentType: 'application/json',
        pagination: false,
        onlyInfoPagination: false,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 5,
        pageList: [5],
        paginationShowPageGo: true,
        paginationVAlign: 'bottom',
        paginationHAlign: 'right',
        onClickRow:function (row, $element) {

            var tbobj = document.getElementById("view_theme_ver_tb");
            tbobj.rows[1].removeAttribute("style");
            $('.changeColor').removeClass('changeColor');
            $($element).addClass('changeColor');
            curThemeVerId = row.themeVerId;
            if(row.isPublish == '0'||row.isPublish == '2'){
                curIsEditable = true;
            }else{
                curIsEditable = false;
            }
            // 记载表格相关数据
            loadThemeVerRelData();
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
        },
        columns: [
            {
                filed: "themeVerId",
                visible: false
            },
            {
                field: "themeId",
                visible: false
            },
            {
                field: "verNum",
                title: "版本号",
                align: "left",
                width: 110,
                formatter: function(value, row) {
                    if(value){
                        if((''+value).indexOf('.01')>-1){
                            return 'V' + value;
                        }else{
                            return 'V' + value + '.0';
                        }
                    }
                }
            },
            {
                field: "isPublish",
                title: "状态",
                align: "left",
                width: 110,
                formatter: function(value, row) {

                    var curVer = "<span class='myicon-tick-checked'></span>";
                    if (row.isPublish == '0') { // 未发布
                        return "未发布";
                    }else if (row.isPublish == '1') { // 正式发布
                        return "已发布&nbsp;"+ curVer;
                    }else if (row.isPublish == '2') { // 试运行
                        return "试运行&nbsp;"+ curVer;
                    }else if(row.isPublish == '3') { // 已过时
                        return "已过时";
                    }
                }
            },
            {
                field: '_operate',
                title: '操作',
                sortable: false,
                // width: 300,
                align: "left",
                formatter: function (value, row) {

                var testRunBtn = ''; // 试运行
                var publishBtn = ''; // 发布
                var copyBtn = '<a class="dropdown-item" href="#" onclick="copyOneThemeVer(\''+ row.themeId +'\',\''+ row.themeVerId +'\')"><i class="la la-file"></i>&nbsp;&nbsp;复制</a>';
                var attBtn = '<a class="dropdown-item" href="#" onclick="showThemeVerAtt(\''+ row.themeVerId +'\', \'' + row.isPublish + '\')"><i class="flaticon-network"></i>&nbsp;&nbsp;流程图</a>';
                var showBpmnDiagram = '<a class="dropdown-item" href="#" onclick="showBpmnDiagram(\''+ row.themeVerId +'\', \'' + row.isPublish + '\')"><i class="flaticon-network"></i>&nbsp;&nbsp;在线运行图</a>';
                var createBpmnDiagram = '<a class="dropdown-item" href="#" onclick="createBpmnDiagram(\''+ row.themeVerId +'\', \'' + row.isPublish + '\')"><i class="flaticon-network"></i>&nbsp;&nbsp;逆向生成运行图</a>';
                var setShareInoutBtn = '<a class="dropdown-item" href="#" onclick="setShareInoutBtn(\''+ row.themeVerId +'\', \'' + row.isPublish + '\')"><i class="la la-gear"></i>&nbsp;&nbsp;设置材料共享</a>';

                if (row.isPublish == '0') {
                    testRunBtn = '<a class="dropdown-item" href="#" onclick="testRunThemeVer(\''+row.themeId+'\',\''+row.themeVerId+'\', \'' + row.isPublish + '\', \'' + row.verNum + '\')"><i class="la la-check"></i>&nbsp;&nbsp;试运行</a>';
                }
                if (row.isPublish== '0' || row.isPublish == '2') {
                    publishBtn = '<a class="dropdown-item" href="#" onclick="publishThemeVer(\''+row.themeId+'\',\''+row.themeVerId+'\',\''+row.isPublish+'\', \''+row.verNum+'\')"><i class="la la-lock"></i>&nbsp;&nbsp;发布</a>';
                }

                return '<div class="dropdown">'+
                            '<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">'+
                               '<i class="la la-ellipsis-h"></i>'+
                            '</a>'+
                            '<div class="dropdown-menu dropdown-menu-center">'+
                                testRunBtn + publishBtn + copyBtn + attBtn + showBpmnDiagram + createBpmnDiagram +setShareInoutBtn
                            '</div>'+
                        '</div>';
                }
            }
        ],
        onLoadSuccess: function (data) {

            var tableId = document.getElementById("view_theme_ver_tb");
            $(data).each(function (index, item) {
                if (index==0) {
                    $(item).addClass('selected');
                    tableId.rows[index + 1].setAttribute("style","background: #ece6e6;");
                    curThemeVerId = data[index].themeVerId;
                    if(data[index].isPublish == '0'||data[index].isPublish == '2'){
                        curIsEditable = true;
                    }else{
                        curIsEditable = false;
                    }
                    // 记载表格相关数据
                    loadThemeVerRelData();
                } else {
                    $(item).removeClass('selected');
                }
            });
        }
    });
}

/**
 * 复制某个版本数据作为新版本
 *
 * @param themeId
 * @param themeVerId
 */
function copyOneThemeVer(themeId, themeVerId){

    curThemeVerId = themeVerId;
    $.ajax({
        url: ctx + '/aea/par/theme/ver/unpublished/num.do',
        type: 'POST',
        data: {
            'themeId': themeId,
        },
        async: false,
        success: function (result) {
            if (result.success) {
                swal({
                    text: "此操作将复制当前版本数据并生成最新未发布版本,未发布版本下数据可以编辑,您确定执行吗？",
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(function (result) {
                    if (result.value) {

                        $("#uploadProgress").modal("show");
                        $('#uploadProgressMsg').html("复制版本数据中,请勿点击,耐心等候...");
                        // $('.copy').hide();

                        // loading();

                        $.ajax({
                            url: ctx + '/aea/par/theme/ver/copy/version.do',
                            type: 'POST',
                            data:  {
                                'themeId': themeId,
                                'themeVerId': themeVerId,
                            },
                            // async: false,
                            success: function (result) {
                                if (result.success&&result.content) {

                                    setTimeout(function(){

                                        $("#uploadProgress").modal('hide');
                                        swal({
                                            type: 'success',
                                            title: '复制成功！',
                                            showConfirmButton: false,
                                            timer: 1500
                                        });
                                        view_theme_ver_tb.bootstrapTable('refresh');
                                        curThemeVerId = result.content.themeVerId;
                                        curIsEditable = true;
                                        loadThemeVerRelData();
                                    },500);

                                }else{

                                    setTimeout(function(){

                                        $("#uploadProgress").modal('hide');
                                        swal('错误信息', "复制失败！", 'error');
                                    },500);
                                }
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {

                                setTimeout(function(){

                                    $("#uploadProgress").modal('hide');
                                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                                },500);
                            }
                        });
                    }
                });
            }else {
                swal('提示信息', result.message, 'info');
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

/**
 * 试运行版本版本
 *
 * @param themeId  主题id
 * @param themeVerId  主题版本id
 * @param verStatus  版本状态
 * @param verNum 版本序号
 *
 */
function testRunThemeVer(themeId, themeVerId, verStatus, verNum){

    curThemeVerId = themeVerId;
    curIsEditable = true;
    if(verStatus=='0'){
        swal({
            text: "此操作将更改当前版本状态为试运行并启用,版本下数据可以编辑,您确定执行吗？",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                if(verNum&&verNum.indexOf(".01")>-1){
                    verNum = parseInt(''+verNum) + 1;
                    $.ajax({
                        url: ctx + '/aea/par/theme/ver/testRunOrPublished.do',
                        type: 'POST',
                        data:  {
                            'themeId': themeId,
                            'themeVerId': themeVerId,
                            'verNum': verNum,
                            'type': '2'
                        },
                        // async: false,
                        success: function (result) {
                            if (result.success) {

                                swal({
                                    type: 'success',
                                    title: '操作成功！',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                                view_theme_ver_tb.bootstrapTable('refresh');
                                // 记载表格相关数据
                                loadThemeVerRelData();
                            }else{
                                swal('错误信息', "复制失败！", 'error');
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        }
                    });
                }else{
                    swal('提示信息', "版本序号有问题！", 'info');
                }
            }
        });
    }else{
        swal('提示信息', "当前版本不是未发布版本！", 'info');
    }
}

/**
 * 发布版本
 *
 * @param themeId  主题id
 * @param themeVerId  主题版本id
 * @param verStatus  版本状态
 * @param verNum 版本序号
 *
 */
function publishThemeVer(themeId, themeVerId, verStatus, verNum) {

    curThemeVerId = themeVerId;
    curIsEditable = true;
    if(verStatus=='0' || verStatus=='2'){
        swal({
            text: "此操作将更改当前版本状态为已发布并将其他试运行或发布版本更改为已过时,所有版本下数据不可编辑,您确定执行吗？",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                if(verNum){
                    if(verNum.indexOf(".01")>-1){
                        verNum = parseInt(''+verNum) + 1;
                    }else{
                        verNum = parseInt(''+verNum);
                    }
                    $.ajax({
                        url: ctx + '/aea/par/theme/ver/testRunOrPublished.do',
                        type: 'POST',
                        data:  {
                            'themeId': themeId,
                            'themeVerId': themeVerId,
                            'verNum': verNum,
                            'type': '1'
                        },
                        // async: false,
                        success: function (result) {
                            if (result.success) {

                                swal({
                                    type: 'success',
                                    title: '操作成功！',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                                view_theme_ver_tb.bootstrapTable('refresh');
                                curIsEditable = false;
                                // 记载表格相关数据
                                loadThemeVerRelData();
                            }else{
                                swal('错误信息', "复制失败！", 'error');
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        }
                    });
                }else{
                    swal('提示信息', "版本序号有问题！", 'info');
                }
            }
        });
    }else{
        swal('提示信息', "当前版本不是未发布或者试运行版本！", 'info');
    }
}

/**
 *
 * 审批流流程图
 *
 * @param themeVerId
 * @param verStatus
 */
function showThemeVerAtt(themeVerId, verStatus){

    curThemeVerId = themeVerId;
    if(verStatus=='0' || verStatus=='2'){
        curIsEditable = true;
    }else{
        curIsEditable = false;
    }
    viewThemeVerAtt();
}

// 在线运行图
function showBpmnDiagram(themeVerId, verStatus){

    curThemeVerId = themeVerId;
    if(verStatus=='0' || verStatus=='2'){
        curIsEditable = true;
    }else{
        curIsEditable = false;
    }
    openFullWindow("./ver/showThemeVerDiagram.do?themeVerId="+themeVerId+'&verStatus='+verStatus);
}

function createBpmnDiagram(themeVerId, verStatus){

    curThemeVerId = themeVerId;
    if(verStatus=='0' || verStatus=='2'){
        curIsEditable = true;
    }else{
        curIsEditable = false;
    }
    if(curIsEditable){
        swal({
            text: "注意：逆向生成会覆盖原有流程图！！！ 您确定执行吗？",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {

                $("#uploadProgress").modal("show");
                $('#uploadProgressMsg').html("流程图生成中,请勿点击,耐心等候...");

                $.ajax({
                    url: ctx + '/aea/par/theme/ver/createBpmnDiagram.do',
                    type: 'POST',
                    data:  {
                        'themeVerId': themeVerId,
                    },
                    success: function (result) {
                        if (result.success) {

                            setTimeout(function(){

                                $("#uploadProgress").modal('hide');
                                swal({
                                    type: 'success',
                                    title: '逆向生成成功！',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                            },500);

                        }else{
                            setTimeout(function(){

                                $("#uploadProgress").modal('hide');
                                swal('错误信息', "逆向生成失败！", 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }else {
        swal('提示信息', '当前版本数据不可以编辑,不能生成在线运行图!', 'info');
    }
}

// 设置共享材料
function setShareInoutBtn(themeVerId, verStatus){

    curThemeVerId = themeVerId;
    if(verStatus=='0' || verStatus=='2'){
        curIsEditable = true;
    }else{
        curIsEditable = false;
    }
    if(curIsEditable){
        $('#saveShareBtn').show();
    }else{
        $('#saveShareBtn').hide();
    }
    viewShareMat();
}

// 显示材料共享
function viewShareMat(){

    $('#show_share_mat_modal').modal('show');
    $.ajax({
        type: 'post',
        url: ctx + '/aea/share/mat/listOutItemTreeByThemeVerId',
        async: false,
        data: {'themeVerId': curThemeVerId},
        success: function (data) {
            selectOutItemLoadData = data;
            if (data && data.length > 1) {
                $("#outItemVerId").val(data[1].id);
                $("#search_all_out_item_list_form input[name='outItemName']").val(data[1].name);
            }else{
                swal('提示信息', '此主题版本暂无输出具有批复材料的事项！', 'info');
                return;
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            swal('错误信息', '获取当前主题版本输出事项异常，错误信息：'+ XMLHttpRequest.responseText, 'error');
            return;
        }
    });

    $.ajax({
        type: 'post',
        url: ctx + '/aea/share/mat/listInItemTreeByThemeVerId.do',
        async: false,
        data: {'themeVerId': curThemeVerId},
        success: function (data) {
            selectInItemLoadData = data;
            if (data && data.length > 1) {

                $("#inItemVerId").val(data[1].id);
                $('#inItemStateVerId').val(data[1].icon);
                $("#search_in_item_list_form input[name='inItemName']").val(data[1].name);

            }else{
                swal('提示信息', '此主题版本输入暂无具有批复材料的事项！', 'info');
                return;
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            swal('错误信息', '获取当前主题版本输入事项异常，错误信息：'+ XMLHttpRequest.responseText, 'error');
            return;
        }
    });
    initOutItemMatTable();
}