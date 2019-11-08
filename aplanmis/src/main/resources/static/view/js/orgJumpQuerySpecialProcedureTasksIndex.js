var vm = new Vue({
	el: '#searchTable',
	mixins: [mixins],
	data: function () {
		return {
			//查询数据
			searchFrom: {
				pagination: {
					page: 1,
					pages: 1,
					perpage: 10
				},
				sort: {},
				theme: '',
				applyType: '',
				// applyStartTime: '',
				// applyEndTime: '',
				specialStartTime: '',
				specialEndTime: '',
				keyword: ''
			},
			dicCodeItems: [],//特殊程序类型字典

		}
	},
	methods: {
		//刷新列表
		fetchTableData: function () {
			var ts = this;

			this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

			if (ts.searchFrom.specialStartTime != '' && ts.searchFrom.specialEndTime != '') {
				if (ts.searchFrom.specialStartTime > ts.searchFrom.specialEndTime) {
					ts.apiMessage('申报开始时间不能大于结束时间', 'error');
					return;
				}
			}

			ts.loading = true;
			ts.searchFrom.entrust = entrust;
			request('', {
				url: ctx + 'rest/conditional/jump/lisSpecialProcedureTasks',
				type: 'get',
				data: ts.searchFrom
			}, function (res) {
				ts.loading = false;
				if (res.success) {
					ts.tableData = res.content.list;
					ts.dataTotal = res.content.total;
				} else {
					return ts.apiMessage(res.message, 'error')
				}
			}, function () {
				ts.loading = false;
				return ts.apiMessage('网络错误！', 'error')
			});
		},
		//获取查询条件的数据
		conditionalQueryDic: function () {
			var ts = this;
			// ts.loading = true;
			request('', {
				url: ctx + 'rest/conditional/jump/task/dic/list',
				type: 'get',
				data: {}
			}, function (res) {
				// ts.loading = false;
				if (res.success) {
					ts.themeList = res.content.themeList;
					ts.globalThemeList = res.content.themeList;
				} else {
					return ts.apiMessage(res.message, 'error')
				}
			}, function () {
				// ts.loading = false;
				return ts.apiMessage('网络错误！', 'error')
			});
		},
		//预审
		viewDetail: function (row) {

			var url = ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId;
			if (row.busRecordId) {
				url = url + '&busRecordId=' + row.busRecordId;
			}
			window.open(url, '_blank');

		},
		// 获取数据字典值
		getDicList: function () {
			var that = this;
			request('', {
				url: ctx + 'rest/dict/code/items/list',
				type: 'get',
				data: {dicCodeTypeCode: 'SPECIAL_TYPE'},
			}, function (res) {
				if (res.success) {
					that.dicCodeItems = res.content;
				}
			}, function () {
				vm.$message.error('获取数据字典失败');
			})
		},

		formatSpecialType: function (row, column, value, index) {
			console.log('value=' + value);
			console.log(this.dicCodeItems.length);
			for (var i = 0; i < this.dicCodeItems.length; i++) {
				var item = vm.dicCodeItems[i];
				if (item["itemCode"] == value) {
					value = item["itemName"];
					break;
				}
			}
			return value;
		}
	},
	created: function () {
		//查询数据字典
		this.getDicList();
		this.conditionalQueryDic();
		this.fetchTableData();


	},
	mounted: function () {

	}
});