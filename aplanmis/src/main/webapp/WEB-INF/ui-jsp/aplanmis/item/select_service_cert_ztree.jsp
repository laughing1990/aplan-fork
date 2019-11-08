<%@ page contentType="text/html;charset=utf-8" %>

<!-- 选择证件类型定义-->
<div id="select_service_cert_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_service_cert_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 550px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_service_cert_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;height: 450px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectServiceCertKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info" onclick="searchSelectServiceCertNode();">查询</button>
                        <button type="button" class="btn btn-danger" onclick="clearSearchSelectServiceCertNode();">清空</button>
                        <button type="button" class="btn btn-secondary" onclick="expandSelectServiceCertAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary" onclick="collapseSelectServiceCertAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div style="width: 100%;height: 545px;overflow-y:auto;overflow-x:auto;">
                    <div id="selectServiceCertTree" class="ztree"></div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectServiceCertBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectServiceCertZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

// 选择证件类型树配置
var selectServiceCertKey,
    selectServiceCertNodeList = [],
    selectServiceCertLastValue = "",
    selectServiceCertTree = null,
    selectServiceCertTreeSetting = {
        edit: {
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
            showHorizontal: false//设置是否水平平铺树（自定义属性）

        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onDblClick: onDblClickSelectServiceCertNode
        }
    };

    // 初始化加载函数
    $(function(){

        // 关键字搜索证件类型节点
        selectServiceCertKey = $("#selectServiceCertKeyWord");
        selectServiceCertKey.bind("focus", focusSelectServiceCertKey).bind("blur", blurSelectServiceCertKey).bind("change cut input propertychange",searchSelectServiceCertNode);
        selectServiceCertKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectServiceCertNode();
            }
        });

    });

    function focusSelectServiceCertKey(e) {

        if (selectServiceCertKey.hasClass("empty")) {
            selectServiceCertKey.removeClass("empty");
        }
    }

    function blurSelectServiceCertKey(e) {

        if (selectServiceCertKey.get(0).value === "") {
            selectServiceCertKey.addClass("empty");
        }
        searchSelectServiceCertNode(e);
    }

    function onDblClickSelectServiceCertNode(event, treeId, treeNode) {
        $("#selectServiceCertBtn").trigger("click");
    }

    //全部展开
    function expandSelectServiceCertAllNode(){
        selectServiceCertTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectServiceCertAllNode(){
        selectServiceCertTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectServiceCertNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectServiceCertKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectServiceCertLastValue === value) {
            return;
        }

        // 保存最后一次
        selectServiceCertLastValue = value;

        var nodes = selectServiceCertTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectServiceCertAllNode(nodes);
            return;
        }
        hideSelectServiceCertAllNode(nodes);
        selectServiceCertNodeList = selectServiceCertTree.getNodesByParamFuzzy(keyType, value);
        updateSelectServiceCertNodes(selectServiceCertNodeList);
    }

    // 清空查询
    function clearSearchSelectServiceCertNode(){

        // 清空查询内容
        $('#selectServiceCertKeyWord').val('');
        showSelectServiceCertAllNode(selectServiceCertTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectServiceCertAllNode(nodes){

        nodes = selectServiceCertTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectServiceCertTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectServiceCertAllNode(nodes){

        nodes = selectServiceCertTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectServiceCertTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectServiceCertTree.expandNode(nodes[i],true,true,false,false);
            }
            selectServiceCertTree.showNode(nodes[i]);
            showSelectServiceCertAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectServiceCertNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectServiceCertTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectServiceCertTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectServiceCertTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectServiceCertTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectServiceCertTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectServiceCertTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择证件类型弹窗
    function openSelectServiceCertModal(){

        $('#select_service_cert_ztree_modal').modal('show');
        $('#select_service_cert_ztree_modal_title').html('选择证件类型');
        loadSelectServiceCertZtreeData();
        // expandSelectServiceCertAllNode();
    }

    // 关闭选择证件类型弹窗
    function closeSelectServiceCertZtree(){

        $('#select_service_cert_ztree_modal').modal('hide');
    }

    // 加载选择证件类型数据
    function loadSelectServiceCertZtreeData(){

        $.ajax({
            url: ctx+'/aea/cert/type/getListCertTypeZtreeNode.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    if(selectServiceCertTree)selectServiceCertTree.destroy();
                    selectServiceCertTree = $.fn.zTree.init($("#selectServiceCertTree"), selectServiceCertTreeSetting , data);
                }
            },
            error: function(){
                swal('错误信息', '初始化证件类型数据异常!', 'error');
            }
        });
    }
</script>