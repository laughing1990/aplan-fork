$(function(){
  $('body').css('background','#fff');
  var flag = true; //控制当点击楼层号时，禁止滚动条的代码执行   值为true时，可以执行滚动条代码
  // 滚动条滚动 --  找到当前楼层的索引    控制楼层号  固定操作按钮
  $('.el-scrollbar__wrap').scroll(function () {
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
})
