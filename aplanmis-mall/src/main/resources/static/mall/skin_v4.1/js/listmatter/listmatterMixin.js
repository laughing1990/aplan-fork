var listmatterMixin = {
    data:{
        isfromLeadPageflage:false,
    },
    mounted:function(){
        this.isfromLeadPage();
    },
    methods: {
        // 判断是否是在智能引导页跳转过来的
        isfromLeadPage:function(){
            var stateList = JSON.parse(sessionStorage.getItem('stateList'));
            if(stateList){
                this.isfromLeadPageflage = true;
                this.getAIleadData(stateList);
            }else{
                this.isfromLeadPageflage = false;
            }
        },
        gotoGuideIndex:function () {
            window.location.hash='/DeclarGuidePage';
            window.location.search='';
        },
        // 跳转智能引导页
        openAILeadDia:function(){
            this.AIleadDialogFlag = true;
        },
        // 获取url参数
        GetRequest: function () {
            var url = location.search; //获取url中"?"符后的字串
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
                }
            }
            this.stageId = theRequest.stageId? theRequest.stageId : null;
            this.getItemList(this.stageId);
            return theRequest;
        },
        // 获取文件后缀
        getFileType: function(fileName){
            var index1=fileName.lastIndexOf(".");
            var index2=fileName.length;
            var suffix=fileName.substring(index1+1, index2).toLowerCase();//后缀名
            return suffix;
        },
        fetchStateList:function(stateList){
            var fetchStateListData = [];
            if (stateList.length < 1) return  fetchStateListData;
            stateList.forEach(function (item) {
                if(item.selectAnswerId){
                    var itemSelectAnswerId = item.selectAnswerId
                    if(typeof itemSelectAnswerId == 'object' && itemSelectAnswerId.constructor == Array){
                        for (var i = 0; i < itemSelectAnswerId.length; i++) {
                            fetchStateListData.push(itemSelectAnswerId[i]);
                        }
                    }else if(typeof itemSelectAnswerId == 'string' && itemSelectAnswerId.constructor == String){
                        fetchStateListData.push(itemSelectAnswerId)
                    }
                }
            });
            return fetchStateListData;
        },
        compareAutoSelect:function(coreItemList,parallelItemList){
            var _this = this;
            _this.$nextTick(function(){
                for (var i = 0; i < _this.coreItemList.length; i++) {
                    var isflag ;
                    for (var j = 0; j < coreItemList.length; j++) {
                        if(coreItemList[j].itemVerId == _this.coreItemList[i].itemVerId){
                            isflag = true;
                            console.log(_this.coreItemList[i])
                        }
                    }
                    isflag  &&  _this.$refs.coreItemListTable.toggleRowSelection(_this.coreItemList[i],true);
                }

                for (var i = 0; i < _this.parallelItemList.length; i++) {
                    var isflag ;
                    for (var j = 0; j < parallelItemList.length; j++) {
                        if(parallelItemList[j].itemVerId == _this.parallelItemList[i].itemVerId){
                            isflag = true;
                        }
                    }
                    isflag  &&   _this.$refs.parallelItematable.toggleRowSelection(_this.parallelItemList[i],true);

                }
            })
        },
    }
}
Vue.component('list',{
    template:"#recursionList",
    props: {
        list: Array
    },
    methods:{
        // 事项父情形下获取事项子情形
        getMatterChildsStateList:function(cStateList,pData,answerData,index,type,value){
            var vm = this;
            var answerId = answerData.itemStateId ? answerData.itemStateId : 'ROOT';
            request('', {
                url: ctx +  'rest/guide/itemState/findByParentItemStateId/'+ answerId,
                type: 'get',
            }, function (res) {
                if (res.success) {
                    var content = res.content || [];
                    vm.matterChildsSateListDataHandle(content,cStateList,index,type,value);
                } else {
                    vm.$message.error(res.message);
                }

            }, function () {
                vm.$message.error('获取子情形列表接口失败，请稍后重试！');
            });
        },
        matterChildsSateListDataHandle:function(content,cStateList,index,type,value){
            var vm = this;
            content.map(function (item) {                       // 对多选checkbox绑定的值selectAnswerId类型重置为数组，防止点击出现全部选中
                if(item.answerType !='s'||item.answerType!='y'){
                    item.selectAnswerId = [];
                }
            });
            if(!cStateList[index].children){
                vm.$set(cStateList[index],'children',[]);
            }
            if(type === 'radio'){
                cStateList[index].children = content;
            }else{
                if(value){ // 为真checkbox勾选
                    cStateList[index].children = cStateList[index].children.concat(content);
                }else{
                    if(content.length > 0){
                        var cStateList_C = JSON.parse(JSON.stringify(cStateList[index].children));
                        var indexArry = [];
                        for (var i = 0; i < cStateList_C.length; i++) {
                            for (var j = 0; j < content.length; j++) {
                                if(content[j].itemStateId != cStateList_C[i].itemStateId){
                                    indexArry.push(i);
                                }
                            }
                        }
                        setTimeout(function () {
                            for (var i = 0; i <indexArry.length ; i++) {
                                cStateList[index].children.splice(indexArry[i],1);
                            }
                        },0)
                    }
                }
            }
            console.log(cStateList);
            console.log(cStateList);

            // 更新材料一单清数据
             var selectAnswerId = cStateList[index].selectAnswerId;
            if(typeof  selectAnswerId == 'object'&& selectAnswerId.constructor == Array){
                selectAnswerId.forEach(function (ele) {
                    listmatter.vm.itemStateIds.push(ele);
                })
            }else{
                if($.inArray(selectAnswerId,listmatter.vm.itemStateIds)){
                    listmatter.vm.itemStateIds.push(selectAnswerId);
                }else{
                    listmatter.vm.itemStateIds.push(selectAnswerId);
                }
            }
            listmatter.vm.getMateriallist();
        },

    }
});
