var vm = new Vue({
    el: '#searchTable',
    mixins: [mixins],
    data: function () {
        return {
            //查询数据
            searchFrom: {
                pagination:{
                    page:  1,
                    pages: 1,
                    perpage: 10
                },
                sort:{
                    field: 'modifyTime',
                    sort: 'desc'
                },
                keyword:'',
                //立项类型
                projType:'',
                //行政分区
                regionalism:'',
                //建设性质
                projNature:'',
                //项目级别
                projLevel:'',
                //最小建筑面积
                minBuildAreaSum:'',
                //最大建筑面积
                maxBuildAreaSum:'',
                //修改时间-开始
                modifyStartTime:'',
                //修改时间-结束
                modifyEndTime:'',
                onlyRegion:onlyRegion,
                onlyOrg:onlyOrg,
                handler:handler

            },
            // 是否为本地
            isLocal: false,

            // 页面所有待选项
            allDicItemListData: {
                XM_NATURE:[],
                XM_PROJECT_STEP:[],
                XM_PROJECT_LEVEL:[]
            },

            baseRules: {
                minBuildAreaSum: [{
                    required: false,
                    validator: checkNumber,
                    trigger: 'blur'
                }],
                maxBuildAreaSum: [{
                    required: false,
                    validator: checkNumber,
                    trigger: 'blur'
                }]
            },
            showUploadFlag:false,
            //列表选中
            projSelectList:[],

            // 设置代办标志dialog相关
            // 是否展示设置代办标志dialog
            isShowSetAgencyTagDialog: false,
            // 所有已有的代办中心iotions
            agencyOptionList: [],
            // 代办标志设置表单
            setAgencyForm: {
              windowId: '',   //代办中心id
            },
            setAgencyRules: {
              windowId: [
                { required: true, message: '此项为必选', trigger: 'change'},
              ],
            },
        }
    },
    components:{
        'v-sync-cascader':SyncCascader
    },
    methods: {
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;


            if(this.$refs.regionalismSyncCascader) {
                this.searchFrom.regionalism = this.$refs.regionalismSyncCascader.getSelectecValue();
            }

            ts.loading = true;

            request('', {
                url: ctx + 'aea/proj/info/conditionalQueryAeaProjInfo',
                type: 'get',
                data: ts.searchFrom
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.tableData = res.content.list;
                    ts.dataTotal = res.content.total;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        // 跳转到详情页面
        jumpToDetail: function (id) {
            var _projInfoId = id;
            var parentIfreamUrl = window.frames.location.href;
            var _url = "",_tabName="";
            if (_projInfoId === 'add'){
                _tabName = "新增项目";
                _url = ctx + 'rest/project/detail?parentIfreamUrl='+parentIfreamUrl;
            }else{
                _tabName = "编辑项目"
                _url = ctx + 'rest/project/detail?projInfoId=' + _projInfoId +'&parentIfreamUrl='+parentIfreamUrl;
            }
            var _jumpData = {
                'menuName': _tabName,
                'menuInnerUrl': _url,
                'id': _projInfoId + '_pjdetail'
            };
            parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
        },
        projPanorama:function(projInfoId){
            var url = ctx + 'rest/project/diagram/status/projInfo/?projInfoId='+projInfoId;
            var tempwindow = window.open();
            setTimeout(function () {
                tempwindow.location = url;
            }, 200);
        },
        dateFormatCn:function(row){
            if(row.modifyTime) {
                return formatDate(new Date(row.modifyTime), 'yyyy-MM-dd hh:mm');
            }else{
                return "-";
            }
        },
        //获取查询条件的数据
        conditionalQueryDic: function () {
            var ts = this;
            request('', {
                url: ctx + 'aea/proj/info/getConditionalQueryDic',
                type: 'get',
                data: {}
            }, function (res) {
                if (res.success) {
                    ts.allDicItemListData = res.content;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        load: function(row, treeNode, resolve) {
            var ts = this;
            // ts.loading = true;
            request('', {
                url: ctx + 'aea/proj/info/listChildProjInfoByProjInfoId',
                type: 'get',
                data: {projInfoId:row.projInfoId}
            }, function (res) {
                // ts.loading = false;
                if (res.success) {
                    resolve(res.content);
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                // ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        //列表选中
        handleSelectionChange: function (list) {
            this.projSelectList = list;
        },
        // 批量删除
        batchDel: function () {
            var ts = this,
                _idList = [],
                _delData = {};
            if(!ts.projSelectList.length) return this.apiMessage('您尚未选中项目，请选择');
            confirmMsg('', '您确定删除选中项目吗？', function () {
                for (var i = 0; i < ts.projSelectList.length; i++) {
                    _idList.push(ts.projSelectList[i].projInfoId);
                }
                _delData.projInfoIds = _idList.join(',');
                ts.loading = true;
                request('', {
                    url: ctx + 'aea/proj/info/batchDeleteProjInfo',
                    type: 'get',
                    data: _delData
                }, function (res) {
                    ts.loading = false;
                    if (res.success) {
                        ts.apiMessage('删除成功！', 'success');
                        ts.fetchTableData();
                    } else {
                        ts.apiMessage(res.message, 'error')
                    }
                }, function (msg) {
                    ts.loading = false;
                    ts.apiMessage('删除失败！', 'error');
                });
            });
        },
        //打开导入窗口
        openImportWin:function(){
            this.showUploadFlag = true;
        },
        //导入代办项目
        onBeforeUpload:function(file){
            var ts = this;
            const isOffice = file.type.indexOf('application/vnd') > -1;
            const isLt10M = file.size / 1024 / 1024 < 10;
            if(!isOffice){
                ts.apiMessage('请导入EXCEL文件！','warning');
                return false;
            }
            if(!isLt10M){
                ts.apiMessage('上传文件大小不能超过10MB！','warning');
                return false;
            }
            var fileData = new FormData();
            fileData.append('file',file);
            axios.post(ctx + '/aea/proj/info/importAgentPorjFile', fileData).then(function (res) {
                var result = res.data;
                ts.showUploadFlag = false;
                if(result.success){
                    ts.apiMessage('导入成功！','success');
                    if("failImportData" == result.content){
                        ts.getFailImportData();
                    }
                }else{
                    ts.apiMessage(result.message,'error');
                }
            }).catch(function (error) {
                ts.showUploadFlag = false;
                console.log(error);
            });
            return false;
        },
        getFailImportData:function () {
            window.location.href = ctx + '/aea/proj/info/getFailImportData';
            // window.open(ctx + '/aea/proj/info/getFailImportData')
        },
        //下载模板
        downloadImportTemplate:function () {
            window.location.href = ctx + '/aea/proj/info/downloadImportTemplate';
        },
        // 显示代办提示信息
        showAgentProjInfo:function (row) {
            var html = '';
            if (row.localCode) {
                html = html + "项目代码 : " + row.localCode + "</br></br>";
            }
            if(row.gcbm) {
                html = html + "工程编码 : " + row.gcbm + "</br></br>";
            }
            if(row.projName) {
                html = html + "项目/工程名称 : " + row.projName;
            }
            if(row.ifAgentProj) {
                html = html  + "</br></br>" + "代办中心名称 : " + row.agentName;
            }
            return html;
          },
        // 设置代办标志
        setAgencyTag: function(){
          // console.log(this.projSelectList)
          if(!this.projSelectList.length) return this.$message.warning('请选择项目')
          this.fetchAgencyList();
        },
        // 获取代办中心下拉列表数据
        fetchAgencyList: function () {
          var ts = this;
          ts.loading = true;
          request('', {
            url: ctx + 'aea/service/window/listAgencyWin.do',
            type: 'get',
            data:{
              windowType: 'd',
            }
          }, function (res) {
            ts.loading = false;
            if (res.success) {
              ts.agencyOptionList = res.content;
              ts.isShowSetAgencyTagDialog = true;
            } else {
              return ts.apiMessage(res.message, 'error')
            }
          }, function () {
            ts.loading = false;
            return ts.apiMessage('网络错误！', 'error')
          });
        },
        // 保存设置成代办标识的项目
        saveSetAgencyTagProjList: function () {
          var ts = this;
          ts.$refs['setAgencyForm'].validate(function (valid) {
            if (valid) {
              var saveData = {},
                projInfoIds = [];
              ts.projSelectList.forEach(function (item) {
                projInfoIds.push(item.projInfoId);
              })

              saveData = {
                windowId: ts.setAgencyForm.windowId,
                projInfoIds: projInfoIds.join(',')
              };
              // console.log(saveData)
              // return
              ts.loading = true;
              request('', {
                url: ctx + 'aea/proj/info/saveProjWinRelation',
                type: 'post',
                data: saveData
              }, function (res) {
                ts.loading = false;
                if (res.success) {
                  ts.apiMessage('设置成功！', 'success');
                  ts.isShowSetAgencyTagDialog = false;
                } else {
                  return ts.apiMessage(res.message, 'error')
                }
              }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
              });
            } else {
              ts.apiMessage('请选择代办中心！', 'warning')
              return false;
            }
          });
        },
        // 表格缺省填充
        formatCell: function(row, column, cellValue, index){
          if(!!cellValue)return cellValue;
          return '-';
        },
    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
        if (document.location.protocol == "file:") {
            this.isLocal = true;
        }
    }
});