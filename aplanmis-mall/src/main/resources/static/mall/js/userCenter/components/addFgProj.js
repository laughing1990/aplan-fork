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
  el: "#addFgProject",
  data: function () {
    return {
      // 全局loading
      mloading: false,
      ctx: ctx,
      // 项目基本信息
      projInfoForm: {
        // gcbm: '', //工程编码
        localCode: '', //项目代码
        projName: '', //项目名称
        investSum: '', //总投资
        xmYdmj: '', //用地面积
        investType: '', //投资类型
        theIndustry: '', //行业类别
        landSource: '', //土地来源
        projType: '', //项目类型
        projectDept: '', //立项部门
        financialSource: '', //资金来源
        isForeign: '', //是否外资
        projLevel: '', //重点项目
        projNature: '', //建设性质
        nstartTime: '', //项目开工时间
        xzydmj: '', //新增用地面积
        aboveGround: '', //地上面积
        underGround: '', //地下面积
        endTime: '', //项目建成时间
        projCategory: '', //工程分类
        foreignApproveNum: '', //备案文号
        buildAreaSum: '', //建筑面积
        district: '', //行政区划
        projAddr: '', //项目地址
        eastScope: '', //东至
        southScope: '', //西至
        westScope: '', //南至
        northScope: '', //北至
        scaleContent: '', //建设内容
        themeId: '', //主题ID
        foreignRemark: '', //备注
        projInfoId: '', //项目id 注意：有项目id是编辑原来项目，没有时是分成子项目时
        parentProjId: '', //父项目的项目id
        gbCodeYear: '2017',
        foreignBuildingArea: '',
        isDesignSolution: '0',
        projectAddress: '',
        isAreaEstimate: '0',
      },
      // 项目基本信息-校验规则，必填
      rules: {
        projName: [{
          required: true,
          message: '请输入项目名称',
          trigger: 'blur'
        }, ],
        localCode: [{
          required: true,
          message: '请输入项目代码',
          trigger: 'blur'
        }, ],
        // gcbm: [{
        //   required: true,
        //   message: '请输入工程代码',
        //   trigger: 'blur'
        // }, ],
        regionalism: [{
          required: true,
          message: '请选择行政区划',
          trigger: 'change'
        }, ],
        projectAddress: [{
          required: true,
          message: '请选择建设地点',
          trigger: 'change'
        }, ],
        financialSource: [{
          required: true,
          message: '请选择资金来源',
          trigger: 'change'
        }, ],
        investType: [{
          required: true,
          message: '请选择投资类型',
          trigger: 'change'
        }, ],
        landSource: [{
          required: true,
          message: '请选择土地来源',
          trigger: 'change'
        }, ],
        projType: [{
          required: true,
          message: '请选择立项类型',
          trigger: 'change'
        }, ],
        themeId: [{
          required: true,
          message: '请选择项目类型',
          trigger: 'change'
        }, ],
        projNature: [{
          required: true,
          message: '请选择建设性质',
          trigger: 'change'
        }, ],
        projCategory: [{
          required: true,
          message: '请选择工程分类',
          trigger: 'change'
        }, ],
        nstartTime: [{
          required: true,
          message: '请选择拟开工时间',
          trigger: 'blur'
        }, ],
        endTime: [{
          required: true,
          message: '请选择拟建成时间',
          trigger: 'change'
        }, ],
        investSum: [{
          required: true,
          message: '请输入总投资',
          trigger: 'blur'
        }, ],
        foreignBuildingArea: [{
          required: true,
          message: '请输入总建筑面积',
          trigger: 'blur'
        }, ],
        xmYdmj: [{
          required: true,
          message: '请输入用地面积',
          trigger: 'blur'
        }, ],
        gbCodeYear: [{
          required: true,
          message: '请输入国标行业代码发布年代',
          trigger: 'blur'
        }, ],
        foreignRemark: [{
          required: true,
          message: '请输入建设规模及内容',
          trigger: 'blur'
        }, ]
      },
      themeList: [], // 主题列表
      orgList: [], // 组织列表
      districtList: [], //行政区划
      // 当前保存的是子项目还是原来编辑的项目
      isSaveChildProj: false,

      // 项目基本信息的-项目类型跟建设性质的下拉选项
      proJAllOptions: {
        XM_PROJECT_STEP: [], //项目类型
        XM_NATURE: [], //建设性质
        XM_PROJECT_LEVEL: [], //项目级别
        XM_PROJECT_STEP: [], //立项类型
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

      // 从第三方网站注册好的项目代码
      thirdPartyProjCode: '',
    }
  },
  created: function () {
    this.getRegionListData();
    this.getGbhy();
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
        data: {
          typeId: 'fadff496-cde1-4c72-90b8-766744b18cb9'
        },
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
      }, function (msg) {})
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
    // projTypeChange: function (value) {
    //     var _that = this;
    //     if (value != null && "" != value) {
    //         _that.querySelecTheme(value);
    //     }
    // },
    querySelecTheme: function () {
      var ts = this;
      request('', {
        url: ctx + 'rest/user/getThemes',
        type: 'get',
        //data: {themeType: themeType}
      }, function (result) {
        if (result.success) {
          ts.themeList = result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    //获取部门组织
    querySelectDept: function (rootOrgId) {
      var _that = this;
      request('', {
        url: ctx + 'rest/user/getOrgs',
        type: 'get',
        data: {
          rootOrgId: rootOrgId
        }
      }, function (result) {
        if (result.success) {
          _that.orgList = result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },

    // 公共方法
    // 处理接口message
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
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

    // 项目详情相关
    // 对获取到的编辑项目的基本信息的处理(数据从项目列表里面获取)
    getRegionListData: function () {
      var ts = this;
      request('', {
        url: ctx + 'rest/org/region/list',
        type: 'get',
      }, function (result) {
        if (result.success) {
          ts.districtList = result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    getNames: function (arr) {
      var _that = this;
      var str = '';
      var nameArr = _that.projInfoForm.theIndustry.split(',');
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
    // 保存操作isSaveChildProj： false保存的是原来的项目， true保存的是拆出来的项目的数据
    saveProjData: function () {
      var ts = this;
      ts.mloading = true;
      var _saveData = ts.projInfoForm;
      if (!!ts.projInfoForm.projectAddress) {
        _saveData.projectAddress = ts.projInfoForm.projectAddress.join(',');
      }
      request('', {
        url: ctx + 'rest/user/saveProjectInfo',
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
          userCenter.vm.selectNav = "我的项目";
          localStorage.setItem('selectNav', userCenter.vm.selectNav);
          location.href = ctx + "rest/main/toIndexPage?#declare";
        } else {
          ts.projInfoForm.projectAddress = ts.projInfoForm.projectAddress.split(',');
          return ts.apiMessage("保存失败！", 'error')
        }
      }, function () {
        ts.mloading = false;
        ts.projInfoForm.projectAddress = ts.projInfoForm.projectAddress.split(',');
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    // 项目存档-即保存
    saveProjDataApi: function () {
      var ts = this;
      ts.$refs['projInfoForm'].validate(function (valid) {
        if (valid) {
          ts.mloading = true;
          var _saveData = ts.projInfoForm;
          if (!!ts.projInfoForm.projectAddress) {
            _saveData.projectAddress = ts.projInfoForm.projectAddress.join(',');
          }
          request('', {
            url: ctx + 'rest/user/saveProjectInfo',
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
              // 保存成功后，返回我的项目列表
              setTimeout(function () {
                ts.returnList();
              }, 1000)
            } else {
              ts.projInfoForm.projectAddress = ts.projInfoForm.projectAddress.split(',');
              return ts.apiMessage("保存失败！", 'error')
            }
          }, function () {
            ts.mloading = false;
            ts.projInfoForm.projectAddress = ts.projInfoForm.projectAddress.split(',');
            return ts.apiMessage('网络错误！', 'error')
          });
        } else {
          ts.apiMessage('请完善项目信息！', 'warning')
          return false;
        }
      });

    },

    // 根据项目代码获取新增的项目的基本信息
    fetchProjBaseInfo: function () {
      var ts = this,
        _url = ctx + 'rest/user/projInfo/thirdPlatform/' + ts.thirdPartyProjCode;
      ts.mloading = true;
      request('', {
        url: _url,
        type: 'get',
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.handelProjBaseInfo(res.content)
        } else {
          return ts.apiMessage('没有找到查询项目', 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    // 根据获取到的数据初始化
    handelProjBaseInfo: function (obj) {
      var ts = this,
        baseInfoForm = ts.projInfoForm;
      for (var k in baseInfoForm) {
        if (obj[k] && k !== 'projectAddress') {
          baseInfoForm[k] = obj[k];
        }
      }
      // 建设地点是数组，单独处理
      if (obj['projectAddress']) {
        ts.projInfoForm.projectAddress = obj['projectAddress'].join(',') || [];
      } else {
        ts.projInfoForm.projectAddress = [];
      }
    },

    // 返回我的项目列表
    returnList: function () {
      pager.isShowAddProjPandel = false;
      pager.fetchMyProjList();
    },

    // 打开新增本地项目pandel
    openAddLocalProjPandel: function(){
      pager.loadAddLocalProjPandel();
    },
  },
  mounted: function () {
    // 当前编辑的项目数据（从项目列表那里获取）
    // 获取待选项（项目类型-建设性质）
    this.fetchDicContents();
  },
  filters: {},
})