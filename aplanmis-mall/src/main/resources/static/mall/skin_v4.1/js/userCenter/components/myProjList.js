var pager = new Vue({
  el: '#myProjListId',
  data: {
    loading: false,
    ctx: ctx,

    // 页面列表查询参数
    checkData: {
      pageNum: 1,
      pageSize: 9,
      keyword: ''
    },
    // 页面列表数据
    list: [],
    // 页面列表总数
    total: 0,

    // 是否展示新增项目面板
    isShowAddProjPandel: false,
    // 当前操作的项目
    curHandelProj: {},
    // 是否可加载更多
    hasLoadMoreFlag: true,
  },
  methods: {
    // 页面列表数据查询
    searchList: function () {
      this.checkData.pageNum = 1;
      this.fetchMyProjList();
    },
    // 页面列表数据加载更多
    listLoadMore: function () {
      this.checkData.pageNum++;
      this.fetchMyProjList();
    },
    // 获取页面列表数据
    fetchMyProjList: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/user/root/proj/list',
        type: 'get',
        data: ts.checkData
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.total = res.content.total;
          if (ts.checkData.pageNum == 1) {
            ts.list = res.content.list;
          } else {
            ts.list = ts.list.concat(res.content.list);
          }
          // 计算是否可加载更多，默认第一次可加载更多
          if(ts.total > ts.list.length || ts.checkData.pageNum == 1){
            ts.hasLoadMoreFlag = true;
          }else{
            ts.hasLoadMoreFlag = false;
          }
          console.log(ts.list.length)
          console.log(ts.total)
        } else {
          return ts.$message.error(res.massage)
        }

      }, function (err) {
        ts.$message.error('获取我的项目列表失败')
      });
    },
    // 项目操作-打开全景图
    todiaGramPage :function (item) {
      window.open(ctx + 'rest/project/diagram/status/projInfo?projInfoId='+ item.projInfoId);
    },
    // 打开新增发改项目的面板
    loadAddProjPandel: function(){
      this.isShowAddProjPandel = true;
      $.get(ctx + 'rest/user/toAddFgProj', function (result) {
        $('#addProjPandel').html(result);
      });
    },
    // 打开新增本地项目的面板
    loadAddLocalProjPandel: function(){
      this.isShowAddProjPandel = true;
      $.get(ctx + 'rest/user/toAddLocalProj', function (result) {
        $('#addProjPandel').html(result);
      });
    },
    // 打开签订代办协议的面板
    toSignAgencyAgreementPage: function(item){
      this.isShowAddProjPandel = true;
      this.curHandelProj = item;
      $.get(ctx + 'rest/user/toSignAgencyAgreementPage', function (result) {
        $('#addProjPandel').html(result);
      });
    },
  },
  created: function () {
    this.fetchMyProjList();
  },
})