<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="set_aux_rel_main_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="set_aux_rel_main_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="set_aux_rel_main_modal_title">设置辅线关联主线</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;"></div>
                        <div class="col-md-6" style="padding: 0px;">
                            <div class="row" style="margin: 0px;">
                                <div class="col-7">
                                    <div class="m-input-icon m-input-icon--left">
                                        <input id="mainLineKeyWord" type="text" class="form-control m-input"
                                               placeholder="请输入关键字..." name="keyword" value=""/>
                                        <span class="m-input-icon__icon m-input-icon__icon--left">
                                            <span><i class="la la-search"></i></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <button type="button" class="btn btn-info" onclick="searchMainLine();">查询</button>
                                    <button type="button" class="btn btn-secondary" onclick="clearMainLine();">清空</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="main_line_list_tb_scroll" style="height: 420px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="main_line_list_tb"
                                data-toggle="table",
                                data-click-to-select=true,
                                data-single-select=true,
                                data-pagination=false,
                                data-query-params="mainLineParam",
                                data-url="${pageContext.request.contextPath}/aea/par/stage/listAeaParStageNoPage.do?isDeleted=0">
                            <thead>
                                <tr>
                                    <th data-field="" data-checkbox="true" data-align="center"
                                        data-colspan="1" data-width="10"></th>
                                    <th data-field="stageName" data-align="left" data-width="250">名称</th>
                                    <th data-field="stageCode" data-align="left" data-width="250">编号</th>
                                    <th data-field="isNode" data-align="left" data-width="100" data-formatter="isNodeFormatter">类型</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <!-- 列表区域end -->
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button id="btn_save_main_line_list_tb" type="button" class="btn btn-info">保存</button>
                <button type="button" class="btn btn-secondary" onclick="$('#set_aux_rel_main_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var commonQueryParams = [];
    var curAuxLineId = null;
    var mainLineId = null;

    $(function(){

        $('#main_line_list_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $("#btn_save_main_line_list_tb").click(function (e) {

            //获取选中的数据
            saveAuxRelMainData();
        });

    });

    function isNodeFormatter(value, row, index, field) {

        if(value){
            if(value=='1'){
                return '主线';
            }else if(value=='2'){
                return '辅线';
            }else {
                return '技术审查线';
            }
        }
    }

    function viewAuxRelMain(stageId, parentId){

        curAuxLineId = stageId;
        mainLineId = parentId;
        clearMainLine();
        $("#set_aux_rel_main_modal").modal("show");
    }

    // 查询
    function searchMainLine() {

        commonQueryParams = [];
        commonQueryParams.push({name: "themeVerId", value: curThemeVerId});
        commonQueryParams.push({name: "isNode", value: '1'});
        var keywordVal = $("#mainLineKeyWord").val();
        if (keywordVal) {
            commonQueryParams.push({name: "keyword", value: keywordVal});
        }
        $("#main_line_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#main_line_list_tb").bootstrapTable('refresh', commonQueryParams);       //无参数刷新

        $("#main_line_list_tb").on('load-success.bs.table', function () {
            queryAuxRelMain();
        });
    }

    //参数设置
    function mainLineParam(params) {

        var queryParam = {};
        //组装查询参数
        var buildParam = {};
        if (commonQueryParams) {
            for (var i = 0; i < commonQueryParams.length; i++) {
                buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
            }
            queryParam = $.extend(queryParam, buildParam);
        }
        return queryParam;
    }

    // 设置辅线模块关联主线
    function saveAuxRelMainData() {

        var rows = $('#main_line_list_tb').bootstrapTable('getSelections');
        $.ajax({
           url: ctx + '/aea/par/stage/updateAeaParStage.do',
           type: 'POST',
           data:  {
               "stageId": curAuxLineId,
               "parentId": rows==null?null:rows[0].stageId,
           },
           async: false,
           success: function (result) {
               if (result.success) {
                   swal({
                       text: '保存成功！',
                       type: 'success',
                       timer: 1500
                   });
                   searchParStageCondition('2');
               }else{
                   swal('错误信息', result.message, 'error');
               }
           },
           error: function () {
               swal('错误信息', '保存失败!', 'error');
           }
        });
    }

    // 清空查询
    function clearMainLine() {

        //搜索框置空
        $('#mainLineKeyWord').val('');
        searchMainLine();
    }

    function queryAuxRelMain() {

        $("#main_line_list_tb").bootstrapTable("checkBy", {
            field: 'stageId', values: [mainLineId]
        });
    }

</script>