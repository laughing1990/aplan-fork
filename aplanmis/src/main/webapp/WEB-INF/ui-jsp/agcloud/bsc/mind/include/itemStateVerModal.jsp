<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0;
        margin-right: 0;
    }
    .base tbody tr td label {
        display: inline-block;
    }
    .stateStatus {
        width: 60px;
        height: 30px;
        color: white;
        text-align: center;
        border-radius: 5px;
        display: inline-block;
    }
    .status-unpublish{
        background-color: #c4c5d6;
    }
    .status-testrun{
        background-color: #ffc107;
    }
    .status-publish{
        background-color: #28a745;
    }
    .status-deprecated{
        background-color: #c82333;
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

<div id="itemStateVerModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="itemStateVerModalTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 750px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="itemStateVerModalTitle">情形版本</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div class="m-form m-form--label-align-right m--margin-bottom-5">
                    <div class="row" style="margin: 0px;">
                        <div class="col-md-6"style="text-align: left;">
                            <button type="button" class="btn btn-info" onclick="loadStateVerTable();">刷新</button>&nbsp;
                            <button type="button" class="btn btn-secondary" onclick="openOperaInfo();">
                                <span><i class="flaticon-info"></i>&nbsp;<span>帮助</span></span>
                            </button>
                        </div>
                        <div class="col-md-6" style="text-align: right;padding-right: 15px;">

                        </div>
                    </div>
                </div>
                <div style="margin: 10px 5px 0px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                <div id="itemStateVerScroll" style="height: 450px; overflow-y:auto;overflow-x:auto;">
                    <div class="base" style="padding: 0px 5px;">
                        <table id="itemStateVerTable"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function(){

        $("#itemStateVerScroll").niceScroll({

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

    var $itemStateVerModal = $("#itemStateVerModal");
    var $itemStateVerTable = $("#itemStateVerTable");
    var itemstateVerBootstrapTable;

    function loadStateVerTable() {

        if (itemstateVerBootstrapTable != null) {
            itemstateVerBootstrapTable.bootstrapTable('destroy');
        }
        itemstateVerBootstrapTable = $itemStateVerTable.bootstrapTable({
            url: ctx + '/state/ver/histories.do',
            method: 'get',
            queryParams: {
                'itemVerId': currentBusiId
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
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
            },
            columns: [
                {
                    filed: "itemStateVerId",
                    visible: false
                }, {
                    field: "itemId",
                    visible: false
                }, {
                    field: "itemVerId",
                    visible: false
                }, {
                    field: "verNum",
                    title: "版本号",
                    align: "left",
                    width: 150
                },{
                    field: "verStatus",
                    title: "发布状态",
                    align: "left",
                    width: 150,
                    formatter: function(value, row) {

                        var curVer = "<span class='myicon-tick-checked'></span>";
                        if (row.verStatus == '0') { // 未发布
                            return "未发布";
                        }else if (row.verStatus == '1') { // 正式发布
                            return "已发布&nbsp;"+ curVer;
                        }else if (row.verStatus == '2') { // 试运行
                            return "试运行&nbsp;"+ curVer;
                        }else if(row.verStatus == '3') { // 已过时
                           return "已过时";
                        }
                    }
                }, {
                    field: '_operate',
                    title: '操作',
                    sortable: false,
                    width: 250,
                    align: "left",
                    formatter: function (value, row) {

                        var copyBtn = '<button type="button" class="btn btn-info btn-sm" onclick="copyStateVer(\'' + row.itemVerId + '\', \'' + row.itemStateVerId + '\')"><span><i class="glyphicon glyphicon-file"></i>&nbsp;<span>复制</span></span></button>&nbsp;&nbsp;';
                        var testRunBtn = ''; // 试运行
                        var publishBtn = ''; // 发布
                        if (row.verStatus == '0') {

                            testRunBtn = '<button type="button" class="btn btn-info btn-sm" onclick="testRunStateVer(\'' + row.itemStateVerId + '\', \'' + row.verStatus + '\', \'' + row.verNum + '\')"><span><i class="glyphicon glyphicon-ok"></i>&nbsp;<span>试运行</span></span></button>&nbsp;&nbsp;';
                            publishBtn = '<button type="button" class="btn btn-info btn-sm" onclick="publishStateVer(\'' + row.itemStateVerId + '\', \'' + row.verStatus + '\', \'' + row.verNum + '\')"><span><i class="glyphicon glyphicon-ok"></i>&nbsp;<span>发布</span></span></button>&nbsp;&nbsp;';
                        }
                        if (row.verStatus == '2') {
                            publishBtn = '<button type="button" class="btn btn-info btn-sm" onclick="publishStateVer(\'' + row.itemStateVerId + '\', \'' + row.verStatus + '\', \'' + row.verNum + '\')"><span><i class="glyphicon glyphicon-ok"></i>&nbsp;<span>发布</span></span></button>&nbsp;&nbsp;';
                        }
                        var viewBtn = '<button type="button" class="btn btn-info btn-sm" onclick="viewStateVer(\'' + row.itemStateVerId + '\')"><span><i class="la la-search"></i>&nbsp;<span>查看</span></span></button>&nbsp;&nbsp;';

                        if (itemVerIsEditable) {
                            return testRunBtn + publishBtn + copyBtn + viewBtn;
                        } else {
                            return viewBtn;
                        }
                    }
                }
            ],
            onLoadSuccess: function (data) {

                var tableId = document.getElementById("itemStateVerTable");
                $(data).each(function (index, item) {
                    if (item.itemStateVerId == currentStateVerId) {
                        $(item).addClass('selected');
                        tableId.rows[index + 1].setAttribute("style","background: #ece6e6;");
                    } else {
                        $(item).removeClass('selected');
                    }
                });
            }
        });
    }

    /**
     * 复制版本
     *
     * @param itemVerId
     *
     */
    function copyItemStateVer(){

        $.ajax({
            url: ctx + "/rest/mind/count/unpublished/state/num.do",
            type: 'POST',
            data: {
                'itemVerId': currentBusiId
            },
            async: false,
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
                                url: ctx + "/rest/mind/copy/max/state/version",
                                type: 'POST',
                                data:  {
                                    'itemVerId': currentBusiId
                                },
                                async: false,
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
                                            itemstateVerBootstrapTable.bootstrapTable('refresh');
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
     * @param itemStateVerId
     */
    function copyStateVer(itemVerId, itemStateVerId) {

        $.ajax({
            url: ctx + "/rest/mind/count/unpublished/state/num.do",
            type: 'POST',
            data: {
                'itemVerId': currentBusiId
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
                                url: ctx + "/rest/mind/copy/state/version",
                                type: 'POST',
                                data:  {
                                    'itemVerId': itemVerId,
                                    'stateVerId': itemStateVerId
                                },
                                async: false,
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
                                            itemstateVerBootstrapTable.bootstrapTable('refresh');
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
     * @param itemStateVerId  情形版本id
     * @param stateVerStatus  版本状态
     * @param verNum 版本序号
     *
     */
    function testRunStateVer(itemStateVerId, stateVerStatus, verNum) {

        if(stateVerStatus=='0'){
            swal({
                text: "此操作将更改当前版本状态为试运行并启用,版本下数据可以编辑,您确定执行吗？",
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then(function (result) {
                if (result.value) {
                    if(verNum&&verNum.indexOf(".01")>-1){
                        verNum = parseInt(verNum.substring(1)) + 1;
                        $.ajax({
                            url: ctx + "/state/ver/testRun.do",
                            type: 'POST',
                            data:  {
                                'stateVerId': itemStateVerId,
                                'verNum': verNum
                            },
                            async: false,
                            success: function (result) {
                                if (result.success) {
                                    agcloud.ui.metronic.showSuccessTip('操作成功！', 1500);
                                    itemstateVerBootstrapTable.bootstrapTable('refresh');
                                }else{
                                    swal('错误信息', "复制失败！", 'error');
                                }
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {
                                swal('错误信息', XMLHttpRequest.responseText, 'error');
                            }
                        });
                    }else{
                        agcloud.ui.metronic.showSwal({type: 'info', message: "版本序号有问题!"});
                    }
                }
            });
        }else{
            agcloud.ui.metronic.showSwal({type: 'error', message: "当前版本不是未发布版本!"});
        }
    }

    /**
     * 发布版本
     *
     * @param itemId  事项id
     * @param itemVerId  事项情形版本id
     * @param itemStateVerId  情形版本id
     * @param stateVerStatus  版本状态
     * @param verNum 版本序号
     *
     */
    function publishStateVer(itemStateVerId, stateVerStatus, verNum) {

        if(stateVerStatus=='0'||stateVerStatus=='2'){
            swal({
                text: "此操作将更改当前版本状态为已发布并将其他试运行或发布版本更改为已过时,当前版本下数据不可编辑,您确定执行吗？",
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then(function (result) {
                if (result.value) {
                    if(verNum){
                        if(verNum.indexOf(".01")>-1){
                            verNum = parseInt(verNum.substring(1)) + 1;
                        }else{
                            verNum = parseInt(verNum.substring(1));
                        }
                        $.ajax({
                            url: ctx + "/state/ver/publish.do",
                            type: 'POST',
                            data:  {
                                'stateVerId': itemStateVerId,
                                'verNum': verNum
                            },
                            async: false,
                            success: function (result) {
                                if (result.success) {
                                    if(currentStateVerId==itemStateVerId){
                                        location.reload();
                                    }else{
                                        agcloud.ui.metronic.showSuccessTip('操作成功！', 1500);
                                        itemstateVerBootstrapTable.bootstrapTable('refresh');
                                    }
                                }else{
                                    swal('错误信息', "复制失败！", 'error');
                                }
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {
                                swal('错误信息', XMLHttpRequest.responseText, 'error');
                            }
                        });
                    }else{
                        agcloud.ui.metronic.showSwal({type: 'info', message: "版本序号有问题!"});
                    }
                }
            });
        }else{
            agcloud.ui.metronic.showSwal({type: 'error', message: "当前版本不是未发布或者试运行版本!"});
        }
    }

    /**
     * 查看版本
     *
     * @param itemId
     * @param itemVerId
     * @param itemStateVerId
     */
    function viewStateVer(itemStateVerId) {

        location.href = ctx + '/rest/mind/mindIndex.do?busiType=' + currentBusiType + '&busiId=' + currentBusiId + '&stateVerId=' + itemStateVerId;
    }

    // 操作指南
    function openOperaInfo(){

       $('#item_state_ver_opera_modal').modal('show');
    }

    function isShowCopyMaxVerBtn(){

        if(itemVerIsEditable){
            $('#copy_item_state_ver_btn').show();
        }else{
            $('#copy_item_state_ver_btn').hide();
        }
    }
</script>