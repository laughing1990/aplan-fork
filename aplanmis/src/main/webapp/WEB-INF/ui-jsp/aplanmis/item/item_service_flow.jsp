<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <title>流程许可</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_service_flow.js"></script>
    <script type="text/javascript">
        var  itemId = '${itemId}';
    </script>
    <style type="text/css">
        tbody > tr > td {
            padding: 5px !important;
        }
        .backgroundCorlor{
            background-color:#EBFFFE;
        }
    </style>
</head>

<body style="background-color:white;">
<div class="m-portlet" style="height:auto;">
    <div class="m-portlet__body">
        <%--许可流程表单--%>
        <form class="m-form m-form--fit m-form--label-align-right" id="item_service_flow_edit_from" enctype="multipart/form-data" method="post">
            <!---------------- 隐藏域区域 开始 ----------------->
            <input type="hidden" id="itemId" name="itemId"/>
            <input type="hidden" id="id" name="id"/>
            <!---------------- 隐藏域区域 结束 ----------------->
                <div style="text-align: center;">
                    <h3 class="m-portlet__head-text" id="item_service_flow_title" style="font-weight:bold;">
                        许可流程
                    </h3>
                </div>
                <div>
                    <table data-toggle="table" style=" border:1px solid #0094ff;width: 80%;margin-left: 200px;margin-top: 20px;">
                        <tbody>
                            <tr align="center">
                                <td rowspan="6" class="backgroundCorlor" style=" border:1px solid #0094ff;">许可流程</td>
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">窗口办理流程说明</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <textarea class="form-control" style="width: 98%" name="ckbllcsm" rows="8"></textarea>
                                        <font style="color:red">*</font>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">窗口办理流程图</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div style="text-align: left;">
                                        <input type="text" id="ckbllctDoc" name="ckbllct" class="hide"/>
                                        <input type="file" id="ckbllctDocFile" name="ckbllctDocFile"/>
                                        <div id="ckbllctDocButton" class="form-group ">
                                            <button id="ckbllctDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(0);">下载</button>
                                            <button id="ckbllctDocDelete" type="button" class="btn btn-default" onclick="deleteDoc(0);">删除</button>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">网上办理流程说明</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <textarea class="form-control" style="width: 98%" name="wsbllcsm" rows="8"></textarea>
                                        <font style="color:red">*</font>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">网上办理流程图</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div style="text-align: left;">
                                        <input type="text" id="wsbllctDoc" name="wsbllct" class="hide"/>
                                        <input type="file" id="wsbllctDocFile" name="wsbllctDocFile" />
                                        <div id="wsbllctDocButton" class="form-group ">
                                            <button id="wsbllctDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(1);">下载</button>
                                            <button id="wsbllctDocDelete" type="button" class="btn btn-default" onclick="deleteDoc(1);">删除</button>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">内部许可流程说明</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <textarea class="form-control" style="width: 98%" name="nbxulcsm" rows="8"></textarea>
                                        <font style="color:red">*</font>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" colspan="2" style="border:1px solid #0094ff;">内部许可流程图</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div style="text-align: left;">
                                        <input type="text" id="nbxklctDoc" name="nbxklct" class="hide"/>
                                        <input type="file" id="nbxklctDocFile" name="nbxklctDocFile" />
                                        <div id="nbxklctDocButton" class="form-group ">
                                            <button id="nbxklctDocDownLoad" type="button" class="btn btn-info" onclick="downloadDoc(2);">下载</button>
                                            <button id="nbxklctDocDelete" type="button" class="btn btn-default" onclick="deleteDoc(2);">删除</button>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="row" style="padding-bottom:10px;padding-left:10px;padding-top: 10px;">
                        <div class="col-xl-12" style="text-align: center;">

                            <a  href="javascript:void(0)" onclick="saveForm();"
                                class="btn btn-info m-btn m-btn--icon">
                                <span><span>保存</span></span>
                            </a>&nbsp;
                            <%--<a href="javascript:void(0)" onclick="saveTemp();" class="btn btn-info m-btn m-btn--icon">--%>
                                <%--<span><span>暂存</span></span>--%>
                            <%--</a>&nbsp;--%>
                            <%--<a href="javascript:void(0)" onclick="commitApprove();" class="btn btn-info m-btn m-btn--icon">--%>
                                <%--<span><span>提交审查</span></span>--%>
                            <%--</a>&nbsp;--%>
                            <a  href="javascript:void(0)" onclick="closePage();"
                                class="btn btn-info m-btn m-btn--icon">
                                <span><span>关闭</span></span>
                            </a>&nbsp;
                        </div>
                    </div>
                </div>
                <button type="submit" id="item_legal_remedy_edit_from_submit" style="display:none;"></button>
        </form>
    </div>
</div>
</body>
</html>