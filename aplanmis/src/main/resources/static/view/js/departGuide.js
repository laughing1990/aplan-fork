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
				// sort: {
				// 	field: 'acceptTime',
				// 	sort: 'desc'
				// },
				// theme: '',
				// acceptStartTime: '',
				// acceptEndTime: '',
				// applySource: '',
				// applyType: '',
				// instState: '',
				guideStartTime: '',
				guideEndTime: '',
				keyword: '',
			},

			isShowMsgDetail: false,
			msgDetail: {
				remindContent: '',
				sendUserName: '',
				sendDate: ''
			},
			stateList: [
				{value: '1', label: '牵头部门待签收'},
				{value: '2', label: '牵头部门处理中'},
				{value: '3', label: '所有部门征求处理中'},
				{value: '4', label: '申请人待确认'},
				{value: '5', label: '结束'},
			],
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
				url: ctx + 'dept/guide/list',
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
		// 部门辅导跳往并联申报页面
		viewDetail: function (row) {
			// var url = ctx + 'apanmis/page/stageApplyIndex?guideId='+row.guideId;
			var menuName= '';
			var menuInnerUrl =  '';
			var id = 'menu_'+new Date().getTime();
			menuName = row.projName;
			var themeCategory;
			if (!!row.themeCategory) {
				themeCategory = row.themeCategory.toUpperCase();
			} else {
				themeCategory = 'OTHERS';
			}
			menuInnerUrl = ctx + '/apanmis/page/stageApplyIndex?applyinstId=' + row.applyinstId
				+ '&themeCategory=' + themeCategory + '&guideId=' + row.guideId;
			var data = {
				'menuName':menuName,
				'menuInnerUrl':menuInnerUrl,
				'id':id,
				'applyinstId':row.applyinstId,
			};
			try{
				parent.vm.addTab('',data,'','');
			}catch (e) {
				window.open(menuInnerUrl,'_blank');
			}
			return null;
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
			var solicitDetailId = row.solicitDetailId;
			event.preventDefault();
			ts.loading = true;
			request('bpmFrontUrl', {
				url: ctx + 'rest/solicit/sign/' + solicitDetailId,
				type: 'get',
				data: {}
			}, function (result) {
				ts.loading = false;
				if (result.success) {
					// this.fetchTableData();
					window.setTimeout(function () {
						window.open(ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId + '&busRecordId=' + row.busRecordId + '&itemNature=' + row.itemNature + '&busType=lhps&isNotCompareAssignee=true', '_blank');
						window.location.reload();
					}, 500);
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
		//状态转换
		formatState: function (row, column, cellValue, index) {
			if (row.solicitState && row.solicitState == 2) {
				return '部门辅导完成';
			} else if (row.solicitState && row.solicitState == 3) {
				return '部门辅导中止';
			} else if (row.finishProgressNum == row.allProgressNum && row.promoter) {
				return "结束部门辅导";
			} else if (row.finishProgressNum == row.allProgressNum && !row.promoter) {
				return "牵头部门辅导";
			} else {
				return "部门辅导";
			}
		},
		//按钮转换
		stateBtnFormat: function (row, column, cellValue, index) {
			if (row.finishProgressNum == row.allProgressNum && row.promoter) {
				return '<span class="op-btn" @click="viewDetail(scope.row)">结束联合评审</span>';
			} else {
				return '<span class="op-btn"  @click="viewDetail(scope.row)">联合评审</span>';
			}
		},
		formatDate: function(val){
			return __STATIC.formatDate(val);
		},

	},
	created: function () {
		this.conditionalQueryDic();
		this.fetchTableData();
	}
});