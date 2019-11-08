var app = new Vue({
  el: '#app',
  data: function() {
    return {
      dialogTitie: '',
      multipleSelection: [],
      multipleSelection2: [],
      multipleSelection3: [],
      showRightSlider: false,
      nounDialog: false,
      orgDialog: false,
      addWindowDialog: false,
      treeLeftHeight: document.documentElement.clientHeight - 100,
      pageLoading: false,
      loading: false,
      currentPage: 1,
      pagesize: 10,
      total: 0,
      tableData: [],
      regionData: [],
      showRightClick: false,
      keyword: '',
      keyword2: '',
      pageSize: 10,
      pageNum: 1,
      total: 0,
      curType: {},
      formData: {},
      leftSelNode: {},
      leftSelNodeData: {},
      activeName: 'first',
      treeEdit: false,
      matTypeId: 'root',
      notRoot: true,
      checkedId: false,
      parentData: [],
      matDialog: false,
      oper: '',
      rules: {
        typeName: [
          { required: true, message: '此项为必填' },
        ],
        typeCode: [
          { required: true, message: '此项为必填' },
        ],
      },
      isUsed: false,
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
      request('', {
          type: 'get',
          url: ctx + 'aea/item/mat/type/gtreeMatTypeForEUi.do',
        }, function(res) {
          vm.regionData = res;
          if (!$.isEmptyObject(vm.curType)) {
            vm.$nextTick(function() {
              vm.$refs.Tree.setCurrentKey(vm.curType.id, true);
            })
          } else {
            var node = {
              data: {
                "id": vm.regionData[0].id
              }
            }
            vm.$nextTick(function() {
              $(vm.$refs.Tree.$el).children().addClass("is-current")
            })
          }

        },
        function(err) {
          vm.$message.error('服务器错了哦!');
        })
    },
    // 请求table数据
    showData: function() {
      var vm = this;
      var param = {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        parentTypeId: this.curType.id,
        keyWord: this.keyword
      }
      if (this.curType.id == 'root') {
        param['parentTypeId'] = 'root';
      }
      request('', {
        type: 'get',
        url: ctx + 'aea/item/mat/type/listAeaItemMatTypePage.do',
        data: param
      }, function(res) {
        vm.tableData = res.rows;
        vm.loading = false
        vm.total = res.total;
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
    regionTreeNode: function(data, node) {
      var _this = this;
      this.curType = data;
      this.matTypeId = data.id;
      if (data.id == 'root') {
        this.isUsed = true;
      } else {
        this.isUsed = false;
      }
      this.showData();
      if ($(_this.$refs.Tree.$el).children().hasClass('is-current')) {
        $(_this.$refs.Tree.$el).children().removeClass("is-current")
      }
    },
    handleSelectionChange: function(val) {
      this.multipleSelection = val;
    },
    tabClick: function(val) {
      if (val.name == 'second') {
        this.getParents();
      }
    },
    // 删除窗口
    delateWindow: function(row) {
      var _this = this;
      confirmMsg('', '此操作将删除分类、下级子分类信息,您确定删除吗?', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/item/mat/type/deleteAeaItemMatTypeById.do',
          data: {
            id: row.matTypeId
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
    // 多删除窗口
    delateAllWindow: function() {
      var _this = this;
      var ids = [];
      this.multipleSelection.map(function(item) {
        ids.push(item.matTypeId);
      })
      if (ids.length == 0) {
        _this.$message({
          message: '请先选择要删除的材料',
          type: 'error'
        });
        return;
      }
      confirmMsg('', '此操作将删除分类、下级子分类信息,您确定删除吗?', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/item/mat/type/batchDeleteAeaItemMatType.do',
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
    addType: function() {
      var _this = this;
      this.activeName = 'first';
      this.showRightSlider = true;
      this.formData = new Object;
      if (this.matTypeId == 'root') {
        this.isUsed = true;
      } else {
        this.isUsed = false;
      }
    },
    // 点击编辑窗口
    editWindow: function(row) {

      var _this = this;
      this.activeName = 'first';
      this.matTypeId = row.matTypeId;
      this.isUsed = false;
      request('', {
        type: 'get',
        url: ctx + 'aea/item/mat/type/getAeaItemMatType.do',
        data: {
          id: row.matTypeId
        }
      }, function(res) {
        _this.showRightSlider = true;
        _this.formData = res;
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    dbclickRow: function(row) {

      this.editWindow(row);
    },
    checkCode: function() {
      var _this = this;
      request('', {
        type: 'post',
        url: ctx + 'aea/item/mat/type/checkUniqueTypeCodeNew.do',
        data: {
          matTypeId: this.formData.matTypeId,
          typeCode: this.formData.typeCode
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
      this.$refs['form'].validate((valid) => {
        if (!valid) return false;
        var param = {
          matTypeId: _this.formData.matTypeId ? _this.formData.matTypeId : '',
          parentTypeId: '',
          typeName: _this.formData.typeName ? _this.formData.typeName : '',
          typeCode: _this.formData.typeCode ? _this.formData.typeCode : '',
          typeMemo: _this.formData.typeMemo ? _this.formData.typeMemo : ''
        }
        if (_this.treeEdit) {
          if (_this.oper == 'add') {
            param.parentTypeId = _this.leftSelNodeData.matTypeId;
          } else if (_this.oper == 'edit') {
            param.parentTypeId = _this.leftSelNodeData.parentTypeId;
          }
        } else {
          param.parentTypeId = _this.curType.id ? _this.curType.id : ''
        }
        request('', {
          type: 'post',
          url: ctx + 'aea/item/mat/type/saveAeaItemMatType.do',
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
    // 获取父级
    getParents: function() {

      if (this.matTypeId == 'root') {
        this.isUsed = true;
      } else {
        this.isUsed = false;
      }
      var _this = this;
      request('', {
        type: 'get',
        url: ctx + 'aea/item/mat/type/other/tree.do',
        data: {
          matTypeId: this.matTypeId
        }
      }, function(res) {
        _this.parentData = res;
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    // 保存父级
    setParents: function() {
      var _this = this;
      var id = this.$refs.parentTree.getCheckedKeys();
      if (id.length == 0) {
        _this.$message({
          message: '请指定一个父级',
          type: 'error'
        });
        return
      }
      request('', {
        type: 'get',
        url: ctx + 'aea/item/mat/type/set/parent',
        data: {
          curTypeId: this.matTypeId,
          targetTypeId: id[0]
        }
      }, function(res) {
        _this.$message({
          message: '设置父级成功',
          type: 'success'
        });
        _this.showData();
        _this.getTreeData();
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
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
    treeNodeRightClick: function(event, data, Node) { // 树节点右键事件
      this.showRightClick = true;
      if (data.id == 'root') {
        this.notRoot = false;
        this.isUsed = true;
      } else {
        this.notRoot = true;
        this.isUsed = false;
      }
      $('.right-click-opt').css({ 'left': event.clientX, 'top': event.clientY });

      this.leftSelNode = Node;
      this.leftSelNode = Node;
      this.leftSelNodeData = data.data || '';
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
  },
  watch: {
    // keyword: function(val) {
    //   this.keyword = val;
    //   this.showData();
    // },
    keyword2: function(val) {
      this.$refs.Tree.filter(val);
    },
  }
})