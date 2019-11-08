<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .opusOmSortDiv {
        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .opusOmSortDiv .block_list-title {
        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .opusOmSortDiv ul {
        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .opusOmSortDiv li {
        background-color: #fff;
        padding: 6px 0px;
        display: list-item;
        text-align: -webkit-match-parent;

        /**边框样式**/
        border: 1px solid transparent;
        border-bottom: 1px solid #ebebeb;
    }

    /**
        * 移动到li样式
    */
    .block_ul_td li:hover {
        background: #e7e8eb;
        cursor: move;
        cursor: -webkit-grabbing;
    }

    .opusOmSortDiv .drag-handle_th {
        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .opusOmSortDiv .drag-handle_td {
        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    /**组织列表（名字、属性）、岗位列表（名字、职责）、 用户列表（名字，岗位） 宽度**/
    .opusOmSortDiv .org_name_th, .opusOmSortDiv .org_property_th, .opusOmSortDiv .pos_name_th, .opusOmSortDiv .pos_duty_th, .opusOmSortDiv .user_name_th, .opusOmSortDiv .user_position_th {
        display: inline-block;
        width: 45%;
        font-weight: 600;
    }

    .opusOmSortDiv .org_name_td, .opusOmSortDiv .org_position_td, .opusOmSortDiv .pos_name_td, .opusOmSortDiv .pos_duty_td, .opusOmSortDiv .user_name_td, .opusOmSortDiv .user_position_td {
        display: inline-block;
        width: 45%;
    }
</style>

<!-- 分类排序 -->
<div id="item_in_sort_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="item_in_sort_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="item_in_sort_modal_title">材料证照表单排序</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="opusOmSortDiv1" style="max-height:500px; overflow:auto;">
                    <div class="opusOmSortDiv">
                        <ul id="sortItemInUl" class="block_ul_td"></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-secondary" onclick="$('#item_in_sort_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){

        $('#opusOmSortDiv1').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        Sortable.create(document.getElementById('sortItemInUl'), {
            animation: 150,
            //拖拽完毕之后发生该事件
            onEnd: function (evt) {
                var liObjs = document.getElementsByName('sortItemInLi');
                var errorCount = 0;
                var inoutIds = [];
                var fileTypes = [];
                var sortNos = [];
                if(liObjs!=null&&liObjs.length>0) {
                    for (var i = liObjs.length; i > 0; i--) {
                        var id = $(liObjs[i-1]).attr('category-id');
                        var type = $(liObjs[i-1]).attr('category-type');
                        inoutIds.push(id);
                        fileTypes.push(type);
                        sortNos.push(i);
                    }
                    $.ajax({
                        type: 'post',
                        url: ctx + '/aea/item/inout/updateItemInSortNos.do',
                        dataType: 'json',
                        data: {
                            'inoutIds': inoutIds.toString(),
                            'fileTypes': fileTypes.toString(),
                            'sortNos': sortNos.toString()
                        },
                        success: function (result) {
                            if (result.success) {
                                // 刷新表格
                                clearSearchItemInMatCert();
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
    function sortItemIn(){

        $.post(ctx + '/aea/item/inout/listAeaItemMatCertFormByNoPage.do',{
            'itemVerId': currentBusiId,
            'stateVerId': currentStateVerId,
            'isInput': '1',
        }, function(data){
            if(data!=null&&data.length>0){

                $('#item_in_sort_modal').modal('show');
                $('#opusOmSortDiv1').animate({scrollTop: 0}, 800);//滚动到顶部
                $("#sortItemInUl").html("");

                for(var i=0;i<data.length;i++){
                    var id = data[i].inoutId;
                    var type = data[i].fileType;
                    var name = '【';
                    if(data[i].fileType=='mat'){
                        name += '材料';
                    }else if(data[i].fileType=='cert'){
                        name += '证照';
                    }else if(data[i].fileType=='form'){
                        name += '表单';
                    }
                    name += '】';
                    name += data[i].aeaMatCertName;
                    var liHtml = '<li name="sortItemInLi" category-id="'+ id +'" category-type="'+ type +'">' +
                                     '<span class="drag-handle_td">&#9776;</span>' +
                                     '<span class="org_name_td" style="width: 90%;">'+ name +'</span>' +
                                 '</li>';
                    $('#sortItemInUl').append(liHtml);
                }
            }else{
                swal({
                    type: 'info',
                    title: '暂无材料证照表单数据！',
                    showConfirmButton: false,
                    timer: 1500
                });
            }
        }, 'json');
    }
</script>