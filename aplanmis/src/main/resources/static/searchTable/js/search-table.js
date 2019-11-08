/*
 * @Author: ZL
 * @Date:   2019/05/15
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#searchTable',
  data: function () {
    return {
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      tableData: [ // table 数据
        {
          soruce: '窗口申报',
          applyType: '并联',
          applyCode: '2019091921354736',
          projName: '工业厂房建设',
          applyMan: '吴维',
          stageItem: {
            stageName: '工程建设工程建设工程建设工程建设工程建设',
            itemName: '工程建设事项工程建设事项工程建设事项超出…',
          },
          message: [
            {
              time: '2019-10-08 09:50',
              name: '段涛',
              label: '催办XXX事项审批'
            },
            {
              time: '2019-10-08 09:50',
              name: '段涛',
              label: '催办XXX事项审批'
            },
            {
              time: '2019-10-08 09:50',
              name: '段涛',
              label: '催办XXX事项审批'
            },
            {
              time: '2019-10-08 09:50',
              name: '段涛',
              label: '催办XXX事项审批'
            }
          ],
          status: '办结',
          deadline: '1',
          startTime: '2019-09-19 16:45',
          endTime: '2019-09-19 16:45',
        },
        {
          soruce: '窗口申报',
          applyType: '单项',
          applyCode: '2019091921354736',
          projName: '梁村镇大成岗幼儿园',
          applyMan: '李飞',
          stageItem: {
            stageName: '',
            itemName: '普通燃气设施建设工程竣工验收备案',
          },
          status: '办结',
          deadline: '2',
          startTime: '2019-09-20 17:02',
          endTime: '2019-09-21 16:45',
        },
        {
          soruce: '窗口申报',
          applyType: '并联',
          applyCode: '2019091921354736',
          projName: 'XX工程建设1期',
          applyMan: '徐秀兰',
          stageItem: {
            stageName: '',
            itemName: '建设工程竣工验收备案',
          },
          status: '窗口收件',
          deadline: '-1',
          startTime: '2019-09-19 16:45',
          endTime: '2019-09-19 16:45',
        },
        {
          soruce: '窗口申报',
          applyType: '并联',
          applyCode: '2019091921354736',
          projName: 'XX工程建设2期',
          applyMan: '张晋',
          stageItem: {
            stageName: '工程竣工',
            itemName: '',
          },
          status: '窗口收件',
          deadline: '0',
          startTime: '2019-09-19 16:45',
          endTime: '2019-09-19 16:45',
        }
      ],
      searchFrom: { // 查询表单数据
        keyword: '',
        soruce: '',
        applyType: '0',
        theme: '',
        deadline: '0',
        applyTimeStr: '',
        applyTimeEnd: '',
        arrTimeStr: '',
        arrTimeEnd: '',
      },
      showMoreSearchItem: false, // 是否展示更多查询条件
    }
  },
  mounted: function () {

  },
  methods: {

  }
});