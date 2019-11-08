var majorInfoTreeKey,
    majorInfoTreeNodeList = [],
    majorInfoTreeLastValue = "",
    majorInfoTree = null,
    majorInfoTreeRightClickSelectNode = null,  //树右键选中节点
    majorInfoTreeSetting = {
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
        // check: {
        //     enable: true,
        //     chkStyle: "checkbox",
        //     chkboxType: { "Y": "", "N": "" }
        // },
        view: {
            selectedMulti: false,//设置是否允许同时选中多个节点
            showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: true, //设置 zTree 是否显示节点之间的连线。
            showHorizontal: false//设置是否水平平铺树（自定义属性）

        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onClick: onClickmajorInfoTreeNode,
            onRightClick:onRightClickmajorInfoTreeNode, //右击事件
        }
    };
var ae_major_form_validator;//表单校验

// 加载专业类别数据
function loadMajorInfoTreeData(){
    var h = $('#type_rel_major_list_page').height();
    $('#majorInfoTreeScrollable').height(h-150);
    $.ajax({
        url: ctx+'/supermarket/major/getMajorTreeByQualId.do',
        type:'post',
        data:{
            'QualId':currQualId
        },
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                if(majorInfoTree)majorInfoTree.destroy();
                majorInfoTree = $.fn.zTree.init($("#majorInfoTree"), majorInfoTreeSetting , data);
            }
        },
        error: function(){
            swal('错误信息', '初始化专业类别异常!', 'error');
        }
    });
}
// 初始化树
function initMajorZtree() {
    loadMajorInfoTreeData();
    // 默认选中第一个节点数据
    expandFirstNode();
}

$(function(){
    // 关键字搜索节点
    majorInfoTreeKey = $("#majorInfoTreeKeyWord");
    majorInfoTreeKey.bind("focus", focusmajorInfoTreeKey).bind("blur", blurmajorInfoTreeKey).bind("change cut input propertychange",searchmajorInfoTreeNode);
    majorInfoTreeKey.bind('keydown', function (e){
        if(e.which == 13){
            searchmajorInfoTreeNode();
        }
    });
    $('#majorInfoTreeScrollable').niceScroll({
        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });
    initMajorFormValidator();
});

function focusmajorInfoTreeKey(e) {
    if (majorInfoTreeKey.hasClass("empty")) {
        majorInfoTreeKey.removeClass("empty");
    }
}

function blurmajorInfoTreeKey(e) {
    if (majorInfoTreeKey.get(0).value === "") {
        majorInfoTreeKey.addClass("empty");
    }
    searchmajorInfoTreeNode();
}

// 点击事件
function onClickmajorInfoTreeNode(event, treeId, treeNode) {
    if(treeNode){
        majorInfoTree.selectNode(treeNode,true);
        expandNextNodes();
    }
}

//展开下一级子节点
function expandNextNodes(){
    var selectNodes = majorInfoTree.getSelectedNodes();
    majorInfoTree.expandNode(selectNodes[0], true, null, null);
}

// 右键事件
function onRightClickmajorInfoTreeNode(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();
    majorInfoTree.selectNode(treeNode);
    majorInfoTreeRightClickSelectNode = treeNode;

    if(event.target.tagName.toLowerCase()=='span'||event.target.tagName.toLowerCase()=='a'||event.target.tagName.toLowerCase()=='ul'){
        var y = event.clientY;
        var x = event.clientX-500;
        var h = $('#majorInfoTreeScrollable').height();
        if(y > h-50){
            y -= 150;
        }
        if(majorInfoTreeRightClickSelectNode.pId == 0){
            showRMenu('rootContextMenu',x, y);
        }else{
            showRMenu('serviceMajorContextMenu',x, y);
        }
    }
}

// 默认选中第一个节点
function expandFirstNode(){
    if(majorInfoTree){
        var nodes = majorInfoTree.getNodes();
        if(nodes!=null&&nodes.length>0){
            majorInfoTree.selectNode(nodes[0]); //选中第一个节点
        }
    }
}

//全部展开
function expandmajorInfoTreeAllNode(){
    majorInfoTree.expandAll(true);
}

//全部折叠
function collapsemajorInfoTreeAllNode(){
    majorInfoTree.expandAll(false);
}

// 搜索节点
function searchmajorInfoTreeNode(){
    // 取得输入的关键字的值
    var value = $.trim($('#majorInfoTreeKeyWord').val());
    // 按名字查询
    var keyType = "name";
    // 如果和上次一次，就退出不查了。
    if (majorInfoTreeLastValue === value) {
        return;
    }
    // 保存最后一次
    majorInfoTreeLastValue = value;
    var nodes = majorInfoTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showmajorInfoTreeAllNode(nodes);
        return;
    }
    hidemajorInfoTreeAllNode(nodes);
    majorInfoTreeNodeList = majorInfoTree.getNodesByParamFuzzy(keyType, value);
    updatemajorInfoTreeNodes(majorInfoTreeNodeList);
}

// 清空查询
function clearSearchmajorInfoTreeNode(){
    // 清空查询内容
    $('#majorInfoTreeKeyWord').val('');
    showmajorInfoTreeAllNode(majorInfoTree.getNodes());
}

//隐藏所有节点
function hidemajorInfoTreeAllNode(nodes){
    nodes = majorInfoTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        majorInfoTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showmajorInfoTreeAllNode(nodes){
    nodes = majorInfoTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        // if(nodes[i].getParentNode()!=null){
        //     majorInfoTree.expandNode(nodes[i],false,false,false,false);
        // }else{
        //     majorInfoTree.expandNode(nodes[i],true,true,false,false);
        // }
        majorInfoTree.expandNode(nodes[i],true,true,false,false);
        majorInfoTree.showNode(nodes[i]);
        showmajorInfoTreeAllNode(nodes[i].children);
    }
}

//更新节点状态
function updatemajorInfoTreeNodes(nodeList) {
    if(nodeList!=null&&nodeList.length>0) {
        majorInfoTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {
            //展开当前节点的父节点
            majorInfoTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                majorInfoTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                majorInfoTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            majorInfoTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            majorInfoTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

//表单校验
function initMajorFormValidator() {
    // 设置初始化校验
    ae_major_form_validator = $("#ae_major_form").validate({
        // 定义校验规则
        rules: {
            majorName: {
                required: true,
                maxlength: 200
            },
            majorCode:{
                required: true,
                maxlength: 200,
                remote: {
                    url: ctx+'/supermarket/major/checkUniqueMajorTypeCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        majorId: function(){
                            return $("#ae_major_form input[name='majorId']").val();
                        },
                        majorCode: function() {
                            return $("#ae_major_form input[name='majorCode']").val();
                        }
                    }
                }
            },
            memo: {
                maxlength: 2000
            },
        },
        messages: {
            majorName: {
                required: '此项必填!',
                maxlength: "长度不能超过200个字母!"
            },
            majorCode:{
                required: '此项必填!',
                maxlength: "长度不能超过200个字母!",
                remote: "编号已存在！",
            },
            memo: {
                maxlength: "长度不能超过2000个字母!"
            },
        },
        // 提交表单
        submitHandler: function(form){
            var d = {};
            var t = $('#ae_major_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx + '/supermarket/major/saveAeaImServiceMajor.do',
                type: 'POST',
                data: d,
                success: function (result) {
                    if (result.success&&result.content){
                        $('#ae_major_modal').modal('hide');
                        loadMajorInfoTreeData();
                        swal({
                            title: '提示信息',
                            text: '操作成功!',
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
}
//新增专业类别
function addServiceMajor() {
    hideRightMenu();
    $('#ae_major_modal').modal('show');
    $('#ae_major_modal_title').html('新增专业类别');
    clearMajorForm();
    ae_major_form_validator.resetForm();
    var parentId = majorInfoTreeRightClickSelectNode.id;
    $('#parentMajorId').val(parentId);
    $('#majorQualId').val(currQualId);
}
//编辑专业类别
function editServiceMajor() {
    hideRightMenu();
    if(majorInfoTreeRightClickSelectNode.pId == 0){
        swal({
            title: '提示信息',
            text: '根节点不能编辑!',
            type: 'success',
            timer: 1000,
            showConfirmButton: false
        });
        return;
    }
    $('#ae_major_modal').modal('show');
    $('#ae_major_modal_title').html('编辑专业类别');
    clearMajorForm();
    $.ajax({
        url: ctx + '/supermarket/major/getAeaImServiceMajor.do',
        type: 'POST',
        data: {'id': majorInfoTreeRightClickSelectNode.id},
        async:false,
        success: function (data) {
            loadFormData(true, '#ae_major_form', data);
        },
        error:function(){
            swal('错误信息', "保存信息失败！", 'error');
        }
    });
}
//删除专业类别
function deleteServiceMajor(delChildren) {
    hideRightMenu();
    if(majorInfoTreeRightClickSelectNode.pId == 0){
        swal({
            title: '提示信息',
            text: '根节点不能删除!',
            type: 'success',
            timer: 1000,
            showConfirmButton: false
        });
        return;
    }
    var tip = '此操作将删除：'+ majorInfoTreeRightClickSelectNode.name +',您确定删除吗?';
    if(delChildren && delChildren =='1'){
        tip = '此操作将删除：'+ majorInfoTreeRightClickSelectNode.name +'及其所有子专业类别,您确定删除吗?';
    }
    swal({
        title: '提示信息',
        text: tip,
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
    }).then(function(result) {
        if (result.value) {
            $.ajax({
                url: ctx+'/supermarket/major/deleteAeaImServiceMajorById.do',
                type: 'POST',
                data:{
                    'id': majorInfoTreeRightClickSelectNode.id,
                    'delChildren':delChildren
                },
                success: function (result){
                    if(result.success){
                        swal({
                            title: '提示信息',
                            text: '删除成功!',
                            type: 'success',
                            timer: 1000,
                            showConfirmButton: false
                        });
                        // 刷新
                        loadMajorInfoTreeData();
                    }else{
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', '操作失败！', 'error');
                }
            });
        }
    });
}

//清空表单
function clearMajorForm() {
    $('#majorId').val('');
    $('#parentMajorId').val('');
    $('#majorQualId').val('');
    $('#ae_major_form')[0].reset();
}
//隐藏右键菜单
function hideRightMenu() {
    hideRMenu('rootContextMenu');
    hideRMenu('serviceMajorContextMenu');
}

