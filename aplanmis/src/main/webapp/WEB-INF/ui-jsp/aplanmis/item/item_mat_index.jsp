<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
   <title>事项材料</title>
   <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp" %>
   <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp" %>
   <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp" %>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-agtree3.jsp"%>

    <!--类别选取 -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_type_tree.js" type="text/javascript"></script>
   <script src="${pageContext.request.contextPath}/ui-static/aplanmis/item/ui-js/item_mat_index.js" type="text/javascript"></script>
   <script type="text/javascript">
       var matTypeId = '${matTypeId}';
   </script>
</head>
<body>
<div class="m-portlet m-portlet--mobile">
   <div class="m-portlet__body">
      <ul class="nav nav-tabs" role="tablist">
         <li class="nav-item">
            <a class="nav-link active" data-toggle="tab" href="#m_tabs_1">
               输入材料
            </a>
         </li>
         <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#m_tabs_2">
               输出材料
            </a>
         </li>
      </ul>
      <div class="tab-content">
         <div class="tab-pane active" id="m_tabs_1" role="tabpanel">
            <div class="m-form m-form--label-align-right m--margin-top-20 m--margin-bottom-20">
               <div class="row align-items-center">
                  <div class="col-xl-8 order-2 order-xl-1">
                     <div class="row">
                         <div class="col-md-6">
                             <div style="margin-top: 5px">
                                 <button type="button" class="btn  btn-success"  onclick="addMat('1');">新增事项材料</button>
                                 <button type="button" class="btn  btn-success"  onclick="selectGlogbalMat();">选取全局材料</button>
                             </div>
                         </div>
                        <div class="col-md-3">
                           <div class="m-form__group m-form__group--inline">
                              <div class="m-form__label">
                                 <label class="m-label m-label--single">
                                    全局:
                                 </label>
                              </div>
                              <div class="m-form__control">
                                  <select class="form-control m-bootstrap-select" id="slt_is_global_share">
                                      <option value="">全部</option>
                                      <option value="1">全局</option>
                                      <option value="0">非全局</option>
                                  </select>
                              </div>
                           </div>
                           <div class="d-md-none m--margin-bottom-10"></div>
                        </div>
                         <div class="col-md-3">
                             <div class="m-form__group m-form__group--inline">
                                 <div class="m-form__label">
                                     <label class="m-label m-label--single">
                                         批文:
                                     </label>
                                 </div>
                                 <div class="m-form__control">
                                     <select class="form-control m-bootstrap-select" id="slt_receive_mode">
                                         <option value="">全部</option>
                                         <option value="1">批文</option>
                                         <option value="0">非批文</option>
                                     </select>
                                 </div>
                             </div>
                             <div class="d-md-none m--margin-bottom-10"></div>
                         </div>
                     </div>
                  </div>
                  <div class="col-xl-4 order-1 order-xl-2 m--align-right">
                     <div class="m-input-icon m-input-icon--left">
                        <input type="text" name="keyword" class="form-control m-input" placeholder="查询..." id="generalSearch">
                        <span class="m-input-icon__icon m-input-icon__icon--left">
                            <span><i class="la la-search"></i></span>
						</span>
                     </div>
                  </div>
               </div>
            </div>
            <div class="m_datatable" id="tb_mat"></div>
         </div>
         <div class="tab-pane" id="m_tabs_2" role="tabpanel">
            It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset
            sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
         </div>
      </div>
   </div>
</div>

<!--查询,新增,编辑材料 -->
<jsp:include page="include/item_mat_dialog.jsp"/>

<!--查看全局材料 -->
<jsp:include page="include/item_mat_global_dialog.jsp"/>

<!--材料类别选取窗口 -->
<jsp:include page="add_item_mat.jsp"/>
</body>
</html>