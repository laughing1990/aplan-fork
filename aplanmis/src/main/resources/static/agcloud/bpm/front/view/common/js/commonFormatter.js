//申报阶段类型格式器
function stageFormatter(value, row, index) {
  if (row.IS_SERIES_APPROVE == "串联" || row.IS_SERIES_APPROVE == "单项") {
        var html = "<div class=\"flow_steps\">" +
            "<ul class='stage'>";
        html += "<li class=\"item\"><p>"+value+"</p></li>\n";
        html += "</ul>" +
            "</div>";
        //return html;
        return value;
    }else{
        if(!value) return "";
        var html = "<div class=\"flow_steps\">" +
            "<ul class='stage'>";
        if(value.indexOf("立项用地") != -1){
            if(row.itemName&&row.itemName!=null){
                html += "<li class=\"current\">立项用地</li>" +
                    "<li class='itemName' title='"+row.itemName+"'>"+row.itemName+"</li>";
            }else{
                html += "<li class=\"current\">立项用地</li>" +
                    "<li>&nbsp;</li>" +
                    "<li>&nbsp;</li>" +
                    "<li>&nbsp;</li>";
            }
        }else if(value.indexOf("工程建设") != -1){
            if(row.itemName&&row.itemName!=null){
                html += "<li class=\"current\">工程建设</li>" +
                    "<li class='itemName' title='"+row.itemName+"'>"+row.itemName+"</li>";
            }else{
                html += "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"current\">工程建设</li>" +
                    "<li>&nbsp;</li>" +
                    "<li >&nbsp;</li>";
            }
        }else if(value.indexOf("施工许可") != -1){
            if(row.itemName&&row.itemName!=null){
                html += "<li class=\"current\">施工许可</li>" +
                    "<li class='itemName' title='"+row.itemName+"'>"+row.itemName+"</li>";
            }else{
                html += "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"current\">施工许可</li>" +
                    "<li >&nbsp;</li>";
            }
        }else if(value.indexOf("竣工验收") != -1){
            if(row.itemName&&row.itemName!=null){
                html += "<li class=\"current\">竣工验收</li>" +
                    "<li class='itemName' title='"+row.itemName+"'>"+row.itemName+"</li>";
            }else{
                html += "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"current\">竣工验收</li>";
            }
        }
        else{
            html += value;
        }
        html += "</ul>" +
            "</div>";
        return html;
    }
}
//申报审批类型
function shenpiType(value, row, index) {
    return value;
}
//承诺天数
function promiseDate(value, row, index) {
    var time = value * 24 * 60 * 60 * 1000;//将承诺天数换算层毫秒
    var time2 = new Date(row.START_TIME).getTime();//获取开始受理的时间值
    return formatDatetimeCommon(time + time2,'yyyy年MM月dd日');
}
//剩余天数
function restOfDay(value, row, index) {
    var remainingTime = row.REMAINING_TIME;
    var overdueTime = row.OVERDUE_TIME;

    if(remainingTime != null&&overdueTime!=null){
        //非即办件
        // if(row.ITEM_PROPERTY != '1')
        //     remainingTime++;
        //如果期限小于等于工作日，且时间不在同一天的话。
        if(remainingTime <= 0||overdueTime>0){
            // if(overdueTime == 0.5)
            //     overdueTime = "半";
            return "<span class=\"btn m-btn--pill btn-outline-danger ag-count-down\">逾期"+ overdueTime +"天</span>";
        }else{
            var color = " btn-outline-info "
            if(remainingTime <= 2){
                color = " btn-outline-warning ";
            }
            // if(remainingTime == 0.5)
            //     remainingTime = "半";
            return "<span class=\"btn m-btn--pill "+ color +" ag-count-down\">剩余"+ remainingTime +"天</span>";
        }
    }else{
        var color = " btn-outline-info "
        return "<span class=\"btn m-btn--pill "+ color +" ag-count-down\">剩余"+ row.DUE_NUM +"天</span>";
    }


    // var myDueNum = row.DUE_NUM;
    // //新的接口
    // return restOfDayVersion2(row.TASK_ID,myDueNum);
    //
    // //旧的时限计算接口
    // //暂时把没有办理期限的值默认给 1，值等于0时表示即办件，默认给1天
    // if(row.DUE_NUM == undefined || row.DUE_NUM == null)
    //     myDueNum = 1;
    // if(row.DUE_NUM == 0)
    //     myDueNum = 1;
    // var time = new Date().getTime();//当前时间
    // var time2 = new Date(Date.parse(row.START_TIME.replace(/-/g,"/"))).getTime();//阶段开始时间
    // var time_dateFormat = agcloud.utils.dateFormat(time);
    // var time2_dateFormat = agcloud.utils.dateFormat(time2);
    // var theSameDay = (time_dateFormat == time2_dateFormat);
    // var workDays = null;
    // $.ajax({
    //     type: 'post',
    //     url: ctx + '/aea/business/getWorkDays.do',
    //     data:{dateStart:time2,dateEnd:time},
    //     async:false,
    //     success: function (data) {
    //         if (data.success) {
    //             workDays = parseFloat(data.message);
    //         }else{
    //             console.log(result.message);
    //         }
    //     }
    // });
    // if(workDays != null){
    //     //非即办件
    //     if(row.ITEM_PROPERTY != '1')
    //         myDueNum++;
    //     //如果期限小于等于工作日，且时间不在同一天的话。
    //     if(myDueNum <= workDays && !theSameDay){
    //         var tempDays = workDays - myDueNum;
    //         if(tempDays == 0.5)
    //             tempDays = "半";
    //         return "<span class=\"btn m-btn--pill btn-outline-danger ag-count-down\">逾期"+ tempDays +"天</span>";
    //     }else{
    //         var tempDays = myDueNum - workDays;
    //         var color = " btn-outline-info "
    //         if(tempDays <= 2){
    //             color = " btn-outline-warning ";
    //         }
    //         if(tempDays == 0.5)
    //             tempDays = "半";
    //         return "<span class=\"btn m-btn--pill "+ color +" ag-count-down\">剩余"+ tempDays +"天</span>";
    //     }
    // }
    // var time3 = row.DUE_NUM * 24 * 60 * 60 * 1000;//阶段承诺办理时间
    // if(time > (time2+time3)){
    //     var result = Math.ceil((time - (time2 + time3))/ (24 * 60 * 60 * 1000));
    //     return "<span class=\"btn m-btn--pill btn-outline-danger ag-count-down\">逾期"+ result +"天</span>";
    // }else{
    //     var result = Math.ceil((time3 + time2 - time)/ (24 * 60 * 60 * 1000));
    //     var color = " btn-outline-info "
    //     if(result <= 2){
    //         color = " btn-outline-warning ";
    //     }
    //     return "<span class=\"btn m-btn--pill "+ color +" ag-count-down\">剩余"+ result +"天</span>";
    // }
}
function restOfDayVersion2(taskId,limitTime) {
    var restDays = 0;
    $.ajax({
        type: 'post',
        url: ctx + '/aea/business/getRestOfDay.do',
        data:{taskId:taskId,limitTime:limitTime},
        async:false,
        success: function (data) {
            if (data.success) {
                restDays = parseFloat(data.message);
            }else{
                console.log(result.message);
            }
        }
    });
    if(restDays < 0){
        var tempDays = Math.abs(restDays);
        return "<span class=\"btn m-btn--pill btn-outline-danger ag-count-down\">逾期"+ tempDays +"天</span>";
    }else{
        var color = " btn-outline-info "
        if(restDays <= 2){
            color = " btn-outline-warning ";
        }
        return "<span class=\"btn m-btn--pill "+ color +" ag-count-down\">剩余"+ restDays +"天</span>";
    }
}
//承诺天数
/*
function promiseDate(value, row, index) {
    var time = value * 24 * 60 * 60 * 1000;//将承诺天数换算层毫秒
    var time2 = new Date(row.START_TIME).getTime();//获取开始受理的时间值
    return formatDatetimeCommon(time + time2,'yyyy年MM月dd日');
}*/
function serverObj(value, row, index) {
    if(row.XKDX == undefined || row.XKDX == ""){
        row.XKDX = 5;
    }
    var result = "个人和企业";
    $.ajax({
        type: 'post',
        url: ctx + '/itemGuide/getItemPropertiesXKDX.do',
        data:{dicName:"ITEM_FWJGXZ",code:row.XKDX},
        async:false,
        success: function (data) {
            if (data != '' || data != null) {
                result = data;
            }
        }
    });
    return result;
}
function dateTimeMinuteFormatterCnDate(value, row, index){
    return formatDatetimeCommon(value,'yyyy年MM月dd日 hh:mm');
}