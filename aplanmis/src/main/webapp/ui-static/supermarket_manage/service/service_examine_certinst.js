//全局参数

function showCertinst(certinstId){
    $.ajax({
        url: ctx + '/supermarket/serviceExamine/getCertinstById.do',
        type: 'POST',
        data: {'certinstId': certinstId},
        async: false,
        success: function (data) {
            $('#certinst_modal').modal('show');
            $("#certinstName").val(data.certinstName);
            $("#certinstCode").val(data.certinstCode);
            var _time = data.termStart+" 至 " +data.termEnd;

            var obj =data.serviceMajor;
            var majors = "";
            $.each(obj,function (key,val) {
                majors += val.majorName+"、";
            })
            marjors.substring(0,majors.length-1);
            $("#majors").val(marjors);

            $("#qualLevelName").val(data.qualLevelName);

            $("#attrs").empty();
            $.each(data.certinstDetail,function (i,val) {
                $("#attrs").append("<li class=\"list-group-item\">val.fileName</li>")
            });

            $('#certinst_modal_title').html('证照实例详情');

        },
        error: function () {
            swal('错误信息', "获取证照实例详情信息失败！", 'error');
        }
    });
}