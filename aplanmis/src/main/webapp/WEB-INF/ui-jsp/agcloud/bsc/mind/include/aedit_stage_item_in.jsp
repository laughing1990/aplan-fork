<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 编辑事项配置 -->
<div id="stage_in_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="stage_in_item_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="stage_in_item_modal_title">材料事项配置</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <!--列表区域-->
            <div id="stage_in_item_scroll" style="height: 500px; overflow-y:auto;overflow-x:auto;">
                <div class="base" style="padding: 0px 5px;">
                    <table  id="stage_in_item_table"
                            data-toggle="table"
                            data-method="post"
                            data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                            data-click-to-select=true
                            data-pagination=false
                            data-sortable=false
                            data-query-params="stageInItemParam",
                            data-response-handler="stageInItemResponseData",
                            data-url="${pageContext.request.contextPath}/aea/par/stage/item/listStageItemByInId.do">
                        <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="center"
                                    data-formatter="isCheckFormatter" data-width="10">ID</th>
                                <th data-field="isOptionItem" data-align="left" data-width="80"
                                    data-formatter="isOptionItemFormatter">事项类型</th>
                                <th data-field="itemCode" data-align="left" data-width=200>事项编号</th>
                                <th data-field="itemName" data-align="left" data-width=350 data-formatter="itemOrgNameFormatter">事项名称</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="saveStageInItemBtn" type="button" class="btn btn-info" onclick="saveStageInItem();">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#stage_in_item_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>