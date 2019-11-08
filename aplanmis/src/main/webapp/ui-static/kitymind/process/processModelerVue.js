var vm = new Vue({
  el: '#app',
  data: {
    appId:appId,
    loading:false,
    isShowElementConfig:false, // 是否显示元素配置页面
    isShowChoosePublicElement:false, // 是否显示选择公共元素页面
    choosePBElementTableData:[], // 未绑定公共元素列表数据
    elementTableData:[], // 已绑定公共元素列表数据
    chooseElepageSize:10, // 未绑定公共元素列表页码请求数量
    chooseEleTotal:0, // 未绑定公共元素列表total
    //导入元素
    isshowChooseFlowList4:false,
    isShowMask:false,
    multipleSelection:[],
    multipleSelection2:[],
    choosePBElementTableData:[], // 未绑定公共元素列表数据
    dragstatus:false,
    showTip:false,
    elementKeyword:'' , // 公共元素keyword
    table3Heigth:document.documentElement.clientHeight - 170,
    processTableData: [],//流程数据
    authTableNoDataText: '查询不到业务流程模板关联的流程数据！',
    defaultExpand: 2,//默认展开
    currentExpand: 0,//当前展开
    indent: 40,//缩进
    pageLoading: false,//
  },
  created: function () {
    this.getProcessDefTree();
  },
  methods: {
    elementConfig:function () {
      this.isShowElementConfig = true;
      this.getPublicElementList(vm.appId);
    },
    // 选择公共页面元素弹出 选中的列的数组集合
    changeFun :function (val) {
      this.multipleSelection = val
    },
    changeFun2:function (val){
      this.multipleSelection2 = val
    },
    chooseEleSizeChange:function(val) {
      this.chooseElepageSize = val;
      this.importElement()
    },
    chooseEleCurrentChange:function(val) {
      this.chooseElecurrentPage = val;
      this.importElement()
    },
    // 保存业务流程模板和公共页面元素关联信息
    savePublicElement:function () {
      var vm = this;
      var multipleSelection = vm.multipleSelection;
      var elementTableData = vm.elementTableData;
      for (var i = 0;i < multipleSelection.length;i++){
        for(var j = 0; j < elementTableData.length;j++){
          if(multipleSelection[i].elementId == elementTableData[j].elementId){
            alertMsg('','该元素已经导入了，不能重复导入！');
            return false;
          }
        }
      }
      if(multipleSelection.length === 0){
        alertMsg('','你还没选中');
      }else{
        var params = new Array();
        for (var i = 0;i < multipleSelection.length;i++){
          var appElementJsonDataItem = {elementId:multipleSelection[i].elementId,appId:vm.appId}
          params.push(appElementJsonDataItem);
        }
        request('bpmAdminUrl',{
          type:'post',
          url:ctx+ '/rest/act/tpl/app/savePublicElement',
          data:{
            appElementJsonData:JSON.stringify(params)
          },
        }, function (res) {
          if(res.success){
            vm.$message.success(res.message)
            vm.isShowChoosePublicElement = false
            vm.getPublicElementList(vm.appId)
          }else{
            vm.$message.error(res.message)
          }
        }, function () {
          vm.$message.error('服务器失败');
        });
      }
    },
    //导入流程 下拉框出现/隐藏时触发
    toggleFlowListSelect:function (flag){
      var vm = this
      setTimeout(function(){
        vm.isHideMask = flag
      },200)
    },
    // 导入元素
    importElement:function () {
      // this.isShowChoosePublicElement = true;
      this.multipleSelection = []
      request('bpmAdminUrl',{
        type:'get',
        url:ctx+ '/rest/act/sto/element/getAllElementListNoPage',  //获取平台内置、当前登录人所属机构的元素集合  getAllElementListNoPage getAllElementListByOrgIds
        data:{
          // pageNum:vm.chooseElecurrentPage,
          // pageSize:vm.chooseElepageSize
        }
      }, function (res) {
        if(res.success){
          vm.choosePBElementTableData = res.content;
          //vm.chooseEleTotal = res.content.total;
        }else{
          vm.$message.error(res.message)
        }
      }, function () {
        vm.$message.error('服务器失败');
      });
    },
    start_drag:function(){
      // 添加拖拽属性
      this.dragstatus=true;
      this.showTip=true;
    },
    cancleSort:function(){
      this.showTip=false;
      this.dragstatus=false;
      this.getPublicElementList(this.appId)
    },
    saveSort:function(){
      var vm = this;
      this.showTip=false;
      this.dragstatus=false;
      //$("#draggaleIcon").remove();
      var copyElementTableData =vm.copyElementTableData;
      for(var i=0; i<copyElementTableData.length; i++){
        (function(num){
          request('bpmAdminUrl',{
            type:'post',
            url:ctx+'/rest/act/tpl/app/sortPublicElement',
            data:{
              appElementId:copyElementTableData[i].appElementId,
              sortNo:(num+1)
            },
          },function (res) {
            if(num == copyElementTableData.length-1 ){
              vm.getPublicElementList(vm.appId)
            }
          },function (err) {
            vm.$message.error('服务器出错了哦!');
          })
        })(i)
      }
    },
    // 移除元素
    deleteElementItem:function (appElementIds) {
      var vm = this;
      confirmMsg('提示','删除元素会同步删除元素的权限配置信息，确定要删除吗？',function () {
        request('bpmAdminUrl',{
          type:'delete',
          url:ctx+ '/rest/act/tpl/app/deletePublicElementBatch?appElementIds=' + appElementIds,

        }, function (res) {

          if(res.success){
            vm.$message.success(res.message)
            vm.getPublicElementList(vm.appId)
          }else{
            vm.$message.error(res.message)
          }
        }, function () {
          vm.$message.error('服务器失败');
        });
      })

    },
    // 批量移除元素
    deletePublicElementBatch:function (){
      var vm = this;
      var multipleSelection2 = vm.multipleSelection2;
      if(multipleSelection2.length === 0){
        alertMsg('','你还没选中');
      }else{
        var appElementIdsArry = new Array();
        multipleSelection2.forEach(function(item,index){
          appElementIdsArry.push(item.appElementId);
          var appElementIds =  appElementIdsArry.join(',')
          vm.deleteElementItem(appElementIds)
        })
      }
    },
    // 保存业务流程模板和公共页面元素关联信息
    savePublicElement:function () {
      var vm = this;
      var multipleSelection = vm.multipleSelection;
      var elementTableData = vm.elementTableData;
      for (var i = 0;i < multipleSelection.length;i++){
        for(var j = 0; j < elementTableData.length;j++){
          if(multipleSelection[i].elementId == elementTableData[j].elementId){
            alertMsg('','该元素已经导入了，不能重复导入！');
            return false;
          }
        }
      }
      if(multipleSelection.length === 0){
        alertMsg('','你还没选中');
      }else{
        var params = new Array();
        for (var i = 0;i < multipleSelection.length;i++){
          var appElementJsonDataItem = {elementId:multipleSelection[i].elementId,appId:vm.appId}
          params.push(appElementJsonDataItem);
        }
        request('bpmAdminUrl',{
          type:'post',
          url:ctx+ '/rest/act/tpl/app/savePublicElement',
          data:{
            appElementJsonData:JSON.stringify(params)
          },
        }, function (res) {
          if(res.success){
            vm.$message.success(res.message)
            vm.isShowChoosePublicElement = false
            vm.getPublicElementList(vm.appId)
          }else{
            vm.$message.error(res.message)
          }
        }, function () {
          vm.$message.error('服务器失败');
        });
      }
    },
    // 搜索公共元素配置
    searchElementData:function () {
      this.getPublicElementList(this.appId)
    },
    // 根据业务流程模板id，获取业务流程模板关联的公共页面元素列表
    getPublicElementList:function (appId) {
      var vm = this;
      vm.elementTableData = [];
      request('bpmAdminUrl',{
        type:'get',
        url:ctx+ '/rest/act/tpl/app/getPublicElementListNoPage',
        data: {
          appId:appId,
          keyword:vm.elementKeyword
        }
      }, function (res) {
        if(res.success){
          vm.elementTableData = res.content;
        }else{
          vm.$message.error(res.message)
        }
      }, function () {
        vm.$message.error('服务器失败');
      });
    },

    //使用element-ui渲染流程树
    getProcessDefTree: function () {
      var vm = this;
      vm.processTableData = [];
      vm.authTableNoDataText = '正在加载数据......';
      request('bpmAdminUrl', {
        type: 'get',
        url: ctx + '/rest/mind/processDefTree.do?isPid=false',
        data: {
          appId: appId,
          stateVerId: currentStateVerId,
          keyword: $("#keyword").val()
        },
        timeout: 300000
      }, function (res) {
        if (res && res.length > 0) {
          vm.processTableData = res;
          //必须在表格渲染完成才调用
          vm.$nextTick(function () {
            vm.expand(vm.defaultExpand);
          })
        } else {
          vm.authTableNoDataText = '查询不到业务流程模板关联的流程数据！';
        }
      }, function () {
        vm.$message.error('服务器失败');
        vm.authTableNoDataText = '查询不到业务流程模板关联的流程数据！';
      });
    },
    //树的展开到指定级别
    expand: function (level) {
      vm.currentExpand = level;
      var state = "el-table__expand-icon--expanded";
      if (level && level > 0) {
        var box = $('.el-table__row');
        for (var l = 0; l < level; l++) {
          var classStr = 'el-table__row--level-' + l;
          for (var i = 0; i < box.length; i++) {
            var _this = box.eq(i);
            var expand = $(_this).find(".el-table__expand-icon");
            if (_this.hasClass(classStr) && !expand.hasClass(state)) {
              _this.find('.el-icon.el-icon-arrow-right').trigger('click');
            }
          }
        }
      }
    },
    //全部展开
    allExpand: function () {
      var state = "el-table__expand-icon--expanded";
      var box = $('.el-table__row');
      for (var i = 0; i < box.length; i++) {
        var _this = box.eq(i);
        var expand = $(_this).find(".el-table__expand-icon");
        if (!expand.hasClass(state)) {
          _this.find('.el-icon.el-icon-arrow-right').trigger('click');
        }
      }
    },
    //全部折叠
    allNotExpand: function () {
      var state = "el-table__expand-icon--expanded";
      var box = $('.el-table__row');
      for (var i = 0; i < box.length; i++) {
        var _this = box.eq(i);
        var expand = $(_this).find(".el-table__expand-icon");
        if (expand.hasClass(state)) {
          _this.find('.el-icon.el-icon-arrow-right').trigger('click');
        }
      }
    },
    //启动情形
    showStartEl: function (row) {

      $('#tplAppDefElDialog').modal('show');
      appFlowdefIdVal = row.appFlowdefId;
      var startEl = row.startEl;
      $("#startEl_select").empty();
      $("#startEl_select").append('<option value="">请选择</option>');

      var url;
      var data;
      var stateKey;
      if (currentBusiType == "item") {
        url = ctx + '/aea/item/state/getProcStartCondItemStates.do';
        data = {stateVerId: currentStateVerId};
        stateKey = 'itemStateId';
      } else {
        url = ctx + '/admin/aea/stage/state/getProcStartCondStageStates.do';
        data = {stageId: currentBusiId};
        stateKey = 'parStateId';
      }
      $.ajax({
        url: url,
        data: data,
        type: 'POST',
        success: function (data) {
          if (data != '' && data.length > 0) {
            $.each(data, function (i, n) {
              if (startEl == ("${form.stateinsts['" + n[stateKey] + "']==true}")) {
                $("#startEl_select").append(" <option value=\"${form.stateinsts['" + n[stateKey] + "']==true}\"  selected>" + n['stateName'] + "</option>");
              } else if (n['value'] != '') {
                $("#startEl_select").append(" <option value=\"${form.stateinsts['" + n[stateKey] + "']==true}\">" + n['stateName'] + "</option>");
              }
            });
          }
        },
        error: function () {
          swal('错误信息', '服务器异常！', 'error');
        }
      });
    },
    chooseProcDef: function (row) {
      window.open(ctx + '/bpm/admin/modeler/index.html#/editor/' + row.modelId, '_blank');
    },
    authConfig: function (row) {
      window.open(ctx + '/bpm/admin/template/authConfigForm.html?appFlowdefId=' + row.appFlowdefId + '&appId=' + appId, '_blank');
    },
    showSubprocess: function (row) {
      showSubprocess(row.appFlowdefId, row.defKey, row.defName, row.modelId);
    },
    delActTplAppProc: function (row) {
      delActTplAppProc(row.appFlowdefId);
    },
    setProcMaster: function (row) {
      setProcMaster(row.appFlowdefId);
    },
    editLimit: function (row) {
      if (row.isProcess == '1') {
        editLimit(row.timeLimit, row.timeruleId, row.appFlowdefId);
      } else {
        editLimit(row.timeLimit, row.timeruleId, row.appFlowdefId, row.modelId);
      }
    },
    openTimeGroupDialog: function(row){
      openTimeGroupDialog(row, this);
    },
    addSubProcess: function (type) {
      vm.pageLoading = true;
      //由于缓冲加载效果的原因要把方法放到setTimeout中
      setTimeout(function () {
        $('#busRecordId').val('');
        $('#busRecordName').val('');
        $('#subprocess_modal').modal("show");
        onloadNode();
        onloadTriggerAppFlowdefId();
        // $('input[name="isOuterFlow"]').prop("checked",false);
        isOuterFlow()
        if (type == 0) {
          $('#triggerId').text("");
        }
        if (currentBusiType == 'stage') {
          $('#form_bus').attr("hidden", false);
          // onloadBus();
          onloadItemTree();
        } else {
          $('#form_bus').attr("hidden", true);
        }
        vm.pageLoading = false;
      }, 50);

    },
    importProcess: function () {
      vm.pageLoading = true;
      setTimeout(function () {
        procIdsMap = {};
        $('#tplAppProcChooseDialog').modal('show');
        searchTplAppProcChooseDataV2();
        vm.pageLoading = false;
      }, 50);
    }

  },
  // 自定义拖拽指令
  directives: {
    drag: {
      // 初始化为所以的的数据添加拖拽属性
      componentUpdated: function (el, binding, vnode) {
        if (binding.value && vm.dragstatus) {
          // 放入marco task 运行
          setTimeout(function () {
            var elementArry = el.querySelectorAll('.el-table__body-wrapper .el-table__row')

            elementArry.forEach(function (node, index) {
              node.setAttribute("draggable", true);
              node.addEventListener("dragstart", function (event) {
                event.dataTransfer.effectAllowed = 'remove';
                event.dataTransfer.setData("source", index);
              })
              node.addEventListener("dragover", function (event) {
                event.preventDefault();
              })
              node.addEventListener("drop", function (event) {
                //event.preventDefault();
                var sourceIndex = +event.dataTransfer.getData("source")
                clearTimeout(vm.timer);
                if (index !== sourceIndex && vm.dragstatus) {
                  vm.timer = setTimeout(function () {
                    var tmpArr = vm.elementTableData.concat([]);
                    // index --> new     sourceIndex --> old
                    if ( index > sourceIndex ) {
                      // 往下拉
                      console.log(index, sourceIndex, '往下拉');
                      var tmpO = tmpArr[sourceIndex];
                      for( var i=sourceIndex; i<index; i++ ){
                        tmpArr[i] = tmpArr[i+1];
                      }
                      tmpArr[index] = tmpO;
                    } else {
                      // 往上拉
                      console.log(index, sourceIndex, '往上拉');
                      var tmpO = tmpArr[sourceIndex];
                      for (var i=sourceIndex; i>index; i--) {
                        tmpArr[i] = tmpArr[i-1];
                      }
                      tmpArr[index] = tmpO;
                    }
                    vm.elementTableData = tmpArr;
                    vm.copyElementTableData = tmpArr.concat([]);
                    event.dataTransfer.clearData();
                    return null;
                    var targetEl = elementArry[index].innerHTML;
                    var sourceEL = elementArry[sourceIndex].innerHTML;
                    elementArry[index].innerHTML = sourceEL;
                    elementArry[sourceIndex].innerHTML = targetEl;
                    event.dataTransfer.clearData();
                    // 存储改变的数据
                    var newData = $.extend(true, [], vm.elementTableData);
                    newData[index] = vm.elementTableData[sourceIndex];
                    newData[sourceIndex] = vm.elementTableData[index]
                    vm.copyElementTableData = newData;
                    //node.setAttribute("draggable",false);
                  }, 100)
                }
              });
            });

          }, 0);
        }
      }
    }
  }
})