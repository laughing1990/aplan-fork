var app = new Vue({
  el: '#app',
  data: function() {
    return {
      multipleSelection: [],
      ctx: ctx,
      nounDialog: false,
      orgDialog: false,
      addWindowDialog: false,
      windowHeight: document.documentElement.clientHeight - 186,
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      activeName: 'first',
      labelPosition: 'right',
      noun: '',
      org: '',
      keyword: '',
      regionKeyword: '',
      orgKeyword: '',
      stageExpend: true,
      fileList: [],
      showTrackInfoDialogFlag: false,
      trackInfoList: [],
      noinfo: false
    }
  },
  created: function() {
    this.showData();
    this.applyinstId = getUrlParam('masterEntityKey');
  },
  methods: {
    // 请求table数据
    showData: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + '/gd-tywl/queryOrderByApplyInstId.do',
        data: {
          applyinstId: this.applyinstId
        },
      }, function(res) {
        vm.tableData = res.content.data.rows;

      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },

    clearSearch: function() {
      this.nowOrg = new Object;
      this.nowRegion = new Object;
      this.noun = '';
      this.org = '';
      this.keyword = '';
      this.showData();
    },

    stepTitleFun: function(step) {
      return '【' + step.deal_time + '】【' + step.deal_address + '】';
    },
    trackStatus: function(step) {
      return step.status == '10' ? 'success' : 'finish';
    },
    showTrackInfoFun: function(row) {
      var that = this;
      if (!row.mail_num) {
        return;
      }
      request('', {
        type: 'get',
        url: ctx + 'gd-tywl/queryTrackInfo.do',
        data: {
          // mailNum: '1117825301813',
          mailNum: row.mail_num,
          expressType: 'GD-EMS'
        },
      }, function(res) {
        if (res.success && res.content.mailTrackInfos.length != 0) {
          that.trackInfoList = res.content.mailTrackInfos;
          that.noinfo = false;
        } else {
          that.noinfo = true;
        }
      }, function(err) {
        that.$message.error('服务器错了!');
      })
    }
  },
  filters: {
    status: function(val) {
      switch (val) {
        case '00':
          return '收寄'
          break;
        case '10':
          return '妥投'
          break;
        case '20':
          return '未妥投'
          break;
        case '30':
          return '经传过程中'
          break;
        case '40':
          return '离开处理中心'
          break;
        case '41':
          return '到达处理中心'
          break;
        case '50':
          return '安排投递'
          break;
        case '51':
          return '正在投递'
          break;
        case '60':
          return '揽收'
          break;
        default:
          return '待快递'
          break;
      }
    }
  },
  watch: {

  }
});

function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}