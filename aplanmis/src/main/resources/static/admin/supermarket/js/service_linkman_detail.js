var vm = new Vue({
			el: '#mainContentPanel',
			data: function () {
				return {
					auditForm: {
						memo: '同意', auditFlag: '1', serviceLinkmanId: ''
					},
					dialogTableVisible: false,
					activeName: '0',
					activeNames: ['1', '2', '3', '4', '5'],
					contentObj: {
						practiseDate: '',
						isHead: '',
						certinst: [],
						auditFlag: '',
						memo: '',
						linkmanInfo: {
							linkmanName: '',
							linkmanType: '',
							linkmanAddr: '',
							linkmanMobilePhone: '',
							linkmanMail: '',
							linkmanCertNo: ''
						},
						service: {
							serviceName: '',
							serviceCode: ''
						},
						unitInfo: {
							applicant: '',
							idcard: '',
							contact: '',
							mobile: '',
							email: '',
							applicantDetailSite: '',
							idrepresentative: '',
							idmobile: '',
							idno: '',
							unitNature: '',
							registeredCapital: '',
							registerAuthority: '',
							managementScope: ''

						}
					},
					defaultProps: {
						children: 'children',
						label: 'majorName'
					}
				}
			},
			methods: {
				showAuditDialog: function () {
					this.dialogTableVisible = true;
				},
				backPrd: function () {
					window.location.href = ctx + 'supermarket/serviceLinkman/indexAeaImServiceLinkman.html'
				},
				handleClick(tab, event) {
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

				queryLinkmanDetail: function () {
					var _this = this;
					request('', {
						url: ctx + '/supermarket/serviceLinkman/getServiceLinkmanDetailById?serviceLinkmanId=' + serviceLinkmanId,
						type: 'get'
					}, function (data) {
						if (data.success) {
							//console.log(data.content);
							_this.contentObj = data.content;
						} else {
							// _that.$message({
							// 	message: data.message ? data.message : '信息保存失败',
							// 	type: 'error'
							// });
						}
					}, function (msg) {
						/*_that.$message({
							message: msg.message ? msg.message : '服务请求失败',
							type: 'error'
						});*/
					})
				}
				,
				updateAeaImServiceLinkman: function () {
					var _this = this;
					_this.auditForm.serviceLinkmanId = serviceLinkmanId;
					this.$refs['auditForm'].validate(function (valid) {
						if (valid) {
							request('', {
								url: ctx + '/supermarket/serviceLinkman/updateAeaImServiceLinkman.do',
								type: 'POST',
								data: _this.auditForm
							}, function (data) {
								if (data.success) {
									setTimeout(function () {
										location.href = ctx + 'supermarket/serviceLinkman/indexAeaImServiceLinkman.html'
									}, 1000);
								} else {
									console.log(data.message)
								}
							}, function (msg) {
								/*_that.$message({
									message: msg.message ? msg.message : '服务请求失败',
									type: 'error'
								});*/
							})
						}

					})


				}

			},
			mounted: function () {
				this.queryLinkmanDetail();
			}
		})
