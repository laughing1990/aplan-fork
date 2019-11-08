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
      treeLeftHeight: document.documentElement.clientHeight - 120,
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
      formData: {
        exeDate: ''
      },
      isEdit: false,
      formData2: {},
      leftSelNode: {},
      leftSelNodeData: {},
      activeName: 'first',
      treeEdit: false,
      matTypeId: '',
      notRoot: true,
      notLaw: true,
      checkedId: false,
      attrLibrary: false,
      attrData: [],
      attrKeyword: '',
      parentData: [],
      isCert: false,
      dataBase: {},
      fileList: [],
      fileList2: [],
      lawData: [],
      clauseData: [],
      item: 'law',
      lawLabel: '新增分类',
      clauseLabel: '新增证照',
      rules: {
        legalName: [
          { required: true, message: '此项为必填' },
        ],
        legalLevel: [
          { required: true, message: '此项为必填' },
        ],
        basicNo: [
          { required: true, message: '此项为必填' },
        ],
        issueOrg: [
          { required: true, message: '此项为必填' },
        ],
        exeDate: [
          { required: true, message: '此项为必填' },
        ]
      },
      rules2: {
        clauseTitle: [
          { required: true, message: '此项为必填' },
        ],
        clauseContent: [
          { required: true, message: '此项为必填' },
        ],
        sortNo: [
          { required: true, message: '此项为必填' },
        ]
      }
    }
  },
  created: function() {
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
        url: ctx + 'aea/service/legal/gtreeLegalAndClauseForEui.do',
      }, function(res) {
        res[0].label = ' ' + res[0].label;
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

    // 地区树选择
    regionTreeNode: function(data, node) {
      var _this = this;
      this.curType = data;
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
    // 删除法律法规
    delateLaw: function(row) {
      var _this = this;
      confirmMsg('', '此操作将删除法律法规,相关材料关联数据将失效,您确定删除吗？', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/service/legal/deleteAeaServiceLegalById.do',
          data: {
            id: row.legalId
          }
        }, function(res) {
          _this.$message({
            message: '删除成功',
            type: 'success'
          });
          _this.getTreeData();
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    // 删除条款
    delateClause: function(row) {
      var _this = this;
      confirmMsg('', '此操作将删除法律法规条款,相关材料关联数据将失效,您确定删除吗？', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/service/legal/clause/deleteAeaServiceLegalClauseById.do',
          data: {
            id: row.legalClauseId
          }
        }, function(res) {
          _this.$message({
            message: '删除成功',
            type: 'success'
          });
          _this.getTreeData();
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    // 获取当天日期
    getNowFormatDate: function() {
      var date = new Date();
      var seperator1 = "-";
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var strDate = date.getDate();
      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
      }
      var currentdate = year + seperator1 + month + seperator1 + strDate;
      return currentdate;
    },
    // 新增法律法规
    addType: function(leftSelNodeData) {
      var _this = this;
      this.item = 'law';
      this.fileList = [];
      this.isEdit = false;

      this.showRightSlider = true;
      this.lawLabel = '新增法律法规';
      this.formData = new Object;
      this.$nextTick(function() {
        this.$refs['form'].clearValidate();
      });
      var timestamp = Date.parse(new Date())
      this.$set(this.formData, 'exeDate', timestamp);
      if (leftSelNodeData) {
        this.$set(this.formData, 'parentLegalId', leftSelNodeData.legalId);
      }
    },
    // 新增条款
    addClause: function(leftSelNodeData) {
      var _this = this;
      this.item = 'clause';
      this.fileList2 = [];
      this.isEdit = false;
      this.showRightSlider = true;
      this.clauseLabel = '新增条款';
      this.formData2 = new Object;
      this.$nextTick(function() {
        this.$refs['form2'].clearValidate();
      });
      this.getMaxSortNo();
      if (leftSelNodeData) {
        this.$set(this.formData2, 'legalId', leftSelNodeData.legalId);
      }
    },
    // 点击编辑法律法规
    editLaw: function(row) {
      var _this = this;
      this.isEdit = true;
      this.lawLabel = '编辑法律法规';
      this.lawData = row;
      request('', {
        type: 'post',
        url: ctx + 'aea/service/legal/getAeaServiceLegal.do',
        data: {
          id: row.legalId
        }
      }, function(res) {
        _this.showRightSlider = true;
        _this.formData = res;
        _this.fileList = res.legalAtts;
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    // 点击编辑条款
    editClause: function(row) {
      var _this = this;
      this.isEdit = true;
      this.clauseData = row;
      this.clauseLabel = '编辑条款';
      request('', {
        type: 'post',
        url: ctx + 'aea/service/legal/clause/getAeaServiceLegalClause.do',
        data: {
          id: row.legalClauseId
        }
      }, function(res) {
        _this.showRightSlider = true;
        _this.formData2 = res;
        _this.fileList2 = res.legalClauseAtts;
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    lawSave: function() {
      var _this = this;
      var param = {
        legalId: this.formData.legalId || '',
        parentLegalId: this.formData.parentLegalId || '',
        legalName: this.formData.legalName || '',
        legalLevel: this.formData.legalLevel || '',
        basicNo: this.formData.basicNo || '',
        issueOrg: this.formData.issueOrg || '',
        exeDate: _this.dateFormat(this.formData.exeDate) || '',
        serviceLegalMemo: this.formData.serviceLegalMemo || ''
      };
      console.log(param)
      var formData = new FormData();
      for (var k in param) {
        formData.append(k, param[k])
      }

      this.fileList.forEach(function(item) {
        formData.append('serviceLegalAttFile', item)
      })
      this.$refs['form'].validate(function(valid) {
        if (valid) {
          $.ajax({
            url: ctx + 'aea/service/legal/saveAeaServiceLegal.do',
            type: 'post',
            data: formData,
            dataType: 'json',
            contentType: false,
            processData: false,
            success: function(res) {
              if (res.success) {
                _this.$message({
                  message: '保存成功',
                  type: 'success'
                });
              }
              _this.showRightSlider = false;
              _this.getTreeData();
              _this.fileList = [];
            },
            error: function(msg) {
              _this.$message({
                message: '保存失败',
                type: 'error'
              });
            },
          })
        } else {
          _this.forbidden = false;
        }
      })
    },
    clauseSave: function() {
      var _this = this;
      var param = {
        legalClauseId: this.formData2.legalClauseId || '',
        legalId: this.formData2.legalId || '',
        clauseTitle: this.formData2.clauseTitle || '',
        clauseContent: this.formData2.clauseContent || '',
        sortNo: this.formData2.sortNo || ''
      };
      console.log(param)
      var formData = new FormData();
      for (var k in param) {
        formData.append(k, param[k])
      }

      this.fileList2.forEach(function(item) {
        formData.append('clauseAttFile', item)
      })
      this.$refs['form2'].validate(function(valid) {
        if (valid) {
          $.ajax({
            url: ctx + 'aea/service/legal/clause/saveAeaServiceLegalClause.do',
            type: 'post',
            data: formData,
            dataType: 'json',
            contentType: false,
            processData: false,
            success: function(res) {
              if (res.success) {
                _this.$message({
                  message: '保存成功',
                  type: 'success'
                });
              }
              _this.showRightSlider = false;
              _this.getTreeData();
              _this.fileList2 = [];
            },
            error: function(msg) {
              _this.$message({
                message: '保存失败',
                type: 'error'
              });
            },
          })
        } else {
          _this.forbidden = false;
        }
      })
    },

    // 获取编号
    getMaxSortNo: function() {
      var _this = this;
      request('', {
        type: 'post',
        url: ctx + 'aea/service/legal/clause/getMaxSortNo.do',
        data: {}
      }, function(res) {
        _this.formData2.sortNo = res;
      }, function(err) {
        _this.$message.error('服务器错了哦!');
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

    },
    handleCurrentChange: function(val) {
      this.pageNum = val;
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
        this.notLaw = true;
      }
      if (data.type == 'legal') {
        this.notRoot = true;
        this.notLaw = true;
      }
      if (data.type == 'clause') {
        this.notLaw = false;
        this.notRoot = false;
      }
      $('.right-click-opt').css({ 'left': event.clientX, 'top': event.clientY });

      this.leftSelNode = Node;
      this.leftSelNode = Node;
      this.leftSelNodeData = data.data;
    },
    // 删除数组里指定的对象
    removeArray: function(_arr, _obj) {
      if (!_arr) return;
      var length = _arr.length;
      if (length.length == 0) return;
      for (var i = 0; i < length; i++) {
        if (_arr[i] === _obj) {
          _arr.splice(i, 1);
          return _arr;
        }
      }
    },
    deleteLawAttr: function(item) {
      var _this = this;
      if (!this.isEdit) {
        _this.fileList = _this.removeArray(_this.fileList, item);
        return;
      }
      confirmMsg('', '此操作将删除附件，您确定删除吗？', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/service/legal/deleteAtt.do',
          data: {
            type: 'serviceLegalAtt',
            bizId: _this.lawData.legalId,
            detailId: item.detailId
          }
        }, function(res) {
          _this.fileList = _this.removeArray(_this.fileList, item);

          _this.$message({
            message: '删除附件成功',
            type: 'success'
          });
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    deleteClauseAttr: function(item) {
      var _this = this;
      if (!this.isEdit) {
        _this.fileList2 = _this.removeArray(_this.fileList2, item);
        return;
      }
      confirmMsg('', '此操作将删除附件，您确定删除吗？', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/service/legal/deleteAtt.do',
          data: {
            type: 'clauseAtt',
            bizId: _this.clauseData.legalClauseId,
            detailId: item.detailId
          }
        }, function(res) {
          _this.fileList2 = _this.removeArray(_this.fileList2, item);

          _this.$message({
            message: '删除附件成功',
            type: 'success'
          });
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    previewAttr: function(item) {
      var _this = this;
      window.open(ctx + 'aea/item/mat/showFile.do?detailId=' + item.detailId);
    },
    downloadAttr: function(item) {
      var _this = this;
      window.open(ctx + 'aea/item/mat/downloadGlobalMatDoc.do?detailId=' + item.detailId);
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange: function(file, fileList) {
      console.log(fileList)
      var ts = this;
      // ts.fileList = [];
      // debugger
      fileList.forEach(function(item) {
        if (item.raw) {
          ts.fileList.push(item.raw)
        };

      })
      var a = this.fileList;
    },
    // 附件上传文件列表移除某一项
    enclosureFileSelectRemove: function(file, fileList) {
      console.log(fileList)
      var ts = this;
      ts.fileList = [];
      fileList.forEach(function(item) {
        ts.fileList.push(item.raw)
      })
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange2: function(file, fileList2) {
      console.log(fileList2)
      var ts = this;
      // ts.fileList = [];
      // debugger
      fileList2.forEach(function(item) {
        if (item.raw) {
          ts.fileList2.push(item.raw)
        };

      })
      var a = this.fileList2;
    },
    // 附件上传文件列表移除某一项
    enclosureFileSelectRemove2: function(file, fileList2) {
      console.log(fileList)
      var ts = this;
      ts.fileList = [];
      fileList.forEach(function(item) {
        ts.fileList.push(item.raw)
      })
    },
    // 格式化时间
    dateFormat: function(date) {
      var daterc = date;
      if (daterc != null) {
        var dateMat = new Date(parseInt(daterc));
        return formatDate(dateMat, 'yyyy-MM-dd')
      }
    },
    regionTreeNode: function(data, node) {
      var _this = this;
      if ($(_this.$refs.Tree.$el).children().hasClass('is-current')) {
        $(_this.$refs.Tree.$el).children().removeClass("is-current")
      }
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
    keyword: function(val) {
      this.keyword = val;
    },
    keyword3: function(val) {
      this.keyword3 = val;
      this.showCertData();
    },
    keyword2: function(val) {
      this.$refs.Tree.filter(val);
    },
  }
})