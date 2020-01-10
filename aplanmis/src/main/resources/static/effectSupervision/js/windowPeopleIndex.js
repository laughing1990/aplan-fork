var Index = new Vue({
  el: '#windowPeopleIndex',
  mixins: [mixins],
  data: function () {
    return {
      // 工作台数据
      statusCardData: [{
          name: '项目数',
          count: 0,
          select: false,
          key: 'xiangMu',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: 6,
          state: null
        },
        {
          name: '待办任务',
          count: 0,
          select: false,
          key: 'daiBanRenWu',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: 6,
          state: null
        },
        {
          name: '已办任务',
          count: 0,
          select: false,
          key: 'yiBanRenWu',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: 6,
          state: null
        }, {
          name: '网上待预审',
          count: 0,
          select: false,
          key: 'wangShangDaiYuShen',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: 0,
          state: null
        },
        {
          name: '待补全',
          count: 0,
          select: false,
          key: 'daiBuQuanShenBao',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: 3,
          state: null
        },
        {
          name: '补全待确认',
          count: 0,
          select: false,
          key: 'buQuanDaiQueRenShenBao',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: 4,
          state: null
        },
        {
          name: '不予受理',
          count: 0,
          select: false,
          key: 'buYuShouLiShenBao',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: 5,
          state: null
        },

        {
          name: '申报预警',
          count: 0,
          select: false,
          key: 'shenBaoShiXianYuJing',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: null,
          state: '2'
        },
        {
          name: '申报逾期',
          count: 0,
          select: false,
          key: 'shenBaoShiXianYuQi',
          itemCode: '',
          jumpUrl: '',
          jumpTabId: '',
          applyinstState: null,
          state: '3'
        },
      ],
      // 工作台状态选中
      statusCard: {},

      // 通知公告数据
      noticeList: [],
      // 通知公告-总数
      noticeTotal: 0,
      // 通知公告查询数据
      noticeCheckData: {
        // businessFlag: 1,
        businessFlag: 'busiTypeDefault',
        pageSize: 10,
        pageNum: 1,
      },

      // 个人消息相关
      // 个人消息数据
      personMsgList: [],
      personMsgTotal: 0,
      // 个人消息查询数据
      personMsgCheckData: {
        pageSize: 10,
        pageNum: 1,
      },

      // 项目列表展示表格
      projectTableList: [],
      // 项目列表查询数据
      projectCheckData: {
        // pageNum: 1,
        // pageSize: 5,
        pagination:{
          page:  1,
          pages: 1,
          perpage: 5
        },
        applyinstState: '',
        state: '',
        keyword: ''
      },
      // 项目列表的总数
      projectTotal: 0,

      // 待办列表
      needHandelList: [],
      // 待办列表查询数据
      needHandelCheckData: {
        pageSize: 5,
        pageNum: 1,
        keyword: '',
        sort: 'taskId',
        order: 'asc',
      },
      // 待办列表list-total
      needHandelTotal: 0,

      // 待预审列表
      needYushenList: [],
      // 待预审查询数据
      needYushenCheckData: {
        pageSize: 5,
        pageNum: 1,
        keyword: ''
      },
      // 待预审总数
      needYushenTotal: 0,

      // 补全待确认
      correctNeedSureList: [],
      // 补全待确认-查询参数
      correctNeedSureCheckData: {
        pageSize: 5,
        pageNum: 1,
        keyword: ''
      },
      // 补全待确认-total
      correctNeedSureTotal: 0,

      // 预审操作视图id
      yushenViewId: '',

      // 左侧菜单-并联申报tab索引
      parallelDeclarationTabData: {},

      // 待办列表-消息相关
      isShowMsgDetail: false,
      msgDetail: {
        remindContent: '',
        sendUserName: '',
        sendDate: ''
      }
    }
  },
  methods: {
    // 公共方法
    // 获取某个url下面的某个参数（字符串版）
    getSerachParamsForUrlStr: function (url, name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
      var _search = '?' + ((url.split('?'))[1])
      var r = _search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },

    // 获取工作台的所有状态的数据
    fetchAllStateNum: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/win/portal/count',
        type: 'get',
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            for (var i = 0, len = ts.statusCardData.length; i < len; i++) {
              var att = ts.statusCardData[i].key
              ts.statusCardData[i].count = res.content[att] || 0;
            }
          }
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },
    // 获取工作台所有状态下的视图viewCode
    fetchAllStateViewCode: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/win/portal/getDicViewCode',
        type: 'get',
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            for (var i = 0, len = ts.statusCardData.length; i < len; i++) {
              ts.statusCardData[i].name = res.content[i].itemCode;
              ts.statusCardData[i].itemCode = res.content[i].itemName;
              // ts.statusCardData[i].jumpUrl = ctx + 'bpm/front/view/index.html?viewCode=' +  res.content[i].itemName;
              ts.statusCardData[i].jumpUrl = ctx + res.content[i].itemName;
              ts.statusCardData[i].jumpTabId = res.content[i].itemId;
            }
          }
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },
    // 工作台选中状态
    selectStateForWorkbench: function (item) {
      var ts = this;
      this.statusCardData.forEach(function (i) {
        i.select = false;
      })
      item.select = true;
      this.statusCard = item;
      console.log(parent.vm.leftData)
      // 左侧菜单栏-所有数据
      var _allNavList = parent.vm.leftData;
      var _index = -1; //当前工作台选中数据在菜单中的索引
      // console.log(item.itemCode)
      // for (var i = 0, len = _allNavList.length; i < len; i++) {
      //   var _curViewCode = $.trim(ts.getSerachParamsForUrlStr(_allNavList[i].menuInnerUrl, 'viewCode'))
      //   if (_curViewCode && _curViewCode == item.itemCode) {
      //     _index = i;
      //     break;
      //   }
      // }
      for (var i = 0, len = _allNavList.length; i < len; i++) {
        var _curViewUrl = (_allNavList[i].menuInnerUrl).replace(/\s/g, "");
        if (_curViewUrl && _curViewUrl.indexOf(item.itemCode) > -1) {
          _index = i;
          break;
        }
      }
      
      if (_index > -1) {
        parent.vm.addTab('', parent.vm.leftData[_index], parent.vm.activeTabIframe, _index);
      } else {
        var _jumpData = {
          'menuName': item.name,
          'menuInnerUrl': item.jumpUrl,
          'id': item.jumpTabId
        };
        parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
      }

    },
    // 获取项目列表的表格数据
    fetchProjectTableList: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'aea/proj/info/conditionalQueryAeaProjInfo',
        type: 'get',
        data: ts.projectCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            ts.projectTableList = res.content.list;
            ts.projectTotal = res.content.total;
          }
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },
    // 项目列表的表格-换页
    projectHandleCurrentChange: function (val) {
      // this.projectCheckData.pageNum = val;
      this.projectCheckData.pagination.page = val;
      this.projectCheckData.pagination.pages = val;
      this.fetchProjectTableList();
    },

    // 获取通知公告数据
    fetchNoticeListData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/aoa/notice/content/getPageAoaNoticeContentCascade',
        type: 'get',
        data: ts.noticeCheckData
      }, function (res) {
        ts.loading = false;
        if (res.success && res.content != null) {
          ts.noticeList = res.content.rows;
          ts.noticeTotal = res.content.total;
        }
      }, function (msg) {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },
    // 查看更多的通知公告数据
    moreNoticeList: function (command) {
      // console.log(command)
      var url = ctx + 'rest/aoa/notice/content/viewNotice?businessFlag=busiTypeDefault';
      if(command && command.contentId){
        url = url + "&contentId=" + command.contentId
      }
      try {
        var tabsData = {
          menuName: '我的公告',
          id: 'gonggao',
          menuInnerUrl: url,
          menuSeq: '.13a3cab5-8120-4031-9b4d-a8ffa39dc9fa.131ae63c-58b8-4de9-aacf-6e927c17f919.'
        }
        // console.log(tabsData.menuInnerUrl)
        try {
          //获取父iframe vue对象
          var parentVue = window.parent.vm
          parentVue.addTab('', tabsData, 'MENU_', parentVue.leftMenuActive++);
        } catch (err) {
          top.postMessage(tabsData, '*')
        }
      } catch (e) {
        window.open(url, '_blank');
      }
    },

    // 获取个人消息数据
    fetchPersonalMsgData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/aoa/msg/range/getPageAoaMsgRangeForMyReceive',
        type: 'post',
        data: ts.personMsgCheckData
      }, function (res) {
        ts.loading = false;
        if (res.success && res.content != null) {
          ts.personMsgList = res.content.rows;
          ts.personMsgTotal = res.content.total;
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },
    // 个人消息查看更多数据-跳转页面
    moreMassageList: function (command) {
      console.log(command)
      var url = ctx + 'aoa/msg/index';
      if(command && command.rangeId){
        url = url + "?rangeId=" + command.rangeId
      }
      try {
        var tabsData = {
          menuName: '我的消息',
          id: 'wodexiaoxi',
          menuInnerUrl: url,
        }
        try {
          //获取父iframe vue对象
          var parentVue = window.parent.vm
          parentVue.addTab('', tabsData, 'MENU_', parentVue.leftMenuActive++);
        } catch (err) {
          top.postMessage(tabsData, '*')
        }
      } catch (e) {
        window.open(url, '_blank');
      }
    },

    // 待办列表-相关
    // 待办列表-双击某一行
    needHandelListRowDbFn: function(row){
      this.daibanbanliHand(row);
    },
    // 待办列表-剩余时间排序
    needHandelListSurplusSort: function(obj){
      if(obj.order == 'descending'){
        this.needHandelCheckData.sort = "desc"
      }else{
        this.needHandelCheckData.sort = "asc"
      }
      this.fetchNeedHandelList();
    },
    // 待办列表-切换页
    needHandelListCurrentChange: function (val) {
      this.needHandelCheckData.pageNum = val;
      this.fetchNeedHandelList();
    },
    // 获取待办列表表格数据
    fetchNeedHandelList: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/win/portal/listDaiBan',
        type: 'get',
        data: ts.needHandelCheckData
      }, function (res) {
        ts.loading = false;
        if (res.success && res.content != null) {
          ts.needHandelList = res.content.list;
          ts.needHandelTotal = res.content.total;
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },

    // 待预审列表-相关
    // 待预审列表-双击某一行
    needYushenListRowDbFn: function(row){
      this.yushen(row);
    },
    // 待预审列表-切换页
    needYushenListCurrentChange: function (val) {
      this.needYushenCheckData.pageNum = val;
      this.fetchNeedYushenList();
    },
    // 获取待预审列表表格数据
    fetchNeedYushenList: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/win/portal/listWangShangDaiYuShen',
        type: 'get',
        data: ts.needYushenCheckData
      }, function (res) {
        ts.loading = false;
        if (res.success && res.content != null) {
          ts.needYushenList = res.content.list;
          ts.needYushenTotal = res.content.total;
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },

    // 补全待确认列表-相关
    // 补全待确认列表-双击某一行
    correctNeedSureListRowDbFn: function(row){
      this.wangshangdaiyushen(row);
    },
    // 补全待确认列表-切换页
    correctNeedSureCurrentChange: function (val) {
      this.correctNeedSureCheckData.pageNum = val;
      this.fetchCorrectNeedSureList();
    },
    // 获取补全待确认列表表格数据
    fetchCorrectNeedSureList: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/win/portal/listBuQuanDaiQueRen',
        type: 'get',
        data: ts.correctNeedSureCheckData
      }, function (res) {
        ts.loading = false;
        if (res.success && res.content != null) {
          ts.correctNeedSureList = res.content.list;
          ts.correctNeedSureTotal = res.content.total;
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },

    // 获取预审视图的viewId
    fetchViewId: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'rest/win/portal/queryView',
        type: 'get',
        data: {
          content: '网上待预审'
        }
      }, function (res) {
        ts.loading = false;
        if (res.success && res.content != null) {
          ts.yushenViewId = res.content.viewId;
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.loading = false;
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
        ts.apiMessage('服务请求失败', 'error')
      });
    },

    // 预审操作-网上待预审+补全待确认的查看
    yushen: function (row) {
      var taskId = row.taskId,
        busRecordId = '',
        url = ctx + '/apanmis/page/stageApproveIndex?taskId=' + taskId + '&viewId=' + this.yushenViewId + '&busRecordId=' + busRecordId;
      if (window.frames.length != parent.frames.length) {
        window.parent.open(url, '_blank')
      } else {
        window.open(url, '_blank');
      }
    },

    // 网上待预审操作
    wangshangdaiyushen: function(row){
      var url = ctx+'/apanmis/page/stageApproveIndex?taskId='+row.taskId+'&viewId='+row.viewId;
      if(row.busRecordId){
          url = url + '&busRecordId='+row.busRecordId;
      }
      window.open(url,'_blank');
    },

    // 项目列表-并联申报操作
    jumpParallelDeclaration: function (row) {
      var _url = this.parallelDeclarationTabData.menuInnerUrl;
      // _url.indexOf('?') === -1 ?
      //   _url = _url + '?localCode=' + row.localCode + '&projInfoId=' + row.projInfoId :
      //   _url = _url + "&localCode=" + row.localCode + '&projInfoId=' + row.projInfoId 
      var _jumpData = {
        'menuName': '并联申报',
        'menuInnerUrl': ctx+'apanmis/page/stageApplyIndex',
        'id': row.projInfoId + "_parallelDecal",
        'localCode':  row.localCode,
        'projInfoId': row.projInfoId
      };
      parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
    },
    getParallelDeclareTab: function () {
      var _allNavList = parent.vm.leftData;
      for (var i = 0, len = _allNavList.length; i < len; i++) {
        var _menuName = (_allNavList[i].menuName).replace(/\s/g, "");
        if (_menuName && _menuName.indexOf('并联申报') > -1) {
          this.parallelDeclarationTabData = _allNavList[i];
          break;
        }
      }
    },

    // 项目列表-项目详情（全局库）
    jumpProjectDetail: function(row){
      var _jumpData = {
        'menuName': '项目详情',
        'menuInnerUrl':ctx + 'rest/project/detail?projInfoId=' + row.projInfoId,
        'id':  row.projInfoId
      };
      parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
    },

    // 项目列表-全景图操作
    jumoQuanjingtu: function(row){
      var _url = ctx + "rest/project/diagram/status/projInfo?projInfoId=" + row.projInfoId
      if (window.frames.length != parent.frames.length) {
        parent.open(_url, '_blank')
      }else{
        window.open(_url, '_blank')
      }
    },

    // 补全确认列表跳转补全确认详情
    buquanqueren: function (row) {
      // var _jumpData = {
      //   'menuName': '材料补全确认',
      //   'menuInnerUrl': ctx + 'applyinst/correct/getItemCorrectRealIninst?applyinstCorrectId=' + row.applyinstCorrectId,
      //   'id': row.applyinstCorrectId
      // };
      // parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');

      var correctId = row.applyinstCorrectId;
      var urltemp = ctx + '/applyinst/correct/applyinstConfirm.html?applyinstCorrectId=' + correctId;
      var parentIfreamUrl = window.frames.location.href;
      var currentTabId = parent.vm.activeTabIframe;
      urltemp = urltemp + '&parentIfreamUrl=' + parentIfreamUrl + '&currentTabId' + currentTabId;
      var data = {
        'menuName': '材料补全确认',
        'menuInnerUrl': urltemp,
        'id': 'dqr' + correctId
      };
      try {
        parent.vm.addTab('', data, '', '');
      } catch (e) {
        window.open(urltemp, '_blank');
      }
    },

    // 待办列表中的消息处理
    setRemindMessageRead: function (row, remindReceiverId) {
      var ts = this;
      request('', {
        url: ctx + '/rest/conditional/query/updateRemindRead',
        type: 'get',
        data: {
          'remindReceiverIds': remindReceiverId
        }
      }, function (res) {
        if (res.success) {
          for (var i = 0; i < row.remindList.length; i++) {
            if (row.remindList[i].remindReceiverId == remindReceiverId) {
              row.remindList.splice(i, 1);
              break;
            }
          }
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    showRemindInfo: function (row, remindInfo) {
      this.setRemindMessageRead(row, remindInfo.remindReceiverId);
      this.msgDetail.remindContent = remindInfo.remindContent;
      this.msgDetail.sendUserName = remindInfo.sendUserName;
      this.msgDetail.sendDate = this.formatDatetimeCommon(remindInfo.sendDate, 'yyyy-MM-dd hh:mm');
      this.isShowMsgDetail = true;
    },

    // 项目数表格加上树结构
    load: function(row, treeNode, resolve) {
      var ts = this;
      // ts.loading = true;
      request('', {
          url: ctx + 'aea/proj/info/listChildProjInfoByProjInfoId',
          type: 'get',
          data: {projInfoId:row.projInfoId}
      }, function (res) {
          // ts.loading = false;
          if (res.success) {
              resolve(res.content);
          } else {
              return ts.apiMessage(res.message, 'error')
          }
      }, function () {
          // ts.loading = false;
          return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 待办列表中的办理操作
    daibanbanliHand: function(row){
      var url = ctx+'apanmis/page/stageApproveIndex?taskId='+row.taskId+'&viewId='+row.viewId + '&itemNature=' + row.itemNature;
      if(row.busRecordId){
          url = url + '&busRecordId='+row.busRecordId;
      }
      window.open(url,'_blank');
    },

    // 待办列表中的签收操作
    signTask: function (event, row) {
      var ts = this;
      var taskId = row.taskId;
      var viewId = row.viewId;
      var busRecordId = row.busRecordId;
      event.preventDefault();
      ts.loading = true;
      request('bpmFrontUrl', {
        url: ctx + 'rest/front/task/signTask/' + taskId,
        type: 'get',
        data: {}
      }, function (result) {
        ts.loading = false;
        if (result.success) {
          ts.$message.success(result.message);
          window.setTimeout(function () {
            window.open(ctx + 'apanmis/page/stageApproveIndex?taskId=' + taskId + '&viewId=' + viewId + '&busRecordId=' + busRecordId + '&itemNature=' + row.itemNature, '_blank');
            window.location.reload();
          }, 500);
        } else {
          ts.$message.error(result.message);
        }
      }, function () {
        ts.loading = false;
        ts.$message.error("签收失败！");
      });
    },

    // 计算待办列表第一列的宽度
    getFirstColumnWidth: function () {
      if (this.needHandelList) {
        for (var i = 0; i < this.needHandelList.length; i++) {
          if (this.needHandelList[i].isGreenWay == '1') {
            return '90';
          }
        }
      }
      return '65';
    }
  },
  mounted: function () {
    var ts = this;
    setTimeout(function () {
      ts.fetchNoticeListData();
      ts.fetchPersonalMsgData();
    }, 100)
    // 获取左侧并联申报菜单
    this.getParallelDeclareTab()
  },
  created: function () {
    this.statusCard = this.statusCardData[0];
    this.fetchProjectTableList();
    this.fetchNeedHandelList();
    this.fetchNeedYushenList();
    this.fetchCorrectNeedSureList();
    this.fetchAllStateNum();
    this.fetchAllStateViewCode();
    this.fetchViewId();
  },
  filters: {
    formatterTime: function (val, format) {
      if (!val) return '暂无';
      return formatDate(new Date(val), format)
    },
  },
})