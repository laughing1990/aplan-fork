// last edit by zl 20190916
var vm = new Vue({
    el: '#index',
    data: {
        // 左边部分
        page: 1,
        pageSize: 10,
        loading: true,
        searchKey: null,
        selectedIds: [],
        navId: null,
        selRowData: {
            navId: '',
            navName: '',
            sortNo: '',
            navMemo: '',
            isActive: '1',
        },
        dialogTitie: '',
        dialogEditTable: false,
        rules: {
            navName: [
                {required: true, message: '请输入导航定义', trigger: 'blur'}
            ],
            isActive: [
                {required: true, message: '请选择是否启用', trigger: 'change'}
            ],
            sortNo: [
                { required: true, message: '请输入排序', trigger: 'blur' },
                { type: 'number', message: '排序必须为数字值'}
            ],
        },
        tableData: {},
        // ========= 右边部分 ===========
        searchKeyRight: null,
        selectedRightIds: [],
        loading2: true,
        rightRowData: {
             factorId: '',
             navId: '',
             factorName:'',
             useEl:'',
             stateEl:'',
             isQuestion:'',
             answerType:'',
             mustAnswer:'',
             parentFactorId:'',
             sortNo:'',
             factorMemo:'',
             isActive: '',
             themeId:'',
             themeName: '',
        },
        dialogTitie2: '',
        dialogEditTable2: false,
        rules2: {
            factorName: [
                {required: true, message: '请输入导航情形名称', trigger: 'blur'}
            ],
            isQuestion: [
                {required: true, message: '请选择是否问题', trigger: 'change'}
            ],
            mustAnswer: [
                {required: true, message: '请选择是否回答', trigger: 'change'}
            ],
            answerType: [
                {required: true, message: '请选择回答方式', trigger: 'change'}
            ],
            useEl: [
                {required: true, message: '请选择是否启用El', trigger: 'change'}
            ],
            stateEl: [
                {required: true, message: '请填写El表达式', trigger: 'blur'}
            ],
            sortNo: [
                { required: true, message: '请输入排序', trigger: 'blur' },
                { type: 'number', message: '排序必须为数字值'}
            ],
            isActive: [
                {required: true, message: '请选择是否启用', trigger: 'change'}
            ],
        },
        dataTbRight: [],
        formLabelWidth: '30%',
        initNavSortNo: 1,
        initNavStateSortNo: 1,
        isThemeShow: false,
        isQuestionShow: false,
        useElShow: false,
        dialogTitie3:'',
        dialogEditTable3: false,
        themeData:{
            themeName: '',
            themeCode: '',
            themeMemo: '',
        },
        searchRightData: {},
        dialogSetTheme: false,
        setThemeRootNodeId: [],  // 设置主题树根元素id
        themeFilterText: '',
        setThemeTreeData: [],
        selThemeList: [],
        setThemeSearchFlag: true,
        defaultProps:{
            label: 'label',
            isLeaf: 'leaf'
        },
    },
    mounted: function () {
        this.loadNavTbData();
    },
    watch: {
        tableData: function () {
            this.$refs.navTable.setCurrentRow(this.tableData.rows[0]);
        }
    },
    methods: {
        // 记载主题导航定义数据
        loadNavTbData: function (props) {

            var _that = this;
            var prop = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize? this.pageSize: 10,
                keyword: this.searchKey ? this.searchKey : '',
            };
            if (!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/par/nav/listAeaParNavPage.do?isDeleted=0',
                type: 'post',
                data: props
            }, function (data) {

                _that.tableData = data;
                _that.loading = false;
                _that.searchRightData = _that.tableData.rows[0];
                if(_that.searchRightData){
                    _that.loadNavStateData(_that.searchRightData.navId);
                }
            }, function(msg){

                alertMsg('','加载失败','关闭','error',true);
                _that.loading = false;
                _that.loading2 = false;
            });
        },
        loadNavStateData: function (navId) {

            var _that = this;
            if (navId) {
                _that.navId = navId;
                _that.rightRowData.navId = navId;
                var props = {
                    'navId': navId ? navId : '',
                    'keyword': this.searchKeyRight?this.searchKeyRight:'',
                }
                request('aplanmis', {
                    url: ctx + 'aea/par/factor/gtreeAeaParFactor.do',
                    type: 'post',
                    data: props
                }, function (data) {
                    _that.dataTbRight = [];
                    _that.dataTbRight = data;
                    _that.loading2 = false;

                }, function (result) {
                    alertMsg('', result.message ? result.message : '加载失败', '关闭', 'warning', true);
                    _that.dataTbRight = [];
                });
            } else {
                _that.dataTbRight = [];
                _that.loading2 = false;
            }
        },
        searchLoadNavTb: function(){

            this.loadNavTbData();
        },
        clearFormData: function(){

            this.$refs['selRowData'].resetFields();
        },
        // 选择选择数据处理
        handleSelChange: function (val) {

            var ids = [];
            val.map(function (item) {
                ids.push(item.navId)
            });
            this.selectedIds = ids;
            return this.selectedIds;
        },
        navTbRowClick: function (row) {

            this.loadNavStateData(row.navId);
        },
        handleNavTbSizeChange: function (val) {

            this.pageSize = val;
            this.loadNavTbData();
        },
        handleNavTbCurrentChange: function (val) {

            this.page = val;
            this.loadNavTbData();
        },
        // 新增/编辑主题导航定义数据
        aeditNavTbRow: function(rowData, dialogTitie){

            var _that = this;
            this.dialogTitie = dialogTitie;
            this.dialogEditTable = true;
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            if(rowData){
                for (var key in rowData) {
                    this.selRowData[key] = rowData[key];
                }
            }else{
                _that.initNavSortNo = 1;
                request('aplanmis',{
                    url: ctx + 'aea/par/nav/getMaxSortNo.do',
                    type: 'post',
                    data: {},
                    async: false,
                },function (data) {
                    _that.initNavSortNo = data;
                }, function(msg){});
                this.selRowData = {
                    navId: '',
                    navName: '',
                    sortNo: _that.initNavSortNo? _that.initNavSortNo:1,
                    navMemo: '',
                    isActive: '0',
                };
            }
        },
        saveNavTbRow: function(refsD, rowData){

            var _that = this;
            this.$refs[refsD].validate(function(valid) {
                if (valid) {
                    _that.submitNavTbRow(rowData);
                } else {
                    alertMsg('','请输入完整信息','关闭','warning',true);
                    this.dialogEditTable = true;
                    return false;
                }
            });
        },
        submitNavTbRow: function(data){

            var _that = this;
            request('aplanmis',{
                url: ctx + 'aea/par/nav/saveOrUpdateAeaParNav.do',
                data:data
            },function () {
                _that.$message({
                    message: '保存成功',
                    type: 'success'
                });
                _that.dialogEditTable = false;
                _that.loadNavTbData();
            }, function(){
                alertMsg('','加载失败','关闭','error',true);
            });
        },
        // 删除主题导航定义数据
        delNavTbData: function (data) {

            var _that = this;
            if (data && data.navId) {
                var delData = {
                    'id': data.navId
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/nav/deleteAeaParNavById.do',
                        type: 'post',
                        data: delData
                    },function () {

                        alertMsg('', '删除成功', '关闭', 'success', true);
                        _that.loadNavTbData();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }else{
                alertMsg('', '请选择一条记录!', '关闭', 'info', true);
            }
        },
        batchDelNavTbData: function(){

            var _that = this;
            var ids = _that.selectedIds;
            if ((ids == null || ids.length == 0 || ids == 'undefined')) {
                alertMsg('', '请选择数据!', '关闭', 'error', true);
            } else {
                var delData = {
                    'ids': ids.toString()
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/nav/batchDelAeaParNavByIds.do',
                        data: delData
                    },function () {

                        alertMsg('', '删除成功', '关闭', 'success', true);
                        _that.loadNavTbData();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }
        },
        changeIsActive: function (dataSend, e) {

            e.cancelBubble = true;
            var _that = this;
            var obj = e.currentTarget;
            if(dataSend){
                var id = dataSend.navId;
                var isActive = dataSend.isActive;
                var isDone = false;
                if(isActive=='0'){
                    request('aplanmis', {
                        url: ctx + 'aea/par/nav/listAeaParNavByNoPage.do',
                        type: 'POST',
                        data: {},
                        async: false,
                    }, function (data) {
                        if(data&&data.length>0){
                            _that.$message({
                                message: '已经存在其他启用的导航定义,如要启用当前定义，请先禁用其他已启用定义！',
                                type: 'error'
                            });
                            isActive=='1'?$(obj).prop("checked",true):$(obj).prop("checked",false);
                            isDone = false;
                            return;
                        }else{
                            isDone = true;
                        }
                    }, function (msg) {

                        _that.$message({
                            message: msg.message ? msg.message : '服务请求失败',
                            type: 'error'
                        });
                        isActive=='1'?$(obj).prop("checked", true):$(obj).prop("checked", false);
                    });
                }else{
                    isDone = true;
                }
                if(isDone){
                    var action = isActive=='1'? "禁用" : "启用" ;
                    confirmMsg('', '确定要' + action + '选中的记录吗？', function () {
                        request('aplanmis', {
                            url: ctx + 'aea/par/nav/changIsActiveState.do',
                            type: 'POST',
                            data: {'navId': id},
                        }, function (data) {
                            if (data.success) {
                                _that.$message({
                                    message: '修改成功',
                                    type: 'success'
                                });
                                _that.loadNavTbData();
                            } else {
                                _that.$message({
                                    message: data.message ? data.message : '修改失败，请稍后再试',
                                    type: 'error'
                                });
                                isActive=='1'?$(obj).prop("checked", true):$(obj).prop("checked", false);
                            }
                        }, function (msg) {
                            _that.$message({
                                message: msg.message ? msg.message : '服务请求失败',
                                type: 'error'
                            });
                            isActive=='1'?$(obj).prop("checked",true):$(obj).prop("checked", false);
                        });
                    }, function(){
                        isActive=='1'?$(obj).prop("checked", true):$(obj).prop("checked", false);
                    }, '确定', '取消');
                }
            }else{
                _that.$message({
                    message: '请选择操作对象!',
                    type: 'error'
                });
            }
        },
        showBindingTheme: function(themeId){

            var _that = this;
            if(themeId){
                request('aplanmis',{
                    url: ctx + 'aea/par/theme/getAeaParTheme.do',
                    type: 'post',
                    data: {'id': themeId},
                    async: false,
                },function (data) {
                    this.dialogTitie3 = '查看主题';
                    this.dialogEditTable3 = true;
                    $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
                    if(data){
                        this.themeData = {
                            themeName: data.themeName,
                            themeCode: data.themeCode,
                            themeMemo: data.themeMemo,
                        }
                    }else{
                        this.themeData = {
                            themeName: '',
                            themeCode: '',
                            themeMemo: '',
                        }
                    }
                }, function(){

                    alertMsg('', '删除失败', '获取数据失败', 'error', true);
                });
            }else{
                alertMsg('', '错误信息', '关联主题id为空!', 'error', true);
            }
        },
        clearRightFormData: function(){

            this.$refs['rightRowData'].resetFields();
        },
        clearThemeFormData: function(){

            this.$refs['themeData'].resetFields();
        },
        searchNavState: function (navId){

            this.loadNavStateData(navId)
        },
        aeditNavState: function (data, title){

            var _that = this;
            _that.dialogTitie2 = title;
            _that.dialogEditTable2 = true;
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            if (data) {
                // 问题
                if(data.isQuestion=='1'){
                    _that.isQuestionShow = true;
                    _that.isThemeShow = false;
                    _that.useElShow = false;
                    _that.rules2.isQuestion = [{required: true, message: '请选择是否问题', trigger: 'change'}];
                    _that.rules2.mustAnswer = [{required: true, message: '请选择是否回答', trigger: 'change'}];
                    _that.rules2.answerType = [{required: true, message: '请选择回答方式', trigger: 'change'}];
                    _that.rules2.useEl = [];
                    _that.rules2.stateEl = [];
                // 答案
                }else{
                    _that.isQuestionShow = false;
                    _that.isThemeShow = true;
                    _that.useElShow = true;
                    _that.rules2.isQuestion = [{required: true, message: '请选择是否问题', trigger: 'change'}];
                    _that.rules2.mustAnswer = [];
                    _that.rules2.answerType = [];
                    _that.rules2.useEl = [{required: true, message: '请选择是否启用El', trigger: 'change'}];
                    _that.rules2.stateEl = [];
                }
                _that.rightRowData = {
                    factorId: data.factorId,
                    navId: data.navId,
                    factorName: data.factorName,
                    useEl: data.useEl,
                    stateEl: data.stateEl,
                    isQuestion: data.isQuestion,
                    answerType: data.answerType,
                    mustAnswer: data.mustAnswer,
                    parentFactorId: data.parentFactorId,
                    sortNo: data.sortNo,
                    factorMemo: data.factorMemo,
                    isActive: data.isActive,
                    themeId: data.themeId,
                    themeName: data.themeName,
                };
            }else{
                _that.initNavStateSortNo = 1;
                request('aplanmis',{
                    url: ctx + 'aea/par/factor/getMaxSortNo.do',
                    type: 'post',
                    data: {'navId': _that.navId},
                    async: false,
                },function (data) {
                    _that.initNavStateSortNo = data;
                }, function(msg){});
                this.rightRowData = {
                    factorId: '',
                    navId: _that.navId,
                    factorName:'',
                    useEl:'0',
                    stateEl:'',
                    isQuestion:'1',
                    answerType:'',
                    mustAnswer:'',
                    parentFactorId:'',
                    sortNo: _that.initNavStateSortNo? _that.initNavStateSortNo:1,
                    factorMemo:'',
                    isActive:'1',
                    themeId:'',
                    themeName: '',
                };
                _that.isQuestionShow = true;
                _that.useElShow = false;
                _that.isThemeShow = false;
                _that.rules2.isQuestion = [{required: true, message: '请选择是否问题', trigger: 'change'}];
                _that.rules2.mustAnswer = [{required: true, message: '请选择是否回答', trigger: 'change'}];
                _that.rules2.answerType = [{required: true, message: '请选择回答方式', trigger: 'change'}];
                _that.rules2.useEl = [];
                _that.rules2.stateEl = [];
            }
        },
        addChildState: function(data){

            var _that = this;
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            if (data) {
                _that.dialogTitie2 = (data.isQuestion=='1'?'新增【答】情形':'新增【问】情形');
                _that.dialogEditTable2 = true;
                if(data.isQuestion=='1'){   // 问题时新增答案
                    _that.isQuestionShow = false;
                    _that.isThemeShow = true;
                    _that.useElShow = true;
                    _that.rules2.isQuestion = [{required: true, message: '请选择是否问题', trigger: 'change'}];
                    _that.rules2.mustAnswer = [];
                    _that.rules2.answerType = [];
                    _that.rules2.useEl = [{required: true, message: '请选择是否启用El', trigger: 'change'}];
                    _that.rules2.stateEl = [];
                }else{  // 答案时新增问题
                    _that.isQuestionShow = true;
                    _that.isThemeShow = false;
                    _that.useElShow = false;
                    _that.rules2.isQuestion = [{required: true, message: '请选择是否问题', trigger: 'change'}];
                    _that.rules2.mustAnswer = [{required: true, message: '请选择是否回答', trigger: 'change'}];
                    _that.rules2.answerType = [{required: true, message: '请选择回答方式', trigger: 'change'}];
                    _that.rules2.useEl = [];
                    _that.rules2.stateEl = [];
                }
                _that.initNavStateSortNo = 1;
                request('aplanmis',{
                    url: ctx + 'aea/par/factor/getMaxSortNo.do',
                    type: 'post',
                    data: {'navId': _that.navId},
                    async: false,
                },function (data) {
                    _that.initNavStateSortNo = data;
                }, function(msg){});
                this.rightRowData = {
                    factorId: '',
                    navId: _that.navId,
                    factorName:'',
                    useEl:'0',
                    stateEl:'',
                    isQuestion: (data.isQuestion=='1'?'0':'1'),
                    answerType:'',
                    mustAnswer:'',
                    parentFactorId: data.factorId,
                    sortNo: _that.initNavStateSortNo? _that.initNavStateSortNo:1,
                    factorMemo:'',
                    isActive:'1',
                    themeId:'',
                    themeName: '',
                };
            }else{
                _that.$message({message: '无法获取数据!', type: 'error'});
            }
        },
        handleRightSelChange: function (val) {

            var ids = [];
            val.map(function (item) {
                ids.push(item.factorId)
            });
            this.selectedRightIds = ids;
            return this.selectedRightIds;
        },
        saveRightRowData: function(refsD, rowData){

            var _that = this;
            this.$refs[refsD].validate(function(valid) {
                if (valid) {
                    _that.submitRightRowData(rowData);
                } else {
                    alertMsg('','请输入完整信息','关闭','warning',true);
                    this.dialogEditTable2 = true;
                    return false;
                }
            });
        },
        submitRightRowData: function(data){

            var _that = this;
            request('aplanmis',{
                url: ctx + 'aea/par/factor/saveOrUpdateAeaParFactor.do',
                data:data
            },function () {
                _that.$message({message: '保存成功', type: 'success'});
                _that.dialogEditTable2 = false;
                _that.loadNavStateData(_that.navId);
            }, function(){
                alertMsg('','加载失败','关闭','error',true);
            });
        },
        delAeditNavState: function(data){

            var _that = this;
            if (data && data.factorId) {
                var delData = {
                    'id': data.factorId
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/factor/deleteAeaParFactorById.do',
                        type: 'post',
                        data: delData
                    },function () {

                        _that.$message({message: '删除成功', type: 'success'});
                        _that.loadNavStateData(_that.navId);

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }else{
                alertMsg('', '请选择一条记录!', '关闭', 'info', true);
            }
        },
        batchDelAeditNavState: function (){

            var _that = this;
            var ids = _that.selectedRightIds;
            if ((ids == null || ids.length == 0 || ids == 'undefined')) {
                alertMsg('', '请选择数据!', '关闭', 'error', true);
            } else {
                var delData = {
                    'ids': ids.toString()
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/factor/batchDelAeaParFactorByIds.do',
                        data: delData
                    },function () {

                        _that.$message({message: '批量删除成功', type: 'success'});
                        _that.loadNavStateData(_that.navId);

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }
        },
        showBindingTheme: function(themeId){

            var _that = this;
            _that.dialogTitie3 = '查看主题';
            _that.dialogEditTable3 = true;
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            request('aplanmis',{
                url: ctx + '/aea/par/theme/getAeaParTheme.do',
                type: 'POST',
                data: {'id': themeId},
                async: false,
            },function (data) {
                if (data ) {
                    for (var key in data) {
                        _that.themeData[key] = data[key];
                    }
                } else {
                    _that.themeData = {
                        themeName: '',
                        themeCode: '',
                        themeMemo: '',
                    };
                }
            }, function(){
                alertMsg('', '获取数据失败', '关闭', 'error', true);
            });
        },
        unbindTheme: function(factorId){

            var _that = this;
            confirmMsg('','确认解除主题绑定？',function(){
                request('aplanmis',{
                    url: ctx + 'aea/par/factor/unbindTheme.do',
                    type: 'POST',
                    data: {
                        'factorId': factorId
                    },
                },function (data) {
                    if (data && data.success) {
                        alertMsg('', '解除主题绑定成功', '关闭', 'success', true);
                        _that.loadNavStateData(_that.navId);
                    }
                }, function(){

                    alertMsg('', '解除主题绑定失败', '关闭', 'error', true);
                });
            },'','确定','取消');
        },
        isNullOrBank: function(value){

            if(typeof value == "undefined" || value == null || value == ""){
                return true;
            }else{
                return false;
            }
        },
        selectThemeTree: function (data) {
            var _that = this;
            _that.dialogSetTheme = true;
            _that.themeFilterText = '';
            _that.selThemeList = [];
            if(data){
               var selThemeData = {
                   'id': data.themeId,
                   'label': data.themeName,
                   'type': 'theme'
               }
                _that.selThemeList.push(selThemeData);
            }
            _that.loadTheme();
        },
        loadTheme: function(){
            var _that = this;
            _that.setThemeTreeData = [];
            request('aplanmis', {
                url: ctx + 'aea/par/theme/gtreeThemeForEUi.do',
                type: 'get',
                data: {keyword: _that.themeFilterText}
            }, function (data) {
                _that.setThemeTreeData = data;
                if (data!=null&&data.length>0) {
                    _that.setThemeRootNodeId.push(data[0].id);
                }
            }, function () {
                _that.setThemeTreeData = [];
            });
        },
        themeSearch: function () {
            var _that = this;
            _that.loadTheme();
        },
        clearThemeSearch: function () {
            var _that = this;
            _that.themeFilterText = '';
            _that.selThemeList = [];
            _that.loadTheme();
        },
        selTheme: function (data, node, event) {

            if(data&&data.id!='root'){
                var _that = this;
                _that.selThemeList = [];
                _that.selThemeList.push(data);
            }else{
                return ;
            }
        },
        removeSelTheme: function () {
            var _that = this;
            _that.selThemeList = [];
        },
        saveSelTheme: function () {
            var _that = this;
            if (_that.selThemeList.length > 0) {
                _that.rightRowData.themeId = _that.selThemeList[0].id;
                _that.rightRowData.themeName = _that.selThemeList[0].label;
            }
            _that.dialogSetTheme = false;
        },
    },
});

