<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 新增信息 -->
<div class="modal fade" id="dialog_mat" tabindex="-1" role="dialog" aria-labelledby="dialog_mat_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" >
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px">
                <h5 class="modal-title" id="dialog_mat_title">编辑事项材料</h5>
                <h5 class="modal-title hid" id="dialog_mat_type_title">选择材料类别</h5>
                <h5 class="modal-title" id="dialog_mat_global_title">选取材料</h5>
                <%--<button id="dialogMatGlobalCloseBtn" type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
                    <%--<span aria-hidden="true">&times;</span>--%>
                <%--</button>--%>
            </div>
            <div class="wizard_cust">
                <div id="wizard_mat" class="wizard-step">
                    <jsp:include page="step/stage_mat_step.jsp"/>
                </div>
                <div id="wizard_mat_global" class="wizard-step">
                    <jsp:include page="step/item_mat_global_setp.jsp"/>
                </div>
                <div id="wizard_mat_type" class="wizard-step">
                    <jsp:include page="step/item_mat_type_step.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>