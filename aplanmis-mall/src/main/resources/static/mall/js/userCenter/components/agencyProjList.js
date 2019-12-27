var pager = new Vue({
  el: '#agencyProjListId',
  data: {
    loading: false,
    ctx: ctx,

    // 页面列表查询参数
    checkData: {
      pageNum: 1,
      pageSize: 10,
      keyword: ''
    },
    // 页面列表数据
    list: [],
    // 页面列表总数
    total: 0,

    // 是否展示协议面板
    isShowAddAgreementPandel: false,

  },
  methods: {
    // 页面列表数据查询
    searchList: function () {
      this.checkData.pageNum = 1;
      this.fetchAgencyProjList();
    },
    // 页面列表换页
    handleCurrentChange: function (val) {
      this.checkData.pageNum = val;
      this.fetchAgencyProjList();
    },
    // 页面列表页大小切换
    handleSizeChange: function (val) {
      this.checkData.pageSize = val;
      this.fetchAgencyProjList();
    },

    // 获取页面列表数据
    fetchAgencyProjList: function () {
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
          ts.list = res.content.list;
        } else {
          return ts.$message.error(res.massage)
        }

      }, function (err) {
        ts.$message.error('获取项目代办列表失败')
      });
    },
    // 打开新增发改项目的面板
    loadAddProjPandel: function () {
      this.isShowAddAgreementPandel = true;
      $.get(ctx + 'rest/user/toAddFgProj', function (result) {
        $('#agreementPandel').html(result);
      });
    },
    // 打开新增本地项目的面板
    loadAddLocalProjPandel: function () {
      this.isShowAddAgreementPandel = true;
      $.get(ctx + 'rest/user/toAddLocalProj', function (result) {
        $('#agreementPandel').html(result);
      });
    },
  },
  created: function () {
    this.fetchMyProjList();
  },
})