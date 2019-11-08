<%@ page contentType="text/html;charset=UTF-8" %>

<div id="show_mat_att_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="show_mat_att_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="show_mat_att_modal_title">查看附件</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="show_mat_att_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="show_mat_att_tb"
                                data-toggle="table"
                                <%--data-height="450"--%>
                                data-method="post"
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-click-to-select=true
                                data-pagination-detail-h-align="left"
                                data-pagination-show-page-go="true"
                                data-page-size="10"
                                data-page-list="[10,20,30,50,100]",
                                data-pagination=true
                                data-side-pagination="server"
                                data-pagination-detail-h-align="left"
                                data-query-params="matAttTbParam"
                                data-response-handler="matAttTbResponseData"
                                data-url="${pageContext.request.contextPath}/bsc/att/listAttLinkAndDetail.do">
                            <thead>
                                <tr>
                                    <th data-field="attName" data-align="left"
                                        data-formatter="viewAttNameFormatter"
                                        data-colspan="1" data-width="150">附件名称</th>
                                    <th data-field="attFormat" data-align="center"
                                        data-colspan="1" data-width="80">附件类型</th>
                                    <th data-field="attSize" data-align="center"
                                        data-colspan="1" data-width="100" data-formatter="viewAttSizeFormatter">附件大小(KB)</th>
                                    <th data-field="_operator" data-formatter="attOperFormatter"
                                        data-align="center" data-width="100">操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <!-- 列表区域end -->
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-secondary" onclick="$('#show_mat_att_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>