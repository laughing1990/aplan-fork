var agcloud = {};//全局对象
//流程相关
agcloud.bpm = {
    engine : {
        metronic: {
            //暂存表单，并启动流程

            //办理任务 viewId 是 视图渲染页面必须提供的全局变量
            startTask: function (taskId, busRecordId) {

                window.open(ctx + 'front/process/initProcessTurning.do?taskId=' + taskId + "&viewId=" + viewId + "&busRecordId=" + busRecordId, '_blank');


                //window.open(ctx + 'bpm/front/process/index.html?taskId=' + taskId + "&viewId=" + viewId + "&busRecordId=" + busRecordId, '_blank');
            },
            //查看任务 viewId 是 视图渲染页面必须提供的全局变量
            checkTask: function (taskId, busRecordId) {
                window.open(ctx + 'bpm/front/process/index.html?taskId=' + taskId + "&viewId=" + viewId + "&busRecordId=" + busRecordId, '_blank');
            },
            //签收任务
            signMyTask: function (taskId, callback) {
                confirmMsg("确认信息", "确认签收当前任务吗？", function () {
                    request('bpmFrontUrl', {
                        url: ctx + 'rest/front/task/signTask/' + taskId,
                        type: 'get',
                        data: {sendObjectStr: JSON.stringify(vm.sendParam)}
                    }, function (result) {
                        if (result.success) {
                            vm.$message.success(result.message);
                            if (callback)
                                callback();
                            location.reload();
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error("签收失败！");
                    });
                });
            },
            //指派任务
            assignTask: function (taskId, loginName, callback) {
                request('bpmFrontUrl', {
                    url: ctx + 'rest/front/task/assignTask',
                    type: 'post',
                    data: {taskId: taskId, loginName: loginName}
                }, function (result) {
                    if (result.success) {
                        vm.$message.success(result.message);
                        if (callback)
                            callback();
                    } else {
                        vm.$message.error(result.message);
                    }
                }, function () {
                    vm.$message.error("指派任务失败！");
                });
            },
            //转交任务
            sendOnTask: function (taskId, loginName, callback) {
                if (taskId != null && taskId != '' && loginName != null && loginName != '') {
                    request('bpmFrontUrl', {
                        url: ctx + 'rest/front/task/sendOnTask',
                        type: 'post',
                        data: {taskId: taskId, loginName: loginName}
                    }, function (result) {
                        if (result.success) {
                            vm.$message.success(result.message);
                            if (callback)
                                callback();
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error("转交任务失败！");
                    });
                }
            },
            //认领任务
            claimTask: function (taskId, callback) {
                request('bpmFrontUrl', {
                    url: ctx + 'rest/front/task/claimTask/' + taskId,
                    type: 'get'
                }, function (result) {
                    if (result.success) {
                        vm.$message.success(result.message);
                        if (callback)
                            callback();
                    } else {
                        vm.$message.error(result.message);
                    }
                }, function () {
                    vm.$message.error("转交任务失败！");
                });
            },
            //挂起流程
            suspendProcessInstance: function (processInstanceId, callback) {
                confirmMsg("确认信息", "确认挂起流程吗？", function () {
                    request('bpmFrontUrl', {
                        url: ctx + 'rest/front/process/suspendProcessInstance/' + processInstanceId,
                        type: 'get'
                    }, function (result) {
                        if (result.success) {
                            vm.$message.success(result.message);
                            //修改页面的流程状态
                            vm.isSuspended = '1';
                            //激活和挂起切换
                            var activeOrSuspendButton = vm.getActiveOrSuspendButton("activateProcess");
                            if(activeOrSuspendButton){
                                var id = "button_" + activeOrSuspendButton.elementCode;
                                var $realText = $(activeOrSuspendButton.elementRender.replace("#[id]", vm.taskId));
                                $realText[0].id = id;
                                $("#button_suspendProcess").before($realText);
                            }
                            $("#button_suspendProcess").remove();
                            if (callback)
                                callback();
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error("转交任务失败！");
                    });
                });
            },
            //激活流程
            activateProcessInstance: function (processInstanceId, callback) {
                confirmMsg("确认信息", "确认激活流程吗？", function () {
                    request('bpmFrontUrl', {
                        url: ctx + 'rest/front/process/activateProcessInstance/' + processInstanceId,
                        type: 'get'
                    }, function (result) {
                        if (result.success) {
                            vm.$message.success(result.message);
                            //修改页面的流程状态
                            vm.isSuspended = '0';
                            //激活和挂起切换
                            var activeOrSuspendButton = vm.getActiveOrSuspendButton("suspendProcess");
                            if(activeOrSuspendButton){
                                var id = "button_" + activeOrSuspendButton.elementCode;
                                var $realText = $(activeOrSuspendButton.elementRender.replace("#[id]", taskId));
                                $realText[0].id = id;
                                $("#button_activateProcess").before($realText);
                            }
                            $("#button_activateProcess").remove();
                            if (callback)
                                callback();
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error("转交任务失败！");
                    });
                });
            },
            //任务回退
            returnPrevTask: function (taskId,comment, callback) {
                confirmMsg("确认信息", "确认回退当前任务吗？", function () {
                    request('bpmFrontUrl', {
                        url: ctx + 'rest/front/task/returnPrevTask/' + taskId+'&comment='+comment,
                        type: 'get'
                    }, function (result) {
                        if (result.success) {
                            vm.$message.success(result.message);
                            if (callback)
                                callback();
                            else
                                setTimeout(function () {
                                    window.close();
                                },1500);
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error("转交任务失败！");
                    });
                });
            },
            //直接完成任务
            completeTask: function (taskId, callback) {
                confirmMsg("确认信息", "确认完成并发送任务吗？", function () {
                    request('bpmFrontUrl', {
                        url: ctx + 'rest/front/task/completeTask/' + taskId,
                        type: 'get'
                    }, function (result) {
                        if (result.success) {
                            vm.$message.success(result.message);
                            if (callback)
                                callback();
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error("转交任务失败！");
                    });
                });
            }
        }
    },
    form : {
        metronic:{
            currentPrivData : null,///权限数据，这里写成类变量，是因为每一个表单页面都有一个独立的类，不冲突。
            currentFormId : null,//表单id
            currentFormData : null,//表单数据
            /**
             * 给表单赋值封装方法
             * @param isJsonData  true  是  否  不是
             * @param jsonData  数据
             * @param formId  '#表单id'
             */
            loadFormData : function (isJsonData,formId,jsonData){
                if(!isJsonData){
                    jsonData = eval("("+jsonData+")");
                }
                this.currentFormData = jsonData;
                this.currentFormId = formId;
                var obj = jsonData;
                var key,value,tagName,type,arr;
                for(var x in obj){
                    key = x;
                    value = obj[x];
                    $(formId + " [name='"+key+"'],"+ formId +" [name='"+key+"[]']").each(function(){
                        tagName = $(this)[0].tagName;
                        type = $(this).attr('type');
                        if(tagName == 'INPUT'){
                            if(type == 'radio'){
                                $(this).attr('checked',$(this).val()==value);
                            }else if(type == 'checkbox'){
                                arr = value.split(',');
                                for(var i =0;i<arr.length;i++){
                                    if($(this).val()==arr[i]){
                                        $(this).attr('checked',true);
                                        break;
                                    }
                                }
                            }else{
                                //加入文本框是日期类型的判断，和格式化
                                var dateType = $(this).attr("date-type");
                                var tempValue = value;
                                if(dateType){
                                    if(typeof value == "number"){
                                        if(dateType == "date"){
                                            tempValue = agcloud.bpm.utils.dateFormat(value);
                                        }else if(dateType && dateType == "datetime"){
                                            tempValue = agcloud.bpm.utils.dateTimeFormat(value);
                                        }
                                    }else{
                                        var date = new Date(value).getTime();
                                        if(dateType == "date"){
                                            tempValue = agcloud.bpm.utils.dateFormat(date);
                                        }else if(dateType && dateType == "datetime"){
                                            tempValue = agcloud.bpm.utils.dateTimeFormat(date);
                                        }
                                    }
                                }
                                $(this).val(tempValue);
                            }
                        }else if(tagName == 'TEXTAREA'){
                            $(this).val(value);
                        }else if(tagName=='SELECT'){
                            if($(this).hasClass("selectpicker")){
                                var vs = value.split(",");
                                $(this).selectpicker('val',vs);//设置选中
                                $(this).selectpicker('refresh');
                            }else{
                                $(this).val(value);
                            }
                        }
                    });
                }
            },
            /**
             * 将input框封装成日期框
             * @param selectors  input框id数组
             * @param format 日期格式
             */
            createDateInput : function (selectors,format,callback){
                if(selectors.length==0)return;
                var defaultFormat = "yyyy-mm-dd hh:ii:ss";
                if(format){
                    defaultFormat = format;
                }
                if(defaultFormat.length > 10){
                    for(var i = 0; i<selectors.length; i++){
                        var obj = $('#'+selectors[i]).datetimepicker({
                            language: 'zh',
                            format: defaultFormat,
                            minuteStep: 5,
                            secondStep: 5,
                            todayHighlight: true,
                            autoclose: true,
                            pickerPosition: 'bottom-left',
                            todayBtn: true,
                        });
                        if(callback){
                            obj.on('changeDate',callback);
                        }
                    }
                }else{
                    for(var i = 0; i<selectors.length; i++){
                        var obj = $('#'+selectors[i]).datepicker({
                            language: 'zh',
                            format: defaultFormat,
                            todayHighlight: true,
                            autoclose: true,
                            pickerPosition: 'bottom-left',
                            todayBtn: true,
                        });
                        if(callback){
                            obj.on('changeDate',callback);
                        }
                    }
                }
            },
            /**
             * 根据表单字段的权限配置，字段显隐，是否可编辑等
             * 日期类型的字段会加上日期控件
             * @param formId
             * @param fieldPowerData
             */
            formViewAndEditPower : function (formId, fieldPowerData) {
                var self = this;
                if(fieldPowerData){
                    self.currentPrivData = fieldPowerData;//暂存当前页面的权限数据
                    for(var i in fieldPowerData){
                        var field = fieldPowerData[i];
                        var key = field.elementName;
                        //根据name遍历当前表单下的所有字段
                        $(formId + " [name='"+key+"'],"+ formId +" [name='"+key+"[]']").each(function(){
                            if(field.isHidden == 1){
                                $(this).parent("div").hide();
                                $(this).parent("div").prev().hide();//连同label一起隐藏，待优化
                            }else{
                                var tagName = $(this)[0].tagName;
                                var type = $(this).attr('type');
                                if(field.isReadonly == 1){
                                    if(tagName=='INPUT'){
                                        if(type=='radio' || type=='checkbox'){
                                            $(this).click(function () {
                                                return false;//不可点击
                                            });
                                        }else{
                                            $(this).attr("readonly",true);//只读
                                        }
                                        if(field.columnType == "date" || field.columnType == "datetime"){//移除日期控件
                                            $(this).datepicker("remove");
                                            $(this).datetimepicker("remove");
                                        }
                                    }else if(tagName=='SELECT'){
                                        //直接不让下拉，即使没法把
                                        $(this).prop('disabled', true);
                                        $(this).css({'cursor':'','background':'#ffffff','border-color':'#d9d9d9'});
                                        if($(this).hasClass("selectpicker")){
                                            var $this = $(this);//针对selectpicker类型下来框的不可编辑优化
                                            var inter = setInterval(function () {
                                                if($this.siblings("button").length > 0){
                                                    window.clearInterval(inter);
                                                    $this.siblings("button").css({'cursor':'','background':'#ffffff','border-color':'#d9d9d9'});
                                                    $this.siblings("button").removeClass("disabled");
                                                    $this.parent("div").css('cursor', "");
                                                }
                                            })
                                        }else{
                                            //可下拉，但不可以改变选中的值
                                            // var sec = $(this)[0];
                                            // sec.onfocus=function(){this.defaultIndex=this.selectedIndex;}
                                            // sec.onchange=function(){this.selectedIndex=this.defaultIndex;}
                                        }
                                    }else if(tagName=='TEXTAREA'){
                                        $(this).attr("readonly",true);//只读
                                    }
                                }else{
                                    //初始化日期控件
                                    var selectors = [$(this)[0].id];
                                    if(tagName=='INPUT' && (field.columnType.toLowerCase() == "date" || field.columnType.toLowerCase() == "datetime")){
                                        var dateType = $(this).attr("date-type");
                                        if(dateType && dateType.toLowerCase() == "date"){
                                            self.createDateInput(selectors,"yyyy-mm-dd");
                                        }else if(dateType && dateType.toLowerCase() == "datetime"){
                                            self.createDateInput(selectors);//默认带时分秒的
                                        }
                                    }
                                }
                            }
                        });
                    }
                }else {
                    //没有字段权限配置数据时，默认给日期类型input加上日期控件
                    $(formId + " input").each(function(){
                        var type = $(this).attr('type');
                        if(type == 'text'){
                            var dateType = $(this).attr("date-type");
                            var selectors = [$(this).attr("id")];
                            if(dateType && dateType == "date"){
                                self.createDateInput(selectors,"yyyy-mm-dd");
                            }else if(dateType && dateType == "datetime"){
                                self.createDateInput(selectors);//默认带时分秒的
                            }
                        }
                    })
                }
            },
            //提交时数据校验，不可编辑的字段提交时只会用加载时的数据提交到后台，手动修改html后编辑没用
            formSubmitDataCheck : function () {debugger
                if(this.currentPrivData && this.currentFormId) {
                    for (var i in this.currentPrivData) {
                        var field = this.currentPrivData[i];
                        if (field.isReadonly == 1) {
                            var key = field.elementName;
                            var value = this.currentFormData[key];
                            var inputField = $(this.currentFormId + " [name='" + key + "']");
                            inputField.val(value);//设置原始值，避免被修改
                            inputField.removeAttr("disabled");//移除disabled属性，值才能传递到后台。
                        }
                    }
                }
            },
            //提交后恢复编辑权限设置
            refreshForm : function () {
                this.formViewAndEditPower(this.currentFormId,this.currentPrivData);
            },
            //设置表单的权限信息
            setCurrentPrivData : function (data) {
                this.currentPrivData = data;
            },
            //工作流自定义日期标签的初始化方法
            initBpmDatetimeTag : function (node) {
                var id = node.id;
                var format = node.getAttribute("format");
                var defaultFormat = "yyyy-mm-dd hh:ii:ss";
                if(format){
                    defaultFormat = format;
                }
                if(defaultFormat.length > 10){
                    var obj = $('#' + id).datetimepicker({
                        language: 'zh',
                        format: defaultFormat,
                        minuteStep: 5,
                        secondStep: 5,
                        todayHighlight: true,
                        autoclose: true,
                        pickerPosition: 'bottom-left',
                        todayBtn: true,
                    });
                    $('#' + id).datetimepicker("show");
                }else{
                    var obj = $('#' + id).datepicker({
                        language: 'zh',
                        format: defaultFormat,
                        todayHighlight: true,
                        autoclose: true,
                        pickerPosition: 'bottom-left',
                        todayBtn: true,
                    });
                    $('#' + id).datepicker("show");
                }
            }
        },
        easyui:{

        }
    },
    utils : {
        /**
         * 将日期格式化为yyyy-MM-dd格式
         * @param val
         * @returns {*}
         */
        dateFormat:function (val) {
            return this.dateFormatByExp(val, "yyyy-MM-dd");
        },
        /**
         * 将日期格式化为yyyy-MM-dd HH:mm格式
         * @param val
         * @returns {*}
         */
        dateTimeNoSecendFormat:function (val) {
            return this.dateFormatByExp(val, "yyyy-MM-dd hh:mm");
        },
        /**
         * 将日期格式化为yyyy-MM-dd HH:mm:ss格式
         * @param val
         * @returns {*}
         * 代替生成代码的 formatDatebox函数
         */
        dateTimeFormat:function (val) {
            return this.dateFormatByExp(val, "yyyy-MM-dd hh:mm:ss");
        },
        dateFormat:function (val) {
            return this.dateFormatByExp(val, "yyyy-MM-dd");
        },

        /**
         * 将日期格式化为指定的exp格式
         * @param value 日期值
         * @param exp 格式表达式
         * @returns {null}
         */
        dateFormatByExp: function (value, exp) {
            if (value == null || value == '') {
                return null;
            }
            var dt;
            if (value instanceof Date) {
                dt = value;
            } else {
                dt = new Date(value);
            }
            return dt.format(exp); //扩展的Date的format方法(上述插件实现)
        },
        /**
         * 将数值四舍五入(保留2位小数)后格式化成金额形式
         * @param num 数值(Number或者String)
         * @return 金额格式的字符串,如'1,234,567.45'
         */
        currencyFormat:function (val) {
            if(num){
                num = num.toString().replace(/\$|\,/g,'');
                if(isNaN(num))
                    num = "0";
                sign = (num == (num = Math.abs(num)));
                num = Math.floor(num*100+0.50000000001);
                cents = num%100;
                num = Math.floor(num/100).toString();
                if(cents<10)
                    cents = "0" + cents;
                for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
                    num = num.substring(0,num.length-(4*i+3))+','+
                        num.substring(num.length-(4*i+3));
                return "￥ "+(((sign)?'':'-') + num + '.' + cents);
            }else{
                return "￥ 0.00";
            }
        },
        uuidGenerator:function () {
            function S4() {
                return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
            }

            return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
        },
    }
}
/* utils 工具类相关方法 */
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()// millisecond
    }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}
String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.endWith = function(str){
    if(str==null||str==""||this.length==0||str.length>this.length)
        return false;
    if(this.substring(this.length-str.length)==str)
        return true;
    else
        return false;
    return true;
}
String.prototype.startWith = function(str){
    if(str==null||str==""||this.length==0||str.length>this.length)
        return false;
    if(this.substr(0,str.length)==str)
        return true;
    else
        return false;
    return true;
}