/*require(['jquery','vue','ELEMENT','common'], function($,Vue,ELEMENT,common) {
    Vue.use(ELEMENT);*/
function deepGet(object, path, defaultValue) {
	return (!Array.isArray(path) ? path.replace(/\[/g, '.').replace(/\]/g, '').split('.') : path)
			.reduce((o, k) => (o || {})[k], object) || defaultValue;
}

var vm = new Vue({
	el: '#app',
	mixins: [mixin],
	data: function () {
		return {
			detailData: {
				itemBasicInfo: {},
			},

			// 材料数据
			materialData: {
				aeaItemMats: [],
				stateQuestions: []
			},


			// 每次重新选择后的关联数组
			userSelectAnswerList: [],

			// 材料获取参数
			materialSearchData: {
				itemVerId: getQueryString('itemVerId'),
				isParent: true,
				itemStateId: "",
			},

			// 获取所有的材料
			allMaterials: [],
		}
	},
	computed: {},
	created: function () {
		this.init()
	},
	mounted: function () {

	},
	methods: {
		// 页面初始化
		init: function () {
			this.fetchMatterDetail();
			this.fetchmaterial();
		},

		// 获取页面数据
		fetchMatterDetail: function () {
			var vm = this,
					_searchData = {};
			_searchData.itemVerId = getQueryString('itemVerId')
			console.log(_searchData)
			vm.loading = true;
			$.ajax({
				url: ctx + 'rest/guide/details',
				type: 'get',
				datatype: "json",
				data: _searchData,
				success: function (res) {
					vm.loading = false;
					if (res.success) {
						if (JSON.stringify(res.content) == '{}') {
							vm.loading = false;
							vm._alertState(true, '暂无数据！');
						}
						vm.detailData = res.content;
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

		// 改变当前所阅位置
		changeDetailView: function (target) {
			var _navUl = $('.detail-nav ul');
			var _detailUl = $('.detail-box')
			_navUl.find('li').removeClass('active')
			_navUl.find('li[data-link=' + target + ']').addClass('active');
			_detailUl.find('div[data-link=' + target + ']').siblings().hide();
			_detailUl.find('div[data-link=' + target + ']').show();
		},

		// 办事情形与所需材料
		fetchmaterial: function () {
			var vm = this,
					_searchData = {};
			_searchData = vm.materialSearchData;
			vm.loading = true;
			$.ajax({
				url: ctx + 'rest/guide/getItemStatesAndMaterials',
				type: 'get',
				datatype: "json",
				data: _searchData,
				success: function (res) {
					vm.loading = false;
					if (res.success) {
						vm.loading = false;
						// vm.materialData = res.content
						vm.materialData.stateQuestions = res.content.stateQuestions || []
						vm.materialData.aeaItemMats = res.content.aeaItemMats || []

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

		// 选择情形问题的答案-单选
		radioAnswerChange: function (val, item) {
			// console.log(item)
			if (!val) return
			var vm = this;
			vm.materialSearchData.isParent = false;
			vm.materialSearchData.itemStateId = val.itemStateId;

			var _index = this.deletPre(item)    //取消时的子问题的index

			$.ajax({
				url: ctx + 'rest/guide/getItemStatesAndMaterials',
				type: 'get',
				datatype: "json",
				data: vm.materialSearchData,
				success: function (res) {
					vm.loading = false;
					if (res.success) {
						vm.loading = false;
						// vm.materialData.aeaItemMats = res.content.aeaItemMats;

						// if(_index> 0){
						//     res.content.stateQuestions.forEach(function(m){
						//         vm.materialData.stateQuestions.splice(_index, 1, m)
						//         _index++;
						//     })
						// }else{
						//     vm.materialData.stateQuestions = vm.materialData.stateQuestions .concat(res.content.stateQuestions);
						// }

						vm.materialData.stateQuestions = vm.materialData.stateQuestions.concat(res.content.stateQuestions);
						vm.materialData.aeaItemMats = vm.materialData.aeaItemMats.concat(res.content.aeaItemMats);
						console.log(vm.materialData)
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
		// 取消操作时处理掉后续的子问题
		deletPre: function (item) {
			var _list = [];
			item.answers.forEach(function (t) {
				_list.push(t.itemStateId)
			})

			var _index = 0;
			var _child = '';
			for (var i = 0; i < vm.materialData.stateQuestions.length; i++) {
				var _curParentId = vm.materialData.stateQuestions[i].question.parentStateId
				if (_list.indexOf(_curParentId) !== -1) {
					_index = i;
					_child = vm.materialData.stateQuestions[i]
				}
			}
			if (_index > 0) {
				var _del = vm.materialData.stateQuestions.splice(_index, 1)
			}

			_child && this.deletPre(_child)
			return _index;
		},

		// 多选的处理后续的子问题
		dbDeletPre: function (item) {
			var _list = [];
			item.answers.forEach(function (t) {
				_list.push(t.itemStateId)
			})

			var _index = 0;
			var _child = '';
			for (var i = 0; i < vm.materialData.stateQuestions.length; i++) {
				var _curParentId = vm.materialData.stateQuestions[i].question.parentStateId
				if (_list.indexOf(_curParentId) !== -1) {
					_index = i;
					_child = vm.materialData.stateQuestions[i]
				}
			}
			if (_index > 0) {
				var _del = vm.materialData.stateQuestions.splice(_index, 1)
			}

			_child && this.dbDeletPre(_child)
			return _index;
		},

		// 选择情形问题的答案-多选
		checkboxAnswerChange: function (val, item) {
			if (!val) return
			var vm = this;
			vm.materialSearchData.isParent = false;
			vm.materialSearchData.itemStateId = val.itemStateId;

			var _index = this.dbDeletPre(item)    //取消时的子问题的index
			if (_index) return

			vm.loading = true;

			$.ajax({
				url: ctx + 'rest/guide/getItemStatesAndMaterials',
				type: 'get',
				datatype: "json",
				data: vm.materialSearchData,
				success: function (res) {
					vm.loading = false;
					if (res.success) {
						vm.loading = false;
						// vm.materialData.aeaItemMats = res.content.aeaItemMats;

						// if(_index> 0){
						//     res.content.stateQuestions.forEach(function(m){
						//         vm.materialData.stateQuestions.splice(_index, 1, m)
						//         _index++;
						//     })
						// }else{
						//     vm.materialData.stateQuestions = vm.materialData.stateQuestions .concat(res.content.stateQuestions);
						// }

						vm.materialData.stateQuestions = vm.materialData.stateQuestions.concat(res.content.stateQuestions);
						vm.materialData.aeaItemMats = vm.materialData.aeaItemMats.concat(res.content.aeaItemMats);
						console.log(vm.materialData)
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

		// 查看材料
		lookMaterial: function () {
			var vm = this,
					curNeedNum = 0,
					$situationWrap = $('.situation-wrap'),
					_getData = {},
					_itemstateIdList = [];
			var isneedDoms = $situationWrap.find(".need-span").length;
			var answerDoms = $situationWrap.find("input[type='radio']:checked,input[type='checkbox']:checked");
			for (var i = 0; i < answerDoms.length; i++) {
				if ($(answerDoms[i]).data('isneed') == 1) {
					curNeedNum++;
				}
				_itemstateIdList.push($(answerDoms[i]).data('itemstateid'))
			}
			// console.log(_itemstateIdList)
			if (curNeedNum < isneedDoms) {
				return this._alertState(true, '请选择必填信息');
			}
			_getData.itemStateIds = _itemstateIdList.join(",");
			_getData.itemVerId = getQueryString('itemVerId');

			vm.loading = true;

			$.ajax({
				url: ctx + 'rest/guide/findAllMats',
				type: 'get',
				datatype: "json",
				data: _getData,
				success: function (res) {
					vm.loading = false;
					if (res.success) {
						vm.loading = false;
						vm.allMaterials = res.content;
						vm.changeDetailView('needmaterial');
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

	},
});
/*})*/