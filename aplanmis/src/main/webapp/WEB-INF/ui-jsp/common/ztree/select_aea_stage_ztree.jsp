<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .selectedStageSortDiv {
        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .selectedStageSortDiv .block_list-title {
        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .selectedStageSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .selectedStageSortDiv li {
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

    .selectedStageSortDiv .drag-handle_th {
        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .selectedStageSortDiv .drag-handle_td {
        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    .selectedStageSortDiv .ostage_name_td{
        display: inline-block;
        width: 45%;
    }
</style>

<!-- 选择事 -->
<div id="select_stage_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_stage_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_stage_ztree_modal_title"></h5>
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
                                                工程建设主题阶段库
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectStageKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty"
                                                   placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchSelectStageNode();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchSelectStageNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandSelectStageAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseSelectStageAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectStageTreeDiv" style="height:380px;overflow:auto;">
                                        <ul id="selectStageTree" class="ztree"></ul>
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
                                            <h3 id="selectedStageHeadTitle" class="m-portlet__head-text">
                                                已选阶段
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div id="selectedStageDiv" style="height: 435px;overflow-y:auto;overflow-x:auto;">
                                        <div class="selectedStageSortDiv">
                                            <ul id="selectedStageUl" class="block_ul_td"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectStageBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectStageZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    // 选择事项树配置
    var selectStageKey,
        selectStageNodeList = [],
        selectStageLastValue = "",
        selectStageTree = null,
        selectStageTreeSetting = {
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
                onCheck: onSelectStageCheck,
                onClick: onClickSelectStageNode,
            }
        };

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectStageCheck(event, treeId, treeNode){

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode&&treeNode.type=='stage'){
            // 选中
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectStageLi" category-id="'+treeNode.id+'">' +
                                '<span class="drag-handle_td" onclick="removeSelectedStage(\''+treeNode.id+'\');">×</span>' +
                                '<span class="org_name_td">'+ treeNode.name +'</span>' +
                            '</li>';
                var i=0;
                $('li[name="selectStageLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId == treeNode.id){
                        i++;
                        return false;
                    }
                });
                if(i==0){
                    $('#selectedStageUl').append(liHtml);
                }
                // 取消
            }else{
                treeObj.cancelSelectedNode(treeNode);
                $('li[name="selectStageLi"]').each(function(){
                    var categoryId = $(this).attr('category-id');
                    if(categoryId == treeNode.id){
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
    }

    function onClickSelectStageNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode && treeNode.type == 'stage') {
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
                $('li[name="selectStageLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.id) {
                        $(this).remove();
                        return false;
                    }
                });
            } else {
                treeObj.checkNode(treeNode, true, false, true);
                var liHtml = '<li name="selectStageLi" category-id="'+ treeNode.id + '">' +
                                '<span class="drag-handle_td" onclick="removeSelectedStage(\'' + treeNode.id + '\');">×</span>' +
                                '<span class="org_name_td">' + treeNode.name+ '</span>' +
                             '</li>';
                var i = 0;
                $('li[name="selectStageLi"]').each(function () {
                    var categoryId = $(this).attr('category-id');
                    if (categoryId == treeNode.id) {
                        i++;
                        return false;
                    }
                });
                if (i == 0) {
                    $('#selectedStageUl').append(liHtml);
                }
            }
        }
    }

    // 初始化加载函数
    $(function(){

        $('#selectStageTreeDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#selectedStageDiv').niceScroll({

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
        selectStageKey = $("#selectStageKeyWord");
        selectStageKey.bind("focus", focusSelectStageKey)
                      .bind("blur", blurSelectStageKey)
                      .bind("change cut input propertychange", searchSelectStageNode);
        selectStageKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectStageNode();
            }
        });
    });

    function focusSelectStageKey(e) {

        if (selectStageKey.hasClass("empty")) {
            selectStageKey.removeClass("empty");
        }
    }

    function blurSelectStageKey(e) {

        if (selectStageKey.get(0).value === "") {
            selectStageKey.addClass("empty");
        }
        searchSelectStageNode(e);
    }

    // 删除选中节点
    function removeSelectedStage(stageId){

        $('li[name="selectStageLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            if(categoryId==stageId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectStageTree.cancelSelectedNode();
        var node = selectStageTree.getNodeByParam("id", stageId, null);
        if (node) {
            selectStageTree.checkNode(node, false, true, false);
        }
    }

    function expandNodes(nodes) {

        if (!nodes) return;
        var zTree = $.fn.zTree.getZTreeObj("selectStageTree");
        for (var i=0;i<nodes.length;i++) {
            zTree.expandNode(nodes[i], true, false, false);
            if (nodes[i].isParent) {
                expandNodes(nodes[i].children);//递归
            }
        }
    }

    //全部展开
    function expandSelectStageAllNode(){
        selectStageTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectStageAllNode(){
        selectStageTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectStageNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectStageKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectStageLastValue === value) {
            return;
        }

        // 保存最后一次
        selectStageLastValue = value;

        var nodes = selectStageTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectStageAllNode(nodes);
            return;
        }
        hideSelectStageAllNode(nodes);
        selectStageNodeList = selectStageTree.getNodesByParamFuzzy(keyType, value);
        updateSelectStageNodes(selectStageNodeList);
    }

    // 清空查询
    function clearSearchSelectStageNode(){

        // 清空查询内容
        $('#selectStageKeyWord').val('');
        showSelectStageAllNode(selectStageTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectStageAllNode(nodes){

        nodes = selectStageTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectStageTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectStageAllNode(nodes){

        nodes = selectStageTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectStageTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectStageTree.expandNode(nodes[i],true,true,false,false);
            }
            selectStageTree.showNode(nodes[i]);
            showSelectStageAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectStageNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectStageTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectStageTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectStageTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectStageTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectStageTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectStageTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择事项弹窗
    function openSelectStageModal(){

        $('#select_stage_ztree_modal').modal('show');
        $('#select_stage_ztree_modal_title').html('选择事项');
        initSelectStageZtree();

        // 清空关键字据
        $('#selectStageKeyWord').val('');

        // 清空右侧选中事项数据
        $("#selectedStageUl").html("");

        // 取消上次的选中节点
        selectStageTree.checkAllNodes(false);
    }

    // 关闭选择事项弹窗
    function closeSelectStageZtree(){

        $('#select_stage_ztree_modal').modal('hide');
    }

    // 加载选择事项数据
    function initSelectStageZtree(){

        $.ajax({
            url: ctx+'/aea/par/theme/ver/gtreeOkThemeRelStage.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectStageTree)selectStageTree.destroy();
                    selectStageTree = $.fn.zTree.init($("#selectStageTree"), selectStageTreeSetting,data);
                }
            },
            error: function(){
                swal('错误信息', '初始化主题阶段数据异常!', 'error');
            }
        });
    }
</script>