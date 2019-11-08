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
                    field: 'correctDueTime',
                    sort: 'desc'
				},
				theme: '',
				applyStartTime: '',
				applyEndTime: '',
				applySource: '',
				applyType: '',
				correctDueStartTime: '',
				correctDueEndTime: '',
				organizerId: '',
				keyword: ''
			},
			//承办单位
			organizerList: []

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

			if (ts.searchFrom.correctDueStartTime != '' && ts.searchFrom.correctDueEndTime != '') {
				if (ts.searchFrom.correctDueStartTime > ts.searchFrom.correctDueEndTime) {
					ts.apiMessage('设定补正开始时间不能大于设定补正结束时间', 'error');
					return;
				}
			}

			ts.loading = true;

			request('', {
				url: ctx + 'rest/conditional/query/listUnConfirmItemCorrect',
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
		//补正待确认跳转页面---todo
		viewDetail: function (row) {
			var correctId = row.correctId;
			var parentIfreamUrl = window.frames.location.href;
			var urltemp = ctx + 'rest/correct/matConfirm.html?correctId=' + row.correctId;
			//window.open(urltemp,'_blank')
            var currentTabId = parent.vm.activeTabIframe;
            urltemp = urltemp + '&parentIfreamUrl=' + parentIfreamUrl + '&currentTabId=' + currentTabId;
			var data = {
				'menuName': '材料补正',
				'menuInnerUrl': urltemp,
				'id': correctId
			};
			parent.vm.addTab('', data, '', '');
			return;

		},
		queryOrganizer: function () {
			var ts = this;
			request('', {
				url: ctx + 'rest/conditional/query/listNeedCorrectTasksOrganizer',
				type: 'get',
				data: {},
				async: true
			}, function (res) {
				if (res.success) {
					ts.organizerList = res.content;
				} else {
					return ts.apiMessage(res.message, 'error')
				}
			}, function () {
				return ts.apiMessage('网络错误！', 'error')
			});
		}

	},
	created: function () {
		this.conditionalQueryDic();
		this.queryOrganizer();
		this.fetchTableData();
	}
});