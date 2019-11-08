<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">

    .condStateElText {

        width: 100%;
        min-height: 401px;
        max-height: 401px;
        border: 1px solid #e8e8e8;
        font-size: 12px;
        word-wrap: break-word;
        overflow-x: hidden;
        overflow-y: auto;
        _overflow-y: visible;
    }

    .ztree li span.button.chk {
        width: 13px;
        height: 13px;
        margin: 0px 3px 0 3px;
        cursor: auto;
    }

</style>

<!-- 选择条款字段-->
<div id="select_clause_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_clause_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_clause_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px 0px;">
                <div style="width: 100%;height: 100%; padding: 5px;">
                    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
                        <div class="col-xl-12" style="padding: 0px 2px 0px 8px;">
                            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption">
                                        <div class="m-portlet__head-title">
                                           <span class="m-portlet__head-icon m--hide">
                                               <i class="la la-gear"></i>
                                           </span>
                                            <h3 class="m-portlet__head-text">
                                                法律法规条款库
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 5px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectClauseKeyWord" type="text" class="form-control m-input m-input--solid empty"
                                                   placeholder="请输入关键字..." style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info" onclick="searchSelectClauseNode();">查询</button>
                                            <button type="button" class="btn btn-secondary" onclick="clearSearchSelectClauseNode();">清空</button>
                                            <button type="button" class="btn btn-secondary" onclick="selectClauseTreeexpandAll();">展开</button>
                                            <button type="button" class="btn btn-secondary" onclick="selectClauseTreecollapseAll();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectClauseTree" class="ztree" style="height: 380px;overflow: auto;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectClauseBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectClauseTree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
var selectClauseKeyWord,
selectClauseTreeNodeList = [],
selectClauseTreeLastValue = "",
selectClauseTree = null,
selectClauseTreeSetting = {
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
    check: {
        enable: true,     //这里设置是否显示复选框
    },
    //用于捕获节点被点击的事件回调函数
    callback: {
        onCheck: onSelectClauseCheck,
        onClick: onClickSelectClauseNode,
    }
};

function onSelectClauseCheck(event, treeId, treeNode){

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode&&treeNode.type=='clause'){
        if(treeNode.checked){
            treeObj.selectNode(treeNode);
        }else{
            treeObj.cancelSelectedNode(treeNode);
        }
    }
}

function onClickSelectClauseNode(event, treeId, treeNode) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    if(treeNode&&treeNode.type=='clause'){
        if(treeNode.checked){
            treeObj.checkNode(treeNode,false,false,true);
        }else{
            treeObj.checkNode(treeNode,true,false,true);
        }
    }
}

//法律法规树
function initSelectClauseTree(){

    $.ajax({
        url: ctx+'/aea/service/legal/getListLegalZtreeNode.do',
        type:'post',
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                selectClauseTree = $.fn.zTree.init($("#selectClauseTree"), selectClauseTreeSetting , data);
            }
        },
        error: function(){
            swal('错误信息', '初始化法律法规条款库异常!', 'error');
        }
    });
}

    // 初始化加载函数
    $(function(){

        // 设置弹窗大小
        $('#select_clause_ztree_modal .modal-lg').css('max-width',$('#mainContentPanel').width()*0.60);

        $("#selectClauseTree").niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        // 关键字搜索事项节点
        selectClauseKeyWord = $("#selectClauseKeyWord");
        selectClauseKeyWord.bind("focus", focusSelectClauseKey).bind("blur", blurSelectClauseKey).bind("change cut input propertychange",searchSelectClauseNode);
        selectClauseKeyWord.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectClauseNode();
            }
        });

        // 选择节点
        $('#selectClauseBtn').click(function(){
            var treeObj = $.fn.zTree.getZTreeObj("selectClauseTree");
            selectClauseTreeNodeList = treeObj.getCheckedNodes(true);
            var d = {};
            var t = $('#service_basic_form').serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            d['tableName'] = relTbName;
            d['pkName'] = relPkName;
            d['recordId'] = recordId;
            d.legalClauseId="";
            for(var i=0;i<selectClauseTreeNodeList.length;i++){
                if(i>0){
                    d.legalClauseId += ",";
                }
                d.legalClauseId += selectClauseTreeNodeList[i].id;
            }
            if(selectClauseTreeNodeList!=null&&selectClauseTreeNodeList.length>0){
                $.ajax({
                    url: ctx + '/aea/item/service/basic/batchSaveServiceBasic.do',
                    type: 'POST',
                    data: d,
                    async: false,
                    success: function (result) {
                        if (result.success){
                            $('#select_clause_ztree_modal').modal('hide');
                            swal({
                                title: '提示信息',
                                text: '导入成功!',
                                type: 'success',
                                timer: 1500,
                                showConfirmButton: false
                            });
                            searchCond();
                        }else {
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error: function () {
                        swal('错误信息', "保存类型信息失败！", 'error');
                    }
                });
            }else{
                swal('提示信息','请先选择条款','info');
            }
        });
    });

    function focusSelectClauseKey(e) {

        if (selectClauseKeyWord.hasClass("empty")) {
            selectClauseKeyWord.removeClass("empty");
        }
    }

    function blurSelectClauseKey(e) {

        if (selectClauseKeyWord.get(0).value === "") {
            selectClauseKeyWord.addClass("empty");
        }
        searchSelectClauseNode();
    }

    //全部展开
    function selectClauseTreeexpandAll(){

        selectClauseTree.expandAll(true);
    }

    //全部折叠
    function selectClauseTreecollapseAll(){

        selectClauseTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectClauseNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectClauseKeyWord').val());
        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectClauseTreeLastValue === value) {
            return;
        }

        // 保存最后一次
        selectClauseTreeLastValue = value;

        var nodes = selectClauseTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectClauseAllNode(nodes);
            return;
        }
        hideSelectClauseAllNode(nodes);
        selectClauseTreeNodeList = selectClauseTree.getNodesByParamFuzzy(keyType, value);
        updateSelectClauseNodes(selectClauseTreeNodeList);
    }

    // 清空查询
    function clearSearchSelectClauseNode(){

        // 清空查询内容
        $('#selectClauseKeyWord').val('');
        showSelectClauseAllNode(selectClauseTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectClauseAllNode(nodes){

        nodes = selectClauseTree.transformToArray(nodes);
        for (var i = nodes.length - 1; i >= 0; i--) {
            selectClauseTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectClauseAllNode(nodes){

        nodes = selectClauseTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectClauseTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectClauseTree.expandNode(nodes[i],true,true,false,false);
            }
            selectClauseTree.showNode(nodes[i]);
            showSelectClauseAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectClauseNodes(nodeList) {

        if (nodeList != null && nodeList.length > 0) {
            selectClauseTree.showNodes(nodeList);
            for (var i = 0, l = nodeList.length; i < l; i++) {
                //展开当前节点的父节点
                selectClauseTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while (nodeList[i].getParentNode() != null) {
                    selectClauseTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectClauseTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectClauseTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectClauseTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择表字段
    function openSelectTree(curIsEditable){

        if(!curIsEditable){
            swal('提示信息', '当前事项版本下数据不可编辑!', 'info');
            return;
        }
        $('#select_clause_ztree_modal').modal('show');
        $('#select_clause_ztree_modal_title').html('选择条款');
        $('#selectClauseKeyWord').val('');
        //初始化条款分类树
        initSelectClauseTree();
        //延迟加载已选择的法律法规
        setTimeout(function(){
            loadSelectedClauseData();
        },500);
    }

    //选择默认勾选的法律法规
    function loadSelectedClauseData(){

        $.ajax({
            url: ctx + '/aea/item/service/basic/listAeaItemServiceBasicByRecordId.do',
            type: 'post',
            data: {
                'tableName': relTbName,
                'pkName': relPkName,
                'recordId': recordId
            },
            async: false,
            success: function (data) {
                if(data!=null&&data.length>0){
                    for(var i=0;i<data.length;i++) {
                        // 选择事项库树节点
                        var node = selectClauseTree.getNodeByParam("id", data[i].legalClauseId);
                        if (node) {
                            selectClauseTree.checkNode(node, true, true, false);
                        }
                    }
                }
            }
        });
    }

    // 关闭选择表字段
    function closeSelectClauseTree(){

        $('#select_clause_ztree_modal').modal('hide');
    }

</script>