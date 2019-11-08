<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 选择事项输入输出-->
<div id="select_item_inout_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_item_inout_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 700px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_item_inout_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectItemInoutKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info" onclick="searchSelectItemInoutNode();">查询</button>
                        <button type="button" class="btn btn-danger" onclick="clearSearchSelectItemInoutNode();">清空</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="m-scrollable" data-scrollbar-shown="true" data-scrollable="true" data-max-height="380">
                    <div id="selectItemInoutTree" class="ztree" style="overflow-y:auto;overflow-x:auto;"></div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectItemInoutBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectItemInoutZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

// 选择事项树配置
var selectItemInoutKey,
    selectItemInoutNodeList = [],
    selectItemInoutLastValue = "",
    selectItemInoutTree = null,
    selectItemInoutTreeSetting = {
        edit: {
            enable: false,
            showRemoveBtn: false,//设置是否显示删除按钮
            showRenameBtn: false//设置是否显示编辑名称按钮
        },
        check: {
            enable: false,
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
            onDblClick: onDblClickSelectItemInoutNode
        }
    };

    // 初始化加载函数
    $(function(){

        // 关键字搜索事项节点
        selectItemInoutKey = $("#selectItemInoutKeyWord");
        selectItemInoutKey.bind("focus", focusSelectItemInoutKey).bind("blur", blurSelectItemInoutKey).bind("change cut input propertychange",searchSelectItemInoutNode);
        selectItemInoutKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectItemInoutNode();
            }
        });

    });

    function focusSelectItemInoutKey(e) {

        if (selectItemInoutKey.hasClass("empty")) {
            selectItemInoutKey.removeClass("empty");
        }
    }

    function blurSelectItemInoutKey(e) {

        if (selectItemInoutKey.get(0).value === "") {
            selectItemInoutKey.addClass("empty");
        }
        searchSelectItemInoutNode(e);
    }

    function onDblClickSelectItemInoutNode(event, treeId, treeNode) {

        $("#selectItemInoutBtn").trigger("click");
    }

    //全部展开
    function expandSelectItemInoutAllNode(){

        selectItemInoutTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectItemInoutAllNode(){

        selectItemInoutTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectItemInoutNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectItemInoutKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectItemInoutLastValue === value) {
            return;
        }

        // 保存最后一次
        selectItemInoutLastValue = value;

        var nodes = selectItemInoutTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectItemInoutAllNode(nodes);
            return;
        }
        hideSelectItemInoutAllNode(nodes);
        selectItemInoutNodeList = selectItemInoutTree.getNodesByParamFuzzy(keyType, value);
        updateSelectItemInoutNodes(selectItemInoutNodeList);
    }

    // 清空查询
    function clearSearchSelectItemInoutNode(){

        // 清空查询内容
        $('#selectItemInoutKeyWord').val('');
        showSelectItemInoutAllNode(selectItemInoutTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectItemInoutAllNode(nodes){

        nodes = selectItemInoutTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectItemInoutTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectItemInoutAllNode(nodes){

        nodes = selectItemInoutTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectItemInoutTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectItemInoutTree.expandNode(nodes[i],true,true,false,false);
            }
            selectItemInoutTree.showNode(nodes[i]);
            showSelectItemInoutAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectItemInoutNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectItemInoutTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectItemInoutTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectItemInoutTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectItemInoutTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectItemInoutTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectItemInoutTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择事项输入输出弹窗
    function openSelectItemInoutModal(itemId){

        $('#select_item_inout_ztree_modal').modal('show');
        $('#select_item_inout_ztree_modal_title').html('选择事项输入输出');
        loadSelectItemInoutZtreeData(itemId);
    }

    // 关闭选择事项弹窗
    function closeSelectItemInoutZtree(){

        $('#select_item_inout_ztree_modal').modal('hide');
    }

    // 加载选择事项数据
    function loadSelectItemInoutZtreeData(itemId){

        $.ajax({
            url: ctx+'/aea/item/inout/gtreeAeaItemInoutCascade.do',
            type:'post',
            data:{'itemId':itemId},
            async: false,
            dataType: 'json',
            success: function(data){
                selectItemInoutTree = $.fn.zTree.init($("#selectItemInoutTree"), selectItemInoutTreeSetting , data);
            },
            error: function(){
                swal('错误信息', '初始化事项输入输出异常!', 'error');
            }
        });
    }


</script>