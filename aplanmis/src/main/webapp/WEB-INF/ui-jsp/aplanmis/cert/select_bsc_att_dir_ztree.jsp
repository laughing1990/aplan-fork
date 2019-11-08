<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 选择文件库-->
<div id="select_bsc_att_dir_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_bsc_att_dir_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="width: 700px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_bsc_att_dir_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 10px 0px;">
                <div class="row" style="margin: 0px;">
                    <div class="col-xl-5">
                        <input id="selectBscAttDirKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                               style="background-color: #f0f0f075;border-color: #c4c5d6;">
                    </div>
                    <div class="col-xl-7">
                        <button type="button" class="btn btn-info" onclick="searchSelectBscAttDirNode();">查询</button>
                        <button type="button" class="btn btn-secondary" onclick="clearSearchSelectBscAttDirNode();">清空</button>
                        <button type="button" class="btn btn-secondary" onclick="expandSelectBscAttDirAllNode();">展开</button>
                        <button type="button" class="btn btn-secondary" onclick="collapseSelectBscAttDirAllNode();">折叠</button>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div class="m-scrollable" data-scrollbar-shown="true" data-scrollable="true" data-max-height="380">
                    <div id="selectBscAttDirTree" class="ztree" style="overflow-y:auto;overflow-x:auto;"></div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectBscAttDirBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectBscAttDirZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

// 选择文件库树配置
var selectBscAttDirKey,
    selectBscAttDirNodeList = [],
    selectBscAttDirLastValue = "",
    selectBscAttDirTree = null,
    selectBscAttDirTreeSetting = {
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
            onDblClick: onDblClickSelectBscAttDirNode
        }
    };

    // 初始化加载函数
    $(function(){

        // 关键字搜索文件库节点
        selectBscAttDirKey = $("#selectBscAttDirKeyWord");
        selectBscAttDirKey.bind("focus", focusSelectBscAttDirKey).bind("blur", blurSelectBscAttDirKey).bind("change cut input propertychange",searchSelectBscAttDirNode);
        selectBscAttDirKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectBscAttDirNode();
            }
        });

    });

    function focusSelectBscAttDirKey(e) {

        if (selectBscAttDirKey.hasClass("empty")) {
            selectBscAttDirKey.removeClass("empty");
        }
    }

    function blurSelectBscAttDirKey(e) {

        if (selectBscAttDirKey.get(0).value === "") {
            selectBscAttDirKey.addClass("empty");
        }
        searchSelectBscAttDirNode(e);
    }

    function onDblClickSelectBscAttDirNode(event, treeId, treeNode) {
        $("#selectBscAttDirBtn").trigger("click");
    }

    //全部展开
    function expandSelectBscAttDirAllNode(){
        selectBscAttDirTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectBscAttDirAllNode(){
        selectBscAttDirTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectBscAttDirNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectBscAttDirKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectBscAttDirLastValue === value) {
            return;
        }

        // 保存最后一次
        selectBscAttDirLastValue = value;

        var nodes = selectBscAttDirTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectBscAttDirAllNode(nodes);
            return;
        }
        hideSelectBscAttDirAllNode(nodes);
        selectBscAttDirNodeList = selectBscAttDirTree.getNodesByParamFuzzy(keyType, value);
        updateSelectBscAttDirNodes(selectBscAttDirNodeList);
    }

    // 清空查询
    function clearSearchSelectBscAttDirNode(){

        // 清空查询内容
        $('#selectBscAttDirKeyWord').val('');
        showSelectBscAttDirAllNode(selectBscAttDirTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectBscAttDirAllNode(nodes){

        nodes = selectBscAttDirTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectBscAttDirTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectBscAttDirAllNode(nodes){

        nodes = selectBscAttDirTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectBscAttDirTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectBscAttDirTree.expandNode(nodes[i],true,true,false,false);
            }
            selectBscAttDirTree.showNode(nodes[i]);
            showSelectBscAttDirAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectBscAttDirNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectBscAttDirTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {

                //展开当前节点的父节点
                selectBscAttDirTree.showNode(nodeList[i].getParentNode());
                //显示展开符合条件节点的父节点
                while(nodeList[i].getParentNode()!=null){
                    selectBscAttDirTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                    nodeList[i] = nodeList[i].getParentNode();
                    selectBscAttDirTree.showNode(nodeList[i].getParentNode());
                }
                //显示根节点
                selectBscAttDirTree.showNode(nodeList[i].getParentNode());
                //展开根节点
                selectBscAttDirTree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        }
    }

    // 打开选择文件库弹窗
    var certBscAttDirShowStyle = null;
    function openSelectBscAttDirModal(showStyle){

        certBscAttDirShowStyle = showStyle;
        $('#select_bsc_att_dir_ztree_modal').modal('show');
        $('#select_bsc_att_dir_ztree_modal_title').html('选择文件库');
        loadSelectBscAttDirZtreeData();
    }

    function isOpenSelectBscAttDirModal(obj){

        if(obj){
            var value = $(obj).val();
            if(!value){
                openSelectBscAttDirModal(null);
            }
        }
    }

    // 关闭选择文件库弹窗
    function closeSelectBscAttDirZtree(){

        $('#select_bsc_att_dir_ztree_modal').modal('hide');
    }

    // 加载选择文件库数据
    function loadSelectBscAttDirZtreeData(){

        $.ajax({
            url: ctx+'/aea/cert/gtreeBscAttDir.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data){
                    if(selectBscAttDirTree)selectBscAttDirTree.destroy();
                    selectBscAttDirTree = $.fn.zTree.init($("#selectBscAttDirTree"), selectBscAttDirTreeSetting , data);
                }
            },
            error: function(){
                swal('错误信息', '初始化文件库数据异常!', 'error');
            }
        });
    }
</script>