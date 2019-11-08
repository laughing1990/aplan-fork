var evaluationTable;

$(function(){
    evaluationTable = defaultBootstrap("evaluationTableId", [{
        field: '#',
        title: "序号",
        formatter: function (value,row,index) {
            return index+1;
        },
        sortable: false,
    },{
        field: "evaluateUnitName",
        title: "评价单位",
        textAlign: 'center',
        width: 500,
        textAlign: 'left',
        sortable: false,
    },{
        field: "projName",
        title: "项目名称",
        textAlign: 'center',
        width: 500,
        textAlign: 'left',
        sortable: false,
    }, {
        field: "serviceEvaluationId",
        visible: false
    },{
        field: "createTime",
        title: "提交时间",
        textAlign: 'center',
        width: 100,
        textAlign: 'left',
        sortable: false,
        formatter: function (value,row,index) {
            return getDateText(value);
        }
    }, {
        field: "auditFlag",
        title: "审核状态",
        textAlign: 'center',
        width: 100,
        textAlign: 'left',
        sortable: false,
        formatter: function (value, row, index) {
            if('1'==value){
                return '审核通过';
            }
            if('0'==value){
                return '审核失败';
            }
            if('2'==value){
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
        width: 100,  // 宽度,单位: px
        textAlign: 'center', // 字段列标题和数据排列方式
        formatter: function (value, row, index) {
            var viewBtn;
            if('2'==row.auditFlag){
                viewBtn = '<a href="javascript:showEvaluation(\'' + row.serviceEvaluationId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="审核"><i class="la la-edit"></i></a>';
            }else{
                viewBtn = '<a href="javascript:showEvaluation(\'' + row.serviceEvaluationId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-search"></i></a>';
            }
            return viewBtn;

            return '';
        }
    }], ctx + '/supermarket/evaluation/listAuditServiceEvaluationByPage.do');

    evaluationTable.setRowId("serviceEvaluationId").setSearchBtn("searchEvaluationBtn","keyword").init();
});

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

function changeEvaluationStatus(selectEle) {
    var queryParams = {};

    if(selectEle.value==''){
        queryParams ["auditFlag"] = null;
    }else{
        queryParams ["auditFlag"] = selectEle.value;
    }

    evaluationTable.setExtendQueryParams(queryParams);
}

function showEvaluation(serviceEvaluationId) {

    $.ajax({
        url: ctx+'/supermarket/evaluation/getAeaImServiceEvaluationByServiceEvaluationId.do',
        type: 'POST',
        data:{
            'serviceEvaluationId': serviceEvaluationId
        },
        success: function (result){
            if(result.success){
                $('#evaluation_form')[0].reset();
                $("#evaluation_modal").modal("show");

                loadAllStar(result.content);
                loadFormData(true,'#evaluation_form',result.content);

                $("#auditResult").val(getAuditResult(result.content.auditFlag));

                var edit = false;

                if('2'==result.content.auditFlag){
                    edit = true;
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

            }else{
                swal('错误信息', result.message, 'error');
            }
        },
        error:function(){
            swal('错误信息', '查询失败！', 'error');
        }
    });
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
    if('0'==value){
        return '审核失败';
    }
    if('2'==value){
        return '审核中';
    }

    return '';
}

function closeEvaluationModal() {
    $("#evaluation_modal").modal("hide");
}

function auditEvaluation() {

    $.ajax({
        url: ctx+'/supermarket/evaluation/auditEvaluation.do',
        type: 'POST',
        data:{
            'serviceEvaluationId': $("#serviceEvaluationId").val(),
            'auditFlag': $("#auditFlag").val(),
            'auditOpinion': $("#auditOpinion").val()
        },
        success: function (result){
            if(result.success){
                $("#evaluation_modal").modal("hide");
                swal({
                    title: '提示信息',
                    text: '审核成功!',
                    type: 'success',
                    timer: 1000,
                    showConfirmButton: false
                });
                evaluationTable.refresh();

            }else{
                swal('错误信息', result.message, 'error');
            }
        },
        error:function(){
            swal('错误信息', '提交失败！', 'error');
        }
    });

}

function clearStar(){
    $(".starUl li").removeClass("fullStar");
    $(".starUl li").removeClass("halfStar");
    $(".starUl li").addClass("emptyStar");
}

function loadAllStar(data) {
    clearStar();
    loadStar('serviceQuality',data.serviceQuality);
    loadStar('servicePrescription',data.servicePrescription);
    loadStar('serviceAttitude',data.serviceAttitude);
    loadStar('serviceCharge',data.serviceCharge);
    loadStar('serviceStandard',data.serviceStandard);
    loadStar('compEvaluation',data.compEvaluation);
}


function loadStar(name,value) {
    var ulId = name+'Star';
    var num = 0.0;
    try{
        num = parseFloat(value);
    }catch (e) {

    }

    if(num<=0){
        num = 0.0;
    }

    if(num>5){
        num = 5.0;
    }

    if(num>0.0){
        var liStar =  $("#"+ulId).find("li");
        var index = parseInt(num+'');
        for(var i=0;i<index;i++){
            liStar.eq(i).removeClass("emptyStar");
            liStar.eq(i).addClass("fullStar");
        }

        if(num>index){
            liStar.eq(index).removeClass("emptyStar");
            liStar.eq(index).addClass("halfStar");
        }

    }


}