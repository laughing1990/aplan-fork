var vm = new Vue({
    el: '#searchTable',
    mixins: [mixins],
    data: function () {
        return {
            //查询数据
            searchFrom: {
                pagination: {
                    page: 1,
                    pages: 1,
                    perpage: 10
                },
                sort: {
                    field: 'applyTime',
                    sort: 'desc'
                },
                region: '',
                projType: '',
                theme: '',
                investCategory: '',
                industry: '',
                buildNature: '',
                approvalStage: '',
                applyStartTime: '',
                applyEndTime: '',
                applySource: '',
                projLevel: '',
                applyType: '',
                applyState: '',
                instState: '',
                keyword: '',
                entrust: false
            },

            treeProps: {
                label: 'itemName',
                children: 'children',
                id: 'itemId'
            },

            industryTreeVisible: false,
            filterText: '',

            //所在区域
            regionList: [],
            //立项类型
            projTypeList: [],
            //投资类型
            investCategoryList: [],
            //行业分类
            industryList: [],
            //建设性质
            buildNatureList: [],
            //项目级别
            projLevelList: [],

            //审批阶段
            approvalStageList: [],

            //国标行业树
            industryTree: [],
            industryText: '',
            selectedIndustryList: [],

        }
    },
    methods: {
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;
            this.searchFrom.entrust = entrust;

            if (ts.searchFrom.applyStartTime != '' && ts.searchFrom.applyEndTime != '') {
                if (ts.searchFrom.applyStartTime > ts.searchFrom.applyEndTime) {
                    ts.apiMessage('申报开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            ts.loading = true;

            request('', {
                url: ctx + 'win/efficiency/supervision/listWarnApply',
                type: 'get',
                data: ts.searchFrom
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.tableData = res.content.list;
                    ts.dataTotal = res.content.total;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        //获取查询条件的数据
        conditionalQueryDic: function () {
            var ts = this;
            // ts.loading = true;
            request('', {
                url: ctx + 'rest/conditional/query/apply/dic/list',
                type: 'get',
                data: {}
            }, function (res) {
                // ts.loading = false;
                if (res.success) {
                    ts.regionList = res.content.regionList;
                    ts.themeList = res.content.themeList;
                    ts.projTypeList = res.content.projTypeList;
                    ts.investCategoryList = res.content.investCategoryList;
                    ts.industryList = res.content.industryList;
                    ts.buildNatureList = res.content.buildNatureList;
                    ts.applySourceList = res.content.applySourceList;
                    ts.applyTypeList = res.content.applyTypeList;
                    ts.projLevelList = res.content.projLevelList;
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
        //根据所选主题获取阶段
        queryApprovalStageList: function () {
            this.searchFrom.approvalStage = '';
            this.approvalStageList = [];
            if (this.searchFrom.theme != '') {
                var ts = this;
                // ts.loading = true;
                var themeId = this.searchFrom.theme;
                request('', {
                    url: ctx + 'rest/conditional/query/theme/stage/list',
                    type: 'get',
                    data: {themeId: themeId}
                }, function (res) {
                    // ts.loading = false;
                    if (res.success) {
                        ts.approvalStageList = res.content;
                    } else {
                        return ts.apiMessage(res.message, 'error')
                    }
                }, function () {
                    // ts.loading = false;
                    return ts.apiMessage('网络错误！', 'error')
                });
            }
            this.tableDataSearch();

        },
        //重置
        clearSearch: function () {
            this.searchFrom.region = '';
            this.searchFrom.projType = '';
            this.searchFrom.theme = '';
            this.searchFrom.investCategory = '';
            this.searchFrom.industry = '';
            this.searchFrom.buildNature = '';
            this.searchFrom.approvalStage = '';
            this.searchFrom.applyStartTime = '';
            this.searchFrom.applyEndTime = '';
            this.searchFrom.applySource = '';
            this.searchFrom.projLevel = '';
            this.searchFrom.applyType = '';
            this.searchFrom.applyState = '';
            this.searchFrom.applyinstCode = '';
            this.searchFrom.instState = '';
            this.searchFrom.keyword = '';
            this.resetChecked();
            this.tableDataSearch();
        },
        //导出
        exportExcel: function () {
            var param = encodeURI(JSON.stringify(this.searchFrom));
            window.location.href = ctx + "rest/conditional/query/export/applyinfo" + "?param=" + param;
        },
        //查看详情
        viewDetail: function (row) {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + 'rest/conditional/query/queryApplyInfoTaskId',
                type: 'get',
                data: {applyinstId: row.applyinstId}
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    window.open(ctx + 'apanmis/page/stageApproveIndex?isNotCompareAssignee=true&taskId=' + res.content.taskId + '&viewId=' + res.content.viewId, '_blank');
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        queryIndustryTree: function () {

            var ts = this;
            request('', {
                url: ctx + 'rest/conditional/query/tree/industry',
                type: 'get',
                async: true,
                data: {}
            }, function (res) {
                if (res.success) {
                    ts.industryTree = res.content;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });

        },
        handleCheckChange: function (data, checked, indeterminate) {
            if (checked) {
                this.selectedIndustryList.push(data);
            } else {
                for (var i = 0; i < this.selectedIndustryList.length; i++) {
                    if (this.selectedIndustryList[i].itemId == data.itemId) {
                        this.selectedIndustryList.splice(i, 1);
                    }
                }
            }
            var text = '';
            var value = '';
            for (var i = 0; i < this.selectedIndustryList.length; i++) {
                if (i == 0) {
                    text = this.selectedIndustryList[i].itemName;
                    value = this.selectedIndustryList[i].itemCode;
                } else {
                    text = text + ',' + this.selectedIndustryList[i].itemName;
                    value = value + ',' + this.selectedIndustryList[i].itemCode;
                }
            }

            this.industryText = text;
            this.searchFrom.industry = value;
        },
        filterNode: function (value, data) {
            if (!value) return true;
            return data.itemName.indexOf(value) !== -1;
        },
        resetChecked: function () {
            this.filterText = '';
            if (this.$refs && this.$refs.tree) {
                this.$refs.tree.setCheckedKeys([]);
            }
        },
        clearThemeList: function () {
            var ts = this;
            if (ts.searchFrom.applyType == '1') {
                ts.themeList = [];
                ts.searchFrom.theme = '';
                ts.searchFrom.approvalStage = '';
            } else {
                if (ts.globalThemeList.lenght == 0) {
                    ts.conditionalQueryDic();
                } else {
                    ts.themeList = ts.globalThemeList;
                }
            }

            this.tableDataSearch();
        },
        queryCheckedIndustryTree: function () {
            this.industryTreeVisible = false;
            this.tableDataSearch();
        }

    },
    created: function () {
        this.conditionalQueryDic();
        this.queryIndustryTree();
        this.fetchTableData();
    },
    watch: {
        filterText(val) {
            this.$refs.tree.filter(val);
        }
    }
});