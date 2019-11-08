var stateId;//情形id

//关联情形对应事项
angular.module('kityminderEditor')
    .directive('linkItem4situation', ['commandBinder','$modal', function(commandBinder,$modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/linkItem4situation/linkItem4situation.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function($scope) {
                var ywqxId = UrlParam.param('ywqxId');
                var urlList = WEB_ROOT + '/affairItemMaterialMain/all/list';
                var minder = $scope.minder;
                commandBinder.bind(minder, 'nodeSelectModCmd', $scope);
                urlList=ctx+'/rest/mind'+ '/affairItemMaterialMain/all/list.do';;
                $scope.addHyperlink = function () {

                    var minder = $scope.minder;
                    var nodes = minder.getSelectedNodes();
                    var clNodeList = nodes[0].children;
                    var clNodeIds = [];
                    var currentNode=minder.getSelectedNode();
                    var currentData=currentNode.data;

                    if(curIsEditable) {
                        $('#btn_save_stage_state_item_list_tb').show();
                    }else{
                        $('#btn_save_stage_state_item_list_tb').hide();
                        swal({
                            type: 'info',
                            title: '当前版本下数据不可编辑！',
                            showConfirmButton: false,
                            timer: 1500
                        });
                    }

                    // if(curIsEditable) {
                        // 阶段
                        if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {
                            if(currentData.nodeTypeCode==AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION){
                                if (currentData.operatorTag === 'new') {
                                    agcloud.ui.metronic.showSwal({type: 'info', message: '请先保存当前情形!'});
                                    return;
                                }
                                viewStageItem(currentBusiId,currentNode.data.id);
                                $("#btn_save_stage_state_item_list_tb").unbind("click")
                                $("#btn_save_stage_state_item_list_tb").click(function (e) {
                                    var datas = $('#stage_state_item_list_tb').bootstrapTable('getSelections');//获取选中的数据
                                    examine(e.type, datas);
                                    saveStateItemDialogClose();
                                    var dataArr = saveStateItemAndReturn();
                                    var nodeEntity=dataArr[0];
                                    var linkInfo='';
                                    if(dataArr!=null&&dataArr.length>0){
                                        for(var i=0;i<dataArr.length;i++){
                                            nodeEntity=dataArr[i];
                                            linkInfo+=nodeEntity.name;
                                            if(i<dataArr.length-1){
                                                linkInfo+=',';
                                            }
                                        }
                                        currentNode.setData("text",getNodeText(currentData.name,linkInfo));
                                    }
                                })
                            }else{
                                agcloud.ui.metronic.showSwal({type: 'info', message: '仅支持在情形下关联情形对应事项!'});
                            }
                        }else{
                            agcloud.ui.metronic.showSwal({type: 'info', message: '仅支持在情形下关联情形对应事项!'});
                        }
                    // }else{
                    //     agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                    // }
                }
            }
        }
    }]);

if(handWay=='1') {
//关联情形对应表单
    angular.module('kityminderEditor')
        .directive('linkForm4situation', ['commandBinder', '$modal', function (commandBinder, $modal) {
            return {
                restrict: 'E',
                templateUrl: 'ui/directive/linkForm4situation/linkForm4situation.html',
                scope: {
                    minder: '='
                },
                replace: true,
                link: function ($scope) {
                    var ywqxId = UrlParam.param('ywqxId');
                    var urlList = WEB_ROOT + '/affairItemMaterialMain/all/list';
                    urlList = ctx + '/rest/mind' + '/affairItemMaterialMain/all/list.do';
                    ;
                    $scope.addHyperlink = function () {

                        var minder = $scope.minder;
                        var nodes = minder.getSelectedNodes();
                        var clNodeList = nodes[0].children;
                        var clNodeIds = [];
                        var currentNode = minder.getSelectedNode();
                        var currentData = currentNode.data;

                        if (curIsEditable) {

                            $("#btn_save_stage_state_form_list_tb").show();
                            $("#btn_save_item_form_list_tb").show();
                        } else {

                            $("#btn_save_stage_state_form_list_tb").hide();
                            $("#btn_save_item_form_list_tb").hide();
                            swal({
                                type: 'info',
                                title: '当前版本下数据不可编辑！',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        }

                        // if(curIsEditable) {
                        // 阶段
                        if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {
                            if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) {
                                if (currentData.operatorTag === 'new') {
                                    agcloud.ui.metronic.showSwal({type: 'info', message: '请先保存当前情形!'});
                                    return;
                                }
                                viewStateForm(currentBusiId, currentNode.data.id, "state", true);
                                $("#btn_save_stage_state_form_list_tb").unbind("click")
                                $("#btn_save_stage_state_form_list_tb").click(function (e) {
                                    var datas = $('#stage_state_form_list_tb').bootstrapTable('getSelections');//获取选中的数据
                                    examineStateForm(e.type, datas);
                                    saveStateFormDialogClose();
                                    var dataArr = saveStateFormAndReturn();
                                    var nodeEntity = dataArr[0];
                                    var linkInfo = '';
                                    if (dataArr != null && dataArr.length > 0) {
                                        for (var i = 0; i < dataArr.length; i++) {
                                            nodeEntity = dataArr[i];
                                            linkInfo += nodeEntity.name;
                                            if (i < dataArr.length - 1) {
                                                linkInfo += ',';
                                            }
                                        }
                                        currentNode.setData("text", getNodeText(currentData.name, linkInfo));
                                    }
                                })
                            } else if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) { // 阶段挂通用表单
                                if (currentData.operatorTag === 'new') {
                                    agcloud.ui.metronic.showSwal({type: 'info', message: '请先保存当前情形!'});
                                    return;
                                }

                                viewStateForm(currentBusiId, currentNode.data.id, "stage", true);
                                $("#btn_save_stage_state_form_list_tb").unbind("click")
                                $("#btn_save_stage_state_form_list_tb").click(function (e) {
                                    var datas = $('#stage_state_form_list_tb').bootstrapTable('getSelections');//获取选中的数据
                                    examineStateForm(e.type, datas);
                                    saveStateFormDialogClose();
                                    var dataArr = saveStateFormAndReturn();
                                    var nodeEntity = dataArr[0];
                                    var linkInfo = '';
                                    if (dataArr != null && dataArr.length > 0) {
                                        for (var i = 0; i < dataArr.length; i++) {
                                            nodeEntity = dataArr[i];
                                            linkInfo += nodeEntity.name;
                                            if (i < dataArr.length - 1) {
                                                linkInfo += ',';
                                            }
                                        }
                                        currentNode.setData("text", getNodeText(currentData.name, linkInfo));
                                    }
                                })
                            } else {
                                agcloud.ui.metronic.showSwal({type: 'info', message: '不支持在当前节点下添加表单!'});
                            }
                        } else if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) { // 事项

                            if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) { // 情形节点

                                if (currentData.operatorTag === 'new') {
                                    agcloud.ui.metronic.showSwal({type: 'info', message: '请先保存当前情形!'});
                                    return;
                                }

                                vieItemForm(currentNode.data.id, "state", true);
                                $("#btn_save_item_form_list_tb").unbind("click")
                                $("#btn_save_item_form_list_tb").click(function (e) {
                                    var datas = $('#item_form_list_tb').bootstrapTable('getSelections');//获取选中的数据
                                    examineItemForm(e.type, datas);
                                    saveItemFormDialogClose();
                                    var dataArr = saveItemFormAndReturn();
                                    var nodeEntity = dataArr[0];
                                    var linkInfo = '';
                                    if (dataArr != null && dataArr.length > 0) {
                                        for (var i = 0; i < dataArr.length; i++) {
                                            nodeEntity = dataArr[i];
                                            linkInfo += nodeEntity.name;
                                            if (i < dataArr.length - 1) {
                                                linkInfo += ',';
                                            }
                                        }
                                        currentNode.setData("text", getNodeText(currentData.name, linkInfo));
                                    }
                                });

                            } else if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) { // 事项挂通用表单

                                if (currentData.operatorTag === 'new') {
                                    agcloud.ui.metronic.showSwal({type: 'info', message: '请先保存当前情形!'});
                                    return;
                                }

                                vieItemForm(null, "item", true);
                                $("#btn_save_item_form_list_tb").unbind("click")
                                $("#btn_save_item_form_list_tb").click(function (e) {
                                    var datas = $('#item_form_list_tb').bootstrapTable('getSelections');//获取选中的数据
                                    examineItemForm(e.type, datas);
                                    saveItemFormDialogClose();
                                    var dataArr = saveItemFormAndReturn();
                                    var nodeEntity = dataArr[0];
                                    var linkInfo = '';
                                    if (dataArr != null && dataArr.length > 0) {
                                        for (var i = 0; i < dataArr.length; i++) {
                                            nodeEntity = dataArr[i];
                                            linkInfo += nodeEntity.name;
                                            if (i < dataArr.length - 1) {
                                                linkInfo += ',';
                                            }
                                        }
                                        currentNode.setData("text", getNodeText(currentData.name, linkInfo));
                                    }
                                });
                            } else {
                                agcloud.ui.metronic.showSwal({type: 'info', message: '不支持在当前节点下添加表单!'});
                            }
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'info', message: '不支持在当前节点下添加表单!'});
                        }
                        // }else{
                        //     agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                        // }
                    }
                }
            }
        }]);

    //关联材料证照对应事项
    angular.module('kityminderEditor')
        .directive('linkItem4mat', ['commandBinder', '$modal', function (commandBinder, $modal) {
            return {
                restrict: 'E',
                templateUrl: 'ui/directive/linkItem4mat/linkItem4mat.html',
                scope: {
                    minder: '='
                },
                replace: true,
                link: function ($scope) {
                    var ywqxId = UrlParam.param('ywqxId');
                    var urlList = WEB_ROOT + '/affairItemMaterialMain/all/list';
                    urlList = ctx + '/rest/mind' + '/affairItemMaterialMain/all/list.do';
                    ;
                    $scope.addHyperlink = function () {

                        var minder = $scope.minder;
                        var nodes = minder.getSelectedNodes();
                        var clNodeList = nodes[0].children;
                        var clNodeIds = [];
                        var currentNode = minder.getSelectedNode();
                        var currentData = currentNode.data;

                        if (curIsEditable) {
                            $('#saveStateMatCertItem').show();
                        } else {
                            $('#saveStateMatCertItem').hide();
                            swal({
                                type: 'info',
                                title: '当前版本下数据不可编辑！',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        }

                        // if(curIsEditable) {
                        if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_MAT ||
                            currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_CERT) {
                            var parentNode = currentNode.parent;
                            if (parentNode) {
                                var parentId = parentNode.data.id;
                                // alert(currentNode.data.id);
                                viewStageMatItem(currentBusiId, parentId, currentNode.data.id, currentData.nodeTypeCode, null, parentNode.data.nodeTypeCode);
                            }
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'info', message: '不支持在当前节点下关联事项!'});
                        }
                        // }else{
                        //     agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                        // }
                    }
                }
            }
        }]);

//添加阶段材料
    angular.module('kityminderEditor')
        .directive('addMat4stage', ['commandBinder', '$modal', function (commandBinder, $modal) {
            return {
                restrict: 'E',
                templateUrl: 'ui/directive/addMat4stage/addMat4stage.html',
                scope: {
                    minder: '='
                },
                replace: true,
                link: function ($scope) {
                    var ywqxId = UrlParam.param('ywqxId');
                    var urlList = WEB_ROOT + '/affairItemMaterialMain/all/list';
                    urlList = ctx + '/rest/mind' + '/affairItemMaterialMain/all/list.do';
                    $scope.addHyperlink = function () {

                        var minder = $scope.minder;
                        var nodes = minder.getSelectedNodes();
                        var clNodeList = nodes[0].children;
                        var clNodeIds = [];
                        var currentNode = minder.getSelectedNode();
                        var currentData = currentNode.data;

                        if (curIsEditable) {
                            if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                            } else {
                                agcloud.ui.metronic.showSwal({type: 'info', message: '仅支持在阶段下添加阶段材料!'});
                            }
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                        }
                    }
                }
            }
        }]);

//添加情形材料
    angular.module('kityminderEditor')
        .directive('addMat4situation', ['commandBinder', '$modal', function (commandBinder, $modal) {
            return {
                restrict: 'E',
                templateUrl: 'ui/directive/addMat4situation/addMat4situation.html',
                scope: {
                    minder: '='
                },
                replace: true,
                link: function ($scope) {
                    $scope.addHyperlink = function () {

                        var minder = $scope.minder;
                        var nodes = minder.getSelectedNodes();
                        var clNodeList = nodes[0].children;
                        var clNodeIds = [];
                        var currentNode = minder.getSelectedNode();
                        var currentData = currentNode.data;

                        if (curIsEditable) {

                            if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION
                                || currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM
                                || currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                                if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                                    if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                                        viewStageStateMatList(null);

                                    } else {

                                        viewStageStateMatList(currentData.id);
                                    }

                                } else if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {

                                    if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {

                                        stageItemStateId = '';
                                        $('#itemMatModalTitle').html('通用材料列表');
                                        $('#itemStateMatLi').hide();
                                        $('#itemCommonMatLi').show();
                                        $('#itemCommonMatLi').hide();

                                    } else {

                                        stageItemStateId = currentData.id;
                                        $('#itemMatModalTitle').html('情形材料列表');
                                        $('#itemStateMatLi').show();
                                        $('#itemCommonMatLi').hide();
                                        $('#itemStateMatLi').hide();
                                    }

                                    $('#itemMatModal').modal('show');
                                    $('#item_mat_tab_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                                    $('#item_mat_common_tab_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
                                    loadData4ItemMatModalWhenOpening(stageItemStateId, currentBusiId);

                                } else {
                                    agcloud.ui.metronic.showSwal({type: 'info', message: '不支持在当前节点下添加材料!'});
                                }
                            } else {
                                agcloud.ui.metronic.showSwal({type: 'info', message: '不支持在当前节点下添加材料!'});
                            }
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                        }
                    }
                }
            }
        }
        ]);

//添加全局材料
    angular.module('kityminderEditor')
        .directive('importMat', ['commandBinder', '$modal', function (commandBinder, $modal) {
            return {
                restrict: 'E',
                templateUrl: 'ui/directive/importMat/importMat.html',
                scope: {
                    minder: '='
                },
                replace: true,
                link: function ($scope) {
                    $scope.addHyperlink = function () {

                        var minder = $scope.minder;
                        var nodes = minder.getSelectedNodes();
                        var clNodeList = nodes[0].children;
                        var clNodeIds = [];
                        var currentNode = minder.getSelectedNode();
                        var currentData = currentNode.data;

                        if (curIsEditable) {
                            if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) {

                                $('#item_overmat_import').modal('show');
                                $('#item_overmat_import_title').html('导入库材料');
                                stateId = currentData.id;
                                loadFormFieldData(1, "", stateId, currentBusiType);

                                $("#btn_mat_global_select").unbind("click")
                                $("#btn_mat_global_select").click(function () {
                                    globalMatDialogClose();
                                    var dataArr = [{'id': 'cailiaoA', 'name': '材料A'}];
                                    dataArr = getGlobalMatChoose();
                                    var nodeEntity = dataArr[0];
                                    var data = {};
                                    data.name = nodeEntity.name
                                    data.text = nodeEntity.name
                                    data.nodeTypeCode = AeaMindConst_MIND_NODE_TYPE_CODE_MAT;
                                    data.priority = AeaMindConst_MIND_NODE_PRIORITY_MAPPING_MAT;
                                    data.operatorTag = MindConst_MIND_NODE_OPERATOR_TAG_NEW;
                                    minder.execCommand("appendchildnode", data);
                                    var node = minder.getSelectedNodes();
                                    node[0].data.id = nodeEntity.id;
                                    data.isGlobal = '1';
                                })
                            } else {
                                agcloud.ui.metronic.showSwal({type: 'info', message: '仅支持在情形下导入库材料!'});
                            }
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                        }
                    }
                }
            }
        }]);

//添加电子证照
    angular.module('kityminderEditor')
        .directive('importCert', ['commandBinder', '$modal', function (commandBinder, $modal) {
            return {
                restrict: 'E',
                templateUrl: 'ui/directive/importCert/importCert.html',
                scope: {
                    minder: '='
                },
                replace: true,
                link: function ($scope) {
                    var ywqxId = UrlParam.param('ywqxId');
                    var urlList = ctx + '/rest/mind' + '/affairItemMaterialMain/all/list.do';
                    $scope.addHyperlink = function () {

                        var minder = $scope.minder;
                        var nodes = minder.getSelectedNodes();
                        var clNodeList = nodes[0].children;
                        var clNodeIds = [];
                        var currentNode = minder.getSelectedNode();
                        var currentData = currentNode.data;

                        if (curIsEditable) {

                            // 情形
                            if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE && (
                                    currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION
                                    || currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) ||
                                currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM && (
                                    currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION
                                    || currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM)) {

                                $('#selectCertKeyWord').val("");
                                $('#cert_import_modal').modal('show');
                                $('#cert_import_modal_title').html('电子证照导入');
                                $.ajax({
                                    url: ctx + '/aea/cert/gtreeTypeCert.do',
                                    type: 'post',
                                    data: {},
                                    async: false,
                                    dataType: 'json',
                                    success: function (data) {
                                        if (data != null && data.length > 0) {
                                            selectCertTree = $.fn.zTree.init($("#selectCertTree"), selectCertTreeSetting, data);
                                        }
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                                        swal('错误信息', '初始化电子证照数据异常,错误信息:' + XMLHttpRequest.responseText, 'error');
                                    }
                                });

                                // 阶段
                                if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                                    if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) { // 情形节点

                                        // 取消上次的选中节点
                                        selectCertTree.checkAllNodes(false);
                                        $("#selectedCertUl").html("");
                                        $.ajax({
                                            url: ctx + '/aea/par/in/getCertListByStateId.do',
                                            type: 'POST',
                                            data: {"parStateId": currentData.id},
                                            async: false,
                                            success: function (data) {
                                                if (data != null && data.length > 0) {
                                                    for (var i = 0; i < data.length; i++) {
                                                        // 选择电子证照库树节点
                                                        var node = selectCertTree.getNodeByParam("id", data[i], null);
                                                        if (node) {
                                                            selectCertTree.checkNode(node, true, true, false);
                                                        }
                                                        // 加载右侧数据，已经选择的电子证照库
                                                        var liHtml = '<li name="selectCertLi" category-id="' + data[i] + '">' +
                                                            '<span class="drag-handle_td" ' +
                                                            'onclick="removeSelectedCert(\'' + data[i] + '\');">×</span>' +
                                                            '<span class="org_name_td">' + node.name + '</span>' +
                                                            '</li>';
                                                        $('#selectedCertUl').append(liHtml);
                                                    }
                                                }
                                            },
                                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                                swal('错误信息', '加载右侧已经选择的电子证照数据异常，错误信息:' + XMLHttpRequest.responseText, 'error');
                                            }
                                        });

                                    } else if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) { // 阶段节点

                                        // 取消上次的选中节点
                                        selectCertTree.checkAllNodes(false);
                                        $("#selectedCertUl").html("");
                                        $.ajax({
                                            url: ctx + '/aea/par/in/listNoStateCertByStageId.do',
                                            type: 'POST',
                                            data: {
                                                "stageId": currentBusiId,
                                                "isStateIn": "0",
                                                "fileType": 'cert',
                                                "isDeleted": '0'
                                            },
                                            async: false,
                                            success: function (data) {
                                                if (data != null && data.length > 0) {
                                                    for (var i = 0; i < data.length; i++) {
                                                        // 选择电子证照库树节点
                                                        var node = selectCertTree.getNodeByParam("id", data[i], null);
                                                        if (node) {
                                                            selectCertTree.checkNode(node, true, true, false);
                                                        }
                                                        // 加载右侧数据，已经选择的电子证照库
                                                        var liHtml = '<li name="selectCertLi" category-id="' + data[i] + '">' +
                                                            '<span class="drag-handle_td" ' +
                                                            'onclick="removeSelectedCert(\'' + data[i] + '\');">×</span>' +
                                                            '<span class="org_name_td">' + node.name + '</span>' +
                                                            '</li>';
                                                        $('#selectedCertUl').append(liHtml);
                                                    }
                                                }
                                            },
                                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                                swal('错误信息', '加载右侧已经选择的电子证照数据异常，错误信息:' + XMLHttpRequest.responseText, 'error');
                                            }
                                        });
                                    } else {
                                        agcloud.ui.metronic.showSwal({type: 'info', message: '当前下导入电子证照!'});
                                    }
                                    // 事项
                                } else if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {

                                    if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) { // 情形节点

                                        // 取消上次的选中节点
                                        selectCertTree.checkAllNodes(false);
                                        $("#selectedCertUl").html("");
                                        $.ajax({
                                            url: ctx + '/aea/item/inout/listItemOrStateCerts.do',
                                            type: 'POST',
                                            data: {
                                                "itemVerId": currentBusiId,
                                                "stateVerId": currentStateVerId,
                                                "isStateIn": "1",
                                                "itemStateId": currentData.id,
                                            },
                                            async: false,
                                            success: function (data) {
                                                if (data != null && data.length > 0) {
                                                    for (var i = 0; i < data.length; i++) {
                                                        // 选择电子证照库树节点
                                                        var node = selectCertTree.getNodeByParam("id", data[i], null);
                                                        if (node) {
                                                            selectCertTree.checkNode(node, true, true, false);
                                                        }
                                                        // 加载右侧数据，已经选择的电子证照库
                                                        var liHtml = '<li name="selectCertLi" category-id="' + data[i] + '">' +
                                                            '<span class="drag-handle_td" ' +
                                                            'onclick="removeSelectedCert(\'' + data[i] + '\');">×</span>' +
                                                            '<span class="org_name_td">' + node.name + '</span>' +
                                                            '</li>';
                                                        $('#selectedCertUl').append(liHtml);
                                                    }
                                                }
                                            },
                                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                                swal('错误信息', '加载右侧已经选择的电子证照数据异常，错误信息:' + XMLHttpRequest.responseText, 'error');
                                            }
                                        });

                                    } else if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) { // 事项节点

                                        // 取消上次的选中节点
                                        selectCertTree.checkAllNodes(false);
                                        $("#selectedCertUl").html("");
                                        $.ajax({
                                            url: ctx + '/aea/item/inout/listItemOrStateCerts.do',
                                            type: 'POST',
                                            data: {
                                                "itemVerId": currentBusiId,
                                                "stateVerId": currentStateVerId,
                                                "isStateIn": "0",
                                            },
                                            async: false,
                                            success: function (data) {
                                                if (data != null && data.length > 0) {
                                                    for (var i = 0; i < data.length; i++) {
                                                        // 选择电子证照库树节点
                                                        var node = selectCertTree.getNodeByParam("id", data[i], null);
                                                        if (node) {
                                                            selectCertTree.checkNode(node, true, true, false);
                                                        }
                                                        // 加载右侧数据，已经选择的电子证照库
                                                        var liHtml = '<li name="selectCertLi" category-id="' + data[i] + '">' +
                                                            '<span class="drag-handle_td" ' +
                                                            'onclick="removeSelectedCert(\'' + data[i] + '\');">×</span>' +
                                                            '<span class="org_name_td">' + node.name + '</span>' +
                                                            '</li>';
                                                        $('#selectedCertUl').append(liHtml);
                                                    }
                                                }
                                            },
                                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                                swal('错误信息', '加载右侧已经选择的电子证照数据异常，错误信息:' + XMLHttpRequest.responseText, 'error');
                                            }
                                        });
                                    }
                                }

                                $("#selectCertBtn").unbind("click");
                                $('#selectCertBtn').click(function () {
                                    var certIds = [];
                                    var liObjs = document.getElementsByName('selectCertLi');
                                    if (liObjs != null && liObjs.length > 0) {
                                        for (var i = 0; i < liObjs.length; i++) {
                                            certIds.push($(liObjs[i]).attr('category-id'));
                                        }
                                    }
                                    var getBatchSaveCertUrl = null;
                                    var getBatchSaveCertData = {};

                                    if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {
                                        // 情形
                                        if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) {

                                            getBatchSaveCertUrl = ctx + '/aea/item/inout/batchSaveItemInoutMatCert.do';
                                            getBatchSaveCertData = {
                                                'itemVerId': currentBusiId,
                                                'stateVerId': currentStateVerId,
                                                'isOwner': '1',
                                                'isInput': '1',
                                                'isStateIn': '1',
                                                'itemStateId': currentData.id,
                                                'fileType': 'cert',
                                                'matCertIds': certIds.toString(),
                                            }
                                            // 事项节点
                                        } else if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {

                                            getBatchSaveCertUrl = ctx + '/aea/item/inout/batchSaveItemInoutMatCert.do';
                                            getBatchSaveCertData = {
                                                'itemVerId': currentBusiId,
                                                'stateVerId': currentStateVerId,
                                                'isOwner': '1',
                                                'isInput': '1',
                                                'isStateIn': '0',
                                                'fileType': 'cert',
                                                'matCertIds': certIds.toString(),
                                            }
                                        }

                                    } else if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                                        // 情形
                                        if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) {

                                            getBatchSaveCertUrl = ctx + '/aea/par/in/batchSaveStageStateCertIn.do';
                                            getBatchSaveCertData = {
                                                'stageId': currentBusiId,
                                                'stageStateId': currentData.id,
                                                'certIds': certIds.toString()
                                            };
                                            // 阶段节点
                                        } else if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                                            getBatchSaveCertUrl = ctx + '/aea/par/in/batchSaveStageNoStateCertIn.do';
                                            getBatchSaveCertData = {
                                                'stageId': currentBusiId,
                                                'certIds': certIds.toString()
                                            };
                                        }
                                    }

                                    $.ajax({
                                        url: getBatchSaveCertUrl,
                                        type: 'POST',
                                        data: getBatchSaveCertData,
                                        async: false,
                                        success: function (result) {
                                            if (result.success) {
                                                certDialogClose();
                                                refreshMind();
                                            } else {
                                                swal('错误信息', result.message, 'error');
                                            }
                                        },
                                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                                        }
                                    });
                                });
                            } else {
                                agcloud.ui.metronic.showSwal({type: 'info', message: '不支持在当前节点下添加电子证照!'});
                            }
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                        }
                    }
                }
            }
        }]);
}

//查看情形版本
angular.module('kityminderEditor')
    .directive('stateVersion', ['commandBinder','$modal', function(commandBinder,$modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/stateVersion/historicVersions.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function($scope) {
                $scope.addHyperlink = function () {

                    isShowCopyMaxVerBtn();
                    $('#itemStateVerModal').modal('show');
                    $('#itemStateVerScroll').animate({scrollTop: 0}, 800);//滚动到顶部
                    loadStateVerTable();
                }
            }
        }
    }]);

// 导入xmind
angular.module('kityminderEditor')
    .directive('importfileXmind', ['commandBinder','$modal', function(commandBinder,$modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/importfileXmind/importfileXmind.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function($scope) {
                $scope.addHyperlink = function () {

                    var minder = $scope.minder;
                    var nodes = minder.getSelectedNodes();
                    var clNodeList = nodes[0].children;
                    var clNodeIds = [];
                    var currentNode=minder.getSelectedNode();
                    var currentData=currentNode.data;

                    if (curIsEditable) {

                        $("#uploadXmindFileModal").modal('show');
                        $("#uploadXmindFileBtn").unbind("click")
                        $("#uploadXmindFileBtn").click(function () {
                            $("#uploadXmindFileModal").modal('hide');
                            uploadXmindFile(currentBusiType, currentBusiId);
                        })
                    }else{
                        agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                    }
                }
            }
        }
    }]);

// 导出xmind
angular.module('kityminderEditor')
    .directive('exportfileXmind', ['commandBinder','$modal', function(commandBinder,$modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/exportfileXmind/exportfileXmind.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function($scope) {
                $scope.addHyperlink = function () {

                    var minder = $scope.minder;
                    var nodes = minder.getSelectedNodes();
                    var clNodeList = nodes[0].children;
                    var clNodeIds = [];
                    var currentNode=minder.getSelectedNode();
                    var currentData=currentNode.data;
                    location.href=ctx+'/rest/mind/exportAsXmindFile.do?busiType='+currentBusiType+'&busiId='+currentBusiId + '&stateVerId=' + currentStateVerId;
                }
            }
        }
    }]);

//通过当前业务url和参数，刷新思维导图
function refreshMind() {

    var url = currentUrlLoadData;
    var param = currentBusiLoadPara;
    loadMind(url,param)
};