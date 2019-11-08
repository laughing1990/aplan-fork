// 页面加载完成加载函数
var matTypeInfoTree = null;  // 菜单树
var rMenu = null;
var add_item_mat_type_form_validator = null;//表单校验

$(function () {
    rMenu = $('#rMenu');
    initMenuTree();
    initSearchMatTypeTree();
});

//初始化树搜索框
var selectItemMatTypeKey,
    selectItemMatTypeNodeList = [],
    selectItemMatTypeLastValue = '';

function initSearchMatTypeTree() {
    selectItemMatTypeKey = $("#itemMatTypeKeyWord");
    selectItemMatTypeKey.bind("focus", focusSelectItemMatTypeKey).bind("blur", blurSelectItemMatTypeKey).bind("change cut input propertychange", searchSelectItemMatTypeNode);
    selectItemMatTypeKey.bind('keydown', function (e) {
        if (e.which == 13) {
            searchSelectItemMatTypeNode();
        }
    });
}

function focusSelectItemMatTypeKey(e) {
    if (selectItemMatTypeKey.hasClass("empty")) {
        selectItemMatTypeKey.removeClass("empty");
    }
}

function blurSelectItemMatTypeKey(e) {

    if (selectItemMatTypeKey.get(0).value === "") {
        selectItemMatTypeKey.addClass("empty");
    }
    searchSelectItemMatTypeNode(e);
}

//节点搜索
function searchSelectItemMatTypeNode() {
    // 取得输入的关键字的值
    var value = $.trim($('#itemMatTypeKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectItemMatTypeLastValue === value) {
        return;
    }

    // 保存最后一次
    selectItemMatTypeLastValue = value;

    var nodes = matTypeInfoTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectItemMatTypeAllNode(nodes);
        return;
    }
    hideSelectItemMatTypeAllNode(nodes);
    selectItemMatTypeNodeList = matTypeInfoTree.getNodesByParamFuzzy(keyType, value);
    updateSelectItemMatTypeNodes(selectItemMatTypeNodeList);

}

//隐藏所有节点
function hideSelectItemMatTypeAllNode(nodes) {

    nodes = matTypeInfoTree.transformToArray(nodes);
    for (var i = nodes.length - 1; i >= 0; i--) {
        matTypeInfoTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showSelectItemMatTypeAllNode(nodes) {

    nodes = matTypeInfoTree.transformToArray(nodes);
    for (var i = nodes.length - 1; i >= 0; i--) {
        if (nodes[i].getParentNode() != null) {
            matTypeInfoTree.expandNode(nodes[i], false, false, false, false);
        } else {
            matTypeInfoTree.expandNode(nodes[i], true, true, false, false);
        }
        matTypeInfoTree.showNode(nodes[i]);
        showSelectItemMatTypeAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateSelectItemMatTypeNodes(nodeList) {

    if (nodeList != null && nodeList.length > 0) {
        matTypeInfoTree.showNodes(nodeList);
        for (var i = 0, l = nodeList.length; i < l; i++) {

            //展开当前节点的父节点
            matTypeInfoTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while (nodeList[i].getParentNode() != null) {
                matTypeInfoTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                matTypeInfoTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            matTypeInfoTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            matTypeInfoTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

//清空查询关键字
function clearSearchItemType(obj) {
    var value = $.trim($(obj).val());
    if (value == '') return false;
    $(obj).val('');
    showSelectItemMatTypeAllNode(matTypeInfoTree.getNodes());
}

/**
 * 初始化菜单树
 */
function initMenuTree() {

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
            showTitle: false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: false, //设置 zTree 是否显示节点之间的连线。
            showHorizontal: true//设置是否水平平铺树（自定义属性）
        },
        check: {
            enable: false,
            chkboxType: {"Y": "", "N": ""},
            chkStyle: "checkbox"
        },
        //使用异步加载，必须设置 setting.async 中的各个属性
        async: {
            //设置 zTree 是否开启异步加载模式
            enable: true,
            autoParam: ["id"],
            dataType: "json",
            type: "post",
            url: ctx + '/aea/item/mat/type/getListMatTypeZtreeNode.do'
        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onClick: leftMatTypeTreeOnClick, //点击事件
            onRightClick: matTypeInfoTreeOnRightClick, //右击事件
            //用于捕获异步加载正常结束的事件回调函数
            onAsyncSuccess: onAsyncSuccessAppOrgTree,
            onExpand: function (event, treeId, treeNode, msg) {
                // var zTree = $.fn.zTree.getZTreeObj(treeId);
            }
        }
    };
    if (matTypeInfoTree) matTypeInfoTree.destroy();

    matTypeInfoTree = $.fn.zTree.init($("#matTypeInfoTree"), menuTreeSettings);

}

//在根类别下新增一级类别
function addItemMatType(parentId) {
    hideRMenu();
    if (parentId == '' || typeof (parentId) == 'undefined') parentId = 'root';
   // $('#add_item_mat_type_modal').modal('show');
    //$('#add_item_mat_type_modal_title').html('新增类别');
    //$('#add_item_mat_type_form')[0].reset();
    //$('#matTypeId').val('');
    //$('#parentTypeId').val(parentId);
    //initForm();
    addGlobalMat(parentId);
}

//新增子类别
function addChildItemType(typeId) {
    var selectNodes = matTypeInfoTree.getSelectedNodes();
    var parentId = selectNodes[0].id;
    addItemMatType(parentId);
}

//编辑类别
function editItemType() {

    hideRMenu();
    var selectNodes = matTypeInfoTree.getSelectedNodes();
    var id = selectNodes[0].id;
    $("#ae_cert_header").html(selectNodes[0].name+"编辑");

        showMatTypeDefEdit(id)


    /* var url = ctx + '/aea/item/mat/type/getAeaItemMatType.do';
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
             initForm();
             loadFormData(true,'#add_item_mat_type_form',data);
         },
         error:function(){
             swal('错误信息', '保存失败,服务器异常！', 'error');
         }
     });*/
}

//删除类别
function deleteItemType() {
    hideRMenu();
    var selectNodes = matTypeInfoTree.getSelectedNodes();
    var id = selectNodes[0].id;
    if (id == 'root') {
        swal('提示', '根节点无法删除！', 'error');
        return;
    }
    var url = ctx + '/aea/item/mat/type/deleteAeaItemMatTypeById.do';
    $.ajax({
        type: "POST",
        dataType: "json",
        cache: false,
        url: url,
        data: {id: id},
        success: function (data) {
            initMenuTree();
            swal('提示', '删除成功！', 'info');
            bootstrapTable.bootstrapTable('refresh');
        },
        error: function () {
            swal('错误信息', '删除失败,服务器异常！', 'error');
        }
    });
}

function initForm() {
    if (add_item_mat_type_form_validator) add_item_mat_type_form_validator.destroy();
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
        invalidHandler: function (event, validator) {
            mApp.scrollTo("#add_item_mat_type_form");
            // swal('错误信息', '保存失败！', 'error');
        },
        // 提交表单
        submitHandler: function (form) {
            var form = new FormData(document.getElementById("add_item_mat_type_form"));
            var url = ctx + "/aea/item/mat/type/saveAeaItemMatType.do";
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
                        initMenuTree();
                        swal('提示', '保存成功！', 'info');
                    }
                    ;
                },
                error: function () {
                    swal('错误信息', '保存失败,服务器异常！', 'error');
                }
            });
        }
    });
}

/**
 * 用于捕获异步加载正常结束的事件回调函数
 * event ： 标准的 js event 对象
 * treeId： 对应 zTree 的 treeId，便于用户操控
 * treeNode：进行异步加载的父节点 JSON 数据对象,针对根进行异步加载时，treeNode = null
 * msg：异步获取的节点数据字符串，主要便于用户调试使用。实际数据类型会受 setting.async.dataType 的设置影响，请参考 JQuery API 文档。
 */
function onAsyncSuccessAppOrgTree(event, treeId, treeNode, msg) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    //$("#ae_cert_header").html("编辑材料类型定义");
    //对于根节点，展开下一级
    if (treeNode == null) {
        var nodes = zTree.getNodes();
        for (var i = 0, l = nodes.length; i < l; i++) {
            zTree.expandNode(nodes[i], true, false, false);
        }
        zTree.selectNode(nodes[0]);
    }
};


//树定义不明确，该事件被后面覆盖了
// // 树单击事件
// function matTypeInfoTreeOnClick(event, treeId, treeNode, clickFlag) {
//     loadGlobalMat(treeNode.id);
// }

function leftMatTypeTreeOnClick(event, treeId, treeNode, clickFlag) {
    debugger
    if (treeNode.children != null) {
        showMatTypeDefList(treeNode.id);
    }
    else {
        $("#ae_cert_header").html(treeNode.name+"编辑");
        showMatTypeDefEdit(treeNode.id)
    }


}

function showMatTypeDefList(id) {
    showContentContainer('container_mat_type_def_list');
    GlobalMatDatatable.init();
}

function showMatTypeDefEdit(id) {
    showContentContainer('container_mat_type_def_edit');
    loadMatTypeDefDetail(id);
}

function matTypeInfoTreeSelectNode(id) {
    var node = matTypeInfoTree.getNodeByParam("id", id, null);
    matTypeInfoTree.selectNode(node);
}

// 在ztree上的右击事件
function matTypeInfoTreeOnRightClick(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();
    if (event.target.tagName == 'SPAN') {
        showRMenu(event.clientX + 15, event.clientY);
    }
    if (treeNode.id == 'root') {
        if ($('#newMatType span').html() != '新增类别') {
            $('#newMatType span').html('新增类别');
        }
    } else {
        if ($('#newMatType span').html() != '新增子类') {
            $('#newMatType span').html('新增子类');
        }
    }
    matTypeInfoTree.selectNode(treeNode);
}

//显示右键菜单
function showRMenu(x, y) {

    $("#rMenu ul").show();
    rMenu.css({"top": y + "px", "left": x + "px", "visibility": "visible"}); //设置右键菜单的位置、可见
    $("body").bind("mousedown", onBodyMouseDown);
}

//隐藏右键菜单
function hideRMenu() {

    //设置右键菜单不可见
    if (rMenu) rMenu.css({"visibility": "hidden"});
    $("body").unbind("mousedown", onBodyMouseDown);
}

//鼠标按下事件
function onBodyMouseDown(event) {

    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
        rMenu.css({"visibility": "hidden"});
    }
}

//展开下一级子节点
function expand() {

    hideRMenu();
    var selectNodes = matTypeInfoTree.getSelectedNodes();
    matTypeInfoTree.expandNode(selectNodes[0], true, null, null);
}

//展开所有子节点
function expandSon() {

    hideRMenu();
    var selectNodes = matTypeInfoTree.getSelectedNodes();
    matTypeInfoTree.expandNode(selectNodes[0], true, true, null);
}

//折叠子节点
function collapse() {

    hideRMenu();
    var selectNodes = matTypeInfoTree.getSelectedNodes();
    matTypeInfoTree.expandNode(selectNodes[0], false, null, null);
}

//全部展开
function expandAll() {

    hideRMenu();
    matTypeInfoTree.expandAll(true);
}

//全部折叠
function collapseAll() {

    hideRMenu();
    matTypeInfoTree.expandAll(false);
}


function correlateItemMatInfo(type) {
    var title = '选择输入材料类别';
    if (type == 'out') title = '选择输出材料类别';
    $('#add_item_mat_modal').modal('show');
    $('#add_item_mat_modal_title').html(title);
    initTree();
    initSearchMatTypeZtree();
}



