//催办组件
var SendRemind = {
    template: '#sendRemind',
    data: function () {
        return {
            handleDialog:false,//督办弹窗
            remindTableData:[],//督办弹窗人员列表
            recievePeron:[],//接受人数据
            copySendPeron:[],//抄送人数据
            textarea:'',//督办内容
            type:["n"],//督办方式
            level:'u',//督办等级
            recieveSerch:'',//督办接收人
            recieveLoading:false,//加载loading
            copySendSerch:'',//
            processInstanceId:'',
            multipleSelection:[],
            currentProjName:"",
            currentAppName:"",
            applyinstCode:"",
            handleTitle:'',
            tableLoading:false,
        }
    },
    methods: {
        //催办弹窗
        handleRemind:function(index,row){
            this.processInstanceId = row.procInstId;
            this.currentProjName = row.projName;
            this.applyinstCode = row.applyinstCode;
            this.handleDialog =true;
            this.getpersonData();
            this.getAppName();
            this.intHandleDialog();
            this.$nextTick(function(){
                $(".person .el-select  .el-input .el-input__inner").removeAttr("placeholder");
            });
            var content = "请尽快审批办理"+this.currentProjName+"项目申报（项目代码："+row.localCode+"，申报流水号："+row.applyinstCode+"）。";
            this.textarea = content;
        },
        filter:function(val){
            var _this = this;
            if(!val){
                _this.focus();
                return
            }
            _this.recieveSerch = _this.remindTableData;

            $(".person1 .personSelect").removeClass('personSelect2');
            var currentData = [];
            for(var i=0; i<_this.recieveSerch.length;i++){
                if(_this.recieveSerch[i].userName.indexOf(val)!=-1){
                    currentData.push(_this.recieveSerch[i])
                }
            }
            this.$nextTick(function(){
                $(".el-select-dropdown__item.hover").hide();
            })

            if(currentData.length==0){
                this.$nextTick(function(){
                    $(".el-select-dropdown__item.hover").show().css({"pointer-events":"none","cursor":'default !important'}).children('span').text("无匹配人员");
                })
            }
            _this.recieveSerch = currentData;

        },
        filter2:function(val){
            var _this = this;
            if(!val){
                _this.focus2();
                return
            }
            this.recieveLoading=true;
            this.searchPerson(val);
        },
        focus:function(){
            $(".person1 .personSelect").addClass('personSelect2')
        },
        // 删除人员
        removeTag:function(item){
            var _this = this;
            var removeObj = {}
            for (var i = 0;i<this.currentData.length;i++){
                var obj = this.currentData[i];
                if (obj.userId == item){
                    removeObj = obj;
                    this.currentData.splice(i,1);
                    i--
                }
            }
            this.multipleSelection = this.currentData;
            this.$refs.table.toggleRowSelection(removeObj,false);
            if(!this.multipleSelection[0]){
                this.$refs.table.clearSelection();
                return
            }
        },
        focus2:function(){
            $(".person2 .personSelect").addClass('personSelect2')
        },
        // 发送数据
        sendData:function(){
            var _this = this;

            if(this.recievePeron.length==0){
                _this.$message({
                    message:"请选择接收人",
                    type: 'error'
                });
                return
            }
            if(this.textarea.length==0){
                _this.$message({
                    message:"请填写发送内容",
                    type: 'error'
                });
                return
            }
            confirmMsg('','确定发送吗？',function(){
                for(var i=0; i<_this.recievePeron.length; i++){
                    if((_this.recievePeron.length-1)==i){
                        _this.last=true;
                    }
                    var userIdJson = [];

                    var s = {
                        remindReceiverType:"s",
                        receiverUserId:_this.recievePeron[i]
                    }
                    userIdJson.push(s);
                    if(_this.copySendPeron.length!=0){
                        for(var j=0; j<_this.copySendPeron.length; j++){
                            var c = {
                                remindReceiverType:"c",
                                receiverUserId:_this.copySendPeron[j]
                            }
                        }
                        userIdJson.push(c);
                    }
                    // 查找所选userId对应的taskId
                    for(var j=0; j < _this.multipleSelection.length; j++){
                        if(_this.recievePeron[i] == _this.multipleSelection[j].userId){
                            var taskId = _this.multipleSelection[j].taskId;
                        }
                    }
                    var datas = new Array();
                    $.each(_this.type,function(i,v){
                        var data = {
                            procInstId:_this.processInstanceId,
                            remindContent:_this.textarea,
                            remindLevel:_this.level,
                            remindMode:"p",
                            remindType:v,
                            taskInstId:taskId,
                            userIdJson:JSON.stringify(userIdJson)
                        };
                        datas.push(data);
                    });
                    var param = JSON.stringify(datas);
                    request('',{url:ctx + 'bpm/front/remind/msg/save?projName='+_this.currentProjName+"&appName="+_this.currentAppName+"&applyinstCode="+_this.applyinstCode,type: 'post',ContentType:'application/json',
                        data:param
                    },function (data) {
                        if(data.success){
                            if(_this.last){
                                _this.handleDialog =false;
                                _this.$message({
                                    message:"催办信息发送成功",
                                    type: 'success'
                                });
                                _this.last=false;
                            }
                        }
                    }, function(msg){
                        alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
                    });
                }
            })
        },
        // 初始化
        intHandleDialog:function(){
            this.recievePeron=[];
            this.copySendPeron=[];
            this.textarea="";
            this.type=["n"];
            this.level="u";
            this.currentData=[];
            this.recieveSerch='';
            this.copySendSerch='';
        },
        handleSelectionChange:function(val) {
            this.multipleSelection = val;
            // this.recieveSerch = this.multipleSelection;
            this.recievePeron = [];
            this.currentData = this.multipleSelection;
            var _this = this;
            this.multipleSelection.map(function(item){
                _this.recievePeron.push(item.userId)
            })
        },
        handleSelectionChange2:function(row, column, event){
            for(var i=0; i<this.multipleSelection.length; i++){
                if(this.multipleSelection[i]==row){
                    this.$refs.table.toggleRowSelection(row,false);
                    // this.multipleSelection.splice(i,1);
                    this.recievePeron = [];
                    this.currentData = this.multipleSelection;
                    var _this = this;
                    this.multipleSelection.map(function(item){
                        _this.recievePeron.push(item.userId);
                    })
                    return;
                }
            }
            this.multipleSelection.push(row);
            this.$refs.table.toggleRowSelection(row,true);

            // this.multipleSelection.push(row);
        },
        //获取在办人员
        getpersonData:function(){
            var _this = this;
            request('',{url:ctx + 'bpm/front/remind/process/runtime/users',type: 'get',
                data:{
                    procInstId : _this.processInstanceId
                }
            },function (data) {
                _this.remindTableData = data.content;
                _this.recieveSerch = data.content;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        // 搜索用户
        searchPerson:function(val){
            var _this = this;
            request('',{url:ctx + 'bpm/front/remind/users/'+val,type: 'get',
                data:{}
            },function (data) {
                $(".person2 .personSelect").removeClass('personSelect2')
                $(".el-select-dropdown__item.hover").hide();
                if(data.content.length==0){
                    $(".el-select-dropdown__item.hover").show().css({"pointer-events":"none","cursor":'default !important'}).children('span').text("无匹配人员");
                }
                _this.copySendSerch = data.content;
                _this.recieveLoading=false;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
        // 获取名称
        getAppName:function(){
            var _this = this;
            request('',{url:ctx + 'bpm/front/remind/tpl/app/name',type: 'get',
                data:{
                    procInstId : _this.processInstanceId
                }
            },function (data) {
                _this.handleTitle = "催办 — "+data.message;
                _this.currentAppName = data.message;
            }, function(msg){
                alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
            });
        },
    }
};
//树状结构面板
var SyncTreePanel = {
    template: '#syncTreePanel',

    props: {
        //标题
        title: {
            type: String,
            default: '请选择',
            // required:true
        },
        //树结构
        dataprops: {
            type: Object,
            required:true
        },
        //请求数据地址
        requesturl: {
            type: String,
            required:true
        }

    },
    data: function () {

        return {

            //树结构
            treeProps: {
                label: this.dataprops.label,
                children: this.dataprops.children,
                id:this.dataprops.id
            },

            //面板显示
            treeVisible:false,

            //过滤文本
            filterText:'',

            //显示树结构数据
            syncTree:[],

            //input显示内容
            inputText:'',

            //已选节点
            selectedList:[],
        }
    },
    methods: {
        //获取展示的树结构数据
        queryTree:function () {

            var ts = this;
            request('', {
                url: this.requesturl,
                type: 'get',
                async: true,
                data: {}
            }, function (res) {
                if (res.success) {
                    ts.syncTree = res.content;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });

        },
        //选中处理
        handleCheckChange:function (data, checked, indeterminate) {
            if(checked){
                this.selectedList.push(data);
            }else{
                for(var i=0;i<this.selectedList.length;i++){
                    if(this.selectedList[i][this.dataprops.id]==data[this.dataprops.id]){
                        this.selectedList.splice(i,1);
                    }
                }
            }
            var text = '';
            for(var i=0;i<this.selectedList.length;i++){
                if(i==0){
                    text=this.selectedList[i][this.dataprops.label];
                }else{
                    text=text+','+this.selectedList[i][this.dataprops.label];
                }
            }

            this.inputText = text;
        },

        //过滤关键字
        filterNode:function(value, data) {
            if (!value) return true;
            return data[this.dataprops.label].indexOf(value) !== -1;
        },
        //重置选取
        resetChecked:function() {
            this.filterText='';
            if(this.$refs && this.$refs.tree) {
                this.$refs.tree.setCheckedKeys([]);
            }
        },
        //根据选择的条件查询
        queryCheckedTree: function () {
            this.treeVisible = false;
            this.$emit('tabledatasearch');
        },
        //获取选中值
        getSelectecValue:function(filedname){
            var value = '';
            for(var i=0;i<this.selectedList.length;i++){
                if(i==0){
                    value=this.selectedList[i][filedname];
                }else{
                    value=value+','+this.selectedList[i][filedname];
                }
            }

            return value;
        }
    },
    created: function () {
        this.queryTree();
    },
    watch: {
        filterText: function(val) {
            this.$refs.tree.filter(val);
        }
    }
};

//级联组件
var SyncCascader = {
    template: '#syncCascader',

    props: {
        //结构
        dataprops: {
            type: Object,
            required:true
        },
        //请求数据地址
        requesturl: {
            type: String,
            required:true
        }

    },
    data: function () {

        return {
            //级联结构
            cascaderProps: {
                value: this.dataprops.value,
                label: this.dataprops.label,
                children: this.dataprops.children,
                checkStrictly: this.dataprops.checkStrictly
            },

            //选择的节点
            selectedList:[],

            //列表数据
            dataList:[]
        }
    },
    methods: {
        queryData: function () {
            var ts = this;
            request('', {
                url: this.requesturl,
                type: 'get',
                data: {}
            }, function (res) {
                if (res.success) {
                    ts.dataList = res.content;
                    ts.handelChildren(ts.dataList);
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });
        },

        // 处理最后一层的children,为空就删掉children属性
        handelChildren: function(list){
            for(var i = 0; i <list.length; i++){
                if(list[i].children && list[i].children.length){
                    this.handelChildren(list[i].children)
                }else {
                    delete list[i].children
                }
            }
        },

        //调用父组件的查询
        tableDataSearch:function(){
            this.$emit('tabledatasearch');
        },

        //获取选中值
        getSelectecValue:function(){
            // 只获取最后一个选中节点
            var _value = "";
            if (this.selectedList) {
                var len = this.selectedList.length;
                if(len>0){
                    _value = this.selectedList[len - 1];
                }
            }

            return _value;
        },

        //清空选择
        resetChecked:function() {
            this.selectedList=[];
        },

    },
    created: function () {
        this.queryData();
    }
};

var mixins = {
    data: function () {
        return {
            // 全局页面loading
            loading: false,
            //当前屏幕高度
            curHeight: (document.documentElement.clientHeight || document.body.clientHeight),

            // 是否展示更多查询条件
            showMoreSearchItem: false,

            //表格数据
            tableData: [],
            //数据总数
            dataTotal: 0,

            //主题
            themeList:[],
            //申报来源
            applySourceList:[],
            //申报类型
            applyTypeList:[],
            //申报状态
            applyStateList:[],
            //事项申报状态
            iteminstStateList:[],
            //时限状态
            instStateList:[],

            //用来存放主题
            globalThemeList:[],

            //行业分类树结构
            industryProps:{
                label:"itemName",
                children:"children",
                id:"itemId",
            },

            //行业分类请求地址
            industryRequestUrl:ctx + 'rest/conditional/query/tree/industry',

            // 行政分区级联-prop
            regionalismProps: {
                value: 'regionId',
                label: 'regionName',
                children: 'children',
                checkStrictly: true
            },

            //行政分区请求地址
            regionalismRequestUrl:ctx + 'rest/conditional/query/listBscDicRegion',
        }
    },
    methods: {
        // 处理接口message
        apiMessage: function (msg, type) {
            this.$message({
                message: msg,
                type: type
            })
        },

        // 处理ele-table中的单元格字段中值为null或为空
        formatTableCell: function (row, column, cellValue, index) {
            if (!cellValue || cellValue == null) {
                return '-'
            }
            return cellValue;
        },

        // 列表查询
        tableDataSearch: function () {
            this.searchFrom.pagination.page = 1;
            this.fetchTableData();
        },
        // 列表换页
        handleCurrentChange: function (val) {
            this.searchFrom.pagination.page = val;
            this.fetchTableData();
        },
        // 页面大小change
        handleSizeChange: function (val) {
            this.searchFrom.pagination.perpage = val;
            this.fetchTableData();
        },
        //排序监听
        sortChange: function(column) {

            if(column.order=='ascending'){
                this.searchFrom.sort = {
                    field: column.prop,
                    sort: 'asc'
                };
            }else if(column.order=='descending'){
                this.searchFrom.sort = {
                    field: column.prop,
                    sort: 'desc'
                };
            }else{
                this.searchFrom.sort = {};
            }

            this.fetchTableData();
        },

        //清空主题
        clearThemeList : function () {
            var ts = this;
            if(ts.searchFrom.applyType=='1'){
                ts.themeList = [];
                ts.searchFrom.theme='';
            }else{
                if(ts.globalThemeList.lenght==0){
                    ts.conditionalQueryDic();
                }else{
                    ts.themeList = ts.globalThemeList;
                }
            }

            this.tableDataSearch();
        },

        // 阶段/事项/辅线显示
        stageFormatter : function (row) {
            if(row.stageName && row.itemName){
                return '<div class="step-status" >' +
                    '        <span class="step-status-item green-status"><span class="item-name ellipsis">'+row.stageName+'</span></span>' +
                    '        <span class="step-status-item gary-status long-status-item"><span class="item-name ellipsis">'+row.itemName+'</span></span>' +
                    '    </div>';
            }else if(row.stageName && !row.itemName ){
                var html = "<div class=\"flow_steps\">" +
                    "<ul class='stage'>";
                if(row.stageName.indexOf("立项用地") != -1){
                    html += "<li class=\"current\">立项用地</li>" +
                        "<li>&nbsp;</li>" +
                        "<li>&nbsp;</li>" +
                        "<li >&nbsp;</li>";
                }else if(row.stageName.indexOf("工程建设") != -1){
                    html += "<li class=\"stage-success\">&nbsp;</li>" +
                        "<li class=\"current\">工程建设</li>" +
                        "<li>&nbsp;</li>" +
                        "<li >&nbsp;</li>";
                }else if(row.stageName.indexOf("施工许可") != -1){
                    html += "<li class=\"stage-success\">&nbsp;</li>" +
                        "<li class=\"stage-success\">&nbsp;</li>" +
                        "<li class=\"current\">施工许可</li>" +
                        "<li >&nbsp;</li>";
                }else if(row.stageName.indexOf("竣工验收") != -1){
                    html += "<li class=\"stage-success\">&nbsp;</li>" +
                        "<li class=\"stage-success\">&nbsp;</li>" +
                        "<li class=\"stage-success\">&nbsp;</li>" +
                        "<li class=\"current\">竣工验收</li>";
                }else{
                    return '<span>'+row.stageName+'</span>';
                }

                html += "</ul>" +
                    "</div>";

                return html;
            }else if(row.itemName){
                return '<span>'+row.itemName+'</span>';
            }

            return "-";
        },

        // 显示主题、阶段、事项信息
        showThemeStageItemInfo:function (row) {
            var html = '';
            if (row.themeName) {
                html = html + "所属主题 : " + row.themeName + "</br></br>";
            }

            if(row.stageName) {
                html = html + "所处阶段 : " + row.stageName + "</br></br>";
            }

            if(row.itemName) {
                html = html + "申报事项 : " + row.itemName;
            }

            return html;
        },

        // 显示项目信息
        showProjInfo:function (row) {
            var html = '';
            if (row.localCode) {
                html = html + "项目代码 : " + row.localCode + "</br></br>";
            }

            if(row.gcbm) {
                html = html + "工程编码 : " + row.gcbm + "</br></br>";
            }

            if(row.projName) {
                html = html + "项目/工程名称 : " + row.projName;
            }

            return html;
        },

        //获取查询条件的数据
        conditionalQueryDic: function () {
            var ts = this;
            // ts.loading = true;
            request('', {
                url: ctx + 'rest/conditional/query/task/dic/list',
                type: 'get',
                data: {}
            }, function (res) {
                // ts.loading = false;
                if (res.success) {
                    ts.themeList = res.content.themeList;
                    ts.applySourceList = res.content.applySourceList;
                    ts.applyTypeList = res.content.applyTypeList;
                    ts.applyStateList = res.content.applyStateList;
                    ts.instStateList = res.content.instStateList;
                    ts.globalThemeList = res.content.themeList;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                // ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        formatDatetimeCommon:function(value,format) {
            if (value == null || value == '') {
                return '';
            }
            var dt;
            if (value instanceof Date) {
                dt = value;
            } else if(typeof value == "number"){
                dt = new Date(value);
            }else if(typeof value == "string"){
                try{
                    dt = new Date(Date.parse(value.replace(/-/g,"/")));
                }catch (e){
                    return value;
                }
            }
            return dt.format(format);
        },
        //催办弹窗
        handleRemind:function(index,row){
            this.$refs.sendRemind.handleRemind(index,row);
        },
    }
}

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

// 校验数字
var checkNumber = function (rule, value, callback) {
    var reg = /[^\d^\.]+/g
    if (!reg.test(value)) {
        callback();
    } else {
        return callback(new Error('请输入数字'));
    }
};