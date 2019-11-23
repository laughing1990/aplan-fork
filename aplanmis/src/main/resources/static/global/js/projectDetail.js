// 手机号校验ele
var checkPhone = function (rule, value, callback) {
  if (!value) {
    return callback(new Error('手机号不能为空'));
  } else {
    var reg = /^1[3|4|5|7|8][0-9]\d{8}$/
    // console.log(reg.test(value));
    if (reg.test(value)) {
      callback();
    } else {
      return callback(new Error('请输入正确的手机号'));
    }
  }
};
// 非必填
var checkPhoneNoNeed = function (rule, value, callback) {
  var reg = /^1[3|4|5|7|8][0-9]\d{8}$/
  // console.log(reg.test(value));
  if (reg.test(value) || !value || !value.length) {
    callback();
  } else {
    return callback(new Error('请输入正确的手机号'));
  }
};
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

var vm = new Vue({
  el: '#projectDetail',
  mixins: [mixins],
  data: function () {
    return {
      // 当前项目详情是新增还是编辑
      curProjDetailIsEdit: false,
      // 当前项目详情的id:如果是编辑的话
      curProjInfoId: '',
      // 总共模块类型
      moduleTypeList: [{
          name: '项目/工程信息',
        value: 'baseInfo',
      }, {
        name: '建设单位',
        value: 'buildUnit',
      }],
      // 当前视野内部的module
      curInEyeModule: 'baseInfo',

      // 项目信息
      baseForm: {
        localCode: '', //项目代码
        projName: '', //项目名称
        projCategory: '', //工程分类
        mainClass: '', //项目大类
        projType: '', //立项类型
        isForeign: '', //是否外资项目 0-不是 1-是
        projNature: '', //建设性质
        projUrgency: '', //紧急程度
        projLevel: '', //项目级别
        isJgxm: '', //是否技改项目
        district: [], //行政分区
        regionalism: '', //项目所在区域
        financialSource: '', //资金来源
        investSum: '', //总投资
        xmzbj: '', //项目资本jin
        theIndustry: '', //国标行业
        industryMainClass: '', //产品大类
        industrySubClass: '', //产品小类
        buildAreaSum: '', //建筑面积
        xmYdmj: '', //用地面积
        xzydmj: '', //新增用地面积
        nydmj: '', //农用地面积
        nstartTime: '', //拟开工时间
        endTime: '', //拟建成时间
        buildType: '', //建筑类型
        investType: '', //投资类型
        landSource: '', //土地来源
        landNature: '', //用地性质
        buildNature: '', //建筑性质
        themeId: '', //主题类型
        scaleContent: '', //建设规模及内容
        projAddr: '', //项目具体地址
        currentProgress: '', //目前进展情况
        projQuestion: '', //存在问题
        projCreateUser: '', //创建人姓名
        projCreateMobile: '', //创建人电话
        gcbm: '', //工程编码
        isAreaEstimate:'',//是否完成区域评估
        isDesignSolution:'',//土地是否带设计方案
        gbCodeYear: '2017'
      },
      // 项目信息校验
      baseRules: {
          projName: [
              { required: true,message: '请输入项目/工程名称！', trigger: 'change' },
          ],
          localCode: [
              { required: true,message: '请输入投资平台登记的项目代码！', trigger: ['change','blur'] },
          ],
          gcbm: [
              { required: true,message: '请输入工程代码！', trigger: ['change'] },
          ],
          regionalism: [
              { required: true,message: '请选择审批行政区划！', trigger: ['change'] },
          ],
          projType: [
              { required: true,message: '请选择立项类型！', trigger: ['change'] },
          ],
          projectAddress: [
              { required: true,message: '请输入建设地点！', trigger: ['blur'] },
          ],
          financialSource: [
              { required: true,message: '请选择资金来源！', trigger: ['change'] },
          ],
          landSource: [
              { required: true,message: '请选择土地来源！', trigger: ['change'] },
          ],
          isDesignSolution: [
              { required: true,message: '请选择土地是否带设计方案！', trigger: ['change'] },
          ],
          isAreaEstimate: [
              { required: true,message: '请选择是否完成区域评估！', trigger: ['change'] },
          ],
          projNature: [
              { required: true,message: '请选择建设性质！', trigger: ['change'] },
          ],
          investType: [
              { required: true,message: '请选择投资类型！', trigger: ['change'] },
          ],
          gbCodeYear: [
              { required: true,message: '请输入国标行业代码发布年代！', trigger: ['blur'] },
          ],
          theIndustry: [
              { required: true,message: '请选择国标行业！', trigger: ['blur', 'change'] },
          ],
          nstartTime: [
              { required: true,message: '请选择拟开工时间！', trigger: ['change'] },
          ],
          endTime: [
              { required: true,message: '请选择拟建成时间！', trigger: ['change'] },
          ],
          themeId: [{
              required: true,
              message: '请选择主题类型',
              trigger: 'change'
          }],
          scaleContent: [
              { required: true,message: '请输入建设规模及内容！', trigger: ['blur'] },
          ],
          xmYdmj: [
              {validator:checkNumber, trigger: ['blur'] },
              { required: true,message: '请填写用地面积！', trigger: ['blur'] }
          ],
          xzydmj: [
              {validator:checkNumber, trigger: ['blur'] },
          ],
          buildAreaSum: [
              {validator:checkNumber, trigger: ['blur'] },
              { required: true,message: '请填写建筑面积！', trigger: ['blur'] }
          ],
          aboveGround: [
              {validator:checkNumber, trigger: ['blur'] },
          ],
          underGround: [
              {validator:checkNumber, trigger: ['blur'] },
          ],
          investSum: [
              {validator:checkNumber, trigger: ['blur'] },
              { required: true,message: '请输入总投资！', trigger: ['blur'] }
          ],
          nydmj: [{
              required: false,
              validator: checkNumber,
              trigger: 'blur'
          }], //农用地面积
          xmzbj: [{
              required: false,
              validator: checkNumber,
              trigger: 'blur'
          }],
      },

      // 建设单位
      buildUnitList: [],
      // 单个单位
      buildUnitForm: {
        unitInfoId: '', //单位id
        applicant: '', //单位名称
        //idtype: '', //单位证照类型
        //idcard: '', //单位证件号码
        unifiedSocialCreditCode: '', //统一社会信用代码
        organizationalCode: '',
        induCommRegNum: '',
        taxpayerNum: '',
        creditFlagNum: '',
        idrepresentative: '', //法定代表人
        idno: '', //法人身份证号码
        idmobile: '', //法人联系电话
        applicantDistrict: [], //行政区（园区）
        applicantDetailSite: '', //具体地址
        linkmanName: '', //联系人
        linkmanMobilePhone: '', //联系人手机号
        linkmanMail: '', //联系人电子邮箱
        linkmanCertNo: '', //联系人身份证号
        isShowDropSelectFlag: false, //当前是否显示单位下拉列表（自定义列表）
        linkmanIsSelect: true, //联系人字段是否是选择的
      },
      // 建设单位校验
      buildUnitRules: {
        applicant: [{
          required: true,
          message: '请选择单位',
          trigger: 'change'
        }],
        // idtype: [{
        //   required: true,
        //   message: '请选择单位证照类型',
        //   trigger: 'change'
        // }],
        unifiedSocialCreditCode: [{
          required: true,
          message: '请输入统一社会信用代码',
          trigger: 'blur'
        }],
        idrepresentative: [{
          required: true,
          message: '请输入法定代表人',
          trigger: 'blur'
        }],
        applicantDetailSite: [{
          required: true,
          message: '请输入具体地址',
          trigger: 'blur'
        }],
        // 以下为非必填
        idmobile: [{
          required: false,
          validator: checkPhoneNoNeed,
          trigger: 'blur'
        }],
        linkmanMobilePhone: [{
          required: false,
          validator: checkPhoneNoNeed,
          trigger: 'blur'
        }],
        // linkmanCertNo: [{
        //   required: true,
        //   message: '请完善联系人信息',
        //   trigger: 'blur'
        // }],
      },
      // 单位名称
      buildUnitName: '',
      // 当前正在编辑的单位索引
      curEditBuildUnitIndex: -1,
      // 可供选择的单位列表
      unitList: [{
        unitInfoId: 'sdsd',
        applicant: '公司'
      }],
      // 当前单位的联系人option
      linkManOptions: [],

      // 代办单位
      agencyUnitList: [],
      // 单个代办单位
      agencyUnitForm: {
        unitInfoId: '', //单位id
        applicant: '', //单位名称
        //idtype: '', //单位证照类型
        unifiedSocialCreditCode: '', //统一社会信用代码
        organizationalCode: '',
        induCommRegNum: '',
        taxpayerNum: '',
        creditFlagNum: '',
        idrepresentative: '', //法定代表人
        idno: '', //法人身份证号码
        idmobile: '', //法人联系电话
        applicantDistrict: '', //行政区（园区）
        applicantDetailSite: '', //具体地址
        linkmanName: '', //联系人
        linkmanMobilePhone: '', //联系人手机号
        linkmanMail: '', //联系人电子邮箱
        linkmanCertNo: '', //联系人身份证号
        isShowDropSelectFlag: false, //当前是否显示单位下拉列表（自定义列表）
        linkmanIsSelect: true, //联系人字段是否是选择的
      },
      // 代办单位校验
      agencyUnitRules: {
        applicant: [{
          required: true,
          message: '请选择单位',
          trigger: 'change'
        }],
        // idtype: [{
        //   required: true,
        //   message: '请选择单位证照类型',
        //   trigger: 'change'
        // }],
        unifiedSocialCreditCode: [{
          required: true,
          message: '请输入统一社会信用代码',
          trigger: 'blur'
        }],
        idrepresentative: [{
          required: true,
          message: '请输入法定代表人',
          trigger: 'blur'
        }],
        applicantDetailSite: [{
          required: true,
          message: '请输入具体地址',
          trigger: 'blur'
        }],
        // 以下为非必填
        idmobile: [{
          required: false,
          validator: checkPhoneNoNeed,
          trigger: 'blur'
        }],
        linkmanMobilePhone: [{
          required: false,
          validator: checkPhoneNoNeed,
          trigger: 'blur'
        }],
        // linkmanName: [{
        //   required: true,
        //   message: '请完善联系人信息',
        //   trigger: 'blur'
        // }],
        // linkmanCertNo: [{
        //   required: true,
        //   message: '请完善联系人信息',
        //   trigger: 'blur'
        // }],
      },

      // 当前项目详情是否有代办单位
      hasAgencyUnit: false,


      // 页面所有待选项
      allDicItemListData: {
        XM_XZFQ: [],
      },
      districtList: [],//行政区划

      // 行政分区级联-prop
      SetDistrictDept: {
        value: 'regionId',
        label: 'regionName',
        children: 'children',
        checkStrictly: true
      },
      
    }
  },
  methods: {
    saveData: function () {
      // test
      // this.closeWindowTab();
      // return

      var ts = this;
      var _canSaveBaseFlag = false,
        _canSaveBuildFlag = false,
        _canSaveAgencyFlag = false;
      if (this.buildUnitList.length) {
        for (var i = 0; i < this.buildUnitList.length; i++) {
          this.$refs['buildUnitForm' + i][0].validate(function (valid) {
            if (valid) {
              _canSaveBuildFlag = true;
            } else {
              ts.apiMessage('请完善必填信息！', 'warning')
              _canSaveBuildFlag = false;
              return false;
            }
          });
        }
      }
      if (this.agencyUnitList.length && this.hasAgencyUnit) {
        for (var i = 0; i < this.agencyUnitList.length; i++) {
          this.$refs['agencyUnitForm' + i][0].validate(function (valid) {
            if (valid) {
              _canSaveAgencyFlag = true;
            } else {
              ts.apiMessage('请完善必填信息！', 'warning')
              _canSaveAgencyFlag = false;
              return false;
            }
          });
        }
      } else {
        _canSaveAgencyFlag = true;
      }
      this.$refs['baseForm'].validate(function (valid) {
        if (valid) {
          _canSaveBaseFlag = true;
        } else {
          ts.apiMessage('请完善必填信息！', 'warning')
          _canSaveBaseFlag = false;
          return false;
        }
      });

      // 处理行政分区，只传最后一个地区的id
      var _regionalism = "";
      if (this.baseForm.regionalism) {
        var _area = "", len = this.baseForm.regionalism.length;
        _area = this.baseForm.regionalism[len - 1];
        _regionalism = _area;
      }else{
        _regionalism = "";
      }

      // 处理建设单位中的行政分区
      var _newBuildUnitList =JSON.parse(JSON.stringify(ts.buildUnitList)) || [];
      if(_newBuildUnitList && _newBuildUnitList.length){
        var allList = _newBuildUnitList;
        for(var i =0; i<allList.length;i++){
          var _regionalism1 = "";
          if (allList[i].applicantDistrict) {
            var _area = "", len = allList[i].applicantDistrict.length;
            _area = allList[i].applicantDistrict[len - 1];
            _regionalism1 = _area;
          }else{
            _regionalism1 = "";
          }
          _newBuildUnitList[i].applicantDistrict = _regionalism1;
        }
      }

      // console.log(_canSaveBaseFlag, _canSaveBuildFlag, _canSaveAgencyFlag)
      // console.log(this.baseForm)
      // console.log(this.buildUnitList)
      // console.log(this.agencyUnitList)
      
      if (_canSaveBaseFlag && _canSaveBuildFlag && _canSaveAgencyFlag) {
        var _saveData = {
          baseForm: {}
        }
        _saveData.baseForm = JSON.parse(JSON.stringify(ts.baseForm));
        _saveData.baseForm.regionalism = _regionalism;
        _saveData.baseForm.projectAddress = _saveData.baseForm.projectAddress?_saveData.baseForm.projectAddress.join(','):'';
        _saveData.baseForm = JSON.stringify(_saveData.baseForm);
        // console.log(_saveData)
        // return
        _saveData.buildUnitList = JSON.stringify(_newBuildUnitList); //保存是保存拷贝的那一份数据，原数据有跟控件绑定
        _saveData.agencyUnitList = JSON.stringify(ts.agencyUnitList);
        if (this.curProjInfoId && this.curProjInfoId.length) {
          _saveData.projInfoId = ts.curProjInfoId;
        }
        //   console.log(_saveData)
        // return
        ts.loading = true;
        request('', {
          url: ctx + 'aea/proj/info/saveAeaProjInfo',
          type: 'post',
          data: _saveData
        }, function (res) {
          ts.loading = false;
          if (res.success) {
            ts.apiMessage('保存成功！', 'success');
            ts.closeWindowTab();

            //刷新查询列表tab
            var _iframs = $(window.parent.document).find('iframe');
            var _panentIframsrc = ts.getSerachParamsForUrl('parentIfreamUrl');
            var _tagIframe = '';
            for (var i = 0; i < _iframs.length; i++) {
              var _src = _iframs.eq(i).attr('src');
              if (_src && (_src == _panentIframsrc)) {
                _tagIframe = _iframs.eq(i);
                break;
              }
            }
            _tagIframe[0].contentWindow.location.reload(true);

          } else {
            ts.apiMessage(res.message, 'error')
          }
        }, function (msg) {
          ts.loading = false;
          ts.apiMessage('网络错误！', 'error')
        });
        // $.ajax({
        //   url: ctx + 'aea/proj/info/saveAeaProjInfo',
        //   type: 'post',
        //   data: _saveData,
        //   success: function (res) {
        //     ts.loading = false;
        //     if (res.success) {
        //       ts.apiMessage('保存成功！', 'success')
        //     } else {
        //       ts.apiMessage(res.message, 'error')
        //     }
        //   },
        //   error: function (msg) {
        //     ts.loading = false;
        //     ts.apiMessage('网络错误！', 'error')
        //   },
        // })
      }

    },
    // 项目数据初始化
    init: function () {
      var ts = this;
      var _projInfoId = this.getSerachParamsForUrl('projInfoId');
      console.log(_projInfoId)
      // _projInfoId不存在或者为空，就是新增，要不就是编辑
      if (!_projInfoId) {
        this.buildUnitList.push(JSON.parse(JSON.stringify(ts.buildUnitForm)))
        // this.agencyUnitList.push(JSON.parse(JSON.stringify(ts.agencyUnitForm)))
        this.curProjDetailIsEdit = false;
        this.fetchAllDicContent();
        this.getDistrictList();
      } else {
        // 编辑的时候要先获取项目信息
        this.curProjDetailIsEdit = true;
        this.curProjInfoId = _projInfoId;
        this.fetchProjDetail();
      }
    },
    // 编辑时，获取项目详情
    fetchProjDetail: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'aea/proj/info/getOnlyAeaProjInfoById',
        type: 'get',
        data: {
          id: ts.curProjInfoId
        }
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.initProjDetailData(res.content);
          ts.fetchAllDicContent();
          ts.getDistrictList();
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },
    // 编辑时。初始化项目详情数据
    initProjDetailData: function (obj) {
      var ts = this;
      // 处理建设单位数据
      obj.buildUnitList.forEach(function (item) {
        item.isShowDropSelectFlag = false, //当前是否显示单位下拉列表（自定义下拉列表）
          item.linkmanIsSelect = true
      })
      if (obj.buildUnitList.length) {
        this.buildUnitList = obj.buildUnitList;
      } else {
        this.buildUnitList.push(JSON.parse(JSON.stringify(ts.buildUnitForm)))
      }

      // 处理代办单位数据
      obj.agencyUnitList.forEach(function (item) {
        item.isShowDropSelectFlag = false, //当前是否显示单位下拉列表（自定义下拉列表）
          item.linkmanIsSelect = true
      })
      if (obj.agencyUnitList.length) {
        this.agencyUnitList = obj.agencyUnitList;
        this.hasAgencyUnit = true;
      }

      // 处理项目信息
      if (obj.baseForm) {
        for (var k in ts.baseForm) {
          if (obj.baseForm[k]) {
            ts.baseForm[k] = obj.baseForm[k];
          } else {
            ts.baseForm[k] = "";
          }
        }
      }
      if (obj.baseForm.regionalism) {
        var _arr = [];
        _arr.push(obj.baseForm.regionalism);
        ts.baseForm['regionalism'] = _arr;
      }

      // 处理建设单位中后台给的行政区数据 ：字符串转为数组
      if( this.buildUnitList &&  this.buildUnitList.length){
        var buildUnitList = this.buildUnitList;
        for(var i = 0; i<buildUnitList.length; i++){
          if(buildUnitList[i].applicantDistrict){
            var areaCode = buildUnitList[i].applicantDistrict;
            buildUnitList[i].applicantDistrict = [];
            buildUnitList[i].applicantDistrict.push(areaCode)
          }
        }
      }
      // console.log(this.buildUnitList)

      //处理建设地点
      if (!!ts.baseForm.projectAddress) ts.baseForm.projectAddress = ts.baseForm.projectAddress.split(',');
    },

    // 建设单位相关
    // 添加一个建设单位
    addBuildUnitOne: function () {
      var ts = this;
      this.buildUnitList.push(JSON.parse(JSON.stringify(ts.buildUnitForm)))
      // console.log(this.buildUnitList)
      console.log($('#buildUnit').find('.content-box:last-of-type').offset().top)
    },

    // 移除掉一个建设单位
    delBuildUnitOne: function (idx) {
      if (idx < 0) return;
      this.buildUnitList.splice(idx, 1);
    },

    // 根据输入获取已有的单位列表
    fetchUnitListForInp: debounce(function (kw, index) {
      var ts = this;
      request('', {
        url: ctx + 'aea/projUnit/listAeaUnitInfo',
        type: 'get',
        data: {
          keyword: kw
        }
      }, function (res) {
        ts.unitList = res;
        ts.showSelectDropBox(ts.buildUnitList, index);
        // 如果没有搜索到单位
        if(!res || !res.length){
          ts.linkManOptions = [];
        }
      }, function (msg) {
        ts.apiMessage('网络错误！', 'error')
      });
    }, 500),

    // 单位下来列表选中  obj为下拉选项中选中的单位项， index为当前编辑的-建设单位索引
    showSelectDropBox: function (obj, index) {
      var ts = this;
      obj[index].isShowDropSelectFlag = true;
      // test-当点击外部时，关闭下拉弹框
      document.onclick = function () {
        ts.hideSelectDropBox(obj, index);
        document.onclick = null;
      }
    },
    hideSelectDropBox: function (obj, index) {
      obj[index].isShowDropSelectFlag = false;
    },

    // 选中下拉框中的一项单位数据,  target是所有的建设单位列表， unit是单位下拉列表中选中单位，index是当前编辑的建设单位索引
    selectDropItem: function (target, unit, index) {
      target[index].unitInfoId = unit.unitInfoId; //单位id赋值
      target[index].applicant = unit.applicant; //单位名赋值
      //target[index].idtype = unit.idtype; //单位证照类型赋值
      target[index].unifiedSocialCreditCode = unit.unifiedSocialCreditCode; //单位证件号码赋值
      target[index].idrepresentative = unit.idrepresentative; //法定代表人赋值
      this.fetchLinkManListForUnitInfoId(unit.unitInfoId);
      this.hideSelectDropBox(target, index)
    },

    // 选中单位后，请求联系人下拉列表数据
    fetchLinkManListForUnitInfoId: function (id) {
      var ts = this;
      request('', {
        url: ctx + 'aea/proj/info/listDgLinkmanInfoByunitInfoId',
        type: 'get',
        data: {
          unitInfoId: id
        }
      }, function (res) {
        if (res.success) {
          ts.linkManOptions = res.content;
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.apiMessage('网络错误！', 'error')
      });
    },

    // 联系人选择change  list为所有已生成的建设单位 index为当前正在操作的建设单位， obj为选中的联系人对象
    linkManSelectChange: function (list, index, obj) {
      list[index].linkmanMobilePhone = obj.linkmanMobilePhone || '';
      list[index].linkmanMail = obj.linkmanMail || '';
      list[index].linkmanCertNo = obj.linkmanCertNo || '';
      list[index].linkmanInfoId = obj.linkmanInfoId || '';
    },

    // 联系人选择选择方式改变
    linkmanSelectTypeChange: function (list, index) {
      list[index].linkmanIsSelect = !list[index].linkmanIsSelect;
      // 当联系人的选择方式为手动输入时，清除掉linkmanInfoId，重置联系人名称、联系人手机号码与联系人电子邮箱
      if (!list[index].linkmanIsSelect) {
        delete list[index].linkmanInfoId;
        list[index].linkmanName = '';
        list[index].linkmanMobilePhone = '';
        list[index].linkmanMail = '';
        list[index].linkmanCertNo = '';
      }
    },

    // 联系人的数据输入为下拉选择时，要先判断当前是否已经选择了单位
    linkmanIsSelectFocus: function (list, index) {
      if (!list[index].applicant.length) return this.apiMessage('您尚未选择单位，请先选择单位！', 'warning');
      list[index].unitInfoId && this.fetchLinkManListForUnitInfoId(list[index].unitInfoId);   //从接口获取
      // if(list[index].link){}
      // list[index].unitInfoId && (this.linkManOptions = list[index].linkmanInfoList); //从当前编辑单位的数据里面获取
    },

    // 代办单位相关
    // 添加一个代办单位
    addAgencyUnitOne: function () {
      var ts = this;
      this.agencyUnitList.push(JSON.parse(JSON.stringify(ts.agencyUnitForm)))
      console.log(this.agencyUnitList)
    },

    // 移除掉一个代办单位
    delAgencyUnitOne: function (idx) {
      if (idx < 0) return;
      this.agencyUnitList.splice(idx, 1);
    },

    // 根据输入获取已有的单位列表
    fetchUnitListForAgencyInp: debounce(function (kw, index) {
      var ts = this;
      request('', {
        url: ctx + 'aea/projUnit/listAeaUnitInfo',
        type: 'get',
        data: {
          keyword: kw
        }
      }, function (res) {
        ts.unitList = res;
        ts.showSelectDropBox(ts.agencyUnitList, index);
      }, function (msg) {
        ts.apiMessage('网络错误！', 'error')
      });
    }, 500),

    // 切换立项类型，获取主题类型（项目类型的下拉待选项）
    projTypeChange: function (type) {
      var ts = this;
      if (!type.length) return
      request('', {
        url: ctx + 'aea/proj/info/getThemeType',
        type: 'get',
        data: {
          themeType: type
        }
      }, function (res) {
        ts.allDicItemListData.XM_THEME_TYPE.splice(0);
        ts.allDicItemListData.XM_THEME_TYPE = res;
        console.log(ts.allDicItemListData.XM_THEME_TYPE)
      }, function (msg) {

      });
    },
    // 根据顶级组织ID查询区划列表  rest/org/region/list
    getDistrictList: function () {
        var _that = this;
        request('', {
            url: ctx + 'rest/org/region/list',
            type: 'get',
        }, function (result) {
            if(result.content){
                _that.districtList = result.content;
            }else {
                _that.$message({
                    message: '获取行政区划列表失败',
                    type: 'error'
                });
            }
        }, function (msg) {
            alertMsg('', '网络加载失败！', '关闭', 'error', true);
        })
    },

    // 获取页面中所有select与radio待选项数据
    fetchAllDicContent: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'aea/proj/info/getAllDicContent',
        type: 'get',
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.allDicItemListData = res.content;
          // 编辑项目时，根据立项类型自动获取项目类型的下拉选项
          if(ts.baseForm.projType){
            setTimeout(function(){
              ts.projTypeChange(ts.baseForm.projType);
            },100)
          } 
          // console.log( ts.allDicItemListData.XM_XZFQ)
          // 处理行政分区最后一层的children,为空就删掉
          ts.handelxingzhenfenqu(ts.allDicItemListData.XM_XZFQ)  
        } else {
          ts.apiMessage(res.message, 'error')
        }

      }, function (msg) {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },

    // 处理行政分区最后一层的children,为空就删掉children属性
    handelxingzhenfenqu: function(list){
      for(var i = 0; i <list.length; i++){
        if(list[i].children && list[i].children.length){
          this.handelxingzhenfenqu(list[i].children)
        }else {
          delete list[i].children
        }
      }
    },

    // 取消修改
    cancelEdit: function () {
      var ts = this;
      confirmMsg('', '您确定取消修改吗？', function () {
        ts.closeWindowTab();
      })
    },
    // 关闭窗口-仅限于新打开的窗口
    closeWindowTab: function(){
      // setTimeout(function(){
      //   window.opener = null; //禁止某些浏览器的一些弹窗
      //   window.open('', '_self');
      //   window.close()
      // },500)

      var ts = this;
      var _tabId = ts.getSerachParamsForUrl('projInfoId');
      !!_tabId ? 
        (_tabId = _tabId + '_pjdetail') : 
        (_tabId = 'add_pjdetail');
      setTimeout(function () {
        window.parent.vm.removeTab(_tabId); 
      }, 500)
    },
  },
  created: function () {
    this.init();
    // this.fetchAllDicContent();
  },
  mounted: function () {
    var ts = this;
    $(document).on('scroll', throttle(function () {
      ts.whereDiv("#baseInfo");
      ts.whereDiv("#buildUnit");
      ts.hasAgencyUnit && ts.whereDiv('#agencyUnit')
      var h = $(document).height(); //网页文档的高度
      var c = $(document).scrollTop(); //滚动条距离网页顶部的高度
      var wh = $(window).height(); //页面可视化区域高度

      if (Math.ceil(wh + c) >= h) {
        $('#handelBox').show();
      }
    }, 100, 600))
    $(window).off('resize').on('resize', throttle(function () {
      ts.navLeft();
    }, 100, 600))
    ts.navLeft();
  },
  watch: {
    // 监听是否是添加了代办单位
    hasAgencyUnit: function (val) {
      var ts = this;
      if (val) {
        this.moduleTypeList.push({
          name: '代办单位',
          value: 'agencyUnit',
        });
        // 如果是项目编辑时添加了代办单位，而且原来数据就有代办单位数据，那就不需要新增一个空的代办单位实体数据
        if (!ts.agencyUnitList.length) {
          this.agencyUnitList.push(JSON.parse(JSON.stringify(ts.agencyUnitForm)))
        }

      } else {
        this.moduleTypeList.pop();
        // 如果是代办单位不需要了，那就把agencyUnitList重置为null
        this.agencyUnitList = [];
      }
    },
  },
});