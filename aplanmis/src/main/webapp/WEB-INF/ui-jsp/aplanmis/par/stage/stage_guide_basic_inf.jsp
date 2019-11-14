<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>基本信息</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp" %>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js"
            type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/register-list.css" rel="stylesheet"
          type="text/css"/>
    <link href="${pageContext.request.contextPath}/ui-static/mat/css/global_mat_index.css" rel="stylesheet"
          type="text/css"/>
    <%--<style type="text/css">--%>
        <%--.m-portlet {--%>
            <%--border: 0px solid #e8e8e8;--%>
        <%--}--%>

        <%--.border-none {--%>
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
        var curIsEditable = ${curIsEditable};
        var stageGuideId = '${stageGuideForm.guideId}';
    </script>

</head>
<body>
<div style="padding: 0px 5px 5px 5px;">
    <form id="stage_guide_basic_info_form" method="post" enctype="multipart/form-data">

        <input type="hidden" id="stage_guide_basic_info_id" name="guideId"
               value="${empty stageGuideForm.guideId?'':stageGuideForm.guideId}"/>
        <input type="hidden" name="stageId" value="${stageGuideForm.stageId}"/>

        <table class="matters-table matters-table-bordered matters-table-info" style="width:100%;height:100%;"
               data-toggle="table-collapse" data-row="3">

            <tbody>
            <tr style="display: table-row;">
                <th colspan="4">基本信息</th>
            </tr>
            <tr style="display: table-row;">
                <th>受理对象</th>
                <td colspan="3">
                    <c:forEach items="${applyObjList}" var="item">
                        <label>
                            <input type="checkbox" name="applyObj" value="${item.itemCode}" ${curIsEditable?'':'disabled'}
                                   title="${item.itemName}"/>${item.itemName}
                        </label>
                    </c:forEach>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>受理条件</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 80px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#acceptCond_stage_guide').val(this.innerHTML)">${empty stageGuideForm.acceptCond?(curIsEditable?'':'无'):stageGuideForm.acceptCond}</div>--%>
                    <%--<textarea name="acceptCond" id="acceptCond_stage_guide"--%>
                              <%--style="display:none">${empty stageGuideForm.acceptCond?(curIsEditable?'':'无'):stageGuideForm.acceptCond}</textarea>--%>

                <%--</td>--%>
                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="acceptCond_stage_guide" name="acceptCond" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.acceptCond}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>法定办结时限</th>
                <td>
                    <input type="number" min="0" name="legalDay"
                           class="form-control m-input   ${curIsEditable?'':'border-none'}"
                           value="${empty stageGuideForm.legalDay?(curIsEditable?'':'无'):stageGuideForm.legalDay}" ${curIsEditable?'':'readonly'} />

                </td>
                <th>法定办结时限单位</th>
                <td>
                    <select name="legalType" class="form-control m-input">
                        <option value="">${empty stageGuideForm.legalType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${legalTypes}" var="item">
                            <option value="${item.itemCode}" ${item.itemCode==stageGuideForm.legalType?'selected':''}>${item.itemName}</option>
                        </c:forEach>
                    </select>

                </td>
            </tr>
            <tr style="display: table-row;">
                <th>法定办结时限说明</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 80px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#legalDesc_stage_guide').val(this.innerHTML)">${empty stageGuideForm.legalDesc?(curIsEditable?'':'无'):stageGuideForm.legalDesc}</div>--%>
                    <%--<textarea name="legalDesc" id="legalDesc_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.legalDesc?(curIsEditable?'':'无'):stageGuideForm.legalDesc}</textarea>--%>
                <%--</td>--%>
                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="legalDesc_stage_guide" name="legalDesc" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.legalDesc}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>承诺办结时限</th>
                <td>
                    <input type="number" min="0" name="promiseDay"
                           class="form-control m-input ${curIsEditable?'':'border-none'}"
                           value="${empty stageGuideForm.legalDay?(curIsEditable?'':'无'):stageGuideForm.promiseDay}" ${curIsEditable?'':'readonly'} />

                </td>
                <th>承诺办结时限单位</th>
                <td>
                    <select name="promiseType" class="form-control m-input">
                        <option value="">${empty stageGuideForm.promiseType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                        <c:forEach items="${legalTypes}" var="item">
                            <option value="${item.itemCode}" ${item.itemCode==stageGuideForm.promiseType?'selected':''}>${item.itemName}</option>
                        </c:forEach>
                    </select>

                </td>
            </tr>

            <tr style="display: table-row;">
                <th>承诺办结时限说明</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 80px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#promiseDesc_stage_guide').val(this.innerHTML)">${empty stageGuideForm.promiseDesc?(curIsEditable?'':'无'):stageGuideForm.promiseDesc}</div>--%>
                    <%--<textarea name="promiseDesc" id="promiseDesc_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.promiseDesc?(curIsEditable?'':'无'):stageGuideForm.promiseDesc}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea id="promiseDesc_stage_guide" name="promiseDesc" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.promiseDesc}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>投诉监督窗口地址</th>
                <td colspan="3">
                    <input name="comsupAddress" class="form-control m-input ${curIsEditable?'':'border-none'}"
                           value="${empty stageGuideForm.comsupAddress?(curIsEditable?'':'无'):stageGuideForm.comsupAddress}" ${curIsEditable?'':'readonly'} />
                </td>

            </tr>
            <tr style="display: table-row;">
                <th>投诉监督信函地址</th>
                <td colspan="3">
                    <input name="comsupLetter" class="form-control m-input ${curIsEditable?'':'border-none'}"
                           value="${empty stageGuideForm.comsupLetter?(curIsEditable?'':'无'):stageGuideForm.comsupLetter}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>投诉监督邮箱地址</th>
                <td colspan="3">
                    <input name="comsupEmail" class="form-control m-input ${curIsEditable?'':'border-none'}"
                           value="${empty stageGuideForm.comsupEmail?(curIsEditable?'':'无'):stageGuideForm.comsupEmail}" ${curIsEditable?'':'readonly'} />
                </td>

            </tr>
            <tr style="display: table-row;">
                <th>投诉监督电话</th>
                <td colspan="3">
                    <input name="comsupTel" class="form-control m-input ${curIsEditable?'':'border-none'}"
                           value="${empty stageGuideForm.comsupTel?(curIsEditable?'':'无'):stageGuideForm.comsupTel}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>网上投诉监督网址</th>
                <td colspan="3">
                    <input name="comsupOnline" class="form-control m-input ${curIsEditable?'':'border-none'}"
                           value="${empty stageGuideForm.comsupOnline?(curIsEditable?'':'无'):stageGuideForm.comsupOnline}" ${curIsEditable?'':'readonly'} />
                </td>

            </tr>
            <tr style="display: table-row;">
                <th>窗口申报办理流程说明</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 120px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#ckbllc_stage_guide').val(this.innerHTML)">${empty stageGuideForm.ckbllc?(curIsEditable?'':'无'):stageGuideForm.ckbllc}</div>--%>
                    <%--<textarea name="ckbllc" id="ckbllc_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.ckbllc?(curIsEditable?'':'无'):stageGuideForm.ckbllc}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea name="ckbllc" id="ckbllc_stage_guide" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.ckbllc}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>窗口申报办理流程图(<font color="red">*建议上传图片</font>)</th></th>
                <td colspan="3">
                    <div class="form-group m-form__group row">
                        <div class="col-10">
                            <input id="ckbllctSample" type="file" class="form-control m-input"
                                   name="ckbllctSample" multiple="multiple" onchange="uploadFileChange(this);"/>
                            <span class="custorm-style">
                            <button class="left-button">选择文件</button>
                            <span class="right-text">未选择任何文件</span>
                        </span>
                        </div>
                        <div id="ckbllctSampleDiv" class="col-2">
                            <button id="ckbllctSampleButton" type="button" class="btn btn-info"
                                    onclick="showGuideBasicAtt('CKBLLCT');">查看附件
                            </button>
                        </div>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>网上办理流程说明</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 120px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#wsbllc_stage_guide').val(this.innerHTML)">${empty stageGuideForm.wsbllc?(curIsEditable?'':'无'):stageGuideForm.wsbllc}</div>--%>
                    <%--<textarea name="wsbllc" id="wsbllc_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.wsbllc?(curIsEditable?'':'无'):stageGuideForm.wsbllc}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea name="wsbllc" id="wsbllc_stage_guide" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.wsbllc}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>网上办理流程图(<font color="red">*建议上传图片</font>)</th></th>
                <td colspan="3">
                    <%--<input name="wsbllctSample"  id="wsbllctSample" class="form-control m-input ${curIsEditable?'':'hide'}"  type = "file"  &lt;%&ndash;multiple="multiple"&ndash;%&gt; accept="image/*" />--%>
                    <%--<span id="wsbllctSampleText" class="download blue-succ" >无</span>--%>
                    <div class="form-group m-form__group row">
                        <div class="col-10">
                            <input id="wsbllctSample" type="file" class="form-control m-input"
                                   name="wsbllctSample" multiple="multiple" onchange="uploadFileChange(this);"/>
                            <span class="custorm-style">
                            <button class="left-button">选择文件</button>
                            <span class="right-text">未选择任何文件</span>
                        </span>
                        </div>
                        <div id="wsbllctSampleDiv" class="col-2">
                            <button id="wsbllctSampleButton" type="button" class="btn btn-info"
                                    onclick="showGuideBasicAtt('WSBLLCT');">查看附件
                            </button>
                        </div>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>办理程序</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 80px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#handleFlow_stage_guide').val(this.innerHTML)">${empty stageGuideForm.handleFlow?(curIsEditable?'':'无'):stageGuideForm.handleFlow}</div>--%>
                    <%--<textarea name="handleFlow" id="handleFlow_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.handleFlow?(curIsEditable?'':'无'):stageGuideForm.handleFlow}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea name="handleFlow" id="handleFlow_stage_guide" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.handleFlow}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>办理时限说明</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 80px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#handleTimelimitDesc_stage_guide').val(this.innerHTML)">${empty stageGuideForm.handleTimelimitDesc?(curIsEditable?'':'无'):stageGuideForm.handleTimelimitDesc}</div>--%>
                    <%--<textarea name="handleTimelimitDesc" id="handleTimelimitDesc_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.handleTimelimitDesc?(curIsEditable?'':'无'):stageGuideForm.handleTimelimitDesc}</textarea>--%>

                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea name="handleTimelimitDesc" id="handleTimelimitDesc_stage_guide" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.handleTimelimitDesc}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>温馨提示</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 80px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#warmPrompt_stage_guide').val(this.innerHTML)">${empty stageGuideForm.warmPrompt?(curIsEditable?'':'无'):stageGuideForm.warmPrompt}</div>--%>
                    <%--<textarea name="warmPrompt" id="warmPrompt_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.warmPrompt?(curIsEditable?'':'无'):stageGuideForm.warmPrompt}</textarea>--%>

                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea name="warmPrompt" id="warmPrompt_stage_guide" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.warmPrompt}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>备注</th>
                <%--<td colspan="3">--%>
                    <%--<div class="form-control m-input textarea ${curIsEditable?'':'border-none'}" style="height: 80px;"--%>
                         <%--contenteditable="${curIsEditable?'true':'false'}"--%>
                         <%--onkeyup="$('#guideDemo_stage_guide').val(this.innerHTML)">${empty stageGuideForm.guideDemo?(curIsEditable?'':'无'):stageGuideForm.guideDemo}</div>--%>
                    <%--<textarea name="guideDemo" id="guideDemo_stage_guide" rows="6"--%>
                              <%--style="display: none">${empty stageGuideForm.guideDemo?(curIsEditable?'':'无'):stageGuideForm.guideDemo}</textarea>--%>
                <%--</td>--%>

                <td colspan="3">
                    <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                        <textarea name="guideDemo" id="guideDemo_stage_guide" class="form-control" rows="6" ${curIsEditable?'':'readonly'}>${stageGuideForm.guideDemo}</textarea>
                    </div>
                </td>
            </tr>
            <tr style="display: table-row;">
                <th>流程图附件(<font color="red">*建议上传图片</font>)</th></th>
                <td colspan="3">
                    <div class="form-group m-form__group row">
                        <div class="col-10">
                            <input id="guideAttFile" type="file" class="form-control m-input"
                                   name="guideAttFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                            <span class="custorm-style">
                            <button class="left-button">选择文件</button>
                            <span class="right-text">未选择任何文件</span>
                        </span>
                        </div>
                        <div id="guideAttFileDiv" class="col-2">
                            <button id="guideAttFileButton" type="button" class="btn btn-info"
                                    onclick="showGuideBasicAtt('GUIDE_ATT');">查看附件
                            </button>
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <c:if test="${curIsEditable==true}">
            <div id="saveItemGuideBtn" style="text-align: center;margin-top: 10px;">
                <button type="submit" class="btn btn-info " style="${curIsEditable?'':'display:none'}">保存</button>&nbsp;&nbsp;
                <button type="button" class="btn btn-secondary" onclick="window.location.reload();">刷新</button>
            </div>
        </c:if>
    </form>
</div>

<!-- 进度弹窗 -->
<div id="uploadProgress" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="dialog_item_dept"
     aria-hidden="true">
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
<%@include file="show_guide_basic_att.jsp" %>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_guide_basic_inf.js?<%=isDebugMode%>" type="text/javascript"></script>
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

        clearGuideBasicFile();
        if (${stageGuideForm.guideAttNum>0}) {
            $('#guideAttFileDiv').show();
            $('#guideAttFileButton').html(${stageGuideForm.guideAttNum} +"个附件");
        } else {
            $('#guideAttFileDiv').hide();
        }
        if (${stageGuideForm.ckbllctNum>0}) {
            $('#ckbllctSampleDiv').show();
            $('#ckbllctSampleButton').html(${stageGuideForm.ckbllctNum} +"个附件");
        } else {
            $('#ckbllctSampleDiv').hide();
        }

        if (${stageGuideForm.wsbllctNum>0}) {
            $('#wsbllctSampleDiv').show();
            $('#wsbllctSampleButton').html(${stageGuideForm.wsbllctNum} +"个附件");
        } else {
            $('#wsbllctSampleDiv').hide();
        }
        var applyObj = '${stageGuideForm.applyObj}';
        loadCheckbox(applyObj, 'applyObj');
    });
</script>
</body>
</html>
