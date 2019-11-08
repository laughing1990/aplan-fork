/*
 * @Author: ZL
 * @Date:   $ $
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
	el: '#approveDetails',
	data: function () {
		return {
			tabData: [{
				label: '基本信息',
				name: 'baseInfo',
				id: '1'
			}, {
				label: '材料列表',
				name: 'materialList',
				id: '2'
			}/*, {
        label: '核查意见',
        name: 'checkOption',
        id: '3'
      }*/, {
				label: '办理过程',
				name: 'checkProcess',
				id: '4'
			}, {
				label: '并联审批办理情况汇总',
				name: 'parallelApprove',
				id: '5'
			}],
			tabActive: 0, // tab选择状态
			historyApproveProcessData: [],
			parallelApproveData: [],
			commonMatList: [],
			officeMatList: [],
			stageName: '',
			iteminstMat: {},

			iteminstInfo: {},
			projInfo: {},
			linkman: {},
			applyData: {}

		}
	},
	created: function () {
		this.getBaseInfo();
	},
	mounted: function () {

	},
	filters: {
		formatDate: function (longTypeDate) {
			if (longTypeDate == '' || longTypeDate == null) return '';
			var dateTypeDate = "";
			var date = new Date();
			date.setTime(longTypeDate);
			dateTypeDate += date.getFullYear(); //年
			dateTypeDate += "-" + ((date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : ("0" + (date.getMonth() + 1))); //月
			dateTypeDate += "-" + (date.getDate() >= 10 ? date.getDate() : "0" + date.getDate()); //日
			dateTypeDate += " " + (date.getHours() >= 10 ? date.getHours() : "0" + date.getHours()); //时
			dateTypeDate += ":" + (date.getMinutes() >= 10 ? date.getMinutes() : "0" + date.getMinutes());  //分
			//dateTypeDate += ":" + (date.getSeconds() >= 10 ? date.getSeconds() : "0" + date.getSeconds());  //秒
			return dateTypeDate;
		}
	},
	updated: function () {
	},
	methods: {
		changeTab: function (ele, index) {
			console.log(ele)
			this.tabActive = index;
			$('#' + ele).addClass('active').siblings('.tab-content-item').removeClass('active');
			if (index == 0) {//基本信息
				this.getBaseInfo();
			} else if (index == 1) {//材料列表
				this.listMatinst();
			} else if (index == 2) {//办理过程
				this.listHistoryApproveProcess();
			} else if (index == 3) {//并联审批办理情况汇总
				this.listParallelApproveData();
			}
		},
		listHistoryApproveProcess: function () {
			var vm = this;
			$.ajax({
				url: ctx + 'province/listHistoryApproveProcess',
				type: "get",
				cache: false,
				data: {
					project_code: project_code,
					item_instance_code: item_instance_code
				},
				success: function (result) {
					if (result.success) {
						vm.historyApproveProcessData = result.content;
					} else {
						console.log(result.message)
					}
				},
				error: function (msg) {
					//vm.agcloud.ui.metronic.showSwal({message: "获取办理过程数据失败！",type: 'info'});
				}
			})
		},
		listHistoryApproveProcessOther: function (projectCode, itemInstanceCode) {
			window.open(ctx + 'province/getApproveItem?project_code=' + projectCode + '&item_instance_code=' + itemInstanceCode + '&access_token=' + access_token);
		},
		listParallelApproveData: function () {
			var vm = this;
			$.ajax({
				url: ctx + 'province/listParallelApproveData',
				type: "get",
				cache: false,
				data: {
					project_code: project_code,
					item_instance_code: item_instance_code
				},
				success: function (result) {
					if (result.success) {
						vm.parallelApproveData = result.content;
					} else {
						console.log(result.message)
					}
				},
				error: function (msg) {
					//vm.agcloud.ui.metronic.showSwal({message: "获取并联审批办理情况数据失败！",type: 'info'});
				}
			})
		},
		/*获取材料列表*/
		listMatinst: function () {
			var vm = this;
			$.ajax({
				url: ctx + 'province/getMatintList',
				type: "get",
				cache: false,
				data: {
					project_code: project_code,
					item_instance_code: item_instance_code
				},
				success: function (result) {
					console.log(result)
					if (result.success) {
						vm.iteminstMat = result.content.itemMatinst
						vm.commonMatList = result.content.commonMatList;
						vm.stageName = result.content.stageName;
						vm.officeMatList = result.content.officeMatList;
					} else {
						//vm.agcloud.ui.metronic.showSwal({message: "获取材料列表数据失败！",type: 'info'});
					}
				},
				error: function (msg) {
					//vm.agcloud.ui.metronic.showSwal({message: "获取材料列表数据失败！",type: 'info'});
				}
			})
		},
		/*全部下载按钮*/
		downAllFile: function () {
			window.location.href = ctx + 'province/downFile?item_instance_code=' + item_instance_code;
			console.log(item_instance_code)
		},
		/*下载单个材料实例下的所有附件*/
		downFile: function (matinstId) {
			console.log(matinstId)
			window.location.href = ctx + 'province/downFile?matinstId=' + matinstId;

		},
		getBaseInfo: function () {
			var _this = this;
			$.ajax({
				url: ctx + 'province/getBaseInfo',
				type: "get",
				cache: false,
				data: {
					project_code: project_code,
					item_instance_code: item_instance_code
				},
				success: function (result) {
					if (result.success) {
						_this.iteminstInfo = result.content.iteminstInfo;
						_this.projInfo = result.content.projInfo;
						_this.linkman = result.content.linkman;
						_this.applyData = result.content.applyData;
					} else {
						console.log(result.message)
					}
				},
				error: function (msg) {
					//agcloud.ui.metronic.showSwal({message: "获取申办事项基本信息失败！",type: 'info'});
				}
			})
		},
		/*下载单个文件*/
		downOneFile: function (fileId) {
			console.log("fileId:" + fileId)
			window.location.href = ctx + 'province/downOneFileByFileId?fileId=' + fileId;
		}
	}
});