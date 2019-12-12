var module1 = new Vue({
	el: '#addNeedPaurse',
	mixins: [mixins],
	data: function () {
		var checkMissValue = function (rule, value, callback) {
			if (value === '' || value === undefined || value === null) {
				callback(new Error('该输入项为必输项！'));
			} else if (value.toString().trim() === '') {
				callback(new Error('该输入项为必输项！'));
			} else {
				callback();
			}
		};

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
		return {
			mloading: false, // 整屏loading
			itemVerId: '', // 事项版本id
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
			multipleSelection3: [],
			projInfoId: '', // 项目主键ID
			projCode: '', // 项目代码
			addNeedList: [],// 新增采购需求   // 新增采购需求查询结果列表
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
				isApproveProj: [
					{required: true, message: '请选择投资审批项目', trigger: 'change'}
				],
				approvalCode: [
					{required: true, message: '投资审批项目编码', trigger: 'change'}
				],
				contact: [
					{required: true, message: '请输入项目联系人', trigger: 'change'}
				],
				basicLinkPhone: [
					{required: true, validator: checkPhone, trigger: 'change'}
				],
				mobile: [
					//  { required: true, message: '请输入联系电话', trigger: 'blur' },
					{required: true, validator: checkPhone, trigger: 'change'}
				],
				projName: [
					{required: true, message: '请输入采购项目名称', trigger: 'change'}
				],
				projScale: [
					{required: true, message: '请输入项目规模', trigger: 'change'}
				],
				projScaleContent: [
					{required: true, message: '请输入项目规模描述', trigger: 'change'}
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
					{required: true, message: '请输入执业/职业人员总数', trigger: 'change'}
				],
				registerRequire: [
					{required: true, message: '请输入执业/职业人员要求', trigger: 'change'}
				],
				serviceName: [
					{required: true, message: '请选择所需服务', trigger: 'change'}
				],
				timeLimitExplain: [
					{required: true, message: '请填写服务时限说明', trigger: 'change'}
				],
				serviceContent: [
					{required: true, message: '请填写服务内容', trigger: 'change'}
				],
				amountExplain: [
					{required: true, message: '请填写金额说明', trigger: 'change'}
				],
				recordRequireExplain: [
					{required: true, message: '请填写备案要求说明', trigger: 'change'}
				],
				// qualRequireType:[
				//     { required: true, message: '请选择资质要求', trigger: 'change' }
				// ],
				// qualRequireExplain:[
				//     { required: true, message: '请填写资质要求说明', trigger: 'change' }
				// ],
				isDefineAmount: [
					{required: true, message: '请选择是否确认金额', trigger: 'change'}
				],
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
					{required: true, message: '请选择截止时间', trigger: 'change'}
				],
				choiceImunitTime: [
					{required: true, message: '请选择选取中介时间', trigger: 'change'}
				],
				basePrice: [
					{required: true, message: '请填写服务金额', trigger: 'change'}
				],
				isAvoid: [
					{required: true, message: '请选择是否有回避情况', trigger: 'change'}
				],
				highestPrice: [
					{required: true, message: '请填写服务最高金额', trigger: 'change'}
				],
				unitInfoId: [
					{required: true, message: '请选择指定的中介机构', trigger: 'change'}
				],
				witnessName1: [
					{required: true, message: '请填写见证人姓名', trigger: 'change'}
				],
				witnessPhone1: [
					{required: true, message: '请填写见证人联系电话', trigger: 'change'}
				],
				ownerComplaintPhone: [
					{required: true, message: '请填写投诉质疑电话', trigger: 'change'}
				],
				biddingType: [
					{required: true, message: '请选取中介服务机构方式', trigger: 'change'}
				],
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
			form: {
				isApproveProj: 1,
				isDefineAmount: '1',
				chooseInsertype: ['1', '1'],
				isAvoid: "0",
				biddingType: '1',
				basePrice: '',
				isDiscloseIm: '1',
				isDiscloseBidding: '1',
				basicLinkPhone: ''
			},
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
			basicLinkPhone: '',
			loadingFile: false, // 文件上传loading
			loadingFileWin: false, // 窗口文件上传loading
			showUploadWindowFlag: false, // 是否展示文件上传窗口
			showUploadWindowBtn: true, // 是否展示文件上传窗口操作按钮
			showUploadWindowTitle: '材料附件', // 文件上传窗口header
			uploadTableData: [],
			AllFileList: [], // 智能分拣区所选择文件
			fileList: [],
			checkAll: true, // 智能分拣区文件是否全选
			checkedSelFlie: [], // 智能分拣区已选文件
			AllFileListId: [], // 已选文件name集合
			matIds: [], // 材料matIdS
			selMatinstId: '', // 已选材料实例Id
			matinstIds: [], // 材料实例Ids
			model: {
				rules: {
					getPaper: {required: true, message: "必选", trigger: ["change"]},
					getCopy: {required: true, message: "必选", trigger: ["change"]},
					realPaperCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['change']},
					realCopyCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['change']},
				},
				matsTableData: []
			},
			showFileListKey: [], // 展示材料下文件列表
			showMatTableExpand: false, // 材料列表是否展示expand
			getPaperAll: false,
			getCopyAll: false,
			homePageSize: 10,
			homepPage: 1,
			homeTotal: 0,
			isShowServiceDetail: false, // 是否显示中介服务对应详细信息
			serviceDetail: {}, // 中介服务对应详细信息
			stateList: [], // 情形选中列表
			attachmentUrl: ctx + 'supermarket/purchase/mat/uploadPurchaseAtt', // 附加上传url
			recordId1: '', // 附件1多文件上传所需id
			recordId2: '', // 附件2多文件上传所需id
			materialUploadUrl: ctx + 'supermarket/purchase/mat/att/upload', // 材料一单清电子件上传url
		}
	},
	methods: {
		// 清空表单填写过的数据,并初始化配置数据
		resetForm: function () {
			this.form = new Object();
			this.$set(this.form, 'isDiscloseIm', '1');
			this.$set(this.form, 'isDiscloseBidding', '1');
			this.$set(this.form, 'isApproveProj', '1');
			this.$set(this.form, 'approvalCode', '');
			// this.form.isDiscloseIm = '1';
			// this.form.isDiscloseBidding = '1';
			// this.form.approvalCode = '';
			// this.form.isApproveProj = '1';

			this.isQualRequire = false;
			this.isRegisterRequire = false;
			this.isRecordRequire = false;
			this.promiseService = false;

			this.formData1 = [];
			this.formData2 = [];

			this.AllFileList = [];
			this.model.matsTableData = [];
		},
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

		saveForm: function (formName) {
			var vm = this;
			// console.log(vm.form.chooseInsertype)
			this.$refs[formName].validate(function (valid, obj) {
				if (valid) {
					// if (valid) {
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
						projName: vm.form.projName = vm.form.projName == undefined ? '' : vm.form.projName, //项目名称
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
						// witnessName1: vm.form.witnessName1 || '',
						// witnessName2: vm.witnessName2 || '',
						// witnessName3: vm.witnessName3 || '',
						// witnessPhone1: vm.form.witnessPhone1 || '',
						// witnessPhone2: vm.witnessPhone2 || '',
						// witnessPhone3: vm.witnessPhone3 || '',
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
						biddingType: vm.form.biddingType || '', // 竞价类型：1 随机中标，2 自主选择
						amountExplain: vm.form.amountExplain || '',// 金额说明
						expirationDate: vm.form.expirationDate || '', // 报名截止时间
						choiceImunitTime: vm.form.choiceImunitTime || '', // 选取中介时间
						applyinstCode: vm.form.applyinstCode || '',// 审批流水号
						quoteType: vm.form.quoteType == undefined ? '' : vm.form.quoteType, // 0表示金额，1表示下浮率
					}
					// console.log('serviceId',vm.form.serviceId)
					// console.log('serviceItemId',params.serviceItemId)
					if (vm.form.biddingType == 1) {
						params.isDefineAmount = vm.form.isDefineAmount // 是否确认金额，1 是 0 否
						params.basePrice = vm.form.basePrice || '' // 最低价格（万元）
						params.isAvoid = vm.form.isAvoid;//是否有回避单位
						params.avoidReason = vm.form.avoidReason == undefined ? '' : vm.form.avoidReason;
						params.avoidUnitInfoIds = ids;
					} else if (vm.form.biddingType == 2) {
						params.basePrice = vm.form.basePrice || ''
						params.isDefineAmount = vm.form.isDefineAmount // 是否确认金额，1 是 0 否
						params.agentUnitInfoId = vm.multipleSelection3[0].unitInfoId // 所选中介机构id

					} else if (vm.form.biddingType == 3) {
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

					params.itemVerId = vm.itemVerId;
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
			this.$refs.formTest.validate(function (valid) {
				if (valid) {
					var formData = new FormData();
					var matCountVos = [];
					var selMatinstId = []; // 材料实例id集合
					vm.model.matsTableData.map(function (item) {
						if (item) {
							var copyCnt = 0;
							var paperCnt = 0;

							if (item.matinstId) {
								selMatinstId.push(item.matinstId);
							}
							if (item.certMatinstIds && item.certMatinstIds.length > 0) {
								selMatinstId = selMatinstId.concat(item.certMatinstIds);
							}
							if (item.getCopy == true) {
								copyCnt = item.realCopyCount;
							}
							if (item.getPaper == true) {
								paperCnt = item.realPaperCount;
							}
							if (item.getCopy == true || item.getPaper == true) {
								matCountVos.push({
									"certId": item.certId,
									"certName": item.certName,
									"copyCnt": copyCnt,
									"formName": item.formName,
									"itemVerId": item.itemVerId,
									"matId": item.matId,
									"matProp": item.matProp,
									"paperCnt": paperCnt,
									"stoFormId": item.stoFormId
								})
							}
						}
					});

					formData.append('matCountVos', matCountVos);
					formData.append('matinstsIds', selMatinstId);

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
						// vm.formData1.forEach(function (el, index) {
						// 	formData.append('officialRemarkFile', el);
						// })
						formData.append('officialRemarkFile', vm.recordId1);
					}
					if (vm.formData2.length > 0) {
						// vm.formData2.forEach(function (el, index) {
						// 	formData.append('requireExplainFile', el);
						// })
						formData.append('requireExplainFile', vm.recordId2);
					}

					vm.mloading = true;
					$.ajax({
						type: "POST",
						url: ctx + 'supermarket/purchase/startProjPurchase',
						data: formData,
						// contentType: 'application-x-www-form-urlencoded',
						//如果传递的是FormData数据类型，那么下来的三个参数是必须的，否则会报错
						processData: false, //用于对data参数进行序列化处理，这里必须false；如果是true，就会将FormData转换为String类型
						contentType: false,  //一些文件上传http协议的关系，自行百度，如果上传的有文件，那么只能设置为false
						success: function (res) {  //请求成功后的回调函数
							vm.mloading = false;
							if (res.success) {
								vm.$message({message: '保存成功', type: 'success'});
								vm.projPurchaseId = res.content // 拿到项目需要提交的id

								// 显示列表页，刷新列表数据
								vm.addNeedStatus = 0;
								vm.getUnpublishedProjInfoList();
							} else {
								vm.$message({message: res.message, type: 'error'});
							}
						},
						error: function () {
							vm.mloading = false;
							vm.$message({message: '保存失败', type: 'error'});
						}
					});
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
				vm.mloading = false;
				if (res.success) {
					vm.$message({message: '提交成功', type: 'success'});
					vm.addNeedStatus = 0
				} else {
					vm.$message({message: res.message, type: 'error'});
				}
			}, function (msg) {
				vm.mloading = false;
				vm.$message({message: '加载失败', type: 'error'});
			});
		},
		init: function () {
			// this.form.isApproveProj = '1'
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
			vm.biddingTypeChange(vm.form.biddingType);
			console.log(projInfoId)
			vm.projInfoId = projInfoId;
			request('', {
				url: ctx + 'supermarket/purchase/getProUnitLinkInfo/' + projInfoId, type: 'post',
			}, function (res) {
				console.log("获取项目信息", res)
				if (res.success) {
					var content = res.content;
					vm.aeaLinkmanInfo = !!content.aeaLinkmanInfo ? content.aeaLinkmanInfo : {};
					vm.aeaProjInfo = content.aeaProjInfo;
					vm.aeaUnitInfo = content.aeaUnitInfo;

					vm.ProjName = content.aeaProjInfo.projName;
					if (!!content.aeaLinkmanInfo) {
						vm.$set(vm.form, 'contact', content.aeaLinkmanInfo.linkmanName + '[' + content.aeaLinkmanInfo.linkmanCertNo + ']');
						vm.$set(vm.form, 'basicLinkPhone', content.aeaLinkmanInfo.linkmanMobilePhone);
						vm.$set(vm.form, 'ownerComplaintPhone', content.aeaLinkmanInfo.linkmanMobilePhone)
					}
					//   vm.$set(vm.form,'mobile',content.aeaLinkmanInfo.linkmanMobilePhone)
					if (!!content.aeaLinkmanInfo) vm.basicLinkPhone = content.aeaLinkmanInfo.linkmanMobilePhone; //基本信息联系电话信息
					//   debugger
					vm.$set(vm.form, 'ownerComplaintPhone', content.ownerComplaintPhone);
					vm.form.localCode = content.aeaProjInfo.localCode;
					vm.$set(vm.form, 'approvalCode', content.aeaProjInfo.localCode);// 投资审批项目编码


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
			this.biddingTypeChange(this.form.biddingType)
			this.resetForm();
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
						// debugger
						vm.$set(vm.form, 'contact', content.aeaLinkmanInfo.linkmanName + '[' + content.aeaLinkmanInfo.linkmanCertNo + ']');
						//   vm.$set(vm.form,'mobile',content.aeaLinkmanInfo.linkmanMobilePhone)
						vm.basicLinkPhone = content.aeaLinkmanInfo.linkmanMobilePhone; //基本信息联系电话信息
						vm.$set(vm.form, 'basicLinkPhone', vm.aeaLinkmanInfo.linkmanMobilePhone);
					}

					vm.FinancialCheckList.push('isFinancialFund');
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
				// url: ctx + 'supermarket/purchase/getAgentItemList', type: 'post',
				url: ctx + 'supermarket/purchase/getAgentItemList', type: 'get',
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

			vm.serviceDetail = JSON.parse(JSON.stringify(vm.multipleSelection2[0]));
			vm.itemVerId = value[0].agentItemVerId;
			vm.isShowServiceDetail = true;

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
			vm.getItemServiceList(value[0].agentItemVerId);
			vm.getStatusStateMats(value[0].agentItemVerId); // 获取阶段
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
		//直接选取时选择中介机构
		selectAgentUnit: function () {
			var _this = this;
			_this.chooseAgentTabledialogTable = false;
			if (_this.multipleSelection3.length == 0) {
				_this.$alert('还未选择机构', '提示', {
					confirmButtonText: '确定',
				});
			} else {
				_this.unitInfoId = _this.multipleSelection3[0].unitInfoId;
				_this.form.agentUnitName = _this.multipleSelection3[0].agentUnitName;
			}

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
			// var extension = testmsg === 'exe'
			// var extension2 = testmsg === 'sh'
			// var extension3 = testmsg === 'bat'
			// var extension4 = testmsg === 'com'
			// var extension5 = testmsg === 'dll'
			if (['exe', 'sh', 'bat', 'com', 'dll'].indexOf(testmsg) > -1) {
				this.$message({
					message: '禁止上传以下的附件类型：.exe,.sh,.bat,.com,.dll!',
					type: 'error'
				});
				return false;
			}
			if (file.size > 10485760) {
				this.$message({
					message: '上传文件大小不能超过 10MB!',
					type: 'error'
				});
				return false;
			}
			// if (extension && extension2 && extension3 && extension4 && extension5 && file.size > 10485760) {
			// 	return false;
			// } else {
			// 	return true;
			// }
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
			// this.formData1 = [];
			// for (var i = 0; i < this.fileList1.length; i++) {
			// 	this.formData1.push(this.fileList1[i].raw);
			// }
			console.log(this.fileList1)
		},
		deluploadImg1: function (item, index) {
			var vm = this;
			confirmMsg('确定移除' + item.attName + '?', '', function () {
				// vm.fileList1.splice(index, 1);
				// vm.formData1.splice(index, 1);
				request('', {
					url: ctx + 'supermarket/purchase/mat/att/batch/delete',
					type: 'get',
					data: {
						recordId: vm.recordId1,
						detailIds: item.detailId
					}
				}, function (data) {
					vm.formData1 = data.content.attForms;

					_this.$message({
						message: '删除成功',
						type: 'success'
					});
				}, function (msg) {
					_this.$message({
						message: '删除失败',
						type: 'error'
					});
					// _this.loading = false;
				});
			})

		},
		debounceHandler1: function (file) {
			var _that = this;

			this.debounce1(this.uploadFile1, file);
		},
		debounce1: function (func, file) {
			var vm = this;
			window.clearTimeout(func.tId);
			func.temArr = func.temArr || [];
			func.temArr.push(file);
			console.log(file);
			func.tId = window.setTimeout(function () {
				func(func.temArr);
				func.temArr = [];
			}, 1000);
		},
		uploadFile1: function (file) {
			var vm = this;
			vm.mloading = true;
			var formData = new FormData();

			file.forEach(function (u) {
				// Vue.set(u.file, 'officialRemarkFile', u.file.name);
				formData.append('officialRemarkFile', u.file);
			})
			// formData.append('officialRemarkFile', file.file);
			formData.append('recordId', vm.recordId1);

			$.ajax({
				type: "POST",
				url: ctx + 'supermarket/purchase/mat/uploadPurchaseAtt',
				data: formData,
				// contentType: 'application-x-www-form-urlencoded',
				//如果传递的是FormData数据类型，那么下来的三个参数是必须的，否则会报错
				processData: false, //用于对data参数进行序列化处理，这里必须false；如果是true，就会将FormData转换为String类型
				contentType: false,  //一些文件上传http协议的关系，自行百度，如果上传的有文件，那么只能设置为false
				success: function (res) {  //请求成功后的回调函数
					vm.mloading = false;
					if (res.success) {
						vm.recordId1 = res.content.recordId;
						vm.formData1 = res.content.attForms;
						vm.$message({message: '上传成功', type: 'success'});
					} else {
						vm.$message({message: res.message, type: 'error'});
					}
				},
				error: function () {
					vm.mloading = false;
					vm.$message({message: '上传失败', type: 'error'});
				}
			});
		},
		debounceHandler2: function (file) {
			var _that = this;

			this.debounce2(this.uploadFile2, file);
		},
		debounce2: function (func, file) {
			var vm = this;
			window.clearTimeout(func.tId);
			func.temArr = func.temArr || [];
			func.temArr.push(file);
			console.log(file);
			func.tId = window.setTimeout(function () {
				func(func.temArr);
				func.temArr = [];
			}, 1000);
		},
		uploadFile2: function (file) {
			var vm = this;
			vm.mloading = true;
			var formData = new FormData();

			file.forEach(function (u) {
				// Vue.set(u.file, 'officialRemarkFile', u.file.name);
				formData.append('requireExplainFile', u.file);
			})
			// formData.append('officialRemarkFile', file.file);
			formData.append('recordId', vm.recordId2);

			$.ajax({
				type: "POST",
				url: ctx + 'supermarket/purchase/mat/uploadPurchaseAtt',
				data: formData,
				// contentType: 'application-x-www-form-urlencoded',
				//如果传递的是FormData数据类型，那么下来的三个参数是必须的，否则会报错
				processData: false, //用于对data参数进行序列化处理，这里必须false；如果是true，就会将FormData转换为String类型
				contentType: false,  //一些文件上传http协议的关系，自行百度，如果上传的有文件，那么只能设置为false
				success: function (res) {  //请求成功后的回调函数
					vm.mloading = false;
					if (res.success) {
						vm.recordId2 = res.content.recordId;
						vm.formData2 = res.content.attForms;
						vm.$message({message: '上传成功', type: 'success'});
					} else {
						vm.$message({message: res.message, type: 'error'});
					}
				},
				error: function () {
					vm.mloading = false;
					vm.$message({message: '上传失败', type: 'error'});
				}
			});
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
			// this.formData2 = []
			// for (var i = 0; i < this.fileList2.length; i++) {
			// 	this.formData2.push(this.fileList2[i].raw);
			// }
		},
		deluploadImg2: function (item, index) {
			var vm = this;
			confirmMsg('确定移除' + item.name + '?', '', function () {
				// vm.fileList2.splice(index, 1);
				// vm.formData2.splice(index, 1);
				request('', {
					url: ctx + 'supermarket/purchase/mat/att/batch/delete',
					type: 'get',
					data: {
						recordId: vm.recordId2,
						detailIds: item.detailId
					}
				}, function (data) {
					vm.formData2 = data.content.attForms;

					_this.$message({
						message: '删除成功',
						type: 'success'
					});
				}, function (msg) {
					_this.$message({
						message: '删除失败',
						type: 'error'
					});
					// _this.loading = false;
				});
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
		//直接选取中介机构，勾选中介机构时的方法
		handleSelectionChange3: function (val) {
			if (!val) return false;

			var _newArr = JSON.parse(JSON.stringify(val));
			this.multipleSelection3 = _newArr;
			this.form.unitInfoId = _newArr[0].unitInfoId;

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
		//选取中介机构方式改变方法
		biddingTypeChange: function (val) {
			var vm = this;
			console.log(val)
			if (val == 3) {
				vm.$set(vm.form, 'quoteType', '0')
			}
			if (val == 2) {
				vm.$set(vm.form, 'isDefineAmount', '1');
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
			_this.mloading = true;
			request('', {
				url: ctx + 'supermarket/agentservice/listCheckinUnit', type: 'get', data: {
					pageNum: this.pageNum4,
					pageSize: this.pageSize4,
					applicant: this.organWord,
				}
			}, function (data) {
				_this.mloading = false;
				_this.avoidList = data.content.rows;
				_this.total4 = data.content.total;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.mloading = false;
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

			// 0：政府；1：企业；2：混合
			if (item.financialSource === '0') {
				this.isFinancialFund = true;
				this.isSocialFund = false;
			} else if (item.financialSource === '1') {
				this.isFinancialFund = false;
				this.isSocialFund = true;
			} else {
				this.isFinancialFund = true;
				this.isSocialFund = true;
			}
		},


		// 删除数组里含有某值的对应项
		removeArray: function (_arr, _obj) {
			if (!_arr) return;
			var length = _arr.length;
			if (length.length == 0) return;
			for (var i = 0; i < length; i++) {
				if (_arr[i] === _obj) {
					_arr.splice(i, 1);
					return _arr;
				}
			}
		},
		// pageSize 改变时会触发
		homeHandleSizeChange: function (val) {
			this.pageSize = val;
			this.getUnpublishedProjInfoList();
		},
		// currentPage 改变时会触发
		homeHandleCurrentChange: function (val) {
			this.pageNum = val;
			this.getUnpublishedProjInfoList();
		},
		myUploadFile: function (file) {
			var _that = this;
			_that.checkedSelFlie.map(function (item) {
				if (item == file.file.name) {
					_that.fileData.append('file', file.file);
				}
			});
		},
		// 文件上传 change事件
		fileChange: function (file, fileList) {
			var _that = this;
			_that.AllFileListId = [];
			if (fileList.length > 0) {
				this.AllFileList = this.unique(fileList, 'file');
				this.AllFileList.map(function (item) {
					_that.AllFileListId.push(item.name);
				});
				_that.checkedSelFlie = _that.AllFileListId;
			}
		},
		// 材料 事项去重
		unique: function (arr, flag) {
			var result = {};
			var finalResult = [];
			var arr = arr.reverse();
			for (var i = 0; i < arr.length; i++) {
				if (flag == 'mats') {
					result[arr[i].matId] = arr[i];
				} else if (flag == 'stage') {
					result[arr[i].itemVerId] = arr[i];
				} else if (flag == 'file') {
					result[arr[i].name] = arr[i];
				}
			}
			// for (item in result) {
			//     finalResult.push(result[item]);
			// }
			var keysItems = Object.keys(result);
			keysItems.map(function (item) {
				finalResult.push(result[item]);
			})
			return finalResult.reverse();
		},
		// 文件上传 submit事件
		submitUpload: function () {
			this.fileData = new FormData();
			this.$refs.uploadFile.submit();
			var _that = this;
			this.fileData.append("projInfoId", _that.projInfoId);
			this.fileData.append("matIds", _that.matIds);
			this.fileData.append("matinstIds", _that.matinstIds);
			this.fileData.append("unitInfoId", _that.rootUnitInfoId);
			this.uploadLogo = "1";
			if (_that.checkedSelFlie.length == 0) {
				alertMsg('', '请选择需要自动分拣的文件', '关闭', 'warning', true);
				return false;
			} else {
				_that.loadingFile = true;
				axios.post(ctx + 'supermarket/purchase/mat/att/upload/auto', _that.fileData).then(function (res) {
					_that.checkedSelFlie = [];
					_that.AllFileList = [];
					_that.loadingFile = false;
					_that.$refs.uploadFile.clearFiles();
					_that.checkAll = false;
					var matinstIdsObj = [];
					res.data.content.map(function (item) {
						_that.showFileListKey.push(item.matId);
						_that.model.matsTableData.map(function (matItem) {
							if (item.matId == matItem.matId) {
								matItem.matinstId = item.matinstId;
								matItem.matChild = item.fileList;
							}
							if (matItem.matChild.length > 0) {
								_that.showMatTableExpand = true;
							}
							if (matinstIdsObj.indexOf(matItem.matinstId) < 0 && matItem.matinstId != '') {
								matinstIdsObj.push(matItem.matinstId);
							}
						});
					});
					_that.matinstIds = matinstIdsObj.join(',')
				}).catch(function (error) {
					if (error.response) {
						// content.onError('配时文件上传失败(' + error.response.status + ')，' + error.response.data);
					} else if (error.request) {
						// content.onError('配时文件上传失败，服务器端无响应');
					} else {
						// content.onError('配时文件上传失败，请求封装失败');
					}
					_that.loadingFile = false;
				});
			}

		},
		handleCheckedCitiesChange: function (value) {
			var checkedCount = value.length;
			this.checkAll = checkedCount === this.AllFileListId.length;
			this.checkedSelFlie = value;
		},
		handleCheckAllChange: function (val) {
			this.checkedSelFlie = val ? this.AllFileListId : [];
		},
		// 获取文件后缀
		getFileType: function (fileName) {
			var index1 = fileName.lastIndexOf(".");
			var index2 = fileName.length;
			var suffix = fileName.substring(index1 + 1, index2).toLowerCase();//后缀名
			if (suffix == 'docx') {
				suffix = 'doc';
			}
			return suffix;
		},
		// 获取已上传文件列表
		getFileListWin: function (matinstId, rowData) {
			var _that = this;
			request('', {
				url: ctx + 'supermarket/purchase/mat/att/list',
				type: 'get',
				data: {matinstId: matinstId}
			}, function (res) {
				if (res.success) {
					// if(res.content){
					_that.uploadTableData = res.content ? res.content : [];
					if (rowData) {
						rowData.matChild = res.content ? res.content : [];
					}
					if (rowData.matChild.length > 0) {
						_that.showMatTableExpand = true;
					}
					// }
				} else {
					_that.$message({
						message: res.message ? res.message : '加载已上传材料列表失败',
						type: 'error'
					});
				}
			}, function (msg) {
				_that.$message({
					message: '服务请求失败',
					type: 'error'
				});
			});
		},
		//下载单个附件
		downOneFile: function (data) {
			window.open(ctx + 'supermarket/purchase/mat/att/download?detailIds=' + data.fileId, '_blank')
		},
		//删除单个文件附件
		delOneFile: function (data, matData) {
			var _that = this;
			console.log(data);
			console.log(matData)
			request('', {
				url: ctx + 'supermarket/purchase/mat/att/batch/delete',
				type: 'get',
				data: {matinstId: matData.matinstId, detailIds: data.fileId}
			}, function (res) {
				if (res.success) {
					_that.getFileListWin(matData.matinstId, matData);
					_that.$message({
						message: '删除成功',
						type: 'success'
					});
				} else {
					_that.$message({
						message: res.message ? res.message : '删除失败',
						type: 'error'
					});
				}
			}, function (msg) {
				_that.$message({
					message: '服务请求失败',
					type: 'error'
				});
			});
		},
		showMatFiles: function (matId, docType) {  // bsc/att/listAttLinkAndDetailNoPage.do
			var _that = this;
			// var tempwindow = window.open('_blank');
			request('', {
				url: ctx + 'bsc/att/listAttLinkAndDetailNoPage.do',
				type: 'post',
				data: {
					tableName: 'AEA_ITEM_MAT',
					pkName: docType,
					recordId: matId,
					attType: null,
				},
			}, function (result) {
				if (result != null && result.length > 1) {

					var trHtml = '';
					_that.showMatFilesDialogShow = true;
					$.each(result, function (i, file) {
						trHtml += '<div class="td-cust clearfix" data-type="file" data-key="' + file.detailId + '"  data-format="' + file.attFormat + '">\n' +
								'                    <div class="file-image fl"><img src="' + _that.getFileIcon(file.attName) + '" /></div>\n' +
								'                    <div class="file-name fl">\n' +
								'                        <a href="' + _that.kpFileOpenEvent(file.detailId) + '" target="_blank" title="' + file.attName + '">' + _that.cutString(file.attName, 50) + '</a>\n' +
								'                    </div>\n' +
								'                </div>';
					});
					_that.dialogHtml = trHtml;
				} else if (result.length == 1) {
					var url = ctx + 'file/showFile.do?detailId=' + result[0].detailId;
					window.open.location = ctx + 'file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url);
				} else {
					_that.showMatFilesDialogShow = true;
					_that.dialogHtml = '无模板';
				}
			}, function (msg) {
				_that.$message({
					message: '服务请求失败',
					type: 'error'
				});
			});
		},
		// 材料全选
		checkAllMatChange: function (val, flag) {
			if (val == false) {
				val = '';
			}
			var _that = this;
			// this.checkedCities[[index]] = val ? this.id[[index]] : []
			_that.model.matsTableData.map(function (item) {
				if (flag == 'paper') {
					item.getPaper = val;
					_that.getPaperAll = val;
				} else {
					item.getCopy = val;
					_that.getCopyAll = val;
				}
			});
			// this.isIndeterminate[index] = false
		},
		// 材料单选
		checkedMatChange: function (val, index, flag) {
			var _that = this;
			if (val == false) {
				val = '';
			}
			if (flag == 'paper') {
				_that.model.matsTableData[index].getPaper = val;
				_that.getPaperAll = val;
			} else {
				_that.model.matsTableData[index].getCopy = val;
				_that.getCopyAll = val;
			}
		},
		showUploadWindow: function (data) { // 展示文件上传下载弹窗
			var _that = this;
			_that.showUploadWindowFlag = true;
			_that.selMatRowData = data;
			_that.selMatinstId = data.matinstId ? data.matinstId : '',
					_that.showUploadWindowTitle = '材料附件 - ' + data.matName
			_that.getFileListWin(data.matinstId, data);
		},
		// 获取并行情形列表id
		getCoreItemsStatusListId: function () {
			var _that = this;
			var stateListLen = _that.stateList.length;
			var selStateIds = [];
			for (var i = 0; i < stateListLen; i++) {  // 已选并联情形id集合
				if (_that.stateList[i].selectAnswerId !== undefined || _that.stateList[i].selectAnswerId !== null) {
					// selStateIds=[];
					// return true;
					if (typeof (_that.stateList[i].selectAnswerId) == 'object' && _that.stateList[i].selectAnswerId.length > 0) {
						selStateIds = selStateIds.concat(_that.stateList[i].selectAnswerId);
					} else if (_that.stateList[i].selectAnswerId !== '') {
						selStateIds.push(_that.stateList[i].selectAnswerId);
					}
				}
			}
			selStateIds = selStateIds.filter(function (item, index) {
				//当前元素，在原始数组中的第一个索引==当前索引值，否则返回当前元素
				return selStateIds.indexOf(item, 0) === index
			})
			return selStateIds;
		},
		// 获取事项情形和材料
		getStatusStateMats: function (agentItemVerId) {
			var _that = this;

			request('opus-admin', {
				url: ctx + 'supermarket/purchase/mat/getItemMatList',
				type: 'get',
				data: {
					itemVerId: agentItemVerId
				}
			}, function (data) {
				if (data.success) {
					_that.matIds = [];
					_that.model.matsTableData = _that.unique(data.content, 'mats');
					// _that.model.matsTableData.map(function (item, index) {
					// 	_that.matIds.push(item.matId);
					// 	if (_that.matCodes.indexOf(item.matCode) < 0) {
					// 		_that.matCodes.push(item.matCode);
					// 	}
					// });

					var matinstIdsObj = [];
					_that.model.matsTableData.map(function (item) {
						if (item.matChild == 'undefined' || item.matChild == undefined) {
							Vue.set(item, 'matChild', []);
						}
						if (item.certChild == 'undefined' || item.certChild == undefined) {
							Vue.set(item, 'certChild', []);
						}
						if (item.matinstId == 'undefined' || item.matinstId == undefined) {
							Vue.set(item, 'matinstId', '');
						}
						if (item.getPaper == 'undefined' || item.getPaper == undefined) {
							Vue.set(item, 'getPaper', '');
						}
						if (item.getCopy == 'undefined' || item.getCopy == undefined) {
							Vue.set(item, 'getCopy', '');
						}
						if (item.realPaperCount == 'undefined' || item.realPaperCount == undefined) {
							Vue.set(item, 'realPaperCount', item.duePaperCount);
						}
						if (item.realCopyCount == 'undefined' || item.realCopyCount == undefined) {
							Vue.set(item, 'realCopyCount', item.dueCopyCount);
						}
						if (matinstIdsObj.indexOf(item.matinstId) && item.matinstId != '') {
							matinstIdsObj.push(item.matinstId);
						}
						// if (_that.matCodes.indexOf(item.matCode) < 0) {
						// 	_that.matCodes.push(item.matCode);
						// }
						_that.matIds.push(item.matId);

					});
					_that.matinstIds = matinstIdsObj.join(',')
					_that.ifMatsSelAll();
				} else {
					_that.$message({
						message: '获取情形材料失败',
						type: 'error'
					});
				}
			}, function (msg) {
				_that.$message({
					message: '获取情形材料失败',
					type: 'error'
				});
			});
		},
		// 判断是否材料全选
		ifMatsSelAll: function () {
			var getCopyCount = 0;
			var _that = this;
			var getPaperCount = 0;
			_that.model.matsTableData.map(function (item) {
				if (item.getCopy == true) {
					getCopyCount++
				}
				if (item.getPaper == true) {
					getPaperCount++
				}
			})
			if (getCopyCount == _that.model.matsTableData.length) {
				_that.getCopyAll = true;
			} else {
				_that.getCopyAll = '';
			}
			if (getPaperCount == _that.model.matsTableData.length) {
				_that.getPaperAll = true;
			} else {
				_that.getPaperAll = '';
			}
		},
		// 文件上传弹窗页面-下载电子件
		downloadFile: function () {
			var _that = this;
			var detailIds = [];
			if (_that.fileSelectionList.length == 0) {
				_that.$message({
					message: '请勾选数据后操作！',
					type: 'error'
				});
				return false;
			}
			_that.fileSelectionList.map(function (item, index) {
				detailIds.push(item.fileId);
			});
			detailIds = detailIds.join(',');
			window.open(ctx + 'supermarket/purchase/mat/att/download?detailIds=' + detailIds, '_blank')
		},
		// 文件上传弹窗页面-删除电子件
		delSelectFileCom: function () {
			var _that = this;
			var detailIds = [];
			if (_that.fileSelectionList.length == 0) {
				_that.$message({
					message: '请勾选数据后操作！',
					type: 'error'
				});
				return false;
			}
			_that.fileSelectionList.map(function (item, index) {
				detailIds.push(item.fileId);
			});
			detailIds = detailIds.join(',');
			var url = ctx + 'supermarket/purchase/mat/att/batch/delete';
			request('', {
				url: url,
				type: 'get',
				data: {matinstId: _that.selMatinstId, detailIds: detailIds}
			}, function (res) {
				if (res.success) {
					_that.getFileListWin(res.content, _that.selMatRowData);
					_that.$message({
						message: '删除成功',
						type: 'success'
					});
				} else {
					_that.$message({
						message: res.message ? res.message : '删除失败',
						type: 'error'
					});
				}
			}, function (msg) {
				_that.$message({
					message: '服务请求失败',
					type: 'error'
				});
			});
		},
		debounceHandler: function (file) {
			this.loadingFileWin = true;
			this.debounce(this.uploadFileCom, file);
		},
		debounce: function (func, file) {
			var vm = this;
			window.clearTimeout(func.tId);
			func.temArr = func.temArr || [];
			func.temArr.push(file);
			console.log(file);
			func.tId = window.setTimeout(function () {
				this.loadingFileWin = false;
				func(func.temArr);
				func.temArr = [];
			}, 1000);
		},
		// 文件上传弹窗页面-上传电子件
		uploadFileCom: function (file) {
			var _that = this;
			var rowData = _that.selMatRowData;
			this.fileWinData = new FormData();
			file.forEach(function (u) {
				Vue.set(u.file, 'fileName', u.file.name);
				_that.fileWinData.append('file', u.file);
			})
			// Vue.set(file.file,'fileName',file.file.name);
			// this.fileWinData.append('file', file.file);
			this.fileWinData.append("matId", rowData.matId);
			this.fileWinData.append("matinstCode", rowData.matCode);
			this.fileWinData.append("matinstName", rowData.matName ? rowData.matName : '');
			this.fileWinData.append("projInfoId", _that.projInfoId);
			this.fileWinData.append("unitInfoId", _that.rootUnitInfoId);
			this.fileWinData.append("matinstId", rowData.matinstId ? rowData.matinstId : '');
			this.fileWinData.append("matProp", rowData.matProp ? rowData.matProp : '');
			this.fileWinData.append("certId", rowData.certId ? rowData.certId : '');
			this.fileWinData.append("stoFormId", rowData.stoFormId ? rowData.stoFormId : '');
			_that.loadingFileWin = true;
			axios.post(ctx + 'supermarket/purchase/mat/att/upload', _that.fileWinData).then(function (res) {
				file.forEach(function (u) {
					Vue.set(u.file, 'matinstId', res.data.content);
				})
				// Vue.set(file.file,'matinstId',res.data.content)
				_that.selMatinstId = res.data.content;
				_that.getFileListWin(res.data.content, rowData);
				var matinstIdsObj = [];
				_that.model.matsTableData.map(function (item) {
					if (item) {
						if (item.matId == rowData.matId) {
							item.matinstId = res.data.content;
							_that.showFileListKey.push(item.matId)
						}
						if (matinstIdsObj.indexOf(item.matinstId) < 0 && item.matinstId != '') {
							matinstIdsObj.push(item.matinstId);
						}
						;
					}
				});
				_that.matinstIds = matinstIdsObj.join(',');
				_that.loadingFileWin = false;
				_that.$message({
					message: '上传成功',
					type: 'success'
				});

			}).catch(function (error) {
				_that.loadingFileWin = false;
				if (error.response) {
					_that.$message({
						message: '文件上传失败(' + error.response.status + ')，' + error.response.data,
						type: 'error'
					});
				} else if (error.request) {
					_that.$message({
						message: '文件上传失败，服务器端无响应',
						type: 'error'
					});
				} else {
					_that.$message({
						message: '文件上传失败，请求封装失败',
						type: 'error'
					});
				}

			});
		},
		// 预览电子件
		filePreview: function (data, flag) { // flag==pdf 查看施工图
			var detailId = data.fileId;
			var _that = this;
			var regText = /doc|docx|pdf|ppt|pptx|xls|xlsx|txt$/;
			var fileName = data.fileName;
			var fileType = this.getFileType(fileName);
			var flashAttributes = '';
			_that.filePreviewCount++
			if (flag == 'pdf'||flag == 'PDF') {
				var tempwindow = window.open(); // 先打开页面
				setTimeout(function () {
					tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + detailId;
				}, 1000)
			} else {
				if (fileType == 'pdf'||flag == 'PDF') {
					var tempwindow = window.open(); // 先打开页面
					setTimeout(function () {
						tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
					}, 1000)
				} else if (regText.test(fileType)) {
					// previewPdf/pdfIsCoverted
					_that.mloading = true;
					request('', {
						url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
						type: 'get',
					}, function (result) {
						if (result.success) {
							_that.mloading = false;
							var tempwindow = window.open(); // 先打开页面
							setTimeout(function () {
								tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
							}, 1000)
						} else {
							if (_that.filePreviewCount > 9) {
								confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
									_that.filePreviewCount = 0;
									_that.filePreview(data);
								}, function () {
									_that.filePreviewCount = 0;
									_that.mloading = false;
									return false;
								}, '确定', '取消', 'warning', true)
							} else {
								setTimeout(function () {
									_that.filePreview(data);
								}, 1000)
							}
						}
					}, function (msg) {
						_that.mloading = false;
						_that.$message({
							message: '文件预览失败',
							type: 'error'
						});
					})
				} else {
					_that.mloading = false;
					var tempwindow = window.open(); // 先打开页面
					setTimeout(function () {
						tempwindow.location = ctx + 'supermarket/purchase/mat/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
					}, 1000)
				}
			}
		},
		// 预览源文件
		filePreview1: function (data) {
			var detailId = data.fileId;
			var flashAttributes = '';
			var tempwindow = window.open(); // 先打开页面
			setTimeout(function () {
				tempwindow.location = ctx + 'supermarket/purchase/mat/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
			}, 1000)
		},
		// 勾选电子件
		selectionFileChange: function (val) {
			this.fileSelectionList = val;
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
			return this.form.biddingType != '3' ? 'isDefineAmount' : '';
		},
		isDiscloseImchange: function () {
			return this.form.biddingType == '1' ? 'isDiscloseIm' : '';
		},
		expirationDatechange: function () {
			return this.form.biddingType == '2' ? 'expirationDate' : '';
		},
		agentUnitNamechange: function () {
			return this.form.biddingType == '2' ? 'agentUnitName' : '';
		},
		basePricechange: function () {
			return this.form.isDefineAmount == '1' ? 'basePrice' : '';
		},
		highestPricechange: function () {
			console.log(123)
			return this.form.biddingType == '3' ? 'highestPrice' : '';
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
