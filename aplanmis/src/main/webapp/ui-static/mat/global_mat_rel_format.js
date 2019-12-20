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
    return name + tag ;
}