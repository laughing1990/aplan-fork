var vm = new Vue({
	el: '#mainContentPanel',
	data: function () {
		return {
			// 审核情况下拉选项
			registerOptions: [
				{value: '', label: '请选择'}, {
					value: '1',
					label: '中介机构'
				}, {
					value: '2',
					label: '业主'
				}
			],
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
				}
			],
			searchForm: {
				total: 0,
				pageNum: 1,
				pageSize: 10,
				applicant: '',
				unifiedSocialCreditCode: '',
				registerType: '',
				auditFlag: ''
			},
			registerList: [],
		}
	},
	mounted: function () {
		this.getRegisterList();
	},
	methods: {
		findSearchForm:function(){
			this.getRegisterList();
		},
		clearSearchForm: function () {
			this.$refs['searchForm'].resetFields();
			this.getRegisterList();
		},
		handleSizeChange: function (val) {
			this.searchForm.pageSize = val;
			this.getRegisterList();
		},
		handleCurrentChange: function (val) {
			console.log(val)
			this.searchForm.pageNum = val;
			this.getRegisterList();
		},
		getRegisterList: function () {
			var _this = this;
			request('', {
				url: ctx + '/supermarket/register/listRegister',
				data: _this.searchForm,
				type: 'post'
			}, function (data) {
				if (data.success) {
					_this.registerList = data.content.rows;
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
		registerDeatail: function (row) {
			var url = ctx + '/supermarket/register/detail.html?unitInfoId=' + row.unitInfoId;
			window.location.href = url;
		},
		// 预览电子件 必须要有detailId
		filePreview: function (data) {
			if (!data.detailId) {
				data.detailId = data.fileId;
			} // 设置detailId
			if (!data.attFormat) {
				data.attFormat = data.fileType;
			} // 文件类型
			if (this.allowPreType[data.attFormat]) {
				return this.preFile(data);
			} // 预览pdf、doc等
			// 预览图片等
			var detailId = data.detailId;
			var flashAttributes = '';
			var tempwindow = window.open('_blank'); // 先打开页面
			tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
		}
	}
})