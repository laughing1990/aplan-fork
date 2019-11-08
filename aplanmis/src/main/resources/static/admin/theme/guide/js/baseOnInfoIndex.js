var vm = new Vue({
    el: '#index',
    data: {
        // 事项窗口
        page: 1,
        pageSize: 10,
        loading: true,
        searchKey: null,
        multipleSelection: '',
        selectedIds: [],
        tableData: {},
        // 法律法规
        legalClauseData:{
            legalName: '',
            legalLevel: '',
            basicNo: '',
            issueOrg: '',
            exeDate: '',
            clauseTitle: '',
            clauseContent: '',
            serviceLegalMemo: ''
        },
        dialogIsShow: false,
        readonly: true,
        filterText: '',
        dialogSetLegal: false,
        setLegalTreeData: [],
        legalRootNodeId: '', // 选择树根元素ID
        setLegalExpanded: [],
        defaultLegalProps: {
            children: 'children',
            label: 'label'
        },
        fullHeight: document.documentElement.clientHeight, // 全屏高度
        curIsEditable: curIsEditable
    },
    mounted: function () {

        var _that = this;
        _that.dialogSetLegal = false;
        window.addEventListener("resize", function () {
            return (function () {
                window.fullHeight = document.documentElement.clientHeight
            })();
        });
        _that.loadItemBasics();
        _that.loadLegal();
    },
    methods:{
        loadItemBasics: function(props){

            var _that = this;
            _that.tableData = {};

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
            }

            var prop = {
                'pageNum': _that.page ? _that.page : 1,
                'pageSize': _that.pageSize ? _that.pageSize : 10,
                'keyword': _that.searchKey,
                'tableName': relTbName,
                'pkName': relPkName,
                'recordId': stageId
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/item/service/basic/listItemBasicByPageForEui.do',
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
        searchRow: function(){

            var _that = this;
            var propSearch = {
                'pageNum': _that.page ? _that.page : 1,
                'pageSize': _that.pageSize ? _that.pageSize : 10,
                'keyword': _that.searchKey,
                'tableName': relTbName,
                'pkName': relPkName,
                'recordId': stageId
            };
            _that.loadItemBasics(propSearch)
        },
        itemBasicClickSel: function(row, column, event){

            var _that = this;
            _that.$refs.itemBasicTb.toggleRowSelection(row);
        },
        itemBasicDblClickSel: function(row, column, event){

            var _that = this;
            _that.viewBasicById(row);
        },
        handleSelChange: function (data) {

            var _that = this;
            _that.multipleSelection = data;
            var ids = [];
            _that.multipleSelection.map(function (item) {
                ids.push(item.basicId);
            })
            _that.selectedIds = ids;
            return _that.selectedIds;
        },
        handleSizeChange: function(value) {

            var _that = this;
            _that.pageSize = value;
            _that.loadItemBasics();
        },
        handleCurrentChange: function(value) {

            var _that = this;
            _that.page = value;
            _that.loadItemBasics();
        },
        clearLegalClauseFormData: function(){

            var _that = this;
            _that.$refs['legalClauseData'].resetFields();
        },
        viewBasicById: function(data){

            var _that = this;
            _that.dialogIsShow = true;
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            if(data){
                request('aplanmis',{
                    url: ctx + 'aea/item/service/basic/getAeaItemServiceBasic.do',
                    type: 'post',
                    data: { 'id': data.basicId },
                },function (data) {
                    for (var key in _that.legalClauseData) {
                        _that.legalClauseData[key] = data[key];
                    }
                }, function(){

                    alertMsg('', '加载条款数据失败!', '关闭', 'error', true);
                });
            }else{
                alertMsg('', '请选择一条记录!', '关闭', 'error', true);
            }
        },
        deleteData: function(basicId){

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }
            var _that = this;
            if (basicId) {
                var data = {
                    'id': basicId
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/item/service/basic/deleteAeaItemServiceBasicById.do',
                        type: 'POST',
                        data: data
                    },function () {

                        _that.$message({message: '删除成功', type: 'success'});
                        _that.loadItemBasics();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }else{
                alertMsg('', '请选择一条记录!', '关闭', 'info', true);
            }
        },
        batchDelData: function(){

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }
            var _that = this;
            var ids = _that.selectedIds;
            if ((ids == null || ids.length == 0 || ids == 'undefined')) {
                alertMsg('', '请选择数据!', '关闭', 'error', true);
            } else {
                var delData = {
                    'ids': ids.toString()
                };
                confirmMsg('','确定要批量删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/item/service/basic/batchDeleteServiceBasic.do',
                        type: 'POST',
                        data: delData
                    },function () {

                        _that.$message({message: '批量删除成功', type: 'success'});
                        _that.loadItemBasics();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }
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

            var _that = this;
            return _that.dateFormatByExp(new Date(value), exp);
        },
        isNullOrBank: function(value){

            if(typeof value == "undefined" || value == null || value == ""){
                return true;
            }else{
                return false;
            }
        },
        formatClauseContent: function(value){

            var _that = this;
            if(_that.isNullOrBank(value)){
                return '';
            }else{
                if(value.length>100){
                    return value.substr(0, 100)+'...';
                }else{
                    return value;
                }
            }
        },
        openSelectTree: function(){

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }

            var _that = this;
            _that.dialogSetLegal = true;
            _that.filterText = '';
            $(".el-tree").animate({scrollTop: 0}, 800);//滚动到顶部
            _that.setLegalExpanded = [];
            _that.setLegalExpanded.push(_that.legalRootNodeId);
            if(_that.setLegalTreeData&&_that.setLegalTreeData.length>0){

            }else{
                _that.loadLegal();
            }
        },
        loadSelectedClause: function(){

            var _that = this;
            request('opus-admin', {
                url: ctx + 'aea/item/service/basic/listAeaItemServiceBasicByRecordId.do',
                type: 'post',
                data: {
                    'tableName': relTbName,
                    'pkName': relPkName,
                    'recordId': stageId
                },
                async: false,
            }, function (data) {
                if(data&&data.length>0){
                    var ids = [];
                    for(var i in data){
                        ids.push(data[i].legalClauseId);
                    }
                    _that.$refs.setLegalTree.setCheckedKeys(ids);
                }
            }, function () {

                alertMsg('', '获取事项设立依据数据失败!', '关闭', 'error', true);
            });
        },
        loadLegal: function () {

            var _that = this;
            _that.setLegalTreeData = [];
            _that.legalRootNodeId = null;

            request('opus-admin', {
                url: ctx + 'aea/service/legal/gtreeLegalAndClauseForEui.do',
                type: 'post',
                data: {},
                async: false,
            }, function (data) {
                if(data&&data.length>0){

                    data[0].disabled = true;
                    _that.setLegalTreeData = data;
                    _that.legalRootNodeId = data[0].id;
                }
            }, function () {

                _that.setLegalTreeData = [];
                _that.legalRootNodeId = null;
            });
        },
        clearSelLegalData: function(){

            var _that = this;
            _that.filterText = '';
            _that.$refs.setLegalTree.setCheckedKeys([]);

        },
        filterLegalNode: function(value, data) {

            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        nodeExpand: function(data, node, event){

            var _that = this;
            if(data.type=='clause'){
                data.disabled = false;
                _that.$refs.setLegalTree.setChecked(data, node.checked?false:true, false);
            }else{
                data.disabled = true;
                _that.$refs.setLegalTree.setChecked(data, false, false);
            }
            return data;
        },
        nodeClick: function(data, node, obj){

            var _that = this;
            if(data.type=='clause'){
                data.disabled = false;
                _that.$refs.setLegalTree.setChecked(data, node.checked?false:true, false);
            }else{
                data.disabled = true;
                _that.$refs.setLegalTree.setChecked(data, false, false);
            }
            return data;
        },
        nodeCheck: function(data, node){

            var _that = this;
            if(data.type=='clause'){
                data.disabled = false;
            }else{
                data.disabled = true;
                _that.$refs.setLegalTree.setChecked(data, false, false);
            }
            return data;
        },
        nodeCheckChange: function(data, isChecked, childNodes){

            var _that = this;
            if(data.type=='clause'){
                data.disabled = false;
            }else{
                data.disabled = true;
                _that.$refs.setLegalTree.setChecked(data, false, false);
            }
            return data;
        },
        saveSelLegal: function(){

            var _that = this;
            var ids = _that.$refs.setLegalTree.getCheckedKeys();
            var d = {};
            d['tableName'] = relTbName;
            d['pkName'] = relPkName;
            d['recordId'] = stageId;
            d.legalClauseId = '';
            for(var i=0;i<ids.length;i++){
                if(i>0){
                    d.legalClauseId += ",";
                }
                d.legalClauseId += ids[i];
            }
            request('opus-admin', {
                url: ctx + 'aea/item/service/basic/batchSaveServiceBasic.do',
                type: 'post',
                data: d,
                async: false,
            }, function (result) {
                if (result.success){

                    _that.dialogSetLegal = false;
                    _that.$message({message: '导入成功!', type: 'success'});
                    _that.loadItemBasics();

                }else {
                    alertMsg('', result.message , '关闭', 'error', true);
                }

            }, function () {
                alertMsg('', '导入成功失败!', '关闭', 'error', true);
            });
        }
    },
    watch: {
        filterText: function (value) {

            var _that = this;
            _that.$refs.setLegalTree.filter(value);
        },
    }
});