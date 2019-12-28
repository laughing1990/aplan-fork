<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
    .row{
        margin-left: 0px;
        margin-right: 0px;
    }
</style>

<div id="view_stage_state_mat_list_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="view_stage_state_mat_list_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 10px;height: 45px;">
                <h5 class="modal-title" id="view_stage_state_mat_list_modal_title">材料列表</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px; padding-top: 5px;">
                <ul class="nav nav-tabs" role="tablist" style="margin-bottom: 5px;border-bottom: 0px;">

                    <li id="stageStateMatLi" class="nav-item">
                        <a id="stageStateMatTb" class="nav-link" data-toggle="tab" href="#m_tabs_state_mat" onclick="stageStateMatTbClickFn();">
                            <i class="la la-gear"></i>
                            情形材料
                        </a>
                    </li>

                    <li id="stageCommonMatLi" class="nav-item">
                        <a id="stageCommonMatTb" class="nav-link" data-toggle="tab" href="#m_tabs_common_mat" onclick="stageCommonMatTbClickFn();">
                            <i class="la la-gear"></i>
                            通用材料
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane" id="m_tabs_state_mat" role="tabpanel" style="padding-top: 5px;">
                        <jsp:include page="stage_state_mat_list.jsp"></jsp:include>
                    </div>
                    <div class="tab-pane" id="m_tabs_common_mat" role="tabpanel" style="padding-top: 5px;">
                        <jsp:include page="stage_common_mat_list.jsp"></jsp:include>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var itemMatValidator = null;
    var currentIsCommonMat = false;
    var currentIsStateIn = '1';

    $(document).ready(function(){

        $('#stage_state_mat_tab_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#stage_common_mat_tab_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#stage_state_item_list_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#stage_state_form_list_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $('#stage_state_mat_item_list_tb_scroll').niceScroll({

            cursorcolor: "#e2e5ec",//#CC0071 光标颜色
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "4px", //像素光标的宽度
            cursorborder: "0", //   游标边框css定义
            cursorborderradius: "2px",//以像素为光标边界半径
            autohidemode: true  //是否隐藏滚动条
        });

        $(".left-button").click(function(e) {
            e.preventDefault();
        });
    });

    function stageStateMatTbClickFn(){

        currentIsCommonMat = false;
        currentIsStateIn = '1';
        if(allStateMatIds){
            allStateMatIds = [];
        }
        clearStageMatListSearch();
    }

    function stageCommonMatTbClickFn(){

        currentIsCommonMat = true;
        currentIsStateIn = '0';
        clearStageCommonMatListSearch();
    }

    function viewStageStateMatList(stateId) {

        stageItemStateId = stateId;
        $("#view_stage_state_mat_list_modal").modal('show');

        // 阶段节点
        if (stateId == null) {

            $("#add_stage_mat_btn").hide();
            $("#choose_stage_mat_btn").hide();
            $('#btn_batch_del_stage_state_mat').hide();

            $('#view_stage_state_mat_list_modal_title').html('通用材料列表');
            $('#stageStateMatLi').hide();
            $('#stageCommonMatLi').show();
            $('#stageCommonMatTb').trigger('click');
            $('#stageCommonMatLi').hide();

        // 情形节点
        } else {

            $("#add_stage_mat_btn").show();
            $("#choose_stage_mat_btn").show();
            $('#btn_batch_del_stage_state_mat').show();

            $('#view_stage_state_mat_list_modal_title').html('情形材料列表');
            $('#stageStateMatLi').show();
            $('#stageCommonMatLi').hide();
            $('#stageStateMatTb').trigger('click');
            $('#stageStateMatLi').hide();

        }

        if(itemMatValidator==null){

            var matForm = $('#item_mat_add_form')[0];
            itemMatValidator = $("#item_mat_add_form").validate({
                // 定义校验规则
                rules: {
                    isGlobalShare:{
                        required: true,
                    },
                    matTypeName:{
                        required: true,
                    },
                    matName: {
                        required: true,
                        maxlength: 500,
                        // remote: {
                        //     url: ctx + '/aea/item/mat/checkMatName.do', //后台处理程序
                        //     type: "post",               //数据发送方式
                        //     dataType: "json",           //接受数据格式
                        //     data: {   //要传递的数据
                        //         matId: function () {
                        //             return $("#item_mat_add_form input[name='matId']").val();
                        //         },
                        //         matName: function () {
                        //             return $("#item_mat_add_form input[name='matName']").val();
                        //         },
                        //         isCommon: function () {
                        //             return $("#item_mat_add_form input[name='isCommon']").val();
                        //         },
                        //     }
                        // }
                    },
                    matCode: {
                        required: true,
                        maxlength: 200,
                        remote: {
                            url: ctx + '/aea/item/mat/checkMatCode.do', //后台处理程序
                            type: "post",               //数据发送方式
                            dataType: "json",           //接受数据格式
                            data: {   //要传递的数据
                                matId: function(){
                                    return $("#item_mat_add_form input[name='matId']").val();
                                },
                                matCode: function() {
                                    return $("#item_mat_add_form input[name='matCode']").val();
                                }
                            }
                        }
                    },
                    reviewKeyPoints:{
                        required: true,
                    }
                },
                messages: {
                    isGlobalShare:{
                        required: '<font color="red">此项必填！</font>',
                    },
                    matTypeName:{
                        required: '<font color="red">此项必填！</font>',
                    },
                    matName: {
                        required: '<font color="red">材料名称必填！</font>',
                        maxlength: "最大长度不能超过500字符!",
                        // remote: '材料名称已存在!'
                    },
                    matCode:{
                        required: '<font color="red">材料编号必填！</font>',
                        maxlength: '最大长度不能超过200字符',
                        remote: '材料编号已存在!'
                    },
                    reviewKeyPoints:{
                        required: '<font color="red">此项必填！</font>',
                    }
                },
                // 提交表单
                submitHandler: function (form) {

                    var itemMatAddModal = $('#item_mat_add');
                    $('input[name="stageId"]', itemMatAddModal).val(currentBusiId);
                    $('input[name="isStateIn"]', itemMatAddModal).val(currentIsStateIn);
                    $('input[name="parStateId"]', itemMatAddModal).val(stageItemStateId);

                    var duePaperCount = $('#item_mat_add_form input[name="duePaperCount"]').val();
                    var dueCopyCount = $('#item_mat_add_form input[name="dueCopyCount"]').val();
                    if(!duePaperCount){
                        $('#item_mat_add_form input[name="duePaperCount"]').val('1');
                    }
                    if(!dueCopyCount){
                        $('#item_mat_add_form input[name="dueCopyCount"]').val('1');
                    }

                    $("#uploadProgress").modal("show");
                    $('#mat_save').hide();
                    $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

                    var formData = new FormData(matForm);
                    var url = ctx + '/stage/saveMatAndParIn.do';
                    $.ajax({
                        type: 'post',
                        url: url,
                        dataType: 'json',
                        data: formData,
                        contentType: false,
                        processData: false,
                        success: function (result) {
                            if (result.success) {

                                setTimeout(function(){
                                    swal({
                                        type: 'success',
                                        title: '保存成功！',
                                        showConfirmButton: false,
                                        timer: 1000
                                    });
                                    // 隐藏模式框
                                    $("#uploadProgress").modal('hide');
                                    $('#mat_save').show();
                                    $('#item_mat_add').modal('hide');
                                    if (currentIsCommonMat) {
                                        clearStageCommonMatListSearch();
                                        refreshMind();
                                    } else {
                                        clearStageMatListSearch();
                                        refreshMind();
                                    }

                                },500);
                            } else {

                                setTimeout(function(){
                                    $('#mat_save').hide();
                                    $("#uploadProgress").modal('hide');
                                    swal('错误信息', result.message, 'error');
                                },500);
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {

                            setTimeout(function(){
                                $('#mat_save').hide();
                                $("#uploadProgress").modal('hide');
                                swal('错误信息', XMLHttpRequest.responseText, 'error');
                            },500);
                        }
                    });
                }
            });
        }
    }

    function hideViewStageMatList() {
        $("#view_stage_state_mat_list_modal").modal('hide');
    }
    
    function addStageMat(isCommonMat,isStateIn) {

        $('#item_mat_add').modal('show');
        $('#item_mat_add_title').html('添加材料');
        $('#item_mat_add_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#item_mat_add_form')[0].reset();
        if(itemMatValidator!=null) {
            $("#item_mat_add_form").validate().resetForm();
        }

        $("#item_mat_add_form input[name='matId']").val('');
        $("#item_mat_add_form input[name='isOwner']").val('1');
        $("#item_mat_add_form input[name='isActive']").val('1');
        $("#item_mat_add_form input[name='isGlobalShare']").val('1');
        $("#item_mat_add_form input[name='fileType']").val('mat');
        $("#item_mat_add_form input[name='duePaperCount']").val("1");
        $("#item_mat_add_form input[name='dueCopyCount']").val("1");
        $("#item_mat_add_form input[name='paperIsRequire'][value='0']").prop("checked", true);
        $("#item_mat_add_form input[name='attIsRequire'][value='0']").prop("checked", true);
        $("#item_mat_add_form input[name='zcqy'][value='1']").prop("checked", true);  // 默认支持容缺
        $("#item_mat_add_form input[name='isOfficialDoc'][value='0']").prop("checked", true);  // 是否为批文批复
        $("#item_mat_add_form input[name='certId']").val("");
        $("#item_mat_add_form input[name='stoFormId']").val("");
        $("#item_mat_add_form input[name='stdmatId']").val("");
        $("#item_mat_add_form input[name='matProp'][value='m']").prop("checked", true);
        handleSelectMatProNew('#item_mat_add_form','m');

        $("#templateDocFile").siblings('.custorm-style').find(".right-text").html("");
        $("#sampleDocFile").siblings('.custorm-style').find(".right-text").html("");
        $("#reviewSampleDocFile").siblings('.custorm-style').find(".right-text").html("");
        $('#templateDocDiv').hide();
        $('#sampleDocDiv').hide();
        $('#reviewSampleDocDiv').hide();

        currentIsCommonMat = isCommonMat;
        currentIsStateIn = isStateIn;

        if(currentIsCommonMat){
            $("#item_mat_add_form input[name='isCommon'][value='1']").prop("checked", true);
        }else{
            $("#item_mat_add_form input[name='isCommon'][value='0']").prop("checked", true);
        }
        getMatCodeByAjax();
    }

    function deleteStageMat(isCommonMat,inId) {

        currentIsCommonMat = isCommonMat;
        var msg = '此操作将删除材料与当前情形的关联,确定要执行吗？';
        if(currentIsCommonMat){
            msg = '此操作将删除材料与当前阶段的关联,确定要执行吗？';
        }
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
                    url: ctx + '/aea/par/in/deleteMatAndParIn.do',
                    type: 'post',
                    data: {'inId': inId},
                    async: false,
                    cache: false,
                    success: function (result) {
                        if (result.success) {
                            agcloud.ui.metronic.showSuccessTip('删除成功！', 1500);
                            if(!currentIsCommonMat){
                                location.reload();
                            }else{
                                clearStageCommonMatListSearch();
                            }
                        }else{
                            agcloud.ui.metronic.showSwal({type: 'error', message: '删除失败!'});
                        }
                    }
                });
            }
        });
    }

    function batchDelStageStateMat(isCommonMat) {

        currentIsCommonMat = isCommonMat;
        var msg = '此操作将删除材料与当前情形的关联,确定要执行吗？';
        if(currentIsCommonMat){
            msg = '此操作将删除材料与当前阶段的关联,确定要执行吗？';
        }

        var rows = null;
        if(isCommonMat){
            var rows = $("#stage_common_mat_list_tb").bootstrapTable('getSelections');
        }else{
            var rows = $("#stage_state_mat_list_tb").bootstrapTable('getSelections');
        }
        if (rows != null && rows.length > 0) {
            var inIds = [];
            for (var i = 0; i < rows.length; i++) {
                var row = rows[i];
                inIds.push(row.inId);
            }
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
                        url: ctx + '/aea/par/in/batchDeleteMatAndParIns.do',
                        type: 'post',
                        data: {'inIds': inIds.toString()},
                        async: false,
                        cache: false,
                        success: function (result) {
                            if (result.success) {
                                agcloud.ui.metronic.showSuccessTip('删除成功！', 1500);
                                if(currentIsCommonMat){
                                    clearStageCommonMatListSearch();
                                    refreshMind();
                                }else{
                                    clearStageMatListSearch();
                                    refreshMind();
                                }
                            }else{
                                agcloud.ui.metronic.showSwal({type: 'error', message: '删除失败!'});
                            }
                        }
                    });
                }
            });
        }
    }

    function editStageMat(isCommonMat,matId,isStateIn) {

        $('#item_mat_add').modal('show');
        $('#item_mat_add_title').html('编辑材料');
        $('#item_mat_add_scroll').animate({scrollTop: 0}, 800);//滚动到顶部
        $('#item_mat_add_form')[0].reset();
        if(itemMatValidator!=null) {
            $("#item_mat_add_form").validate().resetForm();
        }
        $("#item_mat_add_form input[name='matId']").val('');
        $("#item_mat_add_form input[name='fileType']").val('mat');
        $("#item_mat_add_form input[name='certId']").val("");
        $("#item_mat_add_form input[name='stoFormId']").val("");
        $("#item_mat_add_form input[name='stdmatId']").val("");

        $("#templateDocFile").siblings('.custorm-style').find(".right-text").html("");
        $("#sampleDocFile").siblings('.custorm-style').find(".right-text").html("");
        $("#reviewSampleDocFile").siblings('.custorm-style').find(".right-text").html("");
        $('#templateDocDiv').hide();
        $('#sampleDocDiv').hide();
        $('#reviewSampleDocDiv').hide();

        currentIsCommonMat = isCommonMat;
        currentIsStateIn = isStateIn;

        $.ajax({
            url: ctx + '/aea/item/mat/getAeaItemMat.do',
            type: 'post',
            data: {'id': matId},
            async: false,
            success: function (data) {
                if (data) {

                    // 记载表单数据
                    loadFormData(true,'#item_mat_add_form',data);

                    if (data.templateDocCount&&data.templateDocCount!=0) {

                        $('#templateDocDiv').show();
                        $('#templateDocButton').html(data.templateDocCount + "个附件");
                    }else{
                        $('#templateDocDiv').hide();
                    }

                    if (data.sampleDocCount&&data.sampleDocCount!=0) {

                        $('#sampleDocDiv').show();
                        $('#sampleDocButton').html(data.sampleDocCount + "个附件");
                    }else{
                        $('#sampleDocDiv').hide();
                    }

                    if (data.reviewSampleDocCount&&data.reviewSampleDocCount!=0) {

                        $('#reviewSampleDocDiv').show();
                        $('#reviewSampleDocButton').html(data.reviewSampleDocCount + "个附件");
                    }else{
                        $('#reviewSampleDocDiv').hide();
                    }
                    if (data.matFrom!=null){

                        var matFromData = data.matFrom;
                        var matFromDataArray = matFromData.split(",");
                        ckAll = document.getElementsByName("matFromCb");
                        ck1= document.getElementById("matFrom1");
                        ck2= document.getElementById("matFrom2");
                        ck3= document.getElementById("matFrom3");
                        ck4= document.getElementById("matFrom4");
                        ck5= document.getElementById("matFrom5");
                        for(qee in matFromDataArray){
                            if(matFromDataArray[qee]==1){
                                ck1.checked = true;
                            }
                            if(matFromDataArray[qee]==2){
                                ck2.checked = true;
                            }
                            if(matFromDataArray[qee]==3){
                                ck3.checked = true;
                            }
                            if(matFromDataArray[qee]==4){
                                ck4.checked = true;
                            }
                            if(matFromDataArray[qee]==5){
                                ck5.checked = true;
                            }
                        }
                    }
                    if (data.matProp){
                        handleSelectMatProNew('#item_mat_add_form',data.matProp);
                    }
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                swal('错误信息', XMLHttpRequest.responseText, 'error');
            }
        });
    }

    function chooseStageMat(isCommon){

        $('#item_overmat_import').modal('show');
        $('#item_overmat_import_title').html('导入库材料');
        stageItemStoIsCommonFlag = isCommon;
        globalMatSearchClear();
        $("#btn_mat_global_select").unbind("click");
        $("#btn_mat_global_select").click(function () {
            globalMatDialogClose();
            var dataArr=getGlobalMatChoose();
            if(dataArr && dataArr.length>0){
                var ids = [];
                for(var i=0;i<dataArr.length;i++){
                    ids.push(dataArr[i].id);
                }
                var data = {'ids':ids.join(","),'stageId': currentBusiId,'isStateIn':'1','stateId': stageItemStateId};
                if(isCommon) {
                    data = {'ids':ids.join(","),'stageId': currentBusiId,'isStateIn':'0'};
                }
                $.ajax({
                    type: 'post',
                    url: ctx+'/aea/item/mat/saveChooseStageMatAndParIn.do',
                    data: data,
                    async: false,
                    cache: false,
                    success: function (result) {
                        if (result.success) {
                            agcloud.ui.metronic.showSuccessTip('导入成功！', 1500);
                            if(isCommon){
                                clearStageCommonMatListSearch();
                                refreshMind();
                            }else {
                                clearStageMatListSearch();
                                refreshMind();
                            }
                        } else {
                            agcloud.ui.metronic.showSwal({type: 'error', message: '导入失败!'});
                        }
                    }
                });
            }

        })
    }
</script>