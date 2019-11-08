var checkPhone = function (rule, value, callback) {
	// debugger
	if (!value) {
		return callback(new Error('手机号不能为空'));
	} else {
		var reg = /^1[3|4|5|7|8][0-9]\d{8}$/
		console.log(reg.test(value));
		if (reg.test(value)) {
			callback();
		} else {
			return callback(new Error('请输入正确的手机号'));
		}
	}
};
//  var ctx = 'http://192.168.14.2:8084/aplanmis-mall/';
var module1 = new Vue({
	el: '#addNeedPaurse',
	mixins: [mixins],
	data: function () {
		return {
			loginUserInfo: {
				isAdministrators: "",
				isOwner: "",
				isPersonAccount: "", // 是否为个人账号 0否
				personName: "",
				sid: "",
				unitId: "",
				unitName: "",
				userId: "",
			},
			projScaleOptions: [{
				value: '投资额（元）',
				label: '投资额（元）'
			}, {
				value: '建筑面积（平方米）',
				label: '建筑面积（平方米）'
			}, {
				value: '资产总额（元）',
				label: '资产总额（元）'
			}, {
				value: '用地面积（平方米）',
				label: '用地面积（平方米）'
			}],
			basePriceDx: '',
			highestPriceDx: '',
			basePriceDx: '',
			chooseAgentTabledialogTable: false,
			serviceTabledialogTable: false,
			serviceKeyword: '',
			aptTreeaFlag: false,
			loading: false,
			pageNum: 1,
			pageSize: 10,
			total: 0,
			pageNum2: 1,
			pageSize2: 10,
			total2: 0,
			pageNum3: 1,
			pageSize3: 10,
			total3: 0,
			pageNum4: 1,
			pageSize4: 10,
			total4: 0,
			multipleSelection4: [],
			multipleSelection2: [],
			multipleSelection3: [{agentUnitName: '还没选择中介机构'}],
			projInfoId: '', // 项目主键ID
			projCode: '', // 项目代码
			addNeedList: [{}],// 新增采购需求   // 新增采购需求查询结果列表
			addNeedCheckData: {},     // 新增采购需求查询参数
			addNeedStatus: 0,     // 新增采购需求-当前展示是为列表还是项目发布详情编辑页
			aeaLinkmanInfo: {},
			aeaProjInfo: {},
			aeaUnitInfo: {},
			readonly: true,
			avoidData: [],
			rules: {
				agentItemName: [
					{required: true, message: '请选择中介服务事项', trigger: 'change'}
				],
				// isApproveProj:[
				//     { required: true, message: '请选择投资审批项目', trigger: 'blur' }
				// ],
				approvalCode: [
					{required: true, message: '投资审批项目编码', trigger: 'blur'}
				],
				contact: [
					{required: true, message: '请输入项目联系人', trigger: 'blur'}
				],
				mobile: [
					//  { required: true, message: '请输入联系电话', trigger: 'blur' },
					{required: true, validator: checkPhone, trigger: 'blur'}
				],
				// projName:[
				//      { required: true, message: '请输入采购项目名称', trigger: 'blur' }
				// ],
				projScale: [
					{required: true, message: '请输入项目规模', trigger: 'change'}
				],
				projScaleContent: [
					{required: true, message: '请输入项目规模描述', trigger: 'blur'}
				],
				chooseInsertype: [
					{type: 'array', required: true, message: '请至少选择一个资金来源', trigger: 'change'}
				],
				financialFundProportion: [
					{required: true, message: '请至少填写资金来源比例', trigger: 'change'}
				],
				socialFundProportion: [
					{required: true, message: '请填写资金来源比例', trigger: 'change'}
				],
				registerTotal: [
					{required: true, message: '请输入执业/职业人员总数', trigger: 'blur'}
				],
				registerRequire: [
					{required: true, message: '请输入执业/职业人员要求', trigger: 'blur'}
				],
				serviceName: [
					{required: true, message: '请选择所需服务', trigger: 'change'}
				],
				timeLimitExplain: [
					{required: true, message: '请填写服务时限说明', trigger: 'blur'}
				],
				serviceContent: [
					{required: true, message: '请填写服务内容', trigger: 'blur'}
				],
				amountExplain: [
					{required: true, message: '请填写金额说明', trigger: 'blur'}
				],
				recordRequireExplain: [
					{required: true, message: '请填写备案要求说明', trigger: 'blur'}
				],
				// qualRequireType:[
				//     { required: true, message: '请选择资质要求', trigger: 'blur' }
				// ],
				// qualRequireExplain:[
				//     { required: true, message: '请填写资质要求说明', trigger: 'blur' }
				// ],
				// isDefineAmount:[
				//     { required: true, message: '请选择是否确认金额', trigger: 'blur' }
				// ],
				isDiscloseIm: [
					{required: true, message: '请选择中选机构公示', trigger: 'change'}
				],
				isDiscloseBidding: [
					{required: true, message: '请选择中选公告公示', trigger: 'change'}
				],
				isLiveWitness: [
					{required: true, message: '请选择见证现场竞价选 取、摇珠', trigger: 'change'}
				],
				expirationDate: [
					{required: true, message: '请选择截止时间', trigger: 'blur'}
				],
				choiceImunitTime: [
					{required: true, message: '请选择选取中介时间', trigger: 'blur'}
				],
				basePrice: [
					{required: true, message: '请填写服务金额', trigger: 'blur'}
				],
				highestPrice: [
					{required: true, message: '请填写服务最高金额', trigger: 'blur'}
				],
				agentUnitName: [
					{required: true, message: '请选择指定的中介机构', trigger: 'blur'}
				],
				witnessName1: [
					{required: true, message: '请填写见证人姓名', trigger: 'blur'}
				],
				witnessPhone1: [
					{required: true, message: '请填写见证人联系电话', trigger: 'blur'}
				],
				ownerComplaintPhone: [
					{required: true, message: '请填写投诉质疑电话', trigger: 'blur'}
				]
			},
			agentUnit: [],
			qualRequireType: '0', // 资质要求
			serviceItemList: [],
			needServiceList: [],
			qualArry: [],
			activeSN: '0',
			activeLE: '0',
			selectdRecord: [],
			checkList: [],
			isQualRequire: false,
			isRegisterRequire: false,
			isRecordRequire: false,
			promiseService: false,
			qualId: null,
			qualLevelId: null,
			qualsArry: [],
			majorQualRequiresArry: [],
			serviceTreeData: [],
			defaultProps: {
				children: 'children',
				label: 'name'
			},
			form: {isApproveProj: 1, isDefineAmount: '1', chooseInsertype: ['1', '1'], isAvoid: "0"},
			isFinancialFund: true,
			isSocialFund: true,
			witnessName1: '',
			witnessName2: '',
			witnessName3: '',
			witnessPhone1: '',
			witnessPhone2: '',
			witnessPhone3: '',
			projPurchaseId: '', //项目需求发布ID
			biddingType: '1',
			FinancialCheckList: [],
			fileList1: [],
			fileList2: [],
			formData1: [],
			formData2: [],
			avoidOrgan: false,
			avoidList: [],
			organWord: '',
			projName: '',
			isAdd: false,
			proj: false,
			ProjName: '',
			projInfoList: [],
			loadingProjInfo: false, // 查询项目列表
			searchKeyword: '',
			//   基本信息联系人电话
			basicLinkPhone: ''
		}
	},
	methods: {
		// 获取自动生成的项目编码
		getAutoProjCode: function () {
			var vm = this;

			request('', {
				url: ctx + 'supermarket/purchase/getAutoProjCode', type: 'post',
			}, function (res) {
				if (res.success) {
					console.log(res)

					vm.$set(vm.form, "localCode", res.content)
					vm.$set(vm.form, 'approvalCode', vm.aeaProjInfo.localCode) // 投资审批项目编码
					if (vm.aeaLinkmanInfo && vm.aeaLinkmanInfo.linkmanName) {

						vm.$set(vm.form, 'contact', vm.aeaLinkmanInfo.linkmanName + '[' + vm.aeaLinkmanInfo.linkmanCertNo + ']') // 设置项目联系人
					}
					vm.$set(vm.form, 'mobile', vm.aeaLinkmanInfo.linkmanMobilePhone) //设置项目联系人电话
				}
			}, function (msg) {
				vm.$message({message: '加载失败', type: 'error'});
			});
		},

		/*checkNode: function () {
			var _this = this;
			request('', {
				url: ctx + 'supermarket/purchase/getProjInfoByLocalCode', type: 'get', data: {
					localCode: this.form.approvalCode
				}
			}, function (data) {
				if (!data.content) {
					_this.$message({
						message: '项目不存在，请重新填写',
						type: 'error'
					});
					_this.form.approvalCode = '';
				} else {
					_this.proj = true;
					_this.projName = data.content.projName;
				}
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
			});
		},*/

		saveForm: function (formName) {
			var vm = this;
			// console.log(vm.form.chooseInsertype)
			this.$refs[formName].validate(function (valid, obj) {
				if (valid) {
					// var aeaImMajorQuals = [{
					//     majorId:'', // 专业ID
					//  majorQualId:'', // 专业资质关联ID(不用传)
					//     qualLevelId:'', // 资质等级ID
					// }]
					var aeaImMajorQuals = [];
					vm.selectdRecord.forEach(function (item, index) {
						if (item.data.length > 0) {
							var qualLevelId = vm.qualArry[item.level1].aeaImQualLevels[item.leve2.toString().split('-')[1]].qualLevelId
							for (var i = 0; i < item.data.length; i++) {
								aeaImMajorQuals.push({qualLevelId: qualLevelId, majorId: item.data[i][i]});
							}
						}
					})

					console.log("提交到专业树的数据", aeaImMajorQuals)
					var isQualRequire = '1'
					if (vm.isQualRequire) {
						isQualRequire = '1'
					} else {
						isQualRequire = '0'
					}
					var isRecordRequire = '1'
					if (vm.isRecordRequire) {
						isRecordRequire = '1'
					} else {
						isRecordRequire = '0'
					}
					var isRegisterRequire = '1'
					if (vm.isRegisterRequire) {
						isRegisterRequire = '1'
					} else {
						isRegisterRequire = '0'
					}
					var promiseService = '1'
					if (vm.promiseService) {
						promiseService = '1'
					} else {
						promiseService = '0'
					}

					var qualRequireType = '1'
					if (vm.qualRequireType) {
						qualRequireType = '1'
					} else {
						qualRequireType = '0'
					}

					var aeaImUnitRequire = {
						isQualRequire: isQualRequire, // 是否需要资质要求：1 需要，0 不需要
						isRecordRequire: isRecordRequire, // 是否需要备案要求：1 需要，0 不需要
						isRegisterRequire: isRegisterRequire, // 是否需要执业/职业人员要求：1 需要，0 不需要
						promiseService: promiseService, // 是否仅承诺服务：1 是，0 否
						otherRequireExplain: vm.form.otherRequireExplain || '', // 其他要求说明
						qualRecordRequire: vm.form.qualRecordRequire == undefined ? '' : vm.form.qualRecordRequire, // 资质备案说明
					}
					if (isQualRequire) {
						aeaImUnitRequire.qualRequireExplain = vm.form.qualRequireExplain || '' // 资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）
						aeaImUnitRequire.qualRequireType = qualRequireType // 资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项
					}
					if (isRegisterRequire) {
						aeaImUnitRequire.registerRequire = vm.form.registerRequire == undefined ? '' : vm.form.registerRequire// 执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）
						aeaImUnitRequire.registerTotal = vm.form.registerTotal == undefined ? '' : vm.form.registerTotal // 执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）
					}
					if (isRecordRequire) {
						aeaImUnitRequire.recordRequireExplain = vm.form.recordRequireExplain == undefined ? '' : vm.form.recordRequireExplain// 备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）
					}
					var isSocialFund
					if (vm.isSocialFund) { // 选择了社会资金
						isSocialFund = "1"
					} else {
						isSocialFund = "0"
					}
					var isFinancialFund
					if (vm.isFinancialFund) { // 选择了财政资金
						isFinancialFund = "1"
						vm.form.chooseInsertType = '1'
					} else {
						isFinancialFund = "0"
						vm.form.chooseInsertType = '0'
					}

					var saveAeaProjInfoVo = {
						isSocialFund: isSocialFund,// 是否为社会资金（资金来源）
						isFinancialFund: isFinancialFund, // 是否为财政资金（资金来源)
						projScaleContent: vm.form.projScaleContent == undefined ? '' : vm.form.projScaleContent, // 项目规模内容
						projScale: vm.form.projScale == undefined ? '' : vm.form.projScale, // 项目规模
						projName: vm.aeaProjInfo.projName = vm.aeaProjInfo.projName == undefined ? '' : vm.aeaProjInfo.projName, //项目名称
						parentProjId: vm.projInfoId || '', //审批项目id
						localCode: vm.form.localCode == undefined ? '' : vm.form.localCode,  //  采购项目编码
						isPurchaseProj: '', // 是否为采购项目
						approvalCode: vm.form.approvalCode || '', // 审批项目编码
					}

					if (isSocialFund == '1') {
						saveAeaProjInfoVo.socialFundProportion = vm.form.socialFundProportion == undefined ? '' : vm.form.socialFundProportion;   // 社会资金比例
					}
					if (isFinancialFund == '1') {
						saveAeaProjInfoVo.financialFundProportion = vm.form.financialFundProportion == undefined ? '' : vm.form.financialFundProportion; // 财政资金比例
					}

					var ids = [];
					$.each(vm.multipleSelection4, function (index, val) {
						ids.push(val.unitInfoId);
					});
					ids = ids.join(",");
					var params = {
						witnessName1: vm.form.witnessName1 || '',
						witnessName2: vm.witnessName2 || '',
						witnessName3: vm.witnessName3 || '',
						witnessPhone1: vm.form.witnessPhone1 || '',
						witnessPhone2: vm.witnessPhone2 || '',
						witnessPhone3: vm.witnessPhone3 || '',
						serviceTimeExplain: vm.form.timeLimitExplain || '',  // 服务时间说明
						serviceId: vm.form.serviceId || '', // 事项ID
						serviceItemId: vm.form.serviceItemId || '',// 服务类型和中介服务事项关联ID，多个用,隔开
						serviceContent: vm.form.serviceContent || '',  // 服务内容
						//projInfoId: // 服务项目ID
						ownerComplaintPhone: vm.form.ownerComplaintPhone || '', // 业主投诉电话
						moblie: vm.form.mobile || '', // 咨询电话
						isLiveWitness: vm.form.isLiveWitness || '', // 是否现场见证：1 是， 0 否
						isDiscloseIm: vm.form.isDiscloseIm || '', // 是否公示中选机构： 1 是， 0 否
						isDiscloseBidding: vm.form.isDiscloseBidding || '', // 是否公示中标公告：1 是， 0 否
						isApproveProj: vm.form.isApproveProj || '', // 是否为投资审批项目：1 是，0 否
						contacts: vm.form.contact || '',// 业主联系人
						biddingType: vm.biddingType || '', // 竞价类型：1 随机中标，2 自主选择
						amountExplain: vm.form.amountExplain || '',// 金额说明
						expirationDate: vm.form.expirationDate || '', // 报名截止时间
						choiceImunitTime: vm.form.choiceImunitTime || '', // 选取中介时间
						applyinstCode: vm.form.applyinstCode || '',// 审批流水号
						quoteType: vm.form.quoteType == undefined ? '' : vm.form.quoteType, // 0表示金额，1表示下浮率
					}
					// console.log('serviceId',vm.form.serviceId)
					// console.log('serviceItemId',params.serviceItemId)
					if (vm.biddingType == 1) {
						params.isDefineAmount = vm.form.isDefineAmount // 是否确认金额，1 是 0 否
						params.basePrice = vm.form.basePrice || '' // 最低价格（万元）
						params.isAvoid = vm.form.isAvoid;//是否有回避单位
						params.avoidReason = vm.form.avoidReason == undefined ? '' : vm.form.avoidReason;
						params.avoidUnitInfoIds = ids;
					} else if (vm.biddingType == 2) {
						params.basePrice = vm.form.basePrice || ''
						params.isDefineAmount = vm.form.isDefineAmount // 是否确认金额，1 是 0 否
						params.agentUnitInfoId = vm.multipleSelection3[0].unitInfoId // 所选中介机构id

					} else if (vm.biddingType == 3) {
						params.highestPrice = vm.form.highestPrice || '' // 最高价格（万元）
						params.basePrice = vm.form.basePrice || '' // 最低价格（万元）
						params.isAvoid = vm.form.isAvoid;//是否有回避单位
						params.avoidReason = vm.form.avoidReason == undefined ? '' : vm.form.avoidReason;
						params.avoidUnitInfoIds = ids;
					}

					if (vm.form.isApproveProj == '1') {

					}

					params.saveAeaProjInfoVo = saveAeaProjInfoVo
					params.aeaImUnitRequire = aeaImUnitRequire

					if (isQualRequire) {
						params.aeaImMajorQuals = aeaImMajorQuals
					}
					// debugger
					// console.log(params)
					// return
					vm.submitSubmit(params);

				} else {
					var noPassageArr = []
					for (var i in obj) {
						noPassageArr.push(obj[i]); //值
					}
					if (noPassageArr.length) {
						return vm.$message({
							message: noPassageArr[0][0].message,
							type: 'error'
						});
					}
					return false;
				}
			});
		},
		submitSubmit: function (params) {
			var vm = this;
			console.log(params)
			console.log(JSON.stringify(params))
			var formData = new FormData();
			for (var i in params) {
				if (typeof params[i] == "object") {
					if (i == "saveAeaProjInfoVo") {
						for (var j in params[i]) {
							formData.append('saveAeaProjInfoVo.' + j, params[i][j])
						}
					}
					if (i == "aeaImUnitRequire") {
						for (var j in params[i]) {
							formData.append('aeaImUnitRequire.' + j, params[i][j])
						}
					}
					if (i == "aeaImMajorQuals") {
						for (var j = 0; j < params[i].length; j++) {
							formData.append('aeaImMajorQuals[' + j + '].qualLevelId', params[i][j].qualLevelId)
							formData.append('aeaImMajorQuals[' + j + '].majorId', params[i][j].majorId)
						}
					}

				} else {
					formData.append(i, params[i])
				}
			}
			if (vm.formData1.length > 0) {
				vm.formData1.forEach(function (el, index) {
					formData.append('officialRemarkFile', el);
				})
			}
			if (vm.formData2.length > 0) {
				vm.formData2.forEach(function (el, index) {
					formData.append('requireExplainFile', el);
				})
			}
			$.ajax({
				type: "POST",
				url: ctx + 'supermarket/purchase/postProjPurchase',
				data: formData,
				// contentType: 'application-x-www-form-urlencoded',
				//如果传递的是FormData数据类型，那么下来的三个参数是必须的，否则会报错
				processData: false, //用于对data参数进行序列化处理，这里必须false；如果是true，就会将FormData转换为String类型
				contentType: false,  //一些文件上传http协议的关系，自行百度，如果上传的有文件，那么只能设置为false
				success: function (res) {  //请求成功后的回调函数
					vm.loading = false;
					if (res.success) {
						vm.$message({message: '保存成功', type: 'success'});
						vm.projPurchaseId = res.content // 拿到项目需要提交的id
					} else {
						vm.$message({message: res.message, type: 'error'});
					}
				},
				error: function () {
					vm.loading = false;
					vm.$message({message: '保存失败', type: 'error'});
				}
			});
		},
		submitProj: function () {
			var vm = this;
			if (!vm.projPurchaseId) {
				alertMsg('', '修改的项目信息需要先点击保存，然后才能提交', '我知道了', 'error', true);
				return false;
			}
			request('', {
				url: ctx + 'supermarket/purchase/submitProjPurchaseByProjPurchaseId', type: 'post', // 提交采购项目
				data: {
					projPurchaseId: vm.projPurchaseId
				},
			}, function (res) {
				vm.loading = false;
				if (res.success) {
					vm.$message({message: '提交成功', type: 'success'});
					vm.addNeedStatus = 0
				} else {
					vm.$message({message: res.message, type: 'error'});
				}
			}, function (msg) {
				vm.loading = false;
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		init: function () {
			this.form.isApproveProj = '1'
			this.getUnpublishedProjInfoList()

		},
		/**
		 * 获取业主单位未发布的项目列表,用于新增采购需求
		 * @return {[type]} [description]
		 */
		getUnpublishedProjInfoList: function () {
			var vm = this;
			vm.loading = true;
			request('', {
				url: ctx + 'supermarket/purchase/getUnpublishedProjInfoList', type: 'post',
				data: {
					pageNum: vm.pageNum,
					pageSize: vm.pageSize,
					projCode: vm.projCode,
				},
			}, function (res) {
				vm.loading = false;
				if (res.success) {
					var rows = res.content.rows;
					var total = res.content.total
					vm.total = total;
					vm.addNeedList = rows;
				} else {
					vm.addNeedList = [];
				}
			}, function (msg) {
				vm.loading = false;
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		/**
		 * 获取项目信息
		 * 获取项目信息,包括单位信息和联系人信息,用于新增采购需求
		 * @return {[type]} [description]
		 */
		getProUnitLinkInfo: function (projInfoId) {
			var vm = this;
			vm.biddingTypeChange(vm.biddingType);
			console.log(projInfoId)
			vm.projInfoId = projInfoId;
			request('', {
				url: ctx + 'supermarket/purchase/getProUnitLinkInfo/' + projInfoId, type: 'post',
			}, function (res) {
				console.log("获取项目信息", res)
				if (res.success) {
					var content = res.content;
					vm.aeaLinkmanInfo = content.aeaLinkmanInfo;
					vm.aeaProjInfo = content.aeaProjInfo;
					vm.aeaUnitInfo = content.aeaUnitInfo;

					vm.ProjName = content.aeaProjInfo.projName;
					vm.$set(vm.form, 'contact', content.aeaLinkmanInfo.linkmanName + '[' + content.aeaLinkmanInfo.linkmanCertNo + ']')
					//   vm.$set(vm.form,'mobile',content.aeaLinkmanInfo.linkmanMobilePhone)
					vm.basicLinkPhone = content.aeaLinkmanInfo.linkmanMobilePhone; //基本信息联系电话信息
					//   debugger
					vm.$set(vm.form, 'ownerComplaintPhone', content.ownerComplaintPhone)
					vm.form.localCode = content.aeaProjInfo.localCode;
					// vm.$set(vm.form,'ownerComplaintPhone',vm.aeaUnitInfo.mobile)
					vm.$set(vm.form, 'approvalCode', content.aeaProjInfo.localCode) // 投资审批项目编码


					if (vm.aeaProjInfo.isFinancialFund == '1') {
						vm.FinancialCheckList.push('isFinancialFund')
					}
					if (vm.aeaProjInfo.isSocialFund == '1') {
						vm.FinancialCheckList.push('isSocialFund')
					}
					vm.getAgentServiceItemList();
				} else {
					vm.$message.error(res.message);
				}
			}, function (msg) {
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		getProUnitLinkInfoWithoutId: function () {
			var vm = this;
			this.biddingTypeChange(this.biddingType)
			vm.form = new Object();
			request('', {
				url: ctx + 'supermarket/purchase/getProUnitLinkInfo', type: 'post',
			}, function (res) {
				if (res.success) {
					var content = res.content;
					vm.aeaLinkmanInfo = content.aeaLinkmanInfo;
					vm.aeaUnitInfo = content.aeaUnitInfo;
					// vm.aeaProjInfo.projName='';
					vm.$set(vm.aeaProjInfo, 'projName', '');

					if (content.aeaLinkmanInfo) {
						debugger
						vm.$set(vm.form, 'contact', content.aeaLinkmanInfo.linkmanName + '[' + content.aeaLinkmanInfo.linkmanCertNo + ']');
						//   vm.$set(vm.form,'mobile',content.aeaLinkmanInfo.linkmanMobilePhone)
						vm.basicLinkPhone = content.aeaLinkmanInfo.linkmanMobilePhone; //基本信息联系电话信息
					}

					vm.FinancialCheckList.push('isFinancialFund')
				} else {
					vm.$message.error(res.message);
				}
			}, function (msg) {
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		/**
		 *  获取中介服务事项列表
		 * @return {[type]} [description]
		 */
		getAgentServiceItemList: function () {
			var vm = this;
			request('', {
				url: ctx + 'supermarket/purchase/getAgentServiceItemList', type: 'post',
				data: {
					keyword: vm.serviceKeyword,
					pageNum: vm.pageNum2,
					pageSize: vm.pageSize2,
				},
			}, function (res) {
				if (res.success) {
					var content = res.content
					vm.total2 = res.content.total
					vm.serviceItemList = content.rows
				}
			}, function (msg) {
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		serviceItemListChange: function () {

			var vm = this;
			vm.selectdRecord = [];
			var value = vm.multipleSelection2
			if (!value.length > 0) {
				vm.$message.error('请选择服务事项');
				return false;
			}
			vm.serviceTabledialogTable = false
			console.log(value)
			// vm.form = JSON.parse(JSON.stringify(value[0]))
			console.log('form', vm.form)
			vm.$set(vm.form, 'serviceName', '');
			var chooseInsertype = ['1', '1']
			vm.$set(vm.form, 'chooseInsertype', chooseInsertype);
			var isQualRequire = value[0].isQualRequire
			var isRegisterRequire = value[0].isRegisterRequire
			var isRecordRequire = value[0].isRecordRequire
			var promiseService = value[0].promiseService
			if (isQualRequire == '1') {
				vm.isQualRequire = true;
			} else {
				vm.isQualRequire = false;
			}
			if (isRegisterRequire == '1') {
				vm.isRegisterRequire = true;
			}
			if (isRecordRequire == '1') {
				vm.isRecordRequire = true
			}
			if (promiseService == '1') {
				vm.promiseService = true
			}

			vm.form.agentOrgName = value[0].agentOrgName;
			vm.form.agentItemName = value[0].agentItemName
			vm.form.itemName = value[0].itemName
			vm.$set(vm.form, 'isApproveProj', '1')
			// vm.getAutoProjCode();
			vm.getItemServiceList(value[0].agentItemVerId)
		},
		needServiceListChange: function (item) {
			var vm = this;
			//  console.log(item)
			vm.selectdRecord = [];  //清掉上一次的选中的资质树数据
			this.$refs.treeService.setCheckedKeys([]);
			vm.qualArry = item.aeaImQualVos;
			vm.form.serviceId = item.serviceId;
			vm.form.serviceItemId = item.serviceItemId;
			for (var i = 0; i < vm.qualArry.length; i++) {
				for (var j = 0; j < vm.qualArry[i].aeaImQualLevels.length; j++) {
					vm.selectdRecord.push({level1: i, leve2: i + "-" + j, data: []})
				}
			}
			console.log(vm.selectdRecord)
			vm.qualId = vm.qualArry[0].qualId
			vm.needServiceList2 = vm.qualArry[0].aeaImQualLevels
			vm.serviceTreeData = vm.qualArry[0].aeaImServiceMajors
			vm.qualLevelId = vm.needServiceList2[0].qualLevelId
			this.setCheckedKeys()
		},
		handleClickAtiveQA: function (tab) {
			var vm = this;
			var tabIndex = Number(tab.index);
			vm.activeSN = tab.index;

			vm.needServiceList2 = vm.qualArry[tabIndex].aeaImQualLevels
			vm.serviceTreeData = vm.qualArry[tabIndex].aeaImServiceMajors
			vm.qualLevelId = vm.needServiceList2[tabIndex].qualLevelId
			vm.qualId = vm.qualArry[tabIndex].qualId
			this.setCheckedKeys()
		},
		handleClickAtiveLE: function (tab) {
			var vm = this;
			console.log(tab)
			this.$refs.treeService.setCheckedKeys([]);
			var tabIndex = Number(tab.index);
			vm.activeLE = tab.index;
			// vm.serviceTreeData = vm.qualArry[tabIndex].aeaImServiceMajors
			vm.qualLevelId = vm.needServiceList2[tabIndex].qualLevelId
			this.setCheckedKeys()
		},
		handleServiceTreeData: function (data, checkState) {
			var vm = this;
			console.log(data)
			console.log(checkState)
			console.log(vm.$refs.treeService.getCheckedKeys())
			var nodeData = vm.$refs.treeService.getNode(data);  // 获得节点数值

			console.log('选中节点', nodeData)

			var majorQualRequiresArry = []
			vm.majorQualRequiresObj = {
				majorId: data.majorId,  // 专业ID
				parentMajorId: data.parentMajorId, // 父ID
				qualId: data.qualId, // 资质ID
				qualLevelId: vm.qualLevelId, // 资质等级ID
				selected: '1', // 1为资质要求选中状态
			}

			vm.majorQualRequiresArry.push(vm.majorQualRequiresObj)
			vm.selectdRecord.forEach(function (el, index) {
				if (el.level1 == vm.activeSN && el.leve2 == vm.activeSN + '-' + vm.activeLE) {
					el.data = []
					el.data.push(vm.$refs.treeService.getCheckedKeys())
				}
			})
			console.log(vm.selectdRecord)
		},
		setCheckedKeys: function () {
			var vm = this;
			var key = []
			if (vm.selectdRecord) {
				vm.selectdRecord.forEach(function (el, index) {
					if (el.level1 == vm.activeSN && el.leve2 == vm.activeSN + '-' + vm.activeLE) {
						key = el.data;
					}
				})
				try {
					vm.$refs.treeService.setCheckedKeys(key[0]);
				} catch (e) {
					console.log('还没选专业树')
				} finally {

				}
			}
		},
		/**
		 *  根据中介服务事项  获取中介服务
		 *中介服务
		 */
		getItemServiceList: function (itemVerId) {
			var vm = this;
			request('', {
				url: ctx + 'supermarket/purchase/getItemServiceList/' + itemVerId, type: 'post',
				data: {
					itemVerId: itemVerId // 事项版本id
				},
			}, function (res) {
				console.log("获取中介服务", res)
				if (res.success) {
					var content = res.content;
					vm.needServiceList = content;
				}
			}, function (msg) {
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		/**
		 * 查询符合条件的中介机构
		 *
		 */
		getAgentUnitInfoList: function (itemVerId) {
			var vm = this;
			var isQualRequire = ''

			if (!vm.form.serviceId) {
				this.$message({
					message: '请先选择服务',
					type: 'error'
				});
				return;
			}

			if (vm.checkList.indexOf('isQualRequire') == "-1") {
				isQualRequire = '0';
			} else {
				isQualRequire = '1';
			}
			var params = JSON.stringify({
				isQualRequire: isQualRequire, // 是否需要资质要求：1 需要，0 不需要
				qualRequireType: vm.qualRequireType, // 资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项
				serviceId: vm.form.serviceId, // 服务ID
			})
			if (vm.majorQualRequiresArry.lenght !== 0) {
				vm.qualsArry.push({qualId: vm.qualId, majorQualRequires: vm.majorQualRequiresArry})
				params.quals = vm.qualsArry
			}
			console.log("请求的参数", params);
			request('', {
				url: ctx + '/supermarket/purchase/getAgentUnitInfoList', type: 'post', data: params,
				ContentType: "application/json",
			}, function (res) {
				console.log("查询符合条件的中介机构", res)
				if (res.success) {
					vm.agentUnit = res.content;
					vm.chooseAgentTabledialogTable = true;
				} else {
					vm.agentUnit = [];
					vm.chooseAgentTabledialogTable = false;
				}
			}, function (msg) {
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		/**
		 * 文件上传部分
		 */
		handleRemove1: function (file, fileList) {
			console.log(file, fileList);
		},
		handlePreview1: function (file) {
			console.log(file);
		},
		handleExceed1: function (files, fileList) {
			this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
		},
		beforeAvatarUpload: function (file) {
			var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
			var extension = testmsg === 'exe'
			var extension2 = testmsg === 'sh'
			var extension3 = testmsg === 'bat'
			var extension4 = testmsg === 'com'
			var extension5 = testmsg === 'dll'
			if (extension || extension2 || extension3 || extension4 || extension5) {
				this.$message({
					message: '禁止上传以下的附件类型：.exe,.sh,.bat,.com,.dll!',
					type: 'error'
				});
			}
			if (file.size > 10485760) {
				this.$message({
					message: '上传文件大小不能超过 10MB!',
					type: 'error'
				});
			}
			if (extension && extension2 && extension3 && extension4 && extension5 && file.size > 10485760) {
				return false;
			} else {
				return true;
			}
		},
		beforeRemove1: function (file, fileList) {
			return this.$confirm('确定移除' + file.name + '?');
		},
		onchange1: function (file, fileList) {
			var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
			var extension = testmsg === 'exe'
			var extension2 = testmsg === 'sh'
			var extension3 = testmsg === 'bat'
			var extension4 = testmsg === 'com'
			var extension5 = testmsg === 'dll'
			if (extension || extension2 || extension3 || extension4 || extension5) {
				this.$message({
					message: '禁止上传以下的附件类型：.exe,.sh,.bat,.com,.dll!',
					type: 'error'
				});
			}
			if (file.size > 10485760) {
				this.$message({
					message: '上传文件大小不能超过 10MB!',
					type: 'error'
				});
			}
			if (extension || extension2 || extension3 || extension4 || extension5 || file.size > 10485760) {
				var fileIndex = fileList.indexOf(file.name);
				fileList.splice(fileIndex, 1);
				return false;
			} else {
				this.fileList1 = fileList
			}
			this.formData1 = [];
			for (var i = 0; i < this.fileList1.length; i++) {
				this.formData1.push(this.fileList1[i].raw);
			}
			console.log(this.fileList1)
		},
		deluploadImg1: function (item, index) {
			var vm = this;
			confirmMsg('确定移除' + item.name + '?', '', function () {
				vm.fileList1.splice(index, 1);
			})

		},


		handleRemove2: function (file, fileList) {
			console.log(file, fileList);
		},
		handlePreview2: function (file) {
			console.log(file);
		},
		handleExceed2: function (files, fileList) {
			this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
		},
		beforeUpload2: function (file) {

		},
		beforeRemove2: function (file, fileList) {
			return this.$confirm('确定移除' + file.name + '?');
		},
		onchange2: function (file, fileList) {
			var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
			var extension = testmsg === 'exe'
			var extension2 = testmsg === 'sh'
			var extension3 = testmsg === 'bat'
			var extension4 = testmsg === 'com'
			var extension5 = testmsg === 'dll'
			if (extension || extension2 || extension3 || extension4 || extension5) {
				this.$message({
					message: '禁止上传以下的附件类型：.exe,.sh,.bat,.com,.dll!',
					type: 'error'
				});
			}
			if (file.size > 10485760) {
				this.$message({
					message: '上传文件大小不能超过 10MB!',
					type: 'error'
				});
			}
			if (extension || extension2 || extension3 || extension4 || extension5 || file.size > 10485760) {
				var fileIndex = fileList.indexOf(file.name);
				fileList.splice(fileIndex, 1);
				return false;
			} else {
				this.fileList2 = fileList
			}
			this.formData2 = []
			for (var i = 0; i < this.fileList2.length; i++) {
				this.formData2.push(this.fileList2[i].raw);
			}
		},
		deluploadImg2: function (item, index) {
			var vm = this;
			confirmMsg('确定移除' + item.name + '?', '', function () {
				vm.fileList2.splice(index, 1);
			})

		},

		handleSizeChange2: function (val) {
			this.pageSize2 = val;
			this.getAgentServiceItemList()
		},
		handleCurrentChange2: function (val) {
			this.pageNum2 = val;
			this.getAgentServiceItemList()
		},
		handleSelectionChange2: function (val) {
			this.multipleSelection2 = val;
			if (val.length > 1) {
				this.$refs.multipleTable2.clearSelection();
				this.$refs.multipleTable2.toggleRowSelection(val.pop());
			}
		},

		handleSizeChange3: function (val) {
			this.pageSize3 = val;
			this.getAgentServiceItemList()
		},
		handleCurrentChange3: function (val) {
			this.pageNum3 = val;
			this.getAgentServiceItemList()
		},
		handleSizeChange4: function (val) {
			this.pageSize4 = val;
			this.selectOrgan()
		},
		handleCurrentChange4: function (val) {
			this.pageNum4 = val;
			this.selectOrgan()
		},
		handleSelectionChange3: function (val) {
			var _newArr = JSON.parse(JSON.stringify(val));
			this.multipleSelection3 = _newArr;
			if (val.length > 1) {
				this.$refs.multipleTable3.clearSelection();
				this.$refs.multipleTable3.toggleRowSelection(val.pop());
			}
		},
		handleSelectionChange4: function (val) {
			//   debugger
			this.multipleSelection4 = val;

		},
		resetData: function () {
			this.pageNum1 = 1;
			this.pageSize1 = 10;
			this.pageNum2 = 1;
			this.pageSize2 = 10;
			this.pageNum3 = 1;
			this.pageSize3 = 10;
		},
		basePriceblur: function () {
			var inputBasePrice = this.form.basePrice;
			this.basePriceDx = this.DX(inputBasePrice);
		},
		highestPriceblur: function () {
			var inputhighestPrice = this.form.highestPrice;
			this.highestPriceDx = this.DX(inputhighestPrice);
		},
		basePriceinput: function () {
			var inputbasePrice = this.form.basePrice;
			this.basePriceDx = this.DX(inputbasePrice);
		},
		DX: function (n) {
			if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
				return "";
			var unit = "千百拾亿千百拾万千百拾元角分", str = "";
			n += "00";
			var p = n.indexOf('.');
			if (p >= 0)
				n = n.substring(0, p) + n.substr(p + 1, 2);
			unit = unit.substr(unit.length - n.length);
			for (var i = 0; i < n.length; i++)
				str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
			return str.replace(/零(千|百|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整");
		},
		biddingTypeChange: function (val) {
			var vm = this;
			console.log(val)
			if (val == 3) {
				vm.$set(vm.form, 'quoteType', '0')
			}
			if (val == 1) {
				vm.$nextTick(function () {
					vm.$set(vm.form, 'isDefineAmount', '1')
					vm.$set(vm.form, 'isAvoid', '0')
				})
			}
		},
		chooseOrgan: function () {
			this.avoidData = this.multipleSelection4;
			this.avoidOrgan = false;
		},
		serachOrgan: function () {
			this.selectOrgan();
		},
		selectOrgan: function () {
			var _this = this;
			_this.avoidOrgan = true;
			request('', {
				url: ctx + 'supermarket/agentservice/listCheckinUnit', type: 'get', data: {
					pageNum: this.pageNum4,
					pageSize: this.pageSize4,
					applicant: this.organWord,
				}
			}, function (data) {
				_this.avoidList = data.content.rows;
				_this.total4 = data.content.total;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},

		// keyword模糊查询项目信息
		searchProjInfoByKeyword: function (queryString) {
			var _that = this;
			_that.projInfoId = '';
			_that.projInfoByKeyword = queryString;
			if (typeof (queryString) != 'undefined') queryString = queryString.trim();
			if (queryString != '') {
				_that.loadingProjInfo = true;
				request('', {
					url: ctx + 'supermarket/purchase/info',
					type: 'get',
					data: {"keyword": queryString},
				}, function (result) {
					if (result.content) {
						_that.projInfoList = result.content;
						_that.loadingProjInfo = false;
					}
				}, function (msg) {
					_that.projInfoList = [];
					_that.loadingProjInfo = false;
				})
			} else {
				_that.projInfoList = [];
			}
		},

		projNameSel: function (item) {
			this.projInfoId = item.projInfoId;
			this.projName = item.projName;
			this.form.approvalCode = item.localCode;
			this.form.parentProjId = item.projInfoId;
		},
	},
	mounted: function () {
		this.loginUserInfo = JSON.parse(localStorage.getItem("loginInfo")) || {}
		this.init();
	},
	computed: {
		unNeeds: function () {
			if (this.isQualRequire || this.isRegisterRequire || this.isRecordRequire) {
				return true;
			}
		},
		needs: function () {
			if (this.promiseService) {
				return true;
			}
		},
		isDefineAmountchange: function () {
			return this.biddingType != '3' ? 'isDefineAmount' : '';
		},
		isDiscloseImchange: function () {
			return this.biddingType == '1' ? 'isDiscloseIm' : '';
		},
		expirationDatechange: function () {
			return this.biddingType == '2' ? 'expirationDate' : '';
		},
		agentUnitNamechange: function () {
			return this.biddingType == '2' ? 'agentUnitName' : '';
		},
		basePricechange: function () {
			return this.form.isDefineAmount == '1' ? 'basePrice' : '';
		},
		highestPricechange: function () {
			console.log(123)
			return this.biddingType == '3' ? 'highestPrice' : '';
		},
		qualRequireTypechange: function () {
			return this.isQualRequire ? 'qualRequireType' : '';
		},
		qualRequireExplainchange: function () {
			return this.isQualRequire ? 'qualRequireExplain' : '';
		},
		registerTotalchange: function () {
			console.log(this.isRegisterRequire)
			return this.isRegisterRequire ? 'registerTotal' : ' ';
		},
		registerRequirechange: function () {
			return this.isRegisterRequire ? 'registerRequire' : ' ';
		},
		recordRequireExplainchange: function () {
			return this.isRecordRequire ? 'recordRequireExplain' : ' ';
		},
		// choiceImunitTimechange:function(){
		//     return this.biddingType != 2? 'recordRequireExplain' : '';
		// },
		approvalCodechange: function () {
			return this.form.isApproveProj == 1 ? 'approvalCode' : '';
		},
		financialFundProportionchange: function () {
			return this.isFinancialFund == 1 ? 'financialFundProportion' : '';
		},
		socialFundProportionchange: function () {
			return this.isSocialFund == 1 ? 'socialFundProportion' : '';
		},
		witnessName1change: function () {
			return this.form.isLiveWitness != 0 ? 'witnessName1' : ' ';
		},
		witnessPhone1change: function () {
			return this.form.isLiveWitness != 0 ? 'witnessPhone1' : ' ';
		}
	},
	watch: {
		serviceKeyword: function () {
			this.resetData()
			this.getAgentServiceItemList()
		},
		isFinancialFund: function (newVal, oldVal) {
			console.log(newVal)
			if (newVal) {
				this.form.chooseInsertype.push('1');
			} else {
				if (!this.isSocialFund) {
					this.form.chooseInsertype = [];
				}
			}
		},
		isSocialFund: function (newVal, oldVal) {
			if (newVal) {
				this.form.chooseInsertype.push('1');
			} else {
				if (!this.isFinancialFund) {
					this.form.chooseInsertype = [];
				}
			}
		}
	}
})
