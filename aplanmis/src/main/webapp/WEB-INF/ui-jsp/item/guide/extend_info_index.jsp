<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>扩展信息</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/register-list.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/ui-static/mat/global/css/global_material_index.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        .m-portlet {
            border: 0px solid #e8e8e8;
        }

        .border-none{
            border: none;
        }
        .textarea {

            _height: 120px;
            margin-left: auto;
            margin-right: auto;
            padding: 3px;
            outline: 0;
            border: 1px solid #a0b3d6;
            font-size: 12px;
            line-height: 24px;
            padding: 2px;
            word-wrap: break-word;
            overflow-x: hidden;
            overflow-y: auto;
        }

        .blue-succ {
            color: #3E97DF;
            cursor: pointer;
        }
    </style>
    <script type="text/javascript">
        var itemVerId = '${itemVerId}';
        var curIsEditable = ${curIsEditable};
        var itemGuideExtendId = '${aeaItemGuideExtend.id}';
    </script>
</head>
<body>
<div style="padding: 0px 5px 5px 5px;">
    <form id="item_extend_info_form" method="post" enctype="multipart/form-data" >
        <input id="item_extend_info_id" type="hidden" name = "id" value = "${empty aeaItemGuideExtend.id?'':aeaItemGuideExtend.id}" />
        <input type="hidden" name = "itemVerId" value = "${itemVerId}" />
        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;height:100%;" data-toggle="table-collapse" data-row="3">

            <tbody>
                <tr style="display: table-row;">
                    <th colspan="4" >事项扩展信息</th>
                </tr>

                <tr style="display: table-row;">
                    <th>结果名称</th>
                    <td colspan="3">
                        <input name = "resultName" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.resultName?(curIsEditable?'':'无'):aeaItemGuideExtend.resultName}" ${curIsEditable?'':'readonly'} />
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>结果样本</th>
                    <td colspan="3">
                        <div class="form-group m-form__group row" style="margin-bottom: 0rem;">
                            <div class="col-10" style="padding-left: 0px;">
                                <input id="resultSample" type="file" class="form-control m-input" name="resultSample"
                                       multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style" >
                                    <button class="left-button" >选择文件</button>
                                    <span class="right-text" >未选择任何文件</span>
                                </span>
                            </div>
                            <div id="resultSampleDiv" class="col-2">
                                <button id="resultSampleButton" type="button" class="btn btn-info"
                                        onclick="showGuideExtendAtt('ZZZ_RESULT_GUID');">查看附件</button>
                            </div>
                        </div>
                    </td>
                </tr>

                <tr style="display: table-row;">

                    <th>业务系统</th>
                    <td>
                        <input name = "xtmc" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xtmc?(curIsEditable?'':'无'):aeaItemGuideExtend.xtmc}" ${curIsEditable?'':'readonly'} />
                    </td>

                    <th>联办机构</th>
                    <td>
                        <input name = "otherDept" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.otherDept?(curIsEditable?'':'无'):aeaItemGuideExtend.otherDept}" ${curIsEditable?'':'readonly'} />
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>是否支持预约办理</th>
                    <td>
                        <select name = "isSchedule" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#isScheduleText').val(this.options[this.options.selectedIndex].text)">
                            <option value="" >${empty aeaItemGuideExtend.isSchedule?(curIsEditable?'请选择':'无'):'请选择'}</option>
                            <option value="0" ${'0'==aeaItemGuideExtend.isSchedule?'selected':''}>否</option>
                            <option value="1" ${'1'==aeaItemGuideExtend.isSchedule?'selected':''}>是</option>
                        </select>
                        <input type="hidden" name = "isScheduleText" id = "isScheduleText" value="${empty aeaItemGuideExtend.isScheduleText?'':aeaItemGuideExtend.isScheduleText}"/>
                    </td>

                    <th>是否支持物流快递</th>
                    <td>
                        <select name = "isExpress" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#isExpressText').val(this.options[this.options.selectedIndex].text)">
                            <option value="" >${empty aeaItemGuideExtend.isExpress?(curIsEditable?'请选择':'无'):'请选择'}</option>
                            <option value="0" ${'0'==aeaItemGuideExtend.isExpress?'selected':''}>否</option>
                            <option value="1" ${'1'==aeaItemGuideExtend.isExpress?'selected':''}>是</option>
                        </select>
                        <input type="hidden" name = "isExpressText" id = "isExpressText" value="${empty aeaItemGuideExtend.isExpressText?'':aeaItemGuideExtend.isExpressText}"/>
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>审批服务形式</th>
                    <td colspan="3">
                        <label><input type="checkbox" name="serviceType" value="1" ${curIsEditable?'':'disabled'} onchange="loadCheckboxText('serviceTypeText','serviceType');"  title = "马上办" />马上办&nbsp;</label>
                        <label><input type="checkbox" name="serviceType" value="2" ${curIsEditable?'':'disabled'} onchange="loadCheckboxText('serviceTypeText','serviceType');"  title = "网上办" />网上办&nbsp;</label>
                        <label><input type="checkbox" name="serviceType" value="3" ${curIsEditable?'':'disabled'} onchange="loadCheckboxText('serviceTypeText','serviceType');" title = "就近办" />就近办&nbsp;</label>
                        <label><input type="checkbox" name="serviceType" value="4" ${curIsEditable?'':'disabled'} onchange="loadCheckboxText('serviceTypeText','serviceType');" title = "一次办" />一次办&nbsp;</label>
                        <label><input type="checkbox" name="serviceType" value="5" ${curIsEditable?'':'disabled'} onchange="loadCheckboxText('serviceTypeText','serviceType');" title = "以上都不是" />以上都不是&nbsp;</label>
                        <label><input type="hidden" name = "serviceTypeText" id = "serviceTypeText" value="${empty aeaItemGuideExtend.serviceTypeText?'':aeaItemGuideExtend.serviceTypeText}" />
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>行使内容</th>
                    <td>
                        <input name = "xsnr" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xsnr?(curIsEditable?'':'无'):aeaItemGuideExtend.xsnr}" ${curIsEditable?'':'readonly'} />
                    </td>
                    <th>通办范围</th>
                    <td>
                        <select name = "handleArea" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#handleAreaText').val(this.options[this.options.selectedIndex].text)">
                            <option value="" >${empty aeaItemGuideExtend.handleArea?(curIsEditable?'请选择':'无'):'请选择'}</option>
                            <option value="0" ${'0'==aeaItemGuideExtend.handleArea?'selected':''}>全国</option>
                            <option value="1" ${'1'==aeaItemGuideExtend.handleArea?'selected':''}>跨省</option>
                            <option value="1" ${'2'==aeaItemGuideExtend.handleArea?'selected':''}>跨市</option>
                            <option value="1" ${'3'==aeaItemGuideExtend.handleArea?'selected':''}>跨县</option>
                            <option value="1" ${'4'==aeaItemGuideExtend.handleArea?'selected':''}>不通办</option>
                        </select>
                        <input type="hidden" name = "handleAreaText" id = "handleAreaText" value="${empty aeaItemGuideExtend.handleAreaText?'':aeaItemGuideExtend.handleAreaText}"/>
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>是否有数量限制</th>
                    <td>
                        <select name = "isLimit" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#isLimitText').val(this.options[this.options.selectedIndex].text)">
                            <option value="" >${empty aeaItemGuideExtend.isLimit?(curIsEditable?'请选择':'无'):'请选择'}</option>
                            <option value="0" ${'0'==aeaItemGuideExtend.isLimit?'selected':''}>否</option>
                            <option value="1" ${'1'==aeaItemGuideExtend.isLimit?'selected':''}>是</option>
                        </select>
                        <input type="hidden" name = "isLimitText" id = "isLimitText" value="${empty aeaItemGuideExtend.isLimitText?'':aeaItemGuideExtend.isLimitText}"/>
                    </td>

                    <th>主题分类</th>
                    <td>
                        <select name = "themeNaturalType" class="form-control m-input" ${curIsEditable?'':'disabled'} onchange="$('#themeNaturalTypeText').val(this.options[this.options.selectedIndex].text)">
                            <option value="" >${empty aeaItemGuideExtend.themeNaturalType?(curIsEditable?'请选择':'无'):'请选择'}</option>
                            <c:forEach items="${themeTypes}" var="themeType">
                                <option value="${themeType.itemCode}" ${themeType.itemCode==aeaItemGuideExtend.themeNaturalType?'selected':''}>${themeType.itemName}</option>
                            </c:forEach>
                        </select>
                        <input type="hidden" name = "themeNaturalTypeText" id = "themeNaturalTypeText" value="${empty aeaItemGuideExtend.themeNaturalTypeText?'':aeaItemGuideExtend.themeNaturalTypeText}"/>
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>申请内容</th>
                    <td colspan="3" >
                        <%--<input name="zzzSqnr" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.zzzSqnr?(curIsEditable?'':'无'):aeaItemGuideExtend.zzzSqnr}" ${curIsEditable?'':'readonly'} />--%>
                        <textarea name="zzzSqnr" class="form-control m-input ${curIsEditable?'':'border-none'}" rows="8" ${curIsEditable?'':'readonly'}>${empty aeaItemGuideExtend.zzzSqnr?(curIsEditable?'':'无'):aeaItemGuideExtend.zzzSqnr}</textarea>
                    </td>
                </tr>

            </tbody>
        </table>

        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;margin-top:30px;" data-toggle="table-collapse" data-row="3">
            <tbody>
                <tr style="display: table-row;">
                    <th colspan="4" >咨询方式</th>
                </tr>
                 <tr style="display: table-row;">
                     <th>咨询地址</th>
                     <td>
                         <input name = "zxckaddress" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.zxckaddress?(curIsEditable?'':'无'):aeaItemGuideExtend.zxckaddress}" ${curIsEditable?'':'readonly'} />
                     </td>

                     <th>咨询网址</th>
                     <td>
                         <input name = "ssjgwz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.ssjgwz?(curIsEditable?'':'无'):aeaItemGuideExtend.ssjgwz}" ${curIsEditable?'':'readonly'} />
                     </td>
                 </tr>


                <tr style="display: table-row;">
                    <th>政务微博</th>
                    <td>
                        <input name = "zxzwwbwz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.zxzwwbwz?(curIsEditable?'':'无'):aeaItemGuideExtend.zxzwwbwz}" ${curIsEditable?'':'readonly'} />
                    </td>

                    <th>微信号</th>
                    <td>
                        <input name = "zxwxh" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.zxwxh?(curIsEditable?'':'无'):aeaItemGuideExtend.zxwxh}" ${curIsEditable?'':'readonly'} />
                    </td>
                </tr>


                <tr style="display: table-row;">
                    <th>电子邮箱</th>
                    <td>
                        <input name = "zxdzyx" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.zxdzyx?(curIsEditable?'':'无'):aeaItemGuideExtend.zxdzyx}" ${curIsEditable?'':'readonly'} />
                    </td>

                    <th>信函地址</th>
                    <td>
                        <input name = "zxxhyjdz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.zxxhyjdz?(curIsEditable?'':'无'):aeaItemGuideExtend.zxxhyjdz}" ${curIsEditable?'':'readonly'} />
                    </td>
                </tr>

            </tbody>
        </table>

        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;margin-top:30px;" data-toggle="table-collapse" data-row="3">

            <tbody>
            <tr style="display: table-row;">
                <th colspan="4" >权利与义务</th>
            </tr>

            <tr style="display: table-row;">
                <th>申请人依法享有以下权利</th>
                <td>
                    <%--<input name = "sqrql" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.sqrql?(curIsEditable?'':'无'):aeaItemGuide.sqrql}" ${curIsEditable?'':'readonly'} />--%>
                    <textarea name = "sqrql" class="form-control m-input ${curIsEditable?'':'border-none'}" rows="8" ${curIsEditable?'':'readonly'}>${empty aeaItemGuideExtend.sqrql?(curIsEditable?'':'无'):aeaItemGuideExtend.sqrql}</textarea>
                </td>
                <th>申请人依法履行以下义务</th>
                <td>
                    <%--<input name = "sqryw" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuide.sqryw?(curIsEditable?'':'无'):aeaItemGuide.sqryw}" ${curIsEditable?'':'readonly'} />--%>
                    <textarea name = "sqryw" class="form-control m-input ${curIsEditable?'':'border-none'}" rows="8" ${curIsEditable?'':'readonly'}>${empty aeaItemGuideExtend.sqryw?(curIsEditable?'':'无'):aeaItemGuideExtend.sqryw}</textarea>
                </td>
            </tr>

            </tbody>
        </table>

        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;margin-top:30px;" data-toggle="table-collapse" data-row="3">
            <tbody>
                <tr style="display: table-row;">
                    <th colspan="4" >行政复议</th>
                </tr>

                <tr style="display: table-row;">
                    <th>行政复议部门</th>
                    <td>
                        <input name = "xzfybm" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzfybm?(curIsEditable?'':'无'):aeaItemGuideExtend.xzfybm}" ${curIsEditable?'':'readonly'} />
                    </td>
                    <th>行政复议地址</th>
                    <td>
                        <input name = "xzfydz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzfydz?(curIsEditable?'':'无'):aeaItemGuideExtend.xzfydz}" ${curIsEditable?'':'readonly'} />
                    </td>
                </tr>

                <tr style="display: table-row;">
                    <th>行政复议电话</th>
                    <td>
                        <input name = "xzfydh" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzfydh?(curIsEditable?'':'无'):aeaItemGuideExtend.xzfydh}" ${curIsEditable?'':'readonly'} />
                    </td>
                    <th>行政复议网址</th>
                    <td>
                        <input name = "xzfywz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzfywz?(curIsEditable?'':'无'):aeaItemGuideExtend.xzfywz}" ${curIsEditable?'':'readonly'} />
                    </td>
                </tr>
            </tbody>
        </table>

        <table class="matters-table matters-table-bordered matters-table-info"
               style="width:100%;margin-top:30px;" data-toggle="table-collapse" data-row="3">
            <tbody>
            <tr style="display: table-row;">
                <th colspan="4" >行政诉讼</th>
            </tr>

            <tr style="display: table-row;">
                <th>行政诉讼部门</th>
                <td>
                    <input name = "xzssbm" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzssbm?(curIsEditable?'':'无'):aeaItemGuideExtend.xzssbm}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>行政诉讼地址</th>
                <td>
                    <input name = "xzssdz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzssdz?(curIsEditable?'':'无'):aeaItemGuideExtend.xzssdz}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>

            <tr style="display: table-row;">
                <th>行政诉讼电话</th>
                <td>
                    <input name = "xzssdh" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzssdh?(curIsEditable?'':'无'):aeaItemGuideExtend.xzssdh}" ${curIsEditable?'':'readonly'} />
                </td>
                <th>行政诉讼网址</th>
                <td>
                    <input name = "xzsswz" class="form-control m-input ${curIsEditable?'':'border-none'}"  value="${empty aeaItemGuideExtend.xzsswz?(curIsEditable?'':'无'):aeaItemGuideExtend.xzsswz}" ${curIsEditable?'':'readonly'} />
                </td>
            </tr>

            </tbody>
        </table>

        <c:if test="${curIsEditable==true}">
            <div id="saveItemGuideExtendBtn" style="text-align: center;margin-top: 10px;">
                <button type="submit" class="btn btn-info" style="${curIsEditable?'':'display:none'}">保存</button>&nbsp;&nbsp;
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
<%@include file="show_guide_extend_att.jsp"%>
<script src="${pageContext.request.contextPath}/ui-static/item/guide/extend_info_index.js?<%=isDebugMode%>" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {

        $('#show_guide_extend_att_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        // 附件
        clearGuideExtendFile();
        if (${aeaItemGuideExtend.zzzResultGuidNum>0}) {
            $('#resultSampleDiv').show();
            $('#resultSampleButton').html(${aeaItemGuideExtend.zzzResultGuidNum} + "个附件");
        }else{
            $('#resultSampleDiv').hide();
        }

        // 选择
        var serviceType = '${aeaItemGuideExtend.serviceType}';
        loadCheckbox(serviceType,'serviceType');
    });

    function preventFormSubmit(){

        $("#item_extend_info_form").submit(function (e) {

            return false;
        });
    }
</script>
</body>
</html>