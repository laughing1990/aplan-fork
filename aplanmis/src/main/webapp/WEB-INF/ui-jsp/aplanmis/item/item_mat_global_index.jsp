<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
   <title>全局材料库</title>
   <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
   <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
   <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
   <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>

   <link href="${pageContext.request.contextPath}/ui-static/aplanmis/item/css/context-menu.css" type="text/css" rel="stylesheet"/>
   <link href="${pageContext.request.contextPath}/ui-static/agcloud/bsc/yunpan/css/orgTheme.css?<%=isDebugMode%>" type="text/css" rel="stylesheet"/>
   <style type="text/css">
      .row{
         margin-left: 0px;
         margin-left: 0px;
      }
   </style>
</head>

<body>
<div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
   <div class="row" style="width: 100%;height: 100%;margin: 0px;">
      <div class="col-xl-3" style="padding: 0px 2px 0px 8px;">
         <div id="westPanel" class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
            <!-- 标题信息 -->
            <div class="m-portlet__head">
               <div class="m-portlet__head-caption">
                  <div class="m-portlet__head-title">
                     <span class="m-portlet__head-icon m--hide"><i class="la la-gear"></i></span>
                     <h3 class="m-portlet__head-text">材料类别</h3>
                  </div>
               </div>
            </div>

            <div class="m-portlet__body" style="padding: 10px 0px;">
               <div class="row" style="margin: 0px;">
                  <div class="col-xl-7">
                     <input id="itemMatTypeKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..." style="background-color: #f0f0f075;border-color: #c4c5d6;">
                  </div>
                  <div class="col-xl-5">
                     <button type="button" class="btn btn-info" onclick="searchItemType();">查询</button>
                     <button type="button" class="btn btn-danger" onclick="clearSearchItemType('#itemMatTypeKeyWord');">清空</button>
                     <%--<button type="button" class="btn btn-secondary" onclick="expandAll();">展开</button>
                     <button type="button" class="btn btn-secondary" onclick="collapseAll();">折叠</button>--%>
                  </div>
               </div>
               <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
               <!-- 树列表区域 -->
               <div id="matTypeTreeScrollable" class="m-scrollable" data-scrollable="true" data-max-height="645">
                  <div id="matTypeInfoTree" class="ztree" style="overflow-y:auto;overflow-x:auto;"></div>
               </div>
            </div>
         </div>
      </div>

      <div class="col-xl-9" style="padding: 0px 8px 0px 2px;" id="contentContainer">
         <div id="container_mat_type_def_list" style="display: ;width: 100%;height: 100%;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
               <div class="m-portlet__head">
                  <div class="m-portlet__head-caption">
                     <div class="m-portlet__head-title">
			    <span class="m-portlet__head-icon m--hide">
				   <i class="la la-gear"></i>
			    </span>
                        <h3 class="m-portlet__head-text">
                           全局材料列表
                        </h3>
                     </div>
                  </div>
               </div>
               <div class="m-portlet__body" style="padding: 10px 5px;">
                  <div class="m-form m-form--label-align-right m--margin-bottom-5">
                     <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                           <button type="button" class="btn  btn-success" onclick="addGlobalMat();">新增</button>
                           <button type="button" class="btn  btn-danger" onclick="batchDelGlobalMat();">删除</button>
                           <button type="button" class="btn  btn-accent" onclick="GlobalMatDatatable.reload();">刷新</button>
                        </div>
                        <div class="col-md-6" style="padding: 0px;">
                           <div class="row" style="margin: 0px;">
                              <div class="col-2"></div>
                              <div class="col-6">
                                 <form id="search_item_cond_form" method="post">
                                    <div class="m-input-icon m-input-icon--left">
                                       <input type="text" id="input_mat_global_search" class="form-control m-input" placeholder="请输入关键字..." name="keyword" value=""/>
                                       <span class="m-input-icon__icon m-input-icon__icon--left">
										        <span><i class="la la-search"></i></span>
										    </span>
                                    </div>
                                 </form>
                              </div>
                              <div class="col-4">
                                 <button type="button" class="btn  btn-info" onclick="GlobalMatDatatable.search();">查询</button>
                                 <button type="button" class="btn  btn-danger" onclick="GlobalMatDatatable.searchClear();">清空</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                  <div class="m-scrollable" data-scrollable="true" data-max-height="600">
                     <div id="tb_mat_global" class="m_datatable"></div>
                  </div>
               </div>
            </div>
         </div>
         <div id="container_mat_type_def_edit" style="display: none;width: 100%;height: 100%;">
            <%@include file="include/mat_type_def_edit.jsp"%>
         </div>
      </div>
   </div>
</div>

<div id="rMenu" class="contextMenuDiv">
   <a href="#" id="newMatType" class="list-group-item" onclick="addChildItemType();">
      <i class="fa flaticon-plus"></i><span>新增子类</span>
   </a>
   <a href="#" class="list-group-item" onclick="editItemType();">
      <i class="fa flaticon-edit-1"></i>编辑类别
   </a>
   <a href="#" class="list-group-item" onclick="deleteItemType();">
      <i class="fa fa-times"></i>删除类别
   </a>
</div>

<!-- 新增模式框 -->
<div id="add_item_mat_type_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="add_item_mat_type_modal_title" aria-hidden="true">
   <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
      <div class="modal-content">
         <div class="modal-header" style="padding: 15px;height: 45px;">
            <h5 class="modal-title" id="add_item_mat_type_modal_title"></h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
            </button>
         </div>
         <div class="modal-body" style="padding: 10px;">
            <form id="add_item_mat_type_form" method="post">

               <input id="matTypeId" type="hidden" name="matTypeId" value=""/>
               <input id="parentTypeId" type="hidden" name="parentTypeId" value=""/>

               <div class="form-group m-form__group row" >
                  <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>分类编号:</label>
                  <div class="col-lg-4">
                     <input type="text" name="typeCode" class="form-control m-input" placeholder="请输入分类编号"/>
                  </div>

                  <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>分类名称:</label>
                  <div class="col-lg-4">
                     <input type="text" name="typeName" class="form-control m-input" placeholder="请输入分类名称"/>
                  </div>
               </div>

               <div class="form-group m-form__group row" >
                  <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                  <div class="col-lg-10">
                     <textarea class="form-control" name="typeMemo" rows="3"></textarea>
                  </div>
               </div>

               <div class="form-group m-form__group row" style="text-align: right;">
                  <div class="col-lg-12">
                     <button type="submit" class="btn btn-info">保存</button>
                     <button type="button" class="btn btn-secondary" onclick="showContentContainer('container_mat_type_def_list');">关闭</button>
                  </div>
               </div>
            </form>
         </div>
      </div>
   </div>
</div>

<%--<jsp:include page="include/item_mat_global_dialog_allinfo.jsp" />--%>

<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_info_tree_edit.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_global.js" type="text/javascript"></script>
</body>
</html>