
var vm = new Vue({
    el: '#market_guide',
    data: {
      loading:false,
      fullHeight:document.documentElement.clientHeight,
      treeLeftHeight:document.documentElement.clientHeight-200,
      isScroll:false,
      activeName:5,
      showRegisterOwner:false,
    },
    created: function () {

        console.log(localStorage.getItem("access_token"));

    },
    mounted: function () {
      var that = this;
      window.addEventListener("resize", function() {
        return (function(){
          window.fullHeight = document.documentElement.clientHeight
          that.fullHeight = window.fullHeight;
          that.treeLeftHeight = that.fullHeight - 100;
        })();
      });
      //树形菜单栏
      $('.page-sidebar .nav-tree li').click(function(e) {
          var selfIndex = $(this).index()
          $(this).addClass('active').siblings().removeClass('active')
          $('.sidebar-con').children('div:eq(' + selfIndex + ')').addClass('active').siblings().removeClass('active')
      });
    },
    methods: {
      showRegisterOwnerFn:function(){
          console.log(123)
          this.showRegisterOwner  = true
          console.log(this.showRegisterOwner)
      },
      tabToggle:function(type){
        this.type=type;
      },
    },
    computed:{
      headMsg:function(){
        return {
          "Authorization":"bearer "+localStorage.getItem("access_token")
        }
      }
    },
    watch:{
      filterText1:function(val){

      }
    }
});
