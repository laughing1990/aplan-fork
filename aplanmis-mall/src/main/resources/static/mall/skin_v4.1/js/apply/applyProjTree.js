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
      projinfoDetailTree: {
        "dueNum": 120,
        "investSum": 1000,
        "localCode": "2017-440607-70-03-006747",
        "nstartTime": "2019-12-14",
        "projId": "",
        "projName": "佛山市顺德区大良镇南芦村沙地元三路建设工程",
        "projStatusTreeStageVo": {
          "applyStatus": "",
          "stageId": "",
          "stageName": ""
        },
        "projStatusVo": [
          {
            "applyStatus": "",
            "childProjStatusVos": [
              {
                "applyStatus": "",
                "childProjStatusVos": [
                  {}
                ],
                "localCode": "",
                "projInfoId": "",
                "projName": "",
                "stageId": ""
              }
            ],
            "localCode": "",
            "projInfoId": "",
            "projName": "",
            "stageId": ""
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
    }
  },
  mounted: function () {

  },
  created: function () {

  },
  methods: {

  }
});
