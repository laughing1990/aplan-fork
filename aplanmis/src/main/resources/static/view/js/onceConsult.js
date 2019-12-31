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
				busType: 'YCZX',
				// busType: 'YJZQ',//测试用
			},

			isShowMsgDetail: false,
			msgDetail: {
				remindContent: '',
				sendUserName: '',
				sendDate: ''
			},

			chnNumChar: ["零", "一", "二", "三", "四", "五", "六", "七", "八", "九"],
			chnUnitSection: ["", "万", "亿", "万亿", "亿亿"],
			chnUnitChar: ["", "十", "百", "千"],


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
			var url = ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId + '&itemNature=' + row.itemNature + '&busType=yczx&isNotCompareAssignee=true';
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
					//ts.fetchTableData();
					window.setTimeout(function () {
						window.open(ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId
								+ '&busRecordId=' + row.busRecordId + '&itemNature=' + row.itemNature + '&busType=yczx&isNotCompareAssignee=true', '_blank');
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
			var _this = this;
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
						return '<span class="op-btn"  @click="viewDetail(scope.row)">结束' + _this.TransformToChinese(row.solicitIndex) + '次征询</span>';
					} else {
						return '<span class="op-btn"  @click="viewDetail(scope.row)">被征集人查看</span>';
					}
				} else {
					if (row.promoter) {
						return '<span class="op-btn" @click="viewDetail(scope.row)">发起人查看</span>';
					} else {
						if (row.solicitIndex === 1) {
							return '<span class="op-btn" @click="viewDetail(scope.row)">回复一次征询</span>';
						} else {
							return '<span class="op-btn" @click="viewDetail(scope.row)">' + _this.TransformToChinese(row.solicitIndex) + '次征询</span>';
						}
					}
				}
			}
		},

		numToChn: function (num) {
			var _this = this;
			var index = num.toString().indexOf(".");
			if (index != -1) {
				var str = num.toString().slice(index);
				var a = "点";
				for (var i = 1; i < str.length; i++) {
					a += _this.chnNumChar[parseInt(str[i])];
				}
				return a;
			} else {
				return;
			}
		},

//定义在每个小节的内部进行转化的方法，其他部分则与小节内部转化方法相同
		sectionToChinese: function (section) {
			var _this = this;
			var str = '', chnstr = '', zero = false, count = 0;   //zero为是否进行补零， 第一次进行取余由于为个位数，默认不补零
			while (section > 0) {
				var v = section % 10;  //对数字取余10，得到的数即为个位数
				if (v == 0) {                    //如果数字为零，则对字符串进行补零
					if (zero) {
						zero = false;        //如果遇到连续多次取余都是0，那么只需补一个零即可
						chnstr = _this.chnNumChar[v] + chnstr;
					}
				} else {
					zero = true;           //第一次取余之后，如果再次取余为零，则需要补零
					str = _this.chnNumChar[v];
					str += _this.chnUnitChar[count];
					chnstr = str + chnstr;
				}
				count++;
				section = Math.floor(section / 10);
			}
			return chnstr;
		},


//定义整个数字全部转换的方法，需要依次对数字进行10000为单位的取余，然后分成小节，按小节计算，当每个小节的数不足1000时，则需要进行补零

		TransformToChinese: function (num) {
			var _this = this;
			//var a = _this.numToChn(num);
			num = Math.floor(num);
			var unitPos = 0;
			var strIns = '', chnStr = '';
			var needZero = false;

			if (num === 0) {
				return _this.chnNumChar[0];
			}
			while (num > 0) {
				var section = num % 10000;
				if (needZero) {
					chnStr = _this.chnNumChar[0] + chnStr;
				}
				strIns = _this.sectionToChinese(section);
				strIns += (section !== 0) ? _this.chnUnitSection[unitPos] : _this.chnUnitSection[0];
				chnStr = strIns + chnStr;
				needZero = (section < 1000) && (section > 0);
				num = Math.floor(num / 10000);
				unitPos++;
			}

			return chnStr;//+ a;
		},

	},
	created: function () {
		this.conditionalQueryDic();
		this.fetchTableData();
	}
});