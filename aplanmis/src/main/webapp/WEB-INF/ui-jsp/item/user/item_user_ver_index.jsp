
<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .row{
        margin-left: 0;
        margin-right: 0;
    }
    .base tbody tr td label {
        display: inline-block;
    }

    .changeColor{

        background: #ece6e6 !important;
        color: white;
    }

    /**绿色勾*/
    .myicon-tick-checked {
        display: inline-block;
        position: relative;
        width: 15px;
        height: 15px;
        border-radius: 50%;
        background-color: #2ac845;
    }


    /**灰色勾*/
    .myicon-tick-uncheck {
        display: inline-block;
        position: relative;
        width: 15px;
        height: 15px;
        border-radius: 50%;
        background-color: #5f646e;
    }


    .myicon-tick-checked:before, .myicon-tick-checked:after,.myicon-tick-uncheck:before,.myicon-tick-uncheck:after {
        content: '';
        pointer-events: none;
        position: absolute;
        color: white;
        border: 1px solid;
        background-color: white;
    }


    .myicon-tick-checked:before,.myicon-tick-uncheck:before {
        width: 1px;
        height: 1px;
        left: 25%;
        top: 50%;
        transform: skew(0deg,50deg);
    }


    .myicon-tick-checked:after,.myicon-tick-uncheck:after {
        width: 3px;
        height: 1px;
        left: 45%;
        top: 42%;
        transform: skew(0deg,-50deg);
    }
</style>

<div id="view_item_ver_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_item_ver_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="view_item_ver_modal_title">事项版本</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="loadItemVerTable();">刷新</button>&nbsp;
                            <button type="button" class="btn btn-secondary" onclick="openOperaInfo();">
                                <span><i class="flaticon-info"></i>&nbsp;<span>帮助</span></span>
                            </button>
                        </div>
                        <div class="col-md-6" style="text-align: right;padding-right: 15px;"></div>
                    </div>
                </div>
                <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="view_item_ver_scroll" style="height: 450px; overflow:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table id="view_item_ver_tb"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    var ver_itemId;
    var curritemNature;
    $(function(){

        $("#view_item_ver_scroll").niceScroll({

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

    // 打开事项版本页面
    function openItemVerModal(itemId, itemVerId, itemNature){

        $('#view_item_ver_modal').modal('show');
        $('#view_item_ver_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        ver_itemId = itemId;
        // ver_itemVerId = itemVerId;
        curritemNature = itemNature;
        loadItemVerTable();
    }

    function openItemVerMaterialModal(itemVerId){
        var busiType='item';
        window.location.href=ctx+'/aea/item/indexSetAgentServiceItemOut.do?busiType=' + busiType + '&busiId=' + itemVerId;
    }

    // 加载事项版本数据
    var view_item_ver_tb;
    function loadItemVerTable() {

        if (view_item_ver_tb != null) {
            view_item_ver_tb.bootstrapTable('destroy');
        }
        view_item_ver_tb = $('#view_item_ver_tb').bootstrapTable({
            url: ctx + '/aea/item/ver/listAeaItemVerNoPage.do',
            method: 'get',
            queryParams: {
                'itemId': ver_itemId
            },
            height: '100%',
            undefinedText: '-',
            striped: false,
            cache: false,
            contentType: 'application/json',
            pagination: false,
            onlyInfoPagination: false,
            sidePagination: 'client',
            pageNumber: 1,
            pageSize: 5,
            pageList: [5],
            paginationShowPageGo: true,
            paginationVAlign: 'bottom',
            paginationHAlign: 'right',
            onClickRow:function (row, $element) {

                $('.changeColor').removeClass('changeColor');
                $($element).addClass('changeColor');
            },
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
            },
            columns: [
                {
                    filed: "itemVerId",
                    visible: false
                },
                {
                    field: "itemId",
                    visible: false
                },
                {
                    field: "verNum",
                    title: "版本号",
                    align: "left",
                    width: 120,
                    formatter: function(value, row) {
                        if(value){
                            if((''+value).indexOf('.01')>-1){
                                return 'V' + value;
                            }else{
                                return 'V' + value + '.0';
                            }
                        }
                    }
                },
                {
                    field: "verStatusName",
                    title: "发布状态",
                    align: "left",
                    width: 120,
                    formatter: function(value, row) {

                        var curVer = "<span class='myicon-tick-checked'></span>";
                        if (row.itemVerStatus == '0') { // 未发布
                            return "未发布";
                        }else if (row.itemVerStatus == '1') { // 正式发布
                            return "已发布&nbsp;"+ curVer;
                        }else if (row.itemVerStatus == '2') { // 试运行
                            return "试运行&nbsp;"+ curVer;
                        }else if(row.itemVerStatus == '3') { // 已过时
                            return "已过时";
                        }
                    }
                },
                {
                    field: '_operate',
                    title: '操作',
                    sortable: false,
                    width: 350,
                    align: "left",
                    formatter: function (value, row) {

                        var itemBtn = '<button type="button" class="btn btn-info btn-sm"  onclick="editItemBasicById(\''+row.itemBasicId+'\',\''+row.itemVerStatus+'\',\'' + row.isCatalog +'\');"><span><i class="la la-search"></i>&nbsp;<span>查看事项</span></span></button>&nbsp;&nbsp;';
                        var copyBtn = '<button type="button" class="btn btn-info btn-sm" onclick="copyOneItemVer(\''+row.itemId+'\',\''+row.itemVerId+'\')"><span><i class="la la-file-text"></i>&nbsp;<span>复制</span></span></button>&nbsp;&nbsp;';
                        var testRunBtn = ''; // 试运行
                        var publishBtn = ''; // 发布
                        if (row.itemVerStatus == '0') {
                            itemBtn = '<button type="button" class="btn btn-info btn-sm" onclick="editItemBasicById(\''+row.itemBasicId+'\',\''+row.itemVerStatus+'\',\'' + row.isCatalog +'\');"><span><i class="la la-edit"></i>&nbsp;<span>编辑事项</span></span></button>&nbsp;&nbsp;';
                            testRunBtn = '<button type="button" class="btn btn-info btn-sm" onclick="testRunItemVer(\''+row.itemId+'\',\''+row.itemVerId+'\', \'' + row.itemVerStatus + '\', \'' + row.verNum + '\')"><span><i class="la la-check-circle"></i>&nbsp;<span>试运行</span></span></button>&nbsp;&nbsp;';
                            publishBtn = '<button type="button" class="btn btn-info btn-sm" onclick="publishItemVer(\''+row.itemId+'\',\''+row.itemVerId+'\', \'' + row.itemVerStatus + '\', \'' + row.verNum + '\')"><span><i class="la la-check-circle"></i>&nbsp;<span>发布</span></span></button>&nbsp;&nbsp;';
                        }
                        if (row.itemVerStatus == '2') {
                            itemBtn = '<button type="button" class="btn btn-info btn-sm" onclick="editItemBasicById(\''+row.itemBasicId+'\',\''+row.itemVerStatus+'\',\'' + row.isCatalog +'\');"><span><i class="la la-edit"></i>&nbsp;<span>编辑事项</span></span></button>&nbsp;&nbsp;';
                            publishBtn = '<button type="button" class="btn btn-info btn-sm" onclick="publishItemVer(\''+row.itemId+'\',\''+row.itemVerId+'\',\''+row.itemVerStatus+'\', \''+row.verNum+'\')"><span><i class="la la-check-circle"></i>&nbsp;<span>发布</span></span></button>&nbsp;&nbsp;';
                        }
                        var viewBtn = '';
                        var copyAndNewBtn = '';
                        if(row.isCatalog=='0') {
                            viewBtn = '<button type="button" class="btn btn-info btn-sm" onclick="viewItemVer(\'' + row.itemId + '\',\'' + row.itemVerId + '\')"><span><i class="la la-search"></i>&nbsp;<span>查看</span></span></button>&nbsp;&nbsp;';
                            copyAndNewBtn = '<button type="button" class="btn btn-info btn-sm" onclick="copyAndNewItem(\'' + row.itemId + '\',\'' + row.itemVerId + '\')"><span><i class="la la-file-text"></i>&nbsp;<span>复制创建</span></span></button>&nbsp;&nbsp;';
                        }
                        var cfgServiceBtn = '';//配置信息
                        if(curritemNature && curritemNature == '8'){
                            cfgServiceBtn = '<button type="button" class="btn btn-info btn-sm"  onclick="cfgServiceInfo(\''+row.itemVerId+'\');">配置信息</button>&nbsp;&nbsp;';
                        }
                        return itemBtn + testRunBtn + publishBtn + copyBtn /*+ copyAndNewBtn*/ + viewBtn + cfgServiceBtn;
                    }
                }
            ],
        });
    }

    //跳转到中介服务事项配置信息页面
    function cfgServiceInfo(itemVerId) {

        openFullWindow(ctx + '/aea/item/itemService.do?itemVerId='+ itemVerId);
    }

    /**
     * 复制版本
     *
     * @param itemVerId
     *
     */
    function copyItemVer(){

        $.ajax({
            url: ctx + "/aea/item/ver/unpublished/num.do",
            type: 'POST',
            data: {
                'itemId': ver_itemId
            },
            // async: false,
            success: function (result) {
                if (result.success) {
                    swal({
                        text: "此操作将复制最新试运行或发布版本数据并生成最新未发布版本,未发布版本下数据可以编辑,您确定执行吗？",
                        type: 'warning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消'
                    }).then(function (result) {
                        if (result.value) {

                            $("#uploadProgress").modal("show");
                            $('#uploadProgressMsg').html("复制版本数据中,请勿点击,耐心等候...");

                            $.ajax({
                                url: ctx + "/aea/item/ver/copy/max/version.do",
                                type: 'POST',
                                data:  {
                                    'itemId': ver_itemId
                                },
                                // async: false,
                                success: function (result) {
                                    if (result.success) {

                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal({
                                                type: 'success',
                                                title: '复制成功！',
                                                showConfirmButton: false,
                                                timer: 1500
                                            });
                                            view_item_ver_tb.bootstrapTable('refresh');
                                            refreshAllItemList();
                                        },500);
                                    }else{

                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal('错误信息', "复制失败！", 'error');
                                        },500);
                                    }
                                },
                                error: function(XMLHttpRequest, textStatus, errorThrown) {

                                    setTimeout(function(){
                                        $("#uploadProgress").modal('hide');
                                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                                    },500);
                                }
                            });
                        }
                    });
                }else {
                    swal('提示信息', result.message, 'info');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }

    /**
     * 复制版本
     *
     * @param itemVerId
     * @param itemVerId
     */
    function copyOneItemVer(itemId, itemVerId) {

        // ver_itemId = itemId;
        // ver_itemVerId = itemVerId;

        $.ajax({
            url: ctx + "/aea/item/ver/unpublished/num.do",
            type: 'POST',
            data: {
                'itemId': itemId
            },
            async: false,
            success: function (result) {
                if (result.success) {
                    swal({
                        text: "此操作将复制当前版本数据并生成最新未发布版本,未发布版本下数据可以编辑,您确定执行吗？",
                        type: 'warning',
                        showCancelButton: true,
                        confirmButtonText: '确定',
                        cancelButtonText: '取消'
                    }).then(function (result) {
                        if (result.value) {

                            $("#uploadProgress").modal("show");
                            $('#uploadProgressMsg').html("复制版本数据中,请勿点击,耐心等候...");

                            $.ajax({
                                url: ctx + "/aea/item/ver/copy/version.do",
                                type: 'POST',
                                data:  {
                                    'itemId': itemId,
                                    'itemVerId': itemVerId,
                                },
                                // async: false,
                                success: function (result) {
                                    if (result.success) {

                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal({
                                                type: 'success',
                                                title: '复制成功！',
                                                showConfirmButton: false,
                                                timer: 1500
                                            });
                                            view_item_ver_tb.bootstrapTable('refresh');
                                            refreshAllItemList();
                                        },500);
                                    }else{

                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal('错误信息', "复制失败！", 'error');
                                        },500);
                                    }
                                },
                                error: function(XMLHttpRequest, textStatus, errorThrown) {

                                    setTimeout(function(){
                                        $("#uploadProgress").modal('hide');
                                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                                    },500);
                                }
                            });
                        }
                    });
                }else {
                    swal('提示信息', result.message, 'info');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }

    /**
     * 试运行版本版本
     *
     * @param itemId  事项id
     * @param itemVerId  事项情形版本id
     * @param verStatus  版本状态
     * @param verNum 版本序号
     *
     */
    function testRunItemVer(itemId, itemVerId, verStatus, verNum) {

        // ver_itemId = itemId;
        // ver_itemVerId = itemVerId;

        if(verStatus=='0'){
            swal({
                text: "此操作将启用当前版本并将其他试运行或已发布版本禁用,【还将处理主题关联事项数据】,版本下数据可以编辑,您确定执行吗？",
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then(function (result) {
                if (result.value) {

                    $("#uploadProgress").modal("show");
                    $('#uploadProgressMsg').html("系统处理中,请勿点击,耐心等候...");

                    if(verNum&&verNum.indexOf(".01")>-1){
                        verNum = parseInt(''+verNum) + 1;
                        $.ajax({
                            url: ctx + "/aea/item/ver/testRunOrPublished.do",
                            type: 'POST',
                            data:  {
                                'itemId': itemId,
                                'itemVerId': itemVerId,
                                'verNum': verNum,
                                'type': '2',
                                'oldVerStatus': verStatus
                            },
                            // async: false,
                            success: function (result) {
                                if (result.success) {
                                    setTimeout(function(){
                                        $("#uploadProgress").modal('hide');
                                        swal({
                                            type: 'success',
                                            title: '操作成功！',
                                            showConfirmButton: false,
                                            timer: 1500
                                        });
                                        view_item_ver_tb.bootstrapTable('refresh');
                                        refreshAllItemList();
                                    },500);
                                }else{
                                    setTimeout(function(){
                                        $("#uploadProgress").modal('hide');
                                        swal('错误信息', result.message, 'error');
                                    },500);
                                }
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {
                                setTimeout(function(){
                                    $("#uploadProgress").modal('hide');
                                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                                },500);
                            }
                        });
                    }else{
                        swal('提示信息', "版本序号有问题！", 'info');
                    }
                }
            });
        }else{
            swal('提示信息', "当前版本不是未发布版本！", 'info');
        }
    }

    /**
     * 发布版本
     *
     * @param itemId  事项id
     * @param itemVerId  事项情形版本id
     * @param verStatus  版本状态
     * @param verNum 版本序号
     *
     */
    function publishItemVer(itemId, itemVerId, verStatus, verNum) {

        // ver_itemId = itemId;
        // ver_itemVerId = itemVerId;

        if(verStatus=='0' || verStatus=='2'){

            var title = "此操作将发布当前版本并将其他试运行或已发布版本禁用,发布后版本下数据不可编辑,您确定执行吗？";
            if(verStatus=='0'){
                title = "此操作将发布当前版本并将其他试运行或已发布版本禁用,【还将处理主题关联事项数据】发布后版本下数据不可编辑,您确定执行吗？";
            }
            swal({
                text: title,
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then(function (result) {
                if (result.value) {

                    if(verStatus=='0'){

                        $("#uploadProgress").modal("show");
                        $('#uploadProgressMsg').html("系统处理中,请勿点击,耐心等候...");
                    }
                    if(verNum){
                        if(verNum.indexOf(".01")>-1){
                            verNum = parseInt(''+verNum) + 1;
                        }else{
                            verNum = parseInt(''+verNum);
                        }
                        $.ajax({
                            url: ctx + "/aea/item/ver/testRunOrPublished.do",
                            type: 'POST',
                            data:  {
                                'itemId': itemId,
                                'itemVerId': itemVerId,
                                'verNum': verNum,
                                'type': '1',
                                'oldVerStatus': verStatus
                            },
                            // async: false,
                            success: function (result) {
                                if (result.success) {
                                    if(verStatus=='0'){

                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal({
                                                type: 'success',
                                                title: '操作成功！',
                                                showConfirmButton: false,
                                                timer: 1500
                                            });
                                            view_item_ver_tb.bootstrapTable('refresh');
                                            refreshAllItemList();
                                        },500);

                                    }else{
                                        swal({
                                            type: 'success',
                                            title: '操作成功！',
                                            showConfirmButton: false,
                                            timer: 1500
                                        });
                                        view_item_ver_tb.bootstrapTable('refresh');
                                        refreshAllItemList();
                                    }
                                }else{

                                    if(verStatus=='0'){
                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal('错误信息', result.message, 'error');
                                        },500);
                                    }else{
                                        swal('错误信息', "复制失败！", 'error');
                                    }
                                }
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {

                                if(verStatus=='0'){
                                    setTimeout(function(){
                                        $("#uploadProgress").modal('hide');
                                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                                    },500);
                                }else{
                                    swal('错误信息', XMLHttpRequest.responseText, 'error');
                                }
                            }
                        });
                    }else{
                        swal('提示信息', "版本序号有问题！", 'info');
                    }
                }
            });
        }else{
            swal('提示信息', "当前版本不是未发布或者试运行版本！", 'info');
        }
    }

    /**
     * 查看版本
     *
     * @param itemId
     * @param itemVerId
     *
     */
    function viewItemVer(itemId, itemVerId) {

        // ver_itemId = itemId;
        // ver_itemVerId = itemVerId;

        openFullWindow(ctx + '/rest/mind/mindIndex.do?busiType=item&busiId='+ itemVerId);
    }

    /**
     * 复制创建
     *
     * @param itemId 当前复制事项itemId
     * @param itemVerId 当前复制事项itemVerId
     */
    function copyAndNewItem(itemId, itemVerId){

        var title = "此操作将复制当前事项版本相关数据并创建新的事项,您确定执行吗？";
        swal({
            text: title,
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(function (result) {
            if (result.value) {

                $("#uploadProgress").modal("show");
                $('#uploadProgressMsg').html("复制数据中,请勿点击,耐心等候...");

                $.ajax({
                    url: ctx + "/aea/item/ver/copyAndCreateNew.do",
                    type: 'POST',
                    data:  {
                        'itemId': itemId,
                        'itemVerId': itemVerId,
                    },
                    success: function (result) {
                        if (result.success) {

                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal({
                                    type: 'success',
                                    title: '操作成功！',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                                refreshAllItemList();
                            },500);
                        }else{

                            setTimeout(function(){
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', result.message, 'error');
                            },500);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {

                        setTimeout(function(){
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', XMLHttpRequest.responseText, 'error');
                        },500);
                    }
                });
            }
        });
    }

    // 操作指南
    function openOperaInfo(){

        $('#item_ver_opera_modal').modal('show');
    }
</script>