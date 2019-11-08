var mixin = {
	data: function () {
		return {
			// 页面loading
			loading: false,
			// 页面alert
			alertData: {
				alertStatus: false,
				alertMsg: '',
			},
			// 页面toast
			toastData: {
				toastStatus: false,
				toastMsg: '',
			},
		}
	},
	created: function () {

	},
	methods: {
		// alert方法
		_alertState: function (state, msg) {
			var vm = this;
			vm.alertData.alertMsg = msg;
			vm.alertData.alertStatus = state;
		},

		// toast方法
		_toastState: function (params) {
			var vm = this;
			var _params = {
				msg: params.msg || '',
				daly: params.daly || 2000
			}
			vm.toastData.toastMsg = _params.msg;
			vm.toastData.toastStatus = true;
			setTimeout(vm._hideToast, _params.daly);
		},
		// 隐藏toast
		_hideToast: function () {
			this.toastData.toastStatus = false;
			this.toastData.toastMsg = "";
		},
	}
}