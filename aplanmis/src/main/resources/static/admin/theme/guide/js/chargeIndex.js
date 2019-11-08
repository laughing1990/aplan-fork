var vm = new Vue({
    el: '#index',
    data: {
        formLabelWidth: '30%',
        pageSize: 10,
        page: 1,
        loading: true,
        searchKey: null,
        multipleSelection: '',
        selectedIds: [],
        selRowData: {
            chargeId: '',
            stageId: '',
            feeName: '',
            feeOrg: '',
            feeType: '1',
            feeTypeText: '',
            bylaw: '',
            isDesc: '0',
            isDescText: '',
            descExplain: '',
            remark: '',
            sortNo: '',
            feeStand: '',
            descLaw: '',
        },
        dialogEditTable: false,
        dialogTitie: '',
        dialogTitieFlag: '',
        tableData: {},
        rules: {
            feeName:[
                { required: true, message: '请输入收费名目', trigger: 'blur' },
                { min: 1, max: 200, message: '长度最大200个字符', trigger: 'blur' }
            ],
            feeOrg:[
                { required: true, message: '请输入收费主体', trigger: 'blur' },
                { min: 1, max: 2000, message: '长度最大2000个字符', trigger: 'blur' }
            ],
            sortNo: [
                { required: true, message: '请输入收费排序', trigger: 'blur' },
                { type: 'number', message: '收费排序必须为数字值'}
            ],
            feeType: [
                { required: true, message: '请输入收费性质', trigger: 'change' },
            ],
            isDesc: [
                { required: true, message: '请输入是否允许减免', trigger: 'change' },
            ],
            feeStand: [
                { required: true, message: '请输入收费标准', trigger: 'blur' },
            ],
            bylaw: [
                { required: true, message: '请输入收费依据', trigger: 'blur' },
            ],
            descExplain:[],
            descLaw: [],
        },
        initSortNo: 1,
        isShow: true,
        curIsEditable: curIsEditable
    },
    mounted: function () {

        this.getDataTable();
    },
    methods:{
        getDataTable: function(props){

            var _that = this;
            _that.tableData = {};

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
            }

            var prop = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize,
                keyword: this.searchKey,
                stageId: stageId,
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/par/stage/charges/page.do',
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
        editRow: function(rowData, dialogTitie){

            var _that = this;
            _that.dialogTitieFlag = dialogTitie;
            dialogTitie ? (curIsEditable?this.dialogTitie = '编辑收费信息':this.dialogTitie = '查看收费信息'): this.dialogTitie = '新增收费信息';
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            if(!curIsEditable&&!dialogTitie){
                _that.dialogEditTable = false;
                _that.isShow = false;
                alertMsg('', '当前事项版本下数据不可编辑', '关闭', 'info', true);
            }else{
                _that.dialogEditTable = true;
                if(curIsEditable){
                    _that.isShow = true;
                }else{
                    _that.isShow = false;
                }
            }
            if(rowData){
                for (var key in this.selRowData) {
                    this.selRowData[key] = rowData[key];
                }
                if(this.selRowData.isDesc=='1'){
                    _that.rules.descExplain = [{
                        required: true, message: '请输入收费减免情形', trigger: 'blur'
                    }];
                    _that.rules.descLaw = [{
                        required: true, message: '请输入减免情形依据', trigger: 'blur'
                    }];
                }else{
                    _that.rules.descExplain = [];
                    _that.rules.descLaw = [];
                }
            }else {
                _that.initSortNo = 1;
                request('aplanmis',{
                    url: ctx + 'aea/par/stage/charges/getMaxSortNo.do',
                    type: 'post',
                    data: {'stageId': stageId},
                    async: false,
                },function (data) {
                    _that.initSortNo = data;
                }, function(msg){});
                this.selRowData = {
                    chargeId: '',
                    stageId: stageId,
                    feeName: '',
                    feeOrg: '',
                    feeType: '1',
                    feeTypeText: '',
                    bylaw: '',
                    isDesc: '0',
                    isDescText: '',
                    descExplain: '',
                    remark: '',
                    sortNo:  _that.initSortNo? _that.initSortNo:1,
                    feeStand: '',
                    descLaw: '',
                };
            }
        },
        handleSelChange: function (val) {

            this.multipleSelection = val;
            var ids = [];
            this.multipleSelection.map(function (item) {
                ids.push(item.chargeId)
            })
            this.selectedIds = ids;
            return this.selectedIds;
        },
        deleteData: function(data){

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }

            var _that = this;
            if (data && data.chargeId) {
                var delData = {
                    'id': data.chargeId
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/stage/charges/delChargesById.do',
                        type: 'post',
                        data: delData
                    },function () {

                        _that.$message({message: '删除成功!', type: 'success'});
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
                        url: ctx + 'aea/par/stage/charges/batchDelChargesByIds.do',
                        data: delData
                    },function () {

                        _that.$message({message: '批量删除成功!', type: 'success'});
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
                stageId: stageId
            };
            this.getDataTable(propSearch)
        },
        dbclickRow: function(row){

            this.editRow(row, 1);
        },
        clickRow: function(row, column, event){

            this.$refs.chargesTb.toggleRowSelection(row);
        },
        clearFormData: function(){

            this.$refs['selRowData'].resetFields();
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
            request('aplanmis',{
                url: ctx + 'aea/par/stage/charges/saveCharges.do',
                data:data
            },function () {
                _that.$message({message: '保存成功', type: 'success'});
                _that.dialogEditTable = false;
                _that.getDataTable();
            }, function(){
                alertMsg('','加载失败','关闭','error',true);
            });
        },
        feeTypeFunc: function(value){
            var _that = this;
            _that.selRowData.feeType = value;
            if(value=='1'){
                _that.selRowData.feeTypeText = '行政事业性收费';
            }else{
                _that.selRowData.feeTypeText = '经营服务性收费';
            }
        },
        isDescFunc: function(value){
            var _that = this;
            _that.selRowData.isDesc = value;
            if(value=='1'){
                _that.selRowData.isDescText = '允许';
                _that.rules.descExplain = [{
                    required: true, message: '请输入收费减免情形', trigger: 'blur'
                }];
                _that.rules.descLaw = [{
                    required: true, message: '请输入减免情形依据', trigger: 'blur'
                }];
            }else{
                _that.selRowData.isDescText = '不允许';
                _that.rules.descExplain = [];
                _that.rules.descLaw = [];
            }
        }
    }
});