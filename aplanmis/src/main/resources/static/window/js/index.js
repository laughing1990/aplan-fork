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
        // var checkPhone = function (rule, value, callback) {
        //   var phoneReg = /^1[0-9]{10}$/
        //   if ((!value) || phoneReg.test(value)) {
        //     callback()
        //   } else {
        //     callback(new Error('电话号码格式不正确'))
        //   }
        // };
        return {
            dialogTitie: '新增服务窗口',
            dialogWinTitie: '新增人员',
            multipleSelection:[],
            multipleSelection2:[],
            multipleSelection3:[],
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
              windowType:'1'
            },
            isActive:'',
            windowType:'',
            regionProps:{
              label: 'label',
            },
            stageExpend:true,
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
                regionName: [
                    { required: true, message: '此项为必填'},
                ],
                orgName: [
                    { required: true, message: '此项为必填'},
                ],
                sortNo: [
                    { required: true, message: '此项为必填'},
                ],
                windowType: [
                    { required: true, message: '此项为必填'},
                ],
                isActive: [
                    { required: true, message: '此项为必填'},
                ],
                isAllItem: [
                    { required: true, message: '此项为必填'},
                ],
                isPublic: [
                    { required: true, message: '此项为必填'},
                ],
                isAllStage: [
                    { required: true, message: '此项为必填'},
                ],
                stageItemFilter: [
                    { required: true, message: '此项为必填'},
                ],
                regionRange: [
                    { required: true, message: '此项为必填'},
                ],
                linkPhone: [
                    // { validator: checkPhone,required: true, message: '电话号码格式不正确'},
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

            },
            isForm:false,
            allUser:[{name:'唐山市'}],
            storeAllUser:[],
            userChecked:[],
            userShrink:true,
            userKeyWord:'',
            allItem:[{name:'工程建设审批事项库'}],
            storeAllItem:[],
            itemChecked:[],
            itemShrink:true,
            itemKeyWord:'',
            allStage:[{stageName:'主题阶段'}],
            storeAllStage:[],
            stageChecked:[],
            tableStageChecked: [],
            stageShrink:true,
            stageKeyWord:'',
            windowCurId:'',
            stageData:[],
            delete:false,
            unOpenItem:[],
            unOpenStage:[],
            allOpenItem:[],
            allOpenStage:[],
            keys:[],
            stages:[],
            userCheckedArr: [],
            itemCheckedArr: [],
            stageCheckedArr: [],
            userCheckedData: [],
            itemCheckedData: [],
            tableStageCheckedData: [],
            layerUserKeyWord: '',
            layerItemKeyWord: '',
            layerStageKeyWord: '',
            itemNatureOption: '',
            itemNatureOptions: [
                {
                    label: '行政事项',
                    value: '0'
                },
                {
                    label: '服务协同事项',
                    value: '9'
                },
                {
                    label: '中介服务事项',
                    value: '8'
                },
                {
                    label: '公用服务事项',
                    value: '6'
                }
            ]
        }
      },
      directives: {clickoutside},
      created:function(){
          this.getAllUser();
          this.showData();
          this.regionTreeData();
          this.orgTreeData();
          
          this.getAllItem();
          this.getAllStage()
          // this.showAllData();
          // $(".loading").hide();
      },
      methods:{
          // 新增编辑窗口关闭回调
          closeWinCallback: function() {
            this.fileList = []
            this.addWindowDialog = false
          },
          // 新增xx窗口相关操作
          openWinDialog: function() {
            if (this.activeName === 'second') {
              this.userChecked = this.userCheckedData
              this.dialogWinTitie = '新增人员'
            } else if (this.activeName === 'third') {
              this.itemChecked = this.itemCheckedData
              this.dialogWinTitie = '新增事项'
            } else if (this.activeName === 'fourth') {
              this.tableStageChecked = this.tableStageCheckedData
              this.dialogWinTitie = '新增阶段'
            }

            this.addWinDialog = true  
          },
          // 新增xx窗口确定按钮操作
          confirmAdd: function() {
            if (this.activeName === 'second') {
              this.userCheckedData = this.userChecked
            } else if (this.activeName === 'third') {
              this.itemCheckedData = this.itemChecked
            } else if (this.activeName === 'fourth') {
              this.tableStageCheckedData = this.tableStageChecked
            }

            this.resetWinSearch();
          },
          // 清空新增xx窗口里相关搜索数据
          resetWinSearch: function() {
              this.addWinDialog = false;
              this.layerUserKeyWord='';
              this.layerItemKeyWord='';
              this.layerStageKeyWord='';
              this.itemNatureOption='';

              this.selectSerachItem();
          },
          addStage: function() {
            var _this = this
            var idArr = []
            _this.addWinDialog = true;
            _this.dialogWinTitie = '新增阶段'

            for (var i = 0; i < this.tableStageCheckedData.length; i++) {
              idArr.push(this.tableStageCheckedData[i].id)
            }

            _this.$nextTick(function(){
              _this.$refs.stageTree.setCheckedKeys(idArr);
            })
          },
          chooseWinlist: function(item, index) {
            this.chooseItem = index;
            this.windowNameTitle = item.windowName

            this.windowCurId = item.windowId;

            if (this.activeName === 'second') {
              this.getUserByWindowId();
            } else if (this.activeName === 'third') {
              this.getItemByWindowId();
            } else if (this.activeName === 'fourth') {
              this.getStageByWindowId();
            }             
          },
          // chooseItemlist: function(item, index) {
          //   this.chooseItem = index;
          //   this.windowNameTitle = item.windowName

          //   this.windowCurId = item.windowId;
          //   this.getItemByWindowId();
          // },
          // chooseStagelist: function(item, index) {
          //   this.chooseItem = index;
          //   this.windowNameTitle = item.windowName

          //   this.windowCurId = item.windowId;
          //   this.getStageByWindowId();
          // },
          closeOpLayer: function() {
            this.operationItem = -1;
          },
          // 请求table数据
          showData: function() {
              var vm = this;
              // vm.loading = true;
              request('',{
                  type:'post',
                  url:ctx+'aea/service/window/listAeaServiceWindow.do',
                  data:{
                    keyword: this.keyword
                  },
              },function (res) {
                  // vm.tableData = res.rows;
                  // vm.loading = false
                  // vm.total = res.total;

                  vm.tableData2 = res.content;

                  if (vm.tableData2.length) {
                    vm.windowNameTitle = vm.tableData2[0].windowName
                    vm.windowCurId = vm.tableData2[0].windowId;
                    vm.getUserByWindowId();
                  } 

                  if (vm.activeName === 'third') {
                    vm.allOpenItem = [];
                    vm.unOpenItem = [];

                    for(var i=0; i<res.content.length;i++){
                      if(res.content[i].isAllItem=='1'){
                        vm.allOpenItem.push(res.content[i]);
                      }else{
                        vm.unOpenItem.push(res.content[i]);
                      }
                    }
                  } else if (vm.activeName === 'fourth') {
                    vm.allOpenStage = [];
                    vm.unOpenStage = [];

                    for(var i=0; i<res.content.length;i++){
                      if(res.content[i].isAllStage=='1'){
                        vm.allOpenStage.push(res.content[i]);
                      }else{
                        vm.unOpenStage.push(res.content[i]);
                      }
                    }
                  }
              },function (err) {
                  vm.$message.error('服务器错了哦!');
              })
          },
          // 请求无分页所有窗口数据
          showAllData: function() {
              var vm = this;
              // vm.loading = true;
              request('',{
                  type:'get',
                  url:ctx+'aea/service/window/listAeaServiceWindow.do',
                  data:{},
              },function (res) {
                  vm.tableData2 = res.content;
                  vm.allOpenItem = [];
                  vm.unOpenItem = [];
                  vm.allOpenStage = [];
                  vm.unOpenStage = [];

                  if (vm.tableData2.length) vm.windowNameTitle = vm.tableData2[0].windowName

                  for(var i=0; i<res.content.length;i++){
                    if(res.content[i].isAllItem=='1'){
                      vm.allOpenItem.push(res.content[i]);
                    }else{
                      vm.unOpenItem.push(res.content[i]);
                    }
                  }
                  for(var i=0; i<res.content.length;i++){
                    if(res.content[i].isAllStage=='1'){
                      vm.allOpenStage.push(res.content[i]);
                    }else{
                      vm.unOpenStage.push(res.content[i]);
                    }
                  }
                  vm.loading = false
              },function (err) {
                  vm.$message.error('服务器错了哦!');
              })
          },
          selectable:function(row,index){
            if(row){
              return 0;
            }
          },
          // 请求地区树数据
          regionTreeData: function() {
              var vm = this;
              // vm.loading = true;
              request('',{
                  type:'get',
                  url:ctx+'aea/service/window/listRegionTreeByOrgId.do',
              },function (res) {
                  var arr = [];
                  arr.push(res);
                  vm.regionData = arr;
                  vm.loading = false
              },function (err) {
                  vm.$message.error('服务器错了哦!');
              })
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
          // 删除窗口
          delateWindow:function(id){
            var _this=this;
            confirmMsg('','此操作将删除选择的服务窗口,确定要删除吗？',function(){
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
                  _this.showAllData();
              },function (err) {
                  _this.$message.error('服务器错了哦!');
              })
            })
          },
          winTabelSelectionChange: function(val) {
            if (this.activeName === 'second') {
              this.userCheckedArr = val
            } else if (this.activeName === 'third') {
              this.itemCheckedArr = val
            } else if (this.activeName === 'fourth') {
              this.stageCheckedArr = val
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
          handleSelectionChange3:function(val){
            this.multipleSelection3 = val;
            if(this.multipleSelection3.length>1){
              this.multipleSelection3 = this.multipleSelection3[0];
              this.$refs.multipleTable2.toggleRowSelection(this.multipleSelection3);
            }
            if(!this.multipleSelection3[0].length==undefined) return;
            this.windowCurId = this.multipleSelection3[0].windowId;
            this.getItemByWindowId();

          },
          handleSelectionChange4:function(val){
            // if(this.multipleSelection4[0]==val[0]) return;
            this.multipleSelection4 = val;
            if(this.multipleSelection4.length>1){
              this.multipleSelection4 = this.multipleSelection4[0];
              this.$refs.multipleTable3.toggleRowSelection(this.multipleSelection4);
            }
            if(this.multipleSelection4[0]==undefined) return;
            this.windowCurId = this.multipleSelection4[0].windowId;
            this.getStageByWindowId();

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
            this.$set(this.formData,'isAllItem','1');
            this.$set(this.formData,'isPublic','1');
            this.$set(this.formData,'isAllStage','1');
            this.$set(this.formData,'stageItemFilter','0');
            this.$set(this.formData,'windowType','0');
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
          // 删除窗口
          delateAllWindow:function(){
            var _this=this;
            var ids = [];
            this.multipleSelection.map(function(item){
              ids.push(item.windowId);
            })
            confirmMsg('','此操作将删除选择的服务窗口,确定要删除吗？',function(){
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
                  _this.showAllData();
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
          itemClick:function(row, column, cell, event){
            if(this.multipleSelection3.length==1 && this.multipleSelection3[0]==row) return;
            this.$refs.multipleTable2.toggleRowSelection(row);

            this.windowCurId = row.windowId;
            this.getItemByWindowId();
          },
          stageClick:function(row, column, cell, event){
            if(this.multipleSelection4.length==1 && this.multipleSelection4[0]==row) return;
            this.$refs.multipleTable3.toggleRowSelection(row);

            this.windowCurId = row.windowId;
            this.getStageByWindowId();
          },
          // 点击编辑窗口
          editWindow:function(id){
            var _this=this;
            this.dialogTitie ='编辑服务窗口';
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
          // 新增和编辑保存窗口
          addAndSaveWindow:function(){
            var _this=this;
            this.dialogTitie ='新增服务窗口'
            this.forbidden=true;
            var param = {
              windowId:this.formData.windowId ? this.formData.windowId : '',
              isActive: this.formData.isActive,
              isAllItem: this.formData.isAllItem,
              isPublic: this.formData.isPublic,
              isAllStage: this.formData.isAllStage,
              stageItemFilter: this.formData.stageItemFilter,
              sortNo: this.formData.sortNo,
              windowType: this.formData.windowType,
              windowName: this.formData.windowName,
              regionName: this.formData.regionName,
              regionId: this.formData.regionId,
              regionRange: this.formData.regionRange,
              orgName: this.formData.orgName,
              orgId: this.formData.orgId,
              linkPhone: this.formData.linkPhone,
              workTime: this.formData.workTime,
              windowAddress: this.formData.windowAddress,
              trafficGuide: this.formData.trafficGuide,
              mapUrl: this.formData.mapUrl?this.formData.mapUrl:'',
              windowMemo: this.formData.windowMemo?this.formData.windowMemo:''
            };
            console.log(param)
            var formData = new FormData();
            for (var k in param) {
              formData.append(k, param[k])
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
                    _this.showAllData();
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
            console.log(fileList)
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
            console.log(fileList)
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
          // 获取事项列表
          getAllItem:function(){
            var _this=this;
            request('',{
                type:'post',
                url:ctx+'aea/item/priv/getTreeByAeaItemBasicList.do?isCatalog=0',
            },function (res) {
                _this.allItem = res;
                _this.storeAllItem = res;
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          // 获取阶段列表
          getAllStage:function(){
            var _this=this;
            request('',{
                type:'get',
                url:ctx+'aea/service/window/listAllStageTree',
            },function (res) {
              for(var i=0; i<res.content.length; i++){
                res.content[i].regionRange='0';
                if(res.content[i].children&&res.content[i].children.length>0){
                  for(var j=0; j<res.content[i].children.length; j++){
                    res.content[i].children[j].regionRange='1';
                  }
                }
              }
              _this.stageData = res.content;
              _this.storeAllStage =res.content;
              $('.loading').hide();
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
          // 窗口人员删除/查询
          batchRemoveUser: function() {
            var _this=this;
            var ids = [];
            var arr = this.userCheckedArr
            arr.map(function(item){
              ids.push(item.id);
            })
            if(ids.length==0){
              _this.$message({
                message: '请先选择人员',
                type: 'error'
              });
              return
            }         

            var allArr = this.userCheckedData  

            for (var i = 0; i < arr.length; i++) {
              for (var j = 0; j < allArr.length; j++) {
                if (allArr.indexOf(arr[i]) > -1) {
                  allArr = this.removeArray(allArr, arr[i]);
                  arr = this.removeArray(arr, arr[i]);
                  i--;
                }
              }
            }
          },
          deleteUser:function(item){
            this.userCheckedData = this.removeArray(this.userCheckedData, item);
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
          // 窗口事项删除/查询
          batchRemoveItem: function() {
            var _this=this;
            var ids = [];
            var arr = this.itemCheckedArr
            arr.map(function(item){
              ids.push(item.id);
            })
            if(ids.length==0){
              _this.$message({
                message: '请先选择事项',
                type: 'error'
              });
              return
            }         

            var allArr = this.itemCheckedData  

            for (var i = 0; i < arr.length; i++) {
              for (var j = 0; j < allArr.length; j++) {
                if (allArr.indexOf(arr[i]) > -1) {
                  allArr = this.removeArray(allArr, arr[i]);
                  arr = this.removeArray(arr, arr[i]);
                  i--;
                }
              }
            }  
          },
          deleteItem:function(item){
            this.itemCheckedData = this.removeArray(this.itemCheckedData, item);
          },
          serachItem:function(val){
            var arr = [];

            if (['0','6','8','9'].indexOf(this.itemNatureOption) > -1) {
                for (var i = 0; i < this.storeAllItem.length; i++) {
                    if(this.storeAllItem[i].name.indexOf(val)!=-1 && this.storeAllItem[i].itemNature == this.itemNatureOption){
                        arr.push(this.storeAllItem[i]);
                    }
                }
            } else {
                for (var i = 0; i < this.storeAllItem.length; i++) {
                    if(this.storeAllItem[i].name.indexOf(val)!=-1){
                        arr.push(this.storeAllItem[i]);
                    }
                }
            }
            // for (var i = 0; i < this.storeAllItem.length; i++) {
            //   if(this.storeAllItem[i].name.indexOf(val)!=-1 && this.storeAllItem[i].itemNature == this.itemNatureOption){
            //     arr.push(this.storeAllItem[i]);
            //   }
            // }
            if(arr.length && arr[0].id!='root'){
              arr.unshift(this.storeAllItem[0]);
            }
            this.allItem= arr;
          },
          selectSerachItem: function()  {
              var arr = [];
              var val = this.layerItemKeyWord

              if (['0','6','8','9'].indexOf(this.itemNatureOption) > -1) {
                  for (var i = 0; i < this.storeAllItem.length; i++) {
                      if(this.storeAllItem[i].name.indexOf(val)!=-1 && this.storeAllItem[i].itemNature == this.itemNatureOption){
                          arr.push(this.storeAllItem[i]);
                      }
                  }
              } else {
                  for (var i = 0; i < this.storeAllItem.length; i++) {
                      if(this.storeAllItem[i].name.indexOf(val)!=-1){
                          arr.push(this.storeAllItem[i]);
                      }
                  }
              }

              if(arr.length && arr[0].id!='root'){
                  arr.unshift(this.storeAllItem[0]);
              }
              this.allItem= arr;
          },
          // 窗口阶段删除/查询
          batchRemoveStage: function() {
            var _this=this;
            var ids = [];
            var arr = this.stageCheckedArr
            arr.map(function(item){
              ids.push(item.id);
            })
            if(ids.length==0){
              _this.$message({
                message: '请先选择阶段',
                type: 'error'
              });
              return
            }     

            var allArr = this.tableStageCheckedData  

            for (var i = 0; i < arr.length; i++) {
              for (var j = 0; j < allArr.length; j++) {
                if (allArr.indexOf(arr[i]) > -1) {
                  allArr = this.removeArray(allArr, arr[i]);
                  arr = this.removeArray(arr, arr[i]);
                  i--;
                }
              }
            }        

            this.delete = true;
          },
          deleteStage:function(item){
            // this.$refs.stageTree.setChecked(item,false,false);
            this.tableStageCheckedData = this.removeArray(this.tableStageCheckedData, item);
            this.delete = true;
          },
          serachStage:function(val){
            var arr = [];
            
            for (var i = 0; i < this.storeAllStage.length; i++) {
              if(this.storeAllStage[i].name.indexOf(val)!=-1){
                arr.push(this.storeAllStage[i]);
              }
            }
            if(arr[0].id!='root'){
              arr.unshift(this.storeAllStage[0]);
            }
            this.allStage= arr;
          },
          // 窗口人员授权保存
          saveWindowUser:function(){
            var _this=this;
            var ids = [];
            this.userCheckedData.map(function(item){
              ids.push(item.id);
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
                url:ctx+'aea/service/window/saveAeaServiceWindowUser.do',
                data:{
                  windowId : this.windowCurId,
                  userIds : ids.join(',')
                }
            },function (res) {
              _this.$message({
                message: '窗口人员授权保存成功',
                type: 'success'
              });
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          // 窗口事项保存
          saveWindowItem:function(){
            var _this=this;
            var ids = [];
            this.itemCheckedData.map(function(item){
              ids.push(item.itemVerId);
            })
            if(ids.length==0){
              _this.$message({
                message: '请先选择事项',
                type: 'error'
              });
              return
            }
            request('',{
                type:'post',
                url:ctx+'aea/service/window/saveAeaServiceWindowItem.do',
                data:{
                  windowId : this.windowCurId,
                  itemVerIds : ids.join(',')
                }
            },function (res) {
              _this.$message({
                message: '窗口事项保存成功',
                type: 'success'
              });
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          // 窗口阶段保存
          saveWindowStage:function(){
            var _this=this;
            var stage = [];
            if(this.tableStageCheckedData.length==0){
              _this.$message({
                message: '请先选择阶段',
                type: 'error'
              });
              return
            }
            this.tableStageCheckedData.map(function(item){
              var obj = {
                 regionRange:item.regionRange,
                 stageId:item.id
              }
              stage.push(obj);
            })
            request('',{
                type:'post',
                url:ctx+'aea/service/window/saveWindowStage',
                ContentType:'application/json',
                data:JSON.stringify({
                  windowId : this.windowCurId,
                  aeaServiceWindowStageList : stage
                })
            },function (res) {
              _this.$message({
                message: '窗口阶段保存成功',
                type: 'success'
              });
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          tabclick:function(val){
            // var arr = [];
            if(val.name=="second"){
              // arr[0]= this.tableData2[0];
              this.windowCurId = this.tableData2[this.chooseItem].windowId;

              if (this.tableData2.length) this.windowNameTitle = this.tableData2[this.chooseItem].windowName

              this.getUserByWindowId();
              // this.$refs.multipleTable.toggleRowSelection(arr[0],true);
            }else if(val.name=="third"){
              // arr[0]= this.unOpenItem[0];
              this.windowCurId = this.tableData2[this.chooseItem].windowId;

              if (this.tableData2.length) this.windowNameTitle = this.tableData2[this.chooseItem].windowName

              this.getItemByWindowId();
              // this.$refs.multipleTable2.toggleRowSelection(arr[0],true);
            }else if(val.name=="fourth"){
              // arr[0]= this.unOpenStage[0];
              this.windowCurId = this.tableData2[this.chooseItem].windowId;

              if (this.tableData2.length) this.windowNameTitle = this.tableData2[this.chooseItem].windowName

              this.getStageByWindowId();
              // this.$refs.multipleTable3.toggleRowSelection(arr[0],true);
            }

          },
          // 根据窗口id获取人员
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
                for(var j=0; j<_this.allUser.length; j++){
                  if(res[i].userId == _this.allUser[j].id){
                    _this.userCheckedData.push(_this.allUser[j]);
                    break;
                  }
                }
              }
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          // 根据窗口id获取事项
          getItemByWindowId:function(){
            var _this=this;
            request('',{
                type:'post',
                url:ctx+'aea/service/window/listWindowItemByWindowId.do',
                data:{
                  windowId : this.windowCurId,
                  keyword:  this.itemKeyWord
                }
            },function (res) {
              _this.itemCheckedData = [];
              for(var i=0; i<res.length; i++){
                for(var j=0; j<_this.allItem.length; j++){
                  if(res[i].itemVerId == _this.allItem[j].itemVerId){
                    _this.itemCheckedData.push(_this.allItem[j]);
                    break;
                  }
                }
              }
            },function (err) {
                _this.$message.error('服务器错了哦!');
            })
          },
          // 根据窗口id获取阶段
          getStageByWindowId:function(){
            var _this=this;
            request('',{
                type:'get',
                url:ctx+'aea/service/window/findAeaServiceWindowStage',
                data:{
                  windowId : this.windowCurId,
                  keyword:  this.stageKeyWord
                }
            },function (res) {
              // _this.stageChecked = [];
              // var arr = [];
              // res.content.map(function(item){
              //   arr.push(item.stageId);
              // })
              for(var i=0; i<_this.stageData.length; i++){
                for(var j=0; j<_this.stageData[i].children.length; j++){
                  for(var m=0; m<res.content.length; m++){
                    if(res.content[m].stageId==_this.stageData[i].children[j].id){
                      _this.stageData[i].children[j].regionRange = res.content[m].regionRange;
                      continue;
                    }
                  }
                }
              }

              // _this.stageChecked = arr
              _this.tableStageCheckedData = res.content
              _this.tableStageCheckedData.map(function(item, index){
                item.id = res.content[index].stageId;
              })
              // _this.$nextTick(function(){
              //   _this.$refs.stageTree.setCheckedKeys(arr);
              // })
                
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
        },
        layerItemKeyWord: function(val) {
          this.serachItem(val);
        },
        layerStageKeyWord: function(val) {
          this.$refs.stageTree.filter(val);
        }
      }
  })

