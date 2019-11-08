// last edit by zl 20190916
var vm = new Vue({
    el: '#index',
    data: {
        pageSizeLeft: 10,
        pageSizeRight: 10,
        page: 1,
        pageRight: 1,
        selectedIds: [],
        selectedRightIds: [],
        loading1: true,
        loading2: true,
        summaryId: '',
        summaryType: '',
        creditTypeList: [],
        inputUnitInfo: true,
        inputLinkmanInfo: false,
        inputInValidReason: true,
        readonly: true,
        fullHeight: document.documentElement.clientHeight,
        defaultProps: {
            label: 'name',
            isLeaf: 'leaf'
        },
        dialogSelUnit: false,
        selUnitTreeData: false,
        unitFilterText: '',
        selUnitRootNodeId: [],
        selUnitInfoList: [],
        dialogSelLinkman: false,
        selLinkmanTreeData: [],
        linkmanFilterText: '',
        selLinkmanRootNodeId: [],
        selLinkmanInfoList: [],
        selRowData: {
            summaryId: '',
            summaryType: 'u',
            creditUnitInfoId: '',
            creditUnitInfoName: '',
            linkmanInfoId: '',
            linkmanInfoName: '',
            cnTableName: '',
            enTableName: '',
            cnDeptName: '',
            enDeptName: '',
            creditType: '',
            isValid: '1',
            invalidReason: '',
            isSync: '0',
            syncSource: '',
            syncTime: '',
        },
        rightRowData: {
            detailId: '',
            summaryId: '',
            cnColumnName: '',
            enColumnName: '',
            columnValue: '',
            columnStrValue: '',
            columnNumberValue: '',
            columnDateValue: '',
            columnDataType: 's',
            isSync: '0',
            syncSource: '',
            syncTime: '',
        },
        dialogEditTable1: false,
        dialogEditTable2: false,
        dialogTitie: '',
        searchKey: '',
        searchKeyRight: '',
        newChildTree: false,
        rules: {
            creditUnitInfoName: [{required: true, message: '请选择单位', trigger: 'change'}],
            linkmanInfoName: [{required: true, message: '请选择联系人', trigger: 'change'}],
            cnTableName: [{required: true, message: '请输入信用信息（中文）', trigger: 'blur'}],
            enTableName: [{required: true, message: '请输入信用信息（英文）', trigger: 'blur'}],
            cnDeptName: [{required: true, message: '请输入评级单位（中文）', trigger: 'blur'}],
            enDeptName: [{required: true, message: '请输入评级单位（英文）', trigger: 'blur'}],
            creditType: [{required: true, message: '请输入信用类型', trigger: 'blur'}],
            invalidReason: [{required: true, message: '请输入失效原因', trigger: 'blur'}],
            cnColumnName: [{required: true, message: '请输入详细字段中文名', trigger: 'blur'}],
            columnDataType: [{required: true, message: '请输入详细值类型', trigger: 'blur'}],
            columnValue: [{required: true, message: '请输入详细值', trigger: 'blur'}],
            columnStrValue: [{required: true, message: '请输入详细值', trigger: 'blur'}],
            columnNumberValue: [{required: true, message: '请输入详细值', trigger: 'blur'},
                {
                    validator(rule, value, callback) {
                        if (/^(-?\d+)(\.\d+)?$/.test(value)) {
                            callback()
                        } else {
                            callback(new Error('请输入数字值'))
                        }
                    },
                    trigger: 'blur'
                }],
            columnDateValue: [{required: true, message: '请输入详细值', trigger: 'blur'}]

        },
        dataTbRight: {},
        tableData: {},
        searchRightData: {},
        inputSyncFiled: false,
        inputSyncFiled2: false
    },
    mounted: function () {
        this.getListData();
    },
    watch: {
        tableData: function () {
            this.$refs.summaryTable.setCurrentRow(this.tableData.rows[0]);
        }
    },
    methods: {
        getListData: function (props) {
            var _that = this;
            var prop = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSizeLeft ? this.pageSizeLeft : 10,
                summaryType: this.summaryType,
                keyword: this.searchKey ? this.searchKey : '',
                order: 'desc'
            };
            if (!props) props = prop;
            request('aplanmis', {url: ctx + 'aea/credit/summary/page.do', data: props}, function (data) {
                _that.tableData = data;
                _that.searchRightData = _that.tableData.rows[0];
                _that.getRightData(_that.searchRightData);
                _that.loading1 = false;
            }, function (result) {
                alertMsg('', result.message ? result.message : '加载失败', '关闭', 'error', true);
                _that.loading1 = false;
                _that.loading2 = false;
            });
        },
        getRightData: function (data) {
            var _that = this;
            if (data) {
                _that.summaryId = data.summaryId;
                var props = {
                    summaryId: data.summaryId ? data.summaryId : '',
                    pageNum: this.pageRight,
                    pageSize: this.pageSizeRight,
                    order: 'asc',
                    keyword: this.searchKeyRight
                }
                request('aplanmis', {url: ctx + 'aea/credit/detail/page.do', data: props}, function (res) {
                    _that.dataTbRight = res;
                    if (res.rows.length > 0) {
                        _that.rightRowData.summaryId = res.rows[0].summaryId;
                    } else {
                        _that.rightRowData.summaryId = data.summaryId;
                    }
                    _that.loading2 = false;
                }, function (result) {
                    alertMsg('', result.message ? result.message : '加载失败', '关闭', 'warning', true);
                    _that.dataTbRight = {};
                });
            } else {
                _that.dataTbRight = {};
                _that.loading2 = false;
            }
        },
        editRow: function (rowData) {
            this.dialogEditTable1 = true;
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            this.getCreditType();
            if (rowData) {
                this.dialogTitie = '编辑信用信息';
                this.selRowData.summaryId = rowData.summaryId;
                this.selRowData.summaryType = rowData.summaryType;
                this.selRowData.creditUnitInfoId = rowData.creditUnitInfoId;
                this.selRowData.creditUnitInfoName = rowData.creditUnitInfoName;
                this.selRowData.linkmanInfoId = rowData.linkmanInfoId;
                this.selRowData.linkmanInfoName = rowData.linkmanInfoName;
                this.selRowData.cnTableName = rowData.cnTableName;
                this.selRowData.enTableName = rowData.enTableName;
                this.selRowData.cnDeptName = rowData.cnDeptName;
                this.selRowData.enDeptName = rowData.enDeptName;
                this.selRowData.creditType = rowData.creditType;
                this.selRowData.isValid = rowData.isValid;
                this.selRowData.invalidReason = rowData.invalidReason;
                this.selRowData.isSync = rowData.isSync;
                this.selRowData.syncSource = rowData.syncSource;
                this.selRowData.syncTime = rowData.syncTime;
                if(this.selRowData.isSync == '1'){
                    this.inputSyncFiled = true;
                }else{
                    this.inputSyncFiled = false;
                }
            } else {
                this.dialogTitie = '新增信用信息'
                this.selRowData.summaryId = '';
                this.selRowData.summaryType = 'u';
                this.selRowData.creditUnitInfoId = '';
                this.selRowData.creditUnitInfoName = '';
                this.selRowData.linkmanInfoId = '';
                this.selRowData.linkmanInfoName = '';
                this.selRowData.cnTableName = '';
                this.selRowData.enTableName = '';
                this.selRowData.cnDeptName = '';
                this.selRowData.enDeptName = '';
                this.selRowData.creditType = '';
                this.selRowData.isValid = '1';
                this.selRowData.invalidReason = '';
                this.selRowData.isSync = '0';
                this.selRowData.syncSource = '';
                this.selRowData.syncTime = '';
                this.inputSyncFiled = false;
            }
            this.changeSummaryTypeHandler(this.selRowData.summaryType);
            this.changeIsValidHandler(this.selRowData.isValid);
        },
        creditTypeFormat: function (row, column) {
            var format = "";
            if (row.creditType == "good") {
                format = "守信";
            } else if (row.creditType == "bad") {
                format = "失信";
            } else if (row.creditType == "reg") {
                format = "登记注册备案";
            } else if (row.creditType == "cert") {
                format = "资质认证许可";
            }
            return format;
        },
        columnDataTypeFormat: function (row, column) {
            var format = "";
            if (row.columnDataType == "s") {
                format = "字符串";
            } else if (row.columnDataType == "n") {
                format = "数字";
            } else if (row.columnDataType == "d") {
                format = "日期";
            }
            return format;
        },
        changeSummaryTypeHandler: function (val) {
            var _that = this;
            if (val == 'u') {
                _that.inputUnitInfo = true;
                _that.inputLinkmanInfo = false;
            } else {
                _that.inputUnitInfo = false;
                _that.inputLinkmanInfo = true;
            }
        },
        changeIsValidHandler: function (val) {
            var _that = this;
            if (val == '0') {
                _that.inputInValidReason = true;
            } else {
                _that.inputInValidReason = false;
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
        isSyncEdit2: function(val) {

            var _that = this;
            _that.rightRowData.isSync = val;
            if (_that.rightRowData.isSync == "0") {
                _that.inputSyncFiled2 = false;
            } else {
                _that.inputSyncFiled2 = true;
            }
        },
        selUnitInfo: function () {
            var _that = this;
            _that.dialogSelUnit = true;
            _that.selUnitTreeData = [];
            _that.selUnitInfoList = [];
            request('aplanmis', {
                url: ctx + 'aea/credit/unit/info/gtreeUnitInfoForEui.do',
                type: 'get',
                data: {keyword: _that.unitFilterText}
            }, function (data) {
                if (data.success) {
                    _that.selUnitTreeData = data.content;
                    if (data.content[0].id) {
                        _that.selUnitRootNodeId.push(data.content[0].id);
                    }
                    if (_that.selRowData.creditUnitInfoId != '' && _that.selRowData.creditUnitInfoName != '') {
                        _that.selUnitInfoList = [];
                        _that.selUnitInfoList.push({
                            id: _that.selRowData.creditUnitInfoId,
                            label: _that.selRowData.creditUnitInfoName
                        });
                    }
                }
            }, function () {
                _that.selUnitTreeData = [];
            });
        },
        clearUnitSearch: function () {
            var _that = this;
            _that.unitFilterText = '';
            _that.selUnitInfo();
        },
        clickSelUnit: function (data, node, event) {
            if (data && data.id != 'root') {
                var _that = this;
                _that.selUnitInfoList = [];
                _that.selUnitInfoList.push(data);
            } else {
                return;
            }
        },
        removeSelUnit: function () {
            var _that = this;
            _that.selUnitInfoList = [];
        },
        saveSelUnit: function () {
            var _that = this;
            if (_that.selUnitInfoList.length > 0) {
                _that.selRowData.creditUnitInfoId = _that.selUnitInfoList[0].id;
                _that.selRowData.creditUnitInfoName = _that.selUnitInfoList[0].label;
            } else {
                _that.selRowData.creditUnitInfoId = '';
                _that.selRowData.creditUnitInfoName = '';
            }
            _that.dialogSelUnit = false;
        },
        closeSelUnitInfo: function () {
            var _that = this;
            _that.dialogSelUnit = false;
        },
        selLinkmanInfo: function () {
            var _that = this;
            _that.dialogSelLinkman = true;
            _that.selLinkmanTreeData = [];
            _that.selLinkmanInfoList = [];
            request('aplanmis', {
                url: ctx + 'aea/credit/redblack/gtreeLinkmanForEui.do',
                type: 'get',
                data: {keyword: _that.linkmanFilterText}
            }, function (data) {
                if (data.success) {
                    _that.selLinkmanTreeData = data.content;
                    if (data.content[0].id) {
                        _that.selLinkmanRootNodeId.push(data.content[0].id);
                    }
                    if (_that.selRowData.linkmanInfoId != '' && _that.selRowData.linkmanName != '') {
                        _that.selLinkmanInfoList = [];
                        _that.selLinkmanInfoList.push({
                            id: _that.selRowData.linkmanInfoId,
                            label: _that.selRowData.linkmanInfoName
                        });
                    }
                }
            }, function () {
                _that.selLinkmanTreeData = [];
            });
        },
        clearLinkmanSearch: function () {
            var _that = this;
            _that.linkmanFilterText = '';
            _that.selLinkmanInfo();
        },
        clickSelLinkman: function (data, node, event) {
            if (data && data.id != 'root') {
                var _that = this;
                _that.selLinkmanInfoList = [];
                _that.selLinkmanInfoList.push(data);
            } else {
                return;
            }
        },
        removeSelLinkman: function () {
            var _that = this;
            _that.selLinkmanInfoList = [];
        },
        saveSelLinkman: function () {
            var _that = this;
            if (_that.selLinkmanInfoList.length > 0) {
                _that.selRowData.linkmanInfoId = _that.selLinkmanInfoList[0].id;
                _that.selRowData.linkmanInfoName = _that.selLinkmanInfoList[0].label;
            } else {
                _that.selRowData.linkmanInfoId = '';
                _that.selRowData.linkmanInfoName = '';
            }
            _that.dialogSelLinkman = false;
        },
        closeSelLinkmanInfo: function () {
            var _that = this;
            _that.dialogSelLinkman = false;
        },
        saveEdit: function (refsD) {
            var dataSubmit = this.$refs[refsD].model;
            var _that = this;
            this.$refs[refsD].validate(function (valid) {
                if (valid) {
                    _that.submitEdit(dataSubmit);
                } else {
                    alertMsg('', '请输入完整信息', '关闭', 'warning', true);
                    _that.dialogEditTable1 = true;
                    return false;
                }
            });
        },
        submitEdit: function (data) {
            var _that = this;
            request('aplanmis', {
                url: ctx + 'aea/credit/summary/saveAeaCreditSummary.do',
                data: data
            }, function (data) {
                if (data.success) {
                    _that.$message({
                        message: data.message ? data.message : '保存成功',
                        type: 'success'
                    });
                } else {
                    _that.$message({
                        message: data.message ? data.message : '保存失败',
                        type: 'error'
                    });
                }
                _that.getListData()
            }, function (result) {
                _that.$message({
                    message: result.message ? result.message : '加载失败',
                    type: 'error'
                });
            });
        },
        deleteData: function (data) {
            var _that = this;
            confirmMsg('', '确定要删除选中的信用信息及所有详情信息吗？', function () {
                var summaryId = data.summaryId;
                request('aplanmis', {
                    url: ctx + 'aea/credit/summary/deleteAeaCreditSummaryById.do',
                    data: {
                        summaryIds: summaryId
                    }
                }, function (succ) {
                    if (succ.success) {
                        _that.$message({
                            message: succ.message ? succ.message : '删除成功',
                            type: 'success'
                        });
                        _that.getListData();
                        // _that.getRightData();
                    } else {
                        _that.$message({
                            message: succ.message ? succ.message : '删除失败',
                            type: 'error'
                        });
                    }
                }, function (result) {
                    _that.$message({
                        message: result.message ? result.message : '删除失败',
                        type: 'error'
                    });
                });
            }, '', '确定', '取消')
        },
        handleSizeChange: function (val) {
            this.pageSizeLeft = val;
            this.getListData();
        },
        handleCurrentChange: function (val) {
            this.page = val;
            this.getListData();
        },
        handleRightSizeChange: function (val) {
            this.pageSizeRight = val;
            this.getRightData({typeId: this.rightRowData.typeId});
        },
        handleRightCurrentChange: function (val) {
            this.pageRight = val;
            this.getRightData({typeId: this.rightRowData.typeId});
        },
        searchRow: function () {
            var propSearch = {
                summaryType: this.summaryType,
                keyword: this.searchKey,
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSizeLeft,
                order: 'desc'
            };
            this.getListData(propSearch)
        },
        searchRowRight: function (summaryId) {
            var props = {
                summaryId: summaryId,
                pageNum: this.pageRight ? this.pageRight : 1,
                pageSize: this.pageSizeRight,
                order: 'asc',
                keyword: this.searchKeyRight
            }
            this.getRightData(props)
        },
        leftRowClick: function (row) {
            this.getRightData(row);
        },
        deleteDetailData: function (data) {
            var _that = this;
            confirmMsg('', '确定要删除选中项吗？', function () {
                var detailId = data.detailId;
                request('aplanmis', {
                    url: ctx + 'aea/credit/detail/deleteAeaCreditDetailById.do',
                    data: {ids: detailId}
                }, function (succ) {
                    if (succ.success) {
                        _that.$message({
                            message: succ.message ? succ.message : '删除成功',
                            type: 'success'
                        });
                        _that.getRightData({summaryId: data.summaryId});
                    } else {
                        _that.$message({
                            message: succ.message ? succ.message : '删除失败',
                            type: 'error'
                        });
                    }
                }, function (result) {
                    alertMsg('', result.message ? result.message : '删除失败', '关闭', 'error', true);
                });
            }, '', '确定', '取消')
        },
        editChildRow: function (rowData) {
            this.dialogEditTable2 = true;
            if (rowData) {
                this.dialogTitie = '编辑信用详细信息'
                this.rightRowData.detailId = rowData.detailId;
                this.rightRowData.summaryId = rowData.summaryId;
                this.rightRowData.cnColumnName = rowData.cnColumnName;
                this.rightRowData.enColumnName = rowData.enColumnName;
                this.rightRowData.columnDataType = rowData.columnDataType;
                this.rightRowData.columnValue = rowData.columnValue;
                if (rowData.columnDataType == 's') {
                    this.rightRowData.columnStrValue = rowData.columnValue;
                } else if (rowData.columnDataType == 'n') {
                    this.rightRowData.columnNumberValue = rowData.columnValue;
                } else if (rowData.columnDataType == 'd') {
                    this.rightRowData.columnDateValue = rowData.columnValue;
                }
                this.rightRowData.isSync = rowData.isSync;
                this.rightRowData.syncSource = rowData.syncSource;
                this.rightRowData.syncTime = rowData.syncTime;
                if(this.rightRowData.isSync == '1'){
                    this.inputSyncFiled2 = true;
                }else{
                    this.inputSyncFiled2= false;
                }
            } else {
                this.dialogTitie = '新增信用详细信息'
                this.rightRowData.detailId = '';
                this.rightRowData.cnColumnName = '';
                this.rightRowData.enColumnName = '';
                this.rightRowData.columnDataType = 's';
                this.rightRowData.columnValue = '';
                this.rightRowData.columnStrValue = '';
                this.rightRowData.columnNumberValue = '';
                this.rightRowData.columnDateValue = '';
                this.rightRowData.isSync = '0';
                this.rightRowData.syncSource = '';
                this.rightRowData.syncTime = '';
                this.inputSyncFiled2 = false;
            }
        },
        saveCreditDetail: function (refsD) {
            var _that = this;
            if (_that.rightRowData.columnDataType == 's') {
                _that.rightRowData.columnValue = _that.rightRowData.columnStrValue;
            } else if (_that.rightRowData.columnDataType == 'n') {
                _that.rightRowData.columnValue = _that.rightRowData.columnNumberValue;
            } else if (_that.rightRowData.columnDataType == 'd') {
                _that.rightRowData.columnValue = _that.rightRowData.columnDateValue;
            }
            var dataSubmit = _that.$refs[refsD].model;
            _that.$refs[refsD].validate(function (valid) {
                if (valid) {
                    request('aplanmis', {
                        url: ctx + 'aea/credit/detail/saveAeaCreditDetail.do',
                        data: _that.rightRowData
                    }, function (succ) {
                        if (succ.success) {
                            _that.$message({
                                message: succ.message ? succ.message : '保存成功',
                                type: 'success'
                            });
                        } else {
                            _that.$message({
                                message: succ.message ? succ.message : '保存失败',
                                type: 'error'
                            });
                        }
                        _that.getRightData({summaryId: _that.rightRowData.summaryId});
                    }, function (result) {
                        alertMsg('', result.message ? result.message : '保存失败', '关闭', 'error', true);
                    });
                } else {
                    alertMsg('', '请输入完整信息', '关闭', 'warning', true);
                    _that.dialogEditTable2 = true;
                    return false;
                }
            });
        },
        clearFormData: function (fromName) {
            this.$refs[fromName].resetFields();
            this.selRowData.summaryType = 'u';
            this.selRowData.isVaild = '0';
            this.inputInValidReason = true;
        },
        summaryTypeSearch: function (val) {
            this.summaryType = val;
            this.getListData();
        },
        handleSelChange: function (val) {
            var ids = [];
            val.map(function (item) {
                ids.push(item.summaryId)
            })
            this.selectedIds = ids;
            return this.selectedIds;
        },
        handleRightSelChange: function (val) {
            var ids = [];
            val.map(function (item) {
                ids.push(item.detailId)
            })
            this.selectedRightIds = ids;
            return this.selectedRightIds;
        },
        removeRow: function () {
            var _that = this;
            confirmMsg('', '确定要删除选中的信用信息及所有详情信息吗？', function () {
                var ids = _that.selectedIds.join(",")
                request('aplanmis', {
                    url: ctx + 'aea/credit/summary/deleteAeaCreditSummaryById.do',
                    data: {
                        summaryIds: ids
                    }
                }, function (succ) {
                    if (succ.success) {
                        _that.$message({
                            message: succ.message ? succ.message : '删除成功',
                            type: 'success'
                        });
                        _that.getListData();
                        // _that.getRightData();
                    } else {
                        _that.$message({
                            message: succ.message ? succ.message : '删除失败',
                            type: 'error'
                        });
                    }
                }, function (result) {
                    _that.$message({
                        message: result.message ? result.message : '删除失败',
                        type: 'error'
                    });
                });
            }, '', '确定', '取消')
        },
        removeRightRow: function () {
            var _that = this;
            confirmMsg('', '确定要删除选中项吗？', function () {
                var ids = _that.selectedRightIds.join(",");
                request('aplanmis', {
                    url: ctx + 'aea/credit/detail/deleteAeaCreditDetailById.do',
                    data: {ids: ids}
                }, function (succ) {
                    if (succ.success) {
                        _that.$message({
                            message: succ.message ? succ.message : '删除成功',
                            type: 'success'
                        });
                        _that.getRightData({summaryId: _that.summaryId});
                    } else {
                        _that.$message({
                            message: succ.message ? succ.message : '删除失败',
                            type: 'error'
                        });
                    }
                }, function (result) {
                    alertMsg('', result.message ? result.message : '删除失败', '关闭', 'error', true);
                });
            }, '', '确定', '取消')
        },
        getCreditType: function () {
            var _that = this;
            request('bsc', {
                url: ctx + 'bsc/dic/code/lgetItems.do?typeCode=AEA_CREDIT_TYPE'
            }, function (succ) {
                _that.creditTypeList = succ;
            }, function (result) {
                alertMsg('', '获取信用类型失败', '关闭', 'error', true);
            });
        }
    },
});

