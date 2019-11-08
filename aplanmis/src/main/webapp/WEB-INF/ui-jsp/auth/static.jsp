<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%--<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>--%>
    <%--<link href="${ssoServerUrl}/framework/ui-themes/aurora-purple/css/agcloud_metronic.css" rel="stylesheet" type="text/css" />--%>
    <%--<link href="${ssoServerUrl}/framework/ui-themes/aurora-purple/css/agcloud_custom_expand.css" rel="stylesheet" type="text/css"/>--%>
    <script>
        var username = "${username}";
        var password = "${password}";
        var orgsStr = '${orgs}';
        var message = '${message}';
        var ctx = "${pageContext.request.contextPath}";
    </script>
</head>
<body style="background-color: #e1f1f0;">
<div id="jumpTip" style="display:none;padding:20px 220px;width:620px;margin:auto;">
    <h3 style="margin-left:200px">正在跳转到系统<i id="juhao"></i></h3>
</div>
<div style="display:none;">
    <iframe style="display:none" name="loginFrame" id="loginFrame" src=""></iframe>
</div>
<div id="errorTip" style="display:none;text-align: center;padding:20px;width:620px;margin:auto;color: #ec3838;">
    <h3>${message}</h3>

</div>
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery-1.11.3.min.js" type="text/javascript"></script>
<%--<script src="${ssoServerUrl}/framework/js-libs/layer-3.1.1/layer.js"></script>--%>
<script>
       var interval;
       function loginSSO(exttoo) {
           interval = setInterval(function () {
               var contentWindow = $("#loginFrame")[0].contentWindow;
               var data = {'static':true,'exttss':username,'exttcc':password,'exttoo':exttoo};
               contentWindow.postMessage(JSON.stringify(data),"*");
           },100);
           var interval2 = setInterval(function () {
               try{
                   if($("#loginFrame")[0].contentWindow){
                       if($("#loginFrame")[0].contentWindow.location.href == "${homePageUri}"){
                           window.clearInterval(interval2);
                           window.location.href = "${homePageUri}";
                       }
                   }
               }catch (e){
               }
           },100);
           if(typeof(layer) != 'undefined'){
               layer.close(layer.index);
           }
           $("#jumpTip").show();
           var loading = 1;
           setInterval(function () {
               if(loading > 6) loading = 1;
               var text = "";
               for(var j=0; j<loading; j++){
                   text += ".";
               }
               $("#juhao").empty();
               $("#juhao").text(text);
               loading++;
           },200);
       }
       $(function () {
           window.addEventListener('message', function(e) {
               if(e.data == "true"){
                   window.clearInterval(interval);
                   var interval1 = setInterval(function () {
                       $.ajax({
                           url:ctx + "/auth/checking",
                           type:"get",
                           dataType:"json",
                           success:function (data) {
                               if(data.success){
                                   window.clearInterval(interval1);
                                   window.location.href = "${homePageUri}";
                               }
                           }
                       })
                   },200);
                   return;
               }
           }, false);
           //1、判断是否有登录错误信息，有则提示并终止登录
           if(message){
               $("#errorTip").show();
               return
           }
           //2、进行登录操作
           var exttoo = "";
           if(orgsStr != 'null' && orgsStr != ""){
               $("#loginFrame").attr("src",'${homePageUri}');
               var orgs = JSON.parse(orgsStr);
               if(orgs.length > 1){
                   var opuOmOrgListLength = orgs.length;
                    //弹出选择框
                   var col = 12/opuOmOrgListLength;
                   //选择组织
                   var orgHtml="<div class='m-portlet ag-seclect-o'>"+
                       "<div class='ag-seclect-o-body'>";
                   for(var i = 0; i < opuOmOrgListLength; i++) {
                       orgHtml = orgHtml+"<a href='javascript:void(0)' target='_self'  onclick='loginSSO(" + '"' + orgs[i].orgId + '"' + ");'><div class='organization col-"+col+"'><span class='d-block'><img src='${ssoServerUrl}/images/user.jpg'/></span><span class='d-block'>" + orgs[i].orgName + "</span></div></a>";
                   }
                   orgHtml=orgHtml+"</div>"+
                       "</div>";

                   //弹出层宽度
                   var layerWith = 270*opuOmOrgListLength;
                   //弹出层
                   if(typeof(layer) != 'undefined') {
                       layer.open({
                           title: '请选择组织',
                           type: 1,
                           skin: 'select_org_layer',
                           area: [layerWith + 'px', '250px'], //宽高
                           content: orgHtml,
                           closeBtn: 0,
                           shade: 0.5
                       });
                   }else{
                       loginSSO(orgs[0].orgId);
                   }
               }else{
                   loginSSO(orgs[0].orgId);
               }
           }else{
               //未有组织机构信息则无法登录
               if(typeof(layer) != 'undefined') {
                   layer.open({
                       title: '温馨提醒',
                       type: 1,
                       skin: 'select_org_layer',
                       area: ['540px', '250px'], //宽高
                       content: "<div style='color: orangered;text-align: center;    margin-top: 90px;font-size: 16px;font-weight: 400'>当前登录账号没有关联组织机构信息，请联系管理员添加所属组织！</div>",
                       closeBtn: 1,
                       shade: 0.5,
                       end: function () {
                           window.opener = window;
                           var win = window.open("about:blank", "_self");
                           win.close();
                       }
                   });
               }else{
                   alert("当前登录账号没有关联组织机构信息，请联系管理员添加所属组织！");
                   window.opener = window;
                   var win = window.open("about:blank", "_self");
                   win.close();
               }
           }
       })
   </script>
</body>
</html>
