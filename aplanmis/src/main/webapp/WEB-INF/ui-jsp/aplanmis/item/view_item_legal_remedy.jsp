<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <title>事项许可法律救济</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_legal_remedy.js"></script>
    <script type="text/javascript">
        var  legalRemedyId = '${legalRemedyId}';
        var  itemBasicId = '${itemBasicId}';
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
        <%--法律救济表单--%>
        <form class="m-form m-form--fit m-form--label-align-right" id="item_legal_remedy_edit_from">
            <!---------------- 隐藏域区域 开始 ----------------->
            <input type="hidden"  name="itemBasicId" value="${itemBasicId}"/>
            <input type="hidden" id="legalRemedyId" name="legalRemedyId"/>
            <!---------------- 隐藏域区域 结束 ----------------->
                <div style="text-align: center;">
                    <h3 class="m-portlet__head-text" id="item_legal_remedy_title" style="font-weight:bold;">
                        法律救济
                    </h3>
                </div>
                <div>
                    <table data-toggle="table" style=" border:1px solid #0094ff;width: 80%;margin-left: 200px;margin-top: 20px;">
                        <tbody>
                            <tr align="center">
                                <td rowspan="6" class="backgroundCorlor" style=" border:1px solid #0094ff;">投诉</td>
                                <td rowspan="5" class="backgroundCorlor" style=" border:1px solid #0094ff;">投诉渠道</td>
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">窗口投诉</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="winComplaint" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">电话投诉</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="telComplaint" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">网上投诉</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="onlineComplaint" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">电子邮件投诉</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="emailComplaint" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">信函投诉</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="letterComplaint" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center" style="height: 100px;">
                                <td class="backgroundCorlor" colspan="2" style="border:1px solid #0094ff;">投诉回复时间及形式</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <textarea class="form-control" style="width: 98%" name="replyTimeForm" rows="3" readonly="readonly"></textarea>
                                    </div>

                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" rowspan="4" colspan="2" style=" border:1px solid #0094ff;">行政复议</td>
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">部门</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admReconDept" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">地址</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admReconAddress" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">电话</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admReconTel" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">网址</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admReconNetAddress" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" rowspan="4" colspan="2" style=" border:1px solid #0094ff;">行政诉讼</td>
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">部门</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admProceDept" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">地址</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admProceAddress" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">电话</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admProceTel" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                            <tr align="center">
                                <td class="backgroundCorlor" style=" border:1px solid #0094ff;">网址</td>
                                <td colspan="5" style=" border:1px solid #0094ff;">
                                    <div  class="form-inline">
                                        <input type="text" class="form-control" style="width: 98%" name="admProceNetAddress" value="" readonly="readonly"/>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <button type="submit" id="item_legal_remedy_edit_from_submit" style="display:none;"></button>
        </form>
    </div>
</div>
</body>
</html>