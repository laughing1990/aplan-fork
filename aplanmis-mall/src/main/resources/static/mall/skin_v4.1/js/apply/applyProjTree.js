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
        "projInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
        "projName": "广州奥格员工宿舍四期项目",
        "localCode": "FS-2020-440604-01-01-000004",
        "themeName": "政府投资工程建设项目（市政类线性工程项目）",
        "nstartTime": "2020-01-01",
        "dueNum": null,
        "investSum": 100000.0,
        "stagesVos": [{
          "stageId": "05f0e905-88c1-4bb0-9e5e-fcfee5e2c476",
          "stageName": "立项用地规划阶段",
          "applyStatus": "1"
        }, {
          "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
          "stageName": "工程建设许可阶段",
          "applyStatus": "0"
        }, {
          "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
          "stageName": " 施工许可阶段",
          "applyStatus": "0"
        }, {
          "stageId": "996d34a5-b615-4920-b142-a29b96d73e90",
          "stageName": "竣工验收阶段",
          "applyStatus": "0"
        }],
        "projStatusVos": [{
          "projInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
          "parentProjInfoId": null,
          "projName": "广州奥格员工宿舍四期项目",
          "localCode": "FS-2020-440604-01-01-000004",
          "applyStatus": "1",
          "stageId": "05f0e905-88c1-4bb0-9e5e-fcfee5e2c476",
          "projFlag": "l",
          "childProjStatusVos": [{
            "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb",
            "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
            "projName": "广州奥格员工宿舍四期项目--后端小组工建",
            "localCode": "FS-2020-440604-01-01-000004",
            "applyStatus": "4",
            "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
            "projFlag": null,
            "childProjStatusVos": null
          }, {
            "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
            "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
            "projName": "广州奥格员工宿舍四期项目--后端小组工建",
            "localCode": "FS-2020-440604-01-01-000004",
            "applyStatus": "4",
            "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
            "projFlag": null,
            "childProjStatusVos": null
          }]
        }],
        "projStatusVoArrs": [
          [{
            "projInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
            "parentProjInfoId": null,
            "projName": "广州奥格员工宿舍四期项目",
            "localCode": "FS-2020-440604-01-01-000004",
            "applyStatus": "1",
            "stageId": "05f0e905-88c1-4bb0-9e5e-fcfee5e2c476",
            "projFlag": "l",
            "childProjStatusVos": [{
              "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb",
              "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
              "projName": "广州奥格员工宿舍4-1期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
              "projFlag": null,
              "childProjStatusVos": []
            }, {
              "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
              "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
              "projName": "广州奥格员工宿舍4-2期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
              "projFlag": null,
              "childProjStatusVos": [{
                "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍五期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }, {
                "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f781",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍六期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              },
              {
                "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f7812",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍7期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }]
            },
            {
              "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f7811",
              "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
              "projName": "广州奥格员工宿舍4-2-1期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
              "projFlag": null,
              "childProjStatusVos": [{
                "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb11",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍5期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }, {
                "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78111",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍6期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }]
            }]
          }],
          [{
            "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb",
            "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
            "projName": "广州奥格员工宿舍4-1期项目--后端小组工建",
            "localCode": "FS-2020-440604-01-01-000004",
            "applyStatus": "4",
            "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
            "projFlag": null,
            "childProjStatusVos": null
          }, {
            "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
            "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
            "projName": "广州奥格员工宿舍4-2期项目--后端小组工建",
            "localCode": "FS-2020-440604-01-01-000004",
            "applyStatus": "4",
            "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
            "projFlag": null,
            "childProjStatusVos": [{
              "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb",
              "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
              "projName": "广州奥格员工宿舍五期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
              "projFlag": null,
              "childProjStatusVos": null
            }, {
              "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f781",
              "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
              "projName": "广州奥格员工宿舍六期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
              "projFlag": null,
              "childProjStatusVos": null
            },
              {
                "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f7812",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍7期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }]
          },
            {
              "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f7811",
              "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
              "projName": "广州奥格员工宿舍4-2-1期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
              "projFlag": null,
              "childProjStatusVos": [{
                "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb11",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍5期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }, {
                "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78111",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍6期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }]
            }],
          [{
            "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb",
            "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
            "projName": "广州奥格员工宿舍五期项目--后端小组工建",
            "localCode": "FS-2020-440604-01-01-000004",
            "applyStatus": "4",
            "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
            "projFlag": null,
            "childProjStatusVos": null
          }, {
            "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f781",
            "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
            "projName": "广州奥格员工宿舍六期项目--后端小组工建",
            "localCode": "FS-2020-440604-01-01-000004",
            "applyStatus": "4",
            "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
            "projFlag": null,
            "childProjStatusVos": null
          },
            {
              "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f7812",
              "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
              "projName": "广州奥格员工宿舍7期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
              "projFlag": null,
              "childProjStatusVos": null
            },
            {
              "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f7811",
              "parentProjInfoId": "88512bd7-8c54-4f31-aba5-3cacea282f25",
              "projName": "广州奥格员工宿舍4-2-1期项目--后端小组工建",
              "localCode": "FS-2020-440604-01-01-000004",
              "applyStatus": "4",
              "stageId": "52c91c5e-1db6-4919-a1c8-b0ca654e06f3",
              "projFlag": null,
              "childProjStatusVos": [{
                "projInfoId": "99baebff-ef42-413d-93ad-b0e0b31238fb11",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍5期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }, {
                "projInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78111",
                "parentProjInfoId": "967d0ff0-95c8-42cc-9c8a-6325e5a63f78",
                "projName": "广州奥格员工宿舍6期项目--后端小组工建",
                "localCode": "FS-2020-440604-01-01-000004",
                "applyStatus": "4",
                "stageId": "736224e5-68fd-47b1-9445-f08ad58b057f",
                "projFlag": null,
                "childProjStatusVos": null
              }]
            }]
        ],
        "projStatusTreeStageVo": {
          "stageId": null,
          "stageName": null,
          "applyStatus": null
        }
      },
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
      debugger;
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
            _that.$nextTick(function(){
              var projTreeItem = $('.projTree-item');
              for(var i=0;i<projTreeItem.length;i++){
                var childrenLength = $(projTreeItem[i]).attr('childrenLength');
                var keyIndex = $(projTreeItem[i]).attr('keyIndex');
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
                    offsetTop = $('#'+parentProjInfoId).offset().top;
                    console.log($('#'+parentProjInfoId).offset().top)
                  }else {
                    if(parentProjInfoIdBefore!=''&&parentProjInfoIdBefore!==parentProjInfoId){
                      $(projTreeItem[i-1]).find('.line').hide();
                    }else if(stageLength==keyIndex){
                      $(projTreeItem[i]).find('.line').hide();
                    }
                    console.log($(projTreeItem[i-1]).offset().top)
                    offsetTop = ($(projTreeItem[i-1]).offset().top)+190;
                  }
                  console.log(offsetTop);
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