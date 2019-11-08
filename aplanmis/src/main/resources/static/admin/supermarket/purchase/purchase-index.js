var vm = new Vue({
	el: '#mainContentPanel',
	data: function () {
		return {
			// 审核情况下拉选项
			auditOptions: [
				{value: '', label: '请选择'}, {
					value: '0',
					label: '未审核'
				}, {
					value: '2',
					label: '审核不通过'
				}, {
					value: '1',
					label: '审核通过'
				}],
			searchForm: {
				total: 0,
				pageNum: 1,
				pageSize: 10,
				itemName: '',
				projName: '',
				applicant: ''
			},
			purchaseList: [],
		}
	},
	mounted: function () {
		this.getPurchaseList();
	},
	methods: {
		findSearchForm:function(){
			this.getPurchaseList();
		},
		clearSearchForm: function () {
			this.$refs['searchForm'].resetFields();
			this.getPurchaseList();
		},
		handleSizeChange: function (val) {
			this.searchForm.pageSize = val;
			this.getPurchaseList();
		},
		handleCurrentChange: function (val) {
			console.log(val)
			this.searchForm.pageNum = val;
			this.getPurchaseList();
		},
		getPurchaseList: function () {
			var _this = this;
			request('', {
				url: ctx + '/supermarket/purchase/listPurchase',
				data: _this.searchForm,
				type: 'post'
			}, function (data) {
				if (data.success) {
					_this.purchaseList = data.content.rows;
					_this.searchForm.total = data.content.total;
				}
			}, function (msg) {
				_this.$message({
					message: msg.message ? msg.message : '服务请求失败',
					type: 'error'
				});
			})
		},
		formatDateTime: function (row, column, cellValue, index) {
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
		},
		purchaseDeatail: function (row) {
			var url = ctx + '/supermarket/purchase/purchase-detail.html?projPurchaseId=' + row.projPurchaseId;
			window.location.href = url;
		}
	}
})