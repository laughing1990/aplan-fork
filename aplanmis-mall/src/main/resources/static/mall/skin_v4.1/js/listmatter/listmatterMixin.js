var listmatterMixin = {
    methods: {
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
            for (var i = 0; i < vm.coreItemList.length; i++) {
                var isflag ;
                for (var j = 0; j < coreItemList.length; j++) {
                    if(coreItemList[j].itemVerId == vm.coreItemList[i].itemVerId){
                        isflag = true;
                    }
                }
                vm.$nextTick(function(){
                    debugger
                    console.log(vm.coreItemList[i])
                    isflag  &&  vm.$refs.coreItemListTable.toggleRowSelection(vm.coreItemList[i].true);
                })
            }

            for (var i = 0; i < vm.parallelItemList.length; i++) {
                var isflag ;
                for (var j = 0; j < parallelItemList.length; j++) {
                    if(parallelItemList[j].itemVerId == vm.parallelItemList[i].itemVerId){
                        isflag = true;
                    }
                }
                vm.$nextTick(function(){
                    debugger
                    console.log(vm.parallelItemList[i])
                    isflag  &&   vm.$refs.parallelItematable.toggleRowSelection(vm.parallelItemList[i],true);
                })
            }
        },
    }
}