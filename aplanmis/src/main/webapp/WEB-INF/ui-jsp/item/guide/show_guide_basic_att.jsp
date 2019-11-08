<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="show_guide_basic_att_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width:900px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="show_guide_basic_att_modal_title">查看附件</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeAndRefreshData();">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="show_guide_basic_att_tb_scroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="show_guide_basic_att_tb"
                                data-toggle="table"
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
                                data-query-params="guideBasicAttTbParam"
                                data-response-handler="guideBasicAttTbResponseData"
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
            <div class="modal-footer" style="padding: 10px;height: 60px;text-align:right;">
                <button type="button" class="btn btn-secondary" onclick="closeAndRefreshData();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var commonQueryParams = [];
    function clearGuideBasicFile(){

        $("#wsbllctSample").siblings('.custorm-style').find(".right-text").html("");
        $("#ckbllctSample").siblings('.custorm-style').find(".right-text").html("");
        $('#wsbllctSampleDiv').hide();
        $('#ckbllctSampleDiv').hide();
    }

    function uploadFileChange(obj){

        $(obj).siblings('.custorm-style').find(".right-text").html("");
        var files = $(obj)[0].files;
        if(files!=null&&files.length>0){
            var names = [];
            for(var i=0;i<files.length;i++){
                names.push(files[i].name);
            }
            $(obj).siblings('.custorm-style').find(".right-text").html(names.toString());
        }
    }

    var showGuideBasicType = null;
    function showGuideBasicAtt(type){

        $('#show_guide_basic_att_modal').modal('show');
        showGuideBasicType = type;
        searchGuideBasicAttSto(type);
    }

    function searchGuideBasicAttSto(type) {

        commonQueryParams = [];
        commonQueryParams.push({name: "tableName",value:"AEA_ITEM_GUIDE"});
        commonQueryParams.push({name: "pkName",value: type});
        commonQueryParams.push({name: "recordId",value: itemGuideId});
        $("#show_guide_basic_att_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#show_guide_basic_att_tb").bootstrapTable('refresh');       //无参数刷新
    }

    function guideBasicAttTbParam(params){

        var pageNum = (params.offset / params.limit) + 1;
        var queryParam = {
            pageNum: pageNum,
            pageSize: params.limit,
            sort: params.sort,
            order: params.order,
        };
        //组装查询参数
        var buildParam = {};
        if (commonQueryParams) {
            for (var i = 0; i < commonQueryParams.length; i++) {
                buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;
            }
            queryParam = $.extend(queryParam, buildParam);
        }
        return queryParam;
    }

    function guideBasicAttTbResponseData(res) {
        return res;
    }

    function viewAttNameFormatter(value, row, index){

        // 图片
        if(row.attFormat=="jpg"||row.attFormat=="png"||row.attFormat=="jpeg"||row.attFormat=="jpe"||row.attFormat=="gif"){

            return '<a href="#" onclick="showImgFile(\''+ row.detailId +'\')">'+ row.attName +'</a>';

        }else{ // 文件

            var url = "";
            if(row.attType=='KP'){

                url = ctx +'/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
            }else{

                url = ctx + '/aea/item/guide/downloadFile.do?detailId='+ row.detailId;
            }
            return '<a href="#" onclick="showDownloadFile(\''+ url +'\')">'+ row.attName +'</a>';
        }
    }

    function viewAttSizeFormatter(value, row, index){

        if(value){
            return value/1000;
        }
    }

    function showDownloadFile(url){

        window.open(url, "下载文件");
    }

    function showImgFile(detailId){

        window.open(ctx+'/aea/item/mat/showFile.do?detailId='+ detailId, "展示图片");
    }

    function attOperFormatter(value, row, index){

        if(row.attType!='KP'){
            var deleteBtn = '<a href="javascript:deleteGuideBasicAttrByDetailId(\'' + row.detailId + '\')" ' +
                                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                                'title="删除"><i class="la la-trash"></i>' +
                            '</a>';
            return deleteBtn;
        }
    }

    function deleteGuideBasicAttrByDetailId(id){

        if(curIsEditable){
            if(id){
                var msg = '此操作将删除附件,确定要删除吗？';
                swal({
                    title: '',
                    text: msg,
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(function (result) {
                    if (result.value) {
                        $.ajax({
                            type: 'post',
                            url: ctx+'/aea/item/guide/delItemGuideAtt.do',
                            dataType: 'json',
                            data: {'detailId': id, 'type': showGuideBasicType, 'itemGuideId': itemGuideId},
                            success: function (result) {
                                if (result.success) {
                                    swal({
                                        type: 'success',
                                        title: '删除成功！',
                                        showConfirmButton: false,
                                        timer: 1000
                                    });
                                    searchGuideBasicAttSto(showGuideBasicType);
                                } else {
                                    swal('错误信息','删除失败','error');
                                }
                            }
                        });
                    }
                });
            }else{
                swal('提示信息', '请选择操作记录！', 'info');
            }
        }else{
            swal('提示信息', '当前事项版本下数据不可编辑！', 'info');
        }
    }

    function closeAndRefreshData(){

        $('#show_guide_basic_att_modal').modal('hide');
        window.location.reload();
    }
</script>
