var app = new Vue({
  el: '#app',
  data: function() {
    return {
      dialogTitie: '',
      multipleSelection: [],
      multipleSelection2: [],
      multipleSelection3: [],

      nounDialog: false,
      orgDialog: false,
      addWindowDialog: false,
      treeLeftHeight: document.documentElement.clientHeight - 500,
      pageLoading: false,
      loading: false,
      currentPage: 1,
      pagesize: 10,
      formData: {},
      formData2: {},
      formData3: {},
      total: 0,
      tableData: [],
      activeNames: '1',
      fieldType: [],
      landAreaUnitSite: [],
      rules: {
        certLandCode: [
          { required: true, message: '请输入建设项目用地规划许可证编号' },
        ],
        landNature: [
          { required: true, message: '请输入用地性质可用地性质ID', trigger: 'blur' },
        ],
        landAreaValue: [
          { required: true, message: '请输入用地面积' },
        ],
        govOrgCodeLand: [
          { required: true, message: '请输入发证机关机构代码' },
        ],
        govOrgNameLand: [
          { required: true, message: '请输入发证机关' },
        ],
        publishTimeLand: [
          { required: true, message: '请选择发证日期' },
        ]
      },
      rules2: {
        certProjectCode: [
          { required: true, message: '请输入建设工程规划许可证编号' },
        ],
        publishOrgCodeProject: [
          { required: true, message: '请输入核发机关组织机构代码' },
        ],
        publishOrgNameProject: [
          { required: true, message: '请输入核发机关' },
        ],
        publishTimeProject: [
          { required: true, message: '请选择核发日期' },
        ]
      },
      rules3: {
        siteCode: [
          { required: true, message: '请输入建设项目选址意见书批文号' },
        ],
        landAreaValueSite: [
          { required: true, message: '请输入拟用地面积' },
        ],
        constructionSize: [
          { required: true, message: '请输入拟建设规模' },
        ],
        govOrgCodeSite: [
          { required: true, message: '请输入核发机关组织机构代码' },
        ],
        govOrgNameSite: [
          { required: true, message: '请输入核发机关' },
        ],
        publishTimeSite: [
          { required: true, message: '请选择核发日期' },
        ],
      },

    }
  },
  created: function() {
    this.getLandAreaType();
    this.showData();

    $(".loading").hide();
  },
  methods: {

    // 获取用地单位/性质
    getLandAreaType: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'rest/dict/code/multi/items/list',
        data: {
          dicCodeTypeCodes: 'Land_Area_Type,XM_FIELD_TYPE'
        },
      }, function(res) {
        vm.landAreaUnitSite = res.content.Land_Area_Type;
        vm.fieldType = res.content.XM_FIELD_TYPE;
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
        url: ctx + 'rest/form/tceop/getTceop.do',
        data: {
          projInfoId: 'q'
        },
      }, function(res) {
        vm.formData = res;
        vm.formData2 = res;
        vm.formData3 = res;
        vm.$nextTick(function() {
          vm.$refs['form'].clearValidate();
          vm.$refs['form2'].clearValidate();
          vm.$refs['form3'].clearValidate();
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
          projInfoId: 'q',
          certLandId: _this.formData.certLandId || '',
          certProjectId: _this.formData2.certProjectId || '',
          siteId: _this.formData3.siteId || '',
          certLandCode: _this.formData.certLandCode || '',
          landAreaUnit: _this.formData.landAreaUnit || '',
          landNature: _this.formData.landNature || '',
          landAreaValue: _this.formData.landAreaValue || '',
          govOrgCodeLand: _this.formData.govOrgCodeLand || '',
          govOrgNameLand: _this.formData.govOrgNameLand || '',
          publishTimeLand: _this.formatTime(_this.formData.publishTimeLand, 'Y-M-D') || '',
          certProjectCode: _this.formData2.certProjectCode || '',
          publishOrgCodeProject: _this.formData2.publishOrgCodeProject || '',
          publishOrgNameProject: _this.formData2.publishOrgNameProject || '',
          publishTimeProject: _this.formatTime(_this.formData2.publishTimeProject, 'Y-M-D') || '',
          siteCode: _this.formData3.siteCode || '',
          landAreaUnitSite: _this.formData3.landAreaUnitSite || '',
          landAreaValueSite: _this.formData3.landAreaValueSite || '',
          constructionSize: _this.formData3.constructionSize || '',
          govOrgCodeSite: _this.formData3.govOrgCodeSite || '',
          govOrgNameSite: _this.formData3.govOrgNameSite || '',
          publishTimeSite: _this.formatTime(_this.formData3.publishTimeSite, 'Y-M-D') || ''
        }
        request('', {
          type: 'post',
          url: ctx + 'rest/form/tceop/saveTceop.do',
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