/*!
 * Copyright (c) 2019-7-11  entry file js
 * author freddy_huang
 *
 * @global  APP 用作全局的通讯桥梁
 *
 */
;(function(w){
    APP = new Vue({
        el:"#APP",
        data:{
            ctx:ctx,
            indexUrl:'./main/mainIndex.html',
            activeName:null,
            topTabData: [{
                label: '首页',
                id: '1',
                url: './main/mainIndex.html',
                thUrl:'rest/main/toMainIndexPage',
                hash:'/',
              }, {
                label: '办事指南',
                id: '2',
                url: './guide/guideIndex.html',
                thUrl:'rest/guide/toGuideIndexPage',
                hash:'/guideIndex',
              }, {
                label: '中介服务超市',
                id: '3',
                url: './supermarket/main/index.html',
                thUrl:'supermarket/main/index.html',
                hash:'/supermarketIndex',
              }, {
                label: '政策法规',
                id: '4',
                url: './regulation/regulationIndex.html',
                thUrl:'rest/main/toRegulationIndexPage',
                hash:'/regulationIndex',
              },
                {
                    label: '法人空间',
                    id: '5',
                    url: './userCenter/userCenterIndex.html',
                    thUrl:'rest/user/toUserCenterindexPage',
                    hash:'/userCenterIndex',
                },
            ],
            routerInitData:[
                {
                    label: '我的云盘',
                    id: 'myCloundSpaces',
                    activeName:4,
                    url: './userCenter/components/my-cloundSpaces.html',
                    thUrl:'rest/user/toMyCloundSpacesPage',
                    hash:'/myCloundSpaces',
                },
            ],
            curentLoginInfo:{},
            isShowCompatibleTips: true // 是否显示浏览器兼容模块提示
        },
        created:function() {
            this.init();
          //do something after creating vue instance
        },
        mounted:function() {
            //do something after mounting vue instance
        },
        methods: {
          routerChange:function(hash,url,thUrl,index){
              if(index === 2){
                    w.open(ctx+thUrl);
                    return false;
              }else{
                  this.activeName = index;
                  if(index === 1){
                      var stageData = {
                          origin:false,
                      }
                      var orgData = {
                          origin2:false,
                      }
                      sessionStorage.setItem('stageData',JSON.stringify(stageData));
                      sessionStorage.setItem('orgData',JSON.stringify(orgData));
                  }
                  w.location.hash = hash;
                  w.location.search = '';
              }
          },
            init: function () {

                // 登陆用户数据
                var _curLoginInfo = localStorage.getItem('loginInfo');
                /*if(_curLoginInfo){
                    var currentLoginInfo = JSON.parse(_curLoginInfo);
                    $('#commonLoginInfo').empty();
                    $('#commonLoginNameInfo').empty();
                    if(currentLoginInfo && currentLoginInfo.unitId){
                        $('#commonLoginNameInfo').append('<a href="##" class="wt-header-enterprise"><span>'+currentLoginInfo.unitName+'</span></a>|');
                        $('#commonLoginInfo').append( ' <a href="javascript:;" @click="logout" class="wt-header-enterprise">退出</a>');
                    }else if(currentLoginInfo && currentLoginInfo.userId){
                        $('#commonLoginNameInfo').append('<a href="##" class="wt-header-enterprise"><span>'+currentLoginInfo.personName+'</span></a>|');
                        $('#commonLoginInfo').append( ' <a href="javascript:;" @click="logout" class="wt-header-enterprise">退出</a>');
                    }else{
                        $('#commonLoginInfo').append( ' <a href="/aplanmis-mall/rest/mall/loginIndex" class="wt-header-enterprise">登录</a>');
                    }
                }else {
                    $('#commonLoginInfo').append( ' <a href="/aplanmis-mall/rest/mall/loginIndex" class="wt-header-enterprise">登录</a>');
                }*/

                if (_curLoginInfo) {
                    this.curentLoginInfo = JSON.parse(_curLoginInfo);
                }else{
                    this.curentLoginInfo = null;
                }
            },

            // 切换单位信息
            switchUnit: function (item) {
                var ts = this;
                request('', {
                    url: ctx + 'rest/mall/user/changeApplicant/' + item.unitInfoId,
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        window.location.reload();
                        ts.curentLoginInfo.unitName = item.applicant;
                        ts.curentLoginInfo.unitId = item.unitId;
                        localStorage.setItem('loginInfo', JSON.stringify(ts.curentLoginInfo));
                    } else {
                        return ts.$message({
                            message: '切换失败！',
                            type: 'error'
                        })
                    }
                }, function () {
                    return ts.$message({
                        message: '切换失败！',
                        type: 'error'
                    })
                });
            },

            // 退出
            logout: function () {
                var ts = this;
                request('', {
                    url: ctx + 'rest/mall/logout',
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        localStorage.clear();
                        window.location.href = window.location.origin +  '/aplanmis-mall/rest/main/toIndexPage';
                        ts.$message({
                            message:'退出成功！',
                            type: 'success'
                        })
                        return;
                    }else{
                        return ts.$message({
                            message: '退出失败！',
                            type: 'error'
                        })
                    }
                }, function () {
                    return ts.$message({
                        message: '退出失败！',
                        type: 'error'
                    })
                });
            },
        },
    })
   w.APP;
})(typeof window === 'undefined'? this : window);
