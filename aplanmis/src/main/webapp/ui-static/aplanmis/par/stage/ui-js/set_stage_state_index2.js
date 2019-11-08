var key,
    nodeList = [],
    lastValue = "",
    stageStateTree = null,
    stageStateTreeSelectNode = null,  // 树选中节点
    add_stage_state_validator = null,
    add_stage_mat_validator = null,
    add_stage_cert_validator = null,
    commonQueryParams = {},
    stageStateTreeSetting = {
        edit: {
            enable: false,
            showRemoveBtn: false,//设置是否显示删除按钮
            showRenameBtn: false//设置是否显示编辑名称按钮
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId"
            }
        },
        view: {
            selectedMulti: false,//设置是否允许同时选中多个节点
            showTitle : false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: false, //设置 zTree 是否显示节点之间的连线。
            showHorizontal: false//设置是否水平平铺树（自定义属性）
        },
        callback:{
            onClick: stageStateTreeOnClick, //点击事件
            onRightClick: stageStateTreeOnRightClick, //右击事件
        }
    };

$(function(){

    $('#stageStateTree').css({'max-height': $('#westPanel').height()-116,'height': $('#westPanel').height()-116});

    $('#stage_rel_state_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-116
    });

    $('#stage_rel_mat_cert_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-116
    });

    $('#child_state_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-165
    });

    $('#state_rel_mat_cert_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-165
    });

    $('#select_state_global_mat_tb').bootstrapTable('resetView',{
        height: 450
    });

    // 关键字搜索事项节点
    key = $("#stageStateKeyWord");
    key.bind("focus", focusKey).bind("blur", blurKey).bind("change cut input propertychange",searchStageStateNode);
    key.bind('keydown', function (e){
        if(e.which == 13){
            searchStageStateNode();
        }
    });

    // 处理第一次加载数据
    if(isNeedState==1){ // 加载分情形数据
         $('#isNeedState1').prop('checked',true);
         $('#isNeedState0').prop('checked',false);
        $('#isNoStageStateDiv').hide();
        $('#isStageStateDiv').show();
        // 加载相关数据
        initStageStateTree();
        selectFirstStateNode();
        initRightShowPage();

    }else { // 加载不分情形数据
        $('#isNeedState1').prop('checked',false);
        $('#isNeedState0').prop('checked',true);
        $('#isNoStageStateDiv').show();
        $('#isStageStateDiv').hide();
        // 加载相关数据
        searchStageRelMatCert();
    }

    // 是否选择启用EL表达式
    $("#useEl").change(function(){

        if($(this).val()=='1'){
            $('#stateElDiv').show();
            $('#stateEl').rules("add", {
                required: true,
                messages: {
                    required: '<font color="red">此项必填！</font>',
                }
            });
        }else{
            $('#stateElDiv').hide();
            $('#stateEl').rules("remove");
        }
    });

    // 是否问题
    $('#isQuestion').change(function(){

        if($(this).val()=='1'){

            $('#mustAnswerDiv').show();
            $('#answerTypeDiv').show();

            $("#add_stage_state_form select[name='mustAnswer']").rules("add", {
                required: true,
                messages: {
                    required: '<font color="red">此项必填！</font>',
                }
            });

            $("#add_stage_state_form select[name='answerType']").rules("add", {
                required: true,
                messages: {
                    required: '<font color="red">此项必填！</font>',
                }
            });
        }else{

            $('#mustAnswerDiv').hide();
            $('#answerTypeDiv').hide();
            $("#add_stage_state_form select[name='mustAnswer']").rules("remove");
            $("#add_stage_state_form select[name='answerType']").rules("remove");
        }
    });

    // 设置初始化校验
    add_stage_state_validator = $('#add_stage_state_form').validate({

        // 定义校验规则
        rules: {
            stateName: {
                required: true,
                maxlength: 500
            },
            useEl:{
                required: true
            },
            sortNo:{
                required: true,
                maxlength: 38
            },
            isQuestion:{
                required: true
            }
        },
        messages: {
            stateName: {
                required: '<font color="red">此项必填!</font>',
                maxlength: "长度不能超过500个字母!"
            },
            useEl:{
                required: '<font color="red">此项必填!</font>'
            },
            sortNo:{
                required: '<font color="red">此项必填!</font>',
                maxlength: "长度不能超过38个字母!"
            },
            isQuestion:{
                required: '<font color="red">此项必填!</font>'
            }
        },
        // 提交表单
        submitHandler: function(form){

            var parentType = $('#add_stage_state_form input[name="parentType"]').val();
            var parentStateId = $('#add_stage_state_form input[name="parentStateId"]').val();
            var isTbEditStateNode = $('#add_stage_state_form input[name="isTbEditStateNode"]').val();

            var d = {};
            var t = $('#add_stage_state_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            if(parentType=='stage'){ // 阶段节点
               d['parentStateId'] = null
            }
            $.ajax({
                url: ctx + '/aea/par/state/saveAeaParState.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){
                        var stateContent = result.content;
                        var newNode = createNodeData(stateContent.parStateId,stateContent.stateName,'state',parentStateId,null);
                        if(newNode){
                            if(stageStateTreeSelectNode!=null&&stageStateTreeSelectNode!=''){
                                if(stageStateTreeSelectNode.id==newNode.id){ // 编辑下级节点
                                    stageStateTreeSelectNode.name = newNode.name;
                                    stageStateTree.updateNode(stageStateTreeSelectNode);
                                    swal({
                                        text: '编辑成功！',
                                        type: 'success',
                                        timer: 1500,
                                        showConfirmButton: false
                                    });
                                    if(isTbEditStateNode=='1'){
                                        //将节点设置为选中状态
                                        var node = stageStateTree.getNodeByParam("id",parentStateId, null);
                                        stageStateTree.selectNode(node);
                                        stageStateTreeSelectNode = node;
                                    }
                                    // 加载页面数据处理
                                    initRightShowPage();

                                }else{   // 新增下级节点
                                    stageStateTree.addNodes(stageStateTreeSelectNode,-1,newNode,true);
                                    swal({
                                        text: '操作成功!',
                                        type: 'success',
                                        showCancelButton: true,
                                        confirmButtonText: '继续新增情形',
                                        confirmButtonClass: "btn btn-success",
                                        cancelButtonText: '情形相关设置',
                                        cancelButtonClass: "btn btn-accent",
                                    }).then(function(result) {
                                        if (result.value) { // 继续新增情形
                                            // 继续新增情形
                                            addStageState();
                                        }else{ // 进入事项设置
                                            //将新增的节点设置为选中
                                            var node = stageStateTree.getNodeByParam("id",stateContent.parStateId, null);
                                            //将节点设置为选中状态
                                            stageStateTree.selectNode(node);
                                            stageStateTreeSelectNode = node;
                                            // 加载页面数据处理
                                            initRightShowPage();
                                        }
                                    });
                                }
                            }else{
                                stageStateTree.addNodes(null,-1,newNode,true);
                                swal({
                                    text: '操作成功!',
                                    type: 'success',
                                    showCancelButton: true,
                                    confirmButtonText: '继续新增情形',
                                    confirmButtonClass: "btn btn-success",
                                    cancelButtonText: '情形相关设置',
                                    cancelButtonClass: "btn btn-accent",
                                }).then(function(result) {
                                    if (result.value) { // 继续新增情形
                                        // 继续新增情形
                                        addStageState();
                                    }else{ // 进入事项设置
                                        //将新增的节点设置为选中
                                        var node = stageStateTree.getNodeByParam("id",stateContent.parStateId, null);
                                        //将节点设置为选中状态
                                        stageStateTree.selectNode(node);
                                        stageStateTreeSelectNode = node;
                                        // 加载页面数据处理
                                        initRightShowPage();
                                    }
                                });
                            }
                        }
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });

    add_stage_mat_validator = $("#add_stage_mat_form").validate({
        // 定义校验规则
        rules: {
            isGlobalShare:{
                required: true,
            },
            matTypeName:{
                required: true,
            },
            matName: {
                required: true,
                maxlength: 100
            },
            matCode: {
                required: true,
                maxlength: 50,
                remote: {
                    url: ctx+'/aea/item/mat/checkMatCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        matId: function(){
                            return $("#add_stage_mat_form input[name='matId']").val();
                        },
                        matCode: function() {
                            return $("#add_stage_mat_form input[name='matCode']").val();
                        }
                    }
                }
            }
        },
        messages: {
            isGlobalShare:{
                required: '<font color="red">此项必填！</font>',
            },
            matTypeName:{
                required: '<font color="red">此项必填！</font>',
            },
            matName: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!"
            },
            matCode: {
                required: '<font color="red">此项必填！</font>',
                maxlength: "长度不能超过50个字母!",
                remote: "编号已存在！",
            }
        },
        // 提交表单
        submitHandler: function () {

            var parentId = $('#add_stage_mat_form input[name="parentId"]').val();
            var isTbEditMatNode = $('#add_stage_mat_form input[name="isTbEditMatNode"]').val();

            var formData = new FormData(document.getElementById("add_stage_mat_form"));
            $.ajax({
                type: "POST",
                url: ctx+'/aea/par/in/saveStageStateMatIn.do',
                dataType: "json",
                cache: false,
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.success&&result.content){
                        var matContent = result.content;
                        var newNode = createNodeData(matContent.matId,matContent.aeaMatCertName,'mat',parentId,matContent.inId);
                        if(newNode){
                            if(stageStateTreeSelectNode!=null&&stageStateTreeSelectNode!=''){
                                if(stageStateTreeSelectNode.id==newNode.id){ // 编辑下级节点
                                    swal({
                                        text: '编辑成功！',
                                        type: 'success',
                                        timer: 1500,
                                        showConfirmButton: false
                                    });
                                    stageStateTreeSelectNode.name = newNode.name;
                                    stageStateTree.updateNode(stageStateTreeSelectNode);
                                    if(isTbEditMatNode=='1'){
                                        //将节点设置为选中状态
                                        var node = stageStateTree.getNodeByParam("id",parentId, null);
                                        stageStateTree.selectNode(node);
                                        stageStateTreeSelectNode = node;
                                    }
                                    // 加载页面数据处理
                                    initRightShowPage();
                                }else{   // 新增下级节点
                                    stageStateTree.addNodes(stageStateTreeSelectNode,-1,newNode,true);
                                    swal({
                                        text: '操作成功!',
                                        type: 'success',
                                        showCancelButton: true,
                                        confirmButtonText: '继续新增材料',
                                        confirmButtonClass: "btn btn-success",
                                        cancelButtonText: '进入材料详情',
                                        cancelButtonClass: "btn btn-accent",
                                    }).then(function(result) {
                                        if (result.value) { // 继续新增情形
                                            // 继续新增材料
                                            addStateMat();
                                        }else{ // 进入事项设置
                                            //将新增的节点设置为选中
                                            var node = stageStateTree.getNodeByParam("id",matContent.matId, null);
                                            //将节点设置为选中状态
                                            stageStateTree.selectNode(node);
                                            stageStateTreeSelectNode = node;
                                            // 加载页面数据处理
                                            initRightShowPage();
                                        }
                                    });
                                }
                            }else{
                                stageStateTree.addNodes(null,-1,newNode,true);
                                swal({
                                    text: '操作成功!',
                                    type: 'success',
                                    showCancelButton: true,
                                    confirmButtonText: '继续新增材料',
                                    confirmButtonClass: "btn btn-success",
                                    cancelButtonText: '进入材料详情',
                                    cancelButtonClass: "btn btn-accent",
                                }).then(function(result) {
                                    if (result.value) {
                                        // 继续新增材料
                                        addStateMat();
                                    }else{ // 进入材料详情
                                        //将新增的节点设置为选中
                                        var node = stageStateTree.getNodeByParam("id",parentId, null);
                                        //将节点设置为选中状态
                                        stageStateTree.selectNode(node);
                                        stageStateTreeSelectNode = node;
                                        // 加载页面数据处理
                                        initRightShowPage();
                                    }
                                });
                            }
                        }

                    } else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });

    add_stage_cert_validator = $("#add_stage_cert_form").validate({

        // 定义校验规则
        rules: {
            certName: {
                required: true,
                maxlength: 500
            },
            certCode: {
                required: true,
                maxlength: 50,
                remote: {
                    url: ctx+'/aea/cert/checkUniqueCertCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        certId: function(){
                            return $("#add_stage_cert_form input[name='certId']").val();
                        },
                        certCode: function() {
                            return $("#add_stage_cert_form input[name='certCode']").val();
                        }
                    }
                }
            },
            attDirName:{
                required: true,
            },
            certTypeId: {
                required: true,
            },
            certHolder:{
                maxlength: 1
            },
            softwareEnv:{
                maxlength: 1000
            },
            busAction:{
                maxlength: 1000
            },
            certMemo:{
                maxlength: 500
            }
        },
        messages: {
            certName: {
                required: '此项必填!',
                maxlength: "长度不能超过500个字母!"
            },
            certCode: {
                required: '此项必填!',
                maxlength: "长度不能超过50个字母!",
                remote: "编号已存在！",
            },
            attDirName:{
                required: '此项必填!',
            },
            certTypeId: {
                required: '此项必填!',
            },
            certHolder:{
                maxlength: "长度不能超过1个字母!"
            },
            softwareEnv:{
                maxlength: "长度不能超过1000个字母!"
            },
            busAction:{
                maxlength: "长度不能超过1000个字母!"
            },
            certMemo:{
                maxlength: "长度不能超过500个字母!"
            }
        },
        // 提交表单
        submitHandler: function(form){

            var certId = $("#add_stage_cert_form input[name='certId']").val();
            var parentId = $("#add_stage_cert_form input[name='parentId']").val();
            var isTbEditCertNode = $("#add_stage_cert_form input[name='isTbEditCertNode']").val();
            var d = {};
            var t = $('#add_stage_cert_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            $.ajax({
                url: ctx+'/aea/cert/saveAeaCert.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){
                        var certContent = result.content;
                        var newNode = createNodeData(certContent.certId,certContent.certName,'certId',parentId,null);
                        if(certId == certContent.certId){ // 更新
                            stageStateTreeSelectNode.name = newNode.name;
                            stageStateTree.updateNode(stageStateTreeSelectNode);
                            swal({
                                text: '操作成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            if(isTbEditCertNode=='1'){ // 表格编辑
                                //将节点设置为选中状态
                                var node = stageStateTree.getNodeByParam("id",parentId, null);
                                if(node){
                                    stageStateTree.selectNode(node);
                                    stageStateTreeSelectNode = node;
                                    initRightShowPage();
                                }
                            }else{
                                //将新增的节点设置为选中
                                var node = stageStateTree.getNodeByParam("id",certId, null);
                                //将节点设置为选中状态
                                if(node){
                                    stageStateTree.selectNode(node);
                                    stageStateTreeSelectNode = node;
                                    initRightShowPage();
                                }
                            }
                        }
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', '保存证照信息失败！', 'error');
                }
            });
        }
    });

    // 选择电子证照
    $('#selectCertBtn').click(function(){

        var certIds = [];
        var certNames = [];
        var liObjs = document.getElementsByName('selectCertLi');
        if(liObjs!=null&&liObjs.length>0){
            for(var i=0;i<liObjs.length;i++) {
                certIds.push($(liObjs[i]).attr('category-id'));
                certNames.push($(liObjs[i]).attr('category-name'));
            }
            var stageStateId = stageStateTreeSelectNode.id;
            $.ajax({
                url: ctx + '/aea/par/in/batchSaveStageStateCertIn.do',
                type: 'POST',
                data: {'stageId': stageId,'stageStateId': stageStateId,'certIds': certIds.toString()},
                async: false,
                success: function (result) {
                    if (result.success) {
                        swal({
                            text: '保存成功！',
                            type: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });
                        closeSelectCetrtZtree();
                        if(selectedStageStateRelCertData!=null&&selectedStageStateRelCertData.length>0){
                            for(var i=0;i<selectedStageStateRelCertData.length;i++){
                                var node = stageStateTree.getNodeByParam("id",selectedStageStateRelCertData[i], null);
                                stageStateTree.remove();
                            }
                        }
                        // 创建节点
                        if(certIds!=null&&certIds.length>0){
                            for(var i=0;i<certIds.length;i++){
                                var node = stageStateTree.getNodeByParam("id",certIds[i], null);
                                if(node==null){
                                    var newNode = createNodeData(certIds[i],certNames[i],'cert',stageStateId,null);
                                    stageStateTree.addNodes(stageStateTreeSelectNode,-1,newNode,true);
                                }
                            }
                        }
                        // 刷新材料证照列表
                        searchStateRelMatCert();
                    }else{
                        swal('错误信息', result.message ,'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }else{
            swal('提示信息', '请选择证照定义！', 'info');
        }
    });

    $('#selectBscAttDirBtn').click(function(){

        var bscAttDirTree = $.fn.zTree.getZTreeObj("selectBscAttDirTree");
        var nodes = bscAttDirTree.getSelectedNodes();
        if(nodes!=null&&nodes.length>0){
            var selectNode = nodes[0];
            if(certBscAttDirShowStyle=='noModal'){
                $("#add_stage_cert_form input[name='attDirId']").val(selectNode.id);
                $("#add_stage_cert_form input[name='attDirName']").val(selectNode.name);
            }else{
                $("#aedit_stage_cert_form input[name='attDirId']").val(selectNode.id);
                $("#aedit_stage_cert_form input[name='attDirName']").val(selectNode.name);
            }
            closeSelectBscAttDirZtree();
        }else{
            swal('提示信息', '请选择目录！', 'info');
        }
    });

    $('#selectMatTypeBtn').click(function(){

        var matTypeNodes = selectMatTypeTree.getSelectedNodes()
        if(matTypeNodes!=null&matTypeNodes.length>0){

            var matTypeId = matTypeNodes[0].id;
            var matTypeName = matTypeNodes[0].name;
            if(matTypeShowStyle=='noModal'){
                $('#add_stage_mat_form input[name="matTypeId"]').val(matTypeId);
                $('#add_stage_mat_form input[name="matTypeName"]').val(matTypeName);
            }else{
                $('#aedit_stage_mat_form input[name="matTypeId"]').val(matTypeId);
                $('#aedit_stage_mat_form input[name="matTypeName"]').val(matTypeName);
            }
            // 关闭窗口
            closeSelectMatTypeZtree();
        }
    });

    // 选择情形绑定阶段事项
    $('#selectStageItemBtn').click(function() {

        var liObjs = document.getElementsByName('selectStageItemLi');
        if (liObjs != null && liObjs.length > 0) {
            var parStateId = stageStateTreeSelectNode.id;
            $.ajax({
                url: ctx + '/aea/par/state/item/saveStateItemRel.do',
                type: 'POST',
                data: {'parStateId': parStateId, 'stageItemId': $(liObjs[0]).attr('category-id')},
                async: false,
                success: function (result) {
                    if (result.success) {
                        swal({
                            text: '保存成功！',
                            type: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });
                        closeSelectStageItemZtree();
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    });
});

// 加载树结构
function initStageStateTree(){

    $.ajax({
        url: ctx+'/aea/par/stage/syncListStageStateMat.do',
        type:'post',
        data:{'stageId': stageId},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0) {
                if (stageStateTree) stageStateTree.destroy();
                stageStateTree = $.fn.zTree.init($("#stageStateTree"), stageStateTreeSetting, data);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

// 默认选择第一个节点
function selectFirstStateNode(){

    var zTree = $.fn.zTree.getZTreeObj('stageStateTree');
    var nodes = zTree.getNodes();
    if(nodes!=null&&nodes.length>0){
        zTree.expandNode(nodes[0], true, false, false);
        stageStateTree.selectNode(nodes[0]);
        stageStateTreeSelectNode = nodes[0];
        initRightShowPage();
    }
}

// 处理右边显示
function initRightShowPage(){

    if(stageStateTreeSelectNode){
        if(stageStateTreeSelectNode.type=='stage'){ // 阶段

            // 展示页面
            $('#add_stage_state_page').hide();
            $('#stage_rel_state_page').show();
            $('#stage_rel_state_mat_page').hide();
            $('#add_stage_mat_page').hide();
            $('#add_stage_cert_page').hide();
            $('#stage_rel_state_title').html('情形列表');

            // 展示情形列表数据
            searchStageState();

        }else if(stageStateTreeSelectNode.type=='state'){ // 情形

            // 展示页面
            $('#add_stage_state_page').hide();
            $('#stage_rel_state_page').hide();
            $('#stage_rel_state_mat_page').show();
            $('#add_stage_mat_page').hide();
            $('#add_stage_cert_page').hide();

            // 加载展示数据
            $('#childStateInfo').trigger('click');

        }else if(stageStateTreeSelectNode.type=='mat'){

            // 展示页面
            $('#add_stage_state_page').hide();
            $('#stage_rel_state_page').hide();
            $('#stage_rel_state_mat_page').hide();
            $('#add_stage_mat_page').show();
            $('#add_stage_cert_page').hide();
            $('#add_stage_mat_header').html('编辑材料');
            // 初始化数据
            $('#add_stage_mat_form')[0].reset();
            add_stage_mat_validator.resetForm();
            $("#add_stage_mat_form input[name='matId']").val('');
            $("#add_stage_mat_form input[name='inId']").val('');
            $("#add_stage_mat_form input[name='isActive']").val('1');
            $("#add_stage_mat_form input[name='isGlobalShare']").val('0');
            $("#add_stage_state_form input[name='parentId']").val('');
            $("#add_stage_state_form input[name='isTbEditMatNode']").val('');

            $.ajax({
                type: 'post',
                url: ctx + '/aea/item/mat/getAeaItemMat.do',
                dataType: 'json',
                async: false,
                data: {'id': stageStateTreeSelectNode.id},
                success: function (data) {
                    if (data) {

                        $("#add_stage_mat_form input[name='inId']").val(stageStateTreeSelectNode.pName);
                        $("#add_stage_mat_form input[name='stageId']").val(stageId);
                        $("#add_stage_mat_form input[name='stageStateId']").val(stageStateTreeSelectNode.pId);
                        $("#add_stage_mat_form input[name='parentId']").val(stageStateTreeSelectNode.pId);

                        if(data.templateDoc!=null && data.templateDoc!=''){
                            $('#templateDocFile').addClass("hide");
                            $('#templateDocButton').removeClass("hide");
                        }else{
                            $('#templateDocFile').removeClass("hide");
                            $('#templateDocButton').addClass("hide");
                        }
                        if(data.sampleDoc!=null && data.sampleDoc!=''){
                            $('#sampleDocFile').addClass("hide");
                            $('#sampleDocButton').removeClass("hide");
                        }else{
                            $('#sampleDocFile').removeClass("hide");
                            $('#sampleDocButton').addClass("hide");
                        }
                        loadFormData(true, "#add_stage_mat_form", data);
                    } else {
                        swal('错误信息', "加载材料数据失败！", 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });

        }else if(stageStateTreeSelectNode.type=='cert'){

            // 展示页面
            $('#add_stage_state_page').hide();
            $('#stage_rel_state_page').hide();
            $('#stage_rel_state_mat_page').hide();
            $('#add_stage_mat_page').hide();
            $('#add_stage_cert_page').show();

            // 加载展示数据
            $('#add_stage_cert_form')[0].reset();
            add_stage_cert_validator.resetForm();
            $("#add_stage_cert_form input[name='certId']").val('');
            $("#add_stage_cert_form input[name='certTypeId']").val('');
            $("#add_stage_cert_form input[name='isTbEditCertNode']").val('0');
            $("#add_stage_cert_form input[name='parentId']").val(stageStateTreeSelectNode.pId);
            $.ajax({
                url: ctx + '/aea/cert/getAeaCert.do',
                type: 'post',
                data: {'id': stageStateTreeSelectNode.id},
                async: false,
                success: function (data) {
                    if (data) {
                        loadFormData(true,'#add_stage_cert_form',data);
                    }
                },
                error: function () {
                    swal('错误信息', "获取证照信息失败！", 'error');
                }
            });
        }
    }
}

// 节点单击事件
function stageStateTreeOnClick(event, treeId, treeNode, clickFlag) {

    if(treeNode){

        stageStateTree.selectNode(treeNode);
        stageStateTreeSelectNode = treeNode;
        initRightShowPage();
        expand();
    }
}

// 在ztree上的右击事件
function stageStateTreeOnRightClick(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();
    stageStateTree.selectNode(treeNode);
    stageStateTreeSelectNode = treeNode;
    initRightShowPage();

    if(event.target.tagName.toLowerCase()=='span'
        ||event.target.tagName.toLowerCase()=='a'
        ||event.target.tagName.toLowerCase()=='ul'){

        var y = event.clientY+10;
        var maxHeight = $('#stageStateTree').height();
        if(event.clientY>maxHeight){
            y -= ($('.contextMenuDiv').height()+5);
        }
        if(treeNode.type=='stage'){
            showRMenu('stageContextMenu',event.clientX+15, y);
        }else if(treeNode.type=='state'){
            showRMenu('stageStateContextMenu',event.clientX+15, y);
        }else{
            showRMenu('matCertContextMenu',event.clientX+15, y);
        }
    }
}

//展开下一级子节点
function expand(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');
    var selectNodes = stageStateTree.getSelectedNodes();
    stageStateTree.expandNode(selectNodes[0], true, null, null);
}

function focusKey(e) {

    if (key.hasClass("empty")) {
        key.removeClass("empty");
    }
}

function blurKey(e) {

    if (key.get(0).value === "") {
        key.addClass("empty");
    }
    searchItemInfoNode(e);
}

// 查询情形树节点
function searchStageStateNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#stageStateKeyWord').val());
    // 按名字查询
    var keyType = "name";
    // 如果和上次一样，就退出不查了。
    if (lastValue === value) {
        return;
    }
    // 保存最后一次
    lastValue = value;
    var nodes = stageStateTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showAllNode(nodes);
        return;
    }
    hideAllNode(nodes);
    nodeList = stageStateTree.getNodesByParamFuzzy(keyType, value);
    updateNodes(nodeList);
}

// 清空查询
function clearSearchStageStateNode(){

    // 清空查询内容
    $('#stageStateKeyWord').val('');
    showAllNode(stageStateTree.getNodes());
}

//隐藏所有节点
function hideAllNode(nodes){

    nodes = stageStateTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        stageStateTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showAllNode(nodes){

    nodes = stageStateTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            stageStateTree.expandNode(nodes[i],false,false,false,false);
        }else{
            stageStateTree.expandNode(nodes[i],true,true,false,false);
        }
        stageStateTree.showNode(nodes[i]);
        showAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        stageStateTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            stageStateTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                stageStateTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                stageStateTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            stageStateTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            stageStateTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 全部展开
function expandStageStateAll(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');
    stageStateTree.expandAll(true);
}

// 全部折叠
function collapseStageStateAll(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');
    stageStateTree.expandAll(false);
}

//  增加情形
function addStageState(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');

    $('#add_stage_state_page').show();
    $('#stage_rel_state_page').hide();
    $('#stage_rel_state_mat_page').hide();
    $('#state_rel_mat_page').hide();
    $('#add_stage_cert_page').hide();
    $('#add_stage_state_header').html('新增情形');

    // 初始化数据
    $('#add_stage_state_form')[0].reset();
    add_stage_state_validator.resetForm();
    $("#add_stage_state_form input[name='parStateId']").val('');
    $("#add_stage_state_form input[name='parentType']").val('');
    $("#add_stage_state_form input[name='parentStateId']").val('');
    $("#add_stage_state_form input[name='isTbEditItemNode']").val('');
    $("#isQuestion option:eq(1)").prop("selected", 'selected');
    $("#useEl option:eq(0)").prop("selected", 'selected');
    $('#mustAnswerDiv').hide();
    $('#answerTypeDiv').hide();
    $('#stateElDiv').hide();

    if(stageStateTreeSelectNode){

        $("#add_stage_state_form input[name='stageId']").val(stageId);
        $("#add_stage_state_form input[name='parentType']").val(stageStateTreeSelectNode.type);
        $("#add_stage_state_form input[name='parentStateId']").val(stageStateTreeSelectNode.id);
        $("#add_stage_state_form input[name='sortNo']").val('1');

    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 增加材料
function addStateMat(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');

    $('#add_stage_state_page').hide();
    $('#stage_rel_state_page').hide();
    $('#stage_rel_state_mat_page').hide();
    $('#add_stage_mat_page').show();
    $('#add_stage_cert_page').hide();
    $('#add_stage_mat_header').html('新增材料');

    // 初始化数据
    $('#add_stage_mat_form')[0].reset();
    add_stage_mat_validator.resetForm();
    $("#add_stage_mat_form input[name='matId']").val('');
    $("#add_stage_mat_form input[name='inId']").val('');
    $("#add_stage_mat_form input[name='isActive']").val('1');
    $("#isGlobalShare option:eq(0)").prop("selected", 'selected');
    $("#add_stage_state_form input[name='parentId']").val('');
    $("#add_stage_state_form input[name='isTbEditMatNode']").val('');
    initDoc();

    if(stageStateTreeSelectNode){

        $("#add_stage_mat_form input[name='stageId']").val(stageId);
        $("#add_stage_mat_form input[name='stageStateId']").val(stageStateTreeSelectNode.id);
        $("#add_stage_mat_form input[name='parentId']").val(stageStateTreeSelectNode.id);

    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 增加证照
function addStateCert(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');
    // 打开电子证照
    if(stageStateTreeSelectNode){
        loadStageStateRelCerts(stageId,stageStateTreeSelectNode.id);
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }

}

// 导入全局材料
function addGlobalMat(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');
    if(stageStateTreeSelectNode){
        $('#select_state_global_mat_modal').modal('show');
        $('#select_state_global_mat_modal_title').html('导入全局材料');
        searchStateGlobalMat();
    } else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 编辑情形
function editStageState(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');

    $('#add_stage_state_page').show();
    $('#stage_rel_state_page').hide();
    $('#stage_rel_state_mat_page').hide();
    $('#state_rel_mat_page').hide();
    $('#add_stage_cert_page').hide();
    $('#add_stage_state_header').html('编辑情形');

    // 初始化数据
    $('#add_stage_state_form')[0].reset();
    add_stage_state_validator.resetForm();
    $("#add_stage_state_form input[name='parStateId']").val('');
    $("#add_stage_state_form input[name='parentType']").val('');
    $("#add_stage_state_form input[name='parentStateId']").val('');
    $("#add_stage_state_form input[name='isTbEditItemNode']").val('');

    if(stageStateTreeSelectNode){
        $.ajax({
            url: ctx + '/aea/par/state/getAeaParState.do',
            type: 'post',
            data: {'id': stageStateTreeSelectNode.id},
            async: false,
            success: function (data) {
                // 记载表单数据
                loadFormData(true,'#add_stage_state_form',data);
                if(data){
                    if(data.parentStateId!=''&&data.parentStateId!=null&&data.parentStateId!=undefined){ // 不为空
                        $("#add_stage_state_form input[name='parentType']").val('state');
                        $("#add_stage_state_form input[name='parentStateId']").val(data.parentStateId);
                    }else{ //为空
                        $("#add_stage_state_form input[name='parentType']").val('stage');
                        $("#add_stage_state_form input[name='parentStateId']").val(stageId);
                    }
                }
            },
            error:function(){
                swal('错误信息', "获取情形信息失败！", 'error');
            }
        });
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 删除情形
function deleteStageState(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');

    if(stageStateTreeSelectNode){
        var selectNode = stageStateTreeSelectNode;
        swal({
            title: '此操作影响：',
            text: '此操作将删除情形以及相关材料数据,您确定删除吗？',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx + '/aea/par/state/deleteAeaParStateById.do',
                    type: 'post',
                    data: {'id': selectNode.id},
                    async: false,
                    success: function (data) {
                        if(data.success){
                            swal({
                                type: 'success',
                                title: '删除成功！',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            // 默认选中父级菜单
                            var parentNode = stageStateTreeSelectNode.getParentNode();
                            if(parentNode){
                                stageStateTree.selectNode(parentNode);
                                stageStateTreeSelectNode = parentNode;
                                // 父级节点展示数据
                                initRightShowPage();

                            }else{
                                stageStateTreeSelectNode = null;
                            }
                            // 移除节点
                            var node = stageStateTree.getNodeByParam("id", selectNode.id, null);
                            stageStateTree.removeNode(node);
                        }
                    },
                    error:function(){
                        swal('错误信息', "删除情形信息失败！", 'error');
                    }
                });
            }
        });
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 编辑材料
function editMatCert(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');

    $('#add_stage_state_page').hide();
    $('#stage_rel_state_page').hide();
    $('#stage_rel_state_mat_page').hide();

    if(stageStateTreeSelectNode){
        if(stageStateTreeSelectNode.type=='mat'){ // 材料

            $('#add_stage_mat_page').show();
            $('#add_stage_cert_page').hide();
            $('#add_stage_mat_header').html('编辑材料');

            // 初始化数据
            $('#add_stage_mat_form')[0].reset();
            add_stage_mat_validator.resetForm();
            $("#add_stage_mat_form input[name='matId']").val('');
            $("#add_stage_mat_form input[name='inId']").val('');
            $("#add_stage_mat_form input[name='isActive']").val('1');
            $("#add_stage_mat_form input[name='isGlobalShare']").val('0');
            $("#add_stage_state_form input[name='parentId']").val('');
            $("#add_stage_state_form input[name='isTbEditMatNode']").val('');

            $.ajax({
                type: 'post',
                url: ctx + '/aea/item/mat/getAeaItemMat.do',
                dataType: 'json',
                async: false,
                data: {'id': stageStateTreeSelectNode.id},
                success: function (data) {
                    if (data) {

                        $("#add_stage_mat_form input[name='inId']").val(stageStateTreeSelectNode.pName);
                        $("#add_stage_mat_form input[name='stageId']").val(stageId);
                        $("#add_stage_mat_form input[name='stageStateId']").val(stageStateTreeSelectNode.pId);
                        $("#add_stage_mat_form input[name='parentId']").val(stageStateTreeSelectNode.pId);

                        if(data.templateDoc!=null && data.templateDoc!=''){
                            $('#templateDocFile').addClass("hide");
                            $('#templateDocButton').removeClass("hide");
                        }else{
                            $('#templateDocFile').removeClass("hide");
                            $('#templateDocButton').addClass("hide");
                        }
                        if(data.sampleDoc!=null && data.sampleDoc!=''){
                            $('#sampleDocFile').addClass("hide");
                            $('#sampleDocButton').removeClass("hide");
                        }else{
                            $('#sampleDocFile').removeClass("hide");
                            $('#sampleDocButton').addClass("hide");
                        }
                        loadFormData(true, "#add_stage_mat_form", data);
                    } else {
                        swal('错误信息', "加载材料数据失败！", 'error');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }else{ // 电子证照
            $('#add_stage_mat_page').hide();
            $('#add_stage_cert_page').show();

            // 加载展示数据
            $('#add_stage_cert_form')[0].reset();
            add_stage_cert_validator.resetForm();
            $("#add_stage_cert_form input[name='certId']").val('');
            $("#add_stage_cert_form input[name='certTypeId']").val('');
            $("#add_stage_cert_form input[name='parentId']").val(stageStateTreeSelectNode.pId);
            $("#add_stage_cert_form input[name='isTbEditCertNode']").val('0');
            $.ajax({
                url: ctx + '/aea/cert/getAeaCert.do',
                type: 'post',
                data: {'id': stageStateTreeSelectNode.id},
                async: false,
                success: function (data) {
                    if (data) {
                        loadFormData(true,'#add_stage_cert_form',data);
                    }
                },
                error: function () {
                    swal('错误信息', "获取证照信息失败！", 'error');
                }
            });
        }
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 删除材料
function deleteMatCert(){

    hideRMenu('stageContextMenu');
    hideRMenu('stageStateContextMenu');
    hideRMenu('matCertContextMenu');

    if(stageStateTreeSelectNode){
        if(stageStateTreeSelectNode.type=='mat'){ // 材料
            var selectNode = stageStateTreeSelectNode;
            swal({
                title: '此操作影响：',
                text: '此操作材料数据,您确定删除吗？',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function(result) {
                if (result.value) {
                    $.ajax({
                        type: 'post',
                        url: ctx + '/aea/item/mat/deleteAeaItemMatById.do',
                        dataType: 'json',
                        data: {'id': selectNode.pName},
                        success: function (result) {
                            if (result.success) {
                                swal({
                                    type: 'success',
                                    title: '删除成功！',
                                    showConfirmButton: false,
                                    timer: 1000
                                });
                                // 默认选中父级菜单
                                var parentNode = stageStateTreeSelectNode.getParentNode();
                                if(parentNode){
                                    stageStateTree.selectNode(parentNode);
                                    stageStateTreeSelectNode = parentNode;
                                    // 父级节点展示数据
                                    initRightShowPage();
                                }else{
                                    stageStateTreeSelectNode = null;
                                }
                                // 移除节点
                                var node = stageStateTree.getNodeByParam("id", selectNode.id, null);
                                stageStateTree.removeNode(node);
                            }
                        },
                        error:function(){
                            swal('错误信息', "删除材料信息失败！", 'error');
                        }
                    });
                }
            });
        }else{ // 电子证照
            var selectNode = stageStateTreeSelectNode;
            swal({
                title: '此操作影响：',
                text: '此操作将删除证照数据,您确定删除吗？',
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(function(result) {
                if (result.value) {
                    $.ajax({
                        url: ctx + '/aea/cert/deleteAeaCertById.do',
                        type: 'post',
                        data: {'id': selectNode.pName},
                        async: false,
                        success: function (data) {
                            if(data.success){
                                swal({
                                    type: 'success',
                                    title: '删除成功！',
                                    showConfirmButton: false,
                                    timer: 1000
                                });
                                // 默认选中父级菜单
                                var parentNode = stageStateTreeSelectNode.getParentNode();
                                if(parentNode){
                                    stageStateTree.selectNode(parentNode);
                                    stageStateTreeSelectNode = parentNode;
                                    // 父级节点展示数据
                                    initRightShowPage();

                                }else{
                                    stageStateTreeSelectNode = null;
                                }
                                // 移除节点
                                var node = stageStateTree.getNodeByParam("id", selectNode.id, null);
                                stageStateTree.removeNode(node);
                            }
                        },
                        error:function(){
                            swal('错误信息', "删除证照信息失败！", 'error');
                        }
                    });
                }
            });
        }
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

// 返回主题版本定义
function backToThemeVer() {

    location.href = ctx+'/aea/par/theme/themeVerIndex.do?themeId='+themeId;
}

// 选择是否分情形
function selectIsNeedState(id){

   var isNeedStateValue = $(id).val();
   // 设置阶段数据
    $.ajax({
        type: 'post',
        url: ctx + '/aea/par/stage/updateAeaParStage.do',
        dataType: 'json',
        async: false,
        data: {'stageId': stageId,'isNeedState': isNeedStateValue},
        success: function (result) {

        },
        error: function(){
            // swal('提示信息', "请选择节点！", 'info');
        }
    });

   if(isNeedStateValue==1){ // 分情形

       $('#isNoStageStateDiv').hide();
       $('#isStageStateDiv').show();

       // 加载相关数据
       initStageStateTree();
       selectFirstStateNode();
       initRightShowPage();

   }else{ // 不分情形

       $('#isNoStageStateDiv').show();
       $('#isStageStateDiv').hide();

       // 加载数据
       searchStageRelMatCert();
   }
}

// 构建节点
function createNodeData(id,name,type,pId,pName){

    var node = {
        'id': id,
        'name': name,
        'type': type,
        'pId': pId,
        'pName': pName,
        'open': true,
        'isParent': true,
        'nocheck': true
    };
    return node;
}

// 需要设置情形数据
function loadStageStateRelCerts(stageId,stateId){

    if(stageId&&stateId){

        $('#selectCertKeyWord').val('');

        // 打开弹窗，加载树数据
        openSelectCertModal();

        // 取消上次的选中节点
        selectCertTree.checkAllNodes(false);

        // 清空右侧选中事项数据
        $("#selectedCertUl").html("");

        //加载已选择数据
        loadSelectedStageStateRelCertData(stageId,stateId);

    }else{
        swal('提示信息','操作对象id为空!','info');
    }
}

// 加载已经选择的电子证照
var selectedStageStateRelCertData = [];
function loadSelectedStageStateRelCertData(stageId,stateId){

    // 勾选和渲染已经选择的电子证照
    $.ajax({
        url: ctx + '/aea/par/state/listInStateCertByStageIdAndStateId.do',
        type: 'post',
        data: {'stageId':stageId ,'stateId': stateId},
        async: false,
        success: function (data) {
            if(data!=null&&data.length>0){
                for(var i=0;i<data.length;i++) {
                    selectedStageStateRelCertData.push(data[i].certId);
                    // 选择电子证照库树节点
                    var node = selectCertTree.getNodeByParam("id", data[i].certId, null);
                    if (node) {
                        selectCertTree.checkNode(node, true, true, false);
                    }
                    // 加载右侧数据，已经选择的电子证照库
                    var liHtml = '<li name="selectCertLi" category-id="' + data[i].certId + '" category-id="' + data[i].aeaMatCertName + '">' +
                                     '<span class="drag-handle_td" ' +
                                     'onclick="removeSelectedCert(\'' + data[i].certId + '\');">×</span>' +
                                     '<span class="org_name_td">' + data[i].aeaMatCertName + '</span>' +
                                 '</li>';
                    $('#selectedCertUl').append(liHtml);
                }
            }
        }
    });
}

// 初始化上传文件显示
function initDoc() {

    $('#templateDocFile').removeClass("hide");
    $('#templateDocButton').addClass("hide");
    $('#sampleDocFile').removeClass("hide");
    $('#sampleDocButton').addClass("hide");
}


function downloadDoc(type) {

    var fileId = null;
    if(type==0){
        fileId = $('#templateDoc').val();
    }else if(type==1){
        fileId = $('#sampleDoc').val();
    }
    window.location.href = ctx+'/aea/item/mat/downloadGlobalMatDoc.do?detailId=' + fileId;
}


function deleteDoc(type) {

    var data = {};
    data.type = type;
    data.matId = $('#add_stage_mat_form input[name="matId"]').val();
    if(type==0){
        data.detailId = $('#templateDoc').val();
    }else if(type==1){
        data.detailId = $('#sampleDoc').val();
    }
    $.ajax({

        type: 'post',
        url: ctx+'/aea/item/mat/deleteGlobalMatDoc.do',
        data: data,
        success: function (result) {
            if (result.success) {
                swal({
                    type: 'success',
                    title: '删除成功！',
                    showConfirmButton: false,
                    timer: 1500
                });
                if(type==0){
                    $('#templateDocButton').addClass("hide");
                    $('#templateDocFile').removeClass("hide");
                    $('#templateDoc').val('');
                }else if(type==1){
                    $('#sampleDocButton').addClass("hide");
                    $('#sampleDocFile').removeClass("hide");
                    $('#sampleDoc').val('');
                }
            }else {
                swal('错误信息', result.message, 'error');
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            swal('错误信息', XMLHttpRequest.responseText, 'error');
        }
    });
}

// 情形绑定事项
function setStageItemInfo(){

    if(stageStateTreeSelectNode){
        openSelectStageItemModal(stageStateTreeSelectNode.id);
    }else{
        swal('提示信息', "请选择节点！", 'info');
    }
}

