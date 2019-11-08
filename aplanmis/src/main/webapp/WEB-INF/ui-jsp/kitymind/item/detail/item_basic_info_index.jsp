<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>事项基本信息</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/register-list.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        .m-portlet {
            border: 0px solid #e8e8e8;
        }
    </style>
</head>
<body>
    <div style="padding: 5px;">
        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;height:100%;" data-toggle="table-collapse" data-row="3">

            <tbody>
                <tr style="display: table-row;">
                    <th>事项名称</th>
                    <td>
                        <p class="matters-truncate">
                            ${empty itemBasic.itemName?'无':itemBasic.itemName}
                        </p>
                    </td>
                    <th>事项类型</th>
                    <td>
                        <p class="matters-truncate">
                            <c:forEach items="${itemTypes}" var="itemType">
                                <c:if test="${itemType.itemCode==itemBasic.itemType}">${empty itemType.itemName?'无':itemType.itemName}</c:if>
                            </c:forEach>
                        </p>
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>基本编码</th>
                    <td><p class="matters-truncate">
                        ${empty itemBasic.basecode?'0227142000':itemBasic.basecode}
                    </p></td>
                    <th>实施编码</th>
                    <td><p class="matters-truncate">
                        ${empty itemBasic.sscode?'11441900773094926W30227142000':itemBasic.sscode}
                    </p></td>
                </tr>

                <tr style="display: table-row;">
                    <th>行使层级</th>
                    <td><p class="matters-truncate">市级</p></td>
                    <th>处罚的行为、种类、幅度</th>
                    <td>
                        <p class="matters-truncate">
                            对违反《广播电视视频点播业务管理办法》第二十八条规定，
                            播出前端未按规定与广播电视行政部门监控系统进行联网行为的处罚
                        </p>
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>实施主体</th>
                    <td><p class="matters-truncate">东莞市文化广电新闻出版局</p></td>
                    <th>实施主体性质</th>
                    <td><p class="matters-truncate">法定机关</p></td>
                </tr>

                <tr style="display: table-row;">
                    <th>法定办结时限</th>
                    <td>
                        <p class="matters-truncate">无&nbsp;&nbsp;</p>
                    </td>
                    <th>业务系统</th>
                    <td><p class="matters-truncate">无</p></td>
                </tr>

                <tr style="display: table-row;">
                    <th>结果名称</th>
                    <td><p class="matters-truncate">无</p></td>
                    <th>结果样本</th>
                    <td>
                        <p class="matters-truncate">无</p>
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>办理结果类型</th>
                    <td colspan="3">
                        <p class="matters-truncate">
                            <c:forEach items="${itemPropertys}" var="itemProperty">
                                <c:if test="${itemProperty.itemCode==itemBasic.itemProperty}">
                                   ${itemProperty.itemName}
                                </c:if>
                            </c:forEach>
                        </p>
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>权力来源</th>
                    <td><p class="matters-truncate">法定本级行使</p></td>
                    <th>联办机构</th>
                    <td><p class="matters-truncate">无</p></td>
                </tr>
            </tbody>
        </table>
    </div>
</body>
</html>