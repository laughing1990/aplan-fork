var frontCondKey,
    nodeList = [],
    lastValue = "",
    frontCondTree = null,
    frontCondTreeSelectNode = null,
    add_item_cond_validator = null,
    frontCondTreeSetting = {
        edit: {
            enable: false, //设置 zTree 是否处于编辑状态
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
            showTitle : false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: false //设置 zTree 是否显示节点之间的连线。
        },
        callback:{
            onClick: frontCondTreeOnClick, //点击事件
            onRightClick: frontCondTreeOnRightClick, //右击事件
        }
    };

$(function(){

    $('#frontCondTree').css({'max-height': $('#westPanel').height()-116,'height': $('#westPanel').height()-116});

    // 初始化事项前置条件树
    initFrontCondTree();

    // 关键字搜索事项节点
    frontCondKey = $("#frontCondKeyWord");
    frontCondKey.bind("focus", focusFrontCondKey)
                .bind("blur", blurFrontCondKey)
                .bind("change cut input propertychange",searchFrontCondNode());
    frontCondKey.bind('keydown', function (e){
        if(e.which == 13){
            searchFrontCondNode();
        }
    });

    $("#useEl").change(function(){

        if($(this).val()=='1'){
            $('#condElDiv').show();
            $('#condEl').rules("add", {
                required: true,
                messages: {
                    required: '<font color="red">此项必填！</font>',
                }
            });
        }else{
            $('#condElDiv').hide();
            $('#condEl').rules("remove");
        }
    });

    // 设置初始化校验
    add_item_cond_validator = $('#add_item_cond_form').validate({
        // 定义校验规则
        rules: {
            condName: {
                required: true,
                maxlength: 500
            },
            useEl:{
                required: true,
            },
            sortNo:{
                required: true,
                maxlength: 38
            },
            condMemo:{
                maxlength: 500
            }
        },
        messages: {
            condName: {
                required: '<font color="red">此项必填!</font>',
                maxlength: "长度不能超过500个字母!"
            },
            useEl:{
                required: '<font color="red">此项必填!</font>',
            },
            sortNo:{
                required: '<font color="red">此项必填!</font>',
                maxlength: "长度不能超过38个字母!"
            },
            condMemo:{
                maxlength: "长度不能超过500个字母!"
            }
        },
        // 提交表单
        submitHandler: function(form){

            var d = {};
            var t = $('#add_item_cond_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/aea/item/cond/saveAeaItemCond.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success){
                        $('#add_item_cond_modal').modal('hide');
                        swal({
                            title: '提示信息',
                            text: '保存成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', "保存信息失败！", 'error');
                }
            });
        }
    });

    $('#closeaddItemCondBtn').click(function(){
        $('#add_item_cond_modal').modal('hide');
        add_item_cond_validator.resetForm();
    });
});

/**
 * 初始化事项树
 */
function initFrontCondTree(){

    $.ajax({
        url: ctx+'/aea/item/gtreeItemCond.do',
        type:'post',
        data:{'itemId': itemId},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(frontCondTree)frontCondTree.destory();
                frontCondTree = $.fn.zTree.init($("#frontCondTree"), frontCondTreeSetting, data);
            }
        },
        error: function(){

        }
    });
}

function focusFrontCondKey(e) {

    if (frontCondKey.hasClass("empty")) {
        frontCondKey.removeClass("empty");
    }
}

function blurFrontCondKey(e) {

    if (frontCondKey.get(0).value === "") {
        frontCondKey.addClass("empty");
    }
    searchFrontCondNode(e);
}

// 搜索节点
function searchFrontCondNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#frontCondKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一样，就退出不查了。
    if (lastValue === value) {
        return;
    }

    // 保存最后一次
    lastValue = value;

    var nodes = frontCondTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showAllNode(nodes);
        return;
    }
    hideAllNode(nodes);
    nodeList = frontCondTree.getNodesByParamFuzzy(keyType, value);
    updateNodes(nodeList);
}

// 清空查询
function clearSearchFrontCondNNode(){

    // 清空查询内容
    $('#frontCondKeyWord').val('');
    showAllNode(frontCondTree.getNodes());
}

//全部展开
function expandFrontCondAllNode(){

    hideRMenu('frontCondRootContextMenu');
    hideRMenu('frontCondNodeContextMenu');
    frontCondTree.expandAll(true);
}

//全部折叠
function collapseFrontCondAllNode(){

    hideRMenu('frontCondRootContextMenu');
    hideRMenu('frontCondNodeContextMenu');
    frontCondTree.expandAll(false);
}

//隐藏所有节点
function hideAllNode(nodes){

    nodes = frontCondTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        frontCondTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showAllNode(nodes){

    nodes = frontCondTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            frontCondTree.expandNode(nodes[i],false,false,false,false);
        }else{
            frontCondTree.expandNode(nodes[i],true,true,false,false);
        }
        frontCondTree.showNode(nodes[i]);
        showAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        frontCondTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            frontCondTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                frontCondTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                frontCondTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            frontCondTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            frontCondTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 树单击事件
function frontCondTreeOnClick(event, treeId, treeNode, clickFlag) {

    if(treeNode){

        frontCondTree.selectNode(treeNode);
        frontCondTreeSelectNode = treeNode;
        expandNextNode();
    }
}

//展开下一级子节点
function expandNextNode(){

    hideRMenu('frontCondRootContextMenu');
    hideRMenu('frontCondNodeContextMenu');
    var selectNodes = frontCondTree.getSelectedNodes();
    frontCondTree.expandNode(selectNodes[0], true, null, null);
}

// 在ztree上的右击事件
function frontCondTreeOnRightClick(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();
    frontCondTree.selectNode(treeNode);
    frontCondTreeSelectNode = treeNode;
    if(event.target.tagName.toLowerCase()=='span'||
        event.target.tagName.toLowerCase()=='a'||
        event.target.tagName.toLowerCase()=='ul'){

        var y = event.clientY+10;
        var maxHeight = $('#frontCondTree').height();
        if(event.clientY>maxHeight){
            y -= ($('.contextMenuDiv').height()+5);
        }
        if(treeNode.type=='cond'){
            showRMenu('frontCondNodeContextMenu',event.clientX+15, y);
        }else{
            showRMenu('frontCondRootContextMenu',event.clientX+15, y);
        }
    }
}

function saveItemNeedCondInfo(){

    if(itemId) {
        var isNeedFrontCond = $('input[name="isNeedFrontCond"]:checked').val();
        $.ajax({
            url: ctx + '/aea/item/updateAeaItem.do',
            type: 'POST',
            data: {'itemId': itemId,'isNeedFrontCond': isNeedFrontCond},
            async: false,
            success: function (result) {
                if(result.success){
                    swal({
                        title: '提示信息',
                        text: '保存成功!',
                        type: 'success',
                        timer: 1000,
                        showConfirmButton: false
                    });
                }else{
                    swal('错误信息', result.message, 'error');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }else{
        swal('提示信息', '事项id为空！', 'info');
    }
}


// 新增前置条件
function addItemCond(){

    if(itemId){

        $('#add_item_cond_modal').modal('show');
        $('#add_item_cond_modal_title').html('新增前置条件');
        $('#add_item_cond_form')[0].reset();
        add_item_cond_validator.resetForm();
        $('#cond_itemCondId').val('');
        $('#cond_itemId').val('');

        $("#add_item_cond_form input[name='itemCondId']").val('');
        $("#add_item_cond_form input[name='itemId']").val(itemId);
        $("#add_item_cond_form input[name='isActive']").val("1");
        $('#useEl option:eq(1)').prop("selected", 'selected');
        $('#isAccept option:eq(1)').prop("selected", 'selected');
        $('#condElDiv').hide();
        $('#noAcceptDemoDiv').hide();

        $.ajax({
            url: ctx + '/aea/item/cond/getMaxSortNo.do',
            type: 'POST',
            data: {},
            async: false,
            success: function (data) {
                $("#add_item_cond_form input[name='sortNo']").val(data);
            },
            error:function(){
                $("#add_item_cond_form input[name='sortNo']").val(1);
            }
        });
    }else{
        swal('提示信息', '事项id为空！', 'info');
    }
}

// 编辑前置条件
function editItemCond(itemCondId){

    $('#add_item_cond_modal').modal('show');
    $('#add_item_cond_modal_title').html('编辑前置条件');
    $('#add_item_cond_form')[0].reset();
    add_item_cond_validator.resetForm();
    $('#cond_itemCondId').val('');
    $('#cond_itemId').val('');

    $('#condElDiv').hide();
    $.ajax({
        url: ctx+'/aea/item/cond/getAeaItemCond.do',
        type: 'POST',
        data: {'id': itemCondId},
        success: function (data) {
            if(data){
                loadFormData(true,'#add_item_cond_form',data);

                if(data.useEl=='1'){
                    $('#condElDiv').show();
                    $('#condEl').rules("add", {
                        required: true,
                        messages: {
                            required: '<font color="red">此项必填！</font>',
                        }
                    });
                }else{
                    $('#condElDiv').hide();
                    $('#condEl').rules("remove");
                }
            }
        },
        error:function(){
            swal('错误信息', '获取前置条件信息出错！', 'error');
        }
    });
}

// 删除分类
function deleteItemCond(itemCondId){

    var msg = '你确定要删除吗？';
    swal({
        title: '提示信息',
        text: '将删除前置条件,可能影响事项处理，您确定删除吗?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value){
            $.ajax({
                url: ctx+'/aea/item/cond/deleteAeaItemCondById.do',
                type: 'POST',
                data: {'id': itemCondId},
                success: function (result) {
                    if(result.success){
                        swal({
                            title: '提示信息',
                            text: '删除成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
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