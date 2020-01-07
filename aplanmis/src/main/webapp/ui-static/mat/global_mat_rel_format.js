function matNameFormatter(value, row, index, field) {

    var name = value+'&nbsp;';
    var tag = "";
    if(row.isCommon=='1'){
        tag += '<span class="circleIcon blueColor">通用</span>';
    }else{
        tag += '<span class="circleIcon blueColor">情形</span>';
    }

    if(row.zcqy=='1'){
        tag += '<span class="circleIcon blueColor">容缺</span>';
    }

    if(row.isOfficialDoc=='1'){
        tag += '<span class="circleIcon blueColor">批复</span>';
    }

    if(row.paperIsRequire=='1'){
        tag += '<span class="circleIcon blueColor">纸需</span>';
    }

    if(row.attIsRequire=='1'){
        tag += '<span class="circleIcon blueColor">电需</span>';
    }

    if(row.isSign=='1'){
        tag += '<span class="circleIcon blueColor">签章</span>';
    }
    return name + tag ;
}

function handleSelectMatProNew(id, value){

    if(value=='m'){

        $('#selectCertDiv').hide();
        $('#selectFormDiv').hide();

        $(id+' input[name="certName"]').rules('remove');
        $(id+' input[name="formName"]').rules('remove');

    }else if(value=='c'){

        $('#selectCertDiv').show();
        $('#selectFormDiv').hide();
        $(id+' input[name="certName"]').rules('add',{
            required: true,
            messages:{
                required: '<font color="red">请选择电子证照！</font>'
            }
        });
        $(id+' input[name="formName"]').rules('remove');

    }else{

        $('#selectCertDiv').hide();
        $('#selectFormDiv').show();
        $(id+' input[name="certName"]').rules('remove');
        $(id+' input[name="formName"]').rules('add',{
            required: true,
            messages:{
                required: '<font color="red">请选择表单！</font>'
            }
        });
    }
}

function matPropormatter(value, row, index){

    var matProp = row.matProp;
    if(matProp){
        if(matProp=='m'){
            return '普通材料';
        }else if(matProp=='c'){
            return '证照材料';
        }else{
            return '在线表单材料';
        }
    }
}

function editActStoFormFunc(id){

    var formId = $(id + ' input[name="stoFormId"]').val();
    if(formId){
        openFullWindow(ctx + '/design?formId='+formId+'&needCallBackSaveActStoForm=0&requiredField=refEntityId');
    }else{
        swal('提示信息','请选择表单!','info');
    }
}
