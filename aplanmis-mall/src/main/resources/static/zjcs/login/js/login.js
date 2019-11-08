/*
 * @Author: ZL
 * @Date:   2019/05/29
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
	el: '#login',
	data: function () {
		return {
			submitUserInfo: {
				userName: '',
				password: '',
				isOwner: 0
			},
			rules: {
				userName: [
					{required: true, message: '请输入用户名', trigger: 'blur'}
				],
				password: [
					{required: true, message: '请输入密码', trigger: 'blur'}
				]
			},
		}
	},
	mounted: function () {
	},
	methods: {
		changeOwner: function (isOwner) {
			this.submitUserInfo.isOwner = isOwner;
		},
		loginSubmit: function () {
			var _that = this;
			this.$refs['loginInfo'].validate(function (valid) {
				_that.submitUserInfo.password = sm3(hex_md5(_that.submitUserInfo.password));
				if (valid) {
					request('', {
						url: ctx + '/supermarket/main/login.do',
						type: 'post',
						data: _that.submitUserInfo,
					}, function (data) {
						if (data.success) {
							localStorage.setItem('loginInfo', JSON.stringify(data.content));
							location.href = _that.submitUserInfo.isOwner == "1" ? ctx + "/supermarket/main/ownerCenter.html" : ctx + "/supermarket/main/agentCenter.html";
						} else {
							_that.$message({
								message: data.message ? data.message : '登录失败，请重新登陆',
								type: 'error'
							});
						}
					}, function (msg) {
						alertMsg('', '服务请求失败', '关闭', 'error', true);
					});
				} else {
					return false;
				}
			})
		}
	}
});
