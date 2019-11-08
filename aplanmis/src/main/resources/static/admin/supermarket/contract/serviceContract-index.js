var vm = new Vue({
    el: '#mainContentPanel',
    data: function () {
        return {

            certinstMajor: [], // 专业
            defaultProps: {
                children: 'child',
                label: 'name'
            },
            serviceContractList: [],
            linkListPage: {
                pageNum: 1,
                pageSize: 10,
                total: 0
            },
            searchForm: {
                total: 0,
                pageNum: 1,
                pageSize: 10,
                keyword: ''
            }
        }
    },
    mounted: function () {
        this.getServiceContract();
    },
    methods: {
        handleSizeChange2: function (val) {
            this.searchForm.pageSize = val;
            this.getServiceContract();
        },
        handleCurrentChange2: function (val) {
            this.searchForm.pageNum = val;
            this.getServiceContract();
        },
        getServiceContract: function () {
            var _this = this;
            request('', {
                url: ctx + '/supermarket/contract/listAuditAeaImContractByPage.do',
                data: _this.searchForm,
                type: 'get'
            }, function (data) {
                debugger;
                if (data.success) {
                    _this.serviceContractList = data.content.rows;
                    _this.searchForm.total = data.content.total;
                } else {
                    _this.$message({
                    	message: data.message ? data.message : '获取服务合同失败',
                    	type: 'error'
                    });
                }
            }, function (msg) {
                _this.$message({
                    message: msg.message ? msg.message : '服务请求失败',
                    type: 'error'
                });
            })
        },
        auditServiceContract:function(contractId){
            var url = ctx + '/supermarket/contract/serviceContractDetail.html?contractId=' + contractId;
            window.location.href = url;
        },
        //格式化审核状态
        formatAudioFlag: function (row, column, cellValue, index) {
            switch (cellValue) {
                case '0':
                    return '待确定';
                case '1':
                    return '已确定';
                case '2':
                    return '审核失败';
                case '3':
                    return '审核中';
                default:
                    return '';
            }
        },
        getOperateAction:function (data) {
                if(data.aeaImPurchaseinst && data.aeaImPurchaseinst.operateAction){
                    var value = data.aeaImPurchaseinst.operateAction;
                    if('3'==value){
                        return '新增合同';
                    }
                    if('4'==value){
                        return '修改合同';
                    }
                    if('5'==value){
                        return '延长结束服务时间';
                    }
                }

                return '新增合同';
            },
        formatDateTime: function (row, column, cellValue, index) {
            if(cellValue==null){
                return null;
            }
            var oDate = new Date(cellValue),
                oYear = oDate.getFullYear(),
                oMonth = oDate.getMonth() + 1,
                oDay = oDate.getDate(),
                oHour = oDate.getHours(),
                oMin = oDate.getMinutes(),
                oSec = oDate.getSeconds();
            var oTime = oYear + '-' + this.getzf(oMonth) + '-' + this.getzf(oDay) + ' ' + this.getzf(oHour) + ':' + this.getzf(oMin) + ':' + this.getzf(oSec);//最后拼接时间
            return oTime;
        },

        //补0操作
        getzf: function (num) {
            if (parseInt(num) < 10) {
                num = '0' + num;
            }
            return num;
        }
    }
})