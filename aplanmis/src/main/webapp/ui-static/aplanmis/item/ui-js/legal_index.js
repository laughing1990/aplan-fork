var legalTree = null;  // 菜单树
var nodeList = [];
var lastValue = '';
var legalTreeSelectNode = null;
var add_legal_validator = null;
var add_legalClause_validator = null;
var legalTreeKey = null;
var legalTreeSettings = {
    edit: {
        enable: false,        //设置 zTree 是否处于编辑状态
        showRemoveBtn: false,//设置是否显示删除按钮
        showRenameBtn: false//设置是否显示编辑名称按钮
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId"
        }
    },
    view: {
        selectedMulti: false,//设置是否允许同时选中多个节点
        showTitle: false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
        showLine: false, //设置 zTree 是否显示节点之间的连线。
        showHorizontal: true//设置是否水平平铺树（自定义属性）
    },
    //用于捕获节点被点击的事件回调函数
    callback: {

        onRightClick: legalTreeOnRightClick
    }
};
$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height',$(document.body).height()-10);
    $('#legalTree').css({'max-height': $('#mainContentPanel').height()-116,'height': $('#mainContentPanel').height()-116});
    $("#legalTree").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $('#show_mat_att_tb_scroll').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 初始化树
    initLegalTree();

    // 初始化表单校验
    initValidate();

    // 关键字搜索文件库节点
    legalTreeKey = $("#keyWord");
    legalTreeKey.bind("focus", focusLegalTreeKey).bind("blur", blurLegalTreeKey()).bind("change cut input propertychange",searchLegal);
    legalTreeKey.bind('keydown', function (e){
        if(e.which == 13){
            searchLegal();
        }
    });

    //关闭新增法律法规窗口
    $('#closeAddLegalBtn').click(function(){

        $('#add_legal_modal').modal('hide');
        add_legal_validator.resetForm();
    });

    //关闭新增条款窗口
    $('#closeAddlegalClauseBtn').click(function(){

        $('#add_legalClause_modal').modal('hide');
        add_legalClause_validator.resetForm();
    });
});

function focusLegalTreeKey(e) {

    if (legalTreeKey.hasClass("empty")) {
        legalTreeKey.removeClass("empty");
    }
}

function blurLegalTreeKey(e) {

    if (legalTreeKey.get(0).value === "") {
        legalTreeKey.addClass("empty");
    }
    searchLegal();
}

//加载法律法规树
function initLegalTree() {

    $.ajax({
        url: ctx + '/aea/service/legal/getListLegalZtreeNode.do',
        type: 'post',
        data: {'id': ''},
        async: false,
        dataType: 'json',
        success: function (data) {
            if (data != null && data.length > 0) {
                legalTree = $.fn.zTree.init($("#legalTree"), legalTreeSettings, data);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            swal('错误信息', '初始化法律法规库异常,错误信息:'+XMLHttpRequest.responseText, 'error');
        }
    });
}

//右击树节点
function legalTreeOnRightClick(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();

    legalTree.selectNode(treeNode);
    legalTreeSelectNode = treeNode;
    if(event.target.tagName.toLowerCase()=='span'||event.target.tagName.toLowerCase()=='a'||event.target.tagName.toLowerCase()=='ul'){

        var y = event.clientY+10;
        var maxHeight = $('#legalTree').height();
        if(event.clientY>maxHeight){
            y -= ($('.contextMenuDiv').height()+5);
        }
        if(treeNode.type=='root'){

            showRMenu('legalContextMenu',event.clientX+15, y);
            $('#addLegalClauseBtn').hide();
            $('#editLegalBtn').hide();
            $('#delLegalBtn').hide();
        }
        if(treeNode.type=='legal'){

            showRMenu('legalContextMenu',event.clientX+15, y);
            $('#addLegalClauseBtn').show();
            $('#editLegalBtn').show();
            $('#delLegalBtn').show();
        }
        if(treeNode.type=='clause'){

            showRMenu('clauseContextMenu',event.clientX+15, y);
        }
    }
}

function changeDateFormat(value, row, index, field) {

    var dateVal = value + "";
    if (value != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        return date.getFullYear() + "-" + month + "-" + currentDate;
    }
    return "";
}

//新增法律法规
function addLegal() {

    hideRMenu('legalContextMenu');
    $('#add_legal_modal').modal('show');
    $('#add_legal_modal_title').html('新增法律法规');
    $('#add_legal_form')[0].reset();
    add_legal_validator.resetForm();
    $("#add_legal_form input[name='legalId']").val('');
    $("#add_legal_form input[name='exeDate']").val(new Date().format("yyyy-MM-dd"));
    clearAllFile();

    if (legalTreeSelectNode) {
        if (legalTreeSelectNode.id == 'root') {
            $("#add_legal_form input[name='parentLegalId']").val('');
        } else {
            $("#add_legal_form input[name='parentLegalId']").val(legalTreeSelectNode.id);
        }
    } else {
        swal('提示信息', "请选择父级节点！", 'info');
    }
}

//编辑法律法规
function editLegal(){

    hideRMenu('legalContextMenu');
    $('#add_legal_modal_title').html('编辑法律法规');
    $('#add_legal_form')[0].reset();
    add_legal_validator.resetForm();
    $("#add_legal_form input[name='legalId']").val('');
    $("#add_legal_form input[name='parentLegalId']").val('');
    $("#add_legal_form input[name='legalName']").val('');
    $("#add_legal_form input[name='legalLevel']").val('');
    $("#add_legal_form input[name='basicNo']").val('');
    $("#add_legal_form input[name='issueOrg']").val('');
    $("#add_legal_form input[name='exeDate']").val('');
    $("#add_legal_form input[name='serviceLegalMemo']").val('');
    clearAllFile();

    if (legalTreeSelectNode) {

        var selectNode = legalTreeSelectNode;
        // 控制页面展示
        $('#add_legal_modal').modal('show');
        $.ajax({
            url: ctx + '/aea/service/legal/getAeaServiceLegal.do',
            type: 'post',
            data: {'id': selectNode.id},
            async: false,
            success: function (data) {

                // 记载表单数据
                loadFormData(true, '#add_legal_form', data);

                if (data!=null&&data.serviceLegalAttCount&&data.serviceLegalAttCount!=0) {

                    $('#serviceLegalAttDiv').show();
                    $('#serviceLegalAttButton').html(data.serviceLegalAttCount + "个附件");
                }else{
                    $('#serviceLegalAttDiv').hide();
                }

                if(data!=null&&data.exeDate) {
                    $("#add_legal_form input[name='exeDate']").val(new Date(data.exeDate).format("yyyy-MM-dd"));
                }else{
                    $("#add_legal_form input[name='exeDate']").val(new Date().format("yyyy-MM-dd"));
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    } else {
        swal('提示信息', "请选择法律法规节点！", 'info');
    }
}

//刪除法律法规
function deleteLegal() {

    hideRMenu('legalContextMenu');
    if (legalTreeSelectNode) {
        var selectNode = legalTreeSelectNode;
        console.log(selectNode);
        if (selectNode.id == "root") {
            swal('提示信息', "根节点不能删除", 'info');
            return;
        }
        swal({
            title: '此操作影响：',
            text: '此操作将删除法律法规,相关材料关联数据将失效,您确定删除吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/service/legal/deleteAeaServiceLegalById.do',
                    type: 'post',
                    data: {'id': selectNode.id},
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1500
                            });
                            // 默认选中父级菜单
                            var parentNode = legalTreeSelectNode.getParentNode();
                            if (parentNode) {
                                legalTree.selectNode(parentNode);
                                legalTreeSelectNode = parentNode;
                            } else {
                                legalTreeSelectNode = null;
                            }
                            // 移除节点
                            var node = legalTree.getNodeByParam("id", selectNode.id, null);
                            legalTree.removeNode(node);
                        }
                    },
                    error: function () {
                        swal('错误信息', "获取法律法规信息失败！", 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', "请选择法律法规节点！", 'info');
    }
}

//新增条款
function addLegalClause(){

    hideRMenu('legalContextMenu');
    $('#add_legalClause_modal').modal('show');
    $('#add_legalClause_modal_title').html('新增法律法规条款');
    $('#add_legalClause_form')[0].reset();
    $("#add_legalClause_form input[name='legalClauseId']").val('');
    add_legalClause_validator.resetForm();
    clearAllFile();

    if (legalTreeSelectNode) {

        $("#add_legalClause_form input[name='legalId']").val(legalTreeSelectNode.id);
        $.ajax({
            url: ctx+'/aea/service/legal/clause/getMaxSortNo.do',
            type: 'post',
            data: {},
            async: false,
            success: function (data) {
                if(data) {
                    $("#add_legalClause_form input[name='sortNo']").val(data);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    } else {
        swal('提示信息', "请选择父级节点！", 'info');
    }
}

//编辑条款
function editLegalClause(){

    hideRMenu('clauseContextMenu');
    $('#add_legalClause_modal_title').html('编辑法律法规条款');
    $('#add_legalClause_form')[0].reset();
    add_legalClause_validator.resetForm();
    $("#add_legalClause_form input[name='legalClauseId']").val('');
    $("#add_legalClause_form input[name='legalId']").val('');
    $("#add_legalClause_form input[name='clauseTitle']").val('');
    $("#add_legalClause_form input[name='clauseContent']").val('');
    $("#add_legalClause_form input[name='sortNo']").val('');
    clearAllFile();

    if (legalTreeSelectNode) {
        var selectNode = legalTreeSelectNode;
        // 控制页面展示
        $('#add_legalClause_modal').modal('show');
        $.ajax({
            url: ctx + '/aea/service/legal/clause/getAeaServiceLegalClause.do',
            type: 'post',
            data: {'id': selectNode.id},
            async: false,
            success: function (data) {

                if (data!=null&&data.clauseAttCount&&data.clauseAttCount!=0) {

                    $('#clauseAttDiv').show();
                    $('#clauseAttButton').html(data.clauseAttCount + "个附件");
                }else{
                    $('#clauseAttDiv').hide();
                }

                // 记载表单数据
                loadFormData(true, '#add_legalClause_form', data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    } else {
        swal('提示信息', "请选择法律法规条款节点！", 'info');
    }
}

//删除条款
function deleteLegalClause() {

    hideRMenu('clauseContextMenu');
    if (legalTreeSelectNode) {
        var selectNode = legalTreeSelectNode;
        swal({
            title: '此操作影响：',
            text: '此操作将删除法律法规条款,相关材料关联数据将失效,您确定删除吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/service/legal/clause/deleteAeaServiceLegalClauseById.do',
                    type: 'post',
                    data: {'id': selectNode.id},
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1500
                            });
                            // 默认选中父级菜单
                            var parentNode = legalTreeSelectNode.getParentNode();
                            if (parentNode) {
                                legalTree.selectNode(parentNode);
                                legalTreeSelectNode = parentNode;
                            } else {
                                legalTreeSelectNode = null;
                            }
                            // 移除节点
                            var node = legalTree.getNodeByParam("id", selectNode.id, null);
                            legalTree.removeNode(node);
                        }
                    },
                    error: function () {
                        swal('错误信息', "获取法律法规条款信息失败！", 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', "请选择法律法规条款节点！", 'info');
    }
}

function showContentContainer(visualContainerId){

    $('#contentContainer >div').each(function () {
        if(visualContainerId==$(this).attr('id')){
            $(this).css('display','block');
        }
        else{
            $(this).css('display','none');
        }
    })
}

function delLegal(legalId) {

}

// 初始化表单检验
function initValidate() {

    //提交法律法规
    add_legal_validator = $('#add_legal_form').validate({
        // 定义校验规则
        rules: {
            legalName: {
                required: true,
            },
            legalLevel: {
                required: true,
            },
            basicNo: {
                required: true,
            },
            issueOrg: {
                required: true,
            },
            exeDate: {
                required: true,
            }

        },
        messages: {
            legalName: {
                required: '<font color="red">此项必填！</font>',
            },
            legalLevel: {
                required: '<font color="red">此项必填！</font>',
            },
            basicNo: {
                required: '<font color="red">此项必填！</font>',
            },
            issueOrg: {
                required: '<font color="red">此项必填！</font>',
            },
            exeDate: {
                required: '<font color="red">此项必填！</font>',
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据保存中,请勿点击,耐心等候...");
            $('#saveLegalBtn').hide();

            var formData = new FormData(document.getElementById("add_legal_form"));
            $.ajax({
                url: ctx + '/aea/service/legal/saveAeaServiceLegal.do',
                type: 'POST',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success && result.content) {

                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            swal({
                                type: 'success',
                                title: '操作成功！',
                                showConfirmButton: false,
                                timer: 1500
                            });
                            $('#add_legal_modal').modal('hide');
                            $('#saveLegalBtn').show();
                            add_legal_validator.resetForm();
                            initLegalTree();

                        },500);
                    } else {

                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            $('#saveLegalBtn').show();
                            swal('错误信息', result.message, 'error');
                        });
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $("#uploadProgress").modal('hide');
                        $('#saveLegalBtn').show();
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    });
                }
            });
        }
    });

    //提交条款
    add_legalClause_validator = $('#add_legalClause_form').validate({
        // 定义校验规则
        rules: {
            clauseTitle: {
                required: true,
            },
            clauseContent: {
                required: true,
            },
            sortNo: {
                required: true,
            }

        },
        messages: {
            clauseTitle: {
                required: '<font color="red">此项必填！</font>',
            },
            clauseContent: {
                required: '<font color="red">此项必填！</font>',
            },
            sortNo: {
                required: '<font color="red">此项必填！</font>',
            }
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据保存中,请勿点击,耐心等候...");
            $('#saveLegalClauseBtn').hide();

            var formData = new FormData(document.getElementById("add_legalClause_form"));
            $.ajax({
                url: ctx + '/aea/service/legal/clause/saveAeaServiceLegalClause.do',
                type: 'POST',
                dataType: 'json',
                data: formData,
                contentType: false,
                processData: false,
                success: function (result) {
                    if (result.success && result.content) {

                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            $('#saveLegalClauseBtn').show();
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1500
                            });
                            $('#add_legalClause_modal').modal('hide');
                            add_legalClause_validator.resetForm();
                            initLegalTree();

                        },500);

                    } else {

                        setTimeout(function(){

                            $("#uploadProgress").modal('hide');
                            $('#saveLegalClauseBtn').show();
                            swal('错误信息', result.message, 'error');

                        });
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $("#uploadProgress").modal('hide');
                        $('#saveLegalClauseBtn').show();
                        swal('错误信息', XMLHttpRequest.responseText, 'error');

                    });
                }
            });
        }
    });
}

//全部展开
function expandAll() {

    hideRMenu('clauseContextMenu');
    hideRMenu('legalContextMenu');
    legalTree.expandAll(true);
}

//全部折叠
function collapseAll() {

    hideRMenu('clauseContextMenu');
    hideRMenu('legalContextMenu');
    legalTree.expandAll(false);
}

// 搜索关键字申请节点
function searchLegal() {

    // 取得输入的关键字的值
    var value = $.trim($('#keyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一样，就退出不查了。
    if (lastValue === value) {
        return;
    }

    // 保存最后一次
    lastValue = value;

    var nodes = legalTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showAllNode(nodes);
        return;
    }
    hideAllNode(nodes);
    nodeList = legalTree.getNodesByParamFuzzy(keyType, value);
    updateNodes(nodeList);
}

//隐藏所有节点
function hideAllNode(nodes) {

    nodes = legalTree.transformToArray(nodes);
    for (var i = nodes.length - 1; i >= 0; i--) {
        legalTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showAllNode(nodes) {

    nodes = legalTree.transformToArray(nodes);
    for (var i = nodes.length - 1; i >= 0; i--) {
        if (nodes[i].getParentNode() != null) {
            legalTree.expandNode(nodes[i], false, false, false, false);
        } else {
            legalTree.expandNode(nodes[i], true, true, false, false);
        }
        legalTree.showNode(nodes[i]);
        showAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateNodes(nodeList) {

    if (nodeList != null && nodeList.length > 0) {
        legalTree.showNodes(nodeList);
        for (var i = 0, l = nodeList.length; i < l; i++) {
            //展开当前节点的父节点
            legalTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while (nodeList[i].getParentNode() != null) {
                legalTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                legalTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            legalTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            legalTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 清空查询法律法规
function clearSearchLegal() {

    // 清空查询内容
    $('#keyWord').val('');
    showAllNode(legalTree.getNodes());
}

// == 附件部分 ==
var commonQueryParams = [];
var showAttType = null;
var showAttBizId = null;
function showAtt(type) {

    $('#show_mat_att_modal').modal('show');
    searchAttSto(type);
    showAttType = type;
    if (type == 'serviceLegalAtt') {
        showAttBizId = $('#add_legal_form input[name="legalId"]').val();
    } else if (type == 'clauseAtt') {
        showAttBizId = $('#add_legalClause_form input[name="legalClauseId"]').val();
    }
}

function searchAttSto(type) {

    var tbName = "";
    var pkName = "";
    showAttType = type;
    if(type=='serviceLegalAtt'){

        tbName = "AEA_SERVICE_LEGAL";
        pkName = "SERVICE_LEGAL_ATT";
        showAttBizId = $('#add_legal_form input[name="legalId"]').val();

    }else if(type=='clauseAtt'){

        tbName = "AEA_SERVICE_LEGAL_CLAUSE";
        pkName = "CLAUSE_ATT";
        showAttBizId = $('#add_legalClause_form input[name="legalClauseId"]').val();
    }
    commonQueryParams = [];
    commonQueryParams.push({name: "tableName", value: tbName});
    commonQueryParams.push({name: "pkName", value: pkName});
    commonQueryParams.push({name: "recordId", value: showAttBizId});
    $("#show_mat_att_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    $("#show_mat_att_tb").bootstrapTable('refresh');       //无参数刷新
}

function matAttTbParam(params){

    var pageNum = (params.offset / params.limit) + 1;
    var queryParam = {
        pageNum: pageNum,
        pageSize: params.limit,
        sort: params.sort,
        order: params.order,
    };
    //组装查询参数
    var buildParam = {};
    if (commonQueryParams) {
        for (var i = 0; i < commonQueryParams.length; i++) {
            buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;
        }
        queryParam = $.extend(queryParam, buildParam);
    }
    return queryParam;
}

function matAttTbResponseData(res) {
    return res;
}

function clearAllFile(){

    $("#serviceLegalAttFile").siblings('.custorm-style').find(".right-text").html("");
    $("#clauseAttFile").siblings('.custorm-style').find(".right-text").html("");
    $('#serviceLegalAttDiv').hide();
    $('#clauseAttDiv').hide();
}

function uploadFileChange(obj){

    $(obj).siblings('.custorm-style').find(".right-text").html("");
    var files = $(obj)[0].files;
    if(files!=null&&files.length>0){
        var names = [];
        for(var i=0;i<files.length;i++){
            names.push(files[i].name);
        }
        $(obj).siblings('.custorm-style').find(".right-text").html(names.toString());
    }
}

function viewAttNameFormatter(value, row, index){

    // 图片
    if(row.attFormat=="jpg"||row.attFormat=="png"||row.attFormat=="jpeg"||row.attFormat=="jpe"||row.attFormat=="gif"){

        var url = "";
        if(row.attType=='KP'){
            url = ctx + '/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
        }else{
            url = ctx + '/aea/item/mat/downloadGlobalMatDoc.do?detailId='+ row.detailId;
        }
        return '<a href="#" onclick="showImgFile(\''+ row.detailId +'\')">'+ row.attName +'</a>';

    }else{ // 文件

        var url = "";
        if(row.attType=='KP'){

            url = ctx +'/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
        }else{

            url = ctx + '/aea/item/mat/downloadGlobalMatDoc.do?detailId='+ row.detailId;
        }
        return '<a href="#" onclick="showDownloadFile(\''+ url +'\')">'+ row.attName +'</a>';
    }
}

function viewAttSizeFormatter(value, row, index){

    if(value){
        return value/1000;
    }
}

function showDownloadFile(url){

    window.open(url,"下载文件");
}

function showImgFile(detailId){

    window.open(ctx + '/aea/item/mat/showFile.do?detailId='+ detailId,"展示图片");
}

function attOperFormatter(value, row, index){

    if(row.attType!='KP'){
        var deleteBtn = '<a href="javascript:deleteAttrByDetailId(\'' + row.detailId + '\')" ' +
                            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                            'title="删除"><i class="la la-trash"></i>' +
                        '</a>';
        return deleteBtn;
    }
}

function deleteAttrByDetailId(id){

    if(id){
        var msg = '此操作将删除附件,确定要删除吗？';
        swal({
            title: '',
            text: msg,
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    type: 'post',
                    url: ctx + '/aea/service/legal/deleteAtt.do',
                    dataType: 'json',
                    data: {
                        'type': showAttType,
                        'bizId': showAttBizId,
                        'detailId': id,
                    },
                    success: function (result) {
                        if (result.success) {
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            searchAttSto(showAttType);
                            if(showAttType=='serviceLegalAtt'){
                                $.ajax({
                                    url: ctx + '/aea/service/legal/getAeaServiceLegal.do',
                                    type: 'post',
                                    data: {'id': showAttBizId},
                                    async: false,
                                    success: function (data) {

                                        // 记载表单数据
                                        loadFormData(true, '#add_legal_form', data);

                                        if (data!=null&&data.serviceLegalAttCount&&data.serviceLegalAttCount!=0) {

                                            $('#serviceLegalAttDiv').show();
                                            $('#serviceLegalAttButton').html(data.serviceLegalAttCount + "个附件");
                                        }else{
                                            $('#serviceLegalAttDiv').hide();
                                        }

                                        if(data!=null&&data.exeDate) {
                                            $("#add_legal_form input[name='exeDate']").val(new Date(data.exeDate).format("yyyy-MM-dd"));
                                        }else{
                                            $("#add_legal_form input[name='exeDate']").val(new Date().format("yyyy-MM-dd"));
                                        }
                                    },
                                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                                    }
                                });
                            }else if(showAttType=='clauseAtt'){
                                $.ajax({
                                    url: ctx + '/aea/service/legal/clause/getAeaServiceLegalClause.do',
                                    type: 'post',
                                    data: {'id': showAttBizId},
                                    async: false,
                                    success: function (data) {

                                        if (data!=null&&data.clauseAttCount&&data.clauseAttCount!=0) {

                                            $('#clauseAttDiv').show();
                                            $('#clauseAttButton').html(data.clauseAttCount + "个附件");
                                        }else{
                                            $('#clauseAttDiv').hide();
                                        }

                                        // 记载表单数据
                                        loadFormData(true, '#add_legalClause_form', data);
                                    },
                                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                                    }
                                });
                            }
                        } else {
                            swal('错误信息','删除失败','error');
                        }
                    }
                });
            }
        });
    }else{
        swal('提示信息', '请选择操作记录！', 'info');
    }
}

