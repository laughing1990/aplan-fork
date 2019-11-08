<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .selectedUserSortDiv {
        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .selectedUserSortDiv .block_list-title {
        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .selectedUserSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .selectedUserSortDiv li {
        background-color: #fff;
        padding: 6px 0px;
        display: list-User;
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

    .selectedUserSortDiv .drag-handle_th {
        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .selectedUserSortDiv .drag-handle_td {
        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    .selectedUserSortDiv .ostage_name_td{
        display: inline-block;
        width: 45%;
    }
</style>

<!-- 选择事 -->
<div id="select_user_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_user_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_user_ztree_modal_title"></h5>
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
                                                组织用户
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectUserKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty"
                                                   placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchSelectUserNode();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchSelectUserNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandSelectUserAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseSelectUserAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectUserTreeDiv" style="height:380px;overflow:auto;">
                                        <ul id="selectUserTree" class="ztree"></ul>
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
                                            <h3 id="selectedUserHeadTitle" class="m-portlet__head-text">
                                                已选用户
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div id="selectedUserDiv" style="height: 435px;overflow-y:auto;overflow-x:auto;">
                                        <div class="selectedUserSortDiv">
                                            <ul id="selectedUserUl" class="block_ul_td"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectUserBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectUserZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    // 选择用户树配置
    var selectUserKey,
        selectUserNodeList = [],
        selectUserLastValue = "",
        selectUserTree = null,
        selectUserTreeSetting = {
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
                onCheck: onSelectUserCheck,
                onClick: onClickSelectUserNode,
            }
        };

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectUserCheck(event, treeId, treeNode){

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode&&treeNode.type=='user'){
            // 选中
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectUserLi" category-id="'+treeNode.id+'">' +
                                '<span class="drag-handle_td" onclick="removeSelectedUser(\''+treeNode.id+'\');">×</span>' +
                                '<span class="org_name_td">'+ treeNode.name +'</span>' +
                            '</li>';
                var i=0;
                $('li[name="selectUserLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId == treeNode.id){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedUserUl').append(liHtml);
                }
                // 取消
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectUserLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId == treeNode.id){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }

    function onClickSelectUserNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode && treeNode.type == 'user') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectUserLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.id) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectUserLi" category-id="'+ treeNode.id + '">' +
                                '<span class="drag-handle_td" onclick="removeSelectedUser(\'' + treeNode.id + '\');">×</span>' +
                                '<span class="org_name_td">' + treeNode.name+ '</span>' +
                             '</li>';
                var i = 0;
                $('li[name="selectUserLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.id) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedUserUl').append(liHtml);
                }
            }
        }
    }

    // 初始化加载函数
    $(function(){

        $('#selectUserTreeDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#selectedUserDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        // 关键字搜索用户节点
        selectUserKey = $("#selectUserKeyWord");
        selectUserKey.bind("focus", focusSelectUserKey)
                     .bind("blur", blurSelectUserKey)
                     .bind("change cut input propertychange",searchSelectUserNode);
        selectUserKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectUserNode();
            }
        });
    });

    function focusSelectUserKey(e) {

        if (selectUserKey.hasClass("empty")) {
            selectUserKey.removeClass("empty");
        }
    }

    function blurSelectUserKey(e) {

        if (selectUserKey.get(0).value === "") {
            selectUserKey.addClass("empty");
        }
        searchSelectUserNode(e);
    }

    // 删除选中节点
    function removeSelectedUser(userId){

        $('li[name="selectUserLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            if(categoryId == userId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectUserTree.cancelSelectedNode();
        var node = selectUserTree.getNodeByParam("id", userId, null);
        if (node) {
            selectUserTree.checkNode(node, false, true, false);
        }
    }

    function expandNodes(nodes) {

        if (!nodes) return;
        var zTree = $.fn.zTree.getZTreeObj("selectUserTree");
        for (var i=0;i<nodes.length;i++) {
            zTree.expandNode(nodes[i], true, false, false);
            if (nodes[i].isParent) {
                expandNodes(nodes[i].children);//递归
            }
        }
    }

    //全部展开
    function expandSelectUserAllNode(){

        selectUserTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectUserAllNode(){

        selectUserTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectUserNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectUserKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectUserLastValue === value) {
            return;
        }

        // 保存最后一次
        selectUserLastValue = value;

        var nodes = selectUserTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectUserAllNode(nodes);
            return;
        }
        hideSelectUserAllNode(nodes);
        selectUserNodeList = selectUserTree.getNodesByParamFuzzy(keyType, value);
        updateSelectUserNodes(selectUserNodeList);
    }

    // 清空查询
    function clearSearchSelectUserNode(){

        // 清空查询内容
        $('#selectUserKeyWord').val('');
        showSelectUserAllNode(selectUserTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectUserAllNode(nodes){

        nodes = selectUserTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectUserTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectUserAllNode(nodes){

        nodes = selectUserTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectUserTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectUserTree.expandNode(nodes[i],true,true,false,false);
            }
            selectUserTree.showNode(nodes[i]);
            showSelectUserAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectUserNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectUserTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectUserTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectUserTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectUserTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectUserTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectUserTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择用户弹窗
    function openSelectUserModal(){

        $('#select_user_ztree_modal').modal('show');
        $('#select_user_ztree_modal_title').html('窗口人员授权');
        initSelectUserZtree();

        // 清空关键字据
        $('#selectUserKeyWord').val('');

        // 清空右侧选中用户数据
        $("#selectedUserUl").html("");

        // 取消上次的选中节点
        selectUserTree.checkAllNodes(false);
    }

    // 关闭选择用户弹窗
    function closeSelectUserZtree(){

        $('#select_user_ztree_modal').modal('hide');
    }

    // 加载选择用户数据
    function initSelectUserZtree(){

        $.ajax({
            url: ctx+'/aea/service/window/listAllUserByOrgId.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectUserTree)selectUserTree.destroy();
                    selectUserTree = $.fn.zTree.init($("#selectUserTree"), selectUserTreeSetting,data);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }
</script>