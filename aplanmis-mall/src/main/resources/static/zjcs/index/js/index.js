/*
 * @Author: ZL
 * @Date:   2019/05/30
 * @Last Modified by:   anchen
 * @Last Modified time: $ $
 */
var vm = new Vue({
	el: '#index',
	data: function () {
		return {
			localFlag: false,
			navListImgData: [],
			navListData: [],
			navListDataShow: [], // 可展示navListData列表
			navListCount: {},
			agentServer: [{
				navLabel: '服务引导',
				imgUrl: '../../../static/zjcs/index/images/fwyd_ico.png',
				goToUrl: '#',
				thImgUrl: '/aplanmis-mall/zjcs/index/images/fwyd_ico.png',
				id: '1'
			}, {
				navLabel: '服务事项',
				imgUrl: '../../static/zjcs/index/images/zjjg_ico.png',
				goToUrl: '/aplanmis-mall/supermarket/main/imServices.html',
				thImgUrl: '/aplanmis-mall/zjcs/index/images/zjjg_ico.png',
				id: '2'
			}, {
				navLabel: '中介机构',
				imgUrl: '../../static/zjcs/index/images/zjlx_ico.png',
				goToUrl: '/aplanmis-mall/supermarket/main/imUnits.html?tab=0',
				thImgUrl: '/aplanmis-mall/zjcs/index/images/zjlx_ico.png',
				id: '3'
			}, {
				navLabel: '中介服务',
				imgUrl: '../../static/zjcs/index/images/zjfw_ico.png',
				goToUrl: '/aplanmis-mall/supermarket/main/imUnits.html?tab=1',
				thImgUrl: '/aplanmis-mall/zjcs/index/images/zjfw_ico.png',
				id: '4'
			}, {
				navLabel: '中介竞价',
				imgUrl: '../../static/zjcs/index/images/zjjj_ico.png',
				goToUrl: '/aplanmis-mall/supermarket/main/agentCenter.html#projectSignup',
				thImgUrl: '/aplanmis-mall/zjcs/index/images/zjjj_ico.png',
				id: '5'
			}, {
				navLabel: '合同范本',
				imgUrl: '../../static/zjcs/index/images/htfb_ico.png',
				goToUrl: '/aplanmis-mall/supermarket/main/contactTemplate.html',
				thImgUrl: '/aplanmis-mall/zjcs/index/images/htfb_ico.png',
				id: '6'
			}],
			noticeList: [{
				notice: '关于印发《关于建立唐山市行政审批中介服务超市的工作方案》的通知',
				url: '/aplanmis-mall/supermarket/main/notice.html?id=0',
				time: '2018-8-20'
			},],
			procurementList: [],
			selectedList: [],
			contractList: [],
			creditList: [{
				imgUrl: '/aplanmis-mall/zjcs/index/images/chop_01.png',
				url: '#'
			}, {
				imgUrl: '/aplanmis-mall/zjcs/index/images/chop_02.png',
				url: '#'
			}, {
				imgUrl: '/aplanmis-mall/zjcs/index/images/chop_03.png',
				url: '#'
			}, {
				imgUrl: '/aplanmis-mall/zjcs/index/images/chop_04.png',
				url: '#'
			}, {
				imgUrl: '/aplanmis-mall/zjcs/index/images/chop_05.png',
				url: '#'
			}, {
				imgUrl: '/aplanmis-mall/zjcs/index/images/chop_06.png',
				url: '#'
			}, {
				imgUrl: '/aplanmis-mall/zjcs/index/images/chop_07.png',
				url: '#'
			}],
			othersLinkList: [{
				label: '—— 各级政府 ——',
				id: '1',
				value: '',
				options: [{
					name: '中华人民共和国中央人民政府门户网站',
					id: '1-1',
					value: '1',
					url: 'http://www.gov.cn/'
				}, {
					name: '辽宁人民政府',
					id: '1-2',
					value: '2',
					url: 'http://www.ln.gov.cn/'
				}, {
					name: '沈阳市人民政府',
					id: '1-3',
					value: '3',
					url: 'http://www.shenyang.gov.cn/'
				}]
			}, {
				label: '—— 媒体网站 ——',
				id: '2',
				value: '',
				options: [{
					name: '沈阳网',
					id: '2-1',
					value: '4',
					url: 'http://www.syd.com.cn/'
				}, {
					name: '新华网',
					id: '2-2',
					value: '5',
					url: 'http://www.xinhuanet.com/'
				}, {
					name: '人民网',
					id: '2-3',
					value: '6',
					url: 'http://www.people.com.cn/'
				}, {
					name: '新浪网',
					id: '2-3',
					value: '7',
					url: 'http://www.sina.com.cn/'
				}]
			}, {
				label: '—— 友情链接 ——',
				id: '3',
				value: '',
				options: [{
					name: '沈阳政务服务中心',
					id: '3-1',
					value: '8',
					url: 'http://www.sysp.gov.cn/'
				}]
			}],
			showLenIndex: 0,
			activeName: 0,
			isScroll: false,
			ctx: ctx,
		}
	},
	created: function () {
		this.init();
		this.getIndexData();
		this.getPageAoaNoticeContentCascade();
	},
	mounted: function () {

	},
	methods: {
		init: function () {
			this.navListImgData = navListImgData
		},
		// 获取首页头部数据
		getIndexData: function () {
			var vm = this
			request('', {
				url: ctx + 'supermarket/main/getIndexData', type: 'get',
			}, function (res) {
				if (res.success) {
					var content = res.content;
					vm.navListData = content.imServices;
					vm.navListCount = content;
					// debugger
					vm.getIndexDataShow(vm.showLenIndex);
					vm.initPurchaseData(res.content); //处理采购公告接口
					vm.initSelectionData(res.content); //处理中选公告接口
					vm.contractList = res.content.purchaseContracts;
				}
			}, function (msg) {
				vm.$message({
					message: '加载失败',
					type: 'error'
				});
			});
		},
		//获取公告列表
		getPageAoaNoticeContentCascade: function () {
			var _this = this;
			// this.loading=true;
			request('', {
				url: ctx + 'rest/aoa/notice/content/getPageAoaNoticeContentCascade', type: 'get',
				data: {
					pageSize: 5,
					pageNum: 1,
					categoryId: '',
					businessFlag: 'supermarket'
				}
			}, function (data) {
				for (var i = 0; i < data.content.rows.length; i++) {
					data.content.rows[i].url = '/aplanmis-mall/supermarket/main/notice.html?id=' + data.content.rows[i].contentId;
					// data.content.rows[i].url = '/Augurit/construction-project/aplanmis-mall/src/main/resources/templates/zjcs/notice/index.html?id='+data.content.rows[i].contentId;
				}
				_this.noticeList = data.content.rows;

			}, function (msg) {
				_this.$message({
					message: '加载失败',
					type: 'error'
				});
				_this.loading = false;
			});
		},
		moneyFilter: function (val) {  // 金额格式化
			val = val.toString().replace(/\$|\,/g, '');
			if (isNaN(val)) {
				val = "0";
			}
			var sign = (val == (val = Math.abs(val)));
			val = Math.floor(val * 100 + 0.50000000001);
			var cents = val % 100;
			val = Math.floor(val / 100).toString();
			if (cents < 10) {
				cents = "0" + cents
			}
			for (var i = 0; i < Math.floor((val.length - (1 + i)) / 3); i++) {
				val = val.substring(0, val.length - (4 * i + 3)) + ',' + val.substring(val.length - (4 * i + 3));
			}
			return (((sign) ? '' : '-') + val + '.' + cents);
		},
		changeLinkUrl: function (url) {
			window.open(url);
		},
		getIndexDataShow: function (len) {  // 头部可展示长度
			this.navListDataShow = this.navListData.slice(len, len + 10);
		},
		nextNavListDataShow: function () {
			if (this.showLenIndex < (this.navListData.length - 10)) {
				this.showLenIndex = this.showLenIndex + 10;
				this.getIndexDataShow(this.showLenIndex);
			} else {
				return;
			}
		},
		prevNavListDataShow: function () {
			if (this.showLenIndex >= 10) {
				this.showLenIndex = this.showLenIndex - 10;
				this.getIndexDataShow(this.showLenIndex);
			} else {
				return;
			}
		},
		itemClick: function (item) {
			window.location.href = "/aplanmis-mall/supermarket/main/agentServiceInfo.html?serviceId=" + item.serviceId + '?serviceName=' + item.serviceName + '?imgUrl=' + item.imgUrl;
		},

		// 采购公告数据处理
		initPurchaseData: function (list) {
			var ts = this,
					_max_len = 4,    //最多展示四个
					_noenough = 0,  //不足的个数
					_noneData = {};  //补充上去的数据
			_noenough = _max_len - list.purchases.length;
			// return
			ts.procurementList = [];
			_noneData = {
				noticeTitle: '',
				url: '#',
				imgUrl: '',
				thImgUrl: '',
				deadline: '',
				price: '',
				companyName: '',
				nondata: true,  //标识为空数据，就是前端自己补充上去的数据
				allData: {}
			};
			ts.procurementList = list.purchases.map(function (item, index) {
				return {
					noticeTitle: item.projName,
					url: '#',
					imgUrl: ctx + item.purchaseImgUrl,
					thImgUrl: ctx + item.purchaseImgUrl,
					deadline: item.expirationDate,
					price: item.basePrice,
					companyName: item.applicant,
					nondata: false,
					allData: item
				}
			});
			// 如果后端数据超过4个，只显示4个,如果小于4个，剩下的前端补充数据为空的假数据
			if (_noenough < 0) {
				ts.procurementList = ts.procurementList.slice(0, _max_len);
			} else {
				for (var i = 0; i < _noenough; i++) {
					ts.procurementList.push(_noneData);
				}
			}
		},
		// 采购公告查看更多
		lookMoreProcure: function () {
			window.location.href = '/aplanmis-mall/supermarket/main/procurementNotice/index.html';
		},
		// 查看采购公告详情
		jumpToProcureDetail: function (obj) {
			var _urlId = obj.allData.projPurchaseId;
			window.location.href = "/aplanmis-mall/supermarket/main/procurementNotice/details.html?projPurchaseId=" + _urlId;
		},

		// 处理中选公告数据
		initSelectionData: function (list) {
			var ts = this,
					_max_len = 4,    //最多展示四个
					_noenough = 0,  //不足的个数
					_noneData = {};  //补充上去的数据
			_noenough = _max_len - list.purchaseSelections.length;
			// return
			ts.selectedList = [];
			_noneData = {
				noticeTitle: '',
				url: '#',
				imgUrl: '',
				thImgUrl: '',
				deadline: '',
				price: '',
				companyName: '',
				nondata: true,  //标识为空数据，就是前端自己补充上去的数据
				allData: {}
			};
			ts.selectedList = list.purchaseSelections.map(function (item, index) {
				return {
					noticeTitle: item.projName,
					url: '#',
					imgUrl: ctx + item.purchaseImgUrl,
					thImgUrl: ctx + item.purchaseImgUrl,
					deadline: item.selectedEndTime,
					price: item.basePrice,
					companyName: item.applicant,
					nondata: false,
					allData: item
				}
			})
			// console.log( ts.selectedList)
			// 如果后端数据超过4个，只显示4个,如果小于4个，剩下的前端补充数据为空的假数据
			if (_noenough < 0) {
				ts.selectedList = ts.selectedList.slice(0, _max_len);
			} else {
				for (var i = 0; i < _noenough; i++) {
					ts.selectedList.push(_noneData);
				}
			}
		},
		// 查看更多中选公告
		lookMoreSelectionNotice: function () {
			window.location.href = '/aplanmis-mall/supermarket/main/beSelectionNotice/index.html#selectionNotice';
		},
		// 查看中选公告详情
		jumpToSelectionNoticeDetail: function (obj) {
			var _urlId = obj.allData.projPurchaseId;
			window.location.href = "/aplanmis-mall/supermarket/main/beSelectionNotice/selectionNoticeDetail.html?projPurchaseId=" + _urlId;
		},

		// 查看更多合同公示
		lookMoreContractNotice: function () {
			window.location.href = '/aplanmis-mall/supermarket/main/beSelectionNotice/index.html#contractNotice';
		},
		// 查看合同公示详情
		jumpToContractNoticeDetail: function (obj) {
			window.location.href = "/aplanmis-mall/supermarket/main/beSelectionNotice/contractNoticeDetail.html?projPurchaseId=" + obj.projPurchaseId;
		},
		jumpUrl: function (index) {
			var url = '';
			debugger;
			switch (index) {
				case '0':
					url = ctx + 'supermarket/main/imUnits.html?tab=0';
					break;
				case '1':
					url = ctx + 'supermarket/main/imServices.html';
					break;
				case '2':
					url = ctx + 'supermarket/main/procurementNotice/index.html';//采购需求
					break;

			}
			window.location.href = url;
		}
	},
	filters: {
		formatDate: function (time) {
			if(time){
				var date = new Date(time);
				return formatDate(date, 'yyyy-MM-dd');
			}
			return '';
		},
		// 采购公告价格format
		procurePriceFormat: function (item) {
			if (!item) return '暂无数据'
			// 随机选取
			if (item.biddingType == 1) {
				if (item.isDefineAmount == 1) {
					return !item.basePrice ? '暂无数据' : ('￥' + item.basePrice.toLocaleString() + '元');
				}
			}
			// 直接选取
			if (item.biddingType == 2) {
				if (item.isDefineAmount == 1) {
					return !item.basePrice ? '暂无数据' : ('￥' + item.basePrice.toLocaleString() + '元');
				}
				if (item.isDefineAmount == 0) {
					return '暂不做评估与预算';
				}
			}
			// 竞价选取
			if (item.biddingType == 3) {
				/*if (item.quoteType == 0) {
					if (!item.basePrice && !item.highestPrice) {
						return '暂无数据'
					}
					return '￥' + item.basePrice.toLocaleString() + '~' + item.highestPrice.toLocaleString() + '元';
				}*/
				if (item.quoteType == 1) {
					if (!item.baseRate && !item.highestRate) {
						return '暂无数据'
					}
					return item.baseRate + '% ~ ' + item.highestRate + '%';
				} else {
					if (!item.basePrice && !item.highestPrice) {
						return '暂无数据'
					}
					return '￥' + item.basePrice.toLocaleString() + '~' + item.highestPrice.toLocaleString() + '元';
				}
			}
			return '暂无数据'
		},
	}
});
