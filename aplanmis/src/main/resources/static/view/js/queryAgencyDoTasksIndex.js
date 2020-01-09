var vm = new Vue({
    el: '#searchTable',
    mixins: [mixins],
    data: function () {
        return {
            //查询数据
            searchFrom: {
                pagination: {
                    page: 1,
                    pages: 1,
                    perpage: 10
                },
                sort: {
                    field: 'startTime',
                    sort: 'desc'
                },
                theme: '',
                agentApplyState: '',
                minStartTime: '',
                maxStartTime: '',
                viewDataCtrl:viewDataCtrl,
                keyword: ''
            },
            statgeNames : ['-','立项用地规划许可阶段','工程建设许可阶段','施工许可阶段','竣工验收阶段'],
            isShowMsgDetail: false,
            msgDetail: {
                remindContent: '',
                sendUserName: '',
                sendDate: ''
            }

        }
    },
    methods: {
        //刷新列表
        fetchTableData: function () {

            var ts = this;
            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;
            ts.loading = true;

            request('', {
                url: ctx + 'rest/conditional/query/listAgencyDoTasks',
                type: 'post',
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
        //办理
        viewDetail: function (row) {
            var menuName= '';
            var menuInnerUrl =  '';
            var id = 'menu_'+new Date().getTime();
            menuName = row.projName;
            menuInnerUrl = ctx + '/apanmis/page/stageApplyIndex?applyAgentId=' + row.applyAgentId;
            menuInnerUrl += '&isApplyAgent=true';
            menuInnerUrl += '&projInfoId='+row.projInfoId;
            var data = {
                'menuName':menuName,
                'menuInnerUrl':menuInnerUrl,
                'id':id,
            };
            try{
                parent.vm.addTab('',data,'','');
            }catch (e) {
                window.open(menuInnerUrl,'_blank');
            }
        },

        //格式化代办状态
        formatAgentApplyState: function (row, column, cellValue, index) {
            if (!cellValue || cellValue == null) {
                return '-'
            }
            var stateNames = ['-','待签订','签订中','待申请人签章','代办中','代办终止','不予受理','代办结束'];
            return stateNames[cellValue];
        },
        // 显示委托代办阶段提示信息
        showStageInfo:function (row) {
            var html = '';
            if(row.agentStageState) {
                var stages = row.agentStageState.split(',');
                var name = '';
                for(var i in stages){
                    name += this.statgeNames[stages[i]] + '，';
                }
                name = name.substring(0,name.length-1);
                html = html + "委托代办阶段 : " + name + "</br></br>";
            }
            return html;
        },
        agentStageStateFormatter : function (row) {
            if(row.agentStageState){
                var stages = row.agentStageState.split(',');
                var showName = '';
                var html = "<div class=\"flow_steps\">" +
                    "<ul class='stage'>";
                for(var i=1;i<=4;i++){
                    var clas = '';
                    var content = '&nbsp;'
                    for(var j=0;j<stages.length;j++){
                        var stage = stages[j];
                        if(stage == i){
                            if(showName == ''){
                                showName = this.statgeNames[i];
                                content = showName;
                            }
                            clas = 'current current-stage-min-width'
                        }
                    }
                    html += "<li class="+ clas +">"+ content +"</li>";
                }
                html += "</ul>" +
                    "</div>";
                return html;
            }
            return "-";
        },
        //签订协议按钮
        showOperateBtn1: function (row) {
            return ('1' === row.agentApplyState);
        },
        //查看按钮
        showOperateBtn2: function (row) {
            return ('2' === row.agentApplyState || '3' === row.agentApplyState || '5' === row.agentApplyState);
        },
        //办理按钮
        showOperateBtn3: function (row) {
            return ('4' === row.agentApplyState);
        },
        //代办状态
        queryAgencyState: function () {
            var ts = this;
            request('', {
                url: ctx + 'rest/conditional/query/agencyState/list',
                type: 'get',
                data: {}
            }, function (res) {
                if (res.success) {
                    ts.agentApplyStateMap = res.content;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });
        },

        // 简单加密
        compile:function(code) {
          var c = String.fromCharCode(code.charCodeAt(0) + code.length);
          for (var i = 1; i < code.length; i++) {
            c += String.fromCharCode(code.charCodeAt(i) + code.charCodeAt(i - 1));
          }
          return escape(c);
        },

        // 跳转代办详情 签订与查看
        toAgencyDetail: function(row, type){
          var _url = ctx + 'aea/proj/apply/agent/agencyProjDetail?applyAgentId=' + row.applyAgentId + '&type=';
          type == 'view' ? _url += this.compile('view'): _url += this.compile('do');
          var tabsData = {
            menuName: '代办详情',
            id: 'agency_detail' + row.applyAgentId,
            menuInnerUrl: _url,
          }
          try {
            //获取父window vue对象
            var parentVue = window.parent.vm
            parentVue.addTab('', tabsData, 'MENU_', parentVue.leftMenuActive++);
          } catch (err) {
            top.postMessage(tabsData, '*')
          }
        },
    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
        this.queryAgencyState();
    }
});