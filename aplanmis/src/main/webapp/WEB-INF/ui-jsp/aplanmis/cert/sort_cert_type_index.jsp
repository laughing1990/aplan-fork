<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 分类排序 -->
<div id="cert_type_sort_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="cert_type_sort_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="width: 600px;" >
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="cert_type_sort_modal_title">分类排序</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="m-portlet__body" style="padding: 15px;max-height:500px;overflow-y:auto;overflow-x: hidden">
                <div class="opusOmSortDiv">
                    <ul id="certTypeUl" class="block_ul_td"></ul>
                </div>
            </div>
            <div class="modal-footer" style="padding: 15px;height: 50px;">
                <button type="button" class="btn btn-info" onclick="saveSortCertType();" style="width: 75px;">确定</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        Sortable.create(document.getElementById('certTypeUl'), {
            animation: 150,
            //拖拽完毕之后发生该事件
            onEnd: function (evt) {
                var liObjs = document.getElementsByName('certTypeLi');
                var errorCount = 0;;
                for(var i=0;i<liObjs.length;i++){
                    var sortNum = i+1;
                    var typeName = $(liObjs[i]).find("span").eq(1).html();
                    var certTypeId = $(liObjs[i]).attr('category-id');
                    $.post(ctx+'/aea/cert/type/updateAeaCertType.do',{'certTypeId':certTypeId,'sortNo':sortNum}, function(data){
                        if(!data.success){
                            errorCount++;
                        }
                    }, 'json');
                }
            }
        });

    });

    //分类排序
    function sortCertType(title){

        $('#cert_type_sort_modal').modal('show');
        $('#cert_type_sort_modal_title').html(title);
        $.post(ctx+'/aea/cert/type/listAeaCertTypeNoPage.do',{}, function(data){
            if(data!=null&&data.length>0){
                $("#certTypeUl").html("");
                for(var i=0;i<data.length;i++){
                    var liHtml = '<li name="certTypeLi" category-id="'+data[i].certTypeId+'">' +
                                    '<span class="drag-handle_td">&#9776;</span>' +
                                    '<span class="org_name_td">'+data[i].typeName+'</span>' +
                                '</li>';
                    $('#certTypeUl').append(liHtml);
                }
            }
        }, 'json');
    }

    //保存分类排序
    function saveSortCertType(){

        $('#cert_type_sort_modal').modal('hide');
        location.reload();
    }
</script>