<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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

<div id="select_solicit_item_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_solicit_item_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_solicit_item_ztree_modal_title">选择事项</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px 0px;">
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
                                            <input id="selectSolicitItemKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info"
                                                    onclick="SearchSelectSolicitItemNode();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchSelectSolicitItemNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandSelectSolicitItemAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseSelectSolicitItemAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectSolicitItemTree" class="ztree" style="height: 380px; overflow: auto;"></div>
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
                                            <h3 class="m-portlet__head-text">
                                                已选事项
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div id="selectedSolicitItemCheckDiv" class="selectedItemSortDiv" style="height: 435px;overflow: auto;">
                                        <ul id="selectedSolicitItemCheckUl" class="block_ul_td"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectedSolicitItemCheckBtn" type="button" class="btn btn-info">保存</button>&nbsp;&nbsp;
                <button type="button" class="btn btn-secondary" onclick="closeSelectSolicitItemZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    // 选择事项树配置
    var selectSolicitItemKey,
        selectSolicitItemNodeList = [],
        selectSolicitItemLastValue = "",
        selectSolicitItemTree = null,
        selectSolicitItemTreeSetting = {
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
                selectedMulti: false,//设置是否允许同时选中多个节点
                showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
                showLine: true, //设置 zTree 是否显示节点之间的连线。
                showHorizontal: false//设置是否水平平铺树（自定义属性）

            },
            //用于捕获节点被点击的事件回调函数
            callback: {

                onCheck: onSelectSolicitItemCheck,
                onClick: onClickSelectSolicitItemNode,
            }
        };

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectSolicitItemCheck(event, treeId, treeNode){

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode&&treeNode.type=='item'){
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectSolicitItemCheckLi" category-id="'+treeNode.itemId + '*' +treeNode.itemVerId+'">' +
                                 '<span class="drag-handle_td" onclick="removeSelectedSolicitItem(\''+treeNode.itemId+'\');">×</span>' +
                                 '<span class="org_name_td">'+treeNode.name+'</span>' +
                              '</li>';
                var i=0;
                $('li[name="selectSolicitItemCheckLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if(categoryId==treeNode.id){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedSolicitItemCheckUl').append(liHtml);
                }
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectSolicitItemCheckLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    var indexOfEnd = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0,indexOfEnd);
                    if(categoryId==treeNode.id){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }

    function onClickSelectSolicitItemNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode && treeNode.type == 'item') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectSolicitItemCheckLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectSolicitItemCheckLi" category-id="' + treeNode.itemId + '*' + treeNode.itemVerId + '">' +
                                '<span class="drag-handle_td" onclick="removeSelectedSolicitItem(\'' + treeNode.itemId + '\');">×</span>' +
                                '<span class="org_name_td">' + treeNode.name + '</span>' +
                             '</li>';
                var i = 0;
                $('li[name="selectSolicitItemCheckLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    var index = categoryId.indexOf("*");
                    categoryId = categoryId.substring(0, index);
                    if (categoryId == treeNode.id) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedSolicitItemCheckUl').append(liHtml);
                }
            }
        }
    }

    function expandSolicitItemAll() {

        var zTree = $.fn.zTree.getZTreeObj("selectSolicitItemTree");
        expandSolicitItemNodes(zTree.getNodes());
    }

    function expandSolicitItemNodes(nodes) {

        if (!nodes) return;
        var zTree = $.fn.zTree.getZTreeObj("selectSolicitItemTree");
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.expandNode(nodes[i], true, false, false);
            if (nodes[i].isParent && nodes[i].zAsync) {
                expandSolicitItemNodes(nodes[i].children);
            }
        }
    }

    // 初始化加载函数
    $(function(){

        $('#selectSolicitItemTree').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#selectedSolicitItemCheckDiv').niceScroll({

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
        selectSolicitItemKey = $("#selectSolicitItemKeyWord");

        selectSolicitItemKey.bind("focus", focusSelectSolicitItemKey)
                            .bind("blur", blurSelectSolicitItemKey)
                            .bind("change cut input propertychange",SearchSelectSolicitItemNode);

        selectSolicitItemKey.bind('keydown', function (e){
            if(e.which == 13){
                SearchSelectSolicitItemNode();
            }
        });

        // Sortable.create(document.getElementById('selectedSolicitItemCheckUl'), {
        //     animation: 150,
        //     onEnd: function (evt) { //拖拽完毕之后发生该事件
        //
        //     }
        // });

        $('#selectedSolicitItemCheckBtn').click(function() {

            var sortNos = [];
            var itemIds = [];
            var liObjs = document.getElementsByName('selectSolicitItemCheckLi');
            for (var i = 0; i < liObjs.length; i++) {
                var categoryId = $(liObjs[i]).attr('category-id');
                itemIds.push(categoryId);
                sortNos.push(i+1);
            }
            swal({
                title: '此操作影响：',
                text: '此操作将重新设置征求事项,您确定要执行吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/solicit/item/batchSaveSolicitItem.do',
                        type: 'POST',
                        data: {
                            'itemIds': itemIds.toString(),
                            'sortNos': sortNos.toString(),
                        },
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    text: '保存事项成功！',
                                    type: 'success',
                                    timer: 1500,
                                    showConfirmButton: false
                                });
                                closeSelectSolicitItemZtree();
                                // 刷新列表
                                searchSolicitItemList();
                            }else{
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {

                            swal('错误信息', XMLHttpRequest.responSeText, 'error');
                        }
                    });
                }
            });
        });
    });

    function initSolicitItemCheck() {

        // 打开弹窗
        $('#select_solicit_item_ztree_modal').modal('show');

        // 初始化树
        initSelectSolicitItemZtree();

        // 加载可选事项数据
        setSolicitItemRelChecks();
    }


    function focusSelectSolicitItemKey(e) {

        if (selectSolicitItemKey.hasClass("empty")) {
            selectSolicitItemKey.removeClass("empty");
        }
    }

    function blurSelectSolicitItemKey(e) {

        if (selectSolicitItemKey.get(0).value === "") {
            selectSolicitItemKey.addClass("empty");
        }
        SearchSelectSolicitItemNode(e);
    }

    // 删除前置检查事项选中节点
    function removeSelectedSolicitItem(itemId){

        $('li[name="selectSolicitItemCheckLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            var indexOfEnd = categoryId.indexOf("*");
            categoryId = categoryId.substring(0, indexOfEnd);
            if(categoryId==itemId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectSolicitItemTree.cancelSelectedNode();
        var node = selectSolicitItemTree.getNodeByParam("id", itemId, null);
        if (node) {
            selectSolicitItemTree.checkNode(node, false, true, false);
        }
    }

    function onDblClickselectSolicitItemNode(event, treeId, treeNode) {

        $("#selectSolicitItemBtn").trigger("click");
    }

    //全部展开
    function expandSelectSolicitItemAllNode(){

        expandSolicitItemAll();
    }

    //全部折叠
    function collapseSelectSolicitItemAllNode(){

        selectSolicitItemTree.expandAll(false);
    }

    // 搜索节点
    function SearchSelectSolicitItemNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectSolicitItemKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectSolicitItemLastValue === value) {
            return;
        }

        // 保存最后一次
        selectSolicitItemLastValue = value;

        var nodes = selectSolicitItemTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectSolicitItemAllNode(nodes);
            return;
        }
        hideSelectSolicitItemAllNode(nodes);
        selectSolicitItemNodeList = selectSolicitItemTree.getNodesByParamFuzzy(keyType, value);
        updateSelectSolicitItemNodes(selectSolicitItemNodeList);
    }

    // 清空查询
    function clearSearchSelectSolicitItemNode(){

        // 清空查询内容
        $('#selectSolicitItemKeyWord').val('');
        showSelectSolicitItemAllNode(selectSolicitItemTree.getNodes());
        setTimeout(function() {
            expandSelectSolicitItemAllNode();
        }, 300);
    }

    //隐藏所有节点
    function hideSelectSolicitItemAllNode(nodes){

        nodes = selectSolicitItemTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectSolicitItemTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectSolicitItemAllNode(nodes){

        nodes = selectSolicitItemTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectSolicitItemTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectSolicitItemTree.expandNode(nodes[i],true,true,false,false);
            }
            selectSolicitItemTree.showNode(nodes[i]);
            showSelectSolicitItemAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectSolicitItemNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectSolicitItemTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectSolicitItemTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectSolicitItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectSolicitItemTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectSolicitItemTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectSolicitItemTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 关闭选择事项弹窗
    function closeSelectSolicitItemZtree(){

        $('#select_solicit_item_ztree_modal').modal('hide');
    }

    // 加载选择事项数据
    function initSelectSolicitItemZtree(){

        $.ajax({
            url: ctx+'/aea/item/gtreeLatestItem.do',
            type:'post',
            data:{'isCatalog': '0'},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectSolicitItemTree)selectSolicitItemTree.destroy();
                    selectSolicitItemTree = $.fn.zTree.init($("#selectSolicitItemTree"), selectSolicitItemTreeSetting, data);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', '初始化事项数据异常,异常信息：'+ XMLHttpRequest.responSeText, 'error');
            }
        });
    }

    //刷新用户事项
    function setSolicitItemRelChecks() {

        // 清空关键字据
        $('#selectSolicitItemKeyWord').val('');

        // 清空右侧选中事项数据
        $("#selectedSolicitItemCheckUl").html("");

        // 取消上次的选中节点
        selectSolicitItemTree.checkAllNodes(false);

        // 取消上次选中节点
        selectSolicitItemTree.cancelSelectedNode();

        loadSelectedSolicitItemData();
    }

    //判断字符是否为空的方法
    function isEmpty(obj){

        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }

    function loadSelectedSolicitItemData(){

        $.ajax({
            url: ctx + '/aea/solicit/item/listAeaSolicitItemRelInfo.do',
            type: 'post',
            data: {},
            async: false,
            success: function (data) {
                if(data!=null&&data.length>0){
                    for(var i=0;i<data.length;i++) {
                        var itemName = data[i].itemName;
                        // 选择事项库树节点
                        var node = selectSolicitItemTree.getNodeByParam("id", data[i].itemId, null);
                        if (node) {
                            selectSolicitItemTree.checkNode(node, true, true, false);
                            itemName = node.name;
                        }else{
                            if(!isEmpty(data[i].isCatalog)){
                                if(data[i].isCatalog=='1'){
                                    itemName = '【标准事项】'+ itemName;
                                    if(!isEmpty(data[i].guideOrgName)){
                                        itemName = itemName +'【'+ data[i].guideOrgName+'】';
                                    }
                                }else{
                                    itemName = '【实施事项】'+ itemName;
                                    if(!isEmpty(data[i].orgName)) {
                                        itemName = itemName + '【' + data[i].orgName + '】';
                                    }
                                }
                            }
                        }
                        // 加载右侧数据，已经选择的事项
                        var liHtml = '<li name="selectSolicitItemCheckLi" category-id="' + data[i].itemId + '*' + data[i].itemVerId + '">' +
                                        '<span class="drag-handle_td" onclick="removeSelectedSolicitItem(\'' + data[i].itemId + '\');">×</span>' +
                                        '<span class="org_name_td">' + itemName +'</span>' +
                                     '</li>';

                        $('#selectedSolicitItemCheckUl').append(liHtml);
                    }
                }
            }
        });
    }

</script>