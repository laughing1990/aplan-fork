$(document).ready(function() {
    var trigger = $('#sidebar-switch'),
        isClosed = true;
    // trigger.click(function() {
    //     hamburger_cross();
    // });
    function hamburger_cross() {
        if (isClosed == true) {
            trigger.removeClass('is-open');
            trigger.addClass('is-closed');
            isClosed = false;
           //flag = true;
        } else {
            trigger.removeClass('is-closed');
            trigger.addClass('is-open');
            isClosed = true;
            //flag = false;
        }
    }
    $('[data-toggle="offcanvas"]').click(function() {
        $('#wrapper').toggleClass('toggled');
        menuWidth = $("#menu").width();
    });
    
    /*
     *  begin 事项列表 table 全选 和 取消全选
     * 
     */
    var table2 = $(".table2");
   	$("#table2-checkbox").change(function(){
   		var $this = $(this);
   		var isCheck =  $this[0].checked;
   		var selectItem = table2.find('.select')
   		if(isCheck){
			selectItem.each(function(index,item){
				item.checked = true;
 			})
   		}else{
   			selectItem.each(function(index,item){
				item.checked = false;
 			})
   		}
   	})
   	 /*
     *  end 事项列表 table 全选 和 取消全选
     * 
     */
    
    /*
     *  begin 滚动时，固定左侧的menu 并导航到相对位置
     * 
     */
 //   	var flag = true; //控制当点击楼层号时，禁止滚动条的代码执行   值为true时，可以执行滚动条代码
 //   	var menuWidth = $("#menu").width();
	// var h;//控制滚走的距离 
	// setTimeout(function(){
	// 	h = $("#menu").offset().top;
	// },1500)
	//3、 滚动条滚动 --  找到当前楼层的索引    控制楼层号
// 	$(window).scroll(function () {
// 		if(flag){
// 	        var items = $("#content").find(".m-accordion__item");
// 	        var menu = $("#menu");
// 	        var top = $(document).scrollTop();
// 	        var currentId = ""; //滚动条现在所在位置的item id
// 	        var cl = '';
// 	        items.each(function () {
// 	            var m = $(this);
// 	            //注意：m.offset().top代表每一个item的顶部位置
// 	            if (top > m.offset().top-80) {
// 	                currentId = "#" + m.attr("id");
// 	                cl = m.attr("id");
// 	            } else {
// 	                return false;
// 	            }
// 	        });
// 	        var currentLink = menu.find(".active");
// 	        if (currentId && currentLink.attr("href") != currentId) {
// 	            currentLink.removeClass("active");
// 	            console.log(currentId)
// 	            menu.find("[nam=" + cl + "]").addClass("active");
// 	            $(currentId).addClass("active").siblings().removeClass("active");
// 	        }
// //	       if ($(window).scrollTop()>=h) {
// //
// //			}else{
// //
// //			} 
// 		}
//     });
     /*
     *  end 滚动时，固定左侧的menu 并导航到相对位置
     * 
     */
    
    // $('.ciclebox').click(function(){
    //     var ele = $(this).children('a').attr('hre');
    //     $(ele).addClass('active').siblings().removeClass('active');
    //     $(this).addClass('active').siblings('.ciclebox').removeClass('active');
    //     var _top =  $(ele).offset().top;
    //     $("body,html").animate({scrollTop:_top-68},500);
    // });

    // 查看附件事件
    $('.detail-btn').click(function(){
      $("#select_person2").addClass("show").css("display","block");
    });
    //关闭附件详细
    $('#select_person2 .close').click(function(){
      $("#select_person2").removeClass("show").css("display","none");
    });
    // 提示框内容
    $(".detail-btn").mouseenter(function(){
      $(".m-tooltip .tooltip-inner").text("已提交1个附件，点击查看详细");
    });
});
