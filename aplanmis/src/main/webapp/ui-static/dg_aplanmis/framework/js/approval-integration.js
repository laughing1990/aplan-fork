$(function(){
  $('body').css('background','#fff');
  //  begin 滚动时，固定左侧的menu 并导航到相对位置
  var flag = true; //控制当点击楼层号时，禁止滚动条的代码执行   值为true时，可以执行滚动条代码
  //3、 滚动条滚动 --  找到当前楼层的索引    控制楼层号  固定操作按钮
  $('.para-approve-content').scroll(function () {
    if (flag) {
      var items = $(".div-step");
      var menu = $("#menu");
      var top = $(document).scrollTop();
      var currentId = ""; //滚动条现在所在位置的item id
      var cl = '';
      var h = $(window).height()/2;
      items.each(function () {
        var m = $(this);
        //m.offset().top代表每一个item的顶部位置
        if (top > m.offset().top-h/2) {
          currentId = "#" + m.attr("id");
          cl = m.attr("id");
        } else {
          return false;
        }
      });
      var currentLink = menu.find(".active");
      if (currentId && currentLink.attr("href") != currentId) {
        currentLink.removeClass("active");
        menu.find("[data-name=" + cl + "]").addClass("active");
        $(currentId).addClass("active").siblings().removeClass("active");
      }
    }
  });
  // end 滚动时，固定左侧的menu 并导航到相对位置
  // 点击左侧滚动导航条 start
  $('.ciclebox').click(function () {
    var ele = $(this).children('a').attr('href');
    $(ele).addClass('active').siblings('.div_step').removeClass('active');
    $(this).addClass('active').siblings('.ciclebox').removeClass('active');
  });
  // 点击左侧滚动导航条 end

  $(window).scroll(function(){
    contenShowView();
  });
  
  // 右侧点击放大 效果
  $('.tab-icon-right').click(function(e){
    e.stopPropagation();
    if($(this).hasClass('expand')){
      $('.content-right').css('width','50%');
      $(this).removeClass('expand').addClass('compress');
      $('.m-accordion__item-title > span').removeClass('ellipsis').css('width','auto');
    }else {
      $('.content-right').css('width','25%');
      $(this).addClass('expand').removeClass('compress');
      $('.m-accordion__item-title > span').addClass('ellipsis').css('width','160px');
    }

  });
  // 右侧展开后 点击其他区域 收起
  $(document).click(function(e){
    var _con = $('.content-right');   // 设置目标区域
    if(!_con.is(e.target) && _con.has(e.target).length === 0){
      $('.content-right').css('width','25%');
      $('.content-right .tab-icon-right').addClass('expand').removeClass('compress');
      $('.content-right .m-accordion__item-title > span').addClass('ellipsis').css('width','160px');
    };
  });

 
  function enterfullscreen() {//进入全屏

    var docElm = document.documentElement;
//W3C
    if (docElm.requestFullscreen) {
      docElm.requestFullscreen();
    }
//FireFox
    else if (docElm.mozRequestFullScreen) {
      docElm.mozRequestFullScreen();
    }
//Chrome等
    else if (docElm.webkitRequestFullScreen) {
      docElm.webkitRequestFullScreen();
    }
//IE11
    else if (docElm.msRequestFullscreen) {
      docElm.msRequestFullscreen();
      var widthFull = $(window).width();
      $('#wrapper').css('width',widthFull);
    }else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
      var wscript = new ActiveXObject("WScript.Shell");
      if (wscript !== null) {
        wscript.SendKeys("{F11}");
      }
    }
  }
 
  function exitfullscreen() { //退出全屏

    if (document.exitFullscreen) {
      document.exitFullscreen();
    }
    else if (document.mozCancelFullScreen) {
      document.mozCancelFullScreen();
    }
    else if (document.webkitCancelFullScreen) {
      document.webkitCancelFullScreen();
    }
    else if (document.msExitFullscreen) {
      document.msExitFullscreen();
    }else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
      var wscript = new ActiveXObject("WScript.Shell");
      if (wscript !== null) {
        wscript.SendKeys("{F11}");
      }
    }
  }

  var a=0;
  $('.full-screen').on('click',function () {
    $('.full-screen i').toggleClass('min');
    a++;
    a%2==1?enterfullscreen():exitfullscreen();
  })
  // 关闭当前页面
  $('.close-window').click(function(){  // ie能实现  谷歌只能实现关闭当前窗口打开空白窗口
    var userAgent = navigator.userAgent;
    if(confirm("您确定要关闭本页吗？")){
      if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
        window.location.href="about:blank";
      } else {
        window.opener = null;
        window.open("", "_self");
        window.close();
      }
    }
  });
  // 左侧 主体内容固定展示

  function contenShowView(){
    var windowHeight = $(window).height();
    var offset=$('.content-left .tab-content').offset().top;
    $('.content-left .tab-content').css('height',windowHeight-offset+'px');
  }
  contenShowView();
  function contenShowHeight() {
    var autoHeightForm = $(window).height();
    var eleOffset = $("#form-tabs-content").offset().top;
    $("#form-tabs-content").height(autoHeightForm-eleOffset-44);
  }
  contenShowHeight();
  $(window).resize(function (){
    contenShowHeight();
  })

})
