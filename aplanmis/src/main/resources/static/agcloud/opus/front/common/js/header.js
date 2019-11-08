/*
* @Author: ZL
* @Date:   2019/4/1
* @Last Modified by:   ZL
* @Last Modified time: $ $
*/
var vm = new Vue({
  el: '#header',
  data: function(){
    var _that = this;
    var checkSame = function (rule, value, callback) {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== _that.editPasswordData.newPassword) {
        callback(new Error('两次输入密码不一致！'));
      } else {
        callback();
      }
    };
    var checkDiffer = function (rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (value == _that.editPasswordData.oldPassword) {
        callback(new Error('新密码和原密码不能相同！'));
      } else {
        callback();
      }
    };
    var checkOldPass = function (rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入原密码'));
      } else {
        request('opus-admin', {
          url: ctx+'opus/front/om/users/passwordCheckout',
          data: {oldPassword: value},
          type: 'get'
        }, function (data) {
          if(data.success){
            callback();
          }else {
            callback(new Error(data.message ? data.message : '原密码错误'));
          }
        })
      }
    };
    return {
      headerData: [], // 头部菜单所有data
      leftData: [],  // 左侧菜单data
      headerActive: 0,  // 头部菜单active状态
      eleLiLength: '', // 菜单的长度
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      showMenuMore: false,  // 展示更多菜单默认隐藏
      menuCount: 1, // 头部菜单展示条数 menuCount+1
      loginName: '', // 用户登陆名
      userName: '', // 用户名
      userId: '',  // 用户id
      hoverCheck: -1, // 左侧子菜单hover状态
      menuTop: '', // 左侧子菜单hover状态子级展示top
      activeTabIframe: '', // 顶部选项卡 绑定active
      activeTabData: [], // 顶部选项卡 所有展示的tab
      tabIndex: [], // 顶部选项卡 所有展示的tab的key
      hideHeaderData: [], // 头部菜单展示data
      headerDataShow: [], // 头部菜单隐藏data
      showMoreItem: false, // 是否展示隐藏的头部菜单
      leftMenuActive:0, // 左侧菜单active
      editUserInfoFlag: false, // 是否展示个人信息topbar
      showUserInfo: false, // 是否展示个人信息弹窗
      editPasswordFlag: false, // 是否展示修改密码弹窗
      editPasswordData: {
        oldPassword:'',
        newPassword: '',
        newPasswordCheck: ''
      }, // 修改密码data
      newPasswordCheck: '', // 再次确认新密码
      editPasswordRule: {
        oldPassword: [
          { required: true, validator: checkOldPass, trigger: 'blur' },
        ],
        newPassword: [
          { validator: checkDiffer, required: true, trigger: 'blur' }
        ],
        newPasswordCheck: [
          { validator: checkSame, required: true,trigger: 'blur' }
        ]
      },  // 修改密码校验
      topOrgId: '',
      userSex: '',
      userEmail: '',
      showTabOpt: false,
      tabOptTop: '',
      tabOptleft: '',
      tabId: '',
      tabKey: '',
      count:null,
      isFirstLoadMenu:true,
      testCount:null,
    }
  },/*
  created:function(){
      this.getViewDataCountByViewCode("view-code-00000011");
  },*/
  mounted: function () {
    var _that = this;
    _that.getUserIndo();
    window.addEventListener("resize", function() {
      _that.curWidth=document.documentElement.clientWidth;
      _that.curHeight = document.documentElement.clientHeight;
      _that.setHeaderShow();
    });
    //在document挂载onlick事件
    document.addEventListener("click",this.displayMenuPopover)
    // 监听子页面的传递过来的数据
    if(window.addEventListener){
      window.addEventListener('message',_that.onMessage,false);
    }else{
      if(window.attachEvent){
        window.attachEvent("onmessage", _that.onMessage);
      }
    };
    _that.getHeaderData();
    _that.getViewDataCountByViewCode("view-code-00000011");
    _that.getViewDataCountByViewCode("view-code-00000030");
    setInterval(function(){
      _that.getViewDataCountByViewCode("view-code-00000011");
      _that.getViewDataCountByViewCode("view-code-00000030");
    },30000)
  },
  methods: {
    getUserIndo: function () {
      var _that = this;
      request('opus-admin', {
        url: ctx+'opus/front/om/users/currOpusLoginUser',
        type: 'get',
      }, function (data) {
        _that.loginName = data.content.user.loginName;
        _that.userName = data.content.user.userName;
        _that.userId = data.content.user.userId;
        _that.topOrgId = data.content.currentOrgId
        _that.getHeaderData();
        _that.getUserAllInfo(data.content.user.loginName);
      }, function (msg) {
        console.log('服务请求失败')
        //alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    getHeaderData: function () {
      var _that = this;
      var props = {
        isTree: 'true',
        netName: '前端网络入口',
        tmnId: '1',
        topOrgId: _that.topOrgId,
        userId: _that.userId
      };
      var _that = this;
      request('opus-admin', {
        url: ctx+'opus/front/om/users/user/'+_that.userId+'/allMenus',
        type: 'get',
        data: props
      }, function (data) {
        _that.headerData = data.content;
        
        if(data.content!=null&&data.content.length>0){
          if(data.content[0].childrenList){
            _that.leftData = data.content[0].childrenList;
            
            // _that.leftData.map(function (item) {
            //     if(item.menuGovUrl.indexOf('view-code-00000011')!=-1){
            //         item.count = _that.testCount;
            //     }
            //
            //     if(item.menuGovUrl.indexOf('view-code-00000030')!=-1){
            //         item.count = _that.testCount;
            //     }
            // });
            _that.getViewDataCountByViewCode("view-code-00000011");
            _that.getViewDataCountByViewCode("view-code-00000030");
            if(_that.isFirstLoadMenu){
              _that.activeTabData.push({
                menuName: _that.leftData[0].menuName,
                id: _that.leftData[0].id,
                menuInnerUrl: _that.leftData[0].menuInnerUrl
              });
              _that.tabIndex.push(_that.leftData[0].id);
            }
          }else {
            _that.activeTabData.push({
              menuName: data.content[0].menuName,
              id: data.content[0].id,
              menuInnerUrl: data.content[0].menuInnerUrl
            });
            _that.tabIndex.push(data.content[0].id);
          }
          
          if(_that.isFirstLoadMenu){
            _that.activeTabIframe =  _that.activeTabData[0].id;
            $('.aside-left .menu-item-active').removeClass('menu-item-active');
            $('#'+_that.activeTabIframe).addClass('menu-item-active');
          }
          _that.isFirstLoadMenu = false;
        }
        _that.setHeaderShow();
      }, function (msg) {
        console.log()
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    getUserAllInfo: function(loginName){
      var _that = this;
      request('agx', {
        url: ctx + 'opus/front/om/users',
        type: 'get',
        data: {loginName: loginName}
      }, function (data) {
        _that.userEmail = data.content.userEmail;
        _that.userSex = data.content.userSex;
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    initMenuCount: function (eleLi, n) {
      this.headerDataShow=eleLi.slice(0,n+1);
      this.hideHeaderData = eleLi.slice(n+1)
    },
    logout: function (e) {
      e.cancelBubble = true;
      var _that = this;
      confirmMsg('', '确定要退出登陆吗？', function () {
        var w = window.open();
        request('opus-admin', {
          url: ctx+'opus/front/om/users/listLogoutUrl',
          type: 'get',
          data: {
            netName : '前端网络入口',
            orgId: _that.topOrgId,
            userId : _that.userId
          }
        }, function (data) {
          if(data.success){
            if(data.content.length>0){
              data.content.map(function(e){
                w.location = e;
                w.close();
              });
            }
            window.location.href = ctx+ 'logout';
          }
        }, function (msg) {
          alertMsg('', '服务请求失败', '关闭', 'error', true);
        });
      })
    },
    setHeaderShow: function () {  // 设置头部可展示菜单长度
      var _that = this;
      var logoWidth = $('#header .pro-logo').outerWidth(true);
      var topbarWidth = $('.topbar-login-info').outerWidth(true);
      var width = _that.curWidth - logoWidth - topbarWidth;
      if (_that.headerData !== null && _that.headerData.length > 0) {
        if (width <= 150 &&  _that.headerData.length > 0) {
          _that.showMenuMore = true;
          _that.menuCount = -1;
        }else if (width <= 360 && width > 150 && _that.headerData.length > 1) {
          _that.showMenuMore = true;
          _that.menuCount = 0;
        }else if (width <= 520 && width > 360 &&  _that.headerData.length > 2) {
          _that.showMenuMore = true;
          _that.menuCount = 1;
        } else if (width <= 680 && width > 520 && _that.headerData.length > 3) {
          _that.showMenuMore = true;
          _that.menuCount = 2;
        } else if (width <= 920 && width > 680 && _that.headerData.length > 4) {
          _that.showMenuMore = true;
          _that.menuCount = 3;
        } else if (width <= 1200 && width > 920 && _that.headerData.length > 6) {
          _that.showMenuMore = true;
          _that.menuCount = 5;
        } else if (width > 1200 && width <= 1320 && _that.headerData.length > 8) {
          _that.showMenuMore = true;
          _that.menuCount = 7;
        } else if (width > 1320 && _that.headerData.length > 9) {
          _that.showMenuMore = true;
          _that.menuCount = 8;
        } else {
          _that.showMenuMore = false;
          _that.menuCount = _that.headerData.length;
        }
      } else {
        _that.showMenuMore = false;
        _that.menuCount = _that.headerData.length;
      }
      _that.initMenuCount(_that.headerData, _that.menuCount)
    },
    headerChangeMenu: function (data, index,e) {
      var _that = this;
      if(!data.childrenList){
        this.addTab(e,data,this.activeTabIframe,index)
      }else {
        this.headerActive = index;
        this.leftData = data.childrenList;
        // this.leftData.map(function (item) {
        //   console.log(item.menuGovUrl);
        //   if(item.menuGovUrl.indexOf('view-code-00000011')!=-1){
        //       item.count = _that.getViewDataCountByViewCode("view-code-00000011");
        //   }
        //
        //   if(item.menuGovUrl.indexOf('view-code-00000030')!=-1){
        //       item.count = _that.getViewDataCountByViewCode("view-code-00000030");
        //   }
        // });
        _that.getViewDataCountByViewCode("view-code-00000011");
        _that.getViewDataCountByViewCode("view-code-00000030");
        this.showMoreItem = false;
        this.leftMenuActive = -1;
        setTimeout(function(){
          $('.aside-left .menu-item-active').removeClass('menu-item-active');
          $('#'+vm.activeTabIframe).addClass('menu-item-active');
        },300)
        _that.menuAddActive();
        $(e.srcElement).addClass('m-menu__item--active').siblings('.m-menu__item--active').removeClass('m-menu__item--active');
        $(e.srcElement).parents('.m-menu__item').addClass('m-menu__item--active').siblings('.m-menu__item--active').removeClass('m-menu__item--active');
        
      }
    },
    showChildrenList: function (ind, $event) {
      var menuItemTop = $event.currentTarget.getBoundingClientRect().top;
      var ele = $('.menu-item-child');
      var h = $(ele[ind]).height();
      this.hoverCheck = ind;
      if (h + menuItemTop < this.curHeight - 80) {
        this.menuTop = menuItemTop;
      } else {
        this.menuTop = menuItemTop - h + 100;
      }
    },
    hideChildrenList: function (ind) {
      this.hoverCheck = -1;
    },
    addTab: function (ev,data,activeTab,ind) {
      ev.cancelBubble = true;
      this.leftMenuActive = ind;
      if(!data.childrenList&&(this.tabIndex.indexOf(data.id) == -1)){
        this.activeTabData.push({
          menuName: data.menuName,
          id: data.id,
          menuInnerUrl: data.menuInnerUrl
        });
        this.activeTabIframe = data.id;
        $('.aside-left .menu-item-active').removeClass('menu-item-active');
        $('#'+this.activeTabIframe).addClass('menu-item-active');
        this.tabIndex.push(data.id);
      }else {
        this.activeTabIframe = data.id;
        this.menuAddActive();
      }
    },
    removeTab: function (targetName) {
      var tabs = this.activeTabData;
      var activeName = this.activeTabIframe;
      var _that = this;
      if (activeName === targetName) {
        tabs.forEach(function (tab, index) {
          if (tab.id === targetName) {
            var nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.id;
            }
          }
        });
      }
      _that.activeTabIframe = activeName;
      $('.aside-left .menu-item-active').removeClass('menu-item-active');
      $('#'+_that.activeTabIframe).addClass('menu-item-active');
      _that.activeTabData = tabs.filter( function (tab) { return tab.id !== targetName});
      _that.tabIndex = [];
      _that.activeTabData.forEach(function(e){
        _that.tabIndex.push(e.id);
      });
    },
    editPasswordSubmit: function(){
      var _that = this;
      _that.$refs['editPasswordData'].validate(function (valid) {
        if (valid) {
          var props = {
            newPassword: _that.editPasswordData.newPassword,
            oldPassword: _that.editPasswordData.oldPassword,
          };
          request('opus-admin', {
            url: ctx+'opus/front/om/users/password',
            data: props,
            type: 'put'
          }, function (data) {
            if(data.success){
              _that.$message({
                message: '密码修改成功',
                type: 'success'
              });
              _that.editPasswordFlag = false;
            }else {
              _that.$message({
                message: data.message ? data.message : '密码修改失败',
                type: 'error'
              });
            }
          },function(msg){
            _that.$message({
              message: msg.message ? msg.message : '密码修改失败',
              type: 'error'
            });
          })
        } else {
          alertMsg('', '请输入完整且正确的信息', '关闭', 'warning', true);
          return false;
        }
      });
    },
    clearDialogData: function(formName){
      this.$refs[formName].resetFields();
    },
    tabMouseRightClick:function(ev){
      ev.cancelBubble = true;
      if(ev.path){
        this.tabId = ev.path[0].id;
        this.tabKey = ev.path[0].id.split('tab-')[1];
      }else {
        this.tabId = ev.srcElement.id
        this.tabKey = ev.srcElement.id.split('tab-')[1];
      }
      if(!(this.tabId.indexOf('tab-MENU_') > -1)){
        return false;
      }else {
        this.showTabOpt = true;
        this.tabOptTop = ev.clientY;
        this.tabOptleft = ev.clientX;
        this.activeTabIframe = this.tabKey;
        $('.aside-left .menu-item-active').removeClass('menu-item-active');
        $('#'+this.activeTabIframe).addClass('menu-item-active');
      }
    },
    closeOther: function () {
      var start = this.tabIndex.indexOf(this.tabKey);
      this.tabIndex = this.tabIndex.slice(start,start+1);
      this.activeTabData = this.activeTabData.slice(start,start+1);
      this.showTabOpt = false;
    },
    closeLeft: function () {
      var start = this.tabIndex.indexOf(this.tabKey);
      this.tabIndex = this.tabIndex.slice(start);
      this.activeTabData = this.activeTabData.slice(start);
      this.showTabOpt = false;
    },
    closeRight: function () {
      var start = this.tabIndex.indexOf(this.tabKey);
      this.tabIndex = this.tabIndex.slice(0,start+1);
      this.activeTabData = this.activeTabData.slice(0,start+1);
      this.showTabOpt = false;
    },
    displayMenuPopover:function () {
      this.showTabOpt = false;
    },
    onMessage:function (e) {
      var _that = this
      var data = e.data
      if(!data.vueDetected&&data.id!=undefined){
        data.menuInnerUrl = e.origin + data.menuInnerUrl
        _that.addTab(e,data,'MENU_'+data.id,_that.leftMenuActive++);
      }
    },
    changeTab: function(){
      var tabEle = $('.el-tabs__content .el-tab-pane');
      var tabEleLen = tabEle.length;
      var _that = this;
      $('.aside-left .menu-item-active').removeClass('menu-item-active');
      $('#'+_that.activeTabIframe).addClass('menu-item-active');
    },
    
    getViewDataCountByViewCode:function(code) {
      var totalCount = 0;
      var _that = this;
      $.ajax({
        url: ctx + 'bpm/front/view/getViewDataCount?viewCode='+code,
        type: "GET",
        async: false,
        success: function (result) {
          if(result){
            totalCount = result;
          }
          _that.leftData.map(function (item) {
            if(item.menuGovUrl.indexOf(code)!=-1){
              Vue.set(item, 'count', totalCount)
            }
          });
        }
      });
    },
    menuAddActive: function(){
      $('#'+this.activeTabIframe).addClass('menu-item-active').siblings('.el-menu-item').removeClass('menu-item-active');
      $('#'+this.activeTabIframe).parents('.menu-item').siblings('.menu-item.menu-item-active').removeClass('menu-item-active');
      $('#'+this.activeTabIframe).parents('.menu-item').addClass('menu-item-active');
    },
  },
  beforeDestroy:function(){
    document.removeEventListener('click');
  }
});
