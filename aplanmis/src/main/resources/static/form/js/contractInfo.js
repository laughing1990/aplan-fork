var app = new Vue({
  el: '#app',

  data: function() {
    // 输入为数字 大于等于0（浮点数）
    var checkNumFloat = function(rule, value, callback) {
      if (value) {
        var flag = !/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(value);
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          callback();
        }

      } else {
        callback();
      }
    };
    return {
      dialogTitie: '',
      pageLoading: false,
      formData: {},
      total: 0,
      tableData: [],
      activeNames: '1',
      fieldType: [],
      landAreaUnitSite: [],
      region: [],
      moneyType: '万元',
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
          { validator: checkNumFloat, trigger: ['blur'] },
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
    this.projInfoId = this.getUrlParam('projInfoId');


  },
  mounted: function() {

    this.getContractType();
    this.showData();
    this.getRegion();

    $(".loading").hide();
  },

  methods: {
    // 获取页面的URL参数
    getUrlParam: function(val) {
      var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
      return svalue ? svalue[1] : svalue;
    },
    // 请求table数据
    getContractType: function() {
      var vm = this;
      // vm.loading = true;

      request('', {
        type: 'get',
        url: ctx + 'rest/dict/code/multi/items/list',
        data: {
          dicCodeTypeCodes: 'contract_type'
        },
      }, function(res) {
        vm.fieldType = res.content.contract_type;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 请求下拉
    getRegion: function() {
      var vm = this;
      // vm.loading = true;

      request('', {
        type: 'get',
        url: ctx + 'rest/org/region/list',
        data: {},
      }, function(res) {
        vm.region = res.content;
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
        url: ctx + 'rest/form/contra/getAeaExProjContract.do',
        data: {
          projInfoId: this.projInfoId
        },
      }, function(res) {
        if (!res.success) {
          vm.$message({
            message: res.message,
            type: 'error'
          });

          return;
        }
        vm.formData = res.content;

        vm.$nextTick(function() {
          vm.$refs['form'].clearValidate();

        });
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
          projInfoId: _this.projInfoId,
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
          url: ctx + 'rest/form/contra/saveAeaExProjContract.do',
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