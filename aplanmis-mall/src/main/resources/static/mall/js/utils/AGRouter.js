/*!
 * router JavaScript Library v1.0.0 for web
 *  http://39.107.139.174:8084
 *  author freddy_huang
 * /aplanmis-project/aplanmis-mall/src/main/resources/static/mall/js/utils
 *
 * Date: 2019-07-12 T15:04
 *
 * describe： 目前只mode name 为 hash 的模式 ，今后有空封装增加其他的mode
 *
 */
;(function(global,factory,plug){
    if(typeof exports === 'object' && module !== 'undefined'){
        module.exports = factory(global.$,plug);
    } else if(typeof define === 'function' && (define.amd || define.cmd)){
        define(factory(global.$,plug));
    } else {
        return  factory.call(global,global.$,plug);
    }
})(typeof window === 'undefined'? this:window,function($,plug){
    //$.fn[plug]
    //路由构造器
    function Router() {
        //接受所有的配置路由内容
        this.routes = {};
        this.curUrl = '';
        //所有路由和函数方法 传给 routes
        this.route = function (path, callback) {
            this.routes[path] = callback || function () {};
        };
        this.refresh = function (e) {
            if(e.type=='load' && /div_step/igm.test(location.hash.slice(1))){
                this.curUrl = '/guideIndex';
            } else if (e.type == 'load' && /declare|scheduleInquire|declareHave|matCompletionList|approve|matSupplementList|lifeCycle|UserInfo|MyHomeIndex|MyMaterials|AddProj|MyCertificateLibrary|CreditDetail|withdrawApplyList|drafts/igm.test(location.hash.slice(1))) {
               this.curUrl = '/userCenterIndex'
            }else if(e.type="hashchange" && /userCenterIndex/igm.test(location.hash.slice(1))){
               this.curUrl = '/userCenterIndex'
            } else{
                //获取改变的hash值
                this.curUrl = location.hash.slice(1) || '/';
            }
            this.routes[this.curUrl] && this.routes[this.curUrl]();
        };
        //监听load（加载url）、hashchange（hash改变）事件
        this.init = function () {
            window.addEventListener('load',this.refresh.bind(this),false);
            window.addEventListener('hashchange',this.refresh.bind(this),false)
        }
    }
    var R = new Router();//使用Router构造器

    R.init();//监听时间

    var res = document.getElementById('app_frame');
    //初始化所有需要用的 路由：hash值 和 加载的内容  暂时没实现路由懒加载
    APP.topTabData.forEach(function(item,index){
        var itemHash = item.hash;
        var itemUlr;
        if (document.location.protocol == "file:") {
             itemUlr = item.url;
        } else {
            itemUlr = ctx + item.thUrl;
        }
        R.route(itemHash,function () {
            APP.activeName = index||0;
            $(res).load(itemUlr);
        })
    })
    APP.routerInitData.forEach(function(item,index){
        var itemHash = item.hash;
        var itemUlr;
        if (document.location.protocol == "file:") {
            itemUlr = item.url;
        } else {
            itemUlr = ctx + item.thUrl;
        }
        R.route(itemHash,function () {
            APP.activeName = item.activeName||0;
            $(res).load(itemUlr);
        })
    })
},'Router');
