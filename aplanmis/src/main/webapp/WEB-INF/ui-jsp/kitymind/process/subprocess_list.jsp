<%@ page contentType="text/html;charset=UTF-8" %>
<link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" type="text/css" rel="stylesheet"/>
<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }

    /*bootstrap下拉多级联动*/
    .dropdown-menu {
        width: 733px;
    !important;
    }
</style>

<div id="subprocess_list_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="subprocess_list_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 70%;width: 70%;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="subprocess_list_modal_title">子流程配置</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;max-height: 500px;height: 500px;overflow-y:auto;overflow-x: hidden">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6" style="padding: 0px;text-align: left;">
                            <button type="button"  class="btn btn-info" onclick="addSubProcess(0)">新增配置</button>
                            <button type="button" class="btn btn-danger" onclick="batchDelSubTrigger()">删除配置</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="subprocessKeyword" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchSubprocess();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearSubprocessSearch();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <!-- 列表区域 -->
                <div class="base" style="padding: 0px 5px;">
                    <table  id="subprocess_list_tb">
                    </table>
                </div>
                <!-- 列表区域end -->
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <%--<button type="button" class="btn btn-info" onclick="saveStateMatItem();">保存</button>--%>
                <%--<button type="button" class="btn btn-secondary"--%>
                <%--onclick="$('#view_state_mat_item_modal').modal('hide');">关闭</button>--%>
            </div>
        </div>
    </div>
</div>

<div id="subprocess_modal" class="modal fade" tabindex="4" role="dialog"
     aria-labelledby="createProcessDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="width:1000px;margin:0 auto;">
            <div class="modal-header" style="padding: 10px;height: 40px;">
                <h5 id="createProcessDialog_title" class="modal-title">子流程触发条件配置</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="text-align: center;">
                <form id="subProcForm">
                    <br/>
                    <div class="form-group m-form__group row" hidden>
                        <label class="col-3 col-form-label">
                            ID：
                        </label>
                        <div class="col-9">
                            <%--<input name="triggerId" class="form-control" type="text" id="triggerId" value=""/>--%>
                            <span id="triggerId"></span>
                        </div>
                    </div>
                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>触发的节点：
                        </label>
                        <div class="col-9">
                            <select id="node" type="text" class="form-control" name="node">

                            </select>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>是否外部流程：
                        </label>
                        <div class="col-9">
                            <div class="col-sm-10 d-flex" id="isOuterFlow">
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    <input ng-model="triggerEvent_task_create" value="0"  name="isOuterFlow" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;是&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span></span>
                                </label>
                                <label class="m-checkbox" ng-show="tabType == 1" style="">
                                    <input ng-model="triggerEvent_task_complete" value="1"  name="isOuterFlow" checked="checked" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;否&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span></span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>被触发的流程：
                        </label>
                        <div class="col-9">
                            <select id="triggerAppFlowdefId" type="text" class="form-control" name="triggerAppFlowdefId">

                            </select>
                        </div>
                    </div>

                    <div class="form-group m-form__group row">
                        <label class="col-3 col-form-label">
                            <font color="red">*</font>触发时机：
                        </label>
                        <div class="col-9">
                            <div class="col-sm-10 d-flex" id="chufa">

                            </div>
                        </div>
                    </div>

                    <%--<div class="form-group m-form__group row" hidden id="form_bus">--%>
                        <%--<label class="col-3 col-form-label">--%>
                            <%--关联事项：--%>
                        <%--</label>--%>
                        <%--<div class="col-9">--%>
                            <%--<select id="busRecordId" type="text" class="form-control" name="busRecordId">--%>

                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>

                    <div class="form-group m-form__group row dropdown" id="form_bus">
                        <label class="col-3 col-form-label">
                            关联事项：
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control m-input" data-toggle="dropdown" id="busRecordName" name="busRecordName" value="" placeholder="请选择关联的事项"/>
                            <ul id="selectItemTreePanel" class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
                                <div id="selectItemTree" class="ztree" style="height: 300px;overflow: auto;"></div>
                            </ul>
                            <input type="hidden" name="busRecordId" id="busRecordId"/>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="padding: 10px">
                <button type="button" class="btn btn-default" onclick="saveSubTrigger()">保存</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    var selectItemTree = null;
    var itemTreeData = [];
    var lastSelectNode = null;
    var lastClickNode = null;
    var selectItemTreeSetting = {
      check: {
        enable: true,
        chkStyle: "checkbox",
        chkboxType: {"Y": "", "N": ""}
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
        showTitle: true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
        showLine: true, //设置 zTree 是否显示节点之间的连线。
        showHorizontal: false//设置是否水平平铺树（自定义属性）
      },
      //用于捕获节点被点击的事件回调函数
      callback: {
        beforeCheck: beforeSelectItemCheck,
        onCheck: onSelectItemCheck,
        beforeClick: beforeSelectItemClick,
        onClick: onClickItemCheck,
      }
    };

    function beforeSelectItemCheck(treeId, treeNode) {
      if(treeNode && treeNode.type == 'ss_item') {
        return true;
      }else {
        swal({
          title: '提示信息',
          text: "只能关联实施事项，请重新选择！",
          type: 'warning',
          timer: 1000,
          showConfirmButton: false
        });
        return false;
      }
    }

    function beforeSelectItemClick(treeId, treeNode, clickFlag) {
      if(treeNode && treeNode.type == 'ss_item') {
        return true;
      }else {
        swal({
          title: '提示信息',
          text: "只能关联实施事项，请重新选择！",
          type: 'warning',
          timer: 1000,
          showConfirmButton: false
        });
        return false;
      }
    }

    /**
     * onCheck事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onSelectItemCheck(event, treeId, treeNode) {
      var treeObj = $.fn.zTree.getZTreeObj(treeId);
      if (treeNode && treeNode.type == 'ss_item') {
        // 选中
        if (treeNode.checked) {
          if (lastSelectNode && lastSelectNode != treeNode) {
            treeObj.checkNode(lastSelectNode, false, false, true);
            treeObj.cancelSelectedNode(lastSelectNode);
          }
          lastSelectNode = treeNode;
          treeObj.selectNode(treeNode);
          $("#busRecordId").val(treeNode.id);
          $("#busRecordName").val(treeNode.name);
          // 取消
        } else {
          lastSelectNode = null;
          treeObj.cancelSelectedNode(treeNode);
          $("#busRecordId").val('');
          $("#busRecordName").val('');
        }
      }
    }
    /**
     * onClick事件响应函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onClickItemCheck(event, treeId, treeNode) {
      var treeObj = $.fn.zTree.getZTreeObj(treeId);
      if (treeNode && treeNode.type == 'ss_item') {
        // 选中
        if (treeNode.checked) {
          treeObj.checkNode(treeNode, false, false, true);
          treeObj.cancelSelectedNode(treeNode);
          $("#busRecordId").val('');
          $("#busRecordName").val('');
          // 取消
        } else {
          treeObj.checkNode(treeNode, true, false, true);
          if(lastClickNode && lastClickNode != treeNode){
            treeObj.checkNode(lastClickNode, false, false, true);
            treeObj.cancelSelectedNode(lastClickNode);
          }
          lastClickNode = treeNode;
          treeObj.selectNode(treeNode);
          $("#busRecordId").val(treeNode.id);
          $("#busRecordName").val(treeNode.name);
        }
      }
    }

    /**
     * 控制面板显隐，临时
     */
    $("#selectItemTree").on('click',function (e) {
      var text = $(e.target).text();
      if(text.indexOf("实") == -1){
        e.stopPropagation();
      }
    });

    //清除查询
    function clearSubprocessSearch() {
        $('#subprocessKeyword').val('');
        //$('#subprocess_list_tb').bootstrapTable('refresh');
        searchSubprocess();
    }

    //
    var outerFlowList=[];
    var innerFlowList=[];
    function addSubProcess(type) {
      vm.addSubProcess(type);
      // $('#busRecordId').val('');
      // $('#busRecordName').val('');
      // $('#subprocess_modal').modal("show");
      // onloadNode();
      // onloadTriggerAppFlowdefId();
      // // $('input[name="isOuterFlow"]').prop("checked",false);
      // isOuterFlow()
      // if(type==0){
      //     $('#triggerId').text("");
      // }
      // if(currentBusiType=='stage'){
      //     $('#form_bus').attr("hidden",false);
      //     // onloadBus();
      //     onloadItemTree();
      // }else{
      //     $('#form_bus').attr("hidden",true);
      // }

    }

    function onloadBus() {
        $('#busRecordId').empty();
        $.ajax({
            url: ctx + '/aea/par/stage/item/listAeaStageItemByStageId.do',
            type: 'POST',
            data: {stageId: currentBusiId},
            async:false,
            success: function (result) {
                var option='<option value="">--请选择--</option>';
                if(result && result.length>0){
                    for (var i=0;i<result.length>0;i++){
                        var row=result[i];
                        option+='<option value="'+row.itemVerId+'">'+row.itemName+'</option> ';
                    }
                }
                $('#busRecordId').append(option);
            }
        });
    }

    /**
     * 加载事项树
     */
    function onloadItemTree() {
      itemTreeData = [];
      $.ajax({
        url: ctx + '/aea/par/stage/item/getStageItemTreeByStageId.do',
        type: 'POST',
        data: {stageId: currentBusiId},
        async: false,
        success: function (result) {
          if (result && result.length > 0) {
            itemTreeData = result;
            formatTreeData(result);
            if(selectItemTree){
              selectItemTree.destroy();
            }
            selectItemTree = $.fn.zTree.init($("#selectItemTree"), selectItemTreeSetting,result);
          }
        }
      });
    }
    /**
     * 格式化实现类型名称
     */
    function formatTreeData(data) {
      for(var i=0; i<data.length; i++){
        var item = data[i];
        if(item.type == 'bz_item'){
          item.name = "【标准事项】" + item.name;
        }else if(item.type == 'ss_item'){
          item.name = "【实施事项】" + item.name;
        }
      }
    }

    function selTriggerAppFlowdefId(flowType) {
        $('#triggerAppFlowdefId').empty();
        var option='<option value="">--请选择--</option>';
        if(flowType=="outer"){
            if(outerFlowList && outerFlowList.length>0){
                for (var i=0;i<outerFlowList.length;i++){
                    var entity=outerFlowList[i];
                    option+='<option value="'+entity.className+'">'+entity.listenerName+'</option> ';
                }
            }
        }else{
            if(innerFlowList && innerFlowList.length>0){
                for (var i=0;i<innerFlowList.length;i++){
                    var entity=innerFlowList[i];
                    option+='<option value="'+entity.id+'">'+entity.defName+'</option> ';
                }
            }
        }
        $('#triggerAppFlowdefId').append(option);
    }

    //加载被触发的流程控件
    function onloadTriggerAppFlowdefId() {
        if(!listenerList||listenerList.length==0){
            $.ajax({
                // url: ctx + '/act/sto/flow/listener/getProcessFlowListeners.do?procKey='+currentDefKey,
                url:ctx+'/act/sto/flow/listener/process/list?procKey='+currentDefKey,
                type: 'GET',
                async:false,
                success: function (result) {
                    outerFlowList=result;
                }
            });
        }else{
            outerFlowList=listenerList;
        }
        $.ajax({
            url: ctx + '/rest/mind/processDefs.do',
            type: 'POST',
            data: {appId: appId},
            async:false,
            success: function (result) {
               innerFlowList=result;
            }
        });
    }

    //当前选择流程节点id
    var selectNodeId = "";
    //当前选择流程节点名称
    var selectNodeName = "";
    //启用单个节点配置，只能配置当前节点下的子流程
    var singleConfig = false;
    //加载触发的节点控件，20190829增加从节点进入子流程配置，只能配置当前节点下的子流程
    function onloadNode() {
        $('#node').empty();
        var option;
        if(singleConfig){
            option = '<option value="'+ selectNodeId +'" selected>' + selectNodeName + '</option> ';
        }else{
            $.ajax({
                url: ctx + '/rest/mind/getTaskNodeList.do',
                type: 'POST',
                async:false,
                data: {defKey: currentDefKey},
                success: function (result) {
                    option = '<option value="">--请选择--</option>';
                    if(result && result.length>0){
                        for (var i=0;i<result.length;i++){
                            var vo=result[i];
                            var value=vo.name;
                            if(!value){
                                value=vo.id;
                            }
                            if(selectNodeId && selectNodeId == vo.id){
                                option+='<option value="'+vo.id+'" selected>'+value+'</option> ';
                            }else{
                                option+='<option value="'+vo.id+'">'+value+'</option> ';
                            }
                        }
                    }
                }
            });
        }
        $('#node').append(option);
        TypeSel();
    }

    //控制内部流程或外部流程选择时，表单的切换
    $('#isOuterFlow').change(function () {
        isOuterFlow();
    });

    function isOuterFlow(){

        var isOuterFlow=$('input[name="isOuterFlow"]:checked').val();
        if(0==isOuterFlow){//是外部流程，这时应该隐藏被触发的流程和触发时机表单控件
            $('#isInnerFlow1').attr("hidden",false);
            selTriggerAppFlowdefId("outer");
        }else if(1==isOuterFlow){
            $('#isInnerFlow1').attr("hidden",false);
            selTriggerAppFlowdefId("inner");
        }else{
            $('#isInnerFlow1').attr("hidden",true);
        }
    }


    $('#node').change(function () {
        TypeSel();
    });
    //加载触发时机控件
    function TypeSel() {
        var strCode=$('#node').find("option:selected").text();
        $('#chufa').empty();
        var label="";
        if(strCode=="开始"||strCode=="结束"){
            label=' <label class="m-checkbox ng-hide" ng-show="tabType == 2&amp;&amp;!isSequenceFlow">\n' +
                '            <input ng-model="triggerEvent_node_start" value="start" name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;节点开始时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
                '    <span></span>\n' +
                '        </label>\n' +
                '        <label class="m-checkbox ng-hide" ng-show="tabType == 2&amp;&amp;!isSequenceFlow">\n' +
                '            <input ng-model="triggerEvent_node_end" value="end"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;节点结束时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
                '    <span></span>\n' +
                '        </label>';
        }
        else if(strCode.indexOf("sid") == 0){

        }else if(strCode.indexOf("sequeFlow")==0) {
            label='<label class="m-checkbox ng-hide" ng-show="isSequenceFlow">\n' +
                '            <input ng-model="triggerEvent_seq_take" value="take"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;到达时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
                '    <span></span>\n' +
                '        </label>';

        }else if(strCode.indexOf("gateway")==0){

        }
        else {
            label='<label class="m-checkbox" ng-show="tabType == 1" style="">\n' +
                '       <input ng-model="triggerEvent_task_create" value="create"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;任务创建时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
                '       <span></span>\n' +
                '  </label>\n' +
                '  <label class="m-checkbox" ng-show="tabType == 1" style="">\n' +
                '       <input ng-model="triggerEvent_task_complete" value="complete"  name="checkName" type="radio" class="ng-pristine ng-untouched ng-valid">&nbsp;任务完成时触发&nbsp;&nbsp;&nbsp;&nbsp;\n' +
                '       <span></span>\n' +
                '  </label>';
        }
        $('#chufa').append(label);
    }

    /**
     * 保存触发条件
     */
    function saveSubTrigger() {
        var triggerAppFlowdefId=$('#triggerAppFlowdefId').find("option:selected").val();
        var triggerElementId=$('#node').find("option:selected").val();
        var triggerEvent=$('input[name="checkName"]:checked').val();
        var busRecordId='';
        var triggerId=$('#triggerId').text();
        var isOuterFlow=$('input[name="isOuterFlow"]:checked').val();
        if(!triggerAppFlowdefId){
            swal('提示信息', '请选择被触发的流程！', 'error');
            return;
        }
      if (triggerAppFlowdefId == currentAppFlowdefId) {
        swal('提示信息', '被触发的流程不可以是当前节点所属的流程，请选择其他流程！', 'error');
        return;
      }
        if(!triggerElementId){
            swal('提示信息', '请选择触发的节点!', 'error');
            return;
        }
        if(!triggerEvent){
            swal('提示信息', '请选择触发时机！', 'error');
            return;
        }
        if(currentBusiType=='stage'){
            // busRecordId=$('#busRecordId').find("option:selected").val();
            busRecordId=$('#busRecordId').val();
            /*if(!busRecordId){
                swal('提示信息', '请选择关联事项!', 'error');
                return;
            }*/
        }else{
            busRecordId=currentBusiId;
        }
        var data={
            "triggerId":triggerId,
            "appFlowdefId":currentAppFlowdefId,
            "triggerAppId":appId,
            "triggerElementId":triggerElementId,
            "triggerAppFlowdefId":triggerAppFlowdefId,
            "triggerEvent":triggerEvent,
            "busRecordId":busRecordId,
            "isOuterFlow":isOuterFlow
        };

        $.ajax({
            url: ctx + '/rest/mind/saveOrUpdateSubTrigger.do',
            type: 'POST',
            data: data,
            success: function (result) {
                if(result.success){
                    $('#subprocess_modal').modal("hide");
                    subprocessTabe.bootstrapTable('refresh');
                  // customTable_tree.bootstrapTable('refresh');
                  refresh();
                }else{
                    swal({
                        title: '提示信息',
                        text: result.message,
                        type: 'warning',
                        timer: 2000,
                        showConfirmButton: false
                    });
                }
            },
            error:function(){
                swal('错误信息', '服务器异常！', 'error');
            }
        });
    }

    //批量删除
    function batchDelSubTrigger() {
        var ids=[];
        var checkBoxs= $('#subprocess_list_tb').bootstrapTable('getSelections');
        var url=ctx +"/rest/mind/batchDelSubTrigger.do";
        if(checkBoxs!=null&&checkBoxs.length>0){
            for(var i=0;i<checkBoxs.length;i++){
                ids.push(checkBoxs[i].triggerId);
            }
            swal({
                title: '提示信息',
                text: '此操作将删除子流程触发配置,您确定删除吗?',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function(result) {
                if (result.value) {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        data:{'ids': ids.toString()},
                        success: function (result){
                            if(result.success){
                                swal({
                                    title: '提示信息',
                                    text: '批量删除成功!',
                                    type: 'success',
                                    timer: 1000,
                                    showConfirmButton: false
                                });
                                subprocessTabe.bootstrapTable('refresh');
                              // customTable_tree.bootstrapTable('refresh');
                              refresh();
                            }else{
                                swal('错误信息', result.message, 'error');
                            }
                        },
                        error:function(){
                            swal('错误信息', '服务器异常！', 'error');
                        }
                    });
                }
            });
        }else{
            swal('提示信息', '请选择操作数据！', 'info');
        }

    }

    //编辑配置
    function editSubTrigger(id) {
        //$('#subProcForm')[0].reset();
        $(':input','#subProcForm')
            .not(':button,:submit,:reset,:hidden')   //将myform表单中input元素type为button、submit、reset、hidden排除
            .val('')  //将input元素的value设为空值
            .removeAttr('checked')
            .removeAttr('selected');
        addSubProcess(1);
        $.ajax({
            url: ctx + '/rest/mind/getSubTriggerById.do',
            type: 'POST',
            data: {id:id},
            async:false,
            success: function (result) {
                $('#node').find('option[value=\"'+result.triggerElementId+'\"]').attr("selected","selected");
                // $('#busRecordId').find('option[value=\"'+result.busRecordId+'\"]').attr("selected","selected");
                $('#busRecordId').val(result.busRecordId);
                for (var i = 0; i < itemTreeData.length; i++) {
                  if (itemTreeData[i].id == $('#busRecordId').val()) {
                    //回显表单的事项名称
                    $('#busRecordName').val(itemTreeData[i].name);
                    if(selectItemTree){
                      //回显到事项树中
                      var node = selectItemTree.getNodeByParam("id", itemTreeData[i].id, null);
                      if (node) {
                        selectItemTree.checkNode(node, true, true, false);
                      }
                    }
                  }
                }
                $('#triggerId').html(result.triggerId);
                $('input[name="checkName"][value=\"'+result.triggerEvent+'\"]').attr("checked",true);
                $('input[name="isOuterFlow"][value=\"'+result.isOuterFlow+'\"]').prop("checked",true);
                isOuterFlow();
                $('#triggerAppFlowdefId').find('option[value=\"'+result.triggerAppFlowdefId+'\"]').attr("selected","selected");
            }
        });
    }

    /**
     * 模糊查询
     */
    function searchSubprocess() {
        showSubprocess(currentAppFlowdefId,currentDefKey);
    }

    function delSubTrigger(id){
        var url=ctx +"/rest/mind/delSubTrigger.do";
        swal({
            title: '提示信息',
            text: '此操作将删除子流程触发配置,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data:{'id': id},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            subprocessTabe.bootstrapTable('refresh');
                            $("#process_design_tb").bootstrapTable('refresh');       //无参数刷新
                          refresh();
                        }else{
                            swal('错误信息', result.message, 'error');
                        }
                    },
                    error:function(){
                        swal('错误信息', '服务器异常！', 'error');
                    }
                });
            }
        });
    }
</script>