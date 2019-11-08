<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>实施机构</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>

    <srcipt src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap3/js/bootstrap.js" type="text/javascript"></srcipt>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/bootstrap3/css/bootstrap.css">

    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/exeorg_index.js" type="text/javascript"></script>
    <script type="text/javascript">
        var itemBasicId = '${itemBasicId}';
    </script>
    <style type="text/css">
        form label.error {
            width: 200px;
            margin-left: 8px;
            color: Red;
        }
    </style>
</head>

<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 15px 10px 5px 10px;margin: 0px;">
        <div class="col-xl-12">
            <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                                <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">实施机关</h3>
                        </div>
                    </div>
                </div>

                <!--数据模块开始-->
                <div class="panel-body">
                    <div id="exeorgScrollable" style="overflow-x: hidden;overflow-y: auto;">
                        <form class="form-horizontal" id="exeorgDataForm" role="form">

                            <input type="hidden" name="exeorgId" value=""/>
                            <input type="hidden" name="itemBasicId" value="${itemBasicId}"/>

                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="ssjgmc"><span style="color:red">*</span>实施机关：</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" name="ssjgmc" id="ssjgmc">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="ssjglb"><span style="color:red">*</span>类别：</label>
                                <div class="col-sm-2">
                                    <select class="form-control" id="ssjglb" name="ssjglb">
                                        <option value="">请选择</option>
                                        <option value="1">法定机关</option>
                                        <option value="2">被授权组织</option>
                                        <option value="3">受委托机关</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><span style="color:red">*</span>实施层级：</label>
                                <div class="col-sm-10">
                                    <c:forEach items="${itemXscjs}" var="itemXscj">
                                        <label class="checkbox-inline"><input type="checkbox" name="sscj" onclick="sscjChange(${itemXscj.itemCode});" value="${itemXscj.itemCode}">${itemXscj.itemName}</label>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="form-group" id="section_gj" style="display: none">
                                <label class="col-sm-2 control-label" for="qzhfgj"><span style="color:red">*</span>权责划分(国家)：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" name="qzhfgj" id="qzhfgj" rows="6"></textarea>
                                </div>
                            </div>
                            <div class="form-group" id="section_sj" style="display: none">
                                <label class="col-sm-2 control-label" for="qzhfsj"><span style="color:red">*</span>权责划分(省)：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" name="qzhfsj" id="qzhfsj" rows="6"></textarea>
                                </div>
                            </div>
                            <div class="form-group" id="section_dsj" style="display: none">
                                <label class="col-sm-2 control-label" for="qzhfdsj"><span style="color:red">*</span>权责划分(市)：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" name="qzhfdsj" id="qzhfdsj" rows="6"></textarea>
                                </div>
                            </div>
                            <div class="form-group" id="section_qx" style="display: none">
                                <label class="col-sm-2 control-label" for="qzhfqx"><span style="color:red">*</span>权责划分(县)：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" name="qzhfqx" id="qzhfqx" rows="6"></textarea>
                                </div>
                            </div>
                            <div class="form-group" id="section_zj" style="display: none">
                                <label class="col-sm-2 control-label" for="qzhfzj"><span style="color:red">*</span>权责划分(镇)：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" name="qzhfzj" id="qzhfzj" rows="6"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="xsnr"><span style="color:red">*</span>行使内容：</label>
                                <div class="col-sm-6">
                                    <textarea class="form-control" name="xsnr" id="xsnr" rows="6"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" ><span style="color:red">*</span>是否单部门单处/科室办理：</label>
                                <div class="col-sm-1">
                                    <label class="radio-inline"><input type="radio"  name="sfdbmdksbl" onclick="radioChange('section_dksmc',1);" value="1">是</label>
                                    <label class="radio-inline"><input type="radio"  name="sfdbmdksbl" onclick="radioChange('section_dksmc',0);" value="0" checked>否</label>
                                </div>
                                <div id="section_dksmc" style="display: none">
                                    <label class="col-sm-1 control-label" for="dksmc"><span style="color:red">*</span>单科室名称：</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" name="dksmc" id="dksmc">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" ><span style="color:red">*</span>是否单部门跨处/科室办理：</label>
                                <div class="col-sm-1">
                                    <label class="radio-inline"><input type="radio"  name="sfdbmkksbl" onclick="radioChange('section_kksmc',1);" value="1">是</label>
                                    <label class="radio-inline"><input type="radio"  name="sfdbmkksbl" onclick="radioChange('section_kksmc',0);" value="0" checked>否</label>
                                </div>
                                <div id="section_kksmc" style="display: none">
                                    <label class="col-sm-1 control-label" for="kksmc"><span style="color:red">*</span>跨科室名称：</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" name="kksmc" id="kksmc">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" ><span style="color:red">*</span>是否单部门跨层级办理：</label>
                                <div class="col-sm-1">
                                    <label class="radio-inline"><input type="radio"  name="sfdbmkcjbl" onclick="radioChange('section_skcj',1);" value="1">是</label>
                                    <label class="radio-inline"><input type="radio"  name="sfdbmkcjbl" onclick="radioChange('section_skcj',0);" value="0" checked>否</label>
                                </div>
                                <div id="section_skcj" style="display: none">
                                    <label class="col-sm-1 control-label" for="skcj"><span style="color:red">*</span>所跨层级：</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" name="skcj" id="skcj">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" ><span style="color:red">*</span>是否单层级跨部门办理：</label>
                                <div class="col-sm-1">
                                    <label class="radio-inline"><input type="radio"  name="sfdcjkbmbl" onclick="radioChange('section_skbmmc',1);" value="1">是</label>
                                    <label class="radio-inline"><input type="radio"  name="sfdcjkbmbl" onclick="radioChange('section_skbmmc',0);" value="0" checked>否</label>
                                </div>
                                <div id="section_skbmmc" style="display: none">
                                    <label class="col-sm-1 control-label" for="skbmmc"><span style="color:red">*</span>所跨部门：</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" name="skbmmc" id="skbmmc">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" ><span style="color:red">*</span>是否跨部门跨层级办理：</label>
                                <div class="col-sm-1">
                                    <label class="radio-inline"><input type="radio"  name="sfkbmkcjbl" onclick="radioChange('section_skcjhbm',1);" value="1">是</label>
                                    <label class="radio-inline"><input type="radio"  name="sfkbmkcjbl" onclick="radioChange('section_skcjhbm',0);" value="0" checked>否</label>
                                </div>
                                <div id="section_skcjhbm" style="display: none">
                                    <label class="col-sm-1 control-label" for="skcjhbm"><span style="color:red">*</span>所跨层级和部门：</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" name="skcjhbm" id="skcjhbm">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" ><span style="color:red">*</span>是否有联办机构：</label>
                                <div class="col-sm-1">
                                    <label class="radio-inline"><input type="radio"  name="sfylbjg" onclick="radioChange('section_lbjg',1);" value="1">是</label>
                                    <label class="radio-inline"><input type="radio"  name="sfylbjg" onclick="radioChange('section_lbjg',0);" value="0" checked>否</label>
                                </div>
                                <div id="section_lbjg"  style="display: none">
                                    <label class="col-sm-1 control-label" for="lbjg"><span style="color:red">*</span>联办机构：</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" name="lbjg" id="lbjg">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group m-form__group row" >
                                <div class="col-lg-12" style="text-align: center;">
                                    <button type="submit" class="btn btn-primary">暂存</button>
                                    <button type="submit" class="btn btn-primary">保存</button>
                                    <button type="submit" class="btn btn-primary">提交审查</button>
                                    <button type="button" class="btn btn-primary">关闭</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <!--数据模块结束-->
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/exeorg_index.js" type="text/javascript"></script>
</body>
</html>