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
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/register-list.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/ui-static/mat/css/global_mat_index.css" rel="stylesheet" type="text/css" />
    <%--<style type="text/css">--%>
        <%--.m-portlet {--%>
            <%--border: 0px solid #e8e8e8;--%>
        <%--}--%>

        <%--.border-none{--%>
            <%--border: none;--%>
        <%--}--%>
        <%--.textarea {--%>

            <%--_height: 120px;--%>
            <%--margin-left: auto;--%>
            <%--margin-right: auto;--%>
            <%--padding: 3px;--%>
            <%--outline: 0;--%>
            <%--border: 1px solid #a0b3d6;--%>
            <%--font-size: 12px;--%>
            <%--line-height: 24px;--%>
            <%--padding: 2px;--%>
            <%--word-wrap: break-word;--%>
            <%--overflow-x: hidden;--%>
            <%--overflow-y: auto;--%>
        <%--}--%>

        <%--.blue-succ {--%>
            <%--color: #3E97DF;--%>
            <%--cursor: pointer;--%>
        <%--}--%>
    <%--</style>--%>
    <script type="text/javascript">
        var itemVerId = '${itemVerId}';
        var curIsEditable = ${curIsEditable};
        var itemGuideId = '${aeaItemGuide.id}';
    </script>
</head>
<body>
<div style="padding: 0px 5px 5px 5px;">
    <form id="item_basic_info_form" method="post" enctype="multipart/form-data" >

        <input type="hidden" name="id" id="item_basic_info_id" value = "${empty aeaItemGuide.id?'':aeaItemGuide.id}" />
        <input type="hidden" name="itemVerId" value = "${itemVerId}" />

        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;height:100%;" data-toggle="table-collapse" data-row="3">

            <tbody>
            <tr style="display: table-row;">
                <th colspan="4" >基本信息</th>
            </tr>
            <tr style="display: table-row;">
                <th>事项名称</th>
                <td colspan="3">
                    <input name = "taskName" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.taskName?(curIsEditable?'':'无'):aeaItemGuide.taskName}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>基本编码</th>
                <td>
                    <input name = "catlogCode" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.catlogCode?(curIsEditable?'':'无'):aeaItemGuide.catlogCode}" ${curIsEditable?'':'readonly'} />
                </td>

                <th>实施编码</th>
                <td>
                    <input name = "taskCode" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.taskCode?(curIsEditable?'':'无'):aeaItemGuide.taskCode}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>事项类型</th>
                <td>
                    <select name = "taskType" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#taskTypeText').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.taskType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${itemTypes}" var="itemType">
                            <option value="${itemType.itemCode}" ${itemType.itemCode==aeaItemGuide.taskType?'selected':''}>${itemType.itemName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name = "taskTypeText" id = "taskTypeText" value="${empty aeaItemGuide.taskTypeText?'':aeaItemGuide.taskTypeText}" />
                </td>
                <th>权力来源</th>
                <td>
                    <select name = "itemsource" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#itemsourceText').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.itemsource?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${itemSources}" var="itemSource">
                            <option value="${itemSource.itemCode}" ${itemSource.itemCode==aeaItemGuide.itemsource?'selected':''}>${itemSource.itemName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name = "itemsourceText" id = "itemsourceText" value="${empty aeaItemGuide.itemsourceText?'':aeaItemGuide.itemsourceText}" />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>实施主体</th>
                <td>
                    <input name = "deptName" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.deptName?(curIsEditable?'':'无'):aeaItemGuide.deptName}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>实施主体编码</th>
                <td>
                    <input name = "deptCode" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.deptCode?(curIsEditable?'':'无'):aeaItemGuide.deptCode}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>实施主体性质</th>
                <td>
                    <select name = "deptType" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#deptTypeText').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.deptType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${deptTypes}" var="deptType">
                            <option value="${deptType.itemCode}" ${deptType.itemCode==aeaItemGuide.deptType?'selected':''}>${deptType.itemName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name = "deptTypeText" id = "deptTypeText" value="${empty aeaItemGuide.deptTypeText?'':aeaItemGuide.deptTypeText}"/>
                </td>
                <th>行使层级</th>
                <td>
                    <select name = "userLevel" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#useLevelText').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.userLevel?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${useLevels}" var="useLevel">
                            <option value="${useLevel.itemCode}" ${useLevel.itemCode==aeaItemGuide.userLevel?'selected':''}>${useLevel.itemName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name = "useLevelText" id = "useLevelText" value="${empty aeaItemGuide.useLevelText?'':aeaItemGuide.useLevelText}" />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>受理条件</th>
                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="acceptCondition" name="acceptCondition" class="form-control" rows="5" ${curIsEditable?'':'readonly'}>${aeaItemGuide.acceptCondition}</textarea>
                    </div>
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>受理权限</th>
                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="acceptPriv" name="acceptPriv" class="form-control" rows="5" ${curIsEditable?'':'readonly'}>${aeaItemGuide.acceptPriv}</textarea>
                    </div>
                </td>
            </tr>

            <tr style="display: table-row;">

                <th>办件类型</th>
                <td>
                    <select name = "projectType" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#projectTypeTexy').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.projectType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${itemPropertys}" var="itemProperty">
                            <option value="${itemProperty.itemCode}" ${itemProperty.itemCode==aeaItemGuide.projectType?'selected':''}>${itemProperty.itemName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name = "projectTypeTexy" id = "projectTypeTexy" value="${empty aeaItemGuide.projectTypeTexy?'':aeaItemGuide.projectTypeTexy}" />
                </td>

                <th>是否收费</th>
                <td>
                    <select name = "isFee" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#isFeeText').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.isFee?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <option value="0" ${'0'==aeaItemGuide.isFee?'selected':''}>否</option>
                        <option value="1" ${'1'==aeaItemGuide.isFee?'selected':''}>是</option>
                    </select>
                    <input type="hidden" name = "isFeeText" id = "isFeeText" value="${empty aeaItemGuide.isFeeText?'':aeaItemGuide.isFeeText}" />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>法定办结时限</th>
                <td>
                    <input type="number" min="0" name = "anticipateDay" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.anticipateDay?(curIsEditable?'':'无'):aeaItemGuide.anticipateDay}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>法定办结时限单位</th>
                <td>
                    <select name = "anticipateType" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#anticipateTypeText').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.anticipateType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${dueUnits}" var="dueUnit">
                            <option value="${dueUnit.itemCode}" ${dueUnit.itemCode==aeaItemGuide.anticipateType?'selected':''}>${dueUnit.itemName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name = "anticipateTypeText" id = "anticipateTypeText" value="${empty aeaItemGuide.anticipateTypeText?'':aeaItemGuide.anticipateTypeText}" />
                </td>
            </tr>

            <%--<tr style="display: table-row;">--%>
                <%--<th>法定办结时限说明</th>--%>
                <%--<td colspan="3">--%>
                    <%--<input name = "fdblsxsm" class="form-control m-input ${curIsEditable?'':'border-none'}" style="height: 80px;"  value="${empty aeaItemGuide.fdblsxsm?(curIsEditable?'':'无'):aeaItemGuide.fdblsxsm}" ${curIsEditable?'':'readonly'} />--%>
                <%--</td>--%>
            <%--</tr>--%>

            <tr style="display: table-row;">
                <th>法定办结时限说明</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input ${curIsEditable?'':'border-none'}" style="height: 100px;" contenteditable="${curIsEditable?'true':'false'}" onkeyup="$('#fdblsxsm').val(this.innerHTML)">${empty aeaItemGuide.fdblsxsm?(curIsEditable?'':'无'):aeaItemGuide.fdblsxsm}</div>--%>
                    <%--<textarea name = "fdblsxsm" id = "fdblsxsm" rows="5" style="display: none">${empty aeaItemGuide.fdblsxsm?(curIsEditable?'':'无'):aeaItemGuide.fdblsxsm}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="fdblsxsm" name="fdblsxsm" class="form-control" rows="5" ${curIsEditable?'':'readonly'}>${aeaItemGuide.fdblsxsm}</textarea>
                    </div>
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>承诺办结时限</th>
                <td>
                    <input type="number" min="0" name = "promiseDay" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.promiseDay?(curIsEditable?'':'无'):aeaItemGuide.promiseDay}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>承诺办结时限单位</th>
                <td>
                    <select name = "promiseType" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#promiseTypeText').val(this.options[this.options.selectedIndex].text)">
                        <option value="" >${empty aeaItemGuide.promiseType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${dueUnits}" var="dueUnit">
                            <option value="${dueUnit.itemCode}" ${dueUnit.itemCode==aeaItemGuide.promiseType?'selected':''}>${dueUnit.itemName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name = "promiseTypeText" id = "promiseTypeText" value="${empty aeaItemGuide.promiseTypeText?'':aeaItemGuide.promiseTypeText}" />
                </td>
            </tr>

            <%--<tr style="display: table-row;">--%>
                <%--<th>承诺办结时限说明</th>--%>
                <%--<td colspan="3">--%>
                    <%--<input name = "crbjsxsm" class="form-control m-input ${curIsEditable?'':'border-none'}"  style="height: 80px;" value="${empty aeaItemGuide.crbjsxsm?(curIsEditable?'':'无'):aeaItemGuide.crbjsxsm}" ${curIsEditable?'':'readonly'} />--%>
                <%--</td>--%>
            <%--</tr>--%>

            <tr style="display: table-row;">
                <th>承诺办结时限说明</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input ${curIsEditable?'':'border-none'}" style="height: 100px;" contenteditable="${curIsEditable?'true':'false'}" onkeyup="$('#crbjsxsm').val(this.innerHTML)">${empty aeaItemGuide.crbjsxsm?(curIsEditable?'':'无'):aeaItemGuide.crbjsxsm}</div>--%>
                    <%--<textarea name = "crbjsxsm" id = "crbjsxsm" rows="5" style="display: none">${empty aeaItemGuide.crbjsxsm?(curIsEditable?'':'无'):aeaItemGuide.crbjsxsm}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="crbjsxsm" name="crbjsxsm" class="form-control" rows="5" ${curIsEditable?'':'readonly'}>${aeaItemGuide.crbjsxsm}</textarea>
                    </div>
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>办理形式</th>
                <td colspan="3">
                    <c:forEach items="${itemSlfss}" var="itemSlfs">
                        <label><input type="checkbox" name="handleType" value="${itemSlfs.itemCode}" ${curIsEditable?'':'disabled'} onchange="loadCheckboxText('handleTypeText','handleType');"  title="${itemSlfs.itemName}"/>${itemSlfs.itemName}</label>
                    </c:forEach>
                    <input type="hidden" name = "handleTypeText" id = "handleTypeText" value="${empty aeaItemGuide.handleTypeText?'':aeaItemGuide.handleTypeText}" />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>服务对象</th>
                <td colspan="3">
                    <c:forEach items="${itemFwjgxzs}" var="itemFwjgxz">
                        <label><input type="checkbox" name="serveType" value="${itemFwjgxz.itemCode}" ${curIsEditable?'':'disabled'} onchange="loadCheckboxText('serveTypeText','serveType');"  title="${itemFwjgxz.itemName}"/>${itemFwjgxz.itemName}</label>
                    </c:forEach>
                    <input type="hidden" name = "serveTypeText" id = "serveTypeText" value="${empty aeaItemGuide.serveTypeText?'':aeaItemGuide.serveTypeText}" />
                </td>
            </tr>

            <%--<tr style="display: table-row;">--%>
                <%--<th>网上办理流程</th>--%>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input ${curIsEditable?'':'border-none'}" style="height: 80px;" contenteditable="${curIsEditable?'true':'false'}" onkeyup="$('#wsbllc').val(this.innerHTML)">${empty aeaItemGuide.wsbllc?(curIsEditable?'':'无'):aeaItemGuide.wsbllc}</div>--%>
                    <%--<textarea name = "wsbllc" id = "wsbllc" rows="5" style="display: none">${empty aeaItemGuide.wsbllc?(curIsEditable?'':'无'):aeaItemGuide.wsbllc}</textarea>--%>
                <%--</td>--%>
            <%--</tr>--%>

            <tr style="display: table-row;">
                <th>网上办理流程</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input ${curIsEditable?'':'border-none'}" style="height: 100px;" contenteditable="${curIsEditable?'true':'false'}" onkeyup="$('#wsbllc').val(this.innerHTML)">${empty aeaItemGuide.wsbllc?(curIsEditable?'':'无'):aeaItemGuide.wsbllc}</div>--%>
                    <%--<textarea name = "wsbllc" id = "wsbllc" rows="5" style="display: none">${empty aeaItemGuide.wsbllc?(curIsEditable?'':'无'):aeaItemGuide.wsbllc}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="wsbllc" name="wsbllc" class="form-control" rows="5" ${curIsEditable?'':'readonly'}>${aeaItemGuide.wsbllc}</textarea>
                    </div>
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>网上办理流程图(<font color="red">*建议上传图片</font>)</th>
                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <div class="col-10" style="padding-left: 0px;">
                            <input id="wsbllctSample" type="file" class="form-control m-input"
                                   name="wsbllctSample" multiple="multiple" onchange="uploadFileChange(this);"/>
                            <span class="custorm-style">
                                <button class="left-button">选择文件</button>
                                <span class="right-text">未选择任何文件</span>
                            </span>
                        </div>
                        <div id="wsbllctSampleDiv" class="col-2">
                            <button id="wsbllctSampleButton" type="button" class="btn btn-info"
                                    onclick="showGuideBasicAtt('WSBLLCT');">查看附件</button>
                        </div>
                    </div>
                </td>
            </tr>

            <%--<tr style="display: table-row;">--%>
                <%--<th>窗口办理流程</th>--%>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input ${curIsEditable?'':'border-none'}" style="height: 80px;" contenteditable="${curIsEditable?'true':'false'}" onkeyup="$('#ckbllc').val(this.innerHTML)">${empty aeaItemGuide.ckbllc?(curIsEditable?'':'无'):aeaItemGuide.ckbllc}</div>--%>
                    <%--<textarea name = "ckbllc" id = "ckbllc"  rows="5" style="display:none">${empty aeaItemGuide.ckbllc?(curIsEditable?'':'无'):aeaItemGuide.ckbllc}</textarea>--%>
                <%--</td>--%>
            <%--</tr>--%>

            <tr style="display: table-row;">
                <th>窗口办理流程</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input ${curIsEditable?'':'border-none'}" style="height: 100px;" contenteditable="${curIsEditable?'true':'false'}" onkeyup="$('#ckbllc').val(this.innerHTML)">${empty aeaItemGuide.ckbllc?(curIsEditable?'':'无'):aeaItemGuide.ckbllc}</div>--%>
                    <%--<textarea name = "ckbllc" id = "ckbllc" rows="5" style="display: none">${empty aeaItemGuide.ckbllc?(curIsEditable?'':'无'):aeaItemGuide.ckbllc}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="ckbllc" name="ckbllc" class="form-control" rows="5" ${curIsEditable?'':'readonly'}>${aeaItemGuide.ckbllc}</textarea>
                    </div>
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>窗口办理流程图(<font color="red">*建议上传图片</font>)</th>
                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <div class="col-10" style="padding-left: 0px;">
                            <input id="ckbllctSample" type="file" class="form-control m-input"
                                   name="ckbllctSample" multiple="multiple" onchange="uploadFileChange(this);"/>
                            <span class="custorm-style">
                            <button class="left-button">选择文件</button>
                            <span class="right-text">未选择任何文件</span>
                        </span>
                        </div>
                        <div id="ckbllctSampleDiv" class="col-2">
                            <button id="ckbllctSampleButton" type="button" class="btn btn-info"
                                    onclick="showGuideBasicAtt('CKBLLCT');">查看附件</button>
                        </div>
                    </div>
                </td>
            </tr>

            </tbody>
        </table>

        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;margin-top:30px;margin-bottom: 30px" data-toggle="table-collapse" data-row="3">

            <tbody>
            <tr style="display: table-row;">
                <th colspan="4" >监督方式</th>
            </tr>
            <tr style="display: table-row;">
                <th>咨询电话</th>
                <td>
                    <input name = "linkway" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.linkway?(curIsEditable?'':'无'):aeaItemGuide.linkway}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>投诉电话</th>
                <td>
                    <input name = "tstel" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.tstel?(curIsEditable?'':'无'):aeaItemGuide.tstel}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>


            <tr style="display: table-row;">
                <th>投诉地址</th>
                <td>
                    <input name = "tsckaddress" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.tsckaddress?(curIsEditable?'':'无'):aeaItemGuide.tsckaddress}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>投诉网址</th>
                <td>
                    <input name = "wstswz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.wstswz?(curIsEditable?'':'无'):aeaItemGuide.wstswz}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>电子邮箱</th>
                <td>
                    <input name = "tsdzyx" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.tsdzyx?(curIsEditable?'':'无'):aeaItemGuide.tsdzyx}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>信函地址</th>
                <td>
                    <input name = "xhtsdz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.xhtsdz?(curIsEditable?'':'无'):aeaItemGuide.xhtsdz}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>
            </tbody>
        </table>
        <c:if test="${curIsEditable==true}">
            <div id="saveItemGuideBtn" style="text-align: center;margin-top: 10px;">
                <button type="submit" class="btn btn-info "  style="${curIsEditable?'':'display:none'}">保存</button>&nbsp;&nbsp;
                <button type="button" class="btn btn-secondary" onclick="window.location.reload();">刷新</button>
            </div>
        </c:if>
    </form>
</div>

<!-- 进度弹窗 -->
<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 600px;">
        <div class="modal-content">
            <div class="modal-body" style="text-align: center;padding: 10px;">
                <img src="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/icons/16/loading.gif"/>
                <div id="uploadProgressMsg" style="padding-top: 5px;">数据修复中，请稍后...</div>
            </div>
        </div>
    </div>
</div>

<!-- 附件 -->
<%@include file="show_guide_basic_att.jsp"%>
<script src="${pageContext.request.contextPath}/ui-static/item/guide/basic_info_index.js?<%=isDebugMode%>" type="text/javascript"></script>
<script type="text/javascript">

    $(function () {

        $('#show_guide_basic_att_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        clearGuideBasicFile();
        if (${aeaItemGuide.ckbllctNum>0}) {
            $('#ckbllctSampleDiv').show();
            $('#ckbllctSampleButton').html(${aeaItemGuide.ckbllctNum} + "个附件");
        }else{
            $('#ckbllctSampleDiv').hide();
        }

        if(${aeaItemGuide.wsbllctNum>0}){
            $('#wsbllctSampleDiv').show();
            $('#wsbllctSampleButton').html(${aeaItemGuide.wsbllctNum} + "个附件");
        }else{
            $('#wsbllctSampleDiv').hide();
        }

        var handleType = '${aeaItemGuide.handleType}';
        var serveType = '${aeaItemGuide.serveType}';
        loadCheckbox(handleType,'handleType');
        loadCheckbox(serveType,'serveType');
    });
</script>
</body>
</html>