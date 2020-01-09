<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 分类排序 -->
<div id="theme_sort_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="theme_sort_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 750px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="theme_sort_modal_title">主题排序</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="opusOmSortDiv1" class="m-portlet__body" style="padding: 0px; max-height:500px; overflow:auto;">
                <div class="opusOmSortDiv">
                    <ul id="themeUl" class="block_ul_td"></ul>
                </div>
            </div>
            <div class="modal-footer" style="padding: 15px;height: 50px;">
                <span style="color: #f23454b8;margin-right: 28rem;">备注：拖拉拽完后点击"确定"按钮生效!</span>
                <button type="button" class="btn btn-info" onclick="saveThemeSort();" style="width: 75px;">确定</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        Sortable.create(document.getElementById('themeUl'), {
            animation: 150,
            onEnd: function (evt) {

            }
        });
    });

    //分类排序
    function themeSort(){

        $('#opusOmSortDiv1').animate({scrollTop: 0}, 800);//滚动到顶部
        $.post(ctx+'/aea/par/theme/listAeaParThemeNoPage.do',{}, function(data){
            if(data!=null&&data.length>0){
                $('#theme_sort_modal').modal('show');
                $("#themeUl").html("");
                for(var i=0;i<data.length;i++){
                    var liHtml = '<li name="themeLi" category-id="'+data[i].themeId+'">' +
                                     '<span class="drag-handle_td" style="width: 6%;">&#9776;</span>' +
                                     '<span class="org_name_td" style="width: 90%;">'+data[i].themeName+'</span>' +
                                  '</li>';
                    $('#themeUl').append(liHtml);
                }
            }else{
                $("#themeUl").html("");
                swal('提示信息', '暂无数据排序!', 'info');
            }
        }, 'json');
    }

    //保存分类排序
    function saveThemeSort(){

        var themeIds = [], sortNos = [];
        var liObjs = document.getElementsByName('themeLi');
        if(liObjs!=null&&liObjs.length>0){
            for(var i=0;i<liObjs.length;i++){
                themeIds.push($(liObjs[i]).attr('category-id'));
                sortNos.push(i+1);
            }
            $.ajax({
                url: ctx+'/aea/par/theme/batchSortThemes.do',
                type: 'post',
                data: {
                    'themeIds': themeIds.toString(),
                    'sortNos': sortNos.toString()
                },
                success: function (result) {
                    if (result.success) {

                        $('#theme_sort_modal').modal('hide');
                        location.reload();
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