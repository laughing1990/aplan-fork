<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .itemFrontCkSortDiv {

        color: #464637;
        font-size: 14px;
        font-family: 'Roboto', sans-serif;
        font-weight: 300;
    }

    /**标题样式**/
    .itemFrontCkSortDiv .block_list-title {

        padding: 10px;
        text-align: center;
        background: #abcdf1;
    }

    .itemFrontCkSortDiv ul {

        margin: 0;
        padding: 0;
        list-style: none;
        display: block;
    }

    .itemFrontCkSortDiv li {

        background-color: #fff;
        padding: 6px 0px;
        display: list-item;
        text-align: -webkit-match-parent;
        border: 1px solid transparent;
        border-bottom: 1px solid #ebebeb;
    }

    .block_ul_td li:hover {

        background: #e7e8eb;
        cursor: move;
        cursor: -webkit-grabbing;
    }

    .itemFrontCkSortDiv .drag-handle_th {

        text-align: center;
        display: inline-block;
        width: 8%;
        font-weight: 600;
    }

    /**拖拽手把**/
    .itemFrontCkSortDiv .drag-handle_td {

        text-align: center;
        font: bold 16px Sans-Serif;
        color: #5F9EDF;
        display: inline-block;
        width: 8%;
    }

    .itemFrontCkSortDiv .stage_name_td{

        display: inline-block;
        width: 45%;
    }
</style>

<!-- 分类排序 -->
<div id="itemFrontCkSortModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="itemFrontCkSortTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="itemFrontCkSortTitle">前置检测排序</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="itemFrontCkSortDiv" style="max-height: 450px; overflow: auto;">
                    <ul id="itemFrontCkSortUl" class="block_ul_td"></ul>
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-secondary" onclick="closeItemFrontCkModal();">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        Sortable.create(document.getElementById('itemFrontCkSortUl'), {
            animation: 150,
            onEnd: function (evt) {
                var objs = document.getElementsByName('itemFrontCkLi');
                if(objs!=null&&objs.length>0){
                    var ids=[],sorts=[];
                    for(var i=0;i<objs.length;i++){
                        var sort = i+1;
                        var id = $(objs[i]).attr('front-ck-id');
                        ids.push(id);
                        sorts.push(sort);
                    }
                    $.ajax({
                        url: ctx + '/aea/item/front/proj/updateFrontCkSort.do',
                        type: 'POST',
                        data: {
                            'ids': ids.toString(),
                            'sorts': sorts.toString(),
                            'type': frontCkType
                        },
                        success: function (result) {
                            if (result.success) {

                                if(frontCkType=='proj'){

                                    item_front_proj_tb.clear();

                                }else if(frontCkType=='item'){

                                    item_front_item_tb.clear();

                                }else if(frontCkType=='partform'){

                                    item_front_partform_tb.clear();
                                }
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
        });

        $('#itemFrontCkSortDiv').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });
    });

    var frontCkType = null;
    function sortItemFrontCheck(type){

        if(curIsEditable){

            frontCkType = null;
            frontCkType = type;
            var url = null;
            $('#itemFrontCkSortModal').modal('show');
            if(type=='proj'){

                $('#itemFrontCkSortTitle').html('前置项目信息排序');
                url = ctx+'/aea/item/front/proj/listAeaItemFrontProjByNoPage.do';

            }else if(type=='item'){

                $('#itemFrontCkSortTitle').html('前置事项信息排序');
                url = ctx + '/aea/item/front/item/listAeaItemFrontItemByNoPage.do';

            }else if(type=='partform'){

                $('#itemFrontCkSortTitle').html('前置扩展表信息排序');
                url = ctx + '/aea/item/front/partform/listAeaItemFrontPartformByNoPage.do';
            }
            $.ajax({
                url: url,
                type: 'POST',
                data: {
                    'itemVerId': currentBusiId
                },
                success: function (data) {

                    $("#itemFrontCkSortUl").html("");
                    if(type=='proj'){

                        for(var i=0;i<data.length;i++){

                            var liHtml = '<li name="itemFrontCkLi" front-ck-id="'+data[i].frontProjId+'">' +
                                            '<span class="drag-handle_td">&#9776;</span>' +
                                            '<span class="stage_name_td">'+data[i].ruleName+'</span>' +
                                         '</li>';
                            $('#itemFrontCkSortUl').append(liHtml);
                        }

                    }else if(type=='item'){

                        for(var i=0;i<data.length;i++){

                            var isCatalog = data[i].frontCkIsCatalog=='1'?'【标准事项】':'【实施事项】';

                            var liHtml = '<li name="itemFrontCkLi" front-ck-id"'+data[i].frontItemId+'">' +
                                            '<span class="drag-handle_td">&#9776;</span>' +
                                            '<span class="stage_name_td">'+ isCatalog + data[i].frontCkItemName+'【'+ data[i].frontCkItemCode +'】'+'</span>' +
                                        '</li>';
                            $('#itemFrontCkSortUl').append(liHtml);
                        }
                    }else if(type=='partform'){

                        for(var i=0;i<data.length;i++){

                            var liHtml = '<li name="itemFrontCkLi" front-ck-id="'+data[i].frontPartformId+'">' +
                                            '<span class="drag-handle_td">&#9776;</span>' +
                                            '<span class="stage_name_td">'+data[i].partformName+'</span>' +
                                        '</li>';
                            $('#itemFrontCkSortUl').append(liHtml);
                        }
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {

                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                }
            });
        }else {
            swal('提示信息', '当前版本下数据不可编辑!', 'info');
        }
    }

    function closeItemFrontCkModal(){

        $('#itemFrontCkSortModal').modal('hide');
    }
</script>