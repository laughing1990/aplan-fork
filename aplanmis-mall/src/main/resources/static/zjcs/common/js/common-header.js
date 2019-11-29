/*
 * @Author: ZL
 * @Date:   2019/05/27
 * @Last Modified by:   anchen
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#headerFooter',
  data: function () {
    return {
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight), //当前屏幕高度
      activeName: 0,
      topTabData: [{
        label: '首页',
        id: '1',
        url: '/aplanmis-mall/supermarket/main/index.html'
      }, {
        label: '中介服务机构',
        id: '2',
        url: '/aplanmis-mall/supermarket/main/imUnits.html'
      }, {
        label: '中介服务事项',
        id: '3',
        url: '/aplanmis-mall/supermarket/main/imServices.html'
      }, {
        label: '采购公告',
        id: '4',
        url: '/aplanmis-mall/supermarket/main/procurementNotice/index.html'
      },{
        label: '中选公告',
        id: '7',
        url: '/aplanmis-mall/supermarket/main/beSelectionNotice/index.html'
      }, {
        label: '超市指南',
        id: '5',
        url: '/aplanmis-mall/supermarket/main/guide.html'
      }, {
        label: '通知公告',
        id: '6',
        url: '/aplanmis-mall/supermarket/main/notice.html'
      }],
      selectOptions: [{
        label: '中介机构',
        value: '1',
      }, {
        label: '中介服务',
        value: '2',
      }, {
        label: '服务事项',
        value: '3',
      }],
      selectOption: '1',

      // 页面loading
      hloading: false,
      // 是否已经登陆
      isLoginFlag: false,
      // 当前登陆的信息
      curLoginInfo: {},
      seachKey: '',

      // 切换单位-当前选择的单位列表的unitInfoId
      curChangeUnitInfoId: '',
      // 当前登陆身份显示的用户名
      curLoginShowName: '',
      othersLinkList: [{
        label: '—— 各级政府 ——',
        id: '1',
        value: '',
        options: [{
          name: '中华人民共和国中央人民政府门户网站',
          id: '1-1',
          value: '1',
          url: 'http://www.gov.cn/'
        },{
          name: '辽宁人民政府',
          id: '1-2',
          value: '2',
          url: 'http://www.ln.gov.cn/'
        },{
          name: '沈阳市人民政府',
          id: '1-3',
          value: '3',
          url: 'http://www.shenyang.gov.cn/'
        }]
      },{
        label: '—— 媒体网站 ——',
        id: '2',
        value: '',
        options: [{
          name: '沈阳网',
          id: '2-1',
          value: '4',
          url: 'http://www.syd.com.cn/'
        },{
          name: '新华网',
          id: '2-2',
          value: '5',
          url: 'http://www.xinhuanet.com/'
        },{
          name: '人民网',
          id: '2-3',
          value: '6',
          url: 'http://www.people.com.cn/'
        },{
          name: '新浪网',
          id: '2-3',
          value: '7',
          url: 'http://www.sina.com.cn/'
        }]
      },{
        label: '—— 友情链接 ——',
        id: '3',
        value: '',
        options: [{
          name: '沈阳政务服务中心',
          id: '3-1',
          value: '8',
          url: 'http://www.sysp.gov.cn/'
        }]
      }],
    }
  },
  mounted: function () {
    this.activeName = parent.vm.activeName;
    parent.window.onscroll = function () { // 定义窗口大小变更通知事件
      parent.vm.isScroll = parent.document.body.scrollHeight > (parent.window.innerHeight || parent.document.documentElement.clientHeight);
      if (!parent.vm.isScroll) {
        parent.$('.footer').attr('class', 'footer fixed-bottom')
      } else {
        parent.$('.footer').attr('class', 'footer')
      }
    };
    setTimeout(function () {
      parent.vm.isScroll = parent.document.body.scrollHeight > (parent.window.innerHeight || parent.document.documentElement.clientHeight)
      if (!parent.vm.isScroll) {
        parent.$('.footer').attr('class', 'footer fixed-bottom')
      } else {
        parent.$('.footer').attr('class', 'footer')
      }
    }, 300)
    this.init();
  },
  methods: {
    changeLinkUrl: function (url) {
        window.open(url);
    },
    openNewUrl: function (url) {
      window.parent.location.href = url;
    },
    login: function () {
      window.parent.location.href = ctx + 'supermarket/main/login.html';
    },
    register: function () {
      window.parent.location.href = ctx + 'supermarket/agentRegister/index.html';
    },
    init: function () {
      var _curLoginInfo = JSON.parse(localStorage.getItem('loginInfo'));
      if (_curLoginInfo) {
        this.isLoginFlag = true;
        this.curLoginInfo = _curLoginInfo;
        // 当前登陆展示的用户名
        if(_curLoginInfo.isPersonAccount == 1){
          this.curLoginShowName = _curLoginInfo.personName;
        }else{
          _curLoginInfo.unitName 
            && (this.curLoginShowName = _curLoginInfo.unitName.length>20? _curLoginInfo.unitName.slice(0,20) + '...':  _curLoginInfo.unitName );
        }
      } else {
        this.isLoginFlag = false;
      }
    },
    // 登出
    loginOut: function () {
      var ts = this;
      // confirmMsg('', '您确定退出当前登录吗？', function () {     
      ts.hloading = true;
      request('', {
        url: ctx + '/supermarket/main/logout',
        type: 'get',
      }, function (res) {
        ts.hloading = false;
        if (res.success) {
          localStorage.removeItem('loginInfo');
          ts.$message({
              message: '退出成功！',
              type: 'success'
          });
          window.parent.location.href = ctx + '/supermarket/main/index.html';
        } else {
          ts.$message({
            message: '退出失败',
            type: 'error'
          });
        }
      }, function (msg) {
        ts.hloading = false;
        ts.$message({
          message: '退出失败',
          type: 'error'
        });
      });
      // })

    },
    // 切换单位
    switchUnit: function (x, y, unitInfos) {
      var ts = this;
      var _unitDropBox = document.createElement('div'); //创建一个企业下拉列表

      // 定义企业下拉选择的样式-位置
      var _unitDropBoxStyle = {
        left: x,
        top: y
      };
      $(_unitDropBox).show().addClass('unit-drop-box').css(_unitDropBoxStyle);

      // 单位列表渲染
      var _unitDropHtml = "<ul>";
      for (var i = 0; i < unitInfos.length; i++) {
        _unitDropHtml += "<li data-unitinfoid='" + unitInfos[i].unitInfoId + "'data-isimunit='" + unitInfos[i].isImUnit + "'data-isownerunit='" + unitInfos[i].isOwnerUnit + "'>" + unitInfos[i].applicant + "</li>"
      }
      _unitDropHtml += '</ul>'
      $(_unitDropBox).html(_unitDropHtml);

      $(parent.window.document.body).append(_unitDropBox);

      // 点击单位list的单位
      $(_unitDropBox).find('ul li').off().on('click', function () {
        // console.log($(this).data())
        var _curSelectUnitObj = $(this).data();
        if (_curSelectUnitObj.isownerunit == 1 && _curSelectUnitObj.isimunit == 1) {
          ts.curChangeUnitInfoId = _curSelectUnitObj.unitinfoid;
          ts.showImAndOwnerSelectDialog();
        } else {
          var _sendData = {};
          _sendData.unitInfoId = _curSelectUnitObj.unitinfoid;
          if (_curSelectUnitObj.isownerunit == 1) {
            _sendData.isOwner = 1;
          }
          if (_curSelectUnitObj.isimunit == 1) {
            _sendData.isOwner = 0;
          }
          ts.sendUnitSelectToApi(_sendData);
        }
      })
      $(_unitDropBox).on('mouseleave', function () {
        $(this).remove();
      })
    },
    // 切换单位-当前选择单位为 中介机构而且是业主
    showImAndOwnerSelectDialog: function () {
      var ts = this,
      _parentWin = $(parent.window.document.body),    //父窗口body
      _sendData = {};
      var _dialogBox = `
      <div class="unit-change-dialog header__unit_change_dialog">
        <div class="unit-content clearfix">
          <div class="unit-im unit-block" data-isowner='0'></div>
          <div class="unit-owner unit-block" data-isowner='1'></div>
          <div class="unit-close">
            <i class="el-icon-close"></i>
          </div>
        </div>
      </div>
      `
      _parentWin.append(_dialogBox);
      _parentWin.find('.unit-change-dialog .unit-block').on('click',function(){
        _sendData.unitInfoId = ts.curChangeUnitInfoId;
        _sendData.isOwner = $(this).data('isowner');
        ts.sendUnitSelectToApi(_sendData);
      });
      _parentWin.find('.unit-change-dialog .unit-close').on('click',function(){
        $(this).parents('.header__unit_change_dialog').remove();
      })
    },
    // 切换单位-api
    sendUnitSelectToApi: function(data){
      if(!data)return;
      var ts = this;
      // console.log(data);
      // return;
      ts.hloading = true;
      request('', {
        url: ctx + 'supermarket/main/changeLoginUnitInfo',
        type: 'post',
        data: data
      }, function (res) {
        ts.hloading = false;
        if (res.success) {
          localStorage.setItem('loginInfo', JSON.stringify(res.content));
          window.parent.location.href = res.content.isOwner == "1" ? ctx + "/supermarket/main/ownerCenter.html" : ctx + "/supermarket/main/agentCenter.html";
          // $(parent.window.document.body).find('.unit-change-dialog').remove(); //切换成功后删掉父窗口的这个dialog节点

          ts.$message({
            message: '切换成功！',
            type: 'success'
          });
        } else {
          ts.$message({
            message: res.message,
            type: 'error'
          });
        }
      }, function (msg) {
        ts.hloading = false;
        ts.$message({
          message: '切换失败!',
          type: 'error'
        });
      });
    },
    // 切换单位-获取单位数据
    switchUnitFetchList: function ($event) {
      var ts = this,
        _showX = '',
        _showY = '',
        _scrollX = '',
        _curLoginInfo = {},
        _unitInfos = [];
      _curLoginInfo = JSON.parse(localStorage.getItem('loginInfo'));
      if (_curLoginInfo.unitInfos && _curLoginInfo.unitInfos.length) {
        _unitInfos = _curLoginInfo.unitInfos;
      }
      _scrollX = parent.window.document.documentElement.scrollLeft || parent.window.document.body.scrollLeft;
      _showX = $event.pageX - _scrollX - 140;
      _showY = $event.pageY + 20;
      ts.switchUnit(_showX, _showY, _unitInfos);
    },

    // 点击企业名，个人名，回到个人中心页
    jumpToPeopleCenter: function(){
      var _curLoginInfo = {};
        _curLoginInfo = JSON.parse(localStorage.getItem('loginInfo'));
        window.parent.location.href = _curLoginInfo.isOwner == "1" 
          ? ctx + "/supermarket/main/ownerCenter.html" 
          : ctx + "/supermarket/main/agentCenter.html";
    },
    // 搜索跳转页面
    switchPage:function(){
      if(this.selectOption=="1"){
        this.openNewUrl("/aplanmis-mall/supermarket/main/imUnits.html?tab=0&seachKey="+this.seachKey);
      }else if(this.selectOption=="2"){
        this.openNewUrl("/aplanmis-mall/supermarket/main/imUnits.html?tab=1&seachKey="+this.seachKey);
      }else{
        this.openNewUrl("/aplanmis-mall/supermarket/main/imServices.html?seachKey="+this.seachKey);
 
      }
      
    }
  }
});