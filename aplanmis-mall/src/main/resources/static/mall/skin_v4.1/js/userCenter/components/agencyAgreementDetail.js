var module = new Vue({
  el: '#agencyAgreementDetail',
  data: {
    dloading: false,
    // 页面项目信息
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
      // nstartTime: '', //项目开工时间
      xzydmj: '', //新增用地面积
      aboveGround: '', //地上面积
      underGround: '', //地下面积
      // endTime: '', //项目建成时间
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
      gbCodeYear: '',
      foreignBuildingArea: '',
      isDesignSolution: '0',
      projectAddress: '',
      isAreaEstimate: '0',
      areaDetailCode: '', //建设地点详情
      currentProgress: '', //项目当前进度
      agentStageState: [], //具体委托阶段

      // 项目单位信息
      applicant: '', //单位名称
      applicantDetailSite: '', //单位地址
      email: '', // 电子邮箱
      unitNature: '', //单位性质
      leaderName: '', //项目负责人
      leaderMobilePhone: '', //联系方式
      leaderDuty: '', //负责人职务
      operatorName: '', //项目经办人
      operatorMobilePhone: '', //经办人联系方式
      operatorDuty: '' //经办人职务
    },

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
  },
  methods: {
    // 获取当前代办项目的信息
    fetchProjInfo: function () {
      var ts = this,
        _url = ctx + 'rest/apply/agent/detail';
      ts.dloading = true;
      request('', {
        url: _url,
        type: 'get',
        data: {
          projInfoId: pager.curHandelProj.projInfoId,
          applyAgentId: pager.curHandelProj.applyAgentId
        }
      }, function (res) {
        ts.dloading = false;
        if (res.success) {
          ts.handelProjInfoData(res.content)
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.dloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    // 处理当先代办项目信息的数据回显
    handelProjInfoData: function (data) {
      var aeaUnitProjLinkmanVo = data.aeaUnitProjLinkmanVo,
        projInfo = data.projInfo;
      var formData = $.extend({}, projInfo, aeaUnitProjLinkmanVo);
      for (var k in this.projInfoForm) {
        if (formData[k]) {
          this.projInfoForm[k] = formData[k];
        }
      }
      this.projInfoForm.agentStageState = data.agentStageState;
      this.dicFormat(this.projInfoForm.financialSource, 'XM_ZJLY', 'financialSourceCn');
      this.dicFormat(this.projInfoForm.landSource, 'XM_TDLY', 'landSourceCn');
    },

    // 获取待选项
    fetchDicContents: function () {
      var ts = this;
      ts.dloading = true;
      request('', {
        url: ctx + 'rest/user/getDicContents',
        type: 'get',
      }, function (res) {
        ts.dloading = false;
        if (res.success) {
          ts.proJAllOptions = res.content;
          ts.fetchProjInfo();
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.dloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 处理资金来源+土地性质
    dicFormat: function (val, type, target) {
      var ts = this,
        str = '',
        all = ts.proJAllOptions[type];
      for (var i = 0, ln = all.length; i < ln; i++) {
        if (val == all[i].itemCode) {
          str = all[i].itemName;
          break;
        }
      }
      ts.projInfoForm[target] = str;
    },
  },
  created: function () {
    // ts.fetchProjInfo();
  },
  mounted: function () {
    this.fetchDicContents();

  },
  filters: {
    unitNatureFormat: function(val){
      var cnList = ['企业','事业单位','社会组织'];
      return cnList[val] || '-'
    },
  },
})