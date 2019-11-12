var app = new Vue({
  el: '#app',
  data: function() {
    return {
      dialogTitie: '',
      multipleSelection: [],
      multipleSelection2: [],
      showRightSlider: false,
      addWindowDialog: false,
      attrTreeHeight: document.documentElement.clientHeight - 300,
      treeLeftHeight: document.documentElement.clientHeight - 100,
      pageLoading: false,
      loading: false,
      tableData: [],
      tableData2: [],
      regionData: [],
      showRightClick: false,
      keyword: '',
      keyword2: '',
      keyword3: '',
      typeControl: '',
      certControl: '',
      pageSize: 10,
      pageNum: 1,
      total: 0,
      pageSize2: 10,
      pageNum2: 1,
      total2: 0,
      isFirst: true,
      certData: {},
      curType: {},
      formData: {},
      formData2: {},
      leftSelNode: {},
      leftSelNodeData: {},
      activeName: 'first',
      treeEdit: false,
      matTypeId: '',
      notRoot: true,
      checkedId: false,
      attrLibrary: false,
      attrData: [],
      attrKeyword: '',
      parentData: [],
      isCert: false,
      dataBase: {},
      item: 'type',
      oper: 'add',
      selfData: false,
      other: false,
      typeLabel: '新增分类',
      cerdLabel: '新增证照',
      tableortree: 'table',
      rules: {
        typeName: [
          { required: true, message: '此项为必填' },
        ],
        typeCode: [
          { required: true, message: '此项为必填' },
        ],
      },
      rules2: {
        certName: [
          { required: true, message: '此项为必填' },
        ],
        certCode: [
          { required: true, message: '此项为必填' },
        ],
        attDirName: [
          { required: true, message: '此项为必填' },
        ],
        sortNo: [
          { required: true, message: '此项为必填' },
        ]
      }
    }
  },
  created: function() {
    this.showData();
    this.getTreeData();
    $(".loading").hide();
  },
  methods: {
    // 请求树数据
    getTreeData: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/type/gtreeCertType.do',
      }, function(res) {
        vm.regionData = res;
        var node = {
          data: {
            "id": vm.regionData[0].id
          }
        }
        vm.$nextTick(function() {
          // vm.clickTreeNode(vm.regionData[0],node);
          $(vm.$refs.Tree.$el).children().addClass("is-current")
        })
        if (vm.treeEdit) {
          vm.$nextTick(function() {
            if ($(vm.$refs.Tree.$el).children().hasClass('is-current')) {
              $(vm.$refs.Tree.$el).children().removeClass("is-current")
            }
            vm.$refs.Tree.setCurrentKey(vm.curType.id);
          })
        }
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 请求分类数据
    showData: function() {
      var vm = this;
      // vm.loading = true;
      var param = {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        parentTypeId: this.curType.id,
        keyword: this.keyword
      }
      if (this.curType.id == 'root') {
        // delete(param['parentTypeId']);
        param['parentTypeId'] = '';
      }
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/type/page.do',
        data: param
      }, function(res) {
        vm.tableData = res.rows;
        vm.loading = false
        vm.total = res.total;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 请求证照数据
    showCertData: function() {
      var vm = this;
      // vm.loading = true;
      var param = {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        certTypeId: this.curType.id,
        keyword: this.keyword3
      }
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/page.do',
        data: param
      }, function(res) {
        vm.tableData2 = res.rows;
        vm.loading = false
        vm.total2 = res.total;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 新增分类获取排序号
    getMaxSortNo: function() {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/type/getMaxSortNo.do',
        data: {
          parentId: this.curType.id
        }
      }, function(res) {
        vm.$set(vm.formData, 'sortNo', res);
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    filterNodeOrg: function(value, data) {
      if (!value) {
        return true;
      }
      return data.label.indexOf(value) !== -1;
    },
    // 修改状态
    changeState: function(row) {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/type/changIsActiveState.do',
        data: {
          id: row.certTypeId
        }
      }, function(res) {
        if (row.isActive == "1") {
          var msg = '启用成功'
        } else {
          var msg = '禁用成功'
        }
        vm.$message({
          message: msg,
          type: 'success'
        });
        vm.getTreeData();
        vm.showData();
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 地区树选择
    regionTreeNode: function(data, node) {
      var _this = this;
      this.curType = data;
      this.showData();
      if (data.id != 'root') {
        this.isFirst = false;
        this.showCertData();
      } else {
        this.isFirst = true;
      }
      if ($(_this.$refs.Tree.$el).children().hasClass('is-current')) {
        $(_this.$refs.Tree.$el).children().removeClass("is-current")
      }
    },
    handleSelectionChange: function(val) {
      this.multipleSelection = val;
    },
    handleSelectionChange2: function(val) {
      this.multipleSelection2 = val;
    },
    // 单击选中
    rowClick1: function(row, column, event) {
      if (column.label == '操作') return;
      this.multipleSelection = row;
      this.$refs.table1.toggleRowSelection(row);

    },
    // 单击选中
    rowClick2: function(row, column, event) {
      if (column.label == '操作') return;
      this.multipleSelection2 = row;
      this.$refs.table2.toggleRowSelection(row);

    },
    // 删除分类
    delateWindow: function(row) {
      var _this = this;
      confirmMsg('', '此操作将删除分类、下级子分类以及相关电子证照信息,您确定删除吗?', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/cert/type/deleteAeaCertTypeById.do',
          data: {
            id: row.certTypeId
          }
        }, function(res) {
          _this.$message({
            message: '删除成功',
            type: 'success'
          });
          _this.getTreeData();
          _this.showData();
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    // 多删除分类
    delateAllWindow: function() {
      var _this = this;
      var ids = [];
      this.multipleSelection.map(function(item) {
        ids.push(item.certTypeId);
      })
      if (ids.length == 0) {
        _this.$message({
          message: '请先选中',
          type: 'error'
        });
        return;
      }
      confirmMsg('', '此操作将批量删除分类、下级子分类以及相关电子证照信息,您确定删除吗?', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/cert/type/batchDeleteCertType.do',
          data: {
            ids: ids.join(',')
          }
        }, function(res) {
          _this.$message({
            message: '删除成功',
            type: 'success'
          });
          _this.getTreeData();
          _this.showData();
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    add: function() {
      this.treeEdit = true;
      this.oper = 'add';
      if (this.curType.id != 'root') {
        this.isFirst = false;
      }
      if (this.treeEdit) {
        if ($(this.$refs.Tree.$el).children().hasClass('is-current')) {
          $(this.$refs.Tree.$el).children().removeClass("is-current")
        }
        this.showData();
        this.showCertData();
        this.$nextTick(function() {
          this.$refs.Tree.setCurrentKey(this.curType.id);
        })
      }
      this.addType();
    },
    edit: function(row) {
      this.treeEdit = true;
      this.oper = 'edit';
      this.isFirst = false;

      if (this.treeEdit) {
        if ($(this.$refs.Tree.$el).children().hasClass('is-current')) {
          $(this.$refs.Tree.$el).children().removeClass("is-current")
        }
        this.showData();
        this.showCertData();
        this.$nextTick(function() {
          this.$refs.Tree.setCurrentKey(this.curType.id);
        })
      }
      this.editWindow(row);
    },
    // 新增分类
    addType: function() {
      var _this = this;
      this.activeName = 'first';

      this.showRightSlider = true;
      this.typeLabel = '新增分类';
      this.formData = new Object;
      this.getMaxSortNo();

    },
    // 点击编辑窗口
    editWindow: function(row) {
      var _this = this;
      this.typeLabel = '编辑分类';

      request('', {
        type: 'get',
        url: ctx + 'aea/cert/type/getAeaCertType.do',
        data: {
          id: row.certTypeId
        }
      }, function(res) {
        _this.showRightSlider = true;
        _this.formData = res;
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    // 添加证照
    addCert: function() {
      var _this = this;
      this.activeName = 'second';
      this.cerdLabel = "新增证照";
      this.showRightSlider = true;

      this.formData2 = new Object;
      this.$set(this.formData2, 'certTypeId', this.curType.id);
      this.$set(this.formData2, 'certId', '');

      this.$nextTick(function() {
        this.$refs['form2'].clearValidate();
      });
      this.getMaxCertSortNo();
    },
    delateAllCert: function() {
      var _this = this;
      var ids = [];
      this.multipleSelection2.map(function(item) {
        ids.push(item.certId);
      })
      if (ids.length == 0) {
        _this.$message({
          message: '请先选中',
          type: 'error'
        });
        return;
      }
      confirmMsg('', '此操作将批量删除电子证照信息,您确定删除吗?', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/cert/batchDeleteCertByIds.do',
          data: {
            ids: ids.join(',')
          }
        }, function(res) {
          _this.$message({
            message: '删除成功',
            type: 'success'
          });
          _this.getTreeData();
          _this.showData();
          _this.showCertData();
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    // 编辑证照
    editCert: function(row) {
      this.cerdLabel = "编辑证照";
      this.showRightSlider = true;
      this.activeName = 'second';
      this.getAeaCert(row.certId);
    },
    getAeaCert: function(id) {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/getAeaCert.do',
        data: {
          id: id
        }
      }, function(res) {
        vm.formData2 = res;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    delateCert: function(row) {
      var _this = this;
      confirmMsg('', '此操作将删除电子证照信息,您确定删除吗?', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/cert/deleteAeaCertById.do',
          data: {
            id: row.certId
          }
        }, function(res) {
          _this.$message({
            message: '删除成功',
            type: 'success'
          });
          _this.getTreeData();
          _this.showData();
          _this.showCertData();
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    selectAttr: function() {
      this.attrLibrary = true;
    },
    // 新增证照获取排序号
    getMaxCertSortNo: function() {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/getMaxCertSortNo.do',
        data: {}
      }, function(res) {
        vm.$set(vm.formData2, 'sortNo', res);
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 获取承诺办结时限单位
    getListDueUnitType: function() {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/listDueUnitType.do',
        data: {}
      }, function(res) {
        vm.attrData = res;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 获取持证所属类型
    getListCertHolderType: function() {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/listCertHolderType.do',
        data: {}
      }, function(res) {
        vm.attrData = res;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    clearRegionKeyword: function() {
      this.attrKeyword = '';
    },
    attrTreeNode: function(data, node, row) {
      var _this = this;
      this.dataBase = data;
    },
    saveDataBase: function() {
      this.$set(this.formData2, 'attDirName', this.dataBase.label);
      this.$set(this.formData2, 'attDirId', this.dataBase.id);
      this.attrLibrary = false;
    },
    getAttrTree: function() {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/cert/gtreeAttDirForEui.do',
        data: {}
      }, function(res) {
        vm.attrData = res;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },

    checkUniqueCertCode: function() {
      var _this = this;
      request('', {
        type: 'post',
        url: ctx + 'aea/cert/checkUniqueCertCode.do',
        data: {
          certCode: this.formData2.certCode,
          certId: this.formData2.certId || ''
        }
      }, function(res) {
        if (res) {
          _this.attrSave();
        } else {
          _this.$message({
            message: '编号重复',
            type: 'error'
          });
        }
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    attrSave: function() {
      var _this = this;
      this.$refs['form2'].validate(function(valid) {
        request('', {
          type: 'post',
          url: ctx + 'aea/cert/saveAeaCert.do',
          data: {
            certId: _this.formData2.certId ? _this.formData2.certId : '',
            certTypeId: _this.formData2.certTypeId ? _this.formData2.certTypeId : '',
            certName: _this.formData2.certName ? _this.formData2.certName : '',
            certCode: _this.formData2.certCode ? _this.formData2.certCode : '',
            attDirId: _this.formData2.attDirId ? _this.formData2.attDirId : '',
            attDirName: _this.formData2.attDirName ? _this.formData2.attDirName : '',
            certHolder: _this.formData2.certHolder ? _this.formData2.certHolder : '',
            dueNum: _this.formData2.dueNum ? _this.formData2.dueNum : '',
            dueUnit: _this.formData2.dueUnit ? _this.formData2.dueUnit : '',
            softwareEnv: _this.formData2.softwareEnv ? _this.formData2.softwareEnv : '',
            busAction: _this.formData2.busAction ? _this.formData2.busAction : '',
            sortNo: _this.formData2.sortNo ? _this.formData2.sortNo : '',
            certMemo: _this.formData2.certMemo ? _this.formData2.certMemo : '',
          }
        }, function(res) {
          _this.$message({
            message: '保存成功',
            type: 'success'
          });
          _this.showCertData();
          _this.showRightSlider = false;
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })

    },
    checkCode: function() {
      var _this = this;
      request('', {
        type: 'post',
        url: ctx + 'aea/cert/type/checkUniqueCertTypeCode.do',
        data: {
          certTypeId: this.formData.certTypeId,
          typeCode: this.formData.typeCode || ''
        }
      }, function(res) {
        if (res) {
          _this.save();
        } else {
          _this.$message({
            message: '编号重复',
            type: 'error'
          });
        }
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    save: function() {
      var _this = this;
      this.$refs['form'].validate(function(valid) {
        if (!valid) return false;
        var param = {
          certTypeId: _this.formData.certTypeId ? _this.formData.certTypeId : '',
          parentTypeId: _this.formData.parentTypeId || '',
          sortNo: _this.formData.sortNo ? _this.formData.sortNo : '',
          typeName: _this.formData.typeName ? _this.formData.typeName : '',
          typeCode: _this.formData.typeCode ? _this.formData.typeCode : '',
          typeMemo: _this.formData.typeMemo ? _this.formData.typeMemo : ''
        }
        if (_this.selfData && !_this.other) {
          param.parentTypeId = _this.curType.id;
        }
        if (param.parentTypeId == 'root') {
          param.parentTypeId = '';
        }
        request('', {
          type: 'post',
          url: ctx + 'aea/cert/type/saveAeaCertType.do',
          data: param
        }, function(res) {
          if (res.success) {
            _this.$message({
              message: '保存成功',
              type: 'success'
            });
            _this.showData();
            _this.getTreeData();
            _this.showRightSlider = false;
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
    nodeClick: function(data, checked, node) {
      this.checkedId = data.id
      this.$refs.parentTree.setCheckedNodes([data]);
    },
    handleClick: function(data, checked, node) {
      if (checked == true) {
        this.checkedId = data.id;
        this.$refs.parentTree.setCheckedNodes([data]);
      }
    },
    handleSizeChange: function(val) {
      this.pageSize = val;
      this.showData();

    },
    handleCurrentChange: function(val) {
      this.pageNum = val;
      this.showData();
    },
    handleSizeChange2: function(val) {
      this.pageSize2 = val;
      this.showCertData();

    },
    handleCurrentChange2: function(val) {
      this.pageNum2 = val;
      this.showCertData();
    },
    treeNodeRightClick: function(event, data, Node) { // 树节点右键事件
      this.showRightClick = true;
      if (data.id == 'root') {
        this.notRoot = false;
      } else {
        this.notRoot = true;
      }
      $('.right-click-opt').css({ 'left': event.clientX, 'top': event.clientY });

      this.leftSelNode = Node;
      this.leftSelNodeData = data.data;
      this.curType = data;

    },
    fold: function() {
      for (var i = 0; i < this.$refs.Tree.store._getAllNodes().length; i++) {
        this.$refs.Tree.store._getAllNodes()[i].expanded = false;
      }
    },
    unfold: function(tree) {
      for (var i = 0; i < this.$refs.Tree.store._getAllNodes().length; i++) {
        this.$refs.Tree.store._getAllNodes()[i].expanded = true;
      }
    },
    fold2: function() {
      for (var i = 0; i < this.$refs.attrTree.store._getAllNodes().length; i++) {
        this.$refs.attrTree.store._getAllNodes()[i].expanded = false;
      }
    },
    unfold2: function(attrTree) {
      for (var i = 0; i < this.$refs.attrTree.store._getAllNodes().length; i++) {
        this.$refs.attrTree.store._getAllNodes()[i].expanded = true;
      }
    },
  },
  watch: {
    // keyword: function(val) {
    //   this.keyword = val;
    //   this.showData();
    // },
    // keyword3: function(val) {
    //   this.keyword3 = val;
    //   this.showCertData();
    // },
    keyword2: function(val) {
      this.$refs.Tree.filter(val);
    },
    attrKeyword: function(val) {
      this.$refs.attrTree.filter(val);
    },
  }
})