var app = new Vue({
  el: '#app',
  data: function() {
    return {
      dialogTitie: '',
      multipleSelection: [],
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
      matLabel: '新增材料',
      formData: {},
      leftSelNode: {},
      leftSelNodeData: {},
      activeName: 'first',
      treeEdit: false,
      matTypeId: 'root',
      notRoot: true,
      checkedId: false,
      parentData: [],
      fileList: [],
      fileList2: [],
      fileList3: [],
      matDialog: false,
      matType: {},
      rules: {
        matTypeName: [
          { required: true, message: '请选择材料类别' },
        ],
        matName: [
          { required: true, message: '请输入材料名称' },
        ],
        matCode: [
          { required: true, message: '请输入材料编号' },
        ],
        isCommon: [
          { required: true, message: '此项为必填' },
        ],
        zcqy: [
          { required: true, message: '此项为必填' },
        ],
        isOfficialDoc: [
          { required: true, message: '此项为必填' },
        ],
        reviewKeyPoints: [
          { required: true, message: '请输入审查要点' },
        ],
      },
      isUsed: false,
      master: false,
      templateData: [],
      sampleData: [],
      reviewSampleData: [],
      templateDialog: false,
      sampleDialog: false,
      reviewSampleDialog: false,
      curRow: {},
      matFrom: [],
    }
  },
  created: function() {
    this.showData();
    this.getTreeData();
    $(".loading").hide();
  },
  methods: {
    // 请求table数据
    showData: function() {
      var vm = this;
      var param = {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        keyword: this.keyword
      }
      request('', {
        type: 'get',
        url: ctx + 'aea/item/mat/listGlobalMatNew.do',
        data: param
      }, function(res) {
        vm.tableData = res.rows;
        vm.loading = false
        vm.total = res.total;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 请求树数据
    getTreeData: function() {
      var vm = this;
      request('', {
          type: 'get',
          url: ctx + 'aea/item/mat/type/gtreeMatTypeForEUi.do',
        }, function(res) {
          vm.regionData = res;
        },
        function(err) {
          vm.$message.error('服务器错了哦!');
        })
    },
    filterNodeOrg: function(value, data) {
      if (!value) {
        return true;
      }
      return data.label.indexOf(value) !== -1;
    },
    handleSelectionChange: function(val) {
      this.multipleSelection = val;
    },
    // 删除窗口
    delateWindow: function(row) {
      var _this = this;
      confirmMsg('', '此操作将删除当前库材料，您确定执行吗？', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/item/mat/deleteAeaItemMatById.do',
          data: {
            id: row.matId
          }
        }, function(res) {
          _this.$message({
            message: res.message,
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
      var names = [];
      this.multipleSelection.map(function(item) {
        ids.push(item.matId);
        names.push(item.matName);
      })
      if (ids.length == 0) {
        _this.$message({
          message: '请先选择要删除的材料',
          type: 'error'
        });
        return;
      }
      confirmMsg('', '此操作将批量删除当前库材料，您确定执行吗？', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/item/mat/batchDeleteAeaItemMatByIds.do',
          data: {
            ids: ids.join(','),
            names: names.join(',')
          }
        }, function(res) {
          _this.$message({
            message: res.message,
            type: 'success'
          });
          _this.showData();
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    addType: function() {
      var _this = this;
      $('.form').scrollTop(0);

      this.matLabel = '新增材料';
      this.showRightSlider = true;
      this.formData = new Object;
      this.$nextTick(function() {
        this.$refs['form'].clearValidate();
      });
      this.$set(this.formData, 'isCommon', '1')
      this.$set(this.formData, 'matId', '')
      this.$set(this.formData, 'zcqy', '1')
      this.$set(this.formData, 'isOfficialDoc', '0')
      this.$set(this.formData, 'paperIsRequire', '0')
      this.$set(this.formData, 'attIsRequire', '0')
      this.$set(this.formData, 'duePaperCount', '')
      this.$set(this.formData, 'dueCopyCount', '')
      this.$set(this.formData, 'isGlobalShare', '1')
      this.$set(this.formData, 'isActive', '1')
      this.matFrom = new Array;
      this.getCode();
    },
    // 点击编辑窗口
    editWindow: function(row) {
      var _this = this;
      $('.form').scrollTop(0);
      this.matLabel = '编辑材料';
      // $(".loading").show();
      _this.showRightSlider = true;
      _this.master = true;
      _this.curRow = row;
      if (row.matFrom) {
        this.matFrom = row.matFrom.split(',');
        this.matFrom = this.matFrom.map(String);
      }

      request('', {
        type: 'get',
        url: ctx + 'aea/item/mat/getAeaItemMat.do',
        data: {
          id: row.matId
        }
      }, function(res) {
        _this.master = false;

        _this.formData = res;
      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },
    // 单击选中
    rowClick: function(row, column, event) {
      if (column.label == '操作') return;
      this.multipleSelection = row;
      this.$refs.table.toggleRowSelection(row);

    },
    checkCode: function() {
      var _this = this;
      request('', {
        type: 'post',
        url: ctx + 'aea/item/mat/checkMatCode.do',
        data: {
          matId: this.curRow.matId || '',
          matCode: this.formData.matCode
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
    getCode: function() {
      var _this = this;
      request('', {
        type: 'post',
        url: ctx + 'aea/item/mat/getOneOrgAutoCode.do',
        data: {
          codeIc: 'AEA-ITEM-MAT-CODE'
        }
      }, function(res) {
        _this.$nextTick(function() {
          _this.$set(this.formData, 'matCode', res.content)
        })

      }, function(err) {
        _this.$message.error('服务器错了哦!');
      })
    },


    save: function() {
      var _this = this;
      var param = {
        matId: this.formData.matId || '',
        isGlobalShare: this.formData.isGlobalShare || '',
        isActive: this.formData.isActive || '',
        matTypeId: this.formData.matTypeId || '',
        matTypeName: this.formData.matTypeName || '',
        matName: this.formData.matName || '',
        matCode: this.formData.matCode || '',
        isCommon: this.formData.isCommon || '',
        zcqy: this.formData.zcqy || '',
        isOfficialDoc: this.formData.isOfficialDoc || '',
        matFrom: this.matFrom.join(',') || '',
        matFromDept: this.formData.matFromDept || '',
        reviewKeyPoints: this.formData.reviewKeyPoints || '',
        reviewBasis: this.formData.reviewBasis || '',
        paperIsRequire: this.formData.paperIsRequire || '',
        attIsRequire: this.formData.attIsRequire || '',
        paperMatType: this.formData.paperMatType || '',
        duePaperType: this.formData.duePaperType || '',
        duePaperCount: this.formData.duePaperCount || '',
        dueCopyType: this.formData.dueCopyType || '',
        dueCopyCount: this.formData.dueCopyCount || '',
        matMemo: this.formData.matMemo || ''

      };
      console.log(param)
      var formData = new FormData();
      for (var k in param) {
        formData.append(k, param[k])
      }

      this.fileList.forEach(function(item) {
        formData.append('templateDocFile', item)
      })
      this.fileList2.forEach(function(item) {
        formData.append('sampleDocFile', item)
      })
      this.fileList3.forEach(function(item) {
        formData.append('reviewSampleDocFile', item)
      })
      this.$refs['form'].validate(function(valid) {
        if (valid) {
          $.ajax({
            url: ctx + 'aea/item/mat/handleGlobalMat.do',
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
              _this.showData();
              _this.fileList = [];
              _this.fileList2 = [];
              _this.fileList3 = [];
            },
            error: function(msg) {
              _this.$message({
                message: '保存失败',
                type: 'error'
              });
            },
          })
        } else {

        }
      })
    },
    templateClick: function(type) {
      var vm = this;
      var param = {
        pageNum: 1,
        pageSize: 100,
        order: 'asc',
        tableName: 'AEA_ITEM_MAT',
        pkName: type == 'template' ? 'TEMPLATE_DOC' : type == 'sample' ? 'SAMPLE_DOC' : 'REVIEW_SAMPLE_DOC',
        recordId: this.curRow.matId
      }
      request('', {
        type: 'post',
        url: ctx + 'bsc/att/listAttLinkAndDetail.do',
        data: param
      }, function(res) {
        if (type == 'template') {
          vm.templateData = res.rows;
        } else if (type == 'sample') {
          vm.sampleData = res.rows;
        } else {
          vm.reviewSampleData = res.rows;
        }
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    regionTreeNode: function(data, node) {
      var _this = this;
      this.matType = data;
      if ($(_this.$refs.Tree.$el).children().hasClass('is-current')) {
        $(_this.$refs.Tree.$el).children().removeClass("is-current")
      }
    },
    chooseMat: function() {
      this.$set(this.formData, 'matTypeName', this.matType.label);
      this.$set(this.formData, 'matTypeId', this.matType.id);
      this.matDialog = false;
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange: function(file, fileList) {
      console.log(fileList)
      var ts = this;
      ts.fileList = [];
      fileList.forEach(function(item) {
        if (item.raw) {
          ts.fileList.push(item.raw)
        };

      })
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange2: function(file, fileList2) {
      console.log(fileList2)
      var ts = this;
      ts.fileList2 = [];
      fileList2.forEach(function(item) {
        if (item.raw) {
          ts.fileList2.push(item.raw)
        };

      })
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange3: function(file, fileList3) {
      console.log(fileList3)
      var ts = this;
      ts.fileList3 = [];
      fileList3.forEach(function(item) {
        if (item.raw) {
          ts.fileList3.push(item.raw)
        };

      })
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
    removeAttr: function(item, type) {
      if (type == 'template') {
        this.fileList = this.removeArray(this.fileList, item);
      } else if (type == 'sample') {
        this.fileList2 = this.removeArray(this.fileList2, item);
      } else {
        this.fileList3 = this.removeArray(this.fileList3, item);
      }

    },
    previewAttr: function(item) {
      var _this = this;
      window.open(ctx + 'aea/item/mat/showFile.do?detailId=' + item.detailId);
    },
    downloadAttr: function(item) {
      var _this = this;
      window.open(ctx + 'aea/item/mat/downloadGlobalMatDoc.do?detailId=' + item.detailId);
    },
    // 预览图片或者下载文件
    previewOrDownload: function(item) {
      if (item.attFormat == 'png' || item.attFormat == 'jpg' || item.attFormat == 'jpeg' || item.attFormat == 'gpe' || item.attFormat == 'gif') {
        this.previewAttr(item);
      } else {
        this.downloadAttr(item);
      }
    },
    deleteAttr: function(item, type) {
      var _this = this;
      confirmMsg('', '此操作将删除附件，您确定删除吗？', function() {
        request('', {
          type: 'post',
          url: ctx + 'aea/item/mat/deleteGlobalMatDoc.do',
          data: {
            detailId: item.detailId,
            type: type == 'template' ? 0 : type == 'sample' ? 1 : 2,
            matId: _this.curRow.matId
          }
        }, function(res) {
          _this.editWindow(_this.curRow);
          _this.templateClick(type);
          _this.$message({
            message: '删除附件成功',
            type: 'success'
          });
        }, function(err) {
          _this.$message.error('服务器错了哦!');
        })
      })
    },
    handleSizeChange: function(val) {
      this.pageSize = val;
      this.showData();

    },
    handleCurrentChange: function(val) {
      this.pageNum = val;
      this.showData();
    },

  },
  filters: {
    sizeFixed: function(item) {
      return item = item / 1000;
    },
    resource: function(val) {
      var arr = [];
      var data = val.split(',');
      for (var i = 0; i < data.length; i++) {
        var str = '';
        switch (data[i]) {
          case "1":
            str = "行政机关";
            break;
          case "2":
            str = "企事业单位";
            break;
          case '3':
            str = "社会组织";
            break;
          case "4":
            str = "申请人";
            break;
          default:
            str = "中介服务";
            break;
        }
        arr.push(str);
      }
      return arr.join(',');

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