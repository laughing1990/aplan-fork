<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div class="m-form m-form--label-align-right m--margin-bottom-5">
    <div class="row" style="margin: 0px;">
        <div class="col-md-6"style="text-align: left;">
            <button type="button" class="btn btn-info" onclick="addStageMat(true,'0');">添加材料</button>
            <button type="button" class="btn btn-info" onclick="chooseStageMat(true);">导入库材料</button>
            <button type="button" class="btn btn-secondary" onclick="batchDelStageStateMat(true);">删除</button>
        </div>
        <div class="col-md-6" style="padding: 0px;">
            <div class="row" style="margin: 0px;">
                <div class="col-7">
                    <div class="m-input-icon m-input-icon--left">
                        <input id="commonMatKeyword" type="text" class="form-control m-input"
                                placeholder="请输入关键字..." name="keyword" value=""/>
                        <span class="m-input-icon__icon m-input-icon__icon--left">
                            <span><i class="la la-search"></i></span>
                        </span>
                    </div>
                </div>
                <div class="col-5">
                    <button type="button" class="btn btn-info" onclick="searchStageCommonMatList();">查询</button>
                    <button type="button" class="btn btn-secondary" onclick="clearStageCommonMatListSearch();">清空</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
<!-- 列表区域 -->
<div id="stage_common_mat_tab_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
    <div class="base" style="padding: 0px 5px;">
        <table  id="stage_common_mat_list_tb"
                data-toggle="table",
                data-click-to-select=true,
                queryParamsType="",
                data-pagination="true",
                data-page-size="5",
                data-page-list="[5]",
                data-side-pagination="server",
                data-query-params="stageCommonMatParam",
                data-maintain-selected = "true",
                data-url="${pageContext.request.contextPath}/aea/par/in/listStageMatByStageId.do">
            <thead>
                <tr>
                    <th data-field="" data-checkbox="true" data-align="center"
                        data-colspan="1" data-width="10" data-formatter="checkCommonMat"></th>
                    <th data-field="inId"  data-align="left" data-width="10">ID</th>
                    <th data-field="matId"  data-align="left" data-width="10">MATID</th>
                    <th data-field="aeaMatCertName" data-align="left" data-width=230>材料名称</th>
                    <th data-field="aeaMatCertCode" data-align="left" data-width=230>材料编号</th>
                    <th data-field="_operator" data-formatter="stageCommonMatFormatter"
                        data-align="center" data-width="100">操作</th>
                </tr>
            </thead>
        </table>
    </div>
</div>
<!-- 列表区域end -->

<script type="text/javascript">

    var commonMatCommonQueryParams = {};

    // 查询
    function searchStageCommonMatList(){

        commonMatCommonQueryParams = {};
        commonMatCommonQueryParams['stageId'] = currentBusiId;
        commonMatCommonQueryParams['keyword'] =  $('#commonMatKeyword').val();
        commonMatCommonQueryParams['isCommon'] =  true;
        commonMatCommonQueryParams['isStateIn'] =  '0';
        $("#stage_common_mat_list_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $('#stage_common_mat_list_tb').bootstrapTable('hideColumn', 'inId');
        $('#stage_common_mat_list_tb').bootstrapTable('hideColumn', 'matId');
        $("#stage_common_mat_list_tb").bootstrapTable('refresh',commonMatCommonQueryParams);
    }


    //参数设置
    function stageCommonMatParam(params) {

        var pageNum = (params.offset / params.limit) + 1;
        commonMatCommonQueryParams['pageNum'] = pageNum;
        commonMatCommonQueryParams['pageSize'] = params.limit;
        commonMatCommonQueryParams['stageId'] = currentBusiId;
        commonMatCommonQueryParams['isCommon'] =  true;
        commonMatCommonQueryParams['keyword'] =  $('#commonMatKeyword').val();
        commonMatCommonQueryParams['isStateIn'] =  '0';
        return commonMatCommonQueryParams;
    }

    function clearStageCommonMatListSearch() {

        $('#commonMatKeyword').val('');//搜索框置空
        searchStageCommonMatList();

    }
    
    function stageCommonMatFormatter(value, row, index) {

        var editBtn = '<a href="javascript:editStageMat(true,\''+row.matId + '\',\'0\')" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
            'title="编辑"><i class="la la-edit"></i>' +
            '</a>';


        var editStageItemInBtn = '<a href="javascript:editCommonMatItem(\''+row.matId + '\')" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
            'title="关联事项"><i class="la la-cog"></i>' +
            '</a>';


        var deleteBtn = '<a href="javascript:deleteStageMat(true,\''+row.inId + '\')" ' +
            'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
            'title="删除"><i class="la la-trash"></i>' +
            '</a>';

        return editBtn + editStageItemInBtn + deleteBtn;
    }

    function editCommonMatItem(matId) {

        viewStageMatItem(currentBusiId,stageItemStateId,matId,AeaMindConst_MIND_NODE_TYPE_CODE_MAT,true);
    }

    function checkCommonMat(i,row){

        return {
            checked: true
        }
    }

</script>