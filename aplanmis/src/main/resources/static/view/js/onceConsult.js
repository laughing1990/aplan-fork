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
					field: 'acceptTime',
					sort: 'desc'
				},
				theme: '',
				acceptStartTime: '',
				acceptEndTime: '',
				applySource: '',
				applyType: '',
				instState: '',
				arriveStartTime: '',
				arriveEndTime: '',
				keyword: '',
				// busType: 'YCZX',
				busType: 'YJZQ'
			},

			isShowMsgDetail: false,
			msgDetail: {
				remindContent: '',
				sendUserName: '',
				sendDate: ''
			}

		}
	},
	methods: {
		//刷新列表
		fetchTableData: function () {

			var ts = this;

			this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

			if (ts.searchFrom.acceptStartTime != '' && ts.searchFrom.acceptEndTime != '') {
				if (ts.searchFrom.acceptStartTime > ts.searchFrom.acceptEndTime) {
					ts.apiMessage('受理开始时间不能大于结束时间', 'error');
					return;
				}
			}

			if (ts.searchFrom.arriveStartTime != '' && ts.searchFrom.arriveEndTime != '') {
				if (ts.searchFrom.arriveStartTime > ts.searchFrom.arriveEndTime) {
					ts.apiMessage('到达开始时间不能大于结束时间', 'error');
					return;
				}
			}
			/*
						if (ts.searchFrom.sort["field"] == "acceptTime" && ts.searchFrom.sort["sort"] == "desc") {
							ts.searchFrom.sort["field"] = "isGreenWay,acceptTime";
						}*/

			ts.loading = true;

			request('', {
				url: ctx + '/rest/solicit/list/solicit',
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
		//办理
		viewDetail: function (row) {
			var url = ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId + '&itemNature=' + row.itemNature;
			if (row.busRecordId) {
				url = url + '&busRecordId=' + row.busRecordId;
			}
			window.open(url, '_blank');
		},

		//判断是否已签收
		isSign: function (row) {
			if (row.solicitId && !row.detailUserId) {
				return true;
			} else if (row.detailUserId && row.signTime) {
				return true;
			} else if (row.detailUserId && !row.signTime) {
				return false;
			}

			return false;
		},
		//签收
		signTask: function (event, row) {
			var ts = this;
			var detailUserId = row.detailUserId;
			event.preventDefault();
			ts.loading = true;
			request('', {
				url: ctx + 'rest/solicit/sign/' + detailUserId,
				type: 'get',
				data: {}
			}, function (result) {
				ts.loading = false;
				if (result.success) {
					ts.fetchTableData();
				} else {
					ts.$message.error(result.message);
				}
			}, function () {
				ts.loading = false;
				ts.$message.error("签收失败！");
			});
		},
		setRemindMessageRead: function (row, remindReceiverId) {

			var ts = this;
			request('', {
				url: ctx + 'rest/conditional/query/updateRemindRead',
				type: 'get',
				data: {'remindReceiverIds': remindReceiverId}
			}, function (res) {
				if (res.success) {
					for (var i = 0; i < row.remindList.length; i++) {
						if (row.remindList[i].remindReceiverId == remindReceiverId) {
							row.remindList.splice(i, 1);
							break;
						}
					}
				} else {
					return ts.apiMessage(res.message, 'error')
				}
			}, function () {
				return ts.apiMessage('网络错误！', 'error')
			});
		},
		showRemindInfo: function (row, remindInfo) {
			this.setRemindMessageRead(row, remindInfo.remindReceiverId);
			this.msgDetail.remindContent = remindInfo.remindContent;
			this.msgDetail.sendUserName = remindInfo.sendUserName;
			this.msgDetail.sendDate = this.formatDatetimeCommon(remindInfo.sendDate, 'yyyy-MM-dd hh:mm');
			this.isShowMsgDetail = true;
		},
		getFirstColumnWidth: function () {
			if (this.tableData) {
				for (var i = 0; i < this.tableData.length; i++) {
					if (this.tableData[i].isGreenWay == '1') {
						return '90';
					}
				}
			}
			return '65';
		},
		solicitStateFormatter: function (row) {

		},
		formatState: function (row, column, cellValue, index) {
			if (['2', '3'].indexOf(row.solicitState) != -1) {
				if (row.conclusionFlag && row.conclusionFlag == '1') {
					return "已结束";
				} else {
					return "待整改"
				}
			} else if (row.solicitState == '1') {//进行中
				return row.rateProgress;
			}
		},
		//按钮格式化
		stateBtnFormat: function (row, column, cellValue, index) {
			if (['2', '3'].indexOf(row.solicitState) != -1) {// 已完成或终止
				//判断是否发起人，只有发起人才能结束征询或重新发起
				if (!row.countLimit || row.countLimit > 0) {//判断重新发起限制次数
					if (row.promoter) {
						return '<span class="op-btn" @click="viewDetail(scope.row)">牵头部门重新发起</span>';
					} else {
						return '<span class="op-btn"  @click="viewDetail(scope.row)">被征集人查看</span>';
					}
				}
			} else if (row.solicitState == '1') {//进行中
				if (row.finishProgressNum === row.allProgressNum) {//全部征询完
					if (row.promoter) {

						return '<span class="op-btn"  @click="viewDetail(scope.row)">结束一次征询</span>';
					} else {
						return '<span class="op-btn"  @click="viewDetail(scope.row)">被征集人查看</span>';
					}
				} else {
					if (row.promoter) {
						return '<span class="op-btn" @click="viewDetail(scope.row)">发起人查看</span>';
					} else {
						return '<span class="op-btn" @click="viewDetail(scope.row)">回复一次征询</span>';
					}
				}
			}
		}

	},
	created: function () {
		this.conditionalQueryDic();
		this.fetchTableData();
	}
});