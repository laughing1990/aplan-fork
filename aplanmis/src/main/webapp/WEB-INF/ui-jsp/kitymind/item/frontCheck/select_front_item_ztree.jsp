<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css">

    .selectedCertSortDiv {
        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .selectedCertSortDiv .block_list-title {
        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .selectedCertSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .selectedCertSortDiv li {
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

    .selectedCertSortDiv .drag-handle_th {
        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .selectedCertSortDiv .drag-handle_td {
        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    .selectedCertSortDiv .ostage_name_td{
        display: inline-block;
        width: 45%;
    }
</style>

<div id="cert_import_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="cert_import_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_cert_ztree_modal_title"></h5>
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
                                            <h3 class="m-portlet__head-text">工程建设审批事项库</h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectItemKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
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
                                    <div id="selectCertTree" class="ztree" style="height: 380px;overflow: auto;"></div>
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
                                    <div style="height: 435px; overflow-y:auto; overflow-x:auto;">
                                        <div class="selectedFrontCheckItemDiv">
                                            <ul id="selectedFrontCheckItemUl" class="block_ul_td"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectedFrontCheckItemBtn" type="button" class="btn btn-info">保存</button>&nbsp;&nbsp;
                <button type="button" class="btn btn-secondary" onclick="certDialogClose();">关闭</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var selectCertKey,
        selectCertNodeList = [],
        selectCertLastValue = "",
        selectCertTree = null,
        selectCertTreeSetting = {
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

    // 初始化加载函数
    $(function(){

        $('#selectCertTree').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#selectedCertSortDiv').niceScroll({

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
        selectCertKey = $("#selectCertKeyWord");
        selectCertKey.bind("focus", focusSelectCertKey)
            .bind("blur", blurSelectCertKey)
            .bind("change cut input propertychange",searchSelectCertNode);
        selectCertKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectCertNode();
            }
        });
    });

    function focusSelectCertKey(e) {

        if (selectCertKey.hasClass("empty")) {
            selectCertKey.removeClass("empty");
        }
    }

    function blurSelectCertKey(e) {

        if (selectCertKey.get(0).value === "") {
            selectCertKey.addClass("empty");
        }
        searchSelectCertNode(e);
    }

    function certImportModelClose() {
        $('#cert_import_modal').modal('hide');
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

    function certDialogClose() {

        $('#cert_import_modal').modal('hide');
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

</script>