<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .selectedItemSortDiv {
        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .selectedItemSortDiv .block_list-title {
        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .selectedItemSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .selectedItemSortDiv li {
        background-color: #fff;
        padding: 6px 0px;
        display: list-item;
        text-align: -webkit-match-parent;

        /**边框样式**/
        border: 1px solid transparent;
        border-bottom: 1px solid #ebebeb;
    }

    /**
        * 移动到li样式
    */
    .block_ul_td li:hover {
        background: #e7e8eb;
        cursor: move;
        cursor: -webkit-grabbing;
    }

    .selectedItemSortDiv .drag-handle_th {
        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .selectedItemSortDiv .drag-handle_td {
        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    .selectedItemSortDiv .ostage_name_td{
        display: inline-block;
        width: 45%;
    }
</style>

<!-- 选择事 -->
<div id="select_item_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_item_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_item_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body"  style="padding: 5px 0px;">
                <div style="width: 100%;height: 100%; padding: 5px;">
                    <div class="row" style="width: 100%;height: 100%;margin: 0px;">
                        <div class="col-xl-6" style="padding: 0px 2px 0px 8px;">
                            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption">
                                        <div class="m-portlet__head-title">
                                           <span class="m-portlet__head-icon m--hide">
                                               <i class="la la-gear"></i>
                                           </span>
                                            <h3 class="m-portlet__head-text">
                                                工程建设审批事项库
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectItemKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty"
                                                   placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchSelectItemNode();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchSelectItemNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandSelectItemAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseSelectItemAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectItemTreeDiv" style="height:380px;overflow:auto;">
                                        <ul id="selectItemTree" class="ztree"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-6" style="padding: 0px 8px 0px 2px;">
                            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                <div class="m-portlet__head">
                                    <div class="m-portlet__head-caption">
                                        <div class="m-portlet__head-title">
                                           <span class="m-portlet__head-icon m--hide">
                                               <i class="la la-gear"></i>
                                           </span>
                                            <h3 id="selectedItemHeadTitle" class="m-portlet__head-text">
                                                已选事项
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div id="selectedItemDiv" style="height: 435px;overflow-y:auto;overflow-x:auto;">
                                        <div class="selectedItemSortDiv">
                                            <ul id="selectedItemUl" class="block_ul_td"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectItemBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectItemZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    // 选择事项树配置
    var selectItemKey,
        selectItemNodeList = [],
        selectItemLastValue = "",
        selectItemTree = null,
        selectItemTreeSetting = {
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
                onCheck: onSelectItemCheck,
                onClick: onClickSelectItemNode,
            }
        };

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectItemCheck(event, treeId, treeNode){

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode&&treeNode.type=='item'){
            // 选中
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectItemLi" category-id="'+treeNode.itemVerId+'">' +
                                '<span class="drag-handle_td" onclick="removeSelectedItem(\''+treeNode.itemVerId+'\');">×</span>' +
                                '<span class="org_name_td">'+ treeNode.name +'</span>' +
                            '</li>';
                var i=0;
                $('li[name="selectItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId == treeNode.itemVerId){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedItemUl').append(liHtml);
                }
                // 取消
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectItemLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId == treeNode.itemVerId){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }

    function onClickSelectItemNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode && treeNode.type == 'item') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.itemVerId) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectItemLi" category-id="'+ treeNode.itemVerId + '">' +
                                '<span class="drag-handle_td" onclick="removeSelectedItem(\'' + treeNode.itemVerId + '\');">×</span>' +
                                '<span class="org_name_td">' + treeNode.name+ '</span>' +
                             '</li>';
                var i = 0;
                $('li[name="selectItemLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.itemVerId) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedItemUl').append(liHtml);
                }
            }
        }
    }

    // 初始化加载函数
    $(function(){

        $('#selectItemTreeDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#selectedItemDiv').niceScroll({

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
        selectItemKey = $("#selectItemKeyWord");
        selectItemKey.bind("focus", focusSelectItemKey)
                     .bind("blur", blurSelectItemKey)
                     .bind("change cut input propertychange",searchSelectItemNode);
        selectItemKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectItemNode();
            }
        });
    });

    function focusSelectItemKey(e) {

        if (selectItemKey.hasClass("empty")) {
            selectItemKey.removeClass("empty");
        }
    }

    function blurSelectItemKey(e) {

        if (selectItemKey.get(0).value === "") {
            selectItemKey.addClass("empty");
        }
        searchSelectItemNode(e);
    }

    // 删除选中节点
    function removeSelectedItem(itemVerId){

        $('li[name="selectItemLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            if(categoryId==itemVerId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectItemTree.cancelSelectedNode();
        var node = selectItemTree.getNodeByParam("itemVerId", itemVerId, null);
        if (node) {
            selectItemTree.checkNode(node, false, true, false);
        }
    }

    function expandNodes(nodes) {

        if (!nodes) return;
        var zTree = $.fn.zTree.getZTreeObj("selectItemTree");
        for (var i=0;i<nodes.length;i++) {
            zTree.expandNode(nodes[i], true, false, false);
            if (nodes[i].isParent) {
                expandNodes(nodes[i].children);//递归
            }
        }
    }

    //全部展开
    function expandSelectItemAllNode(){
        selectItemTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectItemAllNode(){
        selectItemTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectItemNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectItemKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectItemLastValue === value) {
            return;
        }

        // 保存最后一次
        selectItemLastValue = value;

        var nodes = selectItemTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectItemAllNode(nodes);
            return;
        }
        hideSelectItemAllNode(nodes);
        selectItemNodeList = selectItemTree.getNodesByParamFuzzy(keyType, value);
        updateSelectItemNodes(selectItemNodeList);
    }

    // 清空查询
    function clearSearchSelectItemNode(){

        // 清空查询内容
        $('#selectItemKeyWord').val('');
        showSelectItemAllNode(selectItemTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectItemAllNode(nodes){

        nodes = selectItemTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectItemTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectItemAllNode(nodes){

        nodes = selectItemTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectItemTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectItemTree.expandNode(nodes[i],true,true,false,false);
            }
            selectItemTree.showNode(nodes[i]);
            showSelectItemAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectItemNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectItemTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectItemTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectItemTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectItemTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择事项弹窗
    function openSelectItemModal(){

        $('#select_item_ztree_modal').modal('show');
        $('#select_item_ztree_modal_title').html('选择事项');
        initSelectItemZtree();

        // 清空关键字据
        $('#selectItemKeyWord').val('');

        // 清空右侧选中事项数据
        $("#selectedItemUl").html("");

        // 取消上次的选中节点
        selectItemTree.checkAllNodes(false);
    }

    // 关闭选择事项弹窗
    function closeSelectItemZtree(){

        $('#select_item_ztree_modal').modal('hide');
    }

    // 加载选择事项数据
    function initSelectItemZtree(){

        $.ajax({
            url: ctx+'/aea/item/gtreeTestRunOrPublishedItem.do',
            type:'post',
            data:{'isCatalog': '0'},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectItemTree)selectItemTree.destroy();
                    selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting,data);
                }
            },
            error: function(){
                swal('错误信息', '初始化事项数据异常!', 'error');
            }
        });
    }
</script>