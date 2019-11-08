var app = new Vue({
  el: '#app',
  data: function() {
    return {
      dialogTitie: '',
      pageLoading: false,
      formData: {},
      total: 0,
      tableData: [],
      activeNames: '1',
      fieldType: [],
      landAreaUnitSite: [],
      moneyType: '人民币',
      proType: [],
      CertifiType: [],
      jobTitle: [],
      prjType: [],
      linkmanType: [],
      person: [],
      rules: {
        provinceProjCode: [
          { required: true, message: '请输入省级项目编号' },
        ],
        contractCode: [
          { required: true, message: '请输入合同编号' },
        ],
        contractType: [
          { required: true, message: '请选择合同类别' },
        ],
        contractMoeny: [
          { required: true, message: '请输入合同金额' },
        ],
        contractSignTime: [
          { required: true, message: '请选择合同签订日期' },
        ],
        contractConfirmTime: [
          { required: true, message: '请选择合同确认时间' },
        ],
        govOrgCode: [
          { required: true, message: '请输入合同确认的行政单位机构代码' },
        ],
        govOrgName: [
          { required: true, message: '请输入合同确认的行政单位名称' },
        ],
        govOrgAreaCode: [
          { required: true, message: '请输入合同确认的行政单位区域码' },
        ]
      }

    }
  },
  created: function() {
    // this.getXmDwlx();
    // this.getJobTitle();
    // this.getPrjType();
    // this.getLinkmanType();
    // this.getCertifiType();
    this.getAllType();
    this.showData();

    $(".loading").hide();
  },
  methods: {
    // 请求各个类型数据
    getAllType: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'rest/dict/code/multi/items/list',
        data: {
          dicCodeTypeCodes: 'XM_DWLX,JOB_TITLE,C_PRJ_SPTY,PROJ_UNIT_LINKMAN_TYPE,REGIST_CERTIFI_TYPE'
        },
      }, function(res) {
        vm.proType = res.content.XM_DWLX; //请求项目主体类型
        vm.jobTitle = res.content.JOB_TITLE; //请求职称等级
        vm.prjType = res.content.C_PRJ_SPTY; //请求审查人员专业
        vm.linkmanType = res.content.PROJ_UNIT_LINKMAN_TYPE; //请求项目单位联系人类型
        vm.CertifiType = res.content.REGIST_CERTIFI_TYPE; //请求执业注册证类型
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 模糊查询人员
    getLinkMan: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'post',
        url: ctx + 'aea/ex/proj/drawing/list',
        data: {
          keyword: ''
        },
      }, function(res) {
        vm.fieldType = res;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 模糊查询人员
    getPerson: function(val) {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'post',
        url: ctx + 'rest/linkman/list',
        data: {
          keyword: keyword1,
          unitInfoId: val.unitInfoId,
          projInfoId: val.projInfoId
        },
      }, function(res) {
        vm.person = res;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 请求table数据
    showData: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'post',
        url: ctx + 'aea/ex/proj/drawing/index.do',
        data: {
          projInfoId: '031fb1ad-71b9-41af-8fe0-7df5b449a16d'
        },
      }, function(res) {
        vm.formData = res.content;

        // vm.$nextTick(function() {
        //   vm.$refs['form'].clearValidate();

        // });
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    formatNumber: function(n) {
      n = n.toString()
      return n[1] ? n : '0' + n
    },
    formatTime: function(number, format) {

      var formateArr = ['Y', 'M', 'D'];
      var returnArr = [];

      var date = new Date(number);
      returnArr.push(date.getFullYear());
      returnArr.push(this.formatNumber(date.getMonth() + 1));
      returnArr.push(this.formatNumber(date.getDate()));

      for (var i in returnArr) {
        format = format.replace(formateArr[i], returnArr[i]);
      }
      return format;
    },
    save: function() {
      var _this = this;
      this.$refs['form'].validate(function(valid) {
        if (!valid) return false;
        var param = {
          projInfoId: 'a',
          contractId: _this.formData.contractId || '',
          provinceProjCode: _this.formData.provinceProjCode || '',
          contractCode: _this.formData.contractCode || '',
          contractType: _this.formData.contractType || '',
          contractMoeny: _this.formData.contractMoeny || '',
          contractSignTime: _this.formatTime(_this.formData.contractSignTime, 'Y-M-D') || '',
          contractConfirmTime: _this.formatTime(_this.formData.contractConfirmTime, 'Y-M-D') || '',
          govOrgCode: _this.formData.govOrgCode || '',
          govOrgName: _this.formData.govOrgName || '',
          govOrgAreaCode: _this.formData.govOrgAreaCode || ''
        }
        request('', {
          type: 'post',
          url: ctx + 'rest/form/contra/saveAeaExProjDrawing.do',
          data: param
        }, function(res) {
          if (res.success) {
            _this.$message({
              message: '保存成功',
              type: 'success'
            });
            _this.showData();
          } else {
            _this.$message({
              message: '保存失败',
              type: 'error'
            });
          }
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })

    },
    // 解决sleect选不中值问题
    forceUpdate: function() {
      this.$forceUpdate()
    },
  },
  watch: {

  }
})