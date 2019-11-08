<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 选择材料类型定义-->
<div id="select_mat_type_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_mat_type_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 600px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_mat_type_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectMatTypeKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info" onclick="searchSelectMatTypeNode();">查询</button>
                        <button type="button" class="btn btn-secondary" onclick="clearSearchSelectMatTypeNode();">清空</button>
                        <button type="button" class="btn btn-secondary" onclick="expandSelectMatTypeAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary" onclick="collapseSelectMatTypeAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="selectMatTypeTree" class="ztree" style="height: 380px;overflow-y:auto;overflow-x:auto;"></div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectMatTypeBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectMatTypeZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

// 选择材料类型树配置
var selectMatTypeKey,
    selectMatTypeNodeList = [],
    selectMatTypeLastValue = "",
    selectMatTypeTree = null,
    selectMatTypeTreeSetting = {
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
            onDblClick: onDblClickSelectMatTypeNode
        }
    };

    // 初始化加载函数
    $(function(){

        // 关键字搜索材料类型节点
        selectMatTypeKey = $("#selectMatTypeKeyWord");
        selectMatTypeKey.bind("focus", focusSelectMatTypeKey).bind("blur", blurSelectMatTypeKey).bind("change cut input propertychange",searchSelectMatTypeNode);
        selectMatTypeKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectMatTypeNode();
            }
        });

    });

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

    function onDblClickSelectMatTypeNode(event, treeId, treeNode) {
        $("#selectMatTypeBtn").trigger("click");
    }

    //全部展开
    function expandSelectMatTypeAllNode(){
        selectMatTypeTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectMatTypeAllNode(){
        selectMatTypeTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectMatTypeNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectMatTypeKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectMatTypeLastValue === value) {
            return;
        }

        // 保存最后一次
        selectMatTypeLastValue = value;

        var nodes = selectMatTypeTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectMatTypeAllNode(nodes);
            return;
        }
        hideSelectMatTypeAllNode(nodes);
        selectMatTypeNodeList = selectMatTypeTree.getNodesByParamFuzzy(keyType, value);
        updateSelectMatTypeNodes(selectMatTypeNodeList);
    }

    // 清空查询
    function clearSearchSelectMatTypeNode(){

        // 清空查询内容
        $('#selectMatTypeKeyWord').val('');
        showSelectMatTypeAllNode(selectMatTypeTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectMatTypeAllNode(nodes){

        nodes = selectMatTypeTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectMatTypeTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectMatTypeAllNode(nodes){

        nodes = selectMatTypeTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectMatTypeTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectMatTypeTree.expandNode(nodes[i],true,true,false,false);
            }
            selectMatTypeTree.showNode(nodes[i]);
            showSelectMatTypeAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectMatTypeNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectMatTypeTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectMatTypeTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectMatTypeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectMatTypeTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectMatTypeTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectMatTypeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择材料类型弹窗
    var matTypeShowStyle = null;
    function openSelectMatTypeModal(showStyle){

        matTypeShowStyle = showStyle;
        $('#select_mat_type_ztree_modal').modal('show');
        $('#select_mat_type_ztree_modal_title').html('选择材料类型');
        loadSelectMatTypeZtreeData();
        expandSelectMatTypeAllNode();
    }

    // 关闭选择材料类型弹窗
    function closeSelectMatTypeZtree(){

        $('#select_mat_type_ztree_modal').modal('hide');
    }

    // 加载选择材料类型数据
    function loadSelectMatTypeZtreeData(){

        $.ajax({
            url: ctx+'/aea/item/mat/type/getListMatTypeZtreeNode.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectMatTypeTree)selectMatTypeTree.destroy();
                    selectMatTypeTree = $.fn.zTree.init($("#selectMatTypeTree"), selectMatTypeTreeSetting , data);
                }
            },
            error: function(){
                swal('错误信息', '初始化材料类型数据异常!', 'error');
            }
        });
    }
</script>