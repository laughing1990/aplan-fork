//全局参数
var certinstList = [];
var zNodes = [];
var setting = {
    view: {
        addHoverDom: false,
        removeHoverDom: false,
        selectedMulti: false
    },
    check: {
        enable: false
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    edit: {
        enable: false
    }
};

var vm = new Vue({
    el: '#majorTree',
    data: function () {
        return {
            certinstMajor: [], // 专业
            defaultProps: {
                children: 'child',
                label: 'name'
            }
        }
    }
});

$(function () {
   var ul =  window.location.href;
   var unitServiceId = ul.substring(ul.indexOf("?")+1)
    console.info(ul);
    console.info(unitServiceId);
    $.ajax({
        url: ctx + '/supermarket/serviceExamine/getServiceMatter.do',
        type: 'POST',
        data: {'unitServiceId': unitServiceId},
        async: false,
        success: function (data) {
             console.info(data);
            $("#unitServiceId").val(data.unitServiceId);
            console.info(data.auditFlag);
            console.info("data.auditFlag");

            if(data.auditFlag == "0" || data.auditFlag == "1"){
                $('#memo').attr("readonly","readonly");

                var show = $('#serviceButtonT').css('display');
                $('#serviceButtonT').css('display',show =='block'?'none':show);
                $('#serviceButtonF').css('display',show =='block'?'none':show);
            }else {
                var show = $('#serviceButtonT').css('display');
                $('#serviceButtonT').css('display',show =='block'?show:'none');
                $('#serviceButtonF').css('display',show =='block'?show:'none');

            }
            $("#serviceButtonT").attr("onclick","examineService('T')");
            $("#serviceButtonF").attr("onclick","examineService('F')");
            $("#serviceName").val(data.serviceName);
            $("#applicant").val(data.applicant);
            $("#feeReference").val(data.feeReference);
            _serviceId = data.serviceId;
            _unitServiceId = data.unitServiceId;
            showServiceItemList(_serviceId);
            //列表增加从业人员
            // $("#linkmen").empty();
            // $.each(data.linkmanInfo,function(idx,obj){
            //     console.info(obj.linkmanName);
            //     console.info(obj);
            //     $("#linkmen").append("<button type=\"button\"  class=\"btn btn-info\">"+obj.linkmanName+"</button>");
            // });
            showLinkman(data.linkmanInfo);
            showCertinstList(_unitServiceId)
            $('#memo').val(data.memo);
            $('#managementScope').val(data.managementScope);


            $("#serviceContent").val(data.serviceContent);

            certinstList = data.certinstBVos;
            drawTabs(certinstList);
            $('#unit_service_modal_title').html('中介机构服务审批');

        },
        error: function () {
            swal('错误信息', "获取中介结构服务信息失败！", 'error');
        }
    });
});

function cerinstFormatter(value, row, index) {
    var editBtn = '<a href="javascript:showCertinst(\'' + row.certinstId + '\',\''+row.serviceId+'\')" ' +
        'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="详情"><i class="la la-edit"></i></a>';

    return editBtn;
}


function showCertinst(certinstId,serviceId){
    $.ajax({
        url: ctx + '/supermarket/serviceExamine/getCertinstById.do',
        type: 'POST',
        data: {'certinstId': certinstId},
        async: false,
        success: function (data) {
            $('#certinst_modal').modal('show');
            $("#certinstName").val(data.certinstName);
            $("#certinstCode").val(data.certinstCode);
            var _time = data.termStart.substring(0,10)+" 至 " +data.termEnd.substring(0,10);
            $("#_time").val(_time);


            showMajrTee(serviceId,certinstId);

            $("#qualLevelName").val(data.qualLevelName);

            $("#attrs").empty();
            $.each(data.certinstDetail,function (i,val) {
                $("#attrs").append('<li class="list-group-item">'+val.fileName+'</li>');
            });

            $('#certinst_modal_title').html('证照实例详情');

        },
        error: function () {
            swal('错误信息', "获取证照实例详情信息失败！", 'error');
        }
    });
}


function closeCertinstModal() {
    $('#certinst_modal').modal('hide');
}

function drawTabs(certinsts) {
    if(certinsts!=null && certinsts.length>0){
        var i = 1;
        $.each(certinsts,function () {
            if(i==1){
                $('#cert_tabs').append('<li class="nav-item"><a class="nav-link active" data-toggle="tab" id="'+this.certinstId+'" onclick="changeTab(this);">'+this.certinstName+'</a></li>');
                i++;
            }else{
                $('#cert_tabs').append('<li class="nav-item"><a class="nav-link" data-toggle="tab" id="'+this.certinstId+'" onclick="changeTab(this);">'+this.certinstName+'</a></li>');

            }

        });
        //默认显示第一条
        var first = certinsts[0];
        $('#certinstName').val(first.certinstName);
        $('#certinstCode').val(first.certinstCode);
        var _time = first.termStart.substring(0,11)+" 至 "+first.termEnd.substring(0,11);
        $('#_time').val(_time);
        $('#qualLevelName').val(first.qualLevelName);
        $('#managementScope').val(first.managementScope);
        // 显示ztree专业
        vm.certinstMajor =first.majorElTree;

        //xianshizhaopian
        $('#attrs').empty();
        if(first.certinstDetail !=null && first.certinstDetail.length >0){
            $.each(first.certinstDetail,function () {
                //添加预览接口
                // '<a href="#" onclick="showImgFile(\''+ row.detailId +'\')">'+ row.attName +'</a>'
                $('#attrs').append('<a href="#" onclick="showImgFile(\'\'+ this.bscAttDetail.detailId +\'\')"><img src="'+this.bscAttDetail.attPath+'" style="width:100px; height:100px ;margin: 5px;" /></a>')

            });
        }
    }
}

function changeTab(obj) {
    var _id = obj.getAttribute("id");

    $.each(certinstList,function () {
        if(this.certinstId == _id){
            $('#certinstName').val(this.certinstName);
            $('#certinstCode').val(this.certinstCode);
            var _time = this.termStart.substring(0,11)+" 至 "+this.termEnd.substring(0,11);
            $('#_time').val(_time);
            $('#qualLevelName').val(this.qualLevelName);
            $('#managementScope').val(this.managementScope);
            // 显示ztree专业
            certinstMajor =this.majorElTree;

            $(document).ready(function(){
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            });

            //xianshizhaopian
            $('#attrs').empty();
            if(this.certinstDetail !=null && this.certinstDetail.length >0){
                $.each(this.certinstDetail,function () {
                    // $('#attrs').append('<img src="'+this.bscAttDetail.attPath+'" style="width:100px; height:100px ;margin: 5px;" />')
                    $('#attrs').append('<a href="#" onclick="showImgFile(\'\'+ this.bscAttDetail.detailId +\'\')"><img src="'+this.bscAttDetail.attPath+'" style="width:100px; height:100px ;margin: 5px;" /></a>')

                });
            }
        }
    });
}

function showImgFile(detailId){

    window.open(MAT_URL_PREFIX + 'showFile.do?detailId='+ detailId,"展示图片");
}

function showLinkman(data) {
    $('#service_linkman_tb').bootstrapTable({
        data: data,
        columns: [{
            field: 'num',
            title: '序号',
            align: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: 'linkmanName',
            title: '姓名',
            align: 'left'
        }, {
            field: 'serviceName',
            title: '服务类型',
            align: 'left'
        }, {
            field: 'certinsts',
            title: '证书等级',
            align: 'left',
            formatter: function (value, row, index) {
                var str = '';
                for (var i in value) {
                    str += str != '' ? '、' : '';
                    str += value[i].certinstName + '（' + value[i].qualLevelName + '）';
                }
                return str;
            }
        }, {
            field: 'practiseDate',
            title: '从业时间',
            align: 'left',
            formatter: function (value, row, index) {
                return dateTimeFormatter(value);
            }
        }, {
            field: 'certinsts',
            title: '证照附件',
            align: 'left',
            formatter: function (value, row, index) {
                var str = '';
                for (var i in value) {
                    var detail = value[i].certinstDetail;
                    for (var j in detail) {
                        str += str != '' ? '</br>' : '';
                        str += '<a href="javascript:filePreview(\'' + detail[j].fileId + '\');">' + detail[j].fileName + '</a>';
                    }
                }
                return str;
            }
        }]
    });
}