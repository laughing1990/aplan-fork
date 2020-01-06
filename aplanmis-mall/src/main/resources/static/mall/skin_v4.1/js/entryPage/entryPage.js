(function () {
    var entryPage = new Vue({
        el:"#entryPage",
        data:{
        },
        created:function() {
            this.init();
            //do something after creating vue instance
        },
        methods:{
            toDeclarGuidePage:function () {
                window.location.hash='/DeclarGuidePage';
                window.location.search='';
            },
            toMyParallelPage:function(){
                window.location.hash='/myParallelPage';
                window.location.search='';
            },
            toMyPorjList:function(){
                sessionStorage.setItem('myProjListActive',1);
                window.location.hash='/userCenterIndex';
                window.location.search='';
            },
            toUserCenterIndex:function(){
                sessionStorage.setItem('myProjListActive',0);
                window.location.hash='/userCenterIndex';
                window.location.search='';
            },
            toRegulationIndex:function(){
                window.location.hash='/regulationIndex';
                window.location.search='';
            },
        }
    })
})();