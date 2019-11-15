<%@ page contentType="text/html;charset=UTF-8" %>

<div id="select_form_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_form_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 850px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_form_ztree_modal_title">选择表单</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectFormKeyWord" type="text" placeholder="请输入关键字..."
                               class="form-control m-input m-input--solid empty"
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info"
                                onclick="searchSelectFormNode();">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearSearchSelectFormNode();">清空</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="expandSelectFormAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="collapseSelectFormAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="m-scrollable" data-scrollbar-shown="true" data-scrollable="true" data-max-height="380">
                    <div id="selectFormTree" class="ztree" style="overflow-y:auto;overflow-x:auto;"></div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectFormBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectFormModal();">关闭</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var selectFormKey,
        selectFormNodeList = [],
        selectFormLastValue = "",
        selectFormTree = null,
        selectFormTreeSetting = {
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
                onCheck: onSelectFormCheck,
                onClick: onClickSelectFormNode,
                onDblClick: onDblClickSelectFormNode,
            }
        };

    /**
     * 选择事件
     *
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectFormCheck(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode && treeNode.type == 'form') {
            treeObj.cancelSelectedNode();
            if (treeNode.checked) {
                treeObj.selectNode(treeNode);
            }else{
                treeObj.cancelSelectedNode(treeNode);
            }
        }
    }

    /**
     * 点击事件
     *
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onClickSelectFormNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if (treeNode && treeNode.type == 'form') {
            treeObj.checkAllNodes(false);
            if (treeNode.checked) {
                treeObj.checkNode(treeNode, false, false, true);
            }else{
                treeObj.checkNode(treeNode, true, false, true);
            }
        }else{
            treeObj.cancelSelectedNode(treeNode);
        }
    }

    function onDblClickSelectFormNode(event, treeId, treeNode) {

        if (treeNode && treeNode.type == 'form') {
            $("#selectFormBtn").trigger("click");
        }
    }

    // 初始化加载函数
    $(function(){

        $('#selectFormTree').niceScroll({

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
        selectFormKey = $("#selectFormKeyWord");

        selectFormKey.bind("focus", focusSelectFormKey)
                     .bind("blur", blurSelectFormKey)
                     .bind("change cut input propertychange", searchSelectFormNode);

        selectFormKey.bind('keydown', function (e){

            if(e.which == 13){
                searchSelectFormNode();
            }
        });
    });

    function focusSelectFormKey(e) {

        if (selectFormKey.hasClass("empty")) {
            selectFormKey.removeClass("empty");
        }
    }

    function blurSelectFormKey(e) {

        if (selectFormKey.get(0).value === "") {
            selectFormKey.addClass("empty");
        }
        searchSelectFormNode(e);
    }

    function searchSelectFormNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectFormKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectFormLastValue === value) {
            return;
        }

        // 保存最后一次
        selectFormLastValue = value;

        var nodes = selectFormTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectFormAllNode(nodes);
            return;
        }
        hideSelectFormAllNode(nodes);
        selectFormNodeList = selectFormTree.getNodesByParamFuzzy(keyType, value);
        updateSelectFormNodes(selectFormNodeList);
    }

    //全部展开
    function expandSelectFormAllNode(){

        selectFormTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectFormAllNode(){

        selectFormTree.expandAll(false);
    }

    // 清空查询
    function clearSearchSelectFormNode(){

        // 清空查询内容
        $('#selectFormKeyWord').val('');
        showSelectFormAllNode(selectFormTree.getNodes());
    }

    //显示所有节点
    function showSelectFormAllNode(nodes){

        nodes = selectFormTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectFormTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectFormTree.expandNode(nodes[i],true,true,false,false);
            }
            selectFormTree.showNode(nodes[i]);
            showSelectFormAllNode(nodes[i].children);
        }
    }

    //隐藏所有节点
    function hideSelectFormAllNode(nodes){

        nodes = selectFormTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectFormTree.hideNode(nodes[i]);
        }
    }

    //更新节点状态
    function updateSelectFormNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectFormTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectFormTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectFormTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectFormTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectFormTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectFormTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 初始化加载表单
    function initGtreeForm(){

        $.ajax({
            url: MAT_URL_PREFIX + 'gtreeForm.do',
            type: 'post',
            data: {},
            async: false,
            dataType: 'json',
            success: function (data) {
                if (data != null && data.length > 0) {
                    selectFormTree = $.fn.zTree.init($("#selectFormTree"), selectFormTreeSetting, data);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', '初始化表单数据异常,错误信息:' + XMLHttpRequest.responseText, 'error');
            }
        });
    }

    // 打开选择表单
    function openSelectFormModal(formId){

        $('#select_form_ztree_modal').modal('show');
        $('#select_form_ztree_modal_title').html('选择表单');
        // 清空关键字据
        $('#selectFormKeyWord').val('');
        // 加载
        initGtreeForm();
        if(selectFormTree){
            // 取消上次的选择节点
            selectFormTree.checkAllNodes(false);
            // 取消上次选中节点
            selectFormTree.cancelSelectedNode();
            // 选择数据
            selectFormTreeData(formId);
        }
    }

    // 关闭选择表单
    function closeSelectFormModal(){

        $('#select_form_ztree_modal').modal('hide');
    }

    // 选择节点
    function selectFormTreeData(formId){

        if(formId){
            var node = selectFormTree.getNodeByParam("id", formId, null);
            if (node) {
                selectFormTree.checkNode(node, true, true, false);
                selectFormTree.selectNode(node);
            }
        }
    }
</script>