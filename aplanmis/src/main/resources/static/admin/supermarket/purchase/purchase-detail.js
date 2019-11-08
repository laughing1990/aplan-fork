var vm = new Vue({
	el: '#mainContentPanel',
	data: function () {
		return {
			auditForm: {
				auditOpinion: '同意', auditFlag: '1', projPurchaseId: projPurchaseId
			},
			dialogTableVisible: false,
			qualMajorModal: false,
			activeName: '0',
			activeNames: ['1', '2', '3', '4', '5', '6'],
			defaultProps: {
				children: 'children',
				label: 'majorName'
			},
			detailData: {},
			qualMajorTree: []
		}
	},
	methods: {
		showAuditDialog: function () {
			this.dialogTableVisible = true;
		},
		backPrd: function () {
			window.location.href = ctx + 'supermarket/purchase/index.html'
		},
		handleClick:function(tab, event) {
			console.log(tab, event);
			this.activeName = tab.name + '';
		},
		dateTimeFormat: function (val) {
			if (val) {
				var oDate = new Date(val),
						oYear = oDate.getFullYear(),
						oMonth = oDate.getMonth() + 1,
						oDay = oDate.getDate(),
						oTime = oYear + '-' + this.addZero(oMonth) + '-' + this.addZero(oDay);
				return oTime;
			}
			return val;
		},
		addZero: function (num) {
			if (parseInt(num) < 10) {
				num = '0' + num;
			}
			return num;
		},

		getPurchaseDetail: function () {
			var _this = this;
			request('', {
				url: ctx + 'supermarket/purchase/getPurchaseDetail?projPurchaseId=' +projPurchaseId,
				type: 'get'
			}, function (data) {
				if (data.success) {
					_this.detailData = data.content;
				}else{
					_this.$message({
						message: data.message ? data.message : '获取采购数据异常',
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
		//审核提交
		auditSubmit: function () {
			var _this = this;
			this.$refs['auditForm'].validate(function (valid) {
				if (valid) {
					request('', {
						url: ctx + '/supermarket/purchase/audit.do',
						type: 'POST',
						data: _this.auditForm
					}, function (data) {
						if (data.success) {
							location.href = ctx + '/supermarket/purchase/index.html';
						} else {
							console.log(data.message)
							_this.$message({
								message: data.message ? data.message : '审核异常',
								type: 'error'
							});
						}
					}, function (msg) {
						_this.$message({
							message: msg.message ? msg.message : '服务请求失败',
							type: 'error'
						});
					})
				}

			})
		},

		showQualificationTree: function (unitRequireId) {
			var _this = this;
			request('', {
				url: ctx + '/supermarket/purchase/getMajorTreeByUnitRequireId.do',
				type: 'POST',
				data: {'unitRequireId': unitRequireId},
			}, function (data) {
				_this.qualMajorModal = true;
				_this.qualMajorTree = data;
				if (data.success) {
				}
			}, function (msg) {
			})
		},

	},
	filters: {
		changeReceiveType: function (value) {
			var defaultValue = '其他回执';
			//{{(itemBtn.receiptType==2)?'收件回执':(itemBtn.receiptType==1)?'物料回执':(itemBtn.receiptType==3)?'不受理回执':'其他回执'}}
			if (value) {
				switch (value) {
					case '1':
						defaultValue = '物料回执';
						break;
					case '2':
						defaultValue = '受理回执';
						break;
					case '3':
						defaultValue = '不受理回执';
						break;
					case '4':
						defaultValue = '退件回执';
						break;
					case '5':
						defaultValue = '送达回证';
						break;
					case '6':
						defaultValue = '材料补正回执';
						break;
					case '7':
						defaultValue = '行政许可申请书';
						break;
					default:
						defaultValue = '其他回执';
				}
			}
			return defaultValue;
		},
		formatDate: function (time) {
			if (!time) return '-';
			var date = new Date(time);
			if (!date) return;
			return formatDate(date, 'yyyy-MM-dd hh:mm');
		},
	},
	mounted: function () {
		this.getPurchaseDetail();
	}
})
