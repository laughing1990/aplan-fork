var commonQueryParams = [],
    certInfoTreeKey,
    certInfoTreeNodeList = [],
    certInfoTreeLastValue = "",
    certInfoTree = null,
    certInfoTreeSelectNode = null,  //树选中节点
    certInfoTreeSetting = {
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
            onClick: onClickCertInfoTreeNode,
            onRightClick:onRightClickCertInfoTreeNode, //右击事件
        }
    };

// 初始化加载函数
var aeCertTypeValidator = null;
var aeCertValidator = null;
$(function(){

    // 初始化高度
    var H = $(document).height()-10 || $(body).height()-10;
    $('#mainContentPanel').css('height',H);
    $('#certInfoTree').css({'max-height': $('#westPanel').height()-116,'height': $('#westPanel').height()-116});
    $('#ae_cert_type_scroll').css({'max-height': $('#westPanel').height()-111,'height': $('#westPanel').height()-111});
    $('#ae_cert_scroll').css({'max-height': $('#westPanel').height()-111,'height': $('#westPanel').height()-111});
    $('#root_rel_cert_type_list_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-116
    });
    $('#child_cert_type_list_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-161
    });
    $('#type_rel_cert_list_tb').bootstrapTable('resetView',{
        height: $('#westPanel').height()-161
    });

    $("#certInfoTree").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    $(".fixed-table-body").niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

    // 初始化树
    loadCertInfoTreeData();
    // 默认选中第一个节点数据
    expandFirstNode();

    // 关键字搜索文件库节点
    certInfoTreeKey = $("#certInfoTreeKeyWord");
    certInfoTreeKey.bind("focus", focusCertInfoTreeKey).bind("blur", blurCertInfoTreeKey).bind("change cut input propertychange",searchCertInfoTreeNode);
    certInfoTreeKey.bind('keydown', function (e){
        if(e.which == 13){
            searchCertInfoTreeNode();
        }
    });

    aeCertTypeValidator = $('#ae_cert_type_form').validate({
        // 定义校验规则
        rules: {
            typeName: {
                required: true,
                maxlength: 100
            },
            typeCode:{
                required: true,
                maxlength: 50,
                remote: {
                    url: ctx+'/aea/cert/type/checkUniqueCertTypeCode.do', //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {   //要传递的数据
                        certTypeId: function(){
                            return $("#ae_cert_type_form input[name='certTypeId']").val();
                        },
                        typeCode: function() {
                            return $("#ae_cert_type_form input[name='typeCode']").val();
                        }
                    }
                }
            },
            // isActive:{
            //     required: true,
            // },
            sortNo:{
                required: true,
                maxlength: 10
            },
            typeMemo: {
                maxlength: 500
            },
        },
        messages: {
            typeName: {
                required: '此项必填!',
                maxlength: "长度不能超过100个字母!"
            },
            typeCode:{
                required: '此项必填!',
                maxlength: "长度不能超过50个字母!",
                remote: "编号已存在！",
            },
            // isActive:{
            //     required: '此项必填!',
            // },
            sortNo:{
                required: '此项必填!',
                maxlength: "长度不能超过10个字母!",
            },
            typeMemo: {
                maxlength: "长度不能超过500个字母!"
            },
        },
        // 提交表单
        submitHandler: function(form){

            var certTypeId = $("#ae_cert_type_form input[name='certTypeId']").val();
            var isTbEditNode = $("#ae_cert_type_form input[name='isTbEditNode']").val();
            var parentTypeId = $("#ae_cert_type_form input[name='parentTypeId']").val();
            var d = {};
            var t = $('#ae_cert_type_form').serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });
            if(parentTypeId=='root'){
                d['parentTypeId'] = null;
            }
            $.ajax({
                url: ctx + '/aea/cert/type/saveAeaCertType.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){
                        var certTypeContent = result.content;
                        certTypeContent.parentTypeId = parentTypeId;
                        var newNode = createCertTypeNodeData(certTypeContent);
                        if(certTypeId==certTypeContent.certTypeId){ // 更新
                            certInfoTreeSelectNode.name = newNode.name;
                            certInfoTreeSelectNode.pId = newNode.pId;
                            certInfoTreeSelectNode.type = newNode.type;
                            certInfoTreeSelectNode.open = newNode.open;
                            certInfoTreeSelectNode.isParent = newNode.isParent;
                            certInfoTree.updateNode(certInfoTreeSelectNode);
                            swal({
                                title: '提示信息',
                                text: '操作成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            if(isTbEditNode=='1'){ // 表格编辑
                                //将节点设置为选中状态
                                var node = certInfoTree.getNodeByParam("id",parentTypeId, null);
                                certInfoTree.selectNode(node);
                                certInfoTreeSelectNode = node;
                                initRightShowPage(node,true,null,null,null);
                            }else{
                                //将新增的节点设置为选中
                                var node = certInfoTree.getNodeByParam("id",certTypeContent.certTypeId, null);
                                //将节点设置为选中状态
                                if(node){
                                    certInfoTree.selectNode(node);
                                    certInfoTreeSelectNode = node;
                                    initRightShowPage(node,true,null,null,null);
                                }
                            }
                        }else{ // 新增
                            certInfoTree.addNodes(certInfoTreeSelectNode,-1,newNode,true);
                            swal({
                                // title: '提示信息',
                                text: '操作成功!',
                                type: 'success',
                                showCancelButton: true,
                                confirmButtonText: '继续新增分类',
                                confirmButtonClass: "btn btn-success",
                                cancelButtonText: isTbEditNode=='1'?'进入分类列表':'进入分类设置',
                                cancelButtonClass: "btn btn-accent",
                            }).then(function(result) {
                                if (result.value) { // 继续新增分类
                                    initRightShowPage(certInfoTreeSelectNode,false,true,"certType",isTbEditNode);
                                }else{ // 进入其他相关设置
                                    if(isTbEditNode=='1'){
                                        var node = certInfoTree.getNodeByParam("id",parentTypeId, null);
                                        if(node){
                                            certInfoTree.selectNode(node);
                                            certInfoTreeSelectNode = node;
                                            initRightShowPage(node,true,null,null,null);
                                        }
                                    }else{
                                        //将新增的节点设置为选中
                                        var node = certInfoTree.getNodeByParam("id",certTypeContent.certTypeId, null);
                                        //将节点设置为选中状态
                                        if(node){
                                            certInfoTree.selectNode(node);
                                            certInfoTreeSelectNode = node;
                                            initRightShowPage(node,true,null,null,null);
                                        }
                                    }
                                }
                            });
                        }
                    }else {
                        swal('错误信息', result.message, 'error');
                    }
                },
                error:function(){
                    swal('错误信息', "保存类型信息失败！", 'error');
                }
            });
        }
    });

    $('#restAeCertTypeBtn').click(function(){

        var certTypeId = $("#ae_cert_type_form input[name='certTypeId']").val();
        var parentTypeId = $("#ae_cert_type_form input[name='parentTypeId']").val();
        var isTbEditNode = $("#ae_cert_type_form input[name='isTbEditNode']").val();
        // aeCertTypeValidator.resetForm();
        // $('#ae_cert_type_form')[0].reset();
        // $("#ae_cert_type_form input[name='certTypeId']").val(certTypeId);
        // $("#ae_cert_type_form input[name='parentTypeId']").val(parentTypeId);
        // $("#ae_cert_type_form input[name='isTbEditNode']").val(isTbEditNode);

        if(isTbEditNode=='1'){
            var node = certInfoTree.getNodeByParam("id", parentTypeId, null);
            if(node){
                certInfoTree.selectNode(node);
                certInfoTreeSelectNode = node;
                initRightShowPage(node,true,null,null,null);
            }
        }else{
            //将新增的节点设置为选中
            var node = certInfoTree.getNodeByParam("id", certTypeId, null);
            //将节点设置为选中状态
            if(node){
                certInfoTree.selectNode(node);
                certInfoTreeSelectNode = node;
                initRightShowPage(node,true,null,null,null);
            }
        }
    });

    // 设置初始化校验
    aeCertValidator = $("#ae_cert_form").validate({

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
                            return $("#ae_cert_form input[name='certId']").val();
                        },
                        certCode: function() {
                            return $("#ae_cert_form input[name='certCode']").val();
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

            var certId = $("#ae_cert_form input[name='certId']").val();
            var certTypeId = $("#ae_cert_form input[name='certTypeId']").val();
            var isTbEditNode = $("#ae_cert_form input[name='isTbEditNode']").val();
            var d = {};
            var t = $('#ae_cert_form').serializeArray();
            $.each(t, function() {
                // if(this.name=='issueDate'||this.name=='termStart'||this.name=='termEnd'){ // 处理时间格式问题
                //     if(this.value){
                //         d[this.name] = this.value.replace('T',' ');
                //     }
                // }else{
                d[this.name] = this.value;
                // }
            });
            $.ajax({
                url: ctx+'/aea/cert/saveAeaCert.do',
                type: 'POST',
                data: d,
                async: false,
                success: function (result) {
                    if (result.success&&result.content){
                        var certContent = result.content;
                        var newNode = createCertNodeData(certContent);
                        if(certId == certContent.certId){ // 更新
                            certInfoTreeSelectNode.name = newNode.name;
                            certInfoTreeSelectNode.pId = newNode.pId;
                            certInfoTreeSelectNode.type = newNode.type;
                            certInfoTreeSelectNode.open = newNode.open;
                            certInfoTreeSelectNode.isParent = newNode.isParent;
                            certInfoTree.updateNode(certInfoTreeSelectNode);
                            swal({
                                // title: '提示信息',
                                text: '操作成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            if(isTbEditNode=='1'){ // 表格编辑
                                //将节点设置为选中状态
                                var node = certInfoTree.getNodeByParam("id",certTypeId, null);
                                if(node){
                                    certInfoTree.selectNode(node);
                                    certInfoTreeSelectNode = node;
                                    initRightShowPage(node,true,null,null,null);
                                    $('#typeRelCert').trigger('click');
                                    $('#typeRelCert').addClass('active');
                                    $('#childCertType').removeClass('active');
                                }
                            }else{
                                //将新增的节点设置为选中
                                var node = certInfoTree.getNodeByParam("id",certContent.certId, null);
                                //将节点设置为选中状态
                                if(node){
                                    certInfoTree.selectNode(node);
                                    certInfoTreeSelectNode = node;
                                    initRightShowPage(node,true,null,null,"0");
                                }
                            }
                        }else{ // 新增
                            certInfoTree.addNodes(certInfoTreeSelectNode,-1,newNode,true);
                            swal({
                                text: '操作成功!',
                                type: 'success',
                                showCancelButton: true,
                                confirmButtonText: '继续新增证照',
                                confirmButtonClass: "btn btn-success",
                                cancelButtonText: isTbEditNode=='1'?'进入证照列表':'进入证照详情',
                                cancelButtonClass: "btn btn-accent",
                            }).then(function(result) {
                                if (result.value) { // 继续新增分类
                                    initRightShowPage(certInfoTreeSelectNode,false,true,"cert",isTbEditNode);
                                }else{ // 进入其他相关设置
                                    if(isTbEditNode=='1'){
                                        var node = certInfoTree.getNodeByParam("id",certTypeId, null);
                                        if(node){
                                            certInfoTree.selectNode(node);
                                            certInfoTreeSelectNode = node;
                                            initRightShowPage(node,true,null,null,null);
                                            $('#typeRelCert').trigger('click');
                                            $('#typeRelCert').addClass('active');
                                            $('#childCertType').removeClass('active');
                                        }
                                    }else{
                                        //将新增的节点设置为选中
                                        var node = certInfoTree.getNodeByParam("id",certContent.certId, null);
                                        //将节点设置为选中状态
                                        if(node){
                                            certInfoTree.selectNode(node);
                                            certInfoTreeSelectNode = node;
                                            initRightShowPage(node,true,null,null,"0");
                                        }
                                    }
                                }
                            });
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

    $('#restAeCertBtn').click(function(){

        var certId = $("#ae_cert_form input[name='certId']").val();
        var certTypeId = $("#ae_cert_form input[name='certTypeId']").val();
        var isTbEditNode = $("#ae_cert_form input[name='isTbEditNode']").val();

        // if(isTbEditNode=='1'){
            var node = certInfoTree.getNodeByParam("id", certTypeId, null);
            if(node){
                certInfoTree.selectNode(node);
                certInfoTreeSelectNode = node;
                initRightShowPage(node,true,null,null,null);
                $('#typeRelCert').trigger('click');
                $('#typeRelCert').addClass('active');
                $('#childCertType').removeClass('active');
            }
        // }else{
        //     //将新增的节点设置为选中
        //     var node = certInfoTree.getNodeByParam("id", certId, null);
        //     //将节点设置为选中状态
        //     if(node){
        //         certInfoTree.selectNode(node);
        //         certInfoTreeSelectNode = node;
        //         initRightShowPage(node,true,null,null,"0");
        //     }
        // }

        // $('#ae_cert_form')[0].reset();
        // aeCertValidator.resetForm();
        // $("#ae_cert_form input[name='certId']").val(certId);
        // $("#ae_cert_form input[name='certTypeId']").val(certTypeId);
        // $("#ae_cert_form input[name='isTbEditNode']").val(isTbEditNode);
    });

    $('#selectOrgBtn').click(function(){

        var opusOmOrgTree = $.fn.zTree.getZTreeObj("opusOmOrgTree");
        var nodes = opusOmOrgTree.getSelectedNodes();
        if(nodes!=null&&nodes.length>0){
            var selectNode = nodes[0];
            $("#ae_cert_form input[name='issueOrgId']").val(selectNode.id);
            $("#ae_cert_form input[name='issueOrgName']").val(selectNode.name);
            closeselectOpusOmOrgZtree();
        }else{
            swal('提示信息', '请选择机构！', 'info');
        }
    });

    $('#selectBscAttDirBtn').click(function(){

        var bscAttDirTree = $.fn.zTree.getZTreeObj("selectBscAttDirTree");
        var nodes = bscAttDirTree.getSelectedNodes();
        if(nodes!=null&&nodes.length>0){
            var selectNode = nodes[0];
            $("#ae_cert_form input[name='attDirId']").val(selectNode.id);
            $("#ae_cert_form input[name='attDirName']").val(selectNode.name);
            closeSelectBscAttDirZtree();
        }else{
            swal('提示信息', '请选择目录！', 'info');
        }
    });
});

function focusCertInfoTreeKey(e) {

    if (certInfoTreeKey.hasClass("empty")) {
        certInfoTreeKey.removeClass("empty");
    }
}

function blurCertInfoTreeKey(e) {

    if (certInfoTreeKey.get(0).value === "") {
        certInfoTreeKey.addClass("empty");
    }
    searchCertInfoTreeNode();
}

// 点击事件
function onClickCertInfoTreeNode(event, treeId, treeNode) {
debugger
    if(treeNode){
        certInfoTree.selectNode(treeNode);
        certInfoTreeSelectNode = treeNode;
        initRightShowPage(treeNode,true,null,null,"0");
        expandNextNodes();
    }
}

//展开下一级子节点
function expandNextNodes(){

    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    var selectNodes = certInfoTree.getSelectedNodes();
    certInfoTree.expandNode(selectNodes[0], true, null, null);
}

// 右键事件
function onRightClickCertInfoTreeNode(event, treeId, treeNode) {

    //禁止浏览器的菜单打开
    event.preventDefault();
    certInfoTree.selectNode(treeNode);
    certInfoTreeSelectNode = treeNode;
    initRightShowPage(treeNode,true,null,null,null);

    if(event.target.tagName.toLowerCase()=='span'||event.target.tagName.toLowerCase()=='a'||event.target.tagName.toLowerCase()=='ul'){

        var y = event.clientY+10;
        var maxHeight = $('#certInfoTreeScrollable').data('max-height');
        if(event.clientY>maxHeight){
            y -= ($('.contextMenuDiv').height()+5);
        }
        if(treeNode.type=='root'){
            showRMenu('rootCertTypeContextMenu',event.clientX+15, y);
        }else if(treeNode.type=='certType'){
            showRMenu('certTypeContextMenu',event.clientX+15, y);
        }else {
            showRMenu('certContextMenu',event.clientX+15, y);
        }
    }
}

/**
 *  控制右边页面展示
 * @param node 当前选中节点
 * @param isClickEvent // 是否点击事件
 * @param isAdd // 是否新增
 * @param aeType // 新增编辑类型
 * @param isTbEditNode // 是否表编辑 1 是 0 否
 */
function initRightShowPage(node,isClickEvent,isAdd,aeType,isTbEditNode){

    if(node){
        // 点击事件
        if(isClickEvent){

            if(node.type=='root'){ // 根节点

                $('#ae_cert_rel_page').hide();
                $('#ae_cert_type_page').hide();
                $('#type_rel_cert_list_page').hide();
                $('#root_rel_cert_type_list_page').show();
                // 展示分类列表数据
                searchRootRelCertType();

            }else if(node.type=='certType'){ // 分类

                $('#ae_cert_rel_page').hide();
                $('#ae_cert_type_page').hide();
                $('#type_rel_cert_list_page').show();
                $('#root_rel_cert_type_list_page').hide();
                $('#type_rel_cert_list_title').html(node.name);
                $('#childCertType').trigger('click');
                $('#childCertType').addClass('active');
                $('#typeRelCert').removeClass('active');

            }else{
                $('#ae_cert_rel_page').show();
                $('#ae_cert_type_page').hide();
                $('#type_rel_cert_list_page').hide();
                $('#root_rel_cert_type_list_page').hide();
                $('#ae_cert_header').html(node.name);
                // 展示证照数据
                $('#ae_cert_form')[0].reset();
                aeCertValidator.resetForm();
                $("#ae_cert_form input[name='certId']").val('');
                $("#ae_cert_form input[name='certTypeId']").val('');
                $.ajax({
                    url: ctx + '/aea/cert/getAeaCert.do',
                    type: 'post',
                    data: {'id': certInfoTreeSelectNode.id},
                    async: false,
                    success: function (data) {
                        if (data) {
                            loadFormData(true,'#ae_cert_form',data);
                        }
                    },
                    error: function () {
                        swal('错误信息', "获取证照信息失败！", 'error');
                    }
                });
                if(certData!=null&&certData.handleTime){
                    $("#ae_cert_form input[name='issueDate']").val(new Date(certData.issueDate).format("yyyy-MM-ddThh:mm:ss"));
                }else{
                    $("#ae_cert_form input[name='issueDate']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                }
                if(certData!=null&&certData.termStart){
                    $("#ae_cert_form input[name='termStart']").val(new Date(certData.termStart).format("yyyy-MM-ddThh:mm:ss"));
                }else{
                    $("#ae_cert_form input[name='termStart']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                }
                if(certData!=null&&certData.termEnd){
                    $("#ae_cert_form input[name='termEnd']").val(new Date(certData.termEnd).format("yyyy-MM-ddThh:mm:ss"));
                }else{
                    $("#ae_cert_form input[name='termEnd']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                }
            }
        }else {
            // 根节点下新增子分类
            if(node.type=='root'){

                $('#ae_cert_type_page').show();
                $('#ae_cert_rel_page').hide();
                $('#type_rel_cert_list_page').hide();
                $('#root_rel_cert_type_list_page').hide();
                // 清空表单
                aeCertTypeValidator.resetForm();
                $('#ae_cert_type_form')[0].reset();
                $("#ae_cert_type_form input[name='certTypeId']").val('');
                $("#ae_cert_type_form input[name='parentTypeId']").val('');

                // 新增
                if(isAdd){
                    $('#ae_cert_type_header').html('新增证照分类');
                    $("#ae_cert_type_form input[name='parentTypeId']").val('root');
                    $("#ae_cert_type_form input[name='isTbEditNode']").val('1');
                    $("#ae_cert_type_form select[name='isActive'] option:eq(1)").prop("selected", 'selected');
                    $.ajax({
                        url: ctx + '/aea/cert/type/getMaxSortNo.do',
                        type: 'post',
                        data: {'parentId': null},
                        async: false,
                        success: function (data) {
                            if(data){
                                $("#ae_cert_type_form input[name='sortNo']").val(data);
                            }
                        },
                        error: function () {
                            swal('错误信息', "获取分类排序失败！", 'error');
                        }
                    });

                }else{ // 编辑
                    $('#ae_cert_type_header').html(node.name);
                    $("#ae_cert_type_form input[name='isTbEditNode']").val('1');
                    $.ajax({
                        url: ctx + '/aea/cert/type/getAeaCertType.do',
                        type: 'post',
                        data: {'id': certInfoTreeSelectNode.id},
                        async: false,
                        success: function (data) {
                            if (data) {
                                loadFormData(true,'#ae_cert_type_form',data);
                            }
                        },
                        error: function () {
                            swal('错误信息', "获取分类信息失败！", 'error');
                        }
                    });
                    $("#ae_cert_type_form input[name='parentTypeId']").val('root');
                }
            }else if(node.type=='certType'){ // 分类新增、编辑

                if(aeType=='certType'){

                    $('#ae_cert_rel_page').hide();
                    $('#ae_cert_type_page').show();
                    $('#type_rel_cert_list_page').hide();
                    $('#root_rel_cert_type_list_page').hide();
                    aeCertTypeValidator.resetForm();
                    $('#ae_cert_type_form')[0].reset();
                    $("#ae_cert_type_form input[name='certTypeId']").val('');
                    $("#ae_cert_type_form input[name='parentTypeId']").val('');

                    if(isAdd){

                        $('#ae_cert_type_header').html('新增证照分类');
                        aeCertTypeValidator.resetForm();
                        $('#ae_cert_type_form')[0].reset();
                        $("#ae_cert_type_form input[name='parentTypeId']").val(node.id);
                        $("#ae_cert_type_form input[name='isTbEditNode']").val(isTbEditNode);
                        $("#ae_cert_type_form select[name='isActive'] option:eq(1)").prop("selected", 'selected');
                        $.ajax({
                            url: ctx + '/aea/cert/type/getMaxSortNo.do',
                            type: 'post',
                            data: {'parentId': node.id},
                            async: false,
                            success: function (data) {
                                if(data){
                                    $("#ae_cert_type_form input[name='sortNo']").val(data);
                                }
                            },
                            error: function () {
                                swal('错误信息', "获取分类排序失败！", 'error');
                            }
                        });
                    }else{

                        $('#ae_cert_type_header').html(node.name);
                        $("#ae_cert_type_form input[name='isTbEditNode']").val(isTbEditNode);
                        $.ajax({
                            url: ctx + '/aea/cert/type/getAeaCertType.do',
                            type: 'post',
                            data: {'id': certInfoTreeSelectNode.id},
                            async: false,
                            success: function (data) {
                                if (data) {
                                    loadFormData(true,'#ae_cert_type_form',data);
                                }
                            },
                            error: function () {
                                swal('错误信息', "获取分类信息失败！", 'error');
                            }
                        });
                        $("#ae_cert_type_form input[name='parentTypeId']").val(node.pId);
                    }
                }else if(aeType=='cert'){

                    $('#ae_cert_rel_page').show();
                    $('#ae_cert_type_page').hide();
                    $('#type_rel_cert_list_page').hide();
                    $('#root_rel_cert_type_list_page').hide();
                    aeCertValidator.resetForm();
                    $('#ae_cert_form')[0].reset();
                    $("#ae_cert_form input[name='certId']").val('');
                    $("#ae_cert_form input[name='certTypeId']").val('');

                    if(isAdd){

                        $('#ae_cert_header').html('新增证照');
                        $("#ae_cert_form input[name='certTypeId']").val(node.id);
                        $("#ae_cert_form input[name='isTbEditNode']").val(isTbEditNode);
                        $("#ae_cert_form input[name='issueDate']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                        $("#ae_cert_form input[name='termStart']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                        $("#ae_cert_form input[name='termEnd']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));

                        $.ajax({
                            url: ctx + '/aea/cert/getMaxCertSortNo.do',
                            type: 'post',
                            data: {},
                            async: false,
                            success: function (data) {
                                if(data){
                                    $("#ae_cert_form input[name='sortNo']").val(data);
                                }
                            },
                            error: function () {
                                swal('错误信息', "获取证照排序失败！", 'error');
                            }
                        });

                    }else{

                        $('#ae_cert_header').html(node.name);
                        // 展示证照数据
                        $("#ae_cert_form input[name='isTbEditNode']").val(isTbEditNode);
                        var certData = null;
                        $.ajax({
                            url: ctx + '/aea/cert/getAeaCert.do',
                            type: 'post',
                            data: {'id': node.id},
                            async: false,
                            success: function (data) {
                                if (data) {
                                    certData = data;
                                    loadFormData(true,'#ae_cert_form',data);
                                }
                            },
                            error: function () {
                                swal('错误信息', "获取证照信息失败！", 'error');
                            }
                        });
                        if(certData!=null&&certData.handleTime){
                            $("#ae_cert_form input[name='issueDate']").val(new Date(certData.issueDate).format("yyyy-MM-ddThh:mm:ss"));
                        }else{
                            $("#ae_cert_form input[name='issueDate']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                        }
                        if(certData!=null&&certData.termStart){
                            $("#ae_cert_form input[name='termStart']").val(new Date(certData.termStart).format("yyyy-MM-ddThh:mm:ss"));
                        }else{
                            $("#ae_cert_form input[name='termStart']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                        }
                        if(certData!=null&&certData.termEnd){
                            $("#ae_cert_form input[name='termEnd']").val(new Date(certData.termEnd).format("yyyy-MM-ddThh:mm:ss"));
                        }else{
                            $("#ae_cert_form input[name='termEnd']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                        }
                    }
                }
            }else if(node.type=='cert'){ // 证照编辑

                $('#ae_cert_rel_page').show();
                $('#ae_cert_type_page').hide();
                $('#type_rel_cert_list_page').hide();
                $('#root_rel_cert_type_list_page').hide();
                $('#ae_cert_header').html(node.name);
                // 展示证照数据
                aeCertValidator.resetForm();
                $('#ae_cert_form')[0].reset();
                $("#ae_cert_form input[name='certId']").val('');
                $("#ae_cert_form input[name='certTypeId']").val('');
                $("#ae_cert_form input[name='isTbEditNode']").val(isTbEditNode);
                var certData = null;
                $.ajax({
                    url: ctx + '/aea/cert/getAeaCert.do',
                    type: 'post',
                    data: {'id': certInfoTreeSelectNode.id},
                    async: false,
                    success: function (data) {
                        if (data) {
                            certData = data;
                            loadFormData(true,'#ae_cert_form',data);
                        }
                    },
                    error: function () {
                        swal('错误信息', "获取证照信息失败！", 'error');
                    }
                });
                if(certData!=null&&certData.handleTime){
                    $("#ae_cert_form input[name='issueDate']").val(new Date(certData.issueDate).format("yyyy-MM-ddThh:mm:ss"));
                }else{
                    $("#ae_cert_form input[name='issueDate']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                }
                if(certData!=null&&certData.termStart){
                    $("#ae_cert_form input[name='termStart']").val(new Date(certData.termStart).format("yyyy-MM-ddThh:mm:ss"));
                }else{
                    $("#ae_cert_form input[name='termStart']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                }
                if(certData!=null&&certData.termEnd){
                    $("#ae_cert_form input[name='termEnd']").val(new Date(certData.termEnd).format("yyyy-MM-ddThh:mm:ss"));
                }else{
                    $("#ae_cert_form input[name='termEnd']").val(new Date().format("yyyy-MM-ddThh:mm:ss"));
                }
            }
        }
    }
}

// 默认选中第一个节点
function expandFirstNode(){

    if(certInfoTree){
        var nodes = certInfoTree.getNodes();
        if(nodes!=null&&nodes.length>0){
            certInfoTree.selectNode(nodes[0]); //选中第一个节点
            certInfoTreeSelectNode = nodes[0];
            initRightShowPage(nodes[0],true,null,null,null);
        }
    }
}

//全部展开
function expandCertInfoTreeAllNode(){

    certInfoTree.expandAll(true);
}

//全部折叠
function collapseCertInfoTreeAllNode(){

    certInfoTree.expandAll(false);
}

// 搜索节点
function searchCertInfoTreeNode(){

    // 取得输入的关键字的值
    var value = $.trim($('#certInfoTreeKeyWord').val());

    // 按名字查询
    var keyType = "name";

    // 如果和上次一次，就退出不查了。
    if (certInfoTreeLastValue === value) {
        return;
    }

    // 保存最后一次
    certInfoTreeLastValue = value;

    var nodes = certInfoTree.getNodes();
    // 如果要查空字串，就退出不查了。
    if (value == "") {
        showCertInfoTreeAllNode(nodes);
        return;
    }
    hideCertInfoTreeAllNode(nodes);
    certInfoTreeNodeList = certInfoTree.getNodesByParamFuzzy(keyType, value);
    updateCertInfoTreeNodes(certInfoTreeNodeList);
}

// 清空查询
function clearSearchCertInfoTreeNode(){

    // 清空查询内容
    $('#certInfoTreeKeyWord').val('');
    showCertInfoTreeAllNode(certInfoTree.getNodes());
}

//隐藏所有节点
function hideCertInfoTreeAllNode(nodes){

    nodes = certInfoTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        certInfoTree.hideNode(nodes[i]);
    }
}

//显示所有节点
function showCertInfoTreeAllNode(nodes){

    nodes = certInfoTree.transformToArray(nodes);
    for(var i=nodes.length-1; i>=0; i--) {
        if(nodes[i].getParentNode()!=null){
            certInfoTree.expandNode(nodes[i],false,false,false,false);
        }else{
            certInfoTree.expandNode(nodes[i],true,true,false,false);
        }
        certInfoTree.showNode(nodes[i]);
        showCertInfoTreeAllNode(nodes[i].children);
    }
}

//更新节点状态
function updateCertInfoTreeNodes(nodeList) {

    if(nodeList!=null&&nodeList.length>0) {
        certInfoTree.showNodes(nodeList);
        for(var i=0, l=nodeList.length; i<l; i++) {

            //展开当前节点的父节点
            certInfoTree.showNode(nodeList[i].getParentNode());
            //显示展开符合条件节点的父节点
            while(nodeList[i].getParentNode()!=null){
                certInfoTree.expandNode(nodeList[i].getParentNode(), true, false, false);
                nodeList[i] = nodeList[i].getParentNode();
                certInfoTree.showNode(nodeList[i].getParentNode());
            }
            //显示根节点
            certInfoTree.showNode(nodeList[i].getParentNode());
            //展开根节点
            certInfoTree.expandNode(nodeList[i].getParentNode(), true, false, false);
        }
    }
}

// 加载电子证照库数据
function loadCertInfoTreeData(){

    $.ajax({
        url: ctx+'/aea/cert/gtreeTypeCert.do',
        type:'post',
        data:{},
        async: false,
        dataType: 'json',
        success: function(data){
            if(data!=null&&data.length>0){
                for(var i in data){
                    if(data[i].type=='root'){
                        data[i].name='电子证照库';
                        break;
                    }
                }
                if(certInfoTree)certInfoTree.destroy();
                certInfoTree = $.fn.zTree.init($("#certInfoTree"), certInfoTreeSetting , data);
            }
        },
        error: function(){
            swal('错误信息', '初始化电子证照库异常!', 'error');
        }
    });
}

// 新增子分类
function addChildCertType(){

    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    initRightShowPage(certInfoTreeSelectNode,false,true,"certType","0");
}

// 编辑分类
function editCertType(){

    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    initRightShowPage(certInfoTreeSelectNode,false,false,"certType","0");
}

// 删除分类
function deleteCertType(){

    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    var node = certInfoTree.getNodeByParam("id", certInfoTreeSelectNode.id, null);
    var parentNode = certInfoTreeSelectNode.getParentNode();
    if(node&&node.id){
        swal({
            title: '提示信息',
            text: '此操作将删除分类、下级子分类以及相关电子证照信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/cert/type/deleteAeaCertTypeById.do',
                    type: 'POST',
                    data:{'id': node.id},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            if(parentNode){ // 默认选中父级菜单
                                certInfoTree.selectNode(parentNode);
                                certInfoTreeSelectNode = parentNode;
                                initRightShowPage(parentNode,true,null,null,null);
                            }
                            if(node){ // 移除节点
                                certInfoTree.removeNode(node);
                            }
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

//新增电子证照
function addChildCertInfo(){

    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    initRightShowPage(certInfoTreeSelectNode,false,true,"cert","0");
}

// 删除电子证照
function deleteCertInfo(){

    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    var node = certInfoTree.getNodeByParam("id", certInfoTreeSelectNode.id, null);
    var parentNode = certInfoTreeSelectNode.getParentNode(); // 默认选中父级菜单
    if(node&&node.id){
        swal({
            title: '提示信息',
            text: '此操作将删除电子证照信息,您确定删除吗?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function(result) {
            if (result.value) {
                $.ajax({
                    url: ctx+'/aea/cert/deleteAeaCertById.do',
                    type: 'POST',
                    data:{'id': node.id},
                    success: function (result){
                        if(result.success){
                            swal({
                                title: '提示信息',
                                text: '删除成功!',
                                type: 'success',
                                timer: 1000,
                                showConfirmButton: false
                            });
                            if(parentNode){
                                certInfoTree.selectNode(parentNode);
                                certInfoTreeSelectNode = parentNode;
                                initRightShowPage(parentNode,true,null,null,null);
                                $('#typeRelCert').trigger('click');
                                $('#typeRelCert').addClass('active');
                                $('#childCertType').removeClass('active');
                            }
                            // 移除节点
                            if(node){
                                certInfoTree.removeNode(node);
                            }
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

// 移动分类
function moveCertType(){

    // 隐藏右键
    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    swal('提示信息', '功能开发中...', 'info');
}

// 移动证照
function moveCert(){

    // 隐藏右键
    hideRMenu('rootCertTypeContextMenu');
    hideRMenu('certTypeContextMenu');
    hideRMenu('certContextMenu');
    swal('提示信息', '功能开发中...', 'info');
}


// 创建一个证照分类zTree node节点数据
function createCertTypeNodeData(certType){

    if(certType){
        var id = certType.certTypeId;
        var name = certType.typeName;
        var pId =  certType.parentTypeId;
        var type = 'certType';
        var open = true;
        var isParent = true;
        var node = {
            'id': id,
            'name': name,
            'pId': pId,
            'open': open,
            'isParent': isParent,
            'type': type,
        };
        return node;
    }
    return null;
}

// 创建一个证照zTree node节点数据
function createCertNodeData(cert){

    if(cert){
        var id = cert.certId;
        var name = cert.certName;
        var pId =  cert.certTypeId;
        var type = 'cert';
        var open = true;
        var isParent = false;
        var node = {
            'id': id,
            'name': name,
            'pId': pId,
            'open': open,
            'isParent': isParent,
            'type': type,
        };
        return node;
    }
    return null;
}

