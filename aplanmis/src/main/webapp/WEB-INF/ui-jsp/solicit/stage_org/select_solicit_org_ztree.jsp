<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css">

    .selectSolicitOrgSortDiv {
        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .selectSolicitOrgSortDiv .block_list-title {
        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .selectSolicitOrgSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .selectSolicitOrgSortDiv li {
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

    .selectSolicitOrgSortDiv .drag-handle_th {
        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .selectSolicitOrgSortDiv .drag-handle_td {
        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    .selectSolicitOrgSortDiv .ostage_name_td{
        display: inline-block;
        width: 45%;
    }
</style>

<div id="select_solicit_org_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_solicit_org_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_solicit_org_ztree_modal_title">选择牵头部门</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="import_solicit_org_form" method="post">
                <div class="modal-body" style="padding: 5px 0px;">
                    <div style="width: 100%;height: 100%; padding: 5px;">
                        <div class="row" style="width: 100%;height: 100%;margin: 0px;">
                            <div class="col-xl-7" style="padding: 0px 2px 0px 8px;">
                                <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                    <div class="m-portlet__head">
                                        <div class="m-portlet__head-caption">
                                            <div class="m-portlet__head-title">
                                               <span class="m-portlet__head-icon m--hide">
                                                   <i class="la la-gear"></i>
                                               </span>
                                                <h3 class="m-portlet__head-text">
                                                    组织架构
                                                </h3>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="m-portlet__body" style="padding: 5px 0px;">
                                        <div class="row" style="margin: 0px;">
                                            <div class="col-xl-5">
                                                <input id="selectSolicitOrgKeyWord" type="text"
                                                       class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                                       style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                            </div>
                                            <div class="col-xl-7">
                                                <button type="button" class="btn btn-info"
                                                        onclick="searchSelectSolicitOrgOrgNode();">查询</button>
                                                <button type="button" class="btn btn-secondary"
                                                        onclick="clearSearchSelectSolicitOrgOrgNode();">清空</button>
                                                <button type="button" class="btn btn-secondary"
                                                        onclick="expandSelectSolicitOrgAllNode();">展开</button>
                                                <button type="button" class="btn btn-secondary"
                                                        onclick="collapseSelectSolicitOrgAllNode();">折叠</button>
                                            </div>
                                        </div>
                                        <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                        <div id="selectSolicitOrgDiv" style="height: 380px;overflow: auto;">
                                            <div id="selectSolicitOrgTree" class="ztree"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xl-5" style="padding: 0px 8px 0px 2px;">
                                <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                                    <div class="m-portlet__head">
                                        <div class="m-portlet__head-caption">
                                            <div class="m-portlet__head-title">
                                                   <span class="m-portlet__head-icon m--hide">
                                                       <i class="la la-gear"></i>
                                                   </span>
                                                <h3 class="m-portlet__head-text">
                                                    <font color="red">*&nbsp;</font>征求业务模式:
                                                </h3>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="m-portlet__body" style="padding: 10px 0px;">
                                        <div class="form-group m-form__group row" style="margin-bottom: 10px;">
                                            <div class="col-12">
                                                <label class="m-radio">
                                                    <input type="radio" name="solicitType" value="0">多人征求模式<span></span>
                                                </label>
                                            </div>
                                        </div>
                                        <div class="form-group m-form__group row" style="margin-bottom: 10px;">
                                            <div class="col-12">
                                                <label class="m-radio">
                                                    <input type="radio" name="solicitType" value="1">单人征求模式<span></span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id="selectSolicitOrgCheckBtn" type="submit" class="btn btn-info">保存</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-secondary" onclick="closeSelectSolicitOrgZtree();">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">

    // 选择事项树配置
    var selectSolicitOrgKey,
        selectSolicitOrgNodeList = [],
        selectSolicitOrgLastValue = "",
        selectSolicitOrgTree = null,
        selectSolicitOrgTreeSetting = {
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

                onCheck: onSelectSolicitOrgCheck,
                onClick: onClickSelectSolicitOrgNode,
            }
        };

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectSolicitOrgCheck(event, treeId, treeNode){

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode.checked){
            treeObj.selectNode(treeNode);
            // var liHtml = '<li name="selectSolicitOrgCheckLi" category-id="'+treeNode.id+'">' +
            //                 '<span class="drag-handle_td" onclick="removeSolicitOrgCheck(\''+treeNode.id+'\');">×</span>' +
            //                 '<span class="org_name_td">'+treeNode.name+'</span>' +
            //             '</li>';
            // var i=0;
            // $('li[name="selectSolicitOrgCheckLi"]').each(function(){
            //     var categoryId = $(this).attr('category-id');
            //     if(categoryId==treeNode.id){
            //         i++;
            //         return false;
            //     }
            // });
            // if(i==0){
            //     $('#selectSolicitOrgCheckUl').append(liHtml);
            // }
        }else{
            treeObj.cancelSelectedNode(treeNode);
            // $('li[name="selectSolicitOrgCheckLi"]').each(function(){
            //     var categoryId = $(this).attr('category-id');
            //     if(categoryId==treeNode.id){
            //         $(this).remove();
            //         return false;
            //     }
            // });
        }
    }

    function onClickSelectSolicitOrgNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode.checked) {
            treeObj.checkNode(treeNode, false, false, true);
            // $('li[name="selectSolicitOrgCheckLi"]').each(function () {
            //     var categoryId = $(this).attr('category-id');
            //     if (categoryId == treeNode.id) {
            //         $(this).remove();
            //         return false;
            //     }
            // });
        } else {
            treeObj.checkNode(treeNode, true, false, true);
            // var liHtml = '<li name="selectSolicitOrgCheckLi" category-id="' + treeNode.id + '">' +
            //                 '<span class="drag-handle_td" onclick="removeSolicitOrgCheck(\'' + treeNode.id + '\');">×</span>' +
            //                 '<span class="org_name_td">' + treeNode.name + '</span>' +
            //              '</li>';
            // var i = 0;
            // $('li[name="selectSolicitOrgCheckLi"]').each(function () {
            //     var categoryId = $(this).attr('category-id');
            //     if (categoryId == treeNode.id) {
            //         i++;
            //         return false;
            //     }
            // });
            // if (i == 0) {
            //     $('#selectSolicitOrgCheckUl').append(liHtml);
            // }
        }
    }

    function expandSelectSolicitOrgTreeAll() {

        var zTree = $.fn.zTree.getZTreeObj("selectSolicitOrgTree");
        expandSelectSolicitOrgTreeNodes(zTree.getNodes());
    }

    function expandSelectSolicitOrgTreeNodes(nodes) {

        if (!nodes) return;
        var zTree = $.fn.zTree.getZTreeObj("selectSolicitOrgTree");
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.expandNode(nodes[i], true, false, false);
            if (nodes[i].isParent && nodes[i].zAsync) {
                expandSelectSolicitOrgTreeNodes(nodes[i].children);
            }
        }
    }

    // 初始化加载函数
    $(function(){

        $('#selectSolicitOrgDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        // $('#selectSolicitOrgTree').niceScroll({
        //
        //     cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        //     cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        //     cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        //     touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        //     cursorwidth: "4px", //像素光标的宽度
        //     cursorborder: "0", //   游标边框css定义
        //     cursorborderradius: "2px",//以像素为光标边界半径
        //     autohidemode: true  //是否隐藏滚动条
        // });

        $('#selectSolicitOrgCheckDiv').niceScroll({

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
        selectSolicitOrgKey = $("#selectSolicitOrgKeyWord");

        selectSolicitOrgKey.bind("focus", focusSelectSolicitOrgKey)
                         .bind("blur", blurSelectSolicitOrgKey)
                         .bind("change cut input propertychange",searchSelectSolicitOrgOrgNode);

        selectSolicitOrgKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectSolicitOrgOrgNode();
            }
        });

        // Sortable.create(document.getElementById('selectSolicitOrgCheckUl'), {
        //     animation: 150,
        //     onEnd: function (evt) { //拖拽完毕之后发生该事件
        //
        //     }
        // });

        // $('#selectSolicitOrgCheckBtn').click(function() {
        //
        //     var orgIds = [];
        //     var liObjs = document.getElementsByName('selectSolicitOrgCheckLi');
        //     for (var i = 0; i < liObjs.length; i++) {
        //         var categoryId = $(liObjs[i]).attr('category-id');
        //         orgIds.push(categoryId);
        //     }
        //     swal({
        //         title: '此操作影响：',
        //         text: '此操作将重新设置牵头部门数据,您确定要执行吗?',
        //         type: 'warning',
        //         showCancelButton: true,
        //         confirmButtonText: '确定',
        //         cancelButtonText: '取消',
        //     }).then(function (result) {
        //         if (result.value) {
        //             $.ajax({
        //                 url: ctx+'/aea/solicit/org/batchSaveSolicitOrg.do',
        //                 type: 'POST',
        //                 data: {
        //                     'orgIds': orgIds.toString()
        //                 },
        //                 async: false,
        //                 success: function (result) {
        //                     if (result.success) {
        //                         swal({
        //                             text: '保存成功！',
        //                             type: 'success',
        //                             timer: 1500,
        //                             showConfirmButton: false
        //                         });
        //                         closeSelectSolicitOrgZtree();
        //                         // 刷新列表
        //                         searchSolicitOrgList();
        //                     }else{
        //                         swal('错误信息', result.message, 'error');
        //                     }
        //                 },
        //                 error: function (XMLHttpRequest, textStatus, errorThrown) {
        //
        //                     swal('错误信息', XMLHttpRequest.responseText, 'error');
        //                 }
        //             });
        //         }
        //     });
        // });
    });

    function initSolicitOrgCheck() {

        // 打开弹窗
        $('#select_solicit_org_ztree_modal').modal('show');

        // 初始化树
        initsSelectSolicitOrgZtree();

        // 加载可选事项数据
        setRelCheckSolicitOrgs();
    }


    function focusSelectSolicitOrgKey(e) {

        if (selectSolicitOrgKey.hasClass("empty")) {
            selectSolicitOrgKey.removeClass("empty");
        }
    }

    function blurSelectSolicitOrgKey(e) {

        if (selectSolicitOrgKey.get(0).value === "") {
            selectSolicitOrgKey.addClass("empty");
        }
        searchSelectSolicitOrgOrgNode(e);
    }

    // 删除前置检查事项选中节点
    function removeSolicitOrgCheck(id){

        $('li[name="selectSolicitOrgCheckLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            if(categoryId==id){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectSolicitOrgTree.cancelSelectedNode();
        var node = selectSolicitOrgTree.getNodeByParam("id", id, null);
        if (node) {
            selectSolicitOrgTree.checkNode(node, false, true, false);
        }
    }

    function onDblClickSelectSolicitOrgNode(event, treeId, treeNode) {

        $("#selectSolicitOrgBtn").trigger("click");
    }

    //全部展开
    function expandSelectSolicitOrgAllNode(){

        expandSelectSolicitOrgTreeAll();
    }

    //全部折叠
    function collapseSelectSolicitOrgAllNode(){

        selectSolicitOrgTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectSolicitOrgOrgNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectSolicitOrgKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectSolicitOrgLastValue === value) {
            return;
        }

        // 保存最后一次
        selectSolicitOrgLastValue = value;

        var nodes = selectSolicitOrgTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectSolicitOrgAllNode(nodes);
            return;
        }
        hideSelectSolicitOrgAllNode(nodes);
        selectSolicitOrgNodeList = selectSolicitOrgTree.getNodesByParamFuzzy(keyType, value);
        updateSelectSolicitOrgNodes(selectSolicitOrgNodeList);
    }

    // 清空查询
    function clearSearchSelectSolicitOrgOrgNode(){

        // 清空查询内容
        $('#selectSolicitOrgKeyWord').val('');
        showSelectSolicitOrgAllNode(selectSolicitOrgTree.getNodes());
        setTimeout(function() {
            expandSelectSolicitOrgAllNode();
        }, 300);
    }

    //隐藏所有节点
    function hideSelectSolicitOrgAllNode(nodes){

        nodes = selectSolicitOrgTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectSolicitOrgTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectSolicitOrgAllNode(nodes){

        nodes = selectSolicitOrgTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectSolicitOrgTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectSolicitOrgTree.expandNode(nodes[i],true,true,false,false);
            }
            selectSolicitOrgTree.showNode(nodes[i]);
            showSelectSolicitOrgAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectSolicitOrgNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectSolicitOrgTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectSolicitOrgTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectSolicitOrgTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectSolicitOrgTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectSolicitOrgTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectSolicitOrgTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 关闭选择事项弹窗
    function closeSelectSolicitOrgZtree(){

        $('#select_solicit_org_ztree_modal').modal('hide');
    }

    // 加载选择事项数据
    function initsSelectSolicitOrgZtree(){

        $.ajax({
            url: ctx+'/opus/om/org/admin/getOpusOmOrgZtreeNode.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectSolicitOrgTree)selectSolicitOrgTree.destroy();
                    selectSolicitOrgTree = $.fn.zTree.init($("#selectSolicitOrgTree"), selectSolicitOrgTreeSetting, data);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

                swal('错误信息', '初始化组织架构数据异常,异常信息：'+ XMLHttpRequest.responseText, 'error');
            }
        });
    }

    //刷新用户事项
    function setRelCheckSolicitOrgs() {

        // 清空关键字据
        $('#selectSolicitOrgKeyWord').val('');

        // 清空右侧选中事项数据
        $("#selectSolicitOrgCheckUl").html("");

        // 取消上次的选中节点
        selectSolicitOrgTree.checkAllNodes(false);

        // 取消上次选中节点
        selectSolicitOrgTree.cancelSelectedNode();

        // 加载已经选择的数据
        // loadCheckSelectSolicitOrgData();
    }

    //判断字符是否为空的方法
    function isEmpty(obj){

        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }

    function loadCheckSelectSolicitOrgData(){

        $.ajax({
            url: ctx+'/aea/solicit/org/listAeaSolicitOrgRelOrgInfo.do',
            type: 'post',
            data: {},
            async: false,
            success: function (data) {
                if(data!=null&&data.length>0){
                    for(var i=0;i<data.length;i++) {
                        var name = data[i].orgName;
                        var node = selectSolicitOrgTree.getNodeByParam("id", data[i].orgId, null);
                        if (node) {
                            selectSolicitOrgTree.checkNode(node, true, true, false);
                            name = node.name;
                        }

                        var liHtml = '<li name="selectSolicitOrgCheckLi" category-id="' + data[i].orgId + '">' +
                                        '<span class="drag-handle_td" onclick="removeSolicitOrgCheck(\'' + data[i].orgId + '\');">×</span>' +
                                        '<span class="org_name_td">' + name +'</span>' +
                                     '</li>';

                        $('#selectSolicitOrgCheckUl').append(liHtml);
                    }
                }
            }
        });
    }

</script>