var app = new Vue({
  el: "#declareGuidePage55",
  data: {
    loading: false,
    ctx: ctx,
    // 当前申报指引tab
    activeTab: 0,
    // 所有申报指引类型
    allTabList: [{
      name: '办事指南',
      thUrl:'rest/guide/toGuideIndexPage',
      tabIndex: 0
    }, {
      name: '政策法规',
      thUrl: 'rest/main/toRegulationIndexPage',
      tabIndex: 1
    }, {
      name: '操作指引',
      thUrl: 'rest/main/gj',
      tabIndex: 2
    }],
    
  },
  methods: {
    // 切换不同类型申报指引
    switchTab: function (tab) {
      this.activeTab = tab.tabIndex || 0;
      $.get(ctx + tab.thUrl, function (result) {
        $('#declarGuideContent').html(result);
      });
    },
   
    
  },
  mounted: function () {
    // 默认页面第一次获取佛山的
    this.switchTab(this.allTabList[0]);
  },
})