<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .condStateElText {

        width: 100%;
        min-height: 421px;
        max-height: 421px;
        border: 1px solid #e8e8e8;
        font-size: 12px;
        word-wrap: break-word;
        overflow-x: hidden;
        overflow-y: auto;
        _overflow-y: visible;
    }

    #selectMetaDbTbcolTree::-webkit-scrollbar{
        width:4px;
        height:4px;
    }
    #selectMetaDbTbcolTree::-webkit-scrollbar-track{
        background: #fff;
        border-radius: 2px;
    }
    #selectMetaDbTbcolTree::-webkit-scrollbar-thumb{
        background: #e2e5ec;
        border-radius:2px;
    }
    #selectMetaDbTbcolTree::-webkit-scrollbar-thumb:hover{
        background: #aaa;
    }
    #selectMetaDbTbcolTree::-webkit-scrollbar-corner{
        background: #fff;
    }


</style>

<!-- 选择元数据表字段-->
<div id="select_meta_db_tbcol_ztree_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="select_meta_db_tbcol_ztree_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="select_meta_db_tbcol_ztree_modal_title"></h5>
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
                                            <h3 class="m-portlet__head-text">
                                                元数据表字段库（双击选择字段添加至右侧编辑）
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px;">
                                    <div class="row" style="margin: 0px;">
                                        <div class="col-xl-5">
                                            <input id="selectMetaDbTbcolKeyWord" type="text"
                                                   class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                                                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
                                            <input id="isCondOrState" type="hidden" name="isCondOrState" value=""/>
                                        </div>
                                        <div class="col-xl-7">
                                            <button type="button" class="btn btn-info"
                                                    onclick="searchSelectMetaDbTbcolNode();">查询</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="clearSearchSelectMetaDbTbcolNode();">清空</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="expandSelectMetaDbTbcolAllNode();">展开</button>
                                            <button type="button" class="btn btn-secondary"
                                                    onclick="collapseSelectMetaDbTbcolAllNode();">折叠</button>
                                        </div>
                                    </div>
                                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                                    <div id="selectMetaDbTbcolTree" class="ztree" style="height: 380px;overflow: auto;"></div>
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
                                                编辑EL表达式（编辑完成点击右下侧保存按钮）
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div class="m-portlet__body" style="padding: 10px 0px 0px;">
                                    <div style="height: 435px;overflow: auto;">
                                        <div class="form-group m-form__group row" >
                                            <div class="col-lg-12">
                                                <div id="condStateElText" contenteditable="true" class="condStateElText"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="selectMetaDbTbcolBtn" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeSelectMetaDbTbcolZtree();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

var selectMetaDbTbcolKey,
    selectMetaDbTbcolNodeList = [],
    selectMetaDbTbcolLastValue = "",
    selectMetaDbTbcolTree = null,
    selectMetaDbTbcolTreeSetting = {
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
        async: {
            //设置 zTree 是否开启异步加载模式
            enable: true,
            autoParam: ["id"],
            url:ctx+"/aea/item/gtreeTableColumnAsyncZTree.do"
        },
        //用于捕获节点被点击的事件回调函数
        callback: {
            onDblClick: onDblClickSelectMetaDbTbcolNode,
            onAsyncSuccess: onAsyncSuccessSelectMetaDbTbcolNode,
        }
    };

    /**
     * onClick 事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onDblClickSelectMetaDbTbcolNode(event, treeId, treeNode) {

        if(treeNode&&treeNode.type=='column'){
            $('#condStateElText').html($('#condStateElText').html()+convertTableField(treeNode.name));
        }
    }

    // 转换表字段成变量格式
    function convertTableField(name) {

        if(name){
            var index = name.indexOf('【');
            if(index>-1){
                var sb = "";
                name = name.substring(0,index).toLowerCase();
                var filed_chars = name.split("_");
                if (filed_chars!=null&&filed_chars.length>0) {
                    sb += "$"+"{";
                    if(filed_chars.length==1){
                        sb += filed_chars[0];
                    }else{
                        for(var j in filed_chars) {
                            if (j == 0) {
                                sb += (filed_chars[j].substring(0, 1).toLowerCase() + filed_chars[j].substr(1));
                            } else {
                                sb += (filed_chars[j].substring(0, 1).toUpperCase() + filed_chars[j].substr(1));
                            }
                        }
                    }
                    sb += "}";
                }
            }
            return sb;
        }else{
            return "";
        }
    }

    /**
     * 异步加载成功后 事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onAsyncSuccessSelectMetaDbTbcolNode(event, treeId, treeNode) {

        if(treeNode==null||treeNode==undefined||treeNode==''){
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            var nodes = zTree.getNodes();
            if(nodes!=null&&nodes.length>0){
                for(i in nodes){
                    zTree.expandNode(nodes[i], true, false, false);
                }
            }
        }
    }

    // 初始化加载函数
    $(function(){

        // 关键字搜索事项节点
        selectMetaDbTbcolKey = $("#selectMetaDbTbcolKeyWord");
        selectMetaDbTbcolKey.bind("focus", focusSelectMetaDbTbcolKey)
            .bind("blur", blurSelectMetaDbTbcolKey)
            .bind("change cut input propertychange",searchSelectMetaDbTbcolNode);
        selectMetaDbTbcolKey.bind('keydown', function (e){
            if(e.which == 13){
                searchSelectMetaDbTbcolNode();
            }
        });

        // 选择节点
        $('#selectMetaDbTbcolBtn').click(function(){

            var isCondOrState = $('#isCondOrState').val();
            var condStateElText = $('#condStateElText').html();
            var treeObj = $.fn.zTree.getZTreeObj("selectMetaDbTbcolTree");
            if(condStateElText!=null&&condStateElText.length>0){
                swal({
                    title: '提示信息',
                    type: 'info',
                    text: '您是否覆盖原来表达式值?',
                    showCancelButton: true,
                    confirmButtonText: '是',
                    cancelButtonText: '否',
                }).then(function(result) {
                    if(isCondOrState=='isCond'){
                        if (result.value) {
                            $("#condEl").val(condStateElText);
                        }else{
                            $("#condEl").val($("#condEl").val()+"  "+condStateElText);
                        }
                    }else if(isCondOrState=='isState'){
                        if (result.value) {
                            $("#stateEl").val(condStateElText);
                        }else{
                            $("#stateEl").val($("#stateEl").val()+"  "+condStateElText);
                        }
                    }else if(isCondOrState=='stageEl'){
                        if (result.value) {
                            $("#stageEl").val(condStateElText);
                        }else{
                            $("#stageEl").val($("#stageEl").val()+"  "+condStateElText);
                        }
                    }else if(isCondOrState=='elContent'){
                        if (result.value) {
                            $("#elContent").val(condStateElText);
                        }else{
                            $("#elContent").val($("#elContent").val()+"  "+condStateElText);
                        }
                    }else if(isCondOrState){
                        if (result.value) {
                            $("#"+isCondOrState).val(condStateElText);
                        }else{
                            $("#"+isCondOrState).val($("#"+isCondOrState).val()+"  "+condStateElText);
                        }
                    }
                    $('#select_meta_db_tbcol_ztree_modal').modal('hide');
                });
            }else{
                swal('提示信息','请先填写EL表达式内容','info');
            }
        });
    });

    function focusSelectMetaDbTbcolKey(e) {

        if (selectMetaDbTbcolKey.hasClass("empty")) {
            selectMetaDbTbcolKey.removeClass("empty");
        }
    }

    function blurSelectMetaDbTbcolKey(e) {

        if (selectMetaDbTbcolKey.get(0).value === "") {
            selectMetaDbTbcolKey.addClass("empty");
        }
        searchSelectMetaDbTbcolNode(e);
    }

    //全部展开
    function expandSelectMetaDbTbcolAllNode(){

        selectMetaDbTbcolTree.expandAll(true);
    }

    //全部折叠
    function collapseSelectMetaDbTbcolAllNode(){

        selectMetaDbTbcolTree.expandAll(false);
    }

    // 搜索节点
    function searchSelectMetaDbTbcolNode(){

        // 取得输入的关键字的值
        var value = $.trim($('#selectMetaDbTbcolKeyWord').val());

        // 按名字查询
        var keyType = "name";

        // 如果和上次一次，就退出不查了。
        if (selectMetaDbTbcolLastValue === value) {
            return;
        }

        // 保存最后一次
        selectMetaDbTbcolLastValue = value;

        var nodes = selectMetaDbTbcolTree.getNodes();
        // 如果要查空字串，就退出不查了。
        if (value == "") {
            showSelectMetaDbTbcolAllNode(nodes);
            return;
        }
        hideSelectMetaDbTbcolAllNode(nodes);
        selectMetaDbTbcolNodeList = selectMetaDbTbcolTree.getNodesByParamFuzzy(keyType, value);
        updateSelectMetaDbTbcolNodes(selectMetaDbTbcolNodeList);
    }

    // 清空查询
    function clearSearchSelectMetaDbTbcolNode(){

        // 清空查询内容
        $('#selectMetaDbTbcolKeyWord').val('');
        showSelectMetaDbTbcolAllNode(selectMetaDbTbcolTree.getNodes());
    }

    //隐藏所有节点
    function hideSelectMetaDbTbcolAllNode(nodes){

        nodes = selectMetaDbTbcolTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            selectMetaDbTbcolTree.hideNode(nodes[i]);
        }
    }

    //显示所有节点
    function showSelectMetaDbTbcolAllNode(nodes){

        nodes = selectMetaDbTbcolTree.transformToArray(nodes);
        for(var i=nodes.length-1; i>=0; i--) {
            if(nodes[i].getParentNode()!=null){
                selectMetaDbTbcolTree.expandNode(nodes[i],false,false,false,false);
            }else{
                selectMetaDbTbcolTree.expandNode(nodes[i],true,true,false,false);
            }
            selectMetaDbTbcolTree.showNode(nodes[i]);
            showSelectMetaDbTbcolAllNode(nodes[i].children);
        }
    }

    //更新节点状态
    function updateSelectMetaDbTbcolNodes(nodeList) {

        if(nodeList!=null&&nodeList.length>0) {
            selectMetaDbTbcolTree.showNodes(nodeList);
            for(var i=0, l=nodeList.length; i<l; i++) {
                if(nodeList[i].children){
                    selectMetaDbTbcolTree.showNodes(nodeList[i].children);
                }
            }
        }
    }

    // 打开选择表字段
    function openSelectMetaDbTbcolModal(isCondOrState){

        $('#select_meta_db_tbcol_ztree_modal').modal('show');
        $('#select_meta_db_tbcol_ztree_modal_title').html('选择表字段');
        $('#selectMetaDbTbcolKeyWord').val('');
        $('#isCondOrState').val(isCondOrState);
        $('#condStateElText').html('');
        initSelectMetaDbTbcolZtree();
    }

    // 关闭选择表字段
    function closeSelectMetaDbTbcolZtree(){

        $('#select_meta_db_tbcol_ztree_modal').modal('hide');
    }

    // 初始化选择表字段树
    function initSelectMetaDbTbcolZtree() {

        selectMetaDbTbcolTree = $.fn.zTree.init($("#selectMetaDbTbcolTree"), selectMetaDbTbcolTreeSetting);
    }


</script>