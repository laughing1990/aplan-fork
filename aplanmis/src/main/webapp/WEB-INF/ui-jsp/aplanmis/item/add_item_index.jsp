<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
   .row{
       margin-left: 0px;
       margin-right: 0px;
   }

   #addItemScrollable::-webkit-scrollbar{
       width:4px;
       height:4px;
   }
   #addItemScrollable::-webkit-scrollbar-track{
       background: #fff;
       border-radius: 2px;
   }
   #addItemScrollable::-webkit-scrollbar-thumb{
       background: #e2e5ec;
       border-radius:2px;
   }
   #addItemScrollable::-webkit-scrollbar-thumb:hover{
       background: #aaa;
   }
   #addItemScrollable::-webkit-scrollbar-corner{
       background: #fff;
   }
   .form-group label{
        display: block;
        float: left;
        position: relative;
    }
    .form-group input[type="file"]{
        position: absolute;
        width: 10%;
        opacity: 0;
    }
    .form-group .custorm-style{
        display: block;
        width: 100%;
        height: 38px;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
    }
    .form-group .custorm-style .left-button{
        width: 71px;
        font-size: 13px !important;
        height: 22px;
        line-height: 13px;
        float: left;
        border:1px solid #b1b1b1;
        background: linear-gradient(to bottom, #fff, #ccc);
        color: #444;
        margin-top: 0.9%;
        margin-left: 1%;
    }
    .form-group .custorm-style .right-text{
        width: 88%;
        height: 99%;
        line-height: 2.7em;
        display: block;
        overflow: hidden;
    }
</style>

<!-- 新增事项信息 -->
<div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
    <div class="m-portlet__head">
        <div class="m-portlet__head-caption">
            <div class="m-portlet__head-title">
                <h3 id="add_item_info_header" class="m-portlet__head-text">
                   新增事项
                </h3>
            </div>
        </div>
    </div>
    <form id="add_item_info_form" method="post" enctype="multipart/form-data">
        <div class="m-portlet__body" style="padding: 10px 5px;">
            <div id="addItemScrollable" style="overflow-x: hidden;overflow-y: auto;">

                <input type="hidden" name="itemId" value=""/>
                <input type="hidden" name="originalParentItemId" value=""/>
                <input type="hidden" name="parentItemId" value=""/>
                <input type="hidden" name="originalOrgId" value=""/>
                <input type="hidden" name="orgId" value=""/>
                <input type="hidden" name="parentNodeType" value=""/> <!-- 父级节点类型 -->
                <input type="hidden" name="isTbEditItemNode" value=""/>  <!-- 设置表格编辑事项节点 -->

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>事项名称:</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="itemName" value=""/>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>事项编号:</label>
                    <div class="col-lg-10">
                        <input type="text" class="form-control m-input" name="itemCode" value=""/>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">标准事项编码:</label>
                    <div class="col-lg-4">
                        <input type="text" class="form-control m-input" name="standardItemCode" value=""/>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">事项类型:</label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="itemType" value="">
                            <option value="">请选择</option>
                            <c:forEach items="${itemTypes}" var="itemTypeVo">
                                <option value="${itemTypeVo.itemCode}">${itemTypeVo.itemName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group m-form__group row" >

                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>所属组织:</label>
                    <div class="col-lg-4 input-group">
                        <input type="text" class="form-control m-input" name="orgName" placeholder="请点击选择" aria-describedby="select_org_Id" readonly>
                        <div class="input-group-append">
                            <span id="select_org_Id" class="input-group-text" onclick="javascript:selectOpusOmOrgZtree();">
                                <i class="la la-group"></i>
                            </span>
                        </div>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">办件类型:</label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="itemProperty" value="">
                            <option value="">请选择</option>
                            <c:forEach items="${itemPropertys}" var="itemProperty">
                                <option value="${itemProperty.itemCode}">${itemProperty.itemName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">办理结果送达方式:</label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="sendResultMode" value="">
                            <option value="">请选择</option>
                            <c:forEach items="${sendResultModes}" var="sendResultMode">
                                <option value="${sendResultMode.itemCode}">${sendResultMode.itemName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>
                    <div class="col-lg-4">
                        <input type="number" class="form-control m-input" name="acceptMode" value="1"/>
                    </div>
                </div>

                <div class="form-group m-form__group row" >

                    <label class="col-lg-2 col-form-label" style="text-align: right;">承诺时限计量:</label>
                    <div class="col-lg-4">
                        <input type="number" class="form-control m-input" name="dueNum" value="1"/>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">承诺时限单位:</label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="dueUnit" value="">
                            <option value="">请选择</option>
                            <c:forEach items="${dueUnits}" var="dueUnit">
                                <option value="${dueUnit.itemCode}">${dueUnit.itemName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">图标CSS样式:</label>
                    <div class="col-lg-4">
                        <input type="text" class="form-control m-input" name="handleArticle" value=""  />
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">背景CSS样式:</label>
                    <div class="col-lg-4">
                        <input type="text" class="form-control m-input" name="handleFlow" value=""  />
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">文书模版:</label>
                    <div class="col-lg-10">
                        <input type="file" name="docTemplateFile" class="form-control m-input" placeholder="" accept="image/*" >
                        <span class="custorm-style">
                             <button class="left-button">选择文件</button>
                             <span class="right-text" id="rightText1">未选择任何文件</span>
                         </span>
                        <span id="docTemplateAtt">已经上传的：<a id="docTemplateAtta" href="#" target="_blank">文书模版附件</a></span>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">申请表范本:</label>
                    <div class="col-lg-10">
                        <input type="file" name="applyTableDemoFile" class="form-control m-input" placeholder="" accept="image/*" >
                        <span class="custorm-style">
                             <button class="left-button">选择文件</button>
                             <span class="right-text" id="rightText2">未选择任何文件</span>
                         </span>
                        <span id="applyTableDemoAtt">已经上传的：<a id="applyTableDemoAtta" href="#" target="_blank">申请表范本附件</a></span>
                    </div>
                </div>

                <div class="form-group m-form__group row">
                    <label class="col-lg-2 col-form-label" style="text-align: right;">申请表模版:</label>
                    <div class="col-lg-10">
                        <input type="file" name="applyTableTemplateFile" class="form-control m-input" placeholder="" accept="image/*" >
                        <span class="custorm-style">
                             <button class="left-button">选择文件</button>
                             <span class="right-text" id="rightText3">未选择任何文件</span>
                         </span>
                        <span id="applyTableTemplateAtt">已经上传的：<a id="applyTableTemplateAtta" href="#" target="_blank">申请表模版附件</a></span>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">
                        <font color="red">*</font>是否分情形:
                    </label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="isNeedState" value="">
                            <option value="">请选择</option>
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </div>

                    <label class="col-lg-2 col-form-label" style="text-align: right;">
                        <font color="red">*</font>是否本系统处理事项:
                    </label>
                    <div class="col-lg-4">
                        <select type="text" class="form-control" name="outerSystemHandle" value="">
                            <option value="">请选择</option>
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">由第三方系统处理说明:</label>
                    <div class="col-lg-10">
                        <textarea class="form-control" name="outerSystemDesc" rows="5"></textarea>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">跳转至第三方系统的URL地址:</label>
                    <div class="col-lg-10">
                        <textarea class="form-control" name="outerSystemUrl" rows="3"></textarea>
                    </div>
                </div>

                <div class="form-group m-form__group row" >
                    <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                    <div class="col-lg-10">
                        <textarea class="form-control" name="itemMemo" rows="5"></textarea>
                    </div>
                </div>
            </div>
        </div>
        <div class="m-portlet__foot" style="padding: 6px 20px;text-align: right;">
            <button type="submit" class="btn btn-info">保存</button>
            <%--<button id="restAddItemInfoBtn" type="button" class="btn btn-secondary">重置</button>--%>
        </div>
    </form>
</div>
<script type="text/javascript">
  $(document).ready(function(){
    var fileBtn = $("input[type=file]");
    fileBtn.on("change", function(){
        var index = $(this).val().lastIndexOf("\\");
        var sFileName = $(this).val().substr((index+1));
        $(this).siblings('.custorm-style').find(".right-text").html(sFileName);
    });
  })
    
</script>