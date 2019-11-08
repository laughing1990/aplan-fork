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
                    field: 'dismissedTime',
                    sort: 'desc'
				},
				theme: '',
				applyStartTime: '',
				applyEndTime: '',
				applySource: '',
				applyType: '',
				instState: '',
				dismissedStartTime: '',
				dismissedEndTime: '',
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

			if (ts.searchFrom.dismissedStartTime != '' && ts.searchFrom.dismissedEndTime != '') {
				if (ts.searchFrom.dismissedStartTime > ts.searchFrom.dismissedEndTime) {
					ts.apiMessage('不予受理开始时间不能大于不予受理结束时间', 'error');
					return;
				}
			}

			ts.loading = true;

			request('', {
				url: ctx + '/rest/conditional/query/listItemOutScope',
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
		//查看详情
		viewDetail: function (row) {
			var ts = this;
			ts.loading = true;
			request('', {
				url: ctx + '/rest/conditional/query/queryApplyInfoTaskId',
				type: 'get',
				data: {applyinstId: row.applyinstId}
			}, function (res) {
				ts.loading = false;
				if (res.success) {
					if (row.viewId) {
						window.open(ctx + '/apanmis/page/stageApproveIndex?isNotCompareAssignee=true&taskId=' + res.content.taskId + '&viewId=' + row.viewId + '&busRecordId=' + row.busRecordId, '_blank');
					} else {
						window.open(ctx + '/apanmis/page/stageApproveIndex?isNotCompareAssignee=true&taskId=' + res.content.taskId + '&viewId=' + res.content.viewId + '&busRecordId=' + row.busRecordId, '_blank');
					}
				} else {
					return ts.apiMessage(res.message, 'error')
				}
			}, function () {
				ts.loading = false;
				return ts.apiMessage('网络错误！', 'error')
			});

		}

	},
	created: function () {
		this.conditionalQueryDic();
		this.fetchTableData();
	}
});