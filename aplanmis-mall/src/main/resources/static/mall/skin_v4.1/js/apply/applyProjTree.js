/*
 * @Author: ZL
 * @Date:   2019/6/4
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var parallelDeclare = new Vue({
  el: '#applyProjTree',
  data: function () {
    return {
      ctx: ctx,
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight), //当前屏幕高度
      loading: false, // 全屏loading
      projInfoId: '', // 项目代码
      projinfoDetailTree: {
        stagesVos: [],
      }, // 项目树
    }
  },
  mounted: function () {
    console.log(1);
    var _that = this;
    _that.getProjTreeData();
  },
  created: function () {
    this.GetRequest();
  },
  methods: {
    // 获取项目树数据
    getProjTreeData: function (selTreeData) {
      var _that = this;
      // rest/user/status/proj/tree/{projInfoId}
      if(_that.projInfoId==''){
        alertMsg('', '请先选择项目！', '关闭', 'error', true);
      }else {
        _that.loading = true;
        request('', {
          url: ctx + 'rest/user/status/proj/tree/'+_that.projInfoId,
          // url: ctx+'mall/skin_v4.1/js/apply/test.json',
          type: 'get',
        }, function (result) {
          _that.loading = false;
          if (result.success) {
            _that.projinfoDetailTree = result.content;
            // setTimeout(function(){
              _that.$nextTick(function(){
                var projTreeItem = $('.projTree-item');
                for(var i=0;i<projTreeItem.length;i++){
                  var childrenLength = $(projTreeItem[i]).attr('childrenLength');
                  var keyIndex = $(projTreeItem[i]).attr('keyIndex');
                  var stageIndex = $(projTreeItem[i]).attr('stageIndex');
                  var stageIndexLength = _that.projinfoDetailTree.stagesVos.length-1;
                  var stageLength = $(projTreeItem[i]).attr('stageLength')-1;
                  var parentProjInfoId = $(projTreeItem[i]).attr('parentProjInfoId');
                  var stageId = $(projTreeItem[i]).attr('stageId');
                  var stageIdBefore = '',parentProjInfoIdBefore ='';
                  if(i>0){
                    stageIdBefore = $(projTreeItem[i-1]).attr('stageId');
                    parentProjInfoIdBefore = $(projTreeItem[i-1]).attr('parentProjInfoId');
                  }
                  if(childrenLength&&childrenLength>1){
                    var minHeight = (150*childrenLength+((childrenLength-1)*40)) + 'px';
                    $(projTreeItem[i]).css({'min-height': minHeight});
                  }
                  var offsetTop = 0;
                  if($('#'+parentProjInfoId).offset()){
                    if(stageIdBefore!==stageId){
                      if(stageIndex!=stageIndexLength){
                        offsetTop = $('#'+parentProjInfoId).offset().top;
                      }else {
                        offsetTop = $('.stage-index-0').offset().top;
                      }
                    }else {
                      if(parentProjInfoIdBefore!=''&&parentProjInfoIdBefore!==parentProjInfoId){
                        $(projTreeItem[i-1]).find('.line').hide();
                      }else if(stageLength==keyIndex){
                        $(projTreeItem[i]).find('.line').hide();
                      }
                      offsetTop = ($(projTreeItem[i-1]).offset().top)+190;
                    }
                    $(projTreeItem[i]).offset({top:offsetTop});
                  }
                  $(projTreeItem[i]).siblings('i.line').css({top:(offsetTop-541)+'px'});
                  var str = '.stage-index-'+((_that.projinfoDetailTree.projStatusVoArrs.length)-1);
                  var stage1Height =$(str).height();
                  if(stage1Height>0){
                    $('.pro-gc-content .stage-index-0 .projTree-item').css({'height':stage1Height+'px'});
                  }
                }
                if($('.stage-index-1').length==1){
                  var stage1ProjCard = $('.stage-index-1 .projTree-item');
                  var topH = ($(stage1ProjCard[0]).height())/2-1;
                  var bottomH = ($(stage1ProjCard[stage1ProjCard.length-1]).height())/2-3;
                  $('.stage-index-1 .line').css({top:topH,bottom:bottomH});
                }

              })
            // },100)
          }else {
            _that.$message({
              message: result.message ? result.message : '获取项目树信息失败',
              type: 'error'
            });
          }
        }, function (msg) {
          _that.loading = false;
          _that.$message({
            message: msg.message ? msg.message : '获取项目树信息失败',
            type: 'error'
          });
        })
      }
    },
    GetRequest: function () {
      var url = location.search; //获取url中"?"符后的字串
      var theRequest = new Object();
      if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        var strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
          theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
      }
      this.projInfoId = theRequest.projInfoId;
      return theRequest;
    },
    // 跳转申报页
    goToApplyPage: function (projTreeData) {
      if(projTreeData.applyStatus!='1'&&projTreeData.applyStatus!='4'){
        return false;
      }else {
        if(projTreeData.applyInstId&&projTreeData.applyInstId!=''){
          window.location.href = ctx+'rest/main/toIndexPage?applyState=1&applyinstId='+projTreeData.applyinstId+'&guideId='+projTreeData.guideId+'&projInfoId='+projTreeData.projInfoId+'&isQueryIteminstState='+projTreeData.applyStatus+'#/myParallelPage'
        }
      }
    },
    // 跳转查看页
    goToDetailPage: function (projTreeData) {
      window.location.href = ctx+'rest/main/toIndexPage?localCode='+projTreeData.localCode+'&stageId='+projTreeData.stageId+'#declareHave';
    },
  },
  filters: {
    formatApplyStatus: function (value) {
      var defaultValue='';
      // 0:未申报1:申报中2:已办结3:未审核4:已审核)
      if(value){
        switch(value) {
          case '0':
            defaultValue='未申报';
            break;
          case '1':
            defaultValue='申报中';
            break;
          case '2':
            defaultValue='已办结';
            break;
          case '3':
            defaultValue='未审核';
            break;
          case '4':
            defaultValue='已审核';
            break;
        }
      }
      return defaultValue;
    }
  },
});