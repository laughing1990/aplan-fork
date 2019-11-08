$(function () {
  function setModalMarTop(){
    var userAgent = navigator.userAgent;
    if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") != -1) {
      return false;
    } else {
      var windowH = $(window).height();
      var modalH = $('.modal-content').height();
      if(modalH > windowH){
        return false;
      }else {
        var marT = (windowH - modalH)/2;
        $('.modal-content').css('margin-top',marT);
      }
      
    }
  }
})