<%@ page language="java" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>服务咨询管理界面</title>
    <!-- 所有JSP必须引入的公共JSP子页面 -->
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_service_consulting.js"></script>

</head>
<body>
<div class="m-portlet m-portlet--mobile">
    <div class="m-portlet__body" id="pageBody">

        <form class="m-form m-form--fit m-form--label-align-right" style="margin-bottom: 10px">
            <div class="row">
                <div class="col-xl-7">
                    <a href="javascript:void(0)" onclick="createItemServiceConsulting();" class="btn btn-primary m-btn m-btn--icon">
                        <span><span>新建服务/监督</span></span>
                    </a>&nbsp;
                </div>
                <div class="col-xl-5">
                    <div class="input-group">
                        <div class="input-group-prepend"></div>
                        <input type="hidden" id="itemBasicId" name="itemBasicId" value="${itemBasicId}">
                        <input type="text" id="keyword" class="form-control" aria-label="Text input with dropdown button" placeholder="请输入服务咨询岗位查询">
                        <button type="button" class="btn btn-primary" style="border-top-left-radius: 0; border-bottom-left-radius: 0;" onclick="searchCondition();">查询</button>&nbsp;
                        <button type="button" class="btn btn-default" onclick="clearSearch();">清空</button>
                    </div>
                </div>
            </div>
        </form>

        <!--begin: Datatable -->
        <div class="m_datatable" id="ajax_data"></div>
        <!--end: Datatable -->
    </div>
</div>

<!-- 新增/编辑服务咨询信息 -->
<div id="item_service_consulting_add" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="item_service_consulting_add_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="item_service_consulting_add_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="item_service_consulting_form" method="post">

                    <input type="hidden" name="serviceConsultingId" id="serviceConsultingId" value=""/>
                    <input type="hidden" name="itemBasicId" value=""/>

                    <center>
                            <table border="1"  width="98%" style="text-align: center;border: rgb(212,238,249)">
                                <tr style="background-color: rgb(191,235,255)">
                                    <td colspan="4">许可咨询</td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="background-color: rgb(235,255,255)"><font color="red">*</font>类型</td>
                                    <td colspan="2">
                                        <select type="text" class="form-control" name="serviceType" value="">
                                            <option value="zx">咨询</option>
                                            <option value="jd">监督</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="background-color: rgb(235,255,255)"><font color="red">*</font>许可咨询岗位职责和权限</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="gwzzhqx" value=""/></td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="background-color: rgb(235,255,255)"><font color="red">*</font>咨询工作程序</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="zxgzcx" value=""/></td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="background-color: rgb(235,255,255)"><font color="red">*</font>常见问题</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="cjwt" value=""/></td>
                                </tr>
                                <tr>
                                    <td rowspan="8" style="background-color: rgb(235,255,255)">咨询途径</td>
                                    <td style="background-color: rgb(235,255,255)">窗口咨询</td>
                                    <td style="background-color: rgb(235,255,255)"><font color="red">*</font>地址</td>
                                    <td><input type="text" class="form-control m-input" name="dz" value=""/></td>
                                </tr>
                                <tr>
                                    <td style="background-color: rgb(235,255,255)">电话咨询</td>
                                    <td style="background-color: rgb(235,255,255)"><font color="red">*</font>电话号码</td>
                                    <td><input type="text" class="form-control m-input" name="dh" value=""/></td>
                                </tr>
                                <tr>
                                    <td rowspan="3" style="background-color: rgb(235,255,255)">网上咨询</td>
                                    <td style="background-color: rgb(235,255,255)">实施机关咨询网址</td>
                                    <td><input type="text" class="form-control m-input" name="zxdz" value=""/></td>
                                </tr>
                                <tr>
                                    <td style="background-color: rgb(235,255,255)">政务微博及网址</td>
                                    <td><input type="text" class="form-control m-input" name="wbwz" value=""/></td>
                                </tr>
                                <tr>
                                    <td style="background-color: rgb(235,255,255)">微信号</td>
                                    <td><input type="text" class="form-control m-input" name="wxh" value=""/></td>
                                </tr>
                                <tr>
                                    <td style="background-color: rgb(235,255,255)">电子邮件咨询</td>
                                    <td style="background-color: rgb(235,255,255)">电子邮箱</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="email" value=""/></td>
                                </tr>
                                <tr>
                                    <td rowspan="2" style="background-color: rgb(235,255,255)">信函咨询</td>
                                    <td style="background-color: rgb(235,255,255)">邮寄地址</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="yjdz" value=""/></td>
                                </tr>
                                <tr>
                                    <td style="background-color: rgb(235,255,255)">邮政编码</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="yzbm" value=""/></td>
                                </tr>
                                <tr>
                                    <td rowspan="2" style="background-color: rgb(235,255,255)">咨询回复</td>
                                    <td style="background-color: rgb(235,255,255)">回复时限及形式</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="hfsxhxs" value=""/></td>
                                </tr>
                                <tr>
                                    <td style="background-color: rgb(235,255,255)">回复部门</td>
                                    <td colspan="2"><input type="text" class="form-control m-input" name="hfbm" value=""/></td>
                                </tr>
                            </table>
                            <br/>
                            <div class="form-group m-form__group row" style="text-align: center;">
                                <div class="col-lg-12">
                                    <button type="submit" class="btn btn-info">保存</button>
                                    <button type="button" class="btn btn-secondary" onclick="$('#item_service_consulting_add').modal('hide');">关闭</button>
                                </div>
                            </div>
                    <center>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>