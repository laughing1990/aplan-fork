<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 分类排序 -->
<div id="sort_par_front_stage_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="sort_par_front_stage_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="sort_par_front_stage_modal_title">前置检测主线/辅线排序</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="sortParFrontStageDiv" style="max-height:500px; overflow:auto;">
                    <div class="opusOmSortDiv">
                        <ul id="sortParFrontStageUl" class="block_ul_td"></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-secondary"
                        onclick="$('#sort_par_front_stage_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        $('#sortParFrontStageDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        Sortable.create(document.getElementById('sortParFrontStageUl'), {
            animation: 150,
            //拖拽完毕之后发生该事件
            onEnd: function (evt) {
                var liObjs = document.getElementsByName('sortParFrontStageLi');
                var ids = [];
                var sortNos = [];
                if(liObjs!=null&&liObjs.length>0) {
                    for (var i = liObjs.length; i > 0; i--) {
                        var id = $(liObjs[i - 1]).attr('category-id');
                        ids.push(id);
                        sortNos.push(i);
                    }
                    $.ajax({
                        type: 'post',
                        url: ctx+'/aea/par/front/stage/updateAeaParFrontStageSortNos.do',
                        dataType: 'json',
                        data: {
                            'ids': ids.toString(),
                            'sortNos': sortNos.toString()
                        },
                        success: function (result) {
                            if (result.success) {
                                // 重新加载数据
                                par_front_stage_tb.clear();
                            } else {
                                swal('错误信息', '操作成功', 'error');
                            }
                        }
                    });
                }
            }
        });
    });

    //分类排序
    function sortParFrontStage(){

        if(curIsEditable) {

            $.post(ctx + '/aea/par/front/stage/listAeaParFrontStageByNoPage.do', {
                'stageId': currentBusiId,
            }, function (data) {
                if (data != null && data.length > 0) {

                    $('#sort_par_front_stage_modal').modal('show');
                    $('#sortParFrontStageDiv').animate({scrollTop: 0}, 800);//滚动到顶部
                    $("#sortParFrontStageUl").html("");

                    for (var i = 0; i < data.length; i++) {
                        var id = data[i].frontStageId;
                        var name = data[i].histStageName;
                        var liHtml = '<li name="sortParFrontStageLi" category-id="' + id + '">' +
                            '<span class="drag-handle_td">&#9776;</span>' +
                            '<span class="org_name_td" style="width: 90%;">' + name + '</span>' +
                            '</li>';
                        $('#sortParFrontStageUl').append(liHtml);
                    }
                } else {
                    swal({
                        type: 'info',
                        title: '暂无主线/辅线信息前置检测数据！',
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
            }, 'json');
        }else{
            swal('提示信息', '当前版本下数据不可编辑!', 'info');
        }
    }

</script>