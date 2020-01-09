<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 分类排序 -->
<div id="stage_sort_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="stage_sort_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 750px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="stage_sort_modal_title">阶段排序</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="parallerStageSortDiv1" class="m-portlet__body" style="padding: 0px; max-height:500px; overflow:auto;">
                <div class="parallerStageSortDiv">
                    <ul id="parallerStageUl" class="block_ul_td"></ul>
                </div>
            </div>
            <div class="modal-footer" style="padding: 15px;height: 50px;">
                <span style="color: #f23454b8;margin-right: 28rem;">备注：拖拉拽完后点击"确定"按钮生效!</span>
                <button type="button" class="btn btn-info" onclick="saveSortParStage();" style="width: 75px;">确定</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        Sortable.create(document.getElementById('parallerStageUl'), {
            animation: 150,
            onEnd: function (evt) {

            }
        });
    });

    /**
     * 阶段排序
     */
    var stageIsNode = null;
    function sortParStage(isNode){

        //滚动到顶部
        $('#parallerStageSortDiv1').animate({scrollTop: 0}, 800);
        if(curIsEditable){
            stageIsNode = isNode;
            var title = (isNode=='1')?"阶段":"模块";
            $.post(ctx+'/aea/par/stage/listAeaParStageNoPage.do',{
                'isNode':isNode,
                'isDeleted':'0',
                'themeVerId': curThemeVerId
            }, function(data){
                if(data!=null&&data.length>0){
                    $('#stage_sort_modal').modal('show');
                    $('#stage_sort_modal_title').html(title+"排序");
                    $("#parallerStageUl").html("");
                    for(var i=0;i<data.length;i++){
                        var liHtml = '<li name="stageLi" stage-id="'+data[i].stageId+'">' +
                                        '<span class="drag-handle_td" style="width: 6%;">&#9776;</span>' +
                                        '<span class="stage_name_td" style="width: 90%;">'+data[i].stageName+'</span>' +
                                     '</li>';
                        $('#parallerStageUl').append(liHtml);
                    }
                }else{
                    $("#parallerStageUl").html("");
                    swal('提示信息', '暂无数据排序!', 'info');
                }
            }, 'json');
        }else {
            swal('提示信息', '当前版本下数据不可编辑!', 'info');
        }
    }

    //保存分类排序
    function saveSortParStage(){

        var stageIds = [], sortNos = [];
        var liObjs = document.getElementsByName('stageLi');
        if(liObjs!=null&&liObjs.length>0){
            for(var i=0;i<liObjs.length;i++){
                stageIds.push($(liObjs[i]).attr('stage-id'));
                sortNos.push(i+1);
            }
            $.ajax({
                url: ctx+'/aea/par/stage/batchSortStages.do',
                type: 'post',
                data: {
                    'stageIds': stageIds.toString(),
                    'sortNos': sortNos.toString()
                },
                success: function (result) {
                    if (result.success) {

                        $('#stage_sort_modal').modal('hide');
                        searchParStageCondition(stageIsNode);

                    } else {

                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }
    }
</script>