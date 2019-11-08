
var matTypeTree = null;  // 菜单树
var matTypeRMenu = null;
var add_item_mat_type_form_validator;
$(function(){
    matTypeRMenu = $('#matTypeRMenu');
    initAddMatTypeForm();
});
/**
 * 初始化菜单树
 */
function initMatTypeTree(){

    var menuTreeSettings = {
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
                // rootPId: 0
            }
        },
        view: {
            selectedMulti: false,//设置是否允许同时选中多个节点
            showTitle : false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: true, //设置 zTree 是否显示节点之间的连线。
            showHorizontal:true//设置是否水平平铺树（自定义属性）

        },
        check: {
            enable: false,
            chkboxType:{ "Y" : "", "N" : "" },
            chkStyle: "checkbox"
        },
        //使用异步加载，必须设置 setting.async 中的各个属性
        async: {
            //设置 zTree 是否开启异步加载模式
            enable: true,
            autoParam:["id"],
            dataType:"json",
            type:"post",
            url:ctx+'/aea/item/mat/type/getListMatTypeZtreeNode.do'
        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onClick: matTypeTreeOnClick, //点击事件
            onRightClick: matTypeTreeOnRightClick, //右击事件
            onDblClick:matTypeTreeonDblClick,//双击事件
            //用于捕获异步加载正常结束的事件回调函数
            onAsyncSuccess: onAsyncSuccessMatTypeTree
            ,onExpand:function(event, treeId, treeNode, msg){
                // var zTree = $.fn.zTree.getZTreeObj(treeId);
            }
        }
    };
    if(matTypeTree) matTypeTree.destroy();
    matTypeTree = $.fn.zTree.init($("#matTypeTree"), menuTreeSettings);
}
// 树单击事件
function matTypeTreeOnClick(event, treeId, treeNode, clickFlag) {

}
//显示右键菜单
function showMatTypeTreeMenu(x, y) {
    $("#matTypeRMenu ul").show();
    matTypeRMenu.css({"top": y + "px", "left" :x + "px", "visibility":"visible"}); //设置右键菜单的位置、可见
    $("body").bind("mousedown", onBodyMouseDown);
}
// 在ztree上的右击事件
function matTypeTreeOnRightClick(event, treeId, treeNode) {
    //禁止浏览器的菜单打开
    event.preventDefault();
    if(event.target.tagName=='SPAN'){
        showMatTypeTreeMenu( event.clientX-450, event.clientY-145);
    }
    matTypeTree.selectNode(treeNode);
}
//隐藏右键菜单
function hideMatTypeRMenu() {

    //设置右键菜单不可见
    if (matTypeRMenu) matTypeRMenu.css({"visibility": "hidden"});
    $("body").unbind("mousedown", onBodyMouseDown);
}
function initAddMatTypeForm() {
    if(add_item_mat_type_form_validator)add_item_mat_type_form_validator.destroy();
    add_item_mat_type_form_validator = $("#add_item_mat_type_form").validate({
        // 定义校验规则
        rules: {
            typeCode: {
                required: true
            },
            typeName: {
                required: true
            }
        },
        messages: {
            typeCode: {
                required: "该输入项为必输项！"
            },
            typeName: {
                required: "该输入项为必输项！"
            }
        },
        invalidHandler: function(event, validator) {
            mApp.scrollTo("#add_item_mat_type_form");
            // swal('错误信息', '保存失败！', 'error');
        },
        // 提交表单
        submitHandler: function(form){
            var form = new FormData(document.getElementById("add_item_mat_type_form"));
            var url = ctx+"/aea/item/mat/type/saveAeaItemMatType.do";
            // 异步保存
            $.ajax({
                type: "POST",
                dataType: "json",
                cache: false,
                url: url,
                data: form,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.success) {
                        $('#add_item_mat_type_modal').modal('hide');
                        initMatTypeTree();
                        swal('提示', '保存成功！', 'info');
                    };
                },
                error:function(){
                    swal('错误信息', '保存失败,服务器异常！', 'error');
                }
            });
        }
    });
}
//在根类别下新增一级类别
function addItemMatType(parentId) {
    hideMatTypeRMenu();
    if(parentId == '' || typeof (parentId) == 'undefined')parentId = 'root';
    $('#add_item_mat_type_modal').modal('show');
    $('#add_item_mat_type_modal_title').html('新增类别');
    $('#add_item_mat_type_form')[0].reset();
    $('#matTypeId').val('');
    $('#parentTypeId').val(parentId);
    //initAddMatTypeForm();
    add_item_mat_type_form_validator.resetForm();
}
//新增子类别
function addChildItemType(typeId) {
    var selectNodes = matTypeTree.getSelectedNodes();
    var parentId =  selectNodes[0].id;
    addItemMatType(parentId);
}
//编辑类别
function editItemType() {
    hideMatTypeRMenu();
    var selectNodes = matTypeTree.getSelectedNodes();
    var id = selectNodes[0].id;
    var url = ctx + '/aea/item/mat/type/getAeaItemMatType.do';
    $.ajax({
        type: "POST",
        dataType: "json",
        cache: false,
        url: url,
        data: {id:id},
        success: function (data) {
            $('#add_item_mat_type_modal').modal('show');
            $('#add_item_mat_type_modal_title').html('编辑类别');
            $('#add_item_mat_type_form')[0].reset();
            $('#matTypeId').val('');
            $('#parentTypeId').val('');
            //initAddMatTypeForm();
            add_item_mat_type_form_validator.resetForm();
            loadFormData(true,'#add_item_mat_type_form',data);
        },
        error:function(){
            swal('错误信息', '保存失败,服务器异常！', 'error');
        }
    });
}
//删除类别
function deleteItemType() {
    hideMatTypeRMenu();
    var selectNodes = matTypeTree.getSelectedNodes();
    var id = selectNodes[0].id;
    var sendDelItemType = function() {
        var url = ctx + '/aea/item/mat/type/deleteAeaItemMatTypeById.do';
        $.ajax({
            type: "POST",
            dataType: "json",
            cache: false,
            url: url,
            data: {id:id},
            success: function (data) {
                initMatTypeTree();
                swal('提示', '删除成功！', 'info');
            },
            error:function(){
                swal('错误信息', '删除失败,服务器异常！', 'error');
            }
        })
    };
    agcloud.ui.metronic.showConfirm('确定要删除['+selectNodes[0].name+']类别么?',sendDelItemType);
}
function onBodyMouseDown(event){

    if (!(event.target.id == "matTypeRMenu" || $(event.target).parents("#matTypeRMenu").length>0)) {
        matTypeRMenu.css({"visibility" : "hidden"});
    }
}
//双击事件
function matTypeTreeonDblClick(event, treeId, treeNode, clickFlag) {

}

/**
 * 用于捕获异步加载正常结束的事件回调函数
 * event ： 标准的 js event 对象
 * treeId： 对应 zTree 的 treeId，便于用户操控
 * treeNode：进行异步加载的父节点 JSON 数据对象,针对根进行异步加载时，treeNode = null
 * msg：异步获取的节点数据字符串，主要便于用户调试使用。实际数据类型会受 setting.async.dataType 的设置影响，请参考 JQuery API 文档。
 */
function onAsyncSuccessMatTypeTree(event, treeId, treeNode, msg) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    //对于根节点，展开下一级
    if(treeNode==null){
        var nodes = zTree.getNodes();
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.expandNode(nodes[i], true, false, false);
        }
        var childNode = nodes[0].children
        if(childNode != 'undefined'){
            zTree.selectNode(childNode[0]);//选择第一个子节点
        }
    }
}
//关闭弹窗
function closeMatTypeZtree() {
    $('#add_item_mat_modal').modal('hide');
}

//清空搜索左树文字框
// function clearMatTypeZtreeForTree() {
//     $("#matTypeZtreeKeyWord").val("");
//     initMatTypeTree();
// }

//全部展开
function expandTreeAllNode(){
    matTypeTree.expandAll(true);
}

//全部折叠
function collapseAllNode(){
    matTypeTree.expandAll(false);
}

///////////////////////////////////////////////////////////////////////////////////
//初始化树搜索框
var selectMatTypeKey,
    selectMatTypeNodeList = [],
    selectMatTypeLastValue = '';
function initSearchMatTypeZtree() {
    selectMatTypeKey = $("#matTypeZtreeKeyWord");
    selectMatTypeKey.bind("focus", focusSelectMatTypeKey).bind("blur", blurSelectMatTypeKey).bind("change cut input propertychange",searchSelectMatTypeNode);
    selectMatTypeKey.bind('keydown', function (e){
        if(e.which == 13){
            searchSelectMatTypeNode();
        }
    });
}
function focusSelectMatTypeKey(e) {
    if (selectMatTypeKey.hasClass("empty")) {
        selectMatTypeKey.removeClass("empty");
    }
}
function blurSelectMatTypeKey(e) {

    if (selectMatTypeKey.get(0).value === "") {
        selectMatTypeKey.addClass("empty");
    }
    searchSelectMatTypeNode(e);
}
//节点搜索
function searchSelectMatTypeNode(){
    // 取得输入的关键字的值
    var value = $.trim($('#matTypeZtreeKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectMatTypeLastValue === value) {
        return;
    }

    // 保存最后一次
    selectMatTypeLastValue = value;

    var nodes = matTypeTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectMatTypeAllNode(nodes);
        return;
    }
    hideSelectMatTypeAllNode(nodes);
    selectMatTypeNodeList = matTypeTree.getNodesByParamFuzzy(keyType, value);
    updateSelectMatTypeNodes(selectMatTypeNodeList);

}
//隐藏所有节点
function hideSelectMatTypeAllNode(nodes){

    nodes = matTypeTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        matTypeTree.hideNode(nodes[i]);
    }
}
//显示所有节点
function showSelectMatTypeAllNode(nodes){

    nodes = matTypeTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            matTypeTree.expandNode(nodes[i],false,false,false,false);
        }else{
            matTypeTree.expandNode(nodes[i],true,true,false,false);
        }
        matTypeTree.showNode(nodes[i]);
        showSelectMatTypeAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectMatTypeNodes(nodeList) {

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
//清空查询关键字
function clearSearchItemType(obj){
    var value = $.trim($(obj).val());
    if(value == '')return false;
    $(obj).val('');
    showSelectMatTypeAllNode( matTypeTree.getNodes());
}

///////////////////////////////////////////////////////////////////////////////////

//选择事项材料类别
//function selectMatType() {
//}