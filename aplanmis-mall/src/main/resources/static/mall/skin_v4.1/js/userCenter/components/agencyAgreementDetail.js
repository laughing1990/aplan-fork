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

    // 当前项目-从列表带过来的数据
    curProj: {},

    // 代办协议
    agencyAgreementList: [],

    filePreviewCount: 0, // 查询预览是否成功次数
  },
  methods: {
    // 公共方法
    // 处理接口message
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
    },
    // 填充表格
    tableCellFormat: function (row, column, cellValue, index) {
      if (!cellValue && cellValue != 0) return '-';
      return cellValue;
    },

    // 获取当前代办项目的信息
    fetchProjInfo: function () {
      var ts = this,
        _url = ctx + 'rest/user/apply/agent/detail';
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
        projInfo = data.projAgentParamVo;
      var formData = $.extend({}, projInfo, aeaUnitProjLinkmanVo);
      for (var k in this.projInfoForm) {
        if (formData[k]) {
          this.projInfoForm[k] = formData[k];
        }
      }
      this.projInfoForm.agentStageState = data.agentStageState;
      this.agencyAgreementList = data.agentAgreement;
      this.dicFormat(this.projInfoForm.financialSource, 'XM_ZJLY', 'financialSourceCn');
      this.dicFormat(this.projInfoForm.landSource, 'XM_TDLY', 'landSourceCn');
      this.dicFormat(this.projInfoForm.projLevel, 'XM_PROJECT_LEVEL', 'projLevelCn');
      this.agentStageStateFormat(this.projInfoForm.agentStageState);
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

    // 待签章时-提交代办协议
    submitAgentAgreement: function () {
      var ts = this,
        _url = ctx + 'rest/user/apply/agent/submitAgentAgreement/' + ts.curProj.applyAgentId;
      ts.dloading = true;
      request('', {
        url: _url,
        type: 'post',
      }, function (res) {
        ts.dloading = false;
        if (res.success) {
          ts.returnList();
          return ts.apiMessage('提交成功！', 'success')
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.dloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 处理资金来源+土地性质+项目级别
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

    // 处理委托具体委托事项阶段
    agentStageStateFormat: function (data) {
      var opt = {
          '1': '立项用地规划许可阶段',
          '2': '工程建设许可阶段',
          '3': '施工许可阶段',
          '4': '竣工验收阶段',
        },
        allList = data.split(','),
        cnList = [];
      allList.forEach(function (item) {
        cnList.push(opt[item])
      })
      this.projInfoForm.agentStageStateCn = cnList.join(',');
    },

    // 代办项目-各个状态下的样式
    agencyStatusTagClassFn: function () {
      var state = this.curProj.projAgentState;
      if (state == 1) {
        return {
          color: '#f24040',
        }
      } else if (state >= 4) {
        return {
          color: '#00a854',
        }
      } else {
        return {
          color: '#f4a242',
        }
      }
    },

    // 下载电子件
    downloadFile: function (detailIds) {
      window.open(ctx + 'rest/file/applydetail/mat/download/' + detailIds)
    },

    // 预览电子件
    // 文件预览
    previewFile: function (id) {
      if (!id) return;
      if (window.frames.length != parent.frames.length) {
        window.parent.open(ctx + 'rest/mats/att/preview/' + id);
      } else {
        window.open(ctx + 'rest/mats/att/preview/' + id);
      }

    },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase(); //后缀名
      return suffix;
    },
    // 预览电子件
    filePreview: function (data, flag) { // flag==pdf 查看施工图
      var detailId = data.fileId;
      var _that = this;
      var regText = /doc|docx|pdf|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.fileName;
      var fileType = this.getFileType(fileName);
      var flashAttributes = '';
      _that.filePreviewCount++
      if (flag == 'pdf') {
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + detailId;
        }, 1000)
      } else {
        if (fileType == 'pdf') {
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
          }, 1000)
        } else if (regText.test(fileType)) {
          // previewPdf/pdfIsCoverted
          _that.dloading = true;
          request('', {
            url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
            type: 'get',
          }, function (result) {
            if (result.success) {
              _that.dloading = false;
              var tempwindow = window.open(); // 先打开页面
              setTimeout(function () {
                tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
              }, 1000)
            } else {
              if (_that.filePreviewCount > 9) {
                confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                  _that.filePreviewCount = 0;
                  _that.filePreview(data);
                }, function () {
                  _that.filePreviewCount = 0;
                  _that.dloading = false;
                  return false;
                }, '确定', '取消', 'warning', true)
              } else {
                setTimeout(function () {
                  _that.filePreview(data);
                }, 1000)
              }
            }
          }, function (msg) {
            _that.dloading = false;
            _that.$message({
              message: '文件预览失败',
              type: 'error'
            });
          })
        } else {
          _that.dloading = false;
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
          }, 1000)
        }
      }
    },

    // 返回我的项目列表
    returnList: function () {
      pager.checkData.pageNum = 1;
      pager.isShowAddProjPandel = false;
      pager.fetchMyProjList();
    },
  },
  created: function () {
    // ts.fetchProjInfo();
    this.curProj = pager.curHandelProj;
  },
  mounted: function () {
    this.fetchDicContents();
  },
  filters: {
    unitNatureFormat: function (val) {
      var cnList = ['企业', '事业单位', '社会组织'];
      return cnList[val] || '-'
    },
    projAgentStateFormat: function (val) {
      var tagCn = ['待签订', '签订中', '待签章', '已签订', '已终止'];
      return tagCn[val - 1]
    },
  },
})