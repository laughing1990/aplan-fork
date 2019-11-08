  $(function () {
    //  begin 滚动时，固定左侧的menu 并导航到相对位置
    var flag = true; //控制当点击楼层号时，禁止滚动条的代码执行   值为true时，可以执行滚动条代码
    //3、 滚动条滚动 --  找到当前楼层的索引    控制楼层号  固定操作按钮
    $(window).scroll(function () {
      if (flag) {
        var items = $(".div_step");
        var menu = $("#menu");
        var top = $(document).scrollTop();
        var currentId = ""; //滚动条现在所在位置的item id
        var cl = '';
        var h = $(window).height()/2;
        items.each(function () {
          var m = $(this);
          //注意：m.offset().top代表每一个item的顶部位置
          if (top > m.offset().top-h) {
            currentId = "#" + m.attr("id");
            cl = m.attr("id");
          } else {
            return false;
          }
        });
        var currentLink = menu.find(".active");
        if (currentId && currentLink.attr("href") != currentId) {
          currentLink.removeClass("active");
          menu.find("[nam=" + cl + "]").addClass("active");
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

    // 上传弹框事件 start
    $('.up-file').click(function(){
      $('#upFile').trigger("click");
    });
    $('#upFile').change(function(e){
      var size = "0kb", stage = "未上传",html = '';
      var name = e.currentTarget.files[0].name;
      size = e.currentTarget.files[0].size;
      html = "<tr><td>"+
      name+
      "</td><td>"+
      size+'kb'+
      "</td><td>"+
      stage+
      "</td><td class='ac'><button type='button' class='btn btn-info btn-sm'><i class='la la-trash'></i>删除</button></td></tr>"
      $('.up-file-info').html(html);
      $('.up-file').hide();  // 隐藏上传按钮
      $('.start-upload').css({"display": "inline-block"});  // 开始上传按钮展示
    });
    // 开始上传
    $('.start-upload').click(function(){
      
    });
    // 上传弹框事件 end
  })
