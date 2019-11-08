<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>登录用户绑定</title>
    <%String isDebugMode = new Date().getTime() + "";%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/agcloud-common/agcloud.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/auth/md5.js"></script>
    <script type="text/javascript">
        var ctx = "${pageContext.request.contextPath}";
        var extLoginName = "${extLoginName}";
        var orgId = "${orgId}";
    </script>
    <style>
        .ztree li a{padding: 6px 3px 0 0;}
        .ztree li a.curSelectedNode{padding-top: 6px;}
        .modal-header .close{margin:0rem -1rem -1rem auto;}
    </style>
</head>
<body>
<div id="bindingPanel" style="text-align: center;background-color: #dbf3fb;padding:20px;width:620px;margin:auto">
    <div style="text-align:center;padding:10px;font-size:22px">
            请输入唐山市工程建设项目联合审批系统的登录账号密码进行关联绑定
    </div>
    <form id="binding_form">
        <div class="form-group m-form__group row">
            <label class="col-6 col-form-label">
                政务服务网统一登录账号:
            </label>
            <div class="col-6">
                <input type="text" value="${extLoginName}" id="extLoginName" readonly class="form-control m-input">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-6 col-form-label">
                登录账号:
            </label>
            <div class="col-6">
                <input type="text" id="loginName" name="loginName" class="form-control m-input">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-6 col-form-label">
                登录密码:
            </label>
            <div class="col-6">
                <input type="text" id="password" name="password" class="form-control m-input">
            </div>
        </div>
    </form>
    <div style="padding:10px;">
        <button class="btn btn-primary" onclick="doBinding()">绑定</button>
        <button class="btn btn-primary" onclick="createNewUser()">创建账号</button>
    </div>
</div>
<div id="createUserPanel" style="display:none;text-align: center;background-color: #dbf3fb;padding:20px;width:620px;margin:auto">
    <div style="text-align:center;padding:10px;font-size:22px">
        请填写个人账号基本信息
    </div>
    <form id="user_form">
        <div class="form-group m-form__group row">
            <label class="col-4 col-form-label">
                <i style="color:red;margin-right:5px;">*</i>登录账号:
            </label>
            <div class="col-6">
                <input type="text" id="systemLoginName" name="systemLoginName" class="form-control m-input" value="${extLoginName}">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-4 col-form-label">
                <i style="color:red;margin-right:5px;">*</i>登录密码:
            </label>
            <div class="col-6">
                <input type="text" id="systemPassword" name="systemPassword" class="form-control m-input">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-4 col-form-label">
                <i style="color:red;margin-right:5px;">*</i>姓名:
            </label>
            <div class="col-6">
                <input type="text" id="systemUsername" name="systemUsername" class="form-control m-input">
            </div>
        </div>
        <div class="form-group m-form__group row">
            <label class="col-4 col-form-label">
                <i style="color:red;margin-right:5px;">*</i>性别:
            </label>
            <div class="col-6">
                <select class="form-control" id="systemUserSex" >
                    <option value="0" selected="selected">男</option>
                    <option value="1">女</option>
                </select>
            </div>
        </div>
        <div id="orgSelectPanel" class="form-group m-form__group row">
            <label class="col-4 col-form-label">
                <i style="color:red;margin-right:5px;">*</i>所属机构:
            </label>
            <div class="col-6">
                <input type="hidden" id="systemOrgId" />
                <input type="text" id="systemOrgName" name="systemOrgName" class="form-control m-input" style="width: 76%;display: inline-block;">
                <button type="button" class="btn btn-secondary" onclick="selectOrg()">选择</button>
            </div>
        </div>
    </form>
    <div style="padding:10px;">
        <button class="btn btn-primary" onclick="saveUser()">保存</button>
        <button class="btn btn-secondary" onclick="cancelCreate()">取消</button>
    </div>
</div>
<div id="tplAppOrgShowDialog" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="tplAppOrgShowDialog_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" >
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;height: 45px;">
                <h5 id="tplAppOrgShowDialog_title" class="modal-title">请选择您所属的机构</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="height: 500px;overflow:auto">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin-top: 5px; margin-left: 0px;margin-right: 10px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn  btn-info" onclick="saveActTplAppRangeTreeNode()">确定</button>
                        </div>
                    </div>
                </div>
                <div style="padding:10px;">
                    <ul id="appOrgTree" class="ztree" style="overflow:auto;"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    // $("#bindingPanel").hide();
    function doBinding(){
        $.ajax({
            type: "post",
            url: ctx + '/auth/binding/user',
            data: {
                'extLoginName': extLoginName,
                'loginName': $("#loginName").val(),
                'password': hex_md5($("#password").val())
            },
            success: function (data) {
                if(data.success){
                    //跳到静默登录页
                    window.location.href = ctx + '/auth/static/login?userId=' + data.message;
                }else{
                    agcloud.ui.metronic.showErrorTip(data.message);
                }
            },
            error: function () {

            }
        });
    }
    function createNewUser() {
        if(orgId != ""){
            $("#systemOrgId").val(orgId);
            $("#orgSelectPanel").hide();
        }
        $("#bindingPanel").hide();
        $("#createUserPanel").show();
    }
    function cancelCreate() {
        $("#bindingPanel").show();
        $("#createUserPanel").hide();
    }
    var selectedOrgId = null;
    function selectOrg(){
        $('#tplAppOrgShowDialog').modal('show');
        $.fn.zTree.init($("#appOrgTree"), appOrgSettingLeft);
    }
    //组织树 配置setting信息
    var appOrgSettingLeft = {
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
            showLine: false //设置 zTree 是否显示节点之间的连线。
        },
        check: {
            enable: true,
            chkStyle: "radio",  //单选框
            radioType: "all"   //对所有节点设置单选
            // chkboxType:{ "Y" : "", "N" : "" },
            // chkStyle: "checkbox"
        },
        //使用异步加载，必须设置 setting.async 中的各个属性
        async: {
            //设置 zTree 是否开启异步加载模式
            enable: true,
            autoParam:["id"],
            dataType:"json",
            type:"post",
            url:ctx+"/auth/getOpuOmOrgZtree"
        },
        callback: {
            //单击事件
            //onClick: onClickLeftOrgTree,
            //右击事件
            //onRightClick:onRightClickLeftOrgTree,
            //用于捕获异步加载正常结束的事件回调函数
            onAsyncSuccess: onAsyncSuccessAppOrgTree
            ,onExpand:function(event, treeId, treeNode, msg){
                var zTree = $.fn.zTree.getZTreeObj(treeId);
                // checkAppOrgNode(zTree);
            }
        }
    };
    function onAsyncSuccessAppOrgTree(event, treeId, treeNode, msg) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);
        //对于根节点，展开下一级
        if(treeNode==null){
            var nodes = zTree.getNodes();
            for (var i=0, l=nodes.length; i<l; i++) {
                zTree.expandNode(nodes[i], true, false, false);
            }
        }
        checkAppOrgNode(zTree);
    };
    function checkAppOrgNode(ztreeObj) {
        if (selectedOrgId){
            var node = ztreeObj.getNodeByParam("id", selectedOrgId, null);
            if (node != null) ztreeObj.checkNode(node, true, true, true);
        }
    }
    function saveActTplAppRangeTreeNode(){
        var appOrgTreeObj = $.fn.zTree.getZTreeObj("appOrgTree");
        var node = appOrgTreeObj.getCheckedNodes(true);
        if(node != null && node.length > 0) {
            //这里可以扩展为选择多个机构
            selectedOrgId = node[0].id;
            $("#systemOrgName").val(node[0].name);
            $("#systemOrgId").val(selectedOrgId);
            $('#tplAppOrgShowDialog').modal('hide');
        }else{
            agcloud.ui.metronic.showErrorTip("请选择至少一个机构，如果未找到对应机构则可以先选择唐山市，再联系管理员创建相应的机构！");
        }
    }
    function checkUserForm() {
        var form = $('#user_form');
        var valiContent = {
            rules: {
                systemLoginName: {
                    required: true,
                    minlength: 3
                },
                systemPassword: {
                    required: true,
                    minlength: 3
                },
                systemUsername: {
                    required: true,
                    minlength: 2
                }
            },
            messages: {
                systemLoginName: {
                    required: "请输入登录账号",
                    minlength: "登录账号不能小于3个字符"
                },
                systemPassword: {
                    required: "请输入登录密码",
                    minlength: "登录密码不能小于3个字符"
                },
                systemUsername: {
                    required: "请输入姓名",
                    minlength: "姓名不能小于2个字符"
                }
            }
        }
        form.validate(valiContent);
        var valid = form.valid();
        if(orgId == "" && selectedOrgId == null && valid){
            agcloud.ui.metronic.showErrorTip("请选择所属机构！");
            valid = false;
        }
        return valid;
    }
    function saveUser() {
        if(!checkUserForm())
            return;
        $.ajax({
            type: "post",
            url: ctx + '/auth/create/user',
            data: {
                'loginName': $("#systemLoginName").val(),
                'loginPwd': hex_md5($("#systemPassword").val()),
                'userName': $("#systemUsername").val(),
                'userSex': $("#systemUserSex").val(),
                'orgId': $("#systemOrgId").val(),
                'extLoginName':extLoginName
            },
            success: function (data) {
                if (data.success) {
                    agcloud.ui.metronic.showConfirm("账号创建成功，并与统一登录账号绑定成功！请联系管理员分配访问权限！是否立即登陆？",function(){
                        $("#createUserPanel").hide();
                        window.location.href = ctx + "/auth/static/login?userId=" + data.message;
                    });
                }else{
                    agcloud.ui.metronic.showErrorTip(data.message);
                }
            }
        });
    }
</script>
</body>
</html>
