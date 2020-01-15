/*
 * @Author:
 * @Date:
 * @Last Modified by:
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#app',
  data: function () {
    return {
      loading: false,
      tableData: [],
      applyinstId: '',
      reverse: true,
    };
  },
  mounted: function () {
    var vm = this;
    vm.applyinstId = __STATIC.getUrlParam('applyinstId');
    vm.loading = true;
    request('', {
      url: ctx + 'rest/certificate/out/materials/mail/post',
      type: 'get',
      data: {
        applyinstId: vm.applyinstId,
      },
    }, function(res) {
      vm.loading = false;
      if (res.success) {
        handle(res.content.certRegistrationItemVos);
      } else {
        vm.$message.error(res.message || '获取物流信息失败');
      }
    }, function(){
      vm.loading = false;
      vm.$message.error('获取物流信息失败');
    });
    function handle(list) {
      var _list = [];
      list.forEach(function(u){
        if (u.expressNum&&u.expressNum.length) {
          u.logicCompany = '中国邮政';
          u.logicPhone = '95338';
          u.remark = '无';
          u.senderName = '';
          u.addresseeName = '';
          u.logicDetail = [];
          u.certList = [];
          u.itemInfo = {};
          _list.push(u);
          requestLogic(u);
        }
      });
      vm.tableData = _list;
    }
    function requestLogic(u){
      request('', {
        url: ctx + 'rest/certificate/cert/logistics/detail',
        type: 'get',
        data: {
          applyinstId: vm.applyinstId,
          expressNum: u.expressNum,
          iteminstId: u.iteminstId,
        },
      }, function(res){
        if(res.success) {
          u.certList = res.content.aeaHiCertinsts;
          u.logicList = res.content.logisticsOrderDetails;
          // u.logicList[u.logicList.length-1].color = '#169aff';
          u.itemInfo = res.content.aeaHiSmsSendItem;
          u.senderName = u.itemInfo.senderName;
          u.addresseeName = u.itemInfo.addresseeName;
        }
      })
    };
  },
  methods: {},
});












