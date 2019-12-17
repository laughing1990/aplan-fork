var stdTypeMatTree = null;
var stdTypeMatTreeSelectNode = null;
var add_std_mat_type_validator = null;
var add_std_mat_validator = null;
var nodeList = [];
var lastValue = '';
var stdTypeMatTreeKey = null;
var stdTypeMatTreeSettings = {
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

        onRightClick: stdTypeMatTreeOnRightClick
    }
};

$(function () {

    // 初始化高度
    $('#mainContentPanel').css('height', $(document.body).height() - 10);

    // 调整树的高度
    $('#stdTypeMatTree').css({'max-height': $('#mainContentPanel').height() - 116, 'height': $('#mainContentPanel').height() - 116});

    // 设置滚动条样式
    $("#stdTypeMatTree").niceScroll({

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
    initStdTypeMatTree();

    // 初始化表单校验
    initValidate();

    // 关键字搜索文件库节点
    stdTypeMatTreeKey = $("#keyWord");
    stdTypeMatTreeKey.bind("focus", focusStdTypeMatTreeKey).bind("blur", blurStdTypeMatTreeKey()).bind("change cut input propertychange",searchTypeMat());
    stdTypeMatTreeKey.bind('keydown', function (e){
        if(e.which == 13){
            searchTypeMat();
        }
    });

    //关闭新增分类窗口
    $('#closeMatTypeBtn').click(function(){

        $('#aedit_mat_type_modal').modal('hide');
        add_std_mat_type_validator.resetForm();
    });

    //关闭新增标准材料窗口
    $('#closeMatBtn').click(function(){

        $('#aedit_mat_modal').modal('hide');
        add_std_mat_validator.resetForm();
    });
});

function focusStdTypeMatTreeKey(e) {

    if (stdTypeMatTreeKey.hasClass("empty")) {
        stdTypeMatTreeKey.removeClass("empty");
    }
}

function blurStdTypeMatTreeKey(e) {

    if (stdTypeMatTreeKey.get(0).value === "") {
        stdTypeMatTreeKey.addClass("empty");
    }
    searchTypeMat();
}

//加载法律法规树
function initStdTypeMatTree() {

    $.ajax({
        url: ctx+'/aea/stdmat/type/gtrStdTypeMatZtree.do',
        type: 'post',
        data: {},
        async: false,
        dataType: 'json',
        success: function (data) {
            if (data != null && data.length > 0) {
                stdTypeMatTree = $.fn.zTree.init($("#stdTypeMatTree"), stdTypeMatTreeSettings, data);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            swal('错误信息', '初始标准材料库异常,错误信息:'+XMLHttpRequest.responseText, 'error');
        }
    });
}

function initValidate(){

    //提交法律法规
    add_std_mat_type_validator = $('#aedit_std_mat_type_form').validate({
        // 定义校验规则
        rules: {
            typeName: {
                required: true,
                maxlength: 100
            },
            typeCode:{
                required: true,
                maxlength: 50,
                remote: {
                    url: ctx+'/aea/stdmat/type/checkUniqueTypeCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        stdmatTypeId: function(){
                            return $("#aedit_std_mat_type_form input[name='stdmatTypeId']").val();
                        },
                        typeCode: function() {
                            return $("#aedit_std_mat_type_form input[name='typeCode']").val();
                        }
                    }
                }
            },
            sortNo: {
                required: true,
            },
            typeMemo: {
                maxlength: 500
            },
        },
        messages: {
            typeName: {
                required: '此项必填!',
                maxlength: "长度不能超过100个字母!"
            },
            typeCode:{
                required: '此项必填!',
                maxlength: "长度不能超过50个字母!",
                remote: "编号已存在！",
            },
            sortNo: {
                required: '此项必填!',
            },
            typeMemo: {
                maxlength: "长度不能超过500个字母!"
            },
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据保存中,请勿点击,耐心等候...");
            $('#saveMatTypeBtn').hide();

            var matTypeId = $("#aedit_std_mat_type_form input[name='stdmatTypeId']").val();
            var parentTypeId = $("#aedit_std_mat_type_form input[name='parentTypeId']").val();
            var d = {};
            var t = $('#aedit_std_mat_type_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx+'/aea/stdmat/type/saveAeaStdmatType.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){

                        setTimeout(function() {

                            $('#saveMatTypeBtn').show();
                            $("#uploadProgress").modal('hide');

                            var matTypeContent = result.content;
                            matTypeContent.parentTypeId = parentTypeId;
                            var newNode = createMatTypeNodeData(matTypeContent);

                            // 更新
                            if(matTypeId == matTypeContent.stdmatTypeId){
                                stdTypeMatTreeSelectNode.name = newNode.name;
                                stdTypeMatTree.updateNode(stdTypeMatTreeSelectNode);
                                swal({
                                    title: '提示信息',
                                    text: '操作成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                $('#aedit_std_mat_type_modal').modal('hide');
                                //将新增的节点设置为选中
                                var node = stdTypeMatTree.getNodeByParam("id", matTypeContent.stdmatTypeId, null);
                                //将节点设置为选中状态
                                if(node){
                                    stdTypeMatTree.selectNode(node);
                                    stdTypeMatTreeSelectNode = node;
                                }
                            }else{ // 新增
                                stdTypeMatTree.addNodes(stdTypeMatTreeSelectNode, -1, newNode, true);
                                swal({
                                    text: '操作成功!',
                                    type: 'success',
                                    showCancelButton: true,
                                    confirmButtonText: '继续新增',
                                    cancelButtonText: '取消'
                                }).then(function(result) {
                                    if(result.value) { // 继续新增分类
                                        addMatType();
                                    }else{
                                        $('#ae_mat_type_modal').modal('hide');
                                        var node = stdTypeMatTree.getNodeByParam("id", matTypeContent.stdmatTypeId, null);
                                        if(node){
                                            stdTypeMatTree.selectNode(node);
                                            stdTypeMatTreeSelectNode = node;
                                        }
                                    }
                                });
                            }
                        },500);
                    }else {

                        setTimeout(function(){

                            $('#saveMatTypeBtn').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');

                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $('#saveMatTypeBtn').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });

    //提交条款
    add_std_mat_validator = $('#aedit_std_mat_form').validate({
        // 定义校验规则
        rules: {
            stdmatName: {
                required: true,
                maxlength: 100
            },
            stdmatCode:{
                required: true,
                maxlength: 50,
                remote: {
                    url: ctx+'/aea/stdmat/checkUniqueCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        stdmatId: function(){
                            return $("#aedit_std_mat_form input[name='stdmatId']").val();
                        },
                        stdmatCode: function() {
                            return $("#aedit_std_mat_form input[name='stdmatCode']").val();
                        }
                    }
                }
            },
            isAutoCheck:{
                required: true,
            },
            checkKeywords:{
                maxlength: 100
            },
            sortNo: {
                required: true,
            },
            stdmatMemo: {
                maxlength: 500
            },
        },
        messages: {
            stdmatName: {
                required: '此项必填!',
                maxlength: "长度不能超过100个字母!"
            },
            stdmatCode:{
                required: '此项必填!',
                maxlength: "长度不能超过50个字母!",
                remote: "编号已存在！",
            },
            isAutoCheck:{
                required: '此项必填!',
            },
            checkKeywords:{
                maxlength: "长度不能超过100个字母!"
            },
            sortNo: {
                required: '此项必填!',
            },
            stdmatMemo: {
                maxlength: "长度不能超过500个字母!"
            },
        },
        // 提交表单
        submitHandler: function (form) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据保存中,请勿点击,耐心等候...");
            $('#saveMatBtn').hide();

            var matId = $("#aedit_std_mat_form input[name='stdmatId']").val();
            var parentTypeId = $("#aedit_std_mat_form input[name='stdmatTypeId']").val();
            var d = {};
            var t = $('#aedit_std_mat_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx+'/aea/stdmat/saveAeaStdmat.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){

                        setTimeout(function() {

                            $('#saveMatBtn').show();
                            $("#uploadProgress").modal('hide');

                            var matContent = result.content;
                            matContent.parentTypeId = parentTypeId;
                            var newNode = createMatNodeData(matContent);

                            // 更新
                            if(matId == matContent.stdmatId){
                                stdTypeMatTreeSelectNode.name = newNode.name;
                                stdTypeMatTree.updateNode(stdTypeMatTreeSelectNode);
                                swal({
                                    title: '提示信息',
                                    text: '操作成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                $('#aedit_std_mat_modal').modal('hide');
                                //将新增的节点设置为选中
                                var node = stdTypeMatTree.getNodeByParam("id", matContent.stdmatId, null);
                                //将节点设置为选中状态
                                if(node){
                                    stdTypeMatTree.selectNode(node);
                                    stdTypeMatTreeSelectNode = node;
                                }
                            }else{ // 新增
                                stdTypeMatTree.addNodes(stdTypeMatTreeSelectNode, -1, newNode, true);
                                swal({
                                    text: '操作成功!',
                                    type: 'success',
                                    showCancelButton: true,
                                    confirmButtonText: '继续新增',
                                    cancelButtonText: '取消'
                                }).then(function(result) {
                                    if(result.value) { // 继续新增分类
                                        addStdMat();
                                    }else{
                                        $('#aedit_std_mat_modal').modal('hide');
                                        var node = stdTypeMatTree.getNodeByParam("id", matContent.stdmatId, null);
                                        if(node){
                                            stdTypeMatTree.selectNode(node);
                                            stdTypeMatTreeSelectNode = node;
                                        }
                                    }
                                });
                            }
                        },500);
                    }else {

                        setTimeout(function(){

                            $('#saveMatBtn').show();
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', result.message, 'error');
                        },500);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function(){

                        $('#saveMatBtn').show();
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    },500);
                }
            });
        }
    });
}

function createMatTypeNodeData(matTypeData){

    if(matTypeData){

        var id = matTypeData.stdmatTypeId;
        var name = matTypeData.typeName;
        var pId =  matTypeData.parentTypeId;
        var type = 'stdMatType';
        var open = true;
        var isParent = true;
        var nocheck = true;
        var node = {
            'id': id,
            'name': name,
            'pId': pId,
            'open': open,
            'isParent': isParent,
            'type': type,
            'nocheck': nocheck
        };
        return node;
    }
    return null;
}

function createMatNodeData(matData){

    if(matData){

        var id = matData.stdmatId;
        var name = matData.stdmatName;
        var pId =  matData.stdmatTypeId;
        var type = 'stdMat';
        var open = true;
        var isParent = false;
        var nocheck = false;
        var node = {
            'id': id,
            'name': name,
            'pId': pId,
            'open': open,
            'isParent': isParent,
            'type': type,
            'nocheck': nocheck
        };
        return node;
    }
    return null;

}

function stdTypeMatTreeOnRightClick(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();

    stdTypeMatTree.selectNode(treeNode);
    stdTypeMatTreeSelectNode = treeNode;
    if(event.target.tagName.toLowerCase()=='span'||event.target.tagName.toLowerCase()=='a'||event.target.tagName.toLowerCase()=='ul'){

        var y = event.clientY+10;
        var maxHeight = $('#stdTypeMatTree').height();
        if(event.clientY>maxHeight){
            y -= ($('.contextMenuDiv').height()+5);
        }
        if(treeNode.type=='root'){

            showRMenu('rootNodeContextMenu', event.clientX+15, y);
        }
        if(treeNode.type=='stdMatType'){

            showRMenu('matTypeNodeContextMenu', event.clientX+15, y);
        }
        if(treeNode.type=='stdMat'){

            showRMenu('matNodeContextMenu', event.clientX+15, y);
        }
    }
}

//全部展开
function expandAll() {

    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');
    stdTypeMatTree.expandAll(true);
}

//全部折叠
function collapseAll() {

    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');
    stdTypeMatTree.expandAll(false);
}

// 搜索关键字申请节点
function searchTypeMat() {

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

    var nodes = stdTypeMatTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showAllNode(nodes);
        return;
    }
    hideAllNode(nodes);
    nodeList = stdTypeMatTree.getNodesByParamFuzzy(keyType, value);
    updateNodes(nodeList);
}

//隐藏所有节点
function hideAllNode(nodes) {

    nodes = stdTypeMatTree.transformToArray(nodes);
    for (var i = nodes.length - 1; i >= 0; i--) {
        stdTypeMatTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showAllNode(nodes) {

    nodes = stdTypeMatTree.transformToArray(nodes);
    for (var i = nodes.length - 1; i >= 0; i--) {
        if (nodes[i].getParentNode() != null) {
            stdTypeMatTree.expandNode(nodes[i], false, false, false, false);
        } else {
            stdTypeMatTree.expandNode(nodes[i], true, true, false, false);
        }
        stdTypeMatTree.showNode(nodes[i]);
        showAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateNodes(nodeList) {

    if (nodeList != null && nodeList.length > 0) {
        stdTypeMatTree.showNodes(nodeList);
        for (var i = 0, l = nodeList.length; i < l; i++) {
            //展开当前节点的父节点
            stdTypeMatTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while (nodeList[i].getParentNode() != null) {
                stdTypeMatTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                stdTypeMatTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            stdTypeMatTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            stdTypeMatTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 清空查询
function clearSearchTypeMat() {

    // 清空查询内容
    $('#keyWord').val('');
    var nodes = stdTypeMatTree.getNodes();
    showAllNode(nodes);

    setTimeout(function() {
        expandAll();
    }, 300);
}

// 新增类型
function addMatType(){

    // 隐藏右键菜单
    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');

    // 表单初始化
    $('#aedit_std_mat_type_modal').modal('show');
    $('#aedit_std_mat_type_modal_title').html('新增分类');
    $('#aedit_std_mat_type_form')[0].reset();
    add_std_mat_type_validator.resetForm();
    $("#aedit_std_mat_type_form input[name='stdmatTypeId']").val('');

    if (stdTypeMatTreeSelectNode) {

        $("#aedit_std_mat_type_form input[name='parentTypeId']").val(stdTypeMatTreeSelectNode.id);
        // 编号赋值
        $.ajax({
            url: ctx+'/aea/stdmat/type/getMaxSortNo.do',
            type: 'post',
            data: {},
            success: function (data) {
                $("#aedit_std_mat_type_form input[name='sortNo']").val(data);
            }
        });
    } else {
        swal('提示信息', "请选择父级节点！", 'info');
    }
}

// 编辑类型
function editMatType(){

    // 隐藏右键菜单
    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');

    // 表单初始化
    $('#aedit_std_mat_type_modal_title').html('编辑分类');
    $('#aedit_std_mat_type_form')[0].reset();
    add_std_mat_type_validator.resetForm();
    $("#aedit_std_mat_type_form input[name='stdmatTypeId']").val('');

    // 赋值表单数据
    if (stdTypeMatTreeSelectNode) {
        var selectNode = stdTypeMatTreeSelectNode;
        // 控制页面展示
        $('#aedit_std_mat_type_modal').modal('show');

        $.ajax({
            url: ctx+'/aea/stdmat/type/getAeaStdmatType.do',
            type: 'post',
            data: {'id': selectNode.id},
            async: false,
            success: function (data) {

                // 记载表单数据
                loadFormData(true, '#aedit_std_mat_type_form', data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    } else {
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 删除类型
function deleteMatType(){

    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');

    if (stdTypeMatTreeSelectNode) {

        var selectNode = stdTypeMatTreeSelectNode;
        swal({
            title: '此操作影响：',
            text: '此操作将删除当前节点以及下级所有子节点数据,您确定删除吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/stdmat/type/deleteSelfAndAllChildById.do',
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
                            var parentNode = stdTypeMatTreeSelectNode.getParentNode();
                            if (parentNode) {
                                stdTypeMatTree.selectNode(parentNode);
                                stdTypeMatTreeSelectNode = parentNode;
                            } else {
                                stdTypeMatTreeSelectNode = null;
                            }
                            // 移除节点
                            var node = stdTypeMatTree.getNodeByParam("id", selectNode.id, null);
                            stdTypeMatTree.removeNode(node);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', "请选择需要删除节点！", 'info');
    }
}

// 新增标准材料
function addStdMat(){

    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');

    // 表单初始化
    $('#aedit_std_mat_modal').modal('show');
    $('#aedit_std_mat_modal_title').html('新增标准材料');
    $('#aedit_std_mat_form')[0].reset();
    add_std_mat_validator.resetForm();
    $("#aedit_std_mat_form input[name='stdmatId']").val('');

    if (stdTypeMatTreeSelectNode) {

        $("#aedit_std_mat_form input[name='stdmatTypeId']").val(stdTypeMatTreeSelectNode.id);
        // 编号赋值
        $.ajax({
            url: ctx+'/aea/stdmat/getMaxSortNo.do',
            type: 'post',
            data: {},
            success: function (data) {
                $("#aedit_std_mat_form input[name='sortNo']").val(data);
            }
        });
    } else {
        swal('提示信息', "请选择父级节点！", 'info');
    }
}

// 编辑标准材料
function editStdMat(){

    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');

    // 表单初始化
    $('#aedit_std_mat_modal').modal('show');
    $('#aedit_std_mat_modal_title').html('编辑标准材料');
    $('#aedit_std_mat_form')[0].reset();
    add_std_mat_validator.resetForm();
    $("#aedit_std_mat_form input[name='stdmatId']").val('');

    // 赋值表单数据
    if (stdTypeMatTreeSelectNode) {

        var selectNode = stdTypeMatTreeSelectNode;
        // 控制页面展示
        $('#aedit_std_mat_modal').modal('show');

        $.ajax({
            url: ctx+'/aea/stdmat/getAeaStdmat.do',
            type: 'post',
            data: {'id': selectNode.id},
            async: false,
            success: function (data) {

                // 记载表单数据
                loadFormData(true, '#aedit_std_mat_form', data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    } else {
        swal('提示信息', "请选择节点！", 'info');
    }

}

// 删除标准材料
function deleteStdMat(){

    hideRMenu('rootNodeContextMenu');
    hideRMenu('matTypeNodeContextMenu');
    hideRMenu('matNodeContextMenu');

    if (stdTypeMatTreeSelectNode) {

        var selectNode = stdTypeMatTreeSelectNode;
        swal({
            title: '此操作影响：',
            text: '此操作将删除当前节点数据,您确定删除吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/stdmat/deleteAeaStdmatById.do',
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
                            var parentNode = stdTypeMatTreeSelectNode.getParentNode();
                            if (parentNode) {
                                stdTypeMatTree.selectNode(parentNode);
                                stdTypeMatTreeSelectNode = parentNode;
                            } else {
                                stdTypeMatTreeSelectNode = null;
                            }
                            // 移除节点
                            var node = stdTypeMatTree.getNodeByParam("id", selectNode.id, null);
                            stdTypeMatTree.removeNode(node);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }
                });
            }
        });
    } else {
        swal('提示信息', "请选择需要删除节点！", 'info');
    }
}
