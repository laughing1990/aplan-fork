var contractTable;

$(function(){
    contractTable = defaultBootstrap("contractTableId", [{
        field: '#',
        title: "序号",
        sortable: false, // 禁用排序
        formatter: function (value,row,index) {
            return index+1;
        }
    },{
        field: "projName",
        title: "项目名称",
        textAlign: 'center',
        width: 500,
        textAlign: 'left',
        sortable: false
    }, {
        field: "contractName",
        title: "合同名称",
        textAlign: 'center',
        width: 500,
        textAlign: 'left',
        sortable: false
    }, {
        field: "contractId",
        visible: false,
        sortable: false
    }, {
        field: "contractCode",
        title: "合同编码",
        textAlign: 'center',
        width: 500,
        textAlign: 'left',
        sortable: false
    }, {
        field: "signTime",
        title: "签订时间",
        textAlign: 'center',
        width: 100,
        textAlign: 'left',
        sortable: false,
        formatter: function (value,row,index) {
            return getDateText(value);
        }
    },  {
        field: "operateAction",
        title: "审核类型",
        textAlign: 'center',
        width: 100,
        textAlign: 'left',
        sortable: false,
        formatter: function (value, row, index) {
            return getOperateAction(row);
        }
    },{
        field: "auditFlag",
        title: "审核状态",
        textAlign: 'center',
        width: 50,
        textAlign: 'left',
        sortable: false,
        formatter: function (value, row, index) {
            if('1'==value){
                return '审核通过';
            }
            if('2'==value){
                return '审核失败';
            }
            if('3'==value){
                return '审核中';
            }

            return '';
        }
    },{
        field: "auditTime",
        title: "审核时间",
        textAlign: 'center',
        width: 100,
        textAlign: 'left',
        sortable: false,
        formatter: function (value,row,index) {

            return getDateText(value);
        }
    },{
        field: '_operate', // 数据字段
        title: '操作', // 页面字段显示
        sortable: false, // 禁用排序
        width: 50,  // 宽度,单位: px
        textAlign: 'center', // 字段列标题和数据排列方式
        formatter: function (value, row, index) {
            var viewBtn;
            if('3'==row.auditFlag){
                viewBtn = '<a href="javascript:showContract(\'' + row.contractId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="审核"><i class="la la-edit"></i></a>';
            }else{
                viewBtn = '<a href="javascript:showContract(\'' + row.contractId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-search"></i></a>';
            }
            return viewBtn;

            return '';
        }
    }], ctx + '/supermarket/contract/listAuditAeaImContractByPage1.do');

    contractTable.setRowId("contractId").setSearchBtn("searchContractBtn","keyword").init();
});

function changeContractStatus(selectEle) {
    var queryParams = {};

    if(selectEle.value==''){
        queryParams ["auditFlag"] = null;
    }else{
        queryParams ["auditFlag"] = selectEle.value;
    }

    contractTable.setExtendQueryParams(queryParams);
}

function getOperateAction(data) {
    if(data.aeaImPurchaseinst && data.aeaImPurchaseinst.operateAction){

        var value = data.aeaImPurchaseinst.operateAction;
        if('3'==value){
            return '新增合同';
        }
        if('4'==value){
            return '修改合同';
        }
        if('5'==value){
            return '延长结束服务时间';
        }
    }

    return '新增合同';
}


function showContract(contractId) {

    $.ajax({
        url: ctx+'/supermarket/contract/getAeaImContractByContractId.do',
        type: 'POST',
        data:{
            'contractId': contractId
        },
        success: function (result){
            if(result.success){
                $('#contract_form')[0].reset();
                $('#attDiv').html("");
                $('#applyAttDiv').html("");
                $("#contract_modal").modal("show");
                loadFormData(true,'#contract_form',result.content);

                $("#serviceStartTime").val(toDateString(result.content.serviceStartTime));
                $("#serviceEndTime").val(toDateString(result.content.serviceEndTime));
                $("#postponeServiceEndTime").val(toDateString(result.content.postponeServiceEndTime));

                $("#auditResult").val(getAuditResult(result.content.auditFlag));

                $("#operateAction").val(getOperateAction(result.content));

                var postpone = false;
                if(result.content.aeaImPurchaseinst && result.content.aeaImPurchaseinst.operateAction){
                    if("5"==result.content.aeaImPurchaseinst.operateAction){
                        postpone = true;
                    }

                }

                var edit = false;

                if('3'==result.content.auditFlag){
                    edit = true;
                }

                if(postpone){
                    $("#operateDescribeDiv").show();
                    $("#postponeServiceEndTimeDiv").show();
                    $("#operateDescribe").val(result.content.aeaImPurchaseinst.operateDescribe);

                    drawAtt("applyAttDiv",result.content.aeaImPurchaseinst.bscAttForms,"延期说明附件");

                    if(edit){
                        $("#auditOpinion").val();
                    }
                }else{
                    $("#operateDescribeDiv").hide();
                    $("#postponeServiceEndTimeDiv").hide();
                }

                if(edit){
                    $("#auditResultDiv").hide();
                    $("#saveBtn").show();
                    $("#auditFlagSelect").show();
                    $("#auditOpinion").removeAttr("readonly");
                }else{
                    $("#auditResultDiv").show();
                    $("#saveBtn").hide();
                    $("#auditFlagSelect").hide();
                    $("#auditOpinion").attr("readonly","true");
                }

                drawAtt("attDiv",result.content.bscAttForms,"附件");

            }else{
                swal('错误信息', result.message, 'error');
            }
        },
        error:function(){
            swal('错误信息', '查询失败！', 'error');
        }
    });
}

function drawAtt(attDivId,bscAttForms,labelName) {
    if(bscAttForms && bscAttForms.length>0){

        var divEl =  '<label class="col-lg-2 col-form-label" style="text-align: right;">'+labelName+'：</label>' +
            '                    <div class="col-lg-10">';

        for(var i=0;i<bscAttForms.length;i++){
            var att = bscAttForms[i];
            divEl = divEl + '<a style="margin-left:20px;" href="'+ctx + '/supermarket/contract/downloadFile.do?detailId='+att.detailId+'">'+att.attName+'</a></br>';
        }

        divEl = divEl + '</div>';
        $('#'+attDivId).append(divEl);
    }
}

function toDateString(value) {

    if(value && value!='') {
        var d = new Date(value);
        var date = (d.getFullYear()) + "-" +
            (d.getMonth() + 1) + "-" +
            (d.getDate());

        return date;
    }
    return '';
}

function getAuditResult(value) {
    if('1'==value){
        return '审核通过';
    }
    if('2'==value){
        return '审核失败';
    }
    if('3'==value){
        return '审核中';
    }

    return '';
}

function closeContractModal() {
    $("#contract_modal").modal("hide");
}

function auditContract() {

    $.ajax({
        url: ctx+'/supermarket/contract/auditContract.do',
        type: 'POST',
        data:{
            'contractId': $("#contractId").val(),
            'auditFlag': $("#auditFlag").val(),
            'auditOpinion': $("#auditOpinion").val()
        },
        success: function (result){
            if(result.success){
                $("#contract_modal").modal("hide");
                swal({
                    title: '提示信息',
                    text: '审核成功!',
                    type: 'success',
                    timer: 1000,
                    showConfirmButton: false
                });
                contractTable.refresh();

            }else{
                swal('错误信息', result.message, 'error');
            }
        },
        error:function(){
            swal('错误信息', '提交失败！', 'error');
        }
    });
    
}



function getDateText(value) {
    if(value) {
        var d = new Date(value);
        var date = (d.getFullYear()) + "-" +
            (d.getMonth() + 1) + "-" +
            (d.getDate());

        return date;
    }
    return value;
}