var vm = new Vue({
	el: '#mainContentPanel',
	mixins: [mixins],
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
					value: '2',
					label: '未审核'
				}, {
					value: '0',
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
		findSearchForm: function () {
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
		formatStatus: function (row, column, cellValue, index) {
			var deleted = row.isDeleted;
			var auditFlag = row.auditFlag;
			if (deleted && deleted == '0') {//未删除
				switch (auditFlag) {
					case "1":
						return '已审核';
					case "2":
						return "审核中";
					default:
						return "审核失败";
				}
			} else if (deleted && deleted == '1') {
				return '已删除';
			} else {
				return '';
			}
		},
		//补0操作
		getzf: function (num) {
			if (parseInt(num) < 10) {
				num = '0' + num;
			}
			return num;
		},
		registerDeatail: function (row) {
			var url;
			if (row.isImUnit === '1') {
				url = ctx + '/supermarket/register/detail.html?unitInfoId=' + row.unitInfoId;//中介机构
			} else if (row.isOwnerUnit === '1') {
				url = ctx + '/supermarket/register/ownerDetail.html?unitInfoId=' + row.unitInfoId;//业主
			}
			window.location.href = url;
		},
		//删除单位
		deleteUnit: function (row) {
			var url = ctx + '/supermarket/register/delete/' + row.unitInfoId;
			var _this = this;
			request('', {
				url: url,
				type: 'post'
			}, function (data) {
				if (data.success) {
					_this.$message({
						type: 'success',
						message: '删除成功!'
					});
					_this.getRegisterList();
				}
			}, function (msg) {
				_this.$message({
					message: msg.message ? msg.message : '服务请求失败',
					type: 'error'
				});
			})
		},
		/*删除单位确认框*/
		confirmDelete: function (row) {
			var _this = this;
			_this.$confirm('此操作将删除单位及发布的服务、人员, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				_this.deleteUnit(row);
			}).catch(() => {
				_this.$message({
					type: 'info',
					message: '已取消删除'
				});
			});
		},

		/*启用已删除单位确认弹框*/
		confirmEnableUnit: function (row) {
			var _this = this;
			_this.$confirm('启用已删除单位, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				_this.enableUnit(row);
			}).catch(() => {
				_this.$message({
					type: 'info',
					message: '已取消删除'
				});
			});
		},
		//删除单位
		enableUnit: function (row) {
			var url = ctx + '/supermarket/register/enable/' + row.unitInfoId;
			var _this = this;
			request('', {
				url: url,
				type: 'post'
			}, function (data) {
				if (data.success) {
					_this.$message({
						type: 'success',
						message: '启用成功!'
					});
					_this.getRegisterList();
				}
			}, function (msg) {
				_this.$message({
					message: msg.message ? msg.message : '服务请求失败',
					type: 'error'
				});
			})
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