<%@ page contentType="text/html;charset=UTF-8" %>

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

<!-- 选择事 -->
<div id="select_cert_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_cert_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_cert_ztree_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 5px 0px;">
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
                                                电子证照库
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectCertKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty"
                                                   placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchSelectCertNode();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchSelectCertNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandSelectCertAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseSelectCertAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectCertTree" class="ztree"
                                         style="height: 380px;overflow-y:auto;overflow-x:auto;"></div>
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
                                                已选证照
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div style="height: 435px;overflow-y:auto;overflow-x:auto;">
                                        <div class="selectedCertSortDiv">
                                            <ul id="selectedCertUl" class="block_ul_td"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectCertBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectCetrtZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

// 选择事项树配置
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
            selectedMulti: false,//设置是否允许同时选中多个节点
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

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectCertCheck(event, treeId, treeNode){

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        if(treeNode&&treeNode.type=='cert'){
            if(treeNode.checked){
                treeObj.selectNode(treeNode);
                var liHtml = '<li name="selectCertLi" category-id="'+treeNode.id+'" category-name="'+treeNode.name+'">' +
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

    // 初始化加载函数
    $(function(){

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
                var liHtml = '<li name="selectCertLi" category-id="'+treeNode.id+'" category-name="'+treeNode.name+'">' +
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

    // 删除选中节点
    function removeSelectedCert(certId){

        $('li[name="selectCertLi"]').each(function(){
            var categoryId = $(this).attr('category-id');
            if(categoryId==certId){
                $(this).remove();
                return false;
            }
        });
        // 取消所有所选
        selectCertTree.cancelSelectedNode();
        var node = selectCertTree.getNodeByParam("id", certId, null);
        if (node) {
            selectCertTree.checkNode(node, false, true, false);
        }
    }

    function expandNodes(nodes) {

        if (!nodes) return;
        var zTree = $.fn.zTree.getZTreeObj("selectCertTree");
        for (var i=0;i<nodes.length;i++) {
            zTree.expandNode(nodes[i], true, false, false);
            if (nodes[i].isParent) {
                expandNodes(nodes[i].children);//递归
            }
        }
    }

    //全部展开
    function expandSelectCertAllNode(){
        selectCertTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectCertAllNode(){
        selectCertTree.expandAll(false);
    }

    // 搜索节点
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
            showSelectItemAllNode(nodes);
            return;
        }
        hideSelectCertAllNode(nodes);
        selectCertNodeList = selectCertTree.getNodesByParamFuzzy(keyType, value);
        updateSelectCertNodes(selectCertNodeList);
    }

    // 清空查询
    function clearSearchSelectCertNode(){

        // 清空查询内容
        $('#selectItemKeyWord').val('');
        showSelectItemAllNode(selectCertTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectCertAllNode(nodes){

        nodes = selectCertTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectCertTree.hideNode(nodes[i]);
        }
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

    // 打开选择电子证照弹窗
    function openSelectCertModal(){

        $('#select_cert_ztree_modal').modal('show');
        $('#select_cert_ztree_modal_title').html('选择电子证照');
        initSelectCertZtree();
    }

    // 关闭选择电子证照弹窗
    function closeSelectCetrtZtree(){
        $('#select_cert_ztree_modal').modal('hide');
    }

    // 加载选择电子证照数据
    function initSelectCertZtree(){

        $.ajax({
            url: ctx+'/aea/cert/gtreeTypeCert.do',
            type:'post',
            data:{},
            async: false,
            dataType: 'json',
            success: function(data){
                if(data!=null&&data.length>0){
                    // if(selectCertTree)selectCertTree.destory();
                    selectCertTree = $.fn.zTree.init($("#selectCertTree"), selectCertTreeSetting,data);
                }
            },
            error: function(){
                swal('错误信息', '初始化电子证照数据异常!', 'error');
            }
        });
    }
</script>