var selectCertTree = null;
var certDialogSign = 0;
var itemId = "";
var isInputView = 1;
var selectCertLastValue = "";

var selectCertTreeSetting = {
    edit: {
        showRemoveBtn: false,//设置是否显示删除按钮
        showRenameBtn: false//设置是否显示编辑名称按钮
    },
    check: {
        enable: true,
        chkStyle: "checkbox",
        chkboxType: { "Y": "", "N": "" }
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
        selectedMulti: true,//设置是否允许同时选中多个节点
        showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
        showLine: true, //设置 zTree 是否显示节点之间的连线。
        showHorizontal: false//设置是否水平平铺树（自定义属性）

    },
    //用于捕获节点被点击的事件回调函数
    callback: {
        onCheck: onSelectCertCheck,
        onClick: onClickSelectCertNode,
    }
};

function onClickSelectCertNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode&&treeNode.type=='cert'){
        if(treeNode.checked){
            treeObj.checkNode(treeNode,false,false,true);
            $('li[name="selectCertLi"]').each(function(){
                var categoryId = $(this).attr('category-id');
                if(categoryId==treeNode.id){
                    $(this).remove();
                    return false;
                }
            });
        }else{
            treeObj.checkNode(treeNode,true,false,true);
            var liHtml = '<li name="selectCertLi" category-id="'+treeNode.id+'">' +
                '<span class="drag-handle_td" onclick="removeSelectedCert(\''+treeNode.id+'\');">×</span>' +
                '<span class="org_name_td">'+treeNode.name+'</span>' +
                '</li>';
            var i=0;
            $('li[name="selectCertLi"]').each(function(){
                var categoryId = $(this).attr('category-id');
                if(categoryId==treeNode.id){
                    i++;
                    return false;
                }
            });
            if(i==0){
                $('#selectedCertUl').append(liHtml);
            }
        }
    }
}

function onSelectCertCheck(event, treeId, treeNode){
    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode&&treeNode.type=='cert'){
        if(treeNode.checked){
            treeObj.selectNode(treeNode);
            var liHtml = '<li name="selectCertLi" category-id="'+treeNode.id+'">' +
                '<span class="drag-handle_td" style="cursor: pointer;" onclick="removeSelectedCert(\''+treeNode.id+'\');">&nbsp;&nbsp;×&nbsp;&nbsp;</span>' +
                '<span class="org_name_td">'+treeNode.name+'</span>' +
                '</li>';
            var i=0;
            $('li[name="selectCertLi"]').each(function(){
                var categoryId = $(this).attr('category-id');
                if(categoryId==treeNode.id){
                    i++;
                    return false;
                }
            });
            if(i==0){
                $('#selectedCertUl').append(liHtml);
            }
        }else{
            treeObj.cancelSelectedNode(treeNode);
            $('li[name="selectCertLi"]').each(function(){
                var categoryId = $(this).attr('category-id');
                if(categoryId==treeNode.id){
                    $(this).remove();
                    return false;
                }
            });
        }
    }
}

function removeSelectedCert(itemId){
    $('li[name="selectCertLi"]').each(function(){
        var categoryId = $(this).attr('category-id');
        if(categoryId==itemId){
            $(this).remove();
            return false;
        }
    });
    // 取消所有所选
    selectCertTree.cancelSelectedNode();
    var node = selectCertTree.getNodeByParam("id", itemId, null);
    if (node) {
        selectCertTree.checkNode(node, false, true, false);
    }
}

function openCertSelect(isIn) {
    isInputView = isIn;
    $("#select_cert_ztree_modal").modal("show");
    $("#certOperaDiv").show();
    $("#certTreeDiv").hide();
    $('input[type=radio][name=certIsStateInout][value="0"]').prop("checked",true);
    $("#certItemStateDiv").hide();
    $("#certHidIds").val("");
    $("#cert_show_input").val("");
    $("#selectedCertUl").html("");

    $('input[type=radio][name=certIsStateInout]').change(function() {
        if (this.value == '1') {
            $("#certItemStateDiv").show();
        }
        else if (this.value == '0') {
            $("#certItemStateDiv").hide();
            $('#certItemStateId').selectpicker('val', "");
            $('#certItemStateId').selectpicker('refresh');
        }
    });

    if(isIn == 1) {
        $('#certItemStateId').selectpicker('val', "");
        $('#certItemStateId').empty();
        $.ajax({
            url: ctx + '/aea/item/mat/getAllItemState.do',
            type: 'POST',
            data: {itemId:itemId},
            success: function (data) {
                if (data) {
                    var stateItemList = eval('(' + data + ')');
                    if(stateItemList.length > 0) {
                        for (var i = 0; i < stateItemList.length; i++) {
                            $('#certItemStateId').append("<option value='" + stateItemList[i].stateId + "'>" + stateItemList[i].stateName + "</option>");
                        }
                        $('#certItemStateId').selectpicker('refresh');
                        $('input[type=radio][name=certIsStateInout]').attr("disabled",false);
                    } else {
                        $('input[type=radio][name=certIsStateInout]').attr("disabled",true);
                    }
                }
            },
            error: function () {
                swal('错误信息', '获取事项情形信息出错！', 'error');
            }
        });
        $('input[type=radio][name=certIsStateInout]').attr("disabled",false);
    } else {
        $('input[type=radio][name=certIsStateInout]').attr("disabled",true);
    }

    $.ajax({
        url: ctx+'/aea/cert/gtreeTypeCert.do',
        type:'post',
        data:{},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                selectCertTree = $.fn.zTree.init($("#selectCertTree"), selectCertTreeSetting, data);
            }
        },
        error: function(){
            swal('错误信息', '初始化事项数据异常!', 'error');
        }
    });
}

function chooseCert() {
    certDialogSign = 1;
    $("#certTreeDiv").show();
    $("#certOperaDiv").hide();
}

function certDialogChoose() {
    if(certDialogSign == 1) {
        var certIds = "";
        var certNames = "";
        $("li[name='selectCertLi']").each(function(){
            certIds = certIds + $(this).attr("category-id") + ",";
            certNames = certNames + $(this).find("span").eq(1).text() + ",";
        });
        if(certNames.length > 0 && certIds.length > 0) {
            $("#certTreeDiv").hide();
            $("#certOperaDiv").show();
            // $("#selectCertTree").find("li").each(function(){
            //     var ctemp = $(this).find("a").attr("class");
            //     if(ctemp.indexOf("curSelectedNode") > 0) {
            //         $(this).find("a").attr("class","level2");
            //         return;
            //     }
            // });
            // selectCertTree.checkAllNodes(false);
            // $("#selectedCertUl").html("");
            $("#certHidIds").val(certIds.substring(0, certIds.length-1));
            $("#cert_show_input").val(certNames.substring(0, certNames.length-1));
            certDialogSign = 0;
        } else {
            swal('提示', '请勾电子证后操作！', 'info');
            return false;
        }
    } else {
        if($("#certHidIds").val().length > 0) {
            var inCert = {};
            inCert['itemId'] = itemId;
            inCert['isInput'] = isInputView;
            inCert['certIds'] = $("#certHidIds").val();
            inCert['isStateInout'] = $('input[type=radio][name=certIsStateInout]:checked').val();
            inCert['itemStateId'] = $('#certItemStateId').val();
            $.ajax({
                type: 'post',
                url: INOUT_URL_PREFIX + 'addGlobalCertAsInout.do',
                dataType: 'json',
                data: inCert,
                success: function (result) {
                    if (result.success) {
                        agcloud.ui.metronic.showSuccessTip('添加成功！', 1000);
                        $('#select_cert_ztree_modal').modal('hide');
                        refresh(isInputView);
                    } else {
                        agcloud.ui.metronic.showSwal({message: '添加失败!', type: 'error'});
                    }
                }
            });
        } else {
            swal('提示', '请选择导入电子证照后操作！', 'info');
        }
    }
}

function certDialogClose() {
    if(certDialogSign == 1) {
        certDialogSign = 0;
        $("#certTreeDiv").hide();
        $("#certOperaDiv").show();
    } else {
        $("#select_cert_ztree_modal").modal("hide");
    }
}

function loadItemCert(id) {
    itemId = id;
}

function searchSelectCertNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#selectCertKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (selectCertLastValue === value) {
        return;
    }

    // 保存最后一次
    selectCertLastValue = value;

    var nodes = selectCertTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showSelectCertAllNode(nodes);
        return;
    }
    hideSelectCertAllNode(nodes);
    selectCertNodeList = selectCertTree.getNodesByParamFuzzy(keyType, value);
    updateSelectCertNodes(selectCertNodeList);
}

//全部展开
function expandSelectCertAllNode(){
    selectCertTree.expandAll(true);
}

//全部折叠
function collapseSelectCertAllNode(){
    selectCertTree.expandAll(false);
}

// 清空查询
function clearSearchSelectCertNode(){

    // 清空查询内容
    $('#selectCertKeyWord').val('');
    showSelectCertAllNode(selectCertTree.getNodes());
}

//显示所有节点
function showSelectCertAllNode(nodes){

    nodes = selectCertTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            selectCertTree.expandNode(nodes[i],false,false,false,false);
        }else{
            selectCertTree.expandNode(nodes[i],true,true,false,false);
        }
        selectCertTree.showNode(nodes[i]);
        showSelectCertAllNode(nodes[i].children);
    }
}

//隐藏所有节点
function hideSelectCertAllNode(nodes){
    nodes = selectCertTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        selectCertTree.hideNode(nodes[i]);
    }
}

//更新节点状态
function updateSelectCertNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        selectCertTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            selectCertTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                selectCertTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                selectCertTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            selectCertTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            selectCertTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}