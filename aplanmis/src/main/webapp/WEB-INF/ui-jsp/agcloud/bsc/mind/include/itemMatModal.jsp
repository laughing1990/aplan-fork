<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">

    .row{

        margin-left: 0px;
        margin-right: 0px;
    }

    label {

        text-align: right;
    }

    .custom-file .custom-file-label{

        border-color:#d9d9d9 !important;
        font-weight: normal;
        color:#a4a7b1 !important;
    }

    .custom-file-label::after{
        content:"浏览" !important;
    }

    .modal-content {
        margin-top:10%;
    }

    .form-group label{
        display: block;
        float: left;
        position: relative;
    }
    .form-group input[type="file"]{
        position: absolute;
        width: 10%;
        opacity: 0;
    }
    .form-group .custorm-style{
        display: block;
        width: 100%;
        height: 38px;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
    }
    .form-group .custorm-style .left-button{
        width: 71px;
        font-size: 13px !important;
        height: 22px;
        line-height: 13px;
        float: left;
        border:1px solid #b1b1b1;
        background: linear-gradient(to bottom, #fff, #ccc);
        color: #444;
        margin-top: 0.9%;
        margin-left: 1%;
    }
    .form-group .custorm-style .right-text{
        width: 80%;
        height: 99%;
        line-height: 2.7em;
        display: block;
        overflow: hidden;
    }

</style>

<!--  ！！！阶段和事项共用的！！！ 分情形 新增/编辑材料 -->
<div id="item_mat_add" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="item_mat_add_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="item_mat_add_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="item_mat_add_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="item_mat_add_scroll" style="height: 480px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="matId" value=""/>
                        <input type="hidden" name="isOwner" value=""/>
                        <input type="hidden" name="isInput" value=""/>
                        <input type="hidden" name="isActive" value=""/>
                        <input type="hidden" name="isGlobalShare" value="" />
                        <input type="hidden" name="fileType" value=""/>
                        <input type="hidden" name="inoutId" value=""/>
                        <input type="hidden" name="itemVerId" value=""/>
                        <input type="hidden" name="stateVerId" value=""/>
                        <input type="hidden" name="isStateIn" value=""/>

                        <input type="hidden" name="stageId" value=""/>
                        <input type="hidden" name="parStateId" value=""/>
                        <input type="hidden" name="itemStateId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">材料类别<span style="color:red">*</span>:</label>
                            <div class="col-10 input-group" style="padding-right: 8px;padding-left: 8px;">
                                <input type="hidden" name="matTypeId" value=""/>
                                <input type="text" class="form-control m-input"
                                       name="matTypeName" readonly placeholder="请选择材料类别..." >
                                <div class="input-group-append">
                                    <span class="input-group-text open-mat-type">
                                        <i class="la la-tag"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料名称<span style="color:red">*</span>:</label>
                            <div class="col-10">
                                <input class="form-control m-input" type="text" value=""
                                       name="matName" placeholder="请输入材料名称...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料编号<span style="color:red">*</span>:</label>
                            <div class="col-10">
                                <input class="form-control m-input" type="text" value=""
                                       name="matCode" placeholder="请输入材料编号...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">是否通用材料<font color="red">*</font>:</label>
                            <div class="col-2">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="1" onclick="return false;">是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isCommon" value="0" onclick="return false;">否<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label">是否支持容缺<font color="red">*</font>:</label>
                            <div class="col-2">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="zcqy" value="1">是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="zcqy" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label" style="padding-top: 8px;">是否批文批复<font color="red">*</font>:</label>
                            <div class="col-2">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isOfficialDoc" value="1">是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isOfficialDoc" value="0" checked>否<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料来源:</label>
                            <input type="hidden" id="matFrom" name="matFrom" value=""/>
                            <div class="col-10">
                                <div class="m-checkbox-inline">
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom1" value="1" onclick="checkboxOnclick(this)">行政机关<span for="matFrom1"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom2" value="2" onclick="checkboxOnclick(this)">企事业单位<span for="matFrom2"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom3" value="3" onclick="checkboxOnclick(this)">社会组织<span for="matFrom3"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom4" value="4" onclick="checkboxOnclick(this)">申请人<span for="matFrom4"></span>
                                    </label>
                                    <label class="m-checkbox">
                                        <input type="checkbox" name="matFromCb" id="matFrom5" value="5" onclick="checkboxOnclick(this)">中介服务<span for="matFrom5"></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">材料来源部门:</label>
                            <div class="col-10">
                                <input id="matFromDept" class="form-control m-input" name="matFromDept"
                                       placeholder="请输入材料来源部门..." />
                            </div>
                        </div>

                        <input type="text" id="templateDoc" name="templateDoc" class="hide"/>
                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">模板文档:</label>
                            <div class="col-7">
                                <input id="templateDocFile" type="file" class="form-control m-input"
                                       name="templateDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                     <button class="left-button">选择文件</button>
                                     <span class="right-text" id="rightText1">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="templateDocDiv" class="col-3">
                                <button id="templateDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('0');">查看附件</button>
                            </div>
                        </div>

                        <input type="text" id="sampleDoc" name="sampleDoc" class="hide"/>
                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">样本文档:</label>
                            <div class="col-7">
                                <input type="file"  id="sampleDocFile" class="form-control m-input"
                                       name="sampleDocFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                    <button class="left-button">选择文件</button>
                                    <span class="right-text" id="rightText2">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="sampleDocDiv" class="col-3">
                                <button id="sampleDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('1');">查看附件</button>
                            </div>
                        </div>

                        <input type="text" id="reviewSampleDoc" name="reviewSampleDoc" class="hide"/>
                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label">审查样本:</label>
                            <div class="col-7">
                                <input id="reviewSampleDocFile" type="file" class="form-control m-input"
                                       name="reviewSampleDocFile" multiple="multiple" accept="image/*" onchange="uploadFileChange(this);" />
                                <span class="custorm-style">
                                     <button class="left-button">选择文件</button>
                                     <span class="right-text" id="rightText3">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="reviewSampleDocDiv" class="col-3">
                                <button id="reviewSampleDocButton" type="button" class="btn btn-info"
                                        onclick="showMatAtt('2');">查看附件</button>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">审查要点<font color="red">*</font>:</label>
                            <div class="col-10">
                            <textarea name="reviewKeyPoints" class="form-control m-input"
                                      placeholder="请输入审查要点..." rows="4"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">审查依据:</label>
                            <div class="col-10">
                            <textarea name="reviewBasis" class="form-control m-input"
                                      placeholder="请输入备注..." rows="4"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">纸质是否必需:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="paperIsRequire" value="1">必须<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="paperIsRequire" value="0">非必须<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;">电子是否必需:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="attIsRequire" value="1" >必须<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="attIsRequire" value="0">非必须<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">纸质材料类型:</label>
                            <div class="col-10">
                                <select name="paperMatType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">A3</option>
                                    <option value="2">A4</option>
                                    <option value="3">A5</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">原件验收:</label>
                            <div class="col-4">
                                <select name="duePaperType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">验</option>
                                    <option value="2">收</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;">原件数:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="number" value="1"
                                       name="duePaperCount" placeholder="请输入原件数...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">复印件验收类型:</label>
                            <div class="col-4">
                                <select name="dueCopyType" class="form-control m-input">
                                    <option value="">请选择</option>
                                    <option value="0">无</option>
                                    <option value="1">验</option>
                                    <option value="2">收</option>
                                </select>
                            </div>

                            <label class="col-2 col-form-label" style="text-align: right;">复印件数:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="number" value="1"
                                       name="dueCopyCount" placeholder="请输入复印件数...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-10">
                            <textarea name="matMemo" id="matMemo" rows="4"
                                      class="form-control m-input" placeholder="请输入备注..."></textarea>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" id="mat_save" class="btn btn-info">保存</button>
                    <%--<button type="button" id="mat_return" class="btn btn-secondary" style="display: none;"--%>
                            <%--onclick="matReturn();">返回</button>--%>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="itemMatAttModal.jsp" %>

<script type="text/javascript">

    $(function () {

        $(".left-button").click(function(e) {
            e.preventDefault();
        });

        $('#show_mat_att_tb_scroll').niceScroll({

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

    var MAT_URL_PREFIX = ctx + '/aea/item/mat/';
    var matAttQueryParams = [];

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

    function checkboxOnclick(checkbox){

        obj = document.getElementsByName("matFromCb");
        check_val = [];
        for(k in obj){
            if(obj[k].checked)
                check_val.push(obj[k].value);
        }
        var cb = check_val.join(',');
        $('#matFrom').val(cb);
    }

    // 查询事件
    function searchMatSto() {

        var params = $('#search_mat_sto_form').serializeArray();
        matAttQueryParams = [];
        if (params != "") {
            $.each(params, function() {
                matAttQueryParams.push({name: this.name, value: this.value});
            });
        }
        $("#customTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
    }

    function refreshMatSto(){

        $('#search_mat_sto_form')[0].reset();
        searchMatSto();
    }


    // 清空查询
    function clearSearchMatSto() {

        $('#search_mat_sto_form')[0].reset();
        searchMatSto();
    }


    // 列表查询参数处理
    function matStoParam (params) {

        var pageNum = (params.offset / params.limit) + 1;
        var pagination = {
            page: pageNum,
            pages: pageNum,
            perpage: params.limit
        };
        var sort = {
            field: params.sort,
            sort: params.order
        };
        var queryParam = {
            pagination: pagination,
            sort: sort
        };
        //组装查询参数
        var buildParam = {};
        if (matAttQueryParams) {
            for (var i = 0; i < matAttQueryParams.length; i++) {
                buildParam[matAttQueryParams[i].name] = matAttQueryParams[i].value;
            }
            queryParam = $.extend(queryParam, buildParam);
        }
        return queryParam;
    }

    function matStotResponseData(res) {

        return res;
    }


    function matAttTbParam(params){

        // var pageNum = (params.offset / params.limit) + 1;
        // var pagination = {
        //     page: pageNum,
        //     pages: pageNum,
        //     perpage: params.limit
        // };
        // var sort = {
        //     field: params.sort,
        //     sort: params.order
        // };
        // var queryParam = {
        //     pagination: pagination,
        //     sort: sort
        // };
        var pageNum = (params.offset / params.limit) + 1;
        var queryParam = {
            pageNum: pageNum,
            pageSize: params.limit,
            sort: params.sort,
            order: params.order,
        };
        //组装查询参数
        var buildParam = {};
        if (matAttQueryParams) {
            for (var i = 0; i < matAttQueryParams.length; i++) {
                buildParam[matAttQueryParams[i].name] = matAttQueryParams[i].value;
            }
            queryParam = $.extend(queryParam, buildParam);
        }
        return queryParam;
    }

    function matAttTbResponseData(res) {
        return res;
    }

    function searchMatSto() {

        var params = $('#search_mat_sto_form').serializeArray();
        matAttQueryParams = [];
        if (params != "") {
            $.each(params, function() {
                matAttQueryParams.push({name: this.name, value: this.value});
            });
        }
        $("#customTable").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#customTable").bootstrapTable('refresh');       //无参数刷新
    }

    function searchMatAttSto(type) {

        var matId = $('#item_mat_add_form input[name="matId"]').val();
        var pkName = "";
        if(type=='0'){
            pkName = "TEMPLATE_DOC";
        }else if(type=='1'){
            pkName = "SAMPLE_DOC";
        }else if(type=='2'){
            pkName = "REVIEW_SAMPLE_DOC";
        }
        matAttQueryParams = [];
        matAttQueryParams.push({name: "tableName",value:"AEA_ITEM_MAT"});
        matAttQueryParams.push({name: "pkName",value: pkName});
        matAttQueryParams.push({name: "recordId",value: matId});
        $("#show_mat_att_tb").bootstrapTable('selectPage',1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
        $("#show_mat_att_tb").bootstrapTable('refresh');       //无参数刷新
    }

    var showMatType = null;
    function showMatAtt(type){

        $('#show_mat_att_modal').modal('show');
        showMatType = type;
        searchMatAttSto(type);
    }


    function attOperFormatter(value, row, index){

        if(row.attType!='KP'){
            var deleteBtn = '<a href="javascript:deleteMatAttrByDetailId(\'' + row.detailId + '\')" ' +
                'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
                'title="删除"><i class="la la-trash"></i>' +
                '</a>';
            return deleteBtn;
        }
    }


    function viewAttNameFormatter(value, row, index){

        // 图片
        if(row.attFormat=="jpg"||row.attFormat=="png"||row.attFormat=="jpeg"||row.attFormat=="jpe"){

            var url = "";
            if(row.attType=='KP'){
                url = ctx + '/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
            }else{
                url = MAT_URL_PREFIX + 'downloadGlobalMatDoc.do?detailId='+ row.detailId;
            }
            // return '<a href="#" onclick="showFile(\''+ url +'\')">'+ row.attName +'</a>';
            return '<a href="#" onclick="showImgFile(\''+ row.detailId +'\')">'+ row.attName +'</a>';

        }else{ // 文件

            var url = "";
            if(row.attType=='KP'){

                url = ctx +'/rest/item/api/kpdownloadFile.do?detailId='+ row.detailId;
            }else{

                url = MAT_URL_PREFIX + 'downloadGlobalMatDoc.do?detailId='+ row.detailId;
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

        window.open(url,"展示图片");
    }

    function showFile(ulr){

        window.open(MAT_URL_PREFIX + 'showFile.do?ulr='+ encodeURI(ulr),"展示图片");
    }


    function showImgFile(detailId){

        window.open(MAT_URL_PREFIX + 'showFile.do?detailId='+ detailId,"展示图片");
    }


    function deleteMatAttrByDetailId(id){

        if(id){
            var matId = $('#item_mat_add_form input[name="matId"]').val();
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
                        url: MAT_URL_PREFIX+'deleteGlobalMatDoc.do',
                        dataType: 'json',
                        data: {'detailId': id, 'type': showMatType,'matId': matId},
                        success: function (result) {
                            if (result.success) {
                                $.ajax({
                                    url: ctx + '/aea/item/mat/getAeaItemMat.do',
                                    type: 'post',
                                    data: {'id': matId},
                                    async: false,
                                    success: function (data) {
                                        if (data) {

                                            if (data.templateDocCount && data.templateDocCount != 0) {

                                                $('#templateDocDiv').show();
                                                $('#templateDocButton').html(data.templateDocCount + "个附件");
                                            } else {
                                                $('#templateDocDiv').hide();
                                            }

                                            if (data.sampleDocCount && data.sampleDocCount != 0) {

                                                $('#sampleDocDiv').show();
                                                $('#sampleDocButton').html(data.sampleDocCount + "个附件");
                                            } else {
                                                $('#sampleDocDiv').hide();
                                            }

                                            if (data.reviewSampleDocCount && data.reviewSampleDocCount != 0) {

                                                $('#reviewSampleDocDiv').show();
                                                $('#reviewSampleDocButton').html(data.reviewSampleDocCount + "个附件");
                                            } else {
                                                $('#reviewSampleDocDiv').hide();
                                            }
                                        }
                                    }
                                });
                                // 列表数据重新加载
                                searchMatAttSto(showMatType);
                                // loadGlobalMatData(matId);
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
    }

</script>