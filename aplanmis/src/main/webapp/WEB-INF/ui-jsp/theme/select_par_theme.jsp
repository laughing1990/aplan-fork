<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 选择主题 -->
<div id="select_par_theme_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_par_theme_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_par_theme_ztree_modal_title">选择主题</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectParThemeKeyWord" type="text"
                               class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info"
                                onclick="searchSelectParThemeNode();">查询</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="clearSearchSelectParThemeNode();">清空</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="expandSelectParThemeAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary"
                                onclick="collapseSelectParThemeAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="parThemeDiv" style="height:420px;overflow:auto;">
                    <ul id="parThemeTree" class="ztree"></ul>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="saveParThemeBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectParThemeZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    var selectParThemeTree,
        selectParThemeKey,
        selectParThemeNodeList = [],
        selectParThemeLastValue = "";
    //事项输出树配置setting信息
    var parThemeSetting = {

        edit: {
            enable: false, //设置 zTree 是否处于编辑状态
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
            showHorizontal:false//设置是否水平平铺树（自定义属性）

        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onClick: onClickParThemeNode,
            onDblClick: onDblClickParThemeNode
        }
    };

    $(function(){

        // 关键字搜索事项节点
        selectParThemeKey = $("#selectParThemeKeyWord");

        selectParThemeKey.bind("focus", focusSelectParThemeKey)
            .bind("blur", blurSelectParThemeKey)
            .bind("change cut input propertychange",searchSelectParThemeNode);

        selectParThemeKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectParThemeNode();
            }
        });

        $('#parThemeDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });
    });

    function focusSelectParThemeKey(e) {

        if (selectParThemeKey.hasClass("empty")) {
            selectParThemeKey.removeClass("empty");
        }
    }

    function blurSelectParThemeKey(e) {

        if (selectParThemeKey.get(0).value === "") {
            selectParThemeKey.addClass("empty");
        }
        searchSelectParThemeNode(e);
    }

    //全部展开
    function expandSelectParThemeAllNode(){

        selectParThemeTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectParThemeAllNode(){

        selectParThemeTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectParThemeNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectParThemeKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectParThemeLastValue === value) {
            return;
        }

        // 保存最后一次
        selectParThemeLastValue = value;
        var nodes = selectParThemeTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectParThemeAllNode(nodes);
            return;
        }
        hideSelectParThemeAllNode(nodes);
        selectParThemeNodeList = selectParThemeTree.getNodesByParamFuzzy(keyType, value);
        updateSelectParThemeNodes(selectParThemeNodeList);
    }

    // 清空查询
    function clearSearchSelectParThemeNode(){

        // 清空查询内容
        $('#selectParThemeKeyWord').val('');
        showSelectParThemeAllNode(selectParThemeTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectParThemeAllNode(nodes){

        nodes = selectParThemeTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectParThemeTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectParThemeAllNode(nodes){

        nodes = selectParThemeTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectParThemeTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectParThemeTree.expandNode(nodes[i],true,true,false,false);
            }
            selectParThemeTree.showNode(nodes[i]);
            showSelectParThemeAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectParThemeNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectParThemeTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectParThemeTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectParThemeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectParThemeTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectParThemeTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectParThemeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    /**
     * 打开事项输出树
     */
    function selectParThemeZtree(){

        $('#select_par_theme_ztree_modal').modal('show');
        $('#select_par_theme_ztree_modal_title').html('选择主题');
        $('#parThemeDiv').animate({scrollTop: 0}, 800);
        showParTheme();
    }

    /**
     * 关闭事项输出树
     */
    function closeSelectParThemeZtree() {

        $('#select_par_theme_ztree_modal').modal('hide');
    }

    function showParTheme() {

        $.ajax({
            type: "post",
            url: ctx + '/aea/par/theme/gtreeTheme.do',
            // async: false,
            data: {},
            success: function (data) {
                if (data && data.length > 0) {
                    selectParThemeTree = $.fn.zTree.init($("#parThemeTree"), parThemeSetting, data);
                }
            }
        });
    }

    function onClickParThemeNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode.type=='root'){
            treeObj.cancelSelectedNode(treeNode);
            return;
        }
    }

    function onDblClickParThemeNode(event, treeId, treeNode) {

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode.type=='root'){
            treeObj.cancelSelectedNode(treeNode);
            return;
        }
        $("#saveParThemeBtn").trigger("click");
    }
    
</script>