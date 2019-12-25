<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css">

    .selectedSolicitOrgUserSortDiv {
        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .selectedSolicitOrgUserSortDiv .block_list-title {
        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .selectedSolicitOrgUserSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .selectedSolicitOrgUserSortDiv li {
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

    .selectedSolicitOrgUserSortDiv .drag-handle_th {
        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .selectedSolicitOrgUserSortDiv .drag-handle_td {
        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    .selectedSolicitOrgUserSortDiv .ostage_name_td{
        display: inline-block;
        width: 45%;
    }
</style>

<div id="select_solicit_org_user_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_solicit_org_user_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_solicit_org_user_ztree_modal_title">选择征求人员</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 5px 0px;">
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
                                                组织用户
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectSolicitOrgUserKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchSelectSolicitOrgUserNode();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchSelectSolicitOrgUserNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandSelectSolicitOrgUserAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseSelectSolicitOrgUserAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectSolicitOrgUserTree" class="ztree" style="height: 380px; overflow: auto;"></div>
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
                                                已选人员
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div id="selectedCheckSolicitOrgUserDiv" class="selectedSolicitOrgUserSortDiv" style="height: 435px;overflow: auto;">
                                        <ul id="selectedCheckSolicitOrgUserUl" class="block_ul_td"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectedCheckSolicitOrgUserBtn" type="button" class="btn btn-info">保存</button>&nbsp;&nbsp;
                <button type="button" class="btn btn-secondary" onclick="closeSelectSolicitOrgUserZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    // 选择事项树配置
    var selectSolicitOrgUserKey,
        SelectSolicitOrgUserNodeList = [],
        selectSolicitOrgUserLastValue = "",
        selectSolicitOrgUserTree = null,
        selectSolicitOrgUserTreeSetting = {
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

                onCheck: onSelectSolicitOrgUserCheck,
                onClick: onSelectSolicitOrgUserClick,
            }
        };

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectSolicitOrgUserCheck(event, treeId, treeNode){

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode&&treeNode.type=='user'){
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectSolicitOrgUserLi" category-id="'+treeNode.id+'">' +
                                 '<span class="drag-handle_td" onclick="removeSolicitOrgUserSelected(\''+ treeNode.id +'\');">×</span>' +
                                 '<span class="org_name_td">'+treeNode.name+'</span>' +
                             '</li>';
                var i=0;
                $('li[name="selectSolicitOrgUserLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId==treeNode.id){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedCheckSolicitOrgUserUl').append(liHtml);
                }
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectSolicitOrgUserLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId==treeNode.id){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }

    function onSelectSolicitOrgUserClick(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode && treeNode.type == 'user') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectSolicitOrgUserLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.id) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectSolicitOrgUserLi" category-id="' + treeNode.id + '">' +
                                '<span class="drag-handle_td" onclick="removeSolicitOrgUserSelected(\'' + treeNode.id + '\');">×</span>' +
                                '<span class="org_name_td">' + treeNode.name + '</span>' +
                             '</li>';
                var i = 0;
                $('li[name="selectSolicitOrgUserLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.id) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedCheckSolicitOrgUserUl').append(liHtml);
                }
            }
        }
    }

    function expandSolicitOrgUserAll() {

        var zTree = $.fn.zTree.getZTreeObj("selectSolicitOrgUserTree");
        expandSolicitOrgUserNodes(zTree.getNodes());
    }

    function expandSolicitOrgUserNodes(nodes) {

        if (!nodes) return;
        var zTree = $.fn.zTree.getZTreeObj("selectSolicitOrgUserTree");
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.expandNode(nodes[i], true, false, false);
            if (nodes[i].isParent && nodes[i].zAsync) {
                expandSolicitOrgUserNodes(nodes[i].children);
            }
        }
    }

    // 初始化加载函数
    $(function(){

        $('#selectSolicitOrgUserTree').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#selectedCheckSolicitOrgUserDiv').niceScroll({

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
        selectSolicitOrgUserKey = $("#selectSolicitOrgUserKeyWord");

        selectSolicitOrgUserKey.bind("focus", focusSelectSolicitOrgUserKey)
                               .bind("blur", blurSelectSolicitOrgUserKey)
                               .bind("change cut input propertychange",searchSelectSolicitOrgUserNode);

        selectSolicitOrgUserKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectSolicitOrgUserNode();
            }
        });

        Sortable.create(document.getElementById('selectedCheckSolicitOrgUserUl'), {
            animation: 150,
            onEnd: function (evt) { //拖拽完毕之后发生该事件

            }
        });

        $('#selectedCheckSolicitOrgUserBtn').click(function() {

            var solicitOrgId = null;
            if (curSelectSolicitOrg2TreeNode != null) {
                solicitOrgId = curSelectSolicitOrg2TreeNode.id;
                var sortNos = [];
                var userIds = [];
                var liObjs = document.getElementsByName('selectSolicitOrgUserLi');
                for (var i = 0; i < liObjs.length; i++) {
                    var categoryId = $(liObjs[i]).attr('category-id');
                    userIds.push(categoryId);
                    sortNos.push(i+1);
                }
                swal({
                    title: '此操作影响：',
                    text: '此操作将重新设置当前选择的部门征求人员数据,您确定要执行吗?',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(function (result) {
                    if (result.value) {
                        $.ajax({
                            url: ctx + '/aea/solicit/org/user/batchSaveSolicitOrgUser.do',
                            type: 'POST',
                            data: {
                                'userIds': userIds.toString(),
                                'sortNos': sortNos.toString(),
                                'solicitOrgId': solicitOrgId
                            },
                            async: false,
                            success: function (result) {
                                if (result.success) {
                                    swal({
                                        text: '保存成功！',
                                        type: 'success',
                                        timer: 1500,
                                        showConfirmButton: false
                                    });
                                    closeSelectSolicitOrgUserZtree();
                                    // 刷新列表
                                    searchSolicitOrgUserList();
                                }else{
                                    swal('错误信息', result.message, 'error');
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {

                                swal('错误信息', XMLHttpRequest.responseText, 'error');
                            }
                        });
                    }
                });
            } else {
                swal('错误信息', '请选择征求部门节点！', 'error');
            }
        });
    });

    function initSolicitOrgUserCheck() {

        // 打开弹窗
        $('#select_solicit_org_user_ztree_modal').modal('show');

        // 初始化树
        initSelectSolicitOrgUserZtree();

        // 加载可选事项数据
        setSolicitOrgUserChecks();
    }


    function focusSelectSolicitOrgUserKey(e) {

        if (selectSolicitOrgUserKey.hasClass("empty")) {
            selectSolicitOrgUserKey.removeClass("empty");
        }
    }

    function blurSelectSolicitOrgUserKey(e) {

        if (selectSolicitOrgUserKey.get(0).value === "") {
            selectSolicitOrgUserKey.addClass("empty");
        }
        searchSelectSolicitOrgUserNode(e);
    }

    // 删除前置检查事项选中节点
    function removeSolicitOrgUserSelected(userId){

        $('li[name="selectSolicitOrgUserLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            if(categoryId==userId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectSolicitOrgUserTree.cancelSelectedNode();
        var node = selectSolicitOrgUserTree.getNodeByParam("id", userId, null);
        if (node) {
            selectSolicitOrgUserTree.checkNode(node, false, true, false);
        }
    }

    function onDblClickSelectSolicitOrgUserNode(event, treeId, treeNode) {

        $("#selectUserItemBtn").trigger("click");
    }

    //全部展开
    function expandSelectSolicitOrgUserAllNode(){

        expandSolicitOrgUserAll();
    }

    //全部折叠
    function collapseSelectSolicitOrgUserAllNode(){

        selectSolicitOrgUserTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectSolicitOrgUserNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectSolicitOrgUserKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectSolicitOrgUserLastValue === value) {
            return;
        }

        // 保存最后一次
        selectSolicitOrgUserLastValue = value;

        var nodes = selectSolicitOrgUserTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectSolicitOrgUserAllNode(nodes);
            return;
        }
        hideSelectSolicitOrgUserAllNode(nodes);
        SelectSolicitOrgUserNodeList = selectSolicitOrgUserTree.getNodesByParamFuzzy(keyType, value);
        updateSelectSolicitOrgUserNodes(SelectSolicitOrgUserNodeList);
    }

    // 清空查询
    function clearSearchSelectSolicitOrgUserNode(){

        // 清空查询内容
        $('#selectSolicitOrgUserKeyWord').val('');
        showSelectSolicitOrgUserAllNode(selectSolicitOrgUserTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectSolicitOrgUserAllNode(nodes){

        nodes = selectSolicitOrgUserTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectSolicitOrgUserTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectSolicitOrgUserAllNode(nodes){

        nodes = selectSolicitOrgUserTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectSolicitOrgUserTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectSolicitOrgUserTree.expandNode(nodes[i],true,true,false,false);
            }
            selectSolicitOrgUserTree.showNode(nodes[i]);
            showSelectSolicitOrgUserAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectSolicitOrgUserNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectSolicitOrgUserTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectSolicitOrgUserTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectSolicitOrgUserTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectSolicitOrgUserTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectSolicitOrgUserTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectSolicitOrgUserTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 关闭选择事项弹窗
    function closeSelectSolicitOrgUserZtree(){

        $('#select_solicit_org_user_ztree_modal').modal('hide');
    }

    // 加载选择事项数据
    function initSelectSolicitOrgUserZtree(){

        $.ajax({
            url: ctx+'/aea/item/user/gtreeAllUserRelOrgByOrgId.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectSolicitOrgUserTree)selectSolicitOrgUserTree.destroy();
                    selectSolicitOrgUserTree = $.fn.zTree.init($("#selectSolicitOrgUserTree"), selectSolicitOrgUserTreeSetting, data);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', '初始化组织用户数据异常,异常信息：'+ XMLHttpRequest.responseText, 'error');
            }
        });
    }

    //刷新用户事项
    function setSolicitOrgUserChecks() {

        // 清空关键字据
        $('#selectSolicitOrgUserKeyWord').val('');

        // 清空右侧选中事项数据
        $("#selectedCheckSolicitOrgUserUl").html("");

        // 取消上次的选中节点
        selectSolicitOrgUserTree.checkAllNodes(false);

        // 取消上次选中节点
        selectSolicitOrgUserTree.cancelSelectedNode();

        loadSolicitOrgUserSelectedData();
    }

    //判断字符是否为空的方法
    function isEmpty(obj){

        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }

    function loadSolicitOrgUserSelectedData(){

        $.ajax({
            url: ctx+'/aea/solicit/org/user/listAeaSolicitOrgUserRelInfo.do',
            type: 'post',
            data: {},
            async: false,
            success: function (data) {
                if(data!=null&&data.length>0){
                    for(var i=0;i<data.length;i++) {
                        var name = data[i].userName;
                        // 选择事项库树节点
                        var node = selectSolicitOrgUserTree.getNodeByParam("id", data[i].userId, null);
                        if (node) {
                            selectSolicitOrgUserTree.checkNode(node, true, true, false);
                            name = node.name;
                        }
                        // 加载右侧数据，已经选择的事项
                        var liHtml = '<li name="selectSolicitOrgUserLi" category-id="' + data[i].userId + '">' +
                                        '<span class="drag-handle_td" onclick="removeSolicitOrgUserSelected(\'' + data[i].userId + '\');">×</span>' +
                                        '<span class="org_name_td">' + name +'</span>' +
                                     '</li>';

                        $('#selectedCheckSolicitOrgUserUl').append(liHtml);
                    }
                }
            }
        });
    }

</script>