var matTypeTreeKey,
    matTypeTreeNodeList = [],
    matTypeTreeLastValue = "",
    matTypeTree = null,
    matTypeTreeSelectNode = null,  //树选中节点
    aeMatTypeValidator = null,
    commonQueryParams = {},
    matTypeTreeSetting = {
        edit: {
            showRemoveBtn: false,//设置是否显示删除按钮
            showRenameBtn: false//设置是否显示编辑名称按钮
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId",
                rootPId: 0
            }
        },
        view: {
            selectedMulti: false,//设置是否允许同时选中多个节点
            showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: true, //设置 zTree 是否显示节点之间的连线。
            showHorizontal: false//设置是否水平平铺树（自定义属性）

        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onClick: onClickMatTypeTreeNode,
            onRightClick:onRightClickMatTypeTreeNode, //右击事件
        }
    };

// 初始化加载函数
$(function(){

    // 初始化高度
    var H = $(document).height()-10 || $(body).height()-10;
    $('#mainContentPanel').css('height',H);
    $('#matTypeTree').css({'max-height': $('#westPanel').height()-116,'height': $('#westPanel').height()-116});
    $('#mat_type_list_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-116
    });

    $("#matTypeTree").niceScroll({

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


    // 初始化树
    loadMatTypeTreeData();
    // 默认选中第一个节点数据
    expandFirstNode();

    // 关键字搜索文件库节点
    matTypeTreeKey = $("#matTypeTreeKeyWord");
    matTypeTreeKey.bind("focus", focusMatTypeTreeKey)
        .bind("blur", blurMatTypeTreeKey)
        .bind("change cut input propertychange",searchMatTypeTreeNode);
    matTypeTreeKey.bind('keydown', function (e){
        if(e.which == 13){
            searchMatTypeTreeNode();
        }
    });

    aeMatTypeValidator = $('#ae_mat_type_form').validate({
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
                    url: ctx+'/aea/item/mat/type/checkUniqueTypeCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        matTypeId: function(){
                            return $("#ae_mat_type_form input[name='matTypeId']").val();
                        },
                        typeCode: function() {
                            return $("#ae_mat_type_form input[name='typeCode']").val();
                        }
                    }
                }
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
            typeMemo: {
                maxlength: "长度不能超过500个字母!"
            },
        },
        // 提交表单
        submitHandler: function(form){

            var matTypeId = $("#ae_mat_type_form input[name='matTypeId']").val();
            var parentTypeId = $("#ae_mat_type_form input[name='parentTypeId']").val();
            var isTbEditNode = $("#ae_mat_type_form input[name='isTbEditNode']").val();

            var d = {};
            var t = $('#ae_mat_type_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            if(parentTypeId=='root'){
                d['parentTypeId'] = null;
            }
            $.ajax({
                url: ctx + '/aea/item/mat/type/saveAeaItemMatType.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){

                        var matTypeContent = result.content;
                        matTypeContent.parentTypeId = parentTypeId;
                        var newNode = createNodeData(matTypeContent);

                        if(matTypeId == matTypeContent.matTypeId){ // 更新
                            matTypeTreeSelectNode.name = newNode.name;
                            matTypeTree.updateNode(matTypeTreeSelectNode);
                            swal({
                                title: '提示信息',
                                text: '操作成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            $('#ae_mat_type_modal').modal('hide');
                            if(isTbEditNode=='1'){ // 表格编辑
                                //将节点设置为选中状态
                                var node = matTypeTree.getNodeByParam("id",parentTypeId, null);
                                if(node) {
                                    matTypeTree.selectNode(node);
                                    matTypeTreeSelectNode = node;
                                }
                            }else{
                                //将新增的节点设置为选中
                                var node = matTypeTree.getNodeByParam("id",matTypeContent.matTypeId, null);
                                //将节点设置为选中状态
                                if(node){
                                    matTypeTree.selectNode(node);
                                    matTypeTreeSelectNode = node;
                                }
                            }
                            searchMatType();
                        }else{ // 新增
                            matTypeTree.addNodes(matTypeTreeSelectNode,-1,newNode,true);
                            swal({
                                text: '操作成功!',
                                type: 'success',
                                showCancelButton: true,
                                confirmButtonText: '继续新增分类',
                                confirmButtonClass: "btn btn-success",
                                cancelButtonText: '进入分类设置',
                                cancelButtonClass: "btn btn-accent",
                            }).then(function(result) {
                                if(result.value) { // 继续新增分类
                                    addMatType();
                                }else{ // 进入其他相关设置
                                    $('#ae_mat_type_modal').modal('hide');
                                    if(isTbEditNode=='1'){ // 表格编辑
                                        //将节点设置为选中状态
                                        var node = matTypeTree.getNodeByParam("id",parentTypeId, null);
                                        if(node) {
                                            matTypeTree.selectNode(node);
                                            matTypeTreeSelectNode = node;
                                        }
                                    }else{
                                        //将新增的节点设置为选中
                                        var node = matTypeTree.getNodeByParam("id",matTypeContent.matTypeId, null);
                                        //将节点设置为选中状态
                                        if(node){
                                            matTypeTree.selectNode(node);
                                            matTypeTreeSelectNode = node;
                                        }
                                    }
                                    searchMatType();
                                }
                            });
                        }
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', "保存类型信息失败！", 'error');
                }
            });
        }
    });
});

function focusMatTypeTreeKey(e) {

    if (matTypeTreeKey.hasClass("empty")) {
        matTypeTreeKey.removeClass("empty");
    }
}

function blurMatTypeTreeKey(e) {

    if (matTypeTreeKey.get(0).value === "") {
        matTypeTreeKey.addClass("empty");
    }
    searchMatTypeTreeNode();
}

// 点击事件
function onClickMatTypeTreeNode(event, treeId, treeNode) {
    
    if(treeNode){
        matTypeTree.selectNode(treeNode);
        matTypeTreeSelectNode = treeNode;
        searchMatType();
        expandNextNodes();
    }
}

//展开下一级子节点
function expandNextNodes(){

    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    var selectNodes = matTypeTree.getSelectedNodes();
    matTypeTree.expandNode(selectNodes[0], true, null, null);
}

// 右键事件
function onRightClickMatTypeTreeNode(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();
    matTypeTree.selectNode(treeNode);
    matTypeTreeSelectNode = treeNode;
    searchMatType();
    if(event.target.tagName.toLowerCase()=='span'||
        event.target.tagName.toLowerCase()=='a'||
        event.target.tagName.toLowerCase()=='ul'){

        var y = event.clientY+10;
        var maxHeight = $('#matTypeTree').height();
        if(event.clientY>maxHeight){
            y -= ($('.contextMenuDiv').height()+5);
        }
        if(treeNode.type=='root'){
            showRMenu('rootMatTypeContextMenu',event.clientX+15, y);
        }else if(treeNode.type=='matType'){
            showRMenu('matTypeContextMenu',event.clientX+15, y);
        }
    }
}

// 默认选中第一个节点
function expandFirstNode(){

    if(matTypeTree){
        var nodes = matTypeTree.getNodes();
        if(nodes!=null&&nodes.length>0){
            matTypeTree.selectNode(nodes[0]); //选中第一个节点
            matTypeTreeSelectNode = nodes[0];
            searchMatType();
        }
    }
}

//全部展开
function expandMatTypeTreeAllNode(){

    matTypeTree.expandAll(true);
}

//全部折叠
function collapseMatTypeTreeAllNode(){

    matTypeTree.expandAll(false);
}

// 搜索节点
function searchMatTypeTreeNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#matTypeTreeKeyWord').val());
    // 按名字查询
    var keyType = "name";
    // 如果和上次一次，就退出不查了。
    if (matTypeTreeLastValue === value) {
        return;
    }
    // 保存最后一次
    matTypeTreeLastValue = value;
    var nodes = matTypeTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showMatTypeTreeAllNode(nodes);
        return;
    }
    hideMatTypeTreeAllNode(nodes);
    matTypeTreeNodeList = matTypeTree.getNodesByParamFuzzy(keyType, value);
    updateMatTypeTreeNodes(matTypeTreeNodeList);
}

// 清空查询
function clearSearchMatTypeTreeNode(){

    // 清空查询内容
    $('#matTypeTreeKeyWord').val('');
    showMatTypeTreeAllNode(matTypeTree.getNodes());
}

//隐藏所有节点
function hideMatTypeTreeAllNode(nodes){

    nodes = matTypeTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        matTypeTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showMatTypeTreeAllNode(nodes){

    nodes = matTypeTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            matTypeTree.expandNode(nodes[i],false,false,false,false);
        }else{
            matTypeTree.expandNode(nodes[i],true,true,false,false);
        }
        matTypeTree.showNode(nodes[i]);
        showMatTypeTreeAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateMatTypeTreeNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        matTypeTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            matTypeTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                matTypeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                matTypeTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            matTypeTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            matTypeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 加载电子证照库数据
function loadMatTypeTreeData(){

    $.ajax({
        url: ctx+'/aea/item/mat/type/gtreeMatType.do',
        type:'post',
        data:{},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(matTypeTree)matTypeTree.destroy();
                matTypeTree = $.fn.zTree.init($("#matTypeTree"), matTypeTreeSetting , data);
            }
        },
        error: function(){
            swal('错误信息', '初始化材料类型库异常!', 'error');
        }
    });
}

function matTypeFormatter(value, row, index) {

    var editBtn = '<a href="javascript:editMatTypeById(\'' + row.matTypeId + '\')" ' +
                      'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" ' +
                      'title="编辑"><i class="la la-edit"></i>' +
                  '</a>';

    var deleteBtn = '<a href="javascript:deleteMatTypeById(\''+row.matTypeId + '\')" ' +
                        'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                        'title="删除"><i class="la la-trash"></i>' +
                    '</a>';

    return editBtn + deleteBtn;
}

// 新增子分类
function addMatType(type){

    hideRMenu('rootMatTypeContextMenu');
    hideRMenu('matTypeContextMenu');
    $('#ae_mat_type_modal').modal('show');
    $('#ae_mat_type_modal_title').html('新增分类');
    $('#ae_mat_type_form')[0].reset();
    aeMatTypeValidator.resetForm();

    $("#ae_mat_type_form input[name='matTypeId']").val('');
    $("#ae_mat_type_form input[name='parentTypeId']").val('');
    $("#ae_mat_type_form input[name='isTbEditNode']").val('')

    if(matTypeTreeSelectNode){
        $("#ae_mat_type_form input[name='parentTypeId']").val(matTypeTreeSelectNode.id);
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 编辑分类
function editMatType(){

    hideRMenu('rootMatTypeContextMenu');
    hideRMenu('matTypeContextMenu');
    $('#ae_mat_type_modal').modal('show');
    $('#ae_mat_type_modal_title').html('编辑分类');
    $('#ae_mat_type_form')[0].reset();
    aeMatTypeValidator.resetForm();
    $("#ae_mat_type_form input[name='matTypeId']").val('');
    $("#ae_mat_type_form input[name='parentTypeId']").val('');
    $("#ae_mat_type_form input[name='isTbEditNode']").val('');
    if(matTypeTreeSelectNode){
        $.ajax({
            url: ctx + '/aea/item/mat/type/getAeaItemMatType.do',
            type: 'post',
            data: {'id': matTypeTreeSelectNode.id},
            async: false,
            success: function (data) {
                // 记载表单数据
                loadFormData(true,'#ae_mat_type_form',data);
                if(data){
                    if(!data.parentTypeId){
                        $("#ae_mat_type_form input[name='parentTypeId']").val('root');
                    }
                }
            },
            error:function(){
                swal('错误信息', "获取数据失败！", 'error');
            }
        });
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

function editMatTypeById(id){

    var node = matTypeTree.getNodeByParam("id", id, null);
    if(node){

        matTypeTree.selectNode(node);
        matTypeTreeSelectNode = node;
        searchMatType();
        expandNextNodes();
        editMatType();
        $("#ae_mat_type_form input[name='isTbEditNode']").val('1');
    }
}

function deleteMatTypeById(id){

    hideRMenu('rootMatTypeContextMenu');
    hideRMenu('matTypeContextMenu');
    var node = matTypeTree.getNodeByParam("id", id, null);
    var parentNode = matTypeTreeSelectNode.getParentNode();
    if(node&&node.id){
        swal({
            title: '提示信息',
            text: '此操作将删除分类、下级子分类信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/item/mat/type/deleteAeaItemMatTypeById.do',
                    type: 'POST',
                    data:{'id': node.id},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            if(parentNode){ // 默认选中父级菜单
                                matTypeTree.selectNode(parentNode);
                                matTypeTreeSelectNode = parentNode;
                                searchMatType();
                            }
                            if(node){ // 移除节点
                                matTypeTree.removeNode(node);
                            }
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
        swal('提示信息', '请选择操作数据！', 'info');
    }
}


function matTypeParam(params){

    var pageNum = (params.offset / params.limit) + 1;
    commonQueryParams['pageNum'] = pageNum,
    commonQueryParams['pageSize'] = params.limit;
    return commonQueryParams;
}

function batchDeleteMatType(){

    var rows = $("#mat_type_list_tb").bootstrapTable('getSelections');
    var ids = [];
    if(rows!=null&&rows.length>0){
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].matTypeId);
        }
        swal({
            title: '提示信息',
            text: '此操作将删除分类、下级子分类信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/item/mat/type/batchDeleteAeaItemMatType.do',
                    type: 'POST',
                    data:{'ids': ids.toString()},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '批量删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            // 移除节点
                            for(var i=0;i<rows.length;i++){
                                var node = matTypeTree.getNodeByParam("id", rows[i].matTypeId, null);
                                matTypeTree.removeNode(node);
                            }
                            // 刷新表格
                            searchMatType();
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
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

function refreshMatType(){

    searchMatType();
}

function searchMatType(){

    commonQueryParams = {};
    var t = $('#search_mat_Type_form').serializeArray();
    $.each(t, function() {
        commonQueryParams[this.name] = this.value;
    });
    if(matTypeTreeSelectNode.type!='root'){
        commonQueryParams['parentTypeId'] = matTypeTreeSelectNode.id;
    }
    $("#mat_type_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    // $("#mat_type_list_tb").bootstrapTable('refresh');       //无参数刷新
}

function clearSearchMatType(){

    $('#search_mat_Type_form')[0].reset();
    searchMatType();
}

// 删除分类
function deleteMatType(){

    hideRMenu('rootMatTypeContextMenu');
    hideRMenu('matTypeContextMenu');
    var node = matTypeTree.getNodeByParam("id", matTypeTreeSelectNode.id, null);
    var parentNode = matTypeTreeSelectNode.getParentNode();
    if(node&&node.id){
        swal({
            title: '提示信息',
            text: '此操作将删除分类、下级子分类信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/item/mat/type/deleteAeaItemMatTypeById.do',
                    type: 'POST',
                    data:{'id': node.id},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            if(parentNode){ // 默认选中父级菜单
                                matTypeTree.selectNode(parentNode);
                                matTypeTreeSelectNode = parentNode;
                                searchMatType();
                            }
                            if(node){ // 移除节点
                                matTypeTree.removeNode(node);
                            }
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
        swal('提示信息', '请选择操作数据！', 'info');
    }
}

// 创建一个分类zTree node节点数据
function createNodeData(matType){

    if(matType){

        var id = matType.matTypeId;
        var name = matType.typeName;
        var pId =  matType.parentTypeId;
        var type = 'matType';
        var open = true;
        var isParent = true;
        var node = {
            'id': id,
            'name': name,
            'pId': pId,
            'open': open,
            'isParent': isParent,
            'type': type,
        };
        return node;
    }
    return null;
}