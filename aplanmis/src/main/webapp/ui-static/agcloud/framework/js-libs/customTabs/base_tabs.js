/**
 * @author sam
 * @date 2018-07-24
 * @type {string[]}
 * @Todo 基于metronic v5.0 实现的自定义tab页，主要是加入了关闭按钮及鼠标右键菜单；其中右键菜单基于jquery 脚本没有依赖metronic,可以单独剥离
 */
var tabs = function(){};

//tab菜单编号数组
var openedCode = new Array('model');

//右键菜单对象
var kyoPopupMenu={};

//iframe区域点击事件
var IframeOnClick = {
    resolution: 200,
    iframes: [],
    interval: null,
    Iframe: function() {
        this.element = arguments[0];
        this.cb = arguments[1];
        this.hasTracked = false;
    },
    track: function(element, cb) {
        this.iframes.push(new this.Iframe(element, cb));
        if (!this.interval) {
            var _this = this;
            this.interval = setInterval(function() { _this.checkClick(); }, this.resolution);
        }
    },
    checkClick: function() {
        if (document.activeElement) {
            var activeElement = document.activeElement;
            for (var i in this.iframes) {
                if (activeElement === this.iframes[i].element) { // user is in this Iframe
                    if (this.iframes[i].hasTracked == false) {
                        this.iframes[i].cb.apply(window, []);
                        this.iframes[i].hasTracked = true;
                    }
                } else {
                    this.iframes[i].hasTracked = false;
                }
            }
        }
    }
};

Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

//初始化tab
tabs.custom = {
    initTab:function (){
        //点击菜单打开tab页
        $('.m-menu__link').on('click',function () {
            var code = $(this).data('code');
            if(!code){
                return;
            }
            var url = $(this).data('url');
            if(url){
                var title = $(this).children('.m-menu__link-text').html();
                var index = $.inArray(code, openedCode);
                if(index < 0){
                    var tabItem = '<li class="nav-item m-tabs__item">\n' +
                        '<a class="nav-link m-tabs__link" id="item_'+code+'" data-toggle="tab" href="#m_tabs_'+code+'" role="button" aria-haspopup="true" aria-expanded="false">'+title+'<i class="fa fa-times" onclick="tabs.custom.closeTab(\''+code+'\')"></i></a>\n' +
                        '</li>';
                    var tabPanel = '<div class="tab-pane" id="m_tabs_'+code+'" role="tabpanel">\n' +
                        '<div class="m-content"><div class="m-portlet m--margin-bottom-0"><iframe src="'+url+'" style="width:100%; height:calc(100vh - 155px); overflow:hidden" frameborder=\'0\' name="_blank" id="iframe_'+code+'" ></iframe></div>\n' +
                        '</div>';
                    $('#tab_list').append(tabItem);
                    $('#tab_content').append(tabPanel);
                    openedCode.push(code);
                }
            }
            tabs.custom.checkTab(code);
        });

        //初始化右键菜单
        kyoPopupMenu = (function(){
            return {
                sys: function (code) {
                    $('.popup_menu').remove();
                    popupMenuApp = $('<div class="popup_menu app-menu"><ul>' +
                        '<li><a menu="menu1"><i class="fa fa-times"></i> 关闭其他标签页</a></li>' +
                        '<li><a menu="menu2"><i class="fa fa-times"></i> 关闭左边标签页</a></li>' +
                        '<li><a menu="menu3"><i class="fa fa-times"></i> 关闭右边标签页</a></li>' +
                        '</ul></div>')
                        .find('a').attr('href','javascript:;')
                        .end().appendTo('body');
                    //绑定事件
                    $('.app-menu a[menu="menu1"]').on('click', function (){
                        tabs.custom.closeOtherTab(code);
                        $('.popup_menu').hide();
                    });
                    $('.app-menu a[menu="menu2"]').on('click', function (){
                        tabs.custom.closeLeftTab(code);
                        $('.popup_menu').hide();
                    });
                    $('.app-menu a[menu="menu3"]').on('click', function (){
                        tabs.custom.closeRightTab(code);
                        $('.popup_menu').hide();
                    });
                    return popupMenuApp;
                }
            }})();
        //取消右键菜单
        $('html').on('contextmenu', function (){return false;}).click(function(event){
            $('.popup_menu').hide();
        });
    },
    //切换tab页
    checkTab : function (code) {
        $("#item_"+code).trigger('click');

        //桌面点击右击
        $("#item_"+code).on('contextmenu',function (e){
            var popupmenu = kyoPopupMenu.sys(code);
            l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
            t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
            popupmenu.css({left: l,top: t}).show();
            return false;
        });
        //取消右键菜单
        IframeOnClick.track(document.getElementById("iframe_"+code), function() {
            $('.popup_menu').hide();
        });
    },
    //关闭tab页
    closeTab : function (code){
        var tabItem = $('#item_'+code).parent();
        var tabPanel = $('#m_tabs_'+code);
        tabPanel.remove();
        tabItem.remove();
        var index = openedCode.indexOf(code);
        openedCode.remove(code);
        tabs.custom.checkTab(openedCode[index-1]);
    },
    //关闭其他所有tab
    closeOtherTab : function (code){
        var newOpenedCode = new Array('model');
        newOpenedCode.push(code);

        tabs.custom.checkTab(code);

        var index = openedCode.indexOf(code);
        if(index>0){
            for(var i=0;i<openedCode.length;i++){
                var currCode = openedCode[i];
                if(currCode!='model'&&currCode!=code){
                    var tabItem = $('#item_'+currCode).parent();
                    var tabPanel = $('#m_tabs_'+currCode);
                    tabPanel.remove();
                    tabItem.remove();
                }
            }
            openedCode = newOpenedCode;
        }
    },
    //关闭左边tab页
    closeLeftTab : function (code){
        var newOpenedCode = new Array('model');

        tabs.custom.checkTab(code);

        var index = openedCode.indexOf(code);
        for(var i=index;i<openedCode.length;i++){
            newOpenedCode.push(openedCode[i]);
        }

        for(var i=0;i<openedCode.length;i++){
            var currCode = openedCode[i];
            if($.inArray(currCode,newOpenedCode)==-1){
                var tabItem = $('#item_'+currCode).parent();
                var tabPanel = $('#m_tabs_'+currCode);
                tabPanel.remove();
                tabItem.remove();
            }
        }
        openedCode = newOpenedCode;
    },
    //关闭右边tab页
    closeRightTab : function (code){
        var newOpenedCode = new Array();

        var index = openedCode.indexOf(code);
        for(var i=0;i<=index;i++){
            newOpenedCode.push(openedCode[i]);
        }

        tabs.custom.checkTab(openedCode[index]);

        for(var i=0;i<openedCode.length;i++){
            var currCode = openedCode[i];
            if($.inArray(currCode,newOpenedCode)==-1){
                var tabItem = $('#item_'+currCode).parent();
                var tabPanel = $('#m_tabs_'+currCode);
                tabPanel.remove();
                tabItem.remove();
            }
        }
        openedCode = newOpenedCode;
    }
}