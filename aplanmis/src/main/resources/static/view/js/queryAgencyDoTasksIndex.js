function formatDate(date, fmt) {
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  var o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      var str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}

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
          field: 'startTime',
          sort: 'desc'
        },
        theme: '',
        agentApplyState: '',
        minStartTime: '',
        maxStartTime: '',
        viewDataCtrl: viewDataCtrl,
        keyword: ''
      },
      statgeNames: ['-', '立项用地规划许可阶段', '工程建设许可阶段', '施工许可阶段', '竣工验收阶段'],
      isShowMsgDetail: false,
      msgDetail: {
        remindContent: '',
        sendUserName: '',
        sendDate: ''
      },

      // 办结dialog相关
      // 是否展示办结dialog
      isShowBanjieDialog: false,
      // 当前办结行
      curBanjieRow: {},
      endAgentForm: {
        agreementEndTime: new Date(), //办结时间
      },
      endrules: {
        agreementEndTime: [{
          required: true,
          message: '请选择办结时间',
          trigger: 'change'
        }],
      },
      // 是否展示办结单pdf
      prePdfVisible: false,
      // 办结单 pdf -url
      pdfSrc: '',
      stopAgentDialogLoading: false,
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      stopAgentDialogVisible: false,
      stopAgentForm: {
        code: '',
        date: Date.now(),
        reason: '',
      },
      prePdfUrl: '',
      prePdfDialogLoading: false,
      prePdfDialogVisible: false,
      targetRow: {},
    }
  },
  methods: {
    // 终止协议弹窗 签章
    ensureStopAgent: function () {
      var reason = this.stopAgentForm.reason;
      if (!reason.length) {
        return this.$message.error('请填写终止原因');
      }
      var _date = __STATIC.formatDate(this.stopAgentForm.date, 'yyyy-MM-dd');
      var params = {
        applyAgentId: this.applyAgentId,
        agreementStopReason: reason,
        agreementStopTime: _date,
      };
      this.$message.info('接口正在开发中... 请求参数为 ' + JSON.stringify(params));
    },
    // 查看终止协议
    seeStopAgent: function () {
      var reason = this.stopAgentForm.reason;
      if (!reason.length) {
        return this.$message.error('请填写终止原因');
      }
      var _date = __STATIC.formatDate(this.stopAgentForm.date, 'yyyy-MM-dd');
      var url = ctx + 'preview/pdfjs/web/viewer.html?file=';
      var _url = ctx + 'aea/proj/apply/agent/getAgencyStopAgreementFile';
      _url += '?applyAgentId=' + this.applyAgentId;
      _url += '&agreementStopReason=' + reason;
      _url += '&agreementStopTime=' + _date;
      url += encodeURIComponent(_url);
      this.prePdfDialogVisible = true;
      this.prePdfUrl = url;
    },
    // 点击终止协议
    clickStopAgent: function (row) {
      var vm = this;
      this.targetRow = row;
      this.stopAgentForm.fileName = row.projName + '委托终止单.pdf';
      if (this.stopAgentForm.code && this.stopAgentForm.code.legnth) {
        this.stopAgentDialogVisible = true;
        return null;
      }
      var vm = this;
      this.loading = true;
      request('', {
        url: ctx + 'aea/proj/apply/agent/getAgencyDetail?applyAgentId=' + row.applyAgentId,
        type: 'get',
      }, function (res) {
        vm.loading = false;
        if (res.success) {
          try {
            vm.stopAgentForm.code = res.content.aeaProjApplyAgent.agreementCode;
          } catch (e) {
          }
          vm.stopAgentDialogVisible = true;
        } else {
          vm.$message.error(res.message || '获取代办详情数据失败');
        }
      }, function () {
        vm.loading = false;
        vm.$message.error('获取代办详情数据失败');
      });
    },
    // 下载协议
    downLoadAgreement: function () {
      window.open(ctx + 'aea/proj/apply/agent/getAgencyAgreement?applyAgentId=' + this.applyAgentId);
    },
    closePrePdf: function () {
    },
    closeStopAgentDialog: function () {
    },
    //刷新列表
    fetchTableData: function () {

      var ts = this;
      this.searchFrom.pagination.pages = this.searchFrom.pagination.page;
      ts.loading = true;

      request('', {
        url: ctx + 'rest/conditional/query/listAgencyDoTasks',
        type: 'post',
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
    toApplyPage: function (row) {
      var menuName = '';
      var menuInnerUrl = '';
      var id = 'menu_' + new Date().getTime();
      menuName = row.projName;
      menuInnerUrl = ctx + '/apanmis/page/stageApplyIndex?applyAgentId=' + row.applyAgentId;
      menuInnerUrl += '&isApplyAgent=true';
      menuInnerUrl += '&projInfoId=' + row.projInfoId;
      var data = {
        'menuName': menuName,
        'menuInnerUrl': menuInnerUrl,
        'id': id,
      };
      try {
        parent.vm.addTab('', data, '', '');
      } catch (e) {
        window.open(menuInnerUrl, '_blank');
      }
    },

    //格式化代办状态
    formatAgentApplyState: function (row, column, cellValue, index) {
      if (!cellValue || cellValue == null) {
        return '-'
      }
      var stateNames = ['-', '待签订', '签订中', '待申请人签章', '代办中', '拒绝代办', '窗口终止', '代办终止', '窗口办结', '代办结束'];
      return stateNames[cellValue];
    },
    // 显示委托代办阶段提示信息
    showStageInfo: function (row) {
      var html = '';
      if (row.agentStageState) {
        var stages = row.agentStageState.split(',');
        var name = '';
        for (var i in stages) {
          name += this.statgeNames[stages[i]] + '，';
        }
        name = name.substring(0, name.length - 1);
        html = html + "委托代办阶段 : " + name + "</br></br>";
      }
      return html;
    },
    agentStageStateFormatter: function (row) {
      if (row.agentStageState) {
        var stages = row.agentStageState.split(',');
        var showName = '';
        var html = "<div class=\"flow_steps\">" +
          "<ul class='stage'>";
        for (var i = 1; i <= 4; i++) {
          var clas = '';
          var content = '&nbsp;'
          for (var j = 0; j < stages.length; j++) {
            var stage = stages[j];
            if (stage == i) {
              if (showName == '') {
                showName = this.statgeNames[i];
                content = showName;
              }
              clas = 'current current-stage-min-width'
            }
          }
          html += "<li class=" + clas + ">" + content + "</li>";
        }
        html += "</ul>" +
          "</div>";
        return html;
      }
      return "-";
    },
    //签订协议按钮
    showOperateBtn1: function (row) {
      return ('1' === row.agentApplyState);
    },
    //查看按钮
    showOperateBtn2: function (row) {
      return ('2' === row.agentApplyState || '3' === row.agentApplyState || '5' === row.agentApplyState
        || '6' === row.agentApplyState || '7' === row.agentApplyState || '8' === row.agentApplyState || '9' === row.agentApplyState);
    },
    //办理、办结按钮
    showOperateBtn3: function (row) {
      return ('4' === row.agentApplyState);
    },
    //代办状态
    queryAgencyState: function () {
      var ts = this;
      request('', {
        url: ctx + 'rest/conditional/query/agencyState/list',
        type: 'get',
        data: {}
      }, function (res) {
        if (res.success) {
          ts.agentApplyStateMap = res.content;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 简单加密
    compile: function (code) {
      var c = String.fromCharCode(code.charCodeAt(0) + code.length);
      for (var i = 1; i < code.length; i++) {
        c += String.fromCharCode(code.charCodeAt(i) + code.charCodeAt(i - 1));
      }
      return escape(c);
    },

    // 跳转代办详情 签订与查看
    toAgencyDetail: function (row, type) {
      var _url = ctx + 'aea/proj/apply/agent/agencyProjDetail?applyAgentId=' + row.applyAgentId + '&type=';
      type == 'view' ? _url += this.compile('view') : _url += this.compile('do');
      var tabsData = {
        menuName: '代办详情',
        id: 'agency_detail' + row.applyAgentId,
        menuInnerUrl: _url,
      }
      try {
        //获取父window vue对象
        var parentVue = window.parent.vm
        parentVue.addTab('', tabsData, 'MENU_', parentVue.leftMenuActive++);
      } catch (err) {
        top.postMessage(tabsData, '*')
      }
    },

    // 办结dialog相关
    banjie: function (row) {
      this.curBanjieRow = row;
      this.isShowBanjieDialog = true;
    },
    // 查看办结协议
    viewBanjiePdf: function () {
      var ts = this;
      if (!this.endAgentForm.agreementEndTime) return this.$message.error('请选择办结日期');
      var url = ctx + 'preview/pdfjs/web/viewer.html?file=';
      var _url = ctx + 'aea/proj/apply/agent/getAgencyEndAgreementFile';
      _url += '?applyAgentId=' + this.curBanjieRow.applyAgentId;
      _url += '&agreementEndTime=' + formatDate(ts.endAgentForm.agreementEndTime, 'yyyy-MM-dd');
      url += encodeURIComponent(_url);
      this.prePdfVisible = true;
      this.pdfSrc = url;
    },
  },
  created: function () {
    this.conditionalQueryDic();
    this.fetchTableData();
    this.queryAgencyState();
  },
  mounted: function () {
    var _that = this;
    window.addEventListener('resize', function (ev) {
      _that.curWidth = (document.documentElement.clientWidth || document.body.clientWidth);
      _that.curHeight = (document.documentElement.clientHeight || document.body.clientHeight);
    });
  },
});