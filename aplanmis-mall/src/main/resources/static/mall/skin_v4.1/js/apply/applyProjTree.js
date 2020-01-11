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
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      loading: false, // 全屏loading
      // (0:未申报1:申报中2:已办结3:未审核4:已审核)
      projinfoDetailTree: {
        "dueNum": 120,
        "investSum": 1000,
        "localCode": "2017-440607-70-03-006747",
        "nstartTime": "2019-12-14",
        "projId": "",
        "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
        "projStatusVos": [
          {
            "applyStatus": "2",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "1"
          },
          {
            "applyStatus": "2",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-1",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "2"
          },
          {
            "applyStatus": "2",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-2",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "2"
          },
          {
            "applyStatus": "1",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-3",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "2"
          },
          {
            "applyStatus": "2",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-1-1",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "3"
          },
          {
            "applyStatus": "3",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-1-2",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "3"
          },
          {
            "applyStatus": "4",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-1-3",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "3"
          },
          {
            "applyStatus": "2",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-2-1",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "3"
          },
          {
            "applyStatus": "2",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-2-2",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "3"
          },
          {
            "applyStatus": "2",
            "localCode": "2017-440607-70-03-006747",
            "projInfoId": "1-3-1",
            "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
            "stageId": "3"
          }
        ],
        "stagesVos": [
          {
            "applyStatus": "2",
            "stageId": "1",
            "stageName": "立项用地规划许可阶段"
          },
          {
            "applyStatus": "2",
            "stageId": "2",
            "stageName": "工程建设许可阶段"
          },
          {
            "applyStatus": "1",
            "stageId": "3",
            "stageName": "施工许可阶段"
          },
          {
            "applyStatus": "3",
            "stageId": "4",
            "stageName": "竣工验收阶段"
          }
        ],
        "themeName": "政府投资类房屋建筑项目"
      },
      projStatusTree1: [],
      projStatusTree2: [],
      projStatusTree3: [],
      projStatusTree4: [],
      stageId1: '',
      stageId2: '',
      stageId3: '',
      stageId4: '',
    }
  },
  mounted: function () {

  },
  created: function () {

  },
  methods: {
    // 项目树数据处理
    setProjTreeData: function () {
      var _that = this;
      _that.projStatusTree1 = [];
      _that.projStatusTree2 = [];
      _that.projStatusTree3 = [];
      _that.projStatusTree4 = [];
      _that.stageId1 = _that.stagesVos[0].stageId?_that.stagesVos[0].stageId:'';
      _that.stageId2 = _that.stagesVos[1].stageId?_that.stagesVos[1].stageId:'';
      _that.stageId3 = _that.stagesVos[2].stageId?_that.stagesVos[2].stageId:'';
      _that.stageId4 = _that.stagesVos[3].stageId?_that.stagesVos[3].stageId:'';
      _that.projinfoDetailTree.projStatusVos.map(function(item){
        if(item.stageId== _that.stageId1){
          _that.projStatusTree1.push(item);
        }
        if(item.stageId== _that.stageId2){
          _that.projStatusTree2.push(item);
        }
        if(item.stageId== _that.stageId3){
          _that.projStatusTree3.push(item);
        }
        if(item.stageId== _that.stageId4){
          _that.projStatusTree4.push(item);
        }
      })
    }

  }
});
