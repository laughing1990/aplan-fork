<%@ page contentType="text/html;charset=UTF-8" %>

<!--BEGIN: css 样式表-->
<link href="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/css/base/vendors.bundle.css" rel="stylesheet" type="text/css" />
<!--主要样式-->
<link href="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/css/base/style.bundle.css" rel="stylesheet" type="text/css">
<!--覆盖叠加更换主题色样式-->
<link href="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/css/base/theme.css" rel="stylesheet" type="text/css">
<!--覆盖叠加调整布局与边距等样式-->
<link href="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/css/base/layout.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/css/base/base.css" rel="stylesheet" type="text/css">
<!--受理页面样式-->
<link href="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/css/dg-option.css" rel="stylesheet" type="text/css">
<!--END: css 样式表-->
<!--begin弹窗-->
<div class="modal fade" id="mindTypeModal" tabindex="-1" role="dialog"
     aria-labelledby="mindTypeModalTitle" aria-hidden="true" >
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="mindTypeModalTitle">插入下级选择</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: -5px;">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="option-box cleafix" id="option-box">
                    <div class="option-item border fl">
                        <span><i class="flaticon-list-1"></i></span>
                        <p>情形</p>
                    </div>
                    <div class="option-item fr">
                        <span><i class="flaticon-folder-1"></i></span>
                        <p>材料</p>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 15px;">
                <button type="button" class="btn btn-info"  data-dismiss="modal" aria-label="Close" id="mindTypeModalConfirm">确定</button>
            </div>
        </div>
    </div>
</div>
