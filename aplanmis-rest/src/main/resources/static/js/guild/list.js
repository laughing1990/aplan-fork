/*require(['jquery','vue','ELEMENT','common'], function($,Vue,ELEMENT,common) {
    Vue.use(ELEMENT);*/
var _initSearchData = {
	themeId: '',
	itemName: '',
	orgId: '',
	stageId: '',
};
var vm = new Vue({
	el: '#app',
	mixins: [mixin],
	data: function () {
		return {
			// 事项列表
			matterList: [],
			// 事项列表筛选
			matterSearchData: {
				themeId: '',
				itemName: '',
				orgId: '',
				stageId: '',
			},
			// 事项搜索关键字
			matterKeyWord: '',

			// 筛选侧滑是否展示
			filterMaskFlag: false,

			// 筛选--主题分类
			filterData: {
				themeList: [], //主题分类
				stageList: [], //主题分类下的阶段数据
				departList: [], //部门分类
			},

			// 筛选最终选中
			filterSelectItem: {},
		}
	},
	computed: {
		// 是否筛选已选中
		isSelect: function () {
			var vm = this,
					_item = {};
			_item = JSON.stringify(vm.filterSelectItem)
			if (_item === "{}") {
				return false;
			} else {
				return true;
			}
		}
	},
	created: function () {
		this.init()
	},
	mounted: function () {

	},
	methods: {
		// 显示筛选侧滑
		filterSliderStateChange: function (state) {
			state == 'show' ? this.filterMaskFlag = true : this.filterMaskFlag = false
		},

		// 获取事项列表
		fetchGuideList: function () {
			var vm = this,
					_searchData = {};
			_searchData = vm.matterSearchData;
			vm.loading = true;
			$.ajax({
				url: ctx + 'rest/guide/item/list',
				type: 'get',
				datatype: "json",
				data: _searchData,
				success: function (res) {
					vm.loading = false;
					if (res.success) {
						vm.matterList = [].concat(res.content);
						// vm._alertState(true, '获取成功')
						if (!res.content || !res.content.length) {
							vm._alertState(true, '暂无数据！');
							$('.content').find('.list-box').html('暂无数据').css('textAlign', 'center')
						}
					} else {
						vm._alertState(true, res.message);
					}
				},
				error: function (err) {
					vm.loading = false;
					vm._alertState(true, '网络错误！')
				},
			})
		},

		// 搜索关键字
		searchMatter: function () {
			if (!this.matterKeyWord.length) return
			this.matterSearchData.itemName = this.matterKeyWord;
			this.fetchGuideList();
		},

		// 搜索--键盘
		seatchFormKeyboard: function (e) {
			var vm = this,
					keycode = e.keyCode;
			if (keycode == '13') {
				e.preventDefault();
				//请求搜索接口  
				vm.searchMatter()
			}
		},

		// 筛选中获取主题跟部门
		fetchThemeAndDepart: function () {
			var vm = this;
			vm.loading = true;
			$.ajax({
				url: ctx + 'rest/guide/theme/and/org/list',
				type: 'get',
				datatype: "json",
				success: function (res) {

					vm.loading = false;
					if (res.success) {
						vm.filterData.themeList = res.content.themes.map(function (item) {
							return {
								themeId: item.themeId,
								themeName: item.themeName,
								select: false
							}
						})
						vm.filterData.departList = res.content.opuOmOrgs.map(function (item) {
							return {
								orgId: item.orgId,
								orgName: item.orgName,
								select: false
							}
						})

					} else {
						vm._alertState(true, res.message)
					}
				},
				error: function (err) {
					vm.loading = false;
					vm._alertState(true, '网络错误！')
				},
			})
		},

		// 获取当前选中主题下的阶段
		fetchStageFormTheme: function (theme) {
			if (!theme) return
			var vm = this,
					_themeData = {};
			_themeData.themeId = theme.themeId;
			vm.loading = true;
			$.ajax({
				url: ctx + 'rest/guide/stage/list',
				type: 'get',
				datatype: "json",
				data: _themeData,
				success: function (res) {
					vm.loading = false;
					if (res.success) {
						vm.filterData.stageList = res.content.map(function (item) {
							return {
								stageId: item.stageId,
								stageName: item.stageName,
								select: false
							}
						})
					} else {
						vm._alertState(true, res.message)
					}
				},
				error: function (err) {
					vm.loading = false;
					vm._alertState(true, '网络错误！')
				},
			})
		},

		// 重置操作
		resetFilter: function () {
			this.filterData.stageList = [];
			this.filterSelectItem = {};
			this.resetSelect(this.filterData.themeList)
			this.resetSelect(this.filterData.departList)
		},

		//选中重置
		resetSelect: function (list) {
			if (!list) return
			list.forEach(function (item) {
				item.select = false;
			})
		},

		// 筛选主题-选中
		themeSelect: function (obj) {
			var vm = this;
			vm.resetSelect(vm.filterData.themeList)
			obj.select = !obj.select;
			vm.resetSelect(vm.filterData.departList)
			vm.fetchStageFormTheme(obj);
		},

		// 阶段-选中
		stageSelect: function (obj) {
			var vm = this;
			vm.resetSelect(vm.filterData.stageList)
			obj.select = !obj.select;
			vm.resetSelect(vm.filterData.departList);
			vm.filterSelectItem = obj;
		},

		// 部门-选中
		departSelect: function (obj) {
			var vm = this;
			vm.resetSelect(vm.filterData.departList)
			obj.select = !obj.select;
			vm.filterData.stageList = [];
			vm.resetSelect(vm.filterData.themeList)
			vm.filterSelectItem = obj;
		},

		// 根据筛选选中，获取事项列表数据
		fetchMatterFromFilter: function () {
			var vm = this,
					_filterItem = {}
			_filterItem = vm.filterSelectItem;
			vm.matterSearchData = JSON.parse(JSON.stringify(_initSearchData))
			// 如果没有任何选中

			if (JSON.stringify(_filterItem) === "{}") {
				return vm._toastState({
					msg: '请选择分类',
				})
			}
			if (_filterItem.orgId) {
				vm.matterSearchData.orgId = _filterItem.orgId;
			}
			if (_filterItem.stageId) {
				vm.matterSearchData.stageId = _filterItem.stageId;
			}

			vm.fetchGuideList();
			vm.filterMaskFlag = false;
		},

		// 跳转详情
		jumpToDetail: function (event) {

			var _itemverId = event.target.dataset.id || event.target.parent.dataset.id
			// console.log( _itemverId)
			window.location.href = ctx + "rest/html5/guide/detail?itemVerId=" + _itemverId
		},

		init: function () {
			this.fetchGuideList();
			this.fetchThemeAndDepart();
		},
	},
});
/*})*/