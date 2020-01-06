//  var ctx = "http://192.168.15.101:8084/aplanmis-mall/"
var vm = new Vue({
  el: "#guideListVue",
  data: {
    // 全局loading
    ctx: ctx,
    mloading: false,
    noDataTip:"",
    keyword: '',
    pageNumDH: 1,
    pageSizeDH: 10,
    totalDH: 0,
    draftApplyListData: [],
    curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
    curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度

  },
  mounted: function () {
    this.getDraftApplyList()
  },
  methods: {
    // 删除草稿
    delDraftApply: function(_applyinstId){
      var _that = this;
      confirmMsg('确认信息', '是否确定删除此条暂存草稿？', function(){
        _that.mloading = true;
        request('', {
          url: ctx + 'rest/user/draftApply/delete/'+_applyinstId,
          type: 'get',
        }, function (res) {
          _that.mloading = false;
          if (res.success) {
            _that.getDraftApplyList();
            return _that.$message({
              message: '删除成功！',
              type: 'success'
            })
          } else {
            return _that.$message({
              message: res.message?res.message:'删除失败！',
              type: 'error'
            })
          }
        }, function () {
          _that.mloading = false;
          return _that.$message({
            message: res.message?res.message:'删除失败！',
            type: 'error'
          })
        });
      },function(){
        return false;
      },'确认','取消', 'warning', true)
    },
    getDraftApplyList: function () {
      var vm = this;
      vm.mloading = true;
      vm.noDataTip = ""
      request('', {
        url: ctx + 'rest/userCenter/apply/guide/list',
        type: 'get',
        data: {
          keyword: vm.keyword,
          pageNum: vm.pageNumDH,
          pageSize: vm.pageSizeDH,
        }
      }, function (res) {
        vm.mloading = false;
        if (res.success) {
          vm.draftApplyListData = res.content.list;
          if(vm.draftApplyListData.length<=0){
            vm.noDataTip = "暂无数据"
          }
          vm.totalDe = res.content.total;
          vm.totalDH = res.content.total;
        } else {
          return vm.$message({
            message: '加载失败！',
            type: 'error'
          })
        }
      }, function () {
        vm.mloading = false;
        return vm.$message({
          message: '加载失败！',
          type: 'error'
        })
      });
    },
    handleSizeChangeDe: function (val) {
      this.pageSizeDH = val;
      this.getDraftApplyList()
    },
    handleCurrentChangeDe: function (val) {
      this.pageNumDH = val;
      this.getDraftApplyList()
    },
  },
})