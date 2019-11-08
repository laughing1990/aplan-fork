var viewer;// viewer 对象
var vm = new Vue({
	el: '#app',
	data: {
		fullHeight: document.documentElement.clientHeight,
		treeLeftHeight: document.documentElement.clientHeight - 200,
		dialogEditTable: false,
		pageSize: 10,
		pageSize2: 10,
		pageSize3: 10,
		pageSize4: 10,
		pageSize5: 10,
		pageSize6: 10,
		pageSize7: 10,
		pageSize8: 10,
		pageSize9: 10,
		currentPage: 1,
		currentPage2: 1,
		currentPage3: 1,
		currentPage4: 1,
		currentPage5: 1,
		currentPage6: 1,
		currentPage7: 1,
		currentPage8: 1,
		currentPage9: 1,
		page: 1,
		page2: 1,
		page3: 1,
		page4: 1,
		page5: 1,
		page6: 1,
		page7: 1,
		page8: 1,
		page9: 1,
		multipleSelection: [],
		total: 0,
		total2: 0,
		total3: 0,
		total4: 0,
		total5: 0,
		total6: 0,
		total7: 0,
		total8: 0,
		total9: 0,
		pagination: true,
		pagination2: true,
		pagination3: true,
		pagination4: true,
		pagination5: true,
		pagination6: true,
		pagination7: true,
		pagination8: true,
		silderTitle: '新增',
		formData: [],
		rules: {},
		loading: false,
		navItemObj: ["中介机构", "中介详情"],
		keyword: '',
		tab: 1,
		isUp: false,
		activeName4: 'tabs-first',
		title: '',
		dialogTitle: '',
		options: [],
		tableData2: [],
		cardItem2: [{
			compEvaluation: null,
			serviceTypeName: "工程造价咨询",
			img: '../../../static/zjcs/serviceOrgan/gczx_ico.png'
		}],
		cardItem: [],
		fileList: [],
		option1: [],
		option: [{
			label: '4~5星',
			value: 3
		}, {
			label: '3~4星',
			value: 2
		}, {
			label: '0~3星',
			value: 1
		}, {
			label: '暂无评价',
			value: 0
		}],
		items: [],
		form: {},
		checkboxGroup: [],
		checkboxs: [],
		checkBoxItem: [],
		curTab: '0',
		organDetail: {},
		personName: '',
		personName2: '',
		personName3: '',
		tableData3: [{
			personName: "贾岚",
			hasCertifcate: '城乡规划高级工程师、注册城市规划师',
			serviceType: '城乡规划编制'
		},],
		activeName2: 'infoTabs1',
		activeName3: 'tabs-first',
		addCard: '中山六路2号1501自编1504室',
		zipCode: '无',
		personCard: '吴剑平',
		eMailCard: 'Kj577@126.com',
		telePhone: '020-83266642',
		infoBox: [],
		tableData4: [],
		assessType: '',
		assessLevel: 0,
		assessStar: '',
		tableData5: [{
			name: '土地评估',
			organ: '广东省住房和城乡建设厅',
			getDate: '2018-10-08',
			validity: '2020-11-14'
		}],
		tableData6: [],
		tableData7: [],
		tableData8: [],
		treeData: [],
		isScroll: false,
		activeName: 1,
		filterText1: '',
		ctx: ctx,
		unitInfoId: '',
		publishTimeOrderSort: '',
		startOrderSort: '',
		biddingNumOrderSort: '',
		curItem: {},
		serviceSelect: [],
		noticeInfo: {
			name: 'hiuahuiah'
		},
	},
	created: function () {
		this.showTable();
		this.showAuditFlag();
		this.getURLArgs();
		this.getAllService();
		console.log(localStorage.getItem("access_token"));
	},
	mounted: function () {
		var that = this;
		window.addEventListener("resize", function () {
			return (function () {
				window.fullHeight = document.documentElement.clientHeight
				that.fullHeight = window.fullHeight;
				that.treeLeftHeight = that.fullHeight - 100;
			})();
		});
	},
	methods: {
		checkOrg: function () {
			if (this.curTab == 0) {
				this.showTable();
			} else if (this.curTab == 1) {
				this.showService();
			} else if (this.curTab == 2) {
				this.showAuditFlag();
			}
		},
		// 入驻机构
		showTable: function () {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/listCheckinUnit', type: 'get',
				data: {
					pageNum: this.currentPage,
					pageSize: this.pageSize,
					applicant: this.form.applicant,
					compEvaluation: this.form.compEvaluation,
					serviceId: this.checkboxGroup.length > 0 ? JSON.stringify(this.checkboxGroup) : JSON.stringify([]),
					publishTimeOrderSort: this.publishTimeOrderSort,
					startOrderSort: this.startOrderSort
				}
			}, function (data) {
				_this.options = data.content.rows;
				for(var i=0;i<_this.options.length;i++){
					var summaries=_this.options[i].breakFaithRecord.summaries;
					var summarieNum=0;
					for(var j=0;j<summaries.length;j++){
						summarieNum=summarieNum+summaries[j].detailDtos.length;
					}
					_this.options[i].breakFaithRecordNum=summarieNum;
				}
				_this.total = data.content.total;
				if (data.content.total > 10) {
					_this.pagination = true;
				} else {
					_this.pagination = false;
				}
				_this.loading = false;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		// 入驻公示
		showAuditFlag: function () {
			var _this = this;
			request('', {
				url: ctx + 'supermarket/agentservice/listCheckinUnit', type: 'get',
				data: {
					pageNum: this.currentPage2,
					pageSize: this.pageSize2,
					// auditFlag : 2,
					applicant: this.form.applicant,
					serviceId: this.checkboxGroup.length > 0 ? JSON.stringify(this.checkboxGroup) : JSON.stringify([]),
					publishTimeOrderSort: this.publishTimeOrderSort,
					startOrderSort: this.startOrderSort
				}
			}, function (data) {
				_this.tableData2 = data.content.rows;
				_this.total2 = data.content.total;
				if (data.content.total > 10) {
					_this.pagination3 = true;
				} else {
					_this.pagination3 = false;
				}
				_this.loading = false;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		//入驻服务
		showService: function () {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/listCheckinService', type: 'get',
				data: {
					pageNum: this.currentPage3 || this.currentPage6,
					pageSize: this.pageSize3 || this.pageSize6,
					applicant: this.form.applicant,
					serviceId: this.checkboxGroup.length > 0 ? JSON.stringify(this.checkboxGroup) : JSON.stringify([]),
					compEvaluation: this.form.compEvaluation,
					publishTimeOrderSort: this.publishTimeOrderSort,
					startOrderSort: this.startOrderSort,
					unitInfoId: this.unitInfoId
				}
			}, function (data) {
				_this.cardItem = data.content.rows;
				_this.cardItem2 = data.content.rows;
				_this.total3 = data.content.total;
				var obj = {
					serviceId: '',
					serviceName: '请选择'
				}
				_this.serviceSelect.push(obj);
				for (var i = 0; i < data.content.rows.length; i++) {
					_this.serviceSelect.push(data.content.rows[i])
				}
				if (data.content.total > 10) {
					_this.pagination2 = true;
					_this.pagination4 = true;
				} else {
					_this.pagination2 = false;
					_this.pagination4 = false;
				}
				_this.loading = false;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		//入驻机构详情
		showOrganDetail: function (item) {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/getAgentUnitDetail', type: 'get',
				data: {
					unitInfoId: item.unitInfoId
				}
			}, function (data) {
				_this.organDetail = data.content;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		//所有资格信息
		showlistAgentCertinst: function (item) {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/listAgentCertinst', type: 'get',
				data: {
					pageNum: this.currentPage,
					pageSize: this.pageSize,
					unitInfoId: item.unitInfoId
				}
			}, function (data) {
				_this.tableData5 = data.content;
				_this.tableData8 = data.content;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		//中选记录
		showlistWinBidService: function (item) {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/listWinBidService', type: 'get',
				data: {
					pageNum: this.currentPage4,
					pageSize: this.pageSize4,
					unitInfoId: item.unitInfoId,
					projName: this.filterText1,
					serviceId: this.assessType
				}
			}, function (data) {
				_this.tableData4 = data.content.biddingList.rows;
				_this.total4 = data.content.biddingList.total;
				_this.assessLevel = Number(data.content.compEvaluation);
				if (data.content.biddingList.total > 10) {
					_this.pagination5 = true;
				} else {
					_this.pagination5 = false;
				}
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		//联系信息
		showlistAgentHeadLinkman: function (item) {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/listAgentHeadLinkman', type: 'get',
				data: {
					unitInfoId: item.unitInfoId
				}
			}, function (data) {
				_this.infoBox = data.content;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		//执业人员信息
		showlistAgentLinkmanCertinst: function (item) {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/listAgentLinkmanCertinst', type: 'get',
				data: {
					pageNum: this.currentPage5,
					pageSize: this.pageSize5,
					unitInfoId: item.unitInfoId,
					linkmanName: this.personName
				}
			}, function (data) {
				_this.tableData3 = data.content.rows;
				_this.total5 = data.content.total;
				if (data.content.total > 10) {
					_this.pagination6 = true;
				} else {
					_this.pagination6 = false;
				}
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		// 点击机构查询详细信息
		detailClick: function (item) {
			this.curItem = item;
			this.unitInfoId = item.unitInfoId;
			this.showService();
			this.showOrganDetail(item);
			this.showLinkman(item);
			this.showlistAgentCertinst(item);
			this.showlistWinBidService(item);
			this.showlistAgentHeadLinkman(item);
			this.showlistAgentLinkmanCertinst(item);
		},
		//查询单位人员资格证书
		showLinkman: function (item) {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'supermarket/agentservice/listAgentLinkmanCertinst', type: 'get',
				data: {
					pageNum: this.currentPage,
					pageSize: this.pageSize,
					unitInfoId: item.unitInfoId
				}
			}, function (data) {
				_this.tableData3 = data.content.rows;
			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		// 服务类型
		getAllService: function () {
			var _that = this;
			request('', {
				url: ctx + 'supermarket/purchase/getAllService',
				type: 'post',
			}, function (data) {
				if (data.content) {
					_that.checkboxs = data.content;
				}
			}, function (msg) {
				alertMsg('', '服务请求失败', '关闭', 'error', true);
			});
		},
		timeSort: function (e, val) {
			if ($(e.target).hasClass('active')) return;
			$(e.target).addClass('active').siblings().removeClass('active');
			this.publishTimeOrderSort = val;
			if (this.curTab == 0) {
				this.showTable();
			} else if (this.curTab == 1) {
				this.showService();
			} else {
				this.showAuditFlag();
			}
		},
		assessSort: function (e, val) {
			if ($(e.target).hasClass('active')) return;
			$(e.target).addClass('active').siblings().removeClass('active');
			this.startOrderSort = val;
			this.showTable();
			if (this.curTab == 0) {
				this.showTable();
			} else if (this.curTab == 1) {
				this.showService();
			} else {
				this.showAuditFlag();
			}
		},
		isObject: function (obj) {
			for (var key in obj) {
				return false;
			}
			return true;
		},
		getURLArgs: function () {
			//解析地址栏中的中文字符
			var search = decodeURI(window.location.search);
			// console.log('search:' + search)
			if (!search) return
			var params = {}
			search = search.substring(1, search.length);
			var group = search.split('&')
			for (var i = 0; i < group.length; i++) {
				var entry = group[i].split('=')
				params[entry[0]] = entry[1]
			}
			// console.log(params);
			var obj = this.isObject(params);
			if (obj) {
				this.showService();
				return;
			}
			var tabList = $('.tabs__item');
			params.tab = parseInt(params.tab)

			if (!tabList.eq(params.tab).hasClass('active')) {
				tabList.eq(params.tab).addClass('active').siblings().removeClass('active');
				this.curTab = params.tab;
				if (params.serviceId) {
					this.getAllService();
					this.checkboxGroup.push(params.serviceId);
					this.showService();

				}
				if (params.unitInfoId) {
					this.unitInfoId = params.unitInfoId;
					this.tab = params.tab;
					this.showService();
					this.showOrganDetail(params);
					this.showLinkman(params);
					this.showlistAgentCertinst(params);
					this.showlistWinBidService(params);
					this.showlistAgentHeadLinkman(params);
					this.showlistAgentLinkmanCertinst(params);
				}
				// 从其他页面进来
				if ('seachKey' in params) {
					var ts = this;
					this.$set(ts.form, 'applicant', params.seachKey);
					if (params.tab == "0") {
						this.showTable();
					} else {
						this.showService();
					}
				} else {
					if (params.tab == "0") {
						this.showTable();
					} else {
						this.showService();
					}
				}

			} else {
				if (params.seachKey) {
					var ts = this;
					this.$set(ts.form, 'applicant', params.seachKey);
					if (params.tab == "0") {
						this.showTable();
					} else {
						this.showService();
					}
				}
			}
		},
		// 跳转证照页面
		switchPageBook: function (item) {
			// this.showMajorTree();
			// window.open("../../../templates/zjcs/serviceOrgan/index3.html?id="+ id,"_blank");
			window.open("/aplanmis-mall/supermarket/main/qualCertInfo.html?certinstId=" + item.certinstId, "_blank");
		},
		backToOne: function () {
			var list = $('.tabs li');
			$(list).eq(this.curTab - 1).addClass('active').siblings().removeClass('active')
		},
		tabChange1: function (e, num) {
			if (e.target.localName == "a") {
				var target = $(e.target.parentNode);
			} else {
				var target = $(e.target);
			}
			if (target.hasClass('active')) return;
			this.form.type = "";
			this.checkboxGroup = [];
			this.checkBoxItem = [];
			this.form.applicant = '';
			this.form.compEvaluation = "";
			this.curTab = $(e.target.parentNode).index();
			target.addClass('active').siblings().removeClass('active');
			if (num == 0) {
				this.showTable();
			} else if (num == 1) {
				this.showService();
			} else {
				this.showAuditFlag();
			}
		},
		// 部门选择事件
		chooseDm: function (e) {
			var _this = this;
			if ($(e.target).hasClass('active')) {
				$(e.target).removeClass('active')
			} else {
				$(e.target).addClass('active');
			}
		},
		cutStr: function (val) {
			if (!val) return;
			val = val.substring(0, 3) + '******' + val.substr(val.length - 2, 2);
			return val;
		},
		cellClick: function (row, column, cell, event) {
			if (column.label == "中介服务机构名称") {
				this.title = row.name;
				this.tab = false;
			}
		},
		handleClick: function () {

		},
		toggle: function (e) {
			// e.cancelBubble = true;
			// var item = $(".slide-control");
			// if (item.hasClass('control-up')) {
			// 	this.isUp = false;
			// 	$(".checkgroup").addClass('special');
			// 	item.removeClass('control-up').addClass('control-down');
			// } else {
			// 	this.isUp = true;
			// 	$(".checkgroup").removeClass('special');
			// 	item.removeClass('control-down').addClass('control-up');
			// }
			this.isUp = !this.isUp;
		},
		// 解决sleect选不中值问题
		forceUpdate: function () {
			this.$forceUpdate()
		},
		breadNavClick: function (index) {
			if (index == 0) {
				this.tab = true;
			}
		},
		// 跳转二级页面
		switchPage: function (item) {
			// window.open("../../../templates/zjcs/serviceOrgan/index2.html?serviceId="+ item.serviceId+'?unitInfoId='+item.unitInfoId+'?unitServiceId='+item.unitServiceId,"_blank");
			window.open("/aplanmis-mall/supermarket/main/serviceInfo.html?serviceId=" + item.serviceId + '?unitInfoId=' + item.unitInfoId + '?unitServiceId=' + item.unitServiceId, "_blank");
		},

		previewRow: function () {
			// this.preViewFlag = true;
			if (viewer) viewer.destroy();//当存在viewer对象，先销毁
			viewer = new Viewer(document.getElementById("book"), {
				url: 'imgUrl'
			});
			viewer.show();//展示图片
		},

		// 格式化时间
		dateFormat: function (val) {
			var daterc = val;
			if (daterc != null) {
				var dateMat = new Date(parseInt(daterc));
				return formatDate(dateMat, 'yyyy.MM.dd')
			}
		},
		handleSizeChange: function (val) {
			this.pageSize = val;
			this.showTable();
		},
		handleCurrentChange: function (val) {
			this.currentPage = val;
			this.showTable();
		},
		handleSizeChange2: function (val) {
			this.pageSize2 = val;
			this.showAuditFlag();
		},
		handleCurrentChange2: function (val) {
			this.currentPage2 = val;
			this.showAuditFlag();
		},
		handleSizeChange3: function (val) {
			this.pageSize3 = val;
			this.showService();
		},
		handleCurrentChange3: function (val) {
			this.currentPage3 = val;
			this.showService();
		},
		handleSizeChange4: function (val) {
			this.pageSize4 = val;
			this.showlistWinBidService(this.curItem);
		},
		handleCurrentChange4: function (val) {
			this.currentPage4 = val;
			this.showlistWinBidService(this.curItem);
		},
		handleSizeChange5: function (val) {
			this.pageSize5 = val;
			this.showlistAgentLinkmanCertinst(this.curItem);
		},
		handleCurrentChange5: function (val) {
			this.currentPage5 = val;
			this.showlistAgentLinkmanCertinst(this.curItem);
		},
		handleSizeChange6: function (val) {
			this.pageSize6 = val;
			this.showService();
		},
		handleCurrentChange6: function (val) {
			this.currentPage6 = val;
			this.showService();
		},
		handleSizeChange7: function (val) {
			this.pageSize7 = val;

		},
		handleCurrentChange7: function (val) {
			this.currentPage7 = val;

		},
		handleSizeChange8: function (val) {
			this.pageSize8 = val;

		},
		handleCurrentChange8: function (val) {
			this.currentPage8 = val;

		},
		handleSizeChange9: function (val) {
			this.pageSize9 = val;

		},
		handleCurrentChange9: function (val) {
			this.currentPage9 = val;

		},
		handleSelectionChange: function (val) {
			this.multipleSelection = val;
		},
		// 中选记录跳转采购公告详情
		jumpToPurchase: function (row) {
			var _projPurchaseId = row.projPurchaseId;
			window.location.href = "/aplanmis-mall/supermarket/main/procurementNotice/details.html?projPurchaseId=" + _projPurchaseId;
		},
	},
	computed: {
		headMsg: function () {
			return {
				"Authorization": "bearer " + localStorage.getItem("access_token")
			}
		}
	},
	watch: {
		filterText1: function (val) {
			this.filterText1 = val;
			this.showlistWinBidService(this.curItem);
		}
	}
});


