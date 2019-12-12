// 校验数字
var checkNumber = function (rule, value, callback) {
  var reg = /[^\d^\.]+/g
  // console.log(reg.test(value));
  if (!reg.test(value)) {
    callback();
  } else {
    return callback(new Error('请输入数字'));
  }
};
var module1 = new Vue({
  el: "#splitProject",
  data: function () {
    return {
      // 全局loading
      mloading: false,
      ctx: ctx,
      // 当前编辑的项目-从列表里面过来的，不是子项目里面的
      curEditProjData: {},

      // 子项目列表
      sonProjList: [],
      // 子项目列表查询数据
      sonProjSearchData: {
        pageSize: 10,
        pageNum: 1
      },
      // 子项目列表总数
      sonProjTotal: 0,
      selectFlagForm:{
        stageFlag:''//选择阶段
      },
      // 项目基本信息
      projInfoForm: {
        gcbm: '', //工程编码
        localCode: '', //项目代码
        projName: '', //项目名称
        investSum: '', //总投资
        xmYdmj: '', //用地面积
        investType:'',//投资类型
        theIndustry:'',//行业类别
        landSource:'',//土地来源
        projType: '', //项目类型
        projectDept:'',//立项部门
        financialSource:'',//资金来源
        isForeign:'',//是否外资
        projLevel:'',//重点项目
        projNature: '', //建设性质
        nstartTime: '', //项目开工时间
        xzydmj:'',//新增用地面积
        aboveGround:'',//地上面积
        underGround:'',//地下面积
        endTime: '', //项目建成时间
        projCategory:'',//工程分类
        foreignApproveNum: '', //备案文号
        buildAreaSum: '', //建筑面积
        district:'',//行政区划
        projAddr: '', //项目地址
        eastScope:'',//东至
        southScope:'',//西至
        westScope:'',//南至
        northScope:'',//北至
        scaleContent: '', //建设内容
        themeId:'',//主题ID
        foreignRemark: '', //备注
        projInfoId: '', //项目id 注意：有项目id是编辑原来项目，没有时是分成子项目时
        parentProjId: '',  //父项目的项目id
        gbCodeYear: '2017',
        foreignBuildingArea: '',
        isDesignSolution: '0',
        projectAddress: '',
        isAreaEstimate: '0',
        regionalism: '',
      },

      stageRules: {
        stageFlag: [
          {required: true, message: '请选择阶段', trigger: 'change'},
        ],
      },
      // 项目基本信息-校验规则，必填
      rules: {
        projName: [
          {required: true, message: '请输入项目/工程名称', trigger: 'blur'},
        ],
        localCode: [
          {required: true, message: '请输入项目代码', trigger: 'blur'},
        ],
        gcbm: [
          {required: true, message: '请输入工程代码', trigger: 'blur'},
        ],
        regionalism: [
          {required: true, message: '请选择行政区划', trigger: 'change'},
        ],
        projectAddress: [
          {required: true, message: '请选择建设地点', trigger: 'change'},
        ],
        financialSource: [
          {required: true, message: '请选择资金来源', trigger: 'change'},
        ],
        investType: [
          {required: true, message: '请选择投资类型', trigger: 'change'},
        ],
        landSource: [
          {required: true, message: '请选择土地来源', trigger: 'change'},
        ],
        projType: [
          {required: true, message: '请选择立项类型', trigger: 'change'},
        ],
        themeId: [
          {required: true, message: '请选择项目类型', trigger: 'change'},
        ],
        projNature: [
          {required: true, message: '请选择建设性质', trigger: 'change'},
        ],
        projCategory: [
          {required: true, message: '请选择工程分类', trigger: 'change'},
        ],
        nstartTime: [
          {required: true, message: '请选择拟开工时间', trigger: 'blur'},
        ],
        endTime: [
          {required: true, message: '请选择拟建成时间', trigger: 'change'},
        ],
        investSum: [
          {required: true, message: '请输入总投资', trigger: 'blur'},
        ],
        foreignBuildingArea: [
          {required: true, message: '请输入总建筑面积', trigger: 'blur'},
        ],
        xmYdmj: [
          {required: true, message: '请输入用地面积', trigger: 'blur'},
        ],
        gbCodeYear: [
          {required: true, message: '请输入国标行业代码发布年代', trigger: 'blur'},
        ],
        foreignRemark: [
          {required: true, message: '请输入建设规模及内容', trigger: 'blur'},
        ]
      },
      themeList: [], // 主题列表
      orgList: [], // 组织列表
      districtList:[],//行政区划
      // 当前保存的是子项目还是原来编辑的项目
      isSaveChildProj: false,

      // 项目基本信息的-项目类型跟建设性质的下拉选项
      proJAllOptions: {
        XM_PROJECT_STEP: [], //项目类型
        XM_NATURE: [], //建设性质
        XM_PROJECT_LEVEL:[],//项目级别
        XM_PROJECT_STEP:[],//立项类型
        XM_TZLX: [], // 投资类型
        XM_ZJLY: [], // 资金来源
        XM_GBHY: [], // 行业类别
        XM_TDLY: [], // 土地来源
        XM_NATURE: [], // 建设性质
        XM_GCFL: [] // 工程分类
      },
      gbhySelectData: [], // 国标行业下拉选项数据
      gbhyProp: {
        children: 'children',
        label: 'itemName'
      }, // 树配置信息，节点属性以及显示文案的属性
      gbhyShowMsg: '', // 国标行业选中数据的展示
      isShowGbhy: false, // 是否显示国标行业tree模块
      stageFlag:'',//拆分子项目时选择的阶段
      selectStageFlag:false,//是否展示选择阶段窗口
      selectStageList:{1:'工程规划阶段',2:'施工许可阶段'}
    }
  },
  created: function () {
    this.getGbhy();
      this.getRegionList();
      this.querySelecTheme();
  },
  methods: {
    closeGbhyTree: function () {
      this.isShowGbhy = false;
    },
    getGbhy: function () {
      var _that = this;
      request('', {
        url: ctx + 'bsc/dic/code/getItemTreeByTypeId.do',
        type: 'get',
        data: {typeId: 'fadff496-cde1-4c72-90b8-766744b18cb9'},
      }, function (result) {
        if (result) {
          var arr = result;

          if (arr.length) {
            for (var i = 0; i < arr.length; i++) {
              arr[i].disabled = true;
            }
          }
          _that.gbhySelectData = arr;

          _that.gbhyShowMsg = _that.getNames(_that.gbhySelectData);
          _that.gbhyShowMsg = _that.gbhyShowMsg.substr(0, _that.gbhyShowMsg.length - 1);
        }
      }, function (msg) {
      })
    },
    handleCheckChange: function (data, checked, indeterminate) {
      console.log('i am here')
      // console.log(data, checked, indeterminate);
      var arr = this.$refs.gbhy.getCheckedNodes(true);
      var str = [];
      var ids = [];
      for (var i = 0; i < arr.length; i++) {
        str.push(arr[i].itemName);
        ids.push(arr[i].itemCode);
      }
      this.gbhyShowMsg = str.join(',');
      this.projInfoForm.theIndustry = ids.join(',');
    },
    //联动操作
    // projTypeChange:function(value){
    //   var _that=this;
    //   if(value!=null&&""!=value) {
    //     _that.querySelecTheme(value);
    //   }
    // },
      querySelecTheme: function () {
        var ts = this;
      request('', {
        url: ctx + 'rest/user/getThemes',
        type: 'get',
        //data: {themeType:themeType}
      }, function (result) {
        if (result.success) {
          ts.themeList=result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
      //获取部门组织
      // querySelectDept: function (rootOrgId) {
      //     var _that=this;
      //     request('', {
      //         url: ctx + 'rest/user/getOrgs',
      //         type: 'get',
      //         data: {rootOrgId: rootOrgId}
      //     }, function (result) {
      //         if (result.success) {
      //             _that.orgList=result.content;
      //         }
      //     }, function (msg) {
      //         alertMsg('', '网络加载失败！', '关闭', 'error', true);
      //     })
      // },

    // 公共方法
    // 处理接口message
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
    },
    // 返回到项目列表（非子项目）
    returnProjList: function () {
      vm.hideSplitProjectModule();
      this.isSaveChildProj = false;
    },

    // 获取待选项（项目类型-建设性质）
    fetchDicContents: function () {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/getDicContents',
        type: 'get',
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.proJAllOptions = res.content;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 子项目相关
    // 获取子项目列表
    fetchSonProjList: function () {
      var ts = this;
      ts.mloading = true;
      ts.sonProjSearchData.aeaProjInfoId = ts.curEditProjData.projInfoId;
      request('', {
        url: ctx + 'rest/user/getChildProjList',
        type: 'get',
        data: ts.sonProjSearchData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.sonProjList = res.content.list;
          ts.sonProjTotal = res.content.total;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    // 列表页切换、
    handleCurrentChange: function (val) {
      this.sonProjSearchData.pageNum = val;
      this.fetchSonProjList();
    },
    // 列表页大小切换
    handleSizeChange: function (val) {
      this.sonProjSearchData.pageSize = val;
      this.fetchSonProjList()
    },

    // 项目详情相关
    // 对获取到的编辑项目的基本信息的处理(数据从项目列表里面获取)
    handelCurEditProjDetailData: function (data) {
      var ts = this;
      // 如果当前编辑的项目是没有工程编码-那就将项目代码赋值给工程编码
      if (!data.gcbm || data.gcbm == null) {
        data.gcbm = data.localCode;
      }
        debugger
      for (var k in this.projInfoForm) {
        if (k == 'projectAddress') {
          if (!!data.projectAddress) this.projInfoForm.projectAddress = data.projectAddress.split(',');
        } else if (k == 'theIndustry') {
          if (!!data.theIndustry) {
            this.$refs.gbhy.setCheckedKeys(data.theIndustry.split(','));
          }
        } else if (data[k]) {
          this.projInfoForm[k] = data[k];
        } else {
          this.projInfoForm[k] = "";
        }
      }
    },
    getNames: function (arr) {
      var _that = this;
      var str = '';
      var nameArr = _that.curEditProjData.theIndustry.split(',');
      if (arr && arr.length) {
        arr.forEach(function (t, i) {
          if (t.children && t.children.length) {
            str += _that.getNames(t.children)
          } else {
            if (nameArr.indexOf(t.itemCode) > -1) {
              str += t.itemName + ','
            }
          }
        })
      }
      return str
    },
    splitProject:function(){
      this.selectStageFlag = true;
    },
    // 拆分子项目
    clickStageFlag: function () {
      var ts = this,
        _splitData = {};
      _splitData.projInfoId = ts.curEditProjData.projInfoId;
      _splitData.gcbm = ts.curEditProjData.gcbm;
      _splitData.projName = ts.curEditProjData.projName;
      _splitData.localCode = ts.curEditProjData.localCode;
      _splitData.stageFlag = ts.selectFlagForm.stageFlag;

      // ts.$refs['selectFlagForm'].validate(function(valid){
      //     if (valid) {
      //         ts.selectStageFlag = false;
      //         ts.mloading = true;
      //         request('', {
      //             url: ctx + 'rest/user/getChildProject',
      //             type: 'get',
      //             data: _splitData
      //         }, function (res) {
      //             ts.mloading = false;
      //             if (res.success) {
      //                 // 拆分成功后，工程编码，项目名称改成拆出来的子项目的值，同时删除projInfoForm表单中projInfoId；并申明现在是保存的是子项目的信息
      //                 ts.projInfoForm.gcbm = res.content.gcbm;
      //                 ts.projInfoForm.gcbm = res.content.gcbm;
      //                 ts.projInfoForm.projName = res.content.projName;
      //                 if (!!res.content.projectAddress) {
      //                     ts.projInfoForm.projectAddress = res.content.projectAddress.split(',');
      //                 }
      //                 delete ts.projInfoForm.projInfoId;
      //                 ts.isSaveChildProj = true;
      //                 // return ts.apiMessage('拆分成功！', 'success')
      //             } else {
      //                 return ts.apiMessage(res.message, 'error')
      //             }
      //         }, function () {
      //             ts.mloading = false;
      //             return ts.apiMessage('网络错误！', 'error')
      //         });
      //     }
      // })

      ts.selectStageFlag = false;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/getChildProject',
        type: 'get',
        data: _splitData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          // 拆分成功后，工程编码，项目名称改成拆出来的子项目的值，同时删除projInfoForm表单中projInfoId；并申明现在是保存的是子项目的信息
          ts.projInfoForm.gcbm = res.content.gcbm;
          ts.projInfoForm.gcbm = res.content.gcbm;
          ts.projInfoForm.projName = res.content.projName;
          if (!!res.content.projectAddress) {
            ts.projInfoForm.projectAddress = res.content.projectAddress.split(',');
          }
          delete ts.projInfoForm.projInfoId;
          ts.isSaveChildProj = true;
          // return ts.apiMessage('拆分成功！', 'success')
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 保存操作isSaveChildProj： false保存的是原来的项目， true保存的是拆出来的项目的数据
    saveProjData: function () {
      var ts = this,
        _saveData = ts.projInfoForm;
      // console.log(_saveData)
      // 如果是编辑原项目，就不传父项目id
      if(ts.isSaveChildProj){
        ts.projInfoForm.parentProjId = ts.curEditProjData.projInfoId;
      }
      if (!!ts.projInfoForm.projectAddress) {
        _saveData.projectAddress = ts.projInfoForm.projectAddress.join(',');
      }
      // return
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/saveEditedProject',
        type: 'post',
        data: _saveData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.apiMessage('保存成功！', 'success');
          ts.projInfoForm.projInfoId = res.content; //重新赋值id
          if (!!ts.projInfoForm.projectAddress) {
            ts.projInfoForm.projectAddress = ts.projInfoForm.projectAddress.split(',');
          }
          ts.fetchSonProjList();
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });  
    },
      getProjInfo: function (data) {
          var ts = this;
          request('', {
              url: ctx + 'rest/apply/common/projInfo/' + data.projInfoId,
              type: 'get',
              async: false
          }, function (result) {
              if (result.success) {
                  ts.curEditProjData = result.content;
              } else {
                  return ts.apiMessage(res.message, 'error')
              }
          }, function () {
              return ts.apiMessage('网络错误！', 'error')
          });
      },
      getRegionList: function () {
          var ts = this;
          request('', {
              url: ctx + 'rest/org/region/list',
              type: 'get'
          }, function (result) {
              if (result.success) {
                  ts.districtList = result.content;
              }
          }, function (msg) {
              alertMsg('', '网络加载失败！', '关闭', 'error', true);
          })
      }
  },
  mounted: function () {
    // 当前编辑的项目数据（从项目列表那里获取）
    var ts = this;
    this.curEditProjData = JSON.parse(JSON.stringify(vm.curEditProjRow));
      // 获取当前编辑的项目的项目基本信息
      this.getProjInfo(this.curEditProjData);
    // 获取待选项（项目类型-建设性质）
    this.fetchDicContents();
    // 获取当前编辑的项目的子项目列表
    this.fetchSonProjList();
    this.handelCurEditProjDetailData(this.curEditProjData);
  },
  filters: {

  },
})