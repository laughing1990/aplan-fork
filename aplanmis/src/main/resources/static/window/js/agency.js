var clickoutside = {
    // 初始化指令
    bind(el, binding, vnode) {
        function documentHandler(e) {
            // 这里判断点击的元素是否是本身，是本身，则返回
            if (el.contains(e.target)) {
                return false;
            }
            // 判断指令中是否绑定了函数
            if (binding.expression) {
                // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
                binding.value(e);
            }
        }
        // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
        el.__vueClickOutside__ = documentHandler;
        document.addEventListener('click', documentHandler);
    },
    unbind(el, binding) {
        // 解除事件监听
        document.removeEventListener('click', el.__vueClickOutside__);
        delete el.__vueClickOutside__;
    },
};
var app = new Vue({
      el: '#app',
      data:function(){
        return {
            dialogTitie: '新增代办中心',
            dialogWinTitie: '新增人员',
            multipleSelection:[],
            multipleSelection2:[],
            ctx:ctx,
            nounDialog:false,
            orgDialog:false,
            addWindowDialog:false,
            addWinDialog: false,
            treeLeftHeight: document.documentElement.clientHeight - 400, 
            treeLeftHeight2: document.documentElement.clientHeight - 191, 
            windowHeight: document.documentElement.clientHeight - 186, 
            leftMenuH: document.documentElement.clientHeight - 240,
            tableH: document.documentElement.clientHeight - 230,
            winH: document.documentElement.clientHeight,
            pageLoading:true,
            loading:false,
            pageNum:1,
            pageSize:10,
            total:0,
            tableData:[],
            tableData2:[],
            chooseItem: 0,
            operationItem: -1,
            activeName:'second',
            windowNameTitle: '',
            labelPosition:'right',
            noun:'',
            org:'',
            keyword:'',
            regionKeyword:'',
            orgKeyword:'',
            formData:{
              isActive :'1',
              isAllItem :'1',
              isPublic :'1',
              isAllStage :'1',
              stageItemFilter: '0',
              sortNo:'1',
              windowType:'1',
              regionId: [],  //行政区划id
              windowLevel: '区级', //代办中心级别
            },
            isActive:'',
            windowType:'',
            regionProps:{
              label: 'label',
            },
            fileList:[],
            curRegion:{},//树节点点击的数据
            curOrg:{},//树节点点击的数据
            nowRegion:{},
            nowOrg:{},
            regionData:[],
            orgData:[],
            isEdit:false,
            forbidden:false,
            editId:'',
            rules: {
                windowName: [
                    { required: true, message: '此项为必填'},
                ],
                regionId: [
                  { required: true, message: '此项为必填'},
                ],
                regionName: [
                    { required: true, message: '此项为必填'},
                ],
                sortNo: [
                    { required: true, message: '此项为必填'},
                ],
                isActive: [
                    { required: true, message: '此项为必填'},
                ],
                linkPhone: [
                    { required: true, message: '此项为必填'},
                ],
                workTime: [
                    { required: true, message: '此项为必填'},
                ],
                windowAddress: [
                    { required: true, message: '此项为必填'},
                ],
                trafficGuide: [
                    { required: true, message: '此项为必填'},
                ],
                windowLevel: [
                  { required: true, message: '此项为必选'},
              ] ,
               
            },
            isForm:false,
            allUser:[],
            storeAllUser:[],
            userChecked:[],
            userShrink:true,
            userKeyWord:'',
            windowCurId:'',
            delete:false,
            keys:[],
            userCheckedArr: [],
            userCheckedData: [],
            layerUserKeyWord: '',

             // 行政分区级联-prop
            SetDistrictDept: {
              value: 'value',
              label: 'label',
              children: 'children',
              checkStrictly: true,
              expandTrigger: 'hover'
            },
        }
      },
      directives: {
          clickoutside
      },
      created:function(){
          // this.getAllUser();
          this.showData();
          this.regionTreeData();
          this.orgTreeData();
      },
      methods:{
          // 新增编辑代办中心关闭回调
          closeWinCallback: function() {
            this.fileList = []
            this.addWindowDialog = false
          },
          // 新增xx代办中心相关操作
          openWinDialog: function() {
              if (this.activeName === 'second') {
                  if(this.allUser.length == 0){
                      this.getAllUser();
                  }
                  this.userChecked = [];
                  var data = this.userCheckedData;
                  for(var index in data){
                      var userId = data[index].userId;
                      for(var i in this.allUser){
                          var user = this.allUser[i];
                          if(userId === user.id){
                              this.userChecked.push(user);
                              break;
                          }
                      }
                  }
                  this.dialogWinTitie = '新增人员'
              }
              this.addWinDialog = true
          },
          // 新增xx代办中心确定按钮操作
          confirmAdd: function() {
            if (this.activeName === 'second') {
                // var temp = [];
                // for(var j in this.userCheckedData){
                //     temp.push(this.userCheckedData[j].userId);
                // }
                this.userCheckedData = [];
                for(var i in this.userChecked){
                    let uc = this.userChecked[i];
                    // if(temp.indexOf(uc.id) > -1){
                    //     continue;
                    // }
                    var user = new Object();
                    user.userId = uc.id;
                    user.userName = uc.userName;
                    user.userMobile = uc.userMobile;
                    user.orgName = uc.orgName;
                    user.loginName = uc.loginName;
                    this.userCheckedData.push(user);
                }
            }
            this.saveWindowUser();
            this.resetWinSearch();
          },
          // 清空新增xx代办中心里相关搜索数据
          resetWinSearch: function() {
              this.addWinDialog = false;
              this.layerUserKeyWord='';
          },
          chooseWinlist: function(item, index) {
            this.chooseItem = index;
            this.windowNameTitle = item.windowName

            this.windowCurId = item.windowId;

            if (this.activeName === 'second') {
              this.getUserByWindowId();
            }
          },
          closeOpLayer: function() {
            this.operationItem = -1;
          },
          // 请求table数据
          showData: function() {
              var vm = this;
              request('',{
                  type:'post',
                  url:ctx+'aea/service/window/listAgencyWin.do',
                  data:{
                      keyword: this.keyword,
                      windowType: 'd'
                  },
              },function (res) {

                  vm.tableData2 = res.content;

                  if (vm.tableData2.length) {
                    vm.windowNameTitle = vm.tableData2[0].windowName
                    vm.windowCurId = vm.tableData2[0].windowId;
                    vm.getUserByWindowId();
                  }
                  $('.loading').hide();
              },function (err) {
                  vm.$message.error('服务器错了哦!');
              })
          },
          // 请求无分页所有代办中心数据
          // showAllData: function() {
          //     var vm = this;
          //     // vm.loading = true;
          //     request('',{
          //         type:'get',
          //         url:ctx+'aea/service/window/listAeaServiceWindow.do',
          //         data:{},
          //     },function (res) {
          //         vm.tableData2 = res.content;
          //         if (vm.tableData2.length) vm.windowNameTitle = vm.tableData2[0].windowName
          //         vm.loading = false
          //     },function (err) {
          //         vm.$message.error('服务器错了哦!');
          //     })
          // },
          selectable:function(row,index){
            if(row){
              return 0;
            }
          },
          // 请求地区树数据
          regionTreeData: function() {
              var vm = this;
              // vm.loading = true;
              // request('',{
              //     type:'get',
              //     url:ctx+'aea/service/window/listRegionTreeByOrgId.do',
              // },function (res) {
              //     var arr = [];
              //     arr.push(res);
              //     vm.regionData = arr;
              //     vm.loading = false
              // },function (err) {
              //     vm.$message.error('服务器错了哦!');
              // })

            request('', {
              type: 'get',
              url: ctx + 'aea/service/window/getRegionOptions',
            }, function (res) {
              if (res.success) {
                vm.regionData = res.content;
                vm.handelRegionTreeData( vm.regionData)
              } else {
                return vm.$message.error(res.message)
              }
            }, function (err) {
              vm.$message.error('服务器错了哦!');
            })
          },
          // 处理地区数据
          handelRegionTreeData: function(list){
            for(var i = 0; i <list.length; i++){
              if(list[i].children && list[i].children.length){
                this.handelRegionTreeData(list[i].children)
              }else {
                delete list[i].children
              }
            }
          },
          // 请求部门树数据
          orgTreeData: function() {
              var vm = this;
              // vm.loading = true;
              request('',{
                  type:'get',
                  url:ctx+'opus/om/org/admin/getOpusOmOrgElementUINode.do',
              },function (res) {
                  var arr = [];
                  arr.push(res);
                  vm.orgData = arr;
                  vm.loading = false
              },function (err) {
                  vm.$message.error('服务器错了哦!');
              })
          },
          
          clearSearch:function(){
            this.nowOrg = new Object;
            this.nowRegion = new Object;
            this.noun = '';
            this.org = '';
            this.keyword = '';
            this.showData();
          },
          regionTreeNode:function(data, node){
            var _this = this;
            this.curRegion.regionName = data.label;
            this.curRegion.regionId = data.id;
          },
          regionSave:function(){
            if(!this.isForm){
              this.nowRegion = this.curRegion;
              this.noun = this.nowRegion.regionName;
            }else{
              this.$set(this.formData,'regionName',this.curRegion.regionName);
              this.formData.regionId = this.curRegion.regionId;
            }
            this.nounDialog = false;
          },
          orgTreeNode:function(data, node){
            var _this = this;
            this.curOrg.orgName = data.label;
            this.curOrg.orgId = data.id;
          },
          orgSave:function(){
            if(!this.isForm){
              this.nowOrg = this.curOrg;
              this.org = this.nowOrg.orgName;
            }else{
              this.$set(this.formData,'orgName',this.curOrg.orgName);
              this.formData.orgId = this.curOrg.orgId;
            }
            this.orgDialog = false;
          },
          // 删除代办中心
          delateWindow:function(id){
            var _this=this;
            confirmMsg('','此操作将删除选择的代办中心,确定要删除吗？',function(){
              request('',{
                  type:'post',
                  url:ctx+'aea/service/window/deleteAeaServiceWindowById.do',
                  data:{
                    windowId : id
                  }
              },function (res) {
                  _this.$message({
                    message: '删除成功',
                    type: 'success'
                  });
                  _this.showData();
                  // _this.showAllData();
              },function (err) {
                  _this.$message.error('服务器错了哦!');
              })
            })
          },
          winTabelSelectionChange: function(val) {
            if (this.activeName === 'second') {
              this.userCheckedArr = val
            }
          },
          handleSelectionChange:function(val) { 
            this.multipleSelection = val;
          },
          handleSelectionChange2:function(val){
              this.multipleSelection2 = val;
              if(this.multipleSelection2.length>1){
                  this.multipleSelection2 = this.multipleSelection2[0];
                  this.$refs.multipleTable.toggleRowSelection(this.multipleSelection2);
              }

              this.windowCurId = this.multipleSelection2[0].windowId;
              this.getUserByWindowId();

          },
          addWindow:function(){
            var _this = this;
            this.addWindowDialog=true;
            this.formData = new Object;
            this.$nextTick(function () {
              this.$refs['form'].clearValidate();
            });
            this.getSortNo();
            this.fileList=[];
            this.editId = '';
            this.$set(this.formData,'isActive','1');
            this.$set(this.formData,'windowType','d');
            this.$set(this.formData,'windowLevel','区级');
          },
          getSortNo:function(){
            var _this = this;
            request('',{
                  type:'post',
                  url:ctx+'aea/service/window/getMaxSortNo.do',
                  data:{}
              },function (res) {
                _this.$set(_this.formData,'sortNo',res);
              },function (err) {
                  _this.$message.error('服务器错了哦!');
              })
          },
          // 删除代办中心
          delateAllWindow:function(){
            var _this=this;
            var ids = [];
            this.multipleSelection.map(function(item){
              ids.push(item.windowId);
            })
            confirmMsg('','此操作将删除选择的代办中心,确定要删除吗？',function(){
              request('',{
                  type:'post',
                  url:ctx+'aea/service/window/batchDeleteAeaServiceWindow.do',
                  data:{
                    windowIds : ids.join(',')
                  }
              },function (res) {
                  _this.$message({
                    message: '删除成功',
                    type: 'success'
                  });
                  _this.showData();
                  // _this.showAllData();
              },function (err) {
                  _this.$message.error('服务器错了哦!');
              })
            })
          },
          userClick:function(row, column, cell, event){
            if(this.multipleSelection2.length==1 && this.multipleSelection2[0]==row) return;
            this.$refs.multipleTable.toggleRowSelection(row);

            this.windowCurId = row.windowId;
            this.getUserByWindowId();
          },
          // 点击编辑代办中心
          editWindow:function(id){
            var _this=this;
            this.dialogTitie ='编辑代办中心';
            this.isEdit = true;
            this.editId = id;
            request('',{
                type:'post',
                url:ctx+'aea/service/window/getAeaServiceWindowById.do',
                data:{
                  windowId : id
                }
            },function (res) {
                for(var item in res){
                  _this.$set(_this.formData,item,res[item])
                }

                // 处理行政回显
                if (_this.formData.regionId) {
                  var _arr = [];
                  _arr.push(_this.formData.regionId);
                  _this.formData['regionId'] = _arr;
                }

                // _this.formData = res;
                _this.addWindowDialog=true;
                _this.getFile(id);
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          deleteAttr:function(item){
            var _this = this;
            if(!this.editId){
              this.fileList = this.removeArray(this.fileList,item);
              return
            }
            this.fileList = this.removeArray(this.fileList,item);

            request('',{
                type:'post',
                url:ctx+'aea/service/window/deleteServiceWindowMapAtt.do',
                data:{
                  windowId : this.editId,
                  detailId: item.detailId
                }
            },function (res) {
                _this.$message({
                  message: '删除附件成功',
                  type: 'success'
                });
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })

          },
          previewAttr:function(item){
            var _this = this;
            window.open(ctx+'aea/service/window/windowMapAtt/preview?detailId='+item.detailId);
          },
          downloadAttr:function(item){
            var _this = this;
            window.open(ctx+'aea/service/window/downloadServiceWindowMapAtt.do?detailId='+item.detailId);
          },
          // 查询附件
          getFile:function(id){
            var _this=this;
            request('',{
                type:'post',
                url:ctx+'aea/service/window/listWindowMapAtt.do',
                data:{
                  windowId : id
                }
            },function (res) {
                // if (res.content.length==0) return;
               _this.fileList = res.content;
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          // 新增和编辑保存代办中心
          addAndSaveWindow:function(){
            var _this=this;
            this.dialogTitie ='新增代办中心'
            this.forbidden=true;
            var param = {
              windowId:this.formData.windowId ? this.formData.windowId : '',
              isActive: this.formData.isActive,
              sortNo: this.formData.sortNo,
              windowType: this.formData.windowType,
              windowName: this.formData.windowName,
              // regionName: this.formData.regionName,
              regionId: this.formData.regionId,
              // orgName: this.formData.orgName,
              // orgId: this.formData.orgId,
              linkPhone: this.formData.linkPhone,
              workTime: this.formData.workTime,
              windowAddress: this.formData.windowAddress,
              trafficGuide: this.formData.trafficGuide,
              mapUrl: this.formData.mapUrl?this.formData.mapUrl:'',
              windowLevel: this.formData.windowLevel? this.formData.windowLevel: '',
              windowMemo: this.formData.windowMemo?this.formData.windowMemo:''
            };
             // 处理行政分区，只传最后一个地区的id
            var _regionalism = "";
            if (param.regionId) {
              var _area = "", len = param.regionId.length;
              _area =  param.regionId[len - 1];
              _regionalism = _area;
            }else{
              _regionalism = "";
            }

            var saveParams = JSON.parse(JSON.stringify(param));
            saveParams.regionId = _regionalism;
            // console.log(saveParams)
            // return;
            var formData = new FormData();
            for (var k in saveParams) {
              formData.append(k, saveParams[k])
            }
            this.fileList.forEach(function (item) {
              formData.append('mapAttFile', item)
            })
            this.$refs['form'].validate(function (valid) {
              if (valid) {
                $.ajax({
                  url: ctx + 'aea/service/window/saveAeaServiceWindow.do',
                  type: 'post',
                  data: formData,
                  contentType: false,
                  processData: false,
                  success: function (res) {
                    if (res.success) {
                      _this.$message({
                        message: '保存成功',
                        type: 'success'
                      });
                    }
                    _this.addWindowDialog = false;
                    _this.showData();
                    // _this.showAllData();
                    _this.fileList = [];
                    _this.forbidden=false;
                  },
                  error: function (msg) {
                    _this.mloading = false;
                    _this.$message({
                      message: '保存失败',
                      type: 'error'
                    });
                    _this.forbidden=false;
                  },
                })
              } else {
                _this.forbidden=false;
              }
            })
            
          },
          treeChecked:function(){
            this.delete = false;
          },
          removeRepeat:function (arr){
            if(!arr) return;
            for(var i=0;i<arr.length;i++){
                for(var j=i+1;j<arr.length;j++){
                    if(arr[i]==arr[j]){ 
                      //如果第一个等于第二个，splice方法删除第二个
                      arr.splice(j,1);
                      j--;
                    }
                }
            }
            return arr;
          },

          handleCheckChange:function(data, checked, indeterminate) {
              var _this = this;
              if(data.type!="THEME"){
                if(checked==true){
                  this.stageChecked.push(data);
                }else{
                  if(this.delete) return;
                  this.removeArray(_this.stageChecked, data);
                }
              }else{
                if(checked==true){
                  if(!this.stageChecked){
                    this.stageChecked = [];
                  }
                  if(indeterminate){
                    for(var i=0, stage= data.children; i<stage.length; i++){
                      this.removeArray(_this.stageChecked, stage[i]);
                    }
                    return;
                  }else{
                    this.stageChecked = this.stageChecked.concat(data.children)
                  }
                  
                }else{
                  if(!indeterminate && !checked){
                    for(var i=0, stage= data.children; i<stage.length; i++){
                      this.removeArray(_this.stageChecked, stage[i]);
                    }
                  }else{
                    this.removeArray(_this.stageChecked, data);
                  }
                }
              }
              this.removeRepeat(_this.stageChecked)

              this.tableStageChecked = JSON.parse(JSON.stringify(this.stageChecked)) 
          },
          handleSizeChange:function(val) {
            this.pageSize = val;
            this.showData();
          },
          handleCurrentChange:function(val) {
            this.pageNum = val;
            this.showData();
          },
          // 附件上传文件列表变动
          enclosureFileSelectChange: function (file, fileList) {
            var ts = this;
            // ts.fileList = [];
            // debugger
            fileList.forEach(function (item) {
              if(item.raw) {
                ts.fileList.push(item.raw)
              };
              
            })
            var a = this.fileList;
          },
          // 附件上传文件列表移除某一项
          enclosureFileSelectRemove: function (file, fileList) {
            var ts = this;
            ts.fileList = [];
            fileList.forEach(function (item) {
              ts.fileList.push(item.raw)
            })
          },
          filterNodeOrg:function(value, data) {
              if (!value) {
                return true;
              }
              return data.label.indexOf(value) !== -1;
          },
          clearRegionKeyword:function(){
            this.regionKeyword='';
            this.regionTreeData();
          },
          clearOrgKeyword:function(){
            this.orgKeyword='';
            this.orgTreeData();
          },
          fold:function(){
            for(var i=0;i<this.$refs.regionTree.store._getAllNodes().length;i++){
              this.$refs.regionTree.store._getAllNodes()[i].expanded = false;
            }
          },
          unfold:function(tree){
            for(var i=0;i<this.$refs.regionTree.store._getAllNodes().length;i++){
              this.$refs.regionTree.store._getAllNodes()[i].expanded = true;
            }
          },
          fold2:function(){
            for(var i=0;i<this.$refs.orgTree.store._getAllNodes().length;i++){
              this.$refs.orgTree.store._getAllNodes()[i].expanded = false;
            }
          },
          unfold2:function(tree){
            for(var i=0;i<this.$refs.orgTree.store._getAllNodes().length;i++){
              this.$refs.orgTree.store._getAllNodes()[i].expanded = true;
            }
          },
          fold3:function(){
            for(var i=0;i<this.$refs.stageTree.store._getAllNodes().length;i++){
              this.$refs.stageTree.store._getAllNodes()[i].expanded = false;
            }
          },
          unfold3:function(tree){
            for(var i=0;i<this.$refs.stageTree.store._getAllNodes().length;i++){
              this.$refs.stageTree.store._getAllNodes()[i].expanded = true;
            }
          },
          // 获取组织人员列表
          getAllUser:function(){
            var _this=this;
            request('',{
                type:'get',
                url:ctx+'aea/service/window/listAllUserByOrgId.do',
                async: false
            },function (res) {
                _this.allUser = res[0].children;
                _this.storeAllUser = res[0].children;
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          // 树的展开收起
          usercontrol:function(){
              this.userShrink = !this.userShrink;
          },
          // 树的展开收起
          itemcontrol:function(){
              this.itemShrink = !this.itemShrink;
          },
          // 树的展开收起
          stagecontrol:function(){
              this.stageShrink = !this.stageShrink;
          },
          clearUser:function(){
            this.userKeyWord = '';
          },
          clearItem:function(){
            this.itemKeyWord = '';
          },
          clearStage:function(){
            this.stageKeyWord = '';
          },
          // 删除数组里指定的对象
          removeArray:function(_arr, _obj) {
            if(!_arr) return; 
            var length = _arr.length;
            if(length.length==0) return;
            for (var i = 0; i < length; i++) {
                if (_arr[i] === _obj) {
                  _arr.splice(i, 1);
                  return _arr;
                }
            }
          },
          // 代办中心人员删除/查询
          batchRemoveUser: function() {
            var _this=this;
            var ids = [];
            var arr = _this.userCheckedArr
            arr.map(function(item){
              ids.push(item.userId);
            })
            if(ids.length==0){
              _this.$message({
                message: '请先选择人员',
                type: 'error'
              });
              return
            }
            confirmMsg('','此操作将批量删除选择的代办中心人员,确定要删除吗？',function() {
              var allArr = _this.userCheckedData
              for (var i = 0; i < arr.length; i++) {
                  for (var j = 0; j < allArr.length; j++) {
                      if (allArr.indexOf(arr[i]) > -1) {
                          allArr = _this.removeArray(allArr, arr[i]);
                          arr = _this.removeArray(arr, arr[i]);
                          i--;
                      }
                  }
              }
                var idArr = ids.join(',');
                _this.batchDeleteWindowUser(idArr);
            })
          },
          deleteUser:function(item){
              var _this=this;
              confirmMsg('','此操作将删除选择的代办中心人员,确定要删除吗？',function(){
                  _this.userCheckedData = _this.removeArray(_this.userCheckedData, item);
                  _this.batchDeleteWindowUser(item.userId);
              })
          },
          serachUser:function(val){
            var arr = [];
            
            for (var i = 0; i < this.storeAllUser.length; i++) {
              if(this.storeAllUser[i].label.indexOf(val)!=-1){
                arr.push(this.storeAllUser[i]);
              }
            }
            if(arr[0].id!='root'){
              arr.unshift(this.storeAllUser[0]);
            }
            this.allUser = arr;
          },
          // 代办中心人员授权保存
          saveWindowUser:function(){
            var _this=this;
            var ids = [];
              _this.userCheckedData.map(function(item){
              ids.push(item.userId);
            })
            if(ids.length==0){
              _this.$message({
                message: '请先选择人员',
                type: 'error'
              });
              return
            }
            request('',{
                type:'post',
                url:ctx+'aea/service/window/saveAgencyUser.do',
                data:{
                  windowId : this.windowCurId,
                  userIds : ids.join(',')
                }
            },function (res) {
              _this.$message({
                message: '保存成功',
                type: 'success'
              });
                _this.showData();
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          batchDeleteWindowUser:function(idArr){
              var _this=this;
              request('',{
                  type:'post',
                  url:ctx+'aea/service/window/deleteAgencyUser.do',
                  data:{
                      windowId : this.windowCurId,
                      userIds : idArr
                  }
              },function (res) {
                  _this.$message({
                      message: '删除成功',
                      type: 'success'
                  });
                  _this.showData();
              },function (err) {
                  _this.$message.error('服务器错了哦!');
              })
          },
          tabclick:function(val){
            if(val.name=="second"){

              this.windowCurId = this.tableData2[this.chooseItem].windowId;

              if (this.tableData2.length) this.windowNameTitle = this.tableData2[this.chooseItem].windowName

              this.getUserByWindowId();
            }
          },
          // 根据代办中心id获取人员
          getUserByWindowId:function(){
            var _this=this;
            request('',{
                type:'post',
                url:ctx+'aea/service/window/listWindowUserByWindowId.do',
                data:{
                  windowId : this.windowCurId,
                  keyword:  this.userKeyWord
                },
                async: false
            },function (res) {
                _this.userCheckedData = [];
                for(var i=0; i<res.length; i++){
                   _this.userCheckedData.push(res[i]);
                }
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
      },
      watch: {
        regionKeyword: function(val) {
          this.$refs.regionTree.filter(val);
        },
        orgKeyword: function(val) {
          this.$refs.orgTree.filter(val);
        },
        layerUserKeyWord: function(val) {
          this.serachUser(val);
        }
      }
  })

