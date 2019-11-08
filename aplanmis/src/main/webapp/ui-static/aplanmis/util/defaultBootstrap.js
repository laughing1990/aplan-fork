function defaultBootstrap(bstableId, columns, url) {
    var BSTable = function (bstableId,columns,url) {
        this.btInstance = null;//jquery和BootStrapTable绑定的对象
        this.bstableId = bstableId;//table的id
        this.url = url;
        this.method = "get";//get 或是 post
        this.paginationType = "server";//默认分页方式是服务器分页,可选项"client"
        // this.toolbarId = bstableId + "Toolbar";
        this.columns = columns;
        // this.height = 665;//默认表格高度665
        // this.data = {};
        this.queryParams = {}; // 向后台传递的自定义参数
        this.pageSize = 10;//每页的行数
        this.search = false;//是否显示搜索框
        this.maintainSelected = false;//在paginationType为'client'的情况下，是否在切页面和搜索时记住选项
        this.pagination = true;//是否分页
        this.clearBtn = "";//清空按钮的id
        this.searchBtn = "";//查询按钮的id
        this.keyword = "";//关键字输入框的id
        this.overAllArr = new Array();//全局选中的id数组
        this.overAllIds = new Array();//全局选中的row数组
        this.rowId = null;//row的唯一标识id
    };


    BSTable.prototype = {
        /**
         * 初始化bootstrap table
         */
        init: function () {
            var tableId = this.bstableId;
            this.btInstance =
                $('#' + tableId).bootstrapTable({
                    // contentType: "application/x-www-form-urlencoded",
                    url: this.url,              //请求地址
                    method: this.method,        //ajax方式,post还是get
                    // ajaxOptions: {              //ajax请求的附带参数
                    //     data: this.data
                    // },
                    // toolbar: "#" + this.toolbarId,//顶部工具条
                    // striped: true,              //是否显示行间隔色
                    // cache: false,               //是否使用缓存,默认为true
                    sortable: true,             //是否启用排序
                    // sortOrder: "desc",          //排序方式
                    pageNumber: 1,                  //初始化加载第一页，默认第一页
                    pageSize: this.pageSize,               //每页的记录行数
                    pageList: [10, 20, 50, 100],    //可供选择的每页的行数
                    // queryParamsType: '',   //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
                    queryParams: function (params) {
                        // return $.extend(this.queryParams, params);
                        var queryParams = {};
                        if(currentBSTable.pagination){
                            // var pageNum = (params.offset / params.limit) + 1;
                            // queryParams['pageNum'] = pageNum;
                            // queryParams['pageSize'] = params.limit;
                            var pageNum = (params.offset / params.limit) + 1;
                            var pagination = {
                                page: pageNum,
                                pages: pageNum,
                                perpage: params.limit
                            };
                            queryParams["pagination"] = pagination;
                        }

                        var sort = {
                            field: params.sort,
                            sort: params.order
                        };
                        queryParams ["sort"] = sort;

                        if(currentBSTable.keyword!=''){
                            queryParams['keyword'] = $('#'+currentBSTable.keyword).val();
                        }

                        return $.extend(queryParams, currentBSTable.queryParams);

                    },
                    sidePagination: this.paginationType,   //分页方式：client客户端分页，server服务端分页（*）
                    search: this.search,              //是否显示表格搜索，此搜索是客户端搜索，不会进服务端
                    // strictSearch: true,         //设置为 true启用 全匹配搜索，否则为模糊搜索
                    // showColumns: true,          //是否显示所有的列
                    // showRefresh: true,          //是否显示刷新按钮
                    // minimumCountColumns: 2,     //最少允许的列数
                    clickToSelect: true,        //是否启用点击选中行
                    // searchOnEnterKey: true,     //设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
                    columns: this.columns,      //列数组
                    maintainSelected:this.maintainSelected,
                    // height: this.height,
                    // icons: {
                    //     refresh: 'glyphicon-repeat',
                    //     toggle: 'glyphicon-list-alt',
                    //     columns: 'glyphicon-list'
                    // },
                    // iconSize: 'outline',
                    pagination: this.pagination           //是否显示分页条

                });

            this.btInstance.on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {

                if(currentBSTable.rowId!=null && currentBSTable.pagination) {
                    var datas = $.isArray(rows) ? rows : [rows]; // 点击时获取选中的行或取消选中的行
                    currentBSTable.examine(e.type, datas);
                }
            });

            this.btInstance.on('load-success.bs.table', function () {//数据加载成功后

                if(currentBSTable.rowId!=null && currentBSTable.pagination) {
                    var checkArr = currentBSTable.getCurrentPageSelections();
                    if (checkArr != null && checkArr.length > 0) {
                        for(var i=0;i<checkArr.length;i++) {
                            currentBSTable.addOverAllArr(checkArr[i]);
                        }
                    }
                }
            });


            return this;
        },
        /**
         * 向后台传递的自定义参数
         * @param param
         * @returns {BSTable}
         */
        setQueryParams: function (param) {
            this.queryParams = param;
            return this;
        },

        /**
         * 增加自定义参数
         * @param param
         */
        setExtendQueryParams:function(param){
            $.extend(this.queryParams, param);
        },

        /**
         * 设置分页方式：server 或者 client
         * @param type
         * @returns {BSTable}
         */
        setPaginationType: function (type) {
            this.paginationType = type;
            return this;
        },

        // /**
        //  * 设置ajax post请求时候附带的参数
        //  */
        // set: function (key, value) {
        //     if (typeof key == "object") {
        //         for (var i in key) {
        //             if (typeof i == "function")
        //                 continue;
        //             this.data[i] = key[i];
        //         }
        //     } else {
        //         this.data[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
        //     }
        //     return this;
        // },

        // /**
        //  * 设置ajax post请求时候附带的参数
        //  */
        // setData: function (data) {
        //     this.data = data;
        //     return this;
        // },

        /**
         * 清空搜索框和所选并刷新列表
         * @returns {BSTable}
         */
        clear: function () {
            this.overAllArr = new Array();
            this.overAllIds = new Array();
            if(this.keyword!=""){
                $('#'+this.keyword).val('');
            }
            // if(this.search && this.paginationType=='client'){
            //     if(this.keyword!=""){
            //         this.btInstance.bootstrapTable('resetSearch', $('#'+this.keyword).val());
            //     }else{
            //         this.btInstance.bootstrapTable('resetSearch', '');
            //     }
            // }else {
            //     this.refresh(true);
            // }

            if(this.search && this.paginationType=='client'){
                this.btInstance.bootstrapTable('resetSearch', $('#'+this.keyword).val());
            }
            this.refresh(true);

            return this;
        },

        /**
         * 刷新 bootstrap 表格
         * @param withParms
         * @returns {BSTable}
         */
        refresh: function (withParms) {

            this.btInstance.bootstrapTable('selectPage',1);//跳转到第一页，防止其他页查询第一次不显示数据的问题。

            if (withParms) {
                if(this.keyword!='') {
                    this.queryParams['keyword'] = $('#' + this.keyword).val();
                }
                this.btInstance.bootstrapTable('refresh', this.queryParams);
            } else {
                this.btInstance.bootstrapTable('refresh');
            }

            return this;
        },

        /**
         * 请求方式，get 或是 post
         * @param method
         * @returns {BSTable}
         */
        setMethod: function (method) {
            this.method = method;
            return this;
        },

        /**
         * 是否分页
         * @param pagination
         * @returns {BSTable}
         */
        setPagination:function (pagination) {
            this.pagination = pagination;
            return this;
        },

        /**
         * 分页方式为client时，是否在切页面和搜索时记住选项
         * @param maintainSelected
         * @returns {BSTable}
         */
        setMaintainSelected:function(maintainSelected){
            this.maintainSelected = maintainSelected;
            return this;
        },

        /**
         *
         * row的唯一标识id
         * @param rowId
         * @returns {BSTable}
         */
        setRowId:function (rowId) {
            this.rowId = rowId;
            return this;
        },

        /**
         * 是否显示搜索按钮
         * @param search
         * @returns {BSTable}
         */
        setSearch:function(search){
            this.search = search;
            return this;
        },

        /**
         * 设置清空按钮
         * @param clearBtn
         * @returns {BSTable}
         */
        setClearBtn:function(clearBtn){
            this.clearBtn = clearBtn;
            $('#'+this.clearBtn).click(function(){
                currentBSTable.clear();
            });
            return this;
        },

        /**
         * 隐藏内置搜索框
         * @returns {BSTable}
         */
        hideBuiltInSearchBtn:function(){
            $(".fixed-table-toolbar .search").css("display","none");
            return this;
        },

        /**
         * 设置查询按钮
         * @param searchBtn
         * @param keyword
         * @returns {BSTable}
         */
        setSearchBtn:function(searchBtn,keyword){
            this.searchBtn = searchBtn;
            this.keyword = keyword;
            $('#'+this.searchBtn).click(function(){
                if(currentBSTable.keyword!="") {
                    if ($('#' + currentBSTable.keyword).val() == '') {
                        currentBSTable.overAllArr = new Array();
                        currentBSTable.overAllIds = new Array();
                    }
                }
                if(currentBSTable.search && currentBSTable.paginationType=='client'){
                    if(currentBSTable.keyword!=""){
                        currentBSTable.btInstance.bootstrapTable('resetSearch', $('#'+currentBSTable.keyword).val());
                    }else{
                        currentBSTable.btInstance.bootstrapTable('resetSearch', '');
                    }
                }else {
                    currentBSTable.refresh(true);
                }
            });
            return this;
        },

        /**
         *
         * 处理选择和取消事件
         * @param type
         * @param datas
         */
        examine:function (type,datas) {
            if(this.rowId!=null && this.pagination){
                if(type.indexOf('uncheck')==-1){
                    $.each(datas,function(i,v){
                        // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
                        if(currentBSTable.overAllIds.indexOf(v[currentBSTable.rowId]) == -1){
                            currentBSTable.overAllIds.push(v[currentBSTable.rowId]);
                            currentBSTable.overAllArr.push(v);
                        }


                    });
                }else{
                    $.each(datas,function(i,v){
                        var j = currentBSTable.overAllIds.indexOf(v[currentBSTable.rowId]);
                        currentBSTable.overAllIds.splice(j,1);    //删除取消选中行
                        currentBSTable.overAllArr.splice(j,1);
                    });
                }
            }
        },

        /**
         * 获取选择的row
         * @returns {*}
         */
        getSelections:function(){
            var arr = new Array();
            if(this.rowId==null || !this.pagination){
                var checkBoxs = this.btInstance.bootstrapTable('getSelections');
                if(checkBoxs!=null&&checkBoxs.length>0){
                    for (var i = 0; i < checkBoxs.length; i++) {
                        arr.push(checkBoxs[i]);
                    }
                }
            }else{
                return this.overAllArr;
            }

            return arr;
        },

        /**
         * 获取当前页选择的row
         * @returns {any[]}
         */
        getCurrentPageSelections:function(){
            var arr = new Array();

            var checkBoxs = this.btInstance.bootstrapTable('getSelections');
            if(checkBoxs!=null&&checkBoxs.length>0){
                for (var i = 0; i < checkBoxs.length; i++) {
                    arr.push(checkBoxs[i]);
                }
            }

            return arr;
        },

        /**
         * 加入全局数组
         * @param row
         */
        addOverAllArr:function(row){
            if(this.overAllIds.indexOf(row[this.rowId]) == -1 ){
                this.overAllIds.push(row[this.rowId]);
                this.overAllArr.push(row);
            }
        },

        /**
         * 重置表高度
         * @param height
         * @returns {BSTable}
         */
        setResetViewHeight:function(height){
            this.btInstance.bootstrapTable('resetView', {
                height: height
            });

            return this;
        }
    };

    var currentBSTable = new BSTable(bstableId,columns,url);

    return currentBSTable;
}

/**
 * 示范代码
 * server端分页get请求
 * 例子见文件testServerDefaultBootstrap.jsp
 * url:/test/testServerDefaultBootstrap.do
 */
// var BSTable = defaultBootstrap("bstableId",[{
//         checkbox: true,
//         field: '#',
//         formatter: function (value,row,index) {
//             return {
//                 checked : true//设置选中
//             };
//         }
//     },{
//         field: "rowId",
//         visible: false
//     }, {
//         field: '_operate', // 数据字段
//         title: '操作', // 页面字段显示
//         formatter: function (value, row, index) {
//             var viewBtn = '<a href="javascript:showInfo(\'' + row.rowId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-edit"></i></a>';
//             return viewBtn;
//         }
//     }],'url');
// var queryParams = {};//向后台传递的自定义参数
// queryParams['queryParams'] =  'queryParams';
// BSTable.setQueryParams(queryParams).setRowId("rowId").setClearBtn("clearBrn").setSearchBtn("searchBrn","keyword").init();//初始化表格
// BSTable.getSelections();//获取选中框



/**
 * 示范代码
 * client端分页get请求
 * 例子见文件testClientDefaultBootstrap.jsp
 * url:/test/testClientDefaultBootstrap.do
 */
// var BSTable = defaultBootstrap("bstableId",[{
//         checkbox: true,
//         field: '#',
//         formatter: function (value,row,index) {
//             return {
//                 checked : true//设置选中
//             };
//         }
//     },{
//         field: "rowId",
//         visible: false
//     }, {
//         field: '_operate', // 数据字段
//         title: '操作', // 页面字段显示
//         formatter: function (value, row, index) {
//             var viewBtn = '<a href="javascript:showInfo(\'' + row.rowId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="查看"><i class="la la-edit"></i></a>';
//             return viewBtn;
//         }
//     }],'url');
// var queryParams = {};//向后台传递的自定义参数
// queryParams['queryParams'] =  'queryParams';
// BSTable.setQueryParams(queryParams).setPaginationType("client").setSearch(true).setClearBtn("clearBrn").setSearchBtn("searchBrn","keyword").setMaintainSelected(true).init();//初始化表格
// BSTable.hideBuiltInSearchBtn();//隐藏内置搜索框
// BSTable.getSelections();//获取选中框