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
            winH:1800,
            indexUrl:'./main/mainIndex.html',
            activeName:null,
            topTabData: [{
                label: '首页',
                id: '1',
                activeName:0,
                url: './main/mainIndex.html',
                thUrl:'rest/main/toMainIndexPage',
                hash:'/',
              }, {
                label: '办事指南',
                id: '2',
                activeName:1,
                url: './guide/guideIndex.html',
                thUrl:'rest/guide/toGuideIndexPage',
                hash:'/guideIndex',
              }, {
                label: '中介服务超市',
                id: '3',
                activeName:2,
                url: './supermarket/main/index.html',
                thUrl:'supermarket/main/index.html',
                hash:'/supermarketIndex',
              }, {
                label: '政策法规',
                id: '4',
                activeName:3,
                url: './regulation/regulationIndex.html',
                thUrl:'rest/main/toRegulationIndexPage',
                hash:'/regulationIndex',
              },
                {
                    label: '用户中心',
                    id: '5',
                    activeName:4,
                    url: './userCenter/userCenterIndex.html',
                    thUrl:'rest/user/toUserCenterindexPage',
                    hash:'/userCenterIndex',
                },
            ],
            // 要想切分复杂的页面为两个，可以参照下面routerInitData配置
            routerInitData:[
                {
                    label: '我的云盘',
                    id: 'myCloundSpaces',
                    activeName:4,
                    url: './userCenter/components/my-cloundSpaces.html',
                    thUrl:'rest/user/toMyCloundSpacesPage',
                    hash:'/myCloundSpaces',
                },
                {
                    label: '视频教学',
                    id: 'videoTeaching',
                    activeName:0,
                    url: './main/components/videoTeaching.html',
                    thUrl:'rest/main/tovideoTeachingPage',
                    hash:'/videoTeaching',
                },
                {
                    label: '我的单项办事指南',
                    id: 'singlePage',
                    activeName:1,
                    url: './guide/components/single.html',
                    thUrl:'rest/guide/toSinglePage',
                    hash:'/singlePage',
                },
                {
                    label: '办事指南事项一清单',
                    id: 'listmatter',
                    activeName:1,
                    url: './listmatter/listmatter.html',
                    thUrl:'rest/guide/tolistmatterPage',
                    hash:'/listmatter',
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
            this.winH = document.documentElement.clientHeight;
            this.initAGRouter()
        },
        methods: {
          routerChange:function(hash,url,thUrl,index){
              if(index === 2){
                  //  w.open(ctx+thUrl);
                  w.open('http://zj.fsxzfw.gov.cn/gdfs-zjcs-pub/home')
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
                if (_curLoginInfo) {
                    this.curentLoginInfo = JSON.parse(_curLoginInfo);
                }else{
                    this.curentLoginInfo = null;
                }
            },
            initAGRouter:function(){
                var ts = this;
                $("#app_frame").AGRouter({
                    router: ts.routerInitData,
                    APP:ts,
                })
                $("#app_frame").AGRouter({
                    router: ts.topTabData,
                    APP:ts,
                })
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
