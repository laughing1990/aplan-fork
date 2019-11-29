/*
 * @Author: ZL
 * @Date:   2019/05/29
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#regist',
  data: function() {
    return {

    }
  },
  mounted: function() {},
  methods: {
    toggle: function(type) {
      if (type == 'org') {
        window.parent.location.href = ctx + 'supermarket/agentRegister/index.html';
      } else {
        window.parent.location.href = ctx + 'supermarket/ownerRegister/index.html';
      }
    }
  }
});