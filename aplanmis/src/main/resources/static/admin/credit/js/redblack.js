var vm = new Vue({
    el: '#index',
    data: {
        formLabelWidth: '30%',
        pageSize: 10,
        page: 1,
        loading: true,
        searchKey: null,
        redblackTypeS: null,
        multipleSelection: '',
        selectedIds: [],
        selRowData: {
            redblackId: '',
            isBlack: '',
            redblackType: '',
            unitInfoId: '',
            applicant: '',
            linkmanInfoId: '',
            linkmanName:'',
            redblackReason: '',
            redblackLevel:'',
            creditUnit: '',
            creditBasis: '',
            affirmTime: '',
            includeTime: '',
            endTime: '',
            isValid: '',
            invalidReason: '',
            isSync: '',
            syncSource:'',
            syncTime:'',
            rootOrgId: ''
        },
        dialogEditTable: false,
        dialogTitie: '',
        dialogTitieFlag: '',
        tableData: {},
        rules: {
            redblackType: [
                {required: true, message: '请选择对象类型', trigger: 'change'}
            ],
            isBlack:[
                {required: true, message: '请选择是否黑名单' , trigger: 'change'}
            ],
            applicant: [
                {required: true, message: '请选择企业单位', trigger: 'blur'}
            ],
            linkmanName: [
                {required: true, message: '请选择联系人', trigger: 'blur'}
            ],
            redblackReason: [
                {required: true, message: '请输入列入名单原因', trigger: 'blur'}
            ],
            creditUnit: [
                {required: true, message: '请输入认证机关', trigger: 'blur'}
            ],
            creditBasis: [
                {required: true, message: '请输入认证依据', trigger: 'blur'}
            ],
            affirmTime: [
                {required: true, message: '请选择认证时间', trigger: 'blur'}
            ],
            includeTime: [
                {required: true, message: '请选择列入红黑名单时间', trigger: 'blur'}
            ],
            endTime: [
                {required: true, message: '请选择截止时间', trigger: 'blur'}
            ],
            isValid: [
                {required: true, message: '请选择是否有效', trigger: 'change'}
            ],
            isSync: [
                {required: true, message: '请选择是否同步', trigger: 'change'}
            ],
        },
        defaultProps:{
            label: 'name',
            isLeaf: 'leaf'
        },
        // 设置企业单位
        inputUnit: true,
        dialogSetUnit: false,
        setUnitRootNodeId: [],  // 设置企业单位树根元素id
        unitFilterText: '',
        setUnitTreeData: [],
        selSetUnitCheckedList: [],
        searchUnitTree: [],
        baseSeachUnitList: [],
        setUnitExpanded: [],
        setUnitList: [],
        selUnitList: [],
        expandAllUnitNode: false,
        setUnitSearchFlag: true,
        // 设置联系人
        inputLinkman: false,
        dialogSetLinkman: false,  // 是否展示设置联系人窗口
        setLinkmanRootNodeId: [],  // 设置联系人树根元素id
        linkmanFilterText: '',
        setLinkmanTreeData: [],
        selSetLinkmanCheckedList: [],
        searchLinkmanTree: [],
        baseSeachLinkmanList: [],
        setLinkmanExpanded: [],
        setLinkmanList: [],
        selLinkmanList: [],
        expandAllLinkmanNode: false,
        setLinkmanSearchFlag: true,
        fullHeight: document.documentElement.clientHeight, // 全屏高度
        treeLeftHeight: document.documentElement.clientHeight - 100, // 左侧树 的高度
        readonly: true,
        inputSyncFiled: false,
        inputInvalidReason: false,
    },
    mounted: function () {
        this.getDataTable();
        this.dialogSetLinkman = false;
        var that = this;
        window.addEventListener("resize", function () {
            return (function () {
                window.fullHeight = document.documentElement.clientHeight
                that.fullHeight = window.fullHeight;
                that.treeLeftHeight = that.fullHeight - 100;
            })();
        });
    },
    methods:{
        validUnitInfo: function (rule, value, callback) {
            alert(value)
            if (!value) {
                callback(new Error("请选择企业单位"));
            }
        },
        getDataTable: function(props){
            var _that = this;
            var prop = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize,
                keyword: this.searchKey,
                redblackType: this.redblackTypeS,
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/credit/redblack/page.do',
                type: 'post',
                data: props
            },function (data) {

                _that.tableData = data;
                _that.loading = false;

            }, function(msg){

                alertMsg('','加载失败','关闭','error',true);
                _that.loading = false;
            });
        },
        redblackTypeSearch: function(val){

            this.redblackTypeS = val;
            this.getDataTable();
        },
        editRow: function(rowData, dialogTitie){

            var _that = this;
            _that.dialogTitieFlag = dialogTitie;
            dialogTitie ? this.dialogTitie = '编辑红黑名单': this.dialogTitie = '新增红黑名单';
            this.dialogEditTable = true;
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            if(rowData){
                this.selRowData = {
                    redblackId: rowData.redblackId,
                    isBlack: rowData.isBlack,
                    redblackType: rowData.redblackType,
                    unitInfoId: rowData.unitInfoId,
                    applicant: rowData.applicant,
                    linkmanInfoId: rowData.linkmanInfoId,
                    linkmanName: rowData.linkmanName,
                    redblackReason: rowData.redblackReason,
                    redblackLevel: rowData.redblackLevel,
                    creditUnit: rowData.creditUnit,
                    creditBasis: rowData.creditBasis,
                    affirmTime: rowData.affirmTime,
                    includeTime: rowData.includeTime,
                    endTime: rowData.endTime,
                    isValid: rowData.isValid,
                    invalidReason: rowData.invalidReason,
                    isSync: rowData.isSync,
                    syncSource: rowData.syncSource,
                    syncTime: rowData.syncTime,
                    rootOrgId: rowData.rootOrgId
                };
                if (this.selRowData.redblackType == 'u') {
                    this.inputUnit = true;
                    this.inputLinkman = false;
                    this.selUnitList = [];
                    this.selUnitList.push({
                        id: rowData.unitInfoId,
                        label: rowData.applicant
                    });
                } else {
                    this.inputUnit = false;
                    this.inputLinkman = true;
                    this.selLinkmanList = [];
                    this.selLinkmanList.push({
                        id: rowData.linkmanInfoId,
                        label: rowData.linkmanName
                    });
                }
                if(this.selRowData.isValid == '1'){
                    this.inputInvalidReason = false;
                }else{
                    this.inputInvalidReason = true;
                }

                if(this.selRowData.isSync == '1'){
                    this.inputSyncFiled = true;
                }else{
                    this.inputSyncFiled = false;
                }
            }else {
                this.selRowData = {
                    redblackId: '',
                    isBlack: '0',
                    redblackType: 'u',
                    unitInfoId: '',
                    applicant: '',
                    linkmanInfoId: '',
                    linkmanName:'',
                    redblackReason: '',
                    redblackLevel:'',
                    creditUnit: '',
                    creditBasis: '',
                    affirmTime: '',
                    includeTime: '',
                    endTime: '',
                    isValid: '1',
                    invalidReason: '',
                    isSync: '0',
                    syncSource:'',
                    syncTime:'',
                    rootOrgId: ''
                };
                this.inputUnit = true;
                this.inputLinkman = false;
                this.inputInvalidReason = false;
                this.inputSyncFiled = false;
                this.selUnitList = [];
                this.selLinkmanList = [];
            }
        },
        handleSelChange: function (val) {

            this.multipleSelection = val;
            var ids = [];
            this.multipleSelection.map(function (item) {
                ids.push(item.redblackId)
            })
            this.selectedIds = ids;
            return this.selectedIds;
        },
        deleteData: function(data){

            var _that = this;
            if (data && data.redblackId) {
                var delData = {
                    'id': data.redblackId
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/credit/redblack/deleteById.do',
                        type: 'post',
                        data: delData
                    },function () {

                        alertMsg('', '删除成功', '关闭', 'success', true);
                        _that.getDataTable();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }else{
                alertMsg('', '请选择一条记录!', '关闭', 'info', true);
            }
        },
        batchDelData: function(){

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
                        url: ctx + 'aea/credit/redblack/deleteByIds.do',
                        data: delData
                    },function () {

                        alertMsg('', '删除成功', '关闭', 'success', true);
                        _that.getDataTable();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }
        },
        handleSizeChange: function(val) {

            this.pageSize = val;
            this.getDataTable();
        },
        handleCurrentChange: function(val) {

            this.page = val;
            this.getDataTable();
        },
        searchRow: function(){

            var propSearch = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize,
                keyword: this.searchKey,
                redblackType: this.redblackTypeS,
            };
            this.getDataTable(propSearch)
        },
        dbclickRow: function(row){

            this.editRow(row, 1);
        },
        clearFormData: function(){

            this.$refs['selRowData'].resetFields();
        },
        dateFormatByExp: function(date, fmt){

            var o = {
                "M+" : date.getMonth()+1,                 //月份
                "d+" : date.getDate(),                    //日
                "h+" : date.getHours(),                   //小时
                "m+" : date.getMinutes(),                 //分
                "s+" : date.getSeconds(),                 //秒
                "q+" : Math.floor((date.getMonth()+3)/3), //季度
                "S"  : date.getMilliseconds()             //毫秒
            };
            if(/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for(var k in o) {
                if(new RegExp("("+ k +")").test(fmt)){
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
                }
            }
            return fmt;
        },
        formatOperTime: function(value, exp) {

            return this.dateFormatByExp(new Date(value), "yyyy-MM-dd hh:mm:ss");//直接调用公共JS里面的时间类处理的办法
        },
        isNullOrBank: function(value){

            if(typeof value == "undefined" || value == null || value == ""){
                return true;
            }else{
                return false;
            }
        },
        saveEdit: function(refsD, rowData){

            var _that = this;
            this.$refs[refsD].validate(function(valid) {
                if (valid) {
                    _that.submitEdit(rowData);
                } else {
                    alertMsg('','请输入完整信息','关闭','warning',true);
                    this.dialogEditTable = true;
                    return false;
                }
            });
        },
        submitEdit: function(data){

            var _that = this;
            request('aplanmis',{ url: ctx + 'aea/credit/redblack/save.do', data:data },function () {
                alertMsg('','保存成功','关闭','success',true);
                _that.dialogEditTable = false;
                _that.getDataTable();
            }, function(){
                alertMsg('','加载失败','关闭','error',true);
            });
        },
        changeEnableType:function(data){

            data.isValid = data.isValid == 0 ? 1 : 0
        },
        // 加载选择单位树数据
        loadUnit: function () {
            var _that = this;
            _that.setUnitTreeData = [];
            request('aplanmis', {
                url: ctx + 'aea/credit/redblack/gtreeUnitInfoForEui.do',
                type: 'get',
                data: {keyword: _that.unitFilterText}
            }, function (data) {
                if (data.success) {
                    _that.setUnitTreeData = data.content;
                    if (data.content[0].id) {
                        _that.setUnitRootNodeId.push(data.content[0].id);
                    }
                }
            }, function () {
                _that.setUnitTreeData = [];
            });
        },
        // 加载选择联系人树数据
        loadLinkman: function () {
            var _that = this;
            _that.setLinkmanTreeData = [];
            request('aplanmis', {
                url: ctx + 'aea/credit/redblack/gtreeLinkmanForEui.do',
                type: 'get',
                data: {keyword: _that.linkmanFilterText}
            }, function (data) {
                if (data.success) {
                    _that.setLinkmanTreeData = data.content;
                    if (data.content[0].id) {
                        _that.setLinkmanRootNodeId.push(data.content[0].id);
                    }
                }
            }, function () {
                _that.setLinkmanTreeData = [];
            });
        },
        // 设置联系人树
        setLinkman: function () {
            var _that = this;
            _that.setLinkmanExpanded = [];
            _that.selSetLinkmanCheckedList = [];
            _that.setLinkmanList = [];
            _that.closedAllLinkmanTree();
            _that.setLinkmanExpanded.push(_that.setLinkmanRootNodeId);
            var setLinkmanExpandedStr = '';
            request('aplanmis', {
                url: ctx + 'aea/credit/redblack/gtreeUnitInfoForEui.do',
                type: 'get'
            }, function (data) {
                if (data.success) {
                    _that.setLinkmanList = data.content;
                }
                _that.$refs.setAdminTree.setCheckedKeys(_that.selSetAdminCheckedList, false);
            }, function (msg) {
                _that.$message({
                    message: msg.message ? msg.message:'请求失败！',
                    type: 'error'
                });
            });
        },
        clearSearchLinkman: function () {
            this.linkmanfilterText = '';
            this.setLinkSearchFlag = true;
        },
        expandAllLinkmanTree: function () {
            var _that = this;
            this.expandAllLinkmanNode = true;
            _that.setLinkmanExpanded = [];
            for (var i = 0; i < this.$refs.setLinkmanTree.store._getAllNodes().length; i++) {
                this.$refs.setLinkmanTree.store._getAllNodes()[i].expanded = this.expandAllLinkmanNode;
                if (this.$refs.setLinkmanTree.store._getAllNodes()[i].data.id) {
                    _that.setLinkmanExpanded.push(this.$refs.setLinkmanTree.store._getAllNodes()[i].data.id)
                }
            }
        },
        closedAllLinkmanTree: function () {
            var _that = this;
            this.expandAllLinkmanNode = false;
            for (var i = 0; i < this.$refs.setLinkmanTree.store._getAllNodes().length; i++) {
                this.$refs.setLinkmanTree.store._getAllNodes()[i].expanded = this.expandAllLinkmanNode;
                _that.setLinkmanExpanded = [];
            }
        },
        editUnit: function (unitId) {
            var _that = this;
            _that.dialogSetUnit = true;
            _that.loadUnit();
        },
        unitSearch: function () {
            var _that = this;
            _that.loadUnit();
        },
        clearUnitSearch: function () {
            var _that = this;
            _that.unitFilterText = '';
            _that.loadUnit();
        },
        selUnit: function (data, node, event) {

            if(data&&data.id!='root'){
                var _that = this;
                _that.selUnitList = [];
                _that.selUnitList.push(data);
            }else{
                return ;
            }
        },
        removeSelUnit: function () {
            var _that = this;
            _that.selUnitList = [];
        },
        saveSelUnit: function () {
            var _that = this;
            if (_that.selUnitList.length > 0) {
                _that.selRowData.unitInfoId = _that.selUnitList[0].id;
                _that.selRowData.applicant = _that.selUnitList[0].label;
            }
            _that.dialogSetUnit = false;
        },
        editLinkman: function (linkmanId) {
            var _that = this;
            _that.dialogSetLinkman = true;
            _that.loadLinkman()
        },
        linkmanSearch: function () {
            var _that = this;
            _that.loadLinkman();
        },
        clearLinkmanSearch: function () {
            var _that = this;
            _that.linkmanFilterText = '';
            _that.loadLinkman();
        },
        selLinkman: function (data, node, event) {

            if(data&&data.id!='root'){
                var _that = this;
                _that.selLinkmanList = [];
                _that.selLinkmanList.push(data);
            }else{
                return ;
            }
        },
        removeSelLinkman: function () {
            var _that = this;
            _that.selLinkmanList = [];
        },
        saveSelLinkman: function () {
            var _that = this;
            if (_that.selLinkmanList.length > 0) {
                _that.selRowData.linkmanInfoId = _that.selLinkmanList[0].id;
                _that.selRowData.linkmanName = _that.selLinkmanList[0].label;
            }
            _that.dialogSetLinkman = false;
        },
        redblackTypeEdit: function (val) {
            var _that = this;
            _that.selRowData.redblackType = val
            if (_that.selRowData.redblackType == "u") {
                _that.inputUnit = true;
                _that.inputLinkman = false;
            } else {
                _that.inputUnit = false;
                _that.inputLinkman = true;
            }
        },
        isValidEdit: function (val) {
            var _that = this;
            _that.selRowData.isValid = val;
            if (_that.selRowData.isValid == "0") {
                _that.inputInvalidReason = true;
            } else {
                _that.inputInvalidReason = false;
            }
        },
        isSyncEdit: function(val) {

            var _that = this;
            _that.selRowData.isSync = val;
            if (_that.selRowData.isSync == "0") {
                _that.inputSyncFiled = false;
            } else {
                _that.inputSyncFiled = true;
            }
        },
        changeIsValid: function (dataSend, e) {
            e.cancelBubble = true;
            var _that = this;
            request('aplanmis', {
                url: ctx + 'aea/credit/redblack/enOrDisableIsValid.do',
                type: 'get',
                data: {'redblackId': dataSend.redblackId}
            }, function (data) {
                if (data.success) {
                    _that.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    _that.getDataTable();
                } else {
                    _that.$message({
                        message: data.message ? data.message : '修改失败，请稍后再试',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: msg.message ? msg.message : '服务请求失败',
                    type: 'error'
                });
            });
        },
    }
});