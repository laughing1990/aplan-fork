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
				sort: {
                    field: 'applyTime',
                    sort: 'desc'
				},
				theme: '',
				applyType: '',
				applyStartTime: '',
				applyEndTime: '',
				keyword: ''
			}

		}
	},
	methods: {
		//刷新列表
		fetchTableData: function () {
			var ts = this;

			this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

			if (ts.searchFrom.applyStartTime != '' && ts.searchFrom.applyEndTime != '') {
				if (ts.searchFrom.applyStartTime > ts.searchFrom.applyEndTime) {
					ts.apiMessage('申报开始时间不能大于结束时间', 'error');
					return;
				}
			}

			ts.loading = true;

			request('', {
				url: ctx + 'rest/conditional/query/listDoneliminaryTasks',
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
		//预审
		viewDetail: function (row) {

			var url = ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId;
			if (row.busRecordId) {
				url = url + '&busRecordId=' + row.busRecordId;
			}
			window.open(url, '_blank');

		}

	},
	created: function () {
		this.conditionalQueryDic();
		this.fetchTableData();
	}
});