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
				keyword: '',
				itemNature: '8'
			}
		}
	},
	methods: {
		//刷新列表
		fetchTableData: function () {
			var ts = this;

			this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

			ts.loading = true;

			request('', {
				url: ctx + 'rest/conditional/query/listSeriesApplyItem',
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
		//单项申报
		seriesApply: function (row) {
			var itemNature = row.itemNature;
			var itemVerId = row.itemVerId;
			var itemName = row.itemName;
			var urltemp = ctx + 'market/agentApplyIndex.html?itemVerId=' + itemVerId;
			if (itemNature && '8' == itemNature) {
				urltemp = ctx + 'market/agentApplyIndex.html?itemVerId=' + itemVerId;
			}
			var data = {
				'menuName': itemName,
				'menuInnerUrl': urltemp,
				'id': itemVerId
			};
			try {
				parent.vm.addTab('', data, '', '');
			} catch (e) {
				window.open(urltemp, '_blank');
			}
			return;

		}
	},
	created: function () {
		this.fetchTableData();
	}
});